package org.openntf.domino.xsp.xots;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import lotus.domino.Session;

import org.eclipse.core.runtime.Platform;
import org.openntf.domino.session.ISessionFactory;
import org.openntf.domino.thread.AbstractWrapped.WrappedCallable;
import org.openntf.domino.thread.AbstractWrapped.WrappedRunnable;
import org.openntf.domino.thread.DominoExecutor;
import org.openntf.domino.types.Null;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xots.Tasklet;
import org.openntf.domino.xots.Tasklet.Context;
import org.openntf.domino.xsp.ODAPlatform;
import org.openntf.domino.xsp.session.InvalidSessionFactory;
import org.openntf.domino.xsp.session.XPageCurrentSessionFactory;
import org.openntf.domino.xsp.session.XPageNamedSessionFactory;
import org.openntf.domino.xsp.session.XPageSignerSessionFactory;
import org.openntf.domino.xsp.thread.ModuleLoader;
import org.osgi.framework.Bundle;

import com.ibm.commons.util.ThreadLock;
import com.ibm.commons.util.ThreadLockManager;
import com.ibm.designer.runtime.domino.adapter.ComponentModule;
import com.ibm.domino.xsp.module.nsf.ModuleClassLoader;
import com.ibm.domino.xsp.module.nsf.NSFComponentModule;
import com.ibm.domino.xsp.module.nsf.NotesContext;

public class XotsDominoExecutor extends DominoExecutor {
	private static final Logger log_ = Logger.getLogger(XotsDominoExecutor.class.getName());

	/**
	 * 
	 * @author Roland Praml, FOCONIS AG
	 * 
	 * @param <V>
	 */
	protected class XotsWrappedCallable<V> extends WrappedCallable<V> {

		private NSFComponentModule module_;

		public XotsWrappedCallable(final NSFComponentModule module, final Callable<V> wrappedObject) {
			setWrappedTask(wrappedObject);
			module_ = module;
		}

		@SuppressWarnings("unchecked")
		@Override
		public V call() throws Exception {
			return (V) callOrRun(module_, bubbleException, sessionFactory, getWrappedTask());
		}
	}

	/**
	 * 
	 * @author Roland Praml, FOCONIS AG
	 * 
	 */
	protected static class XotsWrappedRunnable extends WrappedRunnable {

		private NSFComponentModule module_;

		public XotsWrappedRunnable(final NSFComponentModule module, final Runnable wrappedObject) {
			setWrappedTask(wrappedObject);
			module_ = module;
		}

		@Override
		public void run() {
			try {
				callOrRun(module_, bubbleException, sessionFactory, getWrappedTask());
			} catch (Exception e) {
				log_.log(Level.SEVERE, "Could not execute " + module_.getModuleName() + "/" + getWrappedTask().getClass(), e);
			}
		}
	}

	/**
	 * This class is used for periodic tasks.
	 * 
	 * @author Roland Praml, FOCONIS AG
	 * 
	 * @param <V>
	 */
	protected static class XotsModuleTasklet extends WrappedCallable<Object> {
		final String moduleName;
		final String className;
		final Object[] args;
		private List<Observer> observers;

		public XotsModuleTasklet(final String moduleName, final String className, final Object[] args) {
			super();
			this.moduleName = moduleName;
			this.className = className;
			this.args = args;
			setWrappedTask(null); // This WrappedCallable has no tasklet if it does NOT run
			try {
				performAction(false); // check if the module does exist
			} catch (ServletException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Object call() throws ServletException {
			return performAction(true);
		}

		/*
		 * This method tries either if the Tasklet really exist at construction time and performs also the execute action
		 */
		private Object performAction(final boolean runReally) throws ServletException {

			Class<?> clazz = null;
			NSFComponentModule module = null;
			NotesContext ctx = null;

			if (moduleName.startsWith("bundle:")) {
				// -- Load the class from bundle
				String bundleName = moduleName.substring(7);
				final Bundle bundle = Platform.getBundle(bundleName);
				if (bundle == null) {
					throw new IllegalArgumentException("Could not find bundle " + bundleName);
				}

				try {
					clazz = bundle.loadClass(className);
				} catch (ClassNotFoundException e) {
					throw new IllegalArgumentException("Could not load class " + className + " in bundle " + bundleName, e);
				}

				return runXotsClass(ctx, clazz.getClassLoader(), clazz, runReally);

			} else {
				if (NotesContext.getCurrentUnchecked() != null) {
					if (runReally) {
						throw new IllegalStateException("There is an open NotesContext. Cannot run " + className + " in " + moduleName);
					} else {
						return null;	// unfortunately, if there is an open NotesContext, we cannot perform the check!
					}
				}
				// -- Load the class from module
				module = ModuleLoader.loadModule(moduleName, true);
				if (module == null) {
					throw new IllegalArgumentException("Could not find bundle " + moduleName);
				}
				ctx = new NotesContext(module);
				NotesContext.initThread(ctx);
				// CHECKME which username should we use? Server
				ctx.initRequest(new FakeHttpRequest(Factory.getLocalServerName()));

				try {

					try {
						clazz = module.getModuleClassLoader().loadClass(className);
					} catch (Exception e) {
						throw new IllegalArgumentException("Could not load class " + className + " in module " + moduleName, e);
					}

					return runXotsClass(ctx, module.getModuleClassLoader(), clazz, runReally);
				} finally {
					NotesContext.termThread();
					ctx = null;
				}
			}
		}

		/**
		 * Here we do the whole setup to run the given XOTS Class
		 * 
		 * @param ctx
		 * @param ctxCl
		 * @param clazz
		 * @param runReally
		 * @return
		 */
		private Object runXotsClass(final NotesContext ctx, final ClassLoader ctxCl, final Class<?> clazz, final boolean runReally) {
			Factory.initThread();
			Factory.setSessionFactory(new XPageCurrentSessionFactory(), SessionType.CURRENT);
			Factory.setSessionFactory(new XPageSignerSessionFactory(false), SessionType.SIGNER);
			Factory.setSessionFactory(new XPageSignerSessionFactory(true), SessionType.SIGNER_FULL_ACCESS);

			if (ODAPlatform.isAppFlagSet("BUBBLEEXCEPTIONS")) {
				DominoUtils.setBubbleExceptions(true);
			}

			Factory.setClassLoader(ctxCl);

			try {
				Tasklet annot = clazz.getAnnotation(Tasklet.class);
				if (annot == null || !annot.isPublic()) {
					throw new IllegalStateException(clazz.getName() + " does not annotate @Tasklet(isPublic=true). Cannot run.");
				}

				Class<?> ctorClasses[] = new Class<?>[args.length];
				Object ctorArgs[] = new Object[args.length];
				for (int i = 0; i < ctorClasses.length; i++) {
					Object arg;
					ctorArgs[i] = arg = args[i];
					ctorClasses[i] = arg == null ? Null.class : arg.getClass();
				}

				Constructor<?> cTor = null;
				try {
					cTor = clazz.getConstructor(ctorClasses);
				} catch (NoSuchMethodException nsme1) {
					try {
						cTor = clazz.getConstructor(new Class<?>[] { Object[].class });
						ctorArgs = new Object[] { ctorArgs };
					} catch (NoSuchMethodException nsme2) {

					}
				}
				if (cTor == null) {
					throw new IllegalStateException(clazz.getName() + " has no constructor for Arguments: " + ctorArgs);
				}

				Thread thread = Thread.currentThread();
				ClassLoader oldCl = thread.getContextClassLoader();
				try {
					if (ctxCl != null) {
						thread.setContextClassLoader(ctxCl);
					}
					try {
						Object tasklet = cTor.newInstance(ctorArgs);

						setWrappedTask(tasklet); // reads annotations and sets wrapped task
						Factory.setSessionFactory(sessionFactory, SessionType.CURRENT);
						initModule(ctx, ctxCl, tasklet);

						if (!(tasklet instanceof Callable) || !(tasklet instanceof Runnable)) {
							throw new IllegalStateException("Could not run " + clazz.getName()
									+ ", as this is no runnable or callable class");
						}
						if (runReally) {
							if (tasklet instanceof Observable && observers != null) {
								for (Observer o : observers)
									((Observable) tasklet).addObserver(o);
							}
							return callOrRun(ctx.getRunningModule(), bubbleException, sessionFactory, tasklet);
						} else {
							return null;
						}

					} catch (Exception ex) {
						log_.log(Level.SEVERE, "Could not run " + clazz.getName(), ex);
						throw new RuntimeException("Error while executing the Tasklet " + clazz.getName(), ex);
					}
				} finally {
					setWrappedTask(null);
					thread.setContextClassLoader(oldCl);
				}
			} finally {
				Factory.termThread();
			}
		}

		@Override
		public void addObserver(final Observer o) {
			if (observers == null) {
				observers = new ArrayList<Observer>();
			}
			observers.add(o);
		}

	}

	/**
	 * Common code for the wrappers
	 * 
	 * @param module
	 * @param bubbleException
	 * @param sessionFactory
	 * @param callable
	 * @param runnable
	 * @return
	 */
	private static Object callOrRun(final NSFComponentModule module, final boolean bubbleException, final ISessionFactory sessionFactory,
			final Object wrappedTask) throws Exception {
		ClassLoader mcl;
		NSFComponentModule codeModule = null;
		if (module == null) {
			// running in Bundle mode...
			mcl = wrappedTask.getClass().getClassLoader();
		} else {
			if (module.isDestroyed()) {
				throw new IllegalArgumentException("Module was destroyed in the meantime. Cannot run");
			}
			module.updateLastModuleAccess();
			codeModule = module.getTemplateModule() == null ? module : module.getTemplateModule();
			codeModule.updateLastModuleAccess();
			mcl = codeModule.getModuleClassLoader();
		}

		final NotesContext ctx = new NotesContext(module);
		NotesContext.initThread(ctx);

		try {
			DominoUtils.setBubbleExceptions(bubbleException);
			Factory.initThread();
			try {

				if (sessionFactory != null) {
					Factory.setSessionFactory(sessionFactory, SessionType.CURRENT);
					org.openntf.domino.Session current = Factory.getSession(SessionType.CURRENT);

					Factory.getNamedSessionFactory(true).createSession(current.getEffectiveUserName());
					Factory.setSessionFactory(sessionFactory, SessionType.CURRENT_FULL_ACCESS);
				}

				// RPr: In my opinion, This is the proper way how to run runnables in a different thread
				ThreadLock readLock = null;
				if (codeModule != null) {
					if (ODAPlatform.isAppFlagSet("LOCKXOTS", codeModule.getNotesApplication())) {
						readLock = getLockManager(codeModule).getReadLock();
					}
				}

				try {
					if (readLock != null)
						readLock.acquire(); // we want to read data from the module, so lock it!

					// set up the classloader
					ClassLoader oldCl = switchClassLoader(mcl);
					try {
						initModule(ctx, mcl, wrappedTask);
						if (wrappedTask instanceof Callable) {
							return ((Callable<?>) wrappedTask).call();
						} else {
							((Runnable) wrappedTask).run();
							return null;
						}
					} finally {
						switchClassLoader(oldCl);
					}
				} finally {
					if (readLock != null)
						readLock.release();
				}
			} catch (Exception e) {
				DominoUtils.handleException(e);
				return null;
			} finally {
				Factory.termThread();
			}
		} finally {
			NotesContext.termThread();
		}
	}

	public XotsDominoExecutor(final int corePoolSize) {
		super(corePoolSize);
	}

	/**
	 * We lock the module, so that it cannot get refreshed. (hope this is a good idea)
	 * 
	 */
	private static Field lockManager_field = AccessController.doPrivileged(new PrivilegedAction<Field>() {
		@Override
		public Field run() {
			Field field;
			try {
				field = ComponentModule.class.getDeclaredField("lockManager");
				field.setAccessible(true);
				return field;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	});

	/**
	 * Returns the lock manager for the given ComponentModule
	 * 
	 * @param module
	 * @return
	 */
	static ThreadLockManager getLockManager(final ComponentModule module) {
		try {
			if (lockManager_field != null) {
				return (ThreadLockManager) lockManager_field.get(module);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Helper for WrappedCallable/WrappedRunnable
	 * 
	 * @param ctx
	 * @param mcl
	 * @param wrapper
	 * @throws ServletException
	 */

	private static void initModule(final NotesContext ctx, final ClassLoader mcl, final Object wrappedObject) throws ServletException {
		// next initialize the context with a FakeHttpRequest. This is neccessary so that internal things
		// like session-creation and so on work properly

		// RPr:
		// You may ask what I do here: ReLoading the module again from the MCL triggers signature checking.

		if (mcl instanceof ModuleClassLoader) {
			URLClassLoader dcl = (URLClassLoader) ((ModuleClassLoader) mcl).getDynamicClassLoader();
			String className = wrappedObject.getClass().getName();
			String str = className.replace('.', '/') + ".class";
			URL url = dcl.findResource(str);
			if (url != null && url.getProtocol().startsWith("xspnsf")) {
				// Set up the "TopLevelXPageSigner == Signer of the runnable
				// As soon as we are in a xspnsf, we do not have a SessionFactory!

				System.out.println("Loading Class from NSF:" + url);
				ctx.setSignerSessionRights("WEB-INF/classes/" + str);

				// RPr: There is a bug: you can decide if you want to use "sessionAsSigner" or "sessionAsSignerFullAccess"
				// But you cannot use both simultaneously!
				Session signerSession = ctx.getSessionAsSigner(true);
				if (signerSession != null) {
					Factory.setSessionFactory(new XPageSignerSessionFactory(false), SessionType.SIGNER);
					Factory.setSessionFactory(new XPageSignerSessionFactory(true), SessionType.SIGNER_FULL_ACCESS);
				} else {
					// do not setup signer sessions if it is not properly signed!
					Factory.setSessionFactory(new InvalidSessionFactory(), SessionType.SIGNER);
					Factory.setSessionFactory(new InvalidSessionFactory(), SessionType.SIGNER_FULL_ACCESS);
				}
			} else {
				// The code is not part from an NSF, so it resides on the server
				Factory.setSessionFactory(new XPageNamedSessionFactory(Factory.getLocalServerName(), false), SessionType.SIGNER);
				Factory.setSessionFactory(new XPageNamedSessionFactory(Factory.getLocalServerName(), true), SessionType.SIGNER_FULL_ACCESS);
			}
		}
		Factory.setClassLoader(mcl);
	}

	/**
	 * Changes the
	 * 
	 * @param codeModule
	 * @return
	 */
	private static ClassLoader switchClassLoader(final ClassLoader newClassLoader) {
		return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {

			@Override
			public ClassLoader run() {
				Thread thread = Thread.currentThread();
				ClassLoader oldCl = thread.getContextClassLoader();
				thread.setContextClassLoader(newClassLoader);
				return oldCl;
			}
		});

	}

	// ------------------------------

	protected NSFComponentModule getCurrentModule() {
		NotesContext ctx = NotesContext.getCurrentUnchecked();
		if (ctx == null)
			return null;
		return ctx.getModule();
	}

	@Override
	protected <V> WrappedCallable<V> wrap(final Callable<V> inner) {
		if (inner instanceof WrappedCallable)
			return (WrappedCallable<V>) inner;

		NSFComponentModule module = getCurrentModule();
		Tasklet annot = inner.getClass().getAnnotation(Tasklet.class);
		if (annot == null || annot.context() == Context.DEFAULT) {
			if (module == null) {
				return super.wrap(inner);
			} else {
				return new XotsWrappedCallable<V>(module, inner);
			}
		} else if (annot.context() == Context.PLUGIN) {
			return super.wrap(inner);
		} else {
			if (module == null) {
				throw new NullPointerException("No module is running");
			}
			return new XotsWrappedCallable<V>(module, inner);
		}
	}

	@Override
	protected WrappedRunnable wrap(final Runnable inner) {
		if (inner instanceof WrappedRunnable)
			return (WrappedRunnable) inner;

		NSFComponentModule module = getCurrentModule();
		Tasklet annot = inner.getClass().getAnnotation(Tasklet.class);
		if (annot == null || annot.context() == Context.DEFAULT) {
			if (module == null) {
				return super.wrap(inner);
			} else {
				return new XotsWrappedRunnable(module, inner);
			}
		} else if (annot.context() == Context.PLUGIN) {
			return super.wrap(inner);
		} else {
			if (module == null) {
				throw new NullPointerException("No module is running");
			}
			return new XotsWrappedRunnable(module, inner);
		}
	}

	@Override
	protected WrappedCallable<?> wrap(final String moduleName, final String className, final Object... ctorArgs) {
		return new XotsModuleTasklet(moduleName, className, ctorArgs);
	}
	//
	//	@Override
	//	protected <T> RunnableFuture<T> newTaskFor(final Callable<T> callable) {
	//		NotesContext ctx = NotesContext.getCurrentUnchecked();
	//		NSFComponentModule module = ctx == null ? null : ctx.getModule();
	//
	//		if (module == null) {
	//			return super.newTaskFor(callable);
	//		} else {
	//			return new XotsDominoFutureTask(module, callable);
	//		}
	//
	//	}
	//
	//	@Override
	//	protected <T> RunnableFuture<T> newTaskFor(final Runnable runnable, final T result) {
	//		NotesContext ctx = NotesContext.getCurrentUnchecked();
	//		NSFComponentModule module = ctx == null ? null : ctx.getModule();
	//
	//		if (module == null) {
	//			return super.newTaskFor(runnable, result);
	//		} else {
	//			return new XotsDominoFutureTask(module, runnable, result);
	//		}
	//	}

}

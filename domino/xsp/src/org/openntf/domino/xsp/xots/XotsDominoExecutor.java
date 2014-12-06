package org.openntf.domino.xsp.xots;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
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
import org.openntf.domino.xsp.helpers.InvalidSessionFactory;
import org.openntf.domino.xsp.helpers.ModuleLoader;
import org.openntf.domino.xsp.session.XPageCurrentSessionFactory;
import org.openntf.domino.xsp.session.XPageNamedSessionFactory;
import org.openntf.domino.xsp.session.XPageSignerSessionFactory;
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
			init(wrappedObject);
			module_ = module;
		}

		@Override
		public V call() throws Exception {
			return callOrRun(module_, bubbleException, sessionFactory, getWrappedTask(), null);
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
			init(wrappedObject);
			module_ = module;
		}

		@Override
		public void run() {

			try {
				callOrRun(module_, bubbleException, sessionFactory, null, getWrappedTask());
			} catch (Exception e) {
				log_.log(Level.SEVERE, "Could not execute " + module_.getModuleName() + "/" + getWrappedTask().getClass(), e);
			}
		}
	}

	protected static class XotsTaskletDefinition<V> extends WrappedCallable<V> {
		final String moduleName;
		final String className;
		final Object[] args;

		public XotsTaskletDefinition(final String moduleName, final String className, final Object[] args) {
			super();
			this.moduleName = moduleName;
			this.className = className;
			this.args = args;
		}

		@Override
		public V call() throws ServletException {
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

				return runXotsClass(clazz, clazz.getClassLoader());
				//runXotsClass(clazz, cLoader, args);
				//System.out.println("TODO: Should run " + clazz);
				//return null; // exec tasklet here!
			} else {
				// -- Load the class from module
				module = ModuleLoader.loadModule(moduleName, true);
				ctx = new NotesContext(module);
				NotesContext.initThread(ctx);
				// CHECKME which username should we use? Server
				ctx.initRequest(new FakeHttpRequest(Factory.getLocalServerName()));

				try {
					if (module == null) {
						throw new IllegalArgumentException("Could not find bundle " + moduleName);
					}
					try {
						clazz = module.getModuleClassLoader().loadClass(className);
					} catch (Exception e) {
						throw new IllegalArgumentException("Could not load class " + className + " in module " + moduleName, e);
					}

					return runXotsClass(clazz, module.getModuleClassLoader());
				} finally {
					NotesContext.termThread();
					ctx = null;
				}
			}
		}

		private V runXotsClass(final Class<?> clazz, final ClassLoader ctxCl) {
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
						if (Callable.class.isAssignableFrom(clazz)) {
							Callable<?> callable = (Callable<?>) cTor.newInstance(ctorArgs);
							return (V) callable.call();
						} else if (Runnable.class.isAssignableFrom(clazz)) {
							Runnable runnable = (Runnable) cTor.newInstance(ctorArgs);
							runnable.run();
							return null;
						} else {
							throw new IllegalStateException("Could not run " + clazz.getName()
									+ ", as this is no runnable or callable class");
						}
					} catch (Exception ex) {
						log_.log(Level.SEVERE, "Could not run " + clazz.getName(), ex);
						throw new RuntimeException("Error while executing the Tasklet " + clazz.getName(), ex);
					}
				} finally {
					thread.setContextClassLoader(oldCl);
				}
			} finally {
				Factory.termThread();
			}
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
	private static <V> V callOrRun(final NSFComponentModule module, final boolean bubbleException, final ISessionFactory sessionFactory,
			final Callable<V> callable, final Runnable runnable) throws Exception {
		ClassLoader mcl;
		NSFComponentModule codeModule = null;
		if (module == null) {
			// running in Bundle mode...
			mcl = callable != null ? callable.getClass().getClassLoader() : runnable.getClass().getClassLoader();
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
				if (codeModule == null) {
					ClassLoader oldCl = switchClassLoader(mcl);
					try {
						if (callable != null) {
							initModule(ctx, mcl, callable);
							return callable.call();
						} else {
							initModule(ctx, mcl, runnable);
							runnable.run();
							return null;
						}
					} finally {
						switchClassLoader(oldCl);
					}
				} else {
					ThreadLock readLock = getLockManager(codeModule).getReadLock();
					try {
						readLock.acquire(); // we want to read data from the module, so lock it!

						// set up the classloader
						ClassLoader oldCl = switchClassLoader(mcl);
						try {
							if (callable != null) {
								initModule(ctx, mcl, callable);
								return callable.call();
							} else {
								initModule(ctx, mcl, runnable);
								runnable.run();
								return null;
							}
						} finally {
							switchClassLoader(oldCl);
						}
					} finally {
						readLock.release();
					}
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

				// TODO: RPr: There is a bug: you can decide if you want to use "sessionAsSigner" or "sessionAsSignerFullAccess"
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
		return new XotsTaskletDefinition(moduleName, className, ctorArgs);
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

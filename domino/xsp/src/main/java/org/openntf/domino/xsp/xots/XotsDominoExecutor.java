package org.openntf.domino.xsp.xots;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import lotus.domino.Session;

import org.eclipse.core.runtime.Platform;
import org.openntf.domino.session.NativeSessionFactory;
import org.openntf.domino.thread.DominoExecutor;
import org.openntf.domino.thread.IWrappedCallable;
import org.openntf.domino.thread.IWrappedRunnable;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xots.AbstractXotsCallable;
import org.openntf.domino.xots.AbstractXotsRunnable;
import org.openntf.domino.xots.Tasklet;
import org.openntf.domino.xots.Tasklet.Context;
import org.openntf.domino.xots.XotsContext;
import org.openntf.domino.xsp.ODAPlatform;
import org.openntf.domino.xsp.session.InvalidSessionFactory;
import org.openntf.domino.xsp.session.XPageNamedSessionFactory;
import org.openntf.domino.xsp.session.XPageSignerSessionFactory;
import org.osgi.framework.Bundle;

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
	 */
	public static class XotsWrappedCallable<V> extends XotsWrappedTask implements IWrappedCallable<V> {

		private NSFComponentModule module_;

		public XotsWrappedCallable(final NSFComponentModule module, final Callable<V> wrappedObject) {
			setWrappedTask(wrappedObject);
			module_ = module;
		}

		@SuppressWarnings("unchecked")
		@Override
		public V call() throws Exception {
			return (V) callOrRun(module_);
		}
	}

	/**
	 * 
	 * @author Roland Praml, FOCONIS AG
	 * 
	 */
	public static class XotsWrappedRunnable extends XotsWrappedTask implements IWrappedRunnable {

		private NSFComponentModule module_;

		public XotsWrappedRunnable(final NSFComponentModule module, final Runnable wrappedObject) {
			setWrappedTask(wrappedObject);
			module_ = module;
		}

		@Override
		public void run() {
			try {
				callOrRun(module_);
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
	 */
	protected static class XotsBundleTasklet extends XotsWrappedTask implements IWrappedCallable<Object> {

		private String description;

		public XotsBundleTasklet(final String bundleName, final String className, final Object[] args) {
			super();
			final Bundle bundle = Platform.getBundle(bundleName);
			Class<?> clazz = null;

			if (bundle == null)
				throw new IllegalArgumentException("Could not find bundle " + bundleName);

			try {
				clazz = bundle.loadClass(className);
			} catch (ClassNotFoundException e) {
				throw new IllegalArgumentException("Could not load class " + className + " in bundle " + bundleName, e);
			}

			description = "bundle:" + bundleName + ":" + className;
			Constructor<?> cTor = findConstructor(clazz, args);

			try {
				setWrappedTask(cTor.newInstance(args));
			} catch (Exception e) {
				DominoUtils.handleException(e);
			} // This WrappedCallable has no tasklet if it does NOT run
		}

		@Override
		public Object call() throws Exception {
			return callOrRun(null);
		}

		@Override
		public String getDescription() {
			return description;
		}
	}

	protected static class XotsModuleTasklet extends XotsWrappedTask implements IWrappedCallable<Object> {
		private String moduleName;
		private String className;
		private Object[] args;
		private ArrayList<Observer> observers;

		public XotsModuleTasklet(final String moduleName, final String className, final Object... args) {
			super();
			this.moduleName = moduleName;
			this.className = className;
			this.args = args;
			if (NotesContext.getCurrentUnchecked() == null) {
				// perform a check if there is NO open context.
				// if there is an open context, we cannot switch the module.
				// maybe we can do this in a separate thread
				NSFComponentModule module = loadModule();
				NotesContext ctx = new NotesContext(module);
				NotesContext.initThread(ctx);
				try {
					Factory.initThread(ODAPlatform.getAppThreadConfig(module.getNotesApplication()));
					try {
						ClassLoader mcl = module.getModuleClassLoader();
						ClassLoader oldCl = switchClassLoader(mcl);
						Factory.setClassLoader(mcl);
						try {
							Class<?> clazz = mcl.loadClass(className);
							findConstructor(clazz, args); // try if we can find the constructor
						} catch (ClassNotFoundException e) {
							throw new IllegalArgumentException("Could not load class " + className + " in module " + moduleName, e);
						} finally {
							switchClassLoader(oldCl);
						}
					} finally {
						Factory.termThread();
					}
				} finally {
					NotesContext.termThread();
				}
			}
		}

		/**
		 * loads the module
		 * 
		 * @return
		 */
		protected NSFComponentModule loadModule() {
			try {
				NSFComponentModule module = ModuleLoader.loadModule(moduleName, true);
				if (module == null)
					throw new IllegalArgumentException("Could not find bundle " + moduleName);
				return module;
			} catch (ServletException e) {
				throw new RuntimeException(e);
			}

		}

		@Override
		public void addObserver(final Observer o) {
			if (observers == null) {
				observers = new ArrayList<Observer>();
			}
			observers.add(o);
		}

		@Override
		public Object call() throws Exception {
			NSFComponentModule module = loadModule();
			NotesContext ctx = new NotesContext(module);
			NotesContext.initThread(ctx);
			try {
				// CHECKME which username should we use? Server
				ctx.initRequest(new FakeHttpRequest(Factory.getLocalServerName()));
				//				ThreadLock readLock = null;
				//
				//				if (ODAPlatform.isAppFlagSet("LOCKXOTS", codeModule.getNotesApplication())) {
				//					readLock = getLockManager(codeModule).getReadLock();
				//					readLock.acquire();
				//				}

				//				try {
				Factory.initThread(ODAPlatform.getAppThreadConfig(module.getNotesApplication()));
				try {
					ClassLoader mcl = module.getModuleClassLoader();
					ClassLoader oldCl = switchClassLoader(mcl);
					Factory.setClassLoader(mcl);
					Factory.setSessionFactory(new NativeSessionFactory(moduleName), SessionType.CURRENT);
					DominoUtils.setBubbleExceptions(true);
					try {
						// Construct & set up
						Class<?> clazz = mcl.loadClass(className);
						Constructor<?> cTor = findConstructor(clazz, args);

						setWrappedTask(cTor.newInstance(args));

						Object wrappedTask = getWrappedTask();
						if (wrappedTask instanceof Observable && observers != null) {
							for (Observer o : observers) {
								((Observable) wrappedTask).addObserver(o);
							}
						}
						return invokeTasklet(ctx, module);
					} finally {
						switchClassLoader(oldCl);
					}
				} finally {
					setWrappedTask(null);
					Factory.termThread();
				}
				//				} finally {
				//					if (readLock != null)
				//						readLock.release();
				//				}
			} finally {
				NotesContext.termThread();
			}
		}

		@Override
		public String getDescription() {
			return moduleName + ":" + className;
		}

	}

	public XotsDominoExecutor(final int corePoolSize) {
		super(corePoolSize, "Xots");
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

	static void initModule(final NotesContext ctx, final ClassLoader mcl, final Object wrappedObject) throws ServletException {
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

	// ------------------------------

	protected NSFComponentModule getCurrentModule() {
		NotesContext ctx = NotesContext.getCurrentUnchecked();
		if (ctx == null)
			return null;
		return ctx.getModule();
	}

	@Override
	protected <V> IWrappedCallable<V> wrap(final Callable<V> inner) {
		if (inner instanceof IWrappedCallable)
			return (IWrappedCallable<V>) inner;

		NSFComponentModule module = getCurrentModule();
		Tasklet annot = inner.getClass().getAnnotation(Tasklet.class);
		if (annot == null || annot.context() == Context.DEFAULT) {
			if (module == null) {
				return super.wrap(inner);
			} else {
				if (inner instanceof AbstractXotsCallable) {
					XotsContext ctx = new XotsContext();
					ctx.setOpenLogApiPath(ODAPlatform.getXspPropertyAsString("xsp.openlog.filepath"));
					ctx.setContextApiPath(module.getDatabasePath());
					ctx.setTaskletClass(inner.getClass().getName());
					((AbstractXotsCallable<?>) inner).setContext(ctx);
				}
				return new XotsWrappedCallable<V>(module, inner);
			}
		} else if (annot.context() == Context.PLUGIN) {
			return super.wrap(inner);
		} else if (annot.context() == Context.XSPSCOPED || annot.context() == Context.XSPBARE) {
			if (inner instanceof AbstractXotsXspCallable) {
				XotsXspContext ctx = new XotsXspContext();
				if (annot.context() == Context.XSPSCOPED) {
					ctx.initialiseXspContext(true);
				} else {
					ctx.initialiseXspContext(false);
				}
				if (module != null) {
					ctx.setOpenLogApiPath(ODAPlatform.getXspPropertyAsString("xsp.openlog.filepath"));
					ctx.setContextApiPath(module.getDatabasePath());
					ctx.setTaskletClass(inner.getClass().getName());
				}
				((AbstractXotsXspCallable) inner).setContext(ctx);
			}
			if (module == null) {
				return super.wrap(inner);
			} else {
				return new XotsWrappedCallable(module, inner);
			}
		} else {
			if (module == null) {
				throw new NullPointerException("No module is running");
			}
			return new XotsWrappedCallable<V>(module, inner);
		}
	}

	@Override
	protected IWrappedRunnable wrap(final Runnable inner) {
		if (inner instanceof IWrappedRunnable)
			return (IWrappedRunnable) inner;

		NSFComponentModule module = getCurrentModule();
		Tasklet annot = inner.getClass().getAnnotation(Tasklet.class);
		if (annot == null || annot.context() == Context.DEFAULT) {
			if (module == null) {
				return super.wrap(inner);
			} else {
				if (inner instanceof AbstractXotsRunnable) {
					XotsContext ctx = new XotsContext();
					ctx.setOpenLogApiPath(ODAPlatform.getXspPropertyAsString("xsp.openlog.filepath"));
					ctx.setContextApiPath(module.getDatabasePath());
					ctx.setTaskletClass(inner.getClass().getName());
					((AbstractXotsRunnable) inner).setContext(ctx);
				}
				return new XotsWrappedRunnable(module, inner);
			}
		} else if (annot.context() == Context.PLUGIN) {
			return super.wrap(inner);
		} else if (annot.context() == Context.XSPSCOPED || annot.context() == Context.XSPBARE) {
			if (inner instanceof AbstractXotsXspRunnable) {
				XotsXspContext ctx = new XotsXspContext();
				if (annot.context() == Context.XSPSCOPED) {
					ctx.initialiseXspContext(true);
				} else {
					ctx.initialiseXspContext(false);
				}
				if (module != null) {
					ctx.setOpenLogApiPath(ODAPlatform.getXspPropertyAsString("xsp.openlog.filepath"));
					ctx.setContextApiPath(module.getDatabasePath());
					ctx.setTaskletClass(inner.getClass().getName());
				}
				((AbstractXotsXspRunnable) inner).setContext(ctx);
			}
			if (module == null) {
				return super.wrap(inner);
			} else {
				return new XotsWrappedRunnable(module, inner);
			}
		} else {
			if (module == null) {
				throw new NullPointerException("No module is running");
			}
			return new XotsWrappedRunnable(module, inner);
		}
	}

	@Override
	protected IWrappedCallable<?> wrap(final String moduleName, final String className, final Object... ctorArgs) {
		if (moduleName.startsWith("bundle:")) {
			return new XotsBundleTasklet(moduleName.substring(7), className, ctorArgs);
		}

		return new XotsModuleTasklet(moduleName, className, ctorArgs);
	}

}

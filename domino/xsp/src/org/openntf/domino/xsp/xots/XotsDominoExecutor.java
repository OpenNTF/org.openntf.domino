package org.openntf.domino.xsp.xots;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.util.concurrent.Callable;

import javax.servlet.ServletException;

import lotus.domino.Session;

import org.openntf.domino.session.ISessionFactory;
import org.openntf.domino.thread.DominoExecutor;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xsp.helpers.InvalidSessionFactory;
import org.openntf.domino.xsp.helpers.XPageSessionFactory;
import org.openntf.domino.xsp.helpers.XPageSignerSessionFactory;

import com.ibm.commons.util.ThreadLock;
import com.ibm.commons.util.ThreadLockManager;
import com.ibm.designer.runtime.domino.adapter.ComponentModule;
import com.ibm.domino.xsp.module.nsf.ModuleClassLoader;
import com.ibm.domino.xsp.module.nsf.NSFComponentModule;
import com.ibm.domino.xsp.module.nsf.NotesContext;

public class XotsDominoExecutor extends DominoExecutor {

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
	 * @param module
	 * @return
	 */
	static NotesContext initNotesContext(final NSFComponentModule module) {
		if (module == null && module.isDestroyed()) {
			throw new IllegalArgumentException("Module was destroyed in the meantime. Cannot run");
		}
		module.updateLastModuleAccess();
		final NotesContext ctx = new NotesContext(module);
		NotesContext.initThread(ctx);
		return ctx;
	}

	/**
	 * Helper for WrappedCallable/WrappedRunnable
	 * 
	 * @param sessionFactory
	 * @throws PrivilegedActionException
	 */
	static void initSessionFactory(final ISessionFactory sessionFactory) throws PrivilegedActionException {
		if (sessionFactory != null) {
			Factory.setSessionFactory(sessionFactory, SessionType.CURRENT);

			org.openntf.domino.Session current = Factory.getSession(SessionType.CURRENT);

			Factory.getNamedSessionFactory(true).createSession(current.getEffectiveUserName());
			Factory.setSessionFactory(sessionFactory, SessionType.CURRENT_FULL_ACCESS);
		}
	}

	/**
	 * Helper for WrappedCallable/WrappedRunnable
	 * 
	 * @param ctx
	 * @param mcl
	 * @param wrapper
	 * @throws ServletException
	 */

	static void initModule(final NotesContext ctx, final ClassLoader mcl, final WrappedAbstract<?> wrapper) throws ServletException {
		// next initialize the context with a FakeHttpRequest. This is neccessary so that internal things
		// like session-creation and so on work properly
		ctx.initRequest(new FakeHttpRequest());
		// RPr:
		// You may ask what I do here: ReLoading the module again from the MCL triggers signature checking.

		if (mcl instanceof ModuleClassLoader) {
			URLClassLoader dcl = (URLClassLoader) ((ModuleClassLoader) mcl).getDynamicClassLoader();
			String className = wrapper.getWrappedObject().getClass().getName();
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
					Factory.setSessionFactory(new XPageSignerSessionFactory(ctx, false), SessionType.SIGNER);
					Factory.setSessionFactory(new XPageSignerSessionFactory(ctx, true), SessionType.SIGNER_FULL_ACCESS);
				} else {
					// do not setup signer sessions if it is not properly signed!
					Factory.setSessionFactory(new InvalidSessionFactory(), SessionType.SIGNER);
					Factory.setSessionFactory(new InvalidSessionFactory(), SessionType.SIGNER_FULL_ACCESS);
				}
			} else {
				// The code is not part from an NSF, so it resides on the server
				Factory.setSessionFactory(new XPageSessionFactory(Factory.getLocalServerName(), false), SessionType.SIGNER);
				Factory.setSessionFactory(new XPageSessionFactory(Factory.getLocalServerName(), true), SessionType.SIGNER_FULL_ACCESS);
			}
		}

		Factory.setClassLoader(mcl);
	}

	// ------------------------------
	protected class XotsWrappedCallable<V> extends WrappedCallable<V> {

		private NSFComponentModule module_;

		public XotsWrappedCallable(final NSFComponentModule module, final Callable<V> wrappedObject) {
			super(wrappedObject);
			module_ = module;
		}

		@Override
		public V call() throws Exception {
			NotesContext ctx = initNotesContext(module_);
			try {
				DominoUtils.setBubbleExceptions(bubbleException);
				Factory.initThread();
				try {
					initSessionFactory(sessionFactory);
					NSFComponentModule codeModule = module_.getTemplateModule() == null ? module_ : module_.getTemplateModule();
					codeModule.updateLastModuleAccess();
					// RPr: In my opinion, This is the proper way how to run runnables in a different thread
					ThreadLock readLock = getLockManager(codeModule).getReadLock();
					try {
						readLock.acquire(); // we want to read data from the module, so lock it!

						// First we set up the classloader
						Thread thread = Thread.currentThread();
						ClassLoader oldCl = thread.getContextClassLoader();
						ClassLoader mcl = codeModule.getModuleClassLoader();
						thread.setContextClassLoader(mcl);
						try {
							initModule(ctx, mcl, this);
							return getWrappedObject().call();
						} finally {
							thread.setContextClassLoader(oldCl);
						}
					} finally {
						readLock.release();
					}
				} finally {
					Factory.termThread();
				}
			} catch (Exception e) {
				DominoUtils.handleException(e);
				return null;
			} finally {
				NotesContext.termThread();
			}

		}
	}

	protected class XotsWrappedRunnable extends WrappedRunnable {

		private NSFComponentModule module_;

		public XotsWrappedRunnable(final NSFComponentModule module, final Runnable wrappedObject) {
			super(wrappedObject);
			module_ = module;
		}

		@Override
		public void run() {
			NotesContext ctx = initNotesContext(module_);
			try {
				DominoUtils.setBubbleExceptions(bubbleException);
				Factory.initThread();
				try {
					initSessionFactory(sessionFactory);
					NSFComponentModule codeModule = module_.getTemplateModule() == null ? module_ : module_.getTemplateModule();
					codeModule.updateLastModuleAccess();
					// RPr: In my opinion, This is the proper way how to run runnables in a different thread
					ThreadLock readLock = getLockManager(codeModule).getReadLock();
					try {
						readLock.acquire(); // we want to read data from the module, so lock it!

						// First we set up the classloader
						Thread thread = Thread.currentThread();
						ClassLoader oldCl = thread.getContextClassLoader();
						ClassLoader mcl = codeModule.getModuleClassLoader();
						thread.setContextClassLoader(mcl);
						try {
							initModule(ctx, mcl, this);
							getWrappedObject().run();
						} finally {
							thread.setContextClassLoader(oldCl);
						}
					} finally {
						readLock.release();
					}
				} finally {
					Factory.termThread();
				}
			} catch (Exception e) {
				DominoUtils.handleException(e);
			} finally {
				NotesContext.termThread();
			}

		}
	}

	protected NSFComponentModule getCurrentModule() {
		NotesContext ctx = NotesContext.getCurrentUnchecked();
		if (ctx == null)
			return null;
		return ctx.getModule();
	}

	@Override
	protected <V> WrappedCallable<V> wrap(final Callable<V> inner) {
		NSFComponentModule module = getCurrentModule();
		if (module == null) {
			return super.wrap(inner);
		} else {
			return new XotsWrappedCallable<V>(module, inner);
		}
	}

	@Override
	protected WrappedRunnable wrap(final Runnable inner) {
		NSFComponentModule module = getCurrentModule();
		if (module == null) {
			return super.wrap(inner);
		} else {
			return new XotsWrappedRunnable(module, inner);
		}
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

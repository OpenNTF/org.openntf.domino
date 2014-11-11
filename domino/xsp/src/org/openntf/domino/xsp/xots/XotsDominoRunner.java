package org.openntf.domino.xsp.xots;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.util.concurrent.Callable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import lotus.domino.NotesException;
import lotus.domino.Session;

import org.openntf.domino.session.ISessionFactory;
import org.openntf.domino.thread.AbstractDominoRunner;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionMode;

import com.ibm.commons.util.ThreadLock;
import com.ibm.commons.util.ThreadLockManager;
import com.ibm.designer.runtime.domino.adapter.ComponentModule;
import com.ibm.domino.xsp.module.nsf.ModuleClassLoader;
import com.ibm.domino.xsp.module.nsf.NSFComponentModule;
import com.ibm.domino.xsp.module.nsf.NotesContext;
import com.ibm.xsp.servlet.local.LocalHttpServletRequest;
import com.ibm.xsp.servlet.local.LocalHttpSession;

/**
 * This runner runs a runnable inside a module.
 * 
 * It does proper setup and determines the signer of that runnable if it is inside an NSF
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public class XotsDominoRunner extends AbstractDominoRunner {

	public NSFComponentModule module_;

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

	public <T> XotsDominoRunner(final NSFComponentModule module, final Callable<T> callable, final ISessionFactory sessionFactory) {
		super(callable, sessionFactory);
		module_ = module;
	}

	public XotsDominoRunner(final NSFComponentModule module, final Runnable runnable, final ISessionFactory sessionFactory) {
		super(runnable, sessionFactory);
		module_ = module;
	}

	public XotsDominoRunner(final NSFComponentModule module, final Runnable runnable) {
		super(runnable);
		module_ = module;
	}

	private AccessControlContext getAccessControlContext() {
		return null;
	}

	@Override
	public void runNotes() {
		if (module_ == null && module_.isDestroyed()) {
			throw new IllegalArgumentException("Module was destroyed in the meantime. Cannot run");
		}
		module_.updateLastModuleAccess();
		final NotesContext ctx = new NotesContext(module_);
		NotesContext.initThread(ctx);
		try {
			NSFComponentModule codeModule = module_.getTemplateModule() == null ? module_ : module_.getTemplateModule();
			codeModule.updateLastModuleAccess();
			// RPr: In my opinion, This is the proper way how to run runnables in a different thread
			ThreadLock readLock = getLockManager(codeModule).getReadLock();
			try {
				readLock.acquire(); // we want to read data from the module, so lock it!
				runWithinModule(ctx, codeModule);
			} finally {
				readLock.release();
			}
		} catch (Exception e) {
			DominoUtils.handleException(e);
		} finally {
			NotesContext.termThread();
		}
	}

	/**
	 * The module is locked and up to date.... so run the runnable
	 * 
	 * @param codeModule
	 * @throws ServletException
	 * @throws NotesException
	 */
	private void runWithinModule(final NotesContext ctx, final NSFComponentModule codeModule) throws ServletException, NotesException {
		// First we set up the classloader
		Thread thread = Thread.currentThread();
		ClassLoader oldCl = thread.getContextClassLoader();
		ClassLoader mcl = codeModule.getModuleClassLoader();
		thread.setContextClassLoader(mcl);

		try {
			// next initialize the context with a FakeHttpRequest. This is neccessary so that internal things
			// like session-creation and so on work properly
			ctx.initRequest(new FakeHttpRequest());
			// RPr:
			// You may ask what I do here: ReLoading the module again from the MCL triggers signature checking.

			if (mcl instanceof ModuleClassLoader) {
				URLClassLoader dcl = (URLClassLoader) ((ModuleClassLoader) mcl).getDynamicClassLoader();
				String className = getRunnable().getClass().getName();
				String str = className.replace('.', '/') + ".class";
				URL url = dcl.findResource(str);
				if (url.getProtocol().startsWith("xspnsf")) {
					// Set up the "TopLevelXPageSigner == Signer of the runnable
					// As soon as we are in a xspnsf, we do not have a SessionFactory!

					if (sessionFactory == null) {
						Factory.setSession(ctx.getCurrentSession(), SessionMode.DEFAULT);
					}

					System.out.println("Loading Class from NSF:" + url);
					ctx.setSignerSessionRights("WEB-INF/classes/" + str);
					// TODO: RPr: There is a bug: you can decide if you want to use "sessionAsSigner" or "sessionAsSignerFullAccess"
					// But you cannot use both simultaneously!
					Session signerSession = ctx.getSessionAsSigner(true);

					if (signerSession != null) {
						Factory.setSession(signerSession, SessionMode.SESSION_AS_SIGNER);
						System.out.println("Code signer is " + signerSession.getUserName());
					} else {
						Factory.setSession(null, SessionMode.SESSION_AS_SIGNER);
					}

				} else {
					Factory.setSessionFactory(sessionFactory, SessionMode.DEFAULT);
					Factory.setSessionFactory(sessionFactory, SessionMode.SESSION_AS_SIGNER);
				}
			}

			Factory.setClassLoader(mcl);

			AccessControlContext runContext = getAccessControlContext();
			if (runContext == null) {
				super.runNotes();
			} else {
				AccessController.doPrivileged(new PrivilegedAction<Object>() {
					@Override
					public Object run() {
						XotsDominoRunner.super.runNotes();
						return null;
					}
				}, runContext);
			}
			//					if (Factory.getCodeSigner() == null) {
			//						System.out.println("XotsDominoRunner: WARNING: " + getRunnable().getClass().getName() + " is tainted!");
			//					}
		} finally {
			thread.setContextClassLoader(oldCl);
		}
	}

	private static class FakeHttpRequest extends LocalHttpServletRequest {
		HttpSession session = new LocalHttpSession();

		public FakeHttpRequest() {
			super(null, null);
		}

		@Override
		public Principal getUserPrincipal() {
			return new Principal() {
				@Override
				public String getName() {
					return (Factory.getLocalServerName());
				}
			};
		}

		@Override
		public HttpSession getSession(final boolean paramBoolean) {
			return session;
		}
	}
}
package org.openntf.domino.xsp.xots;

import java.lang.reflect.Field;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.Callable;

import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;

import org.openntf.domino.session.ISessionFactory;
import org.openntf.domino.thread.AbstractDominoRunner;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionMode;

import com.ibm.commons.util.ThreadLock;
import com.ibm.commons.util.ThreadLockManager;
import com.ibm.designer.runtime.domino.adapter.ComponentModule;
import com.ibm.domino.xsp.module.nsf.NSFComponentModule;
import com.ibm.domino.xsp.module.nsf.NotesContext;

public class XotsDominoRunner extends AbstractDominoRunner {

	public NSFComponentModule module_;

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

	static ThreadLockManager getLockManager(final NSFComponentModule module) {
		try {
			if (lockManager_field != null) {
				return (ThreadLockManager) lockManager_field.get(module);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void runNotes() {
		if (module_ == null && module_.isDestroyed()) {
			throw new IllegalArgumentException("Module was destroyed in the meantime. Cannot run");
		}
		module_.updateLastModuleAccess();
		NotesContext ctx = new NotesContext(module_);
		NotesContext.initThread(ctx);

		try {
			NSFComponentModule codeModule = module_.getTemplateModule() == null ? module_ : module_.getTemplateModule();
			codeModule.updateLastModuleAccess();
			// RPr: In my opinion, This is the proper way how to run runnables in a different thread
			ThreadLock readLock = getLockManager(codeModule).getReadLock();
			try {
				readLock.acquire(); // we want to read data from the module, so lock it!
				Thread thread = Thread.currentThread();
				ClassLoader oldCl = thread.getContextClassLoader();
				ClassLoader mcl = codeModule.getModuleClassLoader();
				thread.setContextClassLoader(mcl);
				try {

					Factory.setClassLoader(mcl);
					Factory.setSessionFactory(sessionFactory, SessionMode.DEFAULT);

					ApplicationFactory aFactory = (ApplicationFactory) FactoryFinder
							.getFactory("javax.faces.application.ApplicationFactory");

					System.out.println(aFactory.getApplication());

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

				} finally {
					thread.setContextClassLoader(oldCl);
				}
			} finally {
				readLock.release();
			}
		} catch (InterruptedException ie) {
			DominoUtils.handleException(ie);
		} finally {
			NotesContext.termThread();
		}
	}
}
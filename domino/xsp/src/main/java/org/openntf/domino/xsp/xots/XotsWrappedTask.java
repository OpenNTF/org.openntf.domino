/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.xsp.xots;

import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.MessageFormat;
import java.util.concurrent.Callable;

import org.openntf.domino.config.Configuration;
import org.openntf.domino.config.XotsConfiguration;
import org.openntf.domino.thread.AbstractWrappedTask;
import org.openntf.domino.types.Null;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xots.Tasklet;
import org.openntf.domino.xsp.ODAPlatform;

import com.ibm.commons.util.ThreadLock;
import com.ibm.domino.xsp.module.nsf.NSFComponentModule;
import com.ibm.domino.xsp.module.nsf.NotesContext;

public class XotsWrappedTask extends AbstractWrappedTask {

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
	protected Object callOrRun(final NSFComponentModule module) throws Exception {
		NSFComponentModule codeModule = null;

		if (module != null) {
			codeModule = module.getTemplateModule() == null ? module : module.getTemplateModule();
			if (module.isDestroyed() || codeModule.isDestroyed()) {
				throw new IllegalArgumentException("Module was destroyed in the meantime. Cannot run"); //$NON-NLS-1$
			}
			module.updateLastModuleAccess();
			codeModule.updateLastModuleAccess();
		}

		final NotesContext ctx = new NotesContext(module);
		NotesContext.initThread(ctx);

		try {
			// checkme: What should we use here?
			//Factory.initThread(ODAPlatform.getAppThreadConfig(module.getNotesApplication()));
			Factory.initThread(sourceThreadConfig);
			try {
				return invokeTasklet(ctx, codeModule);
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

	/**
	 * Invokes the tasklet
	 * 
	 * @param codeModule
	 * @param bubbleException
	 * @param sessionFactory
	 * @param wrappedTask
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "restriction" })
	protected Object invokeTasklet(final NotesContext ctx, final NSFComponentModule module) throws Exception {

		ClassLoader mcl = null;
		ThreadLock readLock = null;
		NSFComponentModule codeModule = null;
		if (module == null) {
			mcl = getWrappedTask().getClass().getClassLoader();
		} else {
			// RPr: In my opinion, This is the proper way how to run runnables in a different thread
			codeModule = module.getTemplateModule();
			if (codeModule == null)
				codeModule = module;

			mcl = codeModule.getModuleClassLoader();
			if (ODAPlatform.isAppFlagSet("LOCKXOTS", codeModule.getNotesApplication())) { //$NON-NLS-1$
				readLock = XotsDominoExecutor.getLockManager(codeModule).getReadLock();
			}
		}

		if (sessionFactory != null) {
			Factory.setSessionFactory(sessionFactory, SessionType.CURRENT);
			@SuppressWarnings("unused")
			org.openntf.domino.Session current = Factory.getSession(SessionType.CURRENT);

		}

		try {
			if (readLock != null)
				readLock.acquire(); // we want to read data from the module, so lock it!

			// set up the classloader
			ClassLoader oldCl = switchClassLoader(mcl);
			try {
				Object wrappedTask = getWrappedTask();
				XotsDominoExecutor.initModule(ctx, mcl, wrappedTask);

				XotsConfiguration config = null;
				if (mcl instanceof org.eclipse.osgi.internal.loader.ModuleClassLoader) {
					// Determine the bundle of mcl
					String bundle = ((org.eclipse.osgi.internal.loader.ModuleClassLoader) mcl).getBundle().getSymbolicName();
					config = Configuration.getXotsBundleConfiguration(bundle, wrappedTask.getClass().getName());
				} else {
					config = Configuration.getXotsNSFConfiguration(module.getDatabasePath(), wrappedTask.getClass().getName());
				}

				try {
					config.logStart();
					Object ret = invokeObject(wrappedTask);
					config.logSuccess();
					return ret;
				} catch (Exception e) {
					config.logError(e);
					throw e;
				}

			} finally {
				switchClassLoader(oldCl);
			}
		} finally {
			if (readLock != null)
				readLock.release();
		}

	}

	protected Object invokeObject(final Object wrappedTask) throws Exception {
		if (wrappedTask instanceof Callable) {
			return ((Callable<?>) wrappedTask).call();
		} else {
			((Runnable) wrappedTask).run();
			return null;
		}
	}

	/**
	 * Changes the Classloader and returns the old one
	 * 
	 * @param codeModule
	 * @return
	 */
	protected ClassLoader switchClassLoader(final ClassLoader newClassLoader) {
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

	/**
	 * Finds the constructor for the given Tasklet
	 * 
	 * @param clazz
	 * @param args
	 * @return
	 */
	protected Constructor<?> findConstructor(final Class<?> clazz, final Object[] args) {
		// sanity check if this is a public tasklet
		Tasklet annot = clazz.getAnnotation(Tasklet.class);
		if (annot == null) {
			throw new IllegalStateException(MessageFormat.format("Cannot run {0}, because it does not annotate @Tasklet.", clazz.getName())); //$NON-NLS-1$
		}

		//		if (!(Callable.class.isAssignableFrom(clazz)) && !(Runnable.class.isAssignableFrom(clazz))) {
		//			throw new IllegalStateException("Cannot run " + clazz.getName() + ", because it is no Runnable or Callable.");
		//		}

		// find the constructor
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
			throw new IllegalStateException(MessageFormat.format("Cannot run {0}, because it has no constructor for Arguments: {1}", clazz.getName(), ctorArgs)); //$NON-NLS-1$
		}
		return cTor;
	}
}
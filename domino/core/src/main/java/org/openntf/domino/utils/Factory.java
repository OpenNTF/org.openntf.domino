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
package org.openntf.domino.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openntf.domino.AutoMime;
import org.openntf.domino.Base;
import org.openntf.domino.Database;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.Session;
import org.openntf.domino.Session.RunContext;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.exceptions.DataNotCompatibleException;
import org.openntf.domino.exceptions.UndefinedDelegateTypeException;
import org.openntf.domino.ext.Session.Fixes;
import org.openntf.domino.logging.Logging;
import org.openntf.domino.session.INamedSessionFactory;
import org.openntf.domino.session.ISessionFactory;
import org.openntf.domino.session.NamedSessionFactory;
import org.openntf.domino.session.NativeSessionFactory;
import org.openntf.domino.session.PasswordSessionFactory;
import org.openntf.domino.session.SessionFullAccessFactory;
import org.openntf.domino.session.TrustedSessionFactory;
import org.openntf.domino.types.FactorySchema;
import org.openntf.domino.types.SessionDescendant;
import org.openntf.service.IServiceLocator;
import org.openntf.service.ServiceLocatorFinder;

import lotus.domino.NotesException;
import lotus.domino.NotesFactory;
import lotus.domino.NotesThread;

/**
 * The Enum Factory. Does the Mapping lotusObject <=> OpenNTF-Object
 */
@SuppressWarnings("nls")
public enum Factory {
	;

	/**
	 * Printer class (will be modified by XSP-environment), so that the Factory prints directly to Console (so no "HTTP JVM" Prefix is
	 * there)
	 *
	 * @author Roland Praml, FOCONIS AG
	 *
	 */
	public static class Printer {
		public void println(final String s) {
			System.out.println(s);
		}
	}

	public static Printer printer = new Printer();

	/**
	 * An identifier for the different session types the factory can create.
	 *
	 * @author Roland Praml, FOCONIS AG
	 *
	 */
	public enum SessionType {
		/**
		 * The current session. This means:
		 *
		 * <ul>
		 * <li>The current XPage session, if you are IN a XPage-Thread. This is equivalent to the "session" SSJS variable</li>
		 * <li>The current XOTS session, if you are IN a XOTS-Thread. <br>
		 * This is either the session that {@link XotsTasklet.Interface#getSessionFactory()} can create (if the Runnable implements that
		 * interface and provide a Factory)<br>
		 * or the session, you specified for that runnable with the {@link XotsTasklet#session()} annotation. See {@link XotsSessionType}
		 * for available types.
		 * </ul>
		 * <b>The Method will fail, if you are running in a wrongly set up Thread</b><br>
		 * (But there should be only XPage-Threads or XOTS-Threads. TODO RPr: and maybe DominoTheads)
		 */
		CURRENT(0, "CURRENT"), //$NON-NLS-1$

		/**
		 * Returns a session with full access. This is a named session (name is equal to {@link #CURRENT}s session name) but with full
		 * access rights. {@link #FULL_ACCESS} may provide the same session as {@link #CURRENT} if the Runnable was annotated with a
		 * *_FULL_ACCESS {@link XotsSessionType}
		 */
		CURRENT_FULL_ACCESS(1, "CURRENT_FULL_ACCESS"), //$NON-NLS-1$

		/**
		 * Returns a named session as signer. The code-signer is either the server (if the runnable is not inside an NSF) or the signer of
		 * that runnable. <br>
		 * <b>Note 1:</b> This session becomes invalid, if you the classloader gets tainted by loading classes that are signed by different
		 * users! <br/>
		 * <b>Note 2:</b> Due a bug, we return always SessionAsSigner (@see http://www.mydominolab.com/2011/10/xpages-sessionassigner.html)
		 */
		SIGNER(2, "SIGNER"), //$NON-NLS-1$

		/**
		 * This is currently the SAME session as {@link #SIGNER} due a Bug in XPages.
		 */
		SIGNER_FULL_ACCESS(3, "SIGNER_FULL_ACCESS"), //$NON-NLS-1$

		/**
		 * Returns a NATIVE session
		 */
		NATIVE(4, "NATIVE"), //$NON-NLS-1$

		/**
		 * Returns a TRUSTED session (This does not yet work!)
		 */
		TRUSTED(5, "TRUSTED"), //$NON-NLS-1$

		/**
		 * Returns a Session with full access.
		 */
		FULL_ACCESS(6, "FULL_ACCESS"), //$NON-NLS-1$

		/**
		 * Returns a Session with full access.
		 */
		PASSWORD(7, "PASSWORD"), //$NON-NLS-1$

		/**
		 * for internal use only!
		 */
		_NAMED_internal(-1, "NAMED"), //$NON-NLS-1$

		/**
		 * for internal use only!
		 */
		_NAMED_FULL_ACCESS_internal(-1, "NAMED_FULL_ACCESS"), //$NON-NLS-1$

		;

		public Session get() {
			return Factory.getSession(this);
		}

		static int SIZE = 8;
		int index;
		String alias;

		SessionType(final int index, final String alias) {
			this.index = index;
			this.alias = alias;
		}
	}

	public static class ThreadConfig {
		public final Fixes[] fixes;
		public final AutoMime autoMime;
		public final boolean bubbleExceptions;

		public ThreadConfig(final Fixes[] fixes, final AutoMime autoMime, final boolean bubbleExceptions) {
			super();
			this.fixes = fixes;
			this.autoMime = autoMime;
			this.bubbleExceptions = bubbleExceptions;
		}

	}

	// this thread config wraps everything and squelches errors (does not change default behavior
	public static ThreadConfig PERMISSIVE_THREAD_CONFIG = new ThreadConfig(Fixes.values(), AutoMime.WRAP_ALL, false);

	public static ThreadConfig STRICT_THREAD_CONFIG = new ThreadConfig(Fixes.values(), AutoMime.WRAP_32K, true);

	/**
	 * Container Class for all statistic counters
	 *
	 * @author Roland Praml, FOCONIS AG
	 *
	 */
	private static class Counters {

		/** The lotus counter. */
		private final Counter lotus;

		/** The recycle err counter. */
		private final Counter recycleErr;

		/** The auto recycle counter. */
		private final Counter autoRecycle;

		/** The manual recycle counter. */
		private final Counter manualRecycle;

		private boolean countPerThread_;

		private Map<Class<?>, Counter> classes;

		/**
		 * Returns a counter for a certain class
		 *
		 * @param clazz
		 *            the class
		 * @return a counter for the class
		 */
		public Counter forClass(final Class<?> clazz) {
			return classes.computeIfAbsent(clazz, key -> new Counter(countPerThread_));
		}

		Counters(final boolean countPerThread) {
			countPerThread_ = countPerThread;
			lotus = new Counter(countPerThread);
			recycleErr = new Counter(countPerThread);
			autoRecycle = new Counter(countPerThread);
			manualRecycle = new Counter(countPerThread);
			classes = new ConcurrentHashMap<Class<?>, Counter>();
		}
	}

	/**
	 * We have so many threadLocals here, so that it is worth to handle them all in a container class.
	 *
	 * @author Roland Praml, FOCONIS AG
	 *
	 */
	private static class ThreadVariables {
		private WrapperFactory wrapperFactory;

		private ClassLoader classLoader;

		private IServiceLocator serviceLocator;

		/**
		 * Support for different Locale
		 */
		private Locale userLocale;

		/** the factories can create a new session */
		public ISessionFactory[] sessionFactories = new ISessionFactory[SessionType.SIZE];

		/** the sessions are stored in the sessionHolder */
		private Session[] sessionHolders = new Session[SessionType.SIZE];

		public INamedSessionFactory namedSessionFactory;
		public INamedSessionFactory namedSessionFullAccessFactory;

		/** These sessions will be recycled at the end of that thread. Key = UserName of session */
		public Map<String, Session> ownSessions = new HashMap<String, Session>();

		private List<Runnable> terminateHooks;

		private ThreadConfig threadConfig;

		public ThreadVariables(final ThreadConfig tc) {
			threadConfig = tc;
		}

		/** clear the object */
		private void clear() {
			wrapperFactory = null;
			classLoader = null;
			serviceLocator = null;
			for (int i = 0; i < SessionType.SIZE; i++) {
				sessionHolders[i] = null;
				sessionFactories[i] = null;
			}
			userLocale = null;
			namedSessionFactory = null;
			namedSessionFullAccessFactory = null;

		}

		public void removeTerminateHook(final Runnable hook) {
			if (terminateHooks == null) {
				return;
			}
			terminateHooks.remove(hook);

		}

		public void addTerminateHook(final Runnable hook) {
			if (terminateHooks == null) {
				terminateHooks = new ArrayList<Runnable>();
			}
			terminateHooks.add(hook);
		}

		public void terminate() {
			if (terminateHooks != null) {
				for (Runnable hook : terminateHooks) {
					hook.run();
				}
				terminateHooks = null;
			}
		}
	}

	private static ISessionFactory[] defaultSessionFactories = new ISessionFactory[SessionType.SIZE];
	private static INamedSessionFactory defaultNamedSessionFactory;
	private static INamedSessionFactory defaultNamedSessionFullAccessFactory;

	/**
	 * Holder for variables that are different per thread
	 */
	private static ThreadLocal<ThreadVariables> threadVariables_ = new ThreadLocal<ThreadVariables>();

	private static List<Runnable> globalTerminateHooks = new ArrayList<Runnable>();
	private static List<Runnable> shutdownHooks = new ArrayList<Runnable>();

	private static String localServerName;

	private static ThreadVariables getThreadVariables() {
		ThreadVariables tv = threadVariables_.get();
		if (tv == null) {
			throw new IllegalStateException(Factory.class.getName() + " is not initialized for this thread!");
		}
		return tv;
	}

	public static ThreadConfig getThreadConfig() {
		ThreadVariables tv = threadVariables_.get();
		if (tv == null) {
			return PERMISSIVE_THREAD_CONFIG;
		}
		return tv.threadConfig;
	}

	private static Map<String, String> ENVIRONMENT = null;
	private static final Map<String, String> NOTESENV = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

	//private static boolean session_init = false;
	//private static boolean jar_init = false;
	private static boolean started = false;

	/**
	 * load the configuration
	 *
	 */
	private static void loadEnvironment() {
		if (ENVIRONMENT == null) {
			ENVIRONMENT = new HashMap<>();
		}
		try {
			AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
				try(InputStream is = Factory.class.getResourceAsStream("/META-INF/MANIFEST.MF")) { //$NON-NLS-1$
					Manifest manifest = new Manifest(is);
					// check that this is your manifest and do what you need or get the next one
					Attributes attrib = manifest.getMainAttributes();

					String bundleName = attrib.getValue("Bundle-SymbolicName"); //$NON-NLS-1$
					if (bundleName != null) {
						int pos;
						if ((pos = bundleName.indexOf(';')) != -1) {
							bundleName = bundleName.substring(0, pos);
						}
						if ("org.openntf.domino".equals(bundleName)) { //$NON-NLS-1$
							ENVIRONMENT.put("version", attrib.getValue("Bundle-Version")); //$NON-NLS-1$ //$NON-NLS-2$
							ENVIRONMENT.put("title", attrib.getValue("Bundle-Name")); //$NON-NLS-1$ //$NON-NLS-2$
							ENVIRONMENT.put("url", attrib.getValue("Implementation-Vendor-URL")); //$NON-NLS-1$ //$NON-NLS-2$
							return null;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			});
		} catch (AccessControlException e) {
			e.printStackTrace();
		}
		ENVIRONMENT.putIfAbsent("version", "0.0.0.unknown"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public static String getEnvironment(final String key) {
		if (ENVIRONMENT == null) {
			loadEnvironment();
		}
		return ENVIRONMENT.get(key);
	}

	public static String getTitle() {
		return getEnvironment("title"); //$NON-NLS-1$
	}

	public static String getUrl() {
		return getEnvironment("url"); //$NON-NLS-1$
	}

	public static String getVersion() {
		return getEnvironment("version"); //$NON-NLS-1$
	}

	public static String getDataPath() {
		return NOTESENV.computeIfAbsent("directory", Factory::_getEnv); //$NON-NLS-1$
	}

	public static String getProgramPath() {
		return NOTESENV.computeIfAbsent("notesprogram", Factory::_getEnv); //$NON-NLS-1$
	}

	public static String getHTTPJVMHeapSize() {
		return NOTESENV.computeIfAbsent("httpjvmheapsize", Factory::_getEnv); //$NON-NLS-1$
	}
	
	private static String _getEnv(String key) {
		try {
			lotus.domino.Session s = NotesFactory.createSession();
			try {
				return s.getEnvironmentString(key, true);
			} finally {
				s.recycle();
			}
		} catch(NotesException e) {
			return e.getLocalizedMessage();
		}
	}

	/** The Constant log_. */
	private static final Logger log_ = Logger.getLogger(Factory.class.getName());

	/** The lotus counter. */
	private static Counters counters;

	public static void enableCounters(final boolean enable, final boolean perThread) {
		if (enable) {
			counters = new Counters(perThread);
		} else {
			counters = null;
		}
	}

	/**
	 * Gets the lotus count.
	 *
	 * @return the lotus count
	 */
	public static int getLotusCount() {
		return counters == null ? 0 : counters.lotus.intValue();
	}

	/**
	 * Count a created lotus element.
	 */
	public static void countLotus(final Class<?> c) {
		if (counters != null) {
			counters.lotus.increment();
			counters.forClass(c).increment();
		}
	}

	/**
	 * Gets the recycle error count.
	 *
	 * @return the recycle error count
	 */
	public static int getRecycleErrorCount() {
		return counters == null ? 0 : counters.recycleErr.intValue();
	}

	/**
	 * Count recycle error.
	 */
	public static void countRecycleError(final Class<?> c) {
		if (counters != null) {
			counters.recycleErr.increment();
		}
	}

	/**
	 * Gets the auto recycle count.
	 *
	 * @return the auto recycle count
	 */
	public static int getAutoRecycleCount() {
		return counters == null ? 0 : counters.autoRecycle.intValue();
	}

	/**
	 * Count auto recycle.
	 *
	 * @return the int
	 */
	public static int countAutoRecycle(final Class<?> c) {
		if (counters != null) {
			counters.forClass(c).decrement();
			return counters.autoRecycle.increment();
		} else {
			return 0;
		}
	}

	/**
	 * Gets the manual recycle count.
	 *
	 * @return the manual recycle count
	 */
	public static int getManualRecycleCount() {
		return counters == null ? 0 : counters.manualRecycle.intValue();
	}

	/**
	 * Count a manual recycle
	 */
	public static int countManualRecycle(final Class<?> c) {
		if (counters != null) {
			counters.forClass(c).decrement();
			return counters.manualRecycle.increment();
		} else {
			return 0;
		}
	}

	/**
	 * get the active object count
	 *
	 * @return The current active object count
	 */
	public static int getActiveObjectCount() {
		if (counters != null) {
			return counters.lotus.intValue() - counters.autoRecycle.intValue() - counters.manualRecycle.intValue();
		} else {
			return 0;
		}
	}

	/**
	 * Determine the run context where we are
	 *
	 * @return The active RunContext
	 */
	public static RunContext getRunContext() {
		// TODO finish this implementation, which needs a lot of work.
		// - ADDIN
		// - APPLET
		// - DIIOP
		// - DOTS
		// - PLUGIN
		// - SERVLET
		// - XPAGES_NSF
		// maybe a simple way to determine => create a Throwable and look into the stack trace
		RunContext result = RunContext.UNKNOWN;
		SecurityManager sm = System.getSecurityManager();
		if (sm == null) {
			return RunContext.CLI;
		}

		Object o = sm.getSecurityContext();
		if (log_.isLoggable(Level.INFO)) {
			log_.log(Level.INFO, "SecurityManager is " + sm.getClass().getName() + " and context is " + o.getClass().getName());
		}
		if (sm instanceof lotus.notes.AgentSecurityManager) {
			lotus.notes.AgentSecurityManager asm = (lotus.notes.AgentSecurityManager) sm;
			Object xsm = asm.getExtenderSecurityContext();
			if (xsm instanceof lotus.notes.AgentSecurityContext) {
			}
			Object asc = asm.getSecurityContext();
			if (asc != null) {
				// System.out.println("Security context is " + asc.getClass().getName());
			}
			// ThreadGroup tg = asm.getThreadGroup();
			// System.out.println("ThreadGroup name: " + tg.getName());

			result = RunContext.AGENT;
		}
		//		com.ibm.domino.http.bootstrap.logger.RCPLoggerConfig rcplc;
		try {
			Class<?> BCLClass = Class.forName("com.ibm.domino.http.bootstrap.BootstrapClassLoader");
			if (BCLClass != null) {
				ClassLoader cl = (ClassLoader) BCLClass.getMethod("getSharedClassLoader").invoke(null);
				if ("com.ibm.domino.http.bootstrap.BootstrapOSGIClassLoader".equals(cl.getClass().getName())) {
					result = RunContext.XPAGES_OSGI;
				}
			}
		} catch (Exception e) {

		}

		return result;
	}

	private static WrapperFactory DEFAULT_WRAPPER_FACTORY = new org.openntf.domino.impl.WrapperFactory();

	/**
	 * returns the wrapper factory for this thread
	 *
	 * @return the thread's wrapper factory
	 */
	public static WrapperFactory getWrapperFactory() {
		ThreadVariables tv = getThreadVariables();
		WrapperFactory wf = tv.wrapperFactory;
		if (wf == null) {
			try {
				List<WrapperFactory> wfList = findApplicationServices(WrapperFactory.class);
				if (wfList.size() > 0) {
					wf = wfList.get(0);
				} else {
					wf = DEFAULT_WRAPPER_FACTORY;
				}
			} catch (Throwable t) {
				log_.log(Level.WARNING, "Getting default WrapperFactory", t);
				wf = DEFAULT_WRAPPER_FACTORY;
			}
			tv.wrapperFactory = wf;
		}
		return wf;
	}

	/**
	 * Returns the wrapper factory if initialized
	 *
	 * @return The active WrapperFactory
	 */
	public static WrapperFactory getWrapperFactory_unchecked() {
		ThreadVariables tv = threadVariables_.get();
		return tv == null ? null : threadVariables_.get().wrapperFactory;
	}

	// RPr: A setter is normally not needed. The wrapperFactory should be configure with an application service!
	//	/**
	//	 * Set/changes the wrapperFactory for this thread
	//	 *
	//	 * @param wf
	//	 *            The new WrapperFactory
	//	 */
	//	public static void setWrapperFactory(final WrapperFactory wf) {
	//		currentWrapperFactory.set(wf);
	//	}

	// --- session handling

	//	@SuppressWarnings("rawtypes")
	//	@Deprecated
	//	public static org.openntf.domino.Document fromLotusDocument(final lotus.domino.Document lotus, final Base parent) {
	//		return getWrapperFactory().fromLotus(lotus, Document.SCHEMA, (Database) parent);
	//	}

	//This should not be needed any more
	//public static void setNoRecycle(final Base<?> base, final boolean value) {
	//	getWrapperFactory().setNoRecycle(base, value);
	//}

	/*
	 * (non-JavaDoc)
	 *
	 * @see org.openntf.domino.WrapperFactory#fromLotus(lotus.domino.Base, FactorySchema, Base)
	 */
	/**
	 * @deprecated Use {@link WrapperFactory#fromLotus(lotus.domino.Base, FactorySchema, Base)} instead.
	 */
	@Deprecated
	@SuppressWarnings("rawtypes")
	public static <T extends Base, D extends lotus.domino.Base, P extends Base> T fromLotus(final D lotus,
			final FactorySchema<T, D, P> schema, final P parent) {
		T result = getWrapperFactory().fromLotus(lotus, schema, parent);

		//		if (result instanceof org.openntf.domino.Session) {
		//			ThreadVariables tv = getThreadVariables();
		//			org.openntf.domino.Session check = tv.sessionHolders[SessionType.CURRENT.index];
		//			if (check == null) {
		//				// TODO RPr: I have really objections to this.
		//				// Setting the first session as default session is NOT nice
		//				log_.log(Level.WARNING, "WARNING! Setting the Session " + result
		//						+ " as CURRENT session. This means you run in a wrong initialized thread", new Throwable());
		//				setSession((org.openntf.domino.Session) result, SessionType.CURRENT);
		//			}
		//		}
		return result;
	}

	// RPr: Should be done directly to current wrapperFactory
	//	public static boolean recacheLotus(final lotus.domino.Base lotus, final Base<?> wrapper, final Base<?> parent) {
	//		return getWrapperFactory().recacheLotusObject(lotus, wrapper, parent);
	//	}

	/**
	 * From lotus wraps a given lotus collection in an org.openntf.domino collection
	 *
	 * @param <T>
	 *            the generic org.openntf.domino type (drapper)
	 * @param <D>
	 *            the generic lotus.domino type (delegate)
	 * @param <P>
	 *            the generic org.openntf.domino type (parent)
	 * @param lotus
	 *            the object to wrap
	 * @param schema
	 *            the generic schema to ensure type safeness (may be null)
	 * @param parent
	 *            the parent
	 * @return the wrapped object
	 * @deprecated Use {@link WrapperFactory#fromLotus(Collection, FactorySchema, Base)} instead.
	 */
	@SuppressWarnings({ "rawtypes" })
	@Deprecated
	public static <T extends Base, D extends lotus.domino.Base, P extends Base> Collection<T> fromLotus(final Collection<?> lotusColl,
			final FactorySchema<T, D, P> schema, final P parent) {
		return getWrapperFactory().fromLotus(lotusColl, schema, parent);
	}

	/**
	 * From lotus wraps a given lotus collection in an org.openntf.domino collection
	 *
	 * @param <T>
	 *            the generic org.openntf.domino type (wrapper)
	 * @param <D>
	 *            the generic lotus.domino type (delegate)
	 * @param <P>
	 *            the generic org.openntf.domino type (parent)
	 * @param lotus
	 *            the object to wrap
	 * @param schema
	 *            the generic schema to ensure type safeness (may be null)
	 * @param parent
	 *            the parent
	 * @return the wrapped object
	 * @deprecated Use {@link WrapperFactory#fromLotusAsVector(Collection, FactorySchema, Base)} instead.
	 */
	@Deprecated
	@SuppressWarnings("rawtypes")
	public static <T extends Base, D extends lotus.domino.Base, P extends Base> Vector<T> fromLotusAsVector(final Collection<?> lotusColl,
			final FactorySchema<T, D, P> schema, final P parent) {
		return getWrapperFactory().fromLotusAsVector(lotusColl, schema, parent);
	}

	// RPr: Deprecated, so I commented this out
	//	/**
	//	 * From lotus.
	//	 *
	//	 * @deprecated Use {@link #fromLotus(lotus.domino.Base, FactorySchema, Base)} instead
	//	 *
	//	 *
	//	 * @param <T>
	//	 *            the generic type
	//	 * @param lotus
	//	 *            the lotus
	//	 * @param T
	//	 *            the t
	//	 * @param parent
	//	 *            the parent
	//	 * @return the t
	//	 */
	//	@SuppressWarnings({ "rawtypes", "unchecked" })
	//	@Deprecated
	//	public static <T> T fromLotus(final lotus.domino.Base lotus, final Class<? extends Base> T, final Base parent) {
	//		return (T) getWrapperFactory().fromLotus(lotus, (FactorySchema) null, parent);
	//	}
	//
	//	/**
	//	 * From lotus.
	//	 *
	//	 * @deprecated Use {@link #fromLotus(Collection, FactorySchema, Base)} instead
	//	 *
	//	 * @param <T>
	//	 *            the generic type
	//	 * @param lotusColl
	//	 *            the lotus coll
	//	 * @param T
	//	 *            the t
	//	 * @param parent
	//	 *            the parent
	//	 * @return the collection
	//	 */
	//	@SuppressWarnings({ "unchecked", "rawtypes" })
	//	@Deprecated
	//	public static <T> Collection<T> fromLotus(final Collection<?> lotusColl, final Class<? extends Base> T, final Base<?> parent) {
	//		return getWrapperFactory().fromLotus(lotusColl, (FactorySchema) null, parent);
	//	}
	//
	//	/**
	//	 * @deprecated Use {@link #fromLotusAsVector(Collection, FactorySchema, Base)}
	//	 */
	//	@Deprecated
	//	@SuppressWarnings({ "unchecked", "rawtypes" })
	//	public static <T> Vector<T> fromLotusAsVector(final Collection<?> lotusColl, final Class<? extends org.openntf.domino.Base> T,
	//			final org.openntf.domino.Base<?> parent) {
	//		return getWrapperFactory().fromLotusAsVector(lotusColl, (FactorySchema) null, parent);
	//	}

	/**
	 * Wrap column values, encapsulating {@link lotus.domino.DateTime}s, {@link lotus.domino.DateRange}s, and {@link lotus.domino.Name}s.
	 *
	 * @param values
	 *            the values
	 * @return a {@link java.util.Vector} with the objects from the collection appropriately wrapped.
	 * @deprecated Use {@link WrapperFactory#wrapColumnValues(Collection, Session)} instead.
	 */
	@Deprecated
	public static java.util.Vector<Object> wrapColumnValues(final Collection<?> values, final org.openntf.domino.Session session) {
		if (values == null) {
			log_.log(Level.WARNING, "Request to wrapColumnValues for a collection of null");
			return null;
		}
		return getWrapperFactory().wrapColumnValues(values, session);
	}

	/**
	 * Method to unwrap a object
	 *
	 * @param the
	 *            object to unwrap
	 * @return the unwrapped object
	 * @deprecated Use {@link WrapperFactory#fromLotus(lotus.domino.Base, FactorySchema, Base)} instead.
	 */
	@Deprecated
	public static <T extends lotus.domino.Base> T toLotus(final T base) {
		return getWrapperFactory().toLotus(base);
	}

	/**
	 * Gets the current session. Equivalent to calling {@link #getSession(SessionType)} with {@link SessionType.CURRENT}.
	 *
	 * @return the session
	 */
	public static org.openntf.domino.Session getSession() {
		return getSession(SessionType.CURRENT);
	}

	/**
	 * Gets the session full access.
	 *
	 * @return the session full access
	 * @deprecated Use {@link #getSession(SessionType)} with {@link SessionType.FULL_ACCESS} instead.
	 */
	@Deprecated
	public static org.openntf.domino.Session getSessionFullAccess() {
		return getSession(SessionType.FULL_ACCESS);
	}

	/**
	 * Gets the trusted session.
	 *
	 * @return the trusted session
	 * @deprecated Use {@link #getSession(SessionType)} with {@link SessionType.TRUSTED} instead.
	 */
	@Deprecated
	public static org.openntf.domino.Session getTrustedSession() {
		return getSession(SessionType.TRUSTED);
	}

	/**
	 * Gets the trusted session.
	 *
	 * @return the trusted session
	 * @deprecated Use {@link #getSession(SessionType)} with {@link SessionType.SIGNER} instead.
	 */
	@Deprecated
	public static org.openntf.domino.Session getSessionAsSigner() {
		return getSession(SessionType.SIGNER);
	}

	public static org.openntf.domino.Session getSession(final SessionType mode, final String paramString) {
		ThreadVariables tv = getThreadVariables();
		org.openntf.domino.Session result = tv.sessionHolders[mode.index];
		if (result == null || result.isDead()) {
			ISessionFactory sf = getSessionFactory(mode);
			if (mode == SessionType.PASSWORD) {
				result = ((PasswordSessionFactory) sf).createSession(paramString);
				result = sf.createSession();
				result.setSessionType(mode);
				tv.sessionHolders[mode.index] = result;
				tv.ownSessions.put(mode.alias, result);

				Session currentChk = tv.sessionHolders[SessionType.CURRENT.index];
				if (currentChk == null) {
					tv.sessionHolders[SessionType.CURRENT.index] = result;
					tv.ownSessions.put(SessionType.CURRENT.alias, result);
				}
			} else if (sf != null) {
				result = sf.createSession();
				result.setSessionType(mode);
				tv.sessionHolders[mode.index] = result;
				// Per default. Session objects are not recycled by the ODA and thats OK so.
				// this is our own session which will be recycled in terminate
				tv.ownSessions.put(mode.alias, result);

				//TODO NTF per RPr we can remove this when we have an alternative way to designate an internal session
				Session currentChk = tv.sessionHolders[SessionType.CURRENT.index];
				if (currentChk == null) {
					tv.sessionHolders[SessionType.CURRENT.index] = result;
					tv.ownSessions.put(SessionType.CURRENT.alias, result);
				}
				//					System.out.println("TEMP DEBUG: Created new session " + System.identityHashCode(result) + " of type " + mode.name()
				//							+ " in thread " + System.identityHashCode(Thread.currentThread()) + " from TV " + System.identityHashCode(tv));
			}
			if (result == null) {
				log_.severe("Unable to get the session of type " + mode.alias
						+ ". This probably means that you are running in an unsupported configuration "
						+ "or you forgot to set up your context at the start of the operation. "
						+ "If you're running in XPages, check the xsp.properties of your database. "
						+ "If you are running in an Agent, make sure you start with a call to "
						+ "Factory.setSession() and pass in your lotus.domino.Session");
				Throwable t = new RuntimeException();
				t.printStackTrace();
			}
		} else {
			//			System.out.println("TEMP DEBUG: Found an existing session " + System.identityHashCode(result) + " of type " + mode.name()
			//					+ " in thread " + System.identityHashCode(Thread.currentThread()) + " from TV " + System.identityHashCode(tv));
		}
		return result;
	}

	/**
	 *
	 * @param mode
	 *            The type of session to create
	 * @return A Session object corresponding to the given type
	 */
	public static org.openntf.domino.Session getSession(final SessionType mode) {
		ThreadVariables tv = getThreadVariables();
		org.openntf.domino.Session result = tv.sessionHolders[mode.index];
		if (result == null || result.isDead()) {
			//			System.out.println("TEMP DEBUG: No session found of type " + mode.name() + " in thread "
			//					+ System.identityHashCode(Thread.currentThread()) + " from TV " + System.identityHashCode(tv));

			ISessionFactory sf = getSessionFactory(mode);
			if (sf != null) {
				//				System.out.println("TEMP DEBUG getting a session using a " + sf.getClass().getName() + " for type " + mode.toString());
				result = sf.createSession();
				//				System.out.println("TEMP DEBUG got a session for " + result.getEffectiveUserName());
				result.setSessionType(mode);
				tv.sessionHolders[mode.index] = result;
				// Per default. Session objects are not recycled by the ODA and thats OK so.
				// this is our own session which will be recycled in terminate
				tv.ownSessions.put(mode.alias, result);

				Session currentChk = tv.sessionHolders[SessionType.CURRENT.index];
				if (currentChk == null) {
					try {
						ISessionFactory chkSf = getSessionFactory(SessionType.CURRENT);
						if (chkSf == null) {
							tv.sessionHolders[SessionType.CURRENT.index] = result;
							tv.ownSessions.put(SessionType.CURRENT.alias, result);
						} else {
							Session chkSession = chkSf.createSession();
							if (chkSession == null) {
								tv.sessionHolders[SessionType.CURRENT.index] = result;
								tv.ownSessions.put(SessionType.CURRENT.alias, result);
							}
						}
					} catch (Exception e) {
						tv.sessionHolders[SessionType.CURRENT.index] = result;
						tv.ownSessions.put(SessionType.CURRENT.alias, result);
					}
				}
				//					System.out.println("TEMP DEBUG: Created new session " + System.identityHashCode(result) + " of type " + mode.name()
				//							+ " in thread " + System.identityHashCode(Thread.currentThread()) + " from TV " + System.identityHashCode(tv));
			} else {
				if (SessionType.NATIVE.equals(mode)) {
					sf = new NativeSessionFactory(null);
					setSessionFactory(sf, mode);
					result = sf.createSession();
					result.setSessionType(mode);
					tv.sessionHolders[mode.index] = result;
					tv.ownSessions.put(mode.alias, result);
				} else {
					log_.severe("A session of type " + mode.alias
							+ " was requested but no Factory has been defined for that type in this thread.");
				}
			}
			if (result == null) {
				log_.severe("Unable to get the session of type " + mode.alias + " from the session factory of type "
						+ (sf == null ? "null" : sf.getClass().getName())
						+ ". This probably means that you are running in an unsupported configuration "
						+ "or you forgot to set up your context at the start of the operation. "
						+ "If you're running in XPages, check the xsp.properties of your database. "
						+ "If you are running in an Agent, make sure you start with a call to "
						+ "Factory.setSession() and pass in your lotus.domino.Session");
				Throwable t = new RuntimeException();
				t.printStackTrace();
			}
		} else {
			//			System.out.println("TEMP DEBUG: Found an existing session " + System.identityHashCode(result) + " of type " + mode.name()
			//					+ " in thread " + System.identityHashCode(Thread.currentThread()) + " from TV " + System.identityHashCode(tv));
		}
		return result;
	}

	/**
	 * Returns the current session, if available. Does never create a session
	 *
	 * @return the session
	 */
	public static org.openntf.domino.Session getSession_unchecked(final SessionType type) {
		ThreadVariables tv = threadVariables_.get();
		return tv == null ? null : tv.sessionHolders[type.index];
	}

	/**
	 * Sets the session for a certain sessionMode
	 *
	 */
	//	public static void setSession(final lotus.domino.Session session, final SessionType mode) {
	//		if (session instanceof org.openntf.domino.Session) {
	//			getThreadVariables().sessionHolders[mode.index] = (org.openntf.domino.Session) session;
	//			//			throw new UnsupportedOperationException("You should not set an org.openntf.domino.session as Session");
	//		} else {
	//			getThreadVariables().sessionHolders[mode.index] = fromLotus(session, Session.SCHEMA, null);
	//		}
	//	}

	public static void setSessionFactory(final ISessionFactory sessionFactory, final SessionType mode) {
		getThreadVariables().sessionFactories[mode.index] = sessionFactory;
	}

	public static ISessionFactory getSessionFactory(final SessionType mode) {
		ThreadVariables tv = threadVariables_.get();
		if (tv == null || tv.sessionFactories[mode.index] == null) {
			return defaultSessionFactories[mode.index];
		}
		return tv.sessionFactories[mode.index];
	}

	/**
	 * // * Sets the current session // * // * @param session // * the lotus session //
	 */
	//	public static void setSession(final lotus.domino.Session session) {
	//		setSession(session, SessionType.DEFAULT);
	//	}
	//
	//	/**
	//	 * Sets the current trusted session
	//	 *
	//	 * @param session
	//	 *            the lotus session
	//	 */
	//	public static void setTrustedSession(final lotus.domino.Session session) {
	//		setSession(session, SessionType.TRUSTED);
	//	}
	//
	//	/**
	//	 * Sets the current session with full access
	//	 *
	//	 * @param session
	//	 *            the lotus session
	//	 */
	//	public static void setSessionFullAccess(final lotus.domino.Session session) {
	//		setSession(session, SessionType.FULL_ACCESS);
	//	}

	//	/**
	//	 * clears the current session
	//	 */
	//	public static void clearSession() {
	//		threadVariables.get().sessionHolder = null;
	//	}

	// TODO: Determine if this is the right way to deal with Xots access to faces contexts

	// RPr: use getSession_unchecked().getCurrentDatabase
	//	/**
	//	 * Returns the session's current database if available. Does never create a session.
	//	 *
	//	 * @see #getSession_unchecked()
	//	 * @return The session's current database
	//	 */
	//	public static Database getDatabase_unchecked() {
	//		Session sess = getSession_unchecked(SessionType.CURRENT);
	//		return (sess == null) ? null : sess.getCurrentDatabase();
	//	}

	// RPr: I think it is a better idea to set the currentDatabase on the currentSesssion

	// TODO remove that code
	//	public static void setDatabase(final Database database) {
	//		setNoRecycle(database, true);
	//		currentDatabaseHolder_.set(database);
	//	}
	//
	//	public static void clearDatabase() {
	//		currentDatabaseHolder_.set(null);
	//	}

	public static ClassLoader getClassLoader() {
		ThreadVariables tv = getThreadVariables();
		if (tv.classLoader == null) {
			ClassLoader loader = null;
			try {
				loader = AccessController.doPrivileged(new PrivilegedExceptionAction<ClassLoader>() {
					@Override
					public ClassLoader run() throws Exception {
						return Thread.currentThread().getContextClassLoader();
					}
				});
			} catch (AccessControlException e) {
				e.printStackTrace();
			} catch (PrivilegedActionException e) {
				e.printStackTrace();
			}
			setClassLoader(loader);
		}
		return tv.classLoader;
	}

	public static <T> List<T> findApplicationServices(final Class<T> serviceClazz) {

		ThreadVariables tv = getThreadVariables();

		if (tv.serviceLocator == null) {
			tv.serviceLocator = ServiceLocatorFinder.findServiceLocator();
		}
		if (tv.serviceLocator == null) {
			throw new IllegalStateException(
					"No service locator available so we cannot find the application services for " + serviceClazz.getName());
		}

		return tv.serviceLocator.findApplicationServices(serviceClazz);
	}

	public static void setClassLoader(final ClassLoader loader) {
		getThreadVariables().classLoader = loader;
	}

	public static void setCurrentToSession(final Session session) {
		//		System.out.println("TEMP DEBUG Setting current session to a session for " + session.getEffectiveUserName() + " in thread "
		//				+ System.identityHashCode(Thread.currentThread()));
		ThreadVariables tv = getThreadVariables();
		tv.sessionHolders[SessionType.CURRENT.index] = session;
	}

	// avoid clear methods
	//	public static void clearWrapperFactory() {
	//		currentWrapperFactory.remove();
	//	}
	//
	//	public static void clearClassLoader() {
	//		currentClassLoader_.remove();
	//	}
	//
	//	public static void clearServiceLocator() {
	//		currentServiceLocator_.remove();
	//	}
	//
	//	public static void clearDominoGraph() {
	//		DominoGraph.clearDocumentCache();
	//	}
	//
	//	public static void clearNoteCoordinateBuffer() {
	//		NoteCoordinate.clearLocals();
	//	}
	//
	//	public static void clearBubbleExceptions() {
	//		DominoUtils.setBubbleExceptions(null);
	//	}

	/**
	 * Begin with a clear environment. Initialize this thread
	 *
	 */
	public static void initThread(final ThreadConfig tc) { // RPr: Method was deliberately renamed
		if (!started) {
			throw new IllegalStateException("Factory is not yet started");
		}
		if (threadVariables_.get() != null) {
			log_.log(Level.SEVERE,
					"WARNING - Thread " + Thread.currentThread().getName() + " was not correctly terminated or initialized twice",
					new Throwable());
		}
		threadVariables_.set(new ThreadVariables(tc));
	}

	/**
	 * terminate the current thread.
	 */
	public static void termThread() { // RPr: Method was deliberately renamed
		if (log_.isLoggable(Level.FINER)) {
			log_.log(Level.FINER, "Factory.termThread()", new Throwable());
		}
		ThreadVariables tv = threadVariables_.get();
		if (tv == null) {
			log_.log(Level.SEVERE,
					"WARNING - Thread " + Thread.currentThread().getName() + " was not correctly initalized or terminated twice",
					new Throwable());
			return;
		}
		//		System.out.println("TEMP DEBUG: Factory thread terminating.");
		//		Throwable trace = new Throwable();
		//		trace.printStackTrace();
		try {

			for (Runnable term : globalTerminateHooks) {
				term.run();
			}
			tv.terminate();
			if (tv.wrapperFactory != null) {
				tv.wrapperFactory.recycle();
			}
			//		System.out.println("DEBUG: cleared " + termCount + " references from the queue...");
			DominoUtils.setBubbleExceptions(null);
			// The last step is to recycle ALL own sessions
			for (Session sess : tv.ownSessions.values()) {
				if (sess != null) {
					sess.recycle();
				}
			}
		} catch (Throwable t) {
			log_.log(Level.SEVERE, "An error occured while terminating the factory", t);
		} finally {
			tv.clear();
			threadVariables_.set(null);
			//			System.gc();
		}
		if (counters != null) {
			System.out.println(dumpCounters(true));
		}
	}

	public static void startup() {
		synchronized (Factory.class) {

			NotesThread.sinitThread();
			try {
				lotus.domino.Session sess = lotus.domino.NotesFactory.createSession();
				try {
					startup(sess);
				} finally {
					sess.recycle();
				}
			} catch (NotesException e) {
				e.printStackTrace();
			} finally {
				NotesThread.stermThread();
			}
		}
	}

	public static synchronized void startup(final lotus.domino.Session session) {
		if (session instanceof org.openntf.domino.Session) {
			throw new UnsupportedOperationException("Initialization must be done on the raw session");
		}
		if (started) {
			Factory.println("OpenNTF Domino API is already started. Cannot start it again");
		}

		try {
			localServerName = session.getUserName();
		} catch (NotesException e) {
			Factory.println(MessageFormat.format("Exception determining the current user name", e.getMessage()));
		}

		Factory.println("Starting the OpenNTF Domino API");

		loadEnvironment();

		// There is NO(!) Default SessionFactory for the current session. you have to set it!
		defaultSessionFactories[SessionType.CURRENT.index] = null;

		// For CURRENT_FULL_ACCESS, we return a named session with full access = true
		defaultSessionFactories[SessionType.CURRENT_FULL_ACCESS.index] = new ISessionFactory() {
			private static final long serialVersionUID = 1L;

			private String getName() {
				return Factory.getSession(SessionType.CURRENT).getEffectiveUserName();
			}

			@Override
			public Session createSession() {
				return Factory.getNamedSession(getName(), true);
			}
		};

		String defaultApiPath = null; // maybe we set this to ODA.nsf

		// In XPages environment, these factories will be replaced
		defaultNamedSessionFactory = new NamedSessionFactory(defaultApiPath);
		defaultNamedSessionFullAccessFactory = new SessionFullAccessFactory(defaultApiPath);
		defaultSessionFactories[SessionType.SIGNER.index] = new NativeSessionFactory(defaultApiPath);
		defaultSessionFactories[SessionType.SIGNER_FULL_ACCESS.index] = new SessionFullAccessFactory(defaultApiPath);

		// This will ALWAYS return the native/trusted/full access session (not overridden in XPages)
		defaultSessionFactories[SessionType.NATIVE.index] = new NativeSessionFactory(defaultApiPath);
		defaultSessionFactories[SessionType.TRUSTED.index] = new TrustedSessionFactory(defaultApiPath);
		defaultSessionFactories[SessionType.FULL_ACCESS.index] = new SessionFullAccessFactory(defaultApiPath);

		started = true;

		Factory.println(MessageFormat.format("OpenNTF API Version {0} started", ENVIRONMENT.get("version"))); //$NON-NLS-2$

		// Start up logging
		try {
			AccessController.doPrivileged((PrivilegedExceptionAction<Object>) () -> {
				Logging.getInstance().startUp();
				return null;
			});
		} catch (AccessControlException e) {
			e.printStackTrace();
		} catch (PrivilegedActionException e) {
			e.printStackTrace();
		}
	}

	public static void setNamedFactories4XPages(final INamedSessionFactory normal, final INamedSessionFactory fullaccess) {
		defaultNamedSessionFactory = normal;
		defaultNamedSessionFullAccessFactory = fullaccess;

	}

	public static synchronized void shutdown() {
		Factory.println("Shutting down the OpenNTF Domino API... ");
		Runnable[] copy = shutdownHooks.toArray(new Runnable[shutdownHooks.size()]);
		for (Runnable term : copy) {
			try {
				term.run();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		Factory.println("OpenNTF Domino API shut down");
		started = false;
	}

	public static boolean isStarted() {
		return started;
	}

	public static boolean isInitialized() {
		return threadVariables_.get() != null;
	}

	public static void setUserLocale(final Locale loc) {
		getThreadVariables().userLocale = loc;
	}

	public static Locale getUserLocale() {
		return getThreadVariables().userLocale;
	}

	/**
	 * Returns the internal locale. The Locale is retrieved by this way:
	 * <ul>
	 * <li>If a currentDatabase is set, the DB is queried for its locale</li>
	 * <li>If there is no database.locale, the system default locale is returned</li>
	 * </ul>
	 * This locale should be used, if you write log entries in a server log for example.
	 *
	 * @return the currentDatabase-locale or default-locale
	 */
	public static Locale getInternalLocale() {
		Locale ret = null;
		// are we in context of an NotesSession? Try to figure out the current database.
		Session sess = getSession_unchecked(SessionType.CURRENT);
		Database db = (sess == null) ? null : sess.getCurrentDatabase();
		if (db != null) {
			ret = db.getLocale();
		}
		if (ret == null) {
			ret = Locale.getDefault();
		}
		return ret;
	}

	/**
	 * Returns the external locale. The Locale is retrieved by this way:
	 * <ul>
	 * <li>Return the external locale (= the browser's locale in most cases) if available</li>
	 * <li>If a currentDatabase is set, the DB is queried for its locale</li>
	 * <li>If there is no database.locale, the system default locale is returned</li>
	 * </ul>
	 * This locale should be used, if you generate messages for the current (browser)user.
	 *
	 * @return the external-locale, currentDatabase-locale or default-locale
	 */
	public static Locale getExternalLocale() {
		Locale ret = getUserLocale();
		if (ret == null) {
			ret = getInternalLocale();
		}
		return ret;
	}

	/**
	 * Debug method to get statistics
	 *
	 */
	public static String dumpCounters(final boolean details) {
		if (counters == null) {
			return "Counters are disabled";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("LotusCount: ");
		sb.append(getLotusCount());

		sb.append(" AutoRecycled: ");
		sb.append(getAutoRecycleCount());
		sb.append(" ManualRecycled: ");
		sb.append(getManualRecycleCount());
		sb.append(" RecycleErrors: ");
		sb.append(getRecycleErrorCount());
		sb.append(" ActiveObjects: ");
		sb.append(getActiveObjectCount());

		if (!counters.classes.isEmpty() && details) {
			sb.append("\n=== The following objects were left in memory ===");
			for (Entry<Class<?>, Counter> e : counters.classes.entrySet()) {
				int i = e.getValue().intValue();
				if (i != 0) {
					sb.append("\n" + i + "\t" + e.getKey().getName());
				}
			}
		}
		return sb.toString();
	}

	public static INamedSessionFactory getNamedSessionFactory(final boolean fullAccess) {
		ThreadVariables tv = getThreadVariables();
		if (fullAccess) {
			return tv.namedSessionFullAccessFactory != null ? tv.namedSessionFullAccessFactory : defaultNamedSessionFullAccessFactory;
		} else {
			return tv.namedSessionFactory != null ? tv.namedSessionFactory : defaultNamedSessionFactory;
		}

	}

	public static org.openntf.domino.Session getNamedSession(final String name, final boolean fullAccess) {
		ThreadVariables tv = getThreadVariables();
		String key = name.toLowerCase() + (fullAccess ? ":full" : ":normal");
		Session sess = tv.ownSessions.get(key);
		if (sess == null || sess.isDead()) {
			INamedSessionFactory sf = getNamedSessionFactory(fullAccess);
			if (sf != null) {
				sess = sf.createSession(name);
				sess.setSessionType(fullAccess ? SessionType._NAMED_FULL_ACCESS_internal : SessionType._NAMED_internal);
			}
			tv.ownSessions.put(key, sess);
		}
		return sess;

	}

	//	/**
	//	 * Gets the parent database.
	//	 *
	//	 * @param base
	//	 *            the base
	//	 * @return the parent database
	//	 */
	//	@Deprecated
	//	public static Database getParentDatabase(final Base<?> base) {
	//		if (base instanceof org.openntf.domino.Database) {
	//			return (org.openntf.domino.Database) base;
	//		} else if (base instanceof DatabaseDescendant) {
	//			return ((DatabaseDescendant) base).getAncestorDatabase();
	//		} else if (base == null) {
	//			throw new NullPointerException("Base object cannot be null");
	//		} else {
	//			throw new UndefinedDelegateTypeException("Couldn't find session for object of type " + base.getClass().getName());
	//		}
	//	}

	/**
	 * Gets the session.
	 *
	 * @param base
	 *            the base
	 * @return the session
	 * @deprecated Use {@link SessionDescendant#getAncestorSession()} on the object instead.
	 */
	@Deprecated
	public static Session getSession(final lotus.domino.Base base) {
		org.openntf.domino.Session result = null;
		if (base instanceof SessionDescendant) {
			result = ((SessionDescendant) base).getAncestorSession();
		} else if (base instanceof org.openntf.domino.Session) {
			result = (org.openntf.domino.Session) base;
		} else if (base == null) {
			throw new NullPointerException("Base object cannot be null");
		}
		if (result == null) {
			throw new UndefinedDelegateTypeException("Couldn't find session for object of type " + base.getClass().getName());
		}
		return result;
	}

	// public static boolean toBoolean(Object value) {
	// if (value instanceof String) {
	// char[] c = ((String) value).toCharArray();
	// if (c.length > 1 || c.length == 0) {
	// return false;
	// } else {
	// return c[0] == '1';
	// }
	// } else if (value instanceof Double) {
	// if (((Double) value).intValue() == 0) {
	// return false;
	// } else {
	// return true;
	// }
	// } else {
	// throw new DataNotCompatibleException("Cannot convert a " + value.getClass().getName() + " to boolean primitive.");
	// }
	// }
	//
	// public static int toInt(Object value) {
	// if (value instanceof Integer) {
	// return ((Integer) value).intValue();
	// } else if (value instanceof Double) {
	// return ((Double) value).intValue();
	// } else {
	// throw new DataNotCompatibleException("Cannot convert a " + value.getClass().getName() + " to int primitive.");
	// }
	// }
	//
	// public static double toDouble(Object value) {
	// if (value instanceof Integer) {
	// return ((Integer) value).doubleValue();
	// } else if (value instanceof Double) {
	// return ((Double) value).doubleValue();
	// } else {
	// throw new DataNotCompatibleException("Cannot convert a " + value.getClass().getName() + " to double primitive.");
	// }
	// }
	//
	// public static long toLong(Object value) {
	// if (value instanceof Integer) {
	// return ((Integer) value).longValue();
	// } else if (value instanceof Double) {
	// return ((Double) value).longValue();
	// } else {
	// throw new DataNotCompatibleException("Cannot convert a " + value.getClass().getName() + " to long primitive.");
	// }
	// }
	//
	// public static short toShort(Object value) {
	// if (value instanceof Integer) {
	// return ((Integer) value).shortValue();
	// } else if (value instanceof Double) {
	// return ((Double) value).shortValue();
	// } else {
	// throw new DataNotCompatibleException("Cannot convert a " + value.getClass().getName() + " to short primitive.");
	// }
	//
	// }
	//
	// public static float toFloat(Object value) {
	// if (value instanceof Integer) {
	// return ((Integer) value).floatValue();
	// } else if (value instanceof Double) {
	// return ((Double) value).floatValue();
	// } else {
	// throw new DataNotCompatibleException("Cannot convert a " + value.getClass().getName() + " to float primitive.");
	// }
	//
	// }
	//
	// public static Object toPrimitive(Vector<Object> values, Class<?> ctype) {
	// if (ctype.isPrimitive()) {
	// throw new DataNotCompatibleException(ctype.getName() + " is not a primitive type.");
	// }
	// if (values.size() > 1) {
	// throw new DataNotCompatibleException("Cannot create a primitive " + ctype + " from data because we have a multiple values.");
	// }
	// if (values.isEmpty()) {
	// throw new DataNotCompatibleException("Cannot create a primitive " + ctype + " from data because we don't have any values.");
	// }
	// if (ctype == Boolean.TYPE)
	// return toBoolean(values.get(0));
	// if (ctype == Integer.TYPE)
	// return toInt(values.get(0));
	// if (ctype == Short.TYPE)
	// return toShort(values.get(0));
	// if (ctype == Long.TYPE)
	// return toLong(values.get(0));
	// if (ctype == Float.TYPE)
	// return toFloat(values.get(0));
	// if (ctype == Double.TYPE)
	// return toDouble(values.get(0));
	// if (ctype == Byte.TYPE)
	// throw new UnimplementedException("Primitive conversion for byte not yet defined");
	// if (ctype == Character.TYPE)
	// throw new UnimplementedException("Primitive conversion for char not yet defined");
	// throw new DataNotCompatibleException("");
	// }
	//
	// public static String join(Collection<Object> values, String separator) {
	// StringBuilder sb = new StringBuilder();
	// Iterator<Object> it = values.iterator();
	// while (it.hasNext()) {
	// sb.append(String.valueOf(it.next()));
	// if (it.hasNext())
	// sb.append(separator);
	// }
	// return sb.toString();
	// }
	//
	// public static String join(Collection<Object> values) {
	// return join(values, ", ");
	// }
	//
	// public static Object toPrimitiveArray(Vector<Object> values, Class<?> ctype) throws DataNotCompatibleException {
	// Object result = null;
	// int size = values.size();
	// if (ctype == Boolean.TYPE) {
	// boolean[] outcome = new boolean[size];
	// // TODO NTF - should allow for String fields that are binary sequences: "1001001" (SOS)
	// for (int i = 0; i < size; i++) {
	// Object o = values.get(i);
	// outcome[i] = toBoolean(o);
	// }
	// result = outcome;
	// } else if (ctype == Byte.TYPE) {
	// byte[] outcome = new byte[size];
	// // TODO
	// result = outcome;
	// } else if (ctype == Character.TYPE) {
	// char[] outcome = new char[size];
	// // TODO How should this work? Just concatenate the char arrays for each String?
	// result = outcome;
	// } else if (ctype == Short.TYPE) {
	// short[] outcome = new short[size];
	// for (int i = 0; i < size; i++) {
	// Object o = values.get(i);
	// outcome[i] = toShort(o);
	// }
	// result = outcome;
	// } else if (ctype == Integer.TYPE) {
	// int[] outcome = new int[size];
	// for (int i = 0; i < size; i++) {
	// Object o = values.get(i);
	// outcome[i] = toInt(o);
	// }
	// result = outcome;
	// } else if (ctype == Long.TYPE) {
	// long[] outcome = new long[size];
	// for (int i = 0; i < size; i++) {
	// Object o = values.get(i);
	// outcome[i] = toLong(o);
	// }
	// result = outcome;
	// } else if (ctype == Float.TYPE) {
	// float[] outcome = new float[size];
	// for (int i = 0; i < size; i++) {
	// Object o = values.get(i);
	// outcome[i] = toFloat(o);
	// }
	// result = outcome;
	// } else if (ctype == Double.TYPE) {
	// double[] outcome = new double[size];
	// for (int i = 0; i < size; i++) {
	// Object o = values.get(i);
	// outcome[i] = toDouble(o);
	// }
	// result = outcome;
	// }
	// return result;
	// }
	//
	// public static Date toDate(Object value) throws DataNotCompatibleException {
	// if (value == null)
	// return null;
	// if (value instanceof Long) {
	// return new Date(((Long) value).longValue());
	// } else if (value instanceof String) {
	// // TODO finish
	// DateFormat df = new SimpleDateFormat();
	// try {
	// return df.parse((String) value);
	// } catch (ParseException e) {
	// throw new DataNotCompatibleException("Cannot create a Date from String value " + (String) value);
	// }
	// } else if (value instanceof lotus.domino.DateTime) {
	// return DominoUtils.toJavaDateSafe((lotus.domino.DateTime) value);
	// } else {
	// throw new DataNotCompatibleException("Cannot create a Date from a " + value.getClass().getName());
	// }
	// }
	//
	// public static Date[] toDates(Collection<Object> vector) throws DataNotCompatibleException {
	// if (vector == null)
	// return null;
	//
	// Date[] result = new Date[vector.size()];
	// int i = 0;
	// for (Object o : vector) {
	// result[i++] = toDate(o);
	// }
	// return result;
	// }
	//
	// public static org.openntf.domino.DateTime[] toDateTimes(Collection<Object> vector, org.openntf.domino.Session session)
	// throws DataNotCompatibleException {
	// if (vector == null)
	// return null;
	//
	// org.openntf.domino.DateTime[] result = new org.openntf.domino.DateTime[vector.size()];
	// int i = 0;
	// for (Object o : vector) {
	// result[i++] = session.createDateTime(toDate(o));
	// }
	// return result;
	// }
	//
	// public static org.openntf.domino.Name[] toNames(Collection<Object> vector, org.openntf.domino.Session session)
	// throws DataNotCompatibleException {
	// if (vector == null)
	// return null;
	//
	// org.openntf.domino.Name[] result = new org.openntf.domino.Name[vector.size()];
	// int i = 0;
	// for (Object o : vector) {
	// result[i++] = session.createName(String.valueOf(o));
	// }
	// return result;
	// }
	//
	// public static String[] toStrings(Collection<Object> vector) throws DataNotCompatibleException {
	// if (vector == null)
	// return null;
	// String[] strings = new String[vector.size()];
	// int i = 0;
	// for (Object o : vector) {
	// if (o instanceof DateTime) {
	// strings[i++] = ((DateTime) o).getGMTTime();
	// } else {
	// strings[i++] = String.valueOf(o);
	// }
	// }
	// return strings;
	// }

	/**
	 * To lotus note collection.
	 *
	 * @param collection
	 *            the collection
	 * @return the org.openntf.domino. note collection
	 *
	 * @deprecated this should be moved to {@link CollectionUtils}
	 */
	@Deprecated
	public static org.openntf.domino.NoteCollection toNoteCollection(final lotus.domino.DocumentCollection collection) {
		org.openntf.domino.NoteCollection result = null;
		if (collection instanceof DocumentCollection) {
			org.openntf.domino.Database db = ((DocumentCollection) collection).getParent();
			result = db.createNoteCollection(false);
			result.add(collection);
		} else {
			throw new DataNotCompatibleException("Cannot convert a non-OpenNTF DocumentCollection to a NoteCollection");
		}
		return result;
	}

	/**
	 * Add a hook that will run on the next "terminate" call
	 *
	 * @param hook
	 *            the hook that should run on next terminate
	 *
	 */
	public static void addTerminateHook(final Runnable hook, final boolean global) {
		if (global) {
			globalTerminateHooks.add(hook);
		} else {
			getThreadVariables().addTerminateHook(hook);
		}
	}

	public static void removeTerminateHook(final Runnable hook, final boolean global) {
		if (global) {
			globalTerminateHooks.remove(hook);
		} else {
			getThreadVariables().removeTerminateHook(hook);
		}
	}

	/**
	 * Add a hook that will run on shutdown
	 */
	public static void addShutdownHook(final Runnable hook) {
		shutdownHooks.add(hook);
	}

	/**
	 * Remove a shutdown hook
	 *
	 * @param hook
	 *            the hook that should be removed
	 */
	public static void removeShutdownHook(final Runnable hook) {
		shutdownHooks.remove(hook);
	}

	public static String getLocalServerName() {
		return localServerName;
	}

	public static void println(String prefix, final String lines) {
		BufferedReader reader = new BufferedReader(new StringReader(lines));
		String line;
		try {
			if (Strings.isBlankString(prefix)) {
				prefix = "[ODA] ";
			} else {
				prefix = "[ODA::" + prefix + "] ";
			}
			while ((line = reader.readLine()) != null) {
				if (line.length() > 0) {
					printer.println(prefix + line);
				}
			}
		} catch (IOException ioex) {

		}
	}

	public static void println(final Object x) {
		println(null, String.valueOf(x));
	}

	public static void println(final Object source, final Object x) {
		if (source == null) {
			println(null, String.valueOf(x));
		} else {
			String prefix;
			if (source instanceof String) {
				prefix = (String) source;
			} else if (source instanceof Class) {
				prefix = ((Class<?>) source).getSimpleName();
			} else {
				prefix = source.getClass().getSimpleName();
			}
			println(prefix, String.valueOf(x));
		}
	}

}

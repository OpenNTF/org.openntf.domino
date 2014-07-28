/*
 * Copyright 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package org.openntf.domino.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.ServiceLoader;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.openntf.domino.Base;
import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.Session;
import org.openntf.domino.Session.RunContext;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.exceptions.DataNotCompatibleException;
import org.openntf.domino.exceptions.UndefinedDelegateTypeException;
import org.openntf.domino.graph.DominoGraph;
import org.openntf.domino.logging.ConsoleFormatter;
import org.openntf.domino.logging.DefaultConsoleHandler;
import org.openntf.domino.logging.DefaultFileHandler;
import org.openntf.domino.logging.FileFormatter;
import org.openntf.domino.logging.OpenLogHandler;
import org.openntf.domino.types.DatabaseDescendant;
import org.openntf.domino.types.FactorySchema;
import org.openntf.domino.types.SessionDescendant;

/**
 * The Enum Factory. Does the Mapping lotusObject <=> OpenNTF-Object
 */
public enum Factory {
	;

	/**
	 * Holder for the wrapper-factory that converts lotus.domino objects to org.openntf.domino objects
	 */
	private static ThreadLocal<WrapperFactory> currentWrapperFactory = new ThreadLocal<WrapperFactory>();

	private static ThreadLocal<ClassLoader> currentClassLoader_ = new ThreadLocal<ClassLoader>();

	private static ThreadLocal<Session> currentSessionHolder_ = new ThreadLocal<Session>();

	private static List<Terminatable> onTerminate_ = new ArrayList<Terminatable>();

	// TODO: Determine if this is the right way to deal with Xots access to faces contexts
	private static ThreadLocal<Database> currentDatabaseHolder_ = new ThreadLocal<Database>();

	/**
	 * setup the environment and loggers
	 * 
	 * @author praml
	 * 
	 */
	private static class SetupJob implements Runnable {
		@Override
		public void run() {
			try {
				AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
					@Override
					public Object run() throws Exception {
						// Windows stores the notes.ini in the program directory; Linux stores it in the data directory
						String progpath = "notes.binary";
						File iniFile = new File(progpath + System.getProperty("file.separator") + "notes.ini");
						if (!iniFile.exists()) {
							progpath = System.getProperty("user.dir");
							iniFile = new File(progpath + System.getProperty("file.separator") + "notes.ini");
						}
						Scanner scanner = new Scanner(iniFile);
						scanner.useDelimiter("\n|\r\n");
						loadEnvironment(scanner);
						scanner.close();
						return null;
					}
				});
			} catch (AccessControlException e) {
				e.printStackTrace();
			} catch (PrivilegedActionException e) {
				e.printStackTrace();
			}

			try {
				AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
					@Override
					// TODO RPr: read config from notes.ini / env
					public Object run() throws Exception {
						String pattern = getDataPath() + "/IBM_TECHNICAL_SUPPORT/org.openntf.%u.%g.log";
						Logger oodLogger = Logger.getLogger("org.openntf.domino");
						oodLogger.setLevel(Level.WARNING);

						DefaultFileHandler dfh = new DefaultFileHandler(pattern, 50000, 100, true);
						dfh.setFormatter(new FileFormatter());
						dfh.setLevel(Level.WARNING);
						oodLogger.addHandler(dfh);

						DefaultConsoleHandler dch = new DefaultConsoleHandler();
						dch.setFormatter(new ConsoleFormatter());
						dch.setLevel(Level.WARNING);
						oodLogger.addHandler(dch);

						OpenLogHandler olh = new OpenLogHandler();
						olh.setLogDbPath("OpenLog.nsf");
						olh.setLevel(Level.WARNING);
						oodLogger.addHandler(olh);

						LogManager manager = LogManager.getLogManager();
						manager.addLogger(oodLogger);
						return null;
					}
				});
			} catch (AccessControlException e) {
				e.printStackTrace();
			} catch (PrivilegedActionException e) {
				e.printStackTrace();
			}
		}
	}

	static {
		SetupJob job = new SetupJob();
		job.run();
		//		TrustedDispatcher td = new TrustedDispatcher();
		//		td.process(job);
		//		System.out.println("DEBUG: SetupJob dispatched");
		//		td.stop(false);
	}

	private static Map<String, String> ENVIRONMENT;
	private static boolean session_init = false;
	private static boolean jar_init = false;

	/**
	 * load the configuration
	 * 
	 * @param session
	 */
	public static void loadEnvironment(/*final lotus.domino.Session session, */final Scanner scanner) {
		if (ENVIRONMENT == null) {
			ENVIRONMENT = new HashMap<String, String>();
		}
		int keyCount = 0;
		if (scanner != null) {
			while (scanner.hasNextLine()) {
				String nextLine = scanner.nextLine();
				int i = nextLine.indexOf('=');
				if (i > 0) {
					keyCount++;
					String key = nextLine.substring(0, i).toLowerCase();
					String value = nextLine.substring(i + 1);
					//					System.out.println("DEBUG " + key + " : " + value);
					ENVIRONMENT.put(key, value);
				}
			}
			//			System.out.println("DEBUG: Added " + keyCount + " environment variables to avoid using a session");
			session_init = true;
		}
		//		if (session != null && !session_init) {
		//			try {
		//				AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
		//					@Override
		//					public Object run() throws Exception {
		//						try {
		//							ENVIRONMENT.put("directory", session.getEnvironmentString("Directory", true));
		//							ENVIRONMENT.put("notesprogram", session.getEnvironmentString("NotesProgram", true));
		//							ENVIRONMENT.put("kittype", session.getEnvironmentString("KitType", true));
		//							ENVIRONMENT.put("servicename", session.getEnvironmentString("ServiceName", true));
		//							ENVIRONMENT.put("httpjvmmaxheapsize", session.getEnvironmentString("HTTPJVMMaxHeapSize", true));
		//							ENVIRONMENT.put("dominocontrollercurrentlog", session.getEnvironmentString("DominoControllerCurrentLog", true));
		//						} catch (NotesException ne) {
		//							ne.printStackTrace();
		//						}
		//						return null;
		//					}
		//				});
		//			} catch (AccessControlException e) {
		//				e.printStackTrace();
		//			} catch (PrivilegedActionException e) {
		//				e.printStackTrace();
		//			}
		//			session_init = true;
		//		}
		if (!jar_init) {
			try {
				AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
					@Override
					public Object run() throws Exception {
						try {
							InputStream inputStream = Factory.class.getClassLoader().getResourceAsStream("META-INF/MANIFEST.MF");
							if (inputStream != null) {
								Manifest mani;
								mani = new Manifest(inputStream);
								Attributes attrib = mani.getMainAttributes();
								ENVIRONMENT.put("version", attrib.getValue("Implementation-Version"));
								ENVIRONMENT.put("title", attrib.getValue("Implementation-Title"));
								ENVIRONMENT.put("url", attrib.getValue("Implementation-Vendor-URL"));
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
						return null;
					}
				});
			} catch (AccessControlException e) {
				e.printStackTrace();
			} catch (PrivilegedActionException e) {
				e.printStackTrace();
			}
			jar_init = true;
		}
	}

	public static String getEnvironment(final String key) {
		if (ENVIRONMENT == null) {
			loadEnvironment(null);
		}
		return ENVIRONMENT.get(key);
	}

	public static String getTitle() {
		return getEnvironment("title");
	}

	public static String getUrl() {
		return getEnvironment("url");
	}

	public static String getVersion() {
		return getEnvironment("version");
	}

	public static String getDataPath() {
		return getEnvironment("directory");
	}

	public static String getProgramPath() {
		return getEnvironment("notesprogram");
	}

	public static String getHTTPJVMHeapSize() {
		return getEnvironment("httpjvmheapsize");
	}

	/** The Constant log_. */
	private static final Logger log_ = Logger.getLogger(Factory.class.getName());

	/** The Constant TRACE_COUNTERS. */
	private static final boolean TRACE_COUNTERS = false;
	/** use a separate counter in each thread */
	private static final boolean COUNT_PER_THREAD = false;

	/** The lotus counter. */
	private static Counter lotusCounter = new Counter(COUNT_PER_THREAD);

	/** The recycle err counter. */
	private static Counter recycleErrCounter = new Counter(COUNT_PER_THREAD);

	/** The auto recycle counter. */
	private static Counter autoRecycleCounter = new Counter(COUNT_PER_THREAD);

	/** The manual recycle counter. */
	private static Counter manualRecycleCounter = new Counter(COUNT_PER_THREAD);

	private static Map<Class<?>, Counter> objectCounter = new ConcurrentHashMap<Class<?>, Counter>() {
		private static final long serialVersionUID = 1L;

		/* (non-Javadoc)
		 * @see java.util.concurrent.ConcurrentHashMap#get(java.lang.Object)
		 */
		@Override
		public Counter get(final Object key) {
			// TODO Auto-generated method stub
			Counter ret = super.get(key);
			if (ret == null) {
				ret = new Counter(COUNT_PER_THREAD);
				put((Class<?>) key, ret);
			}
			return ret;
		}

	};

	/**
	 * Gets the lotus count.
	 * 
	 * @return the lotus count
	 */
	public static int getLotusCount() {
		return lotusCounter.intValue();
	}

	/**
	 * Count a created lotus element.
	 */
	public static void countLotus(final Class<?> c) {
		if (TRACE_COUNTERS) {
			lotusCounter.increment();
			objectCounter.get(c).increment();
		}
	}

	/**
	 * Gets the recycle error count.
	 * 
	 * @return the recycle error count
	 */
	public static int getRecycleErrorCount() {
		return recycleErrCounter.intValue();
	}

	/**
	 * Count recycle error.
	 */
	public static void countRecycleError(final Class<?> c) {
		if (TRACE_COUNTERS)
			recycleErrCounter.increment();
	}

	/**
	 * Gets the auto recycle count.
	 * 
	 * @return the auto recycle count
	 */
	public static int getAutoRecycleCount() {
		return autoRecycleCounter.intValue();
	}

	/**
	 * Count auto recycle.
	 * 
	 * @return the int
	 */
	public static int countAutoRecycle(final Class<?> c) {
		if (TRACE_COUNTERS) {
			objectCounter.get(c).decrement();
			return autoRecycleCounter.increment();
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
		return manualRecycleCounter.intValue();
	}

	/**
	 * Count a manual recycle
	 */
	public static int countManualRecycle(final Class<?> c) {
		if (TRACE_COUNTERS) {
			objectCounter.get(c).decrement();
			return manualRecycleCounter.increment();
		} else {
			return 0;
		}
	}

	/**
	 * get the active object count
	 * 
	 * @return
	 */
	public static int getActiveObjectCount() {
		return lotusCounter.intValue() - autoRecycleCounter.intValue() - manualRecycleCounter.intValue();
	}

	/**
	 * Determine the run context where we are
	 * 
	 * @return
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
		if (sm == null)
			return RunContext.CLI;

		Object o = sm.getSecurityContext();
		if (log_.isLoggable(Level.INFO))
			log_.log(Level.INFO, "SecurityManager is " + sm.getClass().getName() + " and context is " + o.getClass().getName());
		if (sm instanceof lotus.notes.AgentSecurityManager) {
			lotus.notes.AgentSecurityManager asm = (lotus.notes.AgentSecurityManager) sm;
			Object xsm = asm.getExtenderSecurityContext();
			if (xsm instanceof lotus.notes.AgentSecurityContext) {
				lotus.notes.AgentSecurityContext nasc = (lotus.notes.AgentSecurityContext) xsm;
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
				ClassLoader cl = (ClassLoader) BCLClass.getMethod("getSharedClassLoader", null).invoke(null, null);
				if ("com.ibm.domino.http.bootstrap.BootstrapOSGIClassLoader".equals(cl.getClass().getName())) {
					result = RunContext.XPAGES_OSGI;
				}
			}
		} catch (Exception e) {

		}

		return result;
	}

	/**
	 * returns the wrapper factory for this thread
	 * 
	 * @return the thread's wrapper factory
	 */
	public static WrapperFactory getWrapperFactory() {
		WrapperFactory wf = currentWrapperFactory.get();
		if (wf == null) {
			ClassLoader cl = getClassLoader();
			// System.out.println("Got this Classloader: " + cl);

			if (cl != null) {
				try {
					ServiceLoader<WrapperFactory> loader = ServiceLoader.load(WrapperFactory.class, cl);
					Iterator<WrapperFactory> it = loader.iterator();
					if (it.hasNext()) {
						wf = it.next();
						// TODO RPr remove these debug prints
						System.out.println("DEBUG: Using alternative WrapperFactory: " + wf);
					}
				} catch (Throwable t) {
					DominoUtils.handleException(t);
				}
			}
			if (wf == null) {
				// System.out.println("Using default WrapperFactory");
				wf = new org.openntf.domino.impl.WrapperFactory();
			}
			currentWrapperFactory.set(wf);
		}
		return wf;
	}

	/**
	 * Set/changes the wrapperFactory for this thread
	 * 
	 * @param wf
	 */
	public static void setWrapperFactory(final WrapperFactory wf) {
		currentWrapperFactory.set(wf);
	}

	// --- session handling 

	@Deprecated
	public static org.openntf.domino.Document fromLotusDocument(final lotus.domino.Document lotus, final Base parent) {
		return getWrapperFactory().fromLotus(lotus, Document.SCHEMA, (Database) parent);
	}

	public static void setNoRecycle(final Base<?> base, final boolean value) {
		getWrapperFactory().setNoRecycle(base, value);
	}

	/*
	 * (non-JavaDoc)
	 * 
	 * @see org.openntf.domino.WrapperFactory#fromLotus(lotus.domino.Base, FactorySchema, Base)
	 */
	@SuppressWarnings("rawtypes")
	public static <T extends Base, D extends lotus.domino.Base, P extends Base> T fromLotus(final D lotus,
			final FactorySchema<T, D, P> schema, final P parent) {
		return getWrapperFactory().fromLotus(lotus, schema, parent);
	}

	public static boolean recacheLotus(final lotus.domino.Base lotus, final Base<?> wrapper, final Base<?> parent) {
		return getWrapperFactory().recacheLotusObject(lotus, wrapper, parent);
	}

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
	 */
	@SuppressWarnings({ "rawtypes" })
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
	 */
	@SuppressWarnings("rawtypes")
	public static <T extends Base, D extends lotus.domino.Base, P extends Base> Vector<T> fromLotusAsVector(final Collection<?> lotusColl,
			final FactorySchema<T, D, P> schema, final P parent) {
		return getWrapperFactory().fromLotusAsVector(lotusColl, schema, parent);
	}

	/**
	 * From lotus.
	 * 
	 * @deprecated Use {@link #fromLotus(lotus.domino.Base, FactorySchema, Base)} instead
	 * 
	 * 
	 * @param <T>
	 *            the generic type
	 * @param lotus
	 *            the lotus
	 * @param T
	 *            the t
	 * @param parent
	 *            the parent
	 * @return the t
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Deprecated
	public static <T> T fromLotus(final lotus.domino.Base lotus, final Class<? extends Base> T, final Base parent) {
		return (T) getWrapperFactory().fromLotus(lotus, (FactorySchema) null, parent);
	}

	/**
	 * From lotus.
	 * 
	 * @deprecated Use {@link #fromLotus(Collection, FactorySchema, Base)} instead
	 * 
	 * @param <T>
	 *            the generic type
	 * @param lotusColl
	 *            the lotus coll
	 * @param T
	 *            the t
	 * @param parent
	 *            the parent
	 * @return the collection
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Deprecated
	public static <T> Collection<T> fromLotus(final Collection<?> lotusColl, final Class<? extends Base> T, final Base<?> parent) {
		return getWrapperFactory().fromLotus(lotusColl, (FactorySchema) null, parent);
	}

	/**
	 * @deprecated Use {@link #fromLotusAsVector(Collection, FactorySchema, Base)}
	 * @param lotusColl
	 * @param T
	 * @param parent
	 * @return
	 */
	@Deprecated
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> Vector<T> fromLotusAsVector(final Collection<?> lotusColl, final Class<? extends org.openntf.domino.Base> T,
			final org.openntf.domino.Base<?> parent) {
		return getWrapperFactory().fromLotusAsVector(lotusColl, (FactorySchema) null, parent);
	}

	/**
	 * Wrap column values.
	 * 
	 * @param values
	 *            the values
	 * @return the java.util. vector
	 */
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
	 */
	public static <T extends lotus.domino.Base> T toLotus(final T base) {
		return getWrapperFactory().toLotus(base);
	}

	/**
	 * Gets the session.
	 * 
	 * @return the session
	 */
	public static org.openntf.domino.Session getSession() {
		org.openntf.domino.Session result = currentSessionHolder_.get();
		if (result == null) {
			try {
				result = Factory.fromLotus(lotus.domino.NotesFactory.createSession(), Session.SCHEMA, null);
				Factory.setNoRecycle(result, false);  // We have created the session, so we recycle it
			} catch (Exception ne) {
				try {
					result = XSPUtil.getCurrentSession();
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			setSession(result);
		}
		if (result == null) {
			System.out
					.println("SEVERE: Unable to get default session. This probably means that you are running in an unsupported configuration or you forgot to set up your context at the start of the operation. If you're running in XPages, check the xsp.properties of your database. If you are running in an Agent, make sure you start with a call to Factory.fromLotus() and pass in your lotus.domino.Session");
			Throwable t = new Throwable();
			t.printStackTrace();
		}
		return result;
	}

	public static org.openntf.domino.Session getSession_unchecked() {
		return currentSessionHolder_.get();
	}

	public static void setSession(final lotus.domino.Session session) {
		currentSessionHolder_.set(fromLotus(session, Session.SCHEMA, null));
	}

	public static void clearSession() {
		currentSessionHolder_.set(null);
	}

	// TODO: Determine if this is the right way to deal with Xots access to faces contexts
	public static Database getDatabase_unchecked() {
		return currentDatabaseHolder_.get();
	}

	public static void setDatabase(final Database database) {
		setNoRecycle(database, true);
		currentDatabaseHolder_.set(database);
	}

	public static void clearDatabase() {
		currentDatabaseHolder_.set(null);
	}

	public static ClassLoader getClassLoader() {
		if (currentClassLoader_.get() == null) {
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
		return currentClassLoader_.get();
	}

	public static void setClassLoader(final ClassLoader loader) {
		if (loader != null) {
			//			System.out.println("Setting OpenNTF Factory ClassLoader to a " + loader.getClass().getName());
		}
		currentClassLoader_.set(loader);
	}

	public static void clearWrapperFactory() {
		currentWrapperFactory.set(null);
	}

	public static void clearClassLoader() {
		currentClassLoader_.set(null);
	}

	public static void clearDominoGraph() {
		DominoGraph.clearDocumentCache();
	}

	public static void clearBubbleExceptions() {
		DominoUtils.setBubbleExceptions(null);
	}

	/**
	 * Begin with a clear environment
	 */
	public static void init() {
		// TODO Auto-generated method stub

	}

	public static lotus.domino.Session terminate() {
		lotus.domino.Session result = null;
		WrapperFactory wf = getWrapperFactory();
		if (currentSessionHolder_.get() != null) {
			result = wf.toLotus(currentSessionHolder_.get());
		}
		for (Terminatable callback : onTerminate_) {
			callback.terminate();
		}
		clearSession();
		long termCount = wf.terminate();
		//		System.out.println("DEBUG: cleared " + termCount + " references from the queue...");
		clearBubbleExceptions();
		clearDominoGraph();
		clearWrapperFactory();
		clearClassLoader();
		return result;
	}

	/**
	 * Debug method to get statistics
	 * 
	 * @param details
	 * @return
	 */
	public static String dumpCounters(final boolean details) {
		if (!TRACE_COUNTERS)
			return "Counters are disabled";
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

		if (!objectCounter.isEmpty() && details) {
			sb.append("\n=== The following objects were left in memory ===");
			for (Entry<Class<?>, Counter> e : objectCounter.entrySet()) {
				int i = e.getValue().intValue();
				if (i != 0) {
					sb.append("\n" + i + "\t" + e.getKey().getName());
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Gets the session full access.
	 * 
	 * @return the session full access
	 */
	public static org.openntf.domino.Session getSessionFullAccess() {
		try {
			Object result = AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
				@Override
				public Object run() throws Exception {
					lotus.domino.Session s = lotus.domino.NotesFactory.createSessionWithFullAccess();
					return fromLotus(s, org.openntf.domino.Session.SCHEMA, null);
				}
			});
			if (result instanceof org.openntf.domino.Session) {
				Factory.setNoRecycle((org.openntf.domino.Session) result, false); // We have created the session, so we recycle it
				return (org.openntf.domino.Session) result;
			}
		} catch (PrivilegedActionException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/**
	 * Gets the trusted session.
	 * 
	 * @return the trusted session
	 */
	public static org.openntf.domino.Session getTrustedSession() {
		try {
			Object result = AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
				@Override
				public Object run() throws Exception {
					lotus.domino.Session s = lotus.domino.NotesFactory.createTrustedSession();
					return fromLotus(s, org.openntf.domino.Session.SCHEMA, null);
				}
			});
			if (result instanceof org.openntf.domino.Session) {
				Factory.setNoRecycle((org.openntf.domino.Session) result, false); // We have created the session, so we are responsible to recycle it.
				return (org.openntf.domino.Session) result;
			}
		} catch (PrivilegedActionException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/**
	 * Gets the parent database.
	 * 
	 * @param base
	 *            the base
	 * @return the parent database
	 */
	@Deprecated
	public static Database getParentDatabase(final Base<?> base) {
		if (base instanceof org.openntf.domino.Database) {
			return (org.openntf.domino.Database) base;
		} else if (base instanceof DatabaseDescendant) {
			return ((DatabaseDescendant) base).getAncestorDatabase();
		} else {
			throw new UndefinedDelegateTypeException();
		}
	}

	/**
	 * Gets the session.
	 * 
	 * @param base
	 *            the base
	 * @return the session
	 */
	public static Session getSession(final lotus.domino.Base base) {
		org.openntf.domino.Session result = null;
		if (base instanceof SessionDescendant) {
			result = ((SessionDescendant) base).getAncestorSession();
		} else if (base instanceof org.openntf.domino.Session) {
			result = (org.openntf.domino.Session) base;
		} else {
			System.out.println("couldn't find session for object of type " + base.getClass().getName());
			throw new UndefinedDelegateTypeException();
		}
		if (result == null)
			result = getSession(); // last ditch, get the primary Session;
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
	 */
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
	 * This will call the terminate-function of the callback on every "terminate" call. (Across threads!) The callback must handle this with
	 * threadlocals itself. See DateTime for an example
	 * 
	 * @param callback
	 */
	public static void onTerminate(final Terminatable callback) {
		onTerminate_.add(callback);
	}

}

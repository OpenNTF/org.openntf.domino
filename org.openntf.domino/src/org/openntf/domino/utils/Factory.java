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

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import lotus.domino.NotesException;
import lotus.domino.NotesThread;

import org.openntf.domino.Mapper;
import org.openntf.domino.Session.RunContext;
import org.openntf.domino.exceptions.DataNotCompatibleException;
import org.openntf.domino.exceptions.UndefinedDelegateTypeException;
import org.openntf.domino.graph.DominoGraph;
import org.openntf.domino.impl.Base;
import org.openntf.domino.impl.DocumentCollection;
import org.openntf.domino.impl.Session;
import org.openntf.domino.logging.LogSetupRunnable;
import org.openntf.domino.types.DatabaseDescendant;
import org.openntf.domino.types.SessionDescendant;

// TODO: Auto-generated Javadoc
/**
 * The Enum Factory.
 */
public enum Factory {
	;

	static {
		NotesThread nt = new NotesThread(new LogSetupRunnable());
		nt.start();
	}

	public static final String VERSION = "Milestone4";

	private static ThreadLocal<Session> currentSessionHolder_ = new ThreadLocal<Session>() {
		@Override
		protected Session initialValue() {
			return super.initialValue();
		}

	};

	private static ThreadLocal<ClassLoader> currentClassLoader_ = new ThreadLocal<ClassLoader>() {
		@Override
		protected ClassLoader initialValue() {
			return super.initialValue();
		}

	};

	private static ThreadLocal<Mapper> mapper_ = new ThreadLocal<Mapper>() {
		@Override
		protected Mapper initialValue() {
			return super.initialValue();
		}
	};

	/** The Constant log_. */
	private static final Logger log_ = Logger.getLogger(Factory.class.getName());

	/** The Constant TRACE_COUNTERS. */
	private static final boolean TRACE_COUNTERS = true;

	/**
	 * The Class Counter.
	 */
	static class Counter extends ThreadLocal<Integer> {
		// TODO NTF - I'm open to a faster implementation of this. Maybe a mutable int of some kind?
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.ThreadLocal#initialValue()
		 */
		@Override
		protected Integer initialValue() {
			return Integer.valueOf(0);
		}

		/**
		 * Increment.
		 * 
		 * @return the int
		 */
		public int increment() {
			int result = get() + 1;
			set(result);
			return result;
		}

		/**
		 * Decrement.
		 * 
		 * @return the int
		 */
		public int decrement() {
			int result = get() - 1;
			set(result);
			return result;
		}
	};

	/** The lotus counter. */
	private static Counter lotusCounter = new Counter();

	/** The recycle err counter. */
	private static Counter recycleErrCounter = new Counter();

	/** The auto recycle counter. */
	private static Counter autoRecycleCounter = new Counter();

	/**
	 * Gets the lotus count.
	 * 
	 * @return the lotus count
	 */
	public static int getLotusCount() {
		return lotusCounter.get().intValue();
	}

	/**
	 * Count recycle error.
	 */
	public static void countRecycleError() {
		if (TRACE_COUNTERS)
			recycleErrCounter.increment();
	}

	/**
	 * Count auto recycle.
	 * 
	 * @return the int
	 */
	public static int countAutoRecycle() {
		if (TRACE_COUNTERS) {
			return autoRecycleCounter.increment();
		} else {
			return 0;
		}
	}

	/**
	 * Gets the auto recycle count.
	 * 
	 * @return the auto recycle count
	 */
	public static int getAutoRecycleCount() {
		return autoRecycleCounter.get().intValue();
	}

	/**
	 * Gets the recycle error count.
	 * 
	 * @return the recycle error count
	 */
	public static int getRecycleErrorCount() {
		return recycleErrCounter.get().intValue();
	}

	public static RunContext getRunContext() {
		// TODO finish this implementation, which needs a lot of work.
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
		com.ibm.domino.http.bootstrap.logger.RCPLoggerConfig rcplc;
		ClassLoader cl = com.ibm.domino.http.bootstrap.BootstrapClassLoader.getSharedClassLoader();
		if (cl instanceof com.ibm.domino.http.bootstrap.BootstrapOSGIClassLoader) {
			com.ibm.domino.http.bootstrap.BootstrapOSGIClassLoader bocl = (com.ibm.domino.http.bootstrap.BootstrapOSGIClassLoader) cl;
			result = RunContext.XPAGES_OSGI;
		}

		return result;
	}

	/**
	 * From lotus.
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
	public static <T> T fromLotus(final lotus.domino.Base lotus, final Class<? extends org.openntf.domino.Base> T,
			final org.openntf.domino.Base parent) {
		if (lotus == null) {
			return null;
		}
		if (lotus instanceof org.openntf.domino.Base) {
			if (log_.isLoggable(Level.FINE))
				log_.log(Level.FINE, "Returning an already OpenNTF object...");
			return (T) lotus;
		}
		if (T.isAssignableFrom(lotus.getClass())) {
			if (log_.isLoggable(Level.FINE))
				log_.log(Level.FINE, "Returning an assignable object....");
			return (T) lotus;
		}

		T result = null;
		if (lotus instanceof lotus.domino.ACL) {
			result = (T) new org.openntf.domino.impl.ACL((lotus.domino.ACL) lotus, parent);
		} else if (lotus instanceof lotus.domino.ACLEntry) {
			result = (T) new org.openntf.domino.impl.ACLEntry((lotus.domino.ACLEntry) lotus, (org.openntf.domino.ACL) parent);
		} else if (lotus instanceof lotus.domino.AdministrationProcess) {
			result = (T) new org.openntf.domino.impl.AdministrationProcess((lotus.domino.AdministrationProcess) lotus, parent);
		} else if (lotus instanceof lotus.domino.Agent) {
			result = (T) new org.openntf.domino.impl.Agent((lotus.domino.Agent) lotus, parent);
		} else if (lotus instanceof lotus.domino.AgentContext) {
			result = (T) new org.openntf.domino.impl.AgentContext((lotus.domino.AgentContext) lotus, parent);
		} else if (lotus instanceof lotus.domino.ColorObject) {
			result = (T) new org.openntf.domino.impl.ColorObject((lotus.domino.ColorObject) lotus, parent);
		} else if (lotus instanceof lotus.domino.Database) {
			result = (T) new org.openntf.domino.impl.Database((lotus.domino.Database) lotus, parent);
		} else if (lotus instanceof lotus.domino.DateRange) {
			result = (T) new org.openntf.domino.impl.DateRange((lotus.domino.DateRange) lotus, parent);
		} else if (lotus instanceof lotus.domino.DateTime) {
			result = (T) new org.openntf.domino.impl.DateTime((lotus.domino.DateTime) lotus, parent);
		} else if (lotus instanceof lotus.domino.DbDirectory) {
			result = (T) new org.openntf.domino.impl.DbDirectory((lotus.domino.DbDirectory) lotus, parent);
		} else if (lotus instanceof lotus.domino.Directory) {
			result = (T) new org.openntf.domino.impl.Directory((lotus.domino.Directory) lotus, parent);
		} else if (lotus instanceof lotus.domino.DirectoryNavigator) {
			result = (T) new org.openntf.domino.impl.DirectoryNavigator((lotus.domino.DirectoryNavigator) lotus, parent);
		} else if (lotus instanceof lotus.domino.Document) {

			// 25.09.13/RPr: what do you think about this idea to pass every document to the database, so that the
			// mapper can decide how and which object to return
			Mapper mapper = getMapper();
			if (mapper != null) {
				result = (T) mapper.map((lotus.domino.Document) lotus, (org.openntf.domino.Database) parent);
				if (result != null) {
					// TODO: What to do if mapper does not map
					return result;
				}
			}
			System.out.println("No 'org.openntf.domino.mapper' service mapped the document");
			result = (T) new org.openntf.domino.impl.Document((lotus.domino.Document) lotus, parent);

		} else if (lotus instanceof lotus.domino.DocumentCollection) {
			result = (T) new org.openntf.domino.impl.DocumentCollection((lotus.domino.DocumentCollection) lotus, parent);
		} else if (lotus instanceof lotus.domino.DxlExporter) {
			result = (T) new org.openntf.domino.impl.DxlExporter((lotus.domino.DxlExporter) lotus, parent);
		} else if (lotus instanceof lotus.domino.DxlImporter) {
			result = (T) new org.openntf.domino.impl.DxlImporter((lotus.domino.DxlImporter) lotus, parent);
		} else if (lotus instanceof lotus.domino.EmbeddedObject) {
			result = (T) new org.openntf.domino.impl.EmbeddedObject((lotus.domino.EmbeddedObject) lotus, parent);
		} else if (lotus instanceof lotus.domino.Form) {
			result = (T) new org.openntf.domino.impl.Form((lotus.domino.Form) lotus, parent);
		} else if (lotus instanceof lotus.domino.International) {
			result = (T) new org.openntf.domino.impl.International((lotus.domino.International) lotus, parent);
		} else if (lotus instanceof lotus.domino.Item) {
			if (lotus instanceof lotus.domino.RichTextItem) {
				result = (T) new org.openntf.domino.impl.RichTextItem((lotus.domino.RichTextItem) lotus, parent);
			} else {
				result = (T) new org.openntf.domino.impl.Item((lotus.domino.Item) lotus, parent);
			}
		} else if (lotus instanceof lotus.domino.Log) {
			result = (T) new org.openntf.domino.impl.Log((lotus.domino.Log) lotus, parent);
		} else if (lotus instanceof lotus.domino.MIMEEntity) {
			result = (T) new org.openntf.domino.impl.MIMEEntity((lotus.domino.MIMEEntity) lotus, parent);
		} else if (lotus instanceof lotus.domino.MIMEHeader) {
			result = (T) new org.openntf.domino.impl.MIMEHeader((lotus.domino.MIMEHeader) lotus, parent);
		} else if (lotus instanceof lotus.domino.Name) {
			result = (T) new org.openntf.domino.impl.Name((lotus.domino.Name) lotus, parent);
		} else if (lotus instanceof lotus.domino.Newsletter) {
			result = (T) new org.openntf.domino.impl.Newsletter((lotus.domino.Newsletter) lotus, parent);
		} else if (lotus instanceof lotus.domino.NoteCollection) {
			result = (T) new org.openntf.domino.impl.NoteCollection((lotus.domino.NoteCollection) lotus,
					(org.openntf.domino.Database) parent);
			/* not supported in 8.5.3			
					} else if (lotus instanceof lotus.domino.NotesCalendar) {
						result = (T) new org.openntf.domino.impl.NotesCalendar((lotus.domino.NotesCalendar) lotus, parent);
					} else if (lotus instanceof lotus.domino.NotesCalendarEntry) {
						result = (T) new org.openntf.domino.impl.NotesCalendarEntry((lotus.domino.NotesCalendarEntry) lotus, parent);
					} else if (lotus instanceof lotus.domino.NotesCalendarNotice) {
						result = (T) new org.openntf.domino.impl.NotesCalendarNotice((lotus.domino.NotesCalendarNotice) lotus, parent);
			*/
		} else if (lotus instanceof lotus.domino.NotesProperty) {
			result = (T) new org.openntf.domino.impl.NotesProperty((lotus.domino.NotesProperty) lotus, parent);
		} else if (lotus instanceof lotus.domino.Outline) {
			result = (T) new org.openntf.domino.impl.Outline((lotus.domino.Outline) lotus, parent);
		} else if (lotus instanceof lotus.domino.OutlineEntry) {
			result = (T) new org.openntf.domino.impl.OutlineEntry((lotus.domino.OutlineEntry) lotus, parent);
		} else if (lotus instanceof lotus.domino.PropertyBroker) {
			result = (T) new org.openntf.domino.impl.PropertyBroker((lotus.domino.PropertyBroker) lotus, parent);
		} else if (lotus instanceof lotus.domino.Registration) {
			result = (T) new org.openntf.domino.impl.Registration((lotus.domino.Registration) lotus, parent);
		} else if (lotus instanceof lotus.domino.Replication) {
			result = (T) new org.openntf.domino.impl.Replication((lotus.domino.Replication) lotus, parent);
		} else if (lotus instanceof lotus.domino.ReplicationEntry) {
			result = (T) new org.openntf.domino.impl.ReplicationEntry((lotus.domino.ReplicationEntry) lotus, parent);
		} else if (lotus instanceof lotus.domino.RichTextDoclink) {
			result = (T) new org.openntf.domino.impl.RichTextDoclink((lotus.domino.RichTextDoclink) lotus, parent);
		} else if (lotus instanceof lotus.domino.RichTextItem) {
			result = (T) new org.openntf.domino.impl.RichTextItem((lotus.domino.RichTextItem) lotus, parent);
		} else if (lotus instanceof lotus.domino.RichTextNavigator) {
			result = (T) new org.openntf.domino.impl.RichTextNavigator((lotus.domino.RichTextNavigator) lotus, parent);
		} else if (lotus instanceof lotus.domino.RichTextParagraphStyle) {
			result = (T) new org.openntf.domino.impl.RichTextParagraphStyle((lotus.domino.RichTextParagraphStyle) lotus, parent);
		} else if (lotus instanceof lotus.domino.RichTextRange) {
			result = (T) new org.openntf.domino.impl.RichTextRange((lotus.domino.RichTextRange) lotus, parent);
		} else if (lotus instanceof lotus.domino.RichTextSection) {
			result = (T) new org.openntf.domino.impl.RichTextSection((lotus.domino.RichTextSection) lotus, parent);
		} else if (lotus instanceof lotus.domino.RichTextStyle) {
			result = (T) new org.openntf.domino.impl.RichTextStyle((lotus.domino.RichTextStyle) lotus, parent);
		} else if (lotus instanceof lotus.domino.RichTextTab) {
			result = (T) new org.openntf.domino.impl.RichTextTab((lotus.domino.RichTextTab) lotus, parent);
		} else if (lotus instanceof lotus.domino.RichTextTable) {
			result = (T) new org.openntf.domino.impl.RichTextTable((lotus.domino.RichTextTable) lotus, parent);
		} else if (lotus instanceof lotus.domino.Session) {
			result = (T) new org.openntf.domino.impl.Session((lotus.domino.Session) lotus, parent);
			if (currentSessionHolder_.get() != null) {
				try {
					lotus.domino.Session rawSession = (lotus.domino.Session) Base.getDelegate(currentSessionHolder_.get());
					rawSession.isConvertMime();
				} catch (NotesException ne) {
					// System.out.println("Resetting default local session because we got an exception");
					setSession((org.openntf.domino.Session) result);
				}
			} else {
				// System.out.println("Resetting default local session because it was null");
				setSession((org.openntf.domino.Session) result);
			}
		} else if (lotus instanceof lotus.domino.Stream) {
			result = (T) new org.openntf.domino.impl.Stream((lotus.domino.Stream) lotus, parent);
		} else if (lotus instanceof lotus.domino.View) {
			result = (T) new org.openntf.domino.impl.View((lotus.domino.View) lotus, (org.openntf.domino.Database) parent);
		} else if (lotus instanceof lotus.domino.ViewColumn) {
			result = (T) new org.openntf.domino.impl.ViewColumn((lotus.domino.ViewColumn) lotus, (org.openntf.domino.View) parent);
		} else if (lotus instanceof lotus.domino.ViewEntry) {
			result = (T) new org.openntf.domino.impl.ViewEntry((lotus.domino.ViewEntry) lotus, parent);
		} else if (lotus instanceof lotus.domino.ViewEntryCollection) {
			result = (T) new org.openntf.domino.impl.ViewEntryCollection((lotus.domino.ViewEntryCollection) lotus,
					(org.openntf.domino.View) parent);
		} else if (lotus instanceof lotus.domino.ViewNavigator) {
			result = (T) new org.openntf.domino.impl.ViewNavigator((lotus.domino.ViewNavigator) lotus, (org.openntf.domino.View) parent);
		}

		if (result != null) {
			return result;
		}
		// if (TRACE_COUNTERS)
		// lotusCounter.increment();
		//
		throw new UndefinedDelegateTypeException();
	}

	/**
	 * From lotus.
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
	public static <T> Collection<T> fromLotus(final Collection<?> lotusColl, final Class<? extends org.openntf.domino.Base> T,
			final org.openntf.domino.Base<?> parent) {
		Collection<T> result = new ArrayList<T>(); // TODO anyone got a better implementation?
		if (!lotusColl.isEmpty()) {
			for (Object lotus : lotusColl) {
				if (lotus instanceof lotus.domino.Base) {
					result.add((T) fromLotus((lotus.domino.Base) lotus, T, parent));
				}
			}
		}
		return result;

	}

	/**
	 * From lotus as vector.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param lotusColl
	 *            the lotus coll
	 * @param T
	 *            the t
	 * @param parent
	 *            the parent
	 * @return the org.openntf.domino.impl. vector
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> org.openntf.domino.impl.Vector<T> fromLotusAsVector(final Collection<?> lotusColl,
			final Class<? extends org.openntf.domino.Base> T, final org.openntf.domino.Base<?> parent) {
		org.openntf.domino.impl.Vector<T> result = new org.openntf.domino.impl.Vector<T>(); // TODO anyone got a better implementation?
		if (!lotusColl.isEmpty()) {
			for (Object lotus : lotusColl) {
				if (lotus instanceof lotus.domino.local.NotesBase) {
					result.add((T) fromLotus((lotus.domino.Base) lotus, T, parent));
				}
			}
		}
		return result;

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
			return null;
		}
		int i = 0;
		java.util.Vector<Object> result = new org.openntf.domino.impl.Vector<Object>();
		for (Object value : values) {
			if (value == null) {
				result.add(null);
			} else if (value instanceof lotus.domino.DateTime) {
				Object wrapped = null;
				try {
					wrapped = fromLotus((lotus.domino.DateTime) value, org.openntf.domino.impl.DateTime.class, session);
				} catch (Throwable t) {
					System.out.println("Unable to wrap a DateTime found in Vector member " + i + " of " + values.size());
				}
				if (wrapped == null) {
					result.add("");
				} else {
					result.add(wrapped);
				}
			} else if (value instanceof lotus.domino.DateRange) {
				result.add(fromLotus((lotus.domino.DateRange) value, org.openntf.domino.impl.DateRange.class, session));
			} else if (value instanceof Collection) {
				result.add(wrapColumnValues((Collection<?>) value, session));
			} else {
				result.add(value);
			}
			i++;
		}
		return result;
	}

	/**
	 * Wrapped evaluate.
	 * 
	 * @param session
	 *            the session
	 * @param formula
	 *            the formula
	 * @return the java.util. vector
	 */
	public static java.util.Vector<Object> wrappedEvaluate(final org.openntf.domino.Session session, final String formula) {
		java.util.Vector<Object> result = new org.openntf.domino.impl.Vector<Object>();
		java.util.Vector<Object> values = session.evaluate(formula);
		for (Object value : values) {
			if (value instanceof lotus.domino.DateTime) {
				result.add(fromLotus((lotus.domino.DateTime) value, org.openntf.domino.impl.DateTime.class, session));
			} else if (value instanceof lotus.domino.DateRange) {
				result.add(fromLotus((lotus.domino.DateRange) value, org.openntf.domino.impl.DateRange.class, session));
			} else if (value instanceof Collection) {
				result.add(wrapColumnValues((Collection<?>) value, session));
			} else {
				result.add(value);
			}
		}
		return result;
	}

	/**
	 * Wrapped evaluate.
	 * 
	 * @param session
	 *            the session
	 * @param formula
	 *            the formula
	 * @param contextDocument
	 *            the context document
	 * @return the java.util. vector
	 */
	public static java.util.Vector<Object> wrappedEvaluate(final org.openntf.domino.Session session, final String formula,
			final lotus.domino.Document contextDocument) {
		java.util.Vector<Object> result = new org.openntf.domino.impl.Vector<Object>();
		java.util.Vector<Object> values = session.evaluate(formula, contextDocument);
		for (Object value : values) {
			if (value instanceof lotus.domino.DateTime) {
				result.add(fromLotus((lotus.domino.DateTime) value, org.openntf.domino.impl.DateTime.class, session));
			} else if (value instanceof lotus.domino.DateRange) {
				result.add(fromLotus((lotus.domino.DateRange) value, org.openntf.domino.impl.DateRange.class, session));
			} else if (value instanceof Collection) {
				result.add(wrapColumnValues((Collection<?>) value, session));
			} else {
				result.add(value);
			}
		}
		return result;
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
				result = Factory.fromLotus(lotus.domino.NotesFactory.createSession(), Session.class, null);
			} catch (lotus.domino.NotesException ne) {
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

	public static void setSession(final lotus.domino.Session session) {
		currentSessionHolder_.set((Session) fromLotus(session, org.openntf.domino.Session.class, null));
	}

	public static void clearSession() {
		currentSessionHolder_.set(null);
	}

	public static ClassLoader getClassLoader() {
		if (currentClassLoader_.get() == null) {
			setClassLoader(Factory.class.getClassLoader());
		}
		return currentClassLoader_.get();
	}

	public static void setClassLoader(final ClassLoader loader) {
		if (loader != null) {
			//			System.out.println("Setting OpenNTF Factory ClassLoader to a " + loader.getClass().getName());
		}
		currentClassLoader_.set(loader);
	}

	public static void clearClassLoader() {
		currentClassLoader_.set(null);
	}

	public static Mapper getMapper() {
		return mapper_.get();
	}

	public static void setMapper(final Mapper mapper) {
		mapper_.set(mapper);

	}

	public static void clearMapper() {
		mapper_.set(null);
	}

	public static void clearDominoGraph() {
		DominoGraph.clearDocumentCache();
	}

	public static void clearBubbleExceptions() {
		DominoUtils.setBubbleExceptions(null);
	}

	public static void terminate() {
		clearSession();
		clearClassLoader();
		clearBubbleExceptions();
		clearDominoGraph();
		clearMapper();
		//		System.out.println("Terminating OpenNTF Factory");
	}

	/**
	 * Gets the session full access.
	 * 
	 * @return the session full access
	 */
	public static org.openntf.domino.Session getSessionFullAccess() {
		try {
			lotus.domino.Session s = lotus.domino.NotesFactory.createSessionWithFullAccess();
			return fromLotus(s, org.openntf.domino.Session.class, null);
		} catch (lotus.domino.NotesException ne) {
			ne.printStackTrace();
			// DominoUtils.handleException(ne);
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
			lotus.domino.Session s = lotus.domino.NotesFactory.createTrustedSession();
			return fromLotus(s, org.openntf.domino.Session.class, null);
		} catch (lotus.domino.NotesException ne) {
			DominoUtils.handleException(ne);
		}
		return null;
	}

	public static String getDataPath() {

		org.openntf.domino.Session s = Factory.getSession();
		if (s != null) {
			return s.getEnvironmentString("Directory", true);
		} else {
			return "";
		}
	}

	public static String getProgramPath() {
		org.openntf.domino.Session s = Factory.getSession();
		if (s != null) {
			return s.getEnvironmentString("NotesProgram", true);
		} else {
			return "";
		}
	}

	public static String getHTTPJVMHeapSize() {
		String heapSize = "64M (Default)";
		String heapSet = "";
		org.openntf.domino.Session s = Factory.getSession();
		if (s != null) {
			String heapSizeChk = s.getEnvironmentString("HTTPJVMMaxHeapSize", true);
			heapSet = s.getEnvironmentString("HTTPJVMMaxHeapSizeSet", true);
			if ("".equals(heapSizeChk))
				return heapSize;
			if ("".equals(heapSet)) {
				return heapSizeChk + " (But HTTPJVMMaxHeapSizeSet is not set to \"1\" so it has no effect)";
			} else {
				return heapSizeChk;
			}
		} else {
			return heapSize;
		}
	}

	/**
	 * Gets the parent database.
	 * 
	 * @param base
	 *            the base
	 * @return the parent database
	 */
	public static org.openntf.domino.Database getParentDatabase(final org.openntf.domino.Base<?> base) {
		org.openntf.domino.Database result = null;
		if (base instanceof DatabaseDescendant) {
			result = ((DatabaseDescendant) base).getAncestorDatabase();
		} else if (base instanceof org.openntf.domino.Database) {
			result = (org.openntf.domino.Database) base;
		} else {
			throw new UndefinedDelegateTypeException();
		}
		return result;
	}

	/**
	 * Gets the session.
	 * 
	 * @param base
	 *            the base
	 * @return the session
	 */
	public static org.openntf.domino.Session getSession(final org.openntf.domino.Base<?> base) {
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
			result = Session.getDefaultSession(); // last ditch, get the primary Session;
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
		if (collection instanceof org.openntf.domino.impl.DocumentCollection) {
			org.openntf.domino.Database db = ((org.openntf.domino.DocumentCollection) collection).getParent();
			result = db.createNoteCollection(false);
			result.add((DocumentCollection) collection);
		} else {
			throw new DataNotCompatibleException("Cannot convert a non-OpenNTF DocumentCollection to a NoteCollection");
		}
		return result;
	}

}

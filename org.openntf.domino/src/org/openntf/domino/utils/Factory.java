/*
 * Copyright OpenNTF 2013
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

import org.openntf.domino.exceptions.UndefinedDelegateTypeException;
import org.openntf.domino.impl.Session;
import org.openntf.domino.types.DatabaseDescendant;

// TODO: Auto-generated Javadoc
/**
 * The Enum Factory.
 */
public enum Factory {
	;
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
		 */
		public int increment() {
			int result = get() + 1;
			set(result);
			return result;
		}

		/**
		 * Decrement.
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
	public static <T> T fromLotus(lotus.domino.Base lotus, Class<? extends org.openntf.domino.Base> T, org.openntf.domino.Base parent) {
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
		} else if (lotus instanceof lotus.domino.NotesCalendar) {
			result = (T) new org.openntf.domino.impl.NotesCalendar((lotus.domino.NotesCalendar) lotus, parent);
		} else if (lotus instanceof lotus.domino.NotesCalendarEntry) {
			result = (T) new org.openntf.domino.impl.NotesCalendarEntry((lotus.domino.NotesCalendarEntry) lotus, parent);
		} else if (lotus instanceof lotus.domino.NotesCalendarNotice) {
			result = (T) new org.openntf.domino.impl.NotesCalendarNotice((lotus.domino.NotesCalendarNotice) lotus, parent);
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
	public static <T> Collection<T> fromLotus(Collection<?> lotusColl, Class<? extends org.openntf.domino.Base> T,
			org.openntf.domino.Base<?> parent) {
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
	public static <T> org.openntf.domino.impl.Vector<T> fromLotusAsVector(Collection<?> lotusColl,
			Class<? extends org.openntf.domino.Base> T, org.openntf.domino.Base<?> parent) {
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
	public static java.util.Vector<Object> wrapColumnValues(Collection<?> values) {
		if (values == null) {
			return null;
		}

		java.util.Vector<Object> result = new org.openntf.domino.impl.Vector<Object>();
		for (Object value : values) {
			if (value instanceof lotus.domino.DateTime) {
				result.add(fromLotus((lotus.domino.DateTime) value, org.openntf.domino.impl.DateTime.class, getSession()));
			} else if (value instanceof lotus.domino.DateRange) {
				result.add(fromLotus((lotus.domino.DateRange) value, org.openntf.domino.impl.DateRange.class, getSession()));
			} else if (value instanceof Collection) {
				result.add(wrapColumnValues((Collection<?>) value));
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
	 * @return the java.util. vector
	 */
	public static java.util.Vector<Object> wrappedEvaluate(org.openntf.domino.Session session, String formula) {
		java.util.Vector<Object> result = new org.openntf.domino.impl.Vector<Object>();
		java.util.Vector<Object> values = session.evaluate(formula);
		for (Object value : values) {
			if (value instanceof lotus.domino.DateTime) {
				result.add(fromLotus((lotus.domino.DateTime) value, org.openntf.domino.impl.DateTime.class, session));
			} else if (value instanceof lotus.domino.DateRange) {
				result.add(fromLotus((lotus.domino.DateRange) value, org.openntf.domino.impl.DateRange.class, session));
			} else if (value instanceof Collection) {
				result.add(wrapColumnValues((Collection<?>) value));
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
	public static java.util.Vector<Object> wrappedEvaluate(org.openntf.domino.Session session, String formula,
			lotus.domino.Document contextDocument) {
		java.util.Vector<Object> result = new org.openntf.domino.impl.Vector<Object>();
		java.util.Vector<Object> values = session.evaluate(formula, contextDocument);
		for (Object value : values) {
			if (value instanceof lotus.domino.DateTime) {
				result.add(fromLotus((lotus.domino.DateTime) value, org.openntf.domino.impl.DateTime.class, session));
			} else if (value instanceof lotus.domino.DateRange) {
				result.add(fromLotus((lotus.domino.DateRange) value, org.openntf.domino.impl.DateRange.class, session));
			} else if (value instanceof Collection) {
				result.add(wrapColumnValues((Collection<?>) value));
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
		try {
			lotus.domino.Session s = lotus.domino.NotesFactory.createSession();
			return fromLotus(s, org.openntf.domino.Session.class, null);
		} catch (lotus.domino.NotesException ne) {
			DominoUtils.handleException(ne);
		}
		return null;
	}

	public static org.openntf.domino.Session getSessionFullAccess() {
		try {
			lotus.domino.Session s = lotus.domino.NotesFactory.createSessionWithFullAccess();
			return fromLotus(s, org.openntf.domino.Session.class, null);
		} catch (lotus.domino.NotesException ne) {
			DominoUtils.handleException(ne);
		}
		return null;
	}

	public static org.openntf.domino.Session getTrustedSession() {
		try {
			lotus.domino.Session s = lotus.domino.NotesFactory.createTrustedSession();
			return fromLotus(s, org.openntf.domino.Session.class, null);
		} catch (lotus.domino.NotesException ne) {
			DominoUtils.handleException(ne);
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
	public static org.openntf.domino.Database getParentDatabase(org.openntf.domino.Base<?> base) {
		org.openntf.domino.Database result = null;
		if (base instanceof org.openntf.domino.Database) {
			result = (org.openntf.domino.Database) base;
		} else if (base instanceof org.openntf.domino.Document) {
			result = ((org.openntf.domino.Document) base).getParentDatabase();
		} else if (base instanceof org.openntf.domino.Agent) {
			result = ((org.openntf.domino.Agent) base).getParent();
		} else if (base instanceof org.openntf.domino.Outline) {
			result = ((org.openntf.domino.Outline) base).getParentDatabase();
		} else if (base instanceof org.openntf.domino.OutlineEntry) {
			result = ((org.openntf.domino.OutlineEntry) base).getParent().getParentDatabase();
		} else if (base instanceof org.openntf.domino.EmbeddedObject) {
			result = ((org.openntf.domino.EmbeddedObject) base).getParentDocument().getParentDatabase();
		} else if (base instanceof org.openntf.domino.Item) { // Note: this includes RichTextItems too
			result = (org.openntf.domino.Database) ((org.openntf.domino.Item) base).getParent().getParentDatabase();
		} else if (base instanceof org.openntf.domino.DocumentCollection) {
			result = (org.openntf.domino.Database) ((org.openntf.domino.DocumentCollection) base).getParent();
		} else if (base instanceof org.openntf.domino.NoteCollection) {
			result = (org.openntf.domino.Database) ((org.openntf.domino.NoteCollection) base).getParent();
		} else if (base instanceof org.openntf.domino.View) {
			result = (org.openntf.domino.Database) ((org.openntf.domino.View) base).getParent();
		} else if (base instanceof org.openntf.domino.Form) {
			result = ((org.openntf.domino.Form) base).getParent();
		} else if (base instanceof org.openntf.domino.ViewEntry) {
			org.openntf.domino.Base<?> intermediary = ((org.openntf.domino.ViewEntry) base).getParent();
			if (intermediary instanceof org.openntf.domino.ViewEntryCollection) {
				result = ((org.openntf.domino.ViewEntryCollection) intermediary).getParent().getParent();
			} else if (intermediary instanceof org.openntf.domino.ViewNavigator) {
				result = ((org.openntf.domino.ViewNavigator) intermediary).getParentView().getParent();
			} else if (intermediary instanceof org.openntf.domino.View) {
				result = ((org.openntf.domino.View) intermediary).getParent();
			} else {
				throw new UndefinedDelegateTypeException();
			}
		} else if (base instanceof org.openntf.domino.ViewColumn) {
			result = ((org.openntf.domino.ViewColumn) base).getParent().getParent();
		} else if(base instanceof org.openntf.domino.ViewNavigator) {
			result = ((org.openntf.domino.ViewNavigator)base).getParentView().getParent();
		} else if(base instanceof org.openntf.domino.ViewEntryCollection) {
			result = ((org.openntf.domino.ViewEntryCollection)base).getParent().getParent();
		} else if (base instanceof org.openntf.domino.MIMEEntity) {
			result = ((org.openntf.domino.MIMEEntity) base).getParent().getParentDatabase();
		} else if (base instanceof org.openntf.domino.MIMEHeader) {
			result = ((org.openntf.domino.MIMEHeader) base).getParent().getParent().getParentDatabase();
		} else if (base instanceof org.openntf.domino.RichTextSection) {
			result = ((org.openntf.domino.RichTextSection) base).getParent().getParent().getParentDatabase();
		} else if (base instanceof org.openntf.domino.RichTextRange) {
			result = ((org.openntf.domino.RichTextSection) base).getParent().getParent().getParentDatabase();
		} else if (base instanceof org.openntf.domino.RichTextTable) {
			result = ((org.openntf.domino.RichTextTable) base).getParent().getParent().getParentDatabase();
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
	public static org.openntf.domino.Session getSession(org.openntf.domino.Base<?> base) {
		org.openntf.domino.Session result = null;
		if (base instanceof DatabaseDescendant) {
			result = getParentDatabase(base).getParent();
		} else if (base instanceof org.openntf.domino.AgentContext) {
			result = ((org.openntf.domino.AgentContext) base).getParentSession();
		} else if (base instanceof org.openntf.domino.Session) {
			result = (org.openntf.domino.Session) base;
		} else if (base instanceof org.openntf.domino.Database) {
			result = ((org.openntf.domino.Database) base).getParent();
		} else if (base instanceof org.openntf.domino.DateTime) {
			result = ((org.openntf.domino.DateTime) base).getParent();
		} else if (base instanceof org.openntf.domino.DateRange) {
			result = ((org.openntf.domino.DateRange) base).getParent();
		} else if(base instanceof org.openntf.domino.Directory) {
			result = ((org.openntf.domino.Directory)base).getParent();
		} else if(base instanceof org.openntf.domino.DirectoryNavigator) {
			result = ((org.openntf.domino.DirectoryNavigator)base).getParent().getParent();
		} else if (base instanceof org.openntf.domino.Name) {
			result = ((org.openntf.domino.Name) base).getParent();
		} else if (base instanceof org.openntf.domino.Stream) {
			result = ((org.openntf.domino.Stream) base).getParent();
		} else if (base instanceof org.openntf.domino.MIMEEntity) {
			result = ((org.openntf.domino.MIMEEntity) base).getParent().getParentDatabase().getParent();
		} else if (base instanceof org.openntf.domino.MIMEHeader) {
			result = getParentDatabase(base).getParent();
		} else if(base instanceof org.openntf.domino.NotesCalendar) {
			result = ((org.openntf.domino.NotesCalendar)base).getParent();
		} else if(base instanceof org.openntf.domino.NotesCalendarEntry) {
			result = ((org.openntf.domino.NotesCalendarEntry)base).getParent().getParent();
		} else if(base instanceof org.openntf.domino.NotesCalendarNotice) {
			result = ((org.openntf.domino.NotesCalendarNotice)base).getParent().getParent();
		} else {
			System.out.println("couldn't find session for object of type " + base.getClass().getName());
			throw new UndefinedDelegateTypeException();
		}
		if (result == null)
			result = Session.getDefaultSession(); // last ditch, get the primary Session;
		return result;
	}

}

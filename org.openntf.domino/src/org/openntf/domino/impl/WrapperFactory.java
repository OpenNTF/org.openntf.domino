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
package org.openntf.domino.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import lotus.domino.NotesException;
import lotus.domino.local.NotesBase;

import org.openntf.domino.Base;
import org.openntf.domino.Database;
import org.openntf.domino.DateRange;
import org.openntf.domino.DateTime;
import org.openntf.domino.Directory;
import org.openntf.domino.Document;
import org.openntf.domino.MIMEEntity;
import org.openntf.domino.NotesCalendar;
import org.openntf.domino.Outline;
import org.openntf.domino.PropertyBroker;
import org.openntf.domino.Replication;
import org.openntf.domino.RichTextItem;
import org.openntf.domino.RichTextNavigator;
import org.openntf.domino.RichTextParagraphStyle;
import org.openntf.domino.Session;
import org.openntf.domino.SessionHasNoParent;
import org.openntf.domino.View;
import org.openntf.domino.exceptions.UndefinedDelegateTypeException;
import org.openntf.domino.thread.DominoReferenceCache;
import org.openntf.domino.types.FactorySchema;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Enum Factory.
 */
public class WrapperFactory implements org.openntf.domino.WrapperFactory {

	/** this is the holder for all other object that needs to be recycled **/
	private DominoReferenceCache referenceCache = new DominoReferenceCache();

	private long clearCaches() {
		long result = 0;
		// call gc once before processing the queues
		System.gc();
		try {
			//give the gc some ms (not too much, we do not want to delay HTTP-Requests!
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// and ignore this
			//DominoUtils.handleException(e);
		}
		// TODO: Recycle all?
		//System.out.println("Online objects: " + Factory.getActiveObjectCount());
		result = referenceCache.processQueue(null);
		//System.out.println("Online objects: " + Factory.getActiveObjectCount());
		return result;
	}

	/** The Constant log_. */
	private static final Logger log_ = Logger.getLogger(WrapperFactory.class.getName());

	//	/**
	//	 * This function enqueus the reference and lotus, so that it will get recycled. This works by observing the life time of "reference" and
	//	 * calling recycle it neccessary
	//	 * 
	//	 * @param reference
	//	 * @param lotus
	//	 * @return
	//	 */
	//	public void observe(final Object reference, final lotus.domino.Base lotus) {
	//		if (reference != null && lotus != null) {
	//			lotusObjects.put(0L, reference, lotus);
	//			Factory.countLotus();
	//		}
	//	}

	// -- Factory methods
	@Override
	@SuppressWarnings("rawtypes")
	public <T extends Base, D extends lotus.domino.Base, P extends Base> T fromLotus(final D lotus, final FactorySchema<T, D, P> schema,
			final P parent) {
		return fromLotus(lotus, schema, parent, null);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected <T extends Base, D extends lotus.domino.Base, P extends Base> T fromLotus(final D lotus, final FactorySchema<T, D, P> schema,
			final P parent, final long[] prevent_recycling) {
		if (lotus == null) {
			return null;
		}
		if (lotus instanceof Base) {
			if (log_.isLoggable(Level.FINE))
				log_.log(Level.FINE, "Returning an already OpenNTF object...");
			return (T) lotus;
		}

		if (!(lotus instanceof NotesBase)) {
			// RPr: what do we if we don't get a wrappable object at all. This is a programming error, so throw exception
			throw new UndefinedDelegateTypeException("Cannot wrap " + lotus.getClass().getName());
		}

		// 1) These objects are not cached and returned immediately. Recycle is done inside
		if (lotus instanceof lotus.domino.Name 					// These objects are encapsulated
				|| lotus instanceof lotus.domino.DateRange 		// 
				|| lotus instanceof lotus.domino.DateTime) { 	//

			long cpp_key = org.openntf.domino.impl.Base.getLotusId(lotus);
			return (T) wrapLotusObject(lotus, parent, cpp_key);
		}

		return (T) fromLotusObject(lotus, parent, referenceCache, prevent_recycling);

	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T extends Base, D extends lotus.domino.Base, P extends Base> Collection<T> fromLotus(final Collection<?> lotusColl,
			final FactorySchema<T, D, P> schema, final P parent) {
		long[] prevent_recycling = getContainingCppIds(lotusColl);
		Collection<T> result = new ArrayList<T>(lotusColl.size());
		if (!lotusColl.isEmpty()) {
			for (Object lotus : lotusColl) {
				if (lotus instanceof lotus.domino.Base) {
					result.add(fromLotus((D) lotus, schema, parent, prevent_recycling));
				}
			}
		}
		return result;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T extends Base, D extends lotus.domino.Base, P extends Base> Vector<T> fromLotusAsVector(final Collection<?> lotusColl,
			final FactorySchema<T, D, P> schema, final P parent) {
		long[] prevent_recycling = getContainingCppIds(lotusColl);
		Vector<T> result = new Vector<T>(lotusColl.size());
		if (!lotusColl.isEmpty()) {
			for (Object lotus : lotusColl) {
				if (lotus instanceof lotus.domino.Base) {
					result.add(fromLotus((D) lotus, schema, parent, prevent_recycling));
				}
			}
		}
		return result;
	}

	/**
	 * determines all containing Cpp-Ids in the lotusColl
	 * 
	 * @param obj
	 *            The collection of objects.
	 * @return An array of the CPP IDs for the contained objects.
	 */
	public long[] getContainingCppIds(final Object obj) {
		List<lotus.domino.Base> lst = new ArrayList<lotus.domino.Base>();
		getContainingNotesObjects(lst, obj);
		long[] ret = new long[2 + lst.size()]; // the first two elements are reserved
		for (int i = 0; i < lst.size(); i++) {
			ret[i + 2] = org.openntf.domino.impl.Base.getLotusId(lst.get(i));
		}
		return ret;
	}

	private void getContainingNotesObjects(final List<lotus.domino.Base> objects, final Object obj) {
		if (obj instanceof lotus.domino.Base) {
			objects.add((lotus.domino.Base) obj);
		} else if (obj instanceof Iterable) {
			for (Object o : (Iterable<?>) obj) {
				getContainingNotesObjects(objects, o);
			}
		}

	}

	// --- session handling 
	/**
	 * Wraps and caches sessions. Sessions are put in a separate map (otherwise you can use fromLotusObject). (you may overwrite this if we
	 * make a non static IFactory)
	 * 
	 * @param lotus
	 * @param parent
	 */
	protected void setCurrentSession(final Session newSession) {

		// Store the first wrapped session in the sessionHolder
		Session currentSession = Factory.getSession_unchecked();
		if (currentSession == newSession) {
			// if this is the identical object, do nothing
		} else if (currentSession != null) {
			lotus.domino.Session rawSession = org.openntf.domino.impl.Base.toLotus(currentSession);
			if (org.openntf.domino.impl.Base.isDead(rawSession)) {
				//				Factory.loadEnvironment(newSession);
				// System.out.println("Resetting default local session because we got an exception");
				Factory.setSession(newSession);
			}
		} else {
			//			Factory.loadEnvironment(newSession);
			// System.out.println("Resetting default local session because it was null");
			Factory.setSession(newSession);
		}
	}

	// --- others
	/**
	 * Wraps & caches all lotus object except Names, DateTimes, Sessions, Documents
	 * 
	 * @param lotus
	 * @param prevent_recycling
	 *            the cpp_ids that must not get recycled during this task. Attention. For performance reasons you should not use slot 0 and
	 *            1 in the array
	 * @param parent
	 * @return
	 */
	protected Base<?> fromLotusObject(final lotus.domino.Base lotus, final Base<?> parent, final DominoReferenceCache cache,
			long[] prevent_recycling) {
		if (lotus == null) {
			return null;
		}

		if (prevent_recycling == null) {
			prevent_recycling = new long[2];
		}
		// Slot 0 and one are reserved!
		long cpp_key = prevent_recycling[0] = org.openntf.domino.impl.Base.getLotusId(lotus);
		prevent_recycling[1] = org.openntf.domino.impl.Base.getLotusId(parent);

		// RPr: Query the cache. The current lotus object might be enqued, so that it gets recycled in the next step
		Base<?> result = cache.get(cpp_key, Base.class);

		if (result == null) {
			// RPr: If the result is null, we can be sure, that there is no element in our cache map.
			// this happens if no one holds a strong reference to the wrapper. As "get" does some cleanup
			// action, we must ensure, that we do not recycle the CURRENT (and parent) element in the next step

			result = wrapLotusObject(lotus, parent, cpp_key);

			cache.processQueue(prevent_recycling); // recycle all elements but not the current ones

			cache.put(cpp_key, result, lotus);
			if (lotus instanceof lotus.domino.Session				//
					|| lotus instanceof lotus.domino.AgentContext) {
				// these are never recycled by default. If you create your own session, you have to recycle it after use
				// or setNoRecycle to "false"
				cache.setNoRecycle(cpp_key, true);
				//				System.out.println("DEBUG: Wrapping a new Session with object id: " + System.identityHashCode(lotus));
			}
		}
		return result;
	}

	@Override
	public boolean recacheLotusObject(final lotus.domino.Base lotus, final Base<?> wrapper, final Base<?> parent) {
		boolean returnVal = false;
		long[] prevent_recycling = new long[2];
		long cpp_key = prevent_recycling[0] = org.openntf.domino.impl.Base.getLotusId(lotus);
		prevent_recycling[1] = org.openntf.domino.impl.Base.getLotusId(parent);
		Base<?> result = referenceCache.get(cpp_key, Base.class);

		if (result == null) {
			// RPr: If the result is null, we can be sure, that there is no element in our cache map.
			// this happens if no one holds a strong reference to the wrapper. As "get" does some cleanup
			// action, we must ensure, that we do not recycle the CURRENT (and parent) element in the next step

			result = wrapper;

			referenceCache.processQueue(prevent_recycling); // recycle all elements but not the current ones

			referenceCache.put(cpp_key, result, lotus);
			returnVal = true;
		} else {	//NTF not sure what to do in this case. Don't even know what this means...
			System.out.println("PANIC! Why are we recaching a lotus object " + lotus.getClass().getSimpleName()
					+ " that we already have!???!");
		}
		return returnVal;
	}

	@Override
	public void setNoRecycle(final Base<?> base, final boolean value) {
		referenceCache.setNoRecycle(((org.openntf.domino.impl.Base<?, ?, ?>) base).GetCppObj(), value);
	}

	/**
	 * Helper for fromLotusObject, so you can overwrite this
	 * 
	 * @param lotus
	 * @param parent
	 *            can be null, except for {@link DirectoryNavigator}, {@link MIMEEntity}, {@link MIMEHeader}, {@link NotesCalendarEntry},
	 *            {@link NotesCalendarNotice}, {@link Replication}, {@link ReplicationEntry}, {@link RichTextDoclink},
	 *            {@link RichTextNavigator}, {@link RichTextRange}, {@link RichTextSection}, {@link RichTextTab}, {@link RichTextTable}
	 * @param cpp
	 * @return
	 */

	protected Base<?> wrapLotusObject(final lotus.domino.Base lotus, final Base<?> parent, final long cpp) {
		// TODO Auto-generated method stub

		if (lotus instanceof lotus.domino.Name) {
			return new org.openntf.domino.impl.Name((lotus.domino.Name) lotus, (Session) parent, this, cpp);
		}
		if (lotus instanceof lotus.domino.DateRange) {
			return new org.openntf.domino.impl.DateRange((lotus.domino.DateRange) lotus, (Session) parent, this, cpp);
		}

		if (lotus instanceof lotus.domino.DateTime) {
			return new org.openntf.domino.impl.DateTime((lotus.domino.DateTime) lotus, (Session) parent, this, cpp);
		}

		if (lotus instanceof lotus.domino.RichTextItem) { // items & richtextitems are used very often. 
			return new org.openntf.domino.impl.RichTextItem((lotus.domino.RichTextItem) lotus, (Document) parent, this, cpp);
		}

		if (lotus instanceof lotus.domino.Item) {  // check for Item must be behind RichtextItem
			return new org.openntf.domino.impl.Item((lotus.domino.Item) lotus, (Document) parent, this, cpp);
		}

		if (lotus instanceof lotus.domino.Document) {
			return new org.openntf.domino.impl.Document((lotus.domino.Document) lotus, (Database) parent, this, cpp);
		}

		if (lotus instanceof lotus.domino.DocumentCollection) {
			return new org.openntf.domino.impl.DocumentCollection((lotus.domino.DocumentCollection) lotus, (Database) parent, this, cpp);
		}

		if (lotus instanceof lotus.domino.ACL) {
			return new org.openntf.domino.impl.ACL((lotus.domino.ACL) lotus, (Database) parent, this, cpp);
		}
		if (lotus instanceof lotus.domino.ACLEntry) {
			return new org.openntf.domino.impl.ACLEntry((lotus.domino.ACLEntry) lotus, (org.openntf.domino.ACL) parent, this, cpp);
		}
		if (lotus instanceof lotus.domino.AdministrationProcess) {
			return new org.openntf.domino.impl.AdministrationProcess((lotus.domino.AdministrationProcess) lotus, (Session) parent, this,
					cpp);
		}
		if (lotus instanceof lotus.domino.Agent) {
			return new org.openntf.domino.impl.Agent((lotus.domino.Agent) lotus, (Database) parent, this, cpp);
		}
		if (lotus instanceof lotus.domino.AgentContext) {
			return new org.openntf.domino.impl.AgentContext((lotus.domino.AgentContext) lotus, (Session) parent, this, cpp);
		}
		if (lotus instanceof lotus.domino.ColorObject) {
			return new org.openntf.domino.impl.ColorObject((lotus.domino.ColorObject) lotus, (Session) parent, this, cpp);
		}
		if (lotus instanceof lotus.domino.Database) {
			return new org.openntf.domino.impl.Database((lotus.domino.Database) lotus, (Session) parent, this, cpp);
		}
		if (lotus instanceof lotus.domino.DbDirectory) {
			return new org.openntf.domino.impl.DbDirectory((lotus.domino.DbDirectory) lotus, (Session) parent, this, cpp);
		}
		if (lotus instanceof lotus.domino.Directory) {
			return new org.openntf.domino.impl.Directory((lotus.domino.Directory) lotus, (Session) parent, this, cpp);
		}
		if (lotus instanceof lotus.domino.DirectoryNavigator) {
			return new org.openntf.domino.impl.DirectoryNavigator((lotus.domino.DirectoryNavigator) lotus, (Directory) parent, this, cpp);
		}
		if (lotus instanceof lotus.domino.DxlExporter) {
			return new org.openntf.domino.impl.DxlExporter((lotus.domino.DxlExporter) lotus, (Session) parent, this, cpp);
		}
		if (lotus instanceof lotus.domino.DxlImporter) {
			return new org.openntf.domino.impl.DxlImporter((lotus.domino.DxlImporter) lotus, (Session) parent, this, cpp);
		}
		if (lotus instanceof lotus.domino.EmbeddedObject) {
			return new org.openntf.domino.impl.EmbeddedObject((lotus.domino.EmbeddedObject) lotus, (Document) parent, this, cpp);
		}
		if (lotus instanceof lotus.domino.Form) {
			return new org.openntf.domino.impl.Form((lotus.domino.Form) lotus, (Database) parent, this, cpp);
		}
		if (lotus instanceof lotus.domino.International) {
			return new org.openntf.domino.impl.International((lotus.domino.International) lotus, (Session) parent, this, cpp);
		}
		if (lotus instanceof lotus.domino.Log) {
			return new org.openntf.domino.impl.Log((lotus.domino.Log) lotus, (Session) parent, this, cpp);
		}
		if (lotus instanceof lotus.domino.MIMEEntity) {
			return new org.openntf.domino.impl.MIMEEntity((lotus.domino.MIMEEntity) lotus, (Document) parent, this, cpp);
		}
		if (lotus instanceof lotus.domino.MIMEHeader) {
			return new org.openntf.domino.impl.MIMEHeader((lotus.domino.MIMEHeader) lotus, (MIMEEntity) parent, this, cpp);
		}
		if (lotus instanceof lotus.domino.Newsletter) {
			return new org.openntf.domino.impl.Newsletter((lotus.domino.Newsletter) lotus, (Session) parent, this, cpp);
		}
		if (lotus instanceof lotus.domino.NoteCollection) {
			return new org.openntf.domino.impl.NoteCollection((lotus.domino.NoteCollection) lotus, (org.openntf.domino.Database) parent,
					this, cpp);
		}
		if (lotus instanceof lotus.domino.NotesCalendar) {
			return new org.openntf.domino.impl.NotesCalendar((lotus.domino.NotesCalendar) lotus, (Session) parent, this, cpp);
		}
		if (lotus instanceof lotus.domino.NotesCalendarEntry) {
			return new org.openntf.domino.impl.NotesCalendarEntry((lotus.domino.NotesCalendarEntry) lotus, (NotesCalendar) parent, this,
					cpp);
		}
		if (lotus instanceof lotus.domino.NotesCalendarNotice) {
			return new org.openntf.domino.impl.NotesCalendarNotice((lotus.domino.NotesCalendarNotice) lotus, (NotesCalendar) parent, this,
					cpp);
		}
		if (lotus instanceof lotus.domino.NotesProperty) {
			return new org.openntf.domino.impl.NotesProperty((lotus.domino.NotesProperty) lotus, (PropertyBroker) parent, this, cpp);
		}
		if (lotus instanceof lotus.domino.Outline) {
			return new org.openntf.domino.impl.Outline((lotus.domino.Outline) lotus, (Database) parent, this, cpp);
		}
		if (lotus instanceof lotus.domino.OutlineEntry) {
			return new org.openntf.domino.impl.OutlineEntry((lotus.domino.OutlineEntry) lotus, (Outline) parent, this, cpp);
		}
		if (lotus instanceof lotus.domino.PropertyBroker) {
			return new org.openntf.domino.impl.PropertyBroker((lotus.domino.PropertyBroker) lotus, (Session) parent, this, cpp);
		}
		if (lotus instanceof lotus.domino.Registration) {
			return new org.openntf.domino.impl.Registration((lotus.domino.Registration) lotus, (Session) parent, this, cpp);
		}
		if (lotus instanceof lotus.domino.Replication) {
			return new org.openntf.domino.impl.Replication((lotus.domino.Replication) lotus, (Database) parent, this, cpp);
		}
		if (lotus instanceof lotus.domino.ReplicationEntry) {
			return new org.openntf.domino.impl.ReplicationEntry((lotus.domino.ReplicationEntry) lotus, (Replication) parent, this, cpp);
		}
		if (lotus instanceof lotus.domino.RichTextDoclink) {
			return new org.openntf.domino.impl.RichTextDoclink((lotus.domino.RichTextDoclink) lotus, (RichTextItem) parent, this, cpp);
		}
		if (lotus instanceof lotus.domino.RichTextNavigator) {
			return new org.openntf.domino.impl.RichTextNavigator((lotus.domino.RichTextNavigator) lotus, (RichTextItem) parent, this, cpp);
		}
		if (lotus instanceof lotus.domino.RichTextParagraphStyle) {
			return new org.openntf.domino.impl.RichTextParagraphStyle((lotus.domino.RichTextParagraphStyle) lotus, (Session) parent, this,
					cpp);
		}
		if (lotus instanceof lotus.domino.RichTextRange) {
			return new org.openntf.domino.impl.RichTextRange((lotus.domino.RichTextRange) lotus, (RichTextItem) parent, this, cpp);
		}
		if (lotus instanceof lotus.domino.RichTextSection) {
			return new org.openntf.domino.impl.RichTextSection((lotus.domino.RichTextSection) lotus, (RichTextNavigator) parent, this, cpp);
		}
		if (lotus instanceof lotus.domino.RichTextStyle) {
			return new org.openntf.domino.impl.RichTextStyle((lotus.domino.RichTextStyle) lotus, (Session) parent, this, cpp);
		}
		if (lotus instanceof lotus.domino.RichTextTab) {
			return new org.openntf.domino.impl.RichTextTab((lotus.domino.RichTextTab) lotus, (RichTextParagraphStyle) parent, this, cpp);
		}
		if (lotus instanceof lotus.domino.RichTextTable) {
			return new org.openntf.domino.impl.RichTextTable((lotus.domino.RichTextTable) lotus, (RichTextItem) parent, this, cpp);
		}
		if (lotus instanceof lotus.domino.Stream) {
			return new org.openntf.domino.impl.Stream((lotus.domino.Stream) lotus, (Session) parent, this, cpp);
		}
		if (lotus instanceof lotus.domino.View) {
			return new org.openntf.domino.impl.View((lotus.domino.View) lotus, (org.openntf.domino.Database) parent, this, cpp);
		}
		if (lotus instanceof lotus.domino.ViewColumn) {
			return new org.openntf.domino.impl.ViewColumn((lotus.domino.ViewColumn) lotus, (org.openntf.domino.View) parent, this, cpp);
		}
		if (lotus instanceof lotus.domino.ViewEntry) {
			return new org.openntf.domino.impl.ViewEntry((lotus.domino.ViewEntry) lotus, (View) parent, this, cpp);
		}
		if (lotus instanceof lotus.domino.ViewEntryCollection) {
			return new org.openntf.domino.impl.ViewEntryCollection((lotus.domino.ViewEntryCollection) lotus,
					(org.openntf.domino.View) parent, this, cpp);
		}
		if (lotus instanceof lotus.domino.ViewNavigator) {
			return new org.openntf.domino.impl.ViewNavigator((lotus.domino.ViewNavigator) lotus, (org.openntf.domino.View) parent, this,
					cpp);
		}

		if (lotus instanceof lotus.domino.Session) {
			Session result = new org.openntf.domino.impl.Session((lotus.domino.Session) lotus, (SessionHasNoParent) parent, this, cpp);
			setCurrentSession(result);
			return result;
		}
		throw new UndefinedDelegateTypeException(lotus == null ? "null" : lotus.getClass().getName());
		//		return null;
	}

	@Override
	public long terminate() {
		return clearCaches();
	}

	@Override
	public Vector<Object> wrapColumnValues(final Collection<?> values, final Session session) {
		return wrapColumnValues(values, session, null);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Vector<Object> wrapColumnValues(final Collection<?> values, final Session session, final long[] prevent_recycling) {
		if (values == null) {
			return null;
		}
		int i = 0;
		java.util.Vector<Object> result = new java.util.Vector<Object>();

		for (Object value : values) {
			if (value == null) {
				result.add(null);
			} else if (value instanceof lotus.domino.DateTime) {
				Object wrapped = null;
				try {
					wrapped = fromLotus((lotus.domino.DateTime) value, (FactorySchema) null, session, prevent_recycling);
				} catch (Throwable t) {
					if (t instanceof NotesException) {
						String text = ((NotesException) t).text;
						System.out.println("Unable to wrap a DateTime found in Vector member " + i + " of " + values.size() + " because "
								+ text);
						try {
							lotus.domino.DateTime dt = (lotus.domino.DateTime) value;
							String gmttime = dt.getGMTTime();
							System.out.println("GMTTime: " + gmttime);
						} catch (Exception e) {

						}
					}

				}
				if (wrapped == null) {
					result.add("");
				} else {
					result.add(wrapped);
				}
			} else if (value instanceof lotus.domino.DateRange) {
				result.add(fromLotus((lotus.domino.DateRange) value, (FactorySchema) null, session, prevent_recycling));
			} else if (value instanceof Collection) {
				result.add(wrapColumnValues((Collection<?>) value, session, prevent_recycling));
			} else {
				result.add(value);
			}
			i++;
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends lotus.domino.Base> T toLotus(final T base) {
		return (T) org.openntf.domino.impl.Base.getDelegate(base);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.WrapperFactory#createDateTime(java.util.Date, org.openntf.domino.Session)
	 */
	@Override
	public DateTime createDateTime(final Date date, final Session parent) {
		return new org.openntf.domino.impl.DateTime(date, parent, this, 0L);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.WrapperFactory#createDateTime(java.util.Date, org.openntf.domino.Session)
	 */
	@Override
	public DateRange createDateRange(final Date start, final Date end, final Session parent) {
		return new org.openntf.domino.impl.DateRange(start, end, parent, this);
	}

}

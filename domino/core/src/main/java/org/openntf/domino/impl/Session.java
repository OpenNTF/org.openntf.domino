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

import java.awt.Color;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Logger;

import lotus.domino.NotesError;
import lotus.domino.NotesException;

import org.openntf.domino.AdministrationProcess;
import org.openntf.domino.AgentContext;
import org.openntf.domino.AutoMime;
import org.openntf.domino.ColorObject;
import org.openntf.domino.Database;
import org.openntf.domino.DateRange;
import org.openntf.domino.DateTime;
import org.openntf.domino.DbDirectory;
import org.openntf.domino.Directory;
import org.openntf.domino.Document;
import org.openntf.domino.DxlExporter;
import org.openntf.domino.DxlImporter;
import org.openntf.domino.ExceptionDetails;
import org.openntf.domino.International;
import org.openntf.domino.Log;
import org.openntf.domino.Name;
import org.openntf.domino.Newsletter;
import org.openntf.domino.NotesCalendar;
import org.openntf.domino.PropertyBroker;
import org.openntf.domino.Registration;
import org.openntf.domino.RichTextParagraphStyle;
import org.openntf.domino.RichTextStyle;
import org.openntf.domino.Stream;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.annotations.Legacy;
import org.openntf.domino.events.EnumEvent;
import org.openntf.domino.events.GenericDominoEventFactory;
import org.openntf.domino.events.IDominoEvent;
import org.openntf.domino.events.IDominoEventFactory;
import org.openntf.domino.exceptions.UnableToAcquireSessionException;
import org.openntf.domino.exceptions.UserAccessException;
import org.openntf.domino.types.Encapsulated;
import org.openntf.domino.utils.DominoFormatter;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

import com.ibm.icu.util.Calendar;

// TODO: Auto-generated Javadoc
//import lotus.domino.Name;

/**
 * The Class Session.
 *
 * @author nfreeman
 */

public class Session extends BaseResurrectable<org.openntf.domino.Session, lotus.domino.Session, WrapperFactory>
		implements org.openntf.domino.Session {
	/** The Constant log_. */
	private static final Logger log_ = Logger.getLogger(Session.class.getName());

	/** The formatter_. */
	private DominoFormatter formatter_;// RPr: changed to non static as this can cause thread issues

	/* A lock object for "getDatabase" to prevent server crashes.
	 * it seems that the method is not thread safe. (at least if you run the code as java application)
	 * -- stack dump when crash happenes --
	 * @[ 9] 0x5c8456a3 nnotes.Panic@4+883 (604cda8c)
	 * @[10] 0x5c624a9d nnotes.OSVBlockAddr@8+109 (5e36fc00,0)
	 * @[11] 0x5c7c385b nnotes.NetCheckNotesINI@4+2715 (0)
	 * @[12] 0x5cf60378 nnotes.UAFGetAccessGroupsExtend2@12+120 (0,8,0)
	 * @[13] 0x5cf60e77 nnotes.CreateNamesListFromNamesExtend2@20+263 (0,8,604cf01c,604cf028,0)
	 * @[14] 0x5cf60fca nnotes.CreateNamesListFromNamesExtend@16+26 (0,1,604cf01c,604cf028)
	 * @[15] 0x61229378 nlsxbe.ANSession::ANSCreateUserGroupListExtended+344 (0,0,0,0)
	 * @[16] 0x611f168f nlsxbe.ANDatabase::ANDOpen+415 (0,0,0,5f01a954)
	 * @[17] 0x611fc26e nlsxbe.ANDatabase::ANDOpen+350 (0,0,0,0)
	 * @[18] 0x6122aae0 nlsxbe.ANSession::ANSFindOrCreateDBNoClean+1040 (5f01a91c,5f01a924,5f01a954,1)
	 * @[19] 0x61231854 nlsxbe.ANSession::ANSFindOrCreateDB+100 (5f01a91c,5f01a924,0,1)
	 * @[20] 0x612326e2 nlsxbe.Java_lotus_domino_local_Session_NgetDatabase@20+338 (27,5c00634d,7fec41d0,5c011fc8,0)
	 *
	 * See also: http://www-01.ibm.com/support/docview.wss?uid=swg1LO39865 (
	 */
	private static final Object getDB_lock = new Object();

	/** The default session. */

	// RPr: this "should" be the same as Factory.getSession()
	//	private static ThreadLocal<Session> defaultSession = new ThreadLocal<Session>() {
	//		@Override
	//		protected Session initialValue() {
	//			return null;
	//		}
	//	};

	// RPr: removed as this is never set to "true" and makes externalize easier
	// private boolean isDbCached_ = false;

	private transient Database currentDatabase_;
	private transient Boolean isConvertMime_;
	private String currentDatabaseApiPath_;

	private String username_;

	private Set<Fixes> fixes_ = EnumSet.noneOf(Fixes.class);

	@Override
	public boolean isFixEnabled(final Fixes fix) {
		return fixes_.contains(fix);
	}

	@Override
	public void setFixEnable(final Fixes fix, final boolean value) {
		if (value) {
			fixes_.add(fix);
		} else {
			fixes_.remove(fix);
		}
	}

	private boolean featureRestricted_ = false;

	// RPr: the only way to get a session is from the factory. so commented out
	//	/**
	//	 * Instantiates a new session.
	//	 */
	//	public Session() {
	//		// TODO come up with some static methods for finding a Session based on run context (XPages, Agent, DOTS, etc)
	//		super(null, null);
	//	}
	//	@Deprecated
	//	@SuppressWarnings("rawtypes")
	//	public Session(final lotus.domino.Session lotus, final org.openntf.domino.Base parent) {
	//		this(lotus, null, null, 0L);
	//	}

	// FIXME NTF - not sure if there's a context where this makes sense...
	/**
	 * Instantiates a new session.
	 *
	 * @param lotus
	 *            the lotus
	 * @param parent
	 *            the parent. is always NULL
	 * @param wf
	 *            the wrapperFactory
	 * @param cpp_id
	 *            the cpp-id
	 */
	protected Session(final lotus.domino.Session lotus, final WrapperFactory parent) {
		super(lotus, parent, NOTES_SESSION);
		initialize(lotus);
		featureRestricted_ = false;// currently not implemented
	}

	/**
	 * Initialize.
	 *
	 * @param session
	 *            the session
	 */
	private void initialize(final lotus.domino.Session session) {
		setFixEnable(Fixes.DOC_UNID_NULLS, true);
		try {
			username_ = session.getEffectiveUserName();
			formatter_ = new DominoFormatter(getInternational());
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/**
	 * Gets the formatter.
	 *
	 * @return the formatter
	 */
	@Override
	public DominoFormatter getFormatter() {
		return formatter_;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#createAdministrationProcess(java.lang.String)
	 */
	@Override
	public AdministrationProcess createAdministrationProcess(final String server) {
		try {
			return fromLotus(getDelegate().createAdministrationProcess(server), AdministrationProcess.SCHEMA, this);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne, this);

		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#createColorObject()
	 */
	@Override
	public ColorObject createColorObject() {
		try {
			return fromLotus(getDelegate().createColorObject(), ColorObject.SCHEMA, this);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne, this);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#createColorObject(java.awt.Color)
	 */
	@Override
	public ColorObject createColorObject(final Color color) {
		ColorObject result = this.createColorObject();
		result.setRGB(color.getRed(), color.getGreen(), color.getBlue());
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#createDateRange()
	 */
	@Override
	public DateRange createDateRange() {
		return getFactory().create(DateRange.SCHEMA, this, null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#createDateRange(java.util.Date, java.util.Date)
	 */
	@Override
	public DateRange createDateRange(final Date startTime, final Date endTime) {
		return createDateRange(createDateTime(startTime), createDateTime(endTime));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#createDateRange(lotus.domino.DateTime, lotus.domino.DateTime)
	 */
	@Override
	public DateRange createDateRange(final lotus.domino.DateTime startTime, final lotus.domino.DateTime endTime) {
		DateRange ret = getFactory().create(DateRange.SCHEMA, this, null);
		ret.setStartDateTime(startTime);
		ret.setEndDateTime(endTime);
		return ret;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#createDateTime(java.util.Calendar)
	 */
	@Override
	public DateTime createDateTime(final java.util.Calendar date) {
		DateTime ret = getFactory().create(DateTime.SCHEMA, this, null);
		ret.setLocalTime(date);
		return ret;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#createDateTime(java.util.Calendar)
	 */
	@Override
	public DateTime createDateTime(final Calendar date) {
		DateTime ret = getFactory().create(DateTime.SCHEMA, this, null);
		ret.setLocalTime(date);
		return ret;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#createDateTime(java.util.Date)
	 */
	@Override
	public DateTime createDateTime(final Date date) {
		DateTime ret = getFactory().create(DateTime.SCHEMA, this, null);
		ret.setLocalTime(date);
		return ret;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#createDateTime(int, int, int, int, int, int)
	 */
	@Override
	public DateTime createDateTime(final int y, final int m, final int d, final int h, final int i, final int s) {
		Calendar cal = Calendar.getInstance();
		cal.set(y, m - 1, d, h, i, s);
		return createDateTime(cal);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#createDateTime(java.lang.String)
	 */
	@Override
	public DateTime createDateTime(final String date) {
		DateTime ret = getFactory().create(DateTime.SCHEMA, this, null);
		ret.setLocalTime(date);
		return ret;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#createDxlExporter()
	 */
	@Override
	public DxlExporter createDxlExporter() {
		try {
			return fromLotus(getDelegate().createDxlExporter(), DxlExporter.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#createDxlImporter()
	 */
	@Override
	public DxlImporter createDxlImporter() {
		try {
			return fromLotus(getDelegate().createDxlImporter(), DxlImporter.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#createLog(java.lang.String)
	 */
	@Override
	public Log createLog(final String name) {
		try {
			return fromLotus(getDelegate().createLog(name), Log.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#createName(java.lang.String, java.lang.String)
	 */
	@Override
	public Name createName(final String name, final String lang) {
		if (lang == null) {
			return createName(name);
		}
		String[] meta = new String[2];
		meta[0] = name;
		meta[1] = lang;
		return getFactory().create(Name.SCHEMA, this, meta);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#createName(java.lang.String)
	 */
	@Override
	public org.openntf.domino.Name createName(final String name) {
		return getFactory().create(Name.SCHEMA, this, name);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#createName(java.lang.String)
	 */
	@Override
	public org.openntf.domino.Name createNameNonODA(final String name) {
		try {
			return fromLotus(getDelegate().createName(name), Name.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#createNewsletter(lotus.domino.DocumentCollection)
	 */
	@Override
	public Newsletter createNewsletter(final lotus.domino.DocumentCollection collection) {
		try {
			return fromLotus(getDelegate().createNewsletter(toLotus(collection)), Newsletter.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#createRegistration()
	 */
	@Override
	public Registration createRegistration() {
		try {
			return fromLotus(getDelegate().createRegistration(), Registration.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#createRichTextParagraphStyle()
	 */
	@Override
	public RichTextParagraphStyle createRichTextParagraphStyle() {
		try {
			return fromLotus(getDelegate().createRichTextParagraphStyle(), RichTextParagraphStyle.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#createRichTextStyle()
	 */
	@Override
	public RichTextStyle createRichTextStyle() {
		try {
			return fromLotus(getDelegate().createRichTextStyle(), RichTextStyle.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#createStream()
	 */
	@Override
	public Stream createStream() {
		try {
			return fromLotus(getDelegate().createStream(), Stream.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#evaluate(java.lang.String, lotus.domino.Document)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public Vector<Object> evaluate(final String formula, final lotus.domino.Document doc) {
		try {
			//			// TODO RPr: Make an option to enable/disable formula engine
			//			if (doc instanceof Map || doc == null) {
			//				List<Object> ret = Formulas.evaluate(formula, (Map<String, Object>) doc);
			//				return new Vector(ret);
			//			}

			if (doc instanceof Document) {
				String lf = formula.toLowerCase();
				if (lf.contains("field ") || lf.contains("@setfield")) {
					((Document) doc).markDirty();// the document MAY get dirty by evaluate...
				}
			}
			lotus.domino.Session lsession = getDelegate();
			Vector<Object> result = null;

			if (doc == null) {
				try {
					result = lsession.evaluate(formula);
				} catch (NotesException ne1) {
					result = new Vector<Object>();
					result.add("ERROR: " + ne1.text);
				}
			} else {
				lotus.domino.Document ldoc = toLotus(doc);
				try {
					result = lsession.evaluate(formula, ldoc);
				} catch (NotesException ne1) {
					result = new Vector<Object>();
					result.add("ERROR: " + ne1.text);
				}
			}

			if (result == null) {
				return null;//this really shouldn't be possible.
			}
			return wrapColumnValues(result, this);
		} catch (Exception e) {
			DominoUtils.handleException(e, this);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#evaluate(java.lang.String)
	 */
	@Override
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public Vector<Object> evaluate(final String formula) {
		return evaluate(formula, null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#freeTimeSearch(lotus.domino.DateRange, int, java.lang.Object, boolean)
	 */
	@Override
	@Legacy(Legacy.INTERFACES_WARNING)
	public Vector<DateRange> freeTimeSearch(final lotus.domino.DateRange window, final int duration, final Object names,
			final boolean firstFit) {
		try {
			lotus.domino.DateRange dr = toLotus(window);
			Vector<DateRange> ret = fromLotusAsVector(getDelegate().freeTimeSearch(dr, duration, names, firstFit), DateRange.SCHEMA, this);
			if (window instanceof Encapsulated) {
				s_recycle(dr);
			}
			return ret;
		} catch (Exception e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#getAddressBooks()
	 */
	@Override
	@Legacy(Legacy.INTERFACES_WARNING)
	public Vector<org.openntf.domino.Database> getAddressBooks() {
		try {
			return fromLotusAsVector(getDelegate().getAddressBooks(), Database.SCHEMA, this);
		} catch (Exception e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#getAgentContext()
	 */
	@Override
	public AgentContext getAgentContext() {
		try {
			return fromLotus(getDelegate().getAgentContext(), AgentContext.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#getCalendar(lotus.domino.Database)
	 */
	@Override
	public NotesCalendar getCalendar(final lotus.domino.Database db) {
		try {
			//			Database parentDb = null;
			//			if (db instanceof Database) {
			//				parentDb = (Database) db;
			//			} else {
			//				parentDb = fromLotus(db, Database.SCHEMA, this);
			//			}
			return fromLotus(getDelegate().getCalendar(toLotus(db)), NotesCalendar.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#getCommonUserName()
	 */
	@Override
	public String getCommonUserName() {
		try {
			return getDelegate().getCommonUserName();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#getCredentials()
	 */
	@Override
	public Object getCredentials() {
		try {
			return getDelegate().getCredentials();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#getCurrentDatabase()
	 */
	@Override
	public Database getCurrentDatabase() {
		Database result = null;
		if (currentDatabaseApiPath_ == null) {
			//			System.out.println("TEMP DEBUG: currentDatabase_ is null for session " + System.identityHashCode(this) + " in thread "
			//					+ System.identityHashCode(Thread.currentThread()) + " so we're trying to derive it...");
			try {
				result = fromLotus(getDelegate().getCurrentDatabase(), Database.SCHEMA, this);
				if (result == null) {
					//					System.out.println("TEMP DEBUG: returning null for getCurrentDatabase() from session " + System.identityHashCode(this)
					//							+ " in thread " + System.identityHashCode(Thread.currentThread()));
					return null;
				}

				currentDatabase_ = result;
				currentDatabaseApiPath_ = result.getApiPath();
			} catch (NotesException e) {
				DominoUtils.handleException(e, this);
				return null;

			}
		} else {
			if (currentDatabase_ == null) {
				currentDatabase_ = getDatabase(currentDatabaseApiPath_);
			}
			result = currentDatabase_;
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#getDatabase(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public org.openntf.domino.Database getDatabase(final String server, final String db, final boolean createOnFail) {
		// Handle quickly the case of .getDatabase("", "")
		if ((server == null || server.isEmpty()) && (db == null || db.isEmpty())) {
			try {
				synchronized (getDB_lock) {
					return fromLotus(getDelegate().getDatabase("", ""), Database.SCHEMA, this);
				}
			} catch (NotesException e) {
				DominoUtils.handleException(e, this);
				return null;
			}
		}

		if (db == null || db.isEmpty()) {
			throw new IllegalArgumentException("Filepath argument cannot be null or empty string unless the server is also empty.");
		}

		// try {
		lotus.domino.Database database = null;
		org.openntf.domino.Database result = null;

		if (result == null) {
			try {
				boolean isDbRepId = DominoUtils.isReplicaId(db);
				if (isDbRepId) {
					lotus.domino.Database nullDb;
					synchronized (getDB_lock) {
						nullDb = getDelegate().getDatabase(null, null);
					}
					boolean opened = nullDb.openByReplicaID(server, db);
					if (opened) {
						result = fromLotus(nullDb, Database.SCHEMA, this);
					} else {
						s_recycle(nullDb);
						result = null;
					}
				} else {
					synchronized (getDB_lock) {
						database = getDelegate().getDatabase(server, db, createOnFail);
					}
					result = fromLotus(database, Database.SCHEMA, this);
				}
				//				if (isDbCached_ && result != null) {
				//					databases_.put(key, result);
				//					if (isDbRepId) {
				//						databases_.put(result.getApiPath(), result);
				//					} else {
				//						databases_.put(result.getMetaReplicaID(), result);
				//					}
				//				}
			} catch (NotesException e) {
				if (e.id == NotesError.NOTES_ERR_DBNOACCESS) {
					throw new UserAccessException("User " + getEffectiveUserName() + " cannot open database " + db + " on server " + server,
							e);
				} else {
					DominoUtils.handleException(e, this);
					return null;
				}
			}
		}
		return result;
		// } catch (Exception e) {
		// DominoUtils.handleException(e);
		// return null;
		// }
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#getDatabase(java.lang.String, java.lang.String)
	 */
	@Override
	public org.openntf.domino.Database getDatabase(final String server, final String db) {
		return getDatabase(server, db, false);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.Session#getDatabase(java.lang.String)
	 */
	@Override
	public org.openntf.domino.Database getDatabase(final String apiPath) {
		String server = getServerName();
		String dbpath = apiPath;
		int sep = apiPath.indexOf("!!");
		if (sep > -1) {
			server = apiPath.substring(0, sep);
			dbpath = apiPath.substring(sep + 2);
		}
		return getDatabase(server, dbpath);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#getDbDirectory(java.lang.String)
	 */
	@Override
	public DbDirectory getDbDirectory(final String server) {
		try {
			return fromLotus(getDelegate().getDbDirectory(server), DbDirectory.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#getDirectory()
	 */
	@Override
	public Directory getDirectory() {
		try {
			return fromLotus(getDelegate().getDirectory(), Directory.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#getDirectory(java.lang.String)
	 */
	@Override
	public Directory getDirectory(final String server) {
		try {
			return fromLotus(getDelegate().getDirectory(server), Directory.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#getEffectiveUserName()
	 */
	@Override
	public String getEffectiveUserName() {
		try {
			return getDelegate().getEffectiveUserName();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#getEnvironmentString(java.lang.String, boolean)
	 */
	@Override
	public String getEnvironmentString(final String vname, final boolean isSystem) {
		try {
			return getDelegate().getEnvironmentString(vname, isSystem);
		} catch (NotesException e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#getEnvironmentString(java.lang.String)
	 */
	@Override
	public String getEnvironmentString(final String vname) {
		try {
			return getDelegate().getEnvironmentString(vname);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#getEnvironmentValue(java.lang.String, boolean)
	 */
	@Override
	public Object getEnvironmentValue(final String vname, final boolean isSystem) {
		try {
			return getDelegate().getEnvironmentValue(vname, isSystem);
		} catch (NotesException e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#getEnvironmentValue(java.lang.String)
	 */
	@Override
	public Object getEnvironmentValue(final String vname) {
		try {
			return getDelegate().getEnvironmentValue(vname);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#getHttpURL()
	 */
	@Override
	public String getHttpURL() {
		try {
			return getDelegate().getHttpURL();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#getInternational()
	 */
	@Override
	public International getInternational() {
		try {
			return fromLotus(getDelegate().getInternational(), International.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#getNotesVersion()
	 */
	@Override
	public String getNotesVersion() {
		try {
			return getDelegate().getNotesVersion();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#getOrgDirectoryPath()
	 */
	@Override
	public String getOrgDirectoryPath() {
		try {
			return getDelegate().getOrgDirectoryPath();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#getPlatform()
	 */
	@Override
	public String getPlatform() {
		try {
			return getDelegate().getPlatform();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#getPropertyBroker()
	 */
	@Override
	public PropertyBroker getPropertyBroker() {
		try {
			return fromLotus(getDelegate().getPropertyBroker(), PropertyBroker.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#getServerName()
	 */
	@Override
	public String getServerName() {
		try {
			return getDelegate().getServerName();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#getSessionToken()
	 */
	@Override
	public String getSessionToken() {
		try {
			return getDelegate().getSessionToken();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#getSessionToken(java.lang.String)
	 */
	@Override
	public String getSessionToken(final String serverName) {
		try {
			return getDelegate().getSessionToken(serverName);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#getURL()
	 */
	@Override
	public String getURL() {
		try {
			return getDelegate().getURL();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#getURLDatabase()
	 */
	@Override
	public Database getURLDatabase() {
		try {
			return fromLotus(getDelegate().getURLDatabase(), Database.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#getUserGroupNameList()
	 */
	@Override
	public Vector<org.openntf.domino.Name> getUserGroupNameList() {
		try {
			return fromLotusAsVector(getDelegate().getUserGroupNameList(), Name.SCHEMA, this);
		} catch (Exception e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#getUserName()
	 */
	@Override
	public String getUserName() {
		try {
			return getDelegate().getUserName();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#getUserNameList()
	 */
	@Override
	public Vector<org.openntf.domino.Name> getUserNameList() {
		try {
			return fromLotusAsVector(getDelegate().getUserNameList(), Name.SCHEMA, this);
		} catch (Exception e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#getUserNameObject()
	 */
	@Override
	public Name getUserNameObject() {
		return createName(getUserName());
		//		try {
		//			return fromLotus(getDelegate().getUserNameObject(), Name.SCHEMA, this);
		//		} catch (NotesException e) {
		//			DominoUtils.handleException(e, this);
		//			return null;
		//
		//		}
	}

	@Override
	public Name getEffectiveUserNameObject() {
		return createName(getEffectiveUserName());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#getUserPolicySettings(java.lang.String, java.lang.String, int, java.lang.String)
	 */
	@Override
	public Document getUserPolicySettings(final String server, final String name, final int type, final String explicitPolicy) {
		try {
			lotus.domino.Document doc = getDelegate().getUserPolicySettings(server, name, type, explicitPolicy);
			Database db = fromLotus(doc.getParentDatabase(), Database.SCHEMA, this);
			return fromLotus(doc, Document.SCHEMA, db);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#getUserPolicySettings(java.lang.String, java.lang.String, int)
	 */
	@Override
	public Document getUserPolicySettings(final String server, final String name, final int type) {
		try {
			lotus.domino.Document doc = getDelegate().getUserPolicySettings(server, name, type);
			Database db = fromLotus(doc.getParentDatabase(), Database.SCHEMA, this);
			return fromLotus(doc, Document.SCHEMA, db);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#hashPassword(java.lang.String)
	 */
	@Override
	public String hashPassword(final String password) {
		try {
			return getDelegate().hashPassword(password);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#isConvertMIME()
	 */
	@Override
	public boolean isConvertMIME() {
		if (isConvertMime_ == null) {
			try {
				isConvertMime_ = Boolean.valueOf(getDelegate().isConvertMIME());
			} catch (NotesException e) {
				DominoUtils.handleException(e, this);
				isConvertMime_ = Boolean.FALSE;

			}
		}
		return isConvertMime_.booleanValue();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#isConvertMime()
	 */
	@Override
	public boolean isConvertMime() {
		return isConvertMIME();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#isOnServer()
	 */
	@Override
	public boolean isOnServer() {
		try {
			return getDelegate().isOnServer();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#isRestricted()
	 */
	@Override
	public boolean isRestricted() {
		try {
			return getDelegate().isRestricted();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return true;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#isTrackMillisecInJavaDates()
	 */
	@Override
	public boolean isTrackMillisecInJavaDates() {
		try {
			return getDelegate().isTrackMillisecInJavaDates();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return true;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#isTrustedSession()
	 */
	@Override
	public boolean isTrustedSession() {
		try {
			return getDelegate().isTrustedSession();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#isValid()
	 */
	@Override
	public boolean isValid() {
		return getDelegate().isValid();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#resetUserPassword(java.lang.String, java.lang.String, java.lang.String, int)
	 */
	@Override
	public boolean resetUserPassword(final String serverName, final String userName, final String password, final int downloadCount) {
		try {
			return getDelegate().resetUserPassword(serverName, userName, password, downloadCount);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#resetUserPassword(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean resetUserPassword(final String serverName, final String userName, final String password) {
		try {
			return getDelegate().resetUserPassword(serverName, userName, password);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);

		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#resolve(java.lang.String)
	 */
	@Override
	public org.openntf.domino.Base<?> resolve(final String url) {
		try {
			// This will return either a Database, View, Form, Document, or Agent
			// All but the first require finding the parent database
			lotus.domino.Base result = getDelegate().resolve(url);
			if (result == null) {
				return null;
			} else {
				// this should work now for Form, View, Document, Agent, Database
				return fromLotus(result, null, null);
			}
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#sendConsoleCommand(java.lang.String, java.lang.String)
	 */
	@Override
	public String sendConsoleCommand(final String serverName, final String consoleCommand) {
		try {
			return getDelegate().sendConsoleCommand(serverName, consoleCommand);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#setAllowLoopBack(boolean)
	 */
	@Override
	public void setAllowLoopBack(final boolean flag) {
		try {
			getDelegate().setAllowLoopBack(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#setConvertMIME(boolean)
	 */
	@Override
	public void setConvertMIME(final boolean flag) {
		try {
			isConvertMime_ = Boolean.valueOf(flag);
			getDelegate().setConvertMIME(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#setConvertMime(boolean)
	 */
	@Override
	public void setConvertMime(final boolean flag) {
		setConvertMIME(flag);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#setEnvironmentVar(java.lang.String, java.lang.Object, boolean)
	 */
	@Override
	public void setEnvironmentVar(final String vname, final Object value, final boolean isSystem) {
		try {
			getDelegate().setEnvironmentVar(vname, value, isSystem);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#setEnvironmentVar(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setEnvironmentVar(final String vname, final Object value) {
		try {
			getDelegate().setEnvironmentVar(vname, value);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#setTrackMillisecInJavaDates(boolean)
	 */
	@Override
	public void setTrackMillisecInJavaDates(final boolean flag) {
		try {
			getDelegate().setTrackMillisecInJavaDates(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#verifyPassword(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean verifyPassword(final String password, final String hashedPassword) {
		try {
			return getDelegate().verifyPassword(password, hashedPassword);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#getUserGroupNameCollection()
	 */
	@Override
	public Collection<String> getUserGroupNameCollection() {
		Collection<String> result = new ArrayList<String>();
		java.util.Vector<org.openntf.domino.Name> v = this.getUserGroupNameList();
		if (!v.isEmpty()) {
			for (org.openntf.domino.Name name : v) {
				result.add(name.getCanonical());
				// DominoUtils.incinerate(name);
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#getUserNameCollection()
	 */
	@Override
	public Collection<String> getUserNameCollection() {
		Collection<String> result = new ArrayList<String>();
		Vector<org.openntf.domino.Name> v = this.getUserNameList();
		if (!v.isEmpty()) {
			for (org.openntf.domino.Name name : v) {
				result.add(name.getCanonical());
				// DominoUtils.incinerate(name);
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#getAddressBookCollection()
	 */
	@Override
	public Collection<org.openntf.domino.Database> getAddressBookCollection() {
		Collection<org.openntf.domino.Database> result = new ArrayList<org.openntf.domino.Database>();
		Vector<org.openntf.domino.Database> v = this.getAddressBooks();
		if (!v.isEmpty()) {
			for (org.openntf.domino.Database db : v) {
				result.add(db);
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#freeTimeSearch(org.openntf.domino.DateRange, int, java.lang.String, boolean)
	 */
	@Override
	public Collection<org.openntf.domino.DateRange> freeTimeSearch(final org.openntf.domino.DateRange window, final int duration,
			final String names, final boolean firstFit) {
		// TODO verify that we don't end up with an ambiguous method signature
		Collection<org.openntf.domino.DateRange> result = new ArrayList<org.openntf.domino.DateRange>();
		Vector<org.openntf.domino.DateRange> v = this.freeTimeSearch((lotus.domino.DateRange) window, duration, (Object) names, firstFit);
		if (!v.isEmpty()) {
			for (org.openntf.domino.DateRange dr : v) {
				result.add(dr);
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Session#freeTimeSearch(org.openntf.domino.DateRange, int, java.util.Collection, boolean)
	 */
	@Override
	public Collection<org.openntf.domino.DateRange> freeTimeSearch(final org.openntf.domino.DateRange window, final int duration,
			final Collection<String> names, final boolean firstFit) {
		// TODO verify that we don't end up with an ambiguous method signature
		Collection<org.openntf.domino.DateRange> result = new ArrayList<org.openntf.domino.DateRange>();
		Vector<org.openntf.domino.DateRange> v = this.freeTimeSearch((lotus.domino.DateRange) window, duration,
				(Object) new java.util.Vector<String>(names), firstFit);
		if (!v.isEmpty()) {
			for (org.openntf.domino.DateRange dr : v) {
				result.add(dr);
			}
		}
		return result;
	}

	private org.openntf.domino.Session recreateSession() {
		switch (sessionType_) {
		case _NAMED_FULL_ACCESS_internal:
			return Factory.getNamedSession(username_, true);
		case _NAMED_internal:
			return Factory.getNamedSession(username_, false);
		default:
			return Factory.getSession(sessionType_);
		}
	}

	@Override
	public void switchSessionType(final SessionType type) {
		sessionType_ = type;
		identCleared_ = true;
	}

	@Override
	public void resurrect() {// should only happen if the delegate has been destroyed somehow.
		// TODO: Currently gets session. Need to get session, sessionAsSigner or sessionAsSignerWithFullAccess, as appropriate somwhow
		isConvertMime_ = null;
		org.openntf.domino.Session sess = recreateSession();

		if (!(sess instanceof Session)) {
			throw new UnableToAcquireSessionException("SessionFactory could not return a Session");
		}

		getFactory().setNoRecycle(sess, false);

		lotus.domino.Session d = ((Session) sess).getDelegate_unchecked();
		if (d == null) {
			throw new UnableToAcquireSessionException("The created Session does not have a valid delegate");
		}
		try {

			if (!identCleared_ && !username_.equals(d.getEffectiveUserName())) {
				if ("Anonymous".equalsIgnoreCase(username_) || SessionType.CURRENT == sessionType_) {
					//NTF if the session used to be Anonymous, it's legitimate for it to change
					//and if the sessionType is CURRENT, then it's legitimate for the name to have changed.
					username_ = d.getEffectiveUserName();
				} else {
					throw new UnableToAcquireSessionException("The created Session has the wrong user name. (given:"
							+ d.getEffectiveUserName() + ", expected:" + username_ + " for a session type " + sessionType_.name());
				}
			}
		} catch (NotesException e) {
		}
		identCleared_ = false;
		setDelegate(d, true);
		/* No special logging, since by now Session is a BaseThreadSafe */
	}

	//	/*
	//	 * (non-Javadoc)
	//	 *
	//	 * @see org.openntf.domino.impl.Base#getDelegate()
	//	 */
	//	@Override
	//	protected lotus.domino.Session getDelegate() {
	//		lotus.domino.Session session = super.getDelegate();
	//		if (isDead(session)) {
	//			log_.log(Level.WARNING, "Dead session found. Will try to recreate it.");
	//			try {
	//				Session sessionImpl = (Session) getSessionFactory().createSession();
	//				if (sessionImpl != null) {
	//					lotus.domino.Session sessionLotus = sessionImpl.delegate_;
	//					if (sessionLotus != null) {
	//						getFactory().recacheLotusObject(sessionLotus, this, null);
	//						setDelegate(sessionLotus, 0);
	//					} else {
	//						throw new UnableToAcquireSessionException("Factory default Session does not have a valid delegate");
	//					}
	//				} else {
	//					throw new UnableToAcquireSessionException("Factory could not return a default Session");
	//				}
	//			} catch (PrivilegedActionException e) {
	//
	//			}
	//		} else if (session == null) {
	//			throw new UnableToAcquireSessionException(
	//					"This session has a null value for its delegate. How was it created in the first place?");
	//		}
	//		return super.getDelegate();
	//	}

	private IDominoEventFactory eventFactory_;

	private AutoMime isAutoMime_;

	private SessionType sessionType_;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.ext.Session#getEventFactory()
	 */
	@Override
	public IDominoEventFactory getEventFactory() {
		if (eventFactory_ == null) {
			eventFactory_ = new GenericDominoEventFactory();
		}
		return eventFactory_;
	}

	@Override
	public void setEventFactory(final IDominoEventFactory factory) {
		eventFactory_ = factory;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public IDominoEvent generateEvent(final EnumEvent event, final org.openntf.domino.Base source, final org.openntf.domino.Base target,
			final Object payload) {
		return getEventFactory().generate(event, source, target, payload);
	}

	@Override
	@Deprecated
	//use DominoUtils.toCommonName(String) instead
	public String toCommonName(final String name) {
		org.openntf.domino.Name lname = createName(name);
		if (lname.isHierarchical()) {
			return lname.getCommon();
		} else {
			return name;
		}
	}

	@Override
	public void boogie() {
		StringBuilder sb = new StringBuilder();
		sb.append("(_|_)");
		sb.append(" ");
		sb.append("(_/_)");
		sb.append(" ");
		sb.append("(_|_)");
		sb.append(" ");
		sb.append("(_\\_)");
		sb.append(" ");
		System.out.println(sb.toString());
		System.out.println(sb.toString());
		System.out.println(sb.toString());
	}

	private boolean identCleared_ = false;

	@Override
	public void clearIdentity() {
		identCleared_ = true;
	}

	/* (non-Javadoc)
	 * @see lotus.domino.Session#freeResourceSearch(lotus.domino.DateTime, lotus.domino.DateTime, java.lang.String, int, int)
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Vector freeResourceSearch(final lotus.domino.DateTime arg0, final lotus.domino.DateTime arg1, final String arg2, final int arg3,
			final int arg4) {
		try {
			return getDelegate().freeResourceSearch(arg0, arg1, arg2, arg3, arg4);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see lotus.domino.Session#freeResourceSearch(lotus.domino.DateTime, lotus.domino.DateTime, java.lang.String, int, int, java.lang.String, int, java.lang.String, java.lang.String, int)
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Vector freeResourceSearch(final lotus.domino.DateTime arg0, final lotus.domino.DateTime arg1, final String arg2, final int arg3,
			final int arg4, final String arg5, final int arg6, final String arg7, final String arg8, final int arg9) {
		try {
			return getDelegate().freeResourceSearch(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.Session#getUnique()
	 */
	@Override
	public String getUnique() {
		String result = "";
		try {
			Vector<?> v = getDelegate().evaluate("@Unique");
			result = String.valueOf(v.get(0));
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
		return result;
	}

	@Override
	public org.openntf.domino.Database getDatabaseByReplicaID(final String server, final String replicaid) {
		try {
			lotus.domino.Database nullDb;
			synchronized (getDB_lock) {
				nullDb = getDelegate().getDatabase(null, null);
			}
			boolean opened = nullDb.openByReplicaID(server, replicaid);
			if (opened) {
				return fromLotus(nullDb, Database.SCHEMA, this);
			} else {
				s_recycle(nullDb);
			}

		} catch (NotesException ne) {
			DominoUtils.handleException(ne, this);
		}
		return null;
	}

	@Override
	public org.openntf.domino.Database getDatabaseWithFailover(final String server, final String dbfile) {
		try {
			lotus.domino.Database nullDb;
			synchronized (getDB_lock) {
				nullDb = getDelegate().getDatabase(null, null);
			}
			boolean opened = nullDb.openWithFailover(server, dbfile);
			if (opened) {
				return fromLotus(nullDb, Database.SCHEMA, this);
			} else {
				s_recycle(nullDb);
			}

		} catch (NotesException ne) {
			DominoUtils.handleException(ne, this);
		}
		return null;
	}

	@Override
	public org.openntf.domino.Database getDatabaseIfModified(final String server, final String dbfile,
			final lotus.domino.DateTime modifiedsince) {
		try {
			lotus.domino.Database nullDb;
			synchronized (getDB_lock) {
				nullDb = getDelegate().getDatabase(null, null);
			}
			boolean opened = nullDb.openIfModified(server, dbfile, modifiedsince);
			if (opened) {
				return fromLotus(nullDb, Database.SCHEMA, this);
			} else {
				s_recycle(nullDb);
			}

		} catch (NotesException ne) {
			DominoUtils.handleException(ne, this);
		}
		return null;
	}

	@Override
	public org.openntf.domino.Database getDatabaseIfModified(final String server, final String dbfile, final Date modifiedsince) {
		try {
			lotus.domino.Database nullDb;
			synchronized (getDB_lock) {
				nullDb = getDelegate().getDatabase(null, null);
			}
			lotus.domino.DateTime dt = createDateTime(modifiedsince);
			boolean opened = nullDb.openIfModified(server, dbfile, dt);
			s_recycle(dt);
			if (opened) {
				return fromLotus(nullDb, Database.SCHEMA, this);
			} else {
				s_recycle(nullDb);
			}

		} catch (NotesException ne) {
			DominoUtils.handleException(ne, this);
		}
		return null;
	}

	@Override
	public org.openntf.domino.Database getMailDatabase() {
		try {
			lotus.domino.DbDirectory rawDir = getDelegate().getDbDirectory(null);
			lotus.domino.Database rawdb = rawDir.openMailDatabase();
			s_recycle(rawDir);
			if (rawdb != null) {
				return fromLotus(rawdb, Database.SCHEMA, this);
			}
		} catch (NotesException ne) {
			DominoUtils.handleException(ne, this);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.Session#getDocumentByMetaversalID(java.lang.String)
	 */
	@Override
	public org.openntf.domino.Document getDocumentByMetaversalID(final String metaversalID) {
		String serverName = "";
		String id = "";
		if (metaversalID.contains("!!")) {
			int pos = metaversalID.indexOf("!!");
			serverName = metaversalID.substring(0, pos);
			id = metaversalID.substring(pos + 2);
		} else {
			id = metaversalID;
		}
		return getDocumentByMetaversalID(serverName, id);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.Session#getDocumentByMetaversalID(java.lang.String, java.lang.String)
	 */
	@Override
	public org.openntf.domino.Document getDocumentByMetaversalID(final String serverName, final String metaversalID) {
		if (metaversalID.length() != 48) {
			throw new IllegalArgumentException(
					"MetaversalIDs must be 48 characters in length (16 for replicaID, 32 for unid). Value passed was " + metaversalID);
		}
		String replid = metaversalID.substring(0, 16);
		String unid = metaversalID.substring(16);
		org.openntf.domino.Database db = this.getDatabaseByReplicaID(serverName, replid);
		org.openntf.domino.Document doc = db.getDocumentByUNID(unid);
		return doc;
	}

	@Override
	public AutoMime getAutoMime() {
		if (isAutoMime_ == null) {
			//NTF default behavior is for it to be on, so you have to globally turn it off
			return AutoMime.WRAP_ALL;
		} else {
			//NTF unless you've set it on this Session
			return isAutoMime_;
		}
	}

	@Override
	public void setAutoMime(final AutoMime autoMime) {
		isAutoMime_ = autoMime;
	}

	@Override
	public boolean isFeatureRestricted() {
		return featureRestricted_;
	}

	@Override
	public boolean isAnonymous() {
		return "Anonymous".equals(getEffectiveUserName());
	}

	@Override
	public void setCurrentDatabase(final Database db) {
		//		System.out.println("TEMP DEBUG: Setting current database to " + db.getApiPath() + " from session " + System.identityHashCode(this)
		//				+ " in thread " + System.identityHashCode(Thread.currentThread()));
		currentDatabase_ = db;
		currentDatabaseApiPath_ = db.getApiPath();
	}

	@Override
	public void fillExceptionDetails(final List<ExceptionDetails.Entry> result) {
		String userName;
		try {
			userName = getDelegate().getEffectiveUserName();
		} catch (NotesException e) {
			userName = "[getEffectiveUserName -> NotesException: " + e.text + "]";
		}
		result.add(new ExceptionDetails.Entry(this, userName));
	}

	@Override
	public Fixes[] getEnabledFixes() {
		// TODO Auto-generated method stub
		return fixes_.toArray(new Fixes[fixes_.size()]);
	}

	@Override
	public void setSessionType(final SessionType sessionType) {
		if (sessionType_ != null) {
			throw new IllegalStateException("SessionType cannot be changed");
		}
		sessionType_ = sessionType;
	}

	// this is needed for factories that provide an external session
	boolean noRecycle;

	@Override
	public void setNoRecycle(final boolean value) {
		noRecycle = value;
	}

	@Override
	public void recycle() {
		if (noRecycle) {
			return;
		}
		super.recycle();
	}

	//-------------- Externalize/Deexternalize stuff ------------------
	private static final int EXTERNALVERSIONUID = 20141205;

	/**
	 * @deprecated needed for {@link Externalizable} - do not use!
	 */
	@Deprecated
	public Session() {
		super(NOTES_SESSION);
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		//super.writeExternal(out);
		//Session do not write SUPER
		out.writeInt(EXTERNALVERSIONUID);// data version

		getCurrentDatabase();// initializes the currentDatabaseApiPath_
		if (sessionType_ == null) {
			log_.warning("Serializing a session without a sessionType");
			out.writeObject(SessionType.CURRENT);
		} else {
			out.writeObject(sessionType_);
		}
		out.writeObject(username_);
		out.writeObject(currentDatabaseApiPath_);

		out.writeObject(isAutoMime_);
		out.writeObject(fixes_);
		out.writeObject(eventFactory_);
		out.writeBoolean(featureRestricted_);
		// out.writeObject(formatter_); not needed!
		// out.writeBoolean(noRecycle); not needed - done by factory

	}

	@SuppressWarnings("unchecked")
	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		//super.readExternal(in);
		parent = Factory.getWrapperFactory();
		int version = in.readInt();
		if (version != EXTERNALVERSIONUID) {
			throw new InvalidClassException("Cannot read dataversion " + version);
		}

		sessionType_ = (SessionType) in.readObject();
		username_ = (String) in.readObject();
		currentDatabaseApiPath_ = (String) in.readObject();

		isAutoMime_ = (AutoMime) in.readObject();
		fixes_ = (Set<Fixes>) in.readObject();
		eventFactory_ = (IDominoEventFactory) in.readObject();
		featureRestricted_ = in.readBoolean();

	}

	protected Object readResolve() {
		Session ret = (Session) recreateSession();
		readResolveCheck(isAutoMime_, ret.isAutoMime_);
		readResolveCheck(fixes_, ret.fixes_);
		readResolveCheck(eventFactory_, ret.eventFactory_);
		readResolveCheck(featureRestricted_, ret.featureRestricted_);
		return ret;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((currentDatabaseApiPath_ == null) ? 0 : currentDatabaseApiPath_.hashCode());
		result = prime * result + ((sessionType_ == null) ? 0 : sessionType_.hashCode());
		result = prime * result + ((username_ == null) ? 0 : username_.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof Session)) {
			return false;
		}
		Session other = (Session) obj;
		if (currentDatabaseApiPath_ == null) {
			if (other.currentDatabaseApiPath_ != null) {
				return false;
			}
		} else if (!currentDatabaseApiPath_.equals(other.currentDatabaseApiPath_)) {
			return false;
		}
		if (sessionType_ != other.sessionType_) {
			return false;
		}
		if (username_ == null) {
			if (other.username_ != null) {
				return false;
			}
		} else if (!username_.equals(other.username_)) {
			return false;
		}
		return true;
	}

	@Override
	public final WrapperFactory getFactory() {
		return parent;
	}
}

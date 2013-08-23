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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import lotus.domino.NotesException;

import org.openntf.domino.annotations.Legacy;
import org.openntf.domino.exceptions.UnableToAcquireSessionException;
import org.openntf.domino.exceptions.UserAccessException;
import org.openntf.domino.thread.DominoReferenceCounter;
import org.openntf.domino.utils.DominoFormatter;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

import com.ibm.icu.util.Calendar;

// TODO: Auto-generated Javadoc
//import lotus.domino.Name;

/**
 * The Class Session.
 * 
 * @author nfreeman
 */
public class Session extends org.openntf.domino.impl.Base<org.openntf.domino.Session, lotus.domino.Session> implements
		org.openntf.domino.Session {
	/** The Constant log_. */
	private static final Logger log_ = Logger.getLogger(Session.class.getName());

	/** The formatter_. */
	private static DominoFormatter formatter_;

	/** The default session. */
	private static ThreadLocal<Session> defaultSession = new ThreadLocal<Session>() {
		@Override
		protected Session initialValue() {
			return null;
		}
	};

	/** The lotus reference counter_. */
	private DominoReferenceCounter lotusReferenceCounter_ = new DominoReferenceCounter();

	private Set<Fixes> fixes_ = EnumSet.noneOf(Fixes.class);

	public boolean isFixEnabled(final Fixes fix) {
		return fixes_.contains(fix);
	}

	public void setFixEnable(final Fixes fix, final boolean value) {
		if (value) {
			fixes_.add(fix);
		} else {
			fixes_.remove(fix);
		}
	}

	/**
	 * Adds the id.
	 * 
	 * @param id
	 *            the id
	 * @return the int
	 */
	public int addId(final long id) {
		int result = lotusReferenceCounter_.increment(id);
		if (result > 8)
			log_.log(Level.INFO, "Currently tracking more than 8 references for " + id);
		return result;
	}

	/**
	 * Subtract id.
	 * 
	 * @param id
	 *            the id
	 * @return the int
	 */
	public int subtractId(final long id) {
		return lotusReferenceCounter_.decrement(id);
	}

	/**
	 * Gets the default session.
	 * 
	 * @return the default session
	 */
	public static Session getDefaultSession() {
		return defaultSession.get();
	}

	/**
	 * Instantiates a new session.
	 */
	public Session() {
		// TODO come up with some static methods for finding a Session based on run context (XPages, Agent, DOTS, etc)
		super(null, null);
	}

	// FIXME NTF - not sure if there's a context where this makes sense...
	/**
	 * Instantiates a new session.
	 * 
	 * @param lotus
	 *            the lotus
	 * @param parent
	 *            the parent
	 */
	public Session(final lotus.domino.Session lotus, final org.openntf.domino.Base<?> parent) {
		super(lotus, parent);
		initialize(lotus);
	}

	/**
	 * Initialize.
	 * 
	 * @param session
	 *            the session
	 */
	private void initialize(final lotus.domino.Session session) {
		try {
			formatter_ = new DominoFormatter(session.getInternational());
			if (defaultSession.get() == null) {
				defaultSession.set(this);
			}
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/**
	 * Gets the formatter.
	 * 
	 * @return the formatter
	 */
	public static DominoFormatter getFormatter() {
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
			return Factory.fromLotus(getDelegate().createAdministrationProcess(server), AdministrationProcess.class, this);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);

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
			return Factory.fromLotus(getDelegate().createColorObject(), ColorObject.class, this);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
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
		try {
			return Factory.fromLotus(getDelegate().createDateRange(), DateRange.class, this);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Session#createDateRange(java.util.Date, java.util.Date)
	 */
	@Override
	public DateRange createDateRange(final Date startTime, final Date endTime) {
		try {
			return Factory.fromLotus(getDelegate().createDateRange(startTime, endTime), DateRange.class, this);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Session#createDateRange(lotus.domino.DateTime, lotus.domino.DateTime)
	 */
	public DateRange createDateRange(final lotus.domino.DateTime startTime, final lotus.domino.DateTime endTime) {
		try {
			DateRange result;
			lotus.domino.DateTime dt1 = (lotus.domino.DateTime) toLotus(startTime);
			lotus.domino.DateTime dt2 = (lotus.domino.DateTime) toLotus(endTime);
			result = Factory.fromLotus(getDelegate().createDateRange(dt1, dt2), org.openntf.domino.DateRange.class, this);
			enc_recycle(dt1);
			enc_recycle(dt2);
			return result;
		} catch (Exception e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Session#createDateTime(java.util.Calendar)
	 */
	@Override
	public DateTime createDateTime(final java.util.Calendar date) {
		try {
			return Factory.fromLotus(getDelegate().createDateTime(date), DateTime.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Session#createDateTime(java.util.Calendar)
	 */
	@Override
	public DateTime createDateTime(final Calendar date) {
		return createDateTime(date.getTime());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Session#createDateTime(java.util.Date)
	 */
	@Override
	public DateTime createDateTime(final Date date) {
		try {
			return Factory.fromLotus(getDelegate().createDateTime(date), DateTime.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Session#createDateTime(java.lang.String)
	 */
	@Override
	public DateTime createDateTime(final String date) {
		try {
			return Factory.fromLotus(getDelegate().createDateTime(date), DateTime.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Session#createDxlExporter()
	 */
	@Override
	public DxlExporter createDxlExporter() {
		try {
			return Factory.fromLotus(getDelegate().createDxlExporter(), DxlExporter.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
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
			return Factory.fromLotus(getDelegate().createDxlImporter(), DxlImporter.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
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
			return Factory.fromLotus(getDelegate().createLog(name), Log.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
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
		try {
			return Factory.fromLotus(getDelegate().createName(name, lang), Name.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Session#createName(java.lang.String)
	 */
	@Override
	public org.openntf.domino.Name createName(final String name) {
		try {
			return Factory.fromLotus(getDelegate().createName(name), Name.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
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
			return Factory.fromLotus(getDelegate().createNewsletter((lotus.domino.DocumentCollection) toLotus(collection)),
					Newsletter.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
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
			return Factory.fromLotus(getDelegate().createRegistration(), Registration.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
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
			return Factory.fromLotus(getDelegate().createRichTextParagraphStyle(), RichTextParagraphStyle.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
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
			return Factory.fromLotus(getDelegate().createRichTextStyle(), RichTextStyle.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
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
			return Factory.fromLotus(getDelegate().createStream(), Stream.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
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
			return Factory.wrapColumnValues((Vector<Object>) getDelegate().evaluate(formula, (lotus.domino.Document) toLotus(doc)), this);
		} catch (Exception e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Session#evaluate(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public Vector<Object> evaluate(final String formula) {
		try {
			return Factory.wrapColumnValues((Vector<Object>) getDelegate().evaluate(formula), this);
		} catch (Exception e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Session#freeTimeSearch(lotus.domino.DateRange, int, java.lang.Object, boolean)
	 */
	@Override
	@Legacy(Legacy.INTERFACES_WARNING)
	public Vector<org.openntf.domino.DateRange> freeTimeSearch(final lotus.domino.DateRange window, final int duration, final Object names,
			final boolean firstFit) {
		try {
			return Factory.fromLotusAsVector(
					getDelegate().freeTimeSearch((lotus.domino.DateRange) toLotus(window), duration, names, firstFit),
					org.openntf.domino.DateRange.class, this);
		} catch (Exception e) {
			DominoUtils.handleException(e);
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
			return Factory.fromLotusAsVector(getDelegate().getAddressBooks(), org.openntf.domino.Database.class, this);
		} catch (Exception e) {
			DominoUtils.handleException(e);
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
			return Factory.fromLotus(getDelegate().getAgentContext(), AgentContext.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
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
			return Factory.fromLotus(getDelegate().getCalendar((lotus.domino.Database) toLotus(db)), NotesCalendar.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
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
			DominoUtils.handleException(e);
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
			DominoUtils.handleException(e);
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
		try {
			return Factory.fromLotus(getDelegate().getCurrentDatabase(), Database.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Session#getDatabase(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public Database getDatabase(final String server, final String db, final boolean createOnFail) {
		// try {
		lotus.domino.Database database = null;
		try {
			database = getDelegate().getDatabase(server, db, createOnFail);
		} catch (NotesException e) {
			String message = e.text;
			if (message.contains("cannot open database")) {
				throw new UserAccessException("User " + getEffectiveUserName() + " cannot open database " + db + " on server " + server, e);
			} else {
				DominoUtils.handleException(e);
				return null;
			}
		}
		return Factory.fromLotus(database, Database.class, this);
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
	public Database getDatabase(final String server, final String db) {
		return getDatabase(server, db, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Session#getDbDirectory(java.lang.String)
	 */
	@Override
	public DbDirectory getDbDirectory(final String server) {
		try {
			return Factory.fromLotus(getDelegate().getDbDirectory(server), DbDirectory.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
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
			return Factory.fromLotus(getDelegate().getDirectory(), Directory.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
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
			return Factory.fromLotus(getDelegate().getDirectory(server), Directory.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
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
			DominoUtils.handleException(e);
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
			DominoUtils.handleException(e);
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
			DominoUtils.handleException(e);
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
			DominoUtils.handleException(e);
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
			DominoUtils.handleException(e);
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
			DominoUtils.handleException(e);
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
			return Factory.fromLotus(getDelegate().getInternational(), International.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
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
			DominoUtils.handleException(e);
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
			DominoUtils.handleException(e);
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
			DominoUtils.handleException(e);
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
			return Factory.fromLotus(getDelegate().getPropertyBroker(), PropertyBroker.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
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
			DominoUtils.handleException(e);
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
			DominoUtils.handleException(e);
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
			DominoUtils.handleException(e);
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
			DominoUtils.handleException(e);
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
			return Factory.fromLotus(getDelegate().getURLDatabase(), Database.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
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
			return Factory.fromLotusAsVector(getDelegate().getUserGroupNameList(), org.openntf.domino.Name.class, this);
		} catch (Exception e) {
			DominoUtils.handleException(e);
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
			DominoUtils.handleException(e);
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
			return Factory.fromLotusAsVector(getDelegate().getUserNameList(), Name.class, this);
		} catch (Exception e) {
			DominoUtils.handleException(e);
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
		try {
			return Factory.fromLotus(getDelegate().getUserNameObject(), Name.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Session#getUserPolicySettings(java.lang.String, java.lang.String, int, java.lang.String)
	 */
	@Override
	public Document getUserPolicySettings(final String server, final String name, final int type, final String explicitPolicy) {
		try {
			return Factory.fromLotus(getDelegate().getUserPolicySettings(server, name, type, explicitPolicy), Document.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
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
			return Factory.fromLotus(getDelegate().getUserPolicySettings(server, name, type), Document.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
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
			DominoUtils.handleException(e);
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
		try {
			return getDelegate().isConvertMIME();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Session#isConvertMime()
	 */
	@Override
	public boolean isConvertMime() {
		try {
			return getDelegate().isConvertMime();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
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
			DominoUtils.handleException(e);
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
			DominoUtils.handleException(e);
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
			DominoUtils.handleException(e);
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
			DominoUtils.handleException(e);
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
			DominoUtils.handleException(e);
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
			DominoUtils.handleException(e);

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
			return Factory.fromLotus(getDelegate().resolve(url), Base.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
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
			DominoUtils.handleException(e);
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
			DominoUtils.handleException(e);

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
			getDelegate().setConvertMIME(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Session#setConvertMime(boolean)
	 */
	@Override
	public void setConvertMime(final boolean flag) {
		try {
			getDelegate().setConvertMime(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
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
			DominoUtils.handleException(e);

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
			DominoUtils.handleException(e);

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
			DominoUtils.handleException(e);

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
			DominoUtils.handleException(e);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.impl.Base#getDelegate()
	 */
	@Override
	protected lotus.domino.Session getDelegate() {
		lotus.domino.Session session = super.getDelegate();
		try {
			session.isConvertMIME();
		} catch (NotesException ne) {
			org.openntf.domino.impl.Session sessionImpl = (org.openntf.domino.impl.Session) Factory.getSession();
			if (sessionImpl != null) {
				lotus.domino.Session sessionLotus = sessionImpl.delegate_;
				if (sessionLotus != null) {
					setDelegate(sessionLotus);
				} else {
					throw new UnableToAcquireSessionException("Factory default Session does not have a valid delegate");
				}
			} else {
				throw new UnableToAcquireSessionException("Factory could not return a default Session");
			}
		}
		return super.getDelegate();
	}
}

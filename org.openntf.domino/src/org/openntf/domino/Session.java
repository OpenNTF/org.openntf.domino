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
package org.openntf.domino;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import lotus.domino.AdministrationProcess;
import lotus.domino.AgentContext;
import lotus.domino.ColorObject;
import lotus.domino.DateTime;
import lotus.domino.DbDirectory;
import lotus.domino.Directory;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.DxlExporter;
import lotus.domino.DxlImporter;
import lotus.domino.International;
import lotus.domino.Log;
import lotus.domino.Newsletter;
import lotus.domino.NotesCalendar;
import lotus.domino.PropertyBroker;
import lotus.domino.Registration;
import lotus.domino.RichTextParagraphStyle;
import lotus.domino.RichTextStyle;
import lotus.domino.Stream;

import org.openntf.domino.annotations.Legacy;

// TODO: Auto-generated Javadoc
/**
 * The Interface Session.
 */
public interface Session extends lotus.domino.Session, Base<lotus.domino.Session> {
	
	/* (non-Javadoc)
	 * @see lotus.domino.Session#evaluate(java.lang.String)
	 */
	@Override
	public Vector<Object> evaluate(String formula);

	/* (non-Javadoc)
	 * @see lotus.domino.Session#evaluate(java.lang.String, lotus.domino.Document)
	 */
	@Override
	public Vector<Object> evaluate(String formula, Document doc);

	/* (non-Javadoc)
	 * @see lotus.domino.Session#getAddressBooks()
	 */
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING })
	public Vector<Database> getAddressBooks();

	/**
	 * Gets the address book collection.
	 * 
	 * @return the address book collection
	 */
	public Collection<Database> getAddressBookCollection();

	/* (non-Javadoc)
	 * @see lotus.domino.Session#freeTimeSearch(lotus.domino.DateRange, int, java.lang.Object, boolean)
	 */
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING })
	public Vector<DateRange> freeTimeSearch(lotus.domino.DateRange window, int duration, Object names, boolean firstFit);

	/**
	 * Free time search.
	 * 
	 * @param window
	 *            the window
	 * @param duration
	 *            the duration
	 * @param names
	 *            the names
	 * @param firstFit
	 *            the first fit
	 * @return the collection
	 */
	public Collection<DateRange> freeTimeSearch(org.openntf.domino.DateRange window, int duration, String names, boolean firstFit);

	/**
	 * Free time search.
	 * 
	 * @param window
	 *            the window
	 * @param duration
	 *            the duration
	 * @param names
	 *            the names
	 * @param firstFit
	 *            the first fit
	 * @return the collection
	 */
	public Collection<DateRange> freeTimeSearch(org.openntf.domino.DateRange window, int duration, Collection<String> names,
			boolean firstFit);

	/* (non-Javadoc)
	 * @see lotus.domino.Session#getUserGroupNameList()
	 */
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING })
	public Vector<Name> getUserGroupNameList(); // TODO should we use a Vector of names? Or allow someone to request it as String-only so

	// there's no recycle burden?

	/**
	 * Gets the user group name collection.
	 * 
	 * @return the user group name collection
	 */
	public Collection<String> getUserGroupNameCollection();

	/* (non-Javadoc)
	 * @see lotus.domino.Session#getUserNameList()
	 */
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING })
	public Vector<Name> getUserNameList(); // TODO should we use a Vector of names? Or allow someone to request it as String-only so there's

	// no recycle burden?

	/**
	 * Gets the user name collection.
	 * 
	 * @return the user name collection
	 */
	public Collection<String> getUserNameCollection();

	/* (non-Javadoc)
	 * @see lotus.domino.Session#createAdministrationProcess(java.lang.String)
	 */
	@Override
	public AdministrationProcess createAdministrationProcess(String server);

	/* (non-Javadoc)
	 * @see lotus.domino.Session#createColorObject()
	 */
	@Override
	public ColorObject createColorObject();

	/* (non-Javadoc)
	 * @see lotus.domino.Session#createDateRange()
	 */
	@Override
	public lotus.domino.DateRange createDateRange();

	/* (non-Javadoc)
	 * @see lotus.domino.Session#createDateRange(java.util.Date, java.util.Date)
	 */
	@Override
	public lotus.domino.DateRange createDateRange(Date startTime, Date endTime);

	/* (non-Javadoc)
	 * @see lotus.domino.Session#createDateRange(lotus.domino.DateTime, lotus.domino.DateTime)
	 */
	@Override
	public lotus.domino.DateRange createDateRange(DateTime startTime, DateTime endTime);

	/* (non-Javadoc)
	 * @see lotus.domino.Session#createDateTime(java.util.Calendar)
	 */
	@Override
	public DateTime createDateTime(Calendar date);

	/* (non-Javadoc)
	 * @see lotus.domino.Session#createDateTime(java.util.Date)
	 */
	@Override
	public DateTime createDateTime(Date date);

	/* (non-Javadoc)
	 * @see lotus.domino.Session#createDateTime(java.lang.String)
	 */
	@Override
	public DateTime createDateTime(String date);

	/* (non-Javadoc)
	 * @see lotus.domino.Session#createDxlExporter()
	 */
	@Override
	public DxlExporter createDxlExporter();

	/* (non-Javadoc)
	 * @see lotus.domino.Session#createDxlImporter()
	 */
	@Override
	public DxlImporter createDxlImporter();

	/* (non-Javadoc)
	 * @see lotus.domino.Session#createLog(java.lang.String)
	 */
	@Override
	public Log createLog(String name);

	/* (non-Javadoc)
	 * @see lotus.domino.Session#createName(java.lang.String)
	 */
	@Override
	public Name createName(String name);

	/* (non-Javadoc)
	 * @see lotus.domino.Session#createName(java.lang.String, java.lang.String)
	 */
	@Override
	public Name createName(String name, String lang);

	/* (non-Javadoc)
	 * @see lotus.domino.Session#createNewsletter(lotus.domino.DocumentCollection)
	 */
	@Override
	public Newsletter createNewsletter(DocumentCollection collection);

	/* (non-Javadoc)
	 * @see lotus.domino.Session#createRegistration()
	 */
	@Override
	public Registration createRegistration();

	/* (non-Javadoc)
	 * @see lotus.domino.Session#createRichTextParagraphStyle()
	 */
	@Override
	public RichTextParagraphStyle createRichTextParagraphStyle();

	/* (non-Javadoc)
	 * @see lotus.domino.Session#createRichTextStyle()
	 */
	@Override
	public RichTextStyle createRichTextStyle();

	/* (non-Javadoc)
	 * @see lotus.domino.Session#createStream()
	 */
	@Override
	public Stream createStream();

	/* (non-Javadoc)
	 * @see lotus.domino.Session#getAgentContext()
	 */
	@Override
	public AgentContext getAgentContext();

	/* (non-Javadoc)
	 * @see lotus.domino.Session#getCalendar(lotus.domino.Database)
	 */
	@Override
	public NotesCalendar getCalendar(lotus.domino.Database db);

	/* (non-Javadoc)
	 * @see lotus.domino.Session#getCommonUserName()
	 */
	@Override
	public String getCommonUserName();

	/* (non-Javadoc)
	 * @see lotus.domino.Session#getCredentials()
	 */
	@Override
	public Object getCredentials();

	/* (non-Javadoc)
	 * @see lotus.domino.Session#getCurrentDatabase()
	 */
	@Override
	public Database getCurrentDatabase();

	/* (non-Javadoc)
	 * @see lotus.domino.Session#getDatabase(java.lang.String, java.lang.String)
	 */
	@Override
	public Database getDatabase(String server, String db);

	/* (non-Javadoc)
	 * @see lotus.domino.Session#getDatabase(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public Database getDatabase(String server, String db, boolean createOnFail);

	/* (non-Javadoc)
	 * @see lotus.domino.Session#getDbDirectory(java.lang.String)
	 */
	@Override
	public DbDirectory getDbDirectory(String server);

	/* (non-Javadoc)
	 * @see lotus.domino.Session#getDirectory()
	 */
	@Override
	public Directory getDirectory();

	/* (non-Javadoc)
	 * @see lotus.domino.Session#getDirectory(java.lang.String)
	 */
	@Override
	public Directory getDirectory(String server);

	/* (non-Javadoc)
	 * @see lotus.domino.Session#getEffectiveUserName()
	 */
	@Override
	public String getEffectiveUserName();

	/* (non-Javadoc)
	 * @see lotus.domino.Session#getEnvironmentString(java.lang.String)
	 */
	@Override
	public String getEnvironmentString(String vname);

	/* (non-Javadoc)
	 * @see lotus.domino.Session#getEnvironmentString(java.lang.String, boolean)
	 */
	@Override
	public String getEnvironmentString(String vname, boolean isSystem);

	/* (non-Javadoc)
	 * @see lotus.domino.Session#getEnvironmentValue(java.lang.String)
	 */
	@Override
	public Object getEnvironmentValue(String vname);

	/* (non-Javadoc)
	 * @see lotus.domino.Session#getEnvironmentValue(java.lang.String, boolean)
	 */
	@Override
	public Object getEnvironmentValue(String vname, boolean isSystem);

	/* (non-Javadoc)
	 * @see lotus.domino.Session#getHttpURL()
	 */
	@Override
	public String getHttpURL();

	/* (non-Javadoc)
	 * @see lotus.domino.Session#getInternational()
	 */
	@Override
	public International getInternational();

	/* (non-Javadoc)
	 * @see lotus.domino.Session#getNotesVersion()
	 */
	@Override
	public String getNotesVersion();

	/* (non-Javadoc)
	 * @see lotus.domino.Session#getOrgDirectoryPath()
	 */
	@Override
	public String getOrgDirectoryPath();

	/* (non-Javadoc)
	 * @see lotus.domino.Session#getPlatform()
	 */
	@Override
	public String getPlatform();

	/* (non-Javadoc)
	 * @see lotus.domino.Session#getPropertyBroker()
	 */
	@Override
	public PropertyBroker getPropertyBroker();

	/* (non-Javadoc)
	 * @see lotus.domino.Session#getServerName()
	 */
	@Override
	public String getServerName();

	/* (non-Javadoc)
	 * @see lotus.domino.Session#getSessionToken()
	 */
	@Override
	public String getSessionToken();

	/* (non-Javadoc)
	 * @see lotus.domino.Session#getSessionToken(java.lang.String)
	 */
	@Override
	public String getSessionToken(String serverName);

	/* (non-Javadoc)
	 * @see lotus.domino.Session#getURL()
	 */
	@Override
	public String getURL();

	/* (non-Javadoc)
	 * @see lotus.domino.Session#getURLDatabase()
	 */
	@Override
	public Database getURLDatabase();

	/* (non-Javadoc)
	 * @see lotus.domino.Session#getUserName()
	 */
	@Override
	public String getUserName();

	/* (non-Javadoc)
	 * @see lotus.domino.Session#getUserNameObject()
	 */
	@Override
	public Name getUserNameObject();

	/* (non-Javadoc)
	 * @see lotus.domino.Session#getUserPolicySettings(java.lang.String, java.lang.String, int)
	 */
	@Override
	public Document getUserPolicySettings(String server, String name, int type);

	/* (non-Javadoc)
	 * @see lotus.domino.Session#getUserPolicySettings(java.lang.String, java.lang.String, int, java.lang.String)
	 */
	@Override
	public Document getUserPolicySettings(String server, String name, int type, String explicitPolicy);

	/* (non-Javadoc)
	 * @see lotus.domino.Session#hashPassword(java.lang.String)
	 */
	@Override
	public String hashPassword(String password);

	/* (non-Javadoc)
	 * @see lotus.domino.Session#isConvertMime()
	 */
	@Override
	public boolean isConvertMime();

	/* (non-Javadoc)
	 * @see lotus.domino.Session#isConvertMIME()
	 */
	@Override
	public boolean isConvertMIME();

	/* (non-Javadoc)
	 * @see lotus.domino.Session#isOnServer()
	 */
	@Override
	public boolean isOnServer();

	/* (non-Javadoc)
	 * @see lotus.domino.Session#isRestricted()
	 */
	@Override
	public boolean isRestricted();

	/* (non-Javadoc)
	 * @see lotus.domino.Session#isTrackMillisecInJavaDates()
	 */
	@Override
	public boolean isTrackMillisecInJavaDates();

	/* (non-Javadoc)
	 * @see lotus.domino.Session#isTrustedSession()
	 */
	@Override
	public boolean isTrustedSession();

	/* (non-Javadoc)
	 * @see lotus.domino.Session#isValid()
	 */
	@Override
	public boolean isValid();

	/* (non-Javadoc)
	 * @see lotus.domino.Session#resetUserPassword(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean resetUserPassword(String serverName, String userName, String password);

	/* (non-Javadoc)
	 * @see lotus.domino.Session#resetUserPassword(java.lang.String, java.lang.String, java.lang.String, int)
	 */
	@Override
	public boolean resetUserPassword(String serverName, String userName, String password, int downloadCount);

	/* (non-Javadoc)
	 * @see lotus.domino.Session#resolve(java.lang.String)
	 */
	@Override
	public lotus.domino.Base resolve(String url);

	/* (non-Javadoc)
	 * @see lotus.domino.Session#sendConsoleCommand(java.lang.String, java.lang.String)
	 */
	@Override
	public String sendConsoleCommand(String serverName, String consoleCommand);

	/* (non-Javadoc)
	 * @see lotus.domino.Session#setAllowLoopBack(boolean)
	 */
	@Override
	public void setAllowLoopBack(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.Session#setConvertMime(boolean)
	 */
	@Override
	public void setConvertMime(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.Session#setConvertMIME(boolean)
	 */
	@Override
	public void setConvertMIME(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.Session#setEnvironmentVar(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setEnvironmentVar(String vname, Object value);

	/* (non-Javadoc)
	 * @see lotus.domino.Session#setEnvironmentVar(java.lang.String, java.lang.Object, boolean)
	 */
	@Override
	public void setEnvironmentVar(String vname, Object value, boolean isSystem);

	/* (non-Javadoc)
	 * @see lotus.domino.Session#setTrackMillisecInJavaDates(boolean)
	 */
	@Override
	public void setTrackMillisecInJavaDates(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.Session#verifyPassword(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean verifyPassword(String password, String hashedPassword);
}

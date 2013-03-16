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

import lotus.domino.PropertyBroker;

import org.openntf.domino.annotations.Legacy;

/**
 * The Interface Session is the root of the Domino Objects containment hierarchy, providing access to the other Domino objects, and
 * represents the Domino environment of the current program.
 */
public interface Session extends lotus.domino.Session, Base<lotus.domino.Session> {

	/**
	 * Evaluates a Domino formula.
	 * <p>
	 * 
	 * All @Functions that affect the user interface do not work in evaluate. These include:
	 * </p>
	 * <p>
	 * <ul>
	 * <li>@Command</li>
	 * <li>@DbManager</li>
	 * <li>@DbName</li>
	 * <li>@DbTitle</li>
	 * <li>@DDEExecute</li>
	 * <li>@DDEInitiate</li>
	 * <li>@DDEPoke</li>
	 * <li>@DDETerminate</li>
	 * <li>@DialogBox</li>
	 * <li>@PickList</li>
	 * <li>@PostedCommand</li>
	 * <li>@Prompt</li>
	 * <li>@ViewTitle</li>
	 * </ul>
	 * </p>
	 * 
	 * @param formula
	 *            The formula to be evaluated.
	 * 
	 * @return The result of the evaluation. A scalar result is returned in firstElement.
	 */
	@Override
	public Vector<Object> evaluate(String formula);

	/**
	 * Evaluates a Domino formula against a given document.
	 * 
	 * <p>
	 * If the formula contains the name of a field, you must use the 2-parameter method. The formula takes the field from the document
	 * specified as parameter 2.
	 * </p>
	 * 
	 * <p>
	 * You cannot change a document with evaluate; you can only get a result. To change a document, write the result to the document with a
	 * method such as Document.replaceItemValue.
	 * </p>
	 * 
	 * <p>
	 * 
	 * All @Functions that affect the user interface do not work in evaluate. These include:
	 * </p>
	 * <p>
	 * <ul>
	 * <li>@Command</li>
	 * <li>@DbManager</li>
	 * <li>@DbName</li>
	 * <li>@DbTitle</li>
	 * <li>@DDEExecute</li>
	 * <li>@DDEInitiate</li>
	 * <li>@DDEPoke</li>
	 * <li>@DDETerminate</li>
	 * <li>@DialogBox</li>
	 * <li>@PickList</li>
	 * <li>@PostedCommand</li>
	 * <li>@Prompt</li>
	 * <li>@ViewTitle</li>
	 * </ul>
	 * </p>
	 * 
	 * @param formula
	 *            The formula to be evaluated.
	 * @param doc
	 *            The document to evaluate against.
	 * 
	 * @return The result of the evaluation. A scalar result is returned in firstElement.
	 */
	@Override
	public Vector<Object> evaluate(String formula, lotus.domino.Document doc);

	/**
	 * The Domino Directories and Personal Address Books, including directory catalogs, known to the current session.
	 * 
	 * <p>
	 * To distinguish between a Domino Directory and a Personal Address Book, use isPublicAddressBook and isPrivateAddressBook of Database.
	 * </p>
	 * 
	 * <p>
	 * A database retrieved through getAddressBooks is closed. To access all its properties and methods, you must open the database with the
	 * open method in NotesDatabase.
	 * </p>
	 * 
	 * @deprecated Use {@link #getAddressBookCollection()} instead
	 * 
	 * @return A {@link java.lang.Vector} of Databases.
	 */
	@Override
	@Deprecated
	@Legacy( { Legacy.INTERFACES_WARNING })
	public Vector<Database> getAddressBooks();

	/**
	 * A collection of Domino Directories and Personal Address Books, including directory catalogs, known to the current session.
	 * 
	 * <p>
	 * To distinguish between a Domino Directory and a Personal Address Book, use isPublicAddressBook and isPrivateAddressBook of Database.
	 * </p>
	 * 
	 * <p>
	 * A database retrieved through getAddressBooks is closed. To access all its properties and methods, you must open the database with the
	 * open method in NotesDatabase.
	 * </p>
	 * 
	 * @return A collection of Databases.
	 */
	public Collection<Database> getAddressBookCollection();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#freeTimeSearch(lotus.domino.DateRange, int, java.lang.Object, boolean)
	 */
	@Override
	@Deprecated
	@Legacy( { Legacy.INTERFACES_WARNING })
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

	/**
	 * The groups to which the current user belongs.
	 * 
	 * <p>
	 * The "groups" include the hierarchical parents of the current effective user name and the alternate user name, if available. For Mary
	 * Smith/Department One/Acme, for example, the groups include /Department One/Acme and /Acme.
	 * </p>
	 * 
	 * <p>
	 * The groups include those to which the user name belongs in the Domino� Directory or Personal Address Book where the program is
	 * running.
	 * </p>
	 * 
	 * @deprecated Use {@link #getUserGroupNameCollection()} instead.
	 * 
	 * @return A {@link java.lang.Vector} of group names. Elements of of type {@link org.openntf.domino.Name}
	 */
	@Override
	@Deprecated
	@Legacy( { Legacy.INTERFACES_WARNING })
	public Vector<Name> getUserGroupNameList(); // TODO should we use a Vector of names? Or allow someone to request it as String-only so

	// there's no recycle burden?

	/**
	 * A collection of groups to which the current user belongs.
	 * 
	 * <p>
	 * The "groups" include the hierarchical parents of the current effective user name and the alternate user name, if available. For Mary
	 * Smith/Department One/Acme, for example, the groups include /Department One/Acme and /Acme.
	 * </p>
	 * 
	 * <p>
	 * The groups include those to which the user name belongs in the Domino� Directory or Personal Address Book where the program is
	 * running.
	 * </p>
	 * 
	 * @return The user group name collection
	 */
	public Collection<String> getUserGroupNameCollection();

	/**
	 * The name of the user or server that created the session, and the alternate name if it exists.
	 * 
	 * @deprecated Use {@link #getUserNameCollection()} instead.
	 * 
	 * @return If the user does not have an alternate name, getUserNameList returns a vector of one element containing the user name. If the
	 *         user does have an alternate name, getUserNameList returns a vector of two elements containing the user name and the alternate
	 *         user name.
	 */
	@Override
	@Deprecated
	@Legacy( { Legacy.INTERFACES_WARNING })
	public Vector<Name> getUserNameList(); // TODO should we use a Vector of names? Or allow someone to request it as String-only so there's

	// no recycle burden?

	/**
	 * Gets a collection names of the user or server that created the session, and the alternate name if it exists.
	 * 
	 * @return The user name collection
	 */
	public Collection<String> getUserNameCollection();

	/**
	 * Creates a new AdministrationProcess object.
	 * 
	 * @param server
	 *            The name of the server containing the Administration Requests database (ADMIN4.NSF). An empty string means the local
	 *            computer. The server must contain a replica of the Certification Log. You must have access privileges to the Domino�
	 *            Directory on the server for Administration Process requests that use it.
	 * 
	 * @return The newly created {@link org.openntf.domino.AdministrationProcess} object.
	 * 
	 */
	@Override
	public AdministrationProcess createAdministrationProcess(String server);

	/**
	 * Creates a new ColorObject object.
	 * 
	 * @return The newly created {@link org.openntf.domino.ColorObject} object.
	 */
	@Override
	public ColorObject createColorObject();

	/**
	 * Creates a new DateRange object.
	 * 
	 * @return The newly created {@link org.openntf.domino.DateRange} object.
	 */
	@Override
	public lotus.domino.DateRange createDateRange();

	/**
	 * Creates a new DateRange object.
	 * 
	 * @param startTime
	 *            The starting date-time of the range. Cannot be <code>null</code>.
	 * @param endTime
	 *            The ending date-time of the range. Cannot be <code>null</code>.
	 * 
	 * @return The newly created {@link org.openntf.domino.DateRange} object.
	 */
	@Override
	public lotus.domino.DateRange createDateRange(Date startTime, Date endTime);

	/**
	 * Creates a new DateRange object.
	 * 
	 * @param startTime
	 *            The starting date-time of the range. Cannot be <code>null</code>.
	 * @param endTime
	 *            The ending date-time of the range. Cannot be <code>null</code>.
	 * 
	 * @return The newly created {@link org.openntf.domino.DateRange} object.
	 */
	@Override
	public lotus.domino.DateRange createDateRange(lotus.domino.DateTime startTime, lotus.domino.DateTime endTime);

	/**
	 * Creates a DateTime object that represents a specified date and time.
	 * 
	 * @param date
	 *            The date, time, and time zone you want the object to represent using a {@link java.util.Calendar} object.
	 * 
	 * @return The newly created {@link org.openntf.domino.DateTime}.
	 */
	@Override
	public DateTime createDateTime(Calendar date);

	/**
	 * Creates a DateTime object that represents a specified date and time.
	 * 
	 * @param date
	 *            The date, time you want the object to represent using a {@link java.util.Date} object.
	 * 
	 * @return The newly created {@link org.openntf.domino.DateTime}.
	 */
	@Override
	public DateTime createDateTime(Date date);

	/**
	 * Creates a DateTime object that represents a specified date and time.
	 * 
	 * @param date
	 *            The date, time you want the object to represent using a string. @see org.openntf.domino.DateTime for formats.
	 * 
	 * @return The newly created {@link org.openntf.domino.DateTime}.
	 */
	@Override
	public DateTime createDateTime(String date);

	/**
	 * Creates a DxlExporter object.
	 * 
	 * @return The newly created {@link org.openntf.domino.DxlExporter} object.
	 */
	@Override
	public DxlExporter createDxlExporter();

	/**
	 * Creates a DxlImporter object.
	 * 
	 * @return The newly created {@link org.openntf.domino.DxlImporter} object.
	 */
	@Override
	public DxlImporter createDxlImporter();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#createLog(java.lang.String)
	 */
	@Override
	public Log createLog(String name);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#createName(java.lang.String)
	 */
	@Override
	public Name createName(String name);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#createName(java.lang.String, java.lang.String)
	 */
	@Override
	public Name createName(String name, String lang);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#createNewsletter(lotus.domino.DocumentCollection)
	 */
	@Override
	public Newsletter createNewsletter(lotus.domino.DocumentCollection collection);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#createRegistration()
	 */
	@Override
	public Registration createRegistration();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#createRichTextParagraphStyle()
	 */
	@Override
	public RichTextParagraphStyle createRichTextParagraphStyle();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#createRichTextStyle()
	 */
	@Override
	public RichTextStyle createRichTextStyle();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#createStream()
	 */
	@Override
	public Stream createStream();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#getAgentContext()
	 */
	@Override
	public AgentContext getAgentContext();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#getCalendar(lotus.domino.Database)
	 */
	@Override
	public NotesCalendar getCalendar(lotus.domino.Database db);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#getCommonUserName()
	 */
	@Override
	public String getCommonUserName();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#getCredentials()
	 */
	@Override
	public Object getCredentials();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#getCurrentDatabase()
	 */
	@Override
	public Database getCurrentDatabase();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#getDatabase(java.lang.String, java.lang.String)
	 */
	@Override
	public Database getDatabase(String server, String db);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#getDatabase(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public Database getDatabase(String server, String db, boolean createOnFail);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#getDbDirectory(java.lang.String)
	 */
	@Override
	public DbDirectory getDbDirectory(String server);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#getDirectory()
	 */
	@Override
	public Directory getDirectory();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#getDirectory(java.lang.String)
	 */
	@Override
	public Directory getDirectory(String server);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#getEffectiveUserName()
	 */
	@Override
	public String getEffectiveUserName();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#getEnvironmentString(java.lang.String)
	 */
	@Override
	public String getEnvironmentString(String vname);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#getEnvironmentString(java.lang.String, boolean)
	 */
	@Override
	public String getEnvironmentString(String vname, boolean isSystem);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#getEnvironmentValue(java.lang.String)
	 */
	@Override
	public Object getEnvironmentValue(String vname);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#getEnvironmentValue(java.lang.String, boolean)
	 */
	@Override
	public Object getEnvironmentValue(String vname, boolean isSystem);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#getHttpURL()
	 */
	@Override
	public String getHttpURL();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#getInternational()
	 */
	@Override
	public International getInternational();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#getNotesVersion()
	 */
	@Override
	public String getNotesVersion();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#getOrgDirectoryPath()
	 */
	@Override
	public String getOrgDirectoryPath();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#getPlatform()
	 */
	@Override
	public String getPlatform();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#getPropertyBroker()
	 */
	@Override
	public PropertyBroker getPropertyBroker();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#getServerName()
	 */
	@Override
	public String getServerName();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#getSessionToken()
	 */
	@Override
	public String getSessionToken();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#getSessionToken(java.lang.String)
	 */
	@Override
	public String getSessionToken(String serverName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#getURL()
	 */
	@Override
	public String getURL();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#getURLDatabase()
	 */
	@Override
	public Database getURLDatabase();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#getUserName()
	 */
	@Override
	public String getUserName();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#getUserNameObject()
	 */
	@Override
	public Name getUserNameObject();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#getUserPolicySettings(java.lang.String, java.lang.String, int)
	 */
	@Override
	public Document getUserPolicySettings(String server, String name, int type);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#getUserPolicySettings(java.lang.String, java.lang.String, int, java.lang.String)
	 */
	@Override
	public Document getUserPolicySettings(String server, String name, int type, String explicitPolicy);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#hashPassword(java.lang.String)
	 */
	@Override
	public String hashPassword(String password);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#isConvertMime()
	 */
	@Override
	public boolean isConvertMime();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#isConvertMIME()
	 */
	@Override
	public boolean isConvertMIME();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#isOnServer()
	 */
	@Override
	public boolean isOnServer();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#isRestricted()
	 */
	@Override
	public boolean isRestricted();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#isTrackMillisecInJavaDates()
	 */
	@Override
	public boolean isTrackMillisecInJavaDates();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#isTrustedSession()
	 */
	@Override
	public boolean isTrustedSession();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#isValid()
	 */
	@Override
	public boolean isValid();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#resetUserPassword(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean resetUserPassword(String serverName, String userName, String password);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#resetUserPassword(java.lang.String, java.lang.String, java.lang.String, int)
	 */
	@Override
	public boolean resetUserPassword(String serverName, String userName, String password, int downloadCount);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#resolve(java.lang.String)
	 */
	@Override
	public lotus.domino.Base resolve(String url);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#sendConsoleCommand(java.lang.String, java.lang.String)
	 */
	@Override
	public String sendConsoleCommand(String serverName, String consoleCommand);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#setAllowLoopBack(boolean)
	 */
	@Override
	public void setAllowLoopBack(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#setConvertMime(boolean)
	 */
	@Override
	public void setConvertMime(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#setConvertMIME(boolean)
	 */
	@Override
	public void setConvertMIME(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#setEnvironmentVar(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setEnvironmentVar(String vname, Object value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#setEnvironmentVar(java.lang.String, java.lang.Object, boolean)
	 */
	@Override
	public void setEnvironmentVar(String vname, Object value, boolean isSystem);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#setTrackMillisecInJavaDates(boolean)
	 */
	@Override
	public void setTrackMillisecInJavaDates(boolean flag);

	/**
	 * Verifies a plain string value against a hashed value.
	 * 
	 * @param password
	 *            The plain value.
	 * @param hashedPassword
	 *            The hashed value.
	 * 
	 * @return Returns <code>true</code> if the plain vlaue converts to the same hashed value and <code>false</code> if it does not.
	 * 
	 */
	@Override
	public boolean verifyPassword(String password, String hashedPassword);
}

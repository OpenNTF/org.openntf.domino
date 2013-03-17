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
	@Legacy({ Legacy.INTERFACES_WARNING })
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
	@Legacy({ Legacy.INTERFACES_WARNING })
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
	@Legacy({ Legacy.INTERFACES_WARNING })
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
	 * @return The newly created {@link org.openntf.domino.DateTime} object.
	 */
	@Override
	public DateTime createDateTime(Calendar date);

	/**
	 * Creates a DateTime object that represents a specified date and time.
	 * 
	 * @param date
	 *            The date, time you want the object to represent using a {@link java.util.Date} object.
	 * 
	 * @return The newly created {@link org.openntf.domino.DateTime} object.
	 */
	@Override
	public DateTime createDateTime(Date date);

	/**
	 * Creates a DateTime object that represents a specified date and time.
	 * 
	 * @param date
	 *            The date, time you want the object to represent using a string. @see org.openntf.domino.DateTime for formats.
	 * 
	 * @return The newly created {@link org.openntf.domino.DateTime} object.
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

	/**
	 * Creates a new Log object with the name you specify.
	 * 
	 * @param name
	 *            A name that identifies the log.
	 * 
	 * @return The newly created {@link org.openntf.domino.Log} object with the specified name.
	 */
	@Override
	public Log createLog(String name);

	/**
	 * Creates a new Name object.
	 * 
	 * @param name
	 *            A user or server name. If the name is not in the format of an abbreviated or canonical hierarchical name, it is treated as
	 *            a flat name.
	 * 
	 * @return The newly created {@link org.openntf.domino.Name} object.
	 */
	@Override
	public Name createName(String name);

	/**
	 * Creates a new Name object with the specified language.
	 * 
	 * @param name
	 *            A user or server name. If the name is not in the format of an abbreviated or canonical hierarchical name, it is treated as
	 *            a flat name.
	 * @param lang
	 *            A language associated with the user name. For primary user names, the language should be <code>null</code>. For alternate
	 *            user names, a language can be specified. Typical language codes look like
	 *            <p>
	 *            <ul>
	 *            <li>en
	 *            <li>en-IE
	 *            <li>de
	 *            <li>zh-CN
	 *            </ul>
	 * 
	 * @return The newly created {@link org.openntf.domino.Name} object.
	 */
	@Override
	public Name createName(String name, String lang);

	/**
	 * Given a DocumentCollection containing the documents you want, creates a new Newsletter.
	 * 
	 * @param collection
	 *            The documents that you want included in the newsletter. Can be null.
	 * 
	 * @return The newly created {@link org.openntf.domino.Newsletter} object.
	 */
	@Override
	public Newsletter createNewsletter(lotus.domino.DocumentCollection collection);

	/**
	 * Creates a new Registration object.
	 * 
	 * @return The newly created {@link org.openntf.domino.Registration} object.
	 */
	@Override
	public Registration createRegistration();

	/**
	 * Creates a new RichTextParagraphStyle object.
	 * 
	 * @return The newly created {@link org.openntf.domino.RichTextParagraphStyle} object.
	 */
	@Override
	public RichTextParagraphStyle createRichTextParagraphStyle();

	/**
	 * Creates a new RichTextStyle object.
	 * 
	 * @return The newly created {@link org.openntf.domino.RichTextStyle} object.
	 */
	@Override
	public RichTextStyle createRichTextStyle();

	/**
	 * Creates a new Stream object.
	 * 
	 * @return The newly created {@link org.openntf.domino.Stream} object.
	 */
	@Override
	public Stream createStream();

	/**
	 * Represents the agent environment of the current program, if an agent is running it.
	 * 
	 * @return If the current program is not running from an agent, this property returns <code>null</code>, otherwise it returns the
	 *         current {@link org.openntf.domino.AgentContext}.
	 * 
	 */
	@Override
	public AgentContext getAgentContext();

	/**
	 * Creates a new NotesCalendar object.
	 * 
	 * @param db
	 *            A standard Domino mail application, for example, an application based on the template StdR85Mail.
	 * @return The newly created {@link org.openntf.domino.Notescalendar} object.
	 */
	@Override
	public NotesCalendar getCalendar(lotus.domino.Database db);

	/**
	 * The common name of the user that created the session.
	 * <p>
	 * This is the name of the user running the script, which for a server-side code is the server name. For the name of the user logged
	 * into the server, use {@link #getEffectiveUserName()}.
	 * <p>
	 * If the user name is flat (non-hierarchical), this is the same as {@link #getUserName()}.
	 * 
	 * @return The session creators common user name.
	 */
	@Override
	public String getCommonUserName();

	/**
	 * Gets the session's credentials.
	 * 
	 * @deprecated As per IBM help documentation. No replacement.
	 * @return A {@link java.lang.Object} representing the current credentials.
	 */
	@Deprecated
	@Override
	@Legacy({ Legacy.IBM_DEP_WARNING })
	public Object getCredentials();

	/**
	 * Creates a NotesDatabase object that represents the current database and opens the database.
	 * 
	 * @return A {@link org.openntf.domino.Database} object that can be used to access the current database.
	 */
	@Override
	public Database getCurrentDatabase();

	/**
	 * Creates a NotesDatabase object that represents the database located at the server and file name you specify, and opens the database,
	 * if possible.
	 * 
	 * @param server
	 *            The name of the server on which the database resides. Use <code>null</code> to indicate the session's environment, for
	 *            example, the current computer.
	 * @param db
	 *            The file name and location of the database within the Domino data directory. Use a full path name if the database is not
	 *            within the Domino data directory.
	 * 
	 * @return A {@link org.openntf.domino.Database} object that can be used to access the database you have specified, or null if the
	 *         database cannot be opened and createonfail is false. If the database cannot be opened
	 *         {@link org.openntf.domino.Database#isOpen()} is false for the NotesDatabase object.
	 */
	@Override
	public Database getDatabase(String server, String db);

	/**
	 * Creates a NotesDatabase object that represents the database located at the server and file name you specify with an option to create
	 * the database if it does not already exist.
	 * 
	 * @param server
	 *            The name of the server on which the database resides. Use <code>null</code> to indicate the session's environment, for
	 *            example, the current computer.
	 * @param db
	 *            The file name and location of the database within the Domino data directory. Use a full path name if the database is not
	 *            within the Domino data directory.
	 * @param createOnFail
	 *            If true creates a Database object even if the specified database cannot be opened. If false, returns <code>null</code> if
	 *            the database cannot be opened.
	 * 
	 * @return A {@link org.openntf.domino.Database} object that can be used to access the database you have specified, or <code>null</code>
	 *         if the database cannot be opened and <code>createOnFail</code> is false. If the database cannot be opened and
	 *         <code>createOnFail</code> is true, {@link org.openntf.domino.Database#isOpen()} is false for the NotesDatabase object.
	 */
	@Override
	public Database getDatabase(String server, String db, boolean createOnFail);

	/**
	 * Gets a directory of databases.
	 * 
	 * @param server
	 *            The name of the server with database files you want to navigate. Use the empty string to indicate the current session's
	 *            environment
	 * 
	 * @return A {@link org.openntf.domino.DbDirectory} of databases on the server.
	 */
	@Override
	public DbDirectory getDbDirectory(String server);

	/**
	 * Creates a new NotesDirectory object using the name of the current server.
	 * 
	 * @return A {@link org.openntf.domino.Directory} object.
	 */
	@Override
	public Directory getDirectory();

	/**
	 * Creates a new NotesDirectory object using the name of the server you want to access.
	 * <p>
	 * If this method is run on a server, and the server parameter is the name of a different server, the current server must have a trusted
	 * relationship with the specified server.
	 * 
	 * @param server
	 *            The name of the server whose database files you want to navigate. Use no parameter method or an empty string to indicate
	 *            the current server.
	 * @return A {@link org.openntf.domino.Directory} object.
	 */
	@Override
	public Directory getDirectory(String server);

	/**
	 * The login name of the user that created the session.
	 * 
	 * @return The name of the user logged into the server session running the code.
	 */
	@Override
	public String getEffectiveUserName();

	/**
	 * Gets the value of a non-system string environment variable.
	 * 
	 * @param vname
	 *            The name of the environment variable.
	 * 
	 * @return The value of the environment variable.
	 */
	@Override
	public String getEnvironmentString(String vname);

	/**
	 * Gets the value of a string environment variable.
	 * 
	 * @param vname
	 *            The name of the environment variable.
	 * @param isSystem
	 *            If <code>true</code>, the method uses the exact name of the environment variable. If <code>false</code>, the method
	 *            prefixes a dollar sign to the name.
	 * 
	 * @return The value of the environment variable.
	 */
	@Override
	public String getEnvironmentString(String vname, boolean isSystem);

	/**
	 * Gets the value of a non-system numeric environment variable.
	 * <p>
	 * <b>Note:</b> Do not use this method for string values.
	 * 
	 * @param vname
	 *            The name of the environment variable.
	 * 
	 * @return The value of the environment variable.
	 */
	@Override
	public Object getEnvironmentValue(String vname);

	/**
	 * Gets the value of a numeric environment variable.
	 * <p>
	 * <b>Note:</b> Do not use this method for string values.
	 * 
	 * @param vname
	 *            The name of the environment variable.
	 * @param isSystem
	 *            If <code>true</code>, the method uses the exact name of the environment variable. If <code>false</code>, the method
	 *            prefixes a dollar sign to the name.
	 * 
	 * @return The value of the environment variable.
	 */
	@Override
	public Object getEnvironmentValue(String vname, boolean isSystem);

	/**
	 * The Domino URL of a server when HTTP protocols are in effect.
	 * 
	 * @return The URL as a string. If HTTP protocols are not available, this property returns an <code>empty string</code>.
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

	/**
	 * Gets a Server object from the current session.
	 * 
	 * @param serverName
	 *            The name of the server which should be accessed
	 * 
	 * @return A {@link org.openntf.domino.Server} object
	 */
	public Server getServer();

	/**
	 * Gets a Server object from the current session using the specified server name.
	 * 
	 * @param serverName
	 *            The name of the server which should be accessed
	 * 
	 * @return A {@link org.openntf.domino.Server} object
	 */
	public Server getServer(String serverName);

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

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
package org.openntf.domino;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import org.openntf.domino.annotations.Legacy;

// TODO: Auto-generated Javadoc
/**
 * The Interface Session is the root of the Domino Objects containment hierarchy, providing access to the other Domino objects, and
 * represents the Domino environment of the current program.
 */
public interface Session extends lotus.domino.Session, org.openntf.domino.ext.Session, Base<lotus.domino.Session> {

	/**
	 * The Enum RunContext.
	 */
	public static enum RunContext {

		/** The agent. */
		AGENT,
		/** The applet. */
		APPLET,
		/** The dots. */
		DOTS,
		/** The xpages nsf. */
		XPAGES_NSF,
		/** The xpages osgi. */
		XPAGES_OSGI,
		/** The plugin. */
		PLUGIN,
		/** The addin. */
		ADDIN,
		/** The servlet. */
		SERVLET,
		/** The cli. */
		CLI,
		/** The diiop. */
		DIIOP,
		/** The unknown. */
		UNKNOWN
	}

	/**
	 * Creates a new AdministrationProcess object.
	 * 
	 * @param server
	 *            The name of the server containing the Administration Requests database (ADMIN4.NSF). An empty string means the local
	 *            computer. The server must contain a replica of the Certification Log. You must have access privileges to the Domino�
	 *            Directory on the server for Administration Process requests that use it.
	 * 
	 * @return The newly created {@link AdministrationProcess} object.
	 * @since lotus.domino 6.5.0
	 */
	@Override
	public AdministrationProcess createAdministrationProcess(final String server);

	/**
	 * Creates a new ColorObject object.
	 * 
	 * @return The newly created {@link ColorObject} object.
	 * @since lotus.domino 6.5.0
	 */
	@Override
	public ColorObject createColorObject();

	/**
	 * Creates the color object.
	 * 
	 * @param color
	 *            the color
	 * @return the color object
	 */
	public ColorObject createColorObject(final java.awt.Color color);

	/**
	 * Creates a new DateRange object.
	 * 
	 * @return The newly created {@link DateRange} object.
	 * @since lotus.domino 4.5.0
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
	 * @return The newly created {@link DateRange} object.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public lotus.domino.DateRange createDateRange(final Date startTime, final Date endTime);

	/**
	 * Creates a new DateRange object.
	 * 
	 * @param startTime
	 *            The starting date-time of the range. Cannot be <code>null</code>.
	 * @param endTime
	 *            The ending date-time of the range. Cannot be <code>null</code>.
	 * 
	 * @return The newly created {@link DateRange} object.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public lotus.domino.DateRange createDateRange(final lotus.domino.DateTime startTime, final lotus.domino.DateTime endTime);

	/**
	 * Creates a DateTime object that represents a specified date and time.
	 * 
	 * @param date
	 *            The date, time, and time zone you want the object to represent using a {@link java.util.Calendar} object.
	 * 
	 * @return The newly created {@link DateTime} object.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public DateTime createDateTime(final Calendar date);

	/**
	 * Creates a DateTime object that represents a specified date and time.
	 * 
	 * @param date
	 *            The date, time you want the object to represent using a {@link java.util.Date} object.
	 * 
	 * @return The newly created {@link DateTime} object.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public DateTime createDateTime(final Date date);

	// there's no recycle burden?

	/**
	 * Creates a DateTime object that represents a specified date and time.
	 * 
	 * @param date
	 *            The date, time you want the object to represent using a string. @see org.openntf.domino.DateTime for formats.
	 * 
	 * @return The newly created {@link DateTime} object.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public DateTime createDateTime(final String date);

	/**
	 * Creates a DxlExporter object.
	 * 
	 * @return The newly created {@link DxlExporter} object.
	 * @since lotus.domino 6.5.0
	 */
	@Override
	public DxlExporter createDxlExporter();

	// no recycle burden?

	/**
	 * Creates a DxlImporter object.
	 * 
	 * @return The newly created {@link DxlImporter} object.
	 * @since lotus.domino 6.5.0
	 */
	@Override
	public DxlImporter createDxlImporter();

	/**
	 * Creates a new Log object with the name you specify.
	 * 
	 * @param name
	 *            A name that identifies the log.
	 * 
	 * @return The newly created {@link Log} object with the specified name.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public Log createLog(final String name);

	/**
	 * Creates a new Name object.
	 * 
	 * @param name
	 *            A user or server name. If the name is not in the format of an abbreviated or canonical hierarchical name, it is treated as
	 *            a flat name.
	 * 
	 * @return The newly created {@link Name} object.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public Name createName(final String name);

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
	 * @return The newly created {@link Name} object.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public Name createName(final String name, final String lang);

	/**
	 * Given a DocumentCollection containing the documents you want, creates a new Newsletter.
	 * 
	 * @param collection
	 *            The documents that you want included in the newsletter. Can be null.
	 * 
	 * @return The newly created {@link Newsletter} object.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public Newsletter createNewsletter(final lotus.domino.DocumentCollection collection);

	/**
	 * Creates a new Registration object.
	 * 
	 * @return The newly created {@link Registration} object.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public Registration createRegistration();

	/**
	 * Creates a new RichTextParagraphStyle object.
	 * 
	 * @return The newly created {@link RichTextParagraphStyle} object.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public RichTextParagraphStyle createRichTextParagraphStyle();

	/**
	 * Creates a new RichTextStyle object.
	 * 
	 * @return The newly created {@link RichTextStyle} object.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public RichTextStyle createRichTextStyle();

	/**
	 * Creates a new Stream object.
	 * 
	 * @return The newly created {@link Stream} object.
	 * @since lotus.domino 6.0.0
	 */
	@Override
	public Stream createStream();

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
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public Vector<Object> evaluate(final String formula);

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
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public Vector<Object> evaluate(final String formula, final lotus.domino.Document doc);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#freeTimeSearch(lotus.domino.DateRange, int, java.lang.Object, boolean)
	 */
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING })
	public Vector<DateRange> freeTimeSearch(final lotus.domino.DateRange window, final int duration, final Object names, final boolean firstFit);

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
	 * @return A {@link java.lang.Vector Vector} of Databases.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING })
	public Vector<Database> getAddressBooks();

	/**
	 * Represents the agent environment of the current program, if an agent is running it.
	 * 
	 * @return If the current program is not running from an agent, this property returns <code>null</code>, otherwise it returns the
	 *         current {@link AgentContext}.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public AgentContext getAgentContext();

	/**
	 * Creates a new NotesCalendar object.
	 * 
	 * @param db
	 *            A standard Domino mail application, for example, an application based on the template StdR85Mail.
	 * @return The newly created {@link NotesCalendar} object.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public NotesCalendar getCalendar(final lotus.domino.Database db);

	/**
	 * The common name of the user that created the session.
	 * <p>
	 * This is the name of the user running the script, which for a server-side code is the server name. For the name of the user logged
	 * into the server, use {@link #getEffectiveUserName()}.
	 * <p>
	 * If the user name is flat (non-hierarchical), this is the same as {@link #getUserName()}.
	 * 
	 * @return The session creators common user name.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public String getCommonUserName();

	/**
	 * Gets the session's credentials.
	 * 
	 * @return An {@link java.lang.Object Object} representing the current credentials.
	 * @deprecated As per IBM help documentation. No replacement.
	 * @since lotus.domino 4.5.0
	 */
	@Deprecated
	@Override
	@Legacy({ Legacy.IBM_DEP_WARNING })
	public Object getCredentials();

	/**
	 * Creates a NotesDatabase object that represents the current database and opens the database.
	 * 
	 * @return A {@link Database} object that can be used to access the current database.
	 * @since lotus.domino 4.5.0
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
	 * @return A {@link Database} object that can be used to access the database you have specified, or null if the database cannot be
	 *         opened and createonfail is false. If the database cannot be opened {@link Database#isOpen} is false for the NotesDatabase
	 *         object.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public Database getDatabase(final String server, final String db);

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
	 * @return A {@link Database} object that can be used to access the database you have specified, or <code>null</code> if the database
	 *         cannot be opened and <code>createOnFail</code> is false. If the database cannot be opened and <code>createOnFail</code> is
	 *         true, {@link Database#isOpen} is false for the NotesDatabase object.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public Database getDatabase(final String server, final String db, final boolean createOnFail);

	/**
	 * Gets a directory of databases.
	 * 
	 * @param server
	 *            The name of the server with database files you want to navigate. Use the empty string to indicate the current session's
	 *            environment
	 * 
	 * @return A {@link DbDirectory} of databases on the server.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public DbDirectory getDbDirectory(final String server);

	/**
	 * Creates a new NotesDirectory object using the name of the current server.
	 * 
	 * @return A {@link Directory} object.
	 * @since lotus.domino 4.5.0
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
	 * @return A {@link Directory} object.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public Directory getDirectory(final String server);

	/**
	 * The login name of the user that created the session.
	 * 
	 * @return The name of the user logged into the server session running the code.
	 * @since lotus.domino 4.5.0
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
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public String getEnvironmentString(final String vname);

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
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public String getEnvironmentString(final String vname, final boolean isSystem);

	/**
	 * Gets the value of a non-system numeric environment variable.
	 * <p>
	 * <b>Note:</b> Do not use this method for string values.
	 * 
	 * @param vname
	 *            The name of the environment variable.
	 * 
	 * @return The value of the environment variable.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public Object getEnvironmentValue(final String vname);

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
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public Object getEnvironmentValue(final String vname, final boolean isSystem);

	/**
	 * The Domino URL of a server when HTTP protocols are in effect.
	 * 
	 * @return The URL as a string. If HTTP protocols are not available, this property returns an <code>empty string</code>.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public String getHttpURL();

	/**
	 * The international (regional) settings for the session's operating environment.
	 * 
	 * @return the regional settings for the OS as a {@link International} object.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public International getInternational();

	/**
	 * The release of Domino the session is running on.
	 * 
	 * @return a {@link java.lang.String String} containing the current Domino server version.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public String getNotesVersion();

	/**
	 * The default directory of the current user in a hosted organization.
	 * 
	 * @return the default directory of the current user.
	 * @since lotus.domino 6.0.0
	 */
	@Override
	public String getOrgDirectoryPath();

	/**
	 * The name of the platform the session is running on.
	 * <p>
	 * The return value will be one of the following
	 * <p>
	 * <ul>
	 * <li>AIX/64
	 * <li>Linux/64
	 * <li>Macintosh
	 * <li>OS/400�
	 * <li>UNIX
	 * <li>Windows/32
	 * <li>Windows/64
	 * </ul>
	 * 
	 * @return the name of the platform
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public String getPlatform();

	/**
	 * Gets the current sessions property broker.
	 * 
	 * @return the current {@link PropertyBroker} object
	 * @since openntf.domino 1.0.0
	 */
	@Override
	public PropertyBroker getPropertyBroker();

	/**
	 * The full name of the server that the session is running on.
	 * 
	 * @return The name of the server that the code is currently running on
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public String getServerName();

	/**
	 * Gets a session token for enabling sign-on to Domino and WebSphere servers in a domain that supports Single Sign-on.
	 * <p>
	 * The token is unique for each user and is valid for the time specified in the Domino Directory. The format of the token is consistent
	 * with the LtpaToken cookie used by a WebSphere server when the SSO configuration of the Domino server imports the Websphere server
	 * keys. You can also get the token from the HTTP headers in a servlet with HttpServletRequest.getCookies().
	 * 
	 * @return The session token
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public String getSessionToken();

	/**
	 * Gets a session token on a specific server for enabling sign-on to Domino and WebSphere servers in a domain that supports Single
	 * Sign-on.
	 * <p>
	 * The token is unique for each user and is valid for the time specified in the Domino Directory. The format of the token is consistent
	 * with the LtpaToken cookie used by a WebSphere server when the SSO configuration of the Domino server imports the Websphere server
	 * keys. You can also get the token from the HTTP headers in a servlet with HttpServletRequest.getCookies().
	 * 
	 * @param serverName
	 *            The name of a server configured for Single Sign-on. If the server name is not specified, the code must execute on a server
	 *            configured for Single Sign-on.
	 * 
	 * @return The session token
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public String getSessionToken(final String serverName);

	/**
	 * Gets the Domino� URL for the session.
	 * <p>
	 * Effectively this method returns an empty string. Use {@link Database#getURL} to get the URL for a database. See {@link #resolve} for
	 * URL hints.
	 * 
	 * @return The Domino URL for the session.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public String getURL();

	/**
	 * Finds and opens the default Web Navigator database.
	 * 
	 * @return The default Web Navigator database.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public Database getURLDatabase();

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
	 * @deprecated Use {@link #getUserGroupNameCollection} instead.
	 * 
	 * @return A {@link java.lang.Vector vector} of group names. Elements of of type {@link org.openntf.domino.Name}
	 * @since lotus.domino 6.0.0
	 */
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING })
	public Vector<Name> getUserGroupNameList(); // TODO should we use a Vector of names? Or allow someone to request it as String-only so

	/**
	 * The full name of the user or server that created the session.
	 * <p>
	 * This is the name of the "user" running the script, which for a server-side script is the server name. For the name of the user logged
	 * into the server, use {@link #getEffectiveUserName}.
	 * <p>
	 * If the user name is hierarchical, this property returns the fully distinguished name. To get only the common name, use the
	 * 
	 * @return The full name of the user or server that created the session. {@link #getCommonUserName} property.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public String getUserName();

	/**
	 * The name of the user or server that created the session, and the alternate name if it exists.
	 * 
	 * @deprecated Use {@link #getUserNameCollection} instead.
	 * 
	 * @return If the user does not have an alternate name, getUserNameList returns a vector of one element containing the user name. If the
	 *         user does have an alternate name, getUserNameList returns a vector of two elements containing the user name and the alternate
	 *         user name.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING })
	public Vector<Name> getUserNameList(); // TODO should we use a Vector of names? Or allow someone to request it as String-only so there's

	/**
	 * The name, in the form of a NotesName object, of the user or server that created the session.
	 * 
	 * @return The {@link Name} object of the session creator.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public Name getUserNameObject();

	/**
	 * Gets a policy document.
	 * <p>
	 * Policy Types are as follows
	 * <p>
	 * <ul>
	 * 0 - NotesSession.POLICYSETTINGS_REGISTRATION
	 * <li>1 - NotesSession.POLICYSETTINGS_SETUP
	 * <li>2 - NotesSession.POLICYSETTINGS_ARCHIVE
	 * <li>3 - NotesSession.POLICYSETTINGS_SECURITY
	 * <li>4 - NotesSession.POLICYSETTINGS_DESKTOP
	 * <li>5 - NotesSession.POLICYSETTINGS_MAIL
	 * </ul>
	 * 
	 * @param server
	 *            The name of the server containing the policy. An empty string means the local computer.
	 * @param name
	 *            The name of the effective user of the policy. The name must be fully qualified.
	 * @param type
	 *            The type of policy.
	 * @return the user policy settings
	 * @see lotus.domino.Session#getUserPolicySettings
	 * @since lotus.domino 6.5.0
	 */
	@Override
	public Document getUserPolicySettings(final String server, final String name, final int type);

	/**
	 * Gets a policy document.
	 * <p>
	 * Policy Types are as follows
	 * <p>
	 * <ul>
	 * 0 - NotesSession.POLICYSETTINGS_REGISTRATION
	 * <li>1 - NotesSession.POLICYSETTINGS_SETUP
	 * <li>2 - NotesSession.POLICYSETTINGS_ARCHIVE
	 * <li>3 - NotesSession.POLICYSETTINGS_SECURITY
	 * <li>4 - NotesSession.POLICYSETTINGS_DESKTOP
	 * <li>5 - NotesSession.POLICYSETTINGS_MAIL
	 * </ul>
	 * 
	 * @param server
	 *            The name of the server containing the policy. An empty string means the local computer.
	 * @param name
	 *            The name of the effective user of the policy. The name must be fully qualified.
	 * @param type
	 *            The type of policy.
	 * @param explicitPolicy
	 *            The policy namespace, with slash (/) prefix, that contains the settings. If this parameter is specified, the second
	 *            parameter is not used.
	 * @return the user policy settings
	 * @see lotus.domino.Session#getUserPolicySettings
	 * @since lotus.domino 6.5.0
	 */
	@Override
	public Document getUserPolicySettings(final String server, final String name, final int type, final String explicitPolicy);

	/**
	 * Hashes a string value so that it is not recognizable.
	 * 
	 * @param password
	 *            the password
	 * @return The hashed value.
	 * @since lotus.domino 6.5.0
	 */
	@Override
	public String hashPassword(final String password);

	/**
	 * Indicates whether items of type NotesItem.MIME_PART are converted to rich text upon NotesDocument instantiation.
	 * 
	 * @return When <code>true</code> MIME will be converted to RichText, otherwise they will remain MIME Items.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public boolean isConvertMime();

	/**
	 * Indicates whether items of type NotesItem.MIME_PART are converted to rich text upon NotesDocument instantiation.
	 * 
	 * @return When <code>true</code> MIME will be converted to RichText, otherwise they will remain MIME Items.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public boolean isConvertMIME();

	/**
	 * Indicates whether the session is running on a server.
	 * 
	 * @return returns <code>true</code> when the code is running on the server, otherwise returns <code>false</code>
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public boolean isOnServer();

	/**
	 * (Indicates whether the session is restricted.
	 * 
	 * @return returns <code>true</code> when the code is running in a restricted session, otherwise returns <code>false</code>
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public boolean isRestricted();

	/**
	 * Indicates whether milliseconds are used in Java� dates.
	 * <p>
	 * By default milliseconds are not tracked. You must set this property to true if you want milliseconds to appear in Java dates
	 * 
	 * @return returns <code>true</code> when tracking milliseconds in Java Dates, otherwise returns <code>false</code>
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public boolean isTrackMillisecInJavaDates();

	/**
	 * Indicates whether the session is a trusted session.
	 * <p>
	 * Applications running on a server installation that need to access databases on a remote server must have either a Trusted Server
	 * relationship, or a Trusted Session. The userID authority that the application is running under must be accounted for in the ACL of
	 * the remote database. That userID is often the serverID.
	 * 
	 * @return returns <code>true</code> when the code is running in a trusted session, otherwise returns <code>false</code>
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public boolean isTrustedSession();

	/**
	 * Indicates whether an instantiated NotesSession object is still valid.
	 * 
	 * @return returns <code>true</code> when the session valid, otherwise returns <code>false</code>
	 * @since lotus.domino 6.0.0
	 */
	@Override
	public boolean isValid();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#resetUserPassword(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean resetUserPassword(final String serverName, final String userName, final String password);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#resetUserPassword(java.lang.String, java.lang.String, java.lang.String, int)
	 */
	@Override
	public boolean resetUserPassword(final String serverName, final String userName, final String password, final int downloadCount);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#resolve(java.lang.String)
	 */
	@Override
	public lotus.domino.Base resolve(final String url);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#sendConsoleCommand(java.lang.String, java.lang.String)
	 */
	@Override
	public String sendConsoleCommand(final String serverName, final String consoleCommand);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#setAllowLoopBack(boolean)
	 */
	@Override
	public void setAllowLoopBack(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#setConvertMime(boolean)
	 */
	@Override
	public void setConvertMime(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#setConvertMIME(boolean)
	 */
	@Override
	public void setConvertMIME(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#setEnvironmentVar(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setEnvironmentVar(final String vname, final Object value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#setEnvironmentVar(java.lang.String, java.lang.Object, boolean)
	 */
	@Override
	public void setEnvironmentVar(final String vname, final Object value, final boolean isSystem);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Session#setTrackMillisecInJavaDates(boolean)
	 */
	@Override
	public void setTrackMillisecInJavaDates(final boolean flag);

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
	public boolean verifyPassword(final String password, final String hashedPassword);
}

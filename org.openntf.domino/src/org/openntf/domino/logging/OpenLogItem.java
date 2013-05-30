/*
<<<<<<< HEAD
 * Copyright Paul Withers, Intec 2011-2013
=======
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
<<<<<<< HEAD
 * 
 * 
 * Paul Withers, Intec March 2013
>>>>>>> 8bcc4ba53ac549f9bd61dc6172feb2f37e78e12d
 * Some significant enhancements here from the OpenNTF version
 *
 * 1. Everything is designed to work regardless of ExtLib packages
 *
 * 2. _logDbName and debug level set from logging.properties.
 *
 * 3. setThisAgent(boolean) method has been added. By default it gets the current page.
 * Otherwise it gets the previous page. Why? Because if we've been redirected to an error page,
 * we want to know which page the ACTUAL error occurred on.
 *
 *
 * Nathan T. Freeman, GBS Jun 20, 2011
 * Developers notes...
 *
 * Because the log methods are static, one simply needs to call..
 *
 * OpenLogItem.logError(session, throwable)
 *
 * or...
 *
 * OpenLogItem.logError(session, throwable, message, level, document)
 *
 * or...
 *
 * OpenLogItem.logEvent(session, message, level, document)
 * 
=======
>>>>>>> origin/declan
 */

package org.openntf.domino.logging;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;

import lotus.domino.NotesException;
import lotus.domino.local.NotesBase;

import org.openntf.domino.Database;
import org.openntf.domino.DateTime;
import org.openntf.domino.Document;
import org.openntf.domino.RichTextItem;
import org.openntf.domino.Session;
import org.openntf.domino.impl.Base;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class OpenLogItem.
 */
public class OpenLogItem implements Serializable {
	/*
	 * ======================================================= <HEADER> NAME: OpenLogClass script library VERSION: 20070321a AUTHOR(S):
	 * Julian Robichaux ( http://www.nsftools.com ) ORIGINAL SOURCE: The OpenLog database, available as an open-source project at
	 * http://www.OpenNTF.org HISTORY: 20070321a: Added startTime global to mark when this particular agent run began, so you can group
	 * multiple errors/events together more easily (see the "by Database and Start Time" view) 20060314a: fixed bug where logErrorEx would
	 * set the message to the severity type instead of the value of msg. 20041111a: made SEVERITY_ and TYPE_ constants public. 20040928a:
	 * added callingMethodDepth variable, which should be incremented by one in the Synchronized class so we'll get a proper reference to
	 * the calling method; make $PublicAccess = "1" when we create new log docs, so users with Depositor access to this database can still
	 * create log docs. 20040226a: removed synchronization from all methods in the main OpenLogItem class and added the new
	 * SynchronizedOpenLogItem class, which simply extends OpenLogItem and calls all the public methods as synchronized methods. Added
	 * olDebugLevel and debugOut public members to report on internal errors. 20040223a: add variables for user name, effective user name,
	 * access level, user roles, and client version; synchronized most of the public methods. 20040222a: this version got a lot less
	 * aggressive with the Notes object recycling, due to potential problems. Also made LogErrorEx and LogEvent return "" if an error
	 * occurred (to be consistent with LogError); added OpenLogItem(Session s) constructor; now get server name from the Session object, not
	 * the AgentContext object (due to differences in what those two things will give you); add UseDefaultLogDb and UseCustomLogDb methods;
	 * added getLogDatabase method, to be consistent with LotusScript functions; added useServerLogWhenLocal and logToCurrentDatabase
	 * variables/options 20040217a: this version made the agentContext object global and fixed a problem where the agentContext was being
	 * recycled in the constuctor (this is very bad) 20040214b: initial version
	 * 
	 * DISCLAIMER: This code is provided "as-is", and should be used at your own risk. The authors make no express or implied warranty about
	 * anything, and they will not be responsible or liable for any damage caused by the use or misuse of this code or its byproducts. No
	 * guarantees are made about anything.
	 * 
	 * That being said, you can use, modify, and distribute this code in any way you want, as long as you keep this header section intact
	 * and in a prominent place in the code. </HEADER> =======================================================
	 * 
	 * This class contains generic functions that can be used to log events and errors to the OpenLog database. All you have to do it copy
	 * this script library to any database that should be sending errors to the OpenLog database, and add it to your Java agents using the
	 * Edit Project button (see the "Using This Database" doc in the OpenLog database for more details).
	 * 
	 * At the beginning of your agent, create a global instance of this class like this:
	 * 
	 * private OpenLogItem oli = new OpenLogItem();
	 * 
	 * and then in all the try/catch blocks that you want to send errors from, add the line:
	 * 
	 * oli.logError(e);
	 * 
	 * where "e" is the Exception that you caught. That's all you have to do. The LogError method will automatically create a document in
	 * the OpenLog database that contains all sorts of information about the error that occurred, including the name of the agent and
	 * function/sub that it occurred in.
	 * 
	 * For additional functionality, you can use the LogErrorEx function to add a custom message, a severity level, and/or a link to a
	 * NotesDocument to the log doc.
	 * 
	 * In addition, you can use the LogEvent function to add a notification document to the OpenLog database.
	 * 
	 * You'll notice that I trap and discard almost all of the Exceptions that may occur as the methods in this class are running. This is
	 * because the class is normally only used when an error is occurring anyway, so there's not sense in trying to pass any new errors back
	 * up the stack.
	 * 
	 * The master copy of this script library resides in the OpenLog database. All copies of this library in other databases should be set
	 * to inherit changes from that database.
	 */

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The Constant TYPE_ERROR. */
	public static final String TYPE_ERROR = "Error";
	
	/** The Constant TYPE_EVENT. */
	public static final String TYPE_EVENT = "Event";

	/** The _log form name. */
	private final String _logFormName = "LogEvent";

	// MODIFY THESE FOR YOUR OWN ENVIRONMENT
	// (don't forget to use double-backslashes if this database
	// is in a Windows subdirectory -- like "logs\\OpenLog.nsf")
	/** The _log db name. */
	private String _logDbName = "";

	/** The _this database. */
	private String _thisDatabase;
	
	/** The _this server. */
	private String _thisServer;
	
	/** The _this agent. */
	private String _thisAgent;
	// why the object? Because the object version is serializable
	/** The _log success. */
	private Boolean _logSuccess = true;
	
	/** The _access level. */
	private String _accessLevel;
	
	/** The _user roles. */
	private Vector<Object> _userRoles;
	
	/** The _client version. */
	private Vector<String> _clientVersion;

	/** The _severity. */
	private Level _severity;
	
	/** The _event type. */
	private String _eventType;
	
	/** The _message. */
	private String _message;

	/** The _base exception. */
	private Throwable _baseException;
	
	/** The _event java time. */
	private Date _eventJavaTime;
	
	/** The _err doc unid. */
	private String _errDocUnid;

	/** The _session. */
	private transient Session _session;
	
	/** The _log db. */
	private transient Database _logDb;
	
	/** The _current database. */
	private transient Database _currentDatabase;
	
	/** The _start time. */
	private transient DateTime _startTime;
	
	/** The _event time. */
	private transient DateTime _eventTime;
	
	/** The _err doc. */
	private transient Document _errDoc;

	/** The ol debug level. */
	public String olDebugLevel = loadFromProps("org.openntf.domino.logging.OpenLogHandler.OpenLogErrorsLevel");

	// debugOut is the PrintStream that errors will be printed to, for debug
	// levels greater than 1 (System.err by default)
	/** The debug out. */
	public static PrintStream debugOut = System.err;

	/*
	 * Constructor
	 */
	/**
	 * Instantiates a new open log item.
	 */
	public OpenLogItem() {

	}

	/**
	 * Instantiates a new open log item.
	 * 
	 * @param s
	 *            the s
	 */
	public OpenLogItem(Session s) {
		if (s != null) {
			setSession(s);
		}
	}

	/**
	 * Sets the Session property of the OpenLogItem.
	 * 
	 * @param s
	 *            Session
	 */
	private void setSession(Session s) {
		_session = s;
	}

	/**
	 * Gets the _session object, which may have been overridden if passed into logError / logEvent.
	 * 
	 * @return Session
	 */
	private Session getSession() {
		if (_session == null) {
			_session = Factory.getSession();
		}
		return _session;
	}

	/**
	 * Sets the base Throwable for the OpenLogItem.
	 * 
	 * @param base
	 *            Throwable - base throwable to be logged
	 */
	public void setBase(Throwable base) {
		_baseException = base;
	}

	/**
	 * Retrieves the base Throwable passed into the OpenLogItem.
	 * 
	 * @return throwable to be logged
	 */
	public Throwable getBase() {
		return _baseException;
	}

	/**
	 * Sets the severity for the current OpenLogItem.
	 * 
	 * @param severity
	 *            java.util.logging.Level
	 * 
	 *            Options:
	 *            <ul>
	 *            <li>Level.SEVERE</li>
	 *            <li>Level.WARNING</li>
	 *            <li>Level.INFO</li>
	 *            <li>Level.CONFIG</li>
	 *            <li>Level.FINE</li>
	 *            <li>Level.FINER</li>
	 *            <li>Level.FINEST</li>
	 *            </ul>
	 */
	public void setSeverity(Level severity) {
		_severity = severity;
	}

	/**
	 * Sets the message to be logged in this OpenLogItem.
	 * 
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		_message = message;
	}

	/**
	 * Retrieves the current database path.
	 * 
	 * @return the current database path
	 */
	public String getCurrentDatabasePath() {
		if (_thisDatabase == null) {
			try {
				Database db = getCurrentDatabase();
				if (db != null) {
					_thisDatabase = getCurrentDatabase().getFilePath();
				}
			} catch (Exception e) {
				debugPrint(e);
			}
		}
		return _thisDatabase;
	}

	/**
	 * Retrieves the current server name._session = Factory.getSession();
	 * 
	 * @return the current servername or a blank String if local
	 */
	public String getThisServer() {
		if (_thisServer == null) {
			try {
				_thisServer = getSession().getServerName();
				if (_thisServer == null)
					_thisServer = "";
			} catch (Exception e) {
				debugPrint(e);
			}
		}
		return _thisServer;
	}

	/**
	 * Retrieves the agent or XPage name the error occurred on.
	 * 
	 * @return the agent name / XPage name
	 */
	public String getThisAgent() {
		if (_thisAgent == null) {
			setThisAgent(true);
		}
		return _thisAgent;
	}

	// TODO Complete setThisAgent
	/**
	 * Sets the agent or XPage name the error occurred on.
	 * 
	 * @param currPage
	 *            whether or not the current XPage should be used or previous (if the user has been re-routed to an Error page)
	 */
	public void setThisAgent(boolean currPage) {
		_thisAgent = "org.openntf.domino";
	}

	/**
	 * Gets the OpenLog database to be logged to.
	 * 
	 * @return the Log Database
	 */
	public Database getLogDb() {
		if (_logDb == null) {
			try {
				_logDb = getSession().getDatabase(getThisServer(), getLogDbName(), false);
			} catch (Exception e) {
				debugPrint(e);
			}
		} else {
			if (Base.isLocked(_logDb)) {
				_logDb = getSession().getDatabase(getThisServer(), getLogDbName(), false);
			}
		}
		return _logDb;
	}

	/**
	 * Gets the current database.
	 * 
	 * @return current Database object
	 */
	public Database getCurrentDatabase() {
		if (_currentDatabase == null) {
			try {
				_currentDatabase = getSession().getCurrentDatabase();
			} catch (Exception e) {
				debugPrint(e);
			}
		}
		return _currentDatabase;
	}

	/**
	 * Retrieves the UserName property of the current Session.
	 * 
	 * @return the userName property from the Session
	 */
	public String getUserName() {
		try {
			return getSession().getUserName();
		} catch (Exception e) {
			debugPrint(e);
			return "";
		}
	}

	/**
	 * Retrieves the EffectiveUserName property of the current Session.
	 * 
	 * @return the Effective User Name of the session
	 */
	public String getEffName() {
		try {
			return getSession().getEffectiveUserName();
		} catch (Exception e) {
			debugPrint(e);
			return "";
		}
	}

	/**
	 * Retrieves the access level of the current user.
	 * 
	 * @return the access level for the current user
	 */
	public String getAccessLevel() {
		if (_accessLevel == null) {
			try {
				Database db = getCurrentDatabase();
				if (db != null) {
					switch (db.getCurrentAccessLevel()) {
					case 0:
						_accessLevel = "0: No Access";
						break;
					case 1:
						_accessLevel = "1: Depositor";
						break;
					case 2:
						_accessLevel = "2: Reader";
						break;
					case 3:
						_accessLevel = "3: Author";
						break;
					case 4:
						_accessLevel = "4: Editor";
						break;
					case 5:
						_accessLevel = "5: Designer";
						break;
					case 6:
						_accessLevel = "6: Manager";
						break;
					}
				}
			} catch (Exception e) {
				debugPrint(e);
			}
		}
		return _accessLevel;
	}

	/**
	 * Retrieves the roles for the current user for the current database.
	 * 
	 * @return the user roles of the current user
	 */
	public Vector<Object> getUserRoles() {
		if (_userRoles == null) {
			try {
				_userRoles = Factory.wrappedEvaluate(getSession(), "@UserRoles");
			} catch (Exception e) {
				debugPrint(e);
			}
		}
		return _userRoles;
	}

	/**
	 * Retrieves the Notes Client or Domino Server version.
	 * 
	 * @return the version of the Notes Client or server, if running on server
	 */
	public Vector<String> getClientVersion() {
		if (_clientVersion == null) {
			_clientVersion = new Vector<String>();
			try {
				String cver = getSession().getNotesVersion();
				if (cver != null) {
					if (cver.indexOf("|") > 0) {
						_clientVersion.addElement(cver.substring(0, cver.indexOf("|")));
						_clientVersion.addElement(cver.substring(cver.indexOf("|") + 1));
					} else {
						_clientVersion.addElement(cver);
					}
				}
			} catch (Exception e) {
				debugPrint(e);
			}
		}
		return _clientVersion;
	}

	/**
	 * Retrieves the start time for the OpenLogItem.
	 * 
	 * @return the start time
	 */
	public DateTime getStartTime() {
		if (_startTime == null) {
			try {
				Date _startJavaTime = new Date();
				_startTime = getSession().createDateTime(_startJavaTime);
			} catch (Exception e) {
				debugPrint(e);
			}
		}
		return _startTime;
	}

	/**
	 * Retrieves the value for a property from logging.properties file.
	 * 
	 * @param propertyName
	 *            property to be retrieved from the Properties file
	 * @return the value of the property
	 */
	public String loadFromProps(String propertyName) {
		try {
			Properties prop = new Properties();
			prop.load(LogUtils.class.getResourceAsStream("logging.properties"));
			return prop.getProperty(propertyName);
		} catch (IOException e) {
			DominoUtils.handleException(e);
			return "";
		}
	}

	/**
	 * Retrieves the Log Database name to log to.
	 * 
	 * @return the log database name
	 */
	public String getLogDbName() {
		if ("".equals(_logDbName)) {
			String logDbName = loadFromProps("org.openntf.domino.logging.OpenLogHandler.logDbName");
			if ("".equals(logDbName)) {
				setLogDbName(DominoUtils.getDominoIniVar("OpenLogPath", "OpenLog.nsf"));
			} else {
				setLogDbName(logDbName);
			}
		}
		return _logDbName;
	}

	/**
	 * Retrieves the Form name for the log document.
	 * 
	 * @return the Form name of the log document
	 */
	public String getLogFormName() {
		return _logFormName;
	}

	/**
	 * Retrieves the error line for the current Throwable.
	 * 
	 * @param ee
	 *            the ee
	 * @return the error line of the stack trace
	 */
	public int getErrLine(Throwable ee) {
		return ee.getStackTrace()[0].getLineNumber();
	}

	/**
	 * Retrieves the severity level for the OpenLogItem.
	 * 
	 * @return the severity level
	 * 
	 * @see #setSeverity(Level) for options
	 */
	public Level getSeverity() {
		return _severity;
	}

	/**
	 * Retrieves the event time of the OpenLogItem.
	 * 
	 * @return the event time
	 */
	public DateTime getEventTime() {
		if (_eventTime == null) {
			_eventJavaTime = new Date();
			_eventTime = getSession().createDateTime(_eventJavaTime);

		}
		return _eventTime;
	}

	/**
	 * Retrieves the event type - Error or Event.
	 * 
	 * @return the event type
	 */
	public String getEventType() {
		return _eventType;
	}

	/**
	 * Retrieves the error message.
	 * 
	 * @return the error message
	 */
	public String getMessage() {
		if (_message.length() > 0)
			return _message;
		return getBase().getMessage();
	}

	/**
	 * Retrieves the Document the error occurred on, if one exists.
	 * 
	 * @return the Document to be logged into the log document
	 */
	public Document getErrDoc() {
		if (_errDoc != null) {
			if (Base.isRecycled((NotesBase) _errDoc)) {
				_errDoc = getCurrentDatabase().getDocumentByUNID(_errDocUnid);
			}
		}
		return _errDoc;
	}

	/**
	 * Sets the Document on which the error occurred.
	 * 
	 * @param doc
	 *            the Document to be logged into the log document
	 */
	public void setErrDoc(Document doc) {
		if (doc != null) {
			_errDoc = doc;
			try {
				_errDocUnid = doc.getUniversalID();
			} catch (Exception ee) { // Added PW
				debugPrint(ee); // Added PW
			}
		}
	}

	/**
	 * Sets the Log Database name, allowing overriding.
	 * 
	 * @param newLogPath
	 *            new log database path, to modify initial option
	 */
	public void setLogDbName(String newLogPath) {
		_logDbName = newLogPath;
	}

	/**
	 * Sets the new debug level. A String is used because the value is initially set from a properties file
	 * 
	 * Right now the valid debug levels are:
	 * <ul>
	 * <li>0 -- internal errors are discarded</li>
	 * <li>1 -- Exception messages from internal errors are printed</li>
	 * <li>2 -- stack traces from internal errors are also printed</li>
	 * </ul>
	 * 
	 * @param newDebugLevel
	 *            new debug level for any errors generated when creating log document
	 * 
	 */
	public void setOlDebugLevel(String newDebugLevel) {
		olDebugLevel = newDebugLevel;
	}

	/**
	 * Retrieve what the status of the last logging event was.
	 * 
	 * @return success (true) or failure (false)
	 */
	public boolean getLogSuccess() {
		return _logSuccess;
	}

	/*
	 */
	/**
	 * The basic method used to log an error. Just pass the Exception that you caught and this method collects information and saves it to
	 * the OpenLog database.
	 * 
	 * @param ee
	 *            Throwable to be logged
	 * @return the error message
	 */
	public String logError(Throwable ee) {
		if (ee != null) {
			for (StackTraceElement elem : ee.getStackTrace()) {
				if (elem.getClassName().equals(getClass().getName())) {
					// NTF - we are by definition in a loop
					System.out.println(ee.toString());
					debugPrint(ee);
					_logSuccess = false;
					return "";
				}
			}
		}
		try {
			// TODO: Add to errors block in XPages
			// StackTraceElement[] s = ee.getStackTrace();
			// FacesMessage m = new FacesMessage("Error in " + s[0].getClassName() + ", line " + s[0].getLineNumber() + ": " +
			// ee.toString());
			// JSFUtil.getXSPContext().getFacesContext().addMessage(null, m);
			setBase(ee);

			// if (ee.getMessage().length() > 0) {
			if (ee.getMessage() != null) {
				setMessage(ee.getMessage());
			} else {
				setMessage(ee.getClass().getCanonicalName());
			}
			setSeverity(Level.WARNING);
			setEventType(TYPE_ERROR);

			_logSuccess = writeToLog();
			return getMessage();

		} catch (Exception e) {
			System.out.println(e.toString());
			debugPrint(e);
			_logSuccess = false;
			return "";
		}
	}

	/**
	 * Sets the OpenLogItem type - Event or Error.
	 * 
	 * @param typeError
	 *            the new event type
	 */
	private void setEventType(String typeError) {
		_eventType = typeError;
	}

	/**
	 * A More flexible way to send an error to the OpenLog database.
	 * 
	 * @param ee
	 *            Throwable to be logged
	 * @param msg
	 *            Specific error message to be logged
	 * @param severityType
	 *            Severity level, @see {@link #setSeverity(Level)}
	 * @param doc
	 *            Document to be added as a link to the OpenLog document
	 * @return message logged in
	 */
	public String logErrorEx(Throwable ee, String msg, Level severityType, Document doc) {
		if (ee != null) {
			for (StackTraceElement elem : ee.getStackTrace()) {
				if (elem.getClassName().equals(getClass().getName())) {
					// NTF - we are by definition in a loop
					System.out.println(ee.toString());
					debugPrint(ee);
					_logSuccess = false;
					return "";
				}
			}
		}
		try {
			setBase((ee == null ? new Throwable() : ee));
			setMessage((msg == null ? "" : msg));
			setSeverity(severityType == null ? Level.WARNING : severityType);
			setEventType(TYPE_ERROR);
			setErrDoc(doc);

			_logSuccess = writeToLog();
			return msg;

		} catch (Exception e) {
			debugPrint(e);
			_logSuccess = false;
			return "";
		}
	}

	/**
	 * This method allows you to log an Event to the OpenLog database.
	 * 
	 * @param ee
	 *            Throwable to be logged
	 * @param msg
	 *            Specific event message to be logged
	 * @param severityType
	 *            Severity level, @see {@link #setSeverity(Level)}
	 * @param doc
	 *            the doc
	 * @return message logged in
	 */
	public String logEvent(Throwable ee, String msg, Level severityType, Document doc) {
		try {
			setMessage(msg);
			setSeverity(severityType == null ? Level.INFO : severityType);
			setEventType(TYPE_EVENT);
			setErrDoc(doc);
			if (ee == null) { // Added PW - LogEvent will not pass a throwable
				setBase(new Throwable("")); // Added PW
			} else { // Added PW
				setBase(ee); // Added PW
			} // Added PW
			_logSuccess = writeToLog();
			return msg;

		} catch (Exception e) {
			debugPrint(e);
			_logSuccess = false;
			return "";
		}
	}

	/**
	 * Retrieves the stack trace of an Exception as an ArrayList without the initial error message. Also skips over a given number of items
	 * (as determined by the skip parameter)
	 * 
	 * @param ee
	 *            Throwable passed into the OpenLogItem
	 * @param skip
	 *            number of elements to skip
	 * @return ArrayList of elements of stack trace
	 */
	private ArrayList<String> getStackTrace(Throwable ee, int skip) {
		ArrayList<String> v = new ArrayList<String>(32);
		try {
			StringWriter sw = new StringWriter();
			ee.printStackTrace(new PrintWriter(sw));
			StringTokenizer st = new StringTokenizer(sw.toString(), "\n");
			int count = 0;
			while (st.hasMoreTokens()) {
				if (skip <= count++)
					v.add(st.nextToken().trim());
				else
					st.nextToken();
			}

		} catch (Exception e) {
			debugPrint(e);
		}

		return v;
	}

	/**
	 * Gets the Stack Trace from a Throwable, passing to getStackTrace(Throwable, int) passing 0 as second parameter.
	 * 
	 * @param ee
	 *            Throwable passed into the OpenLogItem
	 * @return ArrayList of elements of stack trace
	 */
	private ArrayList<String> getStackTrace(Throwable ee) {
		return getStackTrace(ee, 0);
	}

	/**
	 * Logs an error using the passed Session.
	 * 
	 * @param s
	 *            Session to log the error against
	 * @param ee
	 *            Throwable to be logged
	 */
	public void logError(Session s, Throwable ee) {
		setSession(s);
		logError(ee);
	}

	/**
	 * Logs an error with extended options using the passed Session.
	 * 
	 * @param s
	 *            Session to log the error against
	 * @param ee
	 *            Throwable to be logged
	 * @param message
	 *            message to be logged
	 * @param severityType
	 *            Severity level, @see {@link #setSeverity(Level)}
	 * @param doc
	 *            Document to be added as a link to the OpenLog document
	 */
	public void logError(Session s, Throwable ee, String message, Level severityType, Document doc) {
		setSession(s);
		logErrorEx(ee, message, severityType, doc);
	}

	/**
	 * Logs an event with extended options using the passed Session.
	 * 
	 * @param s
	 *            Session to log the error against
	 * @param ee
	 *            Throwable to be logged
	 * @param message
	 *            message to be logged
	 * @param severityType
	 *            Severity level, @see {@link #setSeverity(Level)}
	 * @param doc
	 *            Document to be added as a link to the OpenLog document
	 */
	public void logEvent(Session s, Throwable ee, String message, Level severityType, Document doc) {
		setSession(s);
		logEvent(ee, message, severityType, doc);
	}

	/**
	 * This is the method that does the actual logging to the OpenLog database.
	 * 
	 * This method creates a document in the log database, populates the fields of that document with the values in the global variables,
	 * and adds some associated information about any Document that needs to be referenced.
	 * 
	 * @return true, if successful
	 */
	public boolean writeToLog() {
		// exit early if there is no database
		Database db = getLogDb();
		if (db == null) {
			return false;
		}

		boolean retval = false;
		Document logDoc = null;
		RichTextItem rtitem = null;
		Database docDb = null;

		try {
			logDoc = db.createDocument();

			logDoc.appendItemValue("Form", _logFormName);

			Throwable ee = getBase();
			StackTraceElement ste = ee.getStackTrace()[0];
			if (ee instanceof NotesException) {
				logDoc.replaceItemValue("LogErrorNumber", ((NotesException) ee).id);
				logDoc.replaceItemValue("LogErrorMessage", ((NotesException) ee).text);
			} else {
				// Fixed next line
				logDoc.replaceItemValue("LogErrorMessage", getMessage());
			}

			logDoc.replaceItemValue("LogStackTrace", getStackTrace(ee));
			logDoc.replaceItemValue("LogErrorLine", ste.getLineNumber());
			logDoc.replaceItemValue("LogSeverity", getSeverity().getName());
			logDoc.replaceItemValue("LogEventTime", getEventTime());
			logDoc.replaceItemValue("LogEventType", getEventType());
			logDoc.replaceItemValue("LogMessage", getMessage());
			logDoc.replaceItemValue("LogFromDatabase", getCurrentDatabasePath());
			logDoc.replaceItemValue("LogFromServer", getThisServer());
			logDoc.replaceItemValue("LogFromAgent", getThisAgent());
			// Fixed next line
			logDoc.replaceItemValue("LogFromMethod", ste.getClassName() + "." + ste.getMethodName());
			logDoc.replaceItemValue("LogAgentLanguage", "Java");
			logDoc.replaceItemValue("LogUserName", getUserName());
			logDoc.replaceItemValue("LogEffectiveName", getEffName());
			logDoc.replaceItemValue("LogAccessLevel", getAccessLevel());
			logDoc.replaceItemValue("LogUserRoles", getUserRoles());
			logDoc.replaceItemValue("LogClientVersion", getClientVersion());
			logDoc.replaceItemValue("LogAgentStartTime", getStartTime());

			if (getErrDoc() != null) {
				docDb = getErrDoc().getParentDatabase();
				rtitem = logDoc.createRichTextItem("LogDocInfo");
				rtitem.appendText("The document associated with this event is:");
				rtitem.addNewLine(1);
				rtitem.appendText("Server: " + docDb.getServer());
				rtitem.addNewLine(1);
				rtitem.appendText("Database: " + docDb.getFilePath());
				rtitem.addNewLine(1);
				rtitem.appendText("UNID: " + getErrDoc().getUniversalID());
				rtitem.addNewLine(1);
				rtitem.appendText("Note ID: " + getErrDoc().getNoteID());
				rtitem.addNewLine(1);
				rtitem.appendText("DocLink: ");
				rtitem.appendDocLink(_errDoc, getErrDoc().getUniversalID());
			}

			// make sure Depositor-level users can add documents too
			logDoc.appendItemValue("$PublicAccess", "1");

			logDoc.save(true);
			retval = true;
		} catch (Exception e) {
			debugPrint(e);
			retval = false;
		}

		return retval;
	}

	/**
	 * This method decides what to do with any Exceptions that we encounter internal to this class, based on the olDebugLevel variable.
	 * 
	 * @param ee
	 *            the ee
	 */
	private void debugPrint(Throwable ee) {
		if ((ee == null) || (debugOut == null))
			return;

		try {
			// debug level of 1 prints the basic error message#
			int debugLevel = Integer.parseInt(olDebugLevel);
			if (debugLevel >= 1) {
				String debugMsg = ee.toString();
				try {
					if (ee instanceof NotesException) {
						NotesException ne = (NotesException) ee;
						debugMsg = "Notes error " + ne.id + ": " + ne.text;
					}
				} catch (Exception e2) {
				}
				debugOut.println("OpenLogItem error: " + debugMsg);
			}

			// debug level of 2 prints the whole stack trace
			if (debugLevel >= 2) {
				debugOut.print("OpenLogItem error trace: ");
				ee.printStackTrace(debugOut);
			}
		} catch (Exception e) {
			// at this point, if we have an error just discard it
		}
	}
}
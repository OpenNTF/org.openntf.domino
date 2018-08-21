/**
 *
 */
package org.openntf.domino.logging;

import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;

/**
 * Interface for an object containing logging information to be written to a document in the OpenLog database.
 * 
 * @author withersp
 *
 */
public interface IOpenLogItem {

	/**
	 * Enum to define log type
	 *
	 * @since org.openntf.domino 4.0.0
	 */
	public static enum LogType {
		TYPE_ERROR("Error"), TYPE_EVENT("Event");

		private final String value_;

		private LogType(final String value) {
			value_ = value;
		}

		public String getValue() {
			return value_;
		}
	}

	/**
	 * Enum to define debug level if errors are encountered writing the log - do nothing, print summary to console or print full details to
	 * console
	 *
	 * @since org.openntf.domino 4.0.0
	 */
	public static enum DebugLevel {
		LEVEL_DISCARD("0"), LEVEL_SUMMARY("1"), LEVEL_FULL("2");

		private final String value_;

		private DebugLevel(final String value) {
			value_ = value;
		}

		public String getValue() {
			return value_;
		}
	}

	/**
	 * This method decides what to do with any Exceptions that we encounter internal to this class, based on the olDebugLevel variable.
	 *
	 * @param ee
	 *            the ee
	 * @since org.openntf.domino 4.0.0
	 */
	abstract void debugPrint(final Throwable ee);

	/**
	 * Retrieves the access level of the current user.
	 *
	 * @return the access level for the current user
	 * @since org.openntf.domino 4.0.0
	 */
	public String getAccessLevel();

	/**
	 * Retrieves the base Throwable passed into the OpenLogItem.
	 *
	 * @return throwable to be logged
	 * @since org.openntf.domino 4.0.0
	 */
	public Throwable getBase();

	/**
	 * Retrieves the Notes Client or Domino Server version.
	 *
	 * @return the version of the Notes Client or Domino server, if running on server
	 * @since org.openntf.domino 4.0.0
	 */
	public Vector<String> getClientVersion();

	/**
	 * Gets the "current" database.
	 *
	 * @return current Database object the error is being logged for
	 * @since org.openntf.domino 4.0.0
	 */
	public Database getCurrentDatabase();

	/**
	 * Sets the current database object to the current database.
	 *
	 * @since org.openntf.domino 5.0.0
	 */
	public void setCurrentDatabase();

	/**
	 * Sets the current database object to a specific database, useful when running outside XPages containers, e.g. Xots.
	 *
	 * @param db
	 *            Database specific database to set as running from
	 * @since org.openntf.domino 5.0.0
	 */
	public void setCurrentDatabase(Database db);

	/**
	 * Gets the "current" database path.
	 *
	 * @return String database path error is being logged for
	 * @since org.openntf.domino 5.0.0
	 */
	public String getCurrentDatabasePath();

	/**
	 * Retrieves the Document the error occurred on, if one exists.
	 *
	 * @return the Document to be logged into the log document
	 * @since org.openntf.domino 4.0.0
	 */
	public Document getErrDoc();

	/**
	 * Retrieves the error line for the current Throwable.
	 *
	 * @param ee
	 *            the ee
	 * @return the error line of the stack trace
	 * @since org.openntf.domino 4.0.0
	 */
	public int getErrLine(final Throwable ee);

	/**
	 * Retrieves the event time of the OpenLogItem.
	 *
	 * @return the event time
	 * @since org.openntf.domino 4.0.0
	 */
	public Date getEventTime();

	/**
	 * Retrieves the event type - Error or Event.
	 *
	 * @return the event type
	 * @since org.openntf.domino 4.0.0
	 */
	public String getEventType();

	/**
	 * Gets the OpenLog database to be logged to.
	 *
	 * @return the Log Database
	 */
	public Database getLogDb();

	/**
	 * Retrieves the Log Database name to log to.
	 *
	 * @return the log database name
	 * @since org.openntf.domino 4.0.0
	 */
	public String getLogDbName();

	/**
	 * Retrieves whether or not to suppress event strack trace
	 *
	 * @return Boolean of whether or not to suppress stack trace
	 * @since org.openntf.domino 5.0.0
	 */
	public Boolean getSuppressEventStack();

	/**
	 * Sets whether the stack trace should be suppressed from Event logs
	 *
	 * @param error
	 *            whether or not to display the errors
	 * @since org.openntf.domino 5.0.0
	 */
	public void setSuppressEventStack(final Boolean suppressEventStack);

	/**
	 * Retrieves the Form name for the log document.
	 *
	 * @return the Form name of the log document
	 * @since org.openntf.domino 4.0.0
	 */
	public String getLogFormName();

	/**
	 * Retrieve what the status of the last logging event was.
	 *
	 * @return success (true) or failure (false)
	 * @since org.openntf.domino 4.0.0
	 */
	public boolean getLogSuccess();

	/**
	 * Retrieves the error message.
	 *
	 * @return the error message
	 * @since org.openntf.domino 4.0.0
	 */
	public String getMessage();

	/**
	 * Retrieves the severity level for the OpenLogItem.
	 *
	 * @return the severity level
	 *
	 * @see #setSeverity(Level) for options
	 * @since org.openntf.domino 4.0.0
	 */
	public Level getSeverity();

	/**
	 * Retrieves the stack trace of an Exception as an ArrayList without the initial error message. Also skips over a given number of items
	 * (as determined by the skip parameter)
	 *
	 * @param ee
	 *            Throwable passed into the OpenLogItem
	 * @param skip
	 *            number of elements to skip
	 * @return ArrayList of elements of stack trace
	 * @since org.openntf.domino 4.0.0
	 */
	public ArrayList<String> getStackTrace(final Throwable ee, final int skip);

	/**
	 * Gets the Stack Trace from a Throwable, passing to getStackTrace(Throwable, int) passing 0 as second parameter.
	 *
	 * @param ee
	 *            Throwable passed into the OpenLogItem
	 * @return ArrayList of elements of stack trace
	 */
	public ArrayList<String> getStackTrace(final Throwable ee);

	/**
	 * Retrieves the start time for the OpenLogItem.
	 *
	 * @return the start time
	 * @since org.openntf.domino 4.0.0
	 */
	public Date getStartTime();

	/**
	 * Retrieves the current server name._session = Factory.getSession();
	 *
	 * @return the current servername or a blank String if local
	 * @since org.openntf.domino 4.0.0
	 */
	public String getThisServer();

	/**
	 * Retrieves the roles for the current user for the current database.
	 *
	 * @return the user roles of the current user
	 * @since org.openntf.domino 4.0.0
	 */
	public Vector<Object> getUserRoles();

	/**
	 * Logs an error using the passed Session.
	 *
	 * @param s
	 *            Session to log the error against
	 * @param ee
	 *            Throwable to be logged
	 * @since org.openntf.domino 4.0.0
	 */
	public void logError(final Session s, final Throwable ee);

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
	 * @since org.openntf.domino 4.0.0
	 */
	public void logError(final Session s, final Throwable ee, final String msg, final Level severityType, final Document doc);

	/**
	 * The basic method used to log an error. Just pass the Exception that you caught and this method collects information and saves it to
	 * the OpenLog database.
	 *
	 * @param ee
	 *            Throwable to be logged
	 * @return the error message
	 * @since org.openntf.domino 4.0.0
	 */
	public String logError(final Throwable ee);

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
	 * @since org.openntf.domino 4.0.0
	 */
	public String logErrorEx(final Throwable ee, final String msg, final Level severityType, final Document doc);

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
	 * @since org.openntf.domino 4.0.0
	 */
	public void logEvent(final Session s, final Throwable ee, final String msg, final Level severityType, final Document doc);

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
	 * @since org.openntf.domino 4.0.0
	 */
	public String logEvent(final Throwable ee, final String msg, final Level severityType, final Document doc);

	/**
	 * Sets the base Throwable for the OpenLogItem.
	 *
	 * @param base
	 *            Throwable - base throwable to be logged
	 * @since org.openntf.domino 4.0.0
	 */
	public void setBase(final Throwable base);

	/**
	 * Sets the Document on which the error occurred.
	 *
	 * @param doc
	 *            the Document to be logged into the log document
	 * @since org.openntf.domino 4.0.0
	 */
	public void setErrDoc(final Document doc);

	/**
	 * Sets the OpenLogItem type - Event or Error.
	 *
	 * @param typeError
	 *            the new event type
	 * @since org.openntf.domino 4.0.0
	 */
	public void setEventType(final LogType typeError);

	/**
	 * Sets the Log Database name, allowing overriding.
	 *
	 * @param newLogPath
	 *            new log database path, to modify initial option
	 * @since org.openntf.domino 4.0.0
	 */
	public void setLogDbName(final String newLogPath);

	/**
	 * Sets what the status of the logging event is
	 *
	 * @param logsuccess
	 *            boolean success or failure
	 * @since org.openntf.domino 4.0.0
	 */
	public void setLogSuccess(final boolean logsuccess);

	/**
	 * Sets the message to be logged in this OpenLogItem.
	 *
	 * @param message
	 *            the message to set
	 * @since org.openntf.domino 4.0.0
	 */
	public void setMessage(final String message);

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
	 * @since org.openntf.domino 4.0.0
	 *
	 */
	public void setOlDebugLevel(final DebugLevel newDebugLevel);

	/**
	 * Sets the Session property of the OpenLogItem.
	 *
	 * @param s
	 *            Session
	 * @since org.openntf.domino 4.0.0
	 */
	abstract void setSession(final Session s);

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
	 * @since org.openntf.domino 4.0.0
	 */
	public void setSeverity(final Level severity);

	/**
	 * Sets the agent / page the error comes from
	 *
	 * @param fromContext
	 *            String context the error log is coming from
	 * @since org.openntf.domino 4.0.0
	 */
	public void setThisAgent(String fromContext);

	/**
	 * Sets the roles
	 *
	 * @param roles
	 *            Vector<Object> of user roles
	 * @since org.openntf.domino 4.0.0
	 */
	public void setUserRoles(Vector<Object> roles);

	/**
	 * This is the method that does the actual logging to the OpenLog database.
	 *
	 * This method creates a document in the log database, populates the fields of that document with the values in the global variables,
	 * and adds some associated information about any Document that needs to be referenced.
	 *
	 * @return true, if successful
	 * @since org.openntf.domino 4.0.0
	 */
	public boolean writeToLog();

}

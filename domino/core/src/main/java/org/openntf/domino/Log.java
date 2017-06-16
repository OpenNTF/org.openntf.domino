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

import java.util.Vector;

import org.openntf.domino.types.FactorySchema;
import org.openntf.domino.types.SessionDescendant;

/**
 * The Interface Log enables you to record actions and errors that take place during a program's execution. You can record actions and
 * errors in
 * <ul>
 * <li>A Domino database
 * <li>A mail memo
 * <li>A file (for programs that run locally)
 * <li>An agent log (for agents)
 * </ul>
 */
public interface Log extends Base<lotus.domino.Log>, lotus.domino.Log, org.openntf.domino.ext.Log, SessionDescendant {

	public static class Schema extends FactorySchema<Log, lotus.domino.Log, Session> {
		@Override
		public Class<Log> typeClass() {
			return Log.class;
		}

		@Override
		public Class<lotus.domino.Log> delegateClass() {
			return lotus.domino.Log.class;
		}

		@Override
		public Class<Session> parentClass() {
			return Session.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/**
	 * Closes a log.
	 * <p>
	 * If you are logging to a mail message, this method sends the mail message to its recipient(s).
	 * </p>
	 */
	@Override
	public void close();

	/**
	 * The number of actions logged so far.
	 *
	 * @return The number of actions as an Integer
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public int getNumActions();

	/**
	 * The number of errors logged so far.
	 * <p>
	 * The NumErrors property is not incremented until after {@link #logError} is called. To get the correct count at the time of a call to
	 * logError, increment getNumErrors by 1.
	 *
	 * @return The number of errors as an Integer
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public int getNumErrors();

	/**
	 * The Domino session that contains a Log object.
	 *
	 * @return The {@link Session} that contains the log object.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public Session getParent();

	/**
	 * The name that identifies the agent whose actions and errors you're logging. The name is the same as the name specified with
	 * {@link Session#createLog(String)}.
	 *
	 * @return The name of the log object. {@link Session#createLog}.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public String getProgramName();

	/**
	 * Indicates if action logging is enabled or not.
	 * <p>
	 * The {@link #logAction} method has no effect while the {@link #isLogActions() LogActions} property is false.
	 *
	 * @return Returns <code>true</code> if action logging is enabled, otherwise returns <code>false</code>
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public boolean isLogActions();

	/**
	 * Indicates if error logging is enabled or not.
	 * <p>
	 * The {@link #logError} method has no effect while the {@link #isLogErrors() LogErrors} property is false.
	 *
	 * @return Returns <code>true</code> if error logging is enabled, otherwise returns <code>false</code>
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public boolean isLogErrors();

	/**
	 * For a log that records to a file, indicates if the log should write over the existing file or append to it.
	 * <p>
	 * This property has no effect on logs that record to a mail message or database.
	 * </p>
	 * <p>
	 * To write over an existing log file, you must set this property to true before calling the {@link #openFileLog(String)} method.
	 * </p>
	 *
	 * @return Returns <code>true</code> if overwriting is enabled, otherwise returns <code>false</code>
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public boolean isOverwriteFile();

	/**
	 * Records an action in a log.
	 * <p>
	 * The behavior of this method depends upon the type of log you open.
	 * <p>
	 * If you open a Domino database using {@link #openNotesLog(String, String)}, this method creates a new document in the database. The
	 * A$ACTION item in the document contains the description.
	 * <p>
	 * If you open a mail memo using {@link #openMailLog(Vector, String)}, this method adds the description to the Body item of the memo,
	 * along with the current date and time.
	 * <p>
	 * If you open a file using {@link #openFileLog(String)}, this method adds the description to the next line of the file, along with the
	 * log's ProgramName and the current date and time.
	 *
	 * @param action
	 *            A description of the action, as you want it to appear in the log.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public void logAction(final String action);

	/**
	 * Records an error in a log.
	 * <p>
	 * The behavior of this method depends upon the type of log you open.
	 * <p>
	 * If you open a Domino database using {@link #openNotesLog(String, String)}, this method creates a new document in the database. The
	 * A$ERRMSG item in the document contains the description.
	 * <p>
	 * If you open a mail memo using {@link #openMailLog(Vector, String)}, this method adds the description to the Body item of the memo,
	 * along with the current date and time.
	 * <p>
	 * If you open a file using {@link #openFileLog(String)}, this method adds the description to the next line of the file, along with the
	 * log's ProgramName and the current date and time.
	 *
	 * @param code
	 *            A number indicating which error occurred.
	 * @param text
	 *            A description of the action, as you want it to appear in the log.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public void logError(final int code, final String text);

	/**
	 * Sends a Domino event out to the network.
	 * <p>
	 * Only scripts running on a server can use this method.
	 * </p>
	 * <p>
	 * This method is a way to post to API event queues and does not affect database, mail, file and agent logs. Using the LogAction or
	 * LogError methods has no effect on event logging.
	 * </p>
	 *
	 * @param text
	 *            The message to send to the network.
	 * @param queue
	 *            The name of the queue. The queue is picked for you if you send an empty string.
	 * @param event
	 *            Indicates the kind of event being logged.
	 * @param severity
	 *            Indicates the severity of the event being logged.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public void logEvent(final String text, final String queue, final int event, final int severity);

	/**
	 * Opens the agent log for the current agent.
	 * <p>
	 * This method stores output in the log for the current agent and fails if the program is not running as an agent. To display an agent
	 * log, select the agent and choose Agent > Log. The log also displays after you run an agent with Actions > Run.
	 *
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public void openAgentLog();

	/**
	 * Starts logging to a file.
	 * <p>
	 * This method returns an error if you call it on a server.
	 * <p>
	 * To write over an existing log file, you must set the {@link #setOverwriteFile(boolean)} property to <code>true</code> before calling
	 * openFileLog.
	 *
	 * @param filePath
	 *            The path and file name of the log file. If the file does not exist, the method creates it for you. If a directory in the
	 *            path does not exist, the method throws an exception.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public void openFileLog(final String filePath);

	/**
	 * Opens a new mail memo for logging.
	 * <p>
	 * The memo is mailed when the log's close method is called, or when the object is deleted.
	 * <p>
	 * When you call this method, Domino uses the current user's mail database to create and send the mail memo. The memo is not saved to
	 * the database.
	 *
	 * @param recipients
	 *            The recipients of the mail memo. Each element is an object of type String.
	 * @param subject
	 *            The subject of the mail memo.
	 * @since lotus.domino 4.5.0
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void openMailLog(final Vector recipients, final String subject);

	/**
	 * Opens a specified Domino database for logging.
	 * <p>
	 * The StdR4AgentLog template (ALOG4.NTF) is designed to display the action and error documents that Log creates. If the database you
	 * specify inherits its design from this template, you can use the database main view to see each of the items previously listed.
	 * Several agents can log to the same server and database; the database categorizes each action and error according to the A$PROGNAME
	 * item.
	 * <p>
	 * One document is created in the database for each error or action that you log. Each document contains the following items:
	 * <p>
	 * <ul>
	 * Form - "Log Entry"
	 * <li>AA$PROGNAME The ProgramName property
	 * <li>A$LOGTIME The date and time that the error or action is logged
	 * <li>A$USER The user at the time the error or action is logged
	 * <li>A$LOGTYPE "Error" or "Action"
	 * <li>A$ACTION A description of the action (actions only)
	 * <li>A$ERRCODE The error code (errors only)
	 * <li>A$ERRMSG A description of the error (errors only)
	 * </ul>
	 *
	 * @param server
	 *            The server on which the database log resides. Use <code>null</code> or an empty string to indicate the current computer: a
	 *            local database if the agent runs on a workstation; a database on that server if the agent runs on a server.
	 * @param database
	 *            The path and file name of the database.
	 */
	@Override
	public void openNotesLog(final String server, final String database);

	/**
	 * Sets if action logging is enabled or not.
	 * <p>
	 * The {@link #logAction} method has no effect while the isLogActions property is false.
	 *
	 * @param flag
	 *            Set to <code>true</code> if action logging should be enabled, otherwise set to <code>false</code>
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public void setLogActions(final boolean flag);

	/**
	 * Sets if error logging is enabled or not.
	 * <p>
	 * The {@link #logError} method has no effect while the isLogErrors property is false.
	 *
	 * @param flag
	 *            Set to <code>true</code> if error logging should be enabled, otherwise set to <code>false</code>
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public void setLogErrors(final boolean flag);

	/**
	 * Sets if file overwriting is enabled or not.
	 * <p>
	 * To write over an existing log file, you must set this property to true before calling the {@link #openFileLog(String)} method.
	 * </p>
	 *
	 * @param flag
	 *            Set to <code>true</code> if log file overwriting should be enabled, otherwise set to <code>false</code>
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public void setOverwriteFile(final boolean flag);

	/**
	 * Sets the name that identifies the agent whose actions and errors you're logging.
	 *
	 * @param name
	 *            The name you want to set.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public void setProgramName(final String name);

}

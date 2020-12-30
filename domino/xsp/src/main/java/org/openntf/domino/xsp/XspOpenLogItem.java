/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.xsp;

/*

 <!--
 Copyright 2011 Paul Withers, Nathan T. Freeman & Tim Tripcony
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and limitations under the License
 -->

 */

/*
 * Paul Withers, Intec March 2013
 * Some significant enhancements here since the version Nathan cleaned up for XPages Help Application.
 * Consequently, I've renamed the package completely, so there is no conflict
 *
 * 1. Everything is now static, so no need to create an OpenLog object
 *
 * 2. Everything now uses ExtLibUtil instead of Tim Tripcony's code (sorry, Tim! ;-) )
 *
 * 3. _logDbName and olDebugLevel are set using getXspPropertyAsString(String, String). 
 *
 * 4. setThisAgent(boolean) method has been added. By default it gets the current page.
 * Otherwise it gets the previous page. Why? Because if we've been redirected to an error page,
 * we want to know which page the ACTUAL error occurred on.
 *
 * 5. logErrorEx has been fixed. It didn't work before.
 *
 * 6. _eventTime and _startTime recycled after creating logDoc. Nathan, I'll sleep a little less tonight,
 * but it's best practice ;-)
 */

/*
 * Nathan T. Freeman, GBS Jun 20, 2011
 * Developers notes...
 *
 * There's more than I'd like to do here, but I think the entry points are much more sensible now.
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
 * All Domino objects have been made recycle-proof. All the nonsense about "init" and "reset"
 * and overloading constructors to do all the work is gone.
 *
 * There really SHOULD be an OpenLogManager that tracks settings like the java.util.Logging package does
 * but that is well beyond the scope of this little update
 *
 * Honestly, knowing that Julian does so much more Java work now, I can completely
 * sympathize with his occasional statement that OpenLog could use a major refactor.
 *
 * I wouldn't call this "major" but certainly "significant."
 *
 * One thing that would be SUPER useful is if the logEvent traced the caller automatically
 * even without a Throwable object passed. The problem is that the most likely use for this
 * entire class is from SSJS, which won't pass a contextual call stack by default.
 *
 * We'd need a LOT more infrastructure for that!
 */

import java.util.logging.Level;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lotus.domino.NotesException;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.RichTextItem;
import org.openntf.domino.logging.BaseOpenLogItem;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

import com.ibm.commons.util.StringUtil;
import com.ibm.icu.util.Calendar;
import com.ibm.xsp.extlib.util.ExtLibUtil;

@SuppressWarnings("nls")
public class XspOpenLogItem extends BaseOpenLogItem {

	private transient String logEmail_;
	private transient static String _logExpireDate;
	private transient String currXPage_;
	private transient String logDbName_;
	private transient Boolean displayError_;
	private transient String displayErrorGeneric_;
	@SuppressWarnings("unused")
	private transient Boolean suppressEventStack_;
	// this variable sets the "debug level" of all the methods
	public String olDebugLevel = getDefaultDebugLevel();

	/*
	 * Use this constructor when you're creating an instance of the class within the confines of an Agent. It will automatically pick up the
	 * agent name, the current database, and the server.
	 */
	public XspOpenLogItem() {

	}

	/**
	 * @return the thisAgent
	 */
	@Override
	public String getThisAgent() {
		if (currXPage_ == null) {
			setThisAgent(true);
		}
		return currXPage_;
	}

	public void setThisAgent(final boolean currPage) {
		try {
			String fromPage = ""; //$NON-NLS-1$
			String includeQueryString = ODAPlatform.getXspPropertyAsString("xsp.openlog.includeQueryString"); //$NON-NLS-1$
			String[] historyUrls = ExtLibUtil.getXspContext().getHistoryUrls();
			if (StringUtil.isEmpty(historyUrls)) {
				fromPage = ExtLibUtil.getXspContext().getUrl().toSiteRelativeString(ExtLibUtil.getXspContext());
			} else {
				if (currPage) {
					fromPage = historyUrls[0];
				} else {
					if (historyUrls.length > 1) {
						fromPage = historyUrls[1];
					} else {
						fromPage = historyUrls[0];
					}
				}
			}

			if (fromPage.indexOf("/") > -1) { //$NON-NLS-1$
				fromPage = fromPage.substring(1, fromPage.length());
			}
			if (!"true".equalsIgnoreCase(includeQueryString)) { //$NON-NLS-1$
				if (fromPage.indexOf("?") > -1) { //$NON-NLS-1$
					fromPage = fromPage.substring(0, fromPage.indexOf("?")); //$NON-NLS-1$
				}
			}
			currXPage_ = fromPage;
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	@Override
	public void reinitialiseSettings() {
		logEmail_ = null;
		logDbName_ = null;
		displayError_ = null;
		displayErrorGeneric_ = null;
		olDebugLevel = getDefaultDebugLevel();
		super.reinitialiseSettings();
	}

	/**
	 * @return the expire date
	 */
	public static String getLogExpireDate() {
		if (StringUtil.isEmpty(_logExpireDate)) {
			_logExpireDate = ODAPlatform.getXspPropertyAsString("xsp.openlog.expireDate"); //$NON-NLS-1$
		}
		return _logExpireDate;
	}

	/**
	 * @return the logEmail
	 */
	public String getLogEmail() {
		try {
			if (StringUtil.isEmpty(logEmail_)) {
				logEmail_ = ODAPlatform.getXspPropertyAsString("xsp.openlog.email"); //$NON-NLS-1$
			}
			return logEmail_;
		} catch (Throwable t) {
			DominoUtils.handleException(t);
			return "";
		}
	}

	/**
	 * @return the logDbName
	 */
	@Override
	public String getLogDbName() {
		try {
			if (StringUtil.isEmpty(logDbName_)) {
				String logDbName_ = ODAPlatform.getXspPropertyAsString("xsp.openlog.filepath"); //$NON-NLS-1$
				if (StringUtil.isEmpty(logDbName_)) {
					super.setLogDbName("OpenLog.nsf"); //$NON-NLS-1$
				} else if ("[CURRENT]".equalsIgnoreCase(logDbName_)) { //$NON-NLS-1$
					super.setLogDbName(getCurrentDatabase().getFilePath());
				} else {
					super.setLogDbName(logDbName_);
				}
			}
			return super.getLogDbName();
		} catch (Throwable t) {
			DominoUtils.handleException(t);
			return "OpenLog.nsf"; //$NON-NLS-1$
		}
	}

	@Override
	public Boolean getSuppressEventStack() {
		try {
			String dummyVar = ODAPlatform.getXspPropertyAsString("xsp.openlog.suppressEventStack"); //$NON-NLS-1$
			if (StringUtil.isEmpty(dummyVar)) {
				setSuppressEventStack(true);
				return true;
			} else if ("false".equalsIgnoreCase(dummyVar)) { //$NON-NLS-1$
				setSuppressEventStack(false);
				return false;
			} else {
				setSuppressEventStack(true);
				return true;
			}
		} catch (Throwable t) {
			DominoUtils.handleException(t);
			return false;
		}
	}

	@Override
	public void setSuppressEventStack(final Boolean suppressEventStack) {
		_suppressEventStack = suppressEventStack;
	}

	/**
	 * @return whether errors should be displayed or not
	 */
	public Boolean getDisplayError() {
		try {
			String dummyVar = ODAPlatform.getXspPropertyAsString("xsp.openlog.displayError"); //$NON-NLS-1$
			if (StringUtil.isEmpty(dummyVar)) {
				setDisplayError(true);
			} else if ("false".equalsIgnoreCase(dummyVar)) { //$NON-NLS-1$
				setDisplayError(false);
			} else {
				setDisplayError(true);
			}
			return displayError_;
		} catch (Throwable t) {
			DominoUtils.handleException(t);
			return false;
		}
	}

	/**
	 * @param error
	 *            whether or not to display the errors
	 */
	public void setDisplayError(final Boolean error) {
		displayError_ = error;
	}

	/**
	 * @return String of a generic error message or an empty string
	 */
	public String getDisplayErrorGeneric() {
		try {
			if (null == displayErrorGeneric_) {
				displayErrorGeneric_ = ODAPlatform.getXspPropertyAsString("xsp.openlog.genericErrorMessage"); //$NON-NLS-1$
			}
			return displayErrorGeneric_;
		} catch (Throwable t) {
			DominoUtils.handleException(t);
			return ""; //$NON-NLS-1$
		}
	}

	private String getDefaultDebugLevel() {
		try {
			String defaultLevel_ = ODAPlatform.getXspPropertyAsString("xsp.openlog.debugLevel"); //$NON-NLS-1$
			if (StringUtil.isEmpty(defaultLevel_)) {
				defaultLevel_ = "2"; //$NON-NLS-1$
			}
			return defaultLevel_;
		} catch (Throwable t) {
			DominoUtils.handleException(t);
			return "2"; //$NON-NLS-1$
		}
	}

	@Override
	public String logError(final Throwable ee) {
		String m = ""; //$NON-NLS-1$
		if (ee != null) {
			for (StackTraceElement elem : ee.getStackTrace()) {
				if (elem.getClassName().equals(XspOpenLogItem.class.getName())) {
					// NTF - we are by definition in a loop
					System.out.println(ee.toString());
					debugPrint(ee);
					setLogSuccess(false);
					return "";
				}
			}
			StackTraceElement[] s = ee.getStackTrace();
			m = "Error in " + s[0].getClassName() + ", line " + s[0].getLineNumber() + ": " + ee.toString();
			if (ee.getMessage() != null) {
				setMessage(ee.getMessage());
			} else {
				setMessage(ee.getClass().getCanonicalName());
			}
		} else {
			m = "No trace information available";

		}
		try {
			setBase(ee);

			// if (ee.getMessage().length() > 0) {

			setSeverity(Level.WARNING);
			setEventType(LogType.TYPE_ERROR);
			setLogSuccess(writeToLog());
			writeToFacesMessage(m);
			return getMessage();

		} catch (Exception e) {
			DominoUtils.handleException(e);
			setLogSuccess(false);
			return ""; //$NON-NLS-1$
		}
	}

	private void writeToFacesMessage(final String message) {
		try {
			if (getDisplayError()) {
				addFacesMessage("", message);
			}
		} catch (Exception e) {
			System.out.println("ODA: User error logged to OpenLog but could not write error to XPages browser. Check OpenLog.");
		}
	}

	/*
	 * This is the method that does the actual logging to the OpenLog database. You'll notice that I actually have a Database parameter,
	 * which is normally a reference to the OpenLog database that you're using. However, it occurred to me that you might want to use this
	 * class to write to an alternate log database at times, so I left you that option (although you're stuck with the field names used by
	 * the OpenLog database in that case).
	 * 
	 * This method creates a document in the log database, populates the fields of that document with the values in our global variables,
	 * and adds some associated information about any Document that needs to be referenced. If you do decide to send log information to an
	 * alternate database, you can just call this method manually after you've called logError or logEvent, and it will write everything to
	 * the database of your choice.
	 */
	@Override
	public boolean writeToLog() {
		boolean retval = false;
		try {
			Database db;
			Document logDoc;
			RichTextItem rtitem;
			Database docDb;

			if (null == Factory.getSession(SessionType.CURRENT).getCurrentDatabase()) {
				if (!StringUtil.equals(super.getCurrentDatabasePath(), "")) { //$NON-NLS-1$
					reinitialiseSettings();
				}
			} else {
				if (!StringUtil.equals(super.getCurrentDatabasePath(),
						Factory.getSession(SessionType.CURRENT).getCurrentDatabase().getFilePath())) {
					reinitialiseSettings();
				}
			}

			if (StringUtil.isEmpty(getLogEmail())) {
				db = getLogDb();
			} else {
				db = Factory.getSession(SessionType.NATIVE).getDatabase(getThisServer(), "mail.box", false); //$NON-NLS-1$
			}
			if (db == null) {
				System.out.println("Could not retrieve database at path " + getLogDbName());
				return false;
			}

			logDoc = db.createDocument();
			rtitem = logDoc.createRichTextItem("LogDocInfo"); //$NON-NLS-1$

			logDoc.appendItemValue("Form", super.getLogFormName()); //$NON-NLS-1$

			Throwable ee = getBase();
			String errMsg = ""; //$NON-NLS-1$
			if (null != ee) {
				StackTraceElement ste = ee.getStackTrace()[0];
				if (ee instanceof NotesException) {
					logDoc.replaceItemValue("LogErrorNumber", ((NotesException) ee).id); //$NON-NLS-1$
					errMsg = ((NotesException) ee).text;
				} else if ("Interpret exception".equals(ee.getMessage()) && ee instanceof com.ibm.jscript.JavaScriptException) { //$NON-NLS-1$
					com.ibm.jscript.InterpretException ie = (com.ibm.jscript.InterpretException) ee;
					errMsg = "Expression Language Interpret Exception " + ie.getExpressionText();
				} else {
					errMsg = ee.getMessage();
				}

				if (LogType.TYPE_EVENT.getValue().equals(getEventType())) {
					if (!getSuppressEventStack()) {
						logDoc.replaceItemValue("LogStackTrace", getStackTrace(ee)); //$NON-NLS-1$
					}
				} else {
					logDoc.replaceItemValue("LogStackTrace", getStackTrace(ee)); //$NON-NLS-1$
				}
				logDoc.replaceItemValue("LogErrorLine", ste.getLineNumber()); //$NON-NLS-1$
				logDoc.replaceItemValue("LogFromMethod", ste.getClass() + "." + ste.getMethodName()); //$NON-NLS-1$ //$NON-NLS-2$
			}

			if ("".equals(errMsg)) {
				errMsg = getMessage();
			} else {
				errMsg += " - " + getMessage();
			}

			logDoc.replaceItemValue("LogErrorMessage", errMsg); //$NON-NLS-1$
			logDoc.replaceItemValue("LogEventTime", getEventTime()); //$NON-NLS-1$
			logDoc.replaceItemValue("LogEventType", getEventType()); //$NON-NLS-1$
			// If greater than 32k, put in logDocInfo
			if (getMessage().length() > 32000) {
				rtitem.appendText(getMessage());
				rtitem.addNewLine();
			} else {
				logDoc.replaceItemValue("LogMessage", getMessage()); //$NON-NLS-1$
			}
			logDoc.replaceItemValue("LogSeverity", getSeverity().getName()); //$NON-NLS-1$
			logDoc.replaceItemValue("LogFromDatabase", getCurrentDatabase().getFilePath()); //$NON-NLS-1$
			logDoc.replaceItemValue("LogFromServer", getThisServer()); //$NON-NLS-1$
			logDoc.replaceItemValue("LogFromAgent", getThisAgent()); //$NON-NLS-1$
			logDoc.replaceItemValue("LogAgentLanguage", "Java"); //$NON-NLS-1$ //$NON-NLS-2$
			logDoc.replaceItemValue("LogUserName", Factory.getSession(SessionType.CURRENT).getUserName()); //$NON-NLS-1$
			logDoc.replaceItemValue("LogEffectiveName", Factory.getSession(SessionType.CURRENT).getEffectiveUserName()); //$NON-NLS-1$
			logDoc.replaceItemValue("LogAccessLevel", getAccessLevel()); //$NON-NLS-1$
			logDoc.replaceItemValue("LogUserRoles", getUserRoles()); //$NON-NLS-1$
			logDoc.replaceItemValue("LogClientVersion", getClientVersion()); //$NON-NLS-1$
			logDoc.replaceItemValue("LogAgentStartTime", getStartTime()); //$NON-NLS-1$

			if (getErrDoc() != null) {
				docDb = getErrDoc().getParentDatabase();
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
				rtitem.appendDocLink(super.getErrDoc(), getErrDoc().getUniversalID());
			}

			// make sure Depositor-level users can add documents too
			logDoc.appendItemValue("$PublicAccess", "1"); //$NON-NLS-1$ //$NON-NLS-2$

			if (StringUtil.isNotEmpty(getLogEmail())) {
				logDoc.replaceItemValue("Recipients", getLogEmail()); //$NON-NLS-1$
			}

			// Set expiry date, if defined
			if (!StringUtil.isEmpty(getLogExpireDate())) {
				try {
					Integer expiryPeriod = new Integer(getLogExpireDate());
					Calendar expTime = Calendar.getInstance();
					expTime.setTime(getStartTime());
					expTime.add(Calendar.DAY_OF_YEAR, expiryPeriod);
					logDoc.replaceItemValue("ExpireDate", expTime.getTime()); //$NON-NLS-1$
				} catch (Throwable t) {
					logDoc.replaceItemValue("ArchiveFlag", //$NON-NLS-1$
							"WARNING: Xsp Properties in the application has a non-numeric value for xsp.openlog.expireDate, so cannot be set to auto-expire");
				}
			}
			logDoc.save(true);
			retval = true;
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		} finally {
			_errDoc = null;
			_errDocUnid = null;
		}

		return retval;
	}

	/**
	 * @param component
	 *            String component ID
	 * @param msg
	 *            String message to be passed back to the browser
	 */
	public void addFacesMessage(final String component, String msg) {
		try {
			if (StringUtil.isNotEmpty(getDisplayErrorGeneric())) {
				if (null == ExtLibUtil.getRequestScope().get("genericOpenLogMessage")) { //$NON-NLS-1$
					ExtLibUtil.getRequestScope().put("genericOpenLogMessage", "Added"); //$NON-NLS-1$
				} else {
					return;
				}
				msg = displayErrorGeneric_;
			}
			FacesContext.getCurrentInstance().addMessage(component, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}
}

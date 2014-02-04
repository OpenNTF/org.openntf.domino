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

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.extlib.util.ExtLibUtil;

public class XspOpenLogItem extends BaseOpenLogItem {

	private transient String logEmail_;
	private transient String currXPage_;
	private transient String logDbName_;
	private transient Boolean displayError_;
	private transient String displayErrorGeneric_;
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
		return super.getThisAgent();
	}

	public void setThisAgent(final boolean currPage) {
		try {
			String fromPage = "";
			String[] historyUrls = ExtLibUtil.getXspContext().getHistoryUrls();
			if (currPage) {
				fromPage = historyUrls[0];
			} else {
				if (historyUrls.length > 1) {
					fromPage = historyUrls[1];
				} else {
					fromPage = historyUrls[0];
				}
			}
			if (fromPage.indexOf("?") > -1) {
				super.setThisAgent(fromPage.substring(1, fromPage.indexOf("?")));
			} else {
				super.setThisAgent(fromPage);
			}
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	public void reinitialiseSettings() {
		logEmail_ = null;
		logDbName_ = null;
		displayError_ = null;
		displayErrorGeneric_ = null;
		olDebugLevel = getDefaultDebugLevel();
	}

	/**
	 * @return the logEmail
	 */
	public String getLogEmail() {
		try {
			if (StringUtil.isEmpty(logEmail_)) {
				logEmail_ = Activator.getXspPropertyAsString("xsp.openlog.email");
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
				String logDbName_ = Activator.getXspPropertyAsString("xsp.openlog.filepath");
				if (StringUtil.isEmpty(logDbName_)) {
					super.setLogDbName("OpenLog.nsf");
				} else if ("[CURRENT]".equals(logDbName_.toUpperCase())) {
					super.setLogDbName(getCurrentDatabase().getFilePath());
				} else {
					super.setLogDbName(logDbName_);
				}
			}
			return super.getLogDbName();
		} catch (Throwable t) {
			DominoUtils.handleException(t);
			return "OpenLog.nsf";
		}
	}

	public Boolean getSuppressEventStack() {
		try {
			String dummyVar = Activator.getXspPropertyAsString("xsp.openlog.suppressEventStack");
			if (StringUtil.isEmpty(dummyVar)) {
				// setSuppressEventStack(true);
			} else if ("FALSE".equals(dummyVar.toUpperCase())) {
				// setSuppressEventStack(false);
			} else {
				// setSuppressEventStack(true);
			}
			return suppressEventStack_;
		} catch (Throwable t) {
			DominoUtils.handleException(t);
			return false;
		}
	}

	/**
	 * @return whether errors should be displayed or not
	 */
	public Boolean getDisplayError() {
		try {
			String dummyVar = Activator.getXspPropertyAsString("xsp.openlog.displayError");
			if (StringUtil.isEmpty(dummyVar)) {
				setDisplayError(true);
			} else if ("FALSE".equals(dummyVar.toUpperCase())) {
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
				displayErrorGeneric_ = Activator.getXspPropertyAsString("xsp.openlog.genericErrorMessage");
			}
			return displayErrorGeneric_;
		} catch (Throwable t) {
			DominoUtils.handleException(t);
			return "";
		}
	}

	private String getDefaultDebugLevel() {
		try {
			String defaultLevel_ = Activator.getXspPropertyAsString("xsp.openlog.debugLevel");
			if (StringUtil.isEmpty(defaultLevel_)) {
				defaultLevel_ = "2";
			}
			return defaultLevel_;
		} catch (Throwable t) {
			DominoUtils.handleException(t);
			return "2";
		}
	}

	@Override
	public String logError(final Throwable ee) {
		FacesMessage m = null;
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
			m = new FacesMessage("Error in " + s[0].getClassName() + ", line " + s[0].getLineNumber() + ": " + ee.toString());
			if (ee.getMessage() != null) {
				setMessage(ee.getMessage());
			} else {
				setMessage(ee.getClass().getCanonicalName());
			}
		} else {
			m = new FacesMessage("No trace information available");

		}
		try {
			ExtLibUtil.getXspContext().getFacesContext().addMessage(null, m);
			setBase(ee);

			// if (ee.getMessage().length() > 0) {

			setSeverity(Level.WARNING);
			setEventType(LogType.TYPE_ERROR);
			setLogSuccess(writeToLog());
			return getMessage();

		} catch (Exception e) {
			DominoUtils.handleException(e);
			setLogSuccess(false);
			return "";
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

			if (!StringUtil.equals(super.getCurrentDatabasePath(), Factory.getSession().getCurrentDatabase().getFilePath())) {
				reinitialiseSettings();
			}

			if (StringUtil.isEmpty(getLogEmail())) {
				db = getLogDb();
			} else {
				db = Factory.getSession().getDatabase(getThisServer(), "mail.box", false);
			}
			if (db == null) {
				System.out.println("Could not retrieve database at path " + getLogDbName());
				return false;
			}

			logDoc = db.createDocument();
			rtitem = logDoc.createRichTextItem("LogDocInfo");

			logDoc.appendItemValue("Form", super.getLogFormName());

			Throwable ee = getBase();
			StackTraceElement ste = ee.getStackTrace()[0];
			String errMsg = "";
			if (ee instanceof NotesException) {
				logDoc.replaceItemValue("LogErrorNumber", ((NotesException) ee).id);
				errMsg = ((NotesException) ee).text;
			} else if ("Interpret exception".equals(ee.getMessage()) && ee instanceof com.ibm.jscript.JavaScriptException) {
				com.ibm.jscript.InterpretException ie = (com.ibm.jscript.InterpretException) ee;
				errMsg = "Expression Language Interpret Exception " + ie.getExpressionText();
			} else {
				errMsg = ee.getMessage();
			}

			if (null == errMsg) {
				errMsg = getMessage();
			}

			logDoc.replaceItemValue("LogErrorMessage", errMsg);
			if (LogType.TYPE_EVENT.getValue().equals(getEventType())) {
				if (!getSuppressEventStack()) {
					logDoc.replaceItemValue("LogStackTrace", getStackTrace(ee));
				}
			}
			logDoc.replaceItemValue("LogErrorLine", ste.getLineNumber());
			logDoc.replaceItemValue("LogSeverity", getSeverity().getName());
			logDoc.replaceItemValue("LogEventTime", getEventTime());
			logDoc.replaceItemValue("LogEventType", getEventType());
			// If greater than 32k, put in logDocInfo
			if (getMessage().length() > 32000) {
				rtitem.appendText(getMessage());
				rtitem.addNewLine();
			} else {
				logDoc.replaceItemValue("LogMessage", getMessage());
			}
			logDoc.replaceItemValue("LogFromDatabase", getCurrentDatabase().getFilePath());
			logDoc.replaceItemValue("LogFromServer", getThisServer());
			logDoc.replaceItemValue("LogFromAgent", getThisAgent());
			logDoc.replaceItemValue("LogFromMethod", ste.getClass() + "." + ste.getMethodName());
			logDoc.replaceItemValue("LogAgentLanguage", "Java");
			logDoc.replaceItemValue("LogUserName", Factory.getSession().getUserName());
			logDoc.replaceItemValue("LogEffectiveName", Factory.getSession().getEffectiveUserName());
			logDoc.replaceItemValue("LogAccessLevel", getAccessLevel());
			logDoc.replaceItemValue("LogUserRoles", getUserRoles());
			logDoc.replaceItemValue("LogClientVersion", getClientVersion());
			logDoc.replaceItemValue("LogAgentStartTime", getStartTime());

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
			logDoc.appendItemValue("$PublicAccess", "1");

			if (StringUtil.isNotEmpty(getLogEmail())) {
				logDoc.replaceItemValue("Recipients", getLogEmail());
			}
			logDoc.save(true);
			retval = true;
		} catch (Throwable t) {
			DominoUtils.handleException(t);
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
				if (null == ExtLibUtil.getRequestScope().get("genericOpenLogMessage")) {
					ExtLibUtil.getRequestScope().put("genericOpenLogMessage", "Added");
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

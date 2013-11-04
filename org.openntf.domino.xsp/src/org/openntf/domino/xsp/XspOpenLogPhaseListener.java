package org.openntf.domino.xsp;

/*

 <!--
 Copyright 2013 Paul Withers
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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.logging.Level;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.ServletException;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.xsp.XspOpenLogErrorHolder.EventError;

import com.ibm.commons.util.StringUtil;
import com.ibm.jscript.InterpretException;
import com.ibm.xsp.exception.EvaluationExceptionEx;

/**
 * @author withersp
 * @since 1.0.0
 * 
 */
public class XspOpenLogPhaseListener implements PhaseListener {
	private static final long serialVersionUID = 1L;
	private Boolean logUncaughtError_;

	/**
	 * Note this only works if you have specified an error-page.xsp.
	 * 
	 * @return whether uncaught errors should be displayed or not (default = false)
	 * 
	 */
	public Boolean getLogUncaughtError() {
		try {
			if (null == logUncaughtError_) {
				String dummyVar = Activator.getXspPropertyAsString("xsp.openlog.logUncaught");
				if (StringUtil.isEmpty(dummyVar)) {
					setLogUncaughtError(false);
				} else if ("FALSE".equals(dummyVar.toUpperCase())) {
					setLogUncaughtError(false);
				} else {
					setLogUncaughtError(true);
				}
			}
			return logUncaughtError_;
		} catch (Throwable t) {
			t.printStackTrace();
			return false;
		}
	}

	/**
	 * @param error
	 *            whether or not to display the errors
	 */
	public void setLogUncaughtError(final Boolean error) {
		logUncaughtError_ = error;
	}

	@SuppressWarnings("unchecked")
	public void beforePhase(final PhaseEvent event) {
		try {
			// Add FacesContext messages for anything captured so far
			Map<String, Object> r = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
			if (null == r.get("error") && getLogUncaughtError()) {
				XspOpenLogUtil.getXspOpenLogItem().setThisAgent(true);
			}
			if (null != r.get("openLogBean")) {
				// requestScope.openLogBean is not null, the developer has called openLogBean.addError(e,this)
				XspOpenLogErrorHolder errList = (XspOpenLogErrorHolder) r.get("openLogBean");
				errList.setLoggedErrors(new LinkedHashSet<EventError>());
				// loop through the ArrayList of EventError objects and add any errors already captured as a facesMessage
				if (null != errList.getErrors()) {
					for (EventError error : errList.getErrors()) {
						errList.addFacesMessageForError(error);
					}
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.event.PhaseListener#afterPhase(javax.faces.event.PhaseEvent)
	 */
	@SuppressWarnings("unchecked")
	public void afterPhase(final PhaseEvent event) {
		try {
			Map<String, Object> r = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
			if (null != r.get("error")) {
				processUncaughtException(r);

			} else if (null != r.get("openLogBean")) {
				// requestScope.openLogBean is not null, the developer has called openLogBean.addError(e,this)
				XspOpenLogErrorHolder errList = (XspOpenLogErrorHolder) r.get("openLogBean");
				// loop through the ArrayList of EventError objects
				if (null != errList.getErrors()) {
					for (EventError error : errList.getErrors()) {
						String msg = "";
						if (!"".equals(error.getMsg()))
							msg = msg + error.getMsg();
						msg = msg + "Error on ";
						if (null != error.getControl()) {
							msg = msg + error.getControl().getId();
						}
						if (null != error.getError()) {
							msg = msg + ":\n\n" + error.getError().getLocalizedMessage() + "\n\n" + error.getError().getExpressionText();
						}
						Level severity = convertSeverity(error.getSeverity());
						Document passedDoc = null;
						if (!"".equals(error.getUnid())) {
							try {
								Database currDb = Factory.getSession().getCurrentDatabase();
								passedDoc = currDb.getDocumentByUNID(error.getUnid());
							} catch (Exception e) {
								msg = msg + "\n\nCould not retrieve document but UNID was passed: " + error.getUnid();
							}
						}
						XspOpenLogUtil.getXspOpenLogItem().logErrorEx(error.getError(), msg, severity, passedDoc);
					}
				}
				// loop through the ArrayList of EventError objects
				if (null != errList.getEvents()) {
					for (EventError eventObj : errList.getEvents()) {
						String msg = "Event logged for ";
						if (null != eventObj.getControl()) {
							msg = msg + eventObj.getControl().getId();
						}
						msg = msg + " " + eventObj.getMsg();
						Level severity = convertSeverity(eventObj.getSeverity());
						Document passedDoc = null;
						if (!"".equals(eventObj.getUnid())) {
							try {
								Database currDb = Factory.getSession().getCurrentDatabase();
								passedDoc = currDb.getDocumentByUNID(eventObj.getUnid());
							} catch (Exception e) {
								msg = msg + "\n\nCould not retrieve document but UNID was passed: " + eventObj.getUnid();
							}
						}
						XspOpenLogUtil.getXspOpenLogItem().logEvent(null, msg, severity, passedDoc);
					}
				}
			}
		} catch (Throwable e) {
			try {
				// We've hit an error in our code here, log the error
				XspOpenLogUtil.getXspOpenLogItem().logError(e);
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

	private static Throwable getCause(final Throwable t) {
		Throwable res;
		if ((t instanceof ServletException)) {
			res = ((ServletException) t).getRootCause();
			if (res != null) {
				return res;
			}
		}
		if ((t instanceof SQLException)) {
			res = ((SQLException) t).getNextException();
			if (res != null) {
				return res;
			}
		}

		return t.getCause();
	}

	/**
	 * Returns the error in readable form
	 * 
	 * @param error
	 * @return
	 */
	public static String getErrorText(final Object error, final boolean addStackTrace) {
		if (error instanceof Throwable) {
			Throwable t = (Throwable) error;
			StringBuilder msg = new StringBuilder();

			do {
				// Write classname and message in one line
				msg.append(t.getClass().getName());
				msg.append(": ");
				msg.append(t.getLocalizedMessage());

				// write details for EvaluationExceptionEx and InterpretException
				if (t instanceof EvaluationExceptionEx) {
					EvaluationExceptionEx ee = (EvaluationExceptionEx) t;
					// Details for component
					msg.append(", Component: ");
					msg.append(ee.getErrorComponentId());
					msg.append(", property: ");
					msg.append(ee.getErrorPropertyId());
					msg.append("\n");
				} else if (t instanceof InterpretException) {
					InterpretException ie = (InterpretException) t;
					msg.append("\n---------------\n");
					msg.append(ie.getExpressionText());
					msg.append("\n---------------\n");
				} else if (t instanceof SQLException) {
					SQLException se = (SQLException) t;
					msg.append(", SQLState: ");
					msg.append(se.getSQLState());
					msg.append("\n");
				} else {
					msg.append("\n");
				}
				t = getCause(t);
				// if (t != null)
				// msg.append("\n");
			} while (t != null);

			if (addStackTrace) {
				msg.append("\nStacktrace:\n");
				StringWriter sw = new StringWriter();
				((Throwable) error).printStackTrace(new PrintWriter(sw));
				msg.append(sw.toString());
			}

			return msg.toString();

		} else {
			System.out.println("Error type not found:" + error.getClass().getName());
			return error.toString();
		}
	}

	/**
	 * This is getting VERY complex because of the variety of exceptions and tracking up the stack trace to find the right class to get as
	 * much info as possible. Extracted into a separate method to make it more readable.
	 * 
	 * @param r
	 *            requestScope map
	 */
	private void processUncaughtException(final Map<String, Object> r) {
		// requestScope.error is not null, we're on the custom error page.
		Object error = r.get("error");

		// Set the agent (page we're on) to the *previous* page
		XspOpenLogUtil.getXspOpenLogItem().setThisAgent(false);

		if (error != null) {
			XspOpenLogUtil.getXspOpenLogItem().logErrorEx((Throwable) error, getErrorText(error, false), null, null);
		} else {
			System.out.println("No error set");
		}
	}

	private Level convertSeverity(final int severity) {
		Level internalLevel = null;
		switch (severity) {
		case 1:
			internalLevel = Level.SEVERE;
			break;
		case 2:
			internalLevel = Level.WARNING;
			break;
		case 3:
			internalLevel = Level.INFO;
			break;
		case 5:
			internalLevel = Level.FINE;
			break;
		case 6:
			internalLevel = Level.FINER;
			break;
		case 7:
			internalLevel = Level.FINEST;
			break;
		default:
			internalLevel = Level.CONFIG;
		}
		return internalLevel;
	}

	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE; // PhaseId.ANY_PHASE;
	}
}

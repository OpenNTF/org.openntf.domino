/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
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

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.logging.Level;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import lotus.domino.NotesException;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xsp.XspOpenLogErrorHolder.EventError;

import com.ibm.jscript.InterpretException;
import com.ibm.jscript.parser.ParseException;
import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.exception.EvaluationExceptionEx;

/**
 * @author withersp
 * @since 1.0.0
 * 
 */
@SuppressWarnings("nls")
public class XspOpenLogPhaseListener implements PhaseListener {
	private static final long serialVersionUID = 1L;
	private static final int RENDER_RESPONSE = 6;

	public XspOpenLogPhaseListener() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.event.PhaseListener#beforePhase(javax.faces.event.PhaseEvent)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void beforePhase(final PhaseEvent event) {
		try {
			// Add FacesContext messages for anything captured so far
			if (RENDER_RESPONSE == event.getPhaseId().getOrdinal()) {
				Map<String, Object> r = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
				Map<String, Object> sessScope = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
				if (null == r.get("error")) { //$NON-NLS-1$
					XspOpenLogUtil.getXspOpenLogItem().setThisAgent(true);
				}
				if (null != sessScope.get("openLogBean")) { //$NON-NLS-1$
					if (!ODAPlatform.isAPIEnabled()) {
						return;
					}
					// sessionScope.openLogBean is not null, the developer has called openLogBean.addError(e,this)
					XspOpenLogErrorHolder errList = (XspOpenLogErrorHolder) sessScope.get("openLogBean"); //$NON-NLS-1$
					errList.setLoggedErrors(new LinkedHashSet<EventError>());
					// loop through the ArrayList of EventError objects and add any errors already captured as a facesMessage
					if (null != errList.getErrors()) {
						for (EventError error : errList.getErrors()) {
							errList.addFacesMessageForError(error);
						}
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
	@Override
	@SuppressWarnings("unchecked")
	public void afterPhase(final PhaseEvent event) {
		try {
			if (RENDER_RESPONSE == event.getPhaseId().getOrdinal()) {
				Map<String, Object> r = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
				Map<String, Object> sessScope = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
				if (null != r.get("error")) { //$NON-NLS-1$
					processUncaughtException(r);

				} else if (null != sessScope.get("openLogBean")) { //$NON-NLS-1$
					if (!ODAPlatform.isAPIEnabled()) {
						return;
					}
					// sessionScope.openLogBean is not null, the developer has called openLogBean.addError(e,this)
					XspOpenLogErrorHolder errList = (XspOpenLogErrorHolder) sessScope.get("openLogBean"); //$NON-NLS-1$
					// loop through the ArrayList of EventError objects
					if (null != errList.getErrors()) {
						for (EventError error : errList.getErrors()) {
							String msg = ""; //$NON-NLS-1$
							if (!"".equals(error.getMsg())) { //$NON-NLS-1$
								msg = msg + error.getMsg();
							}
							msg = msg + "Error on ";
							if (null != error.getControl()) {
								msg = msg + error.getControl().getId();
							}
							if (null != error.getError()) {
								msg = msg + ":\n\n" + error.getError().getLocalizedMessage() + "\n\n" //$NON-NLS-1$ //$NON-NLS-2$
										+ error.getError().getExpressionText();
							}
							Level severity = convertSeverity(error.getSeverity());
							Document passedDoc = null;
							if (!"".equals(error.getUnid())) {
								try {
									Database currDb = Factory.getSession(SessionType.CURRENT).getCurrentDatabase();
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
							msg = msg + " " + eventObj.getMsg(); //$NON-NLS-1$
							Level severity = convertSeverity(eventObj.getSeverity());
							Document passedDoc = null;
							if (!"".equals(eventObj.getUnid())) { //$NON-NLS-1$
								try {
									Database currDb = Factory.getSession(SessionType.CURRENT).getCurrentDatabase();
									passedDoc = currDb.getDocumentByUNID(eventObj.getUnid());
								} catch (Exception e) {
									msg = msg + "\n\nCould not retrieve document but UNID was passed: " + eventObj.getUnid();
								}
							}
							XspOpenLogUtil.getXspOpenLogItem().logEvent(null, msg, severity, passedDoc);
						}
					}
					sessScope.put("openLogBean", null); //$NON-NLS-1$
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

	/**
	 * This is getting VERY complex because of the variety of exceptions and tracking up the stack trace to find the right class to get as
	 * much info as possible. Extracted into a separate method to make it more readable.
	 * 
	 * @param r
	 *            requestScope map
	 */
	private void processUncaughtException(final Map<String, Object> r) {
		// requestScope.error is not null, we're on the custom error page.
		Object error = r.get("error"); //$NON-NLS-1$

		// Set the agent (page we're on) to the *previous* page
		XspOpenLogUtil.getXspOpenLogItem().setThisAgent(false);

		String msg = ""; //$NON-NLS-1$
		if (error instanceof EvaluationExceptionEx) {
			// EvaluationExceptionEx, so SSJS error is on a component property.
			// Hit by ErrorOnLoad.xsp
			EvaluationExceptionEx ee = (EvaluationExceptionEx) error;
			if ("com.ibm.jscript.InterpretException".equals(ee.getCause().getClass().getName())) { //$NON-NLS-1$
				InterpretException ie = (InterpretException) ee.getCause();
				msg = "Error on " + ee.getErrorComponentId() + " " + ee.getErrorPropertyId() + " property/event, line "
						+ Integer.toString(ie.getErrorLine()) + ":\n\n" + ie.getLocalizedMessage() + "\n\n" + ie.getExpressionText();
			} else if ("com.ibm.jscript.parser.ParseException".equals(ee.getCause().getClass().getName())) { //$NON-NLS-1$
				ParseException ie = (ParseException) ee.getCause();
				msg = "Error on " + ee.getErrorComponentId() + " " + ee.getErrorPropertyId() + " property/event " + ":\n\n"
						+ ie.getLocalizedMessage();

			}
			XspOpenLogUtil.getXspOpenLogItem().logErrorEx(ee, msg, null, null);
		} else if(error instanceof PropertyNotFoundException) {
			// Hit by ErrorOnProperty.xsp
			// Property not found exception, so error is on a component property
			PropertyNotFoundException pe = (PropertyNotFoundException) error;
			msg = "PropertyNotFoundException Error, cannot locate component:\n\n" + pe.getLocalizedMessage();
			XspOpenLogUtil.getXspOpenLogItem().logErrorEx(pe, msg, null, null);
		} else if(error instanceof FacesExceptionEx) {
			// FacesException, so error is on event - doesn't get hit in examples. Can this still get hit??
			FacesExceptionEx fe = (FacesExceptionEx) error;
			try {
				if ("lotus.domino.NotesException".equals(fe.getCause().getClass().getName())) { //$NON-NLS-1$
					// sometimes the cause is a NotesException
					NotesException ne = (NotesException) fe.getCause();

					msg = msg + "NotesException - " + Integer.toString(ne.id) + " " + ne.text; //$NON-NLS-1$ //$NON-NLS-2$
				} else if ("java.io.IOException".equals(error.getClass().getName())) { //$NON-NLS-1$
					IOException e = (IOException) error;

					msg = "Java IO:" + error.toString(); //$NON-NLS-1$
					XspOpenLogUtil.getXspOpenLogItem().logErrorEx(e.getCause(), msg, null, null);
				} else {
					EvaluationExceptionEx ee = (EvaluationExceptionEx) fe.getCause();
					InterpretException ie = (InterpretException) ee.getCause();

					msg = "Error on " + ee.getErrorComponentId() + " " + ee.getErrorPropertyId() + " property/event:\n\n"
							+ Integer.toString(ie.getErrorLine()) + ":\n\n" + ie.getLocalizedMessage() + "\n\n" + ie.getExpressionText();
				}
			} catch (Throwable t) {
				try {
					msg = "Unexpected error class: " + fe.getCause().getClass().getName() + "\n Message recorded is: "
							+ fe.getCause().getLocalizedMessage();
				} catch (Throwable ee) {
					msg = fe.getLocalizedMessage();
				}
			}
			XspOpenLogUtil.getXspOpenLogItem().logErrorEx(fe.getCause(), msg, null, null);
		} else if (error instanceof FacesException) {
			// FacesException, so error is on event or method in EL
			FacesException fe = (FacesException) error;
			InterpretException ie = null;
			EvaluationExceptionEx ee = null;
			msg = "Error on ";
			try {
				// javax.faces.el.MethodNotFoundException hit by ErrorOnMethod.xsp
				if (!"javax.faces.el.MethodNotFoundException".equals(fe.getCause().getClass().getName())) { //$NON-NLS-1$
					if ("com.ibm.xsp.exception.EvaluationExceptionEx".equals(fe.getCause().getClass().getName())) { //$NON-NLS-1$
						// Hit by ErrorOnClick.xsp
						ee = (EvaluationExceptionEx) fe.getCause();
					} else if ("javax.faces.el.PropertyNotFoundException".equals(fe.getCause().getClass().getName())) { //$NON-NLS-1$
						// Property not found exception, so error is on a component property
						msg = "PropertyNotFoundException Error, cannot locate component:\n\n";
					} else if ("com.ibm.xsp.exception.EvaluationExceptionEx".equals(fe.getCause().getCause().getClass().getName())) { //$NON-NLS-1$
						// Hit by using e.g. currentDocument.isNewDoc()
						// i.e. using a Variable that relates to a valid Java object but a method that doesn't exist
						ee = (EvaluationExceptionEx) fe.getCause().getCause();
					}
					if (null != ee) {
						msg = msg + ee.getErrorComponentId() + " " + ee.getErrorPropertyId() + " property/event:\n\n";
						if ("com.ibm.jscript.InterpretException".equals(ee.getCause().getClass().getName())) { //$NON-NLS-1$
							ie = (InterpretException) ee.getCause();
						}
					}
				}
			} catch (Throwable t) {
				msg = "Unexpected error class: " + fe.getCause().getClass().getName() + "\n Message recorded is: ";
			}
			if (null != ie) {
				msg = msg + Integer.toString(ie.getErrorLine()) + ":\n\n" + ie.getLocalizedMessage() + "\n\n" + ie.getExpressionText(); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				msg = msg + fe.getCause().getLocalizedMessage();
			}
			XspOpenLogUtil.getXspOpenLogItem().logErrorEx(fe.getCause(), msg, null, null);
		} else {
			try {
				msg = error.toString();
				XspOpenLogUtil.getXspOpenLogItem().logErrorEx((Throwable) error, msg, null, null);
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

	/**
	 * Converts an integer into a Java logging {@link Level}
	 * 
	 * @param severity
	 *            int severity, 1 to 7
	 * @return Level corresponding to the relevant integer, defaulting to Level.CONFIG (4)
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.event.PhaseListener#getPhaseId()
	 */
	@Override
	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}
}

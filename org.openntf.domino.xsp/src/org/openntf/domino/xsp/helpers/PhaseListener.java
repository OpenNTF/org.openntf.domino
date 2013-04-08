/*
 * © Copyright GBS Inc 2011
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
package org.openntf.domino.xsp.helpers;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;

import org.openntf.domino.xsp.Activator;

public class PhaseListener extends AbstractListener implements javax.faces.event.PhaseListener {
	public static final long serialVersionUID = -6528380677556637393L;
	private final static boolean _debug = Activator.isDebug();
	static {
		if (_debug)
			System.out.println(PhaseListener.class.getName() + " loaded");
	}

	public PhaseListener() {
		_debugOut("created");
	}

	private void doBeforeEveryPhase(PhaseEvent arg0) {
		// Insert your code here
	}

	private void doAfterEveryPhase(PhaseEvent arg0) {
		// Insert your code here
	}

	private void doBeforeRestoreView(PhaseEvent arg0) {
		// Insert your code here
	}

	private void doAfterRestoreView(PhaseEvent arg0) {
		// Insert your code here
	}

	private void doBeforeApplyRequest(PhaseEvent arg0) {
		// Insert your code here
	}

	private void doAfterApplyRequest(PhaseEvent arg0) {
		// Insert your code here
	}

	private void doBeforeProcessValidations(PhaseEvent arg0) {
		// Insert your code here
	}

	private void doAfterProcessValidations(PhaseEvent arg0) {
		// Insert your code here
	}

	private void doBeforeUpdateModel(PhaseEvent arg0) {
		// Insert your code here
	}

	private void doAfterUpdateModel(PhaseEvent arg0) {
		// Insert your code here
	}

	private void doBeforeInvokeApplication(PhaseEvent arg0) {
		// Insert your code here
	}

	private void doAfterInvokeApplication(PhaseEvent arg0) {
		// Insert your code here
	}

	private void doBeforeRenderResponse(PhaseEvent arg0) {
		// Insert your code here
	}

	private void doAfterRenderResponse(PhaseEvent arg0) {
		// Insert your code here
	}

	@Override
	public void afterPhase(PhaseEvent arg0) {
		PhaseId curId = arg0.getPhaseId();
		if (PhaseId.APPLY_REQUEST_VALUES.equals(curId)) {
			doAfterApplyRequest(arg0);
		} else if (PhaseId.INVOKE_APPLICATION.equals(curId)) {
			doAfterInvokeApplication(arg0);
		} else if (PhaseId.PROCESS_VALIDATIONS.equals(curId)) {
			doAfterProcessValidations(arg0);
		} else if (PhaseId.RENDER_RESPONSE.equals(curId)) {
			doAfterRenderResponse(arg0);
		} else if (PhaseId.RESTORE_VIEW.equals(curId)) {
			doAfterRestoreView(arg0);
		} else if (PhaseId.UPDATE_MODEL_VALUES.equals(curId)) {
			doAfterUpdateModel(arg0);
		}
		doAfterEveryPhase(arg0);
	}

	@Override
	public void beforePhase(PhaseEvent arg0) {
		PhaseId curId = arg0.getPhaseId();
		if (PhaseId.APPLY_REQUEST_VALUES.equals(curId)) {
			doBeforeApplyRequest(arg0);
		} else if (PhaseId.INVOKE_APPLICATION.equals(curId)) {
			doBeforeInvokeApplication(arg0);
		} else if (PhaseId.PROCESS_VALIDATIONS.equals(curId)) {
			doBeforeProcessValidations(arg0);
		} else if (PhaseId.RENDER_RESPONSE.equals(curId)) {
			doBeforeRenderResponse(arg0);
		} else if (PhaseId.RESTORE_VIEW.equals(curId)) {
			doBeforeRestoreView(arg0);
		} else if (PhaseId.UPDATE_MODEL_VALUES.equals(curId)) {
			doBeforeUpdateModel(arg0);
		}
		doBeforeEveryPhase(arg0);
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}

}

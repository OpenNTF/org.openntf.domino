package org.openntf.domino.xsp.helpers;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;

import org.openntf.domino.utils.Factory;

import com.ibm.xsp.context.FacesContextEx;

public class PhaseListener extends AbstractListener implements javax.faces.event.PhaseListener, com.ibm.xsp.event.FacesContextListener {
	public static final long serialVersionUID = -6528380677556637393L;
	private final static boolean _debug = true;
	static {
		if (_debug)
			System.out.println(PhaseListener.class.getName() + " loaded");
	}

	public PhaseListener() {
		if (_debug)
			System.out.println(PhaseListener.class.getName() + " created");
	}

	@Override
	public void beforeContextReleased(final FacesContext paramFacesContext) {
		Factory.terminate();
	}

	@Override
	public void beforeRenderingPhase(final FacesContext paramFacesContext) {
		// TODO NOOP

	}

	private void doBeforeEveryPhase(final PhaseEvent arg0) {
		FacesContextEx ctx = (FacesContextEx) arg0.getFacesContext();
		Factory.setClassLoader(ctx.getContextClassLoader());
		if (ctx instanceof com.ibm.xsp.context.FacesContextEx) {
			((com.ibm.xsp.context.FacesContextEx) ctx).addRequestListener(this);
		}
	}

	private void doAfterEveryPhase(final PhaseEvent arg0) {
		// Insert your code here
	}

	private void doBeforeRestoreView(final PhaseEvent arg0) {
		// Insert your code here
	}

	private void doAfterRestoreView(final PhaseEvent arg0) {
		// Insert your code here
	}

	private void doBeforeApplyRequest(final PhaseEvent arg0) {
		// Insert your code here
	}

	private void doAfterApplyRequest(final PhaseEvent arg0) {
		// Insert your code here
	}

	private void doBeforeProcessValidations(final PhaseEvent arg0) {
		// Insert your code here
	}

	private void doAfterProcessValidations(final PhaseEvent arg0) {
		// Insert your code here
	}

	private void doBeforeUpdateModel(final PhaseEvent arg0) {
		// Insert your code here
	}

	private void doAfterUpdateModel(final PhaseEvent arg0) {
		// Insert your code here
	}

	private void doBeforeInvokeApplication(final PhaseEvent arg0) {
		// Insert your code here
	}

	private void doAfterInvokeApplication(final PhaseEvent arg0) {
		// Insert your code here
	}

	private void doBeforeRenderResponse(final PhaseEvent arg0) {
		// Insert your code here
	}

	private void doAfterRenderResponse(final PhaseEvent arg0) {
		// Insert your code here
	}

	@Override
	public void afterPhase(final PhaseEvent arg0) {
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
	public void beforePhase(final PhaseEvent arg0) {
		doBeforeEveryPhase(arg0);
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
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}

}

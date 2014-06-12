package org.openntf.domino.xsp.helpers;

import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.servlet.http.HttpServletResponse;

import org.openntf.domino.utils.Factory;

import com.ibm.xsp.application.ApplicationEx;
import com.ibm.xsp.context.FacesContextEx;

/**
 * PhaseListener for the library
 */
public class PhaseListener extends AbstractListener implements javax.faces.event.PhaseListener, com.ibm.xsp.event.FacesContextListener {
	public static final long serialVersionUID = -6528380677556637393L;
	private final static boolean _debug = false;
	static {
		if (_debug)
			System.out.println(PhaseListener.class.getName() + " loaded");
	}

	/**
	 * Constructor
	 */
	public PhaseListener() {
		if (_debug)
			System.out.println(PhaseListener.class.getName() + " created");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.event.FacesContextListener#beforeContextReleased(javax.faces.context.FacesContext)
	 */
	@Override
	public void beforeContextReleased(final FacesContext ctx) {
		if (_debug)
			System.out.println("Completed request " + String.valueOf(System.identityHashCode(ctx)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.event.FacesContextListener#beforeRenderingPhase(javax.faces.context.FacesContext)
	 */
	@Override
	public void beforeRenderingPhase(final FacesContext ctx) {
		Object o = ctx.getExternalContext().getResponse();
		if (o instanceof HttpServletResponse) {
			HttpServletResponse resp = (HttpServletResponse) o;
			resp.addHeader("RequestId", String.valueOf(System.identityHashCode(ctx)));
		} else if (o == null) {
			// System.out.println("Response object not yet available");
		} else {
			// System.out.println("Response object is a " + o.getClass().getName());
		}
	}

	/**
	 * Method to run before every phase is triggered
	 * 
	 * @param arg0
	 *            PhaseEvent currently being run
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	private void doBeforeEveryPhase(final PhaseEvent arg0) {
		FacesContext ctx = arg0.getFacesContext();
		Factory.setClassLoader(Thread.currentThread().getContextClassLoader());

		if (ctx instanceof FacesContextEx) {
			FacesContextEx ctxex = (FacesContextEx) ctx;
			ctxex.addRequestListener(this);
			final ApplicationEx app = ctxex.getApplicationEx();

			Factory.setServiceLocator(new Factory.AppServiceLocator() {
				public <T> List<T> findApplicationServices(final Class<T> serviceClazz) {
					return app.findServices(serviceClazz.getName());
				}
			});

		}
	}

	/**
	 * Method to run after every phase is triggered
	 * 
	 * @param arg0
	 *            PhaseEvent currently being run
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	private void doAfterEveryPhase(final PhaseEvent arg0) {
		// Insert your code here
	}

	/**
	 * Method to run before the RestoreView phase
	 * 
	 * @param arg0
	 *            PhaseEvent currently being run
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	private void doBeforeRestoreView(final PhaseEvent arg0) {
		// Insert your code here
	}

	/**
	 * Method to run after the RestoreView phase
	 * 
	 * @param arg0
	 *            PhaseEvent currently being run
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	private void doAfterRestoreView(final PhaseEvent arg0) {
		// Insert your code here
	}

	/**
	 * Method to run before the ApplyRequest phase
	 * 
	 * @param arg0
	 *            PhaseEvent currently being run
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	private void doBeforeApplyRequest(final PhaseEvent arg0) {
		// Insert your code here
	}

	/**
	 * Method to run after the ApplyRequest phase
	 * 
	 * @param arg0
	 *            PhaseEvent currently being run
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	private void doAfterApplyRequest(final PhaseEvent arg0) {
		// Insert your code here
	}

	/**
	 * Method to run before the ProcessValidation phase
	 * 
	 * @param arg0
	 *            PhaseEvent currently being run
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	private void doBeforeProcessValidations(final PhaseEvent arg0) {
		// Insert your code here
	}

	/**
	 * Method to run after the ProcessValidation phase
	 * 
	 * @param arg0
	 *            PhaseEvent currently being run
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	private void doAfterProcessValidations(final PhaseEvent arg0) {
		// Insert your code here
	}

	/**
	 * Method to run before the UpdateModelValues phase
	 * 
	 * @param arg0
	 *            PhaseEvent currently being run
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	private void doBeforeUpdateModel(final PhaseEvent arg0) {
		// Insert your code here
	}

	/**
	 * Method to run after the UpdateModelValues phase
	 * 
	 * @param arg0
	 *            PhaseEvent currently being run
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	private void doAfterUpdateModel(final PhaseEvent arg0) {
		// Insert your code here
	}

	/**
	 * Method to run before the InvokeApplication phase
	 * 
	 * @param arg0
	 *            PhaseEvent currently being run
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	private void doBeforeInvokeApplication(final PhaseEvent arg0) {
		// Insert your code here
	}

	/**
	 * Method to run after the InvokeApplication phase
	 * 
	 * @param arg0
	 *            PhaseEvent currently being run
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	private void doAfterInvokeApplication(final PhaseEvent arg0) {
		// Insert your code here
	}

	/**
	 * Method to run before the RenderResponse phase
	 * 
	 * @param arg0
	 *            PhaseEvent currently being run
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	private void doBeforeRenderResponse(final PhaseEvent arg0) {
		// Insert your code here
	}

	/**
	 * Method to run after the RenderResponse phase
	 * 
	 * @param arg0
	 *            PhaseEvent currently being run
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	private void doAfterRenderResponse(final PhaseEvent arg0) {
		// Insert your code here
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.event.PhaseListener#afterPhase(javax.faces.event.PhaseEvent)
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.event.PhaseListener#beforePhase(javax.faces.event.PhaseEvent)
	 */
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

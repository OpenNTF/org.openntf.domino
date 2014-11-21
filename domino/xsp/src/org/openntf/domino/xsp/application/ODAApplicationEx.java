package org.openntf.domino.xsp.application;

import javax.faces.application.Application;
import javax.faces.application.ViewHandler;

import com.ibm.xsp.application.ApplicationEx;
import com.ibm.xsp.application.DesignerApplicationEx;
import com.ibm.xsp.controller.FacesController;

public class ODAApplicationEx extends DesignerApplicationEx {
	private static final boolean _debug = true;
	static {
		if (_debug)
			System.out.println("DEBUG: " + ODAApplicationEx.class.getName() + " loaded");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.application.ApplicationExImpl#getController()
	 */
	@Override
	public FacesController getController() {
		return super.getController();
	}

	protected ODAApplicationEx(final Application paramApplication) {
		super(paramApplication);
		initListeners();
		if (_debug) {
			System.out.println("DEBUG: " + getClass().getName() + " created from delegate application "
					+ paramApplication.getClass().getName());
			// System.out.println("PropertyResolver: " + getPropertyResolver().getClass().getName());
			// System.out.println("VariableResolver: " + getVariableResolver().getClass().getName());
			// System.out.println("NavigationHandler: " + getNavigationHandler().getClass().getName());
			// System.out.println("StateManager: " + this.getStateManager().getClass().getName());
			// System.out.println("ViewHandler: " + this.getViewHandler().getClass().getName());
			// System.out.println("FacesController: " + (this.getController() == null ? "null" :
			// this.getController().getClass().getName()));

			// com.ibm.xsp.application.StateManagerImpl
			// com.ibm.xsp.application.ViewHandlerExImpl
			// com.ibm.xsp.el.PropertyResolverImpl
			// com.ibm.xsp.el.VariableResolverImpl
			// com.ibm.xsp.application.NavigationHandlerImpl
			// com.ibm.designer.runtime.domino.adapter.ComponentModule cm;
			// com.ibm.xsp.webapp.DesignerFacesServlet dfs;

		}
	}

	protected ODAApplicationEx(final ApplicationEx paramApplication) {
		super(paramApplication);
		initListeners();
		if (_debug) {
			System.out.println("DEBUG: " + getClass().getName() + " created from ApplicationEx");
			// System.out.println("PropertyResolver: " + getPropertyResolver().getClass().getName());
			// System.out.println("VariableResolver: " + getVariableResolver().getClass().getName());
			// System.out.println("NavigationHandler: " + getNavigationHandler().getClass().getName());
		}

	}

	private void initListeners() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.application.ApplicationExImpl#getViewHandler()
	 */
	@Override
	public ViewHandler getViewHandler() {
		return super.getViewHandler();
	}

}

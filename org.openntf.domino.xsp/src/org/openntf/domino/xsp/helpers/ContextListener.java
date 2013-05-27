package org.openntf.domino.xsp.helpers;

import javax.faces.context.FacesContext;

import org.openntf.domino.xsp.Activator;

import com.ibm.xsp.event.FacesContextListener;

public class ContextListener extends AbstractListener implements FacesContextListener {
	public final static boolean ATTACH_LISTENER = true; // change this to false if you don't want to bother.

	private final static boolean _debug = Activator.isDebug();
	static {
		if (_debug)
			System.out.println(ContextListener.class.getName() + " loaded");
	}

	public ContextListener() {
		_debugOut("created");
	}

	@Override
	public void beforeContextReleased(FacesContext arg0) {
		// this method is guaranteed to be called when request processing is complete, no matter what phase the request was in
		// it is your last chance to access any Domino objects before they will be recycled.
		_debugOut("beforeContextReleased triggered");
		// your code goes here
	}

	@Override
	public void beforeRenderingPhase(FacesContext arg0) {
		// this method is called prior to rendering, even if the rendering was jumped to from some other phase of the JSF lifecycle
		_debugOut("beforeRenderingPhase triggered");
		// your code goes here
	}

}

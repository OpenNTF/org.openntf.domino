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
package org.openntf.domino.xsp.helpers;

import javax.faces.context.FacesContext;

import org.openntf.domino.xsp.ODAPlatform;

import com.ibm.xsp.event.FacesContextListener;

/**
 * ContextListener
 */
@SuppressWarnings("nls")
public class ContextListener extends AbstractListener implements FacesContextListener {
	public final static boolean ATTACH_LISTENER = true; // change this to false if you don't want to bother.

	private final static boolean _debug = ODAPlatform.isDebug();
	static {
		if (_debug)
			System.out.println(ContextListener.class.getName() + " loaded");
	}

	/**
	 * Constructor
	 */
	public ContextListener() {
		_debugOut("created");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.event.FacesContextListener#beforeContextReleased(javax.faces.context.FacesContext)
	 */
	@Override
	public void beforeContextReleased(final FacesContext arg0) {
		// this method is guaranteed to be called when request processing is complete, no matter what phase the request was in
		// it is your last chance to access any Domino objects before they will be recycled.
		_debugOut("beforeContextReleased triggered");
		// your code goes here
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.event.FacesContextListener#beforeRenderingPhase(javax.faces.context.FacesContext)
	 */
	@Override
	public void beforeRenderingPhase(final FacesContext arg0) {
		// this method is called prior to rendering, even if the rendering was jumped to from some other phase of the JSF lifecycle
		_debugOut("beforeRenderingPhase triggered");
		// your code goes here
	}

}

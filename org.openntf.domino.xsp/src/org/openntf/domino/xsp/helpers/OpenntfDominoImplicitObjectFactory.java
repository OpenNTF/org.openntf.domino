/*
 * © Copyright Red Pill, LLC 2013
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

import java.util.Map;

import javax.faces.context.FacesContext;

import org.openntf.domino.utils.Factory;
import org.openntf.domino.xsp.Activator;

import com.ibm.xsp.context.FacesContextEx;
import com.ibm.xsp.el.ImplicitObjectFactory;
import com.ibm.xsp.util.TypedUtil;

public class OpenntfDominoImplicitObjectFactory implements ImplicitObjectFactory {
	public static class ContextListener implements com.ibm.xsp.event.FacesContextListener {
		@Override
		public void beforeContextReleased(FacesContext paramFacesContext) {
			Factory.clearSession();
		}

		@Override
		public void beforeRenderingPhase(FacesContext paramFacesContext) {
			// TODO NOOP

		}
	}

	// TODO this is really just a sample on how to get to an entry point in the API
	private static Boolean GODMODE;

	private static boolean isGodMode() {
		if (GODMODE == null) {
			GODMODE = Boolean.FALSE;
			String[] envs = Activator.getEnvironmentStrings();
			if (envs != null) {
				for (String s : envs) {
					if (s.equalsIgnoreCase("godmode")) {
						GODMODE = Boolean.TRUE;
					}
				}
			}
		}
		return GODMODE.booleanValue();
	}

	private final String[][] implicitObjectList = {
			{ (isGodMode() ? "session" : "opensession"), org.openntf.domino.Session.class.getName() },
			{ (isGodMode() ? "database" : "opendatabase"), org.openntf.domino.Database.class.getName() } };

	public OpenntfDominoImplicitObjectFactory() {
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void createImplicitObjects(FacesContextEx paramFacesContextEx) {
		Map localMap = TypedUtil.getRequestMap(paramFacesContextEx.getExternalContext());
		org.openntf.domino.Session s = null;
		if (localMap.containsKey("session")) {
			Object current = localMap.get("session");
			if (!(current instanceof org.openntf.domino.Session)) {
				s = Factory.fromLotus((lotus.domino.Session) current, org.openntf.domino.Session.class, null);
				localMap.put((isGodMode() ? "session" : "opensession"), s);
			} else {
				s = (org.openntf.domino.Session) current;
			}
		}
		if (localMap.containsKey("database")) {
			Object current = localMap.get("database");
			if (!(current instanceof org.openntf.domino.Session)) {
				org.openntf.domino.Database db = Factory.fromLotus((lotus.domino.Database) current, org.openntf.domino.Database.class, s);
				localMap.put((isGodMode() ? "database" : "opendatabase"), db);
			}
		}
		paramFacesContextEx.addRequestListener(new ContextListener());
	}

	@Override
	public Object getDynamicImplicitObject(FacesContextEx paramFacesContextEx, String paramString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void destroyImplicitObjects(FacesContext paramFacesContext) {
		// TODO Auto-generated method stub

	}

	@Override
	public String[][] getImplicitObjectList() {
		return this.implicitObjectList;
	}

}

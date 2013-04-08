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

import com.ibm.domino.xsp.module.nsf.NotesContext;
import com.ibm.xsp.context.FacesContextEx;
import com.ibm.xsp.util.TypedUtil;

public class ImplicitObjectFactory implements com.ibm.xsp.el.ImplicitObjectFactory {
	// TODO this is really just a sample on how to get to an entry point in the API

	private final String[][] implicitObjectList = { { "session", org.openntf.domino.Session.class.getName() },
			{ "database", org.openntf.domino.Database.class.getName() } };
	private final static boolean _debug = Activator.isDebug();

	public ImplicitObjectFactory() {
		if (_debug)
			System.out.println(getClass().getName() + " created");
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void createImplicitObjects(FacesContextEx paramFacesContextEx) {
		Map localMap = TypedUtil.getRequestMap(paramFacesContextEx.getExternalContext());
		org.openntf.domino.Session s = Factory.fromLotus(NotesContext.getCurrent().getCurrentSession(), org.openntf.domino.Session.class,
				null);
		localMap.put("session", s);
		org.openntf.domino.Database db = Factory.fromLotus(s.getCurrentDatabase(), org.openntf.domino.Database.class, s);
		localMap.put("database", db);
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

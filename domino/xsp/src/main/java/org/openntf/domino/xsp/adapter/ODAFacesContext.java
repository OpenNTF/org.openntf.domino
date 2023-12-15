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
package org.openntf.domino.xsp.adapter;

import java.util.Map;

import javax.faces.context.FacesContext;

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xsp.ODAPlatform;

import com.ibm.xsp.domino.context.DominoFacesContext;
import com.ibm.xsp.util.TypedUtil;

public class ODAFacesContext extends DominoFacesContext {
	public boolean implicitsDone_ = false;

	public ODAFacesContext(final FacesContext paramFacesContext) {
		super(paramFacesContext);
		//		System.out.println("DEBUG: Creating new ODAFacesContext " + System.identityHashCode(this));
	}

	@Override
	public void createImplicitObjects() {
		if (!implicitsDone_) {
			super.createImplicitObjects();
			//			System.out.println("DEBUG: Requesting implicit objects on ODAFacesContext " + System.identityHashCode(this));
			if (ODAPlatform.isAppGodMode(null)) {
				Session session = Factory.getSession(SessionType.CURRENT);
				Database db = session.getCurrentDatabase();
				Map<String, Object> ecMap = TypedUtil.getRequestMap(getExternalContext());

				// overwrite the DominoImplicitObjects objects
				ecMap.put("session", session); //$NON-NLS-1$
				ecMap.put("database", db); //$NON-NLS-1$
			}
			implicitsDone_ = true;
		}
	}

	@Override
	public Object getDynamicImplicitObject(final String objectName) {
		if (ODAPlatform.isAppGodMode(null)) {
			if ("sessionAsSignerWithFullAccess".equals(objectName)) { //$NON-NLS-1$
				return Factory.getSession(SessionType.SIGNER_FULL_ACCESS);
			} else if ("sessionAsSigner".equals(objectName)) { //$NON-NLS-1$
				return Factory.getSession(SessionType.SIGNER);
			}
		}
		return super.getDynamicImplicitObject(objectName);
	}

}

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
	public ODAFacesContext(final FacesContext paramFacesContext) {
		super(paramFacesContext);
	}

	@Override
	public void createImplicitObjects() {
		super.createImplicitObjects();

		if (ODAPlatform.isAppGodMode(null)) {
			Session session = Factory.getSession(SessionType.CURRENT);
			Database db = session.getCurrentDatabase();
			Map<String, Object> ecMap = TypedUtil.getRequestMap(getExternalContext());

			// overwrite the DominoImplicitObjects objects
			ecMap.put("session", session);
			ecMap.put("database", db);
		}
	}

	@Override
	public Object getDynamicImplicitObject(final String objectName) {
		if (ODAPlatform.isAppGodMode(null)) {
			if ("sessionAsSignerWithFullAccess".equals(objectName)) {
				return Factory.getSession(SessionType.SIGNER_FULL_ACCESS);
			} else if ("sessionAsSigner".equals(objectName)) {
				return Factory.getSession(SessionType.SIGNER);
			}
		}
		return super.getDynamicImplicitObject(objectName);
	}

}

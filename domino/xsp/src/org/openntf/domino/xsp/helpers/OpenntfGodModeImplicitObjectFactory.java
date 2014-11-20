package org.openntf.domino.xsp.helpers;

import java.util.Map;

import javax.faces.context.FacesContext;

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xsp.ODAPlatform;

import com.ibm.xsp.context.FacesContextEx;
import com.ibm.xsp.el.ImplicitObjectFactory;
import com.ibm.xsp.util.TypedUtil;

//import org.openntf.domino.Session;
/**
 * Factory for managing the plugin
 */
@SuppressWarnings("unchecked")
public class OpenntfGodModeImplicitObjectFactory implements ImplicitObjectFactory {
	private final String[][] implicitObjectList//
	= { { "database", Database.class.getName() }, //
			{ "session", Session.class.getName() }, //
			{ "sessionAsSigner", Session.class.getName() }, //
			{ "sessionAsSignerWithFullAccess", Session.class.getName() } };

	protected ImplicitObjectFactory delegate;

	public OpenntfGodModeImplicitObjectFactory(final ImplicitObjectFactory iof) {
		delegate = iof;
	}

	@Override
	public void createImplicitObjects(final FacesContextEx ctx) {
		if (!ODAPlatform.isAPIEnabled())
			return;

		Session session = Factory.getSession(SessionType.CURRENT);
		Database db = session.getCurrentDatabase();
		Map<String, Object> ecMap = TypedUtil.getRequestMap(ctx.getExternalContext());

		// first call the delegate. There may be more than the 2 objects in future that we overwrite in the next step
		delegate.createImplicitObjects(ctx);
		// overwrite the IBM objects
		ecMap.put("session", session);
		ecMap.put("database", db);
	}

	@Override
	public Object getDynamicImplicitObject(final FacesContextEx ctx, final String objectName) {
		if ("sessionAsSignerWithFullAccess".equals(objectName)) {
			return Factory.getSession(SessionType.SIGNER_FULL_ACCESS);
		} else if ("sessionAsSigner".equals(objectName)) {
			return Factory.getSession(SessionType.SIGNER);
		}

		return delegate.getDynamicImplicitObject(ctx, objectName);
	}

	@Override
	public void destroyImplicitObjects(final FacesContext paramFacesContext) {
		delegate.destroyImplicitObjects(paramFacesContext);
	}

	@Override
	public String[][] getImplicitObjectList() {
		if (delegate != null)
			return delegate.getImplicitObjectList();
		return implicitObjectList;
	}
}

package org.openntf.domino.xsp.helpers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xsp.ODAPlatform;
import org.openntf.domino.xsp.XspOpenLogErrorHolder;

import com.ibm.xsp.application.ApplicationEx;
import com.ibm.xsp.context.FacesContextEx;
import com.ibm.xsp.el.ImplicitObjectFactory;
import com.ibm.xsp.util.TypedUtil;

//import org.openntf.domino.Session;
/**
 * Factory for the "open" objects. Take care: there is an other Factory: {@link OpenntfGodModeImplicitObjectFactory}
 */
@SuppressWarnings("unchecked")
public class OpenntfDominoImplicitObjectFactory implements ImplicitObjectFactory {

	public boolean implicitsDone_ = false;

	private final String[][] implicitObjectList//
			= { { "openDatabase", Database.class.getName() }, //
					{ "openSession", Session.class.getName() }, //
					{ "openSessionAsSigner", Session.class.getName() }, //
					{ "openSessionAsSignerWithFullAccess", Session.class.getName() }, //
					{ "openLogBean", XspOpenLogErrorHolder.class.getName() }, //
					{ "serverScope", Map.class.getName() }, // a scope server wide
					{ "identityScope", Map.class.getName() }, // a scope per user (server wide)
					{ "userScope", Map.class.getName() }, // a scope per user (application wide)

			};

	private XspOpenLogErrorHolder openLogBean_;

	/**
	 * Creates the "cheap" objects
	 */
	@Override
	public void createImplicitObjects(final FacesContextEx ctx) {
		if (implicitsDone_) {
			implicitsDone_ = true;
			if (!ODAPlatform.isAPIEnabled(null))
				return;

			Session session = Factory.getSession(SessionType.CURRENT);

			Database db = session.getCurrentDatabase();
			Map<String, Object> ecMap = TypedUtil.getRequestMap(ctx.getExternalContext());

			ecMap.put("openSession", session);
			ecMap.put("openDatabase", db);

			// Attach NSA
			if (ODAPlatform.isAppFlagSet("nsa")) {
				Application app = ctx.getApplication();
				if (app instanceof ApplicationEx) {
					NSA.INSTANCE.registerApplication((ApplicationEx) app);
					NSA.INSTANCE.registerSession((ApplicationEx) app, (HttpSession) ctx.getExternalContext().getSession(true));
				}
			}
		}
	}

	@Override
	public Object getDynamicImplicitObject(final FacesContextEx ctx, final String objectName) {
		if (objectName.length() > 1) {

			switch (objectName.charAt(0)) {
			case 's':
				if ("serverScope".equals(objectName))
					return getServerMap(ctx);
			case 'o':
				if ("openSessionAsSignerWithFullAccess".equals(objectName))
					return Factory.getSession(SessionType.SIGNER_FULL_ACCESS);
				if ("openSessionAsSigner".equals(objectName))
					return Factory.getSession(SessionType.SIGNER);
				if ("openLogBean".equals(objectName))
					return getOpenLogBean();
				break;

			case 'i':
				if ("identityScope".equals(objectName))
					return getUserScopeFrom(getServerMap(ctx));
				break;
			case 'u':
				if ("userScope".equals(objectName))
					return getUserScopeFrom(getApplicationMap(ctx));
				break;
			}
		}
		return null;
	}

	private XspOpenLogErrorHolder getOpenLogBean() {
		if (openLogBean_ == null)
			openLogBean_ = new XspOpenLogErrorHolder();
		return openLogBean_;
	}

	@Override
	public void destroyImplicitObjects(final FacesContext paramFacesContext) {
	}

	@Override
	public String[][] getImplicitObjectList() {
		return implicitObjectList;
	}

	/**
	 * Gets the serverScope map (ServerBean instance)
	 * 
	 * @param ctx
	 *            FacesContext
	 * @return Map<String, Object> serverMap
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	private static Map<String, Object> getServerMap(final FacesContext ctx) {
		return ServerBean.getCurrentInstance();
	}

	/**
	 * Returns the applicationMap
	 * 
	 * @param ctx
	 *            FacesContext
	 * @return Map<String, Object> applicationMap
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	private static Map<String, Object> getApplicationMap(final FacesContext ctx) {
		return ((FacesContextEx) ctx).getExternalContext().getApplicationMap();
	}

	//	/**
	//	 * Whether the application is currently under surveillance by {@link NSA}
	//	 * 
	//	 * @param ctx
	//	 *            FacesContext
	//	 * @return boolean
	//	 * @since org.openntf.domino.xsp 4.5.0
	//	 */
	//	public static boolean isAppUnderSurveillance(final FacesContext ctx) {
	//		return false;
	//		//		// Map<String, Object> appMap = ctx.getExternalContext().getApplicationMap();
	//		//		Object current = getAppMap(ctx).get(OpenntfDominoImplicitObjectFactory2.class.getName() + "_NSA");
	//		//		if (current == null) {
	//		//			current = Boolean.FALSE;
	//		//			String[] envs = Activator.getXspProperty(Activator.PLUGIN_ID);
	//		//			if (envs != null) {
	//		//				for (String s : envs) {
	//		//					if (s.equalsIgnoreCase("nsa")) {
	//		//						current = Boolean.TRUE;
	//		//						Application app = ctx.getApplication();
	//		//						if (app instanceof ApplicationEx) {
	//		//							NSA.INSTANCE.registerApplication((ApplicationEx) app);
	//		//							NSA.INSTANCE.registerSession((ApplicationEx) app, (HttpSession) ctx.getExternalContext().getSession(true));
	//		//						}
	//		//					}
	//		//				}
	//		//			} else {
	//		//				// System.out.println("XSP ENV IS NULL!!");
	//		//			}
	//		//			getAppMap(ctx).put(OpenntfDominoImplicitObjectFactory2.class.getName() + "_NSA", current);
	//		//		} else {
	//		//			// System.out.println("Current found: " + String.valueOf(current));
	//		//		}
	//		//		return (Boolean) current;
	//	}
	//

	/**
	 * Loads the getUserScopeFrom for the current user for the given map
	 * 
	 * @param ctx
	 *            FacesContext
	 * @param session
	 *            Session
	 * @return Map<String, Object> corresponding to identityScope global variable
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	private Map<String, Object> getUserScopeFrom(final Map<String, Object> scopeMap) {
		String key = Factory.getSession(SessionType.CURRENT).getEffectiveUserName();

		Map<String, Object> userscope = null;
		Object chk = scopeMap.get(key);
		if ((chk == null) || !(chk instanceof Map)) {
			userscope = new ConcurrentHashMap<String, Object>();
			scopeMap.put(key, userscope);
		} else {
			userscope = (Map<String, Object>) chk;
		}
		return userscope;
	}

}

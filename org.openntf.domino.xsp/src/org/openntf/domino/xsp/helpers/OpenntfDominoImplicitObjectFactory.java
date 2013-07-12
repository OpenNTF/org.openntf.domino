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
		public void beforeContextReleased(final FacesContext paramFacesContext) {
			Factory.terminate();
		}

		@Override
		public void beforeRenderingPhase(final FacesContext paramFacesContext) {
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

	private static boolean isAppGodMode(final FacesContext ctx) {
		Map<String, Object> appMap = ctx.getExternalContext().getApplicationMap();
		Object current = appMap.get(OpenntfDominoImplicitObjectFactory.class.getName());
		if (current == null) {
			current = Boolean.FALSE;
			String[] envs = Activator.getXspProperty(Activator.PLUGIN_ID);
			if (envs != null) {
				for (String s : envs) {
					if (s.equalsIgnoreCase("godmode")) {
						current = Boolean.TRUE;
					}
				}
			}
			appMap.put(OpenntfDominoImplicitObjectFactory.class.getName(), current);
		}
		return (Boolean) current;
	}

	private static boolean isAppMimeFriendly(final FacesContext ctx) {
		Map<String, Object> appMap = ctx.getExternalContext().getApplicationMap();
		Object current = appMap.get(OpenntfDominoImplicitObjectFactory.class.getName());
		if (current == null) {
			current = Boolean.FALSE;
			String[] envs = Activator.getXspProperty(Activator.PLUGIN_ID);
			if (envs != null) {
				for (String s : envs) {
					if (s.equalsIgnoreCase("marcel")) {
						current = Boolean.TRUE;
					}
				}
			}
			appMap.put(OpenntfDominoImplicitObjectFactory.class.getName(), current);
		}
		return (Boolean) current;
	}

	private static boolean isAppDebug(final FacesContext ctx) {
		Map<String, Object> appMap = ctx.getExternalContext().getApplicationMap();
		Object current = appMap.get(OpenntfDominoImplicitObjectFactory.class.getName());
		if (current == null) {
			current = Boolean.FALSE;
			String[] envs = Activator.getXspProperty(Activator.PLUGIN_ID);
			if (envs != null) {
				for (String s : envs) {
					if (s.equalsIgnoreCase("raid")) {
						current = Boolean.TRUE;
					}
				}
			}
			appMap.put(OpenntfDominoImplicitObjectFactory.class.getName(), current);
		}
		return (Boolean) current;
	}

	private final String[][] implicitObjectList = {
			{ (isGodMode() ? "session" : "opensession"), org.openntf.domino.Session.class.getName() },
			{ (isGodMode() ? "database" : "opendatabase"), org.openntf.domino.Database.class.getName() } };

	public OpenntfDominoImplicitObjectFactory() {
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void createImplicitObjects(final FacesContextEx ctx) {
		ctx.addRequestListener(new ContextListener());
		if (isAppDebug(ctx)) {
			System.out.println("Creating implicit objects...");
		}
		String sessionKey = (isAppGodMode(ctx) ? "session" : "opensession");
		String dbKey = isAppGodMode(ctx) ? "database" : "opendatabase";
		Factory.setClassLoader(ctx.getContextClassLoader());
		org.openntf.domino.Session s = null;

		Map localMap = TypedUtil.getRequestMap(ctx.getExternalContext());
		if (localMap.containsKey("session")) {
			Object current = localMap.get("session");
			s = Factory.fromLotus((lotus.domino.Session) current, org.openntf.domino.Session.class, null);
		} else {
			if (isAppDebug(ctx)) {
				System.out.println("RequestMap does not contain session. Trying variable resolver...");
			}
			Object current = ctx.getApplication().getVariableResolver().resolveVariable(ctx, "session");
			if (current != null) {
				s = Factory.fromLotus((lotus.domino.Session) current, org.openntf.domino.Session.class, null);
			} else {
				if (isAppDebug(ctx)) {
					System.out.println("Variable resolver didn't work either :(");
				}
			}
		}
		if (s != null) {
			if (isAppDebug(ctx)) {
				System.out.println("Putting session into map with key " + sessionKey);
			}
			localMap.put(sessionKey, s);
			if (isAppMimeFriendly(ctx))
				s.setConvertMIME(false);
		}

		if (localMap.containsKey("database")) {
			Object current = localMap.get("database");
			if (!(current instanceof org.openntf.domino.Session)) {
				org.openntf.domino.Database db = Factory.fromLotus((lotus.domino.Database) current, org.openntf.domino.Database.class, s);
				localMap.put(dbKey, db);
			}
		}
	}

	@Override
	public Object getDynamicImplicitObject(final FacesContextEx paramFacesContextEx, final String paramString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void destroyImplicitObjects(final FacesContext paramFacesContext) {
		// TODO Auto-generated method stub

	}

	@Override
	public String[][] getImplicitObjectList() {
		return this.implicitObjectList;
	}

}

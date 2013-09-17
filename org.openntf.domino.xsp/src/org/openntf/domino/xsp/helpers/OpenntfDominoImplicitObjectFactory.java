package org.openntf.domino.xsp.helpers;

import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.Map;

import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;

import org.openntf.domino.ext.Session.Fixes;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.xsp.Activator;
import org.openntf.domino.xsp.XspOpenLogErrorHolder;
import org.openntf.domino.xsp.XspOpenLogPhaseListener;

import com.ibm.xsp.context.FacesContextEx;
import com.ibm.xsp.el.ImplicitObjectFactory;
import com.ibm.xsp.util.TypedUtil;

//import org.openntf.domino.Session;
@SuppressWarnings("unchecked")
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
		Object current = appMap.get(OpenntfDominoImplicitObjectFactory.class.getName() + "_GODMODE");
		if (current == null) {
			// System.out.println("Current not found. Creating...");
			current = Boolean.FALSE;
			String[] envs = Activator.getXspProperty(Activator.PLUGIN_ID);
			if (envs != null) {
				// if (envs.length == 0) {
				// System.out.println("Got an empty string array!");
				// }
				for (String s : envs) {
					// System.out.println("Xsp check: " + s);
					if (s.equalsIgnoreCase("godmode")) {
						current = Boolean.TRUE;
					}
				}
			} else {
				// System.out.println("XSP ENV IS NULL!!");
			}
			appMap.put(OpenntfDominoImplicitObjectFactory.class.getName() + "_GODMODE", current);
		} else {
			// System.out.println("Current found: " + String.valueOf(current));
		}
		return (Boolean) current;
	}

	private static boolean isAppMimeFriendly(final FacesContext ctx) {
		Map<String, Object> appMap = ctx.getExternalContext().getApplicationMap();
		Object current = appMap.get(OpenntfDominoImplicitObjectFactory.class.getName() + "_MARCEL");
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
			appMap.put(OpenntfDominoImplicitObjectFactory.class.getName() + "_MARCEL", current);
		}
		return (Boolean) current;
	}

	private static boolean isAppAllFix(final FacesContext ctx) {
		Map<String, Object> appMap = ctx.getExternalContext().getApplicationMap();
		Object current = appMap.get(OpenntfDominoImplicitObjectFactory.class.getName() + "_KHAN");
		if (current == null) {
			current = Boolean.FALSE;
			String[] envs = Activator.getXspProperty(Activator.PLUGIN_ID);
			if (envs != null) {
				for (String s : envs) {
					if (s.equalsIgnoreCase("khan")) {
						current = Boolean.TRUE;
					}
				}
			}
			appMap.put(OpenntfDominoImplicitObjectFactory.class.getName() + "_KHAN", current);
		}
		return (Boolean) current;
	}

	private static boolean isAppDebug(final FacesContext ctx) {
		Map<String, Object> appMap = ctx.getExternalContext().getApplicationMap();
		Object current = appMap.get(OpenntfDominoImplicitObjectFactory.class.getName() + "_RAID");
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
			appMap.put(OpenntfDominoImplicitObjectFactory.class.getName() + "_RAID", current);
		}
		return (Boolean) current;
	}

	private final String[][] implicitObjectList = {
			{ (isGodMode() ? "session" : "opensession"), org.openntf.domino.Session.class.getName() },
			{ (isGodMode() ? "database" : "opendatabase"), org.openntf.domino.Database.class.getName() },
			{ "openLogBean", org.openntf.domino.xsp.XspOpenLogErrorHolder.class.getName() } };

	public OpenntfDominoImplicitObjectFactory() {
	}

	private org.openntf.domino.Session createSession(final FacesContextEx ctx) {
		org.openntf.domino.Session session = null;
		String sessionKey = isAppGodMode(ctx) ? "session" : "opensession";
		Map<String, Object> localMap = TypedUtil.getRequestMap(ctx.getExternalContext());
		lotus.domino.Session rawSession = (lotus.domino.Session) localMap.get("session");
		if (rawSession == null) {
			rawSession = (lotus.domino.Session) ctx.getApplication().getVariableResolver().resolveVariable(ctx, "session");
		}
		if (rawSession != null) {
			session = Factory.fromLotus(rawSession, org.openntf.domino.Session.class, null);
			if (isAppAllFix(ctx)) {
				for (Fixes fix : Fixes.values()) {
					session.setFixEnable(fix, true);
				}
			}
			if (isAppMimeFriendly(ctx))
				session.setConvertMIME(false);
			localMap.put(sessionKey, session);
		} else {
			System.out.println("Unable to locate 'session' through request map or variable resolver. Unable to auto-wrap.");
		}
		return session;
	}

	private org.openntf.domino.Database createDatabase(final FacesContextEx ctx, final org.openntf.domino.Session session) {
		org.openntf.domino.Database database = null;
		String dbKey = isAppGodMode(ctx) ? "database" : "opendatabase";
		Map<String, Object> localMap = TypedUtil.getRequestMap(ctx.getExternalContext());
		lotus.domino.Database rawDatabase = (lotus.domino.Database) localMap.get("database");
		if (rawDatabase == null) {
			rawDatabase = (lotus.domino.Database) ctx.getApplication().getVariableResolver().resolveVariable(ctx, "database");
		}
		if (rawDatabase != null) {
			database = Factory.fromLotus(rawDatabase, org.openntf.domino.Database.class, session);
			localMap.put(dbKey, database);
		} else {
			System.out.println("Unable to locate 'database' through request map or variable resolver. Unable to auto-wrap.");
		}
		return database;
	}

	public void createLogHolder(final FacesContextEx ctx) {
		Map<String, Object> localMap = TypedUtil.getRequestMap(ctx.getExternalContext());
		XspOpenLogErrorHolder ol_ = new XspOpenLogErrorHolder();
		localMap.put("openLogBean", ol_);
	}

	@Override
	public void createImplicitObjects(final FacesContextEx ctx) {
		Factory.setClassLoader(ctx.getContextClassLoader());
		ctx.addRequestListener(new ContextListener());
		try {
			// Adding a phase listener is a restricted operation
			AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
				@Override
				public Object run() throws Exception {
					LifecycleFactory factory = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
					Lifecycle lifecycle = factory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
					lifecycle.addPhaseListener(new XspOpenLogPhaseListener());
					return null;
				}
			});
		} catch (Throwable t) {
			t.printStackTrace();
		}
		if (isAppDebug(ctx)) {
			System.out.println("Beginning creation of implicit objects...");
		}
		org.openntf.domino.Session session = createSession(ctx);
		@SuppressWarnings("unused")
		org.openntf.domino.Database database = createDatabase(ctx, session);
		createLogHolder(ctx);
		if (isAppDebug(ctx)) {
			System.out.println("Done creating implicit objects.");
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

package org.openntf.domino.xsp.helpers;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.faces.application.Application;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.openntf.domino.AutoMime;
import org.openntf.domino.ext.Session.Fixes;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.xsp.Activator;
import org.openntf.domino.xsp.XspOpenLogErrorHolder;

import com.ibm.xsp.application.ApplicationEx;
import com.ibm.xsp.context.FacesContextEx;
import com.ibm.xsp.el.ImplicitObjectFactory;
import com.ibm.xsp.util.TypedUtil;

//import org.openntf.domino.Session;
/**
 * Factory for managing the plugin
 */
@SuppressWarnings("unchecked")
public class OpenntfDominoImplicitObjectFactory2 implements ImplicitObjectFactory {

	// NTF The reason the Factory2 version exists is because we were testing moving the "global" settings like
	// godmode and marcel to the xsp.properties and making them per-Application rather than server-wide.
	private static Boolean GODMODE;

	/**
	 * Gets the application map, allowing us to track Xsp Properties enabled per application
	 * 
	 * @param ctx
	 * @return Map<String, Object>
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	private static Map<String, Object> getAppMap(final FacesContext ctx) {
		if (ctx == null)
			return new HashMap<String, Object>();
		ExternalContext ec = ctx.getExternalContext();
		if (ec == null)
			return new HashMap<String, Object>();
		Map<String, Object> result = ec.getApplicationMap();
		return result;
	}

	/**
	 * Gets the serverScope map (ServerBean instance)
	 * 
	 * @param ctx
	 *            FacesContext
	 * @return Map<String, Object> serverScope
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	private static Map<String, Object> getServerMap(final FacesContext ctx) {
		return ServerBean.getCurrentInstance();
	}

	/**
	 * Whether godMode is enabled in Xsp Properties
	 * 
	 * @return boolean godMode
	 * @since org.openntf.domino.xsp 2.5.0
	 */
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

	/**
	 * Gets whether the godMode flag is enabled for the application
	 * 
	 * @param ctx
	 *            FacesContext
	 * @return boolean
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	public static boolean isAppGodMode(final FacesContext ctx) {
		// Map<String, Object> appMap = ctx.getExternalContext().getApplicationMap();
		Object current = getAppMap(ctx).get(OpenntfDominoImplicitObjectFactory2.class.getName() + "_GODMODE");
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
			getAppMap(ctx).put(OpenntfDominoImplicitObjectFactory2.class.getName() + "_GODMODE", current);
		} else {
			// System.out.println("Current found: " + String.valueOf(current));
		}
		return (Boolean) current;
	}

	/**
	 * Whether the application is currently under surveillance by {@link NSA}
	 * 
	 * @param ctx
	 *            FacesContext
	 * @return boolean
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	public static boolean isAppUnderSurveillance(final FacesContext ctx) {
		// Map<String, Object> appMap = ctx.getExternalContext().getApplicationMap();
		Object current = getAppMap(ctx).get(OpenntfDominoImplicitObjectFactory2.class.getName() + "_NSA");
		if (current == null) {
			current = Boolean.FALSE;
			String[] envs = Activator.getXspProperty(Activator.PLUGIN_ID);
			if (envs != null) {
				for (String s : envs) {
					if (s.equalsIgnoreCase("nsa")) {
						current = Boolean.TRUE;
						Application app = ctx.getApplication();
						if (app instanceof ApplicationEx) {
							NSA.INSTANCE.registerApplication((ApplicationEx) app);
							NSA.INSTANCE.registerSession((ApplicationEx) app, (HttpSession) ctx.getExternalContext().getSession(true));
						}
					}
				}
			} else {
				// System.out.println("XSP ENV IS NULL!!");
			}
			getAppMap(ctx).put(OpenntfDominoImplicitObjectFactory2.class.getName() + "_NSA", current);
		} else {
			// System.out.println("Current found: " + String.valueOf(current));
		}
		return (Boolean) current;
	}

	/**
	 * Gets the AutoMime option enabled for the application, an instance of the enum {@link AutoMime}
	 * 
	 * @param ctx
	 *            FacesContext
	 * @return AutoMime
	 * @since org.openntf.domino.xsp 5.0.0
	 */
	private static AutoMime getAppAutoMime(final FacesContext ctx) {
		// Map<String, Object> appMap = ctx.getExternalContext().getApplicationMap();
		Object current = getAppMap(ctx).get(OpenntfDominoImplicitObjectFactory2.class.getName() + "_AUTOMIME");
		if (current == null) {
			current = AutoMime.WRAP_ALL;
			String[] envs = Activator.getXspProperty(Activator.PLUGIN_ID);
			if (envs != null) {
				for (String s : envs) {
					if (s.equalsIgnoreCase("automime32k")) {
						current = AutoMime.WRAP_32K;
					}
					if (s.equalsIgnoreCase("automimenone")) {
						current = AutoMime.WRAP_NONE;
					}

				}
			}
			getAppMap(ctx).put(OpenntfDominoImplicitObjectFactory2.class.getName() + "_AUTOMIME", current);
		}
		return (AutoMime) current;
	}

	/**
	 * Gets whether the marcel flag is enabled for the application
	 * 
	 * @param ctx
	 *            FacesContext
	 * @return boolean
	 * @since org.openntf.domino.xsp 3.0.0
	 */
	private static boolean isAppMimeFriendly(final FacesContext ctx) {
		// Map<String, Object> appMap = ctx.getExternalContext().getApplicationMap();
		Object current = getAppMap(ctx).get(OpenntfDominoImplicitObjectFactory2.class.getName() + "_MARCEL");
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
			getAppMap(ctx).put(OpenntfDominoImplicitObjectFactory2.class.getName() + "_MARCEL", current);
		}
		return (Boolean) current;
	}

	/**
	 * Gets whether the khan flag is enabled for the application
	 * 
	 * @param ctx
	 *            FacesContext
	 * @return boolean
	 * @since org.openntf.domino.xsp 3.0.0
	 */
	private static boolean isAppAllFix(final FacesContext ctx) {
		// Map<String, Object> appMap = ctx.getExternalContext().getApplicationMap();
		Object current = getAppMap(ctx).get(OpenntfDominoImplicitObjectFactory2.class.getName() + "_KHAN");
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
			getAppMap(ctx).put(OpenntfDominoImplicitObjectFactory2.class.getName() + "_KHAN", current);
		}
		return (Boolean) current;
	}

	/**
	 * Ggets whether the raid flag is enabled for the application
	 * 
	 * @param ctx
	 *            FacesContext
	 * @return boolean
	 * @since org.openntf.domino.xsp 3.0.0
	 */
	private static boolean isAppDebug(final FacesContext ctx) {
		// Map<String, Object> appMap = ctx.getExternalContext().getApplicationMap();
		Object current = getAppMap(ctx).get(OpenntfDominoImplicitObjectFactory2.class.getName() + "_RAID");
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
			getAppMap(ctx).put(OpenntfDominoImplicitObjectFactory2.class.getName() + "_RAID", current);
		}
		return (Boolean) current;
	}

	/**
	 * List of implicit objects (global variables accessible via VariableResolver)
	 * 
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	private final String[][] implicitObjectList = {
			{ (isGodMode() ? "session" : "opensession"), org.openntf.domino.Session.class.getName() },
			{ (isGodMode() ? "database" : "opendatabase"), org.openntf.domino.Database.class.getName() },
			{ (Activator.isAPIEnabled() ? "openLogBean" : "openNtfLogBean"), org.openntf.domino.xsp.XspOpenLogErrorHolder.class.getName() } };

	/**
	 * Constructor
	 */
	public OpenntfDominoImplicitObjectFactory2() {
		// System.out.println("Created implicit object factory 2");
	}

	/**
	 * Gets the current Session
	 * 
	 * @param ctx
	 *            FacesContext
	 * @return Session
	 * @since org.openntf.domino 3.0.0
	 */
	private org.openntf.domino.Session createSession(final FacesContextEx ctx) {
		org.openntf.domino.Session session = null;
		isAppUnderSurveillance(ctx);
		String sessionKey = isAppGodMode(ctx) ? "session" : "opensession";
		Map<String, Object> localMap = TypedUtil.getRequestMap(ctx.getExternalContext());
		lotus.domino.Session rawSession = (lotus.domino.Session) localMap.get("session");
		if (rawSession == null) {
			rawSession = (lotus.domino.Session) ctx.getApplication().getVariableResolver().resolveVariable(ctx, "session");
		}
		if (rawSession != null) {
			session = Factory.fromLotus(rawSession, org.openntf.domino.Session.SCHEMA, null);
			session.setAutoMime(getAppAutoMime(ctx));
			if (isAppMimeFriendly(ctx))
				session.setConvertMIME(false);
			if (isAppAllFix(ctx)) {
				for (Fixes fix : Fixes.values()) {
					session.setFixEnable(fix, true);
				}
			}
			localMap.put(sessionKey, session);
		} else {
			System.out.println("Unable to locate 'session' through request map or variable resolver. Unable to auto-wrap.");
		}
		return session;
	}

	/**
	 * Gets the current database
	 * 
	 * @param ctx
	 *            FacesContext
	 * @param session
	 *            Session
	 * @return Database current database
	 * @since org.openntf.domino.xsp 3.0.0
	 */
	private org.openntf.domino.Database createDatabase(final FacesContextEx ctx, final org.openntf.domino.Session session) {
		org.openntf.domino.Database database = null;
		String dbKey = isAppGodMode(ctx) ? "database" : "opendatabase";
		Map<String, Object> localMap = TypedUtil.getRequestMap(ctx.getExternalContext());
		lotus.domino.Database rawDatabase = (lotus.domino.Database) localMap.get("database");
		if (rawDatabase == null) {
			rawDatabase = (lotus.domino.Database) ctx.getApplication().getVariableResolver().resolveVariable(ctx, "database");
		}
		if (rawDatabase != null) {
			database = Factory.fromLotus(rawDatabase, org.openntf.domino.Database.SCHEMA, session);
			localMap.put(dbKey, database);
		} else {
			System.out.println("Unable to locate 'database' through request map or variable resolver. Unable to auto-wrap.");
		}
		return database;
	}

	/**
	 * Loads the userScope for the current user for the current Application
	 * 
	 * @param ctx
	 *            FacesContext
	 * @param session
	 *            Session
	 * @return Map<String, Object> corresponding to userScope global variable
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	private Map<String, Object> createUserScope(final FacesContextEx ctx, final org.openntf.domino.Session session) {
		String key = session.getEffectiveUserName();
		Map<String, Object> userscope = null;
		Object chk = getAppMap(ctx).get(key);
		if (chk == null) {
			userscope = new ConcurrentHashMap<String, Object>();
			getAppMap(ctx).put(key, userscope);
		} else {
			userscope = (Map<String, Object>) chk;
		}
		Map<String, Object> localMap = TypedUtil.getRequestMap(ctx.getExternalContext());
		localMap.put("userScope", userscope);
		return userscope;
	}

	/**
	 * Loads the identityScope for the current user for the whole server
	 * 
	 * @param ctx
	 *            FacesContext
	 * @param session
	 *            Session
	 * @return Map<String, Object> corresponding to identityScope global variable
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	private Map<String, Object> createIdentityScope(final FacesContextEx ctx, final org.openntf.domino.Session session) {
		String key = session.getEffectiveUserName();
		Map<String, Object> userscope = null;
		Object chk = getServerMap(ctx).get(key);
		if (chk == null) {
			userscope = new ConcurrentHashMap<String, Object>();
			getServerMap(ctx).put(key, userscope);
		} else {
			userscope = (Map<String, Object>) chk;
		}
		Map<String, Object> localMap = TypedUtil.getRequestMap(ctx.getExternalContext());
		localMap.put("identityScope", userscope);
		return userscope;
	}

	/**
	 * Loads the serverScope for the server
	 * 
	 * @param ctx
	 *            FacesContext
	 * @param session
	 *            Session
	 * @return Map<String, Object> corresponding to serverScope global variable
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	private Map<String, Object> createServerScope(final FacesContextEx ctx, final org.openntf.domino.Session session) {
		Map<String, Object> server = getServerMap(ctx);
		Map<String, Object> localMap = TypedUtil.getRequestMap(ctx.getExternalContext());
		localMap.put("serverScope", server);
		return server;
	}

	/**
	 * Loads the openLogBean for the application to sessionScope
	 * 
	 * @param ctx
	 *            FacesContext
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	public void createLogHolder(final FacesContextEx ctx) {
		if (isAppDebug(ctx)) {
			System.out.println("Beginning creation of log holder...");
		}
		if (Activator.isAPIEnabled()) {
			Map<String, Object> localMap = TypedUtil.getSessionMap(ctx.getExternalContext());
			XspOpenLogErrorHolder ol_ = new XspOpenLogErrorHolder();
			localMap.put("openLogBean", ol_);
			if (isAppDebug(ctx)) {
				System.out.println("Created log holder...");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.el.ImplicitObjectFactory#createImplicitObjects(com.ibm.xsp.context.FacesContextEx)
	 */
	@Override
	public void createImplicitObjects(final FacesContextEx ctx) {
		if (isAppDebug(ctx)) {
			System.out.println("Beginning creation of implicit objects...");
		}
		// Factory.setClassLoader(ctx.getContextClassLoader());
		// ctx.addRequestListener(new ContextListener());
		org.openntf.domino.Session session = createSession(ctx);
		@SuppressWarnings("unused")
		org.openntf.domino.Database database = createDatabase(ctx, session);
		createUserScope(ctx, session);
		createIdentityScope(ctx, session);
		createServerScope(ctx, session);
		createLogHolder(ctx);
		if (isAppDebug(ctx)) {
			System.out.println("Done creating implicit objects.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.el.ImplicitObjectFactory#getDynamicImplicitObject(com.ibm.xsp.context.FacesContextEx, java.lang.String)
	 */
	@Override
	public Object getDynamicImplicitObject(final FacesContextEx paramFacesContextEx, final String paramString) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.el.ImplicitObjectFactory#destroyImplicitObjects(javax.faces.context.FacesContext)
	 */
	@Override
	public void destroyImplicitObjects(final FacesContext paramFacesContext) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.el.ImplicitObjectFactory#getImplicitObjectList()
	 */
	@Override
	public String[][] getImplicitObjectList() {
		return this.implicitObjectList;
	}

}

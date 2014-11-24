package org.openntf.domino.xsp.helpers;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.faces.context.FacesContext;

import org.openntf.domino.AutoMime;
import org.openntf.domino.ext.Session.Fixes;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionMode;
import org.openntf.domino.xsp.Activator;
import org.openntf.domino.xsp.XspLibrary;
import org.openntf.domino.xsp.XspOpenLogErrorHolder;

import com.ibm.xsp.context.FacesContextEx;
import com.ibm.xsp.el.ImplicitObjectFactory;
import com.ibm.xsp.util.TypedUtil;

//import org.openntf.domino.Session;
/**
 * Factory for managing the plugin
 */
@SuppressWarnings("unchecked")
public class OpenntfDominoImplicitObjectFactory2 implements ImplicitObjectFactory {
	private static boolean debugAll = false;
	// NTF The reason the Factory2 version exists is because we were testing moving the "global" settings like
	// godmode and marcel to the xsp.properties and making them per-Application rather than server-wide.
	//	private static Boolean GODMODE;

	private transient Boolean GODMODE;
	private transient Boolean APP_ENABLED;
	private transient AutoMime AUTO_MIME;
	private transient Map<String, Boolean> flagMap_;

	/**
	 * Gets the application map, allowing us to track Xsp Properties enabled per application
	 * 
	 * @param ctx
	 * @return Map<String, Object>
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	private Map<String, Boolean> getAppMap() {
		if (flagMap_ == null) {
			flagMap_ = new LinkedHashMap<String, Boolean>();
		}
		return flagMap_;
	}

	private Map<String, Object> getApplicationScopeMap(final FacesContext ctx) {
		return ctx.getExternalContext().getApplicationMap();
	}

	private static Map<String, Object> getServerMap() {
		return ServerBean.getCurrentInstance();
	}

	/**
	 * Gets the serverScope map (ServerBean instance)
	 * 
	 * @param ctx
	 *            FacesContext
	 * @return Map<String, Object> serverScope
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	//	private static Map<String, Object> getServerMap(final FacesContext ctx) {
	//		return ServerBean.getCurrentInstance();
	//	}

	/**
	 * Whether godMode is enabled in Xsp Properties
	 * 
	 * @return boolean godMode
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	private boolean isGodMode() {
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
	 * common code to test if a flag is set in the Xsp properties
	 * 
	 * @param ctx
	 * @param flagName
	 *            use upperCase for flagName, e.g. RAID
	 * @return
	 */
	private boolean isAppLibrarySet() {
		if (APP_ENABLED == null) {
			APP_ENABLED = Boolean.FALSE;
			String[] envs = Activator.getXspProperty("xsp.library.depends");
			if (envs != null) {
				for (String s : envs) {
					if (s.equals(XspLibrary.LIBRARY_ID)) {
						APP_ENABLED = Boolean.TRUE;
					}
				}
			}
		}
		return APP_ENABLED;
	}

	/**
	 * common code to test if a flag is set in the Xsp properties
	 * 
	 * @param ctx
	 * @param flagName
	 *            use upperCase for flagName, e.g. RAID
	 * @return
	 */
	private boolean isAppFlagSet(final String flagName) {
		Boolean current = getAppMap().get(flagName);
		if (current == null) {
			current = Boolean.FALSE;
			String[] envs = Activator.getXspProperty(Activator.PLUGIN_ID);
			if (envs != null) {
				for (String s : envs) {
					if (s.equalsIgnoreCase(flagName)) {
						current = Boolean.TRUE;
					}
				}
			}
			getAppMap().put(flagName, current);
		}
		return current;
	}

	public static boolean sIsAppGodMode() {
		return sIsAppFlagSet("GODMODE");
	}

	private static boolean sIsAppFlagSet(final String flagName) {
		boolean current = false;
		String[] envs = Activator.getXspProperty(Activator.PLUGIN_ID);
		if (envs != null) {
			for (String s : envs) {
				if (s.equalsIgnoreCase(flagName)) {
					current = true;
				}
			}
		}

		return current;
	}

	/**
	 * Gets whether the godMode flag is enabled for the application
	 * 
	 * @param ctx
	 *            FacesContext
	 * @return boolean
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	public boolean isAppGodMode() {
		return isAppFlagSet("GODMODE");
	}

	/**
	 * Gets the AutoMime option enabled for the application, an instance of the enum {@link AutoMime}
	 * 
	 * @param ctx
	 *            FacesContext
	 * @return AutoMime
	 * @since org.openntf.domino.xsp 5.0.0
	 */
	private AutoMime getAppAutoMime() {
		if (AUTO_MIME == null) {
			AUTO_MIME = AutoMime.WRAP_ALL;
			String[] envs = Activator.getXspProperty(Activator.PLUGIN_ID);
			if (envs != null) {
				for (String s : envs) {
					if (s.equalsIgnoreCase("automime32k")) {
						AUTO_MIME = AutoMime.WRAP_32K;
					}
					if (s.equalsIgnoreCase("automimenone")) {
						AUTO_MIME = AutoMime.WRAP_NONE;
					}

				}
			}
		}
		return AUTO_MIME;
	}

	/**
	 * Gets whether the marcel flag is enabled for the application
	 * 
	 * @param ctx
	 *            FacesContext
	 * @return boolean
	 * @since org.openntf.domino.xsp 3.0.0
	 */
	private boolean isAppMimeFriendly() {
		return isAppFlagSet("MARCEL");
	}

	/**
	 * Gets whether the khan flag is enabled for the application
	 * 
	 * @param ctx
	 *            FacesContext
	 * @return boolean
	 * @since org.openntf.domino.xsp 3.0.0
	 */
	private boolean isAppAllFix() {
		return isAppFlagSet("KHAN");
	}

	/**
	 * Ggets whether the raid flag is enabled for the application
	 * 
	 * @param ctx
	 *            FacesContext
	 * @return boolean
	 * @since org.openntf.domino.xsp 3.0.0
	 */
	private boolean isAppDebug() {
		if (debugAll)
			return true;
		return isAppFlagSet("RAID");
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
		String sessionKey = isAppGodMode() ? "session" : "opensession";
		Map<String, Object> localMap = TypedUtil.getRequestMap(ctx.getExternalContext());

		// See if the factory already has an explicit session set (e.g. in Xots)
		//FIXME: NTF We need something more specific here. Back-to-back REST calls can sometimes be serviced by the same thread.
		session = Factory.getSession_unchecked();
		// If we don't have a pre-established session, look for the standard XSP one
		if (session == null) {
			lotus.domino.Session rawSession = (lotus.domino.Session) localMap.get("session");
			if (rawSession == null) {
				rawSession = (lotus.domino.Session) ctx.getApplication().getVariableResolver().resolveVariable(ctx, "session");
			}
			if (rawSession != null) {
				try {
					rawSession.isConvertMIME();
					Factory.setSession(rawSession, SessionMode.DEFAULT);
					// TODO RPr: Should we add session as signer also?

					session = Factory.fromLotus(rawSession, org.openntf.domino.Session.SCHEMA, null);
					//					System.out.println("DEBUG: New session created in thread " + Thread.currentThread().getId() + ": "
					//							+ ctx.getExternalContext().getRequestPathInfo());
				} catch (Exception ne) {
					System.out.println("DEBUG: Session invalid in request from " + ctx.getExternalContext().getRequestContextPath());
				}
			}
		} else {
			//			System.out.println("DEBUG: Session already present when we went to create it in thread " + Thread.currentThread().getId()
			//					+ ": " + ctx.getExternalContext().getRequestPathInfo());
		}

		if (session != null) {
			// Factory.setNoRecycle(session, true);
			session.setAutoMime(getAppAutoMime());
			if (isAppMimeFriendly())
				session.setConvertMIME(false);
			if (isAppAllFix()) {
				for (Fixes fix : Fixes.values()) {
					session.setFixEnable(fix, true);
				}
			}
			if (isAppFlagSet("BUBBLEEXCEPTIONS")) {
				DominoUtils.setBubbleExceptions(true);
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
		String dbKey = isAppGodMode() ? "database" : "opendatabase";
		Map<String, Object> localMap = TypedUtil.getRequestMap(ctx.getExternalContext());

		// TODO: Determine if this is the right way to deal with Xots access to faces contexts
		database = Factory.getDatabase_unchecked();

		if (database == null) {
			lotus.domino.Database rawDatabase = (lotus.domino.Database) localMap.get("database");
			if (rawDatabase == null) {
				rawDatabase = (lotus.domino.Database) ctx.getApplication().getVariableResolver().resolveVariable(ctx, "database");
			}
			if (rawDatabase != null) {
				database = Factory.fromLotus(rawDatabase, org.openntf.domino.Database.SCHEMA, session);
				session.setCurrentDatabase(database);
				//				Factory.setNoRecycle(database, true);
				localMap.put(dbKey, database);
			} else {
				System.out.println("Unable to locate 'database' through request map or variable resolver. Unable to auto-wrap.");
			}
		} else {
			localMap.put(dbKey, database);
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
		Object chk = getAppMap().get(key);
		if (chk == null) {
			userscope = new ConcurrentHashMap<String, Object>();
			getApplicationScopeMap(ctx).put(key, userscope);
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
		Object chk = getServerMap().get(key);
		if (chk == null) {
			userscope = new ConcurrentHashMap<String, Object>();
			getServerMap().put(key, userscope);
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
		Map<String, Object> server = getServerMap();
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
		if (isAppDebug()) {
			System.out.println("DEBUG: Beginning creation of log holder...");
		}
		if (Activator.isAPIEnabled()) {
			Map<String, Object> localMap = TypedUtil.getSessionMap(ctx.getExternalContext());
			XspOpenLogErrorHolder ol_ = new XspOpenLogErrorHolder();
			localMap.put("openLogBean", ol_);
			if (isAppDebug()) {
				System.out.println("DEBUG: Created log holder...");
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
		if (!isAppLibrarySet())
			return;
		if (isAppDebug()) {
			System.out.println("DEBUG: Beginning creation of OpenNTF implicit objects...");
		}
		// TODO RPr: I enabled the "setClassLoader" here
		//		Factory.initThread();
		//		Factory.setUserLocale(ctx.getExternalContext().getRequestLocale());
		//		Factory.setClassLoader(ctx.getContextClassLoader());
		// hopefully locating the app will work now
		org.openntf.domino.Session session = createSession(ctx);
		if (session != null) {
			createDatabase(ctx, session);
			createUserScope(ctx, session);
			createIdentityScope(ctx, session);
			createServerScope(ctx, session);
			createLogHolder(ctx);
		} else {
			System.out.println("WARNING: session object returned during implicitobject creation failed for thread "
					+ Thread.currentThread().getId() + " servicing a request for " + ctx.getExternalContext().getRequestPathInfo());
		}
		if (isAppDebug()) {
			System.out.println("DEBUG: Done creating OpenNTF implicit objects.");
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
	public void destroyImplicitObjects(final FacesContext ctx) {

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

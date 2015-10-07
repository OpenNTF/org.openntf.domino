package org.openntf.domino.xsp;

import java.util.List;
import java.util.concurrent.Callable;

import org.openntf.domino.AutoMime;
import org.openntf.domino.View;
import org.openntf.domino.config.Configuration;
import org.openntf.domino.config.ServerConfiguration;
import org.openntf.domino.exceptions.BackendBridgeSanityCheckException;
import org.openntf.domino.ext.Session.Fixes;
import org.openntf.domino.session.INamedSessionFactory;
import org.openntf.domino.thread.DominoExecutor;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.ThreadConfig;
import org.openntf.domino.xots.Xots;
import org.openntf.domino.xsp.helpers.OsgiServiceLocatorFactory;
import org.openntf.domino.xsp.session.XPageNamedSessionFactory;
import org.openntf.domino.xsp.xots.XotsDominoExecutor;
import org.openntf.service.ServiceLocatorFinder;

import com.ibm.commons.Platform;
import com.ibm.commons.extension.ExtensionManager;
import com.ibm.commons.util.StringUtil;
import com.ibm.designer.runtime.Application;

public enum ODAPlatform {
	;
	// TODO: create an OSGI-command
	public static final boolean _debug = false;
	public static final boolean debugAll = false;
	public static boolean isStarted_ = false;
	private static int xotsStopDelay;

	public synchronized static boolean isStarted() {
		return isStarted_;
	}

	/**
	 * Start up the ODAPlatform.
	 * 
	 * <ol>
	 * <li>configure the {@link ServiceLocatorFinder}</li>
	 * <li>startup the {@link Factory}</li>
	 * <li>Configure the Factory with proper {@link INamedSessionFactory}s for XPages</li>
	 * <li>Call {@link #verifyIGetEntryByKey()}</li>
	 * <li>Start the {@link Xots} with 10 threads (if it is not completely disabled with "XotsTasks=0" in oda.nsf)</li>
	 * </ol>
	 * 
	 * This is done automatically on server start or can manually invoked with<br>
	 * <code>tell http osgi oda start</code><br>
	 * on the server console.
	 */
	public synchronized static void start() {
		if (!isStarted()) {
			isStarted_ = true;
			// Here is all the init/term stuff done
			ServiceLocatorFinder.setServiceLocatorFactory(new OsgiServiceLocatorFactory());
			Factory.startup();
			// Setup the named factories 4 XPages
			Factory.setNamedFactories4XPages(new XPageNamedSessionFactory(false), new XPageNamedSessionFactory(true));
			ServerConfiguration cfg = Configuration.getServerConfiguration();
			int xotsTasks = cfg.getXotsTasks();
			// We must read the value here, because in the ShutDown, it is not possible to navigate through views and the code will fail.
			xotsStopDelay = cfg.getXotsStopDelay();
			if (xotsTasks > 0) {
				DominoExecutor executor = new XotsDominoExecutor(xotsTasks);
				try {
					Xots.start(executor);
				} catch (IllegalStateException e) {
					if (isDebug()) {
						throw e;
					}
				}
				List<?> tasklets = ExtensionManager.findServices(null, ODAPlatform.class, "org.openntf.domino.xots.tasklet");

				for (Object tasklet : tasklets) {
					if (tasklet instanceof Callable<?> || tasklet instanceof Runnable) {
						@SuppressWarnings("unused")
						ClassLoader cl = tasklet.getClass().getClassLoader();

						Factory.println("XOTS", "Registering tasklet " + tasklet);

						if (tasklet instanceof Callable<?>) {
							Xots.getService().submit((Callable<?>) tasklet);
						} else {
							Xots.getService().submit((Runnable) tasklet);
						}
					}
				}

			}
		}
	}

	/**
	 * Stops the ODA Platform and tries to kill all running Xots Tasks.
	 * 
	 * This is done automatically on server shutdown or can manually invoked with<br>
	 * <code>tell http osgi oda stop</code><br>
	 * on the server console.
	 */
	public synchronized static void stop() {
		if (isStarted()) {
			if (Xots.isStarted()) {
				Xots.stop(xotsStopDelay);
			}
			Factory.shutdown();
			isStarted_ = false;
		}
	}

	/**
	 * Gets a property or notes.ini variable, based on the value passed Order of execution is:
	 * <ol>
	 * <li>Platform.getProperty()</li>
	 * <li>System.getProperty()</li>
	 * <li>Os.OSGetEnvironmentString() = notes.ini</li>
	 * </ol>
	 * 
	 * @param propertyName
	 *            String property to check for
	 * @return String value for the property
	 * @since org.openntf.domino.xsp 5.0.0
	 */
	public static String getEnvironmentString(final String propertyName) {
		String result = "";
		try {
			result = Platform.getInstance().getProperty(propertyName);
			if (StringUtil.isEmpty(result)) {
				result = System.getProperty(propertyName);
				if (StringUtil.isEmpty(result)) {
					result = com.ibm.xsp.model.domino.DominoUtils.getEnvironmentString(propertyName);
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return result;
	}

	/**
	 * Gets a notes.ini property, splitting on commas
	 * 
	 * @param propertyName
	 *            String to look up on
	 * @return String[] of values for the given property, split on commas
	 * @since org.openntf.domino 5.0.0
	 */
	public static String[] getEnvironmentStrings(final String propertyName) {
		try {
			String setting = getEnvironmentString(propertyName);
			if (StringUtil.isNotEmpty(setting)) {
				if (StringUtil.indexOfIgnoreCase(setting, ",") > -1) {
					return StringUtil.splitString(setting, ',');
				} else {
					return new String[] { setting };
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return new String[0];
	}

	/**
	 * Gets an Xsp property or notes.ini variable for PLUGIN_ID (="org.openntf.domino.xsp")
	 * 
	 * @return String value for the PLUGIN_ID property
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	public static String getEnvironmentString() {
		return getEnvironmentString(Activator.PLUGIN_ID);
	}

	/**
	 * Gets an Xsp property or notes.ini variable for PLUGIN_ID (="org.openntf.domino.xsp"), splitting on commas
	 * 
	 * @return String[] of values for the given property, split on commas
	 * @since org.openntf.domino 5.0.0
	 */

	public static String[] getEnvironmentStrings() {
		return getEnvironmentStrings(Activator.PLUGIN_ID);
	}

	/**
	 * Gets an Xsp Property, returning just the basic string as seen in the xsp.properties file. Order of execution is:
	 * <ol>
	 * <li>xsp.properties in NSF</li>
	 * <li>Platform.getProperty()</li>
	 * <li>System.getProperty()</li>
	 * <li>Os.OSGetEnvironmentString() = notes.ini</li>
	 * </ol>
	 * 
	 * @param app
	 *            the Application to use (or null for current one)
	 * @param propertyName
	 *            Property name to look for
	 * @return String property value or an empty string
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	public static String getXspPropertyAsString(final String propertyName, Application app) {
		String result = "";
		try {
			if (app == null)
				app = Application.get();

			if (app == null) {
				result = getEnvironmentString(propertyName);
			} else {
				result = app.getProperty(propertyName);
				if (StringUtil.isEmpty(result)) {
					result = getEnvironmentString(propertyName);
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return result;
	}

	/**
	 * Gets an Xsp property. Order of execution is:
	 * <ol>
	 * <li>xsp.properties in NSF</li>
	 * <li>Platform.getProperty()</li>
	 * <li>System.getProperty()</li>
	 * <li>Os.OSGetEnvironmentString() = notes.ini</li>
	 * </ol>
	 * 
	 * @param app
	 *            the Application to use (or null for current one)
	 * @param propertyName
	 *            Property name to look for
	 * @return String array of property, split on commas
	 * @since org.openntf.domino 2.5.0
	 */
	public static String[] getXspProperty(final String propertyName, final Application app) {
		try {
			String setting = getXspPropertyAsString(propertyName, app);
			if (StringUtil.isNotEmpty(setting)) {
				if (StringUtil.indexOfIgnoreCase(setting, ",") > -1) {
					return StringUtil.splitString(setting, ',');
				} else {
					return new String[] { setting };
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return new String[0];
	}

	private static String IS_API_ENABLED = "ODAPlatform.isAPIEnabled";

	/**
	 * Checks whether or not the API is enabled for the current database
	 * 
	 * @param ctx
	 *            the current Application (if none specified, the current is used)
	 * @return boolean whether or not enabled
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	public static boolean isAPIEnabled(Application app) {
		if (app == null)
			app = Application.get();

		if (app == null)
			return false;

		Boolean retVal_ = (Boolean) app.getObject(IS_API_ENABLED);
		if (retVal_ == null) {
			retVal_ = Boolean.FALSE;
			for (String s : getXspProperty("xsp.library.depends", app)) {
				if (s.equalsIgnoreCase(XspLibrary.LIBRARY_ID)) {
					retVal_ = Boolean.TRUE;
					break;
				}
			}
			app.putObject(IS_API_ENABLED, retVal_);
		}
		return retVal_.booleanValue();
	}

	/**
	 * common code to test if a flag is set in the xsp.properties file for the "org.openntf.domino.xsp" value.
	 * 
	 * @param app
	 *            the Application (or null for current one)
	 * @param flagName
	 *            use upperCase for flagName, e.g. RAID
	 * @return true if the flag is set
	 */
	public static boolean isAppFlagSet(final String flagName, Application app) {
		if (app == null)
			app = Application.get();

		if (app == null)
			return false;

		String key = "ODAPlatform.flag." + flagName;
		Boolean retVal_ = (Boolean) app.getObject(key);
		if (retVal_ == null) {
			retVal_ = Boolean.FALSE;
			for (String s : getXspProperty(Activator.PLUGIN_ID, app)) {
				if (s.equalsIgnoreCase(flagName)) {
					retVal_ = Boolean.TRUE;
					break;
				}
			}
			app.putObject(key, retVal_);
		}
		return retVal_.booleanValue();
	}

	private static String GET_AUTO_MIME = "ODAPlatform.getAutoMime";

	/**
	 * Gets the AutoMime option enabled for the application, an instance of the enum {@link AutoMime}
	 * 
	 * @param ctx
	 *            FacesContext
	 * @return AutoMime
	 * @since org.openntf.domino.xsp 5.0.0
	 */
	public static AutoMime getAppAutoMime(Application app) {
		if (app == null)
			app = Application.get();

		if (app == null)
			return AutoMime.WRAP_ALL;

		AutoMime retVal_ = (AutoMime) app.getObject(GET_AUTO_MIME);
		if (retVal_ == null) {
			retVal_ = AutoMime.WRAP_ALL;

			for (String s : getXspProperty(Activator.PLUGIN_ID, app)) {
				if (s.equalsIgnoreCase("automime32k")) {
					retVal_ = AutoMime.WRAP_32K;
					break;
				} else if (s.equalsIgnoreCase("automimenone")) {
					retVal_ = AutoMime.WRAP_NONE;
					break;
				}
			}

			app.putObject(GET_AUTO_MIME, retVal_);
		}
		return retVal_;
	}

	/**
	 * Gets whether the khan flag is enabled for the application
	 * 
	 * @param ctx
	 *            FacesContext
	 * @return boolean
	 * @since org.openntf.domino.xsp 3.0.0
	 */
	public static boolean isAppAllFix(final Application app) {
		return isAppFlagSet("KHAN", app);
	}

	/**
	 * Gets whether the raid flag is enabled for the application
	 * 
	 * @param ctx
	 *            FacesContext
	 * @return boolean
	 * @since org.openntf.domino.xsp 3.0.0
	 */
	public static boolean isAppDebug(final Application app) {
		if (debugAll)
			return true;
		return isAppFlagSet("RAID", app);
	}

	/**
	 * Gets whether the godmode flag is enabled for the application
	 * 
	 * @param ctx
	 *            FacesContext
	 * @return boolean
	 * @since org.openntf.domino.xsp 3.0.0
	 */

	public static boolean isAppGodMode(final Application app) {
		return isAppFlagSet("GODMODE", app);
	}

	/**
	 * Gets whether the marcel flag is enabled for the application
	 * 
	 * @param ctx
	 *            FacesContext
	 * @return boolean
	 * @since org.openntf.domino.xsp 3.0.0
	 */
	public static boolean isAppMimeFriendly(final Application app) {
		return isAppFlagSet("MARCEL", app);
	}

	/**
	 * Whether or not the library is running in debug. In debug, messages are written to the server console
	 * 
	 * @return boolean debug or not
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	public static boolean isDebug() {
		return _debug;
	}

	public static String getXspPropertyAsString(final String propertyName) {
		return getXspPropertyAsString(propertyName, null);
	}

	public static boolean isAPIEnabled() {
		return isAPIEnabled(null);
	}

	public static boolean isAppFlagSet(final String flagName) {
		return isAppFlagSet(flagName, null);
	}

	public static ThreadConfig getAppThreadConfig(final Application app) {
		Fixes[] fixes = isAppFlagSet("KHAN", app) ? Fixes.values() : null;
		AutoMime autoMime = getAppAutoMime(app);
		boolean bubbleExceptions = ODAPlatform.isAppFlagSet("BUBBLEEXCEPTIONS");
		return new ThreadConfig(fixes, autoMime, bubbleExceptions);
	}
}

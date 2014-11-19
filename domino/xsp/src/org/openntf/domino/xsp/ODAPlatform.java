package org.openntf.domino.xsp;

import org.openntf.domino.AutoMime;
import org.openntf.domino.View;
import org.openntf.domino.exceptions.BackendBridgeSanityCheckException;
import org.openntf.domino.session.INamedSessionFactory;
import org.openntf.domino.thread.DominoExecutor;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.xots.Xots;
import org.openntf.domino.xsp.helpers.OsgiServiceLocatorFactory;
import org.openntf.domino.xsp.helpers.XPageSessionFactory;
import org.openntf.domino.xsp.xots.XotsDominoExecutor;
import org.openntf.service.ServiceLocatorFinder;

import com.ibm.commons.Platform;
import com.ibm.commons.util.StringUtil;
import com.ibm.designer.runtime.Application;
import com.ibm.domino.napi.c.BackendBridge;

public enum ODAPlatform {
	;
	// TODO: create an OSGI-command
	public static final boolean _debug = false;
	public static final boolean debugAll = false;

	/**
	 * Start up the ODAPlatform.
	 * 
	 * <ol>
	 * <li>configure the {@link ServiceLocatorFinder}</li>
	 * <li>startup the {@link Factory}</li>
	 * <li>Configure the Factory with proper {@link INamedSessionFactory}s for XPages</li>
	 * <li>Call {@link #verifyIGetEntryByKey()}</li>
	 * <li>Start the {@link Xots} with 10 threads (if it is not completely disabled with "xots_tasks=0" in Notes.ini)</li>
	 * </ol>
	 * 
	 * This is done automatically on server start or can manually invoked with<br>
	 * <code>tell http osgi oda start</code><br>
	 * on the server console.
	 */
	public static void start() {
		// Here is all the init/term stuff done
		ServiceLocatorFinder.setServiceLocatorFactory(new OsgiServiceLocatorFactory());
		Factory.startup();

		// Setup the named factories 4 XPages
		Factory.setNamedFactories4XPages(new XPageSessionFactory(false), new XPageSessionFactory(true));
		verifyIGetEntryByKey();

		int xotsTasks = 10;
		try {
			xotsTasks = Integer.parseInt(getEnvironmentString("xots_tasks"));
		} catch (NumberFormatException e) {
		}
		if (xotsTasks > 0) {
			DominoExecutor executor = new XotsDominoExecutor(xotsTasks);
			Xots.start(executor);
		}

	}

	/**
	 * Stops the ODA Platform and tries to kill all running Xots Tasks.
	 * 
	 * This is done automatically on server shutdown or can manually invoked with<br>
	 * <code>tell http osgi oda stop</code><br>
	 * on the server console.
	 */
	public static void stop() {
		if (Xots.isStarted()) {
			int xotsStopDelay = 15;
			try {
				xotsStopDelay = Integer.parseInt(getEnvironmentString("xots_stop_delay"));
			} catch (NumberFormatException e) {
			}
			Xots.stop(xotsStopDelay);
		}
		Factory.shutdown();

	}

	/**
	 * there is one weird thing in getViewEntryByKeyWithOptions. IBM messed up something in the JNI calls.
	 * 
	 * a correct call would look like this:
	 * 
	 * <pre>
	 * jclass activityClass = env -&gt; GetObjectClass(dummyView);
	 * jmethodID mID = env -&gt; GetMethodID(activityClass, &quot;iGetEntryByKey&quot;, &quot;...&quot;);
	 * entry = env -&gt; CallIntMethod(obj, mID);
	 * </pre>
	 * 
	 * IBM's code probably looks like this:
	 * 
	 * <pre>
	 * jclass activityClass = env->GetObjectClass(lotus.domino.local.View); <font color=red>&lt;--- This is wrong!</font>
	 * jmethodID mID = env->GetMethodID(activityClass, "iGetEntryByKey", "..."); 
	 * entry = env->CallIntMethod(obj, mID);
	 * </pre>
	 * 
	 * so we get the method-pointer mID for the "lotus.domino.local.View" and we call this method on an "org.openntf.domino.impl.View".
	 * 
	 * This is something that normally wouldn't work. But C/C++ does no sanity checks if it operates on the correct class and will call a
	 * (more or less) random method that is on position "mID". (compare to a 'goto 666')
	 * 
	 * To get that working, we must reorder the methods in the View class, so that "iGetEntryByKey" is on the correct place. Every time you
	 * add or remove methods to the View class (and maybe also to the Base class) the position must be checked again. This is done in the
	 * this method:
	 * <ol>
	 * <li>
	 * We call getViewEntryByKeyWithOptions with the "key parameters" dummyView, null, 42.</li>
	 * <li>This will result in a call to dummyView.iGetEntryByKey(null, false, 42);</li>
	 * <li>If iGetEntryByKey is called with a "null" vector and 42 as int, it will throw a "BackendBridgeSanityCheckException" (which we
	 * expect)</li>
	 * <li>If any other mehtod is called it will throw a different exception. (Most likely a NPE, because our view has no delegate)</li>
	 * </ol>
	 * I hope the server would not crash then. I assume this because:
	 * <ul>
	 * <li>null as parameter is less problematic than a Vector that was forced in a String variable</li>
	 * <li>Throwing an exception does not generate a return value that will be forced in a ViewEntry</li>
	 * </ul>
	 */
	private static void verifyIGetEntryByKey() {
		@SuppressWarnings("deprecation")
		View dummyView = new org.openntf.domino.impl.View();
		try {
			BackendBridge.getViewEntryByKeyWithOptions(dummyView, null, 42);
		} catch (BackendBridgeSanityCheckException allGood) {
			Factory.println("Operation of BackendBridge.getViewEntryByKeyWithOptions verified");
			return;
		} catch (Exception e) {
			e.printStackTrace();
			// if you get here, analyze the stack trace and rearrange the "iGetEntryByKey" method in
			// the view to the position that is listed in the stack trace above "getViewEntryByKeyWithOptions"
		}
		// if you do not get an exception, you will have to debug it with "step into"
		Factory.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		Factory.println("Operation of BackendBridge.getViewEntryByKeyWithOptions FAILED");
		Factory.println("Please read the comments in " + ODAPlatform.class.getName());
		Factory.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
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
	public static String getXspPropertyAsString(Application app, final String propertyName) {
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
	public static String[] getXspProperty(final Application app, final String propertyName) {
		try {
			String setting = getXspPropertyAsString(app, propertyName);
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
			for (String s : getXspProperty(app, "xsp.library.depends")) {
				if (s.equalsIgnoreCase("org.openntf.domino.xsp.XspLibrary")) {
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
	public static boolean isAppFlagSet(Application app, final String flagName) {
		if (app == null)
			app = Application.get();

		if (app == null)
			return false;

		String key = "ODAPlatform.flag." + flagName;
		Boolean retVal_ = (Boolean) app.getObject(key);
		if (retVal_ == null) {
			retVal_ = Boolean.FALSE;
			for (String s : getXspProperty(app, Activator.PLUGIN_ID)) {
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

			for (String s : getXspProperty(app, Activator.PLUGIN_ID)) {
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
		return isAppFlagSet(app, "KHAN");
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
		return isAppFlagSet(app, "RAID");
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
		return isAppFlagSet(app, "GODMODE");
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
		return isAppFlagSet(app, "MARCEL");
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
}

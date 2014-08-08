package org.openntf.domino.xsp;

import java.io.InputStream;
import java.net.URL;

import javax.faces.context.FacesContext;

import org.eclipse.core.runtime.Plugin;
import org.openntf.domino.xots.XotsDaemon;
import org.openntf.domino.xsp.readers.LogReader;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import com.ibm.commons.Platform;
import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.application.ApplicationEx;

/**
 * Activator for the org.openntf.domino.xsp library
 * 
 * @since org.openntf.domino.xsp 2.5.0
 * 
 */
public class Activator extends Plugin {
	public static final String PLUGIN_ID = Activator.class.getPackage().getName();
	public static final boolean _debug = false;

	public static Activator instance;

	private static String version;
	private static BundleContext context;

	/**
	 * Gets the current Activator instance
	 * 
	 * @return Activator
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	public static Activator getDefault() {
		return instance;
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

	/**
	 * Gets the bundle context, i.e. the top level of the plugin. Used to get resources from resources folder.
	 * 
	 * @see LogReader
	 * 
	 * @return BundleContext for all resouorces in this plugin
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	static BundleContext getContext() {
		return context;
	}

	/**
	 * Gets the Bundle-Version property from the MANIFEST-MF
	 * 
	 * @return current version
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	public static String getVersion() {
		if (version == null) {
			version = (String) instance.getBundle().getHeaders().get("Bundle-Version");
		}
		return version;
	}

	/**
	 * Gets a resource, relative to the root of the plugin
	 * 
	 * @param path
	 *            String relative to the root of the plugin, e.g. "/resources/log-transform.xsl"
	 * @return InputStream containing the contents of the resource
	 * @throws Exception
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	public InputStream getResourceAsStream(final String path) throws Exception {
		BundleContext ctx = getContext();
		Bundle bundle = ctx.getBundle();
		URL url = bundle.getEntry(path);
		if (url == null) {
			return Activator.class.getResourceAsStream(path);
		} else {
			InputStream result = url.openStream();
			return result;
		}
	}

	/**
	 * Constructor
	 */
	public Activator() {
		instance = this;
	}

	/**
	 * Gets an Xsp property. Order of execution is:
	 * <ol>
	 * <li>xsp.properties in NSF</li>
	 * <li>xsp.properties on Server</li>
	 * <li>System.getProperty</li>
	 * <li>notes.ini</li>
	 * <li>default value passed in</li>
	 * </ol>
	 * 
	 * @param propertyName
	 *            property to retrieve
	 * @param defaultValue
	 *            default value to use if the property can't be found anywhere else
	 * @return String array of property, split on commas
	 * @since org.openntf.domino 2.5.0
	 */
	public static String[] getXspProperty(final String propertyName) {
		String[] result = null;
		try {
			String setting = getXspPropertyAsString(propertyName);
			if (StringUtil.isNotEmpty(setting)) {
				if (StringUtil.indexOfIgnoreCase(setting, ",") > -1) {
					result = StringUtil.splitString(setting, ',');
				} else {
					result = new String[1];
					result[0] = setting;
				}
			} else {
				result = new String[0];
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		// System.out.println("Result for " + propertyName + ": " + StringUtil.concatStrings(result, ',', true));
		return result;
	}

	/**
	 * Gets an Xsp Property, returning just the basic string as seen in the xsp.properties file. Order of execution is:
	 * <ol>
	 * <li>xsp.properties in NSF</li>
	 * <li>xsp.properties on Server</li>
	 * <li>System.getProperty</li>
	 * <li>notes.ini</li>
	 * </ol>
	 * 
	 * @param propertyName
	 *            Property name to look for
	 * @return String property value or an empty string
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	public static String getXspPropertyAsString(final String propertyName) {
		String result = "";
		try {
			ApplicationEx app = ApplicationEx.getInstance(FacesContext.getCurrentInstance());
			if (null == app) {
				result = getEnvironmentStringsAsString(propertyName);
			} else {
				result = app.getApplicationProperty(propertyName, "");
				if (StringUtil.isEmpty(result)) {
					result = getEnvironmentStringsAsString(propertyName);
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return result;
	}

	/**
	 * Checks whether or not the API is enabled for the current database
	 * 
	 * @return boolean whether or not enabled
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	public static boolean isAPIEnabled() {
		boolean retVal_ = Boolean.FALSE;
		try {
			String[] envs = Activator.getXspProperty("xsp.library.depends");
			if (envs != null) {
				// if (envs.length == 0) {
				// System.out.println("Got an empty string array!");
				// }
				for (String s : envs) {
					// System.out.println("Xsp check: " + s);
					if (s.equalsIgnoreCase("org.openntf.domino.xsp.XspLibrary")) {
						retVal_ = Boolean.TRUE;
					}
				}
			} else {
				// System.out.println("XSP ENV IS NULL!!");
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return retVal_;
	}

	/**
	 * Gets a notes.ini property for org.openntf.domino.xsp, splitting on commas
	 * 
	 * @return String[] of values for the given property, split on commas
	 * @since org.openntf.domino 5.0.0
	 */
	public static String[] getEnvironmentStrings() {
		String[] result = null;
		try {
			String setting = getEnvironmentStringsAsString();
			if (StringUtil.isNotEmpty(setting)) {
				if (StringUtil.indexOfIgnoreCase(setting, ",") > -1) {
					result = StringUtil.splitString(setting, ',');
				} else {
					result = new String[1];
					result[0] = setting;
				}
			} else {
				result = new String[0];
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
		String[] result = null;
		try {
			String setting = getEnvironmentStringsAsString(propertyName);
			if (StringUtil.isNotEmpty(setting)) {
				if (StringUtil.indexOfIgnoreCase(setting, ",") > -1) {
					result = StringUtil.splitString(setting, ',');
				} else {
					result = new String[1];
					result[0] = setting;
				}
			} else {
				result = new String[0];
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return result;
	}

	/**
	 * Gets an Xsp property or notes.ini variable, based on the value passed
	 * 
	 * @param propertyName
	 *            String property to check for
	 * @return String value for the property
	 * @since org.openntf.domino.xsp 5.0.0
	 */
	public static String getEnvironmentStringsAsString(final String propertyName) {
		String result = "";
		try {
			result = Platform.getInstance().getProperty(propertyName); // $NON-NLS-1$
			if (StringUtil.isEmpty(result)) {
				result = System.getProperty(propertyName); // $NON-NLS-1$
				if (StringUtil.isEmpty(result)) {
					result = com.ibm.xsp.model.domino.DominoUtils.getEnvironmentString(propertyName); // $NON-NLS-1$
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return result;
	}

	/**
	 * Gets an Xsp property or notes.ini variable for PLUGIN_ID (="org.openntf.domino.xsp")
	 * 
	 * @return String value for the PLUGIN_ID property
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	public static String getEnvironmentStringsAsString() {
		return getEnvironmentStringsAsString(PLUGIN_ID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext )
	 */
	@Override
	public void start(final BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(final BundleContext bundleContext) throws Exception {
		XotsDaemon.stop();
		Activator.context = null;
	}

}

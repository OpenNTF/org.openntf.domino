package org.openntf.domino.xsp;

import java.io.InputStream;
import java.net.URL;

import javax.faces.context.FacesContext;

import org.eclipse.core.runtime.Plugin;
import org.openntf.domino.utils.Factory;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import com.ibm.commons.Platform;
import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.application.ApplicationEx;

public class Activator extends Plugin {
	public static final String PLUGIN_ID = Activator.class.getPackage().getName();
	public static final boolean _debug = false;

	public static Activator instance;

	private static String version;
	private static BundleContext context;

	public static Activator getDefault() {
		return instance;
	}

	public static boolean isDebug() {
		return _debug;
	}

	static BundleContext getContext() {
		return context;
	}

	public static String getVersion() {
		if (version == null) {
			version = (String) instance.getBundle().getHeaders().get("Bundle-Version");
		}
		return version;
	}

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

	public Activator() {
		instance = this;
	}

	/**
	 * Gets a property. Order of execution is:
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

	public static String getXspPropertyAsString(final String propertyName) {
		String result = "";
		try {
			ApplicationEx app = ApplicationEx.getInstance(FacesContext.getCurrentInstance());
			if (null == app) {
				result = getEnvironmentStringsAsString();
			} else {
				result = app.getApplicationProperty(propertyName, "");
				if (StringUtil.isEmpty(result)) {
					result = getEnvironmentStringsAsString();
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return result;
	}

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

	public static String getEnvironmentStringsAsString() {
		String result = "";
		try {
			result = Platform.getInstance().getProperty(PLUGIN_ID); // $NON-NLS-1$
			if (StringUtil.isEmpty(result)) {
				result = System.getProperty(PLUGIN_ID); // $NON-NLS-1$
				if (StringUtil.isEmpty(result)) {
					result = com.ibm.xsp.model.domino.DominoUtils.getEnvironmentString(PLUGIN_ID); // $NON-NLS-1$
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return result;
	}

	private Bundle bundle_;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext )
	 */
	@Override
	public void start(final BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		bundle_ = bundleContext.getBundle();
		Factory.setClassLoader(Thread.currentThread().getContextClassLoader());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(final BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}

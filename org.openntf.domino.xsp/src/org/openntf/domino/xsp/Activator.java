package org.openntf.domino.xsp;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import com.ibm.commons.Platform;
import com.ibm.commons.util.StringUtil;

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

	public Activator() {
		instance = this;
	}

	public static String[] getEnvironmentStrings() {
		String[] result = null;
		try {
			String setting = Platform.getInstance().getProperty(PLUGIN_ID); // $NON-NLS-1$
			if (StringUtil.isEmpty(setting)) {
				setting = System.getProperty(PLUGIN_ID); // $NON-NLS-1$
				if (StringUtil.isEmpty(setting)) {
					setting = com.ibm.xsp.model.domino.DominoUtils.getEnvironmentString(PLUGIN_ID); // $NON-NLS-1$
				}
			}
			if (StringUtil.isNotEmpty(setting)) {
				result = StringUtil.splitString(setting, ',');
			} else {
				result = new String[0];
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}

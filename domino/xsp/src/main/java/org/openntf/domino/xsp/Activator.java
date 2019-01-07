package org.openntf.domino.xsp;

import java.io.InputStream;
import java.net.URL;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.faces.context.FacesContext;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * Activator for the org.openntf.domino.xsp library
 * 
 * @since org.openntf.domino.xsp 2.5.0
 * 
 */
public class Activator extends Plugin {
	public static final String PLUGIN_ID = Activator.class.getPackage().getName();

	public static Activator instance;

	private static String version;
	private ServiceRegistration<CommandProvider> consoleCommandService;

	//private static BundleContext context;

	/**
	 * Registers the AmgrCommandProvider to handle commands
	 * 
	 * @param bundle
	 * 
	 * @param bundleContext
	 */
	private void registerCommandProvider(final BundleContext bundleContext) {
		CommandProvider cp = new OsgiCommandProvider();
		Bundle bundle = bundleContext.getBundle();
		Dictionary<String, Object> cpDictionary = new Hashtable<String, Object>(7);
		cpDictionary.put("service.vendor", bundle.getHeaders().get("Bundle-Vendor"));
		cpDictionary.put("service.ranking", new Integer(Integer.MIN_VALUE));
		cpDictionary.put("service.pid", bundle.getBundleId() + "." + cp.getClass().getName());

		consoleCommandService = bundleContext.registerService(CommandProvider.class, cp, cpDictionary);
	}

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
	 * Gets the Bundle-Version property from the MANIFEST-MF
	 * 
	 * @return current version
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	public static String getVersion() {
		if (version == null) {
			version = instance.getBundle().getHeaders().get("Bundle-Version");
		}
		return version;
	}

	/**
	 * Gets a resource, relative to the root of the plugin
	 * 
	 * @param path
	 *            String relative to the root of the plugin, e.g. "/resources/log-transform.xsl"
	 * @return InputStream containing the contents of the resource
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	public InputStream getResourceAsStream(final String path) throws Exception {
		Bundle bundle = getBundle();
		URL url = bundle.getEntry(path);
		if (url == null) {
			return null;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext )
	 */
	@Override
	public void start(final BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		registerCommandProvider(bundleContext);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(final BundleContext bundleContext) throws Exception {
		if (consoleCommandService != null) {
			consoleCommandService.unregister();
			consoleCommandService = null;
		}
		super.stop(bundleContext);
	}

	/**
	 * @deprecated use {@link ODAPlatform#getXspPropertyAsString} instead.
	 * 
	 */
	@Deprecated
	public static String getXspPropertyAsString(final String propertyName) {
		return ODAPlatform.getXspPropertyAsString(propertyName, null);
	}

	/**
	 * @deprecated use {@link ODAPlatform#isAPIEnabled} instead.
	 */
	@Deprecated
	public static boolean isAPIEnabled() {
		return ODAPlatform.isAPIEnabled(null);
	}

	/**
	 * @deprecated use {@link ODAPlatform#isAPIEnabled} instead.
	 */
	@Deprecated
	public static boolean isAPIEnabled(final FacesContext ctx) {
		return ODAPlatform.isAPIEnabled(null);
	}

	/**
	 * @deprecated use {@link ODAPlatform#isDebug} instead.
	 */
	@Deprecated
	public static boolean isDebug() {
		return ODAPlatform.isDebug();
	}

}

/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.xsp;

import java.io.InputStream;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
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
		cpDictionary.put("service.vendor", bundle.getHeaders().get("Bundle-Vendor")); //$NON-NLS-1$ //$NON-NLS-2$
		cpDictionary.put("service.ranking", new Integer(Integer.MIN_VALUE)); //$NON-NLS-1$
		cpDictionary.put("service.pid", bundle.getBundleId() + "." + cp.getClass().getName()); //$NON-NLS-1$ //$NON-NLS-2$

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
			version = AccessController.doPrivileged((PrivilegedAction<String>)() -> instance.getBundle().getHeaders().get("Bundle-Version")); //$NON-NLS-1$
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

package org.openntf.domino.xsp;

import java.io.InputStream;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.FacesContext;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.openntf.domino.View;
import org.openntf.domino.exceptions.BackendBridgeSanityCheckException;
import org.openntf.domino.thread.DominoExecutor;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.xots.XotsDaemon;
import org.openntf.domino.xsp.xots.XotsDominoExecutor;
import org.openntf.service.IServiceLocator;
import org.openntf.service.IServiceLocatorFactory;
import org.openntf.service.ServiceLocatorFinder;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.ibm.commons.Platform;
import com.ibm.commons.util.StringUtil;
import com.ibm.domino.napi.c.BackendBridge;
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
	private ServiceRegistration consoleCommandService;

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

		consoleCommandService = bundleContext.registerService(CommandProvider.class.getName(), cp, cpDictionary);
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
	 * Whether or not the library is running in debug. In debug, messages are written to the server console
	 * 
	 * @return boolean debug or not
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	public static boolean isDebug() {
		return _debug;
	}

	//	/**
	//	 * Gets the bundle context, i.e. the top level of the plugin. Used to get resources from resources folder.
	//	 * 
	//	 * @see LogReader
	//	 * 
	//	 * @return BundleContext for all resouorces in this plugin
	//	 * @since org.openntf.domino.xsp 2.5.0
	//	 */
	//	static BundleContext getContext() {
	//		return context;
	//	}

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
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	public InputStream getResourceAsStream(final String path) throws Exception {
		//BundleContext ctx = getContext();
		Bundle bundle = getBundle();
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
		super.start(bundleContext);

		registerCommandProvider(bundleContext);

		startOda();

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
		stopOda();
		super.stop(bundleContext);
	}

	public static class OsgiServiceLocatorFactory implements IServiceLocatorFactory {

		@Override
		public IServiceLocator createServiceLocator() {
			return AccessController.doPrivileged(new PrivilegedAction<IServiceLocator>() {
				@Override
				public IServiceLocator run() {
					ApplicationFactory aFactory = (ApplicationFactory) FactoryFinder
							.getFactory("javax.faces.application.ApplicationFactory");
					if (aFactory == null)
						return null;
					final ApplicationEx app_ = (ApplicationEx) aFactory.getApplication();
					if (app_ == null)
						return null;
					return new IServiceLocator() {
						final Map<Class<?>, List<?>> cache = new HashMap<Class<?>, List<?>>();

						@Override
						public <T> List<T> findApplicationServices(final Class<T> serviceClazz) {
							List<T> ret = (List<T>) cache.get(serviceClazz);

							if (ret == null) {
								ret = AccessController.doPrivileged(new PrivilegedAction<List<T>>() {
									@Override
									public List<T> run() {
										return app_.findServices(serviceClazz.getName());
									}
								});
								if (Comparable.class.isAssignableFrom(serviceClazz)) {
									Collections.sort((List<? extends Comparable>) ret);
								}
								cache.put(serviceClazz, ret);
							}
							return ret;
						}
					};

				}
			});
		}

	}

	public static void startOda() {
		// Here is all the init/term stuff done
		ServiceLocatorFinder.setServiceLocatorFactory(new OsgiServiceLocatorFactory());
		Factory.startup();

		verifyIGetEntryByKey();

		DominoExecutor executor = new XotsDominoExecutor(50);
		XotsDaemon.start(executor);

	}

	public static void stopOda() {
		XotsDaemon.stop(600);
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
			System.out.println("Operation of BackendBridge.getViewEntryByKeyWithOptions verified");
			return;
		} catch (Exception e) {
			e.printStackTrace();
			// if you get here, analyze the stack trace and rearrange the "iGetEntryByKey" method in
			// the view to the position that is listed in the stack trace above "getViewEntryByKeyWithOptions"
		}
		// if you do not get an exception, you will have to debug it with "step into"
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		System.out.println("Operation of BackendBridge.getViewEntryByKeyWithOptions FAILED");
		System.out.println("Please read the comments in " + Activator.class.getName());
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

	}

}

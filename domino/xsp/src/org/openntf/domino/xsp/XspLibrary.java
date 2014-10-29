package org.openntf.domino.xsp;

import java.util.ArrayList;
import java.util.List;

import org.openntf.domino.View;
import org.openntf.domino.exceptions.BackendBridgeSanityCheckException;
import org.openntf.domino.xsp.adapter.OpenntfHttpService;
import org.openntf.domino.xsp.config.DominoConfig;

import com.ibm.designer.runtime.domino.adapter.HttpService;
import com.ibm.designer.runtime.domino.adapter.LCDEnvironment;
import com.ibm.domino.napi.c.BackendBridge;
import com.ibm.xsp.extlib.config.ExtlibPluginConfig;
import com.ibm.xsp.library.AbstractXspLibrary;

/**
 * @author nfreeman
 * 
 */
public class XspLibrary extends AbstractXspLibrary {
	private final static String LIBRARY_ID = XspLibrary.class.getName();
	public final static String LIBRARY_BEAN_PREFIX = "org.openntf.domino.xsp";
	private static Boolean GLOBAL;
	private List<ExtlibPluginConfig> plugins;

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
	private void verifyIGetEntryByKey() {
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
		System.out.println("Please read the comments in " + getClass().getName());
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

	}

	/**
	 * Whether or not global mode is enabled (org.openntf.domino.xsp=global). This is used for the
	 * {@link AbstractXspLibrary#isGlobalScope()} method
	 * 
	 * @return boolean, whether global or not
	 */
	private static boolean isGlobal() {
		if (GLOBAL == null) {
			GLOBAL = Boolean.FALSE;
			String[] envs = Activator.getEnvironmentStrings();
			if (envs != null) {
				for (String s : envs) {
					if (s.equalsIgnoreCase("global")) {
						GLOBAL = Boolean.TRUE;
					}
				}
			}
		}
		return GLOBAL.booleanValue();
	}

	/**
	 * Constructor
	 */
	public XspLibrary() {
		System.out.println("Loading org.openntf.domino.xsp library");
		verifyIGetEntryByKey();
		try {
			for (HttpService service : LCDEnvironment.getInstance().getServices()) {
				if (service instanceof OpenntfHttpService) {
					((OpenntfHttpService) service).activate();
				}
			}
		} catch (Throwable t) {
			System.out.println("HttpServices don't work for client");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.library.AbstractXspLibrary#getDependencies()
	 */
	@Override
	public String[] getDependencies() {
		return new String[] { "com.ibm.xsp.core.library", "com.ibm.xsp.extsn.library", "com.ibm.xsp.domino.library",
				"com.ibm.xsp.designer.library", "com.ibm.xsp.extlib.library" };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.library.AbstractXspLibrary#getFacesConfigFiles()
	 */
	@Override
	public String[] getFacesConfigFiles() {
		String[] files = new String[] { "domino-faces-config.xml" };
		// We might want to take this approach in the future???
		// List<ExtlibPluginConfig> plugins = getExtlibPluginConfigs();
		// for( ExtlibPluginConfig plugin: plugins) {
		// files = plugin.getFacesConfigFiles(files);
		// }
		return files;
	}

	/**
	 * Adds {@link DominoConfig} to the list of available plugins, loading the config for the OpenNTF Domino Pickers
	 * 
	 * @return List<ExtlibPluginConfig> of configs in use for the library
	 * @since org.openntf.domino 4.5.0
	 */
	private List<ExtlibPluginConfig> getExtlibPluginConfigs() {
		if (plugins == null) {
			// List<ExtlibPluginConfig> _plugins = ExtensionManager.findServices(null,
			// ExtlibPluginConfig.class.getClassLoader(),
			// ExtlibPluginConfig.EXTENSION_NAME,
			// ExtlibPluginConfig.class);
			/*
			 * We are blocking any extension point contribution to the Library. Agreed to do this way on Sep 30th, 2011 [AC, MK, PR]
			 */
			List<ExtlibPluginConfig> _plugins = new ArrayList<ExtlibPluginConfig>();
			// note, sort these plugins alphabetically by className
			_plugins.add(new DominoConfig());
			plugins = _plugins;
		}
		return plugins;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.library.AbstractXspLibrary#getXspConfigFiles()
	 */
	@Override
	public String[] getXspConfigFiles() {
		String[] files = new String[] {};
		List<ExtlibPluginConfig> plugins = getExtlibPluginConfigs();
		for (ExtlibPluginConfig plugin : plugins) {
			files = plugin.getXspConfigFiles(files);
		}
		return files;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.library.XspLibrary#getLibraryId()
	 */
	@Override
	public String getLibraryId() {
		return LIBRARY_ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.library.AbstractXspLibrary#getPluginId()
	 */
	@Override
	public String getPluginId() {
		return Activator.PLUGIN_ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.library.AbstractXspLibrary#isGlobalScope()
	 */
	@Override
	public boolean isGlobalScope() {
		boolean result = isGlobal();
		// System.out.println(Activator.PLUGIN_ID + " global: " + String.valueOf(result));
		return result;
	}

}

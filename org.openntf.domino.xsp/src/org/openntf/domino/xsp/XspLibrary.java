package org.openntf.domino.xsp;

import java.util.ArrayList;
import java.util.List;

import org.openntf.domino.xsp.config.DominoConfig;

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
	 * 
	 */
	public XspLibrary() {
		System.out.println("Loading org.openntf.domino.xsp library");

	}

	@Override
	public String[] getDependencies() {
		return new String[] { "com.ibm.xsp.extlib.library" };
	}

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

	@Override
	public String[] getXspConfigFiles() {
		String[] files = new String[] {};
		List<ExtlibPluginConfig> plugins = getExtlibPluginConfigs();
		for (ExtlibPluginConfig plugin : plugins) {
			files = plugin.getXspConfigFiles(files);
			for (String file : files) {
				System.out.println(String.valueOf(file));
			}
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

	@Override
	public String getPluginId() {
		return Activator.PLUGIN_ID;
	}

	@Override
	public boolean isGlobalScope() {
		boolean result = isGlobal();
		// System.out.println(Activator.PLUGIN_ID + " global: " + String.valueOf(result));
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.library.AbstractXspLibrary#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return super.isEnabled();
	}

}

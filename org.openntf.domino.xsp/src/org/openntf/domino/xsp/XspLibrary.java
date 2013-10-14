package org.openntf.domino.xsp;

import com.ibm.xsp.library.AbstractXspLibrary;

/**
 * @author nfreeman
 * 
 */
public class XspLibrary extends AbstractXspLibrary {
	private final static String LIBRARY_ID = XspLibrary.class.getName();
	public final static String LIBRARY_BEAN_PREFIX = "org.openntf.domino.xsp";
	private static Boolean GLOBAL;

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
		return new String[] { "com.ibm.xsp.core.library", "com.ibm.xsp.extsn.library", "com.ibm.xsp.domino.library",
				"com.ibm.xsp.designer.library", "org.openntf.domino.plugin.library" };
	}

	@Override
	public String[] getFacesConfigFiles() {
		String[] files = new String[] { "domino-faces-config.xml" };
		return files;
	}

	@Override
	public String[] getXspConfigFiles() {
		String[] files = new String[] {};
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

}

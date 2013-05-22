/**
 * 
 */
package org.openntf.domino.xsp;

import com.ibm.xsp.library.AbstractXspLibrary;

/**
 * @author nfreeman
 * 
 */
public class XspLibrary extends AbstractXspLibrary {
	private final static String LIBRARY_ID = XspLibrary.class.getName();
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

	}

	@Override
	public String[] getFacesConfigFiles() {
		String[] files = new String[] { "META-INF/domino-faces-config.xml" };
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

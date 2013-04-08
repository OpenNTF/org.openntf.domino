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
		return true;
	}

}

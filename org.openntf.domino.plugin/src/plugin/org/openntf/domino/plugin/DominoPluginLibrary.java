package org.openntf.domino.plugin;

import com.ibm.xsp.library.AbstractXspLibrary;

/**
 * @author rpraml
 * 
 */
public class DominoPluginLibrary extends AbstractXspLibrary {
	static {
		System.out.println(DominoPluginLibrary.class + " loaded");
	}

	/**
	 * 
	 */
	public DominoPluginLibrary() {
		System.out.println("Loading org.openntf.domino.plugin library");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.library.XspLibrary#getLibraryId()
	 */
	@Override
	public String getLibraryId() {
		return "org.openntf.domino.plugin.library";
	}

	@Override
	public String getPluginId() {
		return "org.openntf.domino.plugin";
	}
}

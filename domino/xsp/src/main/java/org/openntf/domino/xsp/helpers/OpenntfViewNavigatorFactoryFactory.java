/**
 * 
 */
package org.openntf.domino.xsp.helpers;

import java.util.logging.Logger;

import org.openntf.domino.xsp.ODAPlatform;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.model.domino.ViewNavigatorEx;
import com.ibm.xsp.model.domino.ViewNavigatorFactory;
import com.ibm.xsp.model.domino.viewnavigator.FTViewNavigatorEx;
import com.ibm.xsp.model.domino.viewnavigator.NOIViewNavigatorEx;
import com.ibm.xsp.model.domino.viewnavigator.NOIViewNavigatorEx9;

/**
 * @author Nathan T. Freeman
 * 
 *         OpenntfViewNavigatorFactoryFactory class
 */
public class OpenntfViewNavigatorFactoryFactory implements ViewNavigatorFactory.Factory {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(OpenntfViewNavigatorFactoryFactory.class.getName());

	// static {
	// System.out.println("Loaded " + OpenntfViewNavigatorFactoryFactory.class.getName());
	// }

	/**
	 * Constructor
	 */
	public OpenntfViewNavigatorFactoryFactory() {
		// System.out.println(OpenntfViewNavigatorFactoryFactory.class.getName() + " constructed with no arguments.");
	}

	// public OpenntfViewNavigatorFactoryFactory(final ViewNavigatorFactory delegate) {
	// delegate_ = delegate;
	// System.out.println("OpenntfViewNavigatorFactoryFactory constructed with delegate!");
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.model.domino.ViewNavigatorFactory.Factory#createFactory(java.lang.String, java.lang.String)
	 */
	@Override
	public ViewNavigatorFactory createFactory(final String dbPath, final String viewName) {
		// System.out.println("OpenntfViewNavigatorFactoryFactory.createFactory called with arguments: " + paramString1 + " and "
		// + paramString2);
		return new OpenntfViewNavigatorFactory(dbPath, viewName);
	}

	/**
	 * OpenntfViewNavigatorFactory class
	 */
	public static class OpenntfViewNavigatorFactory extends ViewNavigatorFactory {
		private static final long serialVersionUID = 1L;
		@SuppressWarnings("unused")
		private final String dbPath_;
		@SuppressWarnings("unused")
		private final String viewName_;
		private String entrySearchString;

		/**
		 * Constructor
		 * 
		 * @param dbPath
		 *            String database path for which the load a navigator
		 * @param viewName
		 *            String view name
		 * @since org.openntf.domino.xsp 4.5.0
		 */
		public OpenntfViewNavigatorFactory(final String dbPath, final String viewName) {
			dbPath_ = dbPath;
			viewName_ = viewName;
		}

		@Override
		public String getFTSearch() {
			String superFT = super.getFTSearch();
			if (superFT != null && superFT.startsWith("searchInEntries:")) {
				setFTSearch("");
				entrySearchString = superFT.substring(16);
			}
			return superFT;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ibm.xsp.model.domino.ViewNavigatorFactory#findNavigatorType()
		 */
		@Override
		public int findNavigatorType() {
			// System.out.println("findNavigatorType requested");
			return 1337;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ibm.xsp.model.domino.ViewNavigatorFactory#createNavigator()
		 */
		@Override
		public ViewNavigatorEx createNavigator() {
			ViewNavigatorEx result = super.createNavigator();
			if (result instanceof FTViewNavigatorEx) {
				if (ODAPlatform.isAppGodMode(null) && StringUtil.isNotEmpty(entrySearchString)) {
					result = new OpenntfViewNavigatorEx(this, entrySearchString);
				}
			}
			if (result instanceof NOIViewNavigatorEx9 || result instanceof NOIViewNavigatorEx) {
				if (ODAPlatform.isAppGodMode(null)) {
					// FacesContext ctx = FacesContext.getCurrentInstance();
					// if (ctx instanceof DominoFacesContext) {
					// DominoFacesContext impl = (DominoFacesContext) ctx;
					// ApplicationEx ae = impl.getApplicationEx();
					// String persistMode = ae.getApplicationProperty("xsp.persistence.mode", null);
					// // System.out.println("Current persistence mode: " + persistMode);
					// StateManager sm = impl.getApplicationEx().getStateManager();
					// if (persistMode.equals("file")) {
					// System.out
					// .println("ALERT! This application is set to use file state management mode but also use the OpenNTF API in takeover mode, which can cause problems!");
					// System.out.println("Switch to 'keep current page in memory' to eliminate this message.");
					// // return result;
					// } else if (persistMode.equals("persistance")) { // persistence
					// System.out.println("This application is set to use persistence state management mode");
					// } else if (persistMode.equals("session")) { // session
					// System.out.println("This application is set to use session state management mode");
					// } else { // default
					// System.out.println("This application is set to use basic state management mode");
					// }
					// }
					result = new OpenntfViewNavigatorEx(this, entrySearchString);
				}
			} else {
				// System.out.println("returning a " + result.getClass().getName());
			}
			// if (!(result instanceof OpenntfViewNavigatorEx)) {
			// System.out.println("Returning a ViewNavigatorEx of type " + result.getClass().getName());
			// }
			return result;
		}

		// public OpenntfViewNavigatorFactory(ViewNavigatorFactory delegate) {
		// delegate_ =delegate;
		// System.out.println("OpenntfViewNavigatoryFactory constructed with delegate");
		// }

	}
}

/**
 * 
 */
package org.openntf.domino.xsp.helpers;

import java.util.logging.Logger;

import com.ibm.xsp.model.domino.ViewNavigatorEx;
import com.ibm.xsp.model.domino.ViewNavigatorFactory;
import com.ibm.xsp.model.domino.viewnavigator.NOIViewNavigatorEx;
import com.ibm.xsp.model.domino.viewnavigator.NOIViewNavigatorEx9;

/**
 * @author Nathan T. Freeman
 * 
 */
public class OpenntfViewNavigatorFactoryFactory implements ViewNavigatorFactory.Factory {
	private static final Logger log_ = Logger.getLogger(OpenntfViewNavigatorFactoryFactory.class.getName());
	private static final long serialVersionUID = 1L;

	// static {
	// System.out.println("Loaded " + OpenntfViewNavigatorFactoryFactory.class.getName());
	// }

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

	public static class OpenntfViewNavigatorFactory extends ViewNavigatorFactory {
		private final String dbPath_;
		private final String viewName_;

		public OpenntfViewNavigatorFactory(final String dbPath, final String viewName) {
			dbPath_ = dbPath;
			viewName_ = viewName;
		}

		@Override
		public int findNavigatorType() {
			// System.out.println("findNavigatorType requested");
			return 1337;
		}

		@Override
		public ViewNavigatorEx createNavigator() {
			ViewNavigatorEx result = super.createNavigator();
			if (result instanceof NOIViewNavigatorEx9 || result instanceof NOIViewNavigatorEx) {
				// System.out.println("Creating new OpenntfViewNavigatorEx");
				result = new OpenntfViewNavigatorEx(this);
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

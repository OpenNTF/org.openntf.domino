/**
 * 
 */
package org.openntf.domino.xsp.helpers;

import static org.openntf.domino.utils.DominoUtils.handleException;

import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import lotus.domino.NotesException;
import lotus.domino.Session;
import lotus.domino.View;
import lotus.domino.ViewColumn;
import lotus.domino.ViewEntry;

import org.openntf.domino.utils.Factory;

import com.ibm.xsp.model.domino.ViewNavigatorFactory;
import com.ibm.xsp.model.domino.viewnavigator.NOIViewNavigatorEx9;

/**
 * @author Nathan T. Freeman
 * 
 *         OpenntfViewNavigatorEx class to cope with JNI method RPr: JNI Methods are fixed, so this should not be neccessary any longer.
 */
public class OpenntfViewNavigatorEx extends NOIViewNavigatorEx9 {
	private static final long serialVersionUID = -5568170248903953533L;
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(OpenntfViewNavigatorEx.class.getName());

	/**
	 * Constructor
	 * 
	 * @param paramViewNavigatorFactory
	 *            ViewNavigatorFactory instance
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	public OpenntfViewNavigatorEx(final ViewNavigatorFactory paramViewNavigatorFactory) {
		super(paramViewNavigatorFactory);
		// System.out.println("New OpentfViewNavigatorEx constructed in request id "
		// + System.identityHashCode(FacesContext.getCurrentInstance()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.model.domino.viewnavigator.AbstractNavigator#initNavigator(lotus.domino.View)
	 */
	@SuppressWarnings({ "unchecked", "null" })
	@Override
	public void initNavigator(final View paramView) throws NotesException {
		// System.out.println("Initializing navigator for view " + paramView.getClass().getName() + ": " + paramView.getName()
		// + " in request id " + System.identityHashCode(FacesContext.getCurrentInstance()));
		super.initNavigator(paramView);

		Vector<ViewColumn> cols = paramView.getColumns();

		for (int i = 0; i < cols.size(); i++) {
			ViewColumn col = cols.get(i);
			org.openntf.domino.Session openNtfSession = null;

			if (col.isConstant()) {
				if (openNtfSession == null) {
					Session sess = paramView.getParent().getParent();
					if (sess instanceof org.openntf.domino.Session) {
						openNtfSession = (org.openntf.domino.Session) sess;
					} else {
						openNtfSession = Factory.fromLotus(sess, org.openntf.domino.Session.SCHEMA, null);
					}
				}
				openNtfSession.evaluate(col.getFormula());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.model.domino.viewnavigator.NOIViewNavigatorEx9#calculateExactCount(lotus.domino.View)
	 */
	@Override
	public int calculateExactCount(final View paramView) throws NotesException {
		//if (paramView instanceof org.openntf.domino.impl.View) {
		//paramView = org.openntf.domino.impl.Base.toLotus(paramView);
		try {
			paramView.setAutoUpdate(false);
		} catch (NotesException ne) {
			handleException(ne);
		}
		//}
		return super.calculateExactCount(paramView);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.model.domino.viewnavigator.NOIViewNavigatorEx9#hasMoreRows(lotus.domino.View, int)
	 */
	@Override
	public int hasMoreRows(final View paramView, final int paramInt) {
		//if (paramView instanceof org.openntf.domino.View) {
		//	paramView = org.openntf.domino.impl.Base.toLotus(paramView);
		try {
			paramView.setAutoUpdate(false);
		} catch (NotesException ne) {
			handleException(ne);
		}
		//}
		return super.hasMoreRows(paramView, paramInt);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.model.domino.viewnavigator.NOIViewNavigatorEx9#readEntries(lotus.domino.View, int, int)
	 */
	@Override
	public List<ViewEntry> readEntries(final View paramView, final int paramInt1, final int paramInt2) {
		// try {
		// System.out.println("Reading entries from view " + paramView.getName() + " from " + paramInt1 + " to " + paramInt2);
		// } catch (NotesException e) {
		// handleException(e);
		// }
		//if (paramView instanceof org.openntf.domino.View) {
		//RPR: if we unwrap the view here, the reference to org.openntf.domino.View gets overwritten and the View might get recycled
		//paramView = org.openntf.domino.impl.Base.toLotus(paramView);
		try {
			paramView.setAutoUpdate(false);
		} catch (NotesException ne) {
			handleException(ne);
		}
		//}
		return super.readEntries(paramView, paramInt1, paramInt2);
	}

	// 2014-07-16 The entry is removed completely as it will never work. See DominoViewEntryArray writeExternal/readExternal

	//	/**
	//	 * Entry class
	//	 */
	//	public static class Entry extends NOIViewNavigatorEx9.Entry {
	//		
	//	}

}

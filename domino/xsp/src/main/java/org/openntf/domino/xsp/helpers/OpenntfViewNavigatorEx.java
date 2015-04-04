/**
 * 
 */
package org.openntf.domino.xsp.helpers;

import static org.openntf.domino.utils.DominoUtils.handleException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import lotus.domino.NotesException;
import lotus.domino.View;
import lotus.domino.ViewEntry;

import com.ibm.designer.runtime.domino.bootstrap.util.StringUtil;
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
	private String entrySearchString;

	/**
	 * Constructor
	 * 
	 * @param paramViewNavigatorFactory
	 *            ViewNavigatorFactory instance
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	public OpenntfViewNavigatorEx(final ViewNavigatorFactory paramViewNavigatorFactory, final String entrySearchString) {
		super(paramViewNavigatorFactory);
		this.entrySearchString = entrySearchString;
	}

	/// RPr: Constant values are now handled in the {@link org.openntf.domino.impl.View.DominoColumnInfo} now
	//	/*
	//	 * (non-Javadoc)
	//	 * 
	//	 * @see com.ibm.xsp.model.domino.viewnavigator.AbstractNavigator#initNavigator(lotus.domino.View)
	//	 */
	//	@SuppressWarnings({ "unchecked", "null" })
	//	@Override
	//	public void initNavigator(final View paramView) throws NotesException {
	//		super.initNavigator(paramView);
	//
	//		Vector<ViewColumn> cols = paramView.getColumns();
	//
	//		for (int i = 0; i < cols.size(); i++) {
	//			ViewColumn col = cols.get(i);
	//			org.openntf.domino.Session openNtfSession = null;
	//
	//			if (col.isConstant()) {
	//				if (openNtfSession == null) {
	//					Session sess = paramView.getParent().getParent();
	//					if (sess instanceof org.openntf.domino.Session) {
	//						openNtfSession = (org.openntf.domino.Session) sess;
	//					} else {
	//						openNtfSession = Factory.fromLotus(sess, org.openntf.domino.Session.SCHEMA, null);
	//					}
	//				}
	//				openNtfSession.evaluate(col.getFormula());
	//			}
	//		}
	//	}

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
		List<ViewEntry> superList = super.readEntries(paramView, paramInt1, paramInt2);
		if (StringUtil.isEmpty(entrySearchString)) {
			return superList;
		}
		List<ViewEntry> ret = new ArrayList<ViewEntry>();
		Iterator<ViewEntry> it = superList.iterator();
		try {
			while (it.hasNext()) {
				ViewEntry entry = it.next();
				for (Object val : entry.getColumnValues()) {
					if (contains(val)) {
						ret.add(entry);
					}
				}
			}
		} catch (NotesException e) {
			throw new RuntimeException(e);
		}
		rowCount = ret.size();
		return ret;
	}

	protected boolean contains(final Object val) {
		if (val == null) {
			return false;
		}
		if (val instanceof String) {
			return ((String) val).contains(entrySearchString);
		} else if (val instanceof Vector) {
			for (Object v : (Vector<?>) val) {
				if (contains(v)) {
					return true;
				}
			}
		}
		return false;
	}

	// RPr: 2014-07-16 The entry is removed completely as it will never work. See DominoViewEntryArray writeExternal/readExternal

	//	/**
	//	 * Entry class
	//	 */
	//	public static class Entry extends NOIViewNavigatorEx9.Entry {
	//		
	//	}

}

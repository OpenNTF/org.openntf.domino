/**
 * 
 */
package org.openntf.domino.xsp.helpers;

import java.util.List;
import java.util.logging.Logger;

import lotus.domino.NotesException;
import lotus.domino.View;
import lotus.domino.ViewEntry;

import org.openntf.domino.utils.DominoUtils;

import com.ibm.xsp.model.domino.ViewNavigatorFactory;
import com.ibm.xsp.model.domino.viewnavigator.NOIViewNavigatorEx9;
import com.ibm.xsp.model.domino.viewnavigator.PathPosition;

/**
 * @author Nathan T. Freeman
 * 
 */
public class OpenntfViewNavigatorEx extends NOIViewNavigatorEx9 {
	private static final long serialVersionUID = -5568170248903953533L;
	private static final Logger log_ = Logger.getLogger(OpenntfViewNavigatorEx.class.getName());

	// static {
	// System.out.println("OpenntfViewNavigatorEx class loaded!");
	// }

	/**
	 * @param paramViewNavigatorFactory
	 */
	public OpenntfViewNavigatorEx(final ViewNavigatorFactory paramViewNavigatorFactory) {
		super(paramViewNavigatorFactory);
		// System.out.println("New OpentfViewNavigatorEx constructed");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.model.domino.viewnavigator.NOIViewNavigatorEx9#calculateExactCount(lotus.domino.View)
	 */
	@Override
	public int calculateExactCount(View paramView) throws NotesException {
		if (paramView instanceof org.openntf.domino.View) {
			paramView = (lotus.domino.View) org.openntf.domino.impl.Base.toLotus(paramView);
			try {
				paramView.setAutoUpdate(false);
			} catch (NotesException ne) {
				DominoUtils.handleException(ne);
			}
		}
		return super.calculateExactCount(paramView);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.model.domino.viewnavigator.NOIViewNavigatorEx9#hasMoreRows(lotus.domino.View, int)
	 */
	@Override
	public int hasMoreRows(View paramView, final int paramInt) {
		if (paramView instanceof org.openntf.domino.View) {
			paramView = (lotus.domino.View) org.openntf.domino.impl.Base.toLotus(paramView);
			try {
				paramView.setAutoUpdate(false);
			} catch (NotesException ne) {
				DominoUtils.handleException(ne);
			}
		}
		return super.hasMoreRows(paramView, paramInt);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.model.domino.viewnavigator.NOIViewNavigatorEx9#readEntries(lotus.domino.View, int, int)
	 */
	@Override
	public List<ViewEntry> readEntries(View paramView, final int paramInt1, final int paramInt2) {
		// System.out.println("Calling readEntries with a view parameter of type " + paramView.getClass().getName() + " and " + paramInt1
		// + ", " + paramInt2);
		if (paramView instanceof org.openntf.domino.View) {
			paramView = (lotus.domino.View) org.openntf.domino.impl.Base.toLotus(paramView);
			try {
				paramView.setAutoUpdate(false);
			} catch (NotesException ne) {
				DominoUtils.handleException(ne);
			}
		}
		return super.readEntries(paramView, paramInt1, paramInt2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.model.domino.ViewNavigatorEx#wrapViewEntry(lotus.domino.ViewEntry, java.lang.String, java.lang.String)
	 */
	@Override
	public ViewEntry wrapViewEntry(ViewEntry paramViewEntry, final String paramString1, final String paramString2) throws NotesException {
		if (paramViewEntry instanceof org.openntf.domino.ViewEntry) {
			paramViewEntry = (lotus.domino.ViewEntry) org.openntf.domino.impl.Base.toLotus(paramViewEntry);

		}
		return super.wrapViewEntry(paramViewEntry, paramString1, paramString2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.model.domino.viewnavigator.NOIViewNavigatorEx9#wrapViewEntry(lotus.domino.ViewEntry, java.lang.String,
	 * com.ibm.xsp.model.domino.viewnavigator.PathPosition)
	 */
	@Override
	public ViewEntry wrapViewEntry(ViewEntry paramViewEntry, final String paramString, final PathPosition paramPathPosition)
			throws NotesException {
		if (paramViewEntry instanceof org.openntf.domino.ViewEntry) {
			paramViewEntry = (lotus.domino.ViewEntry) org.openntf.domino.impl.Base.toLotus(paramViewEntry);

		}
		return super.wrapViewEntry(paramViewEntry, paramString, paramPathPosition);
	}

}

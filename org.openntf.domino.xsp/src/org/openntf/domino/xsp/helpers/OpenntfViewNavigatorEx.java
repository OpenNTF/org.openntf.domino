/**
 * 
 */
package org.openntf.domino.xsp.helpers;

import static org.openntf.domino.utils.DominoUtils.handleException;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import lotus.domino.NotesException;
import lotus.domino.Session;
import lotus.domino.View;
import lotus.domino.ViewColumn;
import lotus.domino.ViewEntry;
import lotus.domino.ViewNavigator;

import com.ibm.xsp.model.domino.DominoViewDataContainer;
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
	private String[] constantValues;

	/**
	 * @param paramViewNavigatorFactory
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
	@Override
	public void initNavigator(final View paramView) throws NotesException {
		// System.out.println("Initializing navigator for view " + paramView.getClass().getName() + ": " + paramView.getName()
		// + " in request id " + System.identityHashCode(FacesContext.getCurrentInstance()));
		super.initNavigator(paramView);

		Session sess = paramView.getParent().getParent();
		Vector<ViewColumn> cols = paramView.getColumns();
		constantValues = new String[cols.size()];

		for (int i = 0; i < cols.size(); i++) {
			ViewColumn col = cols.get(i);
			if (col.isConstant()) {
				Vector v = sess.evaluate(col.getFormula());
				constantValues[i] = v.get(0).toString();
			}
		}
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
				handleException(ne);
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
				handleException(ne);
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
		// try {
		// System.out.println("Reading entries from view " + paramView.getName() + " from " + paramInt1 + " to " + paramInt2);
		// } catch (NotesException e) {
		// handleException(e);
		// }
		if (paramView instanceof org.openntf.domino.View) {
			paramView = (lotus.domino.View) org.openntf.domino.impl.Base.toLotus(paramView);
			try {
				paramView.setAutoUpdate(false);
			} catch (NotesException ne) {
				handleException(ne);
			}
		}
		return super.readEntries(paramView, paramInt1, paramInt2);
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
		// return super.wrapViewEntry(paramViewEntry, paramString, paramPathPosition);

		Object parent = paramViewEntry.getParent();
		if (parent instanceof View) {
			// System.out.println("Wrapping a ViewEntry with param " + paramString + " from view "
			// + ((View) paramViewEntry.getParent()).getName() + " in position " + paramPathPosition.getViewFullPosition()
			// + " and indent " + this.offsetColumnIndent);
		} else if (parent instanceof ViewNavigator) {
			// System.out.println("Wrapping a ViewEntry with param " + paramString + " from view nav "
			// + ((ViewNavigator) paramViewEntry.getParent()).getParentView().getName() + " in position "
			// + paramPathPosition.getViewFullPosition() + " and indent " + this.offsetColumnIndent);
		}
		return new Entry(paramViewEntry, paramString, paramPathPosition, this.offsetColumnIndent, this);
	}

	public static class Entry extends NOIViewNavigatorEx9.Entry {
		private static final long serialVersionUID = 1L;
		private OpenntfViewNavigatorEx navigatorEx;

		public Entry() {

		}

		public Entry(final ViewEntry paramViewEntry, final String paramString, final PathPosition paramPathPosition, final int paramInt,
				final OpenntfViewNavigatorEx openntfViewNavigatorEx) throws NotesException {
			super(paramViewEntry, paramString, paramPathPosition, paramInt);
			navigatorEx = openntfViewNavigatorEx;
		}

		private static String getColumnValuesDump(final Entry entry) {
			Vector v = entry._columnValuesEx;
			StringBuilder sb = new StringBuilder();
			sb.append("size: " + v.size());
			sb.append(", [");
			for (int i = 0; i < v.size(); i++) {
				Object value = v.get(i);
				if (value == null) {
					sb.append(i + ": " + "null");
				} else {
					sb.append(i + ": " + (value instanceof Vector ? String.valueOf(((Vector) value).get(0)) + "[]" : String.valueOf(value)));
				}
				sb.append(",");
			}
			sb.append("]");

			return sb.toString();
		}

		@Override
		public Vector getColumnValuesEx() throws NotesException {
			if (this._columnValuesEx != null) {
				return this._columnValuesEx;
			}

			this._columnValuesEx = getJavaColumnValues();
			DominoViewDataContainer container = this._viewDataModel.getDominoViewDataContainer();

			if (this._columnValuesEx.size() < container.getColumnCount()) {
				int i = container.getColumnCount();
				int j = i - this._columnValuesEx.size();
				this._columnValuesEx.setSize(i);
				for (int k = 0; k < i; ++k) {
					int l = container.getColumnValuesIndex(k);
					if (l == 65535) {
						this._columnValuesEx.add(k, navigatorEx.constantValues[k]);
						--j;
					}
					if (j == 0)
						break;
				}
				this._columnValuesEx.setSize(i); // trim to the correct size
			}
			return this._columnValuesEx;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ibm.xsp.model.domino.wrapped.DominoViewEntry#readExternal(java.io.ObjectInput)
		 */
		@Override
		public void readExternal(final ObjectInput paramObjectInput) throws IOException, ClassNotFoundException {
			// System.out.println("Reading externalized OpenntfViewNavigatorEx.Entry");
			super.readExternal(paramObjectInput);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ibm.xsp.model.domino.wrapped.DominoViewEntry#writeExternal(java.io.ObjectOutput)
		 */
		@Override
		public void writeExternal(final ObjectOutput paramObjectOutput) throws IOException {
			// System.out.println("Writing externalized OpenntfViewNavigatorEx.Entry");
			super.writeExternal(paramObjectOutput);
		}
	}

}

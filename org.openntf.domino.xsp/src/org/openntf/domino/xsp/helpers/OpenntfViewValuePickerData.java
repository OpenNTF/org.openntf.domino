package org.openntf.domino.xsp.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import lotus.domino.Database;
import lotus.domino.NotesException;
import lotus.domino.View;
import lotus.domino.ViewColumn;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;
import lotus.domino.ViewNavigator;

import org.openntf.domino.utils.Factory;

import com.ibm.commons.Platform;
import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.extlib.component.picker.data.DominoViewValuePickerData;
import com.ibm.xsp.extlib.component.picker.data.EmptyPickerResult;
import com.ibm.xsp.extlib.component.picker.data.IPickerEntry;
import com.ibm.xsp.extlib.component.picker.data.IPickerOptions;
import com.ibm.xsp.extlib.component.picker.data.IPickerResult;
import com.ibm.xsp.extlib.domino.ExtlibDominoLogger;
import com.ibm.xsp.model.domino.DominoUtils;
import com.ibm.xsp.model.domino.wrapped.DominoViewEntry;

public class OpenntfViewValuePickerData extends DominoViewValuePickerData {

	public OpenntfViewValuePickerData() {
		// TODO Auto-generated constructor stub
	}

	public class _EntryMetaData extends EntryMetaData {
		private int valueIndex;
		private int labelIndex;
		private String[] attributeNames;
		private int[] attributeIndexes;

		@SuppressWarnings("unchecked")
		// $NON-NLS-1$
		protected _EntryMetaData(final IPickerOptions options) throws NotesException {
			super(options);

			Vector<ViewColumn> vc = (Vector<ViewColumn>) getView().getColumns();

			// Look for the key column
			if ((valueIndex = findSortColumnIndex(vc)) < 0) {
				throw new FacesExceptionEx(null, "Cannot find a value column in the view {0}", getView().getName()); // $NLX-DominoViewPickerData.Cannotfindavaluecolumnintheview0-1$
			}

			// Look for the label column
			String labelName = getLabelColumn();
			if (StringUtil.isNotEmpty(labelName)) {
				if ((labelIndex = findColumnIndex(vc, labelName)) < 0) {
					throw new FacesExceptionEx(null, "Cannot find label column {0}", labelName); // $NLX-DominoViewPickerData.Cannotfindlabelcolumn0-1$
				}
			} else {
				labelIndex = -1;
			}

			// // Look for the view attributes
			// this.attributeNames = attributeNames;
			// if(attributeNames!=null) {
			// int sz = attributeNames.length;
			// this.attributeIndexes = new int[sz];
			// for(int i=0; i<sz; i++) {
			// if( (attributeIndexes[i] = findColumnIndex(vc, attributeNames[i]))<0) {
			// throw new FacesExceptionEx(null,"Cannot find attributes column {0}",attributeNames[i]);
			// }
			// }
			// }
		}

		@Override
		protected Entry createEntry(final ViewEntry ve) throws NotesException {
			return new _Entry(this, ve);
		}

		@Override
		protected View openView() throws NotesException {
			Database db = DominoUtils.openDatabaseByName(getDatabaseName());
			View view = db.getView(getViewName());
			String labelName = getLabelColumn();
			if (StringUtil.isNotEmpty(labelName)) {
				try {
					view.resortView(labelName, true);
				} catch (NotesException ex) {
					// We can't resort the view so we silently fail
					// We just report it to the console
					if (ExtlibDominoLogger.DOMINO.isWarnEnabled()) {
						ExtlibDominoLogger.DOMINO.warnp(this, "openView", ex, //$NON-NLS-1$ 
								StringUtil.format("The view {0} needs the column {1} to be sortable for the value picker to be searchable",
										getViewName(), labelName)); // $NLW-DominoViewPickerData_ValuePickerNotSearchable_UnsortableColumn-1$
					}
				}
			}
			return view;
		}

	}

	public class _Entry extends Entry {
		private Object[] attributes;

		protected _Entry(final EntryMetaData metaData, final ViewEntry ve) throws NotesException {
			super(metaData, ve);

			// // And the extra attributes
			// if(metaData.attributeIndexes!=null) {
			// int ac = metaData.attributeIndexes.length;
			// this.attributes = new Object[ac];
			// for(int i=0; i<ac; i++) {
			// attributes[i] = columnValues.get(metaData.attributeIndexes[i]);
			// }
			// }
		}

		@Override
		public _EntryMetaData getMetaData() {
			return (_EntryMetaData) super.getMetaData();
		}

		@Override
		protected Object readValue(final ViewEntry ve, final Vector<Object> columnValues) throws NotesException {
			int idx = getMetaData().valueIndex;
			return idx >= 0 ? columnValues.get(idx) : null;
		}

		@Override
		protected Object readLabel(final ViewEntry ve, final Vector<Object> columnValues) throws NotesException {
			int idx = getMetaData().labelIndex;
			return idx >= 0 ? columnValues.get(idx) : null;
		}

		@Override
		protected Object[] readAttributes(final ViewEntry ve, final Vector<Object> columnValues) throws NotesException {
			return null;
		}
	}

	// ====================================================================
	// Data access implementation
	// ====================================================================

	public abstract static class EntryMetaData {
		private View view;
		private IPickerOptions options;

		protected EntryMetaData(final IPickerOptions options) throws NotesException {
			this.options = options;
			this.view = openView();
		}

		public View getView() {
			return view;
		}

		public IPickerOptions getOptions() {
			return options;
		}

		protected int findSortColumnIndex(final Vector<ViewColumn> vc) throws NotesException {
			int fc = -1;
			// Find the first sorted column
			int nc = vc.size();
			for (int i = 0; i < nc; i++) {
				ViewColumn c = vc.get(i);
				if (c.isSorted()) {
					return i;
				}
				if (fc < 0 && c.getColumnValuesIndex() != DominoViewEntry.VC_NOT_PRESENT) {
					fc = i;
				}
			}
			// Else, return the first column
			return fc;
		}

		protected int findColumnIndex(final Vector<ViewColumn> vc, final String name) throws NotesException {
			int nc = vc.size();
			// Look for a programmatic name first
			for (int i = 0; i < nc; i++) {
				if (StringUtil.equalsIgnoreCase(vc.get(i).getItemName(), name)) {
					return i;
				}
			}
			// Then default to the title
			for (int i = 0; i < nc; i++) {
				if (StringUtil.equalsIgnoreCase(vc.get(i).getTitle(), name)) {
					return i;
				}
			}
			return -1;
		}

		protected abstract View openView() throws NotesException;

		protected abstract Entry createEntry(ViewEntry ve) throws NotesException;
	}

	public abstract static class Entry implements IPickerEntry {
		private EntryMetaData metaData;
		private Object value;
		private Object label;
		private Object[] attributes;

		@SuppressWarnings("unchecked")
		// $NON-NLS-1$
		protected Entry(final EntryMetaData metaData, final ViewEntry ve) throws NotesException {
			this.metaData = metaData;
			// Read the values from the view entry
			Vector<Object> columnValues = ve.getColumnValues();

			// Read the value
			this.value = readValue(ve, columnValues);

			// Read the label
			this.label = readLabel(ve, columnValues);

			// Read the extra attributes
			this.attributes = readAttributes(ve, columnValues);
		}

		public EntryMetaData getMetaData() {
			return metaData;
		}

		public Object getValue() {
			return value;
		}

		public Object getLabel() {
			return label;
		}

		public int getAttributeCount() {
			return 0;
		}

		public String getAttributeName(final int index) {
			return null;
		}

		public Object getAttributeValue(final int index) {
			return attributes[index];
		}

		protected abstract Object readValue(ViewEntry ve, Vector<Object> columnValues) throws NotesException;

		protected abstract Object readLabel(ViewEntry ve, Vector<Object> columnValues) throws NotesException;

		protected abstract Object[] readAttributes(ViewEntry ve, Vector<Object> columnValues) throws NotesException;
	}

	public static class Result implements IPickerResult {
		private List<IPickerEntry> entries;
		private int count;

		protected Result(final List<IPickerEntry> entries, final int count) {
			this.entries = entries;
			this.count = count;
		}

		public List<IPickerEntry> getEntries() {
			return entries;
		}

		public int getTotalCount() {
			return count;
		}
	}

	@Override
	public IPickerResult readEntries(final IPickerOptions options) {
		try {
			EntryMetaData meta = new _EntryMetaData(options);
			View view = meta.getView();
			view.setAutoUpdate(false);
			try {
				ArrayList<IPickerEntry> entries = new ArrayList<IPickerEntry>();

				int start = options.getStart();
				int count = options.getCount();
				String key = options.getKey();
				String _startKey = options.getStartKey();
				if (StringUtil.isNotEmpty(_startKey)) {
					key = _startKey;
				}

				String searchType = getSearchType();
				if (StringUtil.isEmpty(searchType)) {
					searchType = SEARCH_STARTFROM;
				}

				if (StringUtil.equals(searchType, SEARCH_MATCH)) {
					ViewEntryCollection vc = view.getAllEntriesByKey(key);
					ViewEntry ve = start > 0 ? vc.getNthEntry(start) : vc.getFirstEntry();
					for (int i = 0; i < count && ve != null; i++) {
						entries.add(meta.createEntry(ve));
						ve = vc.getNextEntry(ve);
					}
					int nEntries = vc.getCount();
					return new Result(entries, nEntries);
				}
				if (StringUtil.equals(searchType, SEARCH_FTSEARCH)) {
					applyFTSearch(options, view, key);
					ViewEntryCollection vc = view.getAllEntries();
					ViewEntry ve = start > 0 ? vc.getNthEntry(start) : vc.getFirstEntry();
					for (int i = 0; i < count && ve != null; i++) {
						entries.add(meta.createEntry(ve));
						ve = vc.getNextEntry(ve);
					}
					int nEntries = vc.getCount();
					return new Result(entries, nEntries);
				} else {
					ViewNavigator nav = view.createViewNav();
					try {
						ViewEntry ve = null;
						if (key != null) {
							int searchOptions = DominoUtils.FIND_GREATER_THAN | DominoUtils.FIND_EQUAL | DominoUtils.FIND_PARTIAL
									| DominoUtils.FIND_CASE_INSENSITIVE;
							ve = DominoUtils.getViewEntryByKeyWithOptions(Factory.toLotus(view), key, searchOptions);
						} else {
							ve = nav.getCurrent();
						}
						if (start > 0) {
							if (nav.skip(start) != start) {
								// ok not all of them are skipped, stop the
								// process
								count = 0;
							}
						}
						for (int i = 0; i < count && ve != null; i++) {
							entries.add(meta.createEntry(ve));
							ve = nav.getNext(ve);
						}

						int nEntries = -1;
						return new Result(entries, nEntries);
					} finally {
						nav.recycle();
					}
				}
			} finally {
				// Recycle the view?
			}
		} catch (Exception ex) {
			Platform.getInstance().log(ex);
			// Swallow the exception for the end user and return an empty picker
			return new EmptyPickerResult();
		}
	}

}

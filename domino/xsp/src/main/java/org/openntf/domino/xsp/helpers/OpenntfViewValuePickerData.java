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

/**
 * @author withersp
 * 
 *         OpenNTF extension to DominoViewValuePickerData, required because DominoUtils.getViewEntryByKeyWithOptions() crashes the server if
 *         anything other than a lotus.domino.View is passed to it
 */
public class OpenntfViewValuePickerData extends DominoViewValuePickerData {

	/**
	 * Constructor
	 */
	public OpenntfViewValuePickerData() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * _EntryMetaData class, required because the methods of
	 * {@link com.ibm.xsp.extlib.component.picker.data.DominoViewPickerData._EntryMetaData} are 'protected', so not accessible from here
	 * 
	 * This class is an exact duplicate of that class
	 */
	public class _EntryMetaData extends EntryMetaData {
		private int valueIndex;
		private int labelIndex;
		private String[] attributeNames;
		private int[] attributeIndexes;

		/**
		 * Constructor, passing in options for getting a subset of the lookup values, e.g. start, typeahead key, search key, count etc.
		 * 
		 * @param options
		 *            IPickerOptions to refine lookup values
		 * @throws NotesException
		 */
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

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.openntf.domino.xsp.helpers.OpenntfViewValuePickerData.EntryMetaData#createEntry(lotus.domino.ViewEntry)
		 */
		@Override
		protected Entry createEntry(final ViewEntry ve) throws NotesException {
			return new _Entry(this, ve);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.openntf.domino.xsp.helpers.OpenntfViewValuePickerData.EntryMetaData#openView()
		 */
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

	/**
	 * _Entry class, required because the methods of {@link com.ibm.xsp.extlib.component.picker.data.DominoViewPickerData._Entry} are
	 * 'protected', so not accessible from here
	 * 
	 * This class is an exact duplicate of that class
	 */
	public class _Entry extends Entry {
		private Object[] attributes;

		/**
		 * Constructor, passing in the EntryMetaData object for the ViewEntry and the ViewEntry itself
		 * 
		 * @param metaData
		 *            EntryMetaData to access the view, its design and the search options
		 * @param ve
		 *            ViewEntry being iterated
		 * @throws NotesException
		 */
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

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.openntf.domino.xsp.helpers.OpenntfViewValuePickerData.Entry#getMetaData()
		 */
		@Override
		public _EntryMetaData getMetaData() {
			return (_EntryMetaData) super.getMetaData();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.openntf.domino.xsp.helpers.OpenntfViewValuePickerData.Entry#readValue(lotus.domino.ViewEntry, java.util.Vector)
		 */
		@Override
		protected Object readValue(final ViewEntry ve, final Vector<Object> columnValues) throws NotesException {
			int idx = getMetaData().valueIndex;
			return idx >= 0 ? columnValues.get(idx) : null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.openntf.domino.xsp.helpers.OpenntfViewValuePickerData.Entry#readLabel(lotus.domino.ViewEntry, java.util.Vector)
		 */
		@Override
		protected Object readLabel(final ViewEntry ve, final Vector<Object> columnValues) throws NotesException {
			int idx = getMetaData().labelIndex;
			return idx >= 0 ? columnValues.get(idx) : null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.openntf.domino.xsp.helpers.OpenntfViewValuePickerData.Entry#readAttributes(lotus.domino.ViewEntry, java.util.Vector)
		 */
		@Override
		protected Object[] readAttributes(final ViewEntry ve, final Vector<Object> columnValues) throws NotesException {
			return null;
		}
	}

	// ====================================================================
	// Data access implementation
	//
	// Static abstract class for EntryMetaData, extended by _EntryMetaData
	// ====================================================================

	public abstract static class EntryMetaData {
		private View view;
		private IPickerOptions options;

		/**
		 * Constructor loading in the options
		 * 
		 * @param options
		 *            IPickerOptions to refine lookup values
		 * @throws NotesException
		 */
		protected EntryMetaData(final IPickerOptions options) throws NotesException {
			this.options = options;
			this.view = openView();
		}

		/**
		 * Getter for the view property
		 * 
		 * @return View underlying view the ViewEntry is for
		 */
		public View getView() {
			return view;
		}

		/**
		 * Getter for the options property
		 * 
		 * @return IPickerOptions containing e.g. start, source, count, key (typeahead key), startKey (search key)
		 */
		public IPickerOptions getOptions() {
			return options;
		}

		/**
		 * Gets the first sorted column's index from the View
		 * 
		 * @param vc
		 *            Vector<ViewColumn> pulled from the design of the View
		 * @return int first sorted column, starting at 0
		 * @throws NotesException
		 */
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

		/**
		 * Finds the column index for a specific column name searching on:
		 * <ul>
		 * <li>Programmatic name (case insensitive)</li>
		 * <li>Column title</li>
		 * </ul>
		 * 
		 * @param vc
		 *            Vector<ViewColumn> pulled from the design of the View
		 * @param name
		 *            String programmatic name or column title
		 * @return int index of relevant column, starting at 0. -1 if not found.
		 * @throws NotesException
		 */
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

		/**
		 * Opens the underlying View
		 * 
		 * @return View the ViewEntry is from
		 * @throws NotesException
		 */
		protected abstract View openView() throws NotesException;

		/**
		 * Creates a new Entry object from the ViewEntry
		 * 
		 * @param ve
		 *            ViewEntry being passed
		 * @return Entry object based on the ViewEntry
		 * @throws NotesException
		 */
		protected abstract Entry createEntry(ViewEntry ve) throws NotesException;
	}

	/**
	 * Static abstract class based on IPickerEntry interface, further implemented by _Entry
	 */
	public abstract static class Entry implements IPickerEntry {
		private EntryMetaData metaData;
		private Object value;
		private Object label;
		private Object[] attributes;

		/**
		 * Creates a new Entry object
		 * 
		 * @param metaData
		 *            EntryMetaData to access the view, its design and the search options
		 * @param ve
		 *            ViewEntry being iterated
		 * @throws NotesException
		 */
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

		/**
		 * Getter to access the EntryMetaData object loaded in
		 * 
		 * @return EntryMetaData
		 */
		public EntryMetaData getMetaData() {
			return metaData;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ibm.xsp.extlib.component.picker.data.IPickerEntry#getValue()
		 */
		public Object getValue() {
			return value;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ibm.xsp.extlib.component.picker.data.IPickerEntry#getLabel()
		 */
		public Object getLabel() {
			return label;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ibm.xsp.extlib.component.picker.data.IPickerEntry#getAttributeCount()
		 */
		public int getAttributeCount() {
			return 0;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ibm.xsp.extlib.component.picker.data.IPickerEntry#getAttributeName(int)
		 */
		public String getAttributeName(final int index) {
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ibm.xsp.extlib.component.picker.data.IPickerEntry#getAttributeValue(int)
		 */
		public Object getAttributeValue(final int index) {
			return attributes[index];
		}

		/**
		 * Reads the value to store from the picker
		 * 
		 * @param ve
		 *            ViewEntry being read
		 * @param columnValues
		 *            Vector<Object> of column values for the ViewEntry
		 * @return Object value to be stored
		 * @throws NotesException
		 */
		protected abstract Object readValue(ViewEntry ve, Vector<Object> columnValues) throws NotesException;

		/**
		 * Reads the label to display in the picker
		 * 
		 * @param ve
		 *            ViewEntry being read
		 * @param columnValues
		 *            Vector<Object> of column values for the ViewEntry
		 * @return Object label to display
		 * @throws NotesException
		 */
		protected abstract Object readLabel(ViewEntry ve, Vector<Object> columnValues) throws NotesException;

		/**
		 * Reads the attributes for the ViewEntry. In _Entry this returns null
		 * 
		 * @param ve
		 *            ViewEntry being read
		 * @param columnValues
		 *            Vector<Object> of column values for the ViewEntry
		 * @return Object[] of attributes
		 * @throws NotesException
		 */
		protected abstract Object[] readAttributes(ViewEntry ve, Vector<Object> columnValues) throws NotesException;
	}

	/**
	 * Static abstract class based on IPickerResult interface containing the entries to return and the count
	 */
	public static class Result implements IPickerResult {
		private List<IPickerEntry> entries;
		private int count;

		/**
		 * Constructor, passing in entries to display and the count
		 * 
		 * @param entries
		 *            List<IPickerEntry> of options to display
		 * @param count
		 *            int number of results
		 */
		protected Result(final List<IPickerEntry> entries, final int count) {
			this.entries = entries;
			this.count = count;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ibm.xsp.extlib.component.picker.data.IPickerResult#getEntries()
		 */
		public List<IPickerEntry> getEntries() {
			return entries;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ibm.xsp.extlib.component.picker.data.IPickerResult#getTotalCount()
		 */
		public int getTotalCount() {
			return count;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibm.xsp.extlib.component.picker.data.AbstractDominoViewPickerData#readEntries(com.ibm.xsp.extlib.component.picker.data.IPickerOptions
	 * )
	 */
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
							// This is the one line that's different
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

/**
 * 
 */
package org.openntf.domino.xsp.helpers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import lotus.domino.Database;
import lotus.domino.NotesException;
import lotus.domino.Session;
import lotus.domino.View;
import lotus.domino.ViewColumn;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;
import lotus.domino.ViewNavigator;

import org.openntf.arpa.NamePartsMap;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.utils.Names;

import com.ibm.commons.Platform;
import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.acl.NoAccessSignal;
import com.ibm.xsp.extlib.component.picker.data.DominoNABNamePickerData;
import com.ibm.xsp.extlib.component.picker.data.EmptyPickerResult;
import com.ibm.xsp.extlib.component.picker.data.IPickerEntry;
import com.ibm.xsp.extlib.component.picker.data.IPickerOptions;
import com.ibm.xsp.extlib.component.picker.data.IPickerResult;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.model.domino.DominoUtils;
//import com.ibm.xsp.model.domino.wrapped.DominoDocument;
import com.ibm.xsp.model.domino.wrapped.DominoViewEntry;

/**
 * @author withersp
 * 
 *         OpenntfNABNamePickerData, for use with the NamePicker control
 */
@SuppressWarnings("javadoc")
public class OpenntfNABNamePickerData extends DominoNABNamePickerData {

	private String addressBookSel;
	private String addressBookDb;
	private String nameList;
	private Boolean people;
	private Boolean groups;
	private String returnNameFormat;
	private NamePartsMap.Key returnNameFormatAsKey;

	/**
	 * Constructor
	 */
	public OpenntfNABNamePickerData() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Gets the format a name should be returned as, using returnNameFormat property
	 * 
	 * @return String name in specific format
	 * @since org.openntf.domino.xsp 5.0.0
	 */
	public String getReturnNameFormat() {
		if (null != this.returnNameFormat) {
			if (null == this.returnNameFormatAsKey) {
				for (NamePartsMap.Key nameFormat : NamePartsMap.Key.values()) {
					if (this.returnNameFormat.equals(nameFormat.name())) {
						setReturnNameFormatAsKey(nameFormat);
					}
				}
			}
			return this.returnNameFormat;
		}
		ValueBinding _vb = getValueBinding("returnNameFormat"); //$NON-NLS-1$
		if (_vb != null) {
			String passedVal = (String) _vb.getValue(getFacesContext());
			for (NamePartsMap.Key nameFormat : NamePartsMap.Key.values()) {
				if (passedVal.equals(nameFormat.name())) {
					setReturnNameFormatAsKey(nameFormat);
				}
			}
		}
		return null;
	}

	/**
	 * Loads the return name format
	 * 
	 * @param returnNameFormat
	 *            String
	 * @since org.openntf.domino.xsp 5.0.0
	 */
	public void setReturnNameFormat(final String returnNameFormat) {
		this.returnNameFormat = returnNameFormat;
	}

	/**
	 * Gets the return name format as a {@link NamePartsMap.Key}
	 * 
	 * @return NamePartsMap.Key
	 * @since org.openntf.domino.xsp 5.0.0
	 */
	public NamePartsMap.Key getReturnNameFormatAsKey() {
		return returnNameFormatAsKey;
	}

	/**
	 * Loads the return name format as a {@link NamePartsMap.Key}
	 * 
	 * @param returnNameFormatAsKey
	 *            NamePartsMap.Key
	 * @since org.openntf.domino.xsp 5.0.0
	 */
	public void setReturnNameFormatAsKey(final NamePartsMap.Key returnNameFormatAsKey) {
		this.returnNameFormatAsKey = returnNameFormatAsKey;
	}

	/**
	 * @author withersp
	 * 
	 *         NabDb class for access to a specific database
	 */
	private static class NABDb implements Serializable { // Serializable because it goes to a scope
		private static final long serialVersionUID = 1L;
		String name;
		@SuppressWarnings("unused")
		String title;
		boolean publicNab;
		boolean privateNab;

		/**
		 * Constructor, passing a lotus.domino.Database object
		 * 
		 * @param db
		 *            lotus.domino.Database to load
		 * @throws NotesException
		 *             error
		 * @since org.openntf.domino 4.5.0
		 */
		NABDb(final Database db) throws NotesException {
			this(db.getFilePath(), db.getTitle());
			this.publicNab = db.isPublicAddressBook();
			this.privateNab = db.isPrivateAddressBook();
		}

		/**
		 * Constructor, passing a database filepath and title
		 * 
		 * @param name
		 *            String address book filepath
		 * @param title
		 *            String database title
		 * @throws NotesException
		 * @since org.openntf.domino.xsp 4.5.0
		 */
		NABDb(final String name, final String title) throws NotesException {
			this.name = name;
			this.title = title;
			if (StringUtil.isEmpty(title)) {
				this.title = name;
			}
		}
	}

	/**
	 * @author withersp
	 * 
	 *         EntryMetaData, copied from DominoNABNamePickerData
	 */
	public abstract class _EntryMetaData extends EntryMetaData {
		/**
		 * Constructor, loading picker options
		 * 
		 * @param options
		 * @throws NotesException
		 */
		public _EntryMetaData(final IPickerOptions options) throws NotesException {
			super(options);
		}

		/**
		 * Gets the view name to use
		 * 
		 * @return String view name
		 */
		public abstract String getViewName();

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.openntf.domino.xsp.helpers.OpenntfNABNamePickerData.EntryMetaData#openView()
		 */
		@Override
		public View openView() throws NotesException {
			// Find the database
			Database nabDb = findNAB();
			if (nabDb == null) {
				throw new FacesExceptionEx(null, "Not able to find a valid address book for the name picker"); // $NLX-DominoNABNamePickerData.Notabletofindavalidaddressbookfor-1$
			}
			// Find the view
			String viewName = getViewName();
			if (StringUtil.isEmpty(viewName)) {
				throw new FacesExceptionEx(null, "Not able to find a view in the address book that matches the selection criterias"); // $NLX-DominoNABNamePickerData.Notabletofindaviewintheaddressboo-1$
			}

			View view = nabDb.getView(viewName);
			return view;
		}

		/**
		 * Finds an address book for the current session, based on properties of the Name Picker
		 * 
		 * @return lotus.domino.Database NAB to display
		 * @throws NotesException
		 */
		protected Database findNAB() throws NotesException {
			String sel = getAddressBookSel();

			// Assume the first one for now - should be extended in the future
			IPickerOptions o = getOptions();
			int source = o != null ? o.getSource() : 0;

			NABDb[] sessNabs = getSessionAddressBooks();
			if (sessNabs != null && sessNabs.length > 0) {
				if (StringUtil.isEmpty(sel) || sel.equals(NAB_ALL)) {
					return DominoUtils.openDatabaseByName(sessNabs[source].name);
				} else if (sel.equals(NAB_ALLPUBLIC)) {
					int cpt = 0;
					for (int i = 0; i < sessNabs.length; i++) {
						if (sessNabs[i].publicNab) {
							if (source == cpt++) {
								return DominoUtils.openDatabaseByName(sessNabs[i].name);
							}
						}
					}
				} else if (sel.equals(NAB_ALLPRIVATE)) {
					int cpt = 0;
					for (int i = 0; i < sessNabs.length; i++) {
						if (sessNabs[i].privateNab) {
							if (source == cpt++) {
								return DominoUtils.openDatabaseByName(sessNabs[i].name);
							}
						}
					}
				} else if (sel.equals(NAB_FIRST)) {
					if (sessNabs.length > 0) {
						return DominoUtils.openDatabaseByName(sessNabs[0].name);
					}
				} else if (sel.equals(NAB_FIRST)) {
					for (int i = 0; i < sessNabs.length; i++) {
						if (sessNabs[i].publicNab) {
							return DominoUtils.openDatabaseByName(sessNabs[i].name);
						}
					}
				} else if (sel.equals(NAB_DATABASENAME)) {
					return DominoUtils.openDatabaseByName(getAddressBookDb());
				} else {
					throw new FacesExceptionEx(null, "Unknown address book selection type {0}", sel); // $NLX-DominoNABNamePickerData.Unknownaddressbookselectiontype0.1-1$
				}
			} else {
				// If no NAB is avail, request authentication
				// We force the authetication here, but it does not work outside of basic authentication
				// Moreover, the page is not refreshed afterwards to show the new user.
				throw new NoAccessSignal();
			}

			return null;
		}

	}

	/**
	 * @author withersp
	 * 
	 *         Picker result class, copied from DominoNABNamePickerData
	 */
	public static class Result implements IPickerResult {
		private List<IPickerEntry> entries;
		private int count;

		/**
		 * Constructor
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
		@Override
		public List<IPickerEntry> getEntries() {
			return entries;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ibm.xsp.extlib.component.picker.data.IPickerResult#getTotalCount()
		 */
		@Override
		public int getTotalCount() {
			return count;
		}
	}

	private static final String KEY_NABS = "extlib.pickers.domino.nabs"; //$NON-NLS-1$ 

	/**
	 * Compose the list of all the address books, once for ever...
	 * 
	 * Beyond the cache, this guarantees that the NAB are always retrieved in the same order.
	 * 
	 * The list has to be cached at the session level, as different users can have different ACLs for the databases.
	 * 
	 * @return NabDb[] Array of address books
	 * @throws NotesException
	 */
	private NABDb[] getSessionAddressBooks() throws NotesException {
		Map<String, Object> sc = ExtLibUtil.getSessionScope();
		NABDb[] addressBooks = sc != null ? (NABDb[]) sc.get(KEY_NABS) : null;
		if (addressBooks == null) {
			// Try with the current user
			Session session = ExtLibUtil.getCurrentSession();
			addressBooks = getSessionAddressBooks(session);
			if (addressBooks != null && addressBooks.length > 0) {
				if (sc != null) {
					sc.put(KEY_NABS, addressBooks);
				}
			} else {
				// No NAB is avail - we don't throw a signal from here as it forces authentication
				// as soon as the page is displayed (the control asks for the NAB when rendering)
				// throw new NoAccessSignal();
			}
		}
		return addressBooks;
	}

	/**
	 * Gets the address books for the current Session
	 * 
	 * @param session
	 *            Session
	 * @return NABDb[] Array of address books
	 * @throws NotesException
	 */
	private static NABDb[] getSessionAddressBooks(final Session session) throws NotesException {
		if (session != null) { // Unit tests
			ArrayList<NABDb> nabs = new ArrayList<NABDb>();
			Vector<?> vc = session.getAddressBooks();
			if (vc != null) {
				for (int i = 0; i < vc.size(); i++) {
					Database db = (Database) vc.get(i);
					try {
						db.open();
						try {
							NABDb nab = new NABDb(db);
							nabs.add(nab);
						} finally {
							db.recycle();
						}
					} catch (NotesException ex) {
						// Opening the database can fail if the user doesn't sufficient
						// rights. In this vase, we simply ignore this NAB and continue
						// with the next one.
					}
				}
			}
			return nabs.toArray(new NABDb[nabs.size()]);
		}
		return null;
	}

	/**
	 * @author withersp
	 * 
	 *         Entry class, copied from {@link com.ibm.xsp.extlib.component.picker.data.DominoNABNamePickerData._Entry} because the methods
	 *         are all protected
	 */
	public static abstract class _Entry extends Entry {
		// private Object[] attributes;

		/**
		 * Constructor, passing in the EntryMetaData object for the ViewEntry and the ViewEntry itself
		 * 
		 * @param metaData
		 *            EntryMetaData to access the view, its design and the search options
		 * @param ve
		 *            ViewEntry being iterated
		 * @throws NotesException
		 */
		public _Entry(final EntryMetaData metaData, final ViewEntry ve) throws NotesException {
			super(metaData, ve);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.openntf.domino.xsp.helpers.OpenntfNABNamePickerData.Entry#getMetaData()
		 */
		@Override
		public _EntryMetaData getMetaData() {
			return (_EntryMetaData) super.getMetaData();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.openntf.domino.xsp.helpers.OpenntfNABNamePickerData.Entry#readAttributes(lotus.domino.ViewEntry, java.util.Vector)
		 */
		@Override
		public Object[] readAttributes(final ViewEntry ve, final Vector<Object> columnValues) throws NotesException {
			return null;
		}
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
		@Override
		public Object getValue() {
			return value;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ibm.xsp.extlib.component.picker.data.IPickerEntry#getLabel()
		 */
		@Override
		public Object getLabel() {
			return label;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ibm.xsp.extlib.component.picker.data.IPickerEntry#getAttributeCount()
		 */
		@Override
		public int getAttributeCount() {
			return 0;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ibm.xsp.extlib.component.picker.data.IPickerEntry#getAttributeName(int)
		 */
		@Override
		public String getAttributeName(final int index) {
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ibm.xsp.extlib.component.picker.data.IPickerEntry#getAttributeValue(int)
		 */
		@Override
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
		public abstract Object readValue(ViewEntry ve, Vector<Object> columnValues) throws NotesException;

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
		public abstract Object readLabel(ViewEntry ve, Vector<Object> columnValues) throws NotesException;

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
		public abstract Object[] readAttributes(ViewEntry ve, Vector<Object> columnValues) throws NotesException;
	}

	// ====================================================================
	// Data access implementation
	//
	// Static abstract class for EntryMetaData, extended by _EntryMetaData
	// ====================================================================

	public abstract static class EntryMetaData {
		private View view;
		private IPickerOptions options;
		private NamePartsMap.Key key;

		/**
		 * Constructor loading in the options
		 * 
		 * @param options
		 *            IPickerOptions to refine lookup values
		 * @throws NotesException
		 */
		public EntryMetaData(final IPickerOptions options) throws NotesException {
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
		 * Gets a {@link NamePartsMap.Key} corresponding to the return name format defined on the Name Picker
		 * 
		 * @return NamePartsMap.Key for the return name format
		 */
		public NamePartsMap.Key getKey() {
			return key;
		}

		/**
		 * Loads in the return name format as a {@link NamePartsMap.Key} corresponding to the return name format defined on the Name Picker
		 * 
		 * @param key
		 *            NamePartsMap.Key
		 */
		public void setKey(final NamePartsMap.Key key) {
			this.key = key;
		}

		/**
		 * Gets the first sorted column's index from the View
		 * 
		 * @param vc
		 *            Vector<ViewColumn> pulled from the design of the View
		 * @return int first sorted column, starting at 0
		 * @throws NotesException
		 */
		public int findSortColumnIndex(final Vector<ViewColumn> vc) throws NotesException {
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
		public int findColumnIndex(final Vector<ViewColumn> vc, final String name) throws NotesException {
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
		public abstract View openView() throws NotesException;

		/**
		 * Creates a new Entry object from the ViewEntry
		 * 
		 * @param ve
		 *            ViewEntry being passed
		 * @return Entry object based on the ViewEntry
		 * @throws NotesException
		 */
		public abstract Entry createEntry(ViewEntry ve) throws NotesException;

	}

	// ////////////////////////////////////////////////////////////////////
	// People and groups
	/**
	 * _EntryMetaDataPeople class, required because the methods of
	 * {@link com.ibm.xsp.extlib.component.picker.data.DominoNABNamePickerData._EntryMetaDataPeople} are 'protected', so not accessible from
	 * here
	 * 
	 * This class is an exact duplicate of that class
	 */
	public class _EntryMetaDataPeople extends _EntryMetaData {
		/**
		 * Constructor, passing in options for getting a subset of the lookup values, e.g. start, typeahead key, search key, count etc.
		 * 
		 * @param options
		 *            IPickerOptions to refine lookup values
		 * @throws NotesException
		 */
		public _EntryMetaDataPeople(final IPickerOptions options) throws NotesException {
			super(options);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.openntf.domino.xsp.helpers.OpenntfNABNamePickerData._EntryMetaData#getViewName()
		 */
		@Override
		public String getViewName() {
			return "($VIMPeople)"; // $NON-NLS-1$
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.openntf.domino.xsp.helpers.OpenntfNABNamePickerData.EntryMetaData#createEntry(lotus.domino.ViewEntry)
		 */
		@Override
		public Entry createEntry(final ViewEntry ve) throws NotesException {
			return new _EntryPeople(this, ve);
		}
	}

	/**
	 * _EntryPeople class, required because the methods of
	 * {@link com.ibm.xsp.extlib.component.picker.data.DominoNABNamePickerData._EntryPeople} are 'protected', so not accessible from here
	 * 
	 * This class is an exact duplicate of that class
	 */
	public static class _EntryPeople extends _Entry {
		/**
		 * Constructor, passing in the EntryMetaData object for the ViewEntry and the ViewEntry itself
		 * 
		 * @param metaData
		 *            EntryMetaData to access the view, its design and the search options
		 * @param ve
		 *            ViewEntry being iterated
		 * @throws NotesException
		 */
		public _EntryPeople(final EntryMetaData metaData, final ViewEntry ve) throws NotesException {
			super(metaData, ve);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.openntf.domino.xsp.helpers.OpenntfNABNamePickerData.Entry#readValue(lotus.domino.ViewEntry, java.util.Vector)
		 */
		@Override
		public Object readValue(final ViewEntry ve, final Vector<Object> columnValues) throws NotesException {
			NamePartsMap.Key key = getMetaData().getKey();
			if (null == key) {
				return columnValues.get(0);
			} else {
				return Names.getNamePart(Factory.getSession(SessionType.CURRENT), (String) columnValues.get(0), key);
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.openntf.domino.xsp.helpers.OpenntfNABNamePickerData.Entry#readLabel(lotus.domino.ViewEntry, java.util.Vector)
		 */
		@Override
		public Object readLabel(final ViewEntry ve, final Vector<Object> columnValues) throws NotesException {
			String first = (String) columnValues.get(1);
			String mid = (String) columnValues.get(2);
			String last = (String) columnValues.get(3);
			StringBuilder b = new StringBuilder();
			if (StringUtil.isNotEmpty(first)) {
				b.append(first);
			}
			if (StringUtil.isNotEmpty(mid)) {
				if (b.length() > 0) {
					b.append(" ");
				}
				b.append(mid);
			}
			if (StringUtil.isNotEmpty(last)) {
				if (b.length() > 0) {
					b.append(" ");
				}
				b.append(last);
			}

			return b.toString();
		}
	}

	// ////////////////////////////////////////////////////////////////////
	// People by last name
	/**
	 * _EntryMetaDataPeopleByLastName class, required because the methods of
	 * {@link com.ibm.xsp.extlib.component.picker.data.DominoNABNamePickerData._EntryMetaDataPeopleByLastName} are 'protected', so not
	 * accessible from here
	 * 
	 * This class is an exact duplicate of that class
	 */
	public class _EntryMetaDataPeopleByLastName extends _EntryMetaData {
		/**
		 * Constructor, passing in options for getting a subset of the lookup values, e.g. start, typeahead key, search key, count etc.
		 * 
		 * @param options
		 *            IPickerOptions to refine lookup values
		 * @throws NotesException
		 */
		public _EntryMetaDataPeopleByLastName(final IPickerOptions options) throws NotesException {
			super(options);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.openntf.domino.xsp.helpers.OpenntfNABNamePickerData._EntryMetaData#getViewName()
		 */
		@Override
		public String getViewName() {
			return "($VIMPeopleByLastName)"; // $NON-NLS-1$
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.openntf.domino.xsp.helpers.OpenntfNABNamePickerData.EntryMetaData#createEntry(lotus.domino.ViewEntry)
		 */
		@Override
		public Entry createEntry(final ViewEntry ve) throws NotesException {
			return new _EntryPeopleByLastName(this, ve);
		}
	}

	/**
	 * _EntryPeopleByLastName class, required because the methods of
	 * {@link com.ibm.xsp.extlib.component.picker.data.DominoNABNamePickerData._EntryPeopleByLastName} are 'protected', so not accessible
	 * from here
	 * 
	 * This class is an exact duplicate of that class
	 */
	public static class _EntryPeopleByLastName extends _Entry {
		/**
		 * Constructor, passing in the EntryMetaData object for the ViewEntry and the ViewEntry itself
		 * 
		 * @param metaData
		 *            EntryMetaData to access the view, its design and the search options
		 * @param ve
		 *            ViewEntry being iterated
		 * @throws NotesException
		 */
		public _EntryPeopleByLastName(final EntryMetaData metaData, final ViewEntry ve) throws NotesException {
			super(metaData, ve);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.openntf.domino.xsp.helpers.OpenntfNABNamePickerData.Entry#readValue(lotus.domino.ViewEntry, java.util.Vector)
		 */
		@Override
		public Object readValue(final ViewEntry ve, final Vector<Object> columnValues) throws NotesException {
			NamePartsMap.Key key = getMetaData().getKey();
			if (null == key) {
				return columnValues.get(1);
			} else {
				return Names.getNamePart(Factory.getSession(SessionType.CURRENT), (String) columnValues.get(1), key);
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.openntf.domino.xsp.helpers.OpenntfNABNamePickerData.Entry#readLabel(lotus.domino.ViewEntry, java.util.Vector)
		 */
		@Override
		public Object readLabel(final ViewEntry ve, final Vector<Object> columnValues) throws NotesException {
			String first = (String) columnValues.get(2);
			String mid = (String) columnValues.get(3);
			String last = (String) columnValues.get(0);
			StringBuilder b = new StringBuilder();
			if (StringUtil.isNotEmpty(last)) {
				b.append(last);
			}
			if (StringUtil.isNotEmpty(first)) {
				if (b.length() > 0) {
					b.append(" ");
				}
				b.append(first);
			}
			if (StringUtil.isNotEmpty(mid)) {
				if (b.length() > 0) {
					b.append(" ");
				}
				b.append(mid);
			}

			return b.toString();
		}
	}

	// ////////////////////////////////////////////////////////////////////
	// Groups
	/**
	 * _EntryMetaDataGroup class, required because the methods of
	 * {@link com.ibm.xsp.extlib.component.picker.data.DominoNABNamePickerData._EntryMetaDataGroup} are 'protected', so not accessible from
	 * here
	 * 
	 * This class is an exact duplicate of that class
	 */
	public class _EntryMetaDataGroup extends _EntryMetaData {
		/**
		 * Constructor, passing in options for getting a subset of the lookup values, e.g. start, typeahead key, search key, count etc.
		 * 
		 * @param options
		 *            IPickerOptions to refine lookup values
		 * @throws NotesException
		 */
		public _EntryMetaDataGroup(final IPickerOptions options) throws NotesException {
			super(options);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.openntf.domino.xsp.helpers.OpenntfNABNamePickerData._EntryMetaData#getViewName()
		 */
		@Override
		public String getViewName() {
			return "($VIMGroups)"; // $NON-NLS-1$
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.openntf.domino.xsp.helpers.OpenntfNABNamePickerData.EntryMetaData#createEntry(lotus.domino.ViewEntry)
		 */
		@Override
		public Entry createEntry(final ViewEntry ve) throws NotesException {
			return new _EntryGroup(this, ve);
		}
	}

	/**
	 * _EntryGroup class, required because the methods of
	 * {@link com.ibm.xsp.extlib.component.picker.data.DominoNABNamePickerData._EntryGroup} are 'protected', so not accessible from here
	 * 
	 * This class is an exact duplicate of that class
	 */
	public static class _EntryGroup extends _Entry {
		/**
		 * Constructor, passing in the EntryMetaData object for the ViewEntry and the ViewEntry itself
		 * 
		 * @param metaData
		 *            EntryMetaData to access the view, its design and the search options
		 * @param ve
		 *            ViewEntry being iterated
		 * @throws NotesException
		 */
		public _EntryGroup(final EntryMetaData metaData, final ViewEntry ve) throws NotesException {
			super(metaData, ve);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.openntf.domino.xsp.helpers.OpenntfNABNamePickerData.Entry#readValue(lotus.domino.ViewEntry, java.util.Vector)
		 */
		@Override
		public Object readValue(final ViewEntry ve, final Vector<Object> columnValues) throws NotesException {
			// Groups are never canonical, only have a basic part to them
			return columnValues.get(0);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.openntf.domino.xsp.helpers.OpenntfNABNamePickerData.Entry#readLabel(lotus.domino.ViewEntry, java.util.Vector)
		 */
		@Override
		public Object readLabel(final ViewEntry ve, final Vector<Object> columnValues) throws NotesException {
			return columnValues.get(0);
		}
	}

	// ////////////////////////////////////////////////////////////////////
	// People and groups
	/**
	 * _EntryMetaDataPeopleAndGroup class, required because the methods of
	 * {@link com.ibm.xsp.extlib.component.picker.data.DominoNABNamePickerData._EntryMetaDataPeopleAndGroup} are 'protected', so not
	 * accessible from here
	 * 
	 * This class is an exact duplicate of that class
	 */
	public class _EntryMetaDataPeopleAndGroup extends _EntryMetaData {
		/**
		 * Constructor, passing in options for getting a subset of the lookup values, e.g. start, typeahead key, search key, count etc.
		 * 
		 * @param options
		 *            IPickerOptions to refine lookup values
		 * @throws NotesException
		 */
		public _EntryMetaDataPeopleAndGroup(final IPickerOptions options) throws NotesException {
			super(options);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.openntf.domino.xsp.helpers.OpenntfNABNamePickerData._EntryMetaData#getViewName()
		 */
		@Override
		public String getViewName() {
			return "($VIMPeopleAndGroups)"; // $NON-NLS-1$
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.openntf.domino.xsp.helpers.OpenntfNABNamePickerData.EntryMetaData#createEntry(lotus.domino.ViewEntry)
		 */
		@Override
		public Entry createEntry(final ViewEntry ve) throws NotesException {
			return new _EntryPeopleAndGroup(this, ve);
		}
	}

	/**
	 * _EntryPeopleAndGroup class, required because the methods of
	 * {@link com.ibm.xsp.extlib.component.picker.data.DominoNABNamePickerData._EntryPeopleAndGroup} are 'protected', so not accessible from
	 * here
	 * 
	 * This class is an exact duplicate of that class
	 */
	public static class _EntryPeopleAndGroup extends _Entry {
		/**
		 * Constructor, passing in the EntryMetaData object for the ViewEntry and the ViewEntry itself
		 * 
		 * @param metaData
		 *            EntryMetaData to access the view, its design and the search options
		 * @param ve
		 *            ViewEntry being iterated
		 * @throws NotesException
		 */
		public _EntryPeopleAndGroup(final EntryMetaData metaData, final ViewEntry ve) throws NotesException {
			super(metaData, ve);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.openntf.domino.xsp.helpers.OpenntfNABNamePickerData.Entry#readValue(lotus.domino.ViewEntry, java.util.Vector)
		 */
		@Override
		public Object readValue(final ViewEntry ve, final Vector<Object> columnValues) throws NotesException {
			NamePartsMap.Key key = getMetaData().getKey();
			if ("G".equals(columnValues.get(0))) {
				// Groups are never canonical, only have a basic value
				return columnValues.get(1);
			} else {
				if (null == key) {
					return columnValues.get(1);
				} else {
					return Names.getNamePart(Factory.getSession(SessionType.CURRENT), (String) columnValues.get(1), key);
				}
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.openntf.domino.xsp.helpers.OpenntfNABNamePickerData.Entry#readLabel(lotus.domino.ViewEntry, java.util.Vector)
		 */
		@Override
		public Object readLabel(final ViewEntry ve, final Vector<Object> columnValues) throws NotesException {
			String first = (String) columnValues.get(2);
			String mid = (String) columnValues.get(3);
			String last = (String) columnValues.get(4);
			StringBuilder b = new StringBuilder();
			if (StringUtil.isNotEmpty(first)) {
				b.append(first);
			}
			if (StringUtil.isNotEmpty(mid)) {
				if (b.length() > 0) {
					b.append(" ");
				}
				b.append(mid);
			}
			if (StringUtil.isNotEmpty(last)) {
				if (b.length() > 0) {
					b.append(" ");
				}
				b.append(last);
			}

			return b.toString();
		}
	}

	/**
	 * Creates meta data for the view, based on the PickerData properties and loading in the options
	 * 
	 * @param options
	 *            IPickerOptions to refine lookup values
	 * @return EntryMetaData instance
	 * @throws NotesException
	 */
	public EntryMetaData createOpenntfEntryMetaData(final IPickerOptions options) throws NotesException {
		String list = getNameList();
		if (StringUtil.isEmpty(list)) {
			boolean people = isPeople();
			boolean groups = isGroups();
			if (people && groups) {
				list = "peopleAndGroups"; // $NON-NLS-1$
			} else if (people) {
				list = "people"; // $NON-NLS-1$
			} else if (groups) {
				list = "groups"; // $NON-NLS-1$
			}
		}
		if (StringUtil.isNotEmpty(list)) {
			if (list.equals("peopleAndGroups")) { // $NON-NLS-1$
				return new _EntryMetaDataPeopleAndGroup(options);
			} else if (list.equals("peopleByLastName")) { // $NON-NLS-1$
				return new _EntryMetaDataPeopleByLastName(options);
			} else if (list.equals("people")) { // $NON-NLS-1$
				return new _EntryMetaDataPeople(options);
			} else if (list.equals("groups")) { // $NON-NLS-1$
				return new _EntryMetaDataGroup(options);
			}
		}
		return null;
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
			getReturnNameFormat();
			EntryMetaData meta = createOpenntfEntryMetaData(options);
			meta.setKey(getReturnNameFormatAsKey());
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
								// ok not all of them are skipped, stop the process
								count = 0;
							}
						}
						for (int i = 0; i < count && ve != null; i++) {
							if (ve instanceof ViewEntry) {
								entries.add(meta.createEntry(ve));
							}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.extlib.component.picker.data.DominoNABNamePickerData#saveState(javax.faces.context.FacesContext)
	 */
	@Override
	public Object saveState(final FacesContext context) {
		Object[] state = new Object[7];
		state[0] = super.saveState(context);
		state[1] = addressBookSel;
		state[2] = addressBookDb;
		state[3] = nameList;
		state[4] = people;
		state[5] = groups;
		state[6] = returnNameFormat;
		return state;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.extlib.component.picker.data.DominoNABNamePickerData#restoreState(javax.faces.context.FacesContext,
	 * java.lang.Object)
	 */
	@Override
	public void restoreState(final FacesContext context, final Object value) {
		Object[] state = (Object[]) value;
		super.restoreState(context, state[0]);
		this.addressBookSel = (String) state[1];
		this.addressBookDb = (String) state[2];
		this.nameList = (String) state[3];
		this.people = (Boolean) state[4];
		this.groups = (Boolean) state[5];
		this.returnNameFormat = (String) state[6];
	}
}

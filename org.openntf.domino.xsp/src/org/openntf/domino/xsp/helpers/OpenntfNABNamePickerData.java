/**
 * 
 */
package org.openntf.domino.xsp.helpers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import lotus.domino.Database;
import lotus.domino.NotesException;
import lotus.domino.Session;
import lotus.domino.View;
import lotus.domino.ViewColumn;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;
import lotus.domino.ViewNavigator;

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
 */
public class OpenntfNABNamePickerData extends DominoNABNamePickerData {

	private String addressBookSel;
	private String addressBookDb;
	private String nameList;
	private Boolean people;
	private Boolean groups;

	public OpenntfNABNamePickerData() {
		// TODO Auto-generated constructor stub
	}

	private static class NABDb implements Serializable { // Serializable because it goes to a scope
		private static final long serialVersionUID = 1L;
		String name;
		String title;
		boolean publicNab;
		boolean privateNab;

		NABDb(final Database db) throws NotesException {
			this(db.getFilePath(), db.getTitle());
			this.publicNab = db.isPublicAddressBook();
			this.privateNab = db.isPrivateAddressBook();
		}

		NABDb(final String name, final String title) throws NotesException {
			this.name = name;
			this.title = title;
			if (StringUtil.isEmpty(title)) {
				this.title = name;
			}
		}
	}

	public abstract class _EntryMetaData extends EntryMetaData {
		public _EntryMetaData(final IPickerOptions options) throws NotesException {
			super(options);
		}

		public abstract String getViewName();

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

	// Compose the list of all the address books, once for ever...
	// Beyond the cache, this guarantees that the NAB are always retrieved
	// in the same order.
	// The list has to be cached at the session level, as different users can
	// have different ACLs for the databases.
	private static final String KEY_NABS = "extlib.pickers.domino.nabs"; //$NON-NLS-1$ 

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

	public static abstract class _Entry extends Entry {
		// private Object[] attributes;
		public _Entry(final EntryMetaData metaData, final ViewEntry ve) throws NotesException {
			super(metaData, ve);
		}

		@Override
		public _EntryMetaData getMetaData() {
			return (_EntryMetaData) super.getMetaData();
		}

		@Override
		public Object[] readAttributes(final ViewEntry ve, final Vector<Object> columnValues) throws NotesException {
			return null;
		}
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

		public abstract Object readValue(ViewEntry ve, Vector<Object> columnValues) throws NotesException;

		public abstract Object readLabel(ViewEntry ve, Vector<Object> columnValues) throws NotesException;

		public abstract Object[] readAttributes(ViewEntry ve, Vector<Object> columnValues) throws NotesException;
	}

	// ====================================================================
	// Data access implementation
	// ====================================================================

	public abstract static class EntryMetaData {
		private View view;
		private IPickerOptions options;

		public EntryMetaData(final IPickerOptions options) throws NotesException {
			this.options = options;
			this.view = openView();
		}

		public View getView() {
			return view;
		}

		public IPickerOptions getOptions() {
			return options;
		}

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

		public abstract View openView() throws NotesException;

		public abstract Entry createEntry(ViewEntry ve) throws NotesException;
	}

	// ////////////////////////////////////////////////////////////////////
	// People
	public class _EntryMetaDataPeople extends _EntryMetaData {
		public _EntryMetaDataPeople(final IPickerOptions options) throws NotesException {
			super(options);
		}

		@Override
		public String getViewName() {
			return "($VIMPeople)"; // $NON-NLS-1$
		}

		@Override
		public Entry createEntry(final ViewEntry ve) throws NotesException {
			return new _EntryPeople(this, ve);
		}
	}

	public static class _EntryPeople extends _Entry {
		public _EntryPeople(final EntryMetaData metaData, final ViewEntry ve) throws NotesException {
			super(metaData, ve);
		}

		@Override
		public Object readValue(final ViewEntry ve, final Vector<Object> columnValues) throws NotesException {
			return columnValues.get(0);
		}

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

	public class _EntryMetaDataPeopleByLastName extends _EntryMetaData {
		public _EntryMetaDataPeopleByLastName(final IPickerOptions options) throws NotesException {
			super(options);
		}

		@Override
		public String getViewName() {
			return "($VIMPeopleByLastName)"; // $NON-NLS-1$
		}

		@Override
		public Entry createEntry(final ViewEntry ve) throws NotesException {
			return new _EntryPeopleByLastName(this, ve);
		}
	}

	public static class _EntryPeopleByLastName extends _Entry {
		public _EntryPeopleByLastName(final EntryMetaData metaData, final ViewEntry ve) throws NotesException {
			super(metaData, ve);
		}

		@Override
		public Object readValue(final ViewEntry ve, final Vector<Object> columnValues) throws NotesException {
			return columnValues.get(1);
		}

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
	public class _EntryMetaDataGroup extends _EntryMetaData {
		public _EntryMetaDataGroup(final IPickerOptions options) throws NotesException {
			super(options);
		}

		@Override
		public String getViewName() {
			return "($VIMGroups)"; // $NON-NLS-1$
		}

		@Override
		public Entry createEntry(final ViewEntry ve) throws NotesException {
			return new _EntryGroup(this, ve);
		}
	}

	public static class _EntryGroup extends _Entry {
		public _EntryGroup(final EntryMetaData metaData, final ViewEntry ve) throws NotesException {
			super(metaData, ve);
		}

		@Override
		public Object readValue(final ViewEntry ve, final Vector<Object> columnValues) throws NotesException {
			return columnValues.get(0);
		}

		@Override
		public Object readLabel(final ViewEntry ve, final Vector<Object> columnValues) throws NotesException {
			return columnValues.get(0);
		}
	}

	// ////////////////////////////////////////////////////////////////////
	// People and groups
	public class _EntryMetaDataPeopleAndGroup extends _EntryMetaData {
		public _EntryMetaDataPeopleAndGroup(final IPickerOptions options) throws NotesException {
			super(options);
		}

		@Override
		public String getViewName() {
			return "($VIMPeopleAndGroups)"; // $NON-NLS-1$
		}

		@Override
		public Entry createEntry(final ViewEntry ve) throws NotesException {
			return new _EntryPeopleAndGroup(this, ve);
		}
	}

	public static class _EntryPeopleAndGroup extends _Entry {
		public _EntryPeopleAndGroup(final EntryMetaData metaData, final ViewEntry ve) throws NotesException {
			super(metaData, ve);
		}

		@Override
		public Object readValue(final ViewEntry ve, final Vector<Object> columnValues) throws NotesException {
			return columnValues.get(1);
		}

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

	@Override
	public IPickerResult readEntries(final IPickerOptions options) {
		try {
			EntryMetaData meta = createOpenntfEntryMetaData(options);
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
							// TODO: Get this working with ViewNavigator
							// int searchOptions = DominoUtils.FIND_GREATER_THAN | DominoUtils.FIND_EQUAL | DominoUtils.FIND_PARTIAL
							// | DominoUtils.FIND_CASE_INSENSITIVE;
							// ve = DominoUtils.getViewEntryByKeyWithOptions(view, key, searchOptions);
							ve = view.getEntryByKey(key, false);
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
}

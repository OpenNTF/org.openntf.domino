/**
 * 
 */
package org.openntf.domino.big.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.DbDirectory;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.Item;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.ViewColumn;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewNavigator;
import org.openntf.domino.helpers.DocumentScanner;
import org.openntf.domino.helpers.DocumentSorter;
import org.openntf.domino.types.CaseInsensitiveString;
import org.openntf.domino.utils.Factory;

/**
 * @author Nathan T. Freeman
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class IndexDatabase implements IScannerStateManager {
	private static final Logger log_ = Logger.getLogger(IndexDatabase.class.getName());

	public static final String TERM_VIEW_NAME = "$TermIndex";
	public static final String TERM_FORM_NAME = "$TermDoc";
	public static final String TERM_KEY_NAME = "TermKey";
	public static final String TERM_MAP_PREFIX = "TermMap_";
	public static final String DBID_NAME = "DbidList";
	public static final String DB_VIEW_NAME = "$DbIndex";
	public static final String DB_FORM_NAME = "$DbDoc";
	public static final String DB_KEY_NAME = "DbKey";
	public static final String DB_TOKEN_LOCATION_NAME = "TokenLocationMap";
	public static final String DB_NAME_LOCATION_NAME = "NameLocationMap";
	public static final String DB_FIELD_TOKEN_NAME = "FieldTokenMap";
	public static final String DB_LAST_INDEX_NAME = "LastIndexTime";
	public static final String DB_DOC_LIST_NAME = "DocumentList";
	public static final String DB_TITLE_NAME = "Title";
	public static final String DB_DOC_SORTER_NAME = "DocumentSorter";
	public static final String[] DEFAULT_STOP_WORDS_EN = "a,able,about,across,after,all,almost,also,am,among,an,and,any,are,as,at,be,because,been,but,by,can,cannot,could,dear,did,do,does,either,else,ever,every,for,from,get,got,had,has,have,he,her,hers,him,his,how,however,i,if,in,into,is,it,its,just,least,let,like,likely,may,me,might,most,must,my,neither,no,nor,not,of,off,often,on,only,or,other,our,own,rather,said,say,says,she,should,since,so,some,than,that,the,their,them,then,there,these,they,this,tis,to,too,twas,us,wants,was,we,were,what,when,where,which,while,who,whom,why,will,with,would,yet,you,your"
			.split(",");

	protected transient Database indexDb_;
	protected transient View termView_;
	protected transient View dbView_;
	protected Set<CharSequence> stopList_;
	protected boolean caseSensitive_ = false;
	protected boolean continue_ = true;

	public static Set<CharSequence> toStringSet(final Object value) {
		Set<CharSequence> result = new HashSet<CharSequence>();
		if (value == null)
			return result;
		if (value instanceof Iterable) {
			Iterable values = (Iterable) value;
			for (Object o : values) {
				if (o instanceof String) {
					result.add((String) o);
				} else if (o instanceof CaseInsensitiveString) {
					result.add(((CaseInsensitiveString) o).toString());
				} else {
					result.add(String.valueOf(o));
				}
			}
		} else if (value instanceof String) {
			result.add((String) value);
		} else if (value instanceof CharSequence) {
			result.add(((CharSequence) value).toString());
		} else {
			result.add(String.valueOf(value));
		}
		return result;
	}

	public static Set<CharSequence> toCISSet(final Object value) {
		Set<CharSequence> result = new HashSet<CharSequence>();
		if (value == null)
			return result;
		if (value instanceof Iterable) {
			Iterable values = (Iterable) value;
			for (Object o : values) {
				if (o instanceof CaseInsensitiveString) {
					result.add((CaseInsensitiveString) o);
				} else if (o instanceof CharSequence) {
					result.add(new CaseInsensitiveString(((CharSequence) o).toString()));
				} else {
					result.add(new CaseInsensitiveString(String.valueOf(o)));
				}
			}
		} else if (value instanceof CaseInsensitiveString) {
			result.add((CaseInsensitiveString) value);
		} else if (value instanceof CharSequence) {
			result.add(new CaseInsensitiveString(((CharSequence) value).toString()));
		} else {
			result.add(new CaseInsensitiveString(String.valueOf(value)));
		}
		return result;
	}

	public IndexDatabase() {

	}

	public IndexDatabase(final Database indexDb) {
		indexDb_ = indexDb;
	}

	public void setCaseSensitive(final boolean value) {
		caseSensitive_ = value;
	}

	public boolean getCaseSensitive() {
		return caseSensitive_;
	}

	public void setDatabase(final Database indexDb) {
		indexDb_ = indexDb;
	}

	public Database getIndexDb() {
		if (indexDb_ == null) {
			indexDb_ = Factory.getSession().getCurrentDatabase();
		}
		return indexDb_;
	}

	protected void initIndexDb() {

		View indexView = getIndexDb().getView(TERM_VIEW_NAME);
		if (indexView == null) {
			indexView = getIndexDb().createView(TERM_VIEW_NAME, "Form=\"" + TERM_FORM_NAME + "\"");
			for (ViewColumn column : indexView.getColumns()) {
				column.setFormula(TERM_KEY_NAME);
				column.setTitle("Term");
				column.setSorted(true);
				column.setSortDescending(false);
			}
		}
		View dbView = getIndexDb().getView(DB_VIEW_NAME);
		if (dbView == null) {
			dbView = getIndexDb().createView(DB_VIEW_NAME, "Form=\"" + DB_FORM_NAME + "\"");
			for (ViewColumn column : dbView.getColumns()) {
				column.setFormula(DB_KEY_NAME);
				column.setTitle("ID");
				column.setSorted(true);
				column.setSortDescending(false);
			}
			ViewColumn titleColumn = dbView.createColumn();
			titleColumn.setFormula(DB_TITLE_NAME);
			titleColumn.setTitle("TITLE");

		}
	}

	public Set<CharSequence> getStopList() {
		if (stopList_ == null) {
			stopList_ = new HashSet<CharSequence>();
			for (String s : DEFAULT_STOP_WORDS_EN) {
				stopList_.add(s);
			}
		}
		return stopList_;
	}

	public void setStopList(final Set<CharSequence> list) {
		stopList_ = list;
	}

	public View getTermView() {
		if (dbView_ == null) {
			initIndexDb();
			dbView_ = getIndexDb().getView(TERM_VIEW_NAME);
		}
		return dbView_;
	}

	public List<String> getTermStarts(final String startsWith, final int count) {
		List<String> result = new ArrayList<String>();
		ViewEntry startEntry = getTermView().getEntryByKey(startsWith, false);
		if (startEntry == null) {
			if (log_.isLoggable(Level.FINE))
				log_.log(Level.FINE, "Unable to find ViewEntry for key " + startsWith);
			//			ViewEntryCollection vec = getTermView().getAllEntriesByKey(startsWith, false);
			//			System.out.println("ViewEntryCollection strategy returned " + vec.getCount() + " entries.");
			return result;
		}
		String val = (String) startEntry.getColumnValue(IndexDatabase.TERM_KEY_NAME, String.class);
		result.add(val);
		ViewNavigator nav = getTermView().createViewNavFrom(startEntry, count);
		for (int i = 1; i < count; i++) {
			ViewEntry nextEntry = nav.getNextSibling();
			val = (String) nextEntry.getColumnValue(IndexDatabase.TERM_KEY_NAME, String.class);
			result.add(val);
		}
		return result;
	}

	private List<String> getTermContains(final String contains) {
		List<String> result = new ArrayList<String>();
		return result;
	}

	public View getDbView() {
		if (termView_ == null) {
			initIndexDb();
			termView_ = getIndexDb().getView(DB_VIEW_NAME);
		}
		return termView_;
	}

	public Document getDbDocument(final CharSequence dbid) {
		String key = dbid.toString().toUpperCase();
		Document result = getIndexDb().getDocumentByKey(key, true);
		if (result.isNewNote()) {
			result.replaceItemValue("Form", DB_FORM_NAME);
			result.replaceItemValue(DB_KEY_NAME, dbid);
			result.save();
		}
		return result;
	}

	public Document getTermDocument(final CharSequence token) {
		String key = caseSensitive_ ? token.toString() : token.toString().toLowerCase();

		Document result = getIndexDb().getDocumentByKey(key, true);
		if (result.isNewNote()) {
			result.replaceItemValue("Form", TERM_FORM_NAME);
			result.replaceItemValue(TERM_KEY_NAME, token);
			result.save();
		}
		return result;
	}

	public Document getNameDocument(final CharSequence name) {
		String key = caseSensitive_ ? name.toString() : name.toString().toLowerCase();

		Document result = getIndexDb().getDocumentByKey(key, true);
		if (result.isNewNote()) {
			result.replaceItemValue("Form", TERM_FORM_NAME);
			result.replaceItemValue("isName", "1");
			result.replaceItemValue(TERM_KEY_NAME, name);
			result.save();
		}
		return result;
	}

	public void scanServer(final Session session, final String serverName) {
		initIndexDb();
		DbDirectory dir = session.getDbDirectory(serverName);
		dir.setDirectoryType(DbDirectory.Type.DATABASE);
		for (Database db : dir) {
			if (!db.getReplicaID().equals(getIndexDb().getReplicaID())) {
				//				System.out.println("Scanning database " + db.getApiPath());
				if (!db.getFilePath().equalsIgnoreCase("redpill\\graph.nsf")) {
					try {
						scanDatabase(db);
					} catch (Throwable t) {
						t.printStackTrace();
					}
				}
			}
			if (!continue_) {
				System.out.println("Escaping process early due to continue_ == false");
				return;
			}
		}
		System.out.println("Completed scan of server " + serverName);
	}

	public DocumentScanner scanDatabase(final Database db) {
		Document dbDoc = getDbDocument(db.getReplicaID());
		DocumentScanner scanner = new DocumentScanner();
		scanner.setTrackFieldTokens(false);
		scanner.setTrackFieldTypes(false);
		scanner.setTrackFieldValues(false);
		scanner.setTrackTokenFreq(false);
		scanner.setTrackTokenLocation(true);
		scanner.setTrackNameLocation(true);
		scanner.setStopTokenList(getStopList());
		scanner.setIgnoreDollar(true);
		scanner.setStateManager(this, db.getReplicaID());
		scanner.setCaseSensitive(getCaseSensitive());
		dbDoc.replaceItemValue(IndexDatabase.DB_TITLE_NAME, db.getTitle());
		if (dbDoc.hasItem(DB_LAST_INDEX_NAME)) {
			scanner.setLastScanDate((Date) dbDoc.getItemValue(DB_LAST_INDEX_NAME, Date.class));
			//			scanner.setStopTokenList(getStopList());
			//			scanner.setIgnoreDollar(true);
			//			Object tokenLocationObject = dbDoc.getItemValue(DB_TOKEN_LOCATION_NAME, Map.class);
			//			if (tokenLocationObject != null && !((Map) tokenLocationObject).isEmpty()) {
			//				scanner.setTokenLocationMap(tokenLocationObject);
			//			}
		}
		if (dbDoc.hasItem(IndexDatabase.DB_DOC_LIST_NAME)) {
			scanner.setCollection((org.openntf.domino.DocumentCollection) dbDoc.getItemValue(IndexDatabase.DB_DOC_LIST_NAME,
					org.openntf.domino.DocumentCollection.class));
		}
		Date scanDate = new Date();
		scanDatabase(db, scanner);
		String dbid = db.getReplicaID();
		//		writeResults(dbid, scanner);
		dbDoc.replaceItemValue(DB_LAST_INDEX_NAME, scanDate);
		//		dbDoc.replaceItemValue(DB_FIELD_TOKEN_NAME, scanner.getFieldTokenMap());
		//		dbDoc.replaceItemValue(DB_TOKEN_LOCATION_NAME, scanner.getTokenLocationMap());
		dbDoc.save();
		return scanner;
	}

	private int totalErrCount_ = 0;

	private static final List<String> MOD_SORT_LIST = new ArrayList<String>();
	static {
		MOD_SORT_LIST.add("@modified");
	}

	private int curDocCount_ = 0;
	private int sortedDocCount_ = 0;

	public DocumentScanner scanDatabase(final Database db, final DocumentScanner scanner) {
		//		System.out.println("Scanning database " + db.getApiPath());
		curDocCount_ = 0;
		scanner.setCaseSensitive(getCaseSensitive());
		Date last = scanner.getLastScanDate();
		if (last == null)
			last = new Date(0);
		int count = db.getModifiedNoteCount(last);

		if (count > 0) {
			DocumentCollection rawColl = db.getModifiedDocuments(last);
			DocumentSorter sorter = new DocumentSorter(rawColl, MOD_SORT_LIST);
			System.out.println("Scanning database " + db.getApiPath() + " with last date of " + last.getTime() + " and found "
					+ rawColl.getCount() + " updates to scan");
			scanner.processSorter(sorter);

		}
		return scanner;
	}

	//	public void writeResults(final CharSequence dbid, final DocumentScanner scanner) {
	//		Map<CharSequence, Map<CharSequence, Set<CharSequence>>> tlMap = scanner.getTokenLocationMap();
	//		saveTokenLocationMap(dbid, tlMap, scanner);
	//	}

	public List<String> getTermDbids(final CharSequence term) {
		List<String> result = new ArrayList<String>();
		Document doc = getTermDocument(term);
		for (Item item : doc.getItems()) {
			String itemName = item.getName();
			if (itemName.startsWith(TERM_MAP_PREFIX)) {
				String dbid = itemName.substring(TERM_MAP_PREFIX.length());
				result.add(dbid);
			}
		}
		return result;
	}

	public int getTermHitCount(final String term) {
		int result = 0;
		Document doc = getTermDocument(term);
		for (Item item : doc.getItems()) {
			String itemName = item.getName();
			if (itemName.startsWith(TERM_MAP_PREFIX)) {
				//				String dbid = itemName.substring(TERM_MAP_PREFIX.length());
				Map termMap = doc.getItemValue(itemName, Map.class);
				for (Object key : termMap.keySet()) {
					Object val = termMap.get(key);
					if (val instanceof Collection) {
						result += ((Collection) val).size();
					}
				}
			}
		}
		return result;
	}

	protected int getTermDbidHitCount(final Document doc, final CharSequence dbid) {
		int result = 0;
		String itemName = TERM_MAP_PREFIX + dbid;
		if (doc.hasItem(itemName)) {
			Map termMap = doc.getItemValue(itemName, Map.class);
			for (Object key : termMap.keySet()) {
				Object val = termMap.get(key);
				if (val instanceof Collection) {
					result += ((Collection) val).size();
				}
			}
		}
		return result;
	}

	public Set<CharSequence> getTermItemsInDbids(final String term, final Collection<String> dbids) {
		Set<CharSequence> result = new HashSet<CharSequence>();
		Document doc = getTermDocument(term);
		for (String dbid : dbids) {
			String itemName = TERM_MAP_PREFIX + dbid;
			if (doc.hasItem(itemName)) {
				Map termMap = doc.getItemValue(itemName, Map.class);
				result.addAll(termMap.keySet());
			}
		}
		return result;
	}

	public List<IndexHit> getTermResults(final CharSequence term, final int limit, final Set<CharSequence> dbids,
			final Set<CharSequence> itemNames, final Set<CharSequence> forms) {
		List<IndexHit> results = new ArrayList<IndexHit>();
		Document doc = getTermDocument(term);
		int dbCount = 0;
		if (dbids == null || dbids.isEmpty()) {
			for (Item item : doc.getItems()) {
				String itemName = item.getName();
				if (itemName.startsWith(TERM_MAP_PREFIX)) {
					dbCount++;
					String dbid = itemName.substring(TERM_MAP_PREFIX.length());
					Map termMap = doc.getItemValue(itemName, Map.class);
					results.addAll(getTermResultsForItemsForms(termMap, itemNames, forms, term, dbid));
					if (limit != 0 && results.size() >= limit) {
						return results;
					}
				}
			}
		} else {
			for (CharSequence dbid : dbids) {
				String itemName = TERM_MAP_PREFIX + dbid;
				if (doc.hasItem(itemName)) {
					dbCount++;
					Map termMap = doc.getItemValue(itemName, Map.class);
					results.addAll(getTermResultsForItemsForms(termMap, itemNames, forms, term, dbid));
					if (limit != 0 && results.size() >= limit) {
						return results;
					}
				}
			}
		}
		if (dbCount < 1) {
			System.out.println("No databases found that contain term " + term + " in document " + doc.getNoteID() + ": "
					+ doc.getAncestorDatabase().getApiPath());
		}
		return results;
	}

	protected List<IndexHit> getTermResultsForItemsForms(final Map map, final Set<CharSequence> itemNames, final Set<CharSequence> forms,
			final CharSequence term, final CharSequence dbid) {
		List<IndexHit> results = new ArrayList<IndexHit>();
		if (itemNames == null || itemNames.isEmpty()) {
			if (map.keySet() != null) {
				for (Object key : map.keySet()) {
					Object val = map.get(key);
					List<IndexHit> hits = null;
					if (val instanceof Set) {
						//					System.out.println("Already have a set of " + ((Set) val).size() + " elements");
						hits = getTermResultsForForms((Set) val, forms, term, dbid, key.toString());
					} else {
						//					System.out.println("Converting to a set from a " + (val == null ? "null" : val.getClass().getName()));
						hits = getTermResultsForForms(toStringSet(val), forms, term, dbid, key.toString());
					}
					results.addAll(hits);
				}
			}
		} else {
			for (CharSequence key : itemNames) {
				//				System.out.println("Adding hit results for item " + key);

				Object val = map.get(key);
				List<IndexHit> hits = null;
				if (val instanceof Set) {
					//					System.out.println("Already have a set of " + ((Set) val).size() + " elements");
					hits = getTermResultsForForms((Set) val, forms, term, dbid, key.toString());
				} else {
					//					System.out.println("Converting to a set from a " + (val == null ? "null" : val.getClass().getName()));
					hits = getTermResultsForForms(toStringSet(val), forms, term, dbid, key.toString());
				}
				results.addAll(hits);
			}
		}
		return results;
	}

	protected List<IndexHit> getTermResultsForForms(final Set<CharSequence> unids, final Set<CharSequence> forms, final CharSequence term,
			final CharSequence dbid, final CharSequence item) {
		List<IndexHit> results = new ArrayList<IndexHit>();
		if (forms == null || forms.isEmpty()) {
			if (unids != null && !unids.isEmpty()) {
				for (CharSequence unid : unids) {
					try {
						results.add(createHit(term, dbid, item, unid));
					} catch (Exception e) {
						System.out.println("Exception occured trying to create a hit for db " + dbid + " on term " + term
								+ " with a string of " + String.valueOf(unid) + " from a set of " + unids.size());
					}
				}
			}
		} else {
			for (CharSequence unid : unids) {
				String formName = unid.toString().substring(33);
				for (CharSequence form : forms) {
					if (formName.equalsIgnoreCase(form.toString())) {
						results.add(createHit(term, dbid, item, unid));
						break;
					}
				}
			}
		}
		return results;
	}

	protected IndexHit createHit(final CharSequence term, final CharSequence dbid, final CharSequence item, final CharSequence unid) {
		return new IndexHit(term, dbid, item, unid);
	}

	public Set<CharSequence> getTermUnidInDbsItems(final String term, final Collection<String> dbids, final Collection<?> itemNames) {
		Set<CharSequence> unids = new HashSet<CharSequence>();
		Document doc = getTermDocument(term);
		for (String dbid : dbids) {
			String itemName = TERM_MAP_PREFIX + dbid;
			if (doc.hasItem(itemName)) {
				Map termMap = doc.getItemValue(itemName, Map.class);

				for (Object key : itemNames) {
					CaseInsensitiveString ciskey = null;
					if (key instanceof CaseInsensitiveString) {
						ciskey = (CaseInsensitiveString) key;
					} else if (key instanceof String) {
						ciskey = new CaseInsensitiveString((String) key);
					} else {
						ciskey = new CaseInsensitiveString(String.valueOf(key));
					}
					Object termObj = termMap.get(ciskey);
					if (termObj != null) {
						if (termObj instanceof Collection) {
							unids.addAll((Collection) termObj);
						} else if (termObj instanceof CharSequence) {
							unids.add(((CharSequence) termObj).toString());
						} else {
							unids.add(String.valueOf(termObj));
						}
					}
				}
			}
		}
		return unids;
	}

	public Set<String> getTermLinksInDbsItems(final Session session, final String serverName, final String term,
			final Collection<CharSequence> dbids, final Collection<?> itemNames) {
		Set<String> unids = new HashSet<String>();
		Document doc = getTermDocument(term);
		for (CharSequence dbid : dbids) {
			String itemName = TERM_MAP_PREFIX + dbid;
			if (doc.hasItem(itemName)) {
				Map termMap = doc.getItemValue(itemName, Map.class);
				//				Database db = session.getDatabaseByReplicaID(serverName, dbid);
				//				if (db != null) {
				for (Object key : itemNames) {
					CaseInsensitiveString ciskey = null;
					if (key instanceof CaseInsensitiveString) {
						ciskey = (CaseInsensitiveString) key;
					} else if (key instanceof String) {
						ciskey = new CaseInsensitiveString((String) key);
					} else {
						ciskey = new CaseInsensitiveString(String.valueOf(key));
					}
					Object termObj = termMap.get(ciskey);
					if (termObj != null) {
						if (termObj instanceof Collection) {
							for (Object unid : (Collection) termObj) {
								//									Document curDoc = db.getDocumentByUNID((String) unid);
								unids.add("http://localhost/__" + dbid + ".nsf/0/" + unid);
							}
							//								unids.addAll((Collection) termObj);
						} else if (termObj instanceof CharSequence) {
							//								Document curDoc = db.getDocumentByUNID((String) ((CharSequence) termObj).toString());
							unids.add("http://localhost/__" + dbid + ".nsf/0/" + ((CharSequence) termObj).toString());
							//								unids.add(((CharSequence) termObj).toString());
						} else {
							unids.add(String.valueOf(termObj));
						}
					}
				}
				//				}
			}
		}
		return unids;
	}

	public Set<CharSequence> getTermUnidInItems(final CharSequence term, final Collection<String> itemNames) {
		Set<CharSequence> unids = new HashSet<CharSequence>();
		Document doc = getTermDocument(term);
		for (Item item : doc.getItems()) {
			String itemName = item.getName();
			if (itemName.startsWith(TERM_MAP_PREFIX)) {
				//				String dbid = itemName.substring(TERM_MAP_PREFIX.length());
				Map termMap = doc.getItemValue(itemName, Map.class);
				for (String key : itemNames) {
					CaseInsensitiveString ciskey = new CaseInsensitiveString(key);
					Object termObj = termMap.get(ciskey);
					if (termObj != null) {
						if (termObj instanceof Collection) {
							unids.addAll((Collection) termObj);
						} else if (termObj instanceof CharSequence) {
							unids.add(((CharSequence) termObj).toString());
						} else {
							unids.add(String.valueOf(termObj));
						}
					}
				}
			}
		}
		return unids;
	}

	public Set<String> getTermUnidInDbids(final CharSequence term, final Collection<String> dbids) {
		Set<String> unids = new HashSet<String>();
		Document doc = getTermDocument(term);
		for (String dbid : dbids) {
			String itemName = TERM_MAP_PREFIX + dbid;
			if (doc.hasItem(itemName)) {
				Map termMap = doc.getItemValue(itemName, Map.class);
				for (Object key : termMap.keySet()) {
					Object termObj = termMap.get(key);
					if (termObj != null) {
						if (termObj instanceof Collection) {
							unids.addAll((Collection) termObj);
						} else if (termObj instanceof CharSequence) {
							unids.add(((CharSequence) termObj).toString());
						} else {
							unids.add(String.valueOf(termObj));

						}
					}
				}
			}
		}
		return unids;
	}

	public Map<String, Set<CharSequence>> getTermItemMap(final String term) {
		Map<String, Set<CharSequence>> result = new LinkedHashMap<String, Set<CharSequence>>();
		Document doc = getTermDocument(term);
		for (Item item : doc.getItems()) {
			String itemName = item.getName();
			if (itemName.startsWith(TERM_MAP_PREFIX)) {
				String dbid = itemName.substring(TERM_MAP_PREFIX.length());
				Map termMap = doc.getItemValue(itemName, Map.class);
				result.put(dbid, termMap.keySet());
			}
		}
		return result;
	}

	public static List<String> dbidCollToTitle(final Session session, final String serverName, final Collection<String> dbids) {
		List<String> result = new ArrayList<String>();
		for (String dbid : dbids) {
			Database db = session.getDatabaseByReplicaID(serverName, dbid);
			if (db != null) {
				result.add(db.getTitle() + "|" + dbid);
			}
		}
		return result;
	}

	public static List<String> dbMapToCheckbox(final Session session, final String serverName, final Map<String, AtomicInteger> dbMap) {
		List<String> result = new ArrayList<String>();
		for (String dbid : dbMap.keySet()) {
			Database db = session.getDatabaseByReplicaID(serverName, dbid);
			if (db != null) {
				result.add(db.getTitle() + " (" + dbMap.get(dbid) + ")|" + dbid);
			}
		}
		return result;
	}

	public static List<String> itemMapToCheckbox(final Map<String, AtomicInteger> itemMap) {
		List<String> result = new ArrayList<String>();
		for (String item : itemMap.keySet()) {
			result.add(item.substring(16) + " (" + itemMap.get(item) + ")|" + item.substring(16));
		}
		return result;
	}

	public static List<String> formMapToCheckbox(final Map<String, AtomicInteger> formMap) {
		List<String> result = new ArrayList<String>();
		for (String form : formMap.keySet()) {
			result.add(form.substring(16) + " (" + formMap.get(form) + ")|" + form.substring(16));
		}
		return result;
	}

	public Map<CharSequence, Set<CharSequence>> getTermUnidMap(final CharSequence term) {
		Map<CharSequence, Set<CharSequence>> result = new LinkedHashMap<CharSequence, Set<CharSequence>>();
		Document doc = getTermDocument(term);
		for (Item item : doc.getItems()) {
			String itemName = item.getName();
			if (itemName.startsWith(TERM_MAP_PREFIX)) {
				String dbid = itemName.substring(TERM_MAP_PREFIX.length());
				Set<CharSequence> unids = new HashSet<CharSequence>();
				Map termMap = doc.getItemValue(itemName, Map.class);
				for (Object key : termMap.keySet()) {
					Object termObj = termMap.get(key);
					if (termObj != null) {
						if (termObj instanceof Collection) {
							unids.addAll((Collection) termObj);
						} else if (termObj instanceof CharSequence) {
							unids.add(((CharSequence) termObj).toString());
						} else {
							unids.add(String.valueOf(termObj));
						}
					}
				}
				result.put(dbid, unids);
			}
		}
		return result;
	}

	public CharSequence lastToken_ = null;

	public Map<CharSequence, Set<CharSequence>> restoreTokenLocationMap(final CharSequence token, final Object mapKey) {
		Map result = null;
		Document doc = getTermDocument(token.toString());
		String itemName = TERM_MAP_PREFIX + String.valueOf(mapKey);
		if (doc.hasItem(itemName)) {
			result = doc.getItemValue(itemName, Map.class);
			//			System.out.println("Found existing term match for: " + token.toString() + " with " + result.size() + " items");
		} else {
			result = new ConcurrentHashMap<CaseInsensitiveString, Set<String>>();
		}
		return result;
	}

	public void saveTokenLocationMap(final CharSequence token, final Object mapKey, final Map<CharSequence, Set<CharSequence>> map) {

		String term = token.toString();
		Document termDoc = getTermDocument(term);
		termDoc.replaceItemValue(TERM_MAP_PREFIX + String.valueOf(mapKey), map);
		termDoc.save();
	}

	public void setLastIndexDate(final Object mapKey, final Date date) {
		Document dbDoc = getDbDocument((String) mapKey);
		dbDoc.replaceItemValue(DB_LAST_INDEX_NAME, date);
		dbDoc.save();
	}

	public Date getLastIndexDate(final Object mapKey) {
		Document dbDoc = getDbDocument((String) mapKey);
		Date result = (Date) dbDoc.getItemValue(DB_LAST_INDEX_NAME, java.util.Date.class);
		if (result == null)
			result = new Date(0);
		return result;
	}

	public void saveTokenLocationMap(final Object mapKey, final Map<CharSequence, Map<CharSequence, Set<CharSequence>>> fullMap,
			final DocumentScanner scanner) {
		setLastIndexDate(mapKey, scanner.getLastDocModDate());

		//		Document dbDoc = getDbDocument((String) mapKey);
		//		if (scanner.getCollection() != null) {
		//			dbDoc.replaceItemValue(IndexDatabase.DB_DOC_LIST_NAME, scanner.getCollection());
		//		}
		//
		//		if (scanner.getSorter() != null) {
		//			dbDoc.replaceItemValue(IndexDatabase.DB_DOC_SORTER_NAME, scanner.getSorter());
		//		}
		//		dbDoc.save();
		Set<CharSequence> keySet = fullMap.keySet();
		for (CharSequence cis : keySet) {
			Map<CharSequence, Set<CharSequence>> tlValue = fullMap.get(cis);
			String term = cis.toString();
			Document termDoc = getTermDocument(term);
			String itemName = TERM_MAP_PREFIX + String.valueOf(mapKey);
			termDoc.replaceItemValue(itemName, tlValue);
			if (termDoc.save()) {
				//				System.out.println("Saved term doc for " + term);
			}
		}
	}

	public void update(final Observable o, final Object arg) {
		IScannerStateManager.ScanStatus status = null;
		if (arg instanceof IScannerStateManager.ScanStatus) {
			status = (IScannerStateManager.ScanStatus) arg;
		}
		DocumentScanner scanner = null;
		if (o instanceof DocumentScanner) {
			scanner = (DocumentScanner) o;
		} else {
			System.out.println("Observable object was not a DocumentScanner. It was a " + (o == null ? "null" : o.getClass().getName()));
		}
		if (status != null) {
			switch (status) {
			case NEW:
				break;
			case RUNNING:
				if (scanner != null) {
					if (scanner.isTrackTokenLocation()) {
						Map tokenLocationMap = scanner.getTokenLocationMap();
						int tlsize = tokenLocationMap.size();
						if (tlsize >= 1024) {
							//							System.out.println("Processed " + scanner.getDocCount() + " documents so far, " + scanner.getItemCount()
							//									+ " items and " + scanner.getTokenCount());
							synchronized (tokenLocationMap) {
								saveTokenLocationMap(scanner.getStateManagerKey(), tokenLocationMap, scanner);
								tokenLocationMap.clear();
							}
						}
					} else {
						System.out.println("TokenLocation not being tracked by scanner");
					}
					if (scanner.isTrackNameLocation()) {
						Map nameLocationMap = scanner.getNameLocationMap();
						int nlsize = nameLocationMap.size();
						if (nlsize >= 128) {
							synchronized (nameLocationMap) {
								saveTokenLocationMap(scanner.getStateManagerKey(), nameLocationMap, scanner);
								nameLocationMap.clear();
							}
						}
					}
				} else {
					System.out.println("Scanner is null from notifications");
				}
				break;
			case COMPLETE:
				if (scanner != null) {
					//					System.out.println("Processed " + scanner.getDocCount() + " documents at completion, " + scanner.getItemCount()
					//							+ " items and " + scanner.getTokenCount());
					if (scanner.isTrackTokenLocation()) {
						Map tokenLocationMap = scanner.getTokenLocationMap();
						synchronized (tokenLocationMap) {
							saveTokenLocationMap(scanner.getStateManagerKey(), tokenLocationMap, scanner);
							tokenLocationMap.clear();
						}
					} else {
						System.out.println("TokenLocation not being tracked by scanner");
					}
					if (scanner.isTrackNameLocation()) {
						Map nameLocationMap = scanner.getNameLocationMap();
						synchronized (nameLocationMap) {
							saveNameLocationMap(scanner.getStateManagerKey(), nameLocationMap, scanner);
							nameLocationMap.clear();
						}
					}
				} else {
					if (scanner == null) {
						System.out.println("ALERT! Scanner was null??");
					}
				}
				break;
			case ERROR:
				break;
			case INTERRUPTED:
				break;
			}
		} else {
			System.out.println("Scan status was null?");
		}
	}

	public Map<CharSequence, Set<CharSequence>> restoreNameLocationMap(final CharSequence name, final Object mapKey) {
		Map result = null;
		Document doc = getNameDocument(name.toString());
		String itemName = TERM_MAP_PREFIX + String.valueOf(mapKey);
		if (doc.hasItem(itemName)) {
			result = doc.getItemValue(itemName, Map.class);
		} else {
			result = new ConcurrentHashMap<CaseInsensitiveString, Set<String>>();
		}
		return result;
	}

	public void saveNameLocationMap(final CharSequence name, final Object mapKey, final Map<CharSequence, Set<CharSequence>> map) {
		String lname = name.toString();
		Document nameDoc = getNameDocument(lname);
		nameDoc.replaceItemValue(TERM_MAP_PREFIX + String.valueOf(mapKey), map);
		nameDoc.save();
	}

	public void saveNameLocationMap(final Object mapKey, final Map<CharSequence, Map<CharSequence, Set<CharSequence>>> fullMap,
			final DocumentScanner scanner) {
		//		Document dbDoc = getDbDocument((String) mapKey);
		//		if (scanner.getCollection() != null) {
		//			dbDoc.replaceItemValue(IndexDatabase.DB_DOC_LIST_NAME, scanner.getCollection());
		//		}
		//
		//		if (scanner.getSorter() != null) {
		//			dbDoc.replaceItemValue(IndexDatabase.DB_DOC_SORTER_NAME, scanner.getSorter());
		//		}
		//		dbDoc.save();
		Set<CharSequence> keySet = fullMap.keySet();
		for (CharSequence cis : keySet) {
			Map<CharSequence, Set<CharSequence>> tlValue = fullMap.get(cis);
			String name = cis.toString();
			Document nameDoc = getNameDocument(name);
			String itemName = TERM_MAP_PREFIX + String.valueOf(mapKey);
			nameDoc.replaceItemValue(itemName, tlValue);
			if (nameDoc.save()) {
				//				System.out.println("Saved term doc for " + term);
			}
		}
	}
}

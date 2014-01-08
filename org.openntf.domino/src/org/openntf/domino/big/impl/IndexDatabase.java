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
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.DbDirectory;
import org.openntf.domino.Document;
import org.openntf.domino.Item;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.ViewColumn;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewNavigator;
import org.openntf.domino.helpers.DocumentScanner;
import org.openntf.domino.types.CaseInsensitiveString;
import org.openntf.domino.utils.Factory;

/**
 * @author Nathan T. Freeman
 * 
 */
public class IndexDatabase {
	private static final Logger log_ = Logger.getLogger(IndexDatabase.class.getName());
	private static final long serialVersionUID = 1L;

	public static final String TERM_VIEW_NAME = "$TermIndex";
	public static final String TERM_FORM_NAME = "$TermDoc";
	public static final String TERM_KEY_NAME = "TermKey";
	public static final String TERM_MAP_PREFIX = "TermMap_";
	public static final String DBID_NAME = "DbidList";
	public static final String DB_VIEW_NAME = "$DbIndex";
	public static final String DB_FORM_NAME = "$DbDoc";
	public static final String DB_KEY_NAME = "DbKey";
	public static final String DB_TOKEN_LOCATION_NAME = "TokenLocationMap";
	public static final String DB_FIELD_TOKEN_NAME = "FieldTokenMap";
	public static final String DB_LAST_INDEX_NAME = "LastIndexTime";
	public static final String[] DEFAULT_STOP_WORDS_EN = "a,able,about,across,after,all,almost,also,am,among,an,and,any,are,as,at,be,because,been,but,by,can,cannot,could,dear,did,do,does,either,else,ever,every,for,from,get,got,had,has,have,he,her,hers,him,his,how,however,i,if,in,into,is,it,its,just,least,let,like,likely,may,me,might,most,must,my,neither,no,nor,not,of,off,often,on,only,or,other,our,own,rather,said,say,says,she,should,since,so,some,than,that,the,their,them,then,there,these,they,this,tis,to,too,twas,us,wants,was,we,were,what,when,where,which,while,who,whom,why,will,with,would,yet,you,your"
			.split(",");

	private transient Database indexDb_;
	private transient View termView_;
	private transient View dbView_;
	private Set<String> stopList_;

	public static Set<String> toStringSet(final Object value) {
		Set<String> result = new HashSet<String>();
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

	public static Set<CaseInsensitiveString> toCISSet(final Object value) {
		Set<CaseInsensitiveString> result = new HashSet<CaseInsensitiveString>();
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

	public void setDatabase(final Database indexDb) {
		indexDb_ = indexDb;
	}

	private Database getIndexDb() {
		if (indexDb_ == null) {
			indexDb_ = Factory.getSession().getCurrentDatabase();
		}
		return indexDb_;
	}

	private void initIndexDb() {

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
		}
	}

	public Set<String> getStopList() {
		if (stopList_ == null) {
			stopList_ = new HashSet<String>();
			for (String s : DEFAULT_STOP_WORDS_EN) {
				stopList_.add(s);
			}
		}
		return stopList_;
	}

	public void setStopList(final Set<String> list) {
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

	public Document getDbDocument(final String dbid) {
		String key = dbid.toUpperCase();
		Document result = getIndexDb().getDocumentByKey(key, true);
		if (result.getFormName().length() < 1) {
			result.replaceItemValue("Form", DB_FORM_NAME);
			result.replaceItemValue(DB_KEY_NAME, dbid);
		}
		return result;
	}

	public Document getTermDocument(final String token) {
		String key = token.toLowerCase();

		Document result = getIndexDb().getDocumentByKey(key, true);
		if (result.getFormName().length() < 1) {
			result.replaceItemValue("Form", TERM_FORM_NAME);
			result.replaceItemValue(TERM_KEY_NAME, token);
		}
		return result;
	}

	public void scanServer(final Session session, final String serverName) {
		initIndexDb();
		DbDirectory dir = session.getDbDirectory(serverName);
		dir.setDirectoryType(DbDirectory.Type.DATABASE);
		for (Database db : dir) {
			if (!db.getReplicaID().equals(getIndexDb().getReplicaID())) {
				System.out.println("Scanning database " + db.getApiPath());
				if (!db.getFilePath().equalsIgnoreCase("redpill\\graph.nsf")) {
					try {
						scanDatabase(db);
					} catch (Throwable t) {
						t.printStackTrace();
					}
				}
			}
		}
	}

	public DocumentScanner scanDatabase(final Database db) {
		Document dbDoc = getDbDocument(db.getReplicaID());
		DocumentScanner scanner = new DocumentScanner();
		if (dbDoc.isNewNote()) {
			scanner.setStopTokenList(getStopList());
			scanner.setIgnoreDollar(true);
		} else {
			scanner.setLastScanDate((Date) dbDoc.getItemValue(DB_LAST_INDEX_NAME, Date.class));
			scanner.setStopTokenList(getStopList());
			scanner.setIgnoreDollar(true);
			Object tokenLocationObject = dbDoc.getItemValue(DB_TOKEN_LOCATION_NAME, Map.class);
			scanner.setTokenLocationMap(tokenLocationObject);
			Object fieldTokenObject = dbDoc.getItemValue(DB_FIELD_TOKEN_NAME, Map.class);
			scanner.setFieldTokenMap(fieldTokenObject);
		}
		Date scanDate = new Date();
		scanDatabase(db, scanner);
		String dbid = db.getReplicaID();
		writeResults(dbid, scanner);
		dbDoc.replaceItemValue(DB_LAST_INDEX_NAME, scanDate);
		dbDoc.replaceItemValue(DB_FIELD_TOKEN_NAME, scanner.getFieldTokenMap());
		dbDoc.save();
		return scanner;
	}

	public DocumentScanner scanDatabase(final Database db, final DocumentScanner scanner) {
		Date last = scanner.getLastScanDate();
		if (last == null)
			last = new Date(0);
		int count = db.getModifiedNoteCount(last);
		if (count > 0) {
			for (Document doc : db.getModifiedDocuments(last)) {
				scanner.processDocument(doc);
			}
		}
		return scanner;
	}

	public void writeResults(final String dbid, final DocumentScanner scanner) {
		//		DatabaseTransaction txn = indexDb_.startTransaction();
		Map<CaseInsensitiveString, Map<CaseInsensitiveString, Set<String>>> tlMap = scanner.getTokenLocationMap();
		for (CaseInsensitiveString cis : tlMap.keySet()) {
			Map<CaseInsensitiveString, Set<String>> tlValue = tlMap.get(cis);
			String term = cis.getFolded();
			Document termDoc = getTermDocument(term);
			termDoc.replaceItemValue(TERM_MAP_PREFIX + dbid, tlValue);
			termDoc.appendItemValue(DBID_NAME, dbid, true);
			termDoc.save();
		}
		//		txn.commit();
	}

	public List<String> getTermDbids(final String term) {
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
				String dbid = itemName.substring(TERM_MAP_PREFIX.length());
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

	protected int getTermDbidHitCount(final Document doc, final String dbid) {
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

	public List<IndexHit> getTermResults(final String term, final int limit, final Set<String> dbids,
			final Set<CaseInsensitiveString> itemNames, final Set<String> forms) {
		List<IndexHit> results = new ArrayList<IndexHit>();
		Document doc = getTermDocument(term);
		if (dbids == null || dbids.isEmpty()) {
			for (Item item : doc.getItems()) {
				String itemName = item.getName();
				if (itemName.startsWith(TERM_MAP_PREFIX)) {
					String dbid = itemName.substring(TERM_MAP_PREFIX.length());
					Map termMap = doc.getItemValue(itemName, Map.class);
					results.addAll(getTermResultsForItemsForms(termMap, itemNames, forms, term, dbid));
					if (limit != 0 && results.size() >= limit) {
						return results;
					}
				}
			}
		} else {
			for (String dbid : dbids) {
				String itemName = TERM_MAP_PREFIX + dbid;
				if (doc.hasItem(itemName)) {
					Map termMap = doc.getItemValue(itemName, Map.class);
					results.addAll(getTermResultsForItemsForms(termMap, itemNames, forms, term, dbid));
					if (limit != 0 && results.size() >= limit) {
						return results;
					}
				}
			}
		}
		return results;
	}

	private List<IndexHit> getTermResultsForItemsForms(final Map map, final Set<CaseInsensitiveString> itemNames, final Set<String> forms,
			final String term, final String dbid) {
		List<IndexHit> results = new ArrayList<IndexHit>();
		if (itemNames == null || itemNames.isEmpty()) {
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
		} else {
			for (CaseInsensitiveString key : itemNames) {
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

	private List<IndexHit> getTermResultsForForms(final Set<String> unids, final Set<String> forms, final String term, final String dbid,
			final String item) {
		List<IndexHit> results = new ArrayList<IndexHit>();
		if (forms == null || forms.isEmpty()) {
			if (unids != null && !unids.isEmpty()) {
				for (String unid : unids) {
					//					System.out.println("Unid is: " + unid);
					//					if (unid == null)
					//						System.out.println("unid is null in a set of " + unids.size() + " values?");
					try {
						results.add(new IndexHit(term, dbid, item, unid));
					} catch (Exception e) {
						System.out.println("Exception occured trying to create a hit for db " + dbid + " on term " + term
								+ " with a string of " + String.valueOf(unid) + " from a set of " + unids.size());
					}
				}
			}
		} else {
			for (String unid : unids) {
				String formName = unid.substring(33);
				for (String form : forms) {
					if (formName.equalsIgnoreCase(form)) {
						//						if (unid == null)
						//							System.out.println("Unid is null in a case of matching form: " + form + "???");
						results.add(new IndexHit(term, dbid, item, unid));
						break;
					}
				}
			}
		}
		return results;
	}

	public Set<String> getTermUnidInDbsItems(final String term, final Collection<String> dbids, final Collection<?> itemNames) {
		Set<String> unids = new HashSet<String>();
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
			final Collection<String> dbids, final Collection<?> itemNames) {
		Set<String> unids = new HashSet<String>();
		Document doc = getTermDocument(term);
		for (String dbid : dbids) {
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

	public Set<String> getTermUnidInItems(final String term, final Collection<String> itemNames) {
		Set<String> unids = new HashSet<String>();
		Document doc = getTermDocument(term);
		for (Item item : doc.getItems()) {
			String itemName = item.getName();
			if (itemName.startsWith(TERM_MAP_PREFIX)) {
				String dbid = itemName.substring(TERM_MAP_PREFIX.length());
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

	public Set<String> getTermUnidInDbids(final String term, final Collection<String> dbids) {
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

	public Map<String, Set<String>> getTermUnidMap(final String term) {
		Map<String, Set<String>> result = new LinkedHashMap<String, Set<String>>();
		Document doc = getTermDocument(term);
		for (Item item : doc.getItems()) {
			String itemName = item.getName();
			if (itemName.startsWith(TERM_MAP_PREFIX)) {
				String dbid = itemName.substring(TERM_MAP_PREFIX.length());
				Set<String> unids = new HashSet<String>();
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
}

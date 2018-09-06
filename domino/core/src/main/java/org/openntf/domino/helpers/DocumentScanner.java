package org.openntf.domino.helpers;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Observable;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Item;
import org.openntf.domino.RichTextItem;
import org.openntf.domino.Session;
import org.openntf.domino.big.impl.IScannerStateManager;
import org.openntf.domino.big.impl.IScannerStateManager.ScanStatus;
import org.openntf.domino.types.CaseInsensitiveString;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.TypeUtils;

public class DocumentScanner extends Observable {
	private static final Logger log_ = Logger.getLogger(DocumentScanner.class.getName());

	protected boolean trackFieldTokens_ = true;
	protected Map<CharSequence, NavigableSet<CharSequence>> fieldTokenMap_;
	//Map<FIELDNAME, Set<TOKEN>>

	protected boolean trackTokenLocation_ = true;
	protected Map<CharSequence, Map<CharSequence, Set<CharSequence>>> tokenLocationMap_;
	//Map<TERM, Map<FIELDNAME, List<UNIDS>>>

	protected boolean trackNameLocation_ = true;
	protected Map<CharSequence, Map<CharSequence, Set<CharSequence>>> nameLocationMap_;

	protected boolean trackValueLocation_ = true;
	protected Map<CharSequence, Map<CharSequence, Set<CharSequence>>> valueLocationMap_;

	protected boolean trackRichTextLocation_ = true;
	protected Map<CharSequence, Map<CharSequence, CharSequence>> richTextLocationMap_;

	protected boolean trackFieldValues_ = true;

	protected boolean splitNameTokens_ = true;
	@SuppressWarnings("rawtypes")
	protected Map<CharSequence, NavigableSet<Comparable>> fieldValueMap_;
	//Map<FIELDNAME, Set<VALUE>>

	protected boolean trackFieldTypes_ = true;
	protected Map<CharSequence, Item.Type> fieldTypeMap_;
	//Map<FIELDNAME, ITEMTYPE>

	protected Set<CharSequence> stopTokenList_;

	protected boolean trackTokenFreq_ = true;
	protected NavigableMap<CharSequence, Integer> tokenFreqMap_;
	//Map<TOKEN, INSTANCECOUNT>

	protected boolean ignoreDollar_ = true;
	protected boolean caseSensitive_ = false;
	//	private boolean caseSensitiveValues_ = false;
	protected long docCount_ = 0l;
	protected long docsToProcess_ = 0l;
	protected int docLimit_ = Integer.MAX_VALUE;
	protected Date lastScanDate_;
	protected Date lastDocModDate_;
	protected IScannerStateManager stateManager_;
	protected Object stateManagerKey_;

	protected int zeroDocCount_ = 0;
	protected int errCount_ = 0;
	protected long itemCount_ = 0l;
	protected long tokenCount_ = 0l;
	protected int reportDocCount_ = 100;

	public void setCaseSensitive(final boolean value) {
		caseSensitive_ = value;
	}

	public boolean isCaseSensitive() {
		return caseSensitive_;
	}

	/**
	 * @return the reportDocCount
	 */
	public int getReportDocCount() {
		return reportDocCount_;
	}

	/**
	 * @param reportDocCount
	 *            the reportDocCount to set
	 */
	public void setReportDocCount(final int reportDocCount) {
		reportDocCount_ = reportDocCount;
	}

	/**
	 * @return the docLimit
	 */
	public int getDocLimit() {
		return docLimit_;
	}

	/**
	 * @param docLimit
	 *            the docLimit to set
	 */
	public void setDocLimit(final int docLimit) {
		docLimit_ = docLimit;
	}

	/**
	 * @return the stateManager
	 */
	public IScannerStateManager getStateManager() {
		return stateManager_;
	}

	/**
	 * @return the stateManagerKey
	 */
	public Object getStateManagerKey() {
		return stateManagerKey_;
	}

	/**
	 * @param stateManager
	 *            the stateManager to set
	 */
	public void setStateManager(final IScannerStateManager stateManager, final Object stateManagerKey) {
		//		System.out.println("State manager being added to scanner: " + stateManager.getClass().getName() + " key: "
		//				+ String.valueOf(stateManagerKey));
		stateManager_ = stateManager;
		stateManagerKey_ = stateManagerKey;
		addObserver(stateManager);
	}

	@Override
	public void notifyObservers(final Object arg) {
		super.notifyObservers(arg);
	}

	public int getZeroDocCount() {
		return zeroDocCount_;
	}

	public int getErrCount() {
		return errCount_;
	}

	public DocumentScanner() {
	}

	/**
	 * Instantiates a new document scanner.
	 *
	 * @param stopTokenList
	 *            the stop token list
	 */
	public DocumentScanner(final Set<CharSequence> stopTokenList) {
		stopTokenList_ = stopTokenList;
	}

	public Set<CharSequence> getStopTokenList() {
		if (stopTokenList_ == null) {
			stopTokenList_ = new HashSet<CharSequence>();
		}
		return stopTokenList_;
	}

	public void setIgnoreDollar(final boolean ignore) {
		ignoreDollar_ = ignore;
	}

	public void setLastScanDate(final Date value) {
		lastScanDate_ = value;
	}

	public boolean getIgnoreDollar() {
		return ignoreDollar_;
	}

	public Date getLastScanDate() {
		if (lastScanDate_ == null) {
			lastScanDate_ = new Date(0);
		}
		return lastScanDate_;
	}

	public Date getLastDocModDate() {
		if (lastDocModDate_ == null) {
			lastDocModDate_ = new Date(0);
		}
		return lastDocModDate_;
	}

	public void setLastDocModDate(final Date value) {
		lastDocModDate_ = value;
	}

	/**
	 * Gets the field token map.
	 *
	 * @return the field token map
	 */
	public Map<CharSequence, NavigableSet<CharSequence>> getFieldTokenMap() {
		if (fieldTokenMap_ == null) {
			fieldTokenMap_ = new HashMap<CharSequence, NavigableSet<CharSequence>>();
		}
		return fieldTokenMap_;
	}

	public Map<CharSequence, Map<CharSequence, Set<CharSequence>>> getTokenLocationMap() {
		if (tokenLocationMap_ == null) {
			//			System.out.println("Setting up new tokenLocationMap for scanner");
			tokenLocationMap_ = new ConcurrentSkipListMap<CharSequence, Map<CharSequence, Set<CharSequence>>>();
		}
		return tokenLocationMap_;
	}

	public Map<CharSequence, Map<CharSequence, Set<CharSequence>>> getNameLocationMap() {
		if (nameLocationMap_ == null) {
			nameLocationMap_ = new ConcurrentSkipListMap<CharSequence, Map<CharSequence, Set<CharSequence>>>();
		}
		return nameLocationMap_;
	}

	public Map<CharSequence, Map<CharSequence, Set<CharSequence>>> getValueLocationMap() {
		if (valueLocationMap_ == null) {
			valueLocationMap_ = new ConcurrentSkipListMap<CharSequence, Map<CharSequence, Set<CharSequence>>>();
		}
		return valueLocationMap_;
	}

	public Map<CharSequence, Map<CharSequence, CharSequence>> getRichTextLocationMap() {
		if (richTextLocationMap_ == null) {
			richTextLocationMap_ = new ConcurrentSkipListMap<CharSequence, Map<CharSequence, CharSequence>>();
		}
		return richTextLocationMap_;
	}

	public Map<CharSequence, CharSequence> getRichTextLocationMap(final CharSequence value) {
		Map<CharSequence, CharSequence> result = null;
		Map<CharSequence, Map<CharSequence, CharSequence>> localMap = getRichTextLocationMap();
		result = localMap.get(value);
		if (result == null) {
			if (getStateManager() == null) {
				result = new ConcurrentHashMap<CharSequence, CharSequence>();
			} else {
				result = getStateManager().restoreRichTextLocationMap(value, getStateManagerKey());
				if (result == null) {
					errCount_++;
					result = new ConcurrentHashMap<CharSequence, CharSequence>();
				}
			}
			synchronized (localMap) {
				localMap.put(value, result);
			}
		}
		return result;
	}

	public Map<CharSequence, Set<CharSequence>> getTokenLocationMap(final CharSequence token) {
		Map<CharSequence, Set<CharSequence>> result = null;
		Map<CharSequence, Map<CharSequence, Set<CharSequence>>> localMap = getTokenLocationMap();
		result = localMap.get(token);
		if (result == null) {
			if (getStateManager() == null) {
				result = new ConcurrentHashMap<CharSequence, Set<CharSequence>>();
			} else {
				result = getStateManager().restoreTokenLocationMap(token, getStateManagerKey());
				if (result == null) {
					errCount_++;
					System.out.println(
							"Error attempting to restore tokenLocationMap for token " + token + ". This is the " + errCount_ + " error.");
					result = new ConcurrentHashMap<CharSequence, Set<CharSequence>>();
				}
			}
			synchronized (localMap) {
				localMap.put(token, result);
			}
		}
		return result;
	}

	public Map<CharSequence, Set<CharSequence>> getValueLocationMap(final CharSequence value) {
		Map<CharSequence, Set<CharSequence>> result = null;
		Map<CharSequence, Map<CharSequence, Set<CharSequence>>> localMap = getValueLocationMap();
		result = localMap.get(value);
		if (result == null) {
			if (getStateManager() == null) {
				result = new ConcurrentHashMap<CharSequence, Set<CharSequence>>();
			} else {
				result = getStateManager().restoreValueLocationMap(value, getStateManagerKey());
				if (result == null) {
					errCount_++;
					result = new ConcurrentHashMap<CharSequence, Set<CharSequence>>();
				}
			}
			synchronized (localMap) {
				localMap.put(value, result);
			}
		}
		return result;
	}

	public Map<CharSequence, Set<CharSequence>> getNameLocationMap(final CharSequence name) {
		Map<CharSequence, Set<CharSequence>> result = null;
		Map<CharSequence, Map<CharSequence, Set<CharSequence>>> localMap = getNameLocationMap();
		result = localMap.get(name);
		if (result == null) {
			if (getStateManager() == null) {
				result = new ConcurrentHashMap<CharSequence, Set<CharSequence>>();
			} else {
				result = getStateManager().restoreTokenLocationMap(name, getStateManagerKey());
				if (result == null) {
					errCount_++;
					result = new ConcurrentHashMap<CharSequence, Set<CharSequence>>();
				}
			}
			synchronized (localMap) {
				localMap.put(name, result);
			}
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	public Map<CharSequence, NavigableSet<Comparable>> getFieldValueMap() {
		if (fieldValueMap_ == null) {
			fieldValueMap_ = new HashMap<CharSequence, NavigableSet<Comparable>>();
		}
		return fieldValueMap_;
	}

	public Map<CharSequence, Item.Type> getFieldTypeMap() {
		if (fieldTypeMap_ == null) {
			fieldTypeMap_ = new HashMap<CharSequence, Item.Type>();
		}
		return fieldTypeMap_;
	}

	/**
	 * Gets the token freq map.
	 *
	 * @return the token freq map
	 */
	public NavigableMap<CharSequence, Integer> getTokenFreqMap() {
		if (tokenFreqMap_ == null) {
			tokenFreqMap_ = new ConcurrentSkipListMap<CharSequence, Integer>();
		}
		return tokenFreqMap_;
	}

	public static final Pattern REGEX_SUFFIX_TRIM = Pattern.compile("\\W*$");
	public static final Pattern REGEX_PREFIX_TRIM = Pattern.compile("^\\W*");
	public static final Pattern REGEX_PUNCTUATION = Pattern.compile("\\p{P}");
	public static final Pattern REGEX_NONALPHANUMERIC = Pattern.compile("[^a-zA-Z0-9-']");

	public static CharSequence scrubToken(final String token, final boolean caseSensitive) {
		//		Matcher puncMatch = REGEX_PUNCTUATION.matcher(token);
		//		String result = puncMatch.replaceAll("");
		Matcher pMatch = REGEX_PREFIX_TRIM.matcher(token);
		String result = pMatch.replaceAll("");
		Matcher sMatch = REGEX_SUFFIX_TRIM.matcher(result);
		result = sMatch.replaceAll("");

		result = result.trim();
		if (DominoUtils.isHex(result)) {
			return null;
		}

		return caseSensitive ? result : new CaseInsensitiveString(result.toLowerCase());
	}

	public boolean isStopped(final CharSequence token) {
		return getStopTokenList().contains(token);
	}

	Set<Integer> nonText = new HashSet<Integer>();

	public Set<Integer> getNonText() {
		return nonText;
	}

	public String getNonTextSummary() {
		StringBuilder nt = new StringBuilder();
		nt.append("[");
		for (Integer i : getNonText()) {
			nt.append(i + ":");
		}
		nt.append("]");
		return nt.toString();
	}

	public void complete() {
		//		System.out.println("COMPLETING.");
		setChanged();
		notifyObservers(ScanStatus.COMPLETE);
	}

	private org.openntf.domino.DocumentCollection collection_;

	public void processCollection() {
		//		System.out.println("DEBUG: Scanning a collection of " + collection_.getCount());
		docsToProcess_ = collection_.getCount();
		for (Document doc : collection_) {
			if (docCount_ < docLimit_) {
				if (!Thread.interrupted()) {
					processDocument(doc);
					if (docCount_ % getReportDocCount() == 0) {
						System.out.println("Document scanner has indexed " + docCount_ + " documents so far.");
					}
				}
			} else {
				break;
			}
		}
		complete();
	}

	public void processCollection(final org.openntf.domino.DocumentCollection collection) {
		setCollection(collection);
		processCollection();
	}

	private org.openntf.domino.NoteCollection noteCollection_;

	public void processNoteCollection(final org.openntf.domino.NoteCollection collection) {
		setNoteCollection(collection);
		processNoteCollection();
	}

	public void processNoteCollection() {
		//		System.out.println("DEBUG: Scanning a collection of " + collection_.getCount());
		docsToProcess_ = noteCollection_.getCount();
		for (String nid : noteCollection_) {
			if (docCount_ < docLimit_) {
				if (!Thread.interrupted()) {
					processDocument(noteCollection_.getAncestorDatabase().getDocumentByID(nid));
					if (docCount_ % getReportDocCount() == 0) {
						System.out.println("Document scanner has indexed " + docCount_ + " documents so far.");
					}
				}
			} else {
				break;
			}
		}
		complete();
	}

	public org.openntf.domino.DocumentCollection getCollection() {
		return collection_;
	}

	public void setCollection(final org.openntf.domino.DocumentCollection collection) {
		collection_ = collection;
	}

	public void setNoteCollection(final org.openntf.domino.NoteCollection collection) {
		noteCollection_ = collection;
	}

	public void processSorter(final org.openntf.domino.helpers.DocumentSorter sorter) {
		setSorter(sorter);
		processCollection(sorter.sort());
	}

	private org.openntf.domino.helpers.DocumentSorter sorter_;

	public org.openntf.domino.helpers.DocumentSorter getSorter() {
		return sorter_;
	}

	public void setSorter(final org.openntf.domino.helpers.DocumentSorter sorter) {
		sorter_ = sorter;
	}

	public org.openntf.domino.Database getCurrentDatabase() {
		if (getCollection() != null) {
			return getCollection().getAncestorDatabase();
		} else {
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	public void processDocument(final Document doc) {
		try {
			if (doc != null && !doc.isDeleted()) {
				docCount_++;
				Map<CharSequence, Item.Type> typeMap = getFieldTypeMap();
				Vector<Item> items = doc.getItems();
				boolean hasReaders = doc.hasReaders();
				String address = doc.getUniversalID() + (hasReaders ? "1" : "0") + doc.getFormName();
				//			System.out.println("TEMP DEBUG processing document " + doc.getMetaversalID() + " with " + items.size() + " items.");
				for (Item item : items) {
					if (item != null) {
						CaseInsensitiveString name = new CaseInsensitiveString(item.getName());
						if (!(name.startsWith("$") && getIgnoreDollar())) {
							//						if ("Body".equalsIgnoreCase(name.toString())) {
							//							System.out.println("TEMP DEBUG Processing a Body item");
							//						}
							try {
								boolean isRT = false;
								String value = null;
								Vector<Object> values = null;
								RichTextItem rti = null;

								switch (item.getTypeEx()) {
								case AUTHORS:
								case READERS:
								case NAMES:
								case TEXT:
									value = item.getValueString();
									values = item.getValues();
									break;
								case RICHTEXT:
									rti = (RichTextItem) item;
									isRT = true;
									break;
								default:
								}
								if (value != null && value.length() > 0 && !DominoUtils.isNumber(value)) {
									if (item.isNames() || DominoUtils.isHierarchicalName(value)) {
										if (values != null && !values.isEmpty()) {
											for (Object o : values) {
												if (o instanceof String) {
													CharSequence parmName = caseSensitive_ ? (String) o
															: new CaseInsensitiveString((String) o);
													processName(parmName, name, doc.getAncestorSession(), address);
												}
											}
										}
									} else {
										itemCount_++;
										if (values != null && !values.isEmpty()) {
											for (Object o : values) {
												processValue(name, o, address);
											}
										} else {
											processTextValue(name, value, address);
										}
									}
								}

								if (isTrackRichTextLocation() && isRT) {
									processRichText(name, rti, address);
								} else {
									//								System.out.println("TEMP DEBUG Not processing item " + name);
								}

								if (isTrackFieldTypes()) {
									if (!typeMap.containsKey(name)) {
										typeMap.put(name, item.getTypeEx());
									}
								}
							} catch (Exception e) {
								Database db = doc.getAncestorDatabase();
								System.err.println("Unable to scan item: " + name + " in Document " + doc.getNoteID() + " in database "
										+ db.getApiPath() + " due to an " + e.getClass().getName() + " with message " + e.getMessage());
								e.printStackTrace();

							}
						}
					}
				}

				setLastDocModDate(doc.getLastModifiedDate());
				setChanged();
				notifyObservers(ScanStatus.RUNNING);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public void processRichText(final CaseInsensitiveString name, final RichTextItem rtitem, final String address) {
		if (isTrackRichTextLocation()) {
			//			System.out.println("TEMP DEBUG PROCESSING rich text item " + name);
			String mid = rtitem.getAncestorDocument().getMetaversalID();
			String text = rtitem.getUnformattedText();
			if (text.length() > 2) {
				Map<CharSequence, CharSequence> tlval = getRichTextLocationMap(mid);
				tlval.put(name, text);
			}

			Scanner s = new Scanner(text);
			s.useDelimiter(REGEX_NONALPHANUMERIC);
			while (s.hasNext()) {
				CharSequence token = scrubToken(s.next(), caseSensitive_);
				if (token != null && (token.length() > 2) && !isStopped(token)) {
					tokenCount_++;
					processToken(token, name, address);
				}
			}
			s.close();

		}
	}

	public void processTextValue(final CaseInsensitiveString name, final Object value, final String address) {
		if (value instanceof CharSequence) {
			String chkVal = value.toString();
			if (DominoUtils.isUnid(chkVal) || DominoUtils.isReplicaId(chkVal) || DominoUtils.isMetaversalId(chkVal)
					|| DominoUtils.isNumber(chkVal)) {
				//				System.out.println("TEMP DEBUG skipping processing on value of '" + chkVal + "'");
				return;
			}
		}
		if (value instanceof CharSequence) {
			String val = ((CharSequence) value).toString();
			Scanner s = new Scanner(val);
			s.useDelimiter(REGEX_NONALPHANUMERIC);
			while (s.hasNext()) {
				CharSequence token = scrubToken(s.next(), caseSensitive_);
				if (token != null && (token.length() > 2) && !isStopped(token)) {
					tokenCount_++;
					processToken(token, name, address);
				}
			}
			s.close();
		}
		if (isTrackValueLocation()) {
			Map<CharSequence, Set<CharSequence>> tlval = getValueLocationMap(String.valueOf(value));
			if (tlval.containsKey(name)) {
				Set<CharSequence> tllist = tlval.get(name);
				tllist.add(address);
			} else {
				Set<CharSequence> tllist = new ConcurrentSkipListSet<CharSequence>();
				tllist.add(address);
				tlval.put(name, tllist);
			}
		}
	}

	public void processValue(final CaseInsensitiveString name, final Object value, final String address) {
		if (value instanceof CharSequence) {
			String chkVal = value.toString();
			if (DominoUtils.isUnid(chkVal) || DominoUtils.isReplicaId(chkVal) || DominoUtils.isMetaversalId(chkVal)
					|| DominoUtils.isNumber(chkVal)) {
				//				System.out.println("TEMP DEBUG skipping processing on value of '" + chkVal + "'");
				return;
			}
		}
		if (value instanceof String) {
			String val = (String) value;
			Scanner s = new Scanner(val);
			s.useDelimiter(REGEX_NONALPHANUMERIC);
			while (s.hasNext()) {
				CharSequence token = scrubToken(s.next(), caseSensitive_);
				if (token != null && (token.length() > 2) && !isStopped(token)) {
					tokenCount_++;
					processToken(token, name, address);
				}
			}
			s.close();
		}
		if (isTrackFieldValues()) {
			Map<CharSequence, NavigableSet<Comparable>> vmap = getFieldValueMap();
			NavigableSet<Comparable> valueSet = null;
			if (!vmap.containsKey(name)) {
				valueSet = new ConcurrentSkipListSet<Comparable>();
				vmap.put(name, valueSet);
			} else {
				valueSet = vmap.get(name);
			}
			Comparable c = TypeUtils.toComparable(value);
			if (c != null) {
				valueSet.add(c);
			}
		}
		if (isTrackValueLocation()) {
			Map<CharSequence, Set<CharSequence>> tlval = getValueLocationMap(String.valueOf(value));
			if (tlval.containsKey(name)) {
				Set<CharSequence> tllist = tlval.get(name);
				tllist.add(address);
			} else {
				Set<CharSequence> tllist = new ConcurrentSkipListSet<CharSequence>();
				tllist.add(address);
				tlval.put(name, tllist);
			}
		}
	}

	public long getDocCount() {
		return docCount_;
	}

	public long getDocsToProcess() {
		return docsToProcess_;
	}

	public long getItemCount() {
		return itemCount_;
	}

	public long getTokenCount() {
		return tokenCount_;
	}

	protected void processName(final CharSequence name, final CharSequence itemName, final Session session, final String address) {
		Map<CharSequence, Integer> tfmap = getTokenFreqMap();
		Map<CharSequence, NavigableSet<CharSequence>> tmap = getFieldTokenMap();
		NavigableSet<CharSequence> tokenSet = null;
		if (!tmap.containsKey(itemName)) {
			tokenSet = new ConcurrentSkipListSet<CharSequence>();
			tmap.put(itemName, tokenSet);
		} else {
			tokenSet = tmap.get(itemName);
		}
		tokenSet.add(name);
		if (DominoUtils.isHierarchicalName(name.toString())) {
			CharSequence cn = caseSensitive_ ? DominoUtils.toCommonName(name.toString())
					: new CaseInsensitiveString(DominoUtils.toCommonName(name.toString()));
			tokenSet.add(cn);
			if (tfmap.containsKey(cn)) {
				tfmap.put(cn, tfmap.get(cn) + 1);
			} else {
				tfmap.put(cn, 1);
			}
			if (isTrackNameLocation()) {
				Map<CharSequence, Set<CharSequence>> tlval = getNameLocationMap(name.toString());
				if (tlval.containsKey(itemName)) {
					Set<CharSequence> tllist = tlval.get(itemName);
					tllist.add(address);
				} else {
					Set<CharSequence> tllist = new ConcurrentSkipListSet<CharSequence>();
					tllist.add(address);
					tlval.put(itemName, tllist);
				}
			}
		} else {
			CharSequence lname = caseSensitive_ ? name : new CaseInsensitiveString(name);
			if (tfmap.containsKey(lname)) {
				tfmap.put(lname, tfmap.get(lname) + 1);
			} else {
				tfmap.put(lname, 1);
			}
		}
		if (this.isSplitNameTokens()) {
			if (name instanceof String) {
				String val = (String) name;
				Scanner s = new Scanner(val);
				s.useDelimiter(REGEX_NONALPHANUMERIC);
				while (s.hasNext()) {
					CharSequence token = scrubToken(s.next(), caseSensitive_);
					if (token != null && (token.length() > 2) && !isStopped(token)) {
						tokenCount_++;
						processToken(token, itemName, address);
					}
				}
			}
		}
		if (isTrackFieldValues()) {
			Map<CharSequence, NavigableSet<Comparable>> vmap = getFieldValueMap();
			NavigableSet<Comparable> valueSet = null;
			if (!vmap.containsKey(name)) {
				valueSet = new ConcurrentSkipListSet<Comparable>();
				vmap.put(name, valueSet);
			} else {
				valueSet = vmap.get(name);
			}
			valueSet.add(name.toString());
		}

	}

	private int TEMP_COMPUTER_TRACKING = 0;

	protected void processToken(final CharSequence token, final CharSequence itemName, final String address/*, final Document doc*/) {
		if (isTrackFieldTokens()) {
			Map<CharSequence, NavigableSet<CharSequence>> tmap = getFieldTokenMap();
			if (!tmap.containsKey(itemName)) {
				NavigableSet<CharSequence> tokenSet = new ConcurrentSkipListSet<CharSequence>();
				tokenSet.add(token);
				tmap.put(itemName, tokenSet);
			} else {
				NavigableSet<CharSequence> tokenSet = tmap.get(itemName);
				tokenSet.add(token);
			}
		}
		if (isTrackTokenFreq()) {
			Map<CharSequence, Integer> tfmap = getTokenFreqMap();
			if (tfmap.containsKey(token)) {
				tfmap.put(token, tfmap.get(token) + 1);
			} else {
				tfmap.put(token, 1);
			}
		}
		if (isTrackTokenLocation()) {
			//			if ("computer".equalsIgnoreCase(token.toString()) && "body".equalsIgnoreCase(itemName.toString())) {
			//				System.out.println("TEMP DEBUG Found term 'Computer' in a Body field in document " + address + " This is the "
			//						+ ++TEMP_COMPUTER_TRACKING + " time this has happened.");
			//			}
			Map<CharSequence, Set<CharSequence>> tlval = getTokenLocationMap(token);
			if (tlval.containsKey(itemName)) {
				Set<CharSequence> tllist = tlval.get(itemName);
				tllist.add(address);
			} else {
				Set<CharSequence> tllist = new ConcurrentSkipListSet<CharSequence>();
				tllist.add(address);
				tlval.put(itemName, tllist);
			}
		}
	}

	public void setFieldTokenMap(final Map<CharSequence, NavigableSet<CharSequence>> fieldTokenMap) {
		fieldTokenMap_ = fieldTokenMap;
	}

	@SuppressWarnings("unchecked")
	public void setFieldTokenMap(final Object value) {
		if (validateFieldTokenMap(value)) {
			fieldTokenMap_ = (Map<CharSequence, NavigableSet<CharSequence>>) value;
		}
	}

	public void setTokenLocationMap(final Map<CharSequence, Map<CharSequence, Set<CharSequence>>> value) {
		tokenLocationMap_ = value;
	}

	public void setValueLocationMap(final Map<CharSequence, Map<CharSequence, Set<CharSequence>>> value) {
		valueLocationMap_ = value;
	}

	public void setNameLocationMap(final Map<CharSequence, Map<CharSequence, Set<CharSequence>>> value) {
		nameLocationMap_ = value;
	}

	@SuppressWarnings("unchecked")
	public void setTokenLocationMap(final Object value) {
		//		System.out.println("Setting tokenLocationMap to a " + value.getClass().getName());

		if (validateTokenLocationMap(value)) {
			setTokenLocationMap((Map<CharSequence, Map<CharSequence, Set<CharSequence>>>) value);
		} else {
			System.out.println("Proposed TokenLocationMap didn't validate");
		}
	}

	@SuppressWarnings("rawtypes")
	public void setFieldValueMap(final Map<CharSequence, NavigableSet<Comparable>> fieldValueMap) {
		fieldValueMap_ = fieldValueMap;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setFieldValueMap(final Object value) {
		if (DocumentScanner.validateFieldValueMap(value)) {
			fieldValueMap_ = (Map<CharSequence, NavigableSet<Comparable>>) value;
		}
	}

	public void setFieldTypeMap(final Map<CharSequence, Item.Type> fieldTypeMap) {
		fieldTypeMap_ = fieldTypeMap;
	}

	@SuppressWarnings("unchecked")
	public void setFieldTypeMap(final Object value) {
		if (DocumentScanner.validateFieldTypeMap(value)) {
			fieldTypeMap_ = (Map<CharSequence, Item.Type>) value;
		}
	}

	public void setTokenFreqMap(final NavigableMap<CharSequence, Integer> tokenFreqMap) {
		tokenFreqMap_ = tokenFreqMap;
	}

	@SuppressWarnings("unchecked")
	public void setTokenFreqMap(final Object value) {
		if (DocumentScanner.validateTokenFreqMap(value)) {
			tokenFreqMap_ = (NavigableMap<CharSequence, Integer>) value;
		}
	}

	/**
	 * @return the trackFieldTokens
	 */
	public boolean isTrackFieldTokens() {
		return trackFieldTokens_;
	}

	/**
	 * @param trackFieldTokens
	 *            the trackFieldTokens to set
	 */
	public void setTrackFieldTokens(final boolean trackFieldTokens) {
		trackFieldTokens_ = trackFieldTokens;
	}

	/**
	 * @return the trackTokenLocation
	 */
	public boolean isTrackTokenLocation() {
		return trackTokenLocation_;
	}

	public boolean isTrackValueLocation() {
		return trackValueLocation_;
	}

	public boolean isTrackRichTextLocation() {
		return trackRichTextLocation_;
	}

	/**
	 * @return the trackNameLocation
	 */
	public boolean isTrackNameLocation() {
		return trackNameLocation_;
	}

	/**
	 * @param trackTokenLocation
	 *            the trackTokenLocation to set
	 */
	public void setTrackTokenLocation(final boolean trackTokenLocation) {
		trackTokenLocation_ = trackTokenLocation;
	}

	public void setTrackValueLocation(final boolean trackValueLocation) {
		trackValueLocation_ = trackValueLocation;
	}

	public void setTrackRichTextLocation(final boolean trackRichTextLocation) {
		trackRichTextLocation_ = trackRichTextLocation;
	}

	/**
	 * @param trackNameLocation
	 *            the trackNameLocation to set
	 */
	public void setTrackNameLocation(final boolean trackNameLocation) {
		trackNameLocation_ = trackNameLocation;
	}

	/**
	 * @return the trackFieldValues
	 */
	public boolean isTrackFieldValues() {
		return trackFieldValues_;
	}

	/**
	 * @param trackFieldValues
	 *            the trackFieldValues to set
	 */
	public void setTrackFieldValues(final boolean trackFieldValues) {
		trackFieldValues_ = trackFieldValues;
	}

	/**
	 * @return the trackFieldTypes
	 */
	public boolean isTrackFieldTypes() {
		return trackFieldTypes_ || trackFieldValues_;
	}

	/**
	 * @param trackFieldTypes
	 *            the trackFieldTypes to set
	 */
	public void setTrackFieldTypes(final boolean trackFieldTypes) {
		trackFieldTypes_ = trackFieldTypes;
	}

	/**
	 * @return the trackTokenFreq
	 */
	public boolean isTrackTokenFreq() {
		return trackTokenFreq_;
	}

	public boolean isSplitNameTokens() {
		return splitNameTokens_;
	}

	public void setSplitNameTokens(final boolean splitNameTokens) {
		splitNameTokens_ = splitNameTokens;
	}

	/**
	 * @param trackTokenFreq
	 *            the trackTokenFreq to set
	 */
	public void setTrackTokenFreq(final boolean trackTokenFreq) {
		trackTokenFreq_ = trackTokenFreq;
	}

	/**
	 * @param stopTokenList
	 *            the stopTokenList to set
	 */
	public void setStopTokenList(final Set<?> value) {
		stopTokenList_ = new HashSet<CharSequence>();
		for (Object o : value) {
			if (o instanceof CaseInsensitiveString) {
				stopTokenList_.add((CaseInsensitiveString) o);
			} else if (o instanceof String) {
				stopTokenList_.add(new CaseInsensitiveString((String) o));
			} else {
				stopTokenList_.add(new CaseInsensitiveString(String.valueOf(o)));
			}
		}
	}

	public void removeDocument(final Document doc) {
		String mid = doc.getMetaversalID();
		removeDocument(mid);
	}

	public void removeDocument(final Database db, final String noteid) {
		String unid = db.getUNID(noteid);
		String mid = (db.getReplicaID() + unid).toLowerCase();
		removeDocument(mid);
	}

	public void removeDocument(final String metaversalid) {
		//TODO NTF
	}

	@SuppressWarnings("rawtypes")
	public static boolean validateFieldTokenMap(final Object obj) {
		boolean result = false;
		if (obj == null) {
			return result;
		}
		Class<?> clazz = obj.getClass();
		//Map<CaseInsensitiveString, NavigableSet<CaseInsensitiveString>>
		if (Map.class.isAssignableFrom(clazz)) {
			if (((Map) obj).isEmpty()) {
				return false;
			}
			Set keys = ((Map) obj).keySet();
			Object keyObj = keys.iterator().next();
			if (CaseInsensitiveString.class.isAssignableFrom(keyObj.getClass())) {
				Object valObj = ((Map) obj).get(keyObj);
				if (NavigableSet.class.isAssignableFrom(valObj.getClass())) {
					if (((NavigableSet) valObj).isEmpty()) {
						return false;
					}
					Object tokenObj = ((NavigableSet) valObj).iterator().next();
					if (CaseInsensitiveString.class.isAssignableFrom(tokenObj.getClass())) {
						result = true;
					}
				}
			}
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	public static boolean validateFieldValueMap(final Object obj) {
		boolean result = false;
		if (obj == null) {
			return result;
		}
		Class<?> clazz = obj.getClass();
		//Map<CaseInsensitiveString, NavigableSet<Comparable>>
		if (Map.class.isAssignableFrom(clazz)) {
			if (((Map) obj).isEmpty()) {
				return false;
			}
			Set keys = ((Map) obj).keySet();
			Object keyObj = keys.iterator().next();
			if (CaseInsensitiveString.class.isAssignableFrom(keyObj.getClass())) {
				Object valObj = ((Map) obj).get(keyObj);
				if (NavigableSet.class.isAssignableFrom(valObj.getClass())) {
					if (((NavigableSet) valObj).isEmpty()) {
						return false;
					}
					Object tokenObj = ((NavigableSet) valObj).iterator().next();
					if (Comparable.class.isAssignableFrom(tokenObj.getClass())) {
						result = true;
					}
				}
			}
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	public static boolean validateFieldTypeMap(final Object obj) {
		boolean result = false;
		if (obj == null) {
			return result;
		}
		Class<?> clazz = obj.getClass();
		//Map<CaseInsensitiveString, Integer>
		if (Map.class.isAssignableFrom(clazz)) {
			if (((Map) obj).isEmpty()) {
				return false;
			}
			Set keys = ((Map) obj).keySet();
			Object keyObj = keys.iterator().next();
			if (CaseInsensitiveString.class.isAssignableFrom(keyObj.getClass())) {
				Object valObj = ((Map) obj).get(keyObj);
				if (Integer.class.isAssignableFrom(valObj.getClass())) {
					result = true;
				}
			}
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	public static boolean validateTokenFreqMap(final Object obj) {
		boolean result = false;
		if (obj == null) {
			return result;
		}
		Class<?> clazz = obj.getClass();
		//NavigableMap<CaseInsensitiveString, Integer>
		if (NavigableMap.class.isAssignableFrom(clazz)) {
			if (((NavigableMap) obj).isEmpty()) {
				return false;
			}
			Set keys = ((NavigableMap) obj).keySet();
			Object keyObj = keys.iterator().next();
			if (CaseInsensitiveString.class.isAssignableFrom(keyObj.getClass())) {
				Object valObj = ((NavigableMap) obj).get(keyObj);
				if (Integer.class.isAssignableFrom(valObj.getClass())) {
					result = true;
				}
			}
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	public static boolean validateTokenLocationMap(final Object obj) {
		boolean result = false;
		if (obj == null) {
			return result;
		}
		Class<?> clazz = obj.getClass();
		//Map<CaseInsensitiveString, Map<CaseInsensitiveString, Set<String>>>
		if (Map.class.isAssignableFrom(clazz)) {
			if (((Map) obj).isEmpty()) {
				System.out.println("Map is empty so not valid TokenLocationMap");
				return false;
			}
			Set keys = ((Map) obj).keySet();
			Object keyObj = keys.iterator().next();
			if (CaseInsensitiveString.class.isAssignableFrom(keyObj.getClass())) {
				Object valObj = ((Map) obj).get(keyObj);
				if (Map.class.isAssignableFrom(valObj.getClass())) {
					if (((Map) valObj).isEmpty()) {
						System.out.println("Submap is empty so not valid TokenLocationMap");
						return false;
					}
					Set subkeys = ((Map) valObj).keySet();
					for (Object subkeyObj : subkeys) {
						if (CaseInsensitiveString.class.isAssignableFrom(subkeyObj.getClass())) {
							Object subvalObj = ((Map) valObj).get(subkeyObj);
							if (subvalObj != null) {
								if (Set.class.isAssignableFrom(subvalObj.getClass())) {
									for (Object unidObj : (Set) subvalObj) {
										if (unidObj != null) {
											if (String.class.isAssignableFrom(unidObj.getClass())) {
												result = true;
												break;
											} else {
												System.out.println(
														"Unid is a " + unidObj.getClass().getName() + " so not valid TokenLocationMap");
											}
										}
									}
								} else {
									System.out.println("Subval is a " + subvalObj.getClass().getName() + " so not valid TokenLocationMap");
								}
							}
						} else {
							System.out.println("Subkey is a " + subkeyObj.getClass().getName() + " empty so not valid TokenLocationMap");
						}
					}
				} else {
					System.out.println("Value is a " + valObj.getClass().getName() + " so not valid TokenLocationMap");
				}
			} else {
				System.out.println("Key object is a " + keyObj.getClass().getName() + " so not valid TokenLocationMap");
			}
		}
		return result;
	}

}

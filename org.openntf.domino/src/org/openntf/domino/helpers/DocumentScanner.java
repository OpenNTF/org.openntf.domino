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
import java.util.logging.Level;
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

public class DocumentScanner extends Observable {
	private static final Logger log_ = Logger.getLogger(DocumentScanner.class.getName());

	@SuppressWarnings("rawtypes")
	public static boolean validateFieldTokenMap(final Object obj) {
		boolean result = false;
		if (obj == null)
			return result;
		Class<?> clazz = obj.getClass();
		//Map<CaseInsensitiveString, NavigableSet<CaseInsensitiveString>>
		if (Map.class.isAssignableFrom(clazz)) {
			if (((Map) obj).isEmpty())
				return false;
			Set keys = ((Map) obj).keySet();
			Object keyObj = keys.iterator().next();
			if (CaseInsensitiveString.class.isAssignableFrom(keyObj.getClass())) {
				Object valObj = ((Map) obj).get(keyObj);
				if (NavigableSet.class.isAssignableFrom(valObj.getClass())) {
					if (((NavigableSet) valObj).isEmpty())
						return false;
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
		if (obj == null)
			return result;
		Class<?> clazz = obj.getClass();
		//Map<CaseInsensitiveString, NavigableSet<Comparable>>
		if (Map.class.isAssignableFrom(clazz)) {
			if (((Map) obj).isEmpty())
				return false;
			Set keys = ((Map) obj).keySet();
			Object keyObj = keys.iterator().next();
			if (CaseInsensitiveString.class.isAssignableFrom(keyObj.getClass())) {
				Object valObj = ((Map) obj).get(keyObj);
				if (NavigableSet.class.isAssignableFrom(valObj.getClass())) {
					if (((NavigableSet) valObj).isEmpty())
						return false;
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
		if (obj == null)
			return result;
		Class<?> clazz = obj.getClass();
		//Map<CaseInsensitiveString, Integer>
		if (Map.class.isAssignableFrom(clazz)) {
			if (((Map) obj).isEmpty())
				return false;
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
		if (obj == null)
			return result;
		Class<?> clazz = obj.getClass();
		//NavigableMap<CaseInsensitiveString, Integer>
		if (NavigableMap.class.isAssignableFrom(clazz)) {
			if (((NavigableMap) obj).isEmpty())
				return false;
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
		if (obj == null)
			return result;
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
												System.out.println("Unid is a " + unidObj.getClass().getName()
														+ " so not valid TokenLocationMap");
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

	private boolean trackFieldTokens_ = true;
	private Map<CharSequence, NavigableSet<CharSequence>> fieldTokenMap_;
	//Map<FIELDNAME, Set<TOKEN>>

	private boolean trackTokenLocation_ = true;
	private Map<CharSequence, Map<CharSequence, Set<CharSequence>>> tokenLocationMap_;
	//Map<TERM, Map<FIELDNAME, List<UNIDS>>>

	private boolean trackNameLocation_ = true;
	private Map<CharSequence, Map<CharSequence, Set<CharSequence>>> nameLocationMap_;

	private boolean trackFieldValues_ = true;
	private Map<CharSequence, NavigableSet<Comparable>> fieldValueMap_;
	//Map<FIELDNAME, Set<VALUE>>

	private boolean trackFieldTypes_ = true;
	private Map<CharSequence, Integer> fieldTypeMap_;
	//Map<FIELDNAME, ITEMTYPE>

	private Set<CharSequence> stopTokenList_;

	private boolean trackTokenFreq_ = true;
	private NavigableMap<CharSequence, Integer> tokenFreqMap_;
	//Map<TOKEN, INSTANCECOUNT>

	private boolean ignoreDollar_ = true;
	private boolean caseSensitive_ = false;
	//	private boolean caseSensitiveValues_ = false;
	private long docCount_ = 0l;
	private int docLimit_ = Integer.MAX_VALUE;
	private Date lastScanDate_;
	private Date lastDocModDate_;
	private IScannerStateManager stateManager_;
	private Object stateManagerKey_;

	private int zeroDocCount_ = 0;
	private int errCount_ = 0;
	private long itemCount_ = 0l;
	private long tokenCount_ = 0l;

	public void setCaseSensitive(final boolean value) {
		caseSensitive_ = value;
	}

	public boolean getCaseSensitive() {
		return caseSensitive_;
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
					result = new ConcurrentHashMap<CharSequence, Set<CharSequence>>();
				}
			}
			synchronized (localMap) {
				localMap.put(token, result);
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

	public Map<CharSequence, NavigableSet<Comparable>> getFieldValueMap() {
		if (fieldValueMap_ == null) {
			fieldValueMap_ = new HashMap<CharSequence, NavigableSet<Comparable>>();
		}
		return fieldValueMap_;
	}

	public Map<CharSequence, Integer> getFieldTypeMap() {
		if (fieldTypeMap_ == null) {
			fieldTypeMap_ = new HashMap<CharSequence, Integer>();
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
		if (DominoUtils.isHex(result))
			return null;

		return caseSensitive ? result : new CaseInsensitiveString(result);
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
		notifyObservers(ScanStatus.COMPLETE);
	}

	private org.openntf.domino.DocumentCollection collection_;

	public void processCollection() {
		for (Document doc : collection_) {
			if (docCount_ < docLimit_) {
				processDocument(doc);
			}
		}
		complete();
	}

	public void processCollection(final org.openntf.domino.DocumentCollection collection) {
		setCollection(collection);
		processCollection();
	}

	public org.openntf.domino.DocumentCollection getCollection() {
		return collection_;
	}

	public void setCollection(final org.openntf.domino.DocumentCollection collection) {
		collection_ = collection;
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
		if (doc != null) {
			docCount_++;
			Map<CharSequence, NavigableSet<Comparable>> vmap = getFieldValueMap();
			Map<CharSequence, Integer> typeMap = getFieldTypeMap();
			Vector<Item> items = doc.getItems();
			boolean hasReaders = doc.hasReaders();
			String address = doc.getUniversalID() + (hasReaders ? "1" : "0") + doc.getFormName();
			for (Item item : items) {
				CaseInsensitiveString name = new CaseInsensitiveString(item.getName());
				if (/*lastMod.after(getLastScanDate()) && */!(name.startsWith("$") && getIgnoreDollar())) {
					try {
						String value = null;
						Vector<Object> values = null;

						switch (item.getType()) {
						case Item.AUTHORS:
						case Item.READERS:
						case Item.NAMES:
						case Item.TEXT:
							value = item.getValueString();
							values = item.getValues();
							break;
						case Item.RICHTEXT:
							value = ((RichTextItem) item).getUnformattedText();
							break;
						default:
						}
						if (value != null && value.length() > 0 && !DominoUtils.isNumber(value)) {
							if (item.isNames()) {
								if (values != null && !values.isEmpty()) {
									for (Object o : values) {
										if (o instanceof String) {
											CharSequence parmName = caseSensitive_ ? (String) o : new CaseInsensitiveString((String) o);
											processName(parmName, name, doc.getAncestorSession(), address);
										}
									}
								}
							} else {
								itemCount_++;
								if (values != null && !values.isEmpty()) {
									for (Object o : values) {
										if (o instanceof String) {
											String val = (String) o;
											Scanner s = new Scanner(val);
											s.useDelimiter(REGEX_NONALPHANUMERIC);
											while (s.hasNext()) {
												CharSequence token = scrubToken(s.next(), caseSensitive_);
												if (token != null && (token.length() > 2) && !isStopped(token)) {
													tokenCount_++;
													processToken(token, name, address, doc);
												}
											}
										}
									}
								} else {
									Scanner s = new Scanner(value);
									s.useDelimiter(REGEX_NONALPHANUMERIC);
									while (s.hasNext()) {
										CharSequence token = scrubToken(s.next(), caseSensitive_);
										if (token != null && (token.length() > 2) && !isStopped(token)) {
											tokenCount_++;
											processToken(token, name, address, doc);
										}
									}
								}
							}
						}

						if (isTrackFieldTypes()) {
							if (!typeMap.containsKey(name)) {
								typeMap.put(name, item.getType());
							}
						}
						if (isTrackFieldValues()) {
							if (typeMap.get(name).equals(item.getType())) {
								Vector<Object> vals = null;
								vals = item.getValues();
								if (vals != null && !vals.isEmpty()) {
									NavigableSet<Comparable> valueSet = new ConcurrentSkipListSet<Comparable>();
									if (!vmap.containsKey(name)) {
										vmap.put(name, valueSet);
									} else {
										valueSet = vmap.get(name);
									}
									java.util.Collection<Comparable> c = DominoUtils.toComparable(vals);
									if (!c.isEmpty()) {
										valueSet.addAll(c);
									}
								}
							}
						}
					} catch (Exception e) {
						Database db = doc.getAncestorDatabase();
						log_.log(Level.WARNING,
								"Unable to scan item: " + name + " in Document " + doc.getNoteID() + " in database " + db.getFilePath(), e);
					}
				}
			}

			setLastDocModDate(doc.getLastModifiedDate());
			setChanged();
			notifyObservers(ScanStatus.RUNNING);
		}
	}

	public long getDocCount() {
		return docCount_;
	}

	public long getItemCount() {
		return itemCount_;
	}

	public long getTokenCount() {
		return tokenCount_;
	}

	private void processName(final CharSequence name, final CharSequence itemName, final Session session, final String address) {
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
			CharSequence cn = caseSensitive_ ? DominoUtils.toCommonName(name.toString()) : new CaseInsensitiveString(
					DominoUtils.toCommonName(name.toString()));
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
	}

	private void processToken(final CharSequence token, final CharSequence itemName, final String address, final Document doc) {

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

	public void setFieldTokenMap(final Object value) {
		if (validateFieldTokenMap(value)) {
			fieldTokenMap_ = (Map) value;
		}
	}

	public void setTokenLocationMap(final Map<CharSequence, Map<CharSequence, Set<CharSequence>>> value) {
		//		System.out.println("Setting tokenLocationMap to a " + value.getClass().getName());
		tokenLocationMap_ = value;
	}

	public void setTokenLocationMap(final Object value) {
		//		System.out.println("Setting tokenLocationMap to a " + value.getClass().getName());

		if (validateTokenLocationMap(value)) {
			setTokenLocationMap((Map<CharSequence, Map<CharSequence, Set<CharSequence>>>) value);
		} else {
			System.out.println("Proposed TokenLocationMap didn't validate");
		}
	}

	public void setFieldValueMap(final Map<CharSequence, NavigableSet<Comparable>> fieldValueMap) {
		fieldValueMap_ = fieldValueMap;
	}

	public void setFieldValueMap(final Object value) {
		if (DocumentScanner.validateFieldValueMap(value)) {
			fieldValueMap_ = (Map) value;
		}
	}

	public void setFieldTypeMap(final Map<CharSequence, Integer> fieldTypeMap) {
		fieldTypeMap_ = fieldTypeMap;
	}

	public void setFieldTypeMap(final Object value) {
		if (DocumentScanner.validateFieldTypeMap(value)) {
			fieldTypeMap_ = (Map) value;
		}
	}

	public void setTokenFreqMap(final NavigableMap<CharSequence, Integer> tokenFreqMap) {
		tokenFreqMap_ = tokenFreqMap;
	}

	public void setTokenFreqMap(final Object value) {
		if (DocumentScanner.validateTokenFreqMap(value)) {
			tokenFreqMap_ = (NavigableMap) value;
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
}

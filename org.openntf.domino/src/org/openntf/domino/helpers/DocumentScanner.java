package org.openntf.domino.helpers;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
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
import org.openntf.domino.Name;
import org.openntf.domino.RichTextItem;
import org.openntf.domino.Session;
import org.openntf.domino.types.CaseInsensitiveString;
import org.openntf.domino.utils.DominoUtils;

public class DocumentScanner {
	private static final Logger log_ = Logger.getLogger(DocumentScanner.class.getName());

	public static boolean validateFieldTokenMap(final Object obj) {
		boolean result = false;
		if (obj == null)
			return result;
		Class<?> clazz = obj.getClass();
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

	public static boolean validateFieldValueMap(final Object obj) {
		boolean result = false;
		if (obj == null)
			return result;
		Class<?> clazz = obj.getClass();
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

	public static boolean validateFieldTypeMap(final Object obj) {
		boolean result = false;
		if (obj == null)
			return result;
		Class<?> clazz = obj.getClass();
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

	public static boolean validateTokenFreqMap(final Object obj) {
		boolean result = false;
		if (obj == null)
			return result;
		Class<?> clazz = obj.getClass();
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

	public static boolean validateTokenLocationMap(final Object obj) {
		boolean result = false;
		if (obj == null)
			return result;
		Class<?> clazz = obj.getClass();
		if (Map.class.isAssignableFrom(clazz)) {
			if (((Map) obj).isEmpty())
				return false;
			Set keys = ((Map) obj).keySet();
			Object keyObj = keys.iterator().next();
			if (CaseInsensitiveString.class.isAssignableFrom(keyObj.getClass())) {
				Object valObj = ((Map) obj).get(keyObj);
				if (Map.class.isAssignableFrom(valObj.getClass())) {
					if (((Map) valObj).isEmpty())
						return false;
					Set subkeys = ((Map) valObj).keySet();
					Object subkeyObj = keys.iterator().next();
					if (CaseInsensitiveString.class.isAssignableFrom(subkeyObj.getClass())) {
						Object subvalObj = ((Map) valObj).get(subkeyObj);
						if (subvalObj != null) {
							if (Set.class.isAssignableFrom(subvalObj.getClass())) {
								if (((Set) subvalObj).isEmpty())
									return false;
								Object unidObj = ((Set) subvalObj).iterator().next();
								if (CaseInsensitiveString.class.isAssignableFrom(unidObj.getClass())) {
									result = true;
								}
							}
						}
					}
				}
			}
		}
		return result;
	}

	private Map<CaseInsensitiveString, NavigableSet<CaseInsensitiveString>> fieldTokenMap_;
	//Map<FIELDNAME, Set<TOKEN>>

	private Map<CaseInsensitiveString, Map<CaseInsensitiveString, Set<String>>> tokenLocationMap_;
	//Map<TERM, Map<FIELDNAME, List<UNIDS>>>

	private Map<CaseInsensitiveString, NavigableSet<Comparable>> fieldValueMap_;
	//Map<FIELDNAME, Set<VALUE>>

	private Map<CaseInsensitiveString, Integer> fieldTypeMap_;
	//Map<FIELDNAME, ITEMTYPE>

	private Set<CaseInsensitiveString> stopTokenList_;

	private NavigableMap<CaseInsensitiveString, Integer> tokenFreqMap_;
	//Map<TOKEN, INSTANCECOUNT>

	private boolean ignoreDollar_ = true;

	private Date lastScanDate_ = new Date(0);

	public DocumentScanner() {
	}

	/**
	 * Instantiates a new document scanner.
	 * 
	 * @param stopTokenList
	 *            the stop token list
	 */
	public DocumentScanner(final Set<CaseInsensitiveString> stopTokenList) {
		stopTokenList_ = stopTokenList;
	}

	public Set<CaseInsensitiveString> getStopTokenList() {
		if (stopTokenList_ == null) {
			stopTokenList_ = new HashSet<CaseInsensitiveString>();
		}
		return stopTokenList_;
	}

	public void setStopTokenList(final Set<?> value) {
		stopTokenList_ = new HashSet<CaseInsensitiveString>();
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

	/**
	 * Gets the field token map.
	 * 
	 * @return the field token map
	 */
	public Map<CaseInsensitiveString, NavigableSet<CaseInsensitiveString>> getFieldTokenMap() {
		if (fieldTokenMap_ == null) {
			fieldTokenMap_ = new HashMap<CaseInsensitiveString, NavigableSet<CaseInsensitiveString>>();
		}
		return fieldTokenMap_;
	}

	public Map<CaseInsensitiveString, Map<CaseInsensitiveString, Set<String>>> getTokenLocationMap() {
		if (tokenLocationMap_ == null) {
			tokenLocationMap_ = new ConcurrentSkipListMap<CaseInsensitiveString, Map<CaseInsensitiveString, Set<String>>>();
		}
		return tokenLocationMap_;
	}

	public Map<CaseInsensitiveString, Set<String>> getTokenLocationMap(final CaseInsensitiveString token) {
		Map<CaseInsensitiveString, Set<String>> result = null;
		result = getTokenLocationMap().get(token);
		if (result == null) {
			result = new ConcurrentHashMap<CaseInsensitiveString, Set<String>>();
			synchronized (result) {
				getTokenLocationMap().put(token, result);
			}

		}
		return result;
	}

	public Map<CaseInsensitiveString, NavigableSet<Comparable>> getFieldValueMap() {
		if (fieldValueMap_ == null) {
			fieldValueMap_ = new HashMap<CaseInsensitiveString, NavigableSet<Comparable>>();
		}
		return fieldValueMap_;
	}

	public Map<CaseInsensitiveString, Integer> getFieldTypeMap() {
		if (fieldTypeMap_ == null) {
			fieldTypeMap_ = new HashMap<CaseInsensitiveString, Integer>();
		}
		return fieldTypeMap_;
	}

	/**
	 * Gets the token freq map.
	 * 
	 * @return the token freq map
	 */
	public NavigableMap<CaseInsensitiveString, Integer> getTokenFreqMap() {
		if (tokenFreqMap_ == null) {
			tokenFreqMap_ = new ConcurrentSkipListMap<CaseInsensitiveString, Integer>();
		}
		return tokenFreqMap_;
	}

	public static final Pattern REGEX_SUFFIX_TRIM = Pattern.compile("\\W*$");
	public static final Pattern REGEX_PREFIX_TRIM = Pattern.compile("^\\W*");
	public static final Pattern REGEX_PUNCTUATION = Pattern.compile("\\p{P}");
	public static final Pattern REGEX_NONALPHANUMERIC = Pattern.compile("[^a-zA-Z0-9-]");

	public static CaseInsensitiveString scrubToken(final String token) {
		Matcher puncMatch = REGEX_PREFIX_TRIM.matcher(token);
		String result = puncMatch.replaceAll("");
		Matcher pMatch = REGEX_PREFIX_TRIM.matcher(result);
		result = pMatch.replaceAll("");
		Matcher sMatch = REGEX_PREFIX_TRIM.matcher(result);
		result = sMatch.replaceAll("");
		result = result.trim();
		if (DominoUtils.isHex(result))
			return null;
		return new CaseInsensitiveString(result);
	}

	public boolean isStopped(final CaseInsensitiveString token) {
		return getStopTokenList().contains(token);
	}

	@SuppressWarnings("rawtypes")
	public void processDocument(final Document doc) {
		if (doc != null) {
			//		Map<String, NavigableSet<String>> tmap = getFieldTokenMap();
			Map<CaseInsensitiveString, NavigableSet<Comparable>> vmap = getFieldValueMap();
			//		Map<String, Map<String, List<String>>> tlmap = getTokenLocationMap();
			Map<CaseInsensitiveString, Integer> typeMap = getFieldTypeMap();
			//		Map<String, Integer> tfmap = getTokenFreqMap();
			Vector<Item> items = doc.getItems();
			String unid = doc.getUniversalID();
			//		Set<String> stopList = getStopTokenList();
			for (Item item : items) {
				if (item.getLastModifiedDate().after(getLastScanDate())) {
					CaseInsensitiveString name = new CaseInsensitiveString(item.getName());
					try {
						if (name.startsWith("$") && getIgnoreDollar())
							break;
						if (!typeMap.containsKey(name)) {
							typeMap.put(name, item.getType());
						}
						if (typeMap.get(name).equals(item.getType())) {
							Vector<Object> vals = null;
							vals = item.getValues();
							if (vals != null && !vals.isEmpty()) {
								NavigableSet<Comparable> valueSet = null;
								if (!vmap.containsKey(name)) {
									valueSet = new ConcurrentSkipListSet<Comparable>();
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
											processName(new CaseInsensitiveString((String) o), name, doc.getAncestorSession());
										}
									}
								}
							} else {
								if (values != null && !values.isEmpty()) {
									for (Object o : values) {
										if (o instanceof String) {
											String val = (String) o;
											Scanner s = new Scanner(val);
											s.useDelimiter(REGEX_NONALPHANUMERIC);
											while (s.hasNext()) {
												CaseInsensitiveString token = scrubToken(s.next());
												if (token != null && (token.length() > 2) && !isStopped(token)) {
													processToken(token, name, unid);
												}
											}
										}
									}
								} else {
									Scanner s = new Scanner(value);
									s.useDelimiter(REGEX_NONALPHANUMERIC);
									while (s.hasNext()) {
										CaseInsensitiveString token = scrubToken(s.next());
										if (token != null && (token.length() > 2) && !isStopped(token)) {
											processToken(token, name, unid);
										}
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
		}
	}

	private void processName(final CaseInsensitiveString name, final CaseInsensitiveString itemName, final Session session) {
		Map<CaseInsensitiveString, Integer> tfmap = getTokenFreqMap();
		Map<CaseInsensitiveString, NavigableSet<CaseInsensitiveString>> tmap = getFieldTokenMap();
		NavigableSet<CaseInsensitiveString> tokenSet = null;
		if (!tmap.containsKey(itemName)) {
			tokenSet = new ConcurrentSkipListSet<CaseInsensitiveString>();
			tmap.put(itemName, tokenSet);
		} else {
			tokenSet = tmap.get(itemName);
		}
		Name Nname = session.createName(name.toString());
		tokenSet.add(name);
		if (Nname.isHierarchical()) {
			CaseInsensitiveString cn = new CaseInsensitiveString(Nname.getCommon());
			tokenSet.add(cn);
			if (tfmap.containsKey(cn)) {
				tfmap.put(cn, tfmap.get(cn) + 1);
			} else {
				tfmap.put(cn, 1);
			}
		} else {
			if (tfmap.containsKey(name)) {
				tfmap.put(name, tfmap.get(name) + 1);
			} else {
				tfmap.put(name, 1);
			}
		}
	}

	private void processToken(final CaseInsensitiveString token, final CaseInsensitiveString itemName, final String unid) {
		Map<CaseInsensitiveString, Map<CaseInsensitiveString, Set<String>>> tlmap = getTokenLocationMap();
		Map<CaseInsensitiveString, Integer> tfmap = getTokenFreqMap();
		Map<CaseInsensitiveString, NavigableSet<CaseInsensitiveString>> tmap = getFieldTokenMap();

		if (!tmap.containsKey(itemName)) {
			NavigableSet<CaseInsensitiveString> tokenSet = new ConcurrentSkipListSet<CaseInsensitiveString>();
			tokenSet.add(token);
			tmap.put(itemName, tokenSet);
		} else {
			NavigableSet<CaseInsensitiveString> tokenSet = tmap.get(itemName);
			tokenSet.add(token);
		}
		if (tfmap.containsKey(token)) {
			tfmap.put(token, tfmap.get(token) + 1);
		} else {
			tfmap.put(token, 1);
		}

		if (tlmap.containsKey(token)) {
			Map<CaseInsensitiveString, Set<String>> tlval = tlmap.get(token);
			if (tlval.containsKey(itemName)) {
				Set<String> tllist = tlval.get(itemName);
				if (!tllist.contains(unid)) {
					tllist.add(unid);
				}
			} else {
				Set<String> tllist = new HashSet<String>();
				tllist.add(unid);
				tlval.put(itemName, tllist);
			}
		} else {
			Map<CaseInsensitiveString, Set<String>> tlval = new HashMap<CaseInsensitiveString, Set<String>>();
			Set<String> tllist = new HashSet<String>();
			tllist.add(unid);
			tlval.put(itemName, tllist);
			tlmap.put(token, tlval);
		}
	}

	public void setFieldTokenMap(final Map<CaseInsensitiveString, NavigableSet<CaseInsensitiveString>> fieldTokenMap) {
		fieldTokenMap_ = fieldTokenMap;
	}

	public void setFieldTokenMap(final Object value) {
		if (validateFieldTokenMap(value)) {
			fieldTokenMap_ = (Map) value;
		}
	}

	public void setTokenLocationMap(final Map<CaseInsensitiveString, Map<CaseInsensitiveString, Set<String>>> value) {
		tokenLocationMap_ = value;
	}

	public void setTokenLocationMap(final Object value) {
		if (validateTokenLocationMap(value)) {
			tokenLocationMap_ = (Map) value;
		}
	}

	public void setFieldValueMap(final Map<CaseInsensitiveString, NavigableSet<Comparable>> fieldValueMap) {
		fieldValueMap_ = fieldValueMap;
	}

	public void setFieldValueMap(final Object value) {
		if (DocumentScanner.validateFieldValueMap(value)) {
			fieldValueMap_ = (Map) value;
		}
	}

	public void setFieldTypeMap(final Map<CaseInsensitiveString, Integer> fieldTypeMap) {
		fieldTypeMap_ = fieldTypeMap;
	}

	public void setFieldTypeMap(final Object value) {
		if (DocumentScanner.validateFieldTypeMap(value)) {
			fieldTypeMap_ = (Map) value;
		}
	}

	public void setTokenFreqMap(final NavigableMap<CaseInsensitiveString, Integer> tokenFreqMap) {
		tokenFreqMap_ = tokenFreqMap;
	}

	public void setTokenFreqMap(final Object value) {
		if (DocumentScanner.validateTokenFreqMap(value)) {
			tokenFreqMap_ = (NavigableMap) value;
		}
	}
}

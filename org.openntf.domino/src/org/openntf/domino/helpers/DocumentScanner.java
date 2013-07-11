package org.openntf.domino.helpers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Item;
import org.openntf.domino.Name;
import org.openntf.domino.RichTextItem;
import org.openntf.domino.utils.DominoUtils;

public class DocumentScanner {
	private static final Logger log_ = Logger.getLogger(DocumentScanner.class.getName());

	private Map<String, NavigableSet<String>> fieldTokenMap_;

	private Map<String, NavigableSet<Comparable>> fieldValueMap_;

	private Map<String, Integer> fieldTypeMap_;

	private Set<String> stopTokenList_;

	private NavigableMap<String, Integer> tokenFreqMap_;

	private boolean ignoreDollar_ = true;

	public DocumentScanner() {
		stopTokenList_ = Collections.emptySet();
	}

	/**
	 * Instantiates a new document scanner.
	 * 
	 * @param stopTokenList
	 *            the stop token list
	 */
	public DocumentScanner(final Set<String> stopTokenList) {
		stopTokenList_ = stopTokenList;
	}

	public void setIgnoreDollar(final boolean ignore) {
		ignoreDollar_ = ignore;
	}

	public boolean getIgnoreDollar() {
		return ignoreDollar_;
	}

	/**
	 * Gets the field token map.
	 * 
	 * @return the field token map
	 */
	public Map<String, NavigableSet<String>> getFieldTokenMap() {
		if (fieldTokenMap_ == null) {
			fieldTokenMap_ = new HashMap<String, NavigableSet<String>>();
		}
		return fieldTokenMap_;
	}

	public Map<String, NavigableSet<Comparable>> getFieldValueMap() {
		if (fieldValueMap_ == null) {
			fieldValueMap_ = new HashMap<String, NavigableSet<Comparable>>();
		}
		return fieldValueMap_;
	}

	public Map<String, Integer> getFieldTypeMap() {
		if (fieldTypeMap_ == null) {
			fieldTypeMap_ = new HashMap<String, Integer>();
		}
		return fieldTypeMap_;
	}

	/**
	 * Gets the token freq map.
	 * 
	 * @return the token freq map
	 */
	public NavigableMap<String, Integer> getTokenFreqMap() {
		if (tokenFreqMap_ == null) {
			tokenFreqMap_ = new ConcurrentSkipListMap<String, Integer>(String.CASE_INSENSITIVE_ORDER);
		}
		return tokenFreqMap_;
	}

	@SuppressWarnings("rawtypes")
	public void processDocument(final Document doc) {

		Map<String, NavigableSet<String>> tmap = getFieldTokenMap();
		Map<String, NavigableSet<Comparable>> vmap = getFieldValueMap();
		Map<String, Integer> typeMap = getFieldTypeMap();
		Map<String, Integer> tfmap = getTokenFreqMap();
		Vector<Item> items = doc.getItems();
		for (Item item : items) {
			try {
				String name = item.getName();
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
				Vector<String> values = null;
				switch (item.getType()) {
				case Item.AUTHORS:
				case Item.READERS:
				case Item.NAMES:
				case Item.TEXT:
					value = item.getValueString();
					values = item.getValues(String.class);
					break;
				case Item.RICHTEXT:
					value = ((RichTextItem) item).getUnformattedText();
					break;
				default:

				}
				if (value != null && value.length() > 0 && !DominoUtils.isNumber(value)) {
					NavigableSet<String> tokenSet = null;

					if (!tmap.containsKey(item.getName())) {
						tokenSet = new ConcurrentSkipListSet<String>(String.CASE_INSENSITIVE_ORDER);
						tmap.put(name, tokenSet);
					} else {
						tokenSet = tmap.get(name);
					}

					if (item.isNames()) {
						if (!values.isEmpty()) {
							for (String val : values) {
								Name Nname = doc.getAncestorSession().createName(value);
								if (Nname.isHierarchical()) {
									String cn = Nname.getCommon();
									tokenSet.add(cn);
									if (tfmap.containsKey(cn)) {
										tfmap.put(cn, tfmap.get(cn) + 1);
									} else {
										tfmap.put(cn, 1);
									}
								} else {
									tokenSet.add(value);
									if (tfmap.containsKey(value)) {
										tfmap.put(value, tfmap.get(value) + 1);
									} else {
										tfmap.put(value, 1);
									}
								}
							}
						}
					} else {
						if (!values.isEmpty()) {
							for (String val : values) {
								Scanner s = new Scanner(val);
								while (s.hasNext()) {
									String token = s.next();
									token = token.replaceAll("\\W*$", "");
									token = token.replaceAll("^\\W*", "");
									token = token.trim();
									if ((token.length() > 2) && !(stopTokenList_.contains(token))) {
										tokenSet.add(token);
										if (tfmap.containsKey(token)) {
											tfmap.put(token, tfmap.get(token) + 1);
										} else {
											tfmap.put(token, 1);
										}
									}
								}
							}
						} else {
							Scanner s = new Scanner(value);
							while (s.hasNext()) {
								String token = s.next();
								token = token.replaceAll("\\W*$", "");
								token = token.replaceAll("^\\W*", "");
								token = token.trim();
								if ((token.length() > 2) && !(stopTokenList_.contains(token))) {
									tokenSet.add(token);
									if (tfmap.containsKey(token)) {
										tfmap.put(token, tfmap.get(token) + 1);
									} else {
										tfmap.put(token, 1);
									}
								}
							}
						}
					}
				}
			} catch (Exception e) {
				Database db = doc.getAncestorDatabase();
				log_.log(Level.WARNING, "Unable to scan next item in Document " + doc.getNoteID() + " in database " + db.getFilePath());
			}

		}
	}

	public void setFieldTokenMap(final Map<String, NavigableSet<String>> fieldTokenMap) {
		fieldTokenMap_ = fieldTokenMap;
	}

	public void setFieldValueMap(final Map<String, NavigableSet<Comparable>> fieldValueMap) {
		fieldValueMap_ = fieldValueMap;
	}

	public void setFieldTypeMap(final Map<String, Integer> fieldTypeMap) {
		fieldTypeMap_ = fieldTypeMap;
	}

	public void setTokenFreqMap(final NavigableMap<String, Integer> tokenFreqMap) {
		tokenFreqMap_ = tokenFreqMap;
	}

}

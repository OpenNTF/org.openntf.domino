package org.openntf.domino.helpers;

import java.io.Serializable;
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
import java.util.logging.Logger;

import org.openntf.domino.Document;
import org.openntf.domino.Item;
import org.openntf.domino.RichTextItem;
import org.openntf.domino.utils.DominoUtils;

public class DocumentScanner {
	private static final Logger log_ = Logger.getLogger(DocumentScanner.class.getName());

	private Map<String, NavigableSet<String>> fieldTokenMap_;

	private Map<String, NavigableSet<Serializable>> fieldValueMap_;

	private Map<String, Integer> fieldTypeMap_;

	private Set<String> stopTokenList_;

	private NavigableMap<String, Integer> tokenFreqMap_;

	public DocumentScanner() {
		stopTokenList_ = Collections.emptySet();
	}

	public DocumentScanner(Set<String> stopTokenList) {
		stopTokenList_ = stopTokenList;
	}

	public Map<String, NavigableSet<String>> getFieldTokenMap() {
		if (fieldTokenMap_ == null) {
			fieldTokenMap_ = new HashMap<String, NavigableSet<String>>();
		}
		return fieldTokenMap_;
	}

	public Map<String, NavigableSet<Serializable>> getFieldValueMap() {
		if (fieldValueMap_ == null) {
			fieldValueMap_ = new HashMap<String, NavigableSet<Serializable>>();
		}
		return fieldValueMap_;
	}

	public Map<String, Integer> getFieldTypeMap() {
		if (fieldTypeMap_ == null) {
			fieldTypeMap_ = new HashMap<String, Integer>();
		}
		return fieldTypeMap_;
	}

	public NavigableMap<String, Integer> getTokenFreqMap() {
		if (tokenFreqMap_ == null) {
			tokenFreqMap_ = new ConcurrentSkipListMap<String, Integer>(String.CASE_INSENSITIVE_ORDER);
		}
		return tokenFreqMap_;
	}

	public void processDocument(Document doc) {
		Map<String, NavigableSet<String>> tmap = getFieldTokenMap();
		Map<String, NavigableSet<Serializable>> vmap = getFieldValueMap();
		Map<String, Integer> typeMap = getFieldTypeMap();
		Map<String, Integer> tfmap = getTokenFreqMap();
		Vector<Item> items = doc.getItems();
		for (Item item : items) {
			String name = item.getName();
			if (!typeMap.containsKey(name)) {
				typeMap.put(name, item.getType());
			}
			if (typeMap.get(name).equals(item.getType())) {
				try {
					Vector<Object> vals = null;
					vals = item.getValues();
					if (vals != null && !vals.isEmpty()) {
						NavigableSet<Serializable> valueSet = null;
						if (!vmap.containsKey(name)) {
							valueSet = new ConcurrentSkipListSet<Serializable>();
							vmap.put(name, valueSet);
						} else {
							valueSet = vmap.get(name);
						}
						java.util.Collection<Serializable> c = DominoUtils.toSerializable(vals);
						if (!c.isEmpty()) {
							valueSet.addAll(c);
						}
					}
				} catch (Exception e) {
					DominoUtils.handleException(e);
				}
			}
			String value = null;
			switch (item.getType()) {
			case Item.TEXT:
				value = item.getValueString();
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

	public void setFieldTokenMap(Map<String, NavigableSet<String>> fieldTokenMap) {
		fieldTokenMap_ = fieldTokenMap;
	}

	public void setFieldValueMap(Map<String, NavigableSet<Serializable>> fieldValueMap) {
		fieldValueMap_ = fieldValueMap;
	}

	public void setFieldTypeMap(Map<String, Integer> fieldTypeMap) {
		fieldTypeMap_ = fieldTypeMap;
	}

	public void setTokenFreqMap(NavigableMap<String, Integer> tokenFreqMap) {
		tokenFreqMap_ = tokenFreqMap;
	}

}

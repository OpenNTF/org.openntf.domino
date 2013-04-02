/*
 * Copyright OpenNTF 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package org.openntf.domino.helpers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Logger;

import org.openntf.domino.Document;
import org.openntf.domino.Item;
import org.openntf.domino.RichTextItem;
import org.openntf.domino.utils.DominoUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class DocumentScanner.
 */
public class DocumentScanner {
	
	/** The Constant log_. */
	private static final Logger log_ = Logger.getLogger(DocumentScanner.class.getName());

	/** The stop token list_. */
	private Set<String> stopTokenList_;

	/**
	 * Instantiates a new document scanner.
	 */
	public DocumentScanner() {
		stopTokenList_ = Collections.emptySet();
	}

	/**
	 * Instantiates a new document scanner.
	 * 
	 * @param stopTokenList
	 *            the stop token list
	 */
	public DocumentScanner(Set<String> stopTokenList) {
		stopTokenList_ = stopTokenList;
	}

	/** The field token map_. */
	private Map<String, NavigableSet<String>> fieldTokenMap_;
	
	/** The token freq map_. */
	private NavigableMap<String, Integer> tokenFreqMap_;

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

	/**
	 * Sets the token freq map.
	 * 
	 * @param tokenFreqMap
	 *            the token freq map
	 */
	public void setTokenFreqMap(NavigableMap<String, Integer> tokenFreqMap) {
		tokenFreqMap_ = tokenFreqMap;
	}

	/**
	 * Sets the field token map.
	 * 
	 * @param fieldTokenMap
	 *            the field token map
	 */
	public void setFieldTokenMap(Map<String, NavigableSet<String>> fieldTokenMap) {
		fieldTokenMap_ = fieldTokenMap;
	}

	/**
	 * Process document.
	 * 
	 * @param doc
	 *            the doc
	 */
	public void processDocument(Document doc) {
		Map<String, NavigableSet<String>> tmap = getFieldTokenMap();
		Map<String, Integer> tfmap = getTokenFreqMap();

		for (Item item : doc.getItems()) {
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
					tmap.put(item.getName(), tokenSet);
				} else {
					tokenSet = tmap.get(item.getName());
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

}

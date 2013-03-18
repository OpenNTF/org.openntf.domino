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

public class DocumentScanner {
	private static final Logger log_ = Logger.getLogger(DocumentScanner.class.getName());

	private Set<String> stopTokenList_;

	public DocumentScanner() {
		stopTokenList_ = Collections.emptySet();
	}

	public DocumentScanner(Set<String> stopTokenList) {
		stopTokenList_ = stopTokenList;
	}

	private Map<String, NavigableSet<String>> fieldTokenMap_;
	private NavigableMap<String, Integer> tokenFreqMap_;

	public Map<String, NavigableSet<String>> getFieldTokenMap() {
		if (fieldTokenMap_ == null) {
			fieldTokenMap_ = new HashMap<String, NavigableSet<String>>();
		}
		return fieldTokenMap_;
	}

	public NavigableMap<String, Integer> getTokenFreqMap() {
		if (tokenFreqMap_ == null) {
			tokenFreqMap_ = new ConcurrentSkipListMap<String, Integer>(String.CASE_INSENSITIVE_ORDER);
		}
		return tokenFreqMap_;
	}

	public void setTokenFreqMap(NavigableMap<String, Integer> tokenFreqMap) {
		tokenFreqMap_ = tokenFreqMap;
	}

	public void setFieldTokenMap(Map<String, NavigableSet<String>> fieldTokenMap) {
		fieldTokenMap_ = fieldTokenMap;
	}

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

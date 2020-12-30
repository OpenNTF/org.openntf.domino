/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.big;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.big.impl.IScannerStateManager;
import org.openntf.domino.big.impl.IndexHit;
import org.openntf.domino.helpers.DocumentScanner;
import org.openntf.domino.types.CaseInsensitiveString;

@SuppressWarnings("nls")
public interface IndexDatabase extends IScannerStateManager {

	public static final String TERM_VIEW_NAME = "$TermIndex";
	public static final String TERM_FORM_NAME = "$TermDoc";
	public static final String TERM_KEY_NAME = "TermKey";
	public static final String TERM_MAP_PREFIX = "_ODA_TermMap_";

	public static final String VALUE_FORM_NAME = "$ValueDoc";
	public static final String VALUE_KEY_NAME = "ValueKey";
	public static final String VALUE_MAP_PREFIX = "_ODA_ValueMap_";

	public static final String RICH_TEXT_FORM_NAME = "$RichTextReference";
	public static final String RICH_TEXT_ID_PREFIX = "_ODA_RichText_";

	public static final String DBID_NAME = "DbidList";
	public static final String DB_VIEW_NAME = "$DbIndex";
	public static final String DB_FORM_NAME = "$DbDoc";
	public static final String DB_KEY_NAME = "DbKey";
	public static final String DB_TOKEN_LOCATION_NAME = "TokenLocationMap";
	public static final String DB_NAME_LOCATION_NAME = "NameLocationMap";
	public static final String DB_FIELD_TOKEN_NAME = "FieldTokenMap";
	public static final String DB_LAST_INDEX_NAME = "LastIndexTime";
	public static final String DB_INDEX_STATUS = "IndexStatus";
	public static final String DB_DOC_COUNT = "DocsProcessed";
	public static final String DB_DOCS_TO_PROCESS = "DocsToProcess";
	public static final String DB_DOC_LIST_NAME = "DocumentList";
	public static final String DB_TITLE_NAME = "Title";
	public static final String DB_DOC_SORTER_NAME = "DocumentSorter";
	public static final String[] DEFAULT_STOP_WORDS_EN = "a,able,about,across,after,all,almost,also,am,among,an,and,any,are,as,at,be,because,been,but,by,can,cannot,could,dear,did,do,does,either,else,ever,every,for,from,get,got,had,has,have,he,her,hers,him,his,how,however,i,if,in,into,is,it,its,just,least,let,like,likely,may,me,might,most,must,my,neither,no,nor,not,of,off,often,on,only,or,other,our,own,rather,said,say,says,she,should,since,so,some,than,that,the,their,them,then,there,these,they,this,tis,to,too,twas,us,wants,was,we,were,what,when,where,which,while,who,whom,why,will,with,would,yet,you,your"
			.split(",");

	public static class Utils {
		public static Set<CharSequence> toCISSet(final Object value) {
			Set<CharSequence> result = new HashSet<CharSequence>();
			if (value == null) {
				return result;
			}
			if (value instanceof Iterable) {
				Iterable<?> values = (Iterable<?>) value;
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

		public static Set<CharSequence> toStringSet(final Object value) {
			Set<CharSequence> result = new HashSet<CharSequence>();
			if (value == null) {
				return result;
			}
			if (value instanceof Iterable) {
				Iterable<?> values = (Iterable<?>) value;
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

		public static List<String> dbidCollToTitle(final Session session, final String serverName, final Collection<String> dbids) {
			List<String> result = new ArrayList<String>();
			for (String dbid : dbids) {
				Database db = session.getDatabase(serverName, dbid);
				if (db != null) {
					result.add(db.getTitle() + "|" + dbid);
				}
			}
			return result;
		}

		public static List<String> dbMapToCheckbox(final Session session, final String serverName, final Map<String, AtomicInteger> dbMap) {
			List<String> result = new ArrayList<String>();
			for (String dbid : dbMap.keySet()) {
				Database db = session.getDatabase(serverName, dbid);
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
	}

	public abstract void setCaseSensitive(boolean value);

	public abstract boolean getCaseSensitive();

	public abstract void setDatabase(Database indexDb);

	public abstract Database getIndexDb();

	public abstract Set<CharSequence> getStopList();

	public abstract void setStopList(Set<CharSequence> list);

	public abstract View getTermView();

	public abstract List<String> getTermStarts(String startsWith, int count);

	public abstract View getDbView();

	public abstract Document getDbDocument(CharSequence dbid);

	public abstract Document getTermDocument(CharSequence token);

	public abstract Document getNameDocument(CharSequence name);

	public abstract Document getValueDocument(CharSequence value);

	public abstract void scanServer(Session session, String serverName);

	public abstract DocumentScanner scanDatabase(Database db);

	public abstract DocumentScanner scanDatabase(Database db, DocumentScanner scanner);

	public abstract List<String> getTermDbids(CharSequence term);

	public abstract int getTermHitCount(String term);

	public abstract Set<CharSequence> getTermItemsInDbids(String term, Collection<String> dbids);

	public abstract List<IndexHit> getTermResults(CharSequence term, int limit, Set<CharSequence> dbids, Set<CharSequence> itemNames,
			Set<CharSequence> forms);

	public abstract Set<CharSequence> getTermUnidInDbsItems(String term, Collection<String> dbids, Collection<?> itemNames);

	public abstract Set<String> getTermLinksInDbsItems(Session session, String serverName, String term, Collection<CharSequence> dbids,
			Collection<?> itemNames);

	public abstract Set<CharSequence> getTermUnidInItems(CharSequence term, Collection<String> itemNames);

	public abstract Set<String> getTermUnidInDbids(CharSequence term, Collection<String> dbids);

	public abstract Map<String, Set<CharSequence>> getTermItemMap(String term);

	public abstract Map<CharSequence, Set<CharSequence>> getTermUnidMap(CharSequence term);

	//	public abstract void setLastIndexDate(Object mapKey, Date date);
	//
	//	public abstract Date getLastIndexDate(Object mapKey);
	//
	//	public abstract Map<CharSequence, Set<CharSequence>> restoreTokenLocationMap(CharSequence token, Object mapKey);
	//
	//	public abstract Map<CharSequence, Set<CharSequence>> restoreValueLocationMap(CharSequence value, Object mapKey);
	//
	//	public abstract Map<CharSequence, Set<CharSequence>> restoreNameLocationMap(CharSequence name, Object mapKey);

	//	public abstract void saveTokenLocationMap(CharSequence token, Object mapKey, Map<CharSequence, Set<CharSequence>> map);

	//	public abstract void saveTokenLocationMap(Object mapKey, Map<CharSequence, Map<CharSequence, Set<CharSequence>>> fullMap,
	//			DocumentScanner scanner);

	//	public abstract void saveNameLocationMap(CharSequence name, Object mapKey, Map<CharSequence, Set<CharSequence>> map);

	//	public abstract void saveNameLocationMap(Object mapKey, Map<CharSequence, Map<CharSequence, Set<CharSequence>>> fullMap,
	//			DocumentScanner scanner);

	//	public abstract void saveValueLocationMap(CharSequence token, Object mapKey, Map<CharSequence, Set<CharSequence>> map);

	//	public abstract void saveValueLocationMap(Object mapKey, Map<CharSequence, Map<CharSequence, Set<CharSequence>>> fullMap,
	//			DocumentScanner scanner);

	@Override
	public abstract void update(Observable o, Object arg);

}
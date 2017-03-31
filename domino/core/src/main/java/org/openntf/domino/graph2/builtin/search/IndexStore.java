package org.openntf.domino.graph2.builtin.search;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.big.IndexDatabase;
import org.openntf.domino.big.impl.IScannerStateManager;
import org.openntf.domino.big.impl.IndexHit;
import org.openntf.domino.graph2.DGraph;
import org.openntf.domino.graph2.DVertex;
import org.openntf.domino.graph2.builtin.identity.Name;
import org.openntf.domino.graph2.impl.DElementStore;
import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.helpers.DocumentScanner;
import org.openntf.domino.types.CaseInsensitiveString;

import com.tinkerpop.blueprints.Graph;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class IndexStore extends DElementStore implements IndexDatabase, IScannerStateManager {

	private IndexDatabase indexDb_;

	public IndexStore() {

	}

	private IndexDatabase getInternalIndexDatabase() {
		if (indexDb_ == null) {
			indexDb_ = new org.openntf.domino.big.impl.IndexDatabase((Database) getStoreDelegate());
			indexDb_.setCaseSensitive(false);
		}
		return indexDb_;
	}

	@Override
	public void setCaseSensitive(final boolean value) {
		getInternalIndexDatabase().setCaseSensitive(value);
	}

	@Override
	public boolean getCaseSensitive() {
		return getInternalIndexDatabase().getCaseSensitive();
	}

	@Override
	public void setDatabase(final Database indexDb) {
		getInternalIndexDatabase().setDatabase(indexDb);
	}

	@Override
	public Database getIndexDb() {
		return getInternalIndexDatabase().getIndexDb();
	}

	@Override
	public Set<CharSequence> getStopList() {
		return getInternalIndexDatabase().getStopList();
	}

	@Override
	public void setStopList(final Set<CharSequence> list) {
		getInternalIndexDatabase().setStopList(list);
	}

	@Override
	public View getTermView() {
		return getInternalIndexDatabase().getTermView();
	}

	@Override
	public List<String> getTermStarts(final String startsWith, final int count) {
		return getInternalIndexDatabase().getTermStarts(startsWith, count);
	}

	@Override
	public View getDbView() {
		return getInternalIndexDatabase().getDbView();
	}

	@Override
	public Document getDbDocument(final CharSequence dbid) {
		return getInternalIndexDatabase().getDbDocument(dbid);
	}

	@Override
	public Document getTermDocument(final CharSequence token) {
		Term term = (Term) getGraph().getVertex(token.toString().toLowerCase(), Term.class);
		return term.asDocument();
	}

	@Override
	public Document getNameDocument(final CharSequence name) {
		Name nameV = (Name) getGraph().getVertex(name.toString(), Name.class);
		return nameV.asDocument();
	}

	@Override
	public Document getValueDocument(final CharSequence value) {
		Value valueV = (Value) getGraph().getVertex(value.toString().toLowerCase(), Value.class);
		return valueV.asDocument();
	}

	@Override
	public void scanServer(final Session session, final String serverName) {
		getInternalIndexDatabase().scanServer(session, serverName);
	}

	@Override
	public DocumentScanner scanDatabase(final Database db) {
		return getInternalIndexDatabase().scanDatabase(db);
	}

	@Override
	public DocumentScanner scanDatabase(final Database db, final DocumentScanner scanner) {
		return getInternalIndexDatabase().scanDatabase(db, scanner);
	}

	@Override
	public List<String> getTermDbids(final CharSequence term) {
		return getInternalIndexDatabase().getTermDbids(term);
	}

	@Override
	public int getTermHitCount(final String term) {
		return getInternalIndexDatabase().getTermHitCount(term);
	}

	@Override
	public Set<CharSequence> getTermItemsInDbids(final String term, final Collection<String> dbids) {
		return getInternalIndexDatabase().getTermItemsInDbids(term, dbids);
	}

	@Override
	public List<IndexHit> getTermResults(final CharSequence term, final int limit, final Set<CharSequence> dbids,
			final Set<CharSequence> itemNames, final Set<CharSequence> forms) {
		return getInternalIndexDatabase().getTermResults(term, limit, dbids, itemNames, forms);
	}

	@Override
	public Set<CharSequence> getTermUnidInDbsItems(final String term, final Collection<String> dbids, final Collection<?> itemNames) {
		return getInternalIndexDatabase().getTermUnidInDbsItems(term, dbids, itemNames);
	}

	@Override
	public Set<String> getTermLinksInDbsItems(final Session session, final String serverName, final String term,
			final Collection<CharSequence> dbids, final Collection<?> itemNames) {
		return getInternalIndexDatabase().getTermLinksInDbsItems(session, serverName, term, dbids, itemNames);
	}

	@Override
	public Set<CharSequence> getTermUnidInItems(final CharSequence term, final Collection<String> itemNames) {
		return getInternalIndexDatabase().getTermUnidInItems(term, itemNames);
	}

	@Override
	public Set<String> getTermUnidInDbids(final CharSequence term, final Collection<String> dbids) {
		return getInternalIndexDatabase().getTermUnidInDbids(term, dbids);
	}

	@Override
	public Map<String, Set<CharSequence>> getTermItemMap(final String term) {
		return getInternalIndexDatabase().getTermItemMap(term);
	}

	@Override
	public Map<CharSequence, Set<CharSequence>> getTermUnidMap(final CharSequence term) {
		return getInternalIndexDatabase().getTermUnidMap(term);
	}

	@Override
	public Map<CharSequence, Set<CharSequence>> restoreTokenLocationMap(final CharSequence token, final Object mapKey) {
		Map result = null;
		Term tokenV = (Term) getGraph().getVertex(token.toString(), Term.class);
		if (tokenV == null) {
			tokenV = (Term) getGraph().addVertex(token.toString(), Term.class);
		}
		String itemName = TERM_MAP_PREFIX + String.valueOf(mapKey);
		Document doc = tokenV.asDocument();
		if (doc.hasItem(itemName)) {
			result = doc.getItemValue(itemName, Map.class);
		} else {
			result = new HashMap<CaseInsensitiveString, Set<String>>();
		}
		return result;
	}

	@Override
	public Map<CharSequence, Set<CharSequence>> restoreValueLocationMap(final CharSequence value, final Object mapKey) {
		Map result = null;
		Value valueV = (Value) getGraph().getVertex(value.toString(), Value.class);
		if (valueV == null) {
			valueV = (Value) getGraph().addVertex(value.toString(), Value.class);
		}
		String itemName = VALUE_MAP_PREFIX + String.valueOf(mapKey);
		Document doc = valueV.asDocument();
		if (doc.hasItem(itemName)) {
			result = doc.getItemValue(itemName, Map.class);
		} else {
			result = new HashMap<CaseInsensitiveString, Set<String>>();
		}
		return result;
	}

	@Override
	public Map<CharSequence, CharSequence> restoreRichTextLocationMap(final CharSequence value, final Object mapKey) {
		return new HashMap<CharSequence, CharSequence>();
	}

	protected DFramedTransactionalGraph getGraph() {
		DGraph graph = getConfiguration().getGraph();
		Graph extGraph = graph.getExtendedGraph();
		if (extGraph instanceof DFramedTransactionalGraph) {
			return (DFramedTransactionalGraph) extGraph;
		} else {
			throw new IllegalStateException("Graph is a " + graph.getClass().getName());
		}
	}

	//	@Override
	//	public void saveTokenLocationMap(final CharSequence token, final Object mapKey, final Map<CharSequence, Set<CharSequence>> map) {
	//		String strValue = token.toString();
	//		Term tokenV = (Term) getGraph().getVertex(strValue.toLowerCase(), Term.class);
	//		DVertex dv = (DVertex) tokenV.asVertex();
	//		dv.setProperty(TERM_MAP_PREFIX + String.valueOf(mapKey), map);
	//		if (tokenV.getValue() == null || tokenV.getValue().length() == 0) {
	//			tokenV.setValue(strValue);
	//		}
	//	}

	@Override
	public void saveTokenLocationMap(final Object mapKey, final Map<CharSequence, Map<CharSequence, Set<CharSequence>>> fullMap,
			final DocumentScanner scanner) {
		setLastIndexDate(mapKey, scanner.getLastDocModDate());
		Set<CharSequence> keySet = fullMap.keySet();
		if (keySet.size() > 0) {
			for (CharSequence cis : keySet) {
				Map<CharSequence, Set<CharSequence>> tlValue = fullMap.get(cis);
				String strValue = cis.toString();
				Term tokenV = (Term) getGraph().getVertex(strValue.toLowerCase(), Term.class);
				if (tokenV == null) {
					tokenV = (Term) getGraph().addVertex(strValue.toLowerCase(), Term.class);
				}
				DVertex dv = (DVertex) tokenV.asVertex();
				String itemName = TERM_MAP_PREFIX + String.valueOf(mapKey);
				dv.setProperty(itemName, tlValue);
				if (tokenV.getValue() == null || tokenV.getValue().length() == 0) {
					tokenV.setValue(strValue);
				}
			}
		} else {
			//			System.out.println("DEBUG: keyset was empty for index tokens");
		}
	}

	@Override
	public void setLastIndexDate(final Object mapKey, final Date date) {
		getInternalIndexDatabase().setLastIndexDate(mapKey, date);
	}

	@Override
	public Date getLastIndexDate(final Object mapKey) {
		return getInternalIndexDatabase().getLastIndexDate(mapKey);
	}

	@Override
	public void update(final Observable o, final Object arg) {
		try {
			IScannerStateManager.ScanStatus status = null;
			if (arg instanceof IScannerStateManager.ScanStatus) {
				status = (IScannerStateManager.ScanStatus) arg;
			}
			DocumentScanner scanner = null;
			if (o instanceof DocumentScanner) {
				scanner = (DocumentScanner) o;
			} else {
				System.out
						.println("Observable object was not a DocumentScanner. It was a " + (o == null ? "null" : o.getClass().getName()));
			}
			if (status != null) {
				switch (status) {
				case NEW:
					break;
				case RUNNING:
					//				System.out.println("DEBUG: branched to running status...");
					if (scanner != null) {
						if (scanner.isTrackTokenLocation()) {
							Map tokenLocationMap = scanner.getTokenLocationMap();
							int tlsize = tokenLocationMap.size();
							if (tlsize >= 256) {
								//								System.out.println("Processed " + scanner.getDocCount() + " documents so far, " + scanner.getItemCount()
								//										+ " items and " + scanner.getTokenCount());
								synchronized (tokenLocationMap) {
									saveTokenLocationMap(scanner.getStateManagerKey(), tokenLocationMap, scanner);
									tokenLocationMap.clear();
								}
							}
						} else {
							//							System.out.println("TokenLocation not being tracked by scanner");
						}
						if (scanner.isTrackNameLocation()) {
							Map nameLocationMap = scanner.getNameLocationMap();
							int nlsize = nameLocationMap.size();
							if (nlsize >= 128) {
								synchronized (nameLocationMap) {
									saveNameLocationMap(scanner.getStateManagerKey(), nameLocationMap, scanner);
									nameLocationMap.clear();
								}
							}
						}
						if (scanner.isTrackValueLocation()) {
							Map valueLocationMap = scanner.getValueLocationMap();
							int tlsize = valueLocationMap.size();
							if (tlsize >= 512) {
								//								System.out.println("Processed " + scanner.getDocCount() + " documents so far, " + scanner.getItemCount()
								//										+ " items and " + scanner.getTokenCount());
								synchronized (valueLocationMap) {
									saveValueLocationMap(scanner.getStateManagerKey(), valueLocationMap, scanner);
									valueLocationMap.clear();
								}
							}
						}
						if (scanner.isTrackRichTextLocation()) {
							Map richTextLocationMap = scanner.getRichTextLocationMap();
							int tlsize = richTextLocationMap.size();
							if (tlsize >= 16) {
								//								System.out.println("Processed " + scanner.getDocCount() + " documents so far, " + scanner.getItemCount()
								//										+ " items and " + scanner.getTokenCount());
								synchronized (richTextLocationMap) {
									saveRichTextLocationMap(scanner.getStateManagerKey(), richTextLocationMap, scanner);
									richTextLocationMap.clear();
								}
							}
						}
					} else {
						System.out.println("Scanner is null from notifications");
					}
					getGraph().commit();
					break;
				case COMPLETE:
					//					System.out.println("DEBUG: branched to complete status...");

					if (scanner != null) {
						if (scanner.isTrackTokenLocation()) {
							Map tokenLocationMap = scanner.getTokenLocationMap();
							synchronized (tokenLocationMap) {
								saveTokenLocationMap(scanner.getStateManagerKey(), tokenLocationMap, scanner);
								tokenLocationMap.clear();
							}
						} else {
							//							System.out.println("TokenLocation not being tracked by scanner");
						}
						if (scanner.isTrackNameLocation()) {
							Map nameLocationMap = scanner.getNameLocationMap();
							synchronized (nameLocationMap) {
								saveNameLocationMap(scanner.getStateManagerKey(), nameLocationMap, scanner);
								nameLocationMap.clear();
							}
						}
						if (scanner.isTrackValueLocation()) {
							Map valueLocationMap = scanner.getValueLocationMap();
							synchronized (valueLocationMap) {
								saveValueLocationMap(scanner.getStateManagerKey(), valueLocationMap, scanner);
								valueLocationMap.clear();
							}
						}
						if (scanner.isTrackRichTextLocation()) {
							System.out.println("TEMP DEBUG finalizing Rich Text Location map");
							Map richTextLocationMap = scanner.getRichTextLocationMap();
							synchronized (richTextLocationMap) {
								saveRichTextLocationMap(scanner.getStateManagerKey(), richTextLocationMap, scanner);
								richTextLocationMap.clear();
							}
						}
					} else {
						System.out.println("ALERT! Scanner was null??");
					}
					getGraph().commit();
					break;
				case ERROR:
					break;
				case INTERRUPTED:
					break;
				}
			} else {
				System.out.println("Scan status was null?");
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	@Override
	public Map<CharSequence, Set<CharSequence>> restoreNameLocationMap(final CharSequence name, final Object mapKey) {
		Map result = null;
		Name nameV = (Name) getGraph().addVertex(name.toString(), Name.class);
		String itemName = TERM_MAP_PREFIX + String.valueOf(mapKey);
		DVertex dv = (DVertex) nameV.asVertex();
		if (dv.hasProperty(itemName)) {
			Object raw = dv.getProperty(itemName);
			if (raw instanceof Map) {
				result = (Map) raw;
			} else {
				result = new HashMap<CaseInsensitiveString, Set<String>>();
			}
		} else {
			result = new HashMap<CaseInsensitiveString, Set<String>>();
		}
		return result;
	}

	//	@Override
	//	public void saveNameLocationMap(final CharSequence name, final Object mapKey, final Map<CharSequence, Set<CharSequence>> map) {
	//		Name nameV = (Name) getGraph().getVertex(name.toString(), Name.class);
	//		DVertex dv = (DVertex) nameV.asVertex();
	//		dv.setProperty(TERM_MAP_PREFIX + String.valueOf(mapKey), map);
	//	}

	@Override
	public void saveNameLocationMap(final Object mapKey, final Map<CharSequence, Map<CharSequence, Set<CharSequence>>> fullMap,
			final DocumentScanner scanner) {
		Set<CharSequence> keySet = fullMap.keySet();
		for (CharSequence cis : keySet) {
			Map<CharSequence, Set<CharSequence>> tlValue = fullMap.get(cis);
			String name = cis.toString();
			Name nameV = (Name) getGraph().addVertex(name, Name.class);
			DVertex dv = (DVertex) nameV.asVertex();
			String itemName = TERM_MAP_PREFIX + String.valueOf(mapKey);
			dv.setProperty(itemName, tlValue);
			Name.Utils.processName(nameV, getGraph(), scanner.isCaseSensitive(), false);
		}
	}

	@Override
	public void saveValueLocationMap(final Object mapKey, final Map<CharSequence, Map<CharSequence, Set<CharSequence>>> fullMap,
			final DocumentScanner scanner) {
		setLastIndexDate(mapKey, scanner.getLastDocModDate());
		Set<CharSequence> keySet = fullMap.keySet();
		if (keySet.size() > 0) {
			for (CharSequence cis : keySet) {
				Map<CharSequence, Set<CharSequence>> tlValue = fullMap.get(cis);
				String strValue = cis.toString();
				Value valueV = (Value) getGraph().getVertex(strValue.toLowerCase(), Value.class);
				if (valueV == null) {
					valueV = (Value) getGraph().addVertex(strValue.toLowerCase(), Value.class);
				}
				DVertex dv = (DVertex) valueV.asVertex();
				String itemName = VALUE_MAP_PREFIX + String.valueOf(mapKey);
				dv.setProperty(itemName, tlValue);
				valueV.setValue(strValue);
				Value.Utils.processValue(valueV, getGraph(), scanner.isCaseSensitive(), false);
			}
		} else {
			//			System.out.println("DEBUG: keyset was empty for index tokens");
		}
	}

	@Override
	public void saveRichTextLocationMap(final Object mapKey, final Map<CharSequence, Map<CharSequence, CharSequence>> fullMap,
			final DocumentScanner scanner) {
		setLastIndexDate(mapKey, scanner.getLastDocModDate());
		Set<CharSequence> keySet = fullMap.keySet();
		if (keySet.size() > 0) {
			//			System.out.println("TEMP DEBUG saving an RT location map of size " + keySet.size());
			for (CharSequence cis : keySet) {
				Map<CharSequence, CharSequence> tlValue = fullMap.get(cis);
				for (CharSequence itemName : tlValue.keySet()) {
					String key = RICH_TEXT_ID_PREFIX + cis.toString() + "$" + itemName.toString();
					//					System.out.println("TEMP DEBUG RT Location Map processing key " + key);
					RichTextReference rtV = (RichTextReference) getGraph().getVertex(key.toLowerCase(), RichTextReference.class);
					if (rtV == null) {
						rtV = (RichTextReference) getGraph().addVertex(key.toLowerCase(), RichTextReference.class);
					}
					rtV.setItemName(itemName.toString());
					rtV.setSourceMetaid(cis.toString());
					rtV.setTokenProcessed(false);
					RichTextReference.Utils.processContent(rtV, getGraph(), scanner.isCaseSensitive(), false);
				}
			}
		} else {
			//			System.out.println("DEBUG: keyset was empty for index tokens");
		}
	}

	//	@Override
	//	public void saveValueLocationMap(final CharSequence value, final Object mapKey, final Map<CharSequence, Set<CharSequence>> map) {
	//		String strValue = value.toString();
	//		Value valueV = (Value) getGraph().getVertex(strValue.toLowerCase(), Value.class);
	//		DVertex dv = (DVertex) valueV.asVertex();
	//		dv.setProperty(VALUE_MAP_PREFIX + String.valueOf(mapKey), map);
	//		valueV.setValue(strValue);
	//		Scanner s = new Scanner(strValue);
	//		s.useDelimiter(DocumentScanner.REGEX_NONALPHANUMERIC);
	//		while (s.hasNext()) {
	//			CharSequence token = DocumentScanner.scrubToken(s.next(), scanner.isCaseSensitive());
	//			if (token != null && (token.length() > 2)) {
	//				Term tokenV = (Term) getGraph().getVertex(token.toString().toLowerCase(), Term.class);
	//				if (tokenV.getValue() == null || tokenV.getValue().length() == 0) {
	//					tokenV.setValue(token.toString());
	//				}
	//				valueV.addTerm(tokenV);
	//			}
	//		}
	//	}

}

package org.openntf.domino.graph2.builtin.search;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Scanner;
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

	public static final String BASE_DXL = "<?xml version='1.0'?>\r\n" + "<!DOCTYPE database SYSTEM 'xmlschemas/domino_9_0_1.dtd'>\r\n"
			+ "<database xmlns='http://www.lotus.com/dxl' version='9.0' maintenanceversion='1.0'\r\n" + "  title='redpill Term Index' \r\n"
			+ " allowstoredforms='false' maintainunread='false' increasemaxfields='true'>\r\n" + "<acl maxinternetaccess='manager'>\r\n"
			+ "<aclentry name='-Default-' default='true' level='reader' createpersonalagents='false'\r\n"
			+ " createpersonalviews='false' createlsjavaagents='false' writepublicdocs='false'/>\r\n"
			+ "<aclentry name='Anonymous' level='noaccess' readpublicdocs='false' writepublicdocs='false'/>\r\n"
			+ "<aclentry name='LocalDomainServers' type='servergroup' level='manager' deletedocs='true'/>\r\n"
			+ "<aclentry name='LocalDomainAdmins' type='persongroup' level='manager' deletedocs='false'/>\r\n" + "</acl>\r\n" + "\r\n"
			+ "<launchsettings><noteslaunch whenopened='openframeset' restorelastview='true'\r\n"
			+ " frameset='$fsLaunch'/></launchsettings>\r\n" + "<note default='true' class='icon'>\r\n"
			+ "<item name='IconBitmap' summary='true'>\r\n" + "<rawitemdata type='6'>\r\n"
			+ "AiAgAgcA/////////////////+H////Af///gB///wAA//4AAH/8AAAf+AAAB/AAAADgAAAAwAAA\r\n"
			+ "AcAAAAPAAAAHwAAAD+AAAB/wAAA/+AAAf/44AH///AA///4DH///h4/////H////4/////H////4\r\n"
			+ "/////P////////////////////8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\r\n"
			+ "AAAAAAAAAAAAAAAAAAAAAA4A4AAAAAAAAAAAAAAAAADg/gDgAAAAAAAAAAAAAAAOD/7uAOAAAAAA\r\n"
			+ "AAAAAAAA4P8R/u4AAA4AAAAAAAAADg/xERH+7u8A4AAAAAAAAOD/ERERER/w/gDgAAAAAA4P8RER\r\n"
			+ "EREfDxHuAOAAAADg/xERERER8PEREe4ADgAOD/Ef8RERHw8RERERH/4A4P8RER/xEfDxEREREf8A\r\n"
			+ "AA/xH/ERER8PERERER/wAADvEREf8RHw8RERERH/AAAA4R/xER8fDxEREREf8AAAAA4RH/ER8PER\r\n"
			+ "4Q4R/wAAAAAA4RERHw8RERH87/AAAAAAAA7hEeDhEe4RHG4AAAAAAAAADu4ADhER7h/G4AAAAAAA\r\n"
			+ "AAAAAADhERH/DG4AAAAAAAAAAAAADuEf8ADG4AAAAAAAAAAAAAAO7uAADG4AAAAAAAAAAAAAAAAA\r\n"
			+ "AADG4AAAAAAAAAAAAAAAAAAADG4AAAAAAAAAAAAAAAAAAADBAAAAAAAAAAAAAAAAAAAAAIgAAAAA\r\n"
			+ "AAAAAAAAAAAAAABVAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\r\n" + "AAAAAAAAAAAAAAAAAAAAAAAAAA==\r\n"
			+ "</rawitemdata></item>\r\n" + "<item name='$LANGUAGE'><text>en</text></item>\r\n"
			+ "<item name='$DefaultFrameset'><text>$fsLaunch</text></item>\r\n"
			+ "<item name='$DefaultWebFrameset'><text>$fsLaunch</text></item>\r\n"
			+ "<item name='$FlagsNoRefresh'><text>t</text></item>\r\n" + "<item name='$Flags'><text>t7JFKzf</text></item>\r\n"
			+ "<item name='$Daos'><text>1</text></item>\r\n" + "<item name='$TITLE'><text>Graph Full-text Index</text></item></note>\r\n"
			+ "</database>";

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
		Term tokenV = (Term) getGraph().addVertex(token.toString(), Term.class);
		String itemName = TERM_MAP_PREFIX + String.valueOf(mapKey);
		DVertex dv = (DVertex) tokenV.asVertex();
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

	@Override
	public Map<CharSequence, Set<CharSequence>> restoreValueLocationMap(final CharSequence value, final Object mapKey) {
		Map result = null;
		Value valueV = (Value) getGraph().addVertex(value.toString(), Value.class);
		String itemName = VALUE_MAP_PREFIX + String.valueOf(mapKey);
		DVertex dv = (DVertex) valueV.asVertex();
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
				Term tokenV = (Term) getGraph().addVertex(strValue.toLowerCase(), Term.class);
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
							if (tlsize >= 1024) {
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
							if (tlsize >= 1024) {
								//								System.out.println("Processed " + scanner.getDocCount() + " documents so far, " + scanner.getItemCount()
								//										+ " items and " + scanner.getTokenCount());
								synchronized (valueLocationMap) {
									saveValueLocationMap(scanner.getStateManagerKey(), valueLocationMap, scanner);
									valueLocationMap.clear();
								}
							}
						}
					} else {
						System.out.println("Scanner is null from notifications");
					}
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
					} else {
						System.out.println("ALERT! Scanner was null??");
					}
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

			Scanner s = new Scanner(name);
			s.useDelimiter(DocumentScanner.REGEX_NONALPHANUMERIC);
			while (s.hasNext()) {
				CharSequence token = DocumentScanner.scrubToken(s.next(), scanner.isCaseSensitive());
				if (token != null && (token.length() > 2)) {
					Term tokenV = (Term) getGraph().addVertex(token.toString().toLowerCase(), Term.class);
					if (tokenV.getValue() == null || tokenV.getValue().length() == 0) {
						tokenV.setValue(token.toString());
					}
					nameV.addPart(tokenV);
				}
			}
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
				Value valueV = (Value) getGraph().addVertex(strValue.toLowerCase(), Value.class);
				DVertex dv = (DVertex) valueV.asVertex();
				String itemName = VALUE_MAP_PREFIX + String.valueOf(mapKey);
				dv.setProperty(itemName, tlValue);
				valueV.setValue(strValue);
				Scanner s = new Scanner(strValue);
				s.useDelimiter(DocumentScanner.REGEX_NONALPHANUMERIC);
				while (s.hasNext()) {
					CharSequence token = DocumentScanner.scrubToken(s.next(), scanner.isCaseSensitive());
					if (token != null && (token.length() > 2)) {
						Term tokenV = (Term) getGraph().addVertex(token.toString().toLowerCase(), Term.class);
						if (tokenV.getValue() == null || tokenV.getValue().length() == 0) {
							tokenV.setValue(token.toString());
						}
						valueV.addTerm(tokenV);
					}
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

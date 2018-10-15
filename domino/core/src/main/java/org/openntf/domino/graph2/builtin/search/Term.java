package org.openntf.domino.graph2.builtin.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openntf.domino.Document;
import org.openntf.domino.big.IndexDatabase;
import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.builtin.DVertexFrame;
import org.openntf.domino.graph2.builtin.identity.Name;
import org.openntf.domino.graph2.builtin.identity.Name.ContainsPart;
import org.openntf.domino.graph2.builtin.search.RichTextReference.RichTextContainsTerm;
import org.openntf.domino.graph2.builtin.search.Value.ContainsTerm;
import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.modules.javahandler.JavaHandler;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerClass;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerContext;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@SuppressWarnings("rawtypes")
@TypeValue(IndexDatabase.TERM_FORM_NAME)
@JavaHandlerClass(Term.TermImpl.class)
public interface Term extends DVertexFrame {

	@TypedProperty("value")
	public String getValue();

	@TypedProperty("value")
	public void setValue(String value);

	@TypedProperty("lemma")
	public String getLemma();

	@TypedProperty("lemma")
	public void setLemma(String lemma);

	// interesting parts of speech
	// NNP, VB, JJ, NNS, NN, VBD, CD, JJS, VBN, WP, RB, MD, VBG,

	// uninteresting parts of speech
	// IN, CC, HYPH, VBZ, DT, PRP, \,, ., ?, !,
	@TypedProperty("POS")
	public String getPartOfSpeech();

	@TypedProperty("POS")
	public void setPartOfSpeech(String pos);

	@JavaHandler
	@TypedProperty("Hits")
	public Map getHits();

	@JavaHandler
	public Map getHits(final Map<CharSequence, Set<CharSequence>> filterMap);

	@JavaHandler
	public void setHits(Map<CharSequence, Set<CharSequence>> hitsMap, String replicaid);

	@JavaHandler
	public Map<CharSequence, Set<CharSequence>> getHits(String replicaid);

	@JavaHandler
	@TypedProperty("HitRepls")
	public List<CharSequence> getHitRepls();

	@AdjacencyUnique(label = ContainsTerm.LABEL)
	public List<Value> getValues();

	@AdjacencyUnique(label = ContainsTerm.LABEL)
	public ContainsTerm addValue(Value value);

	@AdjacencyUnique(label = ContainsTerm.LABEL)
	public void removeValue(Value value);

	@IncidenceUnique(label = ContainsTerm.LABEL)
	public List<ContainsTerm> getContainsTerms();

	@IncidenceUnique(label = ContainsTerm.LABEL)
	public int countContainsTerms();

	@IncidenceUnique(label = ContainsTerm.LABEL)
	public void removeContainsTerm(ContainsTerm containsTerm);

	@AdjacencyUnique(label = RichTextContainsTerm.LABEL)
	public List<RichTextReference> getRichTextReferences();

	@AdjacencyUnique(label = RichTextContainsTerm.LABEL)
	public RichTextContainsTerm addRichTextReference(RichTextReference reference);

	@AdjacencyUnique(label = RichTextContainsTerm.LABEL)
	public void removeRichTextReference(RichTextReference reference);

	@IncidenceUnique(label = RichTextContainsTerm.LABEL)
	public List<RichTextContainsTerm> getRichTextContainsTerms();

	@IncidenceUnique(label = RichTextContainsTerm.LABEL)
	public int countRichTextContainsTerms();

	@IncidenceUnique(label = RichTextContainsTerm.LABEL)
	public void removeRichTextContainsTerm(RichTextContainsTerm containsTerm);

	@AdjacencyUnique(label = ContainsPart.LABEL)
	public List<Name> getNames();

	@AdjacencyUnique(label = ContainsPart.LABEL)
	public ContainsPart addName(Name name);

	@AdjacencyUnique(label = ContainsPart.LABEL)
	public void removeName(Name name);

	@IncidenceUnique(label = ContainsPart.LABEL)
	public List<ContainsPart> getContainsParts();

	@IncidenceUnique(label = ContainsPart.LABEL)
	public int countContainsParts();

	@IncidenceUnique(label = ContainsPart.LABEL)
	public void removeContainsPart(ContainsPart containsPart);

	@JavaHandler
	public boolean removeDocument(final Document document);

	public static abstract class TermImpl extends DVertexFrameImpl implements Term, JavaHandlerContext<Vertex> {

		@Override
		public Map getHits() {
			Map<CharSequence, Object> result = new LinkedHashMap<CharSequence, Object>();
			for (CharSequence replid : getHitRepls()) {
				String itemname = IndexDatabase.TERM_MAP_PREFIX + replid;
				result.put(replid, this.asVertex().getProperty(itemname));
			}
			return result;
		}

		@Override
		public boolean removeDocument(final Document document) {
			boolean result = false;
			String replid = document.getAncestorDatabase().getReplicaID();
			String unid = document.getUniversalID().toLowerCase();
			String itemname = IndexDatabase.TERM_MAP_PREFIX + replid;
			Vertex v = asVertex();
			Object raw = v.getProperty(itemname);
			if (raw != null & raw instanceof Map) {
				Map m = (Map) raw;
				for (Object key : m.keySet()) {
					Object value = m.get(key);
					if (value instanceof Collection) {
						Set<CharSequence> removeSet = new HashSet<CharSequence>();
						for (Object line : (Collection) value) {
							if (line instanceof CharSequence) {
								String address = ((CharSequence) line).toString().toLowerCase();
								if (address.startsWith(unid)) {
									removeSet.add((CharSequence) line);
								}
							}
						}
						if (!removeSet.isEmpty()) {
							result = ((Collection) value).removeAll(removeSet);
						}
					}
				}
			}
			v.setProperty(itemname, raw);
			((DFramedTransactionalGraph) g()).commit();
			return result;
		}

		@Override
		@SuppressWarnings("rawtypes")
		public Map getHits(final Map<CharSequence, Set<CharSequence>> filterMap) {
			Map<CharSequence, Map<CharSequence, List<CharSequence>>> result = new LinkedHashMap<CharSequence, Map<CharSequence, List<CharSequence>>>();
			Set<CharSequence> replicas = null;
			Set<CharSequence> forms = null;
			Set<CharSequence> fields = null;
			if (filterMap.containsKey("replica")) {
				replicas = filterMap.get("replica");
			}
			if (filterMap.containsKey("form")) {
				forms = filterMap.get("form");
			}
			if (filterMap.containsKey("field")) {
				fields = filterMap.get("field");
			}
			for (CharSequence replid : (replicas != null ? replicas : getHitRepls())) {
				Map<CharSequence, List<CharSequence>> fieldMap = new LinkedHashMap<CharSequence, List<CharSequence>>();
				String itemname = IndexDatabase.VALUE_MAP_PREFIX + replid;
				Vertex v = asVertex();
				Object raw = v.getProperty(itemname);
				if (raw != null & raw instanceof Map) {
					Map m = (Map) raw;
					for (Object key : (fields != null ? fields : m.keySet())) {
						List<CharSequence> addresses = new ArrayList<CharSequence>();
						Object raw2 = m.get(key);
						if (raw2 instanceof Collection) {
							for (Object line : (Collection) raw2) {
								if (line instanceof CharSequence) {
									String address = ((CharSequence) line).toString();
									if (forms != null) {
										for (CharSequence curForm : forms) {
											String curFormStr = curForm.toString().toLowerCase();
											if (address.toLowerCase().endsWith(curFormStr)) {
												addresses.add(address);
												break;
											}
										}
									} else {
										addresses.add(address);
									}
								}
							}
						}
						if (!addresses.isEmpty()) {
							fieldMap.put(String.valueOf(key), addresses);
						}
					}
				}
				if (!fieldMap.isEmpty()) {
					result.put(replid, fieldMap);
				}
			}
			return result;
		}

		@Override
		public void setHits(final Map<CharSequence, Set<CharSequence>> hitsMap, final String replicaid) {
			String itemname = IndexDatabase.TERM_MAP_PREFIX + replicaid;
			this.asVertex().setProperty(itemname, hitsMap);
		}

		@Override
		public Map<CharSequence, Set<CharSequence>> getHits(final String replicaid) {
			String itemname = IndexDatabase.TERM_MAP_PREFIX + replicaid;
			return this.asVertex().getProperty(itemname);
		}

		@Override
		public List<CharSequence> getHitRepls() {
			List<CharSequence> result = new ArrayList<CharSequence>();
			Map<CharSequence, Object> map = this.asMap();
			for (CharSequence cs : map.keySet()) {
				String str = cs.toString();
				if (str.startsWith(IndexDatabase.TERM_MAP_PREFIX)) {
					result.add(str.substring(IndexDatabase.TERM_MAP_PREFIX.length()));
				}
			}
			return result;
		}

	}

}

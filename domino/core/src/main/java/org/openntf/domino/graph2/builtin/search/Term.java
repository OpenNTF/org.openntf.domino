package org.openntf.domino.graph2.builtin.search;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openntf.domino.big.IndexDatabase;
import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.builtin.DVertexFrame;
import org.openntf.domino.graph2.builtin.identity.Name;
import org.openntf.domino.graph2.builtin.identity.Name.ContainsPart;
import org.openntf.domino.graph2.builtin.search.Value.ContainsTerm;

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
	@TypedProperty("HitRepls")
	public List<CharSequence> getHitRepls();

	@AdjacencyUnique(label = ContainsTerm.LABEL)
	public Iterable<Value> getValues();

	@AdjacencyUnique(label = ContainsTerm.LABEL)
	public ContainsTerm addValue(Value value);

	@AdjacencyUnique(label = ContainsTerm.LABEL)
	public void removeValue(Value value);

	@IncidenceUnique(label = ContainsTerm.LABEL)
	public Iterable<ContainsTerm> getContainsTerms();

	@IncidenceUnique(label = ContainsTerm.LABEL)
	public int countContainsTerms();

	@IncidenceUnique(label = ContainsTerm.LABEL)
	public void removeContainsTerm(ContainsTerm containsTerm);

	@AdjacencyUnique(label = ContainsPart.LABEL)
	public Iterable<Name> getNames();

	@AdjacencyUnique(label = ContainsPart.LABEL)
	public ContainsPart addName(Name name);

	@AdjacencyUnique(label = ContainsPart.LABEL)
	public void removeName(Name name);

	@IncidenceUnique(label = ContainsPart.LABEL)
	public Iterable<ContainsPart> getContainsParts();

	@IncidenceUnique(label = ContainsPart.LABEL)
	public int countContainsParts();

	@IncidenceUnique(label = ContainsPart.LABEL)
	public void removeContainsPart(ContainsPart containsPart);

	public static abstract class TermImpl extends DVertexFrameImpl implements Term, JavaHandlerContext<Vertex> {

		@Override
		public Map getHits() {
			Map<CharSequence, Object> result = new LinkedHashMap<CharSequence, Object>();
			for (CharSequence replid : getHitRepls()) {
				String itemname = IndexDatabase.TERM_MAP_PREFIX + replid;
				//				System.out.println("TEMP DEBUG getting value from item " + itemname);
				result.put(replid, this.asVertex().getProperty(itemname));
			}
			return result;
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

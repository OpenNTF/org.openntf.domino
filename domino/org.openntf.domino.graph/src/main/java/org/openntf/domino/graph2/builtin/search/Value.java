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
package org.openntf.domino.graph2.builtin.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.openntf.domino.Document;
import org.openntf.domino.big.IndexDatabase;
import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.builtin.DEdgeFrame;
import org.openntf.domino.graph2.builtin.DVertexFrame;
import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.helpers.DocumentScanner;
import org.openntf.domino.types.CaseInsensitiveString;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;
import com.tinkerpop.frames.modules.javahandler.JavaHandler;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerClass;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerContext;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue(IndexDatabase.VALUE_FORM_NAME)
@JavaHandlerClass(Value.ValueImpl.class)
@SuppressWarnings({"unchecked", "rawtypes", "nls"})
public interface Value extends DVertexFrame {
	public static final String REPLICA_KEY = "replica";
	public static final String FORM_KEY = "form";
	public static final String FIELD_KEY = "field";

	public static enum Utils {
		;
		public static void processValue(final Value value, final DFramedTransactionalGraph graph, final boolean caseSensitive,
				final boolean commit) {
			Boolean processed = value.isTokenProcessed();
			if (processed == null || !processed) {
				String val = value.getValue();
				Scanner s = new Scanner(val);
				s.useDelimiter(DocumentScanner.REGEX_NONALPHANUMERIC);
				while (s.hasNext()) {
					CharSequence token = DocumentScanner.scrubToken(s.next(), caseSensitive);
					if (token != null && (token.length() > 2)) {
						Term tokenV = (Term) graph.addVertex(token.toString().toLowerCase(), Term.class);
						if (tokenV.getValue() == null || tokenV.getValue().length() == 0) {
							tokenV.setValue(token.toString());
						}
						value.addTerm(tokenV);

					}
				}
				s.close();
				value.setTokenProcessed(true);
				if (commit) {
					graph.commit();
				}
			}
		}

		public static void processRemoveValue(final Document sourceDoc, final Value value, final DFramedTransactionalGraph graph,
				final boolean caseSensitive, final boolean commit) {
			try {
				String val = value.getValue();
				Scanner s = new Scanner(val);
				s.useDelimiter(DocumentScanner.REGEX_NONALPHANUMERIC);
				while (s.hasNext()) {
					CharSequence token = DocumentScanner.scrubToken(s.next(), caseSensitive);
					if (token != null && (token.length() > 2)) {
						Term tokenV = (Term) graph.getVertex(token.toString().toLowerCase(), Term.class);
						if (tokenV != null) {
							tokenV.removeDocument(sourceDoc);
						}
					}
				}
				s.close();
				value.removeDocument(sourceDoc);
				if (commit) {
					graph.commit();
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

	@TypeValue(ContainsTerm.LABEL)
	public static interface ContainsTerm extends DEdgeFrame {
		public static final String LABEL = "ContainsTerm";

		@InVertex
		public Value getValue();

		@OutVertex
		public Term getTerm();

		//		@TypedProperty("replicaid")
		//		public String getReplicaID();
		//
		//		@TypedProperty("replicaid")
		//		public void setReplicaID(String replicaid);
		//
		//		@TypedProperty("formname")
		//		public String getFormName();
		//
		//		@TypedProperty("formname")
		//		public void setFormName(String formname);
		//
		//		@TypedProperty("itemname")
		//		public String getItemName();
		//
		//		@TypedProperty("itemname")
		//		public void setItemName(String itemname);
		//
		//		@TypedProperty("hasreaders")
		//		public Boolean hasReaders();
		//
		//		@TypedProperty("hasreaders")
		//		public void setReaders(boolean hasReaders);
	}

	@TypedProperty("value")
	public String getValue();

	@TypedProperty("value")
	public void setValue(String value);

	@TypedProperty("isTokenProcessed")
	public Boolean isTokenProcessed();

	@TypedProperty("isTokenProcessed")
	public void setTokenProcessed(boolean processed);

	@JavaHandler
	@TypedProperty("Hits")
	public Map getHits();

	@JavaHandler
	public Map getHits(final Map<CharSequence, Set<CharSequence>> filterMap);

	@JavaHandler
	@TypedProperty("HitRepls")
	public List<CharSequence> getHitRepls();

	@AdjacencyUnique(label = ContainsTerm.LABEL, direction = Direction.IN)
	public List<Term> getTerms();

	@AdjacencyUnique(label = ContainsTerm.LABEL, direction = Direction.IN)
	public ContainsTerm addTerm(Term term);

	@AdjacencyUnique(label = ContainsTerm.LABEL, direction = Direction.IN)
	public void removeTerm(Term term);

	@IncidenceUnique(label = ContainsTerm.LABEL, direction = Direction.IN)
	public List<ContainsTerm> getContainsTerms();

	@IncidenceUnique(label = ContainsTerm.LABEL, direction = Direction.IN)
	public int countContainsTerms();

	@IncidenceUnique(label = ContainsTerm.LABEL, direction = Direction.IN)
	public void removeContainsTerm(ContainsTerm containsTerm);

	@JavaHandler
	public boolean removeDocument(final Document document);

	public static abstract class ValueImpl extends DVertexFrameImpl implements Value, JavaHandlerContext<Vertex> {

		@Override
		public Map getHits() {
			Map<CharSequence, Object> result = new LinkedHashMap<CharSequence, Object>();
			for (CharSequence replid : getHitRepls()) {
				String itemname = IndexDatabase.VALUE_MAP_PREFIX + replid;
				//				Vertex v = asVertex();
				//				Object raw = v.getProperty(itemname);
				//				if (raw instanceof Map) {
				//					Map m = (Map) raw;
				//					for (Object key : m.keySet()) {
				//					}
				//				}
				result.put(replid, this.asVertex().getProperty(itemname));
			}
			return result;
		}

		@Override
		public boolean removeDocument(final Document document) {
			boolean result = false;
			String replid = document.getAncestorDatabase().getReplicaID();
			String unid = document.getUniversalID().toLowerCase();
			String itemname = IndexDatabase.VALUE_MAP_PREFIX + replid;
			Vertex v = asVertex();
			Object raw = v.getProperty(itemname);
			if (raw != null && raw instanceof Map) {
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
		public Map getHits(final Map<CharSequence, Set<CharSequence>> filterMap) {
			Map<CharSequence, Map<CharSequence, List<CharSequence>>> result = new LinkedHashMap<CharSequence, Map<CharSequence, List<CharSequence>>>();
			Set<CharSequence> replicas = null;
			Set<CharSequence> forms = null;
			Set<CharSequence> fields = null;
			if (filterMap.containsKey(Value.REPLICA_KEY)) {
				replicas = filterMap.get(Value.REPLICA_KEY);
			}
			if (filterMap.containsKey(Value.FORM_KEY)) {
				forms = filterMap.get(Value.FORM_KEY);
			}
			if (filterMap.containsKey(Value.FIELD_KEY)) {
				fields = filterMap.get(Value.FIELD_KEY);
			}
			for (CharSequence replid : (replicas != null ? replicas : CaseInsensitiveString.toCaseInsensitive(getHitRepls()))) {
				Map<CharSequence, List<CharSequence>> fieldMap = new LinkedHashMap<CharSequence, List<CharSequence>>();
				String itemname = IndexDatabase.VALUE_MAP_PREFIX + replid;
				Vertex v = asVertex();
				Object raw = v.getProperty(itemname);
				if (raw != null && raw instanceof Map) {
					Map m = (Map) raw;
					for (Object key : (fields != null ? fields : CaseInsensitiveString.toCaseInsensitive(m.keySet()))) {
						List<CharSequence> addresses = new ArrayList<CharSequence>();
						Object raw2 = m.get(key);
						if (raw2 instanceof Collection) {
							for (Object rawline : (Collection) raw2) {
								if (rawline instanceof CharSequence) {
									String line = ((CharSequence) rawline).toString();
									CaseInsensitiveString form = new CaseInsensitiveString(line.substring(33));
									if (forms != null) {
										for (CharSequence curForm : forms) {
											if (form.equals(curForm)) {
												addresses.add(line);
												break;
											}
										}
									} else {
										addresses.add(line);
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
		public List<CharSequence> getHitRepls() {
			List<CharSequence> result = new ArrayList<CharSequence>();
			Map<CharSequence, Object> map = this.asMap();
			for (CharSequence cs : map.keySet()) {
				String str = cs.toString();
				if (str.startsWith(IndexDatabase.VALUE_MAP_PREFIX)) {
					result.add(str.substring(IndexDatabase.VALUE_MAP_PREFIX.length()));
				}
			}
			return result;
		}

	}

}

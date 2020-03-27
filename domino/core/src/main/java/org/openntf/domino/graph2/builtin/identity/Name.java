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
package org.openntf.domino.graph2.builtin.identity;

import java.util.List;
import java.util.Scanner;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.builtin.DEdgeFrame;
import org.openntf.domino.graph2.builtin.DVertexFrame;
import org.openntf.domino.graph2.builtin.search.Term;
import org.openntf.domino.graph2.builtin.social.Socializer;
import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.graph2.impl.DProxyVertex;
import org.openntf.domino.helpers.DocumentScanner;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;
import com.tinkerpop.frames.modules.javahandler.JavaHandler;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerClass;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerContext;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("org.openntf.domino.graph2.builtin.identity.Name")
@JavaHandlerClass(Name.NameImpl.class)
@SuppressWarnings({ "rawtypes", "unchecked" })
public interface Name extends DVertexFrame, Socializer {
	public static enum Utils {
		;
		public static void processName(final Name name, final DFramedTransactionalGraph graph, final boolean caseSensitive,
				final boolean commit) {
			Boolean processed = name.isTokenProcessed();
			if (processed == null || !processed) {
				String val = name.getName();
				try(Scanner s = new Scanner(val)) {
					s.useDelimiter(DocumentScanner.REGEX_NONALPHANUMERIC);
					while (s.hasNext()) {
						CharSequence token = DocumentScanner.scrubToken(s.next(), caseSensitive);
						if (token != null && (token.length() > 2)) {
							Term tokenV = (Term) graph.addVertex(token.toString().toLowerCase(), Term.class);
							if (tokenV.getValue() == null || tokenV.getValue().length() == 0) {
								tokenV.setValue(token.toString());
							}
							name.addPart(tokenV);
						}
					}
				}
				name.setTokenProcessed(true);
				if (commit) {
					graph.commit();
				}
			}
		}
	}

	@TypeValue(ContainsPart.LABEL)
	public static interface ContainsPart extends DEdgeFrame {
		public static final String LABEL = "ContainsPart";

		@InVertex
		public Name getName();

		@OutVertex
		public Term getTerm();
	}

	@JavaHandler
	@TypedProperty("_ODA_Name")
	public String getName();

	@TypedProperty("_ODA_Name")
	public void setName(String name);

	@TypedProperty("_ODA_isTokenProcessed")
	public Boolean isTokenProcessed();

	@TypedProperty("_ODA_isTokenProcessed")
	public void setTokenProcessed(boolean processed);

	@AdjacencyUnique(label = ContainsPart.LABEL, direction = Direction.IN)
	public List<Term> getParts();

	@AdjacencyUnique(label = ContainsPart.LABEL, direction = Direction.IN)
	public ContainsPart addPart(Term term);

	@AdjacencyUnique(label = ContainsPart.LABEL, direction = Direction.IN)
	public void removePart(Term term);

	@IncidenceUnique(label = ContainsPart.LABEL, direction = Direction.IN)
	public List<ContainsPart> getContainsParts();

	@IncidenceUnique(label = ContainsPart.LABEL, direction = Direction.IN)
	public int countContainsParts();

	@IncidenceUnique(label = ContainsPart.LABEL, direction = Direction.IN)
	public void removeContainsPart(ContainsPart containsPart);

	public abstract static class NameImpl extends DVertexFrameImpl implements Name, JavaHandlerContext<Vertex> {
		@Override
		public String getName() {
			try {
				Vertex v = this.asVertex();
				String form = v.getProperty("form");
				if (v instanceof DProxyVertex) {
					Vertex pv = ((DProxyVertex) v).getProxyDelegate();
					if (pv != null) {
						form = pv.getProperty("form");
					}
				}
				//				System.out.println("TEMP DEBUG Getting a name from a Name object backed by a " + form);
				if (form.equalsIgnoreCase("person")) {
					Object raw = v.getProperty("fullname");
					if (raw instanceof String) {
						return (String) raw;
					}
				} else if (form.equalsIgnoreCase("group")) {
					//TODO verify this!
					Object raw = v.getProperty("groupname");
					if (raw instanceof String) {
						return (String) raw;
					}
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
			return "";
		}

	}

}

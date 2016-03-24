package org.openntf.domino.graph2.builtin.identity;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.builtin.DEdgeFrame;
import org.openntf.domino.graph2.builtin.DVertexFrame;
import org.openntf.domino.graph2.builtin.search.Term;
import org.openntf.domino.graph2.builtin.social.Socializer;
import org.openntf.domino.graph2.impl.DProxyVertex;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;
import com.tinkerpop.frames.modules.javahandler.JavaHandler;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerClass;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerContext;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Name")
@JavaHandlerClass(Name.NameImpl.class)
public interface Name extends DVertexFrame, Socializer {
	@TypeValue(ContainsPart.LABEL)
	public static interface ContainsPart extends DEdgeFrame {
		public static final String LABEL = "ContainsPart";

		@InVertex
		public Name getName();

		@OutVertex
		public Term getTerm();
	}

	@JavaHandler
	@TypedProperty("Name")
	public String getName();

	@TypedProperty("Name")
	public void setName(String name);

	@AdjacencyUnique(label = ContainsPart.LABEL, direction = Direction.IN)
	public Iterable<Term> getParts();

	@AdjacencyUnique(label = ContainsPart.LABEL, direction = Direction.IN)
	public ContainsPart addPart(Term term);

	@AdjacencyUnique(label = ContainsPart.LABEL, direction = Direction.IN)
	public void removePart(Term term);

	@IncidenceUnique(label = ContainsPart.LABEL, direction = Direction.IN)
	public Iterable<ContainsPart> getContainsParts();

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

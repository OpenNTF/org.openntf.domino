package org.openntf.domino.graph2.builtin;

import java.util.Map;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.View;
import org.openntf.domino.graph2.annotations.Shardable;
import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.impl.DVertex;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.Incidence;
import com.tinkerpop.frames.OutVertex;
import com.tinkerpop.frames.VertexFrame;
import com.tinkerpop.frames.modules.javahandler.JavaHandler;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerClass;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerContext;
import com.tinkerpop.frames.modules.typedgraph.TypeField;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@Shardable
@TypeField("$FormulaClass")
@TypeValue("1")
@JavaHandlerClass(ViewVertex.ViewVertexImpl.class)
public interface ViewVertex extends VertexFrame {

	@JavaHandler
	public Document asDocument();

	@JavaHandler
	public Map<CharSequence, Object> asMap();

	@JavaHandler
	public View asView();

	@TypedProperty("$Title")
	public String getTitle();

	@Incidence(label = "contents", direction = Direction.OUT)
	public Iterable<Contains> getContents();

	public abstract static class ViewVertexImpl implements ViewVertex, JavaHandlerContext<Vertex> {
		@Override
		public Document asDocument() {
			Object raw = asVertex();
			if (raw instanceof DVertex) {
				Object delegate = ((DVertex) raw).getDelegate();
				if (delegate instanceof Document) {
					return (Document) delegate;
				}
				throw new RuntimeException("VertexFrame not backed by org.openntf.domino.Document. Instead it's a "
						+ (delegate == null ? "null" : delegate.getClass().getName()));
			}
			throw new RuntimeException("VertexFrame not backed by org.openntf.domino.graph2.DVertex. Instead it's a "
					+ (raw == null ? "null" : raw.getClass().getName()));
		}

		@Override
		public View asView() {
			Document doc = asDocument();
			Database db = doc.getParentDatabase();
			return db.getView(doc);
		}

		@SuppressWarnings("unchecked")
		@Override
		public Map<CharSequence, Object> asMap() {
			Object raw = asVertex();
			if (raw instanceof DVertex) {
				Object delegate = ((DVertex) raw).getDelegate();
				if (delegate instanceof Map) {
					return (Map<CharSequence, Object>) delegate;
				}
				throw new RuntimeException("VertexFrame not backed by a Map. Instead it's a "
						+ (delegate == null ? "null" : delegate.getClass().getName()));
			}
			throw new RuntimeException("VertexFrame not backed by org.openntf.domino.graph2.DVertex. Instead it's a "
					+ (raw == null ? "null" : raw.getClass().getName()));
		}
	}

	public static interface Contains extends EdgeFrame {
		public static final String LABEL = "Contains";

		@InVertex
		public Vertex getIn();

		@OutVertex
		public Vertex getOut();

	}

}

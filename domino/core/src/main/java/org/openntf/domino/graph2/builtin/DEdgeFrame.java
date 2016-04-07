package org.openntf.domino.graph2.builtin;

import java.util.Date;
import java.util.Map;

import org.openntf.domino.Document;
import org.openntf.domino.graph2.DEdge;
import org.openntf.domino.graph2.annotations.TypedProperty;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.modules.javahandler.JavaHandler;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerClass;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerContext;
import com.tinkerpop.frames.modules.typedgraph.TypeField;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeField("form")
@TypeValue("edgeFrame")
@JavaHandlerClass(DEdgeFrame.DEdgeFrameImpl.class)
public interface DEdgeFrame extends EdgeFrame {
	@TypedProperty(value = "@CreatedDate", derived = true)
	public Date getCreated();

	@TypedProperty(value = "@ModifiedDate", derived = true)
	public Date getModified();

	@JavaHandler
	public Map<CharSequence, Object> asMap();

	@JavaHandler
	public Document asDocument();

	public abstract static class DEdgeFrameImpl implements DEdgeFrame, JavaHandlerContext<Vertex> {
		@Override
		public Document asDocument() {
			Object raw = asEdge();
			if (raw instanceof DEdge) {
				Object delegate = ((DEdge) raw).getDelegate();
				if (delegate instanceof Document) {
					return (Document) delegate;
				}
				throw new RuntimeException("EdgeFrame not backed by org.openntf.domino.Document. Instead it's a "
						+ (delegate == null ? "null" : delegate.getClass().getName()));
			}
			throw new RuntimeException("EdgeFrame not backed by org.openntf.domino.graph2.DEdge. Instead it's a "
					+ (raw == null ? "null" : raw.getClass().getName()));
		}

		@SuppressWarnings("unchecked")
		@Override
		public Map<CharSequence, Object> asMap() {
			Object raw = asEdge();
			if (raw instanceof DEdge) {
				Object delegate = ((DEdge) raw).getDelegate();
				if (delegate instanceof Map) {
					return (Map<CharSequence, Object>) delegate;
				}
				throw new RuntimeException("EdgeFrame not backed by a Map. Instead it's a "
						+ (delegate == null ? "null" : delegate.getClass().getName()));
			}
			throw new RuntimeException("EdgeFrame not backed by org.openntf.domino.graph2.DEdge. Instead it's a "
					+ (raw == null ? "null" : raw.getClass().getName()));
		}
	}

}

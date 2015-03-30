package org.openntf.domino.graph2.builtin;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.openntf.domino.Document;
import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.impl.DVertex;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.modules.javahandler.JavaHandler;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerClass;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerContext;
import com.tinkerpop.frames.modules.typedgraph.TypeField;

@TypeField("form")
@JavaHandlerClass(DVertexFrame.DVertexFrameImpl.class)
public interface DVertexFrame extends Editable {
	@TypedProperty(value = "@CreatedDate", derived = true)
	public Date getCreated();

	@TypedProperty(value = "@ModifiedDate", derived = true)
	public Date getModified();

	@JavaHandler
	public Document asDocument();

	@JavaHandler
	public Map<CharSequence, Object> asMap();

	@JavaHandler
	public String[] getEditors();

	//TODO NTF Future
	//	public String[] getOwners();

	//TODO NTF Future
	//	public String[] getReaders();

	public abstract static class DVertexFrameImpl implements DVertexFrame, JavaHandlerContext<Vertex> {
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

		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		public String[] getEditors() {
			String[] result = null;
			Object raw = asVertex().getProperty("$UpdatedBy");
			if (raw != null) {
				if (raw instanceof Collection) {
					result = org.openntf.domino.utils.TypeUtils.collectionToStrings((Collection) raw);
				}
			}
			return result;
		}

		//		@Override
		//		public Date getCreated() {
		//			//			System.out.println("Impl.getCreated() called");
		//			Date result = null;
		//			Object raw = ((DVertex) asVertex()).getProperty("@CreatedDate", Date.class);
		//			if (raw instanceof Date) {
		//				return result;
		//			} else {
		//				System.out.println("Looked for a Date but got a " + (raw == null ? "null" : raw.getClass().getName()));
		//				return new Date(0);
		//			}
		//		}
	}

}

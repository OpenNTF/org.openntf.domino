package org.openntf.domino.graph2.builtin;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.openntf.domino.Document;
import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.impl.DVertex;

import com.tinkerpop.frames.modules.javahandler.JavaHandler;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerClass;
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
	public String[] getEditors();

	//TODO NTF Future
	//	public String[] getOwners();

	//TODO NTF Future
	//	public String[] getReaders();

	public abstract static class DVertexFrameImpl implements DVertexFrame {
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

		public Map<CharSequence, Object> asMap() {
			//TODO NTF
			Object raw = asVertex();
			if (raw instanceof DVertex) {
				Object delegate = ((DVertex) raw).getDelegate();
				if (delegate instanceof Map) {
					return (Map) delegate;
				}
				throw new RuntimeException("VertexFrame not backed by a Map. Instead it's a "
						+ (delegate == null ? "null" : delegate.getClass().getName()));
			}
			throw new RuntimeException("VertexFrame not backed by org.openntf.domino.graph2.DVertex. Instead it's a "
					+ (raw == null ? "null" : raw.getClass().getName()));
		}

		@SuppressWarnings("rawtypes")
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
	}

}

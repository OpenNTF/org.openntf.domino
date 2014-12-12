package org.openntf.domino.graph2.builtin;

import java.util.Collection;
import java.util.Date;

import org.openntf.domino.graph2.annotations.TypedProperty;

import com.tinkerpop.frames.modules.typedgraph.TypeField;

@TypeField("form")
public interface DVertexFrame extends Editable {
	@TypedProperty(value = "@CreatedDate", derived = true)
	public Date getCreated();

	@TypedProperty(value = "@ModifiedDate", derived = true)
	public Date getModified();

	public String[] getEditors();

	//TODO NTF Future
	//	public String[] getOwners();

	//TODO NTF Future
	//	public String[] getReaders();

	public abstract static class DVertexFrameImpl implements DVertexFrame {
		@SuppressWarnings({ "unchecked" })
		@Override
		public String[] getEditors() {
			String[] result = null;
			Object raw = asVertex().getProperty("$UpdatedBy");
			if (raw != null) {
				if (raw instanceof Collection) {
					result = org.openntf.domino.utils.TypeUtils.collectionToStrings((Collection<Object>) raw);
				}
			}
			return result;
		}
	}

}

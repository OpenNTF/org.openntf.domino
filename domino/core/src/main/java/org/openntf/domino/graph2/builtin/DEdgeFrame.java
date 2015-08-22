package org.openntf.domino.graph2.builtin;

import java.util.Date;

import org.openntf.domino.graph2.annotations.TypedProperty;

import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.modules.typedgraph.TypeField;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeField("form")
@TypeValue("edgeFrame")
public interface DEdgeFrame extends EdgeFrame {
	@TypedProperty(value = "@CreatedDate", derived = true)
	public Date getCreated();

	@TypedProperty(value = "@ModifiedDate", derived = true)
	public Date getModified();

}

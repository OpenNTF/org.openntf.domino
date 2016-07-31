package org.openntf.domino.tests.obusse;

import org.openntf.domino.graph2.builtin.DVertexFrame;

import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Presenter")
public interface Presenter extends DVertexFrame {
	@Property("name")
	// = field name
	public String getName();

	@Property("name")
	void setName(String name);
}

package org.openntf.domino.tests.obusse;

import org.openntf.domino.graph2.builtin.DVertexFrame;

import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Attendee")
public interface Attendee extends DVertexFrame {
	@Property("name")
	public String getName();

	@Property("name")
	public void setName(String name);
}
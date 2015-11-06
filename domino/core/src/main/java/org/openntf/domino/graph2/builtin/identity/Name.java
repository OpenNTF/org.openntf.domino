package org.openntf.domino.graph2.builtin.identity;

import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.builtin.DVertexFrame;
import org.openntf.domino.graph2.builtin.social.Socializer;

import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Name")
public interface Name extends DVertexFrame, Socializer {
	@TypedProperty("Name")
	public String getName();

	@TypedProperty("Name")
	public void setName(String name);
}

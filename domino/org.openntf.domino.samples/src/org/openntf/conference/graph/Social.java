package org.openntf.conference.graph;

import org.openntf.domino.graph2.annotations.TypedProperty;

import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Social")
public interface Social extends Event {
	public static enum Features {
		REFRESHMENTS, OPENBAR, MUSIC, CODING, DEMO
	}

	@TypedProperty("Features")
	public Features[] getFeatures();

	@TypedProperty("Features")
	public void setFeatures(Features[] features);
}

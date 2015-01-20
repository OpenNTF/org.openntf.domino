package org.openntf.conference.graph;

import org.openntf.domino.graph2.annotations.TypedProperty;

public interface Social extends Event {
	public static enum Features {
		REFRESHMENTS, OPENBAR, MUSIC, CODING, DEMO
	}

	@TypedProperty("Features")
	public Features[] getFeatures();

	@TypedProperty("Features")
	public void setFeatures(Features[] features);
}

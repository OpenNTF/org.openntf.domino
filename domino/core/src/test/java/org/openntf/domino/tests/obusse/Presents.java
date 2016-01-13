package org.openntf.domino.tests.obusse;

import org.openntf.domino.graph2.builtin.DEdgeFrame;

import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;

public interface Presents extends DEdgeFrame {
	@InVertex
	Presenter getPresenter();

	@OutVertex
	ConferenceSession getSession();
}

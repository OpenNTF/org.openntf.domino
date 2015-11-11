package org.openntf.domino.tests.obusse;

import org.openntf.domino.graph2.builtin.DEdgeFrame;

import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("attends")
public interface Attends extends DEdgeFrame {
	@OutVertex
	Iterable<Attendee> getAttendees();

	@InVertex
	ConferenceSession getSession();
}

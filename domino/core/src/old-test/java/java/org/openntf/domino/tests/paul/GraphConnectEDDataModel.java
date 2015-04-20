package org.openntf.domino.tests.paul;

import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.VertexFrame;
import com.tinkerpop.frames.modules.typedgraph.TypeField;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

public class GraphConnectEDDataModel {

	@TypeField("form")
	public interface DVertexFrame extends VertexFrame {

	}

	@TypeField("form")
	public interface DEdgeFrame extends EdgeFrame {

	}

	@TypeValue("Speaker")
	public interface Speaker extends VertexFrame {

	}

	public GraphConnectEDDataModel() {
	}

}

package org.openntf.domino.graph;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public class DominoEdge extends DominoElement implements Edge {
	public static final String GRAPH_TYPE_VALUE = "OpenEdge";
	public static final String IN_NAME = "_OPEN_IN";
	public static final String LABEL_NAME = "_OPEN_LABEL";
	public static final String OUT_NAME = "_OPEN_OUT";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	transient Vertex in_;
	private String inKey_;
	transient Vertex out_;
	private String outKey_;

	public DominoEdge(DominoGraph parent, org.openntf.domino.Document doc) {
		super(parent, doc);
	}

	@Override
	public String getLabel() {
		return getProperty(DominoEdge.LABEL_NAME, String.class);
	}

	@Override
	public Vertex getVertex(Direction direction) throws IllegalArgumentException {
		return parent_.getVertex(getVertexId(direction));
	}

	public String getVertexId(Direction direction) {
		if (direction == Direction.IN) {
			if (inKey_ == null) {
				inKey_ = getProperty(DominoEdge.IN_NAME, String.class);
			}
			return inKey_;
		}
		if (direction == Direction.OUT) {
			if (outKey_ == null) {
				outKey_ = getProperty(DominoEdge.OUT_NAME, String.class);
			}
			return outKey_;
		}
		return null;
	}

	public void relate(DominoVertex in, DominoVertex out) {
		setInDoc(in);
		setOutDoc(out);
	}

	public void setInDoc(Vertex in) {
		in_ = in;
		inKey_ = (String) in.getId();
		setProperty(DominoEdge.IN_NAME, inKey_);
		((DominoVertex) in).addInEdge(this);
	}

	void setLabel(String label) {
		setProperty(DominoEdge.LABEL_NAME, label);
	}

	public void setOutDoc(Vertex out) {
		out_ = out;
		outKey_ = (String) out.getId();
		setProperty(DominoEdge.OUT_NAME, outKey_);
		((DominoVertex) out).addOutEdge(this);
	}

	// @Override
	// public void save() {
	// Object o = getProperty("Form", String.class);
	// if (o == null || ((String) o).length() < 1) {
	// setProperty("Form", DominoEdge.GRAPH_TYPE_VALUE);
	// }
	// super.save();
	// }

}

package org.openntf.domino.graph;

import java.io.Serializable;
import java.util.logging.Logger;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public class DominoEdge extends DominoElement implements Edge, Serializable {
	private static final Logger log_ = Logger.getLogger(DominoEdge.class.getName());
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
	private String label_;

	public DominoEdge(final DominoGraph parent, final org.openntf.domino.Document doc) {
		super(parent, doc);
	}

	@Override
	public String getLabel() {
		if (label_ == null) {
			label_ = getProperty(DominoEdge.LABEL_NAME, String.class);
		}
		return label_;
	}

	@Override
	public Vertex getVertex(final Direction direction) throws IllegalArgumentException {
		return parent_.getVertex(getVertexId(direction));
	}

	public String getVertexId(final Direction direction) {
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

	public void relate(final DominoVertex in, final DominoVertex out) {
		setInDoc(in);
		setOutDoc(out);
	}

	public void setInDoc(final Vertex in) {
		((DominoVertex) in).addInEdge(this);
		in_ = in;
		inKey_ = (String) in.getId();
		setProperty(DominoEdge.IN_NAME, inKey_);
	}

	void setLabel(final String label) {
		label_ = label;
		setProperty(DominoEdge.LABEL_NAME, label);
	}

	public void setOutDoc(final Vertex out) {
		((DominoVertex) out).addOutEdge(this);
		out_ = out;
		outKey_ = (String) out.getId();
		setProperty(DominoEdge.OUT_NAME, outKey_);
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

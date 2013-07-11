/**
 * 
 */
package org.openntf.domino.graph;

import java.util.logging.Logger;

import com.tinkerpop.blueprints.Vertex;

/**
 * @author nfreeman
 * 
 */
public class AbstractEdgeHelper<IN extends Vertex, OUT extends Vertex> implements IEdgeHelper {
	private static final Logger log_ = Logger.getLogger(AbstractEdgeHelper.class.getName());
	private static final long serialVersionUID = 1L;

	private final String label_;
	private final Class<? extends Vertex> inType_;
	private final Class<? extends Vertex> outType_;

	public AbstractEdgeHelper(final String label, final Class<? extends Vertex> inType, final Class<? extends Vertex> outType) {
		label_ = label;
		inType_ = inType;
		outType_ = outType;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.graph.IEdgeHelper#getLabel()
	 */
	@Override
	public String getLabel() {
		return label_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.graph.IEdgeHelper#getInType()
	 */
	@Override
	public Class<? extends Vertex> getInType() {
		return inType_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.graph.IEdgeHelper#getOutType()
	 */
	@Override
	public Class<? extends Vertex> getOutType() {
		return outType_;
	}

}

/**
 * 
 */
package org.openntf.domino.graph;

import java.util.logging.Logger;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

/**
 * @author nfreeman
 * 
 */
public class AbstractEdgeHelper implements IEdgeHelper {
	private static final Logger log_ = Logger.getLogger(AbstractEdgeHelper.class.getName());

	public static class EdgeHelperException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public EdgeHelperException(final String message) {

		}
	}

	private final String label_;
	private final Class<? extends Vertex> inType_;
	private final Class<? extends Vertex> outType_;
	private final boolean sameTypes_;
	private final boolean unique_;
	private final DominoGraph parent_;

	public AbstractEdgeHelper(final DominoGraph parent, final String label, final Class<? extends Vertex> inType,
			final Class<? extends Vertex> outType, final boolean unique) {
		label_ = label;
		inType_ = inType;
		outType_ = outType;
		unique_ = unique;
		sameTypes_ = inType_.equals(outType_);
		parent_ = parent;
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

	public boolean isUnique() {
		return unique_;
	}

	public boolean isSameTypes() {
		return sameTypes_;
	}

	public Edge makeEdge(final Vertex defaultOut, final Vertex defaultIn) {
		Edge result = null;
		Vertex inVert = defaultIn;
		Vertex outVert = defaultOut;
		if (defaultOut == null || defaultIn == null)
			throw new EdgeHelperException("Cannot create edges with null vertex");
		if (defaultOut.getClass().equals(defaultIn.getClass()) && isSameTypes()) {

		} else {
			if (getInType().equals(defaultOut.getClass())) {
				inVert = defaultOut;
			} else if (getOutType().equals(defaultOut.getClass())) {
				outVert = defaultOut;
			} else {
				// System.out.println("Cannot create an edge of type " + getLabel() + " with a vertex of type "
				// + defaultOut.getClass().getName());
				throw new EdgeHelperException("Cannot create an edge of type " + getLabel() + " with a vertex of type "
						+ defaultOut.getClass().getName());
			}
			if (getInType().equals(defaultIn.getClass())) {
				inVert = defaultIn;
			} else if (getOutType().equals(defaultIn.getClass())) {
				outVert = defaultIn;
			} else {
				// System.out.println("Cannot create an edge of type " + getLabel() + " with a vertex of type "
				// + defaultIn.getClass().getName());
				throw new EdgeHelperException("Cannot create an edge of type " + getLabel() + " with a vertex of type "
						+ defaultIn.getClass().getName());
			}
		}
		if (isUnique()) {
			result = parent_.getOrAddEdge(null, outVert, inVert, getLabel());
		} else {
			result = parent_.addEdge(null, outVert, inVert, getLabel());
		}
		return result;
	}

	public Edge findEdge(final Vertex defaultOut, final Vertex defaultIn) {
		Edge result = null;
		Vertex inVert = defaultIn;
		Vertex outVert = defaultOut;
		if (defaultOut == null || defaultIn == null)
			throw new EdgeHelperException("Cannot find edges with null vertex");
		if (defaultOut.getClass().equals(defaultIn.getClass()) && isSameTypes()) {

		} else {
			if (getInType().equals(defaultOut.getClass())) {
				inVert = defaultOut;
			} else if (getOutType().equals(defaultOut.getClass())) {
				outVert = defaultOut;
			} else {
				throw new EdgeHelperException("Cannot find an edge of type " + getLabel() + " with a vertex of type "
						+ defaultOut.getClass().getName());
			}
			if (getInType().equals(defaultIn.getClass())) {
				inVert = defaultIn;
			} else if (getOutType().equals(defaultIn.getClass())) {
				outVert = defaultIn;
			} else {
				throw new EdgeHelperException("Cannot find an edge of type " + getLabel() + " with a vertex of type "
						+ defaultIn.getClass().getName());
			}
		}
		result = parent_.getEdge(outVert, inVert, getLabel());
		return result;
	}
}

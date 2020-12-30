/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * 
 */
package org.openntf.domino.graph;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.logging.Logger;

//import javax.annotation.Nonnull;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

/**
 * @author nfreeman
 * 
 */
@Deprecated
@SuppressWarnings("nls")
public class AbstractEdgeHelper implements IEdgeHelper {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(AbstractEdgeHelper.class.getName());

	public static class EdgeHelperException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public EdgeHelperException(final String message) {
			super(message);
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

	@Override
	public boolean isUnique() {
		return unique_;
	}

	public boolean isSameTypes() {
		return sameTypes_;
	}

	@Override
	public Class<? extends Vertex> getOtherType(final Class<? extends Vertex> type) {
		if (getInType().equals(type)) {
			return getOutType();
		}
		if (getOutType().equals(type)) {
			return getInType();
		}
		if (getInType().isAssignableFrom(type))
			return getOutType();
		if (getOutType().isAssignableFrom(type))
			return getInType();
		throw new EdgeHelperException(type.getName() + " is not a participating type in edge " + getLabel());
	}

	@Override
	public Class<? extends Vertex> getOtherType(final Vertex vertex) {
		return getOtherType(vertex.getClass());
	}

	@Override
	public int getEdgeCount(final Vertex vertex) {
		if (vertex instanceof DominoVertex) {
			if (getInType().equals(vertex.getClass())) {
				return ((DominoVertex) vertex).getInEdgeCount(getLabel());
			}
			if (getOutType().equals(vertex.getClass())) {
				return ((DominoVertex) vertex).getOutEdgeCount(getLabel());
			}
			if (getInType().isAssignableFrom(vertex.getClass())) {
				return ((DominoVertex) vertex).getInEdgeCount(getLabel());
			}
			if (getOutType().isAssignableFrom(vertex.getClass())) {
				return ((DominoVertex) vertex).getOutEdgeCount(getLabel());
			}
			return getEdges(vertex).size();
		} else {
			return getEdges(vertex).size();
		}
	}

	@Override
	public Set<? extends Edge> getEdges(final Vertex vertex) {
		//		System.out.println("DEBUG: Getting edges...");
		Set<Edge> results = null;

		if (sameTypes_) {
			//			System.out.println("DEBUG: Both directions are " + inType_.getSimpleName() + " in helper " + getLabel());
			results = (Set<Edge>) vertex.getEdges(Direction.IN, getLabel());
			if (results.size() == 0) {
				results = (Set<Edge>) vertex.getEdges(Direction.OUT, getLabel());
				//				System.out.println("DEBUG: Returning " + results.size() + " outs");
			} else {
				//				System.out.println("DEBUG: Returning " + results.size() + " ins");
			}
		} else {
			//			System.out.println("DEBUG: Analyzing directions for helper " + getLabel() + " with a vertex of "
			//					+ vertex.getClass().getSimpleName());

			if (getInType().equals(vertex.getClass())) {
				results = (Set<Edge>) vertex.getEdges(Direction.IN, getLabel());
				//				System.out.println("DEBUG: " + vertex.getClass().getSimpleName() + " is the IN type");
			} else if (getOutType().equals(vertex.getClass())) {
				results = (Set<Edge>) vertex.getEdges(Direction.OUT, getLabel());
				//				System.out.println("DEBUG: " + vertex.getClass().getSimpleName() + " is the OUT type");
			} else if (getInType().isAssignableFrom(vertex.getClass())) {
				results = (Set<Edge>) vertex.getEdges(Direction.IN, getLabel());
				//				System.out.println("DEBUG: " + vertex.getClass().getSimpleName() + " can be assigned as the IN type");
			} else if (getOutType().isAssignableFrom(vertex.getClass())) {
				results = (Set<Edge>) vertex.getEdges(Direction.OUT, getLabel());
				//				System.out.println("DEBUG: " + vertex.getClass().getSimpleName() + " can be assigned as the OUT type");
			}
		}
		if (results == null) {
			//			System.out.println("DEBUG: Exception " + vertex.getClass().getName() + " is not a participating type in edge " + getLabel());
			throw new EdgeHelperException(vertex.getClass().getName() + " is not a participating type in edge " + getLabel());
		} else {
			//			System.out.println("DEBUG: Returning " + results.size() + " edges for helper " + getLabel());
			return Collections.unmodifiableSet(results);
		}
	}

	@Override
	public Set<? extends Edge> getFilteredEdges(final Vertex vertex, final Map<String, Object> filterMap) {
		Set<Edge> result = new LinkedHashSet<Edge>();
		Set<? extends Edge> rawset = getEdges(vertex);
		for (Edge edge : rawset) {
			for (String key : filterMap.keySet()) {
				Object value = edge.getProperty(key);
				if (value != null) {
					if (value.equals(filterMap.get(key))) {
						result.add(edge);
					}
				}
			}
		}
		return Collections.unmodifiableSet(result);
	}

	@Override
	public SortedSet<? extends Edge> getSortedEdges(final Vertex vertex, final String... sortproperties) {
		try {
			Set<? extends Edge> rawSet = getEdges(vertex);
			//			System.out.println("Requested sorting of " + rawSet.size() + " raw edges");

			SortedSet<? extends Edge> sortedSet = DominoGraph.sortEdges(rawSet, sortproperties);
			//			System.out.println("Returning sorted edge set of " + sortedSet.size());
			SortedSet<? extends Edge> result = sortedSet;
			return result;
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}

	@Override
	public Set<? extends Vertex> getOtherVertexes(final Vertex vertex) {
		Set<Vertex> result = new LinkedHashSet<Vertex>();
		Class<?> vclass = vertex.getClass();
		Direction od = null;
		if (getInType().equals(vclass))
			od = Direction.OUT;
		if (od == null && getOutType().equals(vclass))
			od = Direction.IN;
		if (od == null && getInType().isAssignableFrom(vclass))
			od = Direction.OUT;
		if (od == null && getOutType().isAssignableFrom(vclass))
			od = Direction.IN;
		if (od == null) {
			throw new EdgeHelperException(vertex.getClass().getName() + " is not a participating type in edge " + getLabel());
		} else {
			Set<? extends Edge> edges = getEdges(vertex);
			for (Edge edge : edges) {
				result.add(edge.getVertex(od));
			}
			return Collections.unmodifiableSet(result);
		}
	}

	@Override
	public Vertex getOtherVertex(final Edge edge, final Vertex vertex) {
		if (edge instanceof IDominoEdge) {
			return ((IDominoEdge) edge).getOtherVertex(vertex);
		} else {
			if (vertex.getId().equals(edge.getVertex(Direction.IN).getId())) {
				return edge.getVertex(Direction.OUT);
			} else {
				return edge.getVertex(Direction.IN);
			}
		}
	}

	@Override
	public Set<? extends Vertex> getSortedOtherVertexes(final Vertex vertex, final String... sortproperties) {
		try {
			Set<? extends Vertex> rawSet = getOtherVertexes(vertex);
			return Collections.unmodifiableSortedSet(DominoGraph.sortVertexes(rawSet, sortproperties));
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}

	@Override
	public Set<Vertex> getOtherVertexesByEdge(final Vertex vertex, final String... sortproperties) {
		Set<Vertex> result = new LinkedHashSet<Vertex>();
		Class<?> vclass = vertex.getClass();
		Direction od = null;
		if (getInType().equals(vclass))
			od = Direction.OUT;
		if (od == null && getOutType().equals(vclass))
			od = Direction.IN;
		if (od == null && getInType().isAssignableFrom(vclass))
			od = Direction.OUT;
		if (od == null && getOutType().isAssignableFrom(vclass))
			od = Direction.IN;
		if (od == null) {
			throw new EdgeHelperException(vertex.getClass().getName() + " is not a participating type in edge " + getLabel());
		} else {
			SortedSet<? extends Edge> edges = getSortedEdges(vertex, sortproperties);
			for (Edge edge : edges) {
				result.add(edge.getVertex(od));
			}
			return Collections.unmodifiableSet(result);
		}
	}

	@Override
	public Edge makeEdge(final Vertex defaultOut, final Vertex defaultIn) {
		if (defaultOut == null || defaultIn == null) {
			throw new RuntimeException("Cannot create an edge of type " + getLabel() + " where a vertex is null!");
		}
		Edge result = null;
		Vertex inVert = null;
		Vertex outVert = null;
		boolean inExact = false;
		boolean outExact = false;
		boolean inPartial = false;
		boolean outPartial = false;
		boolean reinExact = false;
		boolean reoutExact = false;
		boolean reinPartial = false;
		boolean reoutPartial = false;
		Class<?> inClass = defaultIn.getClass();
		Class<?> outClass = defaultOut.getClass();
		Class<?> inType = getInType();
		Class<?> outType = getOutType();
		if (defaultOut == null || defaultIn == null)
			throw new EdgeHelperException("Cannot create edges with null vertex");
		if (defaultOut.getClass().equals(defaultIn.getClass()) && isSameTypes()) {
			inVert = defaultIn;
			outVert = defaultOut;
		} else {
			inExact = inType.equals(inClass);
			outExact = outType.equals(outClass);
			inPartial = inType.isAssignableFrom(inClass);
			outPartial = outType.isAssignableFrom(outClass);
			reinExact = outType.equals(inClass);
			reoutExact = inType.equals(outClass);
			reinPartial = outType.isAssignableFrom(inClass);
			reoutPartial = inType.isAssignableFrom(outClass);

			if (inExact && outExact) {	//perfect
				inVert = defaultIn;
				outVert = defaultOut;
			} else if ((inExact && outPartial) || (outExact && inPartial)) {  //good enough
				inVert = defaultIn;
				outVert = defaultOut;
			} else if (inPartial && outPartial) {
				if (reinExact || reoutExact) {	//invert
					inVert = defaultOut;
					outVert = defaultIn;
				} else {	//it'll do
					inVert = defaultIn;
					outVert = defaultOut;
				}
			}

			if (inVert == null || outVert == null) {
				if (reinExact && reoutExact) {	//perfectly backwards
					inVert = defaultOut;
					outVert = defaultIn;
				} else if ((reinExact && reoutPartial) || (reoutExact && reinPartial)) {  //backwards, with inheritance
					inVert = defaultOut;
					outVert = defaultIn;
				} else if (reinPartial && reoutPartial) {	//inversion works, so we'll settle for it.
					inVert = defaultOut;
					outVert = defaultIn;
				}
			}

			if (inVert == null || outVert == null) {
				StringBuilder sb = new StringBuilder();
				sb.append("Label: ");
				sb.append(getLabel());
				sb.append(" intype: ");
				sb.append(inType.getSimpleName());
				sb.append(" outtype: ");
				sb.append(outType.getSimpleName());
				sb.append(" invert: ");
				sb.append(inClass.getSimpleName());
				sb.append(" outvert: ");
				sb.append(outClass.getSimpleName());
				sb.append(" inE: ");
				sb.append(inExact);
				sb.append(" outE: ");
				sb.append(outExact);
				sb.append(" inP: ");
				sb.append(inPartial);
				sb.append(" outP: ");
				sb.append(outPartial);
				sb.append(" reinE: ");
				sb.append(reinExact);
				sb.append(" reoutE: ");
				sb.append(reoutExact);
				sb.append(" reinP: ");
				sb.append(reinPartial);
				sb.append(" reoutP: ");
				sb.append(reoutPartial);
				throw new EdgeHelperException("Cannot create an edge - " + sb.toString());
			}
		}
		if (isUnique()) {
			result = parent_.getOrAddEdge(null, outVert, inVert, getLabel());
		} else {
			result = parent_.addEdge(null, outVert, inVert, getLabel());
		}
		return result;
	}

	@Override
	public Edge findEdge(final/*@Nonnull*/Vertex defaultOut, final/*@Nonnull*/Vertex defaultIn) {
		Edge result = null;
		Vertex inVert = null;
		Vertex outVert = null;
		boolean inExact = false;
		boolean outExact = false;
		boolean inPartial = false;
		boolean outPartial = false;
		boolean reinExact = false;
		boolean reoutExact = false;
		boolean reinPartial = false;
		boolean reoutPartial = false;
		Class<?> inClass = defaultIn.getClass();
		Class<?> outClass = defaultOut.getClass();
		Class<?> inType = getInType();
		Class<?> outType = getOutType();
		if (defaultOut == null || defaultIn == null)
			throw new EdgeHelperException("Cannot create edges with null vertex");
		if (defaultOut.getClass().equals(defaultIn.getClass()) && isSameTypes()) {
			inVert = defaultIn;
			outVert = defaultOut;
		} else {
			inExact = inType.equals(inClass);
			outExact = outType.equals(outClass);
			inPartial = inType.isAssignableFrom(inClass);
			outPartial = outType.isAssignableFrom(outClass);
			reinExact = outType.equals(inClass);
			reoutExact = inType.equals(outClass);
			reinPartial = outType.isAssignableFrom(inClass);
			reoutPartial = inType.isAssignableFrom(outClass);

			if (inExact && outExact) {	//perfect
				inVert = defaultIn;
				outVert = defaultOut;
			} else if ((inExact && outPartial) || (outExact && inPartial)) {  //good enough
				inVert = defaultIn;
				outVert = defaultOut;
			} else if (inPartial && outPartial) {
				if (reinExact || reoutExact) {	//invert
					inVert = defaultOut;
					outVert = defaultIn;
				} else {	//it'll do
					inVert = defaultIn;
					outVert = defaultOut;
				}
			}

			if (inVert == null || outVert == null) {
				if (reinExact && reoutExact) {	//perfectly backwards
					inVert = defaultOut;
					outVert = defaultIn;
				} else if ((reinExact && reoutPartial) || (reoutExact && reinPartial)) {  //backwards, with inheritance
					inVert = defaultOut;
					outVert = defaultIn;
				} else if (reinPartial && reoutPartial) {	//inversion works, so we'll settle for it.
					inVert = defaultOut;
					outVert = defaultIn;
				}
			}

			if (inVert == null || outVert == null) {
				StringBuilder sb = new StringBuilder();
				sb.append("Label: ");
				sb.append(getLabel());
				sb.append(" intype: ");
				sb.append(inType.getSimpleName());
				sb.append(" outtype: ");
				sb.append(outType.getSimpleName());
				sb.append(" invert: ");
				sb.append(inClass.getSimpleName());
				sb.append(" outvert: ");
				sb.append(outClass.getSimpleName());
				sb.append(" inE: ");
				sb.append(inExact);
				sb.append(" outE: ");
				sb.append(outExact);
				sb.append(" inP: ");
				sb.append(inPartial);
				sb.append(" outP: ");
				sb.append(outPartial);
				sb.append(" reinE: ");
				sb.append(reinExact);
				sb.append(" reoutE: ");
				sb.append(reoutExact);
				sb.append(" reinP: ");
				sb.append(reinPartial);
				sb.append(" reoutP: ");
				sb.append(reoutPartial);
				throw new EdgeHelperException("Cannot create an edge - " + sb.toString());
			}
		}
		result = parent_.getEdge(outVert, inVert, getLabel());
		return result;
	}
}

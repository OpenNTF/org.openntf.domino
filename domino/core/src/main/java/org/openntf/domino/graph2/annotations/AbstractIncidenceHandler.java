package org.openntf.domino.graph2.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;

import org.openntf.domino.big.NoteCoordinate;
import org.openntf.domino.graph2.DEdgeList;
import org.openntf.domino.graph2.DElementStore;
import org.openntf.domino.graph2.DVertex;
import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.utils.DominoUtils;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.Adjacency;
import com.tinkerpop.frames.ClassUtilities;
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.Incidence;
import com.tinkerpop.frames.VertexFrame;

public abstract class AbstractIncidenceHandler {

	public AbstractIncidenceHandler() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object processVertexIncidence(final Annotation annotation, final Method method, final Object[] arguments,
			final FramedGraph framedGraph, final Vertex vertex) {
		Direction dir = Direction.BOTH;
		String label = "";
		boolean unique = false;
		if (annotation instanceof Adjacency) {
			dir = ((Adjacency) annotation).direction();
			label = ((Adjacency) annotation).label();
		} else if (annotation instanceof AdjacencyUnique) {
			dir = ((AdjacencyUnique) annotation).direction();
			label = ((AdjacencyUnique) annotation).label();
			unique = true;
		} else if (annotation instanceof Incidence) {
			dir = ((Incidence) annotation).direction();
			label = ((Incidence) annotation).label();
		} else if (annotation instanceof IncidenceUnique) {
			dir = ((IncidenceUnique) annotation).direction();
			label = ((IncidenceUnique) annotation).label();
			unique = true;
		}

		if (ClassUtilities.isGetMethod(method)) {
			Class<?> returnType = method.getReturnType();
			if (Iterable.class.isAssignableFrom(returnType)) {
				DEdgeList edgeList = (DEdgeList) vertex.getEdges(dir, label);
				edgeList.setUnique(unique);
				return new FramedEdgeList(framedGraph, vertex, edgeList, ClassUtilities.getGenericClass(method));
			} else if (Edge.class.isAssignableFrom(returnType)) {
				return vertex.getEdges(dir, label).iterator().next();
			} else {
				Edge e = vertex.getEdges(dir, label).iterator().next();
				return framedGraph.frame(e, returnType);
			}
		} else if (AnnotationUtilities.isFindMethod(method)) {
			Vertex newVertex;
			Edge resultEdge = null;
			newVertex = ((VertexFrame) arguments[0]).asVertex();
			resultEdge = findEdge(annotation, framedGraph, vertex, newVertex);
			if (resultEdge != null) {
				return framedGraph.frame(resultEdge, method.getReturnType());
			}
		} else if (AnnotationUtilities.isCountMethod(method)) {
			return countEdges(annotation, vertex);
		} else if (ClassUtilities.isAddMethod(method)) {
			Vertex newVertex;
			Edge resultEdge = null;
			newVertex = ((VertexFrame) arguments[0]).asVertex();
			if (unique) {
				resultEdge = findEdge(annotation, framedGraph, vertex, newVertex);
			}
			if (resultEdge == null) {
				String replicaid = null;
				if (framedGraph instanceof DFramedTransactionalGraph) {
					DElementStore store = ((DFramedTransactionalGraph) framedGraph).getElementStore(method.getReturnType());
					long rawkey = store.getStoreKey();
					replicaid = NoteCoordinate.Utils.getReplidFromLong(rawkey);
				}
				resultEdge = addEdge(annotation, framedGraph, vertex, newVertex, replicaid);
			}
			return framedGraph.frame(resultEdge, method.getReturnType());
		} else if (ClassUtilities.isRemoveMethod(method)) {
			framedGraph.removeEdge(((EdgeFrame) arguments[0]).asEdge());
			return null;
		}

		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object processVertexAdjacency(final Annotation annotation, final Method method, final Object[] arguments,
			final FramedGraph framedGraph, final Vertex vertex) {
		Edge resultEdge = null;
		Class<?> returnType = method.getReturnType();
		Direction dir = Direction.BOTH;
		String label = "";
		boolean unique = false;
		if (annotation instanceof Adjacency) {
			dir = ((Adjacency) annotation).direction();
			label = ((Adjacency) annotation).label();
		} else if (annotation instanceof AdjacencyUnique) {
			dir = ((AdjacencyUnique) annotation).direction();
			label = ((AdjacencyUnique) annotation).label();
			unique = true;
		} else if (annotation instanceof Incidence) {
			dir = ((Incidence) annotation).direction();
			label = ((Incidence) annotation).label();
		} else if (annotation instanceof IncidenceUnique) {
			dir = ((IncidenceUnique) annotation).direction();
			label = ((IncidenceUnique) annotation).label();
			unique = true;
		}

		if (ClassUtilities.isGetMethod(method)) {
			final FramedVertexList r = new FramedVertexList(framedGraph, vertex, vertex.getVertices(dir, label),
					ClassUtilities.getGenericClass(method));
			if (ClassUtilities.returnsIterable(method)) {
				return r;
			} else {
				return r.iterator().hasNext() ? r.iterator().next() : null;
			}
		} else if (ClassUtilities.isAddMethod(method)) {
			Vertex newVertex;
			Object returnValue = null;
			if (arguments == null) {
				// Use this method to get the vertex so that the vertex
				// initializer is called.
				returnValue = framedGraph.addVertex(null, returnType);
				newVertex = ((VertexFrame) returnValue).asVertex();
			} else {
				newVertex = ((VertexFrame) arguments[0]).asVertex();
			}
			if (unique) {
				resultEdge = findEdge(annotation, framedGraph, vertex, newVertex);
			}
			if (resultEdge == null) {
				String replicaid = null;
				if (framedGraph instanceof DFramedTransactionalGraph) {
					DElementStore store = ((DFramedTransactionalGraph) framedGraph).getElementStore(returnType);
					long rawkey = store.getStoreKey();
					replicaid = NoteCoordinate.Utils.getReplidFromLong(rawkey);
				}
				resultEdge = addEdge(annotation, framedGraph, vertex, newVertex, replicaid);
			}
			if (returnType.isPrimitive()) {
				return null;
			} else if (Edge.class.isAssignableFrom(returnType)) {
				return resultEdge;
			} else if (EdgeFrame.class.isAssignableFrom(returnType)) {
				//				System.out.println("TEMP DEBUG about to wrap edge with id " + resultEdge.getId());
				Object result = framedGraph.frame(resultEdge, returnType);
				return result;
			} else {
				return returnValue;
			}
		} else if (ClassUtilities.isRemoveMethod(method)) {
			removeEdges(dir, label, vertex, ((VertexFrame) arguments[0]).asVertex(), framedGraph);
			return null;
		} else if (AnnotationUtilities.isCountMethod(method)) {
			return countEdges(annotation, vertex);
		} else if (ClassUtilities.isSetMethod(method)) {
			removeEdges(dir, label, vertex, null, framedGraph);
			if (ClassUtilities.acceptsIterable(method)) {
				for (Object o : (Iterable) arguments[0]) {
					Vertex v = ((VertexFrame) o).asVertex();
					addEdge(annotation, framedGraph, vertex, v, null);
				}
				return null;
			} else {
				if (null != arguments[0]) {
					Vertex newVertex = ((VertexFrame) arguments[0]).asVertex();
					addEdge(annotation, framedGraph, vertex, newVertex, null);
				}
				return null;
			}
		}

		return null;
	}

	@SuppressWarnings("rawtypes")
	private Edge findEdge(final Annotation annotation, final FramedGraph framedGraph, final Vertex vertex, final Vertex newVertex) {
		Edge result = null;
		Direction dir = Direction.BOTH;
		String label = "";
		boolean unique = false;
		if (annotation instanceof Adjacency) {
			dir = ((Adjacency) annotation).direction();
			label = ((Adjacency) annotation).label();
		} else if (annotation instanceof AdjacencyUnique) {
			dir = ((AdjacencyUnique) annotation).direction();
			label = ((AdjacencyUnique) annotation).label();
			unique = true;
		} else if (annotation instanceof Incidence) {
			dir = ((Incidence) annotation).direction();
			label = ((Incidence) annotation).label();
		} else if (annotation instanceof IncidenceUnique) {
			dir = ((IncidenceUnique) annotation).direction();
			label = ((IncidenceUnique) annotation).label();
			unique = true;
		}

		switch (dir) {
		case OUT:
			result = ((DVertex) vertex).findOutEdge(newVertex, label, unique);
			break;
		case IN:
			result = ((DVertex) vertex).findInEdge(newVertex, label, unique);
			break;
		default:
			break;
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	private int countEdges(final Annotation annotation, final Vertex vertex) {
		int result = 0;
		Direction dir = Direction.BOTH;
		String label = "";
		if (annotation instanceof Adjacency) {
			dir = ((Adjacency) annotation).direction();
			label = ((Adjacency) annotation).label();
		} else if (annotation instanceof AdjacencyUnique) {
			dir = ((AdjacencyUnique) annotation).direction();
			label = ((AdjacencyUnique) annotation).label();
		} else if (annotation instanceof Incidence) {
			dir = ((Incidence) annotation).direction();
			label = ((Incidence) annotation).label();
		} else if (annotation instanceof IncidenceUnique) {
			dir = ((IncidenceUnique) annotation).direction();
			label = ((IncidenceUnique) annotation).label();
		}
		switch (dir) {
		case OUT:
			if (vertex instanceof DVertex) {
				result = ((DVertex) vertex).getOutEdgeCount(label);
			} else {
				Iterable<Edge> it = vertex.getEdges(Direction.OUT, label);
				if (it instanceof Collection) {
					result = ((Collection) it).size();
				} else {
					for (Edge e : it) {
						result++;
					}
				}
			}
			break;
		case IN:
			if (vertex instanceof DVertex) {
				result = ((DVertex) vertex).getInEdgeCount(label);
			} else {
				Iterable<Edge> it = vertex.getEdges(Direction.IN, label);
				if (it instanceof Collection) {
					result = ((Collection) it).size();
				} else {
					for (Edge e : it) {
						result++;
					}
				}
			}
			break;
		case BOTH:
			throw new UnsupportedOperationException("Direction.BOTH it not supported on 'add' or 'set' methods");
		}
		return result;
	}

	protected NoteCoordinate getForcedId(final Vertex outVertex, final Vertex inVertex, final String label, final String replicaid) {
		String outString = ((NoteCoordinate) outVertex.getId()).toString();
		String inString = ((NoteCoordinate) inVertex.getId()).toString();
		String targetid = DominoUtils.toUnid(outString + label + inString);
		return NoteCoordinate.Utils.getNoteCoordinate(replicaid, targetid);
	}

	@SuppressWarnings("rawtypes")
	private Edge addEdge(final Annotation annotation, final FramedGraph framedGraph, final Vertex vertex, final Vertex newVertex,
			final String replicaid) {
		Edge result = null;
		Direction dir = Direction.BOTH;
		String label = "";
		boolean unique = false;
		if (annotation instanceof Adjacency) {
			dir = ((Adjacency) annotation).direction();
			label = ((Adjacency) annotation).label();
		} else if (annotation instanceof AdjacencyUnique) {
			dir = ((AdjacencyUnique) annotation).direction();
			label = ((AdjacencyUnique) annotation).label();
			unique = true;
		} else if (annotation instanceof Incidence) {
			dir = ((Incidence) annotation).direction();
			label = ((Incidence) annotation).label();
		} else if (annotation instanceof IncidenceUnique) {
			dir = ((IncidenceUnique) annotation).direction();
			label = ((IncidenceUnique) annotation).label();
			unique = true;
		}
		NoteCoordinate id = null;
		switch (dir) {
		case OUT:
			if (unique)
				id = getForcedId(vertex, newVertex, label, replicaid);
			result = framedGraph.addEdge(id, vertex, newVertex, label);
			break;
		case IN:
			if (unique)
				id = getForcedId(newVertex, vertex, label, replicaid);
			result = framedGraph.addEdge(id, newVertex, vertex, label);
			break;
		case BOTH:
			throw new UnsupportedOperationException("Direction.BOTH it not supported on 'add' or 'set' methods");
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	private void removeEdges(final Direction direction, final String label, final Vertex element, final Vertex otherVertex,
			final FramedGraph framedGraph) {
		for (final Edge edge : element.getEdges(direction, label)) {
			if (null == otherVertex || edge.getVertex(direction.opposite()).equals(otherVertex)) {
				framedGraph.removeEdge(edge);
			}
		}
	}

}

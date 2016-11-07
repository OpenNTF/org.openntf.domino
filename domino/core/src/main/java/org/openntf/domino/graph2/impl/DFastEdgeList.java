package org.openntf.domino.graph2.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.openntf.domino.big.NoteCoordinate;
import org.openntf.domino.big.NoteList;
import org.openntf.domino.graph2.DEdgeList;
import org.openntf.domino.utils.DominoUtils;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public class DFastEdgeList implements org.openntf.domino.graph2.DEdgeList {
	protected final DVertex sourceVertex_;
	protected final DGraph parentGraph_;
	protected List<NoteCoordinate> delegate_;
	protected boolean isUnique_;
	protected String label_;
	protected String storeid_;

	public static class DFastEdgeIterator implements ListIterator<Edge> {
		private ListIterator<NoteCoordinate> delegate_;
		private DVertex source_;
		private DGraph parent_;

		public DFastEdgeIterator(final DVertex source, final DGraph parent, final ListIterator<NoteCoordinate> delegate) {
			source_ = source;
			parent_ = parent;
			delegate_ = delegate;
		}

		@Override
		public void add(final Edge arg0) {
			delegate_.add(getNC(arg0));
		}

		@Override
		public boolean hasNext() {
			return delegate_.hasNext();
		}

		@Override
		public boolean hasPrevious() {
			return delegate_.hasPrevious();
		}

		@Override
		public Edge next() {
			Edge result = null;
			try {
				result = parent_.getEdge(delegate_.next());
			} catch (Throwable t) {
				System.err.println(
						"Exception caught iterating an edge list. This is most likely caused by data corruption, typically because a replicaid changed. Bypassing for now...");
				if (hasNext()) {
					result = next();
				}
			}
			return result;
		}

		@Override
		public int nextIndex() {
			return delegate_.nextIndex();
		}

		@Override
		public Edge previous() {
			Edge result = null;
			try {
				result = parent_.getEdge(delegate_.previous());
			} catch (Throwable t) {
				System.err.println(
						"Exception caught iterating an edge list. This is most likely caused by data corruption, typically because a replicaid changed. Bypassing for now...");
				if (hasPrevious()) {
					result = previous();
				}
			}
			return result;
		}

		@Override
		public int previousIndex() {
			return delegate_.previousIndex();
		}

		@Override
		public void remove() {
			delegate_.remove();
		}

		@Override
		public void set(final Edge arg0) {
			delegate_.set(getNC(arg0));
		}

	}

	private static NoteCoordinate getNC(final Edge edge) {
		Object rawid = edge.getId();
		if (rawid instanceof NoteCoordinate) {
			return (NoteCoordinate) rawid;
		}
		return null;
	}

	private static Collection<NoteCoordinate> getNCs(final Collection<?> arg0) {
		if (arg0 == null)
			return null;
		Collection<NoteCoordinate> ncs = new ArrayList<NoteCoordinate>();
		for (Object raw : arg0) {
			if (raw instanceof Edge) {
				ncs.add(getNC((Edge) raw));
			}
		}
		return ncs;
	}

	public DFastEdgeList(final DVertex source, final DGraph parent, final NoteList notelist, final String label) {
		sourceVertex_ = source;
		delegate_ = Collections.synchronizedList(notelist);
		parentGraph_ = parent;
		label_ = label;
		if (!notelist.isEmpty()) {
			NoteCoordinate nc = notelist.get(0);
			storeid_ = nc.getReplicaId();
		}
		//		Factory.println("DFastEdgeList " + label + " initialized for source vertex " + source.getId() + " , size " + delegate_.size());
		//		if (delegate_.size() > 0) {
		//			Throwable t = new Throwable();
		//			t.printStackTrace();
		//		}
	}

	@Deprecated
	public DFastEdgeList(final DVertex source, final DGraph parent, final NoteList notelist) {
		sourceVertex_ = source;
		delegate_ = Collections.synchronizedList(notelist);
		parentGraph_ = parent;
		if (!notelist.isEmpty()) {
			NoteCoordinate nc = notelist.get(0);
			storeid_ = nc.getReplicaId();
		}
		//		Factory.println("DFastEdgeList initialized for source vertex " + source.getId() + " , size " + delegate_.size());
		//		if (delegate_.size() > 0) {
		//			Throwable t = new Throwable();
		//			t.printStackTrace();
		//		}
	}

	//	Throwable t_ = null;

	@Override
	public boolean add(final Edge arg0) {
		//		System.out.println("Adding an edge to a DFastEdgeList");
		NoteCoordinate nc = getNC(arg0);
		if (storeid_ == null) {
			storeid_ = nc.getReplicaId();
		}
		if (!delegate_.contains(nc)) {
			//			if ("DirectedBy".equals(label_)) {
			//				t_ = new Throwable();
			//			}
			return delegate_.add(nc);
		} else {
			//			if ("DirectedBy".equals(label_)) {
			//				org.openntf.domino.utils.Factory.println("TEMP DEBUG Stopping an add of edge " + arg0.getId()
			//						+ " because it is already in the list. Here's where it's trying to add...");
			//				Throwable t = new Throwable();
			//				t.printStackTrace();
			//				if (t_ != null) {
			//					org.openntf.domino.utils.Factory.println("TEMP DEBUG And here is the prior call stack...");
			//					t_.printStackTrace();
			//				} else {
			//					Factory.println("TEMP DEBUG prior stack trace is null!??!");
			//				}
			//			}
			return false;
		}
	}

	@Override
	public void add(final int arg0, final Edge arg1) {
		NoteCoordinate nc = getNC(arg1);
		if (storeid_ == null) {
			storeid_ = nc.getReplicaId();
		}
		delegate_.add(arg0, nc);
	}

	@Override
	public boolean addAll(final Collection<? extends Edge> arg0) {
		return delegate_.addAll(getNCs(arg0));
	}

	@Override
	public boolean addAll(final int arg0, final Collection<? extends Edge> arg1) {
		return delegate_.addAll(arg0, getNCs(arg1));
	}

	@Override
	public void clear() {
		delegate_.clear();
	}

	@Override
	public boolean contains(final Object arg0) {
		if (arg0 == null)
			return false;
		if (arg0 instanceof Edge) {
			NoteCoordinate nc = getNC((Edge) arg0);
			return delegate_.contains(nc);
		} else if (arg0 instanceof NoteCoordinate) {
			return delegate_.contains(arg0);
		} else if (arg0 instanceof CharSequence) {
			NoteCoordinate nc = NoteCoordinate.Utils.getNoteCoordinate((CharSequence) arg0);
			return delegate_.contains(nc);
		} else {
			throw new IllegalArgumentException(
					"Cannot check whether a DFastEdgeList contains an object of type " + arg0.getClass().getName());
		}
	}

	@Override
	public boolean containsAll(final Collection<?> arg0) {
		boolean result = true;
		for (Object raw : arg0) {
			result = contains(raw);
			if (!result) {
				break;
			}
		}
		return result;
	}

	@Override
	public Edge get(final int arg0) {
		NoteCoordinate nc = delegate_.get(arg0);
		return parentGraph_.getEdge(nc);
	}

	@Override
	public int indexOf(final Object arg0) {
		if (arg0 == null)
			return -1;
		if (arg0 instanceof Edge) {
			NoteCoordinate nc = getNC((Edge) arg0);
			return delegate_.indexOf(nc);
		} else if (arg0 instanceof NoteCoordinate) {
			return delegate_.indexOf(arg0);
		} else if (arg0 instanceof CharSequence) {
			NoteCoordinate nc = NoteCoordinate.Utils.getNoteCoordinate((CharSequence) arg0);
			return delegate_.indexOf(nc);
		} else {
			throw new IllegalArgumentException("Cannot get the index of an object of type " + arg0.getClass().getName());
		}
	}

	@Override
	public boolean isEmpty() {
		return delegate_.isEmpty();
	}

	@Override
	public Iterator<Edge> iterator() {
		return new DFastEdgeIterator(sourceVertex_, parentGraph_, delegate_.listIterator());
	}

	@Override
	public int lastIndexOf(final Object arg0) {
		if (arg0 == null)
			return -1;
		if (arg0 instanceof Edge) {
			NoteCoordinate nc = getNC((Edge) arg0);
			return delegate_.lastIndexOf(nc);
		} else if (arg0 instanceof NoteCoordinate) {
			return delegate_.lastIndexOf(arg0);
		} else if (arg0 instanceof CharSequence) {
			NoteCoordinate nc = NoteCoordinate.Utils.getNoteCoordinate((CharSequence) arg0);
			return delegate_.lastIndexOf(nc);
		} else {
			throw new IllegalArgumentException("Cannot get the index of an object of type " + arg0.getClass().getName());
		}
	}

	@Override
	public ListIterator<Edge> listIterator() {
		return new DFastEdgeIterator(sourceVertex_, parentGraph_, delegate_.listIterator());
	}

	@Override
	public ListIterator<Edge> listIterator(final int arg0) {
		return new DFastEdgeIterator(sourceVertex_, parentGraph_, delegate_.listIterator(arg0));
	}

	@Override
	public Edge remove(final int arg0) {
		NoteCoordinate nc = delegate_.remove(arg0);
		return parentGraph_.getEdge(nc);
	}

	@Override
	public boolean remove(final Object arg0) {
		if (arg0 == null)
			return false;
		if (arg0 instanceof Edge) {
			NoteCoordinate nc = getNC((Edge) arg0);
			return delegate_.remove(nc);
		} else if (arg0 instanceof NoteCoordinate) {
			return delegate_.remove(arg0);
		} else if (arg0 instanceof CharSequence) {
			NoteCoordinate nc = NoteCoordinate.Utils.getNoteCoordinate((CharSequence) arg0);
			return delegate_.remove(nc);
		} else {
			throw new IllegalArgumentException("Cannot remove an object of type " + arg0.getClass().getName());
		}
	}

	@Override
	public boolean removeAll(final Collection<?> arg0) {
		boolean result = false;
		for (Object raw : arg0) {
			if (remove(raw))
				result = true;
		}
		return result;
	}

	@Override
	public boolean retainAll(final Collection<?> arg0) {
		return delegate_.retainAll(getNCs(arg0));
	}

	@Override
	public Edge set(final int arg0, final Edge arg1) {
		NoteCoordinate nc = delegate_.set(arg0, getNC(arg1));
		if (storeid_ == null) {
			storeid_ = nc.getReplicaId();
		}
		return parentGraph_.getEdge(nc);
	}

	@Override
	public int size() {
		return delegate_.size();
	}

	@Override
	public List<Edge> subList(final int arg0, final int arg1) {
		DFastEdgeList result = new DFastEdgeList(sourceVertex_, parentGraph_, (NoteList) delegate_.subList(arg0, arg1), label_);
		//		result.setLabel(label_);
		result.setUnique(isUnique_);
		return result;
	}

	@Override
	public Object[] toArray() {
		return delegate_.toArray();
	}

	@Override
	public <T> T[] toArray(final T[] arg0) {
		return delegate_.toArray(arg0);
	}

	@Override
	public DEdgeList atomic() {
		return this;
	}

	@Override
	public DEdgeList unmodifiable() {
		return this;
	}

	@Override
	public Edge findEdge(final Vertex toVertex) {
		Edge result = null;
		Object toId = toVertex.getId();
		Object fromId = sourceVertex_.getId();
		if (isUnique() && !isEmpty()) {
			if (toId instanceof NoteCoordinate && fromId instanceof NoteCoordinate) {
				String toString = ((NoteCoordinate) toId).toString();
				String fromString = ((NoteCoordinate) fromId).toString();
				String testString1 = DominoUtils.toUnid(toString + getLabel() + fromString);
				String testString2 = DominoUtils.toUnid(toString + getLabel() + fromString);
				NoteCoordinate nc1 = NoteCoordinate.Utils.getNoteCoordinate(storeid_, testString1);
				NoteCoordinate nc2 = NoteCoordinate.Utils.getNoteCoordinate(storeid_, testString2);
				if (contains(nc1)) {
					result = parentGraph_.getEdge(nc1);
				} else if (contains(nc2)) {
					result = parentGraph_.getEdge(nc2);
				}
			} else {
				//NTF then we go back to the old way...
			}
		}
		if (result == null && this.size() > 0 && !isUnique()) {
			for (Edge edge : this) {
				if (edge instanceof DEdge) {
					DEdge dedge = (DEdge) edge;
					if (toId.equals(dedge.getOtherVertexId(sourceVertex_))) {
						result = dedge;
						break;
					}
				} else {
					if (edge != null) {
						Vertex inVertex = edge.getVertex(Direction.IN);
						if (fromId.equals(inVertex.getId())) {
							if (toId.equals(edge.getVertex(Direction.OUT))) {
								result = edge;
								break;
							}
						} else if (toId.equals(inVertex.getId())) {
							result = edge;
							break;
						}
					}
				}
			}
		} else {
			//			System.out.println("DEBUG: No edges defined in EdgeList");
		}
		return result;
	}

	@Override
	public DEdgeList applyFilter(final String key, final Object value) {
		DEdgeList result = new org.openntf.domino.graph2.impl.DEdgeList(sourceVertex_);
		if (this.size() > 0) {
			for (Edge edge : this) {
				if (edge instanceof DEdge) {
					DEdge dedge = (DEdge) edge;
					if (value.equals(dedge.getProperty(key))) {
						result.add(edge);
					}
				}
			}
		}
		return result;
	}

	@Override
	public DVertexList toVertexList() {
		//		System.out.println("TEMP DEBUG Converting an edge list to a vertex list");
		DVertexList result = new DVertexList(sourceVertex_);
		if (this.size() > 0) {
			for (Edge edge : this) {
				if (edge instanceof DEdge) {
					DEdge dedge = (DEdge) edge;
					try {
						DVertex vert = (DVertex) dedge.getOtherVertex(sourceVertex_);
						result.add(vert);
					} catch (Throwable t) {
						t.printStackTrace();
					}
				} else {
					//					System.out.println("TEMP DEBUG EdgeList didn't have a DEdge. It had a " + edge.getClass().getName());
				}
			}
		} else {
			//			System.out.println("TEMP DEBUG EdgeList size is not greater than 0.");
		}
		return result;
	}

	@Override
	public boolean isUnique() {
		return isUnique_;
	}

	@Override
	public void setUnique(final boolean isUnique) {
		isUnique_ = true;
	}

	@Override
	public String getLabel() {
		return label_;
	}

	@Override
	public void setLabel(final String label) {
		label_ = label;
		//		Factory.println("Setting label to " + label);
	}

}

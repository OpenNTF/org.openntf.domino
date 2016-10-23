package org.openntf.domino.graph2.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewNavigator;
import org.openntf.domino.big.ViewEntryCoordinate;
import org.openntf.domino.big.impl.ViewEntryList;
import org.openntf.domino.exceptions.UnimplementedException;
import org.openntf.domino.graph2.DEdgeList;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Vertex;

public class DEdgeEntryList implements DEdgeList {
	public static class DEdgeEntryListIterator implements ListIterator<Edge> {
		private ListIterator<ViewEntryCoordinate> delegate_;
		private DVertex source_;
		private DElementStore store_;

		public DEdgeEntryListIterator(final ListIterator<ViewEntryCoordinate> iterator, final DVertex source, final DElementStore store) {
			delegate_ = iterator;
			source_ = source;
			store_ = store;
			//			System.out.println("TEMP DEBUG Creating a DEdgeEntryListIterator from a " + iterator.getClass().getName());
		}

		@Override
		public boolean hasNext() {
			boolean result = delegate_.hasNext();
			//			System.out.println("TEMP DEBUG hasNext resulted in a " + String.valueOf(result));
			return result;
		}

		@Override
		public Edge next() {
			DEntryEdge result = null;
			ViewEntryCoordinate vec = delegate_.next();
			if (vec != null) {
				Element raw = store_.getElement(vec);
				if (raw instanceof Edge) {
					Edge edge = (Edge) raw;
					if (edge instanceof DEntryEdge) {
						result = (DEntryEdge) edge;
						result.setInVertex(source_);
						//				System.out.println("TEMP DEBUG edge " + result.getDelegate().getClass().getName());
						return result;
					} else {
						throw new IllegalStateException(
								"ElementStore did not return a DEntryEdge. It returned a " + edge.getClass().getName());
					}
				} else {
					//					System.out.println("Next entry is not an edge. It's a " + raw.getClass().getName() + " id: " + raw.getId());
					//					System.out.println("VEC is " + vec.getPosition() + ": " + vec.getUNID());
				}
			}
			return null;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("DEdgeEntryLists are not modifiable because they are based on view indexes");
		}

		@Override
		public void add(final Edge arg0) {
			throw new UnsupportedOperationException("DEdgeEntryLists are not modifiable because they are based on view indexes");
		}

		@Override
		public boolean hasPrevious() {
			return delegate_.hasPrevious();
		}

		@Override
		public int nextIndex() {
			return delegate_.nextIndex();
		}

		@Override
		public Edge previous() {
			DEntryEdge result = (DEntryEdge) store_.getElement(delegate_.previous());
			result.setInVertex(source_);
			return result;
		}

		@Override
		public int previousIndex() {
			return delegate_.previousIndex();
		}

		@Override
		public void set(final Edge arg0) {
			throw new UnsupportedOperationException("DEdgeEntryLists are not modifiable because they are based on view indexes");
		}

	}

	private ViewEntryList entryList_;
	private DVertex source_;
	private DElementStore store_;
	protected boolean isUnique_;
	protected String label_;

	private DEdgeEntryList(final DVertex source, final DElementStore store, final ViewEntryList entryList) {
		source_ = source;
		store_ = store;
		entryList_ = entryList;
	}

	public DEdgeEntryList(final DVertex source, final DElementStore store) {
		source_ = source;
		store_ = store;
		initEntryList();
	}

	//	public DEdgeEntryList(final DVertex source, final DElementStore store, final List<CharSequence> startkeys) {
	//		source_ = source;
	//		store_ = store;
	//		initEntryList(startkeys);
	//	}

	protected void initEntryList() {
		ViewNavigator nav = null;
		if (source_.getDelegateType().equals(org.openntf.domino.View.class)) {
			nav = source_.getView().createViewNavMaxLevel(0);
		} else if (source_ instanceof DCategoryVertex) {
			nav = ((DCategoryVertex) source_).getSubNavigator();
		} else {
			throw new IllegalStateException("Cannot create a DEdgeEntryList from a Vertex backed by a " + source_.getClass().getName());
		}
		//		System.out.println("TEMP DEBUG EntryList navigator from ViewVertex has " + nav.getCount() + " entries");
		entryList_ = new ViewEntryList(nav);
	}

	public void initEntryList(final List<CharSequence> list) {
		ViewNavigator nav = null;
		if (source_.getDelegateType().equals(org.openntf.domino.View.class)) {
			View view = source_.getView();
			Vector<Object> repeatKeys = new Vector<Object>();
			repeatKeys.addAll(list);
			ViewEntry entry = view.getFirstEntryByKey(repeatKeys, false);
			nav = view.createViewNavFrom(entry);
			entryList_ = new ViewEntryList(nav);
		} else {
			throw new IllegalArgumentException("Cannot use start keys on anything except a view root.");
		}
	}

	@Override
	public boolean add(final Edge arg0) {
		throw new UnsupportedOperationException("DEdgeEntryLists are not modifiable because they are based on view indexes");
	}

	@Override
	public void add(final int arg0, final Edge arg1) {
		throw new UnsupportedOperationException("DEdgeEntryLists are not modifiable because they are based on view indexes");
	}

	@Override
	public boolean addAll(final Collection<? extends Edge> arg0) {
		throw new UnsupportedOperationException("DEdgeEntryLists are not modifiable because they are based on view indexes");
	}

	@Override
	public boolean addAll(final int arg0, final Collection<? extends Edge> arg1) {
		throw new UnsupportedOperationException("DEdgeEntryLists are not modifiable because they are based on view indexes");
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("DEdgeEntryLists are not modifiable because they are based on view indexes");
	}

	@Override
	public boolean contains(final Object arg0) {
		if (arg0 instanceof DEntryEdge) {
			Object rawid = ((DEntryEdge) arg0).getId();
			return entryList_.contains(rawid);
		} else {
			throw new IllegalArgumentException("Cannot report on the last index of a " + arg0.getClass().getName());
		}
	}

	@Override
	public boolean containsAll(final Collection<?> arg0) {
		throw new UnimplementedException();
	}

	@Override
	public DEdge get(final int arg0) {
		DEntryEdge result = (DEntryEdge) store_.getElement(entryList_.get(arg0));
		result.setInVertex(source_);
		return result;
	}

	@Override
	public int indexOf(final Object arg0) {
		if (arg0 instanceof DEntryEdge) {
			Object rawid = ((DEntryEdge) arg0).getId();
			return entryList_.indexOf(rawid);
		} else {
			throw new IllegalArgumentException("Cannot report on the last index of a " + arg0.getClass().getName());
		}
	}

	@Override
	public boolean isEmpty() {
		return entryList_.isEmpty();
	}

	@Override
	public Iterator<Edge> iterator() {
		return new DEdgeEntryListIterator(entryList_.listIterator(), source_, store_);
	}

	@Override
	public int lastIndexOf(final Object arg0) {
		if (arg0 instanceof DEntryEdge) {
			Object rawid = ((DEntryEdge) arg0).getId();
			return entryList_.lastIndexOf(rawid);
		} else {
			throw new IllegalArgumentException("Cannot report on the last index of a " + arg0.getClass().getName());
		}
	}

	@Override
	public ListIterator<Edge> listIterator() {
		return new DEdgeEntryListIterator(entryList_.listIterator(), source_, store_);
	}

	@Override
	public ListIterator<Edge> listIterator(final int arg0) {
		return new DEdgeEntryListIterator(entryList_.listIterator(arg0), source_, store_);
	}

	@Override
	public Edge remove(final int index) {
		throw new UnsupportedOperationException("DEdgeEntryLists are not modifiable because they are based on view indexes");
	}

	@Override
	public boolean remove(final Object arg0) {
		throw new UnsupportedOperationException("DEdgeEntryLists are not modifiable because they are based on view indexes");
	}

	@Override
	public boolean removeAll(final Collection<?> arg0) {
		throw new UnsupportedOperationException("DEdgeEntryLists are not modifiable because they are based on view indexes");
	}

	@Override
	public boolean retainAll(final Collection<?> arg0) {
		throw new UnsupportedOperationException("DEdgeEntryLists are not modifiable because they are based on view indexes");
	}

	@Override
	public DEdge set(final int arg0, final Edge arg1) {
		throw new UnsupportedOperationException("DEdgeEntryLists are not modifiable because they are based on view indexes");
	}

	@Override
	public int size() {
		return entryList_.size();
	}

	@Override
	public List<Edge> subList(final int arg0, final int arg1) {
		ViewEntryList sublist = (ViewEntryList) entryList_.subList(arg0, arg1);
		return new DEdgeEntryList(source_, store_, sublist);
	}

	@Override
	public Object[] toArray() {
		throw new UnimplementedException();
	}

	@Override
	public <T> T[] toArray(final T[] arg0) {
		throw new UnimplementedException();
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
		Object fromId = source_.getId();
		if (this.size() > 0) {
			for (Edge edge : this) {
				if (edge instanceof DEdge) {
					DEdge dedge = (DEdge) edge;
					if (toId.equals(dedge.getOtherVertexId(source_))) {
						result = dedge;
						break;
					}
				} else {
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
		} else {
			//			System.out.println("DEBUG: No edges defined in EdgeList");
		}
		return result;
	}

	@Override
	public DEdgeList applyFilter(final String key, final Object value) {
		//FIXME NTF This is really broken. You totally wouldn't be able to do this
		DEdgeList result = new DEdgeEntryList(source_, store_);
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
		DVertexList result = new DVertexList(source_);
		if (this.size() > 0) {
			for (Edge edge : this) {
				if (edge instanceof DEdge) {
					DEdge dedge = (DEdge) edge;
					DVertex vert = (DVertex) dedge.getOtherVertex(source_);
					result.add(vert);
				}
			}
		}
		return result;
	}

	@Override
	public boolean isUnique() {
		return isUnique_;
	}

	@Override
	public void setUnique(final boolean isUnique) {
		isUnique_ = isUnique;
	}

	@Override
	public String getLabel() {
		return label_;
	}

	@Override
	public void setLabel(final String label) {
		label_ = label;
	}

}

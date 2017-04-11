package org.openntf.domino.rest.resources.frames;

import com.ibm.commons.util.io.json.JsonArray;
import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonObject;
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.VertexFrame;

import java.util.Iterator;
import java.util.List;

import org.openntf.domino.graph2.annotations.FramedEdgeList;
import org.openntf.domino.graph2.annotations.FramedVertexList;
import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.rest.service.Parameters.ParamMap;

public class JsonFrameListAdapter implements JsonArray {
	protected static class FrameListAdapterIterator implements Iterator<Object> {
		protected final JsonFrameListAdapter parent_;
		protected final Iterator<Object> iterator_;

		public FrameListAdapterIterator(JsonFrameListAdapter parent) {
			parent_ = parent;
			iterator_ = parent.getList().iterator();
		}

		@Override
		public boolean hasNext() {
			return iterator_.hasNext();
		}

		@Override
		public Object next() {
			Object raw = iterator_.next();
			if (raw == null) {
				return null;
			} else if (raw instanceof VertexFrame) {
				return new JsonFrameAdapter(parent_.getGraph(), (VertexFrame) raw, parent_.getParamMap(),
						parent_.isCollectionRoute_);
			} else if (raw instanceof EdgeFrame) {
				return new JsonFrameAdapter(parent_.getGraph(), (EdgeFrame) raw, parent_.getParamMap(),
						parent_.isCollectionRoute_);
			} else {
				throw new IllegalStateException("Iterator returned a " + raw.getClass().getName());
			}
		}

		@Override
		public void remove() {
			iterator_.remove();
		}

	}

	protected DFramedTransactionalGraph<?> graph_;
	protected List<Object> list_;
	protected ParamMap pm_;
	protected boolean isVertex_;
	protected boolean isCollectionRoute_;

	@SuppressWarnings("unchecked")
	public JsonFrameListAdapter(DFramedTransactionalGraph<?> graph, FramedEdgeList<?> edgeList, ParamMap pm,
			boolean isCollectionRoute) {
		graph_ = graph;
		list_ = (List<Object>) edgeList;
		pm_ = pm;
		isVertex_ = false;
		isCollectionRoute_ = isCollectionRoute;
		// System.out.println("TEMP DEBUG JsonFrameListAdapter created from EdgeList");
	}

	@SuppressWarnings("unchecked")
	public JsonFrameListAdapter(DFramedTransactionalGraph<?> graph, FramedVertexList<?> vertexList, ParamMap pm,
			boolean isCollectionRoute) {
		graph_ = graph;
		list_ = (List<Object>) vertexList;
		pm_ = pm;
		isVertex_ = true;
		isCollectionRoute_ = isCollectionRoute;
		// System.out.println("TEMP DEBUG JsonFrameListAdapter created from VertexList");
	}

	protected DFramedTransactionalGraph<?> getGraph() {
		return graph_;
	}

	protected ParamMap getParamMap() {
		return pm_;
	}

	protected List<Object> getList() {
		return list_;
	}

	protected boolean isVertex() {
		return isVertex_;
	}

	@Override
	public Iterator<Object> iterator() {
		return new FrameListAdapterIterator(this);
	}

	@Override
	public int length() throws JsonException {
		return getList().size();
	}

	@Override
	public Object get(int paramInt) throws JsonException {
		if (isVertex()) {
			return new JsonFrameAdapter(getGraph(), (VertexFrame) getList().get(paramInt), getParamMap(),
					isCollectionRoute_);
		} else {
			return new JsonFrameAdapter(getGraph(), (EdgeFrame) getList().get(paramInt), getParamMap(),
					isCollectionRoute_);
		}
	}

	@Override
	public String getString(int paramInt) throws JsonException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getNumber(int paramInt) throws JsonException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean getBoolean(int paramInt) throws JsonException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public JsonObject getObject(int paramInt) throws JsonException {
		return (JsonObject) get(paramInt);
	}

	@Override
	public JsonArray getArray(int paramInt) throws JsonException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void put(int paramInt, Object paramObject) throws JsonException {
		// TODO Auto-generated method stub

	}

	@Override
	public void putString(int paramInt, String paramString) throws JsonException {
		// TODO Auto-generated method stub

	}

	@Override
	public void putNumber(int paramInt, double paramDouble) throws JsonException {
		// TODO Auto-generated method stub

	}

	@Override
	public void putBoolean(int paramInt, boolean paramBoolean) throws JsonException {
		// TODO Auto-generated method stub

	}

	@Override
	public void putObject(int paramInt, JsonObject paramJsonObject) throws JsonException {
		// TODO Auto-generated method stub

	}

	@Override
	public void putArray(int paramInt, JsonArray paramJsonArray) throws JsonException {
		// TODO Auto-generated method stub

	}

	@Override
	public Object remove(int paramInt) throws JsonException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean add(Object paramObject) throws JsonException {
		// TODO Auto-generated method stub
		return false;
	}

}

package org.openntf.domino.graph2.impl;

import java.util.ArrayList;

public class DVertexList extends ArrayList<DVertex> {
	private static final long serialVersionUID = 1L;
	protected final DVertex sourceVertex_;

	public DVertexList(final DVertex source) {
		sourceVertex_ = source;
	}

	/*public DVertexList(final DVertex source, final TableService<DVertex> service) {
		super(service);
		sourceVertex_ = source;
	}*/

	/*@Override
	public DVertexList atomic() {
		return new DVertexList(sourceVertex_, new AtomicTableImpl<DVertex>(service()));
	}

	@Override
	public DVertexList unmodifiable() {
		return new DVertexList(sourceVertex_, new UnmodifiableTableImpl<DVertex>(service()));
	}

	@Override
	public <T extends Collection<DVertex>> Immutable<T> immutable() {
		return super.immutable();
	}*/

	public DVertexList applyFilter(final String key, final Object value) {
		DVertexList result = new DVertexList(sourceVertex_);
		if (this.size() > 0) {
			for (DVertex vertex : this) {
				if (value.equals(vertex.getProperty(key))) {
					result.add(vertex);
				}
			}
		}
		return result;
	}

}

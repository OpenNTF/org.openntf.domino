/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
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

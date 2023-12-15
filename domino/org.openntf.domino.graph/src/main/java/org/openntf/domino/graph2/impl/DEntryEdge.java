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

import java.util.LinkedHashMap;
import java.util.Map;

import org.openntf.domino.ViewEntry;
import org.openntf.domino.big.NoteCoordinate;
import org.openntf.domino.big.ViewEntryCoordinate;
import org.openntf.domino.exceptions.UnimplementedException;
import org.openntf.domino.graph2.DGraph;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

@SuppressWarnings("nls")
public class DEntryEdge extends DEdge {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private DElementStore store_;

	public DEntryEdge(final DGraph parent, final ViewEntry delegate, final ViewEntryCoordinate id, final DElementStore store) {
		super(parent, delegate);
		delegateKey_ = id;
		store_ = store;
	}

	public ViewEntry asEntry() {
		return (ViewEntry) getDelegate();
	}

	@Override
	public Object getVertexId(final Direction direction) {
		if (Direction.OUT.equals(direction)) {
			if (outKey_ == null) {
				try {
					ViewEntry entry = (org.openntf.domino.ViewEntry) getDelegate();
					if (entry.isDocument()) {
						String mid = entry.getDocument().getMetaversalID();
						setOutId(NoteCoordinate.Utils.getNoteCoordinate(mid));
					} else if (entry.isCategory()) {
						String entryid = delegateKey_.toString();
						String mid = "V" + entryid.substring(1);
						setOutId(ViewEntryCoordinate.Utils.getViewEntryCoordinate(mid));
					} else if (entry.isTotal()) {
						setOutId("TOTAL");
						//FIXME NTF Implement this please
					} else {
						Exception e = new UnimplementedException();
						e.printStackTrace();
						try {
							throw e;
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		} else if (Direction.IN.equals(direction)) {
			if (inKey_ == null) {
				//NTF only occurs when EntryEdge is manifested by direct request from ID rather than iteration of EdgeEntryList
				ViewEntryCoordinate vec = (ViewEntryCoordinate) delegateKey_;
				String mid = vec.getViewDocument().getMetaversalID();
				setInId(NoteCoordinate.Utils.getNoteCoordinate(mid));
			}
		}
		return super.getVertexId(direction);
	}

	@Override
	public Vertex getVertex(final Direction direction) throws IllegalArgumentException {
		ViewEntry entry = (org.openntf.domino.ViewEntry) getDelegate();
		if (Direction.OUT.equals(direction) && entry.isCategory()) {
			Map<String, Object> delegateMap = new LinkedHashMap<String, Object>();
			delegateMap.put("value", entry.getCategoryValue());
			delegateMap.put("position", entry.getPosition());
			delegateMap.put("noteid", entry.getNoteID());
			delegateMap.put("childcount", entry.getChildCount());
			DCategoryVertex result = new DCategoryVertex(getParent(), delegateMap, entry.getParentView());
			result.delegateKey_ = getVertexId(Direction.OUT);
			result.setView(entry.getParentView());
			return result;
		}
		return super.getVertex(direction);
	}

	//	public String getPriorCategories() {
	//		//TODO
	//		return null;
	//	}

	@Override
	public void applyChanges() {
		throw new UnsupportedOperationException("Entry edges cannot be updated. They are read-only.");
	}

	@Override
	public void commit() {
		throw new UnsupportedOperationException("Entry edges cannot be updated. They are read-only.");
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("Entry edges cannot be updated. They are read-only.");
	}

}

/*
 * © Copyright Tim Tripcony 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package org.openntf.domino.iterators;

import lotus.domino.NoteCollection;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.impl.Base;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class DocumentIterator extends AbstractDominoIterator<org.openntf.domino.Document> {
	private int index_ = 0;
	private int[] idArray_;
	private transient Document current_;

	public DocumentIterator(DocumentCollection collection) {
		super(collection);
		setIdArray(getCollectionIds(collection));
	}

	protected int[] getCollectionIds(DocumentCollection collection) {
		int[] result = null;
		if (collection != null) {
			NoteCollection nc = null;
			try {
				Database db = Factory.fromLotus(collection.getParent(), Database.class, collection.getParent());
				setDatabase(db);
				nc = org.openntf.domino.impl.DocumentCollection.toLotusNoteCollection(collection);
				if (nc.getCount() > 0) {
					result = nc.getNoteIDs();
				}
			} catch (Throwable t) {
				DominoUtils.handleException(t);
			} finally {
				Base.recycle(nc);
			}
		}
		return result;
	}

	protected int[] getIdArray() {
		return idArray_;
	}

	protected int getIndex() {
		return index_;
	}

	public boolean hasNext() {
		return !((getIndex() + 1) > getIdArray().length);
	}

	public Document next() {
		Document result = null;
		if (hasNext()) {
			String noteId = Integer.toHexString(getIdArray()[getIndex()]);
			setIndex(getIndex() + 1);
			Database db = getDatabase();
			try {
				Base.recycle(current_);
				lotus.domino.Document doc = db.getDocumentByID(noteId);
				result = Factory.fromLotus(doc, Document.class, getDatabase());
				current_ = result;
			} catch (Throwable t) {
				DominoUtils.handleException(t);
			}
		}
		return result;
	}

	public void remove() {
		// NOOP
	}

	protected void setIdArray(int[] idArray) {
		idArray_ = idArray;
	}

	protected void setIndex(int index) {
		index_ = index;
	}
}

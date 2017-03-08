/*
 * Copyright 2013
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
package org.openntf.domino.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import lotus.domino.NotesException;

import org.openntf.domino.Base;
import org.openntf.domino.Database;
import org.openntf.domino.DateTime;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.ext.Session.Fixes;
import org.openntf.domino.iterators.DocumentCollectionIterator;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.TypeUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class DocumentCollection.
 */
public class DocumentCollection extends BaseThreadSafe<org.openntf.domino.DocumentCollection, lotus.domino.DocumentCollection, Database>
		implements org.openntf.domino.DocumentCollection {

	/**
	 * The Class NthDocumentMethodNotPermittedException.
	 */
	static class NthDocumentMethodNotPermittedException extends RuntimeException {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		/**
		 * Instantiates a new nth document method not permitted exception.
		 */
		NthDocumentMethodNotPermittedException() {
			super("The OpenNTF Domino API does not permit the use of GetNthDocument methods in DocumentCollections");
		}
	}

	/**
	 * Instantiates a new outline.
	 *
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	protected DocumentCollection(final lotus.domino.DocumentCollection delegate, final Database parent) {
		super(delegate, parent, NOTES_DOCCOLL);
	}

	private org.openntf.domino.View parentView_;

	@Override
	public void setParentView(final View view) {
		parentView_ = view;
	}

	@Override
	public org.openntf.domino.View getParentView() {
		return parentView_;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#getCount()
	 */
	@Override
	public int getCount() {
		try {
			return getDelegate().getCount();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#getQuery()
	 */
	@Override
	public String getQuery() {
		try {
			return getDelegate().getQuery();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public final Database getParent() {
		return parent;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#getFirstDocument()
	 */
	@Override
	public org.openntf.domino.Document getFirstDocument() {
		try {
			return fromLotus(getDelegate().getFirstDocument(), Document.SCHEMA, getParentDatabase());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#getLastDocument()
	 */
	@Override
	public org.openntf.domino.Document getLastDocument() {
		try {
			return fromLotus(getDelegate().getLastDocument(), Document.SCHEMA, getParentDatabase());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#getNextDocument(lotus.domino.Document)
	 */
	@Override
	public org.openntf.domino.Document getNextDocument(final lotus.domino.Document doc) {
		try {
			return fromLotus(getDelegate().getNextDocument(toLotus(doc)), Document.SCHEMA, getParentDatabase());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#getPrevDocument(lotus.domino.Document)
	 */
	@Override
	public org.openntf.domino.Document getPrevDocument(final lotus.domino.Document doc) {
		try {
			return fromLotus(getDelegate().getPrevDocument(toLotus(doc)), Document.SCHEMA, getParentDatabase());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#getNthDocument(int)
	 */
	@Override
	public org.openntf.domino.Document getNthDocument(final int n) {
		if (getAncestorSession().isFixEnabled(Fixes.BLOCK_NTH_DOCUMENT)) {
			throw new NthDocumentMethodNotPermittedException();
		}
		try {
			return fromLotus(getDelegate().getNthDocument(n), Document.SCHEMA, getParentDatabase());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#getNextDocument()
	 */
	@Override
	public org.openntf.domino.Document getNextDocument() {
		try {
			return fromLotus(getDelegate().getNextDocument(), Document.SCHEMA, getParentDatabase());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#getPrevDocument()
	 */
	@Override
	public org.openntf.domino.Document getPrevDocument() {
		try {
			return fromLotus(getDelegate().getPrevDocument(), Document.SCHEMA, getParentDatabase());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#getDocument(lotus.domino.Document)
	 */
	@Override
	public org.openntf.domino.Document getDocument(final lotus.domino.Document doc) {
		try {
			return fromLotus(getDelegate().getDocument(toLotus(doc)), Document.SCHEMA, getParentDatabase());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#addDocument(lotus.domino.Document)
	 */
	@Override
	public void addDocument(final lotus.domino.Document doc) {
		try {
			getDelegate().addDocument(toLotus(doc));
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#addDocument(lotus.domino.Document, boolean)
	 */
	@Override
	public void addDocument(final lotus.domino.Document doc, final boolean checkDups) {
		try {
			getDelegate().addDocument(toLotus(doc), checkDups);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#deleteDocument(lotus.domino.Document)
	 */
	@Override
	public void deleteDocument(final lotus.domino.Document doc) {
		try {
			getDelegate().deleteDocument(toLotus(doc));
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#FTSearch(java.lang.String)
	 */
	@Override
	public void FTSearch(final String query) {
		try {
			getDelegate().FTSearch(query);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#FTSearch(java.lang.String, int)
	 */
	@Override
	public void FTSearch(final String query, final int maxDocs) {
		try {
			getDelegate().FTSearch(query, maxDocs);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	private Boolean sorted_ = null;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#isSorted()
	 */
	@Override
	public boolean isSorted() {
		try {
			if (sorted_ == null) {
				sorted_ = getDelegate().isSorted();
			}
			return sorted_;
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	void setSorted(final boolean sorted) {
		sorted_ = sorted;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#putAllInFolder(java.lang.String)
	 */
	@Override
	public void putAllInFolder(final String folderName) {
		try {
			getDelegate().putAllInFolder(folderName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#putAllInFolder(java.lang.String, boolean)
	 */
	@Override
	public void putAllInFolder(final String folderName, final boolean createOnFail) {
		try {
			getDelegate().putAllInFolder(folderName, createOnFail);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#removeAll(boolean)
	 */
	@Override
	public void removeAll(final boolean force) {
		try {
			getDelegate().removeAll(force);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#removeAllFromFolder(java.lang.String)
	 */
	@Override
	public void removeAllFromFolder(final String folderName) {
		try {
			getDelegate().removeAllFromFolder(folderName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#stampAll(java.lang.String, java.lang.Object)
	 */
	@Override
	public void stampAll(final String itemName, final Object value) {
		Collection<lotus.domino.Base> recycleThis = new ArrayList<lotus.domino.Base>();
		try {
			Object val = null;
			if (value instanceof lotus.domino.Item) {
				// Special support for items
				val = value;
			} else {
				val = TypeUtils.toItemFriendly(value, getAncestorSession(), recycleThis);
			}
			getDelegate().stampAll(itemName, val);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		} finally {
			s_recycle(recycleThis);
		}
	}

	@Override
	public void stampAll(final Map<String, Object> map) {
		Collection<lotus.domino.Base> recycleThis = new ArrayList<lotus.domino.Base>();
		try {
			//Map<String, Object> localMap = TypeUtils.toStampableMap(map, this);
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				Object val = null;
				if (entry.getValue() instanceof lotus.domino.Item) {
					// Special support for items
					val = entry.getValue();
				} else {
					val = TypeUtils.toItemFriendly(entry.getValue(), getAncestorSession(), recycleThis);
				}
				getDelegate().stampAll(entry.getKey(), val);
			}
		} catch (IllegalArgumentException iae) {
			for (org.openntf.domino.Document doc : this) {
				doc.putAll(map);
				doc.save();
			}
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		} finally {
			s_recycle(recycleThis);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#updateAll()
	 */
	@Override
	public void updateAll() {
		try {
			getDelegate().updateAll();
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#getUntilTime()
	 */
	@Override
	public DateTime getUntilTime() {
		try {
			return fromLotus(getDelegate().getUntilTime(), DateTime.SCHEMA, getAncestorSession());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#markAllRead(java.lang.String)
	 */
	@Override
	public void markAllRead(final String userName) {
		try {
			getDelegate().markAllRead(userName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#markAllUnread(java.lang.String)
	 */
	@Override
	public void markAllUnread(final String userName) {
		try {
			getDelegate().markAllUnread(userName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#markAllRead()
	 */
	@Override
	public void markAllRead() {
		try {
			getDelegate().markAllRead();
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#markAllUnread()
	 */
	@Override
	public void markAllUnread() {
		try {
			getDelegate().markAllUnread();
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#intersect(int)
	 */
	@Override
	public void intersect(final int noteId) {
		try {
			getDelegate().intersect(noteId);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#intersect(java.lang.String)
	 */
	@Override
	public void intersect(final String noteId) {
		try {
			getDelegate().intersect(noteId);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#intersect(lotus.domino.Base)
	 */
	@Override
	public void intersect(final lotus.domino.Base documents) {
		try {
			getDelegate().intersect(toLotus(documents));
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#merge(int)
	 */
	@Override
	public void merge(final int noteId) {
		try {
			getDelegate().merge(noteId);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#merge(java.lang.String)
	 */
	@Override
	public void merge(final String noteId) {
		try {
			getDelegate().merge(noteId);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#merge(lotus.domino.Base)
	 */
	@Override
	public void merge(final lotus.domino.Base documents) {
		try {
			getDelegate().merge(toLotus(documents));
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#subtract(int)
	 */
	@Override
	public void subtract(final int noteId) {
		try {
			getDelegate().subtract(noteId);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#subtract(java.lang.String)
	 */
	@Override
	public void subtract(final String noteId) {
		try {
			getDelegate().subtract(noteId);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#subtract(lotus.domino.Base)
	 */
	@Override
	public void subtract(final lotus.domino.Base documents) {
		try {
			getDelegate().subtract(toLotus(documents));
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#contains(int)
	 */
	@Override
	public boolean contains(final int noteId) {
		try {
			return getDelegate().contains(noteId);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#contains(java.lang.String)
	 */
	@Override
	public boolean contains(final String noteId) {
		try {
			return getDelegate().contains(noteId);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#contains(lotus.domino.Base)
	 */
	@Override
	public boolean contains(final lotus.domino.Base documents) {
		try {
			return getDelegate().contains(toLotus(documents));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DocumentCollection#cloneCollection()
	 */
	@Override
	public org.openntf.domino.DocumentCollection cloneCollection() {
		try {
			return fromLotus(getDelegate().cloneCollection(), DocumentCollection.SCHEMA, getAncestorDatabase());
		} catch (Throwable t) {
			DominoUtils.handleException(t);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<org.openntf.domino.Document> iterator() {
		//return new DocumentIterator(this);
		return new DocumentCollectionIterator(this);
	}

	public final Database getParentDatabase() {
		return parent;
	}

	@Override
	public boolean add(final org.openntf.domino.Document doc) {
		this.addDocument(doc);
		return true;
	}

	@Override
	public boolean addAll(final Collection<? extends org.openntf.domino.Document> docs) {
		if (docs instanceof Base) {
			this.merge((Base<?>) docs);
		} else {
			for (org.openntf.domino.Document doc : docs) {
				this.addDocument(doc);
			}
		}
		return true;
	}

	@Override
	public void clear() {
		org.openntf.domino.Document iconNote = this.getParentDatabase().getDocumentByID("FFFF0010");
		this.intersect(iconNote);
		this.remove(iconNote);
	}

	@Override
	public boolean contains(final Object value) {
		if (value instanceof Integer) {
			return this.contains(((Integer) value).intValue());
		} else if (value instanceof lotus.domino.Document) {
			return this.contains((lotus.domino.Document) value);
		} else if (value instanceof lotus.domino.DocumentCollection) {
			return this.contains((lotus.domino.DocumentCollection) value);
		}
		return false;
	}

	@Override
	public boolean containsAll(final Collection<?> docs) {
		if (docs == null) {
			return false;
		}
		for (Object docObj : docs) {
			if (!this.contains(docObj)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isEmpty() {
		return this.size() > 0;
	}

	@Override
	public boolean remove(final Object doc) {
		if (doc instanceof lotus.domino.Document) {
			this.deleteDocument((lotus.domino.Document) doc);
			return true;
		}
		return false;
	}

	@Override
	public boolean removeAll(final Collection<?> docs) {
		if (docs == null) {
			return false;
		}
		boolean changed = false;
		for (Object docObj : docs) {
			if (this.remove(docObj)) {
				changed = true;
			}
		}
		return changed;
	}

	@Override
	public boolean retainAll(final Collection<?> docs) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		return this.getCount();
	}

	@Override
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T[] toArray(final T[] arg0) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.types.DatabaseDescendant#getAncestorDatabase()
	 */
	@Override
	public final Database getAncestorDatabase() {
		return this.getParentDatabase();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public final Session getAncestorSession() {
		return parent.getAncestorSession();
	}

	@Override
	public org.openntf.domino.DocumentCollection filter(final Object value) {
		org.openntf.domino.DocumentCollection result = getAncestorDatabase().createDocumentCollection();
		for (org.openntf.domino.Document doc : this) {
			if (doc.containsValue(value)) {
				result.add(doc);
			}
		}
		return result;
	}

	@Override
	public org.openntf.domino.DocumentCollection filter(final Object value, final String[] itemnames) {
		org.openntf.domino.DocumentCollection result = getAncestorDatabase().createDocumentCollection();
		for (org.openntf.domino.Document doc : this) {
			if (doc.containsValue(value, itemnames)) {
				result.add(doc);
			}
		}
		return result;
	}

	@Override
	public org.openntf.domino.DocumentCollection filter(final Object value, final Collection<String> itemnames) {
		org.openntf.domino.DocumentCollection result = getAncestorDatabase().createDocumentCollection();
		for (org.openntf.domino.Document doc : this) {
			if (doc.containsValue(value, itemnames)) {
				result.add(doc);
			}
		}
		return result;
	}

	@Override
	public org.openntf.domino.DocumentCollection filter(final Map<String, Object> filterMap) {
		org.openntf.domino.DocumentCollection result = getAncestorDatabase().createDocumentCollection();
		for (org.openntf.domino.Document doc : this) {
			if (doc.containsValues(filterMap)) {
				result.add(doc);
			}
		}
		return result;
	}

	@Override
	protected WrapperFactory getFactory() {
		return parent.getAncestorSession().getFactory();
	}

}

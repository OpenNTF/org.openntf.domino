package org.openntf.domino.iterators;

import java.util.Iterator;

import org.openntf.domino.Document;

public class DocumentCollectionSimpleIterator implements Iterator<org.openntf.domino.Document> {

	private final org.openntf.domino.impl.DocumentCollection _dc;
	private Document _next = null;

	public DocumentCollectionSimpleIterator(final org.openntf.domino.impl.DocumentCollection documentCollection) {
		this._dc = documentCollection;
		this._next = (null == this._dc) ? null : this._dc.getFirstDocument();
	}

	@Override
	public boolean hasNext() {
		return (null != this._next);
	}

	@Override
	public Document next() {
		Document result = this._next;
		if (null != result) {
			this._next = this._dc.getNextDocument(result);
		}

		return result;
	}

	@Override
	public void remove() {
		if (null != this._next) {
			this._dc.deleteDocument(this._next);
		}
	}
}
package org.openntf.domino.iterators;

import java.util.Iterator;

import lotus.domino.NotesException;

import org.openntf.domino.Document;
import org.openntf.domino.impl.Base;
import org.openntf.domino.utils.DominoUtils;

public class DocumentCollectionIterator implements Iterator<org.openntf.domino.Document> {

	/**
	 * 
	 */
	private final org.openntf.domino.impl.DocumentCollection documentCollection_;
	// hold docs unwrapperd
	//private lotus.domino.Document currLotusDoc = null;
	//private lotus.domino.Document nextLotusDoc;
	private org.openntf.domino.Document currWrapper = null;
	private org.openntf.domino.Document nextWrapper;
	private int errCount_ = 0;

	public DocumentCollectionIterator(final org.openntf.domino.impl.DocumentCollection documentCollection) {
		documentCollection_ = documentCollection;
		try {
			lotus.domino.Document nextLotusDoc = ((lotus.domino.DocumentCollection) org.openntf.domino.impl.Base
					.getDelegate(documentCollection_)).getFirstDocument(); // needs no recycle
			// because it is wrapped here (we must do this here otherwise it won't get recycled;
			nextWrapper = documentCollection_.fromLotus(nextLotusDoc, Document.SCHEMA, documentCollection_.getParent());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public boolean hasNext() {
		return nextWrapper != null; // something in Queue?
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	@Override
	public org.openntf.domino.Document next() {
		//		try {
		//currLotusDoc = nextLotusDoc;
		//			if (currWrapper != null) {
		//				System.out.println("CollIsDead:" + documentCollection_.isDead());
		//				System.out.println("CurrIsDead:" + currWrapper.isDead());
		//				System.out.println("NextIsDead:" + nextWrapper.isDead());
		//				System.out.println("CurrUNID:  " + currWrapper.getUniversalID());
		//				System.out.println("NextUNID:  " + nextWrapper.getUniversalID());
		//			}
		lotus.domino.Document nextLotusDoc = null;
		try {
			// this is very tricky, iterate from the 1st to the 2nd
			lotus.domino.DocumentCollection lColl = (lotus.domino.DocumentCollection) org.openntf.domino.impl.Base
					.getDelegate(documentCollection_);
			if (nextWrapper == null || nextWrapper.isDead()) {
				if (nextWrapper == null) {
					System.out.println("ALERT: Wrapped version of next document is NULL");
				} else if (nextWrapper.isDead()) {
					System.out.println("ALERT: Wrapped version of next document is dead");
				} else {
					System.out.println("ALERT: It should have been impossible to arrive here");
					throw new RuntimeException();
				}
			} else {
				lotus.domino.Document lNext = Base.toLotus(nextWrapper);
				lotus.domino.Document lDoc = lColl.getNextDocument(lNext);
				if (lDoc != null) {
					nextLotusDoc = lNext;
				}
			}
		} catch (NotesException ne) {
			errCount_++;
			if (ne.text.contains("not from this collection")) {
				if (errCount_ > 10) {
					currWrapper = null;
					nextWrapper = null;
					return null;
				} else {
					DominoUtils.handleException(ne);
				}
			} else {
				DominoUtils.handleException(ne);
			}
			return null;
		}
		currWrapper = nextWrapper;
		if (nextLotusDoc != null) {
			nextWrapper = documentCollection_.fromLotus(nextLotusDoc, Document.SCHEMA, documentCollection_.getParent()); // and update the wrapper here
		} else {
			nextWrapper = null;
		}
		return currWrapper;											// return the wrapper that wrapped the 1st
		//		} catch (NotesException e) {
		//			DominoUtils.handleException(e);
		//			return null;
		//		}
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	@Override
	public void remove() {
		// TODO Auto-generated method stub
		try {
			((lotus.domino.DocumentCollection) org.openntf.domino.impl.Base.getDelegate(documentCollection_)).deleteDocument(Base
					.toLotus(currWrapper)); // delete from coll! not from disk
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

}
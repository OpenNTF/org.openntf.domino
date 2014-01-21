package org.openntf.domino.impl;

import java.util.Iterator;

import lotus.domino.NotesException;

import org.openntf.domino.Document;
import org.openntf.domino.utils.DominoUtils;

public class DocumentCollectionIterator implements Iterator<org.openntf.domino.Document> {

	/**
	 * 
	 */
	private final DocumentCollection documentCollection_;
	// hold docs unwrapperd
	//private lotus.domino.Document currLotusDoc = null;
	//private lotus.domino.Document nextLotusDoc;
	private org.openntf.domino.Document currWrapper = null;
	private org.openntf.domino.Document nextWrapper;

	DocumentCollectionIterator(final DocumentCollection documentCollection) {
		documentCollection_ = documentCollection;
		try {
			lotus.domino.Document nextLotusDoc = documentCollection_.getDelegate().getFirstDocument(); // needs no recycle
			// because it is wrapped here (we must do this here otherwise it won't get recycled;
			nextWrapper = documentCollection_.fromLotus(nextLotusDoc, Document.SCHEMA, documentCollection_.getParent());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	public boolean hasNext() {
		return nextWrapper != null; // something in Queue?
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	public org.openntf.domino.Document next() {
		try {
			//currLotusDoc = nextLotusDoc;
			//			if (currWrapper != null) {
			//				System.out.println("CollIsDead:" + documentCollection_.isDead());
			//				System.out.println("CurrIsDead:" + currWrapper.isDead());
			//				System.out.println("NextIsDead:" + nextWrapper.isDead());
			//				System.out.println("CurrUNID:  " + currWrapper.getUniversalID());
			//				System.out.println("NextUNID:  " + nextWrapper.getUniversalID());
			//			}
			lotus.domino.Document nextLotusDoc = documentCollection_.getDelegate().getNextDocument(Base.toLotus(nextWrapper)); // this is very tricky, iterate from the 1st to the 2nd

			currWrapper = nextWrapper;
			nextWrapper = documentCollection_.fromLotus(nextLotusDoc, Document.SCHEMA, documentCollection_.getParent()); // and update the wrapper here
			return currWrapper;											// return the wrapper that wrapped the 1st
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		// TODO Auto-generated method stub
		try {
			documentCollection_.getDelegate().deleteDocument(Base.toLotus(currWrapper)); // delete from coll! not from disk
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

}
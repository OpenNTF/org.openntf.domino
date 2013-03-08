package org.openntf.domino.impl;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesException;

import org.openntf.domino.DateTime;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class DocumentCollection extends Base<org.openntf.domino.DocumentCollection, lotus.domino.DocumentCollection> implements
		org.openntf.domino.DocumentCollection {

	private static boolean BLOCK_NTH = true; // TODO replace with some static determination from a policy or permissions rule or
												// something...

	static class NthDocumentMethodNotPermittedException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		NthDocumentMethodNotPermittedException() {
			super("The OpenNTF Domino API does not permit the use of GetNthDocument methods in DocumentCollections");
		}
	}

	public DocumentCollection(lotus.domino.DocumentCollection delegate) {
		super(delegate);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCount() {
		try {
			return getDelegate().getCount();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	@Override
	public String getQuery() {
		try {
			return getDelegate().getQuery();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public Database getParent() {
		try {
			Database result = getDelegate().getParent();
			if (!(result instanceof org.openntf.domino.impl.Database)) {
				result = new org.openntf.domino.impl.Database(result);
			}
			return result;
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public org.openntf.domino.Document getFirstDocument() {
		try {
			return Factory.fromLotus(getDelegate().getFirstDocument(), org.openntf.domino.Document.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public org.openntf.domino.Document getLastDocument() {
		try {
			return Factory.fromLotus(getDelegate().getLastDocument(), org.openntf.domino.Document.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public org.openntf.domino.Document getNextDocument(Document doc) {
		try {
			if (doc instanceof org.openntf.domino.impl.Document) {
				doc = ((org.openntf.domino.impl.Document) doc).getDelegate();
			}
			return Factory.fromLotus(getDelegate().getNextDocument(doc), org.openntf.domino.Document.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public org.openntf.domino.Document getPrevDocument(Document doc) {
		try {
			if (doc instanceof org.openntf.domino.impl.Document) {
				doc = ((org.openntf.domino.impl.Document) doc).getDelegate();
			}
			return Factory.fromLotus(getDelegate().getPrevDocument(doc), org.openntf.domino.Document.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public org.openntf.domino.Document getNthDocument(int n) {
		if (BLOCK_NTH) {
			throw new NthDocumentMethodNotPermittedException();
		}
		try {
			return Factory.fromLotus(getDelegate().getNthDocument(n), org.openntf.domino.Document.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public org.openntf.domino.Document getNextDocument() {
		try {
			return Factory.fromLotus(getDelegate().getNextDocument(), org.openntf.domino.Document.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public org.openntf.domino.Document getPrevDocument() {
		try {
			return Factory.fromLotus(getDelegate().getPrevDocument(), org.openntf.domino.Document.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public org.openntf.domino.Document getDocument(Document doc) {
		try {
			if (doc instanceof org.openntf.domino.impl.Document) {
				doc = ((org.openntf.domino.impl.Document) doc).getDelegate();
			}
			return Factory.fromLotus(getDelegate().getDocument(doc), org.openntf.domino.Document.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public void addDocument(Document doc) {
		try {
			getDelegate().addDocument(doc);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void addDocument(Document doc, boolean checkDups) {
		try {
			getDelegate().addDocument(doc, checkDups);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void deleteDocument(Document doc) {
		try {
			getDelegate().deleteDocument(doc);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void FTSearch(String query) {
		try {
			getDelegate().FTSearch(query);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void FTSearch(String query, int maxDocs) {
		try {
			getDelegate().FTSearch(query, maxDocs);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public boolean isSorted() {
		try {
			return getDelegate().isSorted();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	@Override
	public void putAllInFolder(String folderName) {
		try {
			getDelegate().putAllInFolder(folderName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void putAllInFolder(String folderName, boolean createOnFail) {
		try {
			getDelegate().putAllInFolder(folderName, createOnFail);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void removeAll(boolean force) {
		try {
			getDelegate().removeAll(force);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void removeAllFromFolder(String folderName) {
		try {
			getDelegate().removeAllFromFolder(folderName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void stampAll(String itemName, Object value) {
		try {
			getDelegate().stampAll(itemName, value);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void updateAll() {
		try {
			getDelegate().updateAll();
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public org.openntf.domino.DateTime getUntilTime() {
		try {
			return Factory.fromLotus(getDelegate().getUntilTime(), DateTime.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public void markAllRead(String userName) {
		try {
			getDelegate().markAllRead(userName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void markAllUnread(String userName) {
		try {
			getDelegate().markAllUnread(userName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void markAllRead() {
		try {
			getDelegate().markAllRead();
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void markAllUnread() {
		try {
			getDelegate().markAllUnread();
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void intersect(int noteID) {
		try {
			getDelegate().intersect(noteID);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void intersect(String noteID) {
		try {
			getDelegate().intersect(noteID);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void intersect(lotus.domino.Base documents) {
		try {
			getDelegate().intersect(documents);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void merge(int noteID) {
		try {
			getDelegate().merge(noteID);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void merge(String noteID) {
		try {
			getDelegate().merge(noteID);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void merge(lotus.domino.Base documents) {
		try {
			getDelegate().merge(documents);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void subtract(int noteID) {
		try {
			getDelegate().subtract(noteID);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void subtract(String noteID) {
		try {
			getDelegate().subtract(noteID);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void subtract(lotus.domino.Base documents) {
		try {
			getDelegate().subtract(documents);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public boolean contains(int noteID) {
		try {
			return getDelegate().contains(noteID);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	@Override
	public boolean contains(String noteID) {
		try {
			return getDelegate().contains(noteID);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	@Override
	public boolean contains(lotus.domino.Base documents) {
		try {
			return getDelegate().contains(documents);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	@Override
	public org.openntf.domino.DocumentCollection cloneCollection() {
		try {
			org.openntf.domino.Database parent = (org.openntf.domino.Database) getParent();
			lotus.domino.DocumentCollection emptyCollection = parent.createDocumentCollection();
			org.openntf.domino.DocumentCollection result = new org.openntf.domino.impl.DocumentCollection(emptyCollection);
			result.merge(this);
			return result;
		} catch (Throwable t) {
			DominoUtils.handleException(t);
			return null;

		}
	}

}

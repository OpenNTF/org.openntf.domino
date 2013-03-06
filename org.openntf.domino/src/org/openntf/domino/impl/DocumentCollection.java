package org.openntf.domino.impl;

import lotus.domino.Database;
import lotus.domino.DateTime;
import lotus.domino.Document;
import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;

public class DocumentCollection extends Base<org.openntf.domino.DocumentCollection, lotus.domino.DocumentCollection> implements
		org.openntf.domino.DocumentCollection {

	public DocumentCollection() {
		// TODO Auto-generated constructor stub
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
			Database parent = getDelegate().getParent();
			return new org.openntf.domino.impl.Database(parent);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public Document getFirstDocument() {
		try {
			return getDelegate().getFirstDocument();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public Document getLastDocument() {
		try {
			return getDelegate().getLastDocument();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public Document getNextDocument(Document paramDocument) {
		try {
			return getDelegate().getNextDocument(paramDocument);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public Document getPrevDocument(Document paramDocument) {
		try {
			return getDelegate().getPrevDocument(paramDocument);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public Document getNthDocument(int paramInt) {
		try {
			return getDelegate().getNthDocument(paramInt);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public Document getNextDocument() {
		try {
			return getDelegate().getNextDocument();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public Document getPrevDocument() {
		try {
			return getDelegate().getPrevDocument();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public Document getDocument(Document paramDocument) {
		try {
			return getDelegate().getDocument(paramDocument);
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
	public void deleteDocument(Document paramDocument) {
		try {
			getDelegate().addDocument(paramDocument);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void FTSearch(String paramString) {
		try {
			getDelegate().FTSearch(paramString);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void FTSearch(String paramString, int paramInt) {
		try {
			getDelegate().FTSearch(paramString, paramInt);
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
	public void putAllInFolder(String paramString) {
		try {
			getDelegate().putAllInFolder(paramString);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void putAllInFolder(String paramString, boolean paramBoolean) {
		try {
			getDelegate().putAllInFolder(paramString);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void removeAll(boolean paramBoolean) {
		try {
			getDelegate().removeAll(paramBoolean);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void removeAllFromFolder(String paramString) {
		try {
			getDelegate().removeAllFromFolder(paramString);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void stampAll(String paramString, Object paramObject) {
		try {
			getDelegate().stampAll(paramString, paramObject);
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
	public DateTime getUntilTime() {
		try {
			return getDelegate().getUntilTime();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public void markAllRead(String paramString) {
		try {
			getDelegate().markAllRead(paramString);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void markAllUnread(String paramString) {
		try {
			getDelegate().markAllUnread(paramString);
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
	public void intersect(int paramInt) {
		try {
			getDelegate().intersect(paramInt);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void intersect(String paramString) {
		try {
			getDelegate().intersect(paramString);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void intersect(lotus.domino.Base paramBase) {
		try {
			getDelegate().intersect(paramBase);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void merge(int paramInt) {
		try {
			getDelegate().merge(paramInt);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void merge(String paramString) {
		try {
			getDelegate().merge(paramString);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void merge(lotus.domino.Base paramBase) {
		try {
			getDelegate().merge(paramBase);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void subtract(int paramInt) {
		try {
			getDelegate().subtract(paramInt);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void subtract(String paramString) {
		try {
			getDelegate().subtract(paramString);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void subtract(lotus.domino.Base paramBase) {
		try {
			getDelegate().subtract(paramBase);
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
	public boolean contains(lotus.domino.Base paramBase) {
		try {
			return getDelegate().contains(paramBase);
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
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}
}

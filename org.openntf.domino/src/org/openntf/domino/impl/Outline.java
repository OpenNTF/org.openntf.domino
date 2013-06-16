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

import lotus.domino.NotesException;

import org.openntf.domino.Session;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class Outline.
 */
public class Outline extends Base<org.openntf.domino.Outline, lotus.domino.Outline> implements org.openntf.domino.Outline {

	/**
	 * Instantiates a new outline.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	public Outline(final lotus.domino.Outline delegate, final org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Outline#addEntry(lotus.domino.OutlineEntry, lotus.domino.OutlineEntry)
	 */
	@Override
	public void addEntry(final lotus.domino.OutlineEntry entry, final lotus.domino.OutlineEntry referenceEntry) {
		try {
			getDelegate().addEntry((lotus.domino.OutlineEntry) toLotus(entry), (lotus.domino.OutlineEntry) toLotus(referenceEntry));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Outline#addEntry(lotus.domino.OutlineEntry, lotus.domino.OutlineEntry, boolean)
	 */
	@Override
	public void addEntry(final lotus.domino.OutlineEntry entry, final lotus.domino.OutlineEntry referenceEntry, final boolean after) {
		try {
			getDelegate().addEntry((lotus.domino.OutlineEntry) toLotus(entry), (lotus.domino.OutlineEntry) toLotus(referenceEntry), after);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Outline#addEntry(lotus.domino.OutlineEntry, lotus.domino.OutlineEntry, boolean, boolean)
	 */
	@Override
	public void addEntry(final lotus.domino.OutlineEntry entry, final lotus.domino.OutlineEntry referenceEntry, final boolean after, final boolean asChild) {
		try {
			getDelegate().addEntry((lotus.domino.OutlineEntry) toLotus(entry), (lotus.domino.OutlineEntry) toLotus(referenceEntry), after,
					asChild);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Outline#createEntry(lotus.domino.OutlineEntry)
	 */
	@Override
	public OutlineEntry createEntry(final lotus.domino.OutlineEntry fromEntry) {
		try {
			return Factory.fromLotus(getDelegate().createEntry((lotus.domino.OutlineEntry) toLotus(fromEntry)), OutlineEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Outline#createEntry(lotus.domino.OutlineEntry, lotus.domino.OutlineEntry)
	 */
	@Override
	public OutlineEntry createEntry(final lotus.domino.OutlineEntry fromEntry, final lotus.domino.OutlineEntry referenceEntry) {
		try {
			return Factory.fromLotus(getDelegate().createEntry((lotus.domino.OutlineEntry) toLotus(fromEntry),
					(lotus.domino.OutlineEntry) toLotus(referenceEntry)), OutlineEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Outline#createEntry(lotus.domino.OutlineEntry, lotus.domino.OutlineEntry, boolean)
	 */
	@Override
	public OutlineEntry createEntry(final lotus.domino.OutlineEntry fromEntry, final lotus.domino.OutlineEntry referenceEntry, final boolean after) {
		try {
			return Factory.fromLotus(getDelegate().createEntry((lotus.domino.OutlineEntry) toLotus(fromEntry),
					(lotus.domino.OutlineEntry) toLotus(referenceEntry), after), OutlineEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Outline#createEntry(lotus.domino.OutlineEntry, lotus.domino.OutlineEntry, boolean, boolean)
	 */
	@Override
	public OutlineEntry createEntry(final lotus.domino.OutlineEntry fromEntry, final lotus.domino.OutlineEntry referenceEntry, final boolean after,
			final boolean asChild) {
		try {
			return Factory.fromLotus(getDelegate().createEntry((lotus.domino.OutlineEntry) toLotus(fromEntry),
					(lotus.domino.OutlineEntry) toLotus(referenceEntry), after, asChild), OutlineEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Outline#createEntry(java.lang.String)
	 */
	@Override
	public OutlineEntry createEntry(final String entryName) {
		try {
			return Factory.fromLotus(getDelegate().createEntry(entryName), OutlineEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Outline#createEntry(java.lang.String, lotus.domino.OutlineEntry)
	 */
	@Override
	public OutlineEntry createEntry(final String entryName, final lotus.domino.OutlineEntry referenceEntry) {
		try {
			return Factory.fromLotus(getDelegate().createEntry(entryName, (lotus.domino.OutlineEntry) toLotus(referenceEntry)),
					OutlineEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Outline#createEntry(java.lang.String, lotus.domino.OutlineEntry, boolean)
	 */
	@Override
	public OutlineEntry createEntry(final String entryName, final lotus.domino.OutlineEntry referenceEntry, final boolean after) {
		try {
			return Factory.fromLotus(getDelegate().createEntry(entryName, (lotus.domino.OutlineEntry) toLotus(referenceEntry), after),
					OutlineEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Outline#createEntry(java.lang.String, lotus.domino.OutlineEntry, boolean, boolean)
	 */
	@Override
	public OutlineEntry createEntry(final String entryName, final lotus.domino.OutlineEntry referenceEntry, final boolean after, final boolean asChild) {
		try {
			return Factory.fromLotus(getDelegate().createEntry(entryName, (lotus.domino.OutlineEntry) toLotus(referenceEntry), after,
					asChild), OutlineEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Outline#getAlias()
	 */
	@Override
	public String getAlias() {
		try {
			return getDelegate().getAlias();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Outline#getChild(lotus.domino.OutlineEntry)
	 */
	@Override
	public OutlineEntry getChild(final lotus.domino.OutlineEntry entry) {
		try {
			return Factory.fromLotus(getDelegate().getChild((lotus.domino.OutlineEntry) toLotus(entry)), OutlineEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Outline#getComment()
	 */
	@Override
	public String getComment() {
		try {
			return getDelegate().getComment();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.types.Design#getDocument()
	 */
	@Override
	public Document getDocument() {
		return this.getParentDatabase().getDocumentByUNID(this.getUniversalID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Outline#getFirst()
	 */
	@Override
	public OutlineEntry getFirst() {
		try {
			return Factory.fromLotus(getDelegate().getFirst(), OutlineEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Outline#getLast()
	 */
	@Override
	public OutlineEntry getLast() {
		try {
			return Factory.fromLotus(getDelegate().getLast(), OutlineEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Outline#getName()
	 */
	@Override
	public String getName() {
		try {
			return getDelegate().getName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Outline#getNext(lotus.domino.OutlineEntry)
	 */
	@Override
	public OutlineEntry getNext(final lotus.domino.OutlineEntry entry) {
		try {
			return Factory.fromLotus(getDelegate().getNext((lotus.domino.OutlineEntry) toLotus(entry)), OutlineEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Outline#getNextSibling(lotus.domino.OutlineEntry)
	 */
	@Override
	public OutlineEntry getNextSibling(final lotus.domino.OutlineEntry entry) {
		try {
			return Factory.fromLotus(getDelegate().getNextSibling((lotus.domino.OutlineEntry) toLotus(entry)), OutlineEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.types.Design#getNoteID()
	 */
	@Override
	public String getNoteID() {
		NoteCollection notes = this.getParentDatabase().createNoteCollection(false);
		notes.setSelectOutlines(true);
		notes.setSelectionFormula("$TITLE=\"" + this.getName().replace("\"", "\\\"") + "\"");
		notes.buildCollection();
		return notes.getFirstNoteID();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Outline#getParent(lotus.domino.OutlineEntry)
	 */
	@Override
	public OutlineEntry getParent(final lotus.domino.OutlineEntry entry) {
		try {
			return Factory.fromLotus(getDelegate().getParent((lotus.domino.OutlineEntry) toLotus(entry)), OutlineEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Outline#getParentDatabase()
	 */
	@Override
	public Database getParentDatabase() {
		return (Database) super.getParent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Outline#getPrev(lotus.domino.OutlineEntry)
	 */
	@Override
	public OutlineEntry getPrev(final lotus.domino.OutlineEntry entry) {
		try {
			return Factory.fromLotus(getDelegate().getPrev((lotus.domino.OutlineEntry) toLotus(entry)), OutlineEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Outline#getPrevSibling(lotus.domino.OutlineEntry)
	 */
	@Override
	public OutlineEntry getPrevSibling(final lotus.domino.OutlineEntry entry) {
		try {
			return Factory.fromLotus(getDelegate().getPrevSibling((lotus.domino.OutlineEntry) toLotus(entry)), OutlineEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.types.Design#getUniversalID()
	 */
	@Override
	public String getUniversalID() {
		NoteCollection notes = this.getParentDatabase().createNoteCollection(false);
		notes.setSelectOutlines(true);
		notes.setSelectionFormula("$TITLE=\"" + this.getName().replace("\"", "\\\"") + "\"");
		notes.buildCollection();
		return notes.getUNID(notes.getFirstNoteID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Outline#moveEntry(lotus.domino.OutlineEntry, lotus.domino.OutlineEntry)
	 */
	@Override
	public void moveEntry(final lotus.domino.OutlineEntry entry, final lotus.domino.OutlineEntry referenceEntry) {
		try {
			getDelegate().moveEntry((lotus.domino.OutlineEntry) toLotus(entry), (lotus.domino.OutlineEntry) toLotus(referenceEntry));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Outline#moveEntry(lotus.domino.OutlineEntry, lotus.domino.OutlineEntry, boolean)
	 */
	@Override
	public void moveEntry(final lotus.domino.OutlineEntry entry, final lotus.domino.OutlineEntry referenceEntry, final boolean after) {
		try {
			getDelegate().moveEntry((lotus.domino.OutlineEntry) toLotus(entry), (lotus.domino.OutlineEntry) toLotus(referenceEntry), after);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Outline#moveEntry(lotus.domino.OutlineEntry, lotus.domino.OutlineEntry, boolean, boolean)
	 */
	@Override
	public void moveEntry(final lotus.domino.OutlineEntry entry, final lotus.domino.OutlineEntry referenceEntry, final boolean after, final boolean asChild) {
		try {
			getDelegate().moveEntry((lotus.domino.OutlineEntry) toLotus(entry), (lotus.domino.OutlineEntry) toLotus(referenceEntry), after,
					asChild);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Outline#removeEntry(lotus.domino.OutlineEntry)
	 */
	@Override
	public void removeEntry(final lotus.domino.OutlineEntry entry) {
		try {
			getDelegate().removeEntry((lotus.domino.OutlineEntry) toLotus(entry));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Outline#save()
	 */
	@Override
	public int save() {
		try {
			return getDelegate().save();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Outline#setAlias(java.lang.String)
	 */
	@Override
	public void setAlias(final String alias) {
		try {
			getDelegate().setAlias(alias);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Outline#setComment(java.lang.String)
	 */
	@Override
	public void setComment(final String comment) {
		try {
			getDelegate().setComment(comment);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Outline#setName(java.lang.String)
	 */
	@Override
	public void setName(final String name) {
		try {
			getDelegate().setName(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.DatabaseDescendant#getAncestorDatabase()
	 */
	@Override
	public Database getAncestorDatabase() {
		return this.getParentDatabase();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public Session getAncestorSession() {
		return this.getAncestorDatabase().getAncestorSession();
	}
}

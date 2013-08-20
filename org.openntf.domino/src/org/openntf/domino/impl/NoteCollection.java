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

import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.Database;
import org.openntf.domino.DateTime;
import org.openntf.domino.Session;
import org.openntf.domino.iterators.NoteIterator;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class NoteCollection.
 */
/**
 * @author withersp
 * 
 */
public class NoteCollection extends org.openntf.domino.impl.Base<org.openntf.domino.NoteCollection, lotus.domino.NoteCollection> implements
		org.openntf.domino.NoteCollection {

	/**
	 * To lotus document collection.
	 * 
	 * @param collection
	 *            the collection
	 * @return the org.openntf.domino. document collection
	 */
	public static org.openntf.domino.DocumentCollection toLotusDocumentCollection(final org.openntf.domino.NoteCollection collection) {
		// TODO NTF - this could be more heavily optimized with a .getNoteIds(). Feel free to replace it.
		org.openntf.domino.DocumentCollection result = null;
		if (collection instanceof org.openntf.domino.impl.NoteCollection) {
			org.openntf.domino.Database db = ((org.openntf.domino.impl.NoteCollection) collection).getParent();
			result = db.createDocumentCollection();
			if (collection.getCount() > 0) {
				String nid = collection.getFirstNoteID();
				while (nid != null) {
					result.addDocument(db.getDocumentByID(nid));
					nid = collection.getNextNoteID(nid);
				}
			}
		} else if (collection instanceof lotus.domino.NoteCollection) {
			org.openntf.domino.Database db = (org.openntf.domino.Database) collection.getParent();
			org.openntf.domino.DocumentCollection coll = db.createDocumentCollection();
			if (collection.getCount() > 0) {
				String nid = collection.getFirstNoteID();
				while (nid != null) {
					coll.addDocument(db.getDocumentByID(nid));
					nid = collection.getNextNoteID(nid);
				}
			}
			result = coll;
		}
		return result;
	}

	/**
	 * Instantiates a new note collection.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	public NoteCollection(final lotus.domino.NoteCollection delegate, final org.openntf.domino.Base<?> parent) {
		super(delegate, Factory.getParentDatabase(parent));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#add(int)
	 */
	@Override
	public void add(final int additionSpecifier) {
		try {
			getDelegate().add(additionSpecifier);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#add(int[])
	 */
	@Override
	public void add(final int[] additionSpecifier) {
		try {
			getDelegate().add(additionSpecifier);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#add(lotus.domino.Agent)
	 */
	@Override
	public void add(final lotus.domino.Agent additionSpecifier) {
		try {
			getDelegate().add((lotus.domino.Agent) toLotus(additionSpecifier));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#add(lotus.domino.Document)
	 */
	@Override
	public void add(final lotus.domino.Document additionSpecifier) {
		try {
			getDelegate().add((lotus.domino.Document) toLotus(additionSpecifier));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#add(lotus.domino.DocumentCollection)
	 */
	@Override
	public void add(final lotus.domino.DocumentCollection additionSpecifier) {
		try {
			// TODO Figure out why the normal add() line with the DC throws a NotesException("Invalid object type for method argument")
			lotus.domino.DocumentCollection adder = (lotus.domino.DocumentCollection) toLotus(additionSpecifier);
			lotus.domino.Document doc = adder.getFirstDocument();
			while (doc != null) {
				getDelegate().add(doc);

				lotus.domino.Document tempDoc = doc;
				doc = adder.getNextDocument(doc);
				tempDoc.recycle();
			}
			// getDelegate().add((lotus.domino.DocumentCollection) toLotus(additionSpecifier));
		} catch (NotesException e) {
			e.printStackTrace();
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#add(lotus.domino.Form)
	 */
	@Override
	public void add(final lotus.domino.Form additionSpecifier) {
		try {
			getDelegate().add((lotus.domino.Form) toLotus(additionSpecifier));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#add(lotus.domino.NoteCollection)
	 */
	@Override
	public void add(final lotus.domino.NoteCollection additionSpecifier) {
		try {
			getDelegate().add((lotus.domino.NoteCollection) toLotus(additionSpecifier));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#add(lotus.domino.View)
	 */
	@Override
	public void add(final lotus.domino.View additionSpecifier) {
		try {
			getDelegate().add((lotus.domino.View) toLotus(additionSpecifier));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#add(java.lang.String)
	 */
	@Override
	public void add(final String additionSpecifier) {
		try {
			getDelegate().add(additionSpecifier);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#buildCollection()
	 */
	@Override
	public void buildCollection() {
		try {
			getDelegate().buildCollection();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#clearCollection()
	 */
	@Override
	public void clearCollection() {
		try {
			getDelegate().clearCollection();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#getCount()
	 */
	@Override
	public int getCount() {
		try {
			return getDelegate().getCount();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#getFirstNoteID()
	 */
	@Override
	public String getFirstNoteID() {
		try {
			return getDelegate().getFirstNoteID();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#getLastBuildTime()
	 */
	@Override
	public DateTime getLastBuildTime() {
		try {
			return Factory.fromLotus(getDelegate().getLastBuildTime(), DateTime.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#getLastNoteID()
	 */
	@Override
	public String getLastNoteID() {
		try {
			return getDelegate().getLastNoteID();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#getNextNoteID(java.lang.String)
	 */
	@Override
	public String getNextNoteID(final String noteId) {
		try {
			return getDelegate().getNextNoteID(noteId);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#getNoteIDs()
	 */
	@Override
	public int[] getNoteIDs() {
		try {
			return getDelegate().getNoteIDs();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public org.openntf.domino.Database getParent() {
		return (org.openntf.domino.Database) super.getParent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#getPrevNoteID(java.lang.String)
	 */
	@Override
	public String getPrevNoteID(final String noteId) {
		try {
			return getDelegate().getPrevNoteID(noteId);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#getSelectAcl()
	 */
	@Override
	public boolean getSelectAcl() {
		try {
			return getDelegate().getSelectAcl();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#getSelectActions()
	 */
	@Override
	public boolean getSelectActions() {
		try {
			return getDelegate().getSelectActions();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#getSelectAgents()
	 */
	@Override
	public boolean getSelectAgents() {
		try {
			return getDelegate().getSelectAgents();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#getSelectDatabaseScript()
	 */
	@Override
	public boolean getSelectDatabaseScript() {
		try {
			return getDelegate().getSelectDatabaseScript();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#getSelectDataConnections()
	 */
	@Override
	public boolean getSelectDataConnections() {
		try {
			return getDelegate().getSelectDataConnections();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#getSelectDocuments()
	 */
	@Override
	public boolean getSelectDocuments() {
		try {
			return getDelegate().getSelectDocuments();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#getSelectFolders()
	 */
	@Override
	public boolean getSelectFolders() {
		try {
			return getDelegate().getSelectFolders();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#getSelectForms()
	 */
	@Override
	public boolean getSelectForms() {
		try {
			return getDelegate().getSelectForms();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#getSelectFramesets()
	 */
	@Override
	public boolean getSelectFramesets() {
		try {
			return getDelegate().getSelectFramesets();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#getSelectHelpAbout()
	 */
	@Override
	public boolean getSelectHelpAbout() {
		try {
			return getDelegate().getSelectHelpAbout();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#getSelectHelpIndex()
	 */
	@Override
	public boolean getSelectHelpIndex() {
		try {
			return getDelegate().getSelectHelpIndex();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#getSelectHelpUsing()
	 */
	@Override
	public boolean getSelectHelpUsing() {
		try {
			return getDelegate().getSelectHelpUsing();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#getSelectIcon()
	 */
	@Override
	public boolean getSelectIcon() {
		try {
			return getDelegate().getSelectIcon();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#getSelectImageResources()
	 */
	@Override
	public boolean getSelectImageResources() {
		try {
			return getDelegate().getSelectImageResources();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#getSelectionFormula()
	 */
	@Override
	public String getSelectionFormula() {
		try {
			return getDelegate().getSelectionFormula();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#getSelectJavaResources()
	 */
	@Override
	public boolean getSelectJavaResources() {
		try {
			return getDelegate().getSelectJavaResources();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#getSelectMiscCodeElements()
	 */
	@Override
	public boolean getSelectMiscCodeElements() {
		try {
			return getDelegate().getSelectMiscCodeElements();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#getSelectMiscFormatElements()
	 */
	@Override
	public boolean getSelectMiscFormatElements() {
		try {
			return getDelegate().getSelectMiscFormatElements();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#getSelectMiscIndexElements()
	 */
	@Override
	public boolean getSelectMiscIndexElements() {
		try {
			return getDelegate().getSelectMiscIndexElements();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#getSelectNavigators()
	 */
	@Override
	public boolean getSelectNavigators() {
		try {
			return getDelegate().getSelectNavigators();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#getSelectOutlines()
	 */
	@Override
	public boolean getSelectOutlines() {
		try {
			return getDelegate().getSelectOutlines();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#getSelectPages()
	 */
	@Override
	public boolean getSelectPages() {
		try {
			return getDelegate().getSelectPages();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#getSelectProfiles()
	 */
	@Override
	public boolean getSelectProfiles() {
		try {
			return getDelegate().getSelectProfiles();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#getSelectReplicationFormulas()
	 */
	@Override
	public boolean getSelectReplicationFormulas() {
		try {
			return getDelegate().getSelectReplicationFormulas();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#getSelectScriptLibraries()
	 */
	@Override
	public boolean getSelectScriptLibraries() {
		try {
			return getDelegate().getSelectScriptLibraries();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#getSelectSharedFields()
	 */
	public boolean getSelectSharedFields() {
		try {
			return getDelegate().getSelectSharedFields();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#getSelectStylesheetResources()
	 */
	public boolean getSelectStylesheetResources() {
		try {
			return getDelegate().getSelectStylesheetResources();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#getSelectSubforms()
	 */
	public boolean getSelectSubforms() {
		try {
			return getDelegate().getSelectSubforms();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#getSelectViews()
	 */
	public boolean getSelectViews() {
		try {
			return getDelegate().getSelectViews();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#getSinceTime()
	 */
	public DateTime getSinceTime() {
		try {
			return Factory.fromLotus(getDelegate().getSinceTime(), DateTime.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#getUNID(java.lang.String)
	 */
	public String getUNID(final String unid) {
		try {
			return getDelegate().getUNID(unid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#getUntilTime()
	 */
	public DateTime getUntilTime() {
		try {
			return Factory.fromLotus(getDelegate().getUntilTime(), DateTime.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#intersect(lotus.domino.Agent)
	 */
	public void intersect(final lotus.domino.Agent agent) {
		try {
			getDelegate().intersect((lotus.domino.Agent) toLotus(agent));
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#intersect(lotus.domino.Document)
	 */
	public void intersect(final lotus.domino.Document document) {
		try {
			getDelegate().intersect((lotus.domino.Document) toLotus(document));
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#intersect(lotus.domino.DocumentCollection)
	 */
	public void intersect(final lotus.domino.DocumentCollection collection) {
		try {
			getDelegate().intersect((lotus.domino.DocumentCollection) toLotus(collection));
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#intersect(lotus.domino.Form)
	 */
	public void intersect(final lotus.domino.Form form) {
		try {
			getDelegate().intersect((lotus.domino.Form) toLotus(form));
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#intersect(int)
	 */
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
	 * @see org.openntf.domino.NoteCollection#intersect(lotus.domino.NoteCollection)
	 */
	public void intersect(final lotus.domino.NoteCollection collection) {
		try {
			getDelegate().intersect((lotus.domino.NoteCollection) toLotus(collection));
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#intersect(java.lang.String)
	 */
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
	 * @see org.openntf.domino.NoteCollection#intersect(lotus.domino.View)
	 */
	public void intersect(final lotus.domino.View view) {
		try {
			getDelegate().intersect((lotus.domino.View) toLotus(view));
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<String> iterator() {
		return new NoteIterator(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.impl.Base#recycle()
	 */
	@Override
	public void recycle() {
		try {
			getDelegate().recycle();
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.impl.Base#recycle(java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void recycle(final Vector objects) {
		try {
			getDelegate().recycle(objects);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#remove(lotus.domino.Agent)
	 */
	public void remove(final lotus.domino.Agent agent) {
		try {
			getDelegate().remove((lotus.domino.Agent) toLotus(agent));
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#remove(lotus.domino.Document)
	 */
	public void remove(final lotus.domino.Document document) {
		try {
			getDelegate().remove((lotus.domino.Document) toLotus(document));
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#remove(lotus.domino.DocumentCollection)
	 */
	public void remove(final lotus.domino.DocumentCollection collection) {
		try {
			getDelegate().remove((lotus.domino.DocumentCollection) toLotus(collection));
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#remove(lotus.domino.Form)
	 */
	public void remove(final lotus.domino.Form form) {
		try {
			getDelegate().remove((lotus.domino.Form) toLotus(form));
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#remove(int)
	 */
	public void remove(final int noteId) {
		try {
			getDelegate().remove(noteId);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#remove(lotus.domino.NoteCollection)
	 */
	public void remove(final lotus.domino.NoteCollection collection) {
		try {
			getDelegate().remove((lotus.domino.NoteCollection) toLotus(collection));
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#remove(java.lang.String)
	 */
	public void remove(final String noteId) {
		try {
			getDelegate().remove(noteId);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#remove(lotus.domino.View)
	 */
	public void remove(final lotus.domino.View view) {
		try {
			getDelegate().remove((lotus.domino.View) toLotus(view));
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#selectAllAdminNotes(boolean)
	 */
	public void selectAllAdminNotes(final boolean flag) {
		try {
			getDelegate().selectAllAdminNotes(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#selectAllCodeElements(boolean)
	 */
	public void selectAllCodeElements(final boolean flag) {
		try {
			getDelegate().selectAllCodeElements(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#selectAllDataNotes(boolean)
	 */
	public void selectAllDataNotes(final boolean flag) {
		try {
			getDelegate().selectAllDataNotes(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#selectAllDesignElements(boolean)
	 */
	public void selectAllDesignElements(final boolean flag) {
		try {
			getDelegate().selectAllDesignElements(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#selectAllFormatElements(boolean)
	 */
	public void selectAllFormatElements(final boolean flag) {
		try {
			getDelegate().selectAllFormatElements(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#selectAllIndexElements(boolean)
	 */
	public void selectAllIndexElements(final boolean flag) {
		try {
			getDelegate().selectAllIndexElements(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#selectAllNotes(boolean)
	 */
	public void selectAllNotes(final boolean flag) {
		try {
			getDelegate().selectAllNotes(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#setSelectAcl(boolean)
	 */
	public void setSelectAcl(final boolean flag) {
		try {
			getDelegate().setSelectAcl(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#setSelectActions(boolean)
	 */
	public void setSelectActions(final boolean flag) {
		try {
			getDelegate().setSelectActions(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#setSelectAgents(boolean)
	 */
	public void setSelectAgents(final boolean flag) {
		try {
			getDelegate().setSelectAgents(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#setSelectDatabaseScript(boolean)
	 */
	public void setSelectDatabaseScript(final boolean flag) {
		try {
			getDelegate().setSelectDatabaseScript(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#setSelectDataConnections(boolean)
	 */
	public void setSelectDataConnections(final boolean flag) {
		try {
			getDelegate().setSelectDataConnections(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#setSelectDocuments(boolean)
	 */
	public void setSelectDocuments(final boolean flag) {
		try {
			getDelegate().setSelectDocuments(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#setSelectFolders(boolean)
	 */
	public void setSelectFolders(final boolean flag) {
		try {
			getDelegate().setSelectFolders(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#setSelectForms(boolean)
	 */
	public void setSelectForms(final boolean flag) {
		try {
			getDelegate().setSelectForms(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#setSelectFramesets(boolean)
	 */
	public void setSelectFramesets(final boolean flag) {
		try {
			getDelegate().setSelectFramesets(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#setSelectHelpAbout(boolean)
	 */
	public void setSelectHelpAbout(final boolean flag) {
		try {
			getDelegate().setSelectHelpAbout(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#setSelectHelpIndex(boolean)
	 */
	public void setSelectHelpIndex(final boolean flag) {
		try {
			getDelegate().setSelectHelpIndex(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#setSelectHelpUsing(boolean)
	 */
	public void setSelectHelpUsing(final boolean flag) {
		try {
			getDelegate().setSelectHelpUsing(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#setSelectIcon(boolean)
	 */
	public void setSelectIcon(final boolean flag) {
		try {
			getDelegate().setSelectIcon(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#setSelectImageResources(boolean)
	 */
	public void setSelectImageResources(final boolean flag) {
		try {
			getDelegate().setSelectImageResources(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#setSelectionFormula(java.lang.String)
	 */
	public void setSelectionFormula(final String formula) {
		try {
			getDelegate().setSelectionFormula(formula);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#setSelectJavaResources(boolean)
	 */
	public void setSelectJavaResources(final boolean flag) {
		try {
			getDelegate().setSelectJavaResources(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#setSelectMiscCodeElements(boolean)
	 */
	public void setSelectMiscCodeElements(final boolean flag) {
		try {
			getDelegate().setSelectMiscCodeElements(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#setSelectMiscFormatElements(boolean)
	 */
	public void setSelectMiscFormatElements(final boolean flag) {
		try {
			getDelegate().setSelectMiscFormatElements(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#setSelectMiscIndexElements(boolean)
	 */
	public void setSelectMiscIndexElements(final boolean flag) {
		try {
			getDelegate().setSelectMiscIndexElements(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#setSelectNavigators(boolean)
	 */
	public void setSelectNavigators(final boolean flag) {
		try {
			getDelegate().setSelectNavigators(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#setSelectOutlines(boolean)
	 */
	public void setSelectOutlines(final boolean flag) {
		try {
			getDelegate().setSelectOutlines(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#setSelectPages(boolean)
	 */
	public void setSelectPages(final boolean flag) {
		try {
			getDelegate().setSelectPages(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#setSelectProfiles(boolean)
	 */
	public void setSelectProfiles(final boolean flag) {
		try {
			getDelegate().setSelectProfiles(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#setSelectReplicationFormulas(boolean)
	 */
	public void setSelectReplicationFormulas(final boolean flag) {
		try {
			getDelegate().setSelectReplicationFormulas(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#setSelectScriptLibraries(boolean)
	 */
	public void setSelectScriptLibraries(final boolean flag) {
		try {
			getDelegate().setSelectScriptLibraries(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#setSelectSharedFields(boolean)
	 */
	public void setSelectSharedFields(final boolean flag) {
		try {
			getDelegate().setSelectSharedFields(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#setSelectStylesheetResources(boolean)
	 */
	public void setSelectStylesheetResources(final boolean flag) {
		try {
			getDelegate().setSelectStylesheetResources(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#setSelectSubforms(boolean)
	 */
	public void setSelectSubforms(final boolean flag) {
		try {
			getDelegate().setSelectSubforms(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#setSelectViews(boolean)
	 */
	public void setSelectViews(final boolean flag) {
		try {
			getDelegate().setSelectViews(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NoteCollection#setSinceTime(lotus.domino.DateTime)
	 */
	public void setSinceTime(final lotus.domino.DateTime date) {
		try {
			lotus.domino.DateTime dt = (lotus.domino.DateTime) toLotus(date);
			getDelegate().setSinceTime(dt);
			enc_recycle(dt);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	public void setSinceTime(final java.util.Date date) {
		org.openntf.domino.DateTime dt = new org.openntf.domino.impl.DateTime(date, getAncestorSession());
		setSinceTime(dt);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.DatabaseDescendant#getAncestorDatabase()
	 */
	@Override
	public Database getAncestorDatabase() {
		return this.getParent();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ext.NoteCollection#setSelectOptions(java.util.Set)
	 */
	@Override
	public void setSelectOptions(final Set<SelectOption> options) {
		for (SelectOption option : options) {
			switch (option) {
			case ACL:
				setSelectAcl(true);
				break;
			case ACTIONS:
				setSelectActions(true);
				break;
			case AGENTS:
				setSelectAgents(true);
				break;
			case DATABASE_SCRIPT:
				setSelectDatabaseScript(true);
				break;
			case DATA_CONNECTIONS:
				setSelectDataConnections(true);
				break;
			case DOCUMENTS:
				setSelectDocuments(true);
				break;
			case FOLDERS:
				setSelectFolders(true);
				break;
			case FORMS:
				setSelectForms(true);
				break;
			case FRAMESETS:
				setSelectFramesets(true);
				break;
			case HELP_ABOUT:
				setSelectHelpAbout(true);
				break;
			case HELP_INDEX:
				setSelectHelpIndex(true);
				break;
			case HELP_USING:
				setSelectHelpUsing(true);
				break;
			case ICON:
				setSelectIcon(true);
				break;
			case IMAGE_RESOURCES:
				setSelectImageResources(true);
				break;
			case JAVA_RESOURCES:
				setSelectJavaResources(true);
				break;
			case MISC_CODE:
				setSelectMiscCodeElements(true);
				break;
			case MISC_FORMAT:
				setSelectMiscFormatElements(true);
				break;
			case MISC_INDEX:
				setSelectMiscIndexElements(true);
				break;
			case NAVIGATORS:
				setSelectNavigators(true);
				break;
			case OUTLINES:
				setSelectOutlines(true);
				break;
			case PAGES:
				setSelectPages(true);
				break;
			case PROFILES:
				setSelectProfiles(true);
				break;
			case REPLICATION_FORMULAS:
				setSelectReplicationFormulas(true);
				break;
			case SCRIPT_LIBRARIES:
				setSelectScriptLibraries(true);
				break;
			case SHARED_FIELDS:
				setSelectSharedFields(true);
				break;
			case STYLESHEETS:
				setSelectStylesheetResources(true);
				break;
			case SUBFORMS:
				setSelectSubforms(true);
				break;
			case VIEWS:
				setSelectViews(true);
				break;
			}
		}
	}

}

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
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.Database;
import org.openntf.domino.DateTime;
import org.openntf.domino.Session;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.iterators.NoteIterator;
import org.openntf.domino.utils.DominoUtils;

/**
 * The Class NoteCollection.
 */
/**
 * @author withersp
 *
 */
public class NoteCollection extends BaseThreadSafe<org.openntf.domino.NoteCollection, lotus.domino.NoteCollection, Database>
		implements org.openntf.domino.NoteCollection {
	//private static final Logger log_ = Logger.getLogger(NoteCollection.class.getName());

	/**
	 * Instantiates a new outline.
	 *
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	protected NoteCollection(final lotus.domino.NoteCollection delegate, final Database parent) {
		super(delegate, parent, NOTES_NOTECOLLECTION);
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
			getDelegate().add(toLotus(additionSpecifier));
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
			getDelegate().add(toLotus(additionSpecifier));
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
			//if (additionSpecifier instanceof lotus.domino.DocumentCollection) {
			getDelegate().add(additionSpecifier); // TODO RPr: use toLotus?
			//			} else {
			//				if (log_.isLoggable(Level.WARNING)) {
			//					log_.log(
			//							Level.WARNING,
			//							"Attempting to add a native lotus.domino.DocumentCollection to an org.openntf.domino.NoteCollection. Because we cannot know the use of the DocumentCollection argument later, we cannot auto-recycle. You really shouldn't mix your API types.");
			//				}
			//				lotus.domino.Document doc = additionSpecifier.getFirstDocument();
			//				while (doc != null) {
			//					getDelegate().add(toLotus(doc));
			//
			//					doc = additionSpecifier.getNextDocument(doc);
			//				}
			//			}
		} catch (NotesException e) {
			//			e.printStackTrace();
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
			getDelegate().add(toLotus(additionSpecifier));
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
			getDelegate().add(toLotus(additionSpecifier));
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
			getDelegate().add(toLotus(additionSpecifier));
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
			lotus.domino.NoteCollection nc = getDelegate();
			if (nc == null) {
				System.out.println("Delegate is null???");
			} else {
				getDelegate().buildCollection();
			}
		} catch (Throwable t) {
			DominoUtils.handleException(t);
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
			return fromLotus(getDelegate().getLastBuildTime(), DateTime.SCHEMA, getAncestorSession());
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
	public final Database getParent() {
		return parent;
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
	public DateTime getSinceTime() {
		try {
			return fromLotus(getDelegate().getSinceTime(), DateTime.SCHEMA, getAncestorSession());
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
	@Override
	public String getUNID(final String noteid) {
		try {
			return getDelegate().getUNID(noteid);
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
	 * @see org.openntf.domino.NoteCollection#intersect(lotus.domino.Agent)
	 */
	@Override
	public void intersect(final lotus.domino.Agent agent) {
		try {
			getDelegate().intersect(toLotus(agent));
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.NoteCollection#intersect(lotus.domino.Document)
	 */
	@Override
	public void intersect(final lotus.domino.Document document) {
		try {
			getDelegate().intersect(toLotus(document));
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.NoteCollection#intersect(lotus.domino.DocumentCollection)
	 */
	@Override
	public void intersect(final lotus.domino.DocumentCollection collection) {
		try {
			getDelegate().intersect(toLotus(collection));
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.NoteCollection#intersect(lotus.domino.Form)
	 */
	@Override
	public void intersect(final lotus.domino.Form form) {
		try {
			getDelegate().intersect(toLotus(form));
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.NoteCollection#intersect(int)
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
	 * @see org.openntf.domino.NoteCollection#intersect(lotus.domino.NoteCollection)
	 */
	@Override
	public void intersect(final lotus.domino.NoteCollection collection) {
		try {
			getDelegate().intersect(toLotus(collection));
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.NoteCollection#intersect(java.lang.String)
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
	 * @see org.openntf.domino.NoteCollection#intersect(lotus.domino.View)
	 */
	@Override
	public void intersect(final lotus.domino.View view) {
		try {
			getDelegate().intersect(toLotus(view));
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
	@Override
	public void remove(final lotus.domino.Agent agent) {
		try {
			getDelegate().remove(toLotus(agent));
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.NoteCollection#remove(lotus.domino.Document)
	 */
	@Override
	public void remove(final lotus.domino.Document document) {
		try {
			getDelegate().remove(toLotus(document));
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.NoteCollection#remove(lotus.domino.DocumentCollection)
	 */
	@Override
	public void remove(final lotus.domino.DocumentCollection collection) {
		try {
			getDelegate().remove(toLotus(collection));
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.NoteCollection#remove(lotus.domino.Form)
	 */
	@Override
	public void remove(final lotus.domino.Form form) {
		try {
			getDelegate().remove(toLotus(form));
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.NoteCollection#remove(int)
	 */
	@Override
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
	@Override
	public void remove(final lotus.domino.NoteCollection collection) {
		try {
			getDelegate().remove(toLotus(collection));
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.NoteCollection#remove(java.lang.String)
	 */
	@Override
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
	@Override
	public void remove(final lotus.domino.View view) {
		try {
			getDelegate().remove(toLotus(view));
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.NoteCollection#selectAllAdminNotes(boolean)
	 */
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
	public void setSinceTime(final lotus.domino.DateTime date) {
		@SuppressWarnings("rawtypes")
		List recycleThis = new ArrayList();
		try {
			getDelegate().setSinceTime(toLotus(date, recycleThis));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		} finally {
			s_recycle(recycleThis);
		}

	}

	@Override
	@SuppressWarnings("unchecked")
	public void setSinceTime(final java.util.Date date) {
		@SuppressWarnings("rawtypes")
		List recycleThis = new ArrayList();
		try {
			lotus.domino.DateTime dt = (lotus.domino.DateTime) toDominoFriendly(date, getAncestorSession(), recycleThis);
			getDelegate().setSinceTime(dt);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		} finally {
			s_recycle(recycleThis);
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.types.DatabaseDescendant#getAncestorDatabase()
	 */
	@Override
	public final Database getAncestorDatabase() {
		return parent;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public final Session getAncestorSession() {
		return this.getAncestorDatabase().getAncestorSession();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.ext.NoteCollection#setSelectOptions(java.util.Set)
	 */
	@Override
	public void setSelectOptions(final Set<SelectOption> options) {
		boolean select = true;
		if (options.contains(SelectOption.ALL_BUT_NOT)) {
			selectAllNotes(true);
			select = false;
		}
		for (SelectOption option : options) {
			switch (option) {
			case ACL:
				setSelectAcl(select);
				break;
			case ACTIONS:
				setSelectActions(select);
				break;
			case AGENTS:
				setSelectAgents(select);
				break;
			case DATABASE_SCRIPT:
				setSelectDatabaseScript(select);
				break;
			case DATA_CONNECTIONS:
				setSelectDataConnections(select);
				break;
			case DOCUMENTS:
				setSelectDocuments(select);
				break;
			case FOLDERS:
				setSelectFolders(select);
				break;
			case FORMS:
				setSelectForms(select);
				break;
			case FRAMESETS:
				setSelectFramesets(select);
				break;
			case HELP_ABOUT:
				setSelectHelpAbout(select);
				break;
			case HELP_INDEX:
				setSelectHelpIndex(select);
				break;
			case HELP_USING:
				setSelectHelpUsing(select);
				break;
			case ICON:
				setSelectIcon(select);
				break;
			case IMAGE_RESOURCES:
				setSelectImageResources(select);
				break;
			case JAVA_RESOURCES:
				setSelectJavaResources(select);
				break;
			case MISC_CODE:
				setSelectMiscCodeElements(select);
				break;
			case MISC_FORMAT:
				setSelectMiscFormatElements(select);
				break;
			case MISC_INDEX:
				setSelectMiscIndexElements(select);
				break;
			case NAVIGATORS:
				setSelectNavigators(select);
				break;
			case OUTLINES:
				setSelectOutlines(select);
				break;
			case PAGES:
				setSelectPages(select);
				break;
			case PROFILES:
				setSelectProfiles(select);
				break;
			case REPLICATION_FORMULAS:
				setSelectReplicationFormulas(select);
				break;
			case SCRIPT_LIBRARIES:
				setSelectScriptLibraries(select);
				break;
			case SHARED_FIELDS:
				setSelectSharedFields(select);
				break;
			case STYLESHEETS:
				setSelectStylesheetResources(select);
				break;
			case SUBFORMS:
				setSelectSubforms(select);
				break;
			case VIEWS:
				setSelectViews(select);
				break;
			case ALL_BUT_NOT:
				break;
			default:
				break;

			}
		}
	}

	@Override
	protected WrapperFactory getFactory() {
		return parent.getAncestorSession().getFactory();
	}

}

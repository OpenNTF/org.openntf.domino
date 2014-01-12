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
package org.openntf.domino;

import org.openntf.domino.types.DatabaseDescendant;

/**
 * The Interface NoteCollection.
 */
public interface NoteCollection extends Base<lotus.domino.NoteCollection>, lotus.domino.NoteCollection,
		org.openntf.domino.ext.NoteCollection, Iterable<String>, DatabaseDescendant {

	public static enum SelectOption {
		ACL, ACTIONS, AGENTS, DATABASE_SCRIPT, DATA_CONNECTIONS, DOCUMENTS, FOLDERS, FORMS, FRAMESETS, HELP_ABOUT, HELP_INDEX, HELP_USING, ICON, IMAGE_RESOURCES, JAVA_RESOURCES, MISC_CODE, MISC_FORMAT, MISC_INDEX, NAVIGATORS, OUTLINES, PAGES, PROFILES, REPLICATION_FORMULAS, SCRIPT_LIBRARIES, SHARED_FIELDS, STYLESHEETS, SUBFORMS, VIEWS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#add(lotus.domino.DocumentCollection)
	 */
	@Override
	public void add(final lotus.domino.DocumentCollection additionSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#add(lotus.domino.Form)
	 */
	@Override
	public void add(final lotus.domino.Form additionSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#add(int)
	 */
	@Override
	public void add(final int additionSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#add(int[])
	 */
	@Override
	public void add(final int[] additionSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#add(lotus.domino.Agent)
	 */
	@Override
	public void add(final lotus.domino.Agent additionSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#add(lotus.domino.Document)
	 */
	@Override
	public void add(final lotus.domino.Document additionSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#add(lotus.domino.NoteCollection)
	 */
	@Override
	public void add(final lotus.domino.NoteCollection additionSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#add(java.lang.String)
	 */
	@Override
	public void add(final String noteid);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#add(lotus.domino.View)
	 */
	@Override
	public void add(final lotus.domino.View additionSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#buildCollection()
	 */
	@Override
	public void buildCollection();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#clearCollection()
	 */
	@Override
	public void clearCollection();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getCount()
	 */
	@Override
	public int getCount();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getFirstNoteID()
	 */
	@Override
	public String getFirstNoteID();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getLastBuildTime()
	 */
	@Override
	public DateTime getLastBuildTime();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getLastNoteID()
	 */
	@Override
	public String getLastNoteID();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getNextNoteID(java.lang.String)
	 */
	@Override
	public String getNextNoteID(final String noteId);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getNoteIDs()
	 */
	@Override
	public int[] getNoteIDs();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getParent()
	 */
	@Override
	public Database getParent();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getPrevNoteID(java.lang.String)
	 */
	@Override
	public String getPrevNoteID(final String noteId);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getSelectAcl()
	 */
	@Override
	public boolean getSelectAcl();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getSelectActions()
	 */
	@Override
	public boolean getSelectActions();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getSelectAgents()
	 */
	@Override
	public boolean getSelectAgents();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getSelectDatabaseScript()
	 */
	@Override
	public boolean getSelectDatabaseScript();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getSelectDataConnections()
	 */
	@Override
	public boolean getSelectDataConnections();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getSelectDocuments()
	 */
	@Override
	public boolean getSelectDocuments();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getSelectFolders()
	 */
	@Override
	public boolean getSelectFolders();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getSelectForms()
	 */
	@Override
	public boolean getSelectForms();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getSelectFramesets()
	 */
	@Override
	public boolean getSelectFramesets();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getSelectHelpAbout()
	 */
	@Override
	public boolean getSelectHelpAbout();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getSelectHelpIndex()
	 */
	@Override
	public boolean getSelectHelpIndex();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getSelectHelpUsing()
	 */
	@Override
	public boolean getSelectHelpUsing();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getSelectIcon()
	 */
	@Override
	public boolean getSelectIcon();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getSelectImageResources()
	 */
	@Override
	public boolean getSelectImageResources();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getSelectionFormula()
	 */
	@Override
	public String getSelectionFormula();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getSelectJavaResources()
	 */
	@Override
	public boolean getSelectJavaResources();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getSelectMiscCodeElements()
	 */
	@Override
	public boolean getSelectMiscCodeElements();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getSelectMiscFormatElements()
	 */
	@Override
	public boolean getSelectMiscFormatElements();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getSelectMiscIndexElements()
	 */
	@Override
	public boolean getSelectMiscIndexElements();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getSelectNavigators()
	 */
	@Override
	public boolean getSelectNavigators();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getSelectOutlines()
	 */
	@Override
	public boolean getSelectOutlines();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getSelectPages()
	 */
	@Override
	public boolean getSelectPages();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getSelectProfiles()
	 */
	@Override
	public boolean getSelectProfiles();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getSelectReplicationFormulas()
	 */
	@Override
	public boolean getSelectReplicationFormulas();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getSelectScriptLibraries()
	 */
	@Override
	public boolean getSelectScriptLibraries();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getSelectSharedFields()
	 */
	@Override
	public boolean getSelectSharedFields();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getSelectStylesheetResources()
	 */
	@Override
	public boolean getSelectStylesheetResources();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getSelectSubforms()
	 */
	@Override
	public boolean getSelectSubforms();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getSelectViews()
	 */
	@Override
	public boolean getSelectViews();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getSinceTime()
	 */
	@Override
	public DateTime getSinceTime();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getUNID(java.lang.String)
	 */
	@Override
	public String getUNID(final String noteid);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getUntilTime()
	 */
	@Override
	public DateTime getUntilTime();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#intersect(lotus.domino.Document)
	 */
	@Override
	public void intersect(final lotus.domino.Document intersectionSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#intersect(lotus.domino.DocumentCollection)
	 */
	@Override
	public void intersect(final lotus.domino.DocumentCollection intersectionSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#intersect(lotus.domino.Form)
	 */
	@Override
	public void intersect(final lotus.domino.Form intersectionSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#intersect(int)
	 */
	@Override
	public void intersect(final int intersectionSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#intersect(lotus.domino.Agent)
	 */
	@Override
	public void intersect(final lotus.domino.Agent intersectionSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#intersect(lotus.domino.NoteCollection)
	 */
	@Override
	public void intersect(final lotus.domino.NoteCollection intersectionSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#intersect(java.lang.String)
	 */
	@Override
	public void intersect(final String noteid);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#intersect(lotus.domino.View)
	 */
	@Override
	public void intersect(final lotus.domino.View intersectionSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#remove(lotus.domino.Document)
	 */
	@Override
	public void remove(final lotus.domino.Document removalSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#remove(lotus.domino.DocumentCollection)
	 */
	@Override
	public void remove(final lotus.domino.DocumentCollection removalSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#remove(lotus.domino.Form)
	 */
	@Override
	public void remove(final lotus.domino.Form removalSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#remove(int)
	 */
	@Override
	public void remove(final int removalSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#remove(lotus.domino.Agent)
	 */
	@Override
	public void remove(final lotus.domino.Agent removalSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#remove(lotus.domino.NoteCollection)
	 */
	@Override
	public void remove(final lotus.domino.NoteCollection removalSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#remove(java.lang.String)
	 */
	@Override
	public void remove(final String noteid);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#remove(lotus.domino.View)
	 */
	@Override
	public void remove(final lotus.domino.View removalSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#selectAllAdminNotes(boolean)
	 */
	@Override
	public void selectAllAdminNotes(final boolean selectorValue);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#selectAllCodeElements(boolean)
	 */
	@Override
	public void selectAllCodeElements(final boolean selectorValue);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#selectAllDataNotes(boolean)
	 */
	@Override
	public void selectAllDataNotes(final boolean selectorValue);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#selectAllDesignElements(boolean)
	 */
	@Override
	public void selectAllDesignElements(final boolean selectorValue);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#selectAllFormatElements(boolean)
	 */
	@Override
	public void selectAllFormatElements(final boolean selectorValue);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#selectAllIndexElements(boolean)
	 */
	@Override
	public void selectAllIndexElements(final boolean selectorValue);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#selectAllNotes(boolean)
	 */
	@Override
	public void selectAllNotes(final boolean selectorValue);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectAcl(boolean)
	 */
	@Override
	public void setSelectAcl(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectActions(boolean)
	 */
	@Override
	public void setSelectActions(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectAgents(boolean)
	 */
	@Override
	public void setSelectAgents(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectDatabaseScript(boolean)
	 */
	@Override
	public void setSelectDatabaseScript(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectDataConnections(boolean)
	 */
	@Override
	public void setSelectDataConnections(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectDocuments(boolean)
	 */
	@Override
	public void setSelectDocuments(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectFolders(boolean)
	 */
	@Override
	public void setSelectFolders(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectForms(boolean)
	 */
	@Override
	public void setSelectForms(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectFramesets(boolean)
	 */
	@Override
	public void setSelectFramesets(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectHelpAbout(boolean)
	 */
	@Override
	public void setSelectHelpAbout(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectHelpIndex(boolean)
	 */
	@Override
	public void setSelectHelpIndex(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectHelpUsing(boolean)
	 */
	@Override
	public void setSelectHelpUsing(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectIcon(boolean)
	 */
	@Override
	public void setSelectIcon(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectImageResources(boolean)
	 */
	@Override
	public void setSelectImageResources(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectionFormula(java.lang.String)
	 */
	@Override
	public void setSelectionFormula(final String formula);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectJavaResources(boolean)
	 */
	@Override
	public void setSelectJavaResources(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectMiscCodeElements(boolean)
	 */
	@Override
	public void setSelectMiscCodeElements(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectMiscFormatElements(boolean)
	 */
	@Override
	public void setSelectMiscFormatElements(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectMiscIndexElements(boolean)
	 */
	@Override
	public void setSelectMiscIndexElements(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectNavigators(boolean)
	 */
	@Override
	public void setSelectNavigators(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectOutlines(boolean)
	 */
	@Override
	public void setSelectOutlines(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectPages(boolean)
	 */
	@Override
	public void setSelectPages(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectProfiles(boolean)
	 */
	@Override
	public void setSelectProfiles(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectReplicationFormulas(boolean)
	 */
	@Override
	public void setSelectReplicationFormulas(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectScriptLibraries(boolean)
	 */
	@Override
	public void setSelectScriptLibraries(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectSharedFields(boolean)
	 */
	@Override
	public void setSelectSharedFields(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectStylesheetResources(boolean)
	 */
	@Override
	public void setSelectStylesheetResources(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectSubforms(boolean)
	 */
	@Override
	public void setSelectSubforms(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectViews(boolean)
	 */
	@Override
	public void setSelectViews(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSinceTime(lotus.domino.DateTime)
	 */
	@Override
	public void setSinceTime(final lotus.domino.DateTime sinceTime);

	/**
	 * To string.
	 * 
	 * @return the string
	 */
	@Override
	public String toString();

}

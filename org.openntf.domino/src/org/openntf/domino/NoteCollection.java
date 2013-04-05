/*
 * Copyright OpenNTF 2013
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

import java.util.Vector;

import org.openntf.domino.types.DatabaseDescendant;

// TODO: Auto-generated Javadoc
/**
 * The Interface NoteCollection.
 */
public interface NoteCollection extends Base<lotus.domino.NoteCollection>, lotus.domino.NoteCollection, Iterable<String>,
		DatabaseDescendant {

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#add(lotus.domino.DocumentCollection)
	 */
	@Override
	public void add(lotus.domino.DocumentCollection additionSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#add(lotus.domino.Form)
	 */
	@Override
	public void add(lotus.domino.Form additionSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#add(int)
	 */
	@Override
	public void add(int additionSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#add(int[])
	 */
	@Override
	public void add(int[] additionSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#add(lotus.domino.Agent)
	 */
	@Override
	public void add(lotus.domino.Agent additionSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#add(lotus.domino.Document)
	 */
	@Override
	public void add(lotus.domino.Document additionSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#add(lotus.domino.NoteCollection)
	 */
	@Override
	public void add(lotus.domino.NoteCollection additionSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#add(java.lang.String)
	 */
	@Override
	public void add(String noteid);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#add(lotus.domino.View)
	 */
	@Override
	public void add(lotus.domino.View additionSpecifier);

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

	/**
	 * Equals.
	 * 
	 * @param otherCollection
	 *            the other collection
	 * @return true, if successful
	 */
	public boolean equals(Object otherCollection);

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
	public String getNextNoteID(String noteId);

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
	public String getPrevNoteID(String noteId);

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
	public String getUNID(String noteid);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#getUntilTime()
	 */
	@Override
	public DateTime getUntilTime();

	/**
	 * Hash code.
	 * 
	 * @return the int
	 */
	@Override
	public int hashCode();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#intersect(lotus.domino.Document)
	 */
	@Override
	public void intersect(lotus.domino.Document intersectionSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#intersect(lotus.domino.DocumentCollection)
	 */
	@Override
	public void intersect(lotus.domino.DocumentCollection intersectionSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#intersect(lotus.domino.Form)
	 */
	@Override
	public void intersect(lotus.domino.Form intersectionSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#intersect(int)
	 */
	@Override
	public void intersect(int intersectionSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#intersect(lotus.domino.Agent)
	 */
	@Override
	public void intersect(lotus.domino.Agent intersectionSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#intersect(lotus.domino.NoteCollection)
	 */
	@Override
	public void intersect(lotus.domino.NoteCollection intersectionSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#intersect(java.lang.String)
	 */
	@Override
	public void intersect(String noteid);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#intersect(lotus.domino.View)
	 */
	@Override
	public void intersect(lotus.domino.View intersectionSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Base#recycle()
	 */
	public void recycle();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Base#recycle(java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	public void recycle(Vector objects);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#remove(lotus.domino.Document)
	 */
	@Override
	public void remove(lotus.domino.Document removalSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#remove(lotus.domino.DocumentCollection)
	 */
	@Override
	public void remove(lotus.domino.DocumentCollection removalSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#remove(lotus.domino.Form)
	 */
	@Override
	public void remove(lotus.domino.Form removalSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#remove(int)
	 */
	public void remove(int removalSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#remove(lotus.domino.Agent)
	 */
	@Override
	public void remove(lotus.domino.Agent removalSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#remove(lotus.domino.NoteCollection)
	 */
	@Override
	public void remove(lotus.domino.NoteCollection removalSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#remove(java.lang.String)
	 */
	@Override
	public void remove(String noteid);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#remove(lotus.domino.View)
	 */
	@Override
	public void remove(lotus.domino.View removalSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#selectAllAdminNotes(boolean)
	 */
	@Override
	public void selectAllAdminNotes(boolean selectorValue);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#selectAllCodeElements(boolean)
	 */
	@Override
	public void selectAllCodeElements(boolean selectorValue);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#selectAllDataNotes(boolean)
	 */
	@Override
	public void selectAllDataNotes(boolean selectorValue);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#selectAllDesignElements(boolean)
	 */
	@Override
	public void selectAllDesignElements(boolean selectorValue);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#selectAllFormatElements(boolean)
	 */
	@Override
	public void selectAllFormatElements(boolean selectorValue);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#selectAllIndexElements(boolean)
	 */
	@Override
	public void selectAllIndexElements(boolean selectorValue);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#selectAllNotes(boolean)
	 */
	@Override
	public void selectAllNotes(boolean selectorValue);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectAcl(boolean)
	 */
	public void setSelectAcl(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectActions(boolean)
	 */
	public void setSelectActions(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectAgents(boolean)
	 */
	public void setSelectAgents(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectDatabaseScript(boolean)
	 */
	public void setSelectDatabaseScript(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectDataConnections(boolean)
	 */
	public void setSelectDataConnections(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectDocuments(boolean)
	 */
	@Override
	public void setSelectDocuments(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectFolders(boolean)
	 */
	@Override
	public void setSelectFolders(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectForms(boolean)
	 */
	@Override
	public void setSelectForms(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectFramesets(boolean)
	 */
	@Override
	public void setSelectFramesets(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectHelpAbout(boolean)
	 */
	@Override
	public void setSelectHelpAbout(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectHelpIndex(boolean)
	 */
	@Override
	public void setSelectHelpIndex(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectHelpUsing(boolean)
	 */
	@Override
	public void setSelectHelpUsing(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectIcon(boolean)
	 */
	@Override
	public void setSelectIcon(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectImageResources(boolean)
	 */
	@Override
	public void setSelectImageResources(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectionFormula(java.lang.String)
	 */
	@Override
	public void setSelectionFormula(String formula);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectJavaResources(boolean)
	 */
	@Override
	public void setSelectJavaResources(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectMiscCodeElements(boolean)
	 */
	@Override
	public void setSelectMiscCodeElements(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectMiscFormatElements(boolean)
	 */
	@Override
	public void setSelectMiscFormatElements(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectMiscIndexElements(boolean)
	 */
	@Override
	public void setSelectMiscIndexElements(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectNavigators(boolean)
	 */
	@Override
	public void setSelectNavigators(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectOutlines(boolean)
	 */
	@Override
	public void setSelectOutlines(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectPages(boolean)
	 */
	@Override
	public void setSelectPages(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectProfiles(boolean)
	 */
	@Override
	public void setSelectProfiles(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectReplicationFormulas(boolean)
	 */
	@Override
	public void setSelectReplicationFormulas(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectScriptLibraries(boolean)
	 */
	@Override
	public void setSelectScriptLibraries(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectSharedFields(boolean)
	 */
	@Override
	public void setSelectSharedFields(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectStylesheetResources(boolean)
	 */
	@Override
	public void setSelectStylesheetResources(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectSubforms(boolean)
	 */
	@Override
	public void setSelectSubforms(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSelectViews(boolean)
	 */
	@Override
	public void setSelectViews(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#setSinceTime(lotus.domino.DateTime)
	 */
	@Override
	public void setSinceTime(lotus.domino.DateTime sinceTime);

	/**
	 * To string.
	 * 
	 * @return the string
	 */
	@Override
	public String toString();

}

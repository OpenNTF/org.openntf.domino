package org.openntf.domino.impl;

import lotus.domino.Agent;
import lotus.domino.DateTime;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.Form;
import lotus.domino.View;

public class NoteCollection extends org.openntf.domino.impl.Base<org.openntf.domino.NoteCollection, lotus.domino.NoteCollection> implements
		org.openntf.domino.NoteCollection {

	protected NoteCollection(lotus.domino.NoteCollection delegate, org.openntf.domino.Database parent) {
		super(delegate, parent);
	}

	@Override
	public void add(Agent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void add(Document arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void add(DocumentCollection arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void add(Form arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void add(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void add(int[] arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void add(lotus.domino.NoteCollection arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void add(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void add(View arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void buildCollection() {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearCollection() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getFirstNoteID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DateTime getLastBuildTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLastNoteID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNextNoteID(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getNoteIDs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public org.openntf.domino.Database getParent() {
		return (org.openntf.domino.Database) super.getParent();
	}

	@Override
	public String getPrevNoteID(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getSelectAcl() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getSelectActions() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getSelectAgents() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getSelectDatabaseScript() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getSelectDataConnections() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getSelectDocuments() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getSelectFolders() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getSelectForms() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getSelectFramesets() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getSelectHelpAbout() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getSelectHelpIndex() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getSelectHelpUsing() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getSelectIcon() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getSelectImageResources() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getSelectionFormula() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getSelectJavaResources() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getSelectMiscCodeElements() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getSelectMiscFormatElements() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getSelectMiscIndexElements() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getSelectNavigators() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getSelectOutlines() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getSelectPages() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getSelectProfiles() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getSelectReplicationFormulas() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getSelectScriptLibraries() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getSelectSharedFields() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getSelectStylesheetResources() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getSelectSubforms() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getSelectViews() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public DateTime getSinceTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUNID(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DateTime getUntilTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void intersect(lotus.domino.Agent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void intersect(Document arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void intersect(DocumentCollection arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void intersect(Form arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void intersect(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void intersect(lotus.domino.NoteCollection arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void intersect(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void intersect(View arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(lotus.domino.Agent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(Document arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(DocumentCollection arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(Form arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(lotus.domino.NoteCollection arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(View arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectAllAdminNotes(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectAllCodeElements(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectAllDataNotes(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectAllDesignElements(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectAllFormatElements(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectAllIndexElements(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectAllNotes(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectAcl(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectActions(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectAgents(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectDatabaseScript(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectDataConnections(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectDocuments(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectFolders(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectForms(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectFramesets(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectHelpAbout(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectHelpIndex(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectHelpUsing(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectIcon(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectImageResources(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectionFormula(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectJavaResources(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectMiscCodeElements(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectMiscFormatElements(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectMiscIndexElements(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectNavigators(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectOutlines(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectPages(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectProfiles(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectReplicationFormulas(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectScriptLibraries(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectSharedFields(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectStylesheetResources(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectSubforms(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectViews(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSinceTime(DateTime arg0) {
		// TODO Auto-generated method stub

	}

}

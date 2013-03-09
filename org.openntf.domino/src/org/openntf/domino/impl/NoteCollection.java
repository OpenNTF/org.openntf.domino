package org.openntf.domino.impl;

import lotus.domino.Database;
import lotus.domino.DateTime;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.Form;
import lotus.domino.View;

import org.openntf.domino.Agent;

public class NoteCollection extends org.openntf.domino.impl.Base<org.openntf.domino.NoteCollection, lotus.domino.NoteCollection> implements
		org.openntf.domino.NoteCollection {

	protected NoteCollection(lotus.domino.NoteCollection delegate) {
		super(delegate);
	}

	@Override
	public void add(Agent additionSpecifier) {
		// TODO Auto-generated method stub

	}

	@Override
	public void add(Document additionSpecifier) {
		// TODO Auto-generated method stub

	}

	@Override
	public void add(lotus.domino.Agent additionSpecifier) {
		// TODO Auto-generated method stub

	}

	@Override
	public void add(DocumentCollection additionSpecifier) {
		// TODO Auto-generated method stub

	}

	@Override
	public void add(Form additionSpecifier) {
		// TODO Auto-generated method stub

	}

	@Override
	public void add(int additionSpecifier) {
		// TODO Auto-generated method stub

	}

	@Override
	public void add(int[] additionSpecifier) {
		// TODO Auto-generated method stub

	}

	@Override
	public void add(lotus.domino.NoteCollection additionSpecifier) {
		// TODO Auto-generated method stub

	}

	@Override
	public void add(String additionSpecifier) {
		// TODO Auto-generated method stub

	}

	@Override
	public void add(View additionSpecifier) {
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
	public String getNextNoteID(String noteId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getNoteIDs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Database getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPrevNoteID(String noteId) {
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
	public String getUNID(String unid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DateTime getUntilTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void intersect(lotus.domino.Agent intersectionSpecifier) {
		// TODO Auto-generated method stub

	}

	@Override
	public void intersect(Document intersectionSpecifier) {
		// TODO Auto-generated method stub

	}

	@Override
	public void intersect(DocumentCollection intersectionSpecifier) {
		// TODO Auto-generated method stub

	}

	@Override
	public void intersect(Form intersectionSpecifier) {
		// TODO Auto-generated method stub

	}

	@Override
	public void intersect(int intersectionSpecifier) {
		// TODO Auto-generated method stub

	}

	@Override
	public void intersect(lotus.domino.NoteCollection intersectionSpecifier) {
		// TODO Auto-generated method stub

	}

	@Override
	public void intersect(String intersectionSpecifier) {
		// TODO Auto-generated method stub

	}

	@Override
	public void intersect(View intersectionSpecifier) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(lotus.domino.Agent removalSpecifier) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(Document removalSpecifier) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(DocumentCollection removalSpecifier) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(Form removalSpecifier) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(int removalSpecifier) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(lotus.domino.NoteCollection removalSpecifier) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(String removalSpecifier) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(View removalSpecifier) {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectAllAdminNotes(boolean selectorValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectAllCodeElements(boolean selectorValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectAllDataNotes(boolean selectorValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectAllDesignElements(boolean selectorValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectAllFormatElements(boolean selectorValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectAllIndexElements(boolean selectorValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectAllNotes(boolean selectorValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectAcl(boolean flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectActions(boolean flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectAgents(boolean flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectDatabaseScript(boolean flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectDataConnections(boolean flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectDocuments(boolean flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectFolders(boolean flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectForms(boolean flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectFramesets(boolean flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectHelpAbout(boolean flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectHelpIndex(boolean flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectHelpUsing(boolean flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectIcon(boolean flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectImageResources(boolean flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectionFormula(String flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectJavaResources(boolean flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectMiscCodeElements(boolean flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectMiscFormatElements(boolean flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectMiscIndexElements(boolean flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectNavigators(boolean flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectOutlines(boolean flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectPages(boolean flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectProfiles(boolean flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectReplicationFormulas(boolean flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectScriptLibraries(boolean flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectSharedFields(boolean flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectStylesheetResources(boolean flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectSubforms(boolean flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectViews(boolean flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSinceTime(DateTime sinceTime) {
		// TODO Auto-generated method stub

	}

}

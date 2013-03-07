package org.openntf.domino;

import java.util.Vector;

import lotus.domino.Database;
import lotus.domino.DateTime;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.Form;
import lotus.domino.View;

public interface NoteCollection extends Base<lotus.domino.NoteCollection>, lotus.domino.NoteCollection {

	public void add(Agent arg0);

	@Override
	public void add(lotus.domino.Document arg0);

	@Override
	public void add(lotus.domino.Agent arg0);

	@Override
	public void add(DocumentCollection arg0);

	@Override
	public void add(Form arg0);

	@Override
	public void add(int arg0);

	@Override
	public void add(int[] arg0);

	@Override
	public void add(lotus.domino.NoteCollection arg0);

	@Override
	public void add(String arg0);

	@Override
	public void add(View arg0);

	@Override
	public void buildCollection();

	@Override
	public void clearCollection();

	@Override
	public int getCount();

	@Override
	public String getFirstNoteID();

	@Override
	public DateTime getLastBuildTime();

	@Override
	public String getLastNoteID();

	@Override
	public String getNextNoteID(String arg0);

	@Override
	public int[] getNoteIDs();

	@Override
	public Database getParent();

	@Override
	public String getPrevNoteID(String arg0);

	@Override
	public boolean getSelectAcl();

	@Override
	public boolean getSelectActions();

	@Override
	public boolean getSelectAgents();

	@Override
	public boolean getSelectDatabaseScript();

	@Override
	public boolean getSelectDataConnections();

	@Override
	public boolean getSelectDocuments();

	@Override
	public boolean getSelectFolders();

	@Override
	public boolean getSelectForms();

	@Override
	public boolean getSelectFramesets();

	@Override
	public boolean getSelectHelpAbout();

	@Override
	public boolean getSelectHelpIndex();

	@Override
	public boolean getSelectHelpUsing();

	@Override
	public boolean getSelectIcon();

	@Override
	public boolean getSelectImageResources();

	@Override
	public String getSelectionFormula();

	@Override
	public boolean getSelectJavaResources();

	@Override
	public boolean getSelectMiscCodeElements();

	@Override
	public boolean getSelectMiscFormatElements();

	@Override
	public boolean getSelectMiscIndexElements();

	@Override
	public boolean getSelectNavigators();

	@Override
	public boolean getSelectOutlines();

	@Override
	public boolean getSelectPages();

	@Override
	public boolean getSelectProfiles();

	@Override
	public boolean getSelectReplicationFormulas();

	@Override
	public boolean getSelectScriptLibraries();

	@Override
	public boolean getSelectSharedFields();

	@Override
	public boolean getSelectStylesheetResources();

	@Override
	public boolean getSelectSubforms();

	@Override
	public boolean getSelectViews();

	@Override
	public DateTime getSinceTime();

	@Override
	public String getUNID(String arg0);

	@Override
	public DateTime getUntilTime();

	@Override
	public void intersect(lotus.domino.Agent arg0);

	@Override
	public void intersect(Document arg0);

	@Override
	public void intersect(DocumentCollection arg0);

	@Override
	public void intersect(Form arg0);

	@Override
	public void intersect(int arg0);

	@Override
	public void intersect(lotus.domino.NoteCollection arg0);

	@Override
	public void intersect(String arg0);

	@Override
	public void intersect(View arg0);

	@Override
	public void recycle();

	@Override
	public void recycle(Vector arg0);

	@Override
	public void remove(lotus.domino.Agent arg0);

	@Override
	public void remove(Document arg0);

	@Override
	public void remove(DocumentCollection arg0);

	@Override
	public void remove(Form arg0);

	@Override
	public void remove(int arg0);

	@Override
	public void remove(lotus.domino.NoteCollection arg0);

	@Override
	public void remove(String arg0);

	@Override
	public void remove(View arg0);

	@Override
	public void selectAllAdminNotes(boolean arg0);

	@Override
	public void selectAllCodeElements(boolean arg0);

	@Override
	public void selectAllDataNotes(boolean arg0);

	@Override
	public void selectAllDesignElements(boolean arg0);

	@Override
	public void selectAllFormatElements(boolean arg0);

	public void selectAllIndexElements(boolean arg0);

	@Override
	public void selectAllNotes(boolean arg0);

	@Override
	public void setSelectAcl(boolean arg0);

	public void setSelectActions(boolean arg0);

	public void setSelectAgents(boolean arg0);

	public void setSelectDatabaseScript(boolean arg0);

	public void setSelectDataConnections(boolean arg0);

	@Override
	public void setSelectDocuments(boolean arg0);

	@Override
	public void setSelectFolders(boolean arg0);

	@Override
	public void setSelectForms(boolean arg0);

	@Override
	public void setSelectFramesets(boolean arg0);

	@Override
	public void setSelectHelpAbout(boolean arg0);

	@Override
	public void setSelectHelpIndex(boolean arg0);

	@Override
	public void setSelectHelpUsing(boolean arg0);

	@Override
	public void setSelectIcon(boolean arg0);

	@Override
	public void setSelectImageResources(boolean arg0);

	@Override
	public void setSelectionFormula(String arg0);

	@Override
	public void setSelectJavaResources(boolean arg0);

	@Override
	public void setSelectMiscCodeElements(boolean arg0);

	@Override
	public void setSelectMiscFormatElements(boolean arg0);

	@Override
	public void setSelectMiscIndexElements(boolean arg0);

	@Override
	public void setSelectNavigators(boolean arg0);

	@Override
	public void setSelectOutlines(boolean arg0);

	@Override
	public void setSelectPages(boolean arg0);

	public void setSelectProfiles(boolean arg0);

	public void setSelectReplicationFormulas(boolean arg0);

	public void setSelectScriptLibraries(boolean arg0);

	@Override
	public void setSelectSharedFields(boolean arg0);

	@Override
	public void setSelectStylesheetResources(boolean arg0);

	public void setSelectSubforms(boolean arg0);

	public void setSelectViews(boolean arg0);

	public void setSinceTime(DateTime arg0);

	public boolean equals(Object o);

	@Override
	public int hashCode();

	@Override
	public String toString();

}

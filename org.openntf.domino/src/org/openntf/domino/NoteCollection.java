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
	public void add(lotus.domino.Document additionSpecifier);

	@Override
	public void add(lotus.domino.Agent additionSpecifier);

	@Override
	public void add(DocumentCollection additionSpecifier);

	@Override
	public void add(Form additionSpecifier);

	@Override
	public void add(int additionSpecifier);

	@Override
	public void add(int[] additionSpecifier);

	@Override
	public void add(lotus.domino.NoteCollection additionSpecifier);

	@Override
	public void add(String additionSpecifier);

	@Override
	public void add(View additionSpecifier);

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
	public String getNextNoteID(String noteId);

	@Override
	public int[] getNoteIDs();

	@Override
	public Database getParent();

	@Override
	public String getPrevNoteID(String noteId);

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
	public String getUNID(String unid);

	@Override
	public DateTime getUntilTime();

	@Override
	public void intersect(lotus.domino.Agent intersectionSpecifier);

	@Override
	public void intersect(Document intersectionSpecifier);

	@Override
	public void intersect(DocumentCollection intersectionSpecifier);

	@Override
	public void intersect(Form intersectionSpecifier);

	@Override
	public void intersect(int intersectionSpecifier);

	@Override
	public void intersect(lotus.domino.NoteCollection intersectionSpecifier);

	@Override
	public void intersect(String intersectionSpecifier);

	@Override
	public void intersect(View intersectionSpecifier);

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
	public void remove(lotus.domino.NoteCollection removalSpecifier);

	@Override
	public void remove(String removalSpecifier);

	@Override
	public void remove(View removalSpecifier);

	@Override
	public void selectAllAdminNotes(boolean selectorValue);

	@Override
	public void selectAllCodeElements(boolean selectorValue);

	@Override
	public void selectAllDataNotes(boolean selectorValue);

	@Override
	public void selectAllDesignElements(boolean selectorValue);

	@Override
	public void selectAllFormatElements(boolean selectorValue);

	public void selectAllIndexElements(boolean selectorValue);

	@Override
	public void selectAllNotes(boolean selectorValue);

	@Override
	public void setSelectAcl(boolean flag);

	public void setSelectActions(boolean flag);

	public void setSelectAgents(boolean flag);

	public void setSelectDatabaseScript(boolean flag);

	public void setSelectDataConnections(boolean flag);

	@Override
	public void setSelectDocuments(boolean flag);

	@Override
	public void setSelectFolders(boolean flag);

	@Override
	public void setSelectForms(boolean flag);

	@Override
	public void setSelectFramesets(boolean flag);

	@Override
	public void setSelectHelpAbout(boolean flag);

	@Override
	public void setSelectHelpIndex(boolean flag);

	@Override
	public void setSelectHelpUsing(boolean flag);

	@Override
	public void setSelectIcon(boolean flag);

	@Override
	public void setSelectImageResources(boolean flag);

	@Override
	public void setSelectionFormula(String flag);

	@Override
	public void setSelectJavaResources(boolean flag);

	@Override
	public void setSelectMiscCodeElements(boolean flag);

	@Override
	public void setSelectMiscFormatElements(boolean flag);

	@Override
	public void setSelectMiscIndexElements(boolean flag);

	@Override
	public void setSelectNavigators(boolean flag);

	@Override
	public void setSelectOutlines(boolean flag);

	@Override
	public void setSelectPages(boolean flag);

	public void setSelectProfiles(boolean flag);

	public void setSelectReplicationFormulas(boolean flag);

	public void setSelectScriptLibraries(boolean flag);

	@Override
	public void setSelectSharedFields(boolean flag);

	@Override
	public void setSelectStylesheetResources(boolean flag);

	public void setSelectSubforms(boolean flag);

	public void setSelectViews(boolean flag);

	public void setSinceTime(DateTime sinceTime);

	public boolean equals(Object otherCollection);

	@Override
	public int hashCode();

	@Override
	public String toString();

}

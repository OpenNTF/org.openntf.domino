package org.openntf.domino;

import lotus.domino.Database;
import lotus.domino.DateTime;
import lotus.domino.DocumentCollection;
import lotus.domino.Form;
import lotus.domino.View;

public interface NoteCollection extends Base<lotus.domino.NoteCollection>, lotus.domino.NoteCollection {

	@Override
	public void add(lotus.domino.DocumentCollection additionSpecifier);

	@Override
	public void add(lotus.domino.Form additionSpecifier);

	@Override
	public void add(int additionSpecifier);

	@Override
	public void add(int[] additionSpecifier);

	@Override
	public void add(lotus.domino.Agent additionSpecifier);

	@Override
	public void add(lotus.domino.Document additionSpecifier);

	@Override
	public void add(lotus.domino.NoteCollection additionSpecifier);

	@Override
	public void add(String noteid);

	@Override
	public void add(lotus.domino.View additionSpecifier);

	@Override
	public void buildCollection();

	@Override
	public void clearCollection();

	public boolean equals(Object otherCollection);

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
	public String getUNID(String noteid);

	@Override
	public DateTime getUntilTime();

	@Override
	public int hashCode();

	@Override
	public void intersect(lotus.domino.Document intersectionSpecifier);

	@Override
	public void intersect(lotus.domino.DocumentCollection intersectionSpecifier);

	@Override
	public void intersect(lotus.domino.Form intersectionSpecifier);

	@Override
	public void intersect(int intersectionSpecifier);

	@Override
	public void intersect(lotus.domino.Agent intersectionSpecifier);

	@Override
	public void intersect(lotus.domino.NoteCollection intersectionSpecifier);

	@Override
	public void intersect(String noteid);

	@Override
	public void intersect(lotus.domino.View intersectionSpecifier);

	@Override
	public void remove(lotus.domino.Document removalSpecifier);

	@Override
	public void remove(DocumentCollection removalSpecifier);

	@Override
	public void remove(Form removalSpecifier);

	public void remove(int removalSpecifier);

	@Override
	public void remove(lotus.domino.Agent removalSpecifier);

	@Override
	public void remove(lotus.domino.NoteCollection removalSpecifier);

	@Override
	public void remove(String noteid);

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

	@Override
	public void selectAllIndexElements(boolean selectorValue);

	@Override
	public void selectAllNotes(boolean selectorValue);

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
	public void setSelectionFormula(String formula);

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

	@Override
	public void setSelectProfiles(boolean flag);

	@Override
	public void setSelectReplicationFormulas(boolean flag);

	@Override
	public void setSelectScriptLibraries(boolean flag);

	@Override
	public void setSelectSharedFields(boolean flag);

	@Override
	public void setSelectStylesheetResources(boolean flag);

	@Override
	public void setSelectSubforms(boolean flag);

	@Override
	public void setSelectViews(boolean flag);

	@Override
	public void setSinceTime(lotus.domino.DateTime sinceTime);

	@Override
	public String toString();

}

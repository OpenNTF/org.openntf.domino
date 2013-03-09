package org.openntf.domino.impl;

import lotus.domino.Database;
import lotus.domino.DateTime;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.Form;
import lotus.domino.NotesException;
import lotus.domino.View;

import org.openntf.domino.Agent;
import org.openntf.domino.utils.DominoUtils;

public class NoteCollection extends org.openntf.domino.impl.Base<org.openntf.domino.NoteCollection, lotus.domino.NoteCollection> implements
		org.openntf.domino.NoteCollection {

	protected NoteCollection(lotus.domino.NoteCollection delegate) {
		super(delegate);
	}

	@Override
	public void add(Agent additionSpecifier) {
		try {
			getDelegate().add(additionSpecifier);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void add(Document additionSpecifier) {
		try {
			getDelegate().add(additionSpecifier);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void add(lotus.domino.Agent additionSpecifier) {
		try {
			getDelegate().add(additionSpecifier);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void add(DocumentCollection additionSpecifier) {
		try {
			getDelegate().add(additionSpecifier);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void add(Form additionSpecifier) {
		try {
			getDelegate().add(additionSpecifier);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void add(int additionSpecifier) {
		try {
			getDelegate().add(additionSpecifier);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void add(int[] additionSpecifier) {
		try {
			getDelegate().add(additionSpecifier);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void add(lotus.domino.NoteCollection additionSpecifier) {
		try {
			getDelegate().add(additionSpecifier);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void add(String additionSpecifier) {
		try {
			getDelegate().add(additionSpecifier);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void add(View additionSpecifier) {
		try {
			getDelegate().add(additionSpecifier);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void buildCollection() {
		try {
			getDelegate().buildCollection();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void clearCollection() {
		try {
			getDelegate().clearCollection();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public int getCount() {
		try {
			return getDelegate().getCount();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return 0;
	}

	@Override
	public String getFirstNoteID() {
		try {
			return getDelegate().getFirstNoteID();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public DateTime getLastBuildTime() {
		try {
			return getDelegate().getLastBuildTime();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String getLastNoteID() {
		try {
			return getDelegate().getLastNoteID();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String getNextNoteID(String noteId) {
		try {
			return getDelegate().getNextNoteID(noteId);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public int[] getNoteIDs() {
		try {
			return getDelegate().getNoteIDs();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public Database getParent() {
		try {
			return getDelegate().getParent();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String getPrevNoteID(String noteId) {
		try {
			return getDelegate().getPrevNoteID(noteId);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public boolean getSelectAcl() {
		try {
			return getDelegate().getSelectAcl();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean getSelectActions() {
		try {
			return getDelegate().getSelectActions();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean getSelectAgents() {
		try {
			return getDelegate().getSelectAgents();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean getSelectDatabaseScript() {
		try {
			return getDelegate().getSelectDatabaseScript();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean getSelectDataConnections() {
		try {
			return getDelegate().getSelectDataConnections();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean getSelectDocuments() {
		try {
			return getDelegate().getSelectDocuments();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean getSelectFolders() {
		try {
			return getDelegate().getSelectFolders();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean getSelectForms() {
		try {
			return getDelegate().getSelectForms();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean getSelectFramesets() {
		try {
			return getDelegate().getSelectFramesets();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean getSelectHelpAbout() {
		try {
			return getDelegate().getSelectHelpAbout();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean getSelectHelpIndex() {
		try {
			return getDelegate().getSelectHelpIndex();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean getSelectHelpUsing() {
		try {
			return getDelegate().getSelectHelpUsing();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean getSelectIcon() {
		try {
			return getDelegate().getSelectIcon();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean getSelectImageResources() {
		try {
			return getDelegate().getSelectImageResources();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public String getSelectionFormula() {
		try {
			return getDelegate().getSelectionFormula();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public boolean getSelectJavaResources() {
		try {
			return getDelegate().getSelectJavaResources();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean getSelectMiscCodeElements() {
		try {
			return getDelegate().getSelectMiscCodeElements();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean getSelectMiscFormatElements() {
		try {
			return getDelegate().getSelectMiscFormatElements();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean getSelectMiscIndexElements() {
		try {
			return getDelegate().getSelectMiscIndexElements();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean getSelectNavigators() {
		try {
			return getDelegate().getSelectNavigators();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean getSelectOutlines() {
		try {
			return getDelegate().getSelectOutlines();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean getSelectPages() {
		try {
			return getDelegate().getSelectPages();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean getSelectProfiles() {
		try {
			return getDelegate().getSelectProfiles();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean getSelectReplicationFormulas() {
		try {
			return getDelegate().getSelectReplicationFormulas();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
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

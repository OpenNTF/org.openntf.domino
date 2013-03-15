package org.openntf.domino.impl;

import java.util.Vector;

import lotus.domino.Agent;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.Form;
import lotus.domino.NotesException;
import lotus.domino.View;

import org.openntf.domino.DateTime;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class NoteCollection extends org.openntf.domino.impl.Base<org.openntf.domino.NoteCollection, lotus.domino.NoteCollection> implements
		org.openntf.domino.NoteCollection {
	public NoteCollection(lotus.domino.NoteCollection delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, Factory.getParentDatabase(parent));
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
	public void add(lotus.domino.Agent additionSpecifier) {
		try {
			getDelegate().add(additionSpecifier);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void add(lotus.domino.Document additionSpecifier) {
		try {
			getDelegate().add(additionSpecifier);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void add(lotus.domino.DocumentCollection additionSpecifier) {
		try {
			getDelegate().add(additionSpecifier);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void add(lotus.domino.Form additionSpecifier) {
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
	public void add(lotus.domino.View additionSpecifier) {
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
			return Factory.fromLotus(getDelegate().getLastBuildTime(), DateTime.class, this);
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
	public org.openntf.domino.Database getParent() {
		return (org.openntf.domino.Database) super.getParent();
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
		try {
			return getDelegate().getSelectScriptLibraries();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	public boolean getSelectSharedFields() {
		try {
			return getDelegate().getSelectSharedFields();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	public boolean getSelectStylesheetResources() {
		try {
			return getDelegate().getSelectStylesheetResources();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	public boolean getSelectSubforms() {
		try {
			return getDelegate().getSelectSubforms();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	public boolean getSelectViews() {
		try {
			return getDelegate().getSelectViews();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	public DateTime getSinceTime() {
		try {
			return Factory.fromLotus(getDelegate().getSinceTime(), DateTime.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public String getUNID(String unid) {
		try {
			return getDelegate().getUNID(unid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public DateTime getUntilTime() {
		try {
			return Factory.fromLotus(getDelegate().getUntilTime(), DateTime.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public void intersect(Agent agent) {
		try {
			getDelegate().intersect(agent);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void intersect(Document document) {
		try {
			getDelegate().intersect(document);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void intersect(DocumentCollection collection) {
		try {
			getDelegate().intersect(collection);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void intersect(Form form) {
		try {
			getDelegate().intersect(form);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void intersect(int noteId) {
		try {
			getDelegate().intersect(noteId);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void intersect(lotus.domino.NoteCollection collection) {
		try {
			getDelegate().intersect(collection);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void intersect(String noteId) {
		try {
			getDelegate().intersect(noteId);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void intersect(View view) {
		try {
			getDelegate().intersect(view);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void recycle() {
		try {
			getDelegate().recycle();
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void recycle(Vector objects) {
		try {
			getDelegate().recycle(objects);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void remove(Agent agent) {
		try {
			getDelegate().remove(agent);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void remove(Document document) {
		try {
			getDelegate().remove(document);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void remove(DocumentCollection collection) {
		try {
			getDelegate().remove(collection);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void remove(Form form) {
		try {
			getDelegate().remove(form);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void remove(int noteId) {
		try {
			getDelegate().remove(noteId);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void remove(lotus.domino.NoteCollection collection) {
		try {
			getDelegate().remove(collection);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void remove(String noteId) {
		try {
			getDelegate().remove(noteId);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void remove(View view) {
		try {
			getDelegate().remove(view);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void selectAllAdminNotes(boolean flag) {
		try {
			getDelegate().selectAllAdminNotes(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void selectAllCodeElements(boolean flag) {
		try {
			getDelegate().selectAllCodeElements(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void selectAllDataNotes(boolean flag) {
		try {
			getDelegate().selectAllDataNotes(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void selectAllDesignElements(boolean flag) {
		try {
			getDelegate().selectAllDesignElements(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void selectAllFormatElements(boolean flag) {
		try {
			getDelegate().selectAllFormatElements(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void selectAllIndexElements(boolean flag) {
		try {
			getDelegate().selectAllIndexElements(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void selectAllNotes(boolean flag) {
		try {
			getDelegate().selectAllNotes(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setSelectAcl(boolean flag) {
		try {
			getDelegate().setSelectAcl(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setSelectActions(boolean flag) {
		try {
			getDelegate().setSelectActions(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setSelectAgents(boolean flag) {
		try {
			getDelegate().setSelectAgents(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setSelectDatabaseScript(boolean flag) {
		try {
			getDelegate().setSelectDatabaseScript(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setSelectDataConnections(boolean flag) {
		try {
			getDelegate().setSelectDataConnections(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setSelectDocuments(boolean flag) {
		try {
			getDelegate().setSelectDocuments(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setSelectFolders(boolean flag) {
		try {
			getDelegate().setSelectFolders(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setSelectForms(boolean flag) {
		try {
			getDelegate().setSelectForms(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setSelectFramesets(boolean flag) {
		try {
			getDelegate().setSelectFramesets(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setSelectHelpAbout(boolean flag) {
		try {
			getDelegate().setSelectHelpAbout(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setSelectHelpIndex(boolean flag) {
		try {
			getDelegate().setSelectHelpIndex(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setSelectHelpUsing(boolean flag) {
		try {
			getDelegate().setSelectHelpUsing(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setSelectIcon(boolean flag) {
		try {
			getDelegate().setSelectIcon(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	public void setSelectImageResources(boolean flag) {
		try {
			getDelegate().setSelectImageResources(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	public void setSelectionFormula(String flag) {
		try {
			getDelegate().setSelectionFormula(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	public void setSelectJavaResources(boolean flag) {
		try {
			getDelegate().setSelectJavaResources(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	public void setSelectMiscCodeElements(boolean flag) {
		try {
			getDelegate().setSelectMiscCodeElements(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	public void setSelectMiscFormatElements(boolean flag) {
		try {
			getDelegate().setSelectMiscFormatElements(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setSelectMiscIndexElements(boolean flag) {
		try {
			getDelegate().setSelectMiscIndexElements(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	public void setSelectNavigators(boolean flag) {
		try {
			getDelegate().setSelectNavigators(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	public void setSelectOutlines(boolean flag) {
		try {
			getDelegate().setSelectOutlines(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	public void setSelectPages(boolean flag) {
		try {
			getDelegate().setSelectPages(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	public void setSelectProfiles(boolean flag) {
		try {
			getDelegate().setSelectProfiles(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	public void setSelectReplicationFormulas(boolean flag) {
		try {
			getDelegate().setSelectReplicationFormulas(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	public void setSelectScriptLibraries(boolean flag) {
		try {
			getDelegate().setSelectScriptLibraries(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	public void setSelectSharedFields(boolean flag) {
		try {
			getDelegate().setSelectSharedFields(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	public void setSelectStylesheetResources(boolean flag) {
		try {
			getDelegate().setSelectStylesheetResources(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	public void setSelectSubforms(boolean flag) {
		try {
			getDelegate().setSelectSubforms(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	public void setSelectViews(boolean flag) {
		try {
			getDelegate().setSelectViews(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	public void setSinceTime(lotus.domino.DateTime date) {
		try {
			getDelegate().setSinceTime(date);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

}

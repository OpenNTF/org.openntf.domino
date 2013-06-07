/**
 * 
 */
package org.openntf.domino.design.impl;

import java.util.EnumSet;
import java.util.Set;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.NoteCollection;
import org.openntf.domino.NoteCollection.SelectOption;
import org.openntf.domino.utils.DominoUtils;

/**
 * @author jgallagher
 * 
 */
public class DatabaseDesign implements org.openntf.domino.design.DatabaseDesign {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(DatabaseDesign.class.getName());
	private static final long serialVersionUID = 1L;

	/*
	 * Some handy constant Note IDs for getting specific elements. h/t http://www.nsftools.com/tips/NotesTips.htm#defaultelements
	 */
	private static final String ABOUT_NOTE = "FFFF0002";
	private static final String DEFAULT_FORM = "FFFF0004";
	private static final String DEFAULT_VIEW = "FFFF0008";
	private static final String ICON_NOTE = "FFFF0010";
	private static final String DESIGN_COLLECTION = "FFFF0020";
	private static final String ACL_NOTE = "FFFF0040";
	private static final String USING_NOTE = "FFFF0100";
	private static final String REPLICATION_FORMULA = "FFFF0800";

	private final Database database_;

	public DatabaseDesign(final Database database) {
		database_ = database;
	}

	@Override
	public AboutDocument getAboutDocument() {
		Document doc = database_.getDocumentByID(ABOUT_NOTE);
		if (doc != null) {
			return new AboutDocument(doc);
		}
		return null;
	}

	@Override
	public ACLNote getACL() {
		return new ACLNote(database_.getDocumentByID(ACL_NOTE));
	}

	@Override
	public DesignForm getDefaultForm() {
		Document formDoc = database_.getDocumentByID(DEFAULT_FORM);
		if (formDoc != null) {
			return new DesignForm(formDoc);
		}
		return null;
	}

	@Override
	public DesignView getDefaultView() {
		Document viewDoc = database_.getDocumentByID(DEFAULT_VIEW);
		if (viewDoc != null) {
			return new DesignView(viewDoc);
		}
		return null;
	}

	@Override
	public FileResource getFileResource(final String name) {
		NoteCollection notes = getNoteCollection(String.format(
				" !@Contains($Flags; '~') & @Contains($Flags; 'g') & @Explode($TITLE; '|')=\"%s\" ", DominoUtils
						.escapeForFormulaString(name)), EnumSet.of(SelectOption.MISC_FORMAT));

		String noteId = notes.getFirstNoteID();
		if (!noteId.isEmpty()) {
			Document doc = database_.getDocumentByID(noteId);
			return new FileResource(doc);
		}
		return null;
	}

	@Override
	public DesignCollection<org.openntf.domino.design.FileResource> getFileResources() {
		NoteCollection notes = getNoteCollection(" !@Contains($Flags; '~') & @Contains($Flags; 'g') ", EnumSet.of(SelectOption.MISC_FORMAT));
		return new DesignCollection<org.openntf.domino.design.FileResource>(notes, FileResource.class);
	}

	@Override
	public FileResource getHiddenFileResource(final String name) {
		NoteCollection notes = getNoteCollection(String.format(
				" @Contains($Flags; '~') & @Contains($Flags; 'g') & !@Contains($Flags; 'K':';':'[':',') & @Explode($TITLE; '|')=\"%s\" ",
				DominoUtils.escapeForFormulaString(name)), EnumSet.of(SelectOption.MISC_FORMAT));

		String noteId = notes.getFirstNoteID();
		if (!noteId.isEmpty()) {
			Document doc = database_.getDocumentByID(noteId);
			return new FileResource(doc);
		}
		return null;
	}

	@Override
	public DesignCollection<org.openntf.domino.design.FileResource> getHiddenFileResources() {
		NoteCollection notes = getNoteCollection(" @Contains($Flags; '~') & @Contains($Flags; 'g') & !@Contains($Flags; 'K':';':'[':',')",
				EnumSet.of(SelectOption.MISC_FORMAT));
		return new DesignCollection<org.openntf.domino.design.FileResource>(notes, FileResource.class);
	}

	@Override
	public DesignForm getForm(String name) {
		// TODO Check if this returns subforms
		NoteCollection notes = getNoteCollection(String.format(" @Explode($TITLE; '|')=\"%s\" ", DominoUtils.escapeForFormulaString(name)),
				EnumSet.of(SelectOption.FORMS));

		String noteId = notes.getFirstNoteID();
		if (!noteId.isEmpty()) {
			Document doc = database_.getDocumentByID(noteId);
			return new DesignForm(doc);
		}
		return null;
	}

	@Override
	public DesignCollection<org.openntf.domino.design.DesignForm> getForms() {
		NoteCollection notes = getNoteCollection(" @All ", EnumSet.of(SelectOption.FORMS));
		return new DesignCollection<org.openntf.domino.design.DesignForm>(notes, DesignForm.class);
	}

	@Override
	public IconNote getIconNote() {
		return new IconNote(database_.getDocumentByID(ICON_NOTE));
	}

	@Override
	public ReplicationFormula getReplicationFormula() {
		Document repNote = database_.getDocumentByID(REPLICATION_FORMULA);
		if (repNote != null) {
			return new ReplicationFormula(repNote);
		}
		return null;
	}

	@Override
	public UsingDocument getUsingDocument() {
		Document doc = database_.getDocumentByID(USING_NOTE);
		if (doc != null) {
			return new UsingDocument(doc);
		}
		return null;
	}

	@Override
	public org.openntf.domino.design.DesignView getView(String name) {
		// TODO Check if this returns folders
		NoteCollection notes = getNoteCollection(String.format(" @Explode($TITLE; '|')=\"%s\" ", DominoUtils.escapeForFormulaString(name)),
				EnumSet.of(SelectOption.VIEWS));

		String noteId = notes.getFirstNoteID();
		if (!noteId.isEmpty()) {
			Document doc = database_.getDocumentByID(noteId);
			return new DesignView(doc);
		}
		return null;
	}

	@Override
	public DesignCollection<org.openntf.domino.design.DesignView> getViews() {
		NoteCollection notes = getNoteCollection(" @All ", EnumSet.of(SelectOption.VIEWS));
		return new DesignCollection<org.openntf.domino.design.DesignView>(notes, DesignView.class);
	}

	private NoteCollection getNoteCollection(final String selectionFormula, final Set<SelectOption> options) {
		NoteCollection notes = database_.createNoteCollection(false);
		notes.setSelectOptions(options);
		notes.setSelectionFormula(selectionFormula);
		notes.buildCollection();
		return notes;
	}

}
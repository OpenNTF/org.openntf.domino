/**
 * 
 */
package org.openntf.domino.design.impl;

import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.NoteCollection;
import org.openntf.domino.design.DesignCollection;
import org.openntf.domino.design.FileResource;
import org.openntf.domino.utils.DominoUtils;

/**
 * @author jgallagher
 * 
 */
public class DatabaseDesign implements org.openntf.domino.design.DatabaseDesign {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(DatabaseDesign.class.getName());
	private static final long serialVersionUID = 1L;

	private final Database database_;

	public DatabaseDesign(final Database database) {
		database_ = database;
	}

	@Override
	public FileResource getFileResource(final String name) {
		NoteCollection notes = database_.createNoteCollection(false);
		notes.setSelectMiscFormatElements(true);
		notes.setSelectionFormula(String.format(" !@Contains($Flags; '~') & @Contains($Flags; 'g') & @Explode($TITLE; '|')=\"%s\" ",
				DominoUtils.escapeForFormulaString(name)));
		notes.buildCollection();

		String noteId = notes.getFirstNoteID();
		if (!noteId.isEmpty()) {
			Document resourceDoc = database_.getDocumentByID(noteId);
			return new org.openntf.domino.design.impl.FileResource(resourceDoc);
		}
		return null;
	}

	@Override
	public DesignCollection<FileResource> getFileResources() {
		NoteCollection notes = database_.createNoteCollection(false);
		notes.setSelectMiscFormatElements(true);
		notes.setSelectionFormula(" !@Contains($Flags; '~') & @Contains($Flags; 'g') ");
		notes.buildCollection();
		return new org.openntf.domino.design.impl.DesignCollection<FileResource>(notes, org.openntf.domino.design.impl.FileResource.class);
	}
}

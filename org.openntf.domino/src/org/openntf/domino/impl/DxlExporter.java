package org.openntf.domino.impl;

import java.util.Vector;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.NoteCollection;
import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;

public class DxlExporter extends Base<org.openntf.domino.DxlExporter, lotus.domino.DxlExporter> implements org.openntf.domino.DxlExporter {

	public DxlExporter(lotus.domino.DxlExporter delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	@Override
	public String exportDxl(Document doc) {
		try {
			return getDelegate().exportDxl(doc);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String exportDxl(Database db) {
		try {
			return getDelegate().exportDxl(db);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String exportDxl(DocumentCollection docs) {
		try {
			return getDelegate().exportDxl(docs);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String exportDxl(NoteCollection notes) {
		try {
			return getDelegate().exportDxl(notes);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getAttachmentOmittedText() {
		try {
			return getDelegate().getAttachmentOmittedText();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public boolean getConvertNotesBitmapsToGIF() {
		try {
			return getDelegate().getConvertNotesBitmapsToGIF();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public String getDoctypeSYSTEM() {
		try {
			return getDelegate().getDoctypeSYSTEM();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public boolean getExitOnFirstFatalError() {
		try {
			return getDelegate().getExitOnFirstFatalError();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean getForceNoteFormat() {
		try {
			return getDelegate().getForceNoteFormat();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public String getLog() {
		try {
			return getDelegate().getLog();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getLogComment() {
		try {
			return getDelegate().getLogComment();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public int getMIMEOption() {
		try {
			return getDelegate().getMIMEOption();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public String getOLEObjectOmittedText() {
		try {
			return getDelegate().getOLEObjectOmittedText();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public Vector getOmitItemNames() {
		try {
			return getDelegate().getOmitItemNames();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public boolean getOmitMiscFileObjects() {
		try {
			return getDelegate().getOmitMiscFileObjects();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean getOmitOLEObjects() {
		try {
			return getDelegate().getOmitOLEObjects();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean getOmitRichtextAttachments() {
		try {
			return getDelegate().getOmitRichtextAttachments();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean getOmitRichtextPictures() {
		try {
			return getDelegate().getOmitRichtextPictures();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean getOutputDOCTYPE() {
		try {
			return getDelegate().getOutputDOCTYPE();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public String getPictureOmittedText() {
		try {
			return getDelegate().getPictureOmittedText();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public Vector getRestrictToItemNames() {
		try {
			return getDelegate().getRestrictToItemNames();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public int getRichTextOption() {
		try {
			return getDelegate().getRichTextOption();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public boolean getUncompressAttachments() {
		try {
			return getDelegate().getUncompressAttachments();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public void setAttachmentOmittedText(String replacementText) {
		try {
			getDelegate().setAttachmentOmittedText(replacementText);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setConvertNotesBitmapsToGIF(boolean flag) {
		try {
			getDelegate().setConvertNotesBitmapsToGIF(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setDoctypeSYSTEM(String system) {
		try {
			getDelegate().setDoctypeSYSTEM(system);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setExitOnFirstFatalError(boolean flag) {
		try {
			getDelegate().setExitOnFirstFatalError(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setForceNoteFormat(boolean flag) {
		try {
			getDelegate().setForceNoteFormat(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setLogComment(String comment) {
		try {
			getDelegate().setLogComment(comment);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setMIMEOption(int option) {
		try {
			getDelegate().setMIMEOption(option);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setOLEObjectOmittedText(String replacementText) {
		try {
			getDelegate().setOLEObjectOmittedText(replacementText);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setOmitItemNames(Vector names) {
		try {
			getDelegate().setOmitItemNames(names);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setOmitMiscFileObjects(boolean flag) {
		try {
			getDelegate().setOmitMiscFileObjects(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setOmitOLEObjects(boolean flag) {
		try {
			getDelegate().setOmitOLEObjects(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setOmitRichtextAttachments(boolean flag) {
		try {
			getDelegate().setOmitRichtextAttachments(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setOmitRichtextPictures(boolean flag) {
		try {
			getDelegate().setOmitRichtextPictures(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setOutputDOCTYPE(boolean flag) {
		try {
			getDelegate().setOutputDOCTYPE(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setPictureOmittedText(String replacementText) {
		try {
			getDelegate().setPictureOmittedText(replacementText);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setRestrictToItemNames(Vector names) {
		try {
			getDelegate().setRestrictToItemNames(names);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setRichTextOption(int option) {
		try {
			getDelegate().setRichTextOption(option);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setUncompressAttachments(boolean flag) {
		try {
			getDelegate().setUncompressAttachments(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}
}

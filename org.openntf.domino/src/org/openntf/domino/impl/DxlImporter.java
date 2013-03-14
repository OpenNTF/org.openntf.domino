package org.openntf.domino.impl;

import lotus.domino.Database;
import lotus.domino.NotesException;
import lotus.domino.RichTextItem;
import lotus.domino.Stream;

import org.openntf.domino.utils.DominoUtils;

public class DxlImporter extends Base<org.openntf.domino.DxlImporter, lotus.domino.DxlImporter> implements org.openntf.domino.DxlImporter {

	protected DxlImporter(lotus.domino.DxlImporter delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	@Override
	public int getAclImportOption() {
		try {
			return getDelegate().getAclImportOption();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public boolean getCompileLotusScript() {
		try {
			return getDelegate().getCompileLotusScript();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean getCreateFTIndex() {
		try {
			return getDelegate().getCreateFTIndex();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public int getDesignImportOption() {
		try {
			return getDelegate().getDesignImportOption();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public int getDocumentImportOption() {
		try {
			return getDelegate().getDocumentImportOption();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
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
	public String getFirstImportedNoteID() {
		try {
			return getDelegate().getFirstImportedNoteID();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public int getImportedNoteCount() {
		try {
			return getDelegate().getImportedNoteCount();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public int getInputValidationOption() {
		try {
			return getDelegate().getInputValidationOption();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
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
	public String getNextImportedNoteID(String noteid) {
		try {
			return getDelegate().getNextImportedNoteID(noteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public boolean getReplaceDbProperties() {
		try {
			return getDelegate().getReplaceDbProperties();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean getReplicaRequiredForReplaceOrUpdate() {
		try {
			return getDelegate().getReplicaRequiredForReplaceOrUpdate();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public int getUnknownTokenLogOption() {
		try {
			return getDelegate().getUnknownTokenLogOption();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public void importDxl(RichTextItem rtitem, Database db) {
		try {
			getDelegate().importDxl(rtitem, db);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void importDxl(Stream stream, Database db) {
		try {
			getDelegate().importDxl(stream, db);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void importDxl(String dxl, Database db) {
		try {
			getDelegate().importDxl(dxl, db);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setAclImportOption(int option) {
		try {
			getDelegate().setAclImportOption(option);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setCompileLotusScript(boolean flag) {
		try {
			getDelegate().setCompileLotusScript(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setCreateFTIndex(boolean flag) {
		try {
			getDelegate().setCreateFTIndex(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setDesignImportOption(int option) {
		try {
			getDelegate().setDesignImportOption(option);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setDocumentImportOption(int option) {
		try {
			getDelegate().setDocumentImportOption(option);
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
	public void setInputValidationOption(int option) {
		try {
			getDelegate().setInputValidationOption(option);
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
	public void setReplaceDbProperties(boolean flag) {
		try {
			getDelegate().setReplaceDbProperties(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setReplicaRequiredForReplaceOrUpdate(boolean flag) {
		try {
			getDelegate().setReplicaRequiredForReplaceOrUpdate(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setUnknownTokenLogOption(int option) {
		try {
			getDelegate().setUnknownTokenLogOption(option);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

}

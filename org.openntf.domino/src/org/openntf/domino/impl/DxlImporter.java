/*
 * Copyright OpenNTF 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package org.openntf.domino.impl;

import lotus.domino.Database;
import lotus.domino.NotesException;
import lotus.domino.RichTextItem;
import lotus.domino.Stream;

import org.openntf.domino.utils.DominoUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class DxlImporter.
 */
public class DxlImporter extends Base<org.openntf.domino.DxlImporter, lotus.domino.DxlImporter> implements org.openntf.domino.DxlImporter {

	/**
	 * Instantiates a new dxl importer.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	public DxlImporter(lotus.domino.DxlImporter delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.DxlImporter#getAclImportOption()
	 */
	@Override
	public int getAclImportOption() {
		try {
			return getDelegate().getAclImportOption();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.DxlImporter#getCompileLotusScript()
	 */
	@Override
	public boolean getCompileLotusScript() {
		try {
			return getDelegate().getCompileLotusScript();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.DxlImporter#getCreateFTIndex()
	 */
	@Override
	public boolean getCreateFTIndex() {
		try {
			return getDelegate().getCreateFTIndex();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.DxlImporter#getDesignImportOption()
	 */
	@Override
	public int getDesignImportOption() {
		try {
			return getDelegate().getDesignImportOption();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.DxlImporter#getDocumentImportOption()
	 */
	@Override
	public int getDocumentImportOption() {
		try {
			return getDelegate().getDocumentImportOption();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.DxlImporter#getExitOnFirstFatalError()
	 */
	@Override
	public boolean getExitOnFirstFatalError() {
		try {
			return getDelegate().getExitOnFirstFatalError();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.DxlImporter#getFirstImportedNoteID()
	 */
	@Override
	public String getFirstImportedNoteID() {
		try {
			return getDelegate().getFirstImportedNoteID();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.DxlImporter#getImportedNoteCount()
	 */
	@Override
	public int getImportedNoteCount() {
		try {
			return getDelegate().getImportedNoteCount();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.DxlImporter#getInputValidationOption()
	 */
	@Override
	public int getInputValidationOption() {
		try {
			return getDelegate().getInputValidationOption();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.DxlImporter#getLog()
	 */
	@Override
	public String getLog() {
		try {
			return getDelegate().getLog();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.DxlImporter#getLogComment()
	 */
	@Override
	public String getLogComment() {
		try {
			return getDelegate().getLogComment();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.DxlImporter#getNextImportedNoteID(java.lang.String)
	 */
	@Override
	public String getNextImportedNoteID(String noteid) {
		try {
			return getDelegate().getNextImportedNoteID(noteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.DxlImporter#getReplaceDbProperties()
	 */
	@Override
	public boolean getReplaceDbProperties() {
		try {
			return getDelegate().getReplaceDbProperties();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.DxlImporter#getReplicaRequiredForReplaceOrUpdate()
	 */
	@Override
	public boolean getReplicaRequiredForReplaceOrUpdate() {
		try {
			return getDelegate().getReplicaRequiredForReplaceOrUpdate();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.DxlImporter#getUnknownTokenLogOption()
	 */
	@Override
	public int getUnknownTokenLogOption() {
		try {
			return getDelegate().getUnknownTokenLogOption();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.DxlImporter#importDxl(lotus.domino.RichTextItem, lotus.domino.Database)
	 */
	@Override
	public void importDxl(RichTextItem rtitem, Database db) {
		try {
			getDelegate().importDxl(rtitem, db);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.DxlImporter#importDxl(lotus.domino.Stream, lotus.domino.Database)
	 */
	@Override
	public void importDxl(Stream stream, Database db) {
		try {
			getDelegate().importDxl(stream, db);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.DxlImporter#importDxl(java.lang.String, lotus.domino.Database)
	 */
	@Override
	public void importDxl(String dxl, Database db) {
		try {
			getDelegate().importDxl(dxl, db);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.DxlImporter#setAclImportOption(int)
	 */
	@Override
	public void setAclImportOption(int option) {
		try {
			getDelegate().setAclImportOption(option);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.DxlImporter#setCompileLotusScript(boolean)
	 */
	@Override
	public void setCompileLotusScript(boolean flag) {
		try {
			getDelegate().setCompileLotusScript(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.DxlImporter#setCreateFTIndex(boolean)
	 */
	@Override
	public void setCreateFTIndex(boolean flag) {
		try {
			getDelegate().setCreateFTIndex(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.DxlImporter#setDesignImportOption(int)
	 */
	@Override
	public void setDesignImportOption(int option) {
		try {
			getDelegate().setDesignImportOption(option);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.DxlImporter#setDocumentImportOption(int)
	 */
	@Override
	public void setDocumentImportOption(int option) {
		try {
			getDelegate().setDocumentImportOption(option);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.DxlImporter#setExitOnFirstFatalError(boolean)
	 */
	@Override
	public void setExitOnFirstFatalError(boolean flag) {
		try {
			getDelegate().setExitOnFirstFatalError(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.DxlImporter#setInputValidationOption(int)
	 */
	@Override
	public void setInputValidationOption(int option) {
		try {
			getDelegate().setInputValidationOption(option);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.DxlImporter#setLogComment(java.lang.String)
	 */
	@Override
	public void setLogComment(String comment) {
		try {
			getDelegate().setLogComment(comment);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.DxlImporter#setReplaceDbProperties(boolean)
	 */
	@Override
	public void setReplaceDbProperties(boolean flag) {
		try {
			getDelegate().setReplaceDbProperties(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.DxlImporter#setReplicaRequiredForReplaceOrUpdate(boolean)
	 */
	@Override
	public void setReplicaRequiredForReplaceOrUpdate(boolean flag) {
		try {
			getDelegate().setReplicaRequiredForReplaceOrUpdate(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.DxlImporter#setUnknownTokenLogOption(int)
	 */
	@Override
	public void setUnknownTokenLogOption(int option) {
		try {
			getDelegate().setUnknownTokenLogOption(option);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

}

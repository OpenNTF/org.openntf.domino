/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.impl;

import lotus.domino.NotesException;

import org.openntf.domino.Session;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.utils.DominoUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class DxlImporter.
 */
public class DxlImporter extends BaseThreadSafe<org.openntf.domino.DxlImporter, lotus.domino.DxlImporter, Session> implements
org.openntf.domino.DxlImporter {

	/**
	 * Instantiates a new DxlImporter.
	 *
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	protected DxlImporter(final lotus.domino.DxlImporter delegate, final Session parent) {
		super(delegate, parent, NOTES_DXLIMPORTER);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DxlImporter#getAclImportOption()
	 */
	@Override
	public int getAclImportOption() {
		try {
			return getDelegate().getAclImportOption();
		} catch (NotesException e) {
			DominoUtils.handleException(e, getLog());
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DxlImporter#getCompileLotusScript()
	 */
	@Override
	public boolean getCompileLotusScript() {
		try {
			return getDelegate().getCompileLotusScript();
		} catch (NotesException e) {
			DominoUtils.handleException(e, getLog());
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DxlImporter#getCreateFTIndex()
	 */
	@Override
	public boolean getCreateFTIndex() {
		try {
			return getDelegate().getCreateFTIndex();
		} catch (NotesException e) {
			DominoUtils.handleException(e, getLog());
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DxlImporter#getDesignImportOption()
	 */
	@Override
	public int getDesignImportOption() {
		try {
			return getDelegate().getDesignImportOption();
		} catch (NotesException e) {
			DominoUtils.handleException(e, getLog());
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DxlImporter#getDocumentImportOption()
	 */
	@Override
	public int getDocumentImportOption() {
		try {
			return getDelegate().getDocumentImportOption();
		} catch (NotesException e) {
			DominoUtils.handleException(e, getLog());
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DxlImporter#getExitOnFirstFatalError()
	 */
	@Override
	public boolean getExitOnFirstFatalError() {
		try {
			return getDelegate().getExitOnFirstFatalError();
		} catch (NotesException e) {
			DominoUtils.handleException(e, getLog());
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DxlImporter#getFirstImportedNoteID()
	 */
	@Override
	public String getFirstImportedNoteID() {
		try {
			return getDelegate().getFirstImportedNoteID();
		} catch (NotesException e) {
			DominoUtils.handleException(e, getLog());
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DxlImporter#getImportedNoteCount()
	 */
	@Override
	public int getImportedNoteCount() {
		try {
			return getDelegate().getImportedNoteCount();
		} catch (NotesException e) {
			DominoUtils.handleException(e, getLog());
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DxlImporter#getInputValidationOption()
	 */
	@Override
	public int getInputValidationOption() {
		try {
			return getDelegate().getInputValidationOption();
		} catch (NotesException e) {
			DominoUtils.handleException(e, getLog());
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 *
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

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DxlImporter#getLogComment()
	 */
	@Override
	public String getLogComment() {
		try {
			return getDelegate().getLogComment();
		} catch (NotesException e) {
			DominoUtils.handleException(e, getLog());
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DxlImporter#getNextImportedNoteID(java.lang.String)
	 */
	@Override
	public String getNextImportedNoteID(final String noteid) {
		try {
			return getDelegate().getNextImportedNoteID(noteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e, getLog());
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DxlImporter#getReplaceDbProperties()
	 */
	@Override
	public boolean getReplaceDbProperties() {
		try {
			return getDelegate().getReplaceDbProperties();
		} catch (NotesException e) {
			DominoUtils.handleException(e, getLog());
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DxlImporter#getReplicaRequiredForReplaceOrUpdate()
	 */
	@Override
	public boolean getReplicaRequiredForReplaceOrUpdate() {
		try {
			return getDelegate().getReplicaRequiredForReplaceOrUpdate();
		} catch (NotesException e) {
			DominoUtils.handleException(e, getLog());
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DxlImporter#getUnknownTokenLogOption()
	 */
	@Override
	public int getUnknownTokenLogOption() {
		try {
			return getDelegate().getUnknownTokenLogOption();
		} catch (NotesException e) {
			DominoUtils.handleException(e, getLog());
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DxlImporter#importDxl(lotus.domino.RichTextItem, lotus.domino.Database)
	 */
	@Override
	public void importDxl(final lotus.domino.RichTextItem rtitem, final lotus.domino.Database db) {
		try {
			getDelegate().importDxl(toLotus(rtitem), toLotus(db));
		} catch (NotesException e) {
			//			DominoUtils.handleException(e, getLog());
			DominoUtils.handleException(new Exception(getLog(), e));
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DxlImporter#importDxl(lotus.domino.Stream, lotus.domino.Database)
	 */
	@Override
	public void importDxl(final lotus.domino.Stream stream, final lotus.domino.Database db) {
		try {
			getDelegate().importDxl(toLotus(stream), toLotus(db));
		} catch (NotesException e) {
			//			DominoUtils.handleException(e, getLog());
			DominoUtils.handleException(new Exception(getLog(), e));
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DxlImporter#importDxl(java.lang.String, lotus.domino.Database)
	 */
	@Override
	public void importDxl(final String dxl, final lotus.domino.Database db) {
		try {
			getDelegate().importDxl(dxl, toLotus(db));
		} catch (NotesException e) {
			//			DominoUtils.handleException(e, getLog());
			DominoUtils.handleException(new Exception(getLog(), e));
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DxlImporter#setAclImportOption(int)
	 */
	@Override
	public void setAclImportOption(final int option) {
		try {
			getDelegate().setAclImportOption(option);
		} catch (NotesException e) {
			DominoUtils.handleException(e, getLog());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DxlImporter#setCompileLotusScript(boolean)
	 */
	@Override
	public void setCompileLotusScript(final boolean flag) {
		try {
			getDelegate().setCompileLotusScript(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e, getLog());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DxlImporter#setCreateFTIndex(boolean)
	 */
	@Override
	public void setCreateFTIndex(final boolean flag) {
		try {
			getDelegate().setCreateFTIndex(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e, getLog());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DxlImporter#setDesignImportOption(int)
	 */
	@Override
	public void setDesignImportOption(final int option) {
		try {
			getDelegate().setDesignImportOption(option);
		} catch (NotesException e) {
			DominoUtils.handleException(e, getLog());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DxlImporter#setDocumentImportOption(int)
	 */
	@Override
	public void setDocumentImportOption(final int option) {
		try {
			getDelegate().setDocumentImportOption(option);
		} catch (NotesException e) {
			DominoUtils.handleException(e, getLog());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DxlImporter#setExitOnFirstFatalError(boolean)
	 */
	@Override
	public void setExitOnFirstFatalError(final boolean flag) {
		try {
			getDelegate().setExitOnFirstFatalError(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e, getLog());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DxlImporter#setInputValidationOption(int)
	 */
	@Override
	public void setInputValidationOption(final int option) {
		try {
			getDelegate().setInputValidationOption(option);
		} catch (NotesException e) {
			DominoUtils.handleException(e, getLog());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DxlImporter#setLogComment(java.lang.String)
	 */
	@Override
	public void setLogComment(final String comment) {
		try {
			getDelegate().setLogComment(comment);
		} catch (NotesException e) {
			DominoUtils.handleException(e, getLog());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DxlImporter#setReplaceDbProperties(boolean)
	 */
	@Override
	public void setReplaceDbProperties(final boolean flag) {
		try {
			getDelegate().setReplaceDbProperties(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e, getLog());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DxlImporter#setReplicaRequiredForReplaceOrUpdate(boolean)
	 */
	@Override
	public void setReplicaRequiredForReplaceOrUpdate(final boolean flag) {
		try {
			getDelegate().setReplicaRequiredForReplaceOrUpdate(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e, getLog());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DxlImporter#setUnknownTokenLogOption(int)
	 */
	@Override
	public void setUnknownTokenLogOption(final int option) {
		try {
			getDelegate().setUnknownTokenLogOption(option);
		} catch (NotesException e) {
			DominoUtils.handleException(e, getLog());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public final Session getAncestorSession() {
		return parent;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.ext.DxlImporter#setAclImportOption(org.openntf.domino.DxlImporter.AclImportOption)
	 */
	@Override
	public void setAclImportOption(final AclImportOption option) {
		setAclImportOption(option.getValue());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.ext.DxlImporter#setDesignImportOption(org.openntf.domino.DxlImporter.DesignImportOption)
	 */
	@Override
	public void setDesignImportOption(final DesignImportOption option) {
		setDesignImportOption(option.getValue());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.ext.DxlImporter#setDocumentImportOption(org.openntf.domino.DxlImporter.DocumentImportOption)
	 */
	@Override
	public void setDocumentImportOption(final DocumentImportOption option) {
		setDocumentImportOption(option.getValue());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.ext.DxlImporter#setInputValidationOption(org.openntf.domino.DxlImporter.InputValidationOption)
	 */
	@Override
	public void setInputValidationOption(final InputValidationOption option) {
		setInputValidationOption(option.getValue());
	}

	@Override
	protected WrapperFactory getFactory() {
		return parent.getFactory();
	}

}

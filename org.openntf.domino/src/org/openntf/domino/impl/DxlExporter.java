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

import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.Session;
import org.openntf.domino.utils.DominoUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class DxlExporter.
 */
public class DxlExporter extends Base<org.openntf.domino.DxlExporter, lotus.domino.DxlExporter> implements org.openntf.domino.DxlExporter {

	/**
	 * Instantiates a new dxl exporter.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	public DxlExporter(lotus.domino.DxlExporter delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#exportDxl(lotus.domino.Document)
	 */
	@Override
	public String exportDxl(lotus.domino.Document doc) {
		try {
			return getDelegate().exportDxl((lotus.domino.Document) toLotus(doc));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#exportDxl(lotus.domino.Database)
	 */
	@Override
	public String exportDxl(lotus.domino.Database db) {
		try {
			return getDelegate().exportDxl((lotus.domino.Database) toLotus(db));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#exportDxl(lotus.domino.DocumentCollection)
	 */
	@Override
	public String exportDxl(lotus.domino.DocumentCollection docs) {
		try {
			return getDelegate().exportDxl((lotus.domino.DocumentCollection) toLotus(docs));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#exportDxl(lotus.domino.NoteCollection)
	 */
	@Override
	public String exportDxl(lotus.domino.NoteCollection notes) {
		try {
			return getDelegate().exportDxl((lotus.domino.NoteCollection) toLotus(notes));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#getAttachmentOmittedText()
	 */
	@Override
	public String getAttachmentOmittedText() {
		try {
			return getDelegate().getAttachmentOmittedText();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#getConvertNotesBitmapsToGIF()
	 */
	@Override
	public boolean getConvertNotesBitmapsToGIF() {
		try {
			return getDelegate().getConvertNotesBitmapsToGIF();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#getDoctypeSYSTEM()
	 */
	@Override
	public String getDoctypeSYSTEM() {
		try {
			return getDelegate().getDoctypeSYSTEM();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#getExitOnFirstFatalError()
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#getForceNoteFormat()
	 */
	@Override
	public boolean getForceNoteFormat() {
		try {
			return getDelegate().getForceNoteFormat();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#getLog()
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
	 * @see org.openntf.domino.DxlExporter#getLogComment()
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#getMIMEOption()
	 */
	@Override
	public int getMIMEOption() {
		try {
			return getDelegate().getMIMEOption();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#getOLEObjectOmittedText()
	 */
	@Override
	public String getOLEObjectOmittedText() {
		try {
			return getDelegate().getOLEObjectOmittedText();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#getOmitItemNames()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Vector<String> getOmitItemNames() {
		try {
			return getDelegate().getOmitItemNames();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#getOmitMiscFileObjects()
	 */
	@Override
	public boolean getOmitMiscFileObjects() {
		try {
			return getDelegate().getOmitMiscFileObjects();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#getOmitOLEObjects()
	 */
	@Override
	public boolean getOmitOLEObjects() {
		try {
			return getDelegate().getOmitOLEObjects();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#getOmitRichtextAttachments()
	 */
	@Override
	public boolean getOmitRichtextAttachments() {
		try {
			return getDelegate().getOmitRichtextAttachments();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#getOmitRichtextPictures()
	 */
	@Override
	public boolean getOmitRichtextPictures() {
		try {
			return getDelegate().getOmitRichtextPictures();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#getOutputDOCTYPE()
	 */
	@Override
	public boolean getOutputDOCTYPE() {
		try {
			return getDelegate().getOutputDOCTYPE();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#getPictureOmittedText()
	 */
	@Override
	public String getPictureOmittedText() {
		try {
			return getDelegate().getPictureOmittedText();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#getRestrictToItemNames()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Vector<String> getRestrictToItemNames() {
		try {
			return getDelegate().getRestrictToItemNames();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#getRichTextOption()
	 */
	@Override
	public int getRichTextOption() {
		try {
			return getDelegate().getRichTextOption();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#getUncompressAttachments()
	 */
	@Override
	public boolean getUncompressAttachments() {
		try {
			return getDelegate().getUncompressAttachments();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#setAttachmentOmittedText(java.lang.String)
	 */
	@Override
	public void setAttachmentOmittedText(String replacementText) {
		try {
			getDelegate().setAttachmentOmittedText(replacementText);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#setConvertNotesBitmapsToGIF(boolean)
	 */
	@Override
	public void setConvertNotesBitmapsToGIF(boolean flag) {
		try {
			getDelegate().setConvertNotesBitmapsToGIF(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#setDoctypeSYSTEM(java.lang.String)
	 */
	@Override
	public void setDoctypeSYSTEM(String system) {
		try {
			getDelegate().setDoctypeSYSTEM(system);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#setExitOnFirstFatalError(boolean)
	 */
	@Override
	public void setExitOnFirstFatalError(boolean flag) {
		try {
			getDelegate().setExitOnFirstFatalError(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#setForceNoteFormat(boolean)
	 */
	@Override
	public void setForceNoteFormat(boolean flag) {
		try {
			getDelegate().setForceNoteFormat(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#setLogComment(java.lang.String)
	 */
	@Override
	public void setLogComment(String comment) {
		try {
			getDelegate().setLogComment(comment);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#setMIMEOption(int)
	 */
	@Override
	public void setMIMEOption(int option) {
		try {
			getDelegate().setMIMEOption(option);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#setOLEObjectOmittedText(java.lang.String)
	 */
	@Override
	public void setOLEObjectOmittedText(String replacementText) {
		try {
			getDelegate().setOLEObjectOmittedText(replacementText);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#setOmitItemNames(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setOmitItemNames(Vector names) {
		try {
			getDelegate().setOmitItemNames(names);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#setOmitMiscFileObjects(boolean)
	 */
	@Override
	public void setOmitMiscFileObjects(boolean flag) {
		try {
			getDelegate().setOmitMiscFileObjects(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#setOmitOLEObjects(boolean)
	 */
	@Override
	public void setOmitOLEObjects(boolean flag) {
		try {
			getDelegate().setOmitOLEObjects(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#setOmitRichtextAttachments(boolean)
	 */
	@Override
	public void setOmitRichtextAttachments(boolean flag) {
		try {
			getDelegate().setOmitRichtextAttachments(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#setOmitRichtextPictures(boolean)
	 */
	@Override
	public void setOmitRichtextPictures(boolean flag) {
		try {
			getDelegate().setOmitRichtextPictures(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#setOutputDOCTYPE(boolean)
	 */
	@Override
	public void setOutputDOCTYPE(boolean flag) {
		try {
			getDelegate().setOutputDOCTYPE(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#setPictureOmittedText(java.lang.String)
	 */
	@Override
	public void setPictureOmittedText(String replacementText) {
		try {
			getDelegate().setPictureOmittedText(replacementText);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#setRestrictToItemNames(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setRestrictToItemNames(Vector names) {
		try {
			getDelegate().setRestrictToItemNames(names);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#setRichTextOption(int)
	 */
	@Override
	public void setRichTextOption(int option) {
		try {
			getDelegate().setRichTextOption(option);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DxlExporter#setUncompressAttachments(boolean)
	 */
	@Override
	public void setUncompressAttachments(boolean flag) {
		try {
			getDelegate().setUncompressAttachments(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public Session getAncestorSession() {
		return (Session) this.getParent();
	}
}

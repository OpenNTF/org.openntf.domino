/*
 * Copyright 2013
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
package org.openntf.domino;

import java.util.Vector;

import org.openntf.domino.types.SessionDescendant;

/**
 * The Interface DxlExporter.
 */
public interface DxlExporter extends Base<lotus.domino.DxlExporter>, lotus.domino.DxlExporter, org.openntf.domino.ext.DxlExporter,
		SessionDescendant {

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#exportDxl(lotus.domino.Database)
	 */
	@Override
	public String exportDxl(lotus.domino.Database database);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#exportDxl(lotus.domino.Document)
	 */
	@Override
	public String exportDxl(lotus.domino.Document doc);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#exportDxl(lotus.domino.DocumentCollection)
	 */
	@Override
	public String exportDxl(lotus.domino.DocumentCollection docs);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#exportDxl(lotus.domino.NoteCollection)
	 */
	@Override
	public String exportDxl(lotus.domino.NoteCollection notes);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#getAttachmentOmittedText()
	 */
	@Override
	public String getAttachmentOmittedText();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#getConvertNotesBitmapsToGIF()
	 */
	@Override
	public boolean getConvertNotesBitmapsToGIF();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#getDoctypeSYSTEM()
	 */
	@Override
	public String getDoctypeSYSTEM();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#getExitOnFirstFatalError()
	 */
	@Override
	public boolean getExitOnFirstFatalError();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#getForceNoteFormat()
	 */
	@Override
	public boolean getForceNoteFormat();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#getLog()
	 */
	@Override
	public String getLog();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#getLogComment()
	 */
	@Override
	public String getLogComment();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#getMIMEOption()
	 */
	@Override
	public int getMIMEOption();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#getOLEObjectOmittedText()
	 */
	@Override
	public String getOLEObjectOmittedText();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#getOmitItemNames()
	 */
	@Override
	public Vector<String> getOmitItemNames();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#getOmitMiscFileObjects()
	 */
	@Override
	public boolean getOmitMiscFileObjects();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#getOmitOLEObjects()
	 */
	@Override
	public boolean getOmitOLEObjects();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#getOmitRichtextAttachments()
	 */
	@Override
	public boolean getOmitRichtextAttachments();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#getOmitRichtextPictures()
	 */
	@Override
	public boolean getOmitRichtextPictures();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#getOutputDOCTYPE()
	 */
	@Override
	public boolean getOutputDOCTYPE();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#getPictureOmittedText()
	 */
	@Override
	public String getPictureOmittedText();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#getRestrictToItemNames()
	 */
	@Override
	public Vector<String> getRestrictToItemNames();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#getRichTextOption()
	 */
	@Override
	public int getRichTextOption();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#getUncompressAttachments()
	 */
	@Override
	public boolean getUncompressAttachments();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#setAttachmentOmittedText(java.lang.String)
	 */
	@Override
	public void setAttachmentOmittedText(String replacementText);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#setConvertNotesBitmapsToGIF(boolean)
	 */
	@Override
	public void setConvertNotesBitmapsToGIF(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#setDoctypeSYSTEM(java.lang.String)
	 */
	@Override
	public void setDoctypeSYSTEM(String system);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#setExitOnFirstFatalError(boolean)
	 */
	@Override
	public void setExitOnFirstFatalError(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#setForceNoteFormat(boolean)
	 */
	@Override
	public void setForceNoteFormat(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#setLogComment(java.lang.String)
	 */
	@Override
	public void setLogComment(String comment);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#setMIMEOption(int)
	 */
	@Override
	public void setMIMEOption(int option);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#setOLEObjectOmittedText(java.lang.String)
	 */
	@Override
	public void setOLEObjectOmittedText(String replacementText);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#setOmitItemNames(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setOmitItemNames(Vector names);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#setOmitMiscFileObjects(boolean)
	 */
	@Override
	public void setOmitMiscFileObjects(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#setOmitOLEObjects(boolean)
	 */
	@Override
	public void setOmitOLEObjects(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#setOmitRichtextAttachments(boolean)
	 */
	@Override
	public void setOmitRichtextAttachments(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#setOmitRichtextPictures(boolean)
	 */
	@Override
	public void setOmitRichtextPictures(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#setOutputDOCTYPE(boolean)
	 */
	@Override
	public void setOutputDOCTYPE(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#setPictureOmittedText(java.lang.String)
	 */
	@Override
	public void setPictureOmittedText(String replacementText);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#setRestrictToItemNames(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setRestrictToItemNames(Vector names);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#setRichTextOption(int)
	 */
	@Override
	public void setRichTextOption(int option);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#setUncompressAttachments(boolean)
	 */
	@Override
	public void setUncompressAttachments(boolean flag);

}

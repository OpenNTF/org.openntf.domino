package org.openntf.domino;

import java.util.Vector;

import lotus.domino.DocumentCollection;
import lotus.domino.NoteCollection;

public interface DxlExporter extends Base<lotus.domino.DxlExporter>, lotus.domino.DxlExporter {

	@Override
	public String exportDxl(lotus.domino.Document arg0);

	@Override
	public String exportDxl(lotus.domino.Database arg0);

	@Override
	public String exportDxl(DocumentCollection arg0);

	@Override
	public String exportDxl(NoteCollection arg0);

	@Override
	public String getAttachmentOmittedText();

	@Override
	public boolean getConvertNotesBitmapsToGIF();

	@Override
	public lotus.domino.DxlExporter getDelegate();

	@Override
	public String getDoctypeSYSTEM();

	@Override
	public boolean getExitOnFirstFatalError();

	@Override
	public boolean getForceNoteFormat();

	@Override
	public String getLog();

	@Override
	public String getLogComment();

	@Override
	public int getMIMEOption();

	@Override
	public String getOLEObjectOmittedText();

	@Override
	public Vector getOmitItemNames();

	@Override
	public boolean getOmitMiscFileObjects();

	@Override
	public boolean getOmitOLEObjects();

	@Override
	public boolean getOmitRichtextAttachments();

	@Override
	public boolean getOmitRichtextPictures();

	@Override
	public boolean getOutputDOCTYPE();

	@Override
	public String getPictureOmittedText();

	@Override
	public Vector getRestrictToItemNames();

	@Override
	public int getRichTextOption();

	@Override
	public boolean getUncompressAttachments();

	@Override
	public void recycle();

	@Override
	public void recycle(Vector arg0);

	@Override
	public void setAttachmentOmittedText(String arg0);

	@Override
	public void setConvertNotesBitmapsToGIF(boolean arg0);

	@Override
	public void setDoctypeSYSTEM(String arg0);

	@Override
	public void setExitOnFirstFatalError(boolean arg0);

	@Override
	public void setForceNoteFormat(boolean arg0);

	@Override
	public void setLogComment(String arg0);

	@Override
	public void setMIMEOption(int arg0);

	@Override
	public void setOLEObjectOmittedText(String arg0);

	@Override
	public void setOmitItemNames(Vector arg0);

	@Override
	public void setOmitMiscFileObjects(boolean arg0);

	@Override
	public void setOmitOLEObjects(boolean arg0);

	@Override
	public void setOmitRichtextAttachments(boolean arg0);

	@Override
	public void setOmitRichtextPictures(boolean arg0);

	@Override
	public void setOutputDOCTYPE(boolean arg0);

	@Override
	public void setPictureOmittedText(String arg0);

	@Override
	public void setRestrictToItemNames(Vector arg0);

	@Override
	public void setRichTextOption(int arg0);

	@Override
	public void setUncompressAttachments(boolean arg0);

	@Override
	public boolean equals(Object obj);

	@Override
	public int hashCode();

	@Override
	public String toString();

}

package org.openntf.domino;

import java.util.Vector;

import lotus.domino.DocumentCollection;
import lotus.domino.NoteCollection;

public interface DxlExporter extends Base<lotus.domino.DxlExporter>, lotus.domino.DxlExporter {

	@Override
	public String exportDxl(lotus.domino.Document doc);

	@Override
	public String exportDxl(lotus.domino.Database database);

	@Override
	public String exportDxl(DocumentCollection docs);

	@Override
	public String exportDxl(NoteCollection notes);

	@Override
	public String getAttachmentOmittedText();

	@Override
	public boolean getConvertNotesBitmapsToGIF();

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
	public void setAttachmentOmittedText(String replacementText);

	@Override
	public void setConvertNotesBitmapsToGIF(boolean flag);

	@Override
	public void setDoctypeSYSTEM(String system);

	@Override
	public void setExitOnFirstFatalError(boolean flag);

	@Override
	public void setForceNoteFormat(boolean flag);

	@Override
	public void setLogComment(String comment);

	@Override
	public void setMIMEOption(int option);

	@Override
	public void setOLEObjectOmittedText(String replacementText);

	@Override
	public void setOmitItemNames(Vector names);

	@Override
	public void setOmitMiscFileObjects(boolean flag);

	@Override
	public void setOmitOLEObjects(boolean flag);

	@Override
	public void setOmitRichtextAttachments(boolean flag);

	@Override
	public void setOmitRichtextPictures(boolean flag);

	@Override
	public void setOutputDOCTYPE(boolean flag);

	@Override
	public void setPictureOmittedText(String replacementText);

	@Override
	public void setRestrictToItemNames(Vector names);

	@Override
	public void setRichTextOption(int option);

	@Override
	public void setUncompressAttachments(boolean flag);

}

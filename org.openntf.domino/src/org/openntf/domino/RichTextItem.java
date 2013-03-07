package org.openntf.domino;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Vector;

import lotus.domino.Base;
import lotus.domino.ColorObject;
import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.EmbeddedObject;
import lotus.domino.Item;
import lotus.domino.MIMEEntity;
import lotus.domino.NotesException;
import lotus.domino.RichTextNavigator;
import lotus.domino.RichTextParagraphStyle;
import lotus.domino.RichTextRange;
import lotus.domino.RichTextStyle;
import lotus.domino.View;
import lotus.domino.XSLTResultTarget;

import org.xml.sax.InputSource;

public interface RichTextItem extends lotus.domino.RichTextItem, org.openntf.domino.Base<lotus.domino.RichTextItem> {

	@Override
	public String abstractText(int arg0, boolean arg1, boolean arg2);

	@Override
	public void addNewLine();

	@Override
	public void addNewLine(int arg0);

	@Override
	public void addNewLine(int arg0, boolean arg1);

	@Override
	public void addPageBreak();

	@Override
	public void addPageBreak(RichTextParagraphStyle arg0);

	@Override
	public void addTab();

	@Override
	public void addTab(int arg0);

	@Override
	public void appendDocLink(Database arg0);

	@Override
	public void appendDocLink(Database arg0, String arg1);

	@Override
	public void appendDocLink(Database arg0, String arg1, String arg2);

	@Override
	public void appendDocLink(Document arg0);

	@Override
	public void appendDocLink(Document arg0, String arg1);

	@Override
	public void appendDocLink(Document arg0, String arg1, String arg2);

	@Override
	public void appendDocLink(View arg0);

	@Override
	public void appendDocLink(View arg0, String arg1);

	@Override
	public void appendDocLink(View arg0, String arg1, String arg2);

	@Override
	public void appendParagraphStyle(RichTextParagraphStyle arg0);

	@Override
	public void appendRTItem(lotus.domino.RichTextItem arg0);

	@Override
	public void appendStyle(RichTextStyle arg0);

	@Override
	public void appendTable(int arg0, int arg1);

	@Override
	public void appendTable(int arg0, int arg1, Vector arg2);

	@Override
	public void appendTable(int arg0, int arg1, Vector arg2, int arg3, Vector arg4);

	@Override
	public void appendText(String arg0);

	@Override
	public void appendToTextList(String arg0);

	@Override
	public void appendToTextList(Vector arg0);

	@Override
	public void beginInsert(Base arg0);

	@Override
	public void beginInsert(Base arg0, boolean arg1);

	@Override
	public void beginSection(String arg0);

	@Override
	public void beginSection(String arg0, RichTextStyle arg1);

	@Override
	public void beginSection(String arg0, RichTextStyle arg1, ColorObject arg2, boolean arg3);

	@Override
	public void compact();

	@Override
	public boolean containsValue(Object arg0);

	@Override
	public Item copyItemToDocument(Document arg0);

	@Override
	public Item copyItemToDocument(Document arg0, String arg1);

	@Override
	public RichTextNavigator createNavigator();

	@Override
	public RichTextRange createRange();

	@Override
	public EmbeddedObject embedObject(int arg0, String arg1, String arg2, String arg3);

	@Override
	public void endInsert();

	@Override
	public void endSection();

	@Override
	public DateTime getDateTimeValue();

	@Override
	public lotus.domino.RichTextItem getDelegate();

	@Override
	public EmbeddedObject getEmbeddedObject(String arg0);

	@Override
	public Vector getEmbeddedObjects();

	@Override
	public String getFormattedText(boolean arg0, int arg1, int arg2);

	@Override
	public InputSource getInputSource();

	@Override
	public InputStream getInputStream();

	@Override
	public DateTime getLastModified();

	@Override
	public MIMEEntity getMIMEEntity();

	@Override
	public String getName();

	@Override
	public int getNotesFont(String arg0, boolean arg1);

	@Override
	public Document getParent();

	@Override
	public Reader getReader();

	@Override
	public String getText();

	@Override
	public String getText(int arg0);

	@Override
	public int getType();

	@Override
	public String getUnformattedText();

	@Override
	public Object getValueCustomData() throws IOException, ClassNotFoundException, NotesException;

	@Override
	public Object getValueCustomData(String arg0) throws IOException, ClassNotFoundException, NotesException;

	@Override
	public byte[] getValueCustomDataBytes(String arg0) throws IOException, NotesException;

	@Override
	public Vector getValueDateTimeArray();

	@Override
	public double getValueDouble();

	@Override
	public int getValueInteger();

	@Override
	public int getValueLength();

	@Override
	public Vector getValues();

	@Override
	public String getValueString();

	@Override
	public boolean isAuthors();

	@Override
	public boolean isEncrypted();

	@Override
	public boolean isNames();

	@Override
	public boolean isProtected();

	@Override
	public boolean isReaders();

	@Override
	public boolean isSaveToDisk();

	@Override
	public boolean isSigned();

	@Override
	public boolean isSummary();

	@Override
	public org.w3c.dom.Document parseXML(boolean arg0) throws IOException, NotesException;

	@Override
	public void recycle();

	@Override
	public void recycle(Vector arg0);

	@Override
	public void remove();

	@Override
	public void setAuthors(boolean arg0);

	@Override
	public void setDateTimeValue(lotus.domino.DateTime arg0);

	@Override
	public void setEncrypted(boolean arg0);

	@Override
	public void setNames(boolean arg0);

	@Override
	public void setProtected(boolean arg0);

	@Override
	public void setReaders(boolean arg0);

	@Override
	public void setSaveToDisk(boolean arg0);

	@Override
	public void setSigned(boolean arg0);

	@Override
	public void setSummary(boolean arg0);

	@Override
	public void setValueCustomData(Object arg0) throws IOException, NotesException;

	@Override
	public void setValueCustomData(String arg0, Object arg1) throws IOException, NotesException;

	@Override
	public void setValueCustomDataBytes(String arg0, byte[] arg1) throws IOException, NotesException;

	@Override
	public void setValueDouble(double arg0);

	@Override
	public void setValueInteger(int arg0);

	@Override
	public void setValues(Vector arg0);

	@Override
	public void setValueString(String arg0);

	@Override
	public void transformXML(Object arg0, XSLTResultTarget arg1);

	@Override
	public void update();
}

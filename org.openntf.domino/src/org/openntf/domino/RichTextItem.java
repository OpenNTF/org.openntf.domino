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
	public String abstractText(int maxLen, boolean dropVowels, boolean userDict);

	@Override
	public void addNewLine();

	@Override
	public void addNewLine(int count);

	@Override
	public void addNewLine(int count, boolean newParagraph);

	@Override
	public void addPageBreak();

	@Override
	public void addPageBreak(RichTextParagraphStyle pstyle);

	@Override
	public void addTab();

	@Override
	public void addTab(int count);

	@Override
	public void appendDocLink(Database db);

	@Override
	public void appendDocLink(Database db, String comment);

	@Override
	public void appendDocLink(Database db, String comment, String hotspotText);

	@Override
	public void appendDocLink(Document doc);

	@Override
	public void appendDocLink(Document doc, String comment);

	@Override
	public void appendDocLink(Document doc, String comment, String hotspotText);

	@Override
	public void appendDocLink(View view);

	@Override
	public void appendDocLink(View view, String comment);

	@Override
	public void appendDocLink(View view, String comment, String hotspotText);

	@Override
	public void appendParagraphStyle(RichTextParagraphStyle pstyle);

	@Override
	public void appendRTItem(lotus.domino.RichTextItem rtitem);

	@Override
	public void appendStyle(RichTextStyle rstyle);

	@Override
	public void appendTable(int rows, int columns);

	@Override
	public void appendTable(int rows, int columns, Vector labels);

	@Override
	public void appendTable(int rows, int columns, Vector labels, int leftMargin, Vector pstyles);

	@Override
	public void appendText(String text);

	@Override
	public void appendToTextList(String value);

	@Override
	public void appendToTextList(Vector values);

	@Override
	public void beginInsert(Base element);

	@Override
	public void beginInsert(Base element, boolean after);

	@Override
	public void beginSection(String title);

	@Override
	public void beginSection(String title, RichTextStyle titleStyle);

	@Override
	public void beginSection(String title, RichTextStyle titleStyle, ColorObject barColor, boolean expand);

	@Override
	public void compact();

	@Override
	public boolean containsValue(Object value);

	@Override
	public Item copyItemToDocument(Document doc);

	@Override
	public Item copyItemToDocument(Document doc, String newName);

	@Override
	public RichTextNavigator createNavigator();

	@Override
	public RichTextRange createRange();

	@Override
	public EmbeddedObject embedObject(int type, String className, String source, String name);

	@Override
	public void endInsert();

	@Override
	public void endSection();

	@Override
	public DateTime getDateTimeValue();

	@Override
	public EmbeddedObject getEmbeddedObject(String name);

	@Override
	public Vector<EmbeddedObject> getEmbeddedObjects();

	@Override
	public String getFormattedText(boolean tabStrip, int lineLen, int maxLen);

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
	public int getNotesFont(String faceName, boolean addOnFail);

	@Override
	public Document getParent();

	@Override
	public Reader getReader();

	@Override
	public String getText();

	@Override
	public String getText(int maxLen);

	@Override
	public int getType();

	@Override
	public String getUnformattedText();

	@Override
	public Object getValueCustomData() throws IOException, ClassNotFoundException;

	@Override
	public Object getValueCustomData(String dataTypeName) throws IOException, ClassNotFoundException;

	@Override
	public byte[] getValueCustomDataBytes(String dataTypeName) throws IOException;

	@Override
	public Vector<Object> getValueDateTimeArray();

	@Override
	public double getValueDouble();

	@Override
	public int getValueInteger();

	@Override
	public int getValueLength();

	@Override
	public Vector<Object> getValues();

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
	public org.w3c.dom.Document parseXML(boolean validate) throws IOException, NotesException;

	@Override
	public void remove();

	@Override
	public void setAuthors(boolean flag);

	@Override
	public void setDateTimeValue(lotus.domino.DateTime dt);

	@Override
	public void setEncrypted(boolean flag);

	@Override
	public void setNames(boolean flag);

	@Override
	public void setProtected(boolean flag);

	@Override
	public void setReaders(boolean flag);

	@Override
	public void setSaveToDisk(boolean flag);

	@Override
	public void setSigned(boolean flag);

	@Override
	public void setSummary(boolean flag);

	@Override
	public void setValueCustomData(Object userObj) throws IOException;

	@Override
	public void setValueCustomData(String dataTypeName, Object userObj) throws IOException;

	@Override
	public void setValueCustomDataBytes(String dataTypeName, byte[] byteArray) throws IOException;

	@Override
	public void setValueDouble(double value);

	@Override
	public void setValueInteger(int value);

	@Override
	public void setValues(Vector values);

	@Override
	public void setValueString(String value);

	@Override
	public void transformXML(Object style, XSLTResultTarget result);

	@Override
	public void update();
}

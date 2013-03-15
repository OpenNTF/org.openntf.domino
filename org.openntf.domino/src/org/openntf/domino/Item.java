package org.openntf.domino;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Vector;

import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.XSLTResultTarget;

import org.xml.sax.InputSource;

public interface Item extends Base<lotus.domino.Item>, lotus.domino.Item {
	@Override
	public String abstractText(int maxLen, boolean dropVowels, boolean userDict);

	@Override
	public void appendToTextList(String value);

	@SuppressWarnings("unchecked")
	@Override
	public void appendToTextList(Vector values);

	@Override
	public boolean containsValue(Object value);

	@Override
	public Item copyItemToDocument(Document doc);

	@Override
	public Item copyItemToDocument(Document doc, String newName);

	@Override
	public DateTime getDateTimeValue();

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
	public Object getValueCustomData() throws IOException, ClassNotFoundException;

	@Override
	public Object getValueCustomData(String dataTypeName) throws IOException, ClassNotFoundException;

	@Override
	public byte[] getValueCustomDataBytes(String dataTypeName) throws IOException;

	@Override
	public Vector<DateTime> getValueDateTimeArray();

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
	public void setDateTimeValue(lotus.domino.DateTime dateTime);

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

	@SuppressWarnings("unchecked")
	@Override
	public void setValues(Vector values);

	@Override
	public void setValueString(String value);

	@Override
	public void transformXML(Object style, XSLTResultTarget result);
}

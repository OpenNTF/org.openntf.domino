package org.openntf.domino;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Vector;

import lotus.domino.Document;
import lotus.domino.MIMEEntity;
import lotus.domino.NotesException;
import lotus.domino.XSLTResultTarget;

import org.xml.sax.InputSource;

public interface Item extends Base<lotus.domino.Item>, lotus.domino.Item {
	@Override
	public String abstractText(int arg0, boolean arg1, boolean arg2);

	@Override
	public void appendToTextList(String arg0);

	@Override
	public void appendToTextList(Vector arg0);

	@Override
	public boolean containsValue(Object arg0);

	@Override
	public lotus.domino.Item copyItemToDocument(Document arg0);

	@Override
	public lotus.domino.Item copyItemToDocument(Document arg0, String arg1);

	@Override
	public DateTime getDateTimeValue();

	@Override
	public lotus.domino.Item getDelegate();

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
	public String getText(int arg0);

	@Override
	public int getType();

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
}

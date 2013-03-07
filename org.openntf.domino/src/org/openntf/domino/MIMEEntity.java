package org.openntf.domino;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Vector;

import lotus.domino.MIMEHeader;
import lotus.domino.Stream;
import lotus.domino.XSLTResultTarget;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public interface MIMEEntity extends Base<lotus.domino.MIMEEntity>, lotus.domino.MIMEEntity {

	@Override
	public lotus.domino.MIMEEntity createChildEntity();

	@Override
	public lotus.domino.MIMEEntity createChildEntity(lotus.domino.MIMEEntity arg0);

	@Override
	public MIMEHeader createHeader(String arg0);

	@Override
	public lotus.domino.MIMEEntity createParentEntity();

	@Override
	public void decodeContent();

	@Override
	public void encodeContent(int arg0);

	@Override
	public String getBoundaryEnd();

	@Override
	public String getBoundaryStart();

	@Override
	public String getCharset();

	@Override
	public void getContentAsBytes(Stream arg0);

	@Override
	public void getContentAsBytes(Stream arg0, boolean arg1);

	@Override
	public String getContentAsText();

	@Override
	public void getContentAsText(Stream arg0);

	@Override
	public void getContentAsText(Stream arg0, boolean arg1);

	@Override
	public String getContentSubType();

	@Override
	public String getContentType();

	@Override
	public lotus.domino.MIMEEntity getDelegate();

	@Override
	public int getEncoding();

	@Override
	public void getEntityAsText(Stream arg0);

	@Override
	public void getEntityAsText(Stream arg0, Vector arg1);

	@Override
	public void getEntityAsText(Stream arg0, Vector arg1, boolean arg2);

	@Override
	public lotus.domino.MIMEEntity getFirstChildEntity();

	@Override
	public Vector getHeaderObjects();

	@Override
	public String getHeaders();

	@Override
	public InputSource getInputSource();

	@Override
	public InputStream getInputStream();

	@Override
	public lotus.domino.MIMEEntity getNextEntity();

	@Override
	public lotus.domino.MIMEEntity getNextEntity(int arg0);

	@Override
	public lotus.domino.MIMEEntity getNextSibling();

	@Override
	public MIMEHeader getNthHeader(String arg0);

	@Override
	public MIMEHeader getNthHeader(String arg0, int arg1);

	@Override
	public lotus.domino.MIMEEntity getParentEntity();

	@Override
	public String getPreamble();

	@Override
	public lotus.domino.MIMEEntity getPrevEntity();

	@Override
	public lotus.domino.MIMEEntity getPrevEntity(int arg0);

	@Override
	public lotus.domino.MIMEEntity getPrevSibling();

	@Override
	public Reader getReader();

	@Override
	public String getSomeHeaders();

	@Override
	public String getSomeHeaders(Vector arg0);

	@Override
	public String getSomeHeaders(Vector arg0, boolean arg1);

	@Override
	public Document parseXML(boolean arg0) throws IOException;

	@Override
	public void recycle();

	@Override
	public void recycle(Vector arg0);

	@Override
	public void remove();

	@Override
	public void setContentFromBytes(Stream arg0, String arg1, int arg2);

	@Override
	public void setContentFromText(Stream arg0, String arg1, int arg2);

	@Override
	public void setPreamble(String arg0);

	@Override
	public void transformXML(Object arg0, XSLTResultTarget arg1);

	@Override
	public boolean equals(Object obj);

	@Override
	public int hashCode();

	@Override
	public String toString();

}

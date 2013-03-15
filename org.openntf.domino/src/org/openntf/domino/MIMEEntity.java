package org.openntf.domino;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Vector;

import org.xml.sax.InputSource;

public interface MIMEEntity extends Base<lotus.domino.MIMEEntity>, lotus.domino.MIMEEntity {

	@Override
	public MIMEEntity createChildEntity();

	@Override
	public MIMEEntity createChildEntity(lotus.domino.MIMEEntity nextSibling);

	@Override
	public MIMEHeader createHeader(String headerName);

	@Override
	public MIMEEntity createParentEntity();

	@Override
	public void decodeContent();

	@Override
	public void encodeContent(int encoding);

	@Override
	public String getBoundaryEnd();

	@Override
	public String getBoundaryStart();

	@Override
	public String getCharset();

	@Override
	public void getContentAsBytes(lotus.domino.Stream stream);

	@Override
	public void getContentAsBytes(lotus.domino.Stream stream, boolean decoded);

	@Override
	public String getContentAsText();

	@Override
	public void getContentAsText(lotus.domino.Stream stream);

	@Override
	public void getContentAsText(lotus.domino.Stream stream, boolean decoded);

	@Override
	public String getContentSubType();

	@Override
	public String getContentType();

	@Override
	public int getEncoding();

	@Override
	public void getEntityAsText(lotus.domino.Stream stream);

	@SuppressWarnings("unchecked")
	@Override
	public void getEntityAsText(lotus.domino.Stream stream, Vector headerFilters);

	@SuppressWarnings("unchecked")
	@Override
	public void getEntityAsText(lotus.domino.Stream stream, Vector headerFilters, boolean inclusive);

	@Override
	public MIMEEntity getFirstChildEntity();

	@Override
	public Vector<MIMEHeader> getHeaderObjects();

	@Override
	public String getHeaders();

	@Override
	public InputSource getInputSource();

	@Override
	public InputStream getInputStream();

	@Override
	public MIMEEntity getNextEntity();

	@Override
	public MIMEEntity getNextEntity(int search);

	@Override
	public MIMEEntity getNextSibling();

	@Override
	public MIMEHeader getNthHeader(String headerName);

	@Override
	public MIMEHeader getNthHeader(String headerName, int instance);

	@Override
	public MIMEEntity getParentEntity();

	@Override
	public String getPreamble();

	@Override
	public MIMEEntity getPrevEntity();

	@Override
	public MIMEEntity getPrevEntity(int search);

	@Override
	public MIMEEntity getPrevSibling();

	@Override
	public Reader getReader();

	@Override
	public String getSomeHeaders();

	@SuppressWarnings("unchecked")
	@Override
	public String getSomeHeaders(Vector headerFilters);

	@SuppressWarnings("unchecked")
	@Override
	public String getSomeHeaders(Vector headerFilters, boolean inclusive);

	@Override
	public org.w3c.dom.Document parseXML(boolean validate) throws IOException;

	@Override
	public void remove();

	@Override
	public void setContentFromBytes(lotus.domino.Stream stream, String contentType, int encoding);

	@Override
	public void setContentFromText(lotus.domino.Stream stream, String contentType, int encoding);

	@Override
	public void setPreamble(String preamble);

	@Override
	public void transformXML(Object style, lotus.domino.XSLTResultTarget result);

}

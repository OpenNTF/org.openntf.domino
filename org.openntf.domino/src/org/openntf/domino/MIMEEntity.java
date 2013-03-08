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
	public lotus.domino.MIMEEntity createChildEntity(lotus.domino.MIMEEntity nextSibling);

	@Override
	public MIMEHeader createHeader(String headerName);

	@Override
	public lotus.domino.MIMEEntity createParentEntity();

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
	public void getContentAsBytes(Stream stream);

	@Override
	public void getContentAsBytes(Stream stream, boolean decoded);

	@Override
	public String getContentAsText();

	@Override
	public void getContentAsText(Stream stream);

	@Override
	public void getContentAsText(Stream stream, boolean decoded);

	@Override
	public String getContentSubType();

	@Override
	public String getContentType();

	@Override
	public int getEncoding();

	@Override
	public void getEntityAsText(Stream stream);

	@Override
	public void getEntityAsText(Stream stream, Vector headerFilters);

	@Override
	public void getEntityAsText(Stream stream, Vector headerFilters, boolean inclusive);

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
	public lotus.domino.MIMEEntity getNextEntity(int search);

	@Override
	public lotus.domino.MIMEEntity getNextSibling();

	@Override
	public MIMEHeader getNthHeader(String headerName);

	@Override
	public MIMEHeader getNthHeader(String headerName, int instance);

	@Override
	public lotus.domino.MIMEEntity getParentEntity();

	@Override
	public String getPreamble();

	@Override
	public lotus.domino.MIMEEntity getPrevEntity();

	@Override
	public lotus.domino.MIMEEntity getPrevEntity(int search);

	@Override
	public lotus.domino.MIMEEntity getPrevSibling();

	@Override
	public Reader getReader();

	@Override
	public String getSomeHeaders();

	@Override
	public String getSomeHeaders(Vector headerFilters);

	@Override
	public String getSomeHeaders(Vector headerFilters, boolean inclusive);

	@Override
	public Document parseXML(boolean validate) throws IOException;

	@Override
	public void remove();

	@Override
	public void setContentFromBytes(Stream stream, String contentType, int encoding);

	@Override
	public void setContentFromText(Stream stream, String contentType, int encoding);

	@Override
	public void setPreamble(String preamble);

	@Override
	public void transformXML(Object style, XSLTResultTarget result);

}

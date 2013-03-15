package org.openntf.domino.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.xml.sax.InputSource;

public class MIMEEntity extends Base<org.openntf.domino.MIMEEntity, lotus.domino.MIMEEntity> implements org.openntf.domino.MIMEEntity {

	public MIMEEntity(lotus.domino.MIMEEntity delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	@Override
	public MIMEEntity createChildEntity() {
		try {
			return Factory.fromLotus(getDelegate().createChildEntity(), MIMEEntity.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public MIMEEntity createChildEntity(lotus.domino.MIMEEntity nextSibling) {
		try {
			return Factory.fromLotus(getDelegate().createChildEntity((lotus.domino.MIMEEntity) toLotus(nextSibling)), MIMEEntity.class,
					this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public MIMEHeader createHeader(String headerName) {
		try {
			return Factory.fromLotus(getDelegate().createHeader(headerName), MIMEHeader.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public MIMEEntity createParentEntity() {
		try {
			return Factory.fromLotus(getDelegate().createParentEntity(), MIMEEntity.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public void decodeContent() {
		try {
			getDelegate().decodeContent();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void encodeContent(int encoding) {
		try {
			getDelegate().encodeContent(encoding);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public String getBoundaryEnd() {
		try {
			return getDelegate().getBoundaryEnd();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getBoundaryStart() {
		try {
			return getDelegate().getBoundaryStart();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getCharset() {
		try {
			return getDelegate().getCharset();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public void getContentAsBytes(lotus.domino.Stream stream) {
		try {
			getDelegate().getContentAsBytes((lotus.domino.Stream) toLotus(stream));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void getContentAsBytes(lotus.domino.Stream stream, boolean decoded) {
		try {
			getDelegate().getContentAsBytes((lotus.domino.Stream) toLotus(stream), decoded);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public String getContentAsText() {
		try {
			return getDelegate().getContentAsText();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public void getContentAsText(lotus.domino.Stream stream) {
		try {
			getDelegate().getContentAsText((lotus.domino.Stream) toLotus(stream));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void getContentAsText(lotus.domino.Stream stream, boolean decoded) {
		try {
			getDelegate().getContentAsText((lotus.domino.Stream) toLotus(stream), decoded);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public String getContentSubType() {
		try {
			return getDelegate().getContentSubType();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getContentType() {
		try {
			return getDelegate().getContentType();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public int getEncoding() {
		try {
			return getDelegate().getEncoding();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public void getEntityAsText(lotus.domino.Stream stream) {
		try {
			getDelegate().getEntityAsText((lotus.domino.Stream) toLotus(stream));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void getEntityAsText(lotus.domino.Stream stream, Vector headerFilters) {
		try {
			getDelegate().getEntityAsText((lotus.domino.Stream) toLotus(stream), headerFilters);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void getEntityAsText(lotus.domino.Stream stream, Vector headerFilters, boolean inclusive) {
		try {
			getDelegate().getEntityAsText((lotus.domino.Stream) toLotus(stream), headerFilters, inclusive);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public MIMEEntity getFirstChildEntity() {
		try {
			return Factory.fromLotus(getDelegate().getFirstChildEntity(), MIMEEntity.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public Vector<org.openntf.domino.MIMEHeader> getHeaderObjects() {
		try {
			return Factory.fromLotusAsVector(getDelegate().getHeaderObjects(), org.openntf.domino.MIMEHeader.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getHeaders() {
		try {
			return getDelegate().getHeaders();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public InputSource getInputSource() {
		try {
			return getDelegate().getInputSource();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public InputStream getInputStream() {
		try {
			return getDelegate().getInputStream();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public MIMEEntity getNextEntity() {
		try {
			return Factory.fromLotus(getDelegate().getNextEntity(), MIMEEntity.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public MIMEEntity getNextEntity(int search) {
		try {
			return Factory.fromLotus(getDelegate().getNextEntity(search), MIMEEntity.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public MIMEEntity getNextSibling() {
		try {
			return Factory.fromLotus(getDelegate().getNextSibling(), MIMEEntity.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public MIMEHeader getNthHeader(String headerName) {
		try {
			return Factory.fromLotus(getDelegate().getNthHeader(headerName), MIMEHeader.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public MIMEHeader getNthHeader(String headerName, int instance) {
		try {
			return Factory.fromLotus(getDelegate().getNthHeader(headerName, instance), MIMEHeader.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public MIMEEntity getParentEntity() {
		try {
			return Factory.fromLotus(getDelegate().getParentEntity(), MIMEEntity.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getPreamble() {
		try {
			return getDelegate().getPreamble();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public MIMEEntity getPrevEntity() {
		try {
			return Factory.fromLotus(getDelegate().getPrevEntity(), MIMEEntity.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public MIMEEntity getPrevEntity(int search) {
		try {
			return Factory.fromLotus(getDelegate().getPrevEntity(search), MIMEEntity.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public MIMEEntity getPrevSibling() {
		try {
			return Factory.fromLotus(getDelegate().getPrevSibling(), MIMEEntity.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public Reader getReader() {
		try {
			return getDelegate().getReader();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getSomeHeaders() {
		try {
			return getDelegate().getSomeHeaders();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getSomeHeaders(Vector headerFilters) {
		try {
			return getDelegate().getSomeHeaders(headerFilters);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getSomeHeaders(Vector headerFilters, boolean inclusive) {
		try {
			return getDelegate().getSomeHeaders(headerFilters, inclusive);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public org.w3c.dom.Document parseXML(boolean validate) throws IOException {
		try {
			return getDelegate().parseXML(validate);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public void remove() {
		try {
			getDelegate().remove();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setContentFromBytes(lotus.domino.Stream stream, String contentType, int encoding) {
		try {
			getDelegate().setContentFromBytes((lotus.domino.Stream) toLotus(stream), contentType, encoding);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setContentFromText(lotus.domino.Stream stream, String contentType, int encoding) {
		try {
			getDelegate().setContentFromText((lotus.domino.Stream) toLotus(stream), contentType, encoding);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setPreamble(String preamble) {
		try {
			getDelegate().setPreamble(preamble);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void transformXML(Object style, lotus.domino.XSLTResultTarget result) {
		try {
			getDelegate().transformXML(style, result);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}
}

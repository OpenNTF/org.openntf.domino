/**
 * 
 */
package org.openntf.domino.design.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.DxlExporter;
import org.openntf.domino.DxlImporter;
import org.openntf.domino.Session;
import org.openntf.domino.design.DesignBase;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.xml.XMLDocument;
import org.openntf.domino.utils.xml.XMLNode;
import org.xml.sax.SAXException;

/**
 * @author jgallagher
 * 
 */
public abstract class AbstractDesignBase implements DesignBase {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(AbstractDesignBase.class.getName());
	private static final long serialVersionUID = 1L;

	private String noteId_;
	private final Database database_;
	private XMLDocument dxl_;

	protected AbstractDesignBase(final Document document) {
		database_ = document.getAncestorDatabase();
		noteId_ = document.getNoteID();

		DxlExporter exporter = document.getAncestorSession().createDxlExporter();
		exporter.setOutputDOCTYPE(false);
		loadDxl(exporter.exportDxl(document));
	}

	protected AbstractDesignBase(final Database database) {
		database_ = database;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#isHideFromNotes()
	 */
	@Override
	public boolean isHideFromNotes() {
		return getDocumentElement().getAttribute("hide").equals("notes");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#isHideFromWeb()
	 */
	@Override
	public boolean isHideFromWeb() {
		return getDocumentElement().getAttribute("hide").equals("web");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#isNeedsRefresh()
	 */
	@Override
	public boolean isNeedsRefresh() {
		return getDocumentElement().getAttribute("refresh").equals("true");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#isPreventChanges()
	 */
	@Override
	public boolean isPreventChanges() {
		return getDocumentElement().getAttribute("noreplace").equals("true");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#isPropagatePreventChanges()
	 */
	@Override
	public boolean isPropagatePreventChanges() {
		return getDocumentElement().getAttribute("propagatenoreplace").equals("true");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#setHideFromNotes(boolean)
	 */
	@Override
	public void setHideFromNotes(final boolean hideFromNotes) {
		String hide = getDxl().getFirstChild().getAttribute("hide");
		if (hideFromNotes && !hide.contains("notes")) {
			hide += " notes";
		} else if (!hideFromNotes && hide.contains("notes")) {
			hide = hide.replace("notes", "");
		}
		getDocumentElement().setAttribute("hide", hide.trim());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#setHideFromWeb(boolean)
	 */
	@Override
	public void setHideFromWeb(final boolean hideFromWeb) {
		String hide = getDxl().getFirstChild().getAttribute("hide");
		if (hideFromWeb && !hide.contains("web")) {
			hide += " web";
		} else if (!hideFromWeb && hide.contains("web")) {
			hide = hide.replace("web", "");
		}
		getDocumentElement().setAttribute("hide", hide.trim());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#setNeedsRefresh(boolean)
	 */
	@Override
	public void setNeedsRefresh(final boolean needsRefresh) {
		getDocumentElement().setAttribute("refresh", String.valueOf(needsRefresh));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#setPreventChanges(boolean)
	 */
	@Override
	public void setPreventChanges(final boolean preventChanges) {
		getDocumentElement().setAttribute("noreplace", String.valueOf(preventChanges));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#setPropagatePreventChanges(boolean)
	 */
	@Override
	public void setPropagatePreventChanges(final boolean propagatePreventChanges) {
		getDocumentElement().setAttribute("propagatenoreplace", String.valueOf(propagatePreventChanges));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.Design#getDocument()
	 */
	@Override
	public Document getDocument() {
		if (noteId_ != null && !noteId_.isEmpty()) {
			return database_.getDocumentByID(noteId_);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.Design#getNoteID()
	 */
	@Override
	public String getNoteID() {
		return noteId_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.Design#getUniversalID()
	 */
	@Override
	public String getUniversalID() {
		XMLNode node = getDxl().selectSingleNode("//noteinfo");
		if (node != null) {
			return node.getAttribute("unid");
		}
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.DatabaseDescendant#getAncestorDatabase()
	 */
	@Override
	public Database getAncestorDatabase() {
		return database_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public Session getAncestorSession() {
		return getAncestorDatabase().getAncestorSession();
	}

	public void save() {

		DxlImporter importer = getAncestorSession().createDxlImporter();
		importer.setDesignImportOption(DxlImporter.DesignImportOption.REPLACE_ELSE_CREATE);
		importer.setReplicaRequiredForReplaceOrUpdate(false);
		Database database = getAncestorDatabase();
		try {
			importer.importDxl(getDxl().getXml(), database);
		} catch (IOException e) {
			DominoUtils.handleException(e);
			return;
		}
		noteId_ = importer.getFirstImportedNoteID();

		// Reset the DXL so that it can pick up new noteinfo
		Document document = database.getDocumentByID(noteId_);
		loadDxl(document.generateXML());
	}

	protected XMLDocument getDxl() {
		return dxl_;
	}

	protected XMLNode getDocumentElement() {
		return dxl_.getDocumentElement();
	}

	protected void loadDxl(final String xml) {
		dxl_ = new XMLDocument();
		try {
			dxl_.loadString(xml);
		} catch (SAXException e) {
			DominoUtils.handleException(e);
		} catch (IOException e) {
			DominoUtils.handleException(e);
		} catch (ParserConfigurationException e) {
			DominoUtils.handleException(e);
		}
	}

	protected void loadDxl(final InputStream is) {
		dxl_ = new XMLDocument();
		try {
			dxl_.loadInputStream(is);
		} catch (SAXException e) {
			DominoUtils.handleException(e);
		} catch (IOException e) {
			DominoUtils.handleException(e);
		} catch (ParserConfigurationException e) {
			DominoUtils.handleException(e);
		}
	}

	protected XMLNode getDxlNode(final String xpathString) {
		return getDxl().selectSingleNode(xpathString);
	}
}

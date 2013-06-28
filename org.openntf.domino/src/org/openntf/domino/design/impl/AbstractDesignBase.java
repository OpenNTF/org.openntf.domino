/**
 * 
 */
package org.openntf.domino.design.impl;

import java.io.IOException;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
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

	private Document document_;
	private final Database database_;
	private XMLDocument dxl_;

	public AbstractDesignBase(final Document document) {
		document_ = document;
		database_ = document_.getAncestorDatabase();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#isHideFromNotes()
	 */
	@Override
	public boolean isHideFromNotes() {
		return getDxl().getFirstChild().getAttribute("hide").equals("notes");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#isHideFromWeb()
	 */
	@Override
	public boolean isHideFromWeb() {
		return getDxl().getFirstChild().getAttribute("hide").equals("web");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#isNeedsRefresh()
	 */
	@Override
	public boolean isNeedsRefresh() {
		return getDxl().getFirstChild().getAttribute("refresh").equals("true");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#isPreventChanges()
	 */
	@Override
	public boolean isPreventChanges() {
		return getDxl().getFirstChild().getAttribute("noreplace").equals("true");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#isPropagatePreventChanges()
	 */
	@Override
	public boolean isPropagatePreventChanges() {
		return getDxl().getFirstChild().getAttribute("propagatenoreplace").equals("true");
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
		getDxl().getFirstChild().setAttribute("hide", hide.trim());
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
		getDxl().getFirstChild().setAttribute("hide", hide.trim());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#setNeedsRefresh(boolean)
	 */
	@Override
	public void setNeedsRefresh(final boolean needsRefresh) {
		getDxl().getFirstChild().setAttribute("refresh", String.valueOf(needsRefresh));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#setPreventChanges(boolean)
	 */
	@Override
	public void setPreventChanges(final boolean preventChanges) {
		getDxl().getFirstChild().setAttribute("noreplace", String.valueOf(preventChanges));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#setPropagatePreventChanges(boolean)
	 */
	@Override
	public void setPropagatePreventChanges(final boolean propagatePreventChanges) {
		getDxl().getFirstChild().setAttribute("propagatenoreplace", String.valueOf(propagatePreventChanges));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.Design#getDocument()
	 */
	@Override
	public Document getDocument() {
		return document_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.Design#getNoteID()
	 */
	@Override
	public String getNoteID() {
		try {
			XMLNode node = getDxl().selectSingleNode("//noteinfo");
			if (node != null) {
				return node.getAttribute("noteid");
			}
			return "";
		} catch (XPathExpressionException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.Design#getUniversalID()
	 */
	@Override
	public String getUniversalID() {
		try {
			XMLNode node = getDxl().selectSingleNode("//noteinfo");
			if (node != null) {
				return node.getAttribute("unid");
			}
			return "";
		} catch (XPathExpressionException e) {
			DominoUtils.handleException(e);
			return null;
		}
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
		String noteId = importer.getFirstImportedNoteID();
		document_ = database.getDocumentByID(noteId);

		// Reset the DXL so that it can pick up new noteinfo
		dxl_ = null;
		getDxl();
	}

	protected XMLDocument getDxl() {
		if (dxl_ == null) {
			dxl_ = new XMLDocument();
			try {
				dxl_.loadString(getDocument().generateXML());
			} catch (SAXException e) {
				DominoUtils.handleException(e);
				return null;
			} catch (IOException e) {
				DominoUtils.handleException(e);
				return null;
			} catch (ParserConfigurationException e) {
				DominoUtils.handleException(e);
				return null;
			}
		}
		return dxl_;
	}

	protected XMLNode getDxlNode(final String xpathString) {
		try {
			return getDxl().selectSingleNode(xpathString);
		} catch (XPathExpressionException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}
}

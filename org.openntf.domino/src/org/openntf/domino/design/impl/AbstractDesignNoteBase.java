/**
 * 
 */
package org.openntf.domino.design.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

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
public abstract class AbstractDesignNoteBase implements DesignBase {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(AbstractDesignNoteBase.class.getName());
	private static final long serialVersionUID = 1L;

	private String noteId_;
	private final Database database_;
	private XMLDocument dxl_;

	private static final String DESIGN_FLAG_PRESERVE = "P";
	private static final String DESIGN_FLAG_PROPAGATE_NOCHANGE = "r";
	private static final String DESIGN_FLAG_NEEDSREFRESH = "$";
	private static final String DESIGN_FLAG_HIDE_FROM_WEB = "w";
	private static final String DESIGN_FLAG_HIDE_FROM_NOTES = "n";

	public AbstractDesignNoteBase(final Document document) {
		database_ = document.getAncestorDatabase();
		noteId_ = document.getNoteID();

		DxlExporter exporter = document.getAncestorSession().createDxlExporter();
		exporter.setForceNoteFormat(true);
		loadDxl(exporter.exportDxl(document));
	}

	protected AbstractDesignNoteBase(final Database database) {
		database_ = database;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#isHideFromNotes()
	 */
	@Override
	public boolean isHideFromNotes() {
		return getFlags().contains(DESIGN_FLAG_HIDE_FROM_NOTES);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#isHideFromWeb()
	 */
	@Override
	public boolean isHideFromWeb() {
		return getFlags().contains(DESIGN_FLAG_HIDE_FROM_WEB);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#isNeedsRefresh()
	 */
	@Override
	public boolean isNeedsRefresh() {
		return getFlags().contains(DESIGN_FLAG_NEEDSREFRESH);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#isPreventChanges()
	 */
	@Override
	public boolean isPreventChanges() {
		return getFlags().contains(DESIGN_FLAG_PRESERVE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#isPropagatePreventChanges()
	 */
	@Override
	public boolean isPropagatePreventChanges() {
		return getFlags().contains(DESIGN_FLAG_PROPAGATE_NOCHANGE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#setHideFromNotes(boolean)
	 */
	@Override
	public void setHideFromNotes(final boolean hideFromNotes) {
		if (hideFromNotes) {
			addFlag(DESIGN_FLAG_HIDE_FROM_NOTES);
		} else {
			removeFlag(DESIGN_FLAG_HIDE_FROM_NOTES);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#setHideFromWeb(boolean)
	 */
	@Override
	public void setHideFromWeb(final boolean hideFromWeb) {
		if (hideFromWeb) {
			addFlag(DESIGN_FLAG_HIDE_FROM_WEB);
		} else {
			removeFlag(DESIGN_FLAG_HIDE_FROM_WEB);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#setNeedsRefresh(boolean)
	 */
	@Override
	public void setNeedsRefresh(final boolean needsRefresh) {
		if (needsRefresh) {
			addFlag(DESIGN_FLAG_NEEDSREFRESH);
		} else {
			removeFlag(DESIGN_FLAG_NEEDSREFRESH);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#setPreventChanges(boolean)
	 */
	@Override
	public void setPreventChanges(final boolean preventChanges) {
		if (preventChanges) {
			addFlag(DESIGN_FLAG_PRESERVE);
		} else {
			removeFlag(DESIGN_FLAG_PRESERVE);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#setPropagatePreventChanges(boolean)
	 */
	@Override
	public void setPropagatePreventChanges(final boolean propagatePreventChanges) {
		if (propagatePreventChanges) {
			addFlag(DESIGN_FLAG_PROPAGATE_NOCHANGE);
		} else {
			removeFlag(DESIGN_FLAG_PROPAGATE_NOCHANGE);
		}
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

	@Override
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
		try {
			return getDxl().selectSingleNode(xpathString);
		} catch (XPathExpressionException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	protected String getFlags() {
		return getFlagsNode().getText();
	}

	protected void addFlag(final String flag) {
		XMLNode flagsNode = getFlagsNode();
		String flags = flagsNode.getText();
		if (!flags.contains(flag)) {
			flagsNode.setText(flags + flag);
		}
	}

	protected void removeFlag(final String flag) {
		XMLNode flagsNode = getFlagsNode();
		String flags = flagsNode.getText();
		if (flags.contains(flag)) {
			// Assume this works for now
			flagsNode.setText(flags.replace(flag, ""));
		}
	}

	protected XMLNode getFlagsNode() {
		try {
			XMLNode flagsNode = getDxl().selectSingleNode("//item[@name='$Flags']/text");
			if (flagsNode != null) {
				return flagsNode;
			} else {
				// Better add one!
				flagsNode = getDocumentElement().addChildElement("item");
				flagsNode.setAttribute("name", "$Flags");
				flagsNode = flagsNode.addChildElement("text");
				return flagsNode;
			}
		} catch (XPathExpressionException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}
}

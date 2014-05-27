/*
 * Copyright 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package org.openntf.domino.design.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.DxlExporter;
import org.openntf.domino.DxlImporter;
import org.openntf.domino.Session;
import org.openntf.domino.design.DesignBaseNamed;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.xml.XMLDocument;
import org.openntf.domino.utils.xml.XMLNode;
import org.xml.sax.SAXException;

/**
 * @author jgallagher
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractDesignNoteBase implements DesignBaseNamed {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(AbstractDesignNoteBase.class.getName());

	private String noteId_;
	private transient Database database_;
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
		exporter.setOutputDOCTYPE(false);
		loadDxl(exporter.exportDxl(document));
	}

	protected AbstractDesignNoteBase(final Database database) {
		database_ = database;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.DesignBase#getDxlString()
	 */
	public String getDxlString() {
		try {
			return getDxl().getXml();
		} catch (IOException e) {
			DominoUtils.handleException(e);
			return null;
		}
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

	public void reattach(final Database database) {
		database_ = database;
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
	 * @see org.openntf.domino.design.DesignBaseNamed#getAliases()
	 */
	@Override
	public List<String> getAliases() {
		// Aliases are all the $TITLE values after the first
		List<String> titles = getTitles();
		return new ArrayList<String>(titles.subList(1, titles.size()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBaseNamed#getName()
	 */
	@Override
	public String getName() {
		List<String> titles = getTitles();
		if (titles.size() > 0) {
			return titles.get(0);
		}
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBaseNamed#setAlias(java.lang.String)
	 */
	@Override
	public void setAlias(final String alias) {
		List<String> titles = getTitles();
		List<String> result = new ArrayList<String>(2);
		result.add(titles.size() > 0 ? titles.get(0) : "");
		result.add(alias);
		setTitles(result);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBaseNamed#setAliases(java.lang.Iterable)
	 */
	@Override
	public void setAliases(final Iterable<String> aliases) {
		List<String> titles = getTitles();
		List<String> result = new ArrayList<String>(2);
		result.add(titles.size() > 0 ? titles.get(0) : "");
		for (String alias : aliases) {
			result.add(alias);
		}
		setTitles(result);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBaseNamed#setName(java.lang.String)
	 */
	@Override
	public void setName(final String name) {
		List<String> result = getTitles();
		if (result.size() > 0) {
			result.set(0, name);
		} else {
			result.add(name);
		}
		setTitles(result);
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

	@Override
	public void save() {
		try {
			DxlImporter importer = getAncestorSession().createDxlImporter();
			importer.setDesignImportOption(DxlImporter.DesignImportOption.REPLACE_ELSE_CREATE);
			importer.setReplicaRequiredForReplaceOrUpdate(false);
			Database database = getAncestorDatabase();

			importer.importDxl(getDxl().getXml(), database);

			noteId_ = importer.getFirstImportedNoteID();

			// Reset the DXL so that it can pick up new noteinfo
			Document document = database.getDocumentByID(noteId_);
			loadDxl(document.generateXML());
		} catch (IOException e) {
			DominoUtils.handleException(e);
			return;
		}
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
	}

	protected String getFlagsExt() {
		return getFlagsExtNode().getText();
	}

	protected void addFlagExt(final String flag) {
		XMLNode flagsNode = getFlagsExtNode();
		String flags = flagsNode.getText();
		if (!flags.contains(flag)) {
			flagsNode.setText(flags + flag);
		}
	}

	protected void removeFlagExt(final String flag) {
		XMLNode flagsNode = getFlagsExtNode();
		String flags = flagsNode.getText();
		if (flags.contains(flag)) {
			// Assume this works for now
			flagsNode.setText(flags.replace(flag, ""));
		}
	}

	protected XMLNode getFlagsExtNode() {
		XMLNode flagsNode = getDxl().selectSingleNode("//item[@name='$FlagExt']/text");
		if (flagsNode != null) {
			return flagsNode;
		} else {
			// Better add one!
			flagsNode = getDocumentElement().addChildElement("item");
			flagsNode.setAttribute("name", "$FlagsExt");
			flagsNode = flagsNode.addChildElement("text");
			return flagsNode;
		}
	}

	private List<String> getTitles() {
		List<XMLNode> titleNodes = getTitleNode().selectNodes("//text");
		List<String> result = new ArrayList<String>(titleNodes.size());
		for (XMLNode title : titleNodes) {
			String[] bits = title.getText().split("\\|");
			for (String bit : bits) {
				result.add(bit);
			}
		}
		return result;
	}

	private void setTitles(final Iterable<String> titles) {
		// Clear out existing titles
		XMLNode titleNode = getTitleNode();
		titleNode.getParentNode().removeChild(titleNode);

		// Now re-add them
		titleNode = getTitleNode();
		XMLNode listNode = titleNode.addChildElement("textlist");
		for (String title : titles) {
			XMLNode text = listNode.addChildElement("text");
			text.setText(title);
		}
	}

	private XMLNode getTitleNode() {
		XMLNode titleNode = getDxl().selectSingleNode("//item[@name='$TITLE']");
		if (titleNode == null) {
			titleNode = getDocumentElement().addChildElement("item");
			titleNode.setAttribute("name", "$TITLE");
		}
		return titleNode;
	}
}

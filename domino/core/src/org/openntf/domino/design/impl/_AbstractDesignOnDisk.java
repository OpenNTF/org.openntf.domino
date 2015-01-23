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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;

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

import com.ibm.commons.util.StringUtil;

/**
 * @author jgallagher, Roland Praml
 * 
 */
@SuppressWarnings("serial")
public abstract class _AbstractDesignOnDisk implements DesignBase {
	private static Transformer ODP_META_TRANSFORMER = createTransformer("dxl_metafilter.xslt");

	private transient Database database_;
	private transient Document document_;
	private String universalId_;

	private XMLDocument dxl_;
	//	private static final String NO_ENCODING = "<?xml version='1.0'?>";
	//	private static final String DEFAULT_ENCODING = "<?xml version='1.0'?>";

	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(_AbstractDesignOnDisk.class.getName());

	public _AbstractDesignOnDisk(final Database database) {
		database_ = database;
	}

	protected _AbstractDesignOnDisk(final Document document) {
		setDocument(document);
	}

	protected static Transformer createTransformer(final String resource) {
		return XMLNode.createTransformer(AbstractDesignOnDisk.class.getResourceAsStream(resource));
	}

	//	@Override
	//	public String getName() {
	//		// TODO: use DXL document or member 
	//		return getDocument().getItemValueString("$TITLE");
	//	}

	protected boolean mustEncode(final String resName) {
		for (int i = 0; i < resName.length(); i++) {
			char ch = resName.charAt(i);
			switch (ch) {
			case '/':
			case '\\':
			case ':':
			case '*':
			case '?':
			case '<':
			case '>':
			case '|':
			case '"':
				return true;
			}
		}
		return false;
	}

	protected String encodeResourceName(final String resName) {
		if (resName == null)
			return null;
		if (!mustEncode(resName))
			return resName;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < resName.length(); i++) {
			char ch = resName.charAt(i);
			switch (ch) {
			case '_':
			case '/':
			case '\\':
			case ':':
			case '*':
			case '?':
			case '<':
			case '>':
			case '|':
			case '"':
				sb.append('_');
				sb.append(Integer.toHexString(ch));
				break;
			default:
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	@Override
	public String getOnDiskName() {
		return encodeResourceName(getName());
	}

	@Override
	public String getOnDiskPath() {
		String path = getOnDiskFolder();
		if (path == null)
			return null;
		if (path.length() > 0) {
			path = path + "/" + getOnDiskName();
		} else {
			path = getOnDiskName();
		}
		if (path == null)
			return null;

		String ext = getOnDiskExtension();
		if (ext != null && !path.endsWith(ext))
			path = path + ext;
		return path;
	}

	protected Transformer getOdpTransformer() {
		return null;
	}

	protected Transformer getOdpMetaTransformer() {
		return ODP_META_TRANSFORMER;
	}

	@Override
	public void writeOnDiskFile(final File odpFile) throws IOException {
		getDxl().getXml(getOdpTransformer(), odpFile);
	}

	public final void writeOnDiskMeta(final File odpFile) throws IOException {
		getDxl().getXml(getOdpMetaTransformer(), odpFile);
	}

	//------------------------------ ondisk end --------------------------------

	@Override
	public final void reattach(final Database database) {
		database_ = database;
	}

	protected final void setDocument(final Document document) {
		database_ = document.getAncestorDatabase();
		universalId_ = document.getUniversalID(); // we must save the UNID. because NoteID may change on various instances
		document_ = document;
		dxl_ = null;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.DesignBase#getDxlString()
	 */
	@Override
	public final String getDxlString(final Transformer filter) {
		try {
			return getDxl().getXml(filter);
		} catch (IOException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.DatabaseDescendant#getAncestorDatabase()
	 */
	@Override
	public final Database getAncestorDatabase() {
		return database_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public final Session getAncestorSession() {
		return getAncestorDatabase().getAncestorSession();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.Design#getDocument()
	 */
	@Override
	public final Document getDocument() {
		if (!StringUtil.isEmpty(universalId_)) {
			return database_.getDocumentByID(universalId_);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.Design#getNoteID()
	 */
	@Override
	public final String getUniversalID() {
		return universalId_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.Design#getUniversalID()
	 */
	@Override
	public final String getNoteID() {
		XMLNode node = getDxl().selectSingleNode("//noteinfo");
		if (node != null) {
			return node.getAttribute("noteid");
		}
		return "";
	}

	protected abstract boolean useNoteFormat();

	protected final XMLDocument getDxl() {
		if (dxl_ == null) {
			DxlExporter exporter = getAncestorSession().createDxlExporter();
			exporter.setOutputDOCTYPE(false);
			exporter.setForceNoteFormat(useNoteFormat());

			// TODO: You will get an exporter error, if the design is protected. This should be handled correctly
			if (document_ != null) {
				loadDxl(exporter.exportDxl(document_));
			} else {
				loadDxl(exporter.exportDxl(database_));
			}
		}
		return dxl_;
	}

	protected final XMLNode getDocumentElement() {
		return getDxl().getDocumentElement();
	}

	protected void loadDxl(final String xml) {
		dxl_ = new XMLDocument();
		try {
			// search the first 64 bytes if we have got a "<note>" and compare if we expect note Format or not!
			String part = xml.substring(0, 64);
			if (part.contains("<note") != useNoteFormat()) {
				System.err.println(getClass().getSimpleName() + " (useNoteFormat = " + useNoteFormat() + "): Probably wrong XML Data:"
						+ part);
			}

			//			if (xml.startsWith(NO_ENCODING)) {
			//				xml = DEFAULT_ENCODING + xml.substring(NO_ENCODING.length());
			//				System.err.println("Added default encoding for: " + xml.substring(0, 100));
			//			}
			dxl_.loadString(xml);
		} catch (SAXException e) {
			DominoUtils.handleException(e);
		} catch (IOException e) {
			DominoUtils.handleException(e);
		} catch (ParserConfigurationException e) {
			DominoUtils.handleException(e);
		}
	}

	protected final void loadDxl(final InputStream is) {
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

	@Override
	public final boolean save() {

		DxlImporter importer = getAncestorSession().createDxlImporter();
		importer.setDesignImportOption(DxlImporter.DesignImportOption.REPLACE_ELSE_CREATE);
		importer.setReplicaRequiredForReplaceOrUpdate(false);
		Database database = getAncestorDatabase();
		try {
			importer.importDxl(getDxl().getXml(null), database);
		} catch (IOException e) {
			DominoUtils.handleException(e);
			if (importer != null) {
				System.out.println(importer.getLog());
			}
			return false;
		}

		// Reset the DXL so that it can pick up new noteinfo
		setDocument(database.getDocumentByID(importer.getFirstImportedNoteID()));

		return true;
	}

	public final String getDesignTemplateName() {
		return getItemValueString("$Class");
	}

	public final void setDesignTemplateName(final String designTemplateName) {
		setItemValue("$Class", designTemplateName);
	}

	public final void setItemValue(final String itemName, final Object value) {
		XMLNode node = getDxl().selectSingleNode("//item[@name='" + XMLDocument.escapeXPathValue(itemName) + "']");
		if (node == null) {
			node = getDxl().selectSingleNode("/*").addChildElement("item");
			node.setAttribute("name", itemName);
		} else {
			node.removeChildren();
		}

		if (value instanceof Iterable) {
			Object first = ((Iterable<?>) value).iterator().next();
			XMLNode list = node.addChildElement(first instanceof Number ? "numberlist" : "textlist");

			for (Object val : (Iterable<?>) value) {
				appendItemValueNode(list, val);
			}
		} else {
			appendItemValueNode(node, value);
		}
	}

	private final void appendItemValueNode(final XMLNode node, final Object value) {
		XMLNode child;
		if (value instanceof Number) {
			child = node.addChildElement("number");
		} else {
			child = node.addChildElement("text");
		}
		child.setText(String.valueOf(value));
	}

	public final String getItemValueString(final String itemName) {
		List<Object> result = new ArrayList<Object>();
		XMLNode node = getDxl().selectSingleNode("//item[@name='" + XMLDocument.escapeXPathValue(itemName) + "']");
		if (node != null) {
			node = node.selectSingleNode(".//number | .//text");
			if (node != null)
				return node.getText();
		}
		return "";
	}

	public final List<Object> getItemValue(final String itemName) {
		List<Object> result = new ArrayList<Object>();
		XMLNode node = getDxl().selectSingleNode("//item[@name='" + XMLDocument.escapeXPathValue(itemName) + "']");
		if (node != null) {
			List<XMLNode> nodes = node.selectNodes(".//number | .//text");
			for (XMLNode child : nodes) {
				if (child.getNodeName().equals("number")) {
					result.add(Double.parseDouble(child.getText()));
				} else {
					result.add(child.getText());
				}
			}
		}
		return result;
	}

	public final List<String> getItemValueStrings(final String itemName) {
		List<String> result = new ArrayList<String>();
		XMLNode node = getDxl().selectSingleNode("//item[@name='" + XMLDocument.escapeXPathValue(itemName) + "']");
		if (node != null) {
			List<XMLNode> nodes = node.selectNodes(".//number | .//text");
			for (XMLNode child : nodes) {
				result.add(child.getText());
			}
		}
		return result;
	}

	public final XMLNode getDxlNode(final String xpathString) {
		return getDxl().selectSingleNode(xpathString);
	}

	/**
	 * Called, when deserializing the object
	 * 
	 * @param in
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(final java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		// TODO: Reattach the database?
	}

	/**
	 * Called, when serializing the object. Needed to support lazy dxl initalization representation
	 * 
	 * @param out
	 * @throws IOException
	 */
	private void writeObject(final java.io.ObjectOutputStream out) throws IOException {
		getDxl();
		out.defaultWriteObject();
	}
}

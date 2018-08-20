/*
 * Copyright 2015
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
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.DxlExporter;
import org.openntf.domino.DxlImporter;
import org.openntf.domino.Session;
import org.openntf.domino.design.DesignBase;
import org.openntf.domino.design.DesignBaseNamed;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.xml.XMLDocument;
import org.openntf.domino.utils.xml.XMLNode;
import org.xml.sax.SAXException;

import com.ibm.commons.util.StringUtil;

/**
 * This is the Root class of all DesignNotes
 *
 * @author jgallagher, Roland Praml
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractDesignBase implements DesignBase {

	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(AbstractDesignBase.class.getName());

	private static Transformer ODP_META_TRANSFORMER = createTransformer("dxl_metafilter.xslt");

	private static final char DESIGN_FLAG_PRESERVE = 'P';
	private static final char DESIGN_FLAG_PROPAGATE_NOCHANGE = 'r';
	private static final char DESIGN_FLAG_NEEDSREFRESH = '$';
	private static final char DESIGN_FLAG_HIDE_FROM_WEB = 'w';
	private static final char DESIGN_FLAG_HIDE_FROM_NOTES = 'n';

	protected static final String FLAGS_ITEM = "$Flags";
	protected static final String FLAGS_EXT_ITEM = "$FlagsExt";
	protected static final String TITLE_ITEM = "$TITLE";

	private transient Database database_;
	private transient Document document_;
	private String universalId_;

	private XMLDocument dxl_;

	private DxlFormat dxlFormat_ = DxlFormat.NONE;

	private transient ODPMapping odpMapping_;

	private Date lastModified_;

	/**
	 * Create a new DesignBase based on the given database. You may add content to this DesignBase and save it afterwards.
	 *
	 * @param database
	 *            the Database
	 */
	public AbstractDesignBase(final Database database) {
		this(database, null);
	}

	/**
	 * Create a new DesignBase based on the given database and resource stream. You may add content to this DesignBase and save it
	 * afterwards.
	 *
	 * @param database
	 *            the Database
	 * @param String
	 *            fileName of DXL
	 */
	public AbstractDesignBase(final Database database, InputStream is) {
		database_ = database;
		try {

			if (null == is) {
				is = getClass().getResourceAsStream(getClass().getSimpleName() + ".xml");
			}

			loadDxl(is);
			is.close();
			save();
		} catch (IOException e) {
			DominoUtils.handleException(e);
		}
	}

	/**
	 * Create a new DesginBase based on the given document. This Method will be invoked by {@link DesignFactory#fromDocument(Document)}
	 *
	 * @param document
	 */
	protected AbstractDesignBase(final Document document) {
		setDocument(document);
	}

	protected DxlFormat getDxlFormat(final boolean detect) {
		if (detect) {
			getDxl();
		}
		return dxlFormat_;
	}

	protected abstract boolean enforceRawFormat();

	//	/**
	//	 * The preferd ODA Format (to fix bugs)
	//	 * @return
	//	 */
	//	protected abstract DxlFormat preferedOdaFormat();
	//	/**
	//	 * Indicates, wether this Note should be exported in Raw-Format.
	//	 */
	//	protected boolean enforceRawFormat() {
	//		return true;
	//	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.design.DesignBase#isHideFromNotes()
	 */
	@Override
	public final boolean isHideFromNotes() {
		switch (getDxlFormat(false)) {
		case DXL:
			return getDocumentElement().getAttribute("hide").contains("notes");
		default:
			return hasFlag(DESIGN_FLAG_HIDE_FROM_NOTES);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.design.DesignBase#isHideFromWeb()
	 */
	@Override
	public final boolean isHideFromWeb() {
		switch (getDxlFormat(false)) {
		case DXL:
			return getDocumentElement().getAttribute("hide").contains("web");
		default:
			return hasFlag(DESIGN_FLAG_HIDE_FROM_WEB);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.design.DesignBase#isNeedsRefresh()
	 */
	@Override
	public final boolean isNeedsRefresh() {
		switch (getDxlFormat(false)) {
		case DXL:
			return getDocumentElement().getAttribute("refresh").equals("true");
		default:
			return hasFlag(DESIGN_FLAG_NEEDSREFRESH);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.design.DesignBase#isPreventChanges()
	 */
	@Override
	public final boolean isPreventChanges() {
		switch (getDxlFormat(false)) {
		case DXL:
			return getDocumentElement().getAttribute("noreplace").equals("true");
		default:
			return hasFlag(DESIGN_FLAG_PRESERVE);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.design.DesignBase#isPropagatePreventChanges()
	 */
	@Override
	public final boolean isPropagatePreventChanges() {
		switch (getDxlFormat(false)) {
		case DXL:
			return getDocumentElement().getAttribute("propagatenoreplace").equals("true");
		default:
			return hasFlag(DESIGN_FLAG_PROPAGATE_NOCHANGE);
		}
	}

	/*
	 * Helper for Non-raw Notes
	 */
	protected void setHide(final String platform, final boolean hide) {
		if (getDxlFormat(false) != DxlFormat.DXL) {
			throw new IllegalStateException("Not in DXL Format");
		}
		String platforms = getDxl().getFirstChild().getAttribute("hide");
		if (hide) {
			if (platforms.contains(platform)) {
				return;
			}
			platforms += " " + platform;
		} else {
			if (!platforms.contains(platform)) {
				return;
			}
			platforms = platforms.replace(platform, "");
		}
		getDocumentElement().setAttribute("hide", platforms.trim());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.design.DesignBase#setHideFromNotes(boolean)
	 */
	@Override
	public final void setHideFromNotes(final boolean hideFromNotes) {
		switch (getDxlFormat(true)) {
		case DXL:
			setHide("notes", hideFromNotes);
		default:
			setFlag(DESIGN_FLAG_HIDE_FROM_NOTES, hideFromNotes);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.design.DesignBase#setHideFromWeb(boolean)
	 */
	@Override
	public void setHideFromWeb(final boolean hideFromWeb) {
		switch (getDxlFormat(true)) {
		case DXL:
			setHide("web", hideFromWeb);
		default:
			setFlag(DESIGN_FLAG_HIDE_FROM_WEB, hideFromWeb);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.design.DesignBase#setNeedsRefresh(boolean)
	 */
	@Override
	public void setNeedsRefresh(final boolean needsRefresh) {
		switch (getDxlFormat(true)) {
		case DXL:
			getDocumentElement().setAttribute("refresh", String.valueOf(needsRefresh));
		default:
			setFlag(DESIGN_FLAG_NEEDSREFRESH, needsRefresh);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.design.DesignBase#setPreventChanges(boolean)
	 */
	@Override
	public void setPreventChanges(final boolean preventChanges) {
		switch (getDxlFormat(true)) {
		case DXL:
			getDocumentElement().setAttribute("noreplace", String.valueOf(preventChanges));
		default:
			setFlag(DESIGN_FLAG_PRESERVE, preventChanges);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.design.DesignBase#setPropagatePreventChanges(boolean)
	 */
	@Override
	public void setPropagatePreventChanges(final boolean propagatePreventChanges) {
		switch (getDxlFormat(true)) {
		case DXL:
			getDocumentElement().setAttribute("propagatenoreplace", String.valueOf(propagatePreventChanges));
		default:
			setFlag(DESIGN_FLAG_PROPAGATE_NOCHANGE, propagatePreventChanges);
		}
	}

	// -------------- Flags stuff

	protected String getFlags() {
		if (getDxlFormat(true) != DxlFormat.RAWNOTE) {
			Document doc = getDocument();
			if (null != doc) {
				return doc.getItemValueString(FLAGS_ITEM);
			}
			throw new IllegalStateException("Flags are available only in DxlFormat.RAWNOTE");
		}
		return getItemValueString(FLAGS_ITEM);
	}

	protected void setFlag(final char flag, final boolean enabled) {
		String flags = getFlags();
		if (enabled) {
			if (flags.indexOf(flag) < 0) {
				setItemValue(FLAGS_ITEM, flags + flag, FLAG_SUMMARY);
			}
		} else {
			if (flags.indexOf(flag) >= 0) {
				// Assume this works for now
				setItemValue(FLAGS_ITEM, flags.replace(flag + "", ""), FLAG_SUMMARY);
			}
		}
	}

	protected boolean hasFlag(final char flag) {
		return getFlags().indexOf(flag) >= 0;
	}

	// FlagsExt
	protected String getFlagsExt() {
		if (getDxlFormat(true) != DxlFormat.RAWNOTE) {
			Document doc = getDocument();
			if (null != doc) {
				return doc.getItemValueString(FLAGS_EXT_ITEM);
			}
			throw new IllegalStateException("Flags are available only in DxlFormat.RAWNOTE");
		}
		return getItemValueString(FLAGS_EXT_ITEM);
	}

	protected void setFlagExt(final char flag, final boolean enabled) {
		String flags = getFlagsExt();
		if (enabled) {
			if (flags.indexOf(flag) < 0) {
				setItemValue(FLAGS_EXT_ITEM, flags + flag, FLAG_SIGN_SUMMARY);
			}
		} else {
			if (flags.indexOf(flag) >= 0) {
				// Assume this works for now
				setItemValue(FLAGS_EXT_ITEM, flags.replace(flag + "", ""), FLAG_SIGN_SUMMARY);
			}
		}
	}

	protected boolean hasFlagExt(final char flag) {
		return getFlagsExt().indexOf(flag) >= 0;
	}

	//	private static final String NO_ENCODING = "<?xml version='1.0'?>";
	//	private static final String DEFAULT_ENCODING = "<?xml version='1.0'?>";

	/**
	 * Creates a transformer with the given file resource (in this package)
	 *
	 * @param resource
	 * @return
	 */
	protected static Transformer createTransformer(final String resource) {
		return XMLNode.createTransformer(AbstractDesignBase.class.getResourceAsStream(resource));
	}

	/**
	 * Returns, if the resourceName must be encoded
	 *
	 * @param resName
	 *            the resourceName
	 * @return true if the resourceName contains invalid characters
	 */
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

	/**
	 * Encodes the resource name, so that it is ODP-compatible
	 *
	 * @param resName
	 *            the resource name
	 * @return the encoded version (replaces / \ : * &gt; &lt; | " )
	 */
	protected String encodeResourceName(final String resName) {
		if (resName == null) {
			return null;
		}
		if (!mustEncode(resName)) {
			return resName;
		}
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

	//	/**
	//	 * Returns the folder of this designelemnt in the ODP
	//	 *
	//	 * @return the folder (e.g. Code/Java)
	//	 */
	//	@Override
	//	public abstract String getOnDiskFolder();

	/**
	 * Returns the name of this resource
	 *
	 * @return the name (e.g. org/openntf/myJavaClass)
	 */

	protected String getOnDiskName() {
		String odpName = getOdpMapping().getFileName();

		String extension = "";
		String ret;
		if (odpName == null) { // no name specified
			if (this instanceof DesignBaseNamed) {
				ret = encodeResourceName(((DesignBaseNamed) this).getName());
			} else {
				ret = getUniversalID();
			}
		} else if (odpName.startsWith("*")) {
			ret = ((DesignBaseNamed) this).getName();
		} else if (!odpName.startsWith(".")) {
			ret = odpName;
		} else {
			if (this instanceof DesignBaseNamed) {
				ret = encodeResourceName(((DesignBaseNamed) this).getName());
			} else {
				ret = getUniversalID();
			}
			extension = odpName;
		}
		if (!ret.endsWith(extension)) {
			ret = ret + extension;
		}
		return ret;
	}

	//	/**
	//	 * Returns the extension of the ODP-file (e.g. ".java")
	//	 *
	//	 * @return the extension
	//	 */
	//	protected String getOnDiskExtension() {
	//		return "";
	//	}

	protected ODPMapping getOdpMapping() {
		if (odpMapping_ == null) {
			odpMapping_ = ODPMapping.valueOf(getClass());
		}
		return odpMapping_;
	}

	/**
	 * Returns the full path in an ODP of this resoruce
	 *
	 * @return the full path (e.g. Code/Java/org/openntf/myJavaClass.java)
	 */
	public String getOnDiskPath() {
		String path = getOdpMapping().getFolder();
		if (path == null) {
			return null;
		}
		if (path.length() > 0) {
			path = path + "/" + getOnDiskName();
		} else {
			path = getOnDiskName();
		}
		return path;
	}

	/**
	 * Returns the transformer that should be used to clean up the dxl-output
	 *
	 * @return the transformer
	 */
	protected Transformer getOdpTransformer() {
		return null;
	}

	/**
	 * Returns the transformer that should be used to clean up the MetaData-Output
	 *
	 * @return the transformer for ".metadata" file
	 */
	protected Transformer getOdpMetaTransformer() {
		return ODP_META_TRANSFORMER;
	}

	// TODO
	@Override
	public void writeOnDiskFile(final File odpFile) throws IOException {
		getDxl().getXml(getOdpTransformer(), odpFile);
		odpFile.setLastModified(getDocLastModified().getTime());
	}

	// TODO
	public final void writeOnDiskMeta(final File odpFile) throws IOException {
		getDxl().getXml(getOdpMetaTransformer(), odpFile);
		odpFile.setLastModified(getDocLastModified().getTime());
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
		lastModified_ = document.getLastModifiedDate();
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
			return database_.getDocumentByUNID(universalId_);
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

	protected final XMLDocument getDxl() {
		if (dxl_ == null) {
			DxlExporter exporter = getAncestorSession().createDxlExporter();
			exporter.setOutputDOCTYPE(false);
			exporter.setForceNoteFormat(enforceRawFormat());

			// TODO: You will get an exporter error, if the design is protected. This should be handled correctly
			String xml = doExport(exporter);
			loadDxl(xml);
			if (dxl_ == null) {
				throw new IllegalStateException(getClass().getSimpleName() + ": Could not load DXL");
			}
			XMLNode docRoot = getDocumentElement();
			if (docRoot.getNodeName() == "note") {
				if (!enforceRawFormat()) {
					throw new UnsupportedOperationException(
							getClass().getSimpleName() + ": got note in RAW format. this was not expected. NoteID "
									+ (document_ == null ? "" : document_.getNoteID()));
				}
				dxlFormat_ = DxlFormat.RAWNOTE;
			} else {
				if (enforceRawFormat()) {
					throw new UnsupportedOperationException(
							getClass().getSimpleName() + ": Raw format was enforced, but we got a " + docRoot.getNodeName());
				}
				dxlFormat_ = DxlFormat.DXL;
			}
		}
		return dxl_;
	}

	protected String doExport(final DxlExporter exporter) {
		return exporter.exportDxl(document_);
	}

	protected final XMLNode getDocumentElement() {
		return getDxl().getDocumentElement();
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
	public boolean save() {

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
		setItemValue("$Class", designTemplateName, FLAG_SUMMARY); // Summary, don't sign
	}

	/**
	 * Sets the <code>value</code> of the Item with the given <code>itemName</code>
	 *
	 * @param itemName
	 *            the itemName
	 * @param value
	 *            the value to set
	 */
	public final void setItemValue(final String itemName, final Object value, final Set<ItemFlag> flags) {
		getDxlFormat(true); // load DXL before modification
		XMLNode node = getDxlNode("//item[@name='" + XMLDocument.escapeXPathValue(itemName) + "']");
		if (node == null) {
			node = getDxl().selectSingleNode("/*").addChildElement("item");
			node.setAttribute("name", itemName);
		} else {
			node.removeChildren();
		}

		if (flags.contains(ItemFlag._SIGN)) {
			node.setAttribute("sign", "true");
		} else {
			node.removeAttribute("sign");
		}
		if (flags.contains(ItemFlag._SUMMARY)) {
			node.removeAttribute("summary");
		} else {
			node.setAttribute("summary", "false");
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

	/*
	 * Helper
	 */
	private final void appendItemValueNode(final XMLNode node, final Object value) {
		XMLNode child;
		if (value instanceof Number) {
			child = node.addChildElement("number");
		} else {
			child = node.addChildElement("text");
		}
		child.setText(String.valueOf(value));
	}

	/**
	 * Reads the given item name and returns the containing string (can only read number and text items)
	 *
	 * @param itemName
	 *            the ItemName
	 * @return the values as String (if ItemName is a multi value item, the first value is returned)
	 */
	public final String getItemValueString(final String itemName) {
		if (dxlFormat_ == DxlFormat.NONE) {
			return document_.getItemValueString(itemName);
		}
		XMLNode node = getDxlNode("//item[@name='" + XMLDocument.escapeXPathValue(itemName) + "']");
		if (node != null) {
			node = node.selectSingleNode(".//number | .//text");
			if (node != null) {
				return node.getText();
			}
		}
		return "";
	}

	/**
	 * Reads the given item name and returns the containing strings, (can only read number and text items)
	 *
	 * @param itemName
	 *            the ItemName
	 * @param delimiter
	 *            the delimiter for multi values
	 * @return the values as String
	 */
	public final String getItemValueStrings(final String itemName, final String delimiter) {
		StringBuffer sb = new StringBuffer();

		if (dxlFormat_ == DxlFormat.NONE) {
			for (Object value : document_.getItemValue(itemName)) {
				if (sb.length() > 0) {
					sb.append(delimiter);
				}
				sb.append(value);
			}
		} else {
			XMLNode node = getDxlNode("//item[@name='" + XMLDocument.escapeXPathValue(itemName) + "']");
			if (node != null) {
				List<XMLNode> nodes = node.selectNodes(".//number | .//text");
				for (XMLNode child : nodes) {
					if (sb.length() > 0) {
						sb.append(delimiter);
					}
					sb.append(child.getText());
				}
			}
		}
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.design.FileResource#getItemNames()
	 */
	@Override
	public Collection<String> getItemNames() {
		Collection<String> result = new TreeSet<String>();

		for (XMLNode node : getDxl().selectNodes("//item")) {
			String itemName = node.getAttribute("name");
			if (!itemName.isEmpty()) {
				result.add(itemName);
			}
		}

		return result;
	}

	/**
	 * Reads the given item name and returns the containing string (can only read number and text items)
	 *
	 * @param itemName
	 *            the ItemName
	 * @return the values as List<Object> (text entries are returned as String, number entries as Double)
	 */
	public final List<Object> getItemValue(final String itemName) {
		if (getDxlFormat(true) == DxlFormat.NONE) {
			return document_.getItemValue(itemName);
		}
		List<Object> result = new ArrayList<Object>();
		XMLNode node = getDxlNode("//item[@name='" + XMLDocument.escapeXPathValue(itemName) + "']");
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

	/**
	 * Reads the given item name and returns the containing string (can only read number and text items)
	 *
	 * @param itemName
	 *            the ItemName
	 * @return the values as List<String>
	 */
	public final List<String> getItemValueStrings(final String itemName) {
		if (dxlFormat_ == DxlFormat.NONE) {
			return document_.getItemValues(itemName, String.class);
		}
		List<String> result = new ArrayList<String>();
		XMLNode node = getDxlNode("//item[@name='" + XMLDocument.escapeXPathValue(itemName) + "']");
		if (node != null) {
			List<XMLNode> nodes = node.selectNodes(".//number | .//text");
			for (XMLNode child : nodes) {
				result.add(child.getText());
			}
		}
		return result;
	}

	/**
	 * Returns the XML node that mathches the given XPath expression
	 *
	 * @param xpathString
	 *            the XPath
	 * @return the XMLNode
	 */
	public final XMLNode getDxlNode(final String xpathString) {
		return getDxl().selectSingleNode(xpathString);
	}

	// ----------- Serializable stuff ------------------
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

	public Date getDocLastModified() {
		return lastModified_;
	}

	@Override
	public String getDesignerVersion() {
		if (null == getDocument()) {
			return "";
		} else {
			return getDocument().getItemValueString("$DesignerVersioN");
		}
	}

}

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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import lotus.domino.NotesException;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.DxlExporter;
import org.openntf.domino.DxlExporter.RichTextOption;
import org.openntf.domino.DxlImporter;
import org.openntf.domino.design.DxlConverter;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.xml.XMLDocument;
import org.openntf.domino.utils.xml.XMLNode;
import org.xml.sax.SAXException;

/**
 * This is the Root class of all DesignNotes
 * 
 * @author jgallagher, Roland Praml
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractDesignDxlBase extends AbstractDesignBase {

	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(AbstractDesignDxlBase.class.getName());

	private static final char DESIGN_FLAG_PRESERVE = 'P';
	private static final char DESIGN_FLAG_PROPAGATE_NOCHANGE = 'r';
	private static final char DESIGN_FLAG_NEEDSREFRESH = '$';
	private static final char DESIGN_FLAG_HIDE_FROM_WEB = 'w';
	private static final char DESIGN_FLAG_HIDE_FROM_NOTES = 'n';

	protected static final String FLAGS_ITEM = "$Flags";
	protected static final String FLAGS_EXT_ITEM = "$FlagsExt";
	protected static final String TITLE_ITEM = "$TITLE";
	protected static final String ASSIST_TYPE = "$AssistType";

	private XMLDocument dxl_;

	private DxlFormat dxlFormat_ = DxlFormat.NONE;

	private int exportSize;

	/**
	 * Load an empty teemplate
	 */
	protected void loadTemplate() {
		loadDxl(getClass().getResourceAsStream(getClass().getSimpleName() + ".xml"));
	}

	/**
	 * Create a new DesginBase based on the given document. This Method will be invoked by {@link DesignFactory#fromDocument(Document)}
	 * 
	 * @param document
	 */
	@Override
	protected void init(final Document document) {
		setDocument(document);
	}

	/**
	 * Returns the {@link DxlFormat}
	 * 
	 * @param detect
	 *            true: means that the note is really exported
	 * @return {@link DxlFormat}
	 */
	protected DxlFormat getDxlFormat(final boolean detect) {
		if (detect)
			getDxl();
		return dxlFormat_;
	}

	/**
	 * To be implemented
	 * 
	 * @return true if this note should always be exported in RAW format
	 */
	protected abstract boolean enforceRawFormat();

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
			return super.isHideFromNotes();
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
			return super.isHideFromWeb();
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
			return super.isNeedsRefresh();
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
			return super.isPreventChanges();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#isPreventChanges()
	 */
	@Override
	public final boolean isPrivate() {
		switch (getDxlFormat(true)) {
		case DXL:
		case RAWNOTE:
			return "true".equals(getDocumentElement().getAttribute("private"));
		default:
			return getDocument().isPrivate();

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#isPreventChanges()
	 */
	@Override
	public final boolean isDefault() {
		switch (getDxlFormat(true)) {
		case DXL:
		case RAWNOTE:
			return "true".equals(getDocumentElement().getAttribute("default"));
		default:
			return getDocument().isDefault();
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
			return super.isPropagatePreventChanges();
		}
	}

	/*
	 * Helper for Non-raw Notes
	 */
	protected void setHide(final String platform, final boolean hide) {
		if (getDxlFormat(false) != DxlFormat.DXL)
			throw new IllegalStateException("Not in DXL Format");
		String platforms = getDxl().getFirstChild().getAttribute("hide");
		if (hide) {
			if (platforms.contains(platform))
				return;
			platforms += " " + platform;
		} else {
			if (!platforms.contains(platform))
				return;
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
			super.setHideFromNotes(hideFromNotes);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#setHideFromWeb(boolean)
	 */
	@Override
	public final void setHideFromWeb(final boolean hideFromWeb) {
		switch (getDxlFormat(true)) {
		case DXL:
			setHide("web", hideFromWeb);
		default:
			super.setHideFromWeb(hideFromWeb);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#setNeedsRefresh(boolean)
	 */
	@Override
	public final void setNeedsRefresh(final boolean needsRefresh) {
		switch (getDxlFormat(true)) {
		case DXL:
			getDocumentElement().setAttribute("refresh", String.valueOf(needsRefresh));
		default:
			super.setNeedsRefresh(needsRefresh);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#setPreventChanges(boolean)
	 */
	@Override
	public final void setPreventChanges(final boolean preventChanges) {
		switch (getDxlFormat(true)) {
		case DXL:
			getDocumentElement().setAttribute("noreplace", String.valueOf(preventChanges));
		default:
			super.setPreventChanges(preventChanges);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#setPropagatePreventChanges(boolean)
	 */
	@Override
	public final void setPropagatePreventChanges(final boolean propagatePreventChanges) {
		switch (getDxlFormat(true)) {
		case DXL:
			getDocumentElement().setAttribute("propagatenoreplace", String.valueOf(propagatePreventChanges));
		default:
			super.setPropagatePreventChanges(propagatePreventChanges);
		}
	}

	// -------------- Flags stuff

	/**
	 * Returns all flags of this DesignElement
	 * 
	 * @return
	 */
	@Override
	protected String getFlags() {
		if (getDxlFormat(true) != DxlFormat.RAWNOTE)
			throw new IllegalStateException("Flags are available only in DxlFormat.RAWNOTE");
		return getItemValueString(FLAGS_ITEM);
	}

	@Override
	protected void setFlags(final String flags) {
		if (getDxlFormat(true) != DxlFormat.RAWNOTE)
			throw new IllegalStateException("Flags are available only in DxlFormat.RAWNOTE");
		setItemValue(FLAGS_ITEM, flags, FLAG_SUMMARY);

	}

	/**
	 * Returns all extended-flags of this DesignElement
	 * 
	 * @return
	 */
	@Override
	protected String getFlagsExt() {
		if (getDxlFormat(true) != DxlFormat.RAWNOTE)
			throw new IllegalStateException("Flags are available only in DxlFormat.RAWNOTE");
		return getItemValueString(FLAGS_EXT_ITEM);
	}

	@Override
	protected void setFlagsExt(final String flags) {
		if (getDxlFormat(true) != DxlFormat.RAWNOTE)
			throw new IllegalStateException("Flags are available only in DxlFormat.RAWNOTE");
		setItemValue(FLAGS_EXT_ITEM, flags, FLAG_SUMMARY);

	}

	public final String getDesignTemplateName() {
		return getItemValueString(CLASS_ITEM);
	}

	public final void setDesignTemplateName(final String designTemplateName) {
		setItemValue(CLASS_ITEM, designTemplateName, FLAG_SUMMARY); // Summary, don't sign
	}

	//	/**
	//	 * Sets/clears a given exteded-flag character in this DesignElement
	//	 * 
	//	 * @param flag
	//	 *            the flag char
	//	 * @param enabled
	//	 *            if the flag should be set or cleared
	//	 */
	//	@Override
	//	protected void setFlagExt(final char flag, final boolean enabled) {
	//		String flags = getFlagsExt();
	//		if (enabled) {
	//			if (flags.indexOf(flag) < 0) {
	//				setItemValue(FLAGS_EXT_ITEM, flags + flag, FLAG_SIGN_SUMMARY);
	//			}
	//		} else {
	//			if (flags.indexOf(flag) >= 0) {
	//				// Assume this works for now
	//				setItemValue(FLAGS_EXT_ITEM, flags.replace(flag + "", ""), FLAG_SIGN_SUMMARY);
	//			}
	//		}
	//	}
	//
	//	/**
	//	 * Checks if a given flag is present
	//	 * 
	//	 * @param flag
	//	 *            the flag char
	//	 * @return true if the flag is present
	//	 */
	//	@Override
	//	protected boolean hasFlagExt(final char flag) {
	//		return getFlagsExt().indexOf(flag) >= 0;
	//	}
	//
	//	/**
	//	 * Returns the DesignMapping of this Design element
	//	 * 
	//	 * @return
	//	 */
	//	@Override
	//	public DesignFactory getMapping() {
	//		if (odpMapping_ == null) {
	//			odpMapping_ = DesignFactory.valueOf(getClass());
	//		}
	//		return odpMapping_;
	//	}

	@Override
	public void exportDesign(final DxlConverter converter, final OutputStream outputStream) throws IOException {
		if (converter.isRawExportEnabled()) {
			DxlExporter exporter = getAncestorSession().createDxlExporter();
			exporter.setOutputDOCTYPE(false);
			exporter.setForceNoteFormat(true);
			exporter.setRichTextOption(RichTextOption.RAW);
			PrintWriter pw = new PrintWriter(outputStream);
			pw.write(doExport(exporter));
			pw.flush();
		} else {
			converter.writeDesignXML(getDxl(), outputStream);
		}
	}

	@Override
	public int getExportSize(final DxlConverter converter) {
		if (exportSize == -1) {
			try {
				// do an export, just to count the bytes
				CountOutputStream os = new CountOutputStream();
				exportDesign(converter, os);
				exportSize = os.getCount();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		return exportSize;
	}

	@Override
	public void importDesign(final DxlConverter converter, final InputStream inputStream) throws IOException {
		dxl_ = converter.readDesignXML(inputStream);
		checkDxlFormat();
	}

	/**
	 * Creates or updates a meta file.
	 * 
	 * @param metaFile
	 *            The file that should be written.
	 * 
	 */
	public final void exportMeta(final DxlConverter converter, final OutputStream os) throws IOException {
		converter.writeMetaXML(getDxl(), os);
	}

	/**
	 * Reads the content of a meta file into the dxl content of this Design Element.
	 * 
	 * @param metaFile
	 *            The file that should be read.
	 */
	public final void importMeta(final DxlConverter converter, final InputStream is) throws IOException {
		if (is != null) {
			dxl_ = converter.readMetaXML(is);
			checkDxlFormat();
		} else {
			loadDxl(getClass().getResourceAsStream(getClass().getSimpleName() + ".xml"));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.openntf.domino.design.DesignBase#reattach(org.openntf.domino.Database)
	 */
	@Override
	public void reattach(final Database database) {
		super.reattach(database);
		exportSize = -1;
	}

	/**
	 * Frees up memory
	 */
	@Override
	public void flush() {
		dxl_ = null; // Free memory
		dxlFormat_ = DxlFormat.NONE;
		exportSize = -1;
	}

	/*
	 * (non-Javadoc)
	 * @see org.openntf.domino.design.DesignBase#setUniversalID(java.lang.String)
	 */
	@Override
	public void setUniversalID(final String unid) {
		super.setUniversalID(unid);
		XMLNode root = getDxl().getFirstChild();
		XMLNode node = root.selectSingleNode("//noteinfo");

		if (node == null) {
			XMLNode firstNode = root.getFirstChild();
			node = root.insertChildElementBefore("noteinfo", firstNode);
		}
		node.setAttribute("unid", unid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.Design#getUniversalID()
	 */
	@Override
	public final String getNoteID() {
		switch (getDxlFormat(false)) {
		case DXL:
		case RAWNOTE:
			XMLNode node = getDxl().selectSingleNode("//noteinfo");
			if (node != null) {
				return node.getAttribute("noteid");
			}
			return "";
		default:
			return getDocument().getNoteID();
		}
	}

	/**
	 * Returns the XML-Document representation
	 * 
	 * @return
	 */
	protected final XMLDocument getDxl() {
		if (dxl_ == null) {
			if (getDocument() == null) { // a new DesignNote
				loadTemplate();
			} else {
				DxlExporter exporter = getAncestorSession().createDxlExporter();
				exporter.setOutputDOCTYPE(false);
				exporter.setForceNoteFormat(enforceRawFormat());

				// TODO: You will get an exporter error, if the design is protected. This should be handled correctly
				String xml = doExport(exporter);
				loadDxl(xml);
			}
		}
		return dxl_;
	}

	/**
	 * Exports this note. Required for special notes like ACLNote
	 * 
	 * @param exporter
	 *            the exporter
	 * @return the DXL-String
	 */
	protected String doExport(final DxlExporter exporter) {
		return exporter.exportDxl(getDocument());
	}

	/**
	 * Returns the DOM-DocumentElement
	 * 
	 * @return
	 */
	protected final XMLNode getDocumentElement() {
		return getDxl().getDocumentElement();
	}

	/**
	 * Load DXL from given string
	 * 
	 * @param xml
	 */
	protected void loadDxl(final String xml) {
		dxl_ = new XMLDocument();
		try {
			dxl_.loadString(xml);
			checkDxlFormat();
		} catch (SAXException e) {
			DominoUtils.handleException(e);
		} catch (IOException e) {
			DominoUtils.handleException(e);
		} catch (ParserConfigurationException e) {
			DominoUtils.handleException(e);
		}
	}

	/**
	 * Load DXL from given InputStream
	 * 
	 * @param is
	 */
	protected final void loadDxl(final InputStream is) {
		dxl_ = new XMLDocument();
		try {
			dxl_.loadInputStream(is);
			checkDxlFormat();
		} catch (SAXException e) {
			DominoUtils.handleException(e);
		} catch (IOException e) {
			DominoUtils.handleException(e);
		} catch (ParserConfigurationException e) {
			DominoUtils.handleException(e);
		}
	}

	/**
	 * Check if the DXL is valid. i.E. if the correct DXL format is there.
	 */
	protected void checkDxlFormat() {
		if (dxl_ == null)
			throw new IllegalStateException(getClass().getSimpleName() + ": Could not load DXL");
		XMLNode docRoot = getDocumentElement();
		if (docRoot.getNodeName() == "note") {
			if (!enforceRawFormat()) {
				log_.severe(this + ": got note in RAW format. this was not expected.");
			}
			dxlFormat_ = DxlFormat.RAWNOTE;
		} else {
			if (enforceRawFormat()) {
				throw new UnsupportedOperationException(getClass().getSimpleName() + ": Raw format was enforced, but we got a "
						+ docRoot.getNodeName());
			}
			dxlFormat_ = DxlFormat.DXL;
		}
	}

	/**
	 * Saves the
	 */
	@SuppressWarnings("deprecation")
	@Override
	public boolean save(final DxlConverter converter) {

		DxlImporter importer = getAncestorSession().createDxlImporter();
		importer.setDesignImportOption(DxlImporter.DesignImportOption.REPLACE_ELSE_CREATE);
		importer.setCompileLotusScript(false);
		importer.setExitOnFirstFatalError(false);
		importer.setReplicaRequiredForReplaceOrUpdate(false);

		Database db = getAncestorDatabase();
		try {
			if (converter == null) {
				importer.importDxl(getDxl().getXml(), db);
			} else {
				importer.importDxl(converter.getDxlImportString(getDxl()), db);
			}
		} catch (IOException e) {
			DominoUtils.handleException(e);
			if (importer != null) {
				log_.severe("Error while importing " + this + "\n" + importer.getLog());
			}
			return false;
		}
		try {
			if (document_ != null) {
				//document has to be recycled manually here, in order to set a new Document afterwards.
				document_.recycle();
			}
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		// Reset the DXL so that it can pick up new noteinfo
		setDocument(db.getDocumentByID(importer.getFirstImportedNoteID()));
		return true;
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
		if (dxlFormat_ == DxlFormat.NONE)
			return getDocument().getItemValueString(itemName);
		XMLNode node = getDxlNode("//item[@name='" + XMLDocument.escapeXPathValue(itemName) + "']");
		if (node != null) {
			node = node.selectSingleNode(".//number | .//text");
			if (node != null)
				return node.getText();
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
			for (Object value : getDocument().getItemValue(itemName)) {
				if (sb.length() > 0)
					sb.append(delimiter);
				sb.append(value);
			}
		} else {
			XMLNode node = getDxlNode("//item[@name='" + XMLDocument.escapeXPathValue(itemName) + "']");
			if (node != null) {
				List<XMLNode> nodes = node.selectNodes(".//number | .//text");
				for (XMLNode child : nodes) {
					if (sb.length() > 0)
						sb.append(delimiter);
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
		if (dxlFormat_ == DxlFormat.NONE)
			return getDocument().getItemValue(itemName);
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
		if (dxlFormat_ == DxlFormat.NONE)
			return getDocument().getItemValues(itemName, String.class);
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

}

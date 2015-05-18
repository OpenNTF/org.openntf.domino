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
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.design.DesignBaseNamed;
import org.openntf.domino.design.DxlConverter;
import org.openntf.domino.ext.NoteClass;

import com.ibm.designer.domino.napi.NotesAPIException;
import com.ibm.designer.domino.napi.design.FileAccess;

/**
 * A abstract NAPI Implementation of DesignBaseNamed
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public abstract class AbstractDesignNapiBase extends AbstractDesignBase implements DesignBaseNamed {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(AbstractDesignNapiBase.class.getName());

	// These values will be serialized
	private String flags;
	private String flagsExt;
	private String name;
	private String alias;
	private String comment;
	private String designTemplateName;

	private transient int exportSize;

	@Override
	public void flush() {
		flags = null;
		flagsExt = null;
		name = null;
		alias = null;
		comment = null;
		designTemplateName = null;
		exportSize = -1;
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
	 * Called, when deserializing the object
	 * 
	 * @param in
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(final java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
	}

	/**
	 * Called, when serializing the object. Needed to support lazy initalization representation
	 * 
	 * @param out
	 * @throws IOException
	 */
	private void writeObject(final java.io.ObjectOutputStream out) throws IOException {
		if (getDocument() != null) {
			getFlags();
			getFlagsExt();
			getName();
			getAlias();
			getComment();
			getDesignTemplateName();

		}
		out.defaultWriteObject();
	}

	// ------------------------ napi stuff ------------------------
	// These are only (commented) wrappers to the FileAccess methods

	/**
	 * Reads the $FileData/$ImageData item.
	 * 
	 * @param os
	 *            the OutputStream
	 */
	protected static void nReadFileContent(final Document doc, final OutputStream os) throws IOException {
		try {
			FileAccess.readFileContent(doc.getNapiNote(), os);
		} catch (NotesAPIException e) {
			throw new IOException(e);
		}
	}

	/**
	 * Reads a nested file
	 * 
	 * @param fileItem
	 *            the item (e.g $ConfigData)
	 * @param os
	 *            the OutputStream
	 */
	protected static void nReadNestedFileContent(final Document doc, final String fileItem, final OutputStream os) throws IOException {
		try {
			FileAccess.readNestedFileContent(doc.getNapiNote(), fileItem, os);
		} catch (NotesAPIException e) {
			throw new IOException(e);
		}
	}

	/**
	 * Write a standard file (to $FileData item)
	 * 
	 * @param fileName
	 *            the fileName
	 * @param fileData
	 *            the fileData
	 * @throws IOException
	 */
	protected static void nSaveData(final Document doc, final String fileName, final byte[] fileData) throws IOException {
		try {
			FileAccess.saveData(doc.getNapiNote(), fileName, fileData);
		} catch (NotesAPIException e) {
			throw new IOException(e);
		}
	}

	/**
	 * Writes ImageData
	 * 
	 * @param imageData
	 * @throws IOException
	 */
	protected static void nSaveImageData(final Document doc, final byte[] imageData) throws IOException {
		try {
			FileAccess.saveImageContent(doc.getNapiNote(), imageData, "");
		} catch (NotesAPIException e) {
			throw new IOException(e);
		}
	}

	/**
	 * Writes a nested file
	 * 
	 * @param fileData
	 *            the fileData
	 * @param fileItem
	 *            the file item name (e.g. $ConfigData)
	 * @param sizeItem
	 *            the size item name (e.g. $ConfigSize)
	 * @throws IOException
	 */
	protected static void nSaveNestedByteData(final Document doc, final byte[] fileData, final String fileItem, final String sizeItem)
			throws IOException {
		try {
			FileAccess.saveNestedByteData(doc.getNapiNote(), fileData, fileItem, sizeItem);
		} catch (NotesAPIException e) {
			throw new IOException(e);
		}
	}

	/**
	 * 
	 * @param fileName
	 * @param data
	 * @param fileItem
	 * @param sizeItem
	 * @throws IOException
	 */
	protected static void nSaveStringData(final Document doc, final String fileName, final String data, final String fileItem,
			final String sizeItem) throws IOException {
		try {
			FileAccess.saveStringData(doc.getNapiNote(), fileName, data, fileItem, sizeItem);
		} catch (NotesAPIException e) {
			throw new IOException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.openntf.domino.design.DesignBaseNamed#getName()
	 */
	@Override
	public String getName() {
		if (name == null) {
			Document doc = getDocument();
			if (doc != null) {
				String title = doc.getItemValueString(TITLE_ITEM);
				int pos = title.indexOf('|');
				if (pos < 0) {
					name = title;
				} else {
					name = title.substring(0, pos);
				}

			} else {
				name = "";
			}
		}
		return name;
	}

	/*
	 * (non-Javadoc)
	 * @see org.openntf.domino.design.DesignBaseNamed#setName(java.lang.String)
	 */
	@Override
	public void setName(final String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * @see org.openntf.domino.design.DesignBaseNamed#getAlias()
	 */
	@Override
	public String getAlias() {
		if (alias == null) {
			Document doc = getDocument();
			if (doc != null) {
				String title = doc.getItemValueString(TITLE_ITEM);
				int pos = title.indexOf('|');
				if (pos < 0) {
					alias = "";
				} else {
					alias = title.substring(pos + 1);
				}

			} else {
				alias = "";
			}
		}
		return alias;
	}

	/*
	 * (non-Javadoc)
	 * @see org.openntf.domino.design.DesignBaseNamed#setAlias(java.lang.String)
	 */
	@Override
	public void setAlias(final String alias) {
		this.alias = alias;
	}

	public String getComment() {
		if (comment == null) {
			Document doc = getDocument();
			if (doc != null) {
				comment = doc.getItemValueString(COMMENT_ITEM);
			} else {
				comment = "";
			}
		}
		return comment;
	}

	public void setComment(final String comment) {
		this.comment = comment;
	}

	@Override
	public String getDesignTemplateName() {
		if (designTemplateName == null) {
			Document doc = getDocument();
			if (doc != null) {
				designTemplateName = doc.getItemValueString(CLASS_ITEM);
			} else {
				designTemplateName = "";
			}
		}
		return designTemplateName;
	}

	/*
	 * (non-Javadoc)
	 * @see org.openntf.domino.design.DesignBaseNamed#setDesignTemplateName(java.lang.String)
	 */
	@Override
	public void setDesignTemplateName(final String templateName) {
		this.designTemplateName = templateName;
	}

	/*
	 * (non-Javadoc)
	 * @see org.openntf.domino.design.DesignBaseNamed#getAliases()
	 */
	@Override
	public List<String> getAliases() {
		return Arrays.asList(getAlias().split("\\|"));
	}

	/*
	 * (non-Javadoc)
	 * @see org.openntf.domino.design.DesignBaseNamed#setAliases(java.lang.Iterable)
	 */
	@Override
	public void setAliases(final Iterable<String> aliases) {
		StringBuilder sb = new StringBuilder();
		for (String s : aliases) {
			if (sb.length() > 0)
				sb.append('|');
			sb.append(s);
		}
		setAlias(sb.toString());
	}

	public boolean isReadOnly() {
		return hasFlag(DESIGN_FLAG_READONLY);
	}

	public void setReadOnly(final boolean readOnly) {
		setFlag(DESIGN_FLAG_READONLY, readOnly);
	}

	public boolean isDeployable() {
		return hasFlagExt(DESIGN_FLAGEXT_FILE_DEPLOYABLE);
	}

	public void setDeployable(final boolean deployable) {
		setFlagExt(DESIGN_FLAGEXT_FILE_DEPLOYABLE, deployable);
	}

	/**
	 * Returns the exact size of the element. This is required by FOCONIS WebDAV implementation
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.FileResource#getItemNames()
	 */
	@Override
	public Collection<String> getItemNames() {
		return getDocument().keySet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.Design#getUniversalID()
	 */
	@Override
	public final String getNoteID() {
		return getDocument().getNoteID();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#isPreventChanges()
	 */
	@Override
	public final boolean isDefault() {
		return getDocument().isDefault();
	}

	/**
	 * Read the flag item from the document
	 */
	@Override
	protected String getFlags() {
		if (flags == null) {
			Document doc = getDocument();
			if (doc != null) {
				flags = getDocument().getItemValueString(FLAGS_ITEM);
			} else {
				flags = getDefaultFlags();
			}
		}
		return flags;
	}

	/**
	 * Stores the flags
	 */
	@Override
	protected void setFlags(final String flags) {
		this.flags = flags;
	}

	/**
	 * Reads the flagsExt from document
	 */
	@Override
	protected String getFlagsExt() {
		if (flagsExt == null) {
			Document doc = getDocument();
			if (doc != null) {
				flagsExt = getDocument().getItemValueString(FLAGS_EXT_ITEM);
			} else {
				flagsExt = getDefaultFlagsExt();
			}
		}
		return flagsExt;
	}

	/**
	 * sets the flagsExt
	 */
	@Override
	protected void setFlagsExt(final String flagsExt) {
		this.flagsExt = flagsExt;
	}

	protected abstract String getDefaultFlags();

	protected abstract String getDefaultFlagsExt();

	/**
	 * Helper method to set values. If value is empty, the item will be removed
	 * 
	 * @param doc
	 *            the document
	 * @param name
	 *            the itemName
	 * @param value
	 *            the value
	 */
	protected static void setValue(final Document doc, final String name, final String value) {
		if (value == null || "".equals(value)) {
			doc.removeItem(name);
		} else {
			doc.replaceItemValue(name, value);
		}
	}

	protected Document createNewDocument() throws IOException {
		Document doc = getAncestorDatabase().createDocument();
		try {
			NoteClass nc = getMapping().getNoteClass();
			doc.getNapiNote().setClass(nc.nativeValue);
		} catch (NotesAPIException e) {
			throw new IOException(e);
		}
		return doc;

	}

	/**
	 * Saves the
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	@Override
	public boolean save(final DxlConverter converter) throws IOException {
		Document doc = getDocument();
		if (doc == null) {
			doc = createNewDocument();
		}

		// Flags
		setValue(doc, FLAGS_ITEM, getFlags());
		setValue(doc, FLAGS_EXT_ITEM, getFlags());
		// DesignerVersion
		setValue(doc, DESIGNER_VERSION_ITEM, DESIGNER_VERSION_VALUE);

		// Title
		String title = getAlias();
		if ("".equals(title)) {
			title = getName();
		} else {
			title = getName() + "|" + title;
		}
		setValue(doc, TITLE_ITEM, title);

		setValue(doc, COMMENT_ITEM, getComment());
		setValue(doc, CLASS_ITEM, getDesignTemplateName());

		// save additional datas
		saveData(converter, doc);

		setDocument(doc);
		return doc.save();
	}

	protected abstract void saveData(final DxlConverter converter, final Document doc) throws IOException;

	@Override
	public boolean isPrivate() {
		return getDocument().isPrivate();
	}

	public void exportMeta(final DxlConverter converter, final OutputStream os) throws IOException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	public void importMeta(final DxlConverter converter, final InputStream is) throws IOException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

}
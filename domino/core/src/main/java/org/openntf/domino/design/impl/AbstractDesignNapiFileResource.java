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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import org.openntf.domino.Document;
import org.openntf.domino.design.AnyFileResource;
import org.openntf.domino.design.DesignBaseNamed;
import org.openntf.domino.design.DxlConverter;
import org.openntf.domino.utils.DominoUtils;

/**
 * Abstract Design File Resource
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public abstract class AbstractDesignNapiFileResource extends AbstractDesignNapiBase implements DesignBaseNamed, AnyFileResource {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(AbstractDesignNapiFileResource.class.getName());

	private String mimeType;
	protected byte[] fileData;

	@Override
	public void flush() {
		super.flush();
		mimeType = null;
		fileData = null;
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
		if (getDocument() != null) {
			getMimeType();

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			getFileData(bos);
			fileData = bos.toByteArray();
			bos.close();
		}
		out.defaultWriteObject();
	}

	// ------------------------ napi stuff ------------------------

	@Override
	public byte[] getFileData() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			getFileData(bos);
		} catch (IOException e) {
			DominoUtils.handleException(e);
		}
		return bos.toByteArray();
	}

	/**
	 * Return the FileData
	 * 
	 * @param os
	 * @throws IOException
	 */
	@Override
	public void getFileData(final OutputStream os) throws IOException {
		if (fileData != null) {
			os.write(fileData);
		} else {
			nReadFileContent(getDocument(), os);
		}
	}

	/**
	 * Set the FileData
	 * 
	 * @param newFileData
	 * @throws IOException
	 */
	@Override
	public void setFileData(final byte[] newFileData) throws IOException {
		fileData = newFileData;
	}

	@Override
	public void exportDesign(final DxlConverter dxlConverter, final OutputStream outputStream) throws IOException {
		getFileData(outputStream);
	}

	@Override
	public void importDesign(final DxlConverter dxlConverter, final InputStream inputStream) throws IOException {
		setFileData(toBytes(inputStream));
	}

	// Setter
	@Override
	public String getMimeType() {
		if (mimeType == null) {
			Document doc = getDocument();
			if (doc != null) {
				mimeType = getDocument().getItemValue(MIMETYPE_ITEM, String.class);
			} else {
				mimeType = "";
			}
		}
		return mimeType;
	}

	@Override
	public void setMimeType(final String mimeType) {
		this.mimeType = mimeType;
	}

	@Override
	public int getExportSize(final DxlConverter converter) {
		if (fileData == null) {
			return getDocument().getItemValueInteger(DEFAULT_FILESIZE_FIELD);
		} else {
			return fileData.length;
		}
	}

	@Override
	protected void saveData(final DxlConverter converter, final Document doc) throws IOException {
		if (fileData != null) {
			nSaveData(doc, getName(), fileData);
		}
	}

}
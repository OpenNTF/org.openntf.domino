/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.design.impl;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.design.XspXmlContent;
import org.openntf.domino.nsfdata.structs.cd.CData;
import org.openntf.domino.nsfdata.structs.obj.CDObject;
import org.openntf.domino.nsfdata.structs.obj.CDResourceEvent;
import org.openntf.domino.nsfdata.structs.obj.CDResourceFile;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.xml.XMLDocument;
import org.openntf.domino.utils.xml.XMLNode;

public abstract class AbstractDesignFileResource extends AbstractDesignBaseNamed implements org.openntf.domino.design.AnyFileResource {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(AbstractDesignFileResource.class.getName());

	/* for web apps, this file is ready for primetime */
	private static final char DESIGN_FLAGEXT_FILE_DEPLOYABLE = 'D';
	private static final char DESIGN_FLAG_READONLY = '&';

	private static final String DEFAULT_FILEDATA_FIELD = "$FileData";
	private static final String MIMETYPE_FIELD = "$MimeType";

	protected AbstractDesignFileResource(final Document document) {
		super(document);
	}

	protected AbstractDesignFileResource(final Database database) {
		super(database);
		// TODO What is this?
		try {
			InputStream is = DesignView.class.getResourceAsStream("/org/openntf/domino/design/impl/dxl_fileresource.xml");
			loadDxl(is);
			is.close();
		} catch (IOException e) {
			DominoUtils.handleException(e);
		}
	}

	protected AbstractDesignFileResource(final Database database, final String dxlResource) {
		super(database);

		try {
			InputStream is = DesignView.class.getResourceAsStream(dxlResource);
			loadDxl(is);
			is.close();
		} catch (IOException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 *  (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.FileResource#getFileData(java.lang.String)
	 */
	@Override
	public byte[] getFileData() {
		switch (getDxlFormat(true)) {
		case DXL:
			String rawData = getDxl().selectSingleNode("//filedata").getText();
			return parseBase64Binary(rawData);
		default:
			return getFileDataRaw(DEFAULT_FILEDATA_FIELD);

		}
	}

	/**
	 * Reads a FileData Item in RAW-mode
	 * 
	 * @param itemName
	 * @return
	 */
	protected byte[] getFileDataRaw(final String itemName) {
		try {
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			for (XMLNode rawitemdata : getDxl().selectNodes(//
					"//item[@name='" + XMLDocument.escapeXPathValue(itemName) + "']/rawitemdata")) {

				String rawData = rawitemdata.getText();
				byte[] thisData = parseBase64Binary(rawData);
				byteStream.write(thisData);
			}

			if (byteStream.size() > 0) {
				byte[] data = byteStream.toByteArray();
				CData cdata = new CData(data);
				CDObject obj = CDObject.create(cdata);
				// Files may be attached either as FILE or as EVENT... (ssjs for example) Damn. This makes everything quite complex
				if (obj instanceof CDResourceFile)
					return ((CDResourceFile) obj).getFileData();
				if (obj instanceof CDResourceEvent)
					return ((CDResourceEvent) obj).getFileData();
				throw new IllegalStateException("Cannot decode " + obj.getClass().getName());
			} else {
				byteStream = new ByteArrayOutputStream();
				for (XMLNode rawitemdata : getDxl().selectNodes(//
						"//file[@name='" + XMLDocument.escapeXPathValue(itemName) + "']/filedata")) {

					String rawData = rawitemdata.getText();
					byte[] thisData = parseBase64Binary(rawData);
					byteStream.write(thisData);
				}
				return byteStream.toByteArray();

			}
		} catch (IOException ioe) {
			DominoUtils.handleException(ioe);
			return null;
		}
	}

	@Override
	public void setFileData(final byte[] data) {
		switch (getDxlFormat(true)) {
		case DXL:
			XMLNode filedata = getDxl().selectSingleNode("//filedata");
			if (filedata == null) {
				filedata = getDxl().selectSingleNode("/*").addChildElement("filedata");
			}
			filedata.setText(printBase64Binary(data));
		default:
			setFileDataRaw(DEFAULT_FILEDATA_FIELD, data);
		}
	}

	protected void setFileDataRaw(final String itemName, final byte[] fileData) {
		// To set the file content, first clear out existing content
		List<XMLNode> fileDataNodes = getDxl().selectNodes("//item[@name='" + XMLDocument.escapeXPathValue(itemName) + "']");
		for (int i = fileDataNodes.size() - 1; i >= 0; i--) {
			fileDataNodes.get(i).getParentNode().removeChild(fileDataNodes.get(i));
		}

		// Now create a CD record for the file data
		//			CDResourceFile record = CDResourceFile.fromFileData(fileData, "");
		//			byte[] reconData = record.getBytes();
		CDResourceFile record = new CDResourceFile("");
		record.setFileData(fileData);
		byte[] reconData = record.getData().array();

		// Write out the first chunk
		int firstChunk = reconData.length > 20544 ? 20544 : reconData.length;
		String firstChunkData = printBase64Binary(Arrays.copyOfRange(reconData, 0, firstChunk));
		XMLNode documentNode = getDxl().selectSingleNode("//note");
		XMLNode fileDataNode = documentNode.addChildElement("item");
		fileDataNode.setAttribute("name", itemName);
		fileDataNode = fileDataNode.addChildElement("rawitemdata");
		fileDataNode.setAttribute("type", "1");
		fileDataNode.setText(firstChunkData);

		// Write out any remaining chunks
		int remaining = reconData.length - firstChunk;
		int chunks = remaining / 20516;
		if (remaining % 20516 > 0) {
			chunks++;
		}
		int offset = firstChunk;
		for (int i = 0; i < chunks; i++) {
			int chunkSize = remaining > 20516 ? 20516 : remaining;
			String chunkData = printBase64Binary(Arrays.copyOfRange(reconData, offset, offset + chunkSize));

			fileDataNode = documentNode.addChildElement("item");
			fileDataNode.setAttribute("name", itemName);
			fileDataNode = fileDataNode.addChildElement("rawitemdata");
			fileDataNode.setAttribute("type", "1");
			fileDataNode.setText(chunkData);

			remaining -= 20516;
			offset += chunkSize;
		}

		// Also set the file size if we're setting the main field
		if (DEFAULT_FILEDATA_FIELD.equals(itemName)) {
			setItemValue("$FileSize", String.valueOf(fileData.length), FLAG_SIGN_SUMMARY);
		}
	}

	@Override
	public String getMimeType() {
		switch (getDxlFormat(false)) {
		case DXL:
			return getDocumentElement().getAttribute("mimetype");
		default:
			return getItemValueString(MIMETYPE_FIELD);
		}
	}

	@Override
	public void setMimeType(final String mimeType) {
		switch (getDxlFormat(true)) {
		case DXL:
			getDocumentElement().setAttribute("mimetype", mimeType);
		default:
			setItemValue(MIMETYPE_FIELD, mimeType, FLAG_SUMMARY);
		}
	}

	@Override
	public void setName(final String title) {
		super.setName(title);
		// Also set the $FileNames field
		setItemValue("$FileNames", title, FLAG_SIGN_SUMMARY);
	}

	/**
	 * The ODS file that is written here is the file content
	 */

	@Override
	public void writeOnDiskFile(final Path odpFile) throws IOException {
		try(OutputStream os = Files.newOutputStream(odpFile)) {
			os.write(getFileData());
		}
		Files.setLastModifiedTime(odpFile, FileTime.from(Instant.ofEpochMilli(getDocLastModified().getTime())));
	}

	// TODO: map this to DXL
	@Override
	public boolean isReadOnly() {
		return hasFlag(DESIGN_FLAG_READONLY);
	}

	@Override
	public void setReadOnly(final boolean readOnly) {
		setFlag(DESIGN_FLAG_READONLY, readOnly);
	}

	@Override
	public boolean isDeployable() {
		return hasFlagExt(DESIGN_FLAGEXT_FILE_DEPLOYABLE);
	}

	@Override
	public void setDeployable(final boolean deployable) {
	}

	public <T extends XspXmlContent> T getAsXml(final Class<T> schema) {
		try {
			Constructor<T> ctor = schema.getConstructor(FileResource.class);
			return ctor.newInstance(this);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return null;
	}
}
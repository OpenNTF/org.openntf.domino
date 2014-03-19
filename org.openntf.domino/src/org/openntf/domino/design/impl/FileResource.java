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

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.design.cd.CDResourceFile;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.xml.XMLNode;

public class FileResource extends AbstractDesignNoteBase implements org.openntf.domino.design.FileResource {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(FileResource.class.getName());

	private static final String DESIGN_FLAGEXT_FILE_DEPLOYABLE = "D";
	private static final String DESIGN_FLAG_HIDEFROMDESIGNLIST = "~";
	private static final String DESIGN_FLAG_READONLY = "&";

	private static final String DEFAULT_FILEDATA_FIELD = "$FileData";

	protected FileResource(final Document document) {
		super(document);
	}

	protected FileResource(final Database database) {
		super(database);

		try {
			InputStream is = DesignView.class.getResourceAsStream("/org/openntf/domino/design/impl/dxl_fileresource.xml");
			loadDxl(is);
			is.close();
		} catch (IOException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public byte[] getFileData() {
		return getFileData(DEFAULT_FILEDATA_FIELD);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.FileResource#getFileData(java.lang.String)
	 */
	@Override
	public byte[] getFileData(final String itemName) {
		try {
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			for (XMLNode rawitemdata : getDxl().selectNodes("//item[@name='" + escapeXPathValue(itemName) + "']/rawitemdata")) {
				String rawData = rawitemdata.getText();
				byte[] thisData = parseBase64Binary(rawData);
				byteStream.write(thisData);
			}
			byte[] data = byteStream.toByteArray();

			CDResourceFile resourceFile = new CDResourceFile(data);
			return resourceFile.getData();
		} catch (IOException ioe) {
			DominoUtils.handleException(ioe);
			return null;
		}
	}

	public void setFileData(final byte[] fileData) {
		setFileData(DEFAULT_FILEDATA_FIELD, fileData);
	}

	@Override
	public void setFileData(final String itemName, final byte[] fileData) {
		try {
			// To set the file content, first clear out existing content
			List<XMLNode> fileDataNodes = getDxl().selectNodes("//item[@name='" + escapeXPathValue(itemName) + "']");
			for (int i = fileDataNodes.size() - 1; i >= 0; i--) {
				fileDataNodes.get(i).getParentNode().removeChild(fileDataNodes.get(i));
			}

			// Now create a CD record for the file data
			CDResourceFile record = CDResourceFile.fromFileData(fileData, "");
			byte[] reconData = record.getBytes();

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
				XMLNode fileSizeNode = getDocumentElement().selectSingleNode("//item[@name='$FileSize']");
				if (fileSizeNode == null) {
					fileSizeNode = getDocumentElement().addChildElement("item");
					fileSizeNode.setAttribute("name", "$FileSize");
					fileSizeNode.setAttribute("sign", "true");
					fileSizeNode = fileSizeNode.addChildElement("number");
				} else {
					fileSizeNode = fileSizeNode.selectSingleNode("number");
				}
				fileSizeNode.setText(String.valueOf(fileData.length));
			}
		} catch (IOException ioe) {
			DominoUtils.handleException(ioe);
		}
	}

	@Override
	public String getMimeType() {
		return getMimeTypeNode().getText();
	}

	@Override
	public void setMimeType(final String mimeType) {
		getMimeTypeNode().setText(mimeType);
	}

	@Override
	public void setName(final String title) {
		super.setName(title);

		// Also set the $FileNames field
		XMLNode fileNamesNode = getDocumentElement().selectSingleNode("//item[@name='$FileNames']");
		if (fileNamesNode == null) {
			fileNamesNode = getDocumentElement().addChildElement("item");
			fileNamesNode.setAttribute("name", "$FileNames");
			fileNamesNode.setAttribute("sign", "true");
			fileNamesNode = fileNamesNode.addChildElement("text");
		} else {
			fileNamesNode = fileNamesNode.selectSingleNode("text");
		}
		fileNamesNode.setText(title);
	}

	@Override
	public boolean isReadOnly() {
		return getFlags().contains(DESIGN_FLAG_READONLY);
	}

	@Override
	public boolean isDeployable() {
		return getFlagsExt().contains(DESIGN_FLAGEXT_FILE_DEPLOYABLE);
	}

	@Override
	public boolean isHideFromDesignList() {
		return getFlags().contains(DESIGN_FLAG_HIDEFROMDESIGNLIST);
	}

	@Override
	public void setReadOnly(final boolean readOnly) {
		if (readOnly) {
			addFlag(DESIGN_FLAG_READONLY);
		} else {
			removeFlag(DESIGN_FLAG_READONLY);
		}
	}

	public void setDeployable(final boolean deployable) {
		if (deployable) {
			addFlag(DESIGN_FLAGEXT_FILE_DEPLOYABLE);
		} else {
			removeFlag(DESIGN_FLAGEXT_FILE_DEPLOYABLE);
		}
	}

	public void setHideFromDesignList(final boolean hideFromDesignList) {
		if (hideFromDesignList) {
			addFlag(DESIGN_FLAG_HIDEFROMDESIGNLIST);
		} else {
			removeFlag(DESIGN_FLAG_HIDEFROMDESIGNLIST);
		}
	}

	private XMLNode getMimeTypeNode() {
		XMLNode dataNode = getDxl().selectSingleNode("//item[@name='$MimeType']");
		if (dataNode == null) {
			dataNode = getDocumentElement().addChildElement("item");
			dataNode.setAttribute("name", "$MimeType");
			dataNode = dataNode.addChildElement("text");
		} else {
			dataNode = dataNode.selectSingleNode("text");
		}
		return dataNode;
	}

	protected String escapeXPathValue(final String input) {
		return input.replace("'", "\\'");
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
}
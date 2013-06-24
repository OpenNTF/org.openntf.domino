package org.openntf.domino.design.impl;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
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
		try {
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			for (XMLNode rawitemdata : getDxl().selectNodes("//item[@name='$FileData']/rawitemdata")) {
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

	@Override
	public void setFileData(final byte[] fileData) {
		try {
			// To set the file content, first clear out existing content
			List<XMLNode> fileDataNodes = getDxl().selectNodes("//item[@name='$FileData']");
			for (int i = fileDataNodes.size() - 1; i >= 0; i++) {
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
			fileDataNode.setAttribute("name", "$FileData");
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
				fileDataNode.setAttribute("name", "$FileData");
				fileDataNode = fileDataNode.addChildElement("rawitemdata");
				fileDataNode.setAttribute("type", "1");
				fileDataNode.setText(chunkData);

				remaining -= 20516;
				offset += chunkSize;
			}

			// Also set the file size
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
		return getFlags().contains("&");
	}

	public boolean isDeployable() {
		return getFlagsExt().contains(DESIGN_FLAGEXT_FILE_DEPLOYABLE);
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
}
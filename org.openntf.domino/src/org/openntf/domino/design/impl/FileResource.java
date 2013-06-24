package org.openntf.domino.design.impl;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
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
		return parseBase64Binary(getFileDataNode().getTextContent());
	}

	@Override
	public void setFileData(final byte[] fileData) {
		String base64 = printBase64Binary(fileData);
		getFileDataNode().setTextContent("\n" + base64 + "\n");

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

	private XMLNode getFileDataNode() {
		XMLNode dataNode = getDxl().selectSingleNode("//item[@name='$FileData']");
		if (dataNode == null) {
			dataNode = getDocumentElement().addChildElement("item");
			dataNode.setAttribute("name", "$FileData");
			dataNode = dataNode.addChildElement("rawitemdata");
			dataNode.setAttribute("type", "1");
		} else {
			dataNode = dataNode.selectSingleNode("rawitemdata");
		}
		return dataNode;
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
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.nsfdata.structs.cd.CData;
import org.openntf.domino.nsfdata.structs.obj.CDObject;
import org.openntf.domino.nsfdata.structs.obj.CDResourceEvent;
import org.openntf.domino.nsfdata.structs.obj.CDResourceFile;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.xml.XMLDocument;
import org.openntf.domino.utils.xml.XMLNode;

/**
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public abstract class AbstractDesignFileResource extends AbstractDesignBaseNamed {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(AbstractDesignFileResource.class.getName());

	/* for web apps, this file is ready for primetime */
	private static final char DESIGN_FLAGEXT_FILE_DEPLOYABLE = 'D';
	private static final char DESIGN_FLAG_READONLY = '&';

	public static final String DEFAULT_FILEDATA_FIELD = "$FileData";
	public static final String DEFAULT_CONFIGDATA_FIELD = "$ConfigData";
	public static final String DEFAULT_FILESIZE_FIELD = "$FileSize";
	public static final String DEFAULT_CONFIGSIZE_FIELD = "$ConfigSize";

	private static final String MIMETYPE_FIELD = "$MimeType";

	protected void init(final Database database, final String dxlResource) {
		super.init(database);

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

	public void setFileData(final byte[] data) {
		switch (getDxlFormat(true)) {
		case DXL:
			XMLNode filedata = getDxl().selectSingleNode("//filedata");
			if (filedata == null) {
				filedata = getDxl().selectSingleNode("/*").addChildElement("filedata");
			}
			if (data.length == 0) {
				//cannot import empty filedata - node
				filedata.setText(printBase64Binary(new byte[] { 32 }));
			} else {
				filedata.setText(printBase64Binary(data));
			}
			break;
		default:
			if (data.length == 0) {
				//cannot import empty filedata - node
				setFileDataRaw(DEFAULT_FILEDATA_FIELD, new byte[] { 32 });
			} else {
				setFileDataRaw(DEFAULT_FILEDATA_FIELD, data);
			}

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

		for (ByteBuffer chk : record.getChunks()) {
			XMLNode documentNode = getDxl().selectSingleNode("//note");
			XMLNode fileDataNode = documentNode.addChildElement("item");
			fileDataNode.setAttribute("name", itemName);
			fileDataNode = fileDataNode.addChildElement("rawitemdata");
			fileDataNode.setAttribute("type", "1");
			fileDataNode.setText(printBase64Binary(chk.array()));
		}

		// Also set the file size if we're setting the main field
		if (DEFAULT_FILEDATA_FIELD.equals(itemName)) {
			setItemValue(DEFAULT_FILESIZE_FIELD, String.valueOf(fileData.length), FLAG_SIGN_SUMMARY);
		} else if (DEFAULT_CONFIGDATA_FIELD.equals(itemName)) {
			setItemValue(DEFAULT_CONFIGSIZE_FIELD, String.valueOf(fileData.length), FLAG_SIGN_SUMMARY);
		}
	}

	public String getMimeType() {
		switch (getDxlFormat(false)) {
		case DXL:
			return getDocumentElement().getAttribute("mimetype");
		default:
			return getItemValueString(MIMETYPE_FIELD);
		}
	}

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
		if (enforceRawFormat()) {
			setItemValue("$FileNames", title, FLAG_SIGN_SUMMARY);
		}
	}

	/**
	 * The ODS file that is written here is the file content
	 */

	@Override
	public boolean writeOnDiskFile(final File file, final boolean useTransformer) throws IOException {
		FileOutputStream fo = new FileOutputStream(file);
		fo.write(getFileData());
		fo.close();
		updateLastModified(file);
		return true;
	}

	@Override
	public boolean readOnDiskFile(final File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			byte[] data = new byte[(int) file.length()];
			fis.read(data);
			fis.close();
			setFileData(data);
		} catch (IOException e) {
			DominoUtils.handleException(e);
		}
		return true;
	}

	// TODO: map this to DXL
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
		// TODO Auto-generated method stub
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
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.TreeSet;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.xml.XMLNode;

public abstract class AbstractDesginFileResource extends AbstractDesignBaseNamed implements org.openntf.domino.design.FileResource {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(AbstractDesginFileResource.class.getName());

	private static final char DESIGN_FLAGEXT_FILE_DEPLOYABLE = 'D';
	private static final char DESIGN_FLAG_HIDEFROMDESIGNLIST = '~';
	private static final char DESIGN_FLAG_READONLY = '&';

	//private static final String MIMETYPE_FIELD = "$MimeType";

	protected AbstractDesginFileResource(final Document document) {
		super(document);
	}

	protected AbstractDesginFileResource(final Database database) {
		super(database);

		try {
			InputStream is = DesignView.class.getResourceAsStream("/org/openntf/domino/design/impl/dxl_fileresource.xml");
			loadDxl(is);
			is.close();
		} catch (IOException e) {
			DominoUtils.handleException(e);
		}
	}

	protected AbstractDesginFileResource(final Database database, final String dxlResource) {
		super(database);

		try {
			InputStream is = DesignView.class.getResourceAsStream(dxlResource);
			loadDxl(is);
			is.close();
		} catch (IOException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public byte[] getFileData() {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		XMLNode filedata = getDxl().selectSingleNode("//filedata");
		String rawData = filedata.getText();
		return parseBase64Binary(rawData);
	}

	@Override
	public void setFileData(final byte[] data) {
		XMLNode filedata = getDxl().selectSingleNode("//filedata");
		if (filedata == null) {
			filedata = getDxl().selectSingleNode("/*").addChildElement("filedata");
		}
		filedata.setText(printBase64Binary(data));
	}

	@Override
	public String getMimeType() {
		//return getItemValueString(MIMETYPE_FIELD);
		return null;
	}

	@Override
	public void setMimeType(final String mimeType) {
		//setItemValue(MIMETYPE_FIELD, mimeType);
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
		return hasFlag(DESIGN_FLAG_READONLY);
	}

	private boolean hasFlag(final char designFlagReadonly) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDeployable() {
		return hasFlagExt(DESIGN_FLAGEXT_FILE_DEPLOYABLE);
	}

	private boolean hasFlagExt(final char designFlagextFileDeployable) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isHideFromDesignList() {
		return hasFlag(DESIGN_FLAG_HIDEFROMDESIGNLIST);
	}

	@Override
	public void setReadOnly(final boolean readOnly) {
		//setFlag(DESIGN_FLAG_READONLY, readOnly);

	}

	@Override
	public void setDeployable(final boolean deployable) {
		//setFlagExt(DESIGN_FLAGEXT_FILE_DEPLOYABLE, deployable);
	}

	@Override
	public void setHideFromDesignList(final boolean hideFromDesignList) {
		//	setFlag(DESIGN_FLAG_HIDEFROMDESIGNLIST, hideFromDesignList);
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

	@Override
	public void writeOnDiskFile(final File odsFile) throws IOException {
		FileOutputStream fo = new FileOutputStream(odsFile);
		fo.write(getFileData());
		fo.close();

	}
}
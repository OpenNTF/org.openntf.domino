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

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.openntf.domino.design.OnDiskConverter;
import org.openntf.domino.nsfdata.structs.ODSUtils;
import org.openntf.domino.utils.xml.XMLNode;

/**
 * A Client Side JavaScript Library.
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public class ScriptLibraryCSJS extends AbstractDesignFileResource implements org.openntf.domino.design.ScriptLibraryCSJS {
	private static final long serialVersionUID = 1L;

	@Override
	protected boolean enforceRawFormat() {
		return false;
	}

	/*
	 *  (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.FileResource#getFileData(java.lang.String)
	 */
	@Override
	public byte[] getFileData() {
		if (enforceRawFormat())
			return getFileDataRaw("$JavaScriptLibrary");
		return getDxl().selectSingleNode("//code/javascript").getText().getBytes();
	}

	@Override
	public void writeOnDiskFile(final File odpFile, final OnDiskConverter odsConverter) throws IOException {
		// TODO Check for $Scriptlib_error => throw exception if item exists
		String content;
		if (enforceRawFormat()) {
			content = ODSUtils.fromLMBCS(getFileData());
		} else {
			XMLNode jsNode = getDxl().selectSingleNode("//code/javascript");
			if (jsNode != null) {
				content = jsNode.getText();
			} else {
				content = "";
			}
		}
		odsConverter.writeTextFile(content, odpFile);

	}

	@Override
	public void readOnDiskFile(final File file, final OnDiskConverter odsConverter) throws IOException {
		if (getDxlFormat(true) != DxlFormat.DXL) {
			throw new UnsupportedOperationException("cannot import raw CSJS-Library");
		}

		List<XMLNode> fileDataNodes = getDxl().selectNodes("//code");
		for (int i = fileDataNodes.size() - 1; i >= 0; i--) {
			fileDataNodes.get(i).getParentNode().removeChild(fileDataNodes.get(i));
		}

		String fileContent = odsConverter.readTextFile(file);

		XMLNode documentNode = getDxl().selectSingleNode("//scriptlibrary");
		XMLNode fileDataNode = documentNode.addChildElement("code");
		fileDataNode.setAttribute("event", "library");
		fileDataNode.setAttribute("for", "web");
		fileDataNode = fileDataNode.addChildElement("javascript");

		if (fileContent.trim().length() == 0) {
			//cannot import empty code/javascript - node
			fileDataNode.setText("// empty javascript");
		} else {
			fileDataNode.setText(fileContent);
		}

	}
}

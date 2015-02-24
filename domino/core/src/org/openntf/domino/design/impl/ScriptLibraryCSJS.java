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
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.nsfdata.structs.ODSUtils;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.xml.XMLNode;

/**
 * A Client Side JavaScript Library.
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public class ScriptLibraryCSJS extends AbstractDesignFileResource implements org.openntf.domino.design.ScriptLibraryCSJS {
	private static final long serialVersionUID = 1L;

	/**
	 * @param document
	 */
	protected ScriptLibraryCSJS(final Document document) {
		super(document);
	}

	protected ScriptLibraryCSJS(final Database database) {
		super(database);
	}

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
	public boolean writeOnDiskFile(final File odpFile, final boolean useTransformer) throws IOException {
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
		PrintWriter pw = new PrintWriter(odpFile, "UTF-8");
		pw.write(content);
		pw.close();
		updateLastModified(odpFile);
		return true;
	}

	@Override
	public boolean readOnDiskFile(final File file) {
		if (getDxlFormat(true) != DxlFormat.DXL) {
			throw new UnsupportedOperationException("cannot import raw CSJS-Library");
		}
		try {
			List<XMLNode> fileDataNodes = getDxl().selectNodes("//code");
			for (int i = fileDataNodes.size() - 1; i >= 0; i--) {
				fileDataNodes.get(i).getParentNode().removeChild(fileDataNodes.get(i));
			}

			StringBuilder fileContents = new StringBuilder();
			Scanner scanner = new Scanner(file);

			try {
				while (scanner.hasNextLine()) {
					fileContents.append(scanner.nextLine());
					fileContents.append('\n');
				}
			} finally {
				scanner.close();
			}

			XMLNode documentNode = getDxl().selectSingleNode("//scriptlibrary");
			XMLNode fileDataNode = documentNode.addChildElement("code");
			fileDataNode.setAttribute("event", "library");
			fileDataNode.setAttribute("for", "web");
			fileDataNode = fileDataNode.addChildElement("javascript");

			if (fileContents.toString().trim().length() == 0) {
				//cannot import empty code/javascript - node
				fileDataNode.setText("var i;");
			} else {
				fileDataNode.setText(fileContents.toString());
			}

		} catch (IOException e) {
			DominoUtils.handleException(e);
		}
		return true;
	}
}

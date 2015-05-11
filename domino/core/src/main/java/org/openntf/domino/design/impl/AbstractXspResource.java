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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.openntf.domino.design.XspResource;
import org.openntf.domino.utils.DominoUtils;

/**
 * @author jgallagher
 * 
 */
public abstract class AbstractXspResource extends AbstractDesignNapiFileResource implements XspResource {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(AbstractXspResource.class.getName());

	private static final String CLASS_INDEX_ITEM = "$ClassIndexItem";

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.JavaResource#getClassNames()
	 */
	@Override
	public Collection<String> getClassNames() {
		String[] classIndex = getDocument().getItemValue(CLASS_ITEM, String[].class);
		List<String> names = new ArrayList<String>();
		for (String path : classIndex) {
			if (path.startsWith("WEB-INF/classes/")) {
				names.add(DominoUtils.filePathToJavaBinaryName(path.substring(16), "/"));
			} else {
				names.add(""); // add blank entries, otherwise the wrong $ClassData item will be located
			}
		}
		return names;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.JavaResource#getClassData()
	 */
	@Override
	public Map<String, byte[]> getClassData() {
		List<String> names = new ArrayList<String>(getClassNames());

		Map<String, byte[]> result = new HashMap<String, byte[]>();
		for (int i = 0; i < names.size(); i++) {
			byte[] classData = null;
			try {
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				nReadNestedFileContent(getDocument(), "$ClassData" + i, os);
				classData = os.toByteArray();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (classData != null && classData.length > 0) {
				result.put(names.get(i), classData);
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.JavaResource#setClassData(java.util.Map)
	 */
	@Override
	public void setClassData(final Map<String, byte[]> classData) {
		throw new UnsupportedOperationException();
		//		// First step, clear out the existing data, index, and size fields
		//
		//		Document doc = getDocument();
		//
		//		for (int i = 0; i < 99; i++) {
		//			doc.removeItem("$ClassData" + i);
		//			doc.removeItem("$ClassSize" + i);
		//		}
		//
		//		int index = 0;
		//		for (Map.Entry<String, byte[]> classEntry : classData.entrySet()) {
		//			XMLNode sizeNode = itemParent.addChildElement("item");
		//			sizeNode.setAttribute("name", "$ClassSize" + index);
		//			XMLNode sizeText = sizeNode.addChildElement("number");
		//			sizeText.setTextContent(String.valueOf(classEntry.getValue().length));
		//
		//			try {
		//				setFileDataRaw("$ClassData" + index, new ByteArrayInputStream(classEntry.getValue()));
		//			} catch (IOException e) {
		//				DominoUtils.handleException(e);
		//			}
		//
		//			XMLNode name = indexNode.addChildElement("text");
		//			name.setTextContent("WEB-INF/classes/" + DominoUtils.javaBinaryNameToFilePath(classEntry.getKey(), "/"));
		//
		//			index++;
		//		}
	}

}

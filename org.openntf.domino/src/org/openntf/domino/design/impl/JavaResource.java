/**
 * 
 */
package org.openntf.domino.design.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.xml.XMLNode;

/**
 * @author jgallagher
 * 
 */
public class JavaResource extends FileResource implements org.openntf.domino.design.JavaResource {
	private static final Logger log_ = Logger.getLogger(JavaResource.class.getName());
	private static final long serialVersionUID = 1L;

	private static final String CLASS_INDEX_ITEM = "$ClassIndexItem";

	protected JavaResource(final Document document) {
		super(document);
	}

	/**
	 * @param database
	 */
	protected JavaResource(final Database database) {
		super(database);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.JavaResource#getClassData()
	 */
	@Override
	public Map<String, byte[]> getClassData() {
		List<String> names = new ArrayList<String>();
		for (XMLNode node : getDxl().selectNodes("//item[@name='" + CLASS_INDEX_ITEM + "']//text")) {
			// Classes begin with "WEB-INF/classes/"
			String path = node.getText();
			names.add(DominoUtils.filePathToJavaBinaryName(path.substring(16), "/"));
		}

		Map<String, byte[]> result = new HashMap<String, byte[]>();
		for (int i = 0; i < names.size(); i++) {
			byte[] classData = getFileData("$ClassData" + i);
			result.put(names.get(i), classData);
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
		// First step, clear out the existing data, index, and size fields
		XMLNode indexNode = getDxl().selectSingleNode("//item[@name='" + CLASS_INDEX_ITEM + "']");
		XMLNode itemParent = indexNode.getParentNode();
		List<XMLNode> names = indexNode.selectNodes("text");
		for (int i = 0; i < names.size(); i++) {
			XMLNode dataNode = getDxl().selectSingleNode("//item[@name='$ClassData" + i + "']");
			dataNode.getParentNode().removeChild(dataNode);
			XMLNode sizeNode = getDxl().selectSingleNode("//item[@name='$ClassSize" + i + "']");
			sizeNode.getParentNode().removeChild(sizeNode);

			indexNode.removeChild(names.get(i));
		}

		int index = 0;
		for (Map.Entry<String, byte[]> classEntry : classData.entrySet()) {
			XMLNode sizeNode = itemParent.addChildElement("item");
			sizeNode.setAttribute("name", "$ClassSize" + index);
			XMLNode sizeText = sizeNode.addChildElement("number");
			sizeText.setTextContent(String.valueOf(classEntry.getValue().length));

			setFileData("$ClassData" + index, classEntry.getValue());

			XMLNode name = indexNode.addChildElement("text");
			name.setTextContent("WEB-INF/classes/" + DominoUtils.javaBinaryNameToFilePath(classEntry.getKey(), "/"));

			index++;
		}
	}
}

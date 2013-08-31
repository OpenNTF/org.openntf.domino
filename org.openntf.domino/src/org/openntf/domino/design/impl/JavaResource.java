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
}

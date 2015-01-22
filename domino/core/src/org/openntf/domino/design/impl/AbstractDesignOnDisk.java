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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.openntf.domino.design.DesignBase;
import org.openntf.domino.utils.xml.XMLDocument;
import org.openntf.domino.utils.xml.XMLNode;

/**
 * @author jgallagher
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractDesignOnDisk implements DesignBase {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(AbstractDesignOnDisk.class.getName());

	//	@Override
	//	public String getName() {
	//		// TODO: use DXL document or member 
	//		return getDocument().getItemValueString("$TITLE");
	//	}

	protected boolean mustEncode(final String resName) {
		for (int i = 0; i < resName.length(); i++) {
			char ch = resName.charAt(i);
			switch (ch) {
			case '/':
			case '\\':
			case ':':
			case '*':
			case '?':
			case '<':
			case '>':
			case '|':
			case '"':
				return true;
			}
		}
		return false;
	}

	protected String encodeResourceName(final String resName) {
		if (resName == null)
			return null;
		if (!mustEncode(resName))
			return resName;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < resName.length(); i++) {
			char ch = resName.charAt(i);
			switch (ch) {
			case '_':
			case '/':
			case '\\':
			case ':':
			case '*':
			case '?':
			case '<':
			case '>':
			case '|':
			case '"':
				sb.append('_');
				sb.append(Integer.toHexString(ch));
				break;
			default:
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	@Override
	public String getOnDiskName() {
		return encodeResourceName(getName());
	}

	@Override
	public String getOnDiskPath() {
		String path = getOnDiskFolder();
		if (path == null)
			return null;
		if (path.length() > 0) {
			path = path + "/" + getOnDiskName();
		} else {
			path = getOnDiskName();
		}
		if (path == null)
			return null;

		String ext = getOnDiskExtension();
		if (ext != null && !path.endsWith(ext))
			path = path + ext;
		return path;
	}

	@Override
	public void writeOnDiskFile(final File odsFile) throws IOException {
		PrintWriter fo = new PrintWriter(odsFile);
		fo.write(getDxlString());
		fo.close();

	}

	public void writeOnDiskMeta(final File odsFile) throws IOException {
		PrintWriter fo = new PrintWriter(odsFile);
		fo.write(getDxlString());
		fo.close();

	}

	//------------------------------ ondisk end --------------------------------
	protected abstract XMLDocument getDxl();

	public String getDesignTemplateName() {
		return getItemValueString("$Class");
	}

	public void setDesignTemplateName(final String designTemplateName) {
		setItemValue("$Class", designTemplateName);
	}

	protected void setItemValue(final String itemName, final Object value) {
		XMLNode node = getDxl().selectSingleNode("//item[@name='" + XMLDocument.escapeXPathValue(itemName) + "']");
		if (node == null) {
			node = getDxl().selectSingleNode("/*").addChildElement("item");
			node.setAttribute("name", itemName);
		} else {
			node.removeChildren();
		}

		if (value instanceof Iterable) {
			Object first = ((Iterable<?>) value).iterator().next();
			XMLNode list = node.addChildElement(first instanceof Number ? "numberlist" : "textlist");

			for (Object val : (Iterable<?>) value) {
				appendItemValueNode(list, val);
			}
		} else {
			appendItemValueNode(node, value);
		}
	}

	private void appendItemValueNode(final XMLNode node, final Object value) {
		XMLNode child;
		if (value instanceof Number) {
			child = node.addChildElement("number");
		} else {
			child = node.addChildElement("text");
		}
		child.setText(String.valueOf(value));
	}

	protected String getItemValueString(final String itemName) {
		List<Object> result = new ArrayList<Object>();
		XMLNode node = getDxl().selectSingleNode("//item[@name='" + XMLDocument.escapeXPathValue(itemName) + "']");
		if (node != null) {
			node = node.selectSingleNode(".//number | .//text");
			if (node != null)
				return node.getText();
		}
		return "";
	}

	protected List<Object> getItemValue(final String itemName) {
		List<Object> result = new ArrayList<Object>();
		XMLNode node = getDxl().selectSingleNode("//item[@name='" + XMLDocument.escapeXPathValue(itemName) + "']");
		if (node != null) {
			List<XMLNode> nodes = node.selectNodes(".//number | .//text");
			for (XMLNode child : nodes) {
				if (child.getNodeName().equals("number")) {
					result.add(Double.parseDouble(child.getText()));
				} else {
					result.add(child.getText());
				}
			}
		}
		return result;
	}
}

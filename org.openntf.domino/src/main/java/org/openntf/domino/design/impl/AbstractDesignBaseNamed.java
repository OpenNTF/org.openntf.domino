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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.design.DesignBaseNamed;
import org.openntf.domino.utils.xml.XMLDocument;
import org.openntf.domino.utils.xml.XMLNode;

/**
 * @author jgallagher
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractDesignBaseNamed extends AbstractDesignBase implements DesignBaseNamed {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(AbstractDesignBaseNamed.class.getName());

	/**
	 * @param document
	 */
	protected AbstractDesignBaseNamed(final Document document) {
		super(document);
	}

	protected AbstractDesignBaseNamed(final Database database) {
		super(database);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#getAliases()
	 */
	@Override
	public List<String> getAliases() {
		return Arrays.asList(getDxl().getAttribute("alias").split("\\|"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#getAlias()
	 */
	@Override
	public String getAlias() {
		return getDxl().getAttribute("alias");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#getName()
	 */
	@Override
	public String getName() {
		return getDocumentElement().getAttribute("name");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#setAlias(java.lang.String)
	 */
	@Override
	public void setAlias(final String alias) {
		getDocumentElement().setAttribute("alias", alias);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#setAliases(java.lang.Iterable)
	 */
	@Override
	public void setAliases(final Iterable<String> aliases) {
		StringBuilder result = new StringBuilder();
		boolean added = false;
		for (String alias : aliases) {
			if (added)
				result.append("|");
			result.append(alias);
			added = true;
		}
		getDocumentElement().setAttribute("alias", result.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#setName(java.lang.String)
	 */
	@Override
	public void setName(final String name) {
		getDocumentElement().setAttribute("name", name);
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

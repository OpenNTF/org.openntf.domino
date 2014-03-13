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

import java.util.logging.Logger;

import org.openntf.domino.utils.xml.XMLNode;

/**
 * @author jgallagher
 * 
 */
public class DesignColumn implements org.openntf.domino.design.DesignColumn {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(DesignColumn.class.getName());

	private XMLNode node_;

	public DesignColumn(final XMLNode node) {
		node_ = node;
	}

	@Override
	public boolean isResizable() {
		return node_.getAttribute("resizable").equals("true");
	}

	@Override
	public void setResizable(final boolean resizable) {
		node_.setAttribute("resizable", String.valueOf(resizable));
	}

	@Override
	public boolean isSeparateMultipleValues() {
		return node_.getAttribute("separatemultiplevalues").equals("true");
	}

	@Override
	public void setSeparateMultipleValues(final boolean separateMultipleValues) {
		node_.setAttribute("separatemultipleValues", String.valueOf(separateMultipleValues));
	}

	@Override
	public boolean isSortNoAccent() {
		return node_.getAttribute("sortnoaccent").equals("true");
	}

	@Override
	public void setSortNoAccent(final boolean sortNoAccent) {
		node_.setAttribute("sortnoaccent", String.valueOf(sortNoAccent));
	}

	@Override
	public boolean isSortNoCase() {
		return node_.getAttribute("sortnocase").equals("true");
	}

	@Override
	public void setSortNoCase(final boolean sortNoCase) {
		node_.setAttribute("sortnocase", String.valueOf(sortNoCase));
	}

	@Override
	public boolean isShowAsLinks() {
		return node_.getAttribute("showaslinks").equals("true");
	}

	@Override
	public void setShowAsLinks(final boolean showAsLinks) {
		node_.setAttribute("showaslinks", showAsLinks ? "true" : "false");
	}

	@Override
	public String getItemName() {
		return node_.getAttribute("itemname");
	}

	@Override
	public void setItemName(final String itemName) {
		node_.setAttribute("itemname", itemName);
	}

	@Override
	public SortOrder getSortOrder() {
		String sort = node_.getAttribute("sort");
		if (sort == null || sort.isEmpty()) {
			return SortOrder.NONE;
		}
		return SortOrder.valueOf(sort.toUpperCase());
	}

	@Override
	public void setSortOrder(final SortOrder sortOrder) {
		node_.setAttribute("sort", sortOrder.toString().toLowerCase());
	}

	@Override
	public String getTitle() {
		XMLNode columnHeader = node_.selectSingleNode("columnheader");
		if (columnHeader == null) {
			return "";
		}
		return columnHeader.getAttribute("title");
	}

	@Override
	public void setTitle(final String title) {
		XMLNode columnHeader = node_.selectSingleNode("columnheader");
		if (columnHeader == null) {
			XMLNode firstChild = node_.getFirstChild();
			if (firstChild != null) {
				columnHeader = node_.insertChildElementBefore("columnheader", firstChild);
			} else {
				columnHeader = node_.addChildElement("columnheader");
			}
		}
		columnHeader.setAttribute("title", title);
	}

	@Override
	public String getFormula() {
		XMLNode formulaNode = node_.selectSingleNode("code[@event='value']/formula");
		if (formulaNode != null) {
			return formulaNode.getTextContent();
		}

		// If there's no formula node then that means it's just an item directly
		return this.getItemName();
	}

	@Override
	public void setFormula(final String formula) {
		XMLNode formulaNode = node_.selectSingleNode("code[@event='value']/formula");
		if (formulaNode != null) {
			formulaNode.setTextContent(formula);
		} else {
			XMLNode code = node_.addChildElement("code");
			code.setAttribute("event", "value");
			formulaNode = code.addChildElement("formula");
			formulaNode.setTextContent(formula);
		}
	}
}

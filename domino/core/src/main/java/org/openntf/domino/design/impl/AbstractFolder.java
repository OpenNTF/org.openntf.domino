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

import java.io.InputStream;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.design.Folder;
import org.openntf.domino.utils.xml.XMLNode;

/**
 * @author jgallagher
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractFolder extends AbstractDesignBaseNamed implements Folder {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(AbstractFolder.class.getName());

	/**
	 * @param document
	 */
	protected AbstractFolder(final Document document) {
		super(document);
	}

	protected AbstractFolder(final Database database) {
		super(database, AbstractFolder.class.getResourceAsStream("/org/openntf/domino/design/impl/dxl_folder.xml"));
	}

	protected AbstractFolder(final Database database, final InputStream is) {
		super(database, is);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.design.Folder#getColumns()
	 */
	@Override
	public DesignColumnList getColumns() {
		return new DesignColumnList(this, "//column");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.design.DesignColumnList#addColumn()
	 */
	@Override
	public DesignColumn addColumn() {
		// Create the column node and set the defaults
		// Make sure to add the node before any items
		XMLNode node;
		XMLNode item = getDxl().selectSingleNode("//item");
		if (item != null) {
			node = getDocumentElement().insertChildElementBefore("column", item);
		} else {
			node = getDocumentElement().addChildElement("column");
		}

		node.setAttribute("hidedetailrows", "false");
		node.setAttribute("width", "10");
		node.setAttribute("resizable", "true");
		node.setAttribute("separatemultiplevalues", "false");
		node.setAttribute("sortnoaccent", "false");
		node.setAttribute("sortnocase", "true");
		node.setAttribute("showaslinks", "false");

		return new DesignColumn(node);
	}

	@Override
	public void swapColumns(final int a, final int b) {
		getColumns().swap(a, b);
	}

	@Override
	public org.openntf.domino.View getView() {
		// TODO is this safe enough in the event of multiple folders/views with the same name?
		return getAncestorDatabase().getView(getName());
	}

	public boolean isAllowDAS() {
		XMLNode node = getDxlNode("//item[@name='$WebFlags']/text");
		return node != null && node.getText().contains("A");
	}

	public void setAllowDAS(final boolean allowDAS) {
		XMLNode node = getDxlNode("//item[@name='$WebFlags']/text");
		if (node == null) {
			node = getDxl().getDocumentElement().addChildElement("item");
			node.setAttribute("name", "$WebFlags");
			node = node.addChildElement("text");
		}
		if (allowDAS) {
			if (!node.getText().contains("A")) {
				node.setText(node.getText() + "A");
			}
		} else {
			if (node.getText().contains("A")) {
				node.setText(node.getText().replace("A", ""));
			}
		}
	}

	@Override
	public OnRefreshType getOnRefreshUISetting() {
		XMLNode viewNode = getDxl().selectSingleNode("/view");
		String value = viewNode.getAttribute("onrefresh");
		for (OnRefreshType type : OnRefreshType.values()) {
			if (type.getPropertyName().equals(value)) {
				return type;
			}
		}
		return null;
	}

	@Override
	public void setOnRefreshUISetting(final OnRefreshType onRefreshUISetting) {
		XMLNode viewNode = getDxl().selectSingleNode("/view");
		viewNode.setAttribute("onrefresh", onRefreshUISetting.getPropertyName());
	}
}

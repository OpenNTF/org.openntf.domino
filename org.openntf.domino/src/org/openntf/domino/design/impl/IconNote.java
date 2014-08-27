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

import org.openntf.domino.Document;
import org.openntf.domino.utils.xml.XMLNode;

/**
 * @author jgallagher
 * 
 */
public class IconNote extends AbstractDesignBaseNamed implements org.openntf.domino.design.IconNote {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(IconNote.class.getName());

	public static enum DASMode {
		NONE, VIEWS, VIEWSANDDOCUMENTS;
	}

	/**
	 * @param document
	 */
	protected IconNote(final Document document) {
		super(document);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#setAlias(java.lang.String)
	 */
	@Override
	public void setAlias(final String alias) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#setAliases(java.lang.Iterable)
	 */
	@Override
	public void setAliases(final Iterable<String> aliases) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#setName(java.lang.String)
	 */
	@Override
	public void setName(final String name) {
		getDxlNode("/note/item[@name='$TITLE']/text").setTextContent(name);
	}

	public void setDASMode(final DASMode mode) {
		switch (mode) {
		case NONE:
			getDxlNode("/note/item[@name='$AllowRESTDbAPI']/number").setTextContent("0");
			break;
		case VIEWS:
			getDxlNode("/note/item[@name='$AllowRESTDbAPI']/number").setTextContent("1");
			break;
		case VIEWSANDDOCUMENTS:
			getDxlNode("/note/item[@name='$AllowRESTDbAPI']/number").setTextContent("2");
			break;
		}
	}

	public DASMode getDASMode() {
		XMLNode node = getDxlNode("/note/item[name='$AllowRESTDbAPI']/number");
		if (node == null) {
			return DASMode.NONE;
		} else {
			if ("1".equals(node.getText())) {
				return DASMode.VIEWS;
			} else if ("2".equals(node.getText())) {
				return DASMode.VIEWSANDDOCUMENTS;
			} else {
				return DASMode.NONE;
			}
		}
	}
}

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

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.utils.xml.XMLNode;

/**
 * @author jgallagher
 * 
 */
public class DesignView extends AbstractFolder implements org.openntf.domino.design.DesignView {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(DesignView.class.getName());

	/**
	 * @param document
	 */
	protected DesignView(final Document document) {
		super(document);
	}

	protected DesignView(final Database database) {
		super(database);
	}

	@Override
	protected boolean enforceRawFormat() {
		return false;
	}

	@Override
	public String getSelectionFormula() {
		XMLNode formula = getDxl().selectSingleNode("/view/code[@event='selection']/formula");
		if (formula != null) {
			return formula.getText();
		}
		return null;
	}

	@Override
	public void setSelectionFormula(final String selectionFormula) {
		XMLNode formula = getDxl().selectSingleNode("/view/code[@event='selection']/formula");
		if (formula != null) {
			formula.setTextContent(selectionFormula);
		}
	}
}

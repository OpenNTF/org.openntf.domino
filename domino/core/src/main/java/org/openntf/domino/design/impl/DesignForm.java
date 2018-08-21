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

import java.util.List;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.utils.xml.XMLNode;

/**
 * @author jgallagher
 *
 */
public class DesignForm extends Subform implements org.openntf.domino.design.DesignForm {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(DesignForm.class.getName());

	/**
	 * @param document
	 */
	protected DesignForm(final Document document) {
		super(document);
	}

	/**
	 * @param database
	 *            parent
	 * @since ODA 4.3.0
	 */
	protected DesignForm(final Database database) {
		super(database, DesignForm.class.getResourceAsStream("/org/openntf/domino/design/impl/dxl_form.xml"));
	}

	@Override
	public String getXPageAlt() {
		List<Object> itemValue = getItemValue("$XPageAlt");
		return itemValue.isEmpty() ? "" : String.valueOf(itemValue.get(0));
	}

	@Override
	public void setXPageAlt(final String xpageAlt) {
		setItemValue("$XPageAlt", xpageAlt, FLAG_SIGN_SUMMARY);
	}

	@Override
	public String getXPageAltClient() {
		List<Object> itemValue = getItemValue("$XPageAltClient");
		return itemValue.isEmpty() ? "" : String.valueOf(itemValue.get(0));
	}

	@Override
	public void setXPageAltClient(final String xpageAltClient) {
		setItemValue("$XPageAltClient", xpageAltClient, FLAG_SIGN_SUMMARY);
	}

	@Override
	protected XMLNode getBody() {
		XMLNode body = getDxl().selectSingleNode("/form/body/richtext");
		return body;
	}

}

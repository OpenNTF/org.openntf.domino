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

import org.openntf.domino.Document;
import org.openntf.domino.utils.xml.XMLNode;

/**
 * @author jgallagher
 * 
 */
public class DesignForm extends AbstractDesignBaseNamed implements org.openntf.domino.design.DesignForm {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(DesignForm.class.getName());

	/**
	 * @param document
	 */
	protected DesignForm(final Document document) {
		super(document);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignForm#getFields()
	 */
	@Override
	public FormFieldList getFields() {
		return new FormFieldList(this, "//field");
	}

	@Override
	public FormField addField() {
		XMLNode body = getDxl().selectSingleNode("/form/body/richtext");

		// Create an appropriate paragraph definition
		XMLNode finalPardef = getDxl().selectSingleNode("//pardef[last()]");
		int nextId = Integer.valueOf(finalPardef.getAttribute("id")) + 1;
		XMLNode pardef = body.addChildElement("pardef");
		pardef.setAttribute("id", String.valueOf(nextId));
		pardef.setAttribute("hide", "notes web mobile");

		// Now create the par and the field
		XMLNode par = body.addChildElement("par");
		par.setAttribute("def", pardef.getAttribute("id"));

		// Now add the field
		XMLNode field = par.addChildElement("field");
		field.setAttribute("kind", "editable");
		field.setAttribute("name", "");
		field.setAttribute("type", "text");

		return new FormField(field);
	}

	@Override
	public String getXPageAlt() {
		List<Object> itemValue = getItemValue("$XPageAlt");
		return itemValue.isEmpty() ? "" : String.valueOf(itemValue.get(0));
	}

	@Override
	public void setXPageAlt(final String xpageAlt) {
		setItemValue("$XPageAlt", xpageAlt);
	}

	@Override
	public String getXPageAltClient() {
		List<Object> itemValue = getItemValue("$XPageAltClient");
		return itemValue.isEmpty() ? "" : String.valueOf(itemValue.get(0));
	}

	@Override
	public void setXPageAltClient(final String xpageAltClient) {
		setItemValue("$XPageAltClient", xpageAltClient);
	}

	@Override
	public void swapFields(final int a, final int b) {
		getFields().swap(a, b);
	}
}

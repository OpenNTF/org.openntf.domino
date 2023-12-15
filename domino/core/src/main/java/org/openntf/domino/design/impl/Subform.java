/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.design.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.design.DatabaseDesign;
import org.openntf.domino.design.FormField;
import org.openntf.domino.design.FormFieldList;
import org.openntf.domino.utils.Strings;
import org.openntf.domino.utils.xml.XMLNode;
import org.openntf.domino.utils.xml.XMLNodeList;

/**
 * @author Roland Praml
 *
 */
public class Subform extends AbstractDesignBaseNamed implements org.openntf.domino.design.Subform {
	private static final long serialVersionUID = 1L;

	/**
	 * @param document
	 */
	protected Subform(final Document document) {
		super(document);
	}

	/**
	 * @param database
	 *            parent
	 * @since 4.3.0
	 */
	protected Subform(final Database database) {
		super(database, Subform.class.getResourceAsStream("/org/openntf/domino/design/impl/dxl_subform.xml")); //$NON-NLS-1$
	}

	/**
	 * @param database
	 *            parent
	 * @param is
	 *            InputStream to use from which to generate the design element
	 */
	protected Subform(final Database database, final InputStream is) {
		super(database, is);
	}

	@Override
	protected boolean enforceRawFormat() {
		return false;
	}

	@Override
	public FormField addField() {
		XMLNode body = getBody();

		// Create an appropriate paragraph definition
		XMLNode finalPardef = getDxl().selectSingleNode("//pardef[last()]"); //$NON-NLS-1$
		int nextId = Integer.valueOf(finalPardef.getAttribute("id")) + 1; //$NON-NLS-1$
		XMLNode pardef = body.addChildElement("pardef"); //$NON-NLS-1$
		pardef.setAttribute("id", String.valueOf(nextId)); //$NON-NLS-1$

		// Now create the par and the field
		XMLNode par = body.addChildElement("par"); //$NON-NLS-1$
		par.setAttribute("def", pardef.getAttribute("id")); //$NON-NLS-1$ //$NON-NLS-2$

		// Now add the field
		XMLNode field = par.addChildElement("field"); //$NON-NLS-1$
		field.setAttribute("kind", "editable"); //$NON-NLS-1$ //$NON-NLS-2$
		field.setAttribute("name", ""); //$NON-NLS-1$ //$NON-NLS-2$
		field.setAttribute("type", "text"); //$NON-NLS-1$ //$NON-NLS-2$

		return new org.openntf.domino.design.impl.FormField(field);
	}

	protected XMLNode getBody() {
		XMLNode body = getDxl().selectSingleNode("/subform/body/richtext"); //$NON-NLS-1$
		return body;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.design.DesignForm#getFields()
	 */
	@Override
	public FormFieldList getFields() {
		return new org.openntf.domino.design.impl.FormFieldList(this, "//field"); //$NON-NLS-1$
	}

	@Override
	public void swapFields(final int a, final int b) {
		getFields().swap(a, b);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.AnyFormOrSubform#getSubformNodes()
	 */
	@Override
	public XMLNodeList getSubformNodes() {
		return getDxl().selectNodes("//subformref"); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.AnyFormOrSubform#getExplicitSubforms()
	 */
	@Override
	public List<String> getExplicitSubforms() {
		XMLNodeList nodes = getSubformNodes();
		ArrayList<String> subforms = new ArrayList<String>();
		for (XMLNode node : nodes) {
			String name = node.getAttribute("name"); //$NON-NLS-1$
			if (!Strings.isBlankString(name)) {
				subforms.add(node.getAttribute("name")); //$NON-NLS-1$
			}
		}
		return subforms;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.AnyFormOrSubform#getExplicitSubformsRecursive(java.util.List)
	 */
	@Override
	public List<String> getExplicitSubformsRecursive(final List<String> existingList) {
		Database db = getAncestorDatabase();
		DatabaseDesign dbDesign = db.getDesign();
		XMLNodeList nodes = getSubformNodes();
		ArrayList<String> subforms = new ArrayList<String>();
		for (XMLNode node : nodes) {
			String name = node.getAttribute("name"); //$NON-NLS-1$
			if (!Strings.isBlankString(name)) {
				if (!existingList.contains(name)) {
					existingList.add(node.getAttribute("name")); //$NON-NLS-1$
					org.openntf.domino.design.Subform sf = dbDesign.getSubform(name);
					existingList.addAll(sf.getExplicitSubformsRecursive(subforms));
				}
			}
		}
		return existingList;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.AnyFormOrSubform#getComputedSubforms()
	 */
	@Override
	public List<String> getComputedSubforms() {
		XMLNodeList nodes = getSubformNodes();
		ArrayList<String> subforms = new ArrayList<String>();
		for (XMLNode node : nodes) {
			String name = node.getAttribute("name"); //$NON-NLS-1$
			if (Strings.isBlankString(name)) {
				subforms.add(node.selectSingleNode("code[@event='value']/formula").getText()); //$NON-NLS-1$
			}
		}
		return subforms;
	}

}

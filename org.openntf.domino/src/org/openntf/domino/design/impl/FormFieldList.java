/**
 * 
 */
package org.openntf.domino.design.impl;

import java.util.logging.Logger;

import javax.xml.xpath.XPathExpressionException;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.xml.XMLNode;

/**
 * @author jgallagher
 * 
 */
public class FormFieldList extends AbstractDesignComponentList<FormField> implements org.openntf.domino.design.FormFieldList {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(FormFieldList.class.getName());

	protected FormFieldList(final DesignForm parent, final String pattern) {
		super(parent, pattern);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractList#get(int)
	 */
	@Override
	public FormField get(final int index) {
		return new FormField(getNodes().get(index));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(final Object o) {
		if (!(o instanceof FormField || o instanceof String)) {
			throw new IllegalArgumentException();
		}
		String name = o instanceof String ? o.toString() : ((FormField) o).getName();
		for (int i = 0; i < size(); i++) {
			if (name.equalsIgnoreCase(get(i).getName())) {
				remove(i);
				return true;
			}
		}

		return false;
	}

	public FormField addField() {
		try {
			XMLNode body = getParent().getDxl().selectSingleNode("/form/body/richtext");

			// Create an appropriate paragraph definition
			XMLNode finalPardef = getParent().getDxl().selectSingleNode("//pardef[last()]");
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

			refreshNodes();

			return new FormField(field);
		} catch (XPathExpressionException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}
}

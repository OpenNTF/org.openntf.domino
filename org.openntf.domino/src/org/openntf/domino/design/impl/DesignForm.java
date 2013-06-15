/**
 * 
 */
package org.openntf.domino.design.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.xpath.XPathExpressionException;

import org.openntf.domino.Document;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.xml.XMLNode;
import org.openntf.domino.utils.xml.XMLNodeList;

/**
 * @author jgallagher
 * 
 */
public class DesignForm extends AbstractDesignBaseNamed implements org.openntf.domino.design.DesignForm {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(DesignForm.class.getName());
	private static final long serialVersionUID = 1L;

	/**
	 * @param document
	 */
	protected DesignForm(final Document document) {
		super(document);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignForm#addField()
	 */
	@Override
	public FormField addField() {
		try {
			XMLNode body = this.getDxl().selectSingleNode("/form/body/richtext");

			// Create an appropriate paragraph definition
			XMLNode finalPardef = this.getDxl().selectSingleNode("//pardef[last()]");
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
		} catch (XPathExpressionException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignForm#getFields()
	 */
	@Override
	public List<org.openntf.domino.design.FormField> getFields() {
		try {
			List<XMLNode> fieldNodes = getDxl().selectNodes("//field");
			List<org.openntf.domino.design.FormField> result = new ArrayList<org.openntf.domino.design.FormField>(fieldNodes.size());
			for (XMLNode fieldNode : fieldNodes) {
				result.add(new FormField(fieldNode));
			}
			return Collections.unmodifiableList(result);
		} catch (XPathExpressionException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignForm#removeField(int)
	 */
	@Override
	public void removeField(final int index) {
		try {
			List<XMLNode> fieldNodes = getDxl().selectNodes("//field");
			fieldNodes.remove(index);
		} catch (XPathExpressionException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignForm#swapFields(int, int)
	 */
	@Override
	public void swapFields(final int a, final int b) {
		try {
			XMLNodeList fieldNodes = (XMLNodeList) getDxl().selectNodes("//field");
			fieldNodes.swap(a, b);
		} catch (XPathExpressionException e) {
			DominoUtils.handleException(e);
		}
	}

}

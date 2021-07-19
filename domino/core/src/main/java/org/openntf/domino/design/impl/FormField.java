/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
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

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.TypeUtils;
import org.openntf.domino.utils.xml.XMLNode;

import com.ibm.commons.util.StringUtil;

/**
 * @author jgallagher
 * 
 */
public class FormField implements org.openntf.domino.design.FormField {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(FormField.class.getName());

	private final XMLNode node_;

	public FormField(final XMLNode node) {
		node_ = node;
	}

	@Override
	public Kind getKind() {
		return Kind.valueOf(node_.getAttribute("kind").toUpperCase()); //$NON-NLS-1$
	}

	@Override
	public void setKind(final Kind kind) {
		node_.setAttribute("kind", kind.toString().toLowerCase()); //$NON-NLS-1$
	}

	@Override
	public String getName() {
		return node_.getAttribute("name"); //$NON-NLS-1$
	}

	@Override
	public void setName(final String name) {
		node_.setAttribute("name", name); //$NON-NLS-1$
	}

	@Override
	public boolean isAllowMultiValues() {
		return node_.getAttribute("allowmultivalues").equals("true"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public void setAllowMultiValues(final boolean allowMultiValues) {
		node_.setAttribute("allowmultivalues", String.valueOf(allowMultiValues)); //$NON-NLS-1$
	}

	@Override
	public boolean isProtected() {
		return node_.getAttribute("protected").equals("true"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public void setProtected(final boolean _protected) {
		node_.setAttribute("protected", String.valueOf(_protected)); //$NON-NLS-1$
	}

	@Override
	public boolean isSign() {
		return node_.getAttribute("sign").equals("true"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public void setSign(final boolean sign) {
		node_.setAttribute("sign", String.valueOf(sign)); //$NON-NLS-1$
	}

	@Override
	public boolean isSeal() {
		return node_.getAttribute("seal").equals("true"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public void setSeal(final boolean seal) {
		node_.setAttribute("seal", String.valueOf(seal)); //$NON-NLS-1$
	}

	@Override
	public boolean isLookUpAddressOnRefresh() {
		return node_.getAttribute("lookupaddressonrefresh").equals("true"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public void setLookUpAddressOnRefresh(final boolean lookUpAddressOnRefresh) {
		node_.setAttribute("lookupaddressonrefresh", String.valueOf(lookUpAddressOnRefresh)); //$NON-NLS-1$
	}

	@Override
	public boolean isLookUpEachChar() {
		return node_.getAttribute("lookupeachchar").equals("true"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public void setLookUpEachChar(final boolean lookUpEachChar) {
		node_.setAttribute("lookupeachchar", String.valueOf(lookUpEachChar)); //$NON-NLS-1$
	}

	@Override
	public String getDefaultValueFormula() {
		XMLNode node = this.getDefaultValueFormulaNode();
		if (node != null) {
			return node.getText();
		}
		return ""; //$NON-NLS-1$
	}

	@Override
	public void setDefaultValueFormula(final String defaultValueFormula) {
		// DXL is not happy with empty default value nodes, so delete when empty
		XMLNode node = this.getDefaultValueFormulaNode();
		if (defaultValueFormula == null || defaultValueFormula.length() == 0) {
			if (node != null) {
				node_.removeChild(node.getParentNode());
			}
		} else {
			if (node == null) {
				if (defaultValueFormula == null || defaultValueFormula.length() == 0) {
					return;
				}
				node = this.createDefaultValueFormulaNode();
			}
			node.setText(defaultValueFormula);
		}
	}

	// DXL uses the "keyword" field type for several field types, so it's more convenient to make a new faux
	// attribute to handle referring to the field type like a human might
	@Override
	public Type getFieldType() {
		String type = node_.getAttribute("type"); //$NON-NLS-1$
		if (type.equals("keyword")) { //$NON-NLS-1$
			XMLNode keywords = this.getKeywordsNode();
			String ui = keywords.getAttribute("ui"); //$NON-NLS-1$
			if (ui.equals("checkbox")) { //$NON-NLS-1$
				return Type.CHECKBOX;
			} else if (ui.equals("radiobutton")) { //$NON-NLS-1$
				return Type.RADIOBUTTON;
			} else if (ui.equals("combobox")) { //$NON-NLS-1$
				return Type.COMBOBOX;
			} else {
				return Type.DIALOGLIST;
			}
		} else {
			return Type.valueOf(type.toUpperCase());
		}
	}

	@Override
	public void setFieldType(final Type fieldType) {
		try {
			switch (fieldType) {
			case CHECKBOX:
			case COMBOBOX:
			case DIALOGLIST:
			case RADIOBUTTON:
				node_.setAttribute("type", "keyword"); //$NON-NLS-1$ //$NON-NLS-2$
				XMLNode keywords = this.getKeywordsNode();
				keywords.setAttribute("ui", fieldType.toString().toLowerCase()); //$NON-NLS-1$
				keywords.setAttribute("helperbutton", String.valueOf(fieldType == Type.DIALOGLIST)); //$NON-NLS-1$
				if (keywords.getAttribute("columns").length() == 0) { //$NON-NLS-1$
					keywords.setAttribute("columns", "1"); //$NON-NLS-1$ //$NON-NLS-2$
				}
				break;
			default:
				node_.setAttribute("type", fieldType.toString().toLowerCase()); //$NON-NLS-1$
				if (fieldType == Type.PASSWORD) {
					node_.setAttribute("seal", "true"); //$NON-NLS-1$ //$NON-NLS-2$
				} else if (fieldType == Type.RICHTEXTLITE) {
					if (getKind() == Kind.COMPUTEDFORDISPLAY || getKind() == Kind.COMPUTEDWHENCOMPOSED) {
						this.setKind(Kind.COMPUTED);
					}
					if (node_.getAttribute("onlyallow").isEmpty()) { //$NON-NLS-1$
						node_.setAttribute("onlyallow", //$NON-NLS-1$
								"picture sharedimage attachment view datepicker sharedapplet text object calendar inbox help clear graphic link"); //$NON-NLS-1$
					}
					if (node_.getAttribute("firstdisplay").isEmpty()) { //$NON-NLS-1$
						node_.setAttribute("firstdisplay", "text"); //$NON-NLS-1$ //$NON-NLS-2$
					}
				} else if (fieldType == Type.RICHTEXT && (getKind() == Kind.COMPUTEDFORDISPLAY || getKind() == Kind.COMPUTEDWHENCOMPOSED)) {
					setKind(Kind.COMPUTED);
				}
				break;
			}
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public RTLType getFirstDisplay() {
		String firstDisplay = node_.getAttribute("firstdisplay"); //$NON-NLS-1$
		if (!StringUtil.isEmpty(firstDisplay)) {
			return RTLType.valueOf(firstDisplay.toUpperCase());
		}
		return null;
	}

	@Override
	public void setFirstDisplay(final RTLType firstDisplay) {
		if (firstDisplay != null) {
			node_.setAttribute("firstdisplay", firstDisplay.toString().toLowerCase()); //$NON-NLS-1$
		} else {
			node_.setAttribute("firstdisplay", ""); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	@Override
	public Set<RTLType> getOnlyAllow() {
		String values = node_.getAttribute("onlyallow"); //$NON-NLS-1$
		Set<RTLType> result = new HashSet<RTLType>();
		for (String val : values.split("\\s")) { //$NON-NLS-1$
			if (StringUtil.isNotEmpty(val)) {
				result.add(RTLType.valueOf(val.toUpperCase()));
			}
		}
		return result;
	}

	@Override
	public void setOnlyAllow(final Set<RTLType> onlyAllow) {
		if (onlyAllow != null) {
			node_.setAttribute("onlyallow", TypeUtils.join(onlyAllow, " ").toLowerCase()); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			node_.setAttribute("onlyallow", ""); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	@Override
	public String getFieldHelp() {
		return node_.getAttribute("fieldhelp"); //$NON-NLS-1$
	}

	@Override
	public void setFieldHelp(final String fieldHelp) {
		node_.setAttribute("fieldhelp", fieldHelp); //$NON-NLS-1$
	}

	/* ******************************************************************************************
	 * Internal utility methods
	 ********************************************************************************************/
	private XMLNode getKeywordsNode() {
		XMLNode node = node_.selectSingleNode("keywords"); //$NON-NLS-1$

		if (node == null) {
			node = node_.addChildElement("keywords"); //$NON-NLS-1$
		}
		return node;
	}

	private XMLNode getDefaultValueFormulaNode() {
		XMLNode node = node_.selectSingleNode("code[@event='defaultvalue']"); //$NON-NLS-1$

		if (node == null) {
			return null;
		} else {
			node = node.selectSingleNode("formula"); //$NON-NLS-1$
		}
		return node;
	}

	private XMLNode createDefaultValueFormulaNode() {
		XMLNode node = node_.addChildElement("code"); //$NON-NLS-1$
		node.setAttribute("event", "defaultvalue"); //$NON-NLS-1$ //$NON-NLS-2$
		node = node.addChildElement("formula"); //$NON-NLS-1$
		return node;
	}
}

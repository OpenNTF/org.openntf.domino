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

package org.openntf.domino.design;

/**
 * @author jgallagher
 * 
 */
public interface FormField {
	public static enum Type {
		DATETIME, NUMBER, DIALOGLIST, CHECKBOX, RADIOBUTTON, COMBOBOX, RICHTEXT, AUTHORS, NAMES, READERS, PASSWORD, FORMULA, TIMEZONE, RICHTEXTLITE, COLOR
	}

	public static enum Kind {
		COMPUTED, COMPUTEDFORDISPLAY, COMPUTEDWHENCOMPOSED, EDITABLE
	}

	public Kind getKind();

	public void setKind(final Kind kind);

	public String getName();

	public void setName(final String name);

	public boolean isAllowMultiValues();

	public void setAllowMultiValues(final boolean allowMultiValues);

	public boolean isProtected();

	public void setProtected(final boolean _protected);

	public boolean isSign();

	public void setSign(final boolean sign);

	public boolean isSeal();

	public void setSeal(final boolean seal);

	public boolean isLookUpAddressOnRefresh();

	public void setLookUpAddressOnRefresh(final boolean lookUpAddressOnRefresh);

	public boolean isLookUpEachChar();

	public void setLookUpEachChar(final boolean lookUpEachChar);

	public String getDefaultValueFormula();

	public void setDefaultValueFormula(final String defaultValueFormula);

	public Type getFieldType();

	public void setFieldType(final Type fieldType);
}
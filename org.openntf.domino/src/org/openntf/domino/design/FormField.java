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

import java.util.Set;

/**
 * @author jgallagher
 * 
 */
public interface FormField {
	public static enum Type {
		TEXT, DATETIME, NUMBER, DIALOGLIST, CHECKBOX, RADIOBUTTON, COMBOBOX, RICHTEXT, AUTHORS, NAMES, READERS, PASSWORD, FORMULA, TIMEZONE, RICHTEXTLITE, COLOR
	}

	public static enum Kind {
		COMPUTED, COMPUTEDFORDISPLAY, COMPUTEDWHENCOMPOSED, EDITABLE
	}

	public static enum RTLType {
		PICTURE, SHAREDIMAGE, ATTACHMENT, VIEW, DATEPICKER, SHAREDAPPLET, TEXT, OBJECT, CALENDAR, INBOX, HELP, CLEAR, GRAPHIC, LINK, THUMBNAIL
	}

	public Kind getKind();

	public void setKind(Kind kind);

	public String getName();

	public void setName(String name);

	public boolean isAllowMultiValues();

	public void setAllowMultiValues(boolean allowMultiValues);

	public boolean isProtected();

	public void setProtected(boolean _protected);

	public boolean isSign();

	public void setSign(boolean sign);

	public boolean isSeal();

	public void setSeal(boolean seal);

	public boolean isLookUpAddressOnRefresh();

	public void setLookUpAddressOnRefresh(boolean lookUpAddressOnRefresh);

	public boolean isLookUpEachChar();

	public void setLookUpEachChar(boolean lookUpEachChar);

	public String getDefaultValueFormula();

	public void setDefaultValueFormula(String defaultValueFormula);

	public Type getFieldType();

	public void setFieldType(Type fieldType);

	public Set<RTLType> getOnlyAllow();

	public void setOnlyAllow(Set<RTLType> onlyAllow);

	public RTLType getFirstDisplay();

	public void setFirstDisplay(RTLType firstDisplay);
}
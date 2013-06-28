/**
 * 
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
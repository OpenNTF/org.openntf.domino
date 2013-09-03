/**
 * 
 */
package org.openntf.domino.design.impl;

import java.util.logging.Logger;

/**
 * @author jgallagher
 * 
 */
public class FormFieldList extends AbstractDesignComponentList<FormField> implements org.openntf.domino.design.FormFieldList {
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
}

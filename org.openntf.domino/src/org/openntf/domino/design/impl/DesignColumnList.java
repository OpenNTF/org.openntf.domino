/**
 * 
 */
package org.openntf.domino.design.impl;

import java.util.logging.Logger;

/**
 * @author jgallagher
 * 
 */
public class DesignColumnList extends AbstractDesignComponentList<org.openntf.domino.design.DesignColumn> implements
		org.openntf.domino.design.DesignColumnList {
	private static final Logger log_ = Logger.getLogger(DesignColumnList.class.getName());

	protected DesignColumnList(final AbstractDesignBase parent, final String pattern) {
		super(parent, pattern);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractList#get(int)
	 */
	@Override
	public org.openntf.domino.design.DesignColumn get(final int index) {
		return new DesignColumn(getNodes().get(index));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(final Object o) {
		if (!(o instanceof DesignColumn || o instanceof String)) {
			throw new IllegalArgumentException();
		}
		String name = o instanceof String ? o.toString() : ((DesignColumn) o).getItemName();
		for (int i = 0; i < size(); i++) {
			if (name.equalsIgnoreCase(get(i).getItemName())) {
				remove(i);
				return true;
			}
		}

		return false;
	}
}

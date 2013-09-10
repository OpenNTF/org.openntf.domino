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

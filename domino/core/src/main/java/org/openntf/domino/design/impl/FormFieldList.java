/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
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

import java.util.logging.Logger;

/**
 * @author jgallagher
 * 
 */
public class FormFieldList extends AbstractDesignComponentList<FormField> implements org.openntf.domino.design.FormFieldList {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(FormFieldList.class.getName());

	protected FormFieldList(final AbstractDesignBase parent, final String pattern) {
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

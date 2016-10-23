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

import java.util.AbstractList;
import java.util.logging.Logger;

import org.openntf.domino.utils.xml.XMLNodeList;

/**
 * @author jgallagher
 * 
 */
public abstract class AbstractDesignComponentList<E> extends AbstractList<E> {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(AbstractDesignComponentList.class.getName());

	private final AbstractDesignBase parent_;
	private final String pattern_;

	protected AbstractDesignComponentList(final AbstractDesignBase parent, final String pattern) {
		parent_ = parent;
		pattern_ = pattern;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#size()
	 */
	@Override
	public int size() {
		return getNodes().size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractList#remove(int)
	 */
	@Override
	public E remove(final int index) {
		E current = get(index);
		getNodes().remove(index);
		return current;
	}

	protected AbstractDesignBase getParent() {
		return parent_;
	}

	public void swap(final int a, final int b) {
		XMLNodeList fieldNodes = getParent().getDxl().selectNodes(pattern_);
		fieldNodes.swap(a, b);
	}

	protected XMLNodeList getNodes() {
		return parent_.getDxl().selectNodes(pattern_);
	}
}

/**
 * 
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
	private static final long serialVersionUID = 1L;

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
		XMLNodeList fieldNodes = (XMLNodeList) getParent().getDxl().selectNodes(pattern_);
		fieldNodes.swap(a, b);
	}

	protected XMLNodeList getNodes() {
		return (XMLNodeList) parent_.getDxl().selectNodes(pattern_);
	}
}

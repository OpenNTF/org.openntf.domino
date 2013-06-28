/**
 * 
 */
package org.openntf.domino.design.impl;

import java.util.AbstractList;
import java.util.logging.Logger;

import javax.xml.xpath.XPathExpressionException;

import org.openntf.domino.utils.DominoUtils;
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
	private XMLNodeList nodes_;

	protected AbstractDesignComponentList(final AbstractDesignBase parent, final String pattern) {
		parent_ = parent;
		pattern_ = pattern;
		refreshNodes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#size()
	 */
	@Override
	public int size() {
		return nodes_.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractList#remove(int)
	 */
	@Override
	public E remove(final int index) {
		E current = get(index);
		nodes_.remove(index);
		return current;
	}

	protected XMLNodeList getNodes() {
		return nodes_;
	}

	protected AbstractDesignBase getParent() {
		return parent_;
	}

	public void swap(final int a, final int b) {
		try {
			XMLNodeList fieldNodes = (XMLNodeList) getParent().getDxl().selectNodes(pattern_);
			fieldNodes.swap(a, b);
		} catch (XPathExpressionException e) {
			DominoUtils.handleException(e);
		}
	}

	protected void refreshNodes() {
		try {
			nodes_ = (XMLNodeList) parent_.getDxl().selectNodes(pattern_);
		} catch (XPathExpressionException e) {
			DominoUtils.handleException(e);
		}
	}
}

/*
 * Copyright OpenNTF 2013
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
package org.openntf.domino.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class RichTextTab.
 */
public class RichTextTab extends Base<org.openntf.domino.RichTextTab, lotus.domino.RichTextTab> implements org.openntf.domino.RichTextTab {
	private static final Logger log_ = Logger.getLogger(RichTextTab.class.getName());

	/**
	 * Instantiates a new rich text tab.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	public RichTextTab(lotus.domino.RichTextTab delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextTab#clear()
	 */
	@Override
	public void clear() {
		markDirty();
		try {
			getDelegate().clear();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextTab#getPosition()
	 */
	@Override
	public int getPosition() {
		try {
			return getDelegate().getPosition();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextTab#getType()
	 */
	@Override
	public int getType() {
		try {
			return getDelegate().getType();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	public Document getParentDocument() {
		org.openntf.domino.Base<?> parent = super.getParent();
		if (parent instanceof RichTextItem) {
			return ((RichTextItem) parent).getParentDocument();
		} else if (parent instanceof RichTextRange) {
			return ((RichTextRange) parent).getParentDocument();
		} else if (parent instanceof RichTextNavigator) {
			return ((RichTextNavigator) parent).getParentDocument();
		} else {
			if (log_.isLoggable(Level.WARNING)) {
				log_.log(Level.WARNING,
						"RichTextTab doesn't have a RichTextItem, RichTextNavigator or RichTextRange as a parent? That's unpossible! But we got a "
								+ parent.getClass().getName());
			}
		}
		return null;
	}

	public org.openntf.domino.Database getParentDatabase() {
		return getParentDocument().getParentDatabase();
	}

	void markDirty() {
		getParentDocument().markDirty();
	}
}

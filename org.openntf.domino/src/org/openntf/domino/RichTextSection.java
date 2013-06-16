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
package org.openntf.domino;

import org.openntf.domino.types.DocumentDescendant;

/**
 * The Interface RichTextSection.
 */
public interface RichTextSection extends Base<lotus.domino.RichTextSection>, lotus.domino.RichTextSection,
		org.openntf.domino.ext.RichTextSection, DocumentDescendant {

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextSection#getBarColor()
	 */
	@Override
	public ColorObject getBarColor();

	/**
	 * Gets the parent.
	 * 
	 * @return the parent
	 */
	public RichTextItem getParent();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextSection#getTitle()
	 */
	@Override
	public String getTitle();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextSection#getTitleStyle()
	 */
	@Override
	public RichTextStyle getTitleStyle();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextSection#isExpanded()
	 */
	@Override
	public boolean isExpanded();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextSection#remove()
	 */
	@Override
	public void remove();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextSection#setBarColor(lotus.domino.ColorObject)
	 */
	@Override
	public void setBarColor(final lotus.domino.ColorObject color);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextSection#setExpanded(boolean)
	 */
	@Override
	public void setExpanded(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextSection#setTitle(java.lang.String)
	 */
	@Override
	public void setTitle(final String title);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextSection#setTitleStyle(lotus.domino.RichTextStyle)
	 */
	@Override
	public void setTitleStyle(final lotus.domino.RichTextStyle style);

}

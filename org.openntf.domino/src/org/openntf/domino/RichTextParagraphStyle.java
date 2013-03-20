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
package org.openntf.domino;

import java.util.Vector;

import org.openntf.domino.types.DatabaseDescendant;

// TODO: Auto-generated Javadoc
/**
 * The Interface RichTextParagraphStyle.
 */
public interface RichTextParagraphStyle extends Base<lotus.domino.RichTextParagraphStyle>, lotus.domino.RichTextParagraphStyle,
		DatabaseDescendant {

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#clearAllTabs()
	 */
	@Override
	public void clearAllTabs();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#getAlignment()
	 */
	@Override
	public int getAlignment();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#getFirstLineLeftMargin()
	 */
	@Override
	public int getFirstLineLeftMargin();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#getInterLineSpacing()
	 */
	@Override
	public int getInterLineSpacing();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#getLeftMargin()
	 */
	@Override
	public int getLeftMargin();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#getPagination()
	 */
	@Override
	public int getPagination();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#getRightMargin()
	 */
	@Override
	public int getRightMargin();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#getSpacingAbove()
	 */
	@Override
	public int getSpacingAbove();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#getSpacingBelow()
	 */
	@Override
	public int getSpacingBelow();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#getTabs()
	 */
	@Override
	public Vector<RichTextTab> getTabs();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#setAlignment(int)
	 */
	@Override
	public void setAlignment(int value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#setFirstLineLeftMargin(int)
	 */
	@Override
	public void setFirstLineLeftMargin(int value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#setInterLineSpacing(int)
	 */
	@Override
	public void setInterLineSpacing(int value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#setLeftMargin(int)
	 */
	@Override
	public void setLeftMargin(int value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#setPagination(int)
	 */
	@Override
	public void setPagination(int value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#setRightMargin(int)
	 */
	@Override
	public void setRightMargin(int value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#setSpacingAbove(int)
	 */
	@Override
	public void setSpacingAbove(int value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#setSpacingBelow(int)
	 */
	@Override
	public void setSpacingBelow(int value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#setTab(int, int)
	 */
	@Override
	public void setTab(int position, int type);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#setTabs(int, int, int)
	 */
	@Override
	public void setTabs(int count, int startPos, int interval);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#setTabs(int, int, int, int)
	 */
	@Override
	public void setTabs(int count, int startPos, int interval, int type);

}

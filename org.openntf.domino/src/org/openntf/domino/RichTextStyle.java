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

import org.openntf.domino.types.DatabaseDescendant;

// TODO: Auto-generated Javadoc
/**
 * The Interface RichTextStyle.
 */
public interface RichTextStyle extends Base<lotus.domino.RichTextStyle>, lotus.domino.RichTextStyle, DatabaseDescendant {

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextStyle#getBold()
	 */
	@Override
	public int getBold();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextStyle#getColor()
	 */
	public int getColor();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextStyle#getEffects()
	 */
	@Override
	public int getEffects();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextStyle#getFont()
	 */
	@Override
	public int getFont();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextStyle#getFontSize()
	 */
	@Override
	public int getFontSize();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextStyle#getItalic()
	 */
	@Override
	public int getItalic();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextStyle#getParent()
	 */
	@Override
	public Session getParent();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextStyle#getPassThruHTML()
	 */
	@Override
	public int getPassThruHTML();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextStyle#getStrikeThrough()
	 */
	@Override
	public int getStrikeThrough();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextStyle#getUnderline()
	 */
	@Override
	public int getUnderline();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextStyle#isDefault()
	 */
	@Override
	public boolean isDefault();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextStyle#setBold(int)
	 */
	@Override
	public void setBold(int value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextStyle#setColor(int)
	 */
	@Override
	public void setColor(int value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextStyle#setEffects(int)
	 */
	@Override
	public void setEffects(int value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextStyle#setFont(int)
	 */
	@Override
	public void setFont(int value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextStyle#setFontSize(int)
	 */
	@Override
	public void setFontSize(int value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextStyle#setItalic(int)
	 */
	@Override
	public void setItalic(int value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextStyle#setPassThruHTML(int)
	 */
	@Override
	public void setPassThruHTML(int value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextStyle#setStrikeThrough(int)
	 */
	@Override
	public void setStrikeThrough(int value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextStyle#setUnderline(int)
	 */
	@Override
	public void setUnderline(int value);

}

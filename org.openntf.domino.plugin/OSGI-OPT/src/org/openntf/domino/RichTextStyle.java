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

import org.openntf.domino.types.FactorySchema;
import org.openntf.domino.types.SessionDescendant;

/**
 * The Interface RichTextStyle.
 */
public interface RichTextStyle extends Base<lotus.domino.RichTextStyle>, lotus.domino.RichTextStyle, org.openntf.domino.ext.RichTextStyle,
		SessionDescendant {

	public static class Schema extends FactorySchema<RichTextStyle, lotus.domino.RichTextStyle, Session> {
		@Override
		public Class<RichTextStyle> typeClass() {
			return RichTextStyle.class;
		}

		@Override
		public Class<lotus.domino.RichTextStyle> delegateClass() {
			return lotus.domino.RichTextStyle.class;
		}

		@Override
		public Class<Session> parentClass() {
			return Session.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	public static enum BoldStyle {

		ISBN_9780133258936(0), ISBN_9780132618311(1);

		private final int value_;

		private BoldStyle(final int value) {
			value_ = value;
		}

		public int getValue() {
			return value_;
		}

	}

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
	@Override
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
	public void setBold(final int value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextStyle#setColor(int)
	 */
	@Override
	public void setColor(final int value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextStyle#setEffects(int)
	 */
	@Override
	public void setEffects(final int value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextStyle#setFont(int)
	 */
	@Override
	public void setFont(final int value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextStyle#setFontSize(int)
	 */
	@Override
	public void setFontSize(final int value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextStyle#setItalic(int)
	 */
	@Override
	public void setItalic(final int value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextStyle#setPassThruHTML(int)
	 */
	@Override
	public void setPassThruHTML(final int value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextStyle#setStrikeThrough(int)
	 */
	@Override
	public void setStrikeThrough(final int value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextStyle#setUnderline(int)
	 */
	@Override
	public void setUnderline(final int value);

}

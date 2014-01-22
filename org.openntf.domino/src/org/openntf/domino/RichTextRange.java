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
import org.openntf.domino.types.FactorySchema;

/**
 * The Interface RichTextRange.
 */
public interface RichTextRange extends Base<lotus.domino.RichTextRange>, lotus.domino.RichTextRange, org.openntf.domino.ext.RichTextRange,
		DocumentDescendant {

	public static class Schema extends FactorySchema<RichTextRange, lotus.domino.RichTextRange, RichTextItem> {
		@Override
		public Class<RichTextRange> typeClass() {
			return RichTextRange.class;
		}

		@Override
		public Class<lotus.domino.RichTextRange> delegateClass() {
			return lotus.domino.RichTextRange.class;
		}

		@Override
		public Class<RichTextItem> parentClass() {
			return RichTextItem.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextRange#Clone()
	 */
	@Override
	public lotus.domino.RichTextRange Clone();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextRange#findandReplace(java.lang.String, java.lang.String)
	 */
	@Override
	public int findandReplace(final String target, final String replacement);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextRange#findandReplace(java.lang.String, java.lang.String, long)
	 */
	@Override
	public int findandReplace(final String target, final String replacement, final long options);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextRange#getNavigator()
	 */
	@Override
	public RichTextNavigator getNavigator();

	/**
	 * Gets the parent.
	 * 
	 * @return the parent
	 */
	@Override
	public RichTextItem getParent();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextRange#getStyle()
	 */
	@Override
	public RichTextStyle getStyle();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextRange#getTextParagraph()
	 */
	@Override
	public String getTextParagraph();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextRange#getTextRun()
	 */
	@Override
	public String getTextRun();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextRange#getType()
	 */
	@Override
	public int getType();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextRange#remove()
	 */
	@Override
	public void remove();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextRange#reset(boolean, boolean)
	 */
	@Override
	public void reset(final boolean begin, final boolean end);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextRange#setBegin(lotus.domino.Base)
	 */
	@Override
	public void setBegin(final lotus.domino.Base element);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextRange#setEnd(lotus.domino.Base)
	 */
	@Override
	public void setEnd(final lotus.domino.Base element);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextRange#setStyle(lotus.domino.RichTextStyle)
	 */
	@Override
	public void setStyle(final lotus.domino.RichTextStyle style);

}

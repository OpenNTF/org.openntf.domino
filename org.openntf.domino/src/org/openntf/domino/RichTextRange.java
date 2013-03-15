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

// TODO: Auto-generated Javadoc
/**
 * The Interface RichTextRange.
 */
public interface RichTextRange extends Base<lotus.domino.RichTextRange>, lotus.domino.RichTextRange {

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextRange#Clone()
	 */
	@Override
	public lotus.domino.RichTextRange Clone();

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextRange#findandReplace(java.lang.String, java.lang.String)
	 */
	public int findandReplace(String target, String replacement);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextRange#findandReplace(java.lang.String, java.lang.String, long)
	 */
	@Override
	public int findandReplace(String target, String replacement, long options);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextRange#getNavigator()
	 */
	@Override
	public RichTextNavigator getNavigator();

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextRange#getStyle()
	 */
	@Override
	public RichTextStyle getStyle();

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextRange#getTextParagraph()
	 */
	@Override
	public String getTextParagraph();

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextRange#getTextRun()
	 */
	@Override
	public String getTextRun();

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextRange#getType()
	 */
	@Override
	public int getType();

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextRange#remove()
	 */
	@Override
	public void remove();

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextRange#reset(boolean, boolean)
	 */
	@Override
	public void reset(boolean begin, boolean end);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextRange#setBegin(lotus.domino.Base)
	 */
	@Override
	public void setBegin(lotus.domino.Base element);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextRange#setEnd(lotus.domino.Base)
	 */
	@Override
	public void setEnd(lotus.domino.Base element);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextRange#setStyle(lotus.domino.RichTextStyle)
	 */
	@Override
	public void setStyle(lotus.domino.RichTextStyle style);

}

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

import org.openntf.domino.types.DocumentDescendant;

/**
 * The Interface RichTextNavigator.
 */
public interface RichTextNavigator extends Base<lotus.domino.RichTextNavigator>, lotus.domino.RichTextNavigator,
		org.openntf.domino.ext.RichTextNavigator, DocumentDescendant {

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextNavigator#Clone()
	 */
	@Override
	public RichTextNavigator Clone();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextNavigator#findFirstElement(int)
	 */
	@Override
	public boolean findFirstElement(int type);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextNavigator#findFirstString(java.lang.String)
	 */
	@Override
	public boolean findFirstString(String target);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextNavigator#findFirstString(java.lang.String, int)
	 */
	@Override
	public boolean findFirstString(String target, int options);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextNavigator#findLastElement(int)
	 */
	@Override
	public boolean findLastElement(int type);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextNavigator#findNextElement()
	 */
	@Override
	public boolean findNextElement();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextNavigator#findNextElement(int)
	 */
	@Override
	public boolean findNextElement(int type);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextNavigator#findNextElement(int, int)
	 */
	@Override
	public boolean findNextElement(int type, int occurrence);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextNavigator#findNextString(java.lang.String)
	 */
	@Override
	public boolean findNextString(String target);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextNavigator#findNextString(java.lang.String, int)
	 */
	@Override
	public boolean findNextString(String target, int options);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextNavigator#findNthElement(int, int)
	 */
	@Override
	public boolean findNthElement(int type, int occurrence);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextNavigator#getElement()
	 */
	@Override
	public Base<?> getElement();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextNavigator#getFirstElement(int)
	 */
	@Override
	public Base<?> getFirstElement(int type);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextNavigator#getLastElement(int)
	 */
	@Override
	public Base<?> getLastElement(int type);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextNavigator#getNextElement()
	 */
	@Override
	public Base<?> getNextElement();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextNavigator#getNextElement(int)
	 */
	@Override
	public Base<?> getNextElement(int type);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextNavigator#getNextElement(int, int)
	 */
	@Override
	public Base<?> getNextElement(int type, int occurrence);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextNavigator#getNthElement(int, int)
	 */
	@Override
	public Base<?> getNthElement(int type, int occurrence);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextNavigator#setCharOffset(int)
	 */
	@Override
	public void setCharOffset(int offset);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextNavigator#setPosition(lotus.domino.Base)
	 */
	@Override
	public void setPosition(lotus.domino.Base element);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextNavigator#setPositionAtEnd(lotus.domino.Base)
	 */
	@Override
	public void setPositionAtEnd(lotus.domino.Base element);

}

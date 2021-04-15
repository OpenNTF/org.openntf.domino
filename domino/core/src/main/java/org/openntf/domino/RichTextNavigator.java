/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino;

import org.openntf.domino.types.DocumentDescendant;
import org.openntf.domino.types.FactorySchema;

/**
 * The Interface RichTextNavigator.
 */
public interface RichTextNavigator extends Base<lotus.domino.RichTextNavigator>, lotus.domino.RichTextNavigator,
		org.openntf.domino.ext.RichTextNavigator, DocumentDescendant {

	public static class Schema extends FactorySchema<RichTextNavigator, lotus.domino.RichTextNavigator, RichTextItem> {
		@Override
		public Class<RichTextNavigator> typeClass() {
			return RichTextNavigator.class;
		}

		@Override
		public Class<lotus.domino.RichTextNavigator> delegateClass() {
			return lotus.domino.RichTextNavigator.class;
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
	public boolean findFirstElement(final int type);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextNavigator#findFirstString(java.lang.String)
	 */
	@Override
	public boolean findFirstString(final String target);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextNavigator#findFirstString(java.lang.String, int)
	 */
	@Override
	public boolean findFirstString(final String target, final int options);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextNavigator#findLastElement(int)
	 */
	@Override
	public boolean findLastElement(final int type);

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
	public boolean findNextElement(final int type);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextNavigator#findNextElement(int, int)
	 */
	@Override
	public boolean findNextElement(final int type, final int occurrence);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextNavigator#findNextString(java.lang.String)
	 */
	@Override
	public boolean findNextString(final String target);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextNavigator#findNextString(java.lang.String, int)
	 */
	@Override
	public boolean findNextString(final String target, final int options);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextNavigator#findNthElement(int, int)
	 */
	@Override
	public boolean findNthElement(final int type, final int occurrence);

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
	public Base<?> getFirstElement(final int type);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextNavigator#getLastElement(int)
	 */
	@Override
	public Base<?> getLastElement(final int type);

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
	public Base<?> getNextElement(final int type);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextNavigator#getNextElement(int, int)
	 */
	@Override
	public Base<?> getNextElement(final int type, final int occurrence);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextNavigator#getNthElement(int, int)
	 */
	@Override
	public Base<?> getNthElement(final int type, final int occurrence);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextNavigator#setCharOffset(int)
	 */
	@Override
	public void setCharOffset(final int offset);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextNavigator#setPosition(lotus.domino.Base)
	 */
	@Override
	public void setPosition(final lotus.domino.Base element);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextNavigator#setPositionAtEnd(lotus.domino.Base)
	 */
	@Override
	public void setPositionAtEnd(final lotus.domino.Base element);

}

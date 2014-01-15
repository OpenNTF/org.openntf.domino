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
 * The Interface RichTextTab.
 */
public interface RichTextTab extends Base<lotus.domino.RichTextTab>, lotus.domino.RichTextTab, org.openntf.domino.ext.RichTextTab,
		DocumentDescendant {

	public static class Schema extends FactorySchema<RichTextTab, lotus.domino.RichTextTab, RichTextParagraphStyle> {
		@Override
		public Class<RichTextTab> typeClass() {
			return RichTextTab.class;
		}

		@Override
		public Class<lotus.domino.RichTextTab> delegateClass() {
			return lotus.domino.RichTextTab.class;
		}

		@Override
		public Class<RichTextParagraphStyle> parentClass() {
			return RichTextParagraphStyle.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextTab#clear()
	 */
	@Override
	public void clear();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextTab#getPosition()
	 */
	@Override
	public int getPosition();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextTab#getType()
	 */
	@Override
	public int getType();

}

/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
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
 * The Interface RichTextDoclink.
 */
public interface RichTextDoclink extends Base<lotus.domino.RichTextDoclink>, lotus.domino.RichTextDoclink,
		org.openntf.domino.ext.RichTextDoclink, DocumentDescendant {

	public static class Schema extends FactorySchema<ColorObject, lotus.domino.ColorObject, RichTextItem> {
		@Override
		public Class<ColorObject> typeClass() {
			return ColorObject.class;
		}

		@Override
		public Class<lotus.domino.ColorObject> delegateClass() {
			return lotus.domino.ColorObject.class;
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
	 * @see lotus.domino.RichTextDoclink#getDBReplicaID()
	 */
	@Override
	public String getDBReplicaID();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextDoclink#getDisplayComment()
	 */
	@Override
	public String getDisplayComment();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextDoclink#getDocUnID()
	 */
	@Override
	public String getDocUnID();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextDoclink#getHotSpotText()
	 */
	@Override
	public String getHotSpotText();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextDoclink#getHotSpotTextStyle()
	 */
	@Override
	public RichTextStyle getHotSpotTextStyle();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextDoclink#getServerHint()
	 */
	@Override
	public String getServerHint();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextDoclink#getViewUnID()
	 */
	@Override
	public String getViewUnID();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextDoclink#remove()
	 */
	@Override
	public void remove();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextDoclink#setDBReplicaID(java.lang.String)
	 */
	@Override
	public void setDBReplicaID(final String replicaId);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextDoclink#setDisplayComment(java.lang.String)
	 */
	@Override
	public void setDisplayComment(final String comment);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextDoclink#setDocUnID(java.lang.String)
	 */
	@Override
	public void setDocUnID(final String unid);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextDoclink#setHotSpotText(java.lang.String)
	 */
	@Override
	public void setHotSpotText(final String text);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextDoclink#setHotSpotTextStyle(lotus.domino.RichTextStyle)
	 */
	@Override
	public void setHotSpotTextStyle(final lotus.domino.RichTextStyle rtstyle);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextDoclink#setServerHint(java.lang.String)
	 */
	@Override
	public void setServerHint(final String server);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextDoclink#setViewUnID(java.lang.String)
	 */
	@Override
	public void setViewUnID(final String unid);

}

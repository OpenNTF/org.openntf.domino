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

import lotus.domino.RichTextStyle;

// TODO: Auto-generated Javadoc
/**
 * The Interface RichTextDoclink.
 */
public interface RichTextDoclink extends Base<lotus.domino.RichTextDoclink>, lotus.domino.RichTextDoclink {

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextDoclink#getDBReplicaID()
	 */
	@Override
	public String getDBReplicaID();

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextDoclink#getDisplayComment()
	 */
	@Override
	public String getDisplayComment();

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextDoclink#getDocUnID()
	 */
	@Override
	public String getDocUnID();

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextDoclink#getHotSpotText()
	 */
	@Override
	public String getHotSpotText();

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextDoclink#getHotSpotTextStyle()
	 */
	@Override
	public RichTextStyle getHotSpotTextStyle();

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextDoclink#getServerHint()
	 */
	@Override
	public String getServerHint();

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextDoclink#getViewUnID()
	 */
	@Override
	public String getViewUnID();

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextDoclink#remove()
	 */
	@Override
	public void remove();

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextDoclink#setDBReplicaID(java.lang.String)
	 */
	@Override
	public void setDBReplicaID(String replicaId);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextDoclink#setDisplayComment(java.lang.String)
	 */
	@Override
	public void setDisplayComment(String comment);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextDoclink#setDocUnID(java.lang.String)
	 */
	@Override
	public void setDocUnID(String unid);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextDoclink#setHotSpotText(java.lang.String)
	 */
	@Override
	public void setHotSpotText(String text);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextDoclink#setHotSpotTextStyle(lotus.domino.RichTextStyle)
	 */
	@Override
	public void setHotSpotTextStyle(RichTextStyle rtstyle);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextDoclink#setServerHint(java.lang.String)
	 */
	@Override
	public void setServerHint(String server);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextDoclink#setViewUnID(java.lang.String)
	 */
	@Override
	public void setViewUnID(String unid);

}

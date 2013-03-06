package org.openntf.domino;

import java.util.Vector;

import lotus.domino.RichTextStyle;

public interface RichTextDoclink extends Base<lotus.domino.RichTextDoclink>, lotus.domino.RichTextDoclink {

	@Override
	public String getDBReplicaID();

	public lotus.domino.RichTextDoclink getDelegate();

	@Override
	public String getDisplayComment();

	@Override
	public String getDocUnID();

	@Override
	public String getHotSpotText();

	@Override
	public RichTextStyle getHotSpotTextStyle();

	@Override
	public String getServerHint();

	@Override
	public String getViewUnID();

	@Override
	public void recycle();

	@Override
	public void recycle(Vector arg0);

	@Override
	public void remove();

	@Override
	public void setDBReplicaID(String arg0);

	@Override
	public void setDisplayComment(String arg0);

	@Override
	public void setDocUnID(String arg0);

	@Override
	public void setHotSpotText(String arg0);

	@Override
	public void setHotSpotTextStyle(RichTextStyle arg0);

	@Override
	public void setServerHint(String arg0);

	@Override
	public void setViewUnID(String arg0);

	@Override
	public boolean equals(Object obj);

	@Override
	public int hashCode();

	@Override
	public String toString();

}

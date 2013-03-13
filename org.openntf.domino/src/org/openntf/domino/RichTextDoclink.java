package org.openntf.domino;

import lotus.domino.RichTextStyle;

public interface RichTextDoclink extends Base<lotus.domino.RichTextDoclink>, lotus.domino.RichTextDoclink {

	@Override
	public String getDBReplicaID();

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
	public void remove();

	@Override
	public void setDBReplicaID(String replicaId);

	@Override
	public void setDisplayComment(String comment);

	@Override
	public void setDocUnID(String unid);

	@Override
	public void setHotSpotText(String text);

	@Override
	public void setHotSpotTextStyle(RichTextStyle rtstyle);

	@Override
	public void setServerHint(String server);

	@Override
	public void setViewUnID(String unid);

}

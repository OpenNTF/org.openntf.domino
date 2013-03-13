package org.openntf.domino;

public interface DateRange extends Base<lotus.domino.DateRange>, lotus.domino.DateRange {

	@Override
	public org.openntf.domino.DateTime getEndDateTime();

	@Override
	public org.openntf.domino.Session getParent();

	@Override
	public org.openntf.domino.DateTime getStartDateTime();

	@Override
	public String getText();

	@Override
	public void setEndDateTime(lotus.domino.DateTime end);

	@Override
	public void setStartDateTime(lotus.domino.DateTime start);

	@Override
	public void setText(String text);

}

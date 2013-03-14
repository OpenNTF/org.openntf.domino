package org.openntf.domino;

public interface DateRange extends Base<lotus.domino.DateRange>, lotus.domino.DateRange {

	@Override
	public DateTime getEndDateTime();

	public Session getParent();

	@Override
	public DateTime getStartDateTime();

	@Override
	public String getText();

	@Override
	public void setEndDateTime(lotus.domino.DateTime end);

	@Override
	public void setStartDateTime(lotus.domino.DateTime start);

	@Override
	public void setText(String text);

}

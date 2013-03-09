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
	public void setEndDateTime(lotus.domino.DateTime arg0);

	@Override
	public void setStartDateTime(lotus.domino.DateTime arg0);

	@Override
	public void setText(String arg0);

	@Override
	public boolean equals(Object obj);

	@Override
	public int hashCode();

	@Override
	public String toString();

}

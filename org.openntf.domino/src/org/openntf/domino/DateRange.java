package org.openntf.domino;

import java.util.Vector;

public interface DateRange extends Base<lotus.domino.DateRange>, lotus.domino.DateRange {

	@Override
	public lotus.domino.DateRange getDelegate();

	@Override
	public lotus.domino.DateTime getEndDateTime();

	@Override
	public lotus.domino.Session getParent();

	@Override
	public DateTime getStartDateTime();

	@Override
	public String getText();

	@Override
	public void recycle();

	@Override
	public void recycle(Vector arg0);

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

package org.openntf.conference.graph;

import java.util.Date;

import org.openntf.domino.graph2.builtin.DVertexFrame;

public interface TimeSlot extends DVertexFrame {
	public Date getStartTime();

	public void setStartTime(Date startTime);

	public Date getEndTime();

	public void setEndTime(Date endTime);

	public long getLength();	//in minutes

}

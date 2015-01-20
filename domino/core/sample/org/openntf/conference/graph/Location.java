package org.openntf.conference.graph;

import java.util.Date;

import org.openntf.conference.graph.Event.HappeningAt;
import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.builtin.social.Commentable;
import org.openntf.domino.graph2.builtin.social.Likeable;
import org.openntf.domino.graph2.builtin.social.Rateable;

import com.tinkerpop.blueprints.Direction;

public interface Location extends Commentable, Likeable, Rateable {
	public static enum Features {
		RESTAURANT, REFRESHMENTS, BAR, OUTDOOR;
	}

	public Double getLatitude();

	public void setLatitude(Double latitude);

	public Double getLongitude();

	public void setLongitude(Double longitude);

	public String getMapUrl();

	public void setMapUrl(String mapUrl);

	public String getFloor();

	public void setFloor(String floor);

	public String getAddress();

	public void setAddress(String address);

	public Date getOpenTime();

	public void setOpenTime(Date time);

	public Date getCloseTime();

	public void setCloseTime(Date time);

	public Features[] getFeatures();

	public void setFeatures(Features[] features);

	@AdjacencyUnique(label = HappeningAt.LABEL, direction = Direction.IN)
	public Iterable<Event> getEvents();

	@AdjacencyUnique(label = HappeningAt.LABEL, direction = Direction.IN)
	public HappeningAt addEvent(Event event);

	@AdjacencyUnique(label = HappeningAt.LABEL, direction = Direction.IN)
	public void removingEvent(Event event);

	@IncidenceUnique(label = HappeningAt.LABEL, direction = Direction.IN)
	public Iterable<HappeningAt> getHappeningAts();

	@IncidenceUnique(label = HappeningAt.LABEL, direction = Direction.IN)
	public void removingHappeningAt(HappeningAt happeningAt);

}

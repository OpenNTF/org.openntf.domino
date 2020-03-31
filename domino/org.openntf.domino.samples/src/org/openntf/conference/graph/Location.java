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
package org.openntf.conference.graph;

import java.util.Date;

import org.openntf.conference.graph.Event.HappeningAt;
import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.builtin.social.Commentable;
import org.openntf.domino.graph2.builtin.social.Likeable;
import org.openntf.domino.graph2.builtin.social.Rateable;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Location")
public interface Location extends Commentable, Likeable, Rateable {
	public static enum Features {
		RESTAURANT, REFRESHMENTS, BAR, OUTDOOR;
	}

	@TypedProperty("Name")
	public String getName();

	@TypedProperty("Name")
	public void setName(String name);

	@TypedProperty("Latitude")
	public Double getLatitude();

	@TypedProperty("Latitude")
	public void setLatitude(Double latitude);

	@TypedProperty("Longitude")
	public Double getLongitude();

	@TypedProperty("Longitude")
	public void setLongitude(Double longitude);

	@TypedProperty("Mapurl")
	public String getMapUrl();

	@TypedProperty("Mapurl")
	public void setMapUrl(String mapUrl);

	@TypedProperty("Floor")
	public String getFloor();

	@TypedProperty("Floor")
	public void setFloor(String floor);

	@TypedProperty("Address")
	public String getAddress();

	@TypedProperty("Address")
	public void setAddress(String address);

	@TypedProperty("OpenTime")
	public Date getOpenTime();

	@TypedProperty("OpenTime")
	public void setOpenTime(Date time);

	@TypedProperty("CloseTime")
	public Date getCloseTime();

	@TypedProperty("CloseTime")
	public void setCloseTime(Date time);

	@TypedProperty("Features")
	public Features[] getFeatures();

	@TypedProperty("Features")
	public void setFeatures(Features[] features);

	@AdjacencyUnique(label = HappeningAt.LABEL, direction = Direction.IN)
	public Iterable<Event> getEvents();

	@AdjacencyUnique(label = HappeningAt.LABEL, direction = Direction.IN)
	public HappeningAt addEvent(Event event);

	@AdjacencyUnique(label = HappeningAt.LABEL, direction = Direction.IN)
	public void removeEvent(Event event);

	@IncidenceUnique(label = HappeningAt.LABEL, direction = Direction.IN)
	public Iterable<HappeningAt> getHappeningAts();

	@IncidenceUnique(label = HappeningAt.LABEL, direction = Direction.IN)
	public void removeHappeningAt(HappeningAt happeningAt);

}

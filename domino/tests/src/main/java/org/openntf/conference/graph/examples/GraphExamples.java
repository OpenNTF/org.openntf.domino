/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
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
package org.openntf.conference.graph.examples;

import org.openntf.conference.graph.Attendee;
import org.openntf.conference.graph.ConferenceGraph;
import org.openntf.domino.Session;
import org.openntf.domino.graph2.impl.DGraph;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

import com.tinkerpop.frames.FramedGraph;

public class GraphExamples {
	private ConferenceGraph theConference_;

	public GraphExamples() {
	}

	public ConferenceGraph getConference() {
		if (theConference_ == null) {
			theConference_ = new ConferenceGraph();
		}
		return theConference_;
	}

	public FramedGraph<DGraph> getGraph() {
		return getConference().getFramedGraph();
	}

	public Attendee getMe() {
		Session session = Factory.getSession(SessionType.CURRENT);
		String myName = session.getEffectiveUserName();
		return getConference().getAttendee(myName, true);
	}

}

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.openntf.conference.graph.Group.Type;
import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.graph2.builtin.DVertexFrame;
import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.graph2.impl.DGraph;
import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.utils.Strings;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.FramedTransactionalGraph;
import com.tinkerpop.frames.VertexFrame;

public class DataInitializer implements Runnable {
	private long marktime;
	private static final String SRC_DATA_PATH = "OpenNTF Downloads/sphere2015.nsf";

	public DataInitializer() {

	}

	@SuppressWarnings("unused")
	@Override
	public void run() {
		long testStartTime = System.nanoTime();
		marktime = System.nanoTime();
		ConferenceGraph graph = new ConferenceGraph();
		DFramedTransactionalGraph<DGraph> framedGraph = graph.getFramedGraph();
		DGraph baseGraph = framedGraph.getBaseGraph();
		Iterable<Vertex> vertices = baseGraph.getVertices("@", "Form=\"Presentation\"");
		//NTF There's no specific need to do it this way. I just wanted to test the TypeField-based framing
		Map<String, Object> jsonMap = null;
		for (Vertex vertex : vertices) {
			VertexFrame frame = framedGraph.frame(vertex, DVertexFrame.class);

		}
		long testEndTime = System.nanoTime();
		System.out.println("Completed " + getClass().getSimpleName() + " run in " + ((testEndTime - testStartTime) / 1000000) + " ms");
		//
		//		VertexFrame frame = framedGraph.toVertexFrame(jsonMap);
		//		System.out.println("Got a frame of " + frame.getClass().getName());
		//		if (frame instanceof Presentation) {
		//			((Presentation) frame).setStatus(Status.CANCELLED);
		//			System.out.println("Result: " + ((Presentation) frame).getStatus());
		//		} else {
		//			System.out.println("Didn't get a Presentation. GOt a " + frame.getClass().getName());
		//		}
		framedGraph.commit();

	}

	public void run2() {
		long testStartTime = System.nanoTime();
		marktime = System.nanoTime();
		try {
			timelog("Beginning dataInitializer...");

			// Get / create databases
			Session s = Factory.getSession(SessionType.NATIVE);
			Database attendees = s.getDatabase(s.getServerName(), ConferenceGraph.ATTENDEE_PATH, true);
			attendees.getAllDocuments().removeAll(true);
			Database events = s.getDatabase(s.getServerName(), ConferenceGraph.EVENT_PATH, true);
			events.getAllDocuments().removeAll(true);
			Database groups = s.getDatabase(s.getServerName(), ConferenceGraph.GROUP_PATH, true);
			groups.getAllDocuments().removeAll(true);
			Database invites = s.getDatabase(s.getServerName(), ConferenceGraph.INVITE_PATH, true);
			invites.getAllDocuments().removeAll(true);
			Database location = s.getDatabase(s.getServerName(), ConferenceGraph.LOCATION_PATH, true);
			location.getAllDocuments().removeAll(true);
			Database times = s.getDatabase(s.getServerName(), ConferenceGraph.TIMES_PATH, true);
			times.getAllDocuments().removeAll(true);
			Database defaults = s.getDatabase(s.getServerName(), ConferenceGraph.DEFAULT_PATH, true);
			defaults.getAllDocuments().removeAll(true);

			// Initialize the graph
			ConferenceGraph graph = new ConferenceGraph();
			//			graph.initialize();	//NTF already done in constructor
			FramedTransactionalGraph<DGraph> framedGraph = graph.getFramedGraph();

			loadData(s, framedGraph);

			Iterable<Presentation> pres = framedGraph.getVertices(null, null, Presentation.class);

			for (Presentation presentation : pres) {
				System.out.println(presentation.getTitle());
			}

		} catch (Throwable t) {
			t.printStackTrace();
		}
		long testEndTime = System.nanoTime();
		System.out.println("Completed " + getClass().getSimpleName() + " run in " + ((testEndTime - testStartTime) / 1000000) + " ms");
	}

	@SuppressWarnings({ "unused", "deprecation" })
	public void loadData(final org.openntf.domino.Session s, final FramedTransactionalGraph<DGraph> framedGraph) {
		HashMap<String, Location> locs = new HashMap<String, Location>();
		HashMap<String, Track> tracks = new HashMap<String, Track>();
		try {
			Database srcDb = s.getDatabase(s.getServerName(), SRC_DATA_PATH);
			if (null == srcDb) {
				throw new Exception("Source database not found on this Domino server at " + SRC_DATA_PATH);
			}

			// Create Group vertexes
			Group ibm_champion = framedGraph.addVertex("IBM Champions", Group.class);
			ibm_champion.setType(Group.Type.PROGRAM);

			SimpleDateFormat sdf = new SimpleDateFormat();
			View sessions = srcDb.getView("Sessions");
			for (Document doc : sessions.getAllDocuments()) {
				if (!doc.hasItem("$Conflict")) {	// ignore conflicts
					String locKey = doc.getItemValueString("Location");
					Location loc = framedGraph.addVertex(locKey, Location.class);
					if (Strings.isBlankString(loc.getName())) {
						loc.setName(doc.getItemValueString("Location"));
					}

					String trackKey = doc.getItemValueString("Categories");
					Track track = framedGraph.addVertex(trackKey, Track.class);
					if (Strings.isBlankString(track.getTitle())) {
						track.setTitle(doc.getItemValueString("Categories"));
						track.setDescription(doc.getItemValueString("Categories"));
					}

					Date startDate = doc.getItemValue("StartDate", Date.class);
					Date startDateTime = doc.getItemValue("StartDateTime", Date.class);
					Date endDate = doc.getItemValue("EndDate", Date.class);
					Date endDateTime = doc.getItemValue("EndDateTime", Date.class);

					Calendar startCal = new GregorianCalendar();
					startCal.setTime(startDate);
					startCal.set(Calendar.HOUR, startDateTime.getHours());
					startCal.set(Calendar.MINUTE, startDateTime.getMinutes());
					startCal.set(Calendar.SECOND, startDateTime.getSeconds());

					Calendar endCal = new GregorianCalendar();
					endCal.setTime(endDate);
					endCal.set(Calendar.HOUR, endDateTime.getHours());
					endCal.set(Calendar.MINUTE, endDateTime.getMinutes());
					endCal.set(Calendar.SECOND, endDateTime.getSeconds());

					String tsKey = sdf.format(startCal.getTime()) + " - " + sdf.format(endCal.getTime());
					TimeSlot ts = framedGraph.addVertex(tsKey, TimeSlot.class);
					//					ts.setStartTime(startCal.getTime());
					//					ts.setEndTime(endCal.getTime());

					String code = doc.getItemValueString("SessionID");
					// Not sure if I can combine these, that's for later

					Presentation sess = framedGraph.addVertex(code, Presentation.class);
					sess.setTitle(doc.getItemValueString("Subject"));
					sess.setDescription(doc.getItemValueString("Abstract"));
					sess.setStatus(Event.Status.CONFIRMED);
					sess.setSessionId(doc.getItemValueString("SessionID"));
					sess.setLevel(doc.getItemValueString("Level"));
					System.out.println("Assigning location - " + locKey + " to session " + doc.getItemValueString("Subject"));
					sess.addLocation(loc);
					track.addIncludesSession(sess);

					ts.addEvent(sess);

					for (int i = 1; i < 6; i++) {
						String speaker = doc.getItemValueString("Speaker" + String.valueOf(i));
						if ("".equals(speaker)) {
							break;
						}
						String speakerName = speaker;
						String organization = "";
						if (speaker.contains(" - ")) {
							int splitPos = speaker.indexOf(" - ");
							speakerName = speaker.substring(0, splitPos);
							organization = speaker.substring(splitPos + 3, speaker.length());
						}
						Attendee att = framedGraph.addVertex(null, Attendee.class);
						int sep = speakerName.indexOf(" ");
						String firstName = speakerName.substring(0, sep);
						String lastName = speakerName.substring(sep + 1, speakerName.length());
						att.setFirstName(firstName);
						att.setLastName(lastName);

						if (!"".equals(organization)) {
							Group org = framedGraph.addVertex(organization, Group.class);
							org.setName(organization);
							org.setType(Type.COMPANY);
							org.addMember(att);
						}

						sess.addPresentedBy(att);
						sess.addAttendingAttendee(att);
						sess.addPlansToAttend(att);
					}

				}

			}
			framedGraph.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(final String[] args) {
		TestRunnerUtil.runAsDominoThread(new DataInitializer(), TestRunnerUtil.NATIVE_SESSION);
	}

	public void timelog(final String message) {
		long curtime = System.nanoTime();
		long elapsed = curtime - marktime;
		marktime = curtime;
		System.out.println(elapsed / 1000000 + " ms: " + message);
	}

}

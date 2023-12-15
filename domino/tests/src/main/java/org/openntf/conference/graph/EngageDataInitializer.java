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
package org.openntf.conference.graph;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.graph2.impl.DGraph;
import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.utils.Strings;

import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonParser;
import com.tinkerpop.frames.FramedTransactionalGraph;

@SuppressWarnings("nls")
public class EngageDataInitializer implements Runnable {
	private long marktime;
	private SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public EngageDataInitializer() {

	}

	@Override
	public void run() {
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

	private static String readUrl(final String urlString) throws Exception {
		BufferedReader reader = null;
		try {
			URL url = new URL(urlString);
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
			StringBuffer buffer = new StringBuffer();
			int read;
			char[] chars = new char[1024];
			while ((read = reader.read(chars)) != -1)
				buffer.append(chars, 0, read);

			return buffer.toString();
		} finally {
			if (reader != null)
				reader.close();
		}
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public void loadData(final org.openntf.domino.Session s, final FramedTransactionalGraph<DGraph> framedGraph) {
		HashMap<String, Location> locs = new HashMap<String, Location>();
		HashMap<String, Track> tracks = new HashMap<String, Track>();
		HashMap<String, String> trackLkup = new HashMap<String, String>();
		trackLkup.put("Special", "Sp");
		trackLkup.put("Strategy/Deployment", "Str");
		trackLkup.put("Administration", "Adm");
		trackLkup.put("Development", "Dev");
		trackLkup.put("Business", "Bus");
		trackLkup.put("Commercial", "Comm");
		try {
			String urlData = readUrl("http://xceed.be/engage.nsf/api/data/collections/name/BLUG_ViewSessions?start=0&count=100");

			JsonJavaFactory factory = JsonJavaFactory.instanceEx;

			ArrayList<JsonJavaObject> jsonData = (ArrayList<JsonJavaObject>) JsonParser.fromJson(factory, urlData);
			SimpleDateFormat sdf = new SimpleDateFormat();
			for (JsonJavaObject obj : jsonData) {
				String locKey = obj.getAsString("session_room");
				Location loc = framedGraph.addVertex(locKey, Location.class);
				if (Strings.isBlankString(loc.getName())) {
					loc.setName(locKey);
				}

				String trackKey = obj.getAsString("session_track");
				Track track = framedGraph.addVertex(trackLkup.get(trackKey), Track.class);
				if (Strings.isBlankString(track.getTitle())) {
					track.setTitle(trackLkup.get(trackKey));
					track.setDescription(trackKey);
				}

				// DO DATES
				String actualDate = obj.getAsString("session_date");
				String manipulated = actualDate.substring(0, 10) + " " + actualDate.substring(11, 19);
				Date startDate = DATE_FORMAT.parse(manipulated);
				String startTime = obj.getAsString("session_time1");
				String endTime = obj.getAsString("session_time2");
				Calendar startCal = Calendar.getInstance();
				startCal.setTime(startDate);
				startCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startTime.substring(0, 2)));
				startCal.set(Calendar.MINUTE, Integer.parseInt(startTime.substring(3, 5)));
				startCal.set(Calendar.SECOND, 0);
				startCal.set(Calendar.MILLISECOND, 0);
				Calendar endCal = Calendar.getInstance();
				endCal.setTime(startDate);
				endCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(endTime.substring(0, 2)));
				endCal.set(Calendar.MINUTE, Integer.parseInt(endTime.substring(3, 5)));
				endCal.set(Calendar.SECOND, 0);
				endCal.set(Calendar.MILLISECOND, 0);

				String tsKey = sdf.format(startCal.getTime()) + " - " + sdf.format(endCal.getTime());
				TimeSlot ts = framedGraph.addVertex(tsKey, TimeSlot.class);
				ts.setStartTime(startCal);
				ts.setEndTime(endCal);

				String code = obj.getAsString("session_nr");

				Presentation sess = framedGraph.addVertex(code, Presentation.class);
				sess.setTitle(obj.getAsString("session_title"));
				sess.setDescription(obj.getAsString("session_abstract"));
				sess.setStatus(Event.Status.CONFIRMED);
				sess.setSessionId(code);
				System.out.println("Assigning location - " + locKey + " to session " + obj.getAsString("session_title"));
				sess.addLocation(loc);
				track.addIncludesSession(sess);

				ts.addEvent(sess);

				for (int i = 1; i < 6; i++) {
					String suffix = "";
					if (i > 1) {
						suffix = Integer.toString(i);
					}
					String speaker = obj.getAsString("speaker_name" + suffix);
					if ("".equals(speaker)) {
						break;
					}
					String speakerName = speaker;
					String organization = obj.getAsString("speaker_org" + suffix);
					Attendee att = framedGraph.addVertex(null, Attendee.class);
					System.out.println(speaker);
					int sep = speakerName.indexOf(" ");
					if (sep > -1) {
						String firstName = speakerName.substring(0, sep);
						String lastName = speakerName.substring(sep + 1, speakerName.length());
						att.setFirstName(firstName);
						att.setLastName(lastName);
					} else {
						att.setFirstName(speakerName);
					}
					att.setTwitterId(obj.getAsString("speaker_twitter" + suffix));
					att.setUrl(obj.getAsString("speaker_photourl" + suffix));

					if (!"".equals(organization)) {
						Group org = framedGraph.addVertex(organization, Group.class);
						org.setName(organization);
						org.setType(Group.Type.COMPANY);
						org.addMember(att);
					}

					sess.addPresentedBy(att);
					sess.addAttendingAttendee(att);
					sess.addPlansToAttend(att);
				}

			}

			framedGraph.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(final String[] args) {
		TestRunnerUtil.runAsDominoThread(new EngageDataInitializer(), TestRunnerUtil.NATIVE_SESSION);
	}

	public void timelog(final String message) {
		long curtime = System.nanoTime();
		long elapsed = curtime - marktime;
		marktime = curtime;
		System.out.println(elapsed / 1000000 + " ms: " + message);
	}

}

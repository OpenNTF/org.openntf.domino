package org.openntf.conference.graph;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.graph2.impl.DGraph;
import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

import com.tinkerpop.frames.FramedTransactionalGraph;

public class DataInitializer implements Runnable {
	private long marktime;
	private static final String SRC_DATA_PATH = "OpenNTF Downloads/sphere2015.nsf";
	private SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public DataInitializer() {

	}

	@Override
	public void run() {
		long testStartTime = System.nanoTime();
		marktime = System.nanoTime();
		try {
			timelog("Beginning dataInitializer...");

			// Get / create databases
			Session s = Factory.getSession(SessionType.NATIVE);
			//			Database attendees = s.getDatabase(s.getServerName(), ConferenceGraph.ATTENDEE_PATH, true);
			Database events = s.getDatabase(s.getServerName(), ConferenceGraph.EVENT_PATH, true);
			events.getAllDocuments().removeAll(true);
			//			Database groups = s.getDatabase(s.getServerName(), ConferenceGraph.GROUP_PATH, true);
			//			Database invites = s.getDatabase(s.getServerName(), ConferenceGraph.INVITE_PATH, true);
			Database location = s.getDatabase(s.getServerName(), ConferenceGraph.LOCATION_PATH, true);
			location.getAllDocuments().removeAll(true);
			//			Database times = s.getDatabase(s.getServerName(), ConferenceGraph.TIMES_PATH, true);
			//			Database defaults = s.getDatabase(s.getServerName(), ConferenceGraph.DEFAULT_PATH, true);

			// Initialize the graph
			ConferenceGraph graph = new ConferenceGraph();
			//			graph.initialize();	//NTF already done in constructor
			FramedTransactionalGraph<DGraph> framedGraph = graph.getFramedGraph();

			loadData(s, framedGraph);

		} catch (Throwable t) {
			t.printStackTrace();
		}
		long testEndTime = System.nanoTime();
		System.out.println("Completed " + getClass().getSimpleName() + " run in " + ((testEndTime - testStartTime) / 1000000) + " ms");
	}

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

			View sessions = srcDb.getView("Sessions");
			for (Document doc : sessions.getAllDocuments()) {
				if (!doc.hasItem("$Conflict")) {	// ignore conflicts
					String locKey = doc.getItemValueString("Location");
					Location loc;
					if (!locs.containsKey(locKey)) {
						loc = framedGraph.addVertex(null, Location.class);
						loc.setAddress(doc.getItemValueString("Location"));
						locs.put(locKey, loc);
						System.out.println("Added location - " + locKey);
					} else {
						loc = locs.get(locKey);
						System.out.println("Retrieved location - " + locKey);
					}

					String trackKey = doc.getItemValueString("Categories");
					Track track;
					if (tracks.containsKey(trackKey)) {
						track = framedGraph.addVertex(trackKey, Track.class);
						track.setTitle(doc.getItemValueString("Categories"));
						track.setDescription(doc.getItemValueString("Categories"));
						tracks.put(trackKey, track);
						System.out.println("Added track - " + trackKey);
					} else {
						track = tracks.get(trackKey);
						System.out.println("Retrieved track - " + trackKey);
					}

					//					Date dt = doc.getItemValue("CalendarDateTime", Date.class);
					//					Integer duration = doc.getItemValue("Duration", Integer.class);
					//
					//					TimeSlot ts = framedGraph.addVertex(DATE_FORMAT.format(dt) + "~" + String.valueOf(duration), TimeSlot.class);
					//					ts.setStartTime(dt);
					//					Calendar cal = Calendar.getInstance();
					//					cal.setTime(dt);
					//					cal.add(Calendar.MINUTE, duration);
					//					ts.setEndTime(cal.getTime());

					String code = doc.getItemValueString("SessionID");
					// Not sure if I can combine these, that's for later

					Presentation sess = framedGraph.addVertex(code, Presentation.class);
					sess.setTitle(doc.getItemValueString("Subject"));
					sess.setDescription(doc.getItemValueString("Abstract"));
					sess.setStatus(Event.Status.CONFIRMED);
					System.out.println("Assigning location - " + locKey + " to session " + doc.getItemValueString("Subject"));
					sess.addLocation(loc);
					track.addIncludesSession(sess);

					//ts.addEvent(sess);

					//					for (int i = 1; i < 6; i++) {
					//						String speaker = doc.getItemValueString("Speaker" + String.valueOf(i));
					//						if ("".equals(speaker)) {
					//							break;
					//						}
					//						String speakerName = speaker;
					//						String organization = "";
					//						if (speaker.contains(" - ")) {
					//							int splitPos = speaker.indexOf(" - ");
					//							speakerName = speaker.substring(0, splitPos);
					//							organization = speaker.substring(splitPos + 3, speaker.length());
					//						}
					//						Attendee att = framedGraph.addVertex(null, Attendee.class);
					//						int sep = speakerName.indexOf(" ");
					//						String firstName = speakerName.substring(0, sep);
					//						String lastName = speakerName.substring(sep + 1, speakerName.length());
					//						att.setFirstName(firstName);
					//						att.setLastName(lastName);
					//
					//						if (!"".equals(organization)) {
					//							Group org = framedGraph.addVertex(organization, Group.class);
					//							org.setName(organization);
					//							org.setType(Type.COMPANY);
					//							org.addMember(att);
					//						}
					//
					//						sess.addPresentedBy(att);
					//						sess.addAttendingAttendee(att);
					//						sess.addPlansToAttend(att);
					//					}

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

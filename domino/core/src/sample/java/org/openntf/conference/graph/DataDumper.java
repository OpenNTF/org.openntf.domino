package org.openntf.conference.graph;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TimeZone;

import org.openntf.conference.graph.Group.Type;
import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.View;
import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.graph2.impl.DGraph;
import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.utils.Strings;

import com.tinkerpop.frames.FramedTransactionalGraph;

@SuppressWarnings("unused")
public class DataDumper implements Runnable {
	private long marktime;
	private static final String SRC_DATA_PATH = "OpenNTF Downloads/sphere2015.nsf";

	public DataDumper() {

	}

	@Override
	public void run() {
		long testStartTime = System.nanoTime();
		marktime = System.nanoTime();
		ConferenceGraph graph = new ConferenceGraph();
		DFramedTransactionalGraph<DGraph> framedGraph = graph.getFramedGraph();
		DGraph baseGraph = framedGraph.getBaseGraph();
		//		Iterable<Vertex> vertices = baseGraph.getVertices("@", "Form=\"Presentation\"");
		//		//NTF There's no specific need to do it this way. I just wanted to test the TypeField-based framing
		//
		//		for (Vertex vertex : vertices) {
		//			VertexFrame frame = framedGraph.frame(vertex, DVertexFrame.class);
		//			if (frame instanceof Presentation) {
		//				StringBuilder sb = new StringBuilder();
		//				Map<String, Object> jsonMap = framedGraph.toJsonableMap(frame);
		//				for (String key : jsonMap.keySet()) {
		//					sb.append(key + ": \"" + String.valueOf(jsonMap.get(key)) + "\", ");
		//				}
		//				System.out.println("{" + sb.toString() + "}");
		//			}
		//		}
		long testEndTime = System.nanoTime();

		SimpleDateFormat DATE_FORMAT_UK = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DATE_FORMAT_UK.setTimeZone(TimeZone.getDefault());
		SimpleDateFormat DATE_FORMAT_EST = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DATE_FORMAT_EST.setTimeZone(TimeZone.getTimeZone("EST"));

		Presentation pres = framedGraph.getVertex("ID114", Presentation.class);
		Iterable<TimeSlot> times = pres.getTimes();
		for (TimeSlot ts : times) {
			Calendar sTime = ts.getStartTime();
			Calendar eTime = ts.getEndTime();
			System.out.println("GMT Time: " + DATE_FORMAT_UK.format(sTime.getTime()) + " - " + DATE_FORMAT_UK.format(eTime.getTime()));

			System.out.println("EST Time: " + DATE_FORMAT_EST.format(sTime.getTime()) + " - " + DATE_FORMAT_EST.format(eTime.getTime()));
		}
		System.out.println("Completed " + getClass().getSimpleName() + " run in " + ((testEndTime - testStartTime) / 1000000) + " ms");

	}

	@SuppressWarnings({ "deprecation" })
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

					Calendar startCal = new GregorianCalendar(TimeZone.getTimeZone("EST"));
					startCal.setTime(startDate);
					startCal.set(Calendar.HOUR, startDateTime.getHours());
					startCal.set(Calendar.MINUTE, startDateTime.getMinutes());
					startCal.set(Calendar.SECOND, startDateTime.getSeconds());

					Calendar endCal = new GregorianCalendar(TimeZone.getTimeZone("EST"));
					endCal.setTime(endDate);
					endCal.set(Calendar.HOUR, endDateTime.getHours());
					endCal.set(Calendar.MINUTE, endDateTime.getMinutes());
					endCal.set(Calendar.SECOND, endDateTime.getSeconds());

					String tsKey = sdf.format(startCal.getTime()) + " - " + sdf.format(endCal.getTime());
					TimeSlot ts = framedGraph.addVertex(tsKey, TimeSlot.class);
					ts.setStartTime(startCal);
					ts.setEndTime(endCal);

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
		TestRunnerUtil.runAsDominoThread(new DataDumper(), TestRunnerUtil.NATIVE_SESSION);
	}

	public void timelog(final String message) {
		long curtime = System.nanoTime();
		long elapsed = curtime - marktime;
		marktime = curtime;
		System.out.println(elapsed / 1000000 + " ms: " + message);
	}

}

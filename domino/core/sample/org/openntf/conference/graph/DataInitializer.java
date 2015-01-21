package org.openntf.conference.graph;

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
					Location loc = framedGraph.addVertex(doc.getItemValueString("Location"), Location.class);

					Track track = framedGraph.addVertex(doc.getItemValueString("Categories"), Track.class);
					track.setDescription(track.getTitle());

					String code = doc.getItemValueString("SessionID");
					// Not sure if I can combine these, that's for later

					Presentation sess = framedGraph.addVertex(code, Presentation.class);
					sess.addLocation(loc);
					sess.setTitle(doc.getItemValueString("Subject"));
					sess.setDescription(doc.getItemValueString("Abstract"));
					sess.setStatus(Event.Status.CONFIRMED);
					track.addIncludesSession(sess);

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

package org.openntf.conference.graph.examples;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.openntf.conference.graph.ConferenceGraph;
import org.openntf.conference.graph.Presentation;
import org.openntf.conference.graph.Track;
import org.openntf.domino.graph2.builtin.DVertexFrameComparator;
import org.openntf.domino.junit.TestRunnerUtil;

import com.google.common.collect.Ordering;

public class SessionsByTrack implements Runnable {
	private long marktime;

	public SessionsByTrack() {

	}

	@Override
	public void run() {
		HashMap<String, String> trackLkup = new HashMap<String, String>();
		//		trackLkup.put("Special", "Sp");
		//		trackLkup.put("Strategy/Deployment", "Str");
		//		trackLkup.put("Administration", "Adm");
		trackLkup.put("Development", "Dev");
		//		trackLkup.put("Business", "Bus");
		//		trackLkup.put("Commercial", "Comm");
		long testStartTime = System.nanoTime();
		marktime = System.nanoTime();
		try {
			timelog("Beginning Sessions By Track...");
			ConferenceGraph graph = new ConferenceGraph();
			for (Entry<String, String> track : trackLkup.entrySet()) {
				System.out.println("Outputting sessions ordered by ID for " + track.getKey());

				Track dev = graph.getFramedGraph().getVertex(track.getValue(), Track.class);
				Iterable<Presentation> presentations = dev.getIncludesSessions();
				Ordering ord = Ordering.from(new DVertexFrameComparator("SessionID"));
				List<Presentation> presOrdered = ord.sortedCopy(presentations);
				for (Presentation pres : presOrdered) {

					System.out.println(pres.getSessionId() + ": " + pres.getTitle());
				}
			}

			for (Entry<String, String> track : trackLkup.entrySet()) {
				System.out.println("Outputting sessions ordered by Title for " + track.getKey());

				Track dev = graph.getFramedGraph().getVertex(track.getValue(), Track.class);
				Iterable<Presentation> presentations = dev.getIncludesSessions();
				Ordering ord = Ordering.from(new DVertexFrameComparator("SessionTitle"));
				List<Presentation> presOrdered = ord.sortedCopy(presentations);
				for (Presentation pres : presOrdered) {

					System.out.println(pres.getSessionId() + ": " + pres.getTitle());
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		long testEndTime = System.nanoTime();
		System.out.println("Completed " + getClass().getSimpleName() + " run in " + ((testEndTime - testStartTime) / 1000000) + " ms");
	}

	public void timelog(final String message) {
		long curtime = System.nanoTime();
		long elapsed = curtime - marktime;
		marktime = curtime;
		System.out.println(elapsed / 1000000 + " ms: " + message);
	}

	public static void main(final String[] args) {
		TestRunnerUtil.runAsDominoThread(new SessionsByTrack(), TestRunnerUtil.NATIVE_SESSION);
	}
}

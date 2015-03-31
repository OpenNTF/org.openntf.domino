<<<<<<< HEAD:domino/core/src/sample/java/org/openntf/conference/graph/examples/SessionsByTimeslot.java
package org.openntf.conference.graph.examples;

import java.text.SimpleDateFormat;
import java.util.List;

import org.openntf.conference.graph.Attendee;
import org.openntf.conference.graph.ConferenceGraph;
import org.openntf.conference.graph.Event;
import org.openntf.conference.graph.Presentation;
import org.openntf.conference.graph.TimeSlot;
import org.openntf.domino.Session;
import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

import com.google.common.collect.Ordering;

@SuppressWarnings("unused")
public class SessionsByTimeslot implements Runnable {
	private long marktime;
	private SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM HH:mm");

	public SessionsByTimeslot() {

	}

	@Override
	public void run() {
		long testStartTime = System.nanoTime();
		marktime = System.nanoTime();
		try {
			timelog("Beginning Sessions By TimeSlot...");
			ConferenceGraph graph = new ConferenceGraph();

			Session session = Factory.getSession(SessionType.CURRENT);
			String myName = session.getEffectiveUserName();
			Attendee att = graph.getAttendee(myName, false);

			Ordering<TimeSlot> byStart = new Ordering<TimeSlot>() {

				@Override
				public int compare(final TimeSlot t1, final TimeSlot t2) {
					return t1.getStartTime().compareTo(t2.getStartTime());
				}
			};

			Iterable<TimeSlot> times = graph.getTimeSlots();
			List<TimeSlot> timesSorted = byStart.sortedCopy(times);
			for (TimeSlot ts : timesSorted) {
				System.out.println("Sessions running from " + DATE_FORMAT.format(ts.getStartTime().getTime()) + " to "
						+ DATE_FORMAT.format(ts.getEndTime().getTime()));
				Iterable<Event> presentations = ts.getEvents();
				for (Event evt : presentations) {
					if (evt instanceof Presentation) {
						Presentation pres = (Presentation) evt;
						System.out.println(pres.getSessionId() + ": " + pres.getTitle());
					}

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
		TestRunnerUtil.runAsDominoThread(new SessionsByTimeslot(), TestRunnerUtil.NATIVE_SESSION);
	}
}
=======
package org.openntf.conference.graph.examples;

import java.text.SimpleDateFormat;
import java.util.List;

import org.openntf.conference.graph.Attendee;
import org.openntf.conference.graph.ConferenceGraph;
import org.openntf.conference.graph.Event;
import org.openntf.conference.graph.Presentation;
import org.openntf.conference.graph.TimeSlot;
import org.openntf.domino.Session;
import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

import com.google.common.collect.Ordering;

public class SessionsByTimeslot implements Runnable {
	private long marktime;
	private SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM HH:mm");

	public SessionsByTimeslot() {

	}

	@Override
	public void run() {
		long testStartTime = System.nanoTime();
		marktime = System.nanoTime();
		try {
			timelog("Beginning Sessions By TimeSlot...");
			ConferenceGraph graph = new ConferenceGraph();

			Session session = Factory.getSession(SessionType.CURRENT);
			String myName = session.getEffectiveUserName();
			Attendee att = graph.getAttendee(myName, false);

			Ordering<TimeSlot> byStart = new Ordering<TimeSlot>() {

				@Override
				public int compare(final TimeSlot t1, final TimeSlot t2) {
					return t1.getStartTime().compareTo(t2.getStartTime());
				}
			};

			Iterable<TimeSlot> times = graph.getTimeSlots();
			List<TimeSlot> timesSorted = byStart.sortedCopy(times);
			for (TimeSlot ts : timesSorted) {
				System.out.println("Sessions running from " + DATE_FORMAT.format(ts.getStartTime().getTime()) + " to "
						+ DATE_FORMAT.format(ts.getEndTime().getTime()));
				Iterable<Event> presentations = ts.getEvents();
				for (Event evt : presentations) {
					if (evt instanceof Presentation) {
						Presentation pres = (Presentation) evt;
						System.out.println(pres.getSessionId() + ": " + pres.getTitle());
					}

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
		TestRunnerUtil.runAsDominoThread(new SessionsByTimeslot(), TestRunnerUtil.NATIVE_SESSION);
	}
}
>>>>>>> roland:domino/core/sample/org/openntf/conference/graph/examples/SessionsByTimeslot.java

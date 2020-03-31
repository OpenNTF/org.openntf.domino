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
package org.openntf.conference.graph.examples;

import javolution.util.FastSet;

import org.openntf.conference.graph.Attendee;
import org.openntf.conference.graph.ConferenceGraph;
import org.openntf.conference.graph.Event;
import org.openntf.conference.graph.Presentation;
import org.openntf.domino.graph2.builtin.social.Comment;
import org.openntf.domino.graph2.builtin.social.Rates;
import org.openntf.domino.junit.TestRunnerUtil;

import com.google.common.collect.Lists;

public class MiscTester implements Runnable {
	private long marktime;

	public MiscTester() {

	}

	@Override
	public void run() {
		long testStartTime = System.nanoTime();
		marktime = System.nanoTime();
		try {
			ConferenceGraph graph = new ConferenceGraph();
			Attendee paul = graph.getAttendee("paulswithers");
			Attendee dv = graph.getAttendee("DanieleVistalli");
			System.out.println(paul.getEmail());
			Iterable<Event> evts = paul.getAttendingEvents();
			FastSet<Presentation> presentations = new FastSet<Presentation>();
			for (Event evt : evts) {
				if (evt instanceof Presentation) {
					presentations.add((Presentation) evt);
				}
			}
			System.out.println("Paul is attending " + presentations.size() + " Sessions");

			evts = dv.getAttendingEvents();
			presentations = new FastSet<Presentation>();
			for (Event evt : evts) {
				if (evt instanceof Presentation) {
					presentations.add((Presentation) evt);
				}
			}
			System.out.println("Paul is attending " + presentations.size() + " Sessions");

			evts = paul.getPresentingEvents();
			presentations = new FastSet<Presentation>();
			for (Event evt : evts) {
				if (evt instanceof Presentation) {
					presentations.add((Presentation) evt);
				}
			}
			System.out.println("Paul is presenting " + presentations.size() + " Sessions");
			Presentation pres = presentations.iterator().next();
			Comment comm = graph.getFramedGraph().addVertex(null, Comment.class);
			comm.setBody("This is a test comment");
			paul.addComment(comm);
			pres.addComment(comm);
			paul.addLikeable(comm);
			paul.addLikeable(pres);
			// Uncomment this and you get a duplicate
			//			Rates rate1 = paul.addRates(pres);
			//			rate1.setRating(5);
			// This doesn't give a duplicate
			Rates rate3 = pres.addRater(paul);
			rate3.setRating(5);
			Rates rate2 = dv.addRateable(pres);
			rate2.setRating(2);

			System.out.println("Comments by Paul: " + Lists.newArrayList(paul.getComments()).size());
			for (Comment testComm : paul.getComments()) {
				System.out.println(testComm.getBody());
			}
			System.out.println("Likes by Paul: " + Lists.newArrayList(paul.getLikes()).size());
			System.out.println("Comments on Pres: " + Lists.newArrayList(pres.getComments()).size());
			System.out.println("Likes on Pres: " + Lists.newArrayList(pres.countLikedBys()));
			System.out.println("Rates on Pres: " + Lists.newArrayList(pres.getRates()).size());
			for (Rates r : Lists.newArrayList(pres.getRates())) {
				System.out.println("Rating - " + r.asEdge().getId() + " - " + r.getRating());
			}
			System.out.println("Rates by Paul: " + Lists.newArrayList(paul.getRates()).size());
			System.out.println("Rating by Paul: " + pres.getRaterRating(paul));
			System.out.println("Average: " + pres.getAverageRating());

		} catch (Exception e) {
			e.printStackTrace();
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
		TestRunnerUtil.runAsDominoThread(new MiscTester(), TestRunnerUtil.NATIVE_SESSION);
	}
}

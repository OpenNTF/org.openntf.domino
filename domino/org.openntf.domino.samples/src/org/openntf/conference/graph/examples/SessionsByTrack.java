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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.openntf.conference.graph.Attendee;
import org.openntf.conference.graph.ConferenceGraph;
import org.openntf.conference.graph.Presentation;
import org.openntf.conference.graph.Sponsor;
import org.openntf.conference.graph.Sponsor.Level;
import org.openntf.conference.graph.Track;
import org.openntf.domino.graph2.DEdge;
import org.openntf.domino.graph2.annotations.FramedVertexList;
import org.openntf.domino.graph2.builtin.DVertexFrame;
import org.openntf.domino.graph2.builtin.DVertexFrameComparator;
import org.openntf.domino.graph2.impl.DGraph;
import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.types.CaseInsensitiveString;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.gremlin.java.GremlinPipeline;

@SuppressWarnings("nls")
public class SessionsByTrack implements Runnable {
	private long marktime;

	public SessionsByTrack() {

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void run() {
		HashMap<String, String> trackLkup = new HashMap<String, String>();
		trackLkup.put("Special", "Sp");
		trackLkup.put("Strategy/Deployment", "Str");
		trackLkup.put("Administration", "Adm");
		trackLkup.put("Development", "Dev");
		trackLkup.put("Business", "Bus");
		trackLkup.put("Commercial", "Comm");
		long testStartTime = System.nanoTime();
		marktime = System.nanoTime();
		try {
			timelog("Beginning Sessions By Track...");
			FramedGraph<DGraph> graph2 = new ConferenceGraph().getFramedGraph();
			Iterable<Sponsor> sponsors = graph2.getVertices("Level", Level.BRONZE, Sponsor.class);

			for (Sponsor s : sponsors) {
				System.out.println("Sponsor " + s.getName() + " - " + s.getUrl());
			}

			ConferenceGraph graph = new ConferenceGraph();
			for (Entry<String, String> track : trackLkup.entrySet()) {
				System.out.println("Outputting sessions ordered by ID for " + track.getKey());

				Track dev = graph.getFramedGraph().getVertex(track.getValue(), Track.class);
				System.out.println(dev.getDescription());
				Iterable<Presentation> presentations = dev.getIncludesSessions();
				Ordering<DVertexFrame> ord = Ordering.from(new DVertexFrameComparator("SessionID"));
				List<Presentation> presOrdered = ord.sortedCopy(presentations);
				for (Presentation pres : presOrdered) {
					Attendee att = pres.getPresentingAttendees().iterator().next();
					System.out.println(att.getFullname());
					System.out.println(pres.getSessionId() + ": " + pres.getTitle());
				}
			}

			for (Entry<String, String> track : trackLkup.entrySet()) {
				System.out.println("Outputting sessions ordered by Title for " + track.getKey());

				Track dev = graph.getFramedGraph().getVertex(track.getValue(), Track.class);
				Iterable<Presentation> presentations = dev.getIncludesSessions();
				Ordering<DVertexFrame> ord = Ordering.from(new DVertexFrameComparator("SessionTitle"));
				List<Presentation> presOrdered = ord.sortedCopy(presentations);
				for (Presentation pres : presOrdered) {

					System.out.println(pres.getSessionId() + ": " + pres.getTitle());
				}
			}

			System.out.println("OUTPUTTING SORTBY");

			FramedVertexList<Sponsor> sponsors2 = (FramedVertexList<Sponsor>) graph2.getVertices(null, null, Sponsor.class);
			List<CaseInsensitiveString> keys = new ArrayList<CaseInsensitiveString>();
			keys.add(new CaseInsensitiveString("Level"));
			keys.add(new CaseInsensitiveString("Name"));
			sponsors2.sortBy((List<CharSequence>) (List<?>) keys, true);
			for (Sponsor spon : sponsors2) {
				System.out.println(spon.getLevel().name() + " - " + spon.getName());
			}

			// Throws java.lang.ClassCastException: com.sun.proxy.$Proxy11 incompatible with com.tinkerpop.blueprints.Vertex
			// Yet to track down why
			List<Presentation> presentations = Lists.newArrayList(graph.getFramedGraph().getVertices(null, null, Presentation.class));

			//GremlinPipeline pipe = new GremlinPipeline(presentations).outE("PresentedBy").outV().dedup();
			GremlinPipeline pipe = new GremlinPipeline(presentations).outE("PresentedBy");
			List<DEdge> edges = pipe.toList();
			for (DEdge edge : edges) {
				System.out.println(edge.getVertex(Direction.OUT).getId());
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

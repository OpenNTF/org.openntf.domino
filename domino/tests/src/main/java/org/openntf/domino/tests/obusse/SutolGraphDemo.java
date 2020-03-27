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
package org.openntf.domino.tests.obusse;

import java.util.UUID;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.Session;
import org.openntf.domino.graph2.impl.DConfiguration;
import org.openntf.domino.graph2.impl.DElementStore;
import org.openntf.domino.graph2.impl.DFramedGraphFactory;
import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.graph2.impl.DGraph;
import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

public class SutolGraphDemo implements Runnable {

	public static final String SESSIONS_PATH = "SUTOL/sessions.nsf";
	public static final String PRESENTER_PATH = "SUTOL/presenter.nsf";
	public static final String ATTENDEES_PATH = "SUTOL/attendees.nsf";

	public static void main(final String[] args) {
		TestRunnerUtil.runAsDominoThread(new SutolGraphDemo(), TestRunnerUtil.NATIVE_SESSION);
	}

	/**
	 * thread runner
	 */
	@Override
	public void run() {
		initDbs();
		buildGraph();
		testGraph();
	}

	/**
	 * aux method to reset all data in the graph store
	 */
	private void initDbs() {
		try {
			Session session = Factory.getSession(SessionType.CURRENT);
			Database sessionDb = session.getDatabase(SESSIONS_PATH);
			Database presenterDb = session.getDatabase(PRESENTER_PATH);
			Database attendeeDb = session.getDatabase(ATTENDEES_PATH);

			if (sessionDb != null) {
				System.out.println("Cleaning sessions...");
				removeDocs(sessionDb.getAllDocuments());
			}
			if (presenterDb != null) {
				System.out.println("Cleaning presenter...");
				removeDocs(presenterDb.getAllDocuments());
			}
			if (attendeeDb != null) {
				System.out.println("Cleaning attendees...");
				removeDocs(attendeeDb.getAllDocuments());
			}

			session.recycle();
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void removeDocs(final DocumentCollection col) {
		for (Document doc : col) {
			doc.remove(true);
		}
	}

	private void testGraph() {
		try {
			DFramedTransactionalGraph<DGraph> framedGraph = setupGraph();

			Iterable<ConferenceSession> sessions = framedGraph.getVertices(null, null, ConferenceSession.class);
			for (ConferenceSession sess : sessions) {
				System.out.println("FOUND SESSION: " + sess.getTitle());
				for (Attendee att : sess.getAttendees()) {
					System.out.println("ATTENDEE: " + att.getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String createId() {
		return UUID.randomUUID().toString();
	}

	private void buildGraph() {
		try {
			DFramedTransactionalGraph<DGraph> framedGraph = setupGraph();

			ConferenceSession session1 = framedGraph.addVertex(createId(), ConferenceSession.class);
			session1.setTitle("DDM, Letting Admins Sleep later and stay at Pubs longer since 2005");
			Presenter keith = framedGraph.addVertex("Keith Brooks", Presenter.class);
			keith.setName("Keith Brooks");
			session1.addPresenter(keith);

			ConferenceSession session2 = framedGraph.addVertex(createId(), ConferenceSession.class);
			session2.setTitle("Look mum, no passwords!");
			Presenter martin = framedGraph.addVertex("Martin Leyrer", Presenter.class);
			martin.setName("Martin Leyrer");
			session2.addPresenter(martin);

			ConferenceSession session3 = framedGraph.addVertex(createId(), ConferenceSession.class);
			session3.setTitle("Utilizing the OpenNTF Domino API in Domino Applications");
			Presenter oliver = framedGraph.addVertex("Oliver Busse", Presenter.class);
			oliver.setName("Oliver Busse");
			session3.addPresenter(oliver);

			// attendees
			Attendee attOliver = framedGraph.addVertex("OB", Attendee.class);
			attOliver.setName("Oliver Busse");
			Attendee attPeterParker = framedGraph.addVertex("PP", Attendee.class);
			attPeterParker.setName("Peter Parker");

			session1.addAttendee(attOliver);
			session1.addAttendee(attPeterParker);

			framedGraph.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * init the graph data store(s)
	 * 
	 * @return
	 */
	private DFramedTransactionalGraph<DGraph> setupGraph() {

		DElementStore sessionStore = new DElementStore();
		sessionStore.setStoreKey(SESSIONS_PATH);
		sessionStore.addType(ConferenceSession.class);

		DElementStore attendeeStore = new DElementStore();
		attendeeStore.setStoreKey(ATTENDEES_PATH);
		attendeeStore.addType(Attendee.class);

		DElementStore presenterStore = new DElementStore();
		presenterStore.setStoreKey(PRESENTER_PATH);
		presenterStore.addType(Presenter.class);

		DConfiguration config = new DConfiguration();
		DGraph graph = new DGraph(config);

		config.addElementStore(sessionStore);
		config.addElementStore(attendeeStore);
		config.addElementStore(presenterStore);

		config.setDefaultElementStore(sessionStore.getStoreKey());

		DFramedGraphFactory factory = new DFramedGraphFactory(config);
		DFramedTransactionalGraph<DGraph> fg = (DFramedTransactionalGraph<DGraph>) factory.create(graph);
		return fg;
	}

}

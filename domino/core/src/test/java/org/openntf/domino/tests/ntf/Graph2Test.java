package org.openntf.domino.tests.ntf;

import lotus.domino.NotesFactory;

import org.openntf.domino.Session;
import org.openntf.domino.graph2.impl.DConfiguration;
import org.openntf.domino.graph2.impl.DElementStore;
import org.openntf.domino.graph2.impl.DGraph;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

import com.tinkerpop.blueprints.Vertex;

public class Graph2Test implements Runnable {
	private static int THREAD_COUNT = 1;
	private long marktime;

	public static void main(final String[] args) {
		org.openntf.domino.thread.DominoExecutor de = new org.openntf.domino.thread.DominoExecutor(10);
		for (int i = 0; i < THREAD_COUNT; i++) {
			de.execute(new Graph2Test());
		}
		de.shutdown();
	}

	public void timelog(final String message) {
		long curtime = System.nanoTime();
		long elapsed = curtime - marktime;
		marktime = curtime;
		System.out.println(elapsed / 1000000 + " ms: " + message);
	}

	public Graph2Test() {
		// whatever you might want to do in your constructor, but stay away from Domino objects
	}

	@Override
	public void run() {
		run1();
	}

	public void run1() {
		String crewId = "85257D640018A1B3";
		String movieId = "85257D640018AD81";
		String edgeId = "85257D640018BCDF";
		long testStartTime = System.nanoTime();
		marktime = System.nanoTime();
		Session session = this.getSession();

		try {
			timelog("Beginning graph2 test...");

			DElementStore crewStore = new DElementStore();
			crewStore.setStoreKey(crewId);
			DElementStore movieStore = new DElementStore();
			movieStore.setStoreKey(movieId);
			DElementStore edgeStore = new DElementStore();
			edgeStore.setStoreKey(edgeId);
			DConfiguration config = new DConfiguration();
			config.addElementStore(crewStore);
			config.addElementStore(movieStore);
			config.addElementStore(edgeStore);
			DGraph graph = new DGraph(config);
			Vertex starwars = graph.addVertex(movieId + "Star Wars");
			starwars.setProperty("Title", "Star Wars");

			Vertex empire = graph.addVertex(movieId + "The Empire Strikes Back");
			starwars.setProperty("Title", "The Empire Strikes Back");

			Vertex jedi = graph.addVertex(movieId + "Return of the Jedi");
			starwars.setProperty("Title", "Return of the Jedi");

			Vertex lucas = graph.addVertex(crewId + "George Lucas");
			lucas.setProperty("firstName", "George");
			lucas.setProperty("lastName", "Lucas");
			Vertex kershner = graph.addVertex(crewId + "Irvin Kershner");
			kershner.setProperty("firstName", "Irvin");
			kershner.setProperty("lastName", "Kershner");

		} catch (Throwable t) {
			t.printStackTrace();
		}
		long testEndTime = System.nanoTime();
		System.out.println("Completed " + getClass().getSimpleName() + " run in " + ((testEndTime - testStartTime) / 1000000) + " ms");
	}

	protected Session getSession() {
		try {
			Session session = Factory.fromLotus(NotesFactory.createSession(), Session.SCHEMA, null);
			return session;
		} catch (Throwable t) {
			DominoUtils.handleException(t);
			return null;
		}
	}
}

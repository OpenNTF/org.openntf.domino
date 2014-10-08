package org.openntf.domino.tests.spanky;

import lotus.domino.NotesFactory;

import org.openntf.domino.Name;
import org.openntf.domino.Session;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Strings;

public class SpawnedRecordID implements Runnable {
	private static int THREAD_COUNT = 1;

	public static void main(final String[] args) {
		org.openntf.domino.thread.DominoExecutor de = new org.openntf.domino.thread.DominoExecutor(10);
		for (int i = 0; i < THREAD_COUNT; i++) {
			de.execute(new SpawnedRecordID());
		}
		de.shutdown();
	}

	public SpawnedRecordID() {
		// whatever you might want to do in your constructor, but stay away from Domino objects
	}

	@Override
	public void run() {
		try {

			Session session = this.getSession();
			String effective = session.getEffectiveUserName();
			Name name = session.createName((Strings.isBlankString(effective)) ? "Bucky WonderCrust" : effective);

			System.out.println("Spawning RecordIDs for " + name.getAbbreviated());

			for (int i = 0; i < 10; i++) {
				System.out.println("Spawned RecordID: " + Strings.getSpawnedRecordID(name));
			}

		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	protected Session getSession() {
		try {
			Session session = Factory.fromLotus(NotesFactory.createSession(), Session.class, null);
			return session;
		} catch (Throwable t) {
			DominoUtils.handleException(t);
			return null;
		}
	}
}

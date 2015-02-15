/**
 * 
 */
package org.openntf.domino.tests.jpg;

import java.util.Date;

import org.openntf.domino.Session;

/**
 * @author jgallagher
 * 
 */
public class KVTest {
	private KVTest() {
	}

	// e.g. KVStore.testKVStore(session, "Telamon/Frost", "tests/b", 2);
	public static void testKVStore(final Session session, final String server, final String baseName, final int places) {
		KeyValueStore store = new KeyValueStore(session, server, baseName, places);
		long initStart = System.nanoTime();
		store.initializeDatabases();
		long initEnd = System.nanoTime();

		long now = (new Date()).getTime();

		long saveStart = System.nanoTime();
		for (int i = 0; i < 500; i++) {
			store.put(i + "_" + now, i);
		}
		long saveEnd = System.nanoTime();

		long readStart = System.nanoTime();
		for (int i = 0; i < 500; i++) {
			store.get(i + "_" + now);
		}
		long readEnd = System.nanoTime();

		System.out.println("Initialization took " + ((initEnd - initStart) / 1000 / 1000) + "ms");
		System.out.println("Storage took " + ((saveEnd - saveStart) / 1000 / 1000) + "ms");
		System.out.println("Reading took " + ((readEnd - readStart) / 1000 / 1000) + "ms");
	}

	public static void testKVStore(final Session session, final KeyValueStore.ServerStrategy serverstrategy, final String baseName,
			final int places) {
		KeyValueStore store = new KeyValueStore(session, serverstrategy, baseName, places);
		long initStart = System.nanoTime();
		store.initializeDatabases();
		long initEnd = System.nanoTime();

		long now = (new Date()).getTime();

		long saveStart = System.nanoTime();
		for (int i = 0; i < 500; i++) {
			store.put(i + "_" + now, i);
		}
		long saveEnd = System.nanoTime();

		long readStart = System.nanoTime();
		for (int i = 0; i < 500; i++) {
			store.get(i + "_" + now);
		}
		long readEnd = System.nanoTime();

		System.out.println("Initialization took " + ((initEnd - initStart) / 1000 / 1000) + "ms");
		System.out.println("Storage took " + ((saveEnd - saveStart) / 1000 / 1000) + "ms");
		System.out.println("Reading took " + ((readEnd - readStart) / 1000 / 1000) + "ms");
	}
}
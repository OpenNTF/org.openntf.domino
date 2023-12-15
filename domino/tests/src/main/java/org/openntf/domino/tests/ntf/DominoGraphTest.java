/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
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
package org.openntf.domino.tests.ntf;

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.graph.DominoGraph;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

import com.tinkerpop.blueprints.Vertex;

public enum DominoGraphTest {
	INSTANCE;

	private DominoGraphTest() {
	}

	private static final int THREAD_COUNT = 1;
	private static final int delay = 1000;
	private static final String server = "";
	private static final String dbPath = "graphtest.nsf";

	static class Doer implements Runnable {

		@Override
		public void run() {
			long start = System.nanoTime();
			Session s = Factory.getSession(SessionType.FULL_ACCESS);
			Database db = s.getDatabase(server, dbPath);
			DominoGraph graph = new DominoGraph(db);
			for (int i = 1; i <= 10000; i++) {
				Vertex v1 = graph.addVertex(null);
				v1.setProperty("Test1", i);

				Vertex v2 = graph.addVertex(null);
				v2.setProperty("Test1", i);

				graph.addEdge(null, v1, v2, "IAmInYou");
				if (i % 1000 == 0) {
					System.out.println("Iterated test " + i + " times.  Committing...");
					graph.commit();
				}
			}
			graph.commit();
			System.gc();

			long elapsed = System.nanoTime() - start;
			StringBuilder sb = new StringBuilder();
			sb.append("Thread " + Thread.currentThread().getName());
			sb.append(" *** ALL OPERATIONS COMPLETE elapsed time: ");
			sb.append(elapsed / 1000000 + "ms");
			System.out.println(sb.toString());
		}

	}

	public static void main(final String[] args) {

		DominoThread[] threads = new DominoThread[THREAD_COUNT];
		for (int i = 0; i < THREAD_COUNT; i++) {
			threads[i] = new DominoThread(new Doer(), "Scratch Test" + i);
		}

		for (DominoThread thread : threads) {
			thread.start();
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e1) {
				DominoUtils.handleException(e1);

			}
		}

	}
}

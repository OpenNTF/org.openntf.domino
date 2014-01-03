package org.openntf.domino.tests.ntf;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.Session.RunContext;
import org.openntf.domino.schema.IDatabaseSchema;
import org.openntf.domino.schema.IDocumentDefinition;
import org.openntf.domino.schema.impl.DatabaseSchema;
import org.openntf.domino.schema.impl.DocumentDefinition;
import org.openntf.domino.thread.AbstractDominoRunnable;
import org.openntf.domino.thread.DominoExecutor;
import org.openntf.domino.utils.Factory;

public enum DominoSchemaScratchTest {
	INSTANCE;

	private DominoSchemaScratchTest() {
		// TODO Auto-generated constructor stub
	}

	private static final int THREAD_COUNT = 6;
	private static final boolean INCLUDE_FORMS = false;
	private static final int delay = 200;
	// private static final String server = "CN=DevilDog/O=REDPILL";
	private static final String server = "";
	private static final String dbPath = "events4.nsf";

	static class Doer extends AbstractDominoRunnable {
		public void run() {
			long start = System.nanoTime();
			DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");

			Session s = Factory.getSessionFullAccess();
			RunContext rc = Factory.getRunContext();
			Database db = s.getDatabase(server, dbPath);
			IDatabaseSchema schema = new DatabaseSchema();
			IDocumentDefinition docDef1 = new DocumentDefinition();

			long elapsed = System.nanoTime() - start;
			StringBuilder sb = new StringBuilder();
			sb.append("Thread " + Thread.currentThread().getName());
			sb.append(" *** ALL OPERATIONS COMPLETE elapsed time: ");
			sb.append(elapsed / 1000000 + "ms.");

		}

		/* (non-Javadoc)
		 * @see org.openntf.domino.thread.AbstractDominoRunnable#shouldStop()
		 */
		@Override
		public boolean shouldStop() {
			return false;
		}

	}

	public static void main(final String[] args) {
		DominoExecutor de = new DominoExecutor(10);
		for (int i = 0; i < THREAD_COUNT; i++) {
			de.execute(new Doer());
		}
		DominoExecutor de2 = new DominoExecutor(10);

		for (int i = 0; i < THREAD_COUNT; i++) {
			de2.execute(new Doer());
		}

		de.shutdown();
		de2.shutdown();

	}
}

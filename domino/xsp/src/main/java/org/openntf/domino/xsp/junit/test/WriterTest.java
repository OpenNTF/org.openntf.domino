package org.openntf.domino.xsp.junit.test;

import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openntf.domino.junit.SessionDb;
import org.openntf.domino.thread.WorkerExecutor;
import org.openntf.domino.xsp.junit.ModuleJUnitRunner;
import org.openntf.domino.xsp.xots.TaskletWorkerExecutor;
import org.openntf.junit4xpages.OsgiTest;

@OsgiTest
@SessionDb("entwicklung/jfof4/proglibjfof.nsf")
@RunWith(ModuleJUnitRunner.class)
public class WriterTest {

	@Test
	public void testWriter() throws InterruptedException, ExecutionException {

		WorkerExecutor<String> writer = new TaskletWorkerExecutor<String>(DummyAsyncWriter.class);

		for (int i = 0; i < 10; i++) {
			writer.send("I:" + i);
		}
		Thread.sleep(3000);
		for (int i = 10; i < 20; i++) {
			writer.send("I:" + i);
		}
		Thread.sleep(10000);
		for (int i = 20; i < 30; i++) {
			writer.send("I:" + i);
		}
		Thread.sleep(3000);
		for (int i = 30; i < 40; i++) {
			writer.send("I:" + i);
		}
	}
}

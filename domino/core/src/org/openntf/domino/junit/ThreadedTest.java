package org.openntf.domino.junit;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Test;
import org.openntf.domino.xots.XotsDaemon;

public abstract class ThreadedTest implements Callable<String> {

	@Test
	public void runTest() throws InterruptedException, ExecutionException, IllegalAccessException, InstantiationException {
		int threadCount = getThreadCount();
		Future<String> futures[] = new Future[threadCount];
		int i;
		for (i = 0; i < threadCount; i++) {
			futures[i] = XotsDaemon.getInstance().submit(this.getClass().newInstance());
		}
		for (i = 0; i < threadCount; i++) {
			assertEquals("SUCCESS", futures[i].get());
		}
	}

	protected abstract int getThreadCount();
}

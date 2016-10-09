package org.openntf.domino.junit;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Test;
import org.openntf.domino.xots.Xots;

public abstract class ThreadedTest implements Callable<String> {

	@Test
	public void runTest() throws InterruptedException, ExecutionException, IllegalAccessException, InstantiationException {
		int threadCount = getThreadCount();
		@SuppressWarnings("unchecked")
		Future<String> futures[] = new Future[threadCount];
		int i;
		for (i = 0; i < threadCount; i++) {
			futures[i] = Xots.getService().submit(this.getClass().newInstance());
		}
		for (i = 0; i < threadCount; i++) {
			assertEquals("SUCCESS", futures[i].get());
		}
	}

	protected abstract int getThreadCount();
}

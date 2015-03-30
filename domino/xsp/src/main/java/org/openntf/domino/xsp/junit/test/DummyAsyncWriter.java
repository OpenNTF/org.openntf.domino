package org.openntf.domino.xsp.junit.test;

import org.openntf.domino.thread.TaskletWorker;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.xots.Tasklet;

@Tasklet
public class DummyAsyncWriter implements TaskletWorker<String> {

	@Override
	public void process(final String t) {
		System.out.println("Writer says: " + t + ": Session" + Factory.getSession());

	}

	@Override
	public void startUp() {
		System.out.println("Start DummyAsyncWriter");

	}

	@Override
	public void tearDown() {
		// TODO Auto-generated method stub
		System.out.println("Stop DummyAsyncWriter");
	}

}

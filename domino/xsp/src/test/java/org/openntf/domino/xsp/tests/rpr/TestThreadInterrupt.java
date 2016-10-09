package org.openntf.domino.xsp.tests.rpr;

import lotus.domino.NotesException;

import org.junit.Test;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import org.openntf.junit4xpages.OsgiTest;

import com.ibm.domino.xsp.module.nsf.NotesContext;

@OsgiTest
public class TestThreadInterrupt implements Runnable {

	@Test
	public void test() {
		JUnitCore runner = new org.junit.runner.JUnitCore();
		RunListener listener = new TextListener(System.out);
		runner.addListener(listener);
		Result result = runner.run(NotesRunnerTest.class);
		System.out.println("RESULT");
		System.out.println(result);
	}

	@Override
	public void run() {

		NotesContext ctx = new NotesContext(null);
		NotesContext.initThread(ctx);
		try {
			System.out.println(ctx.getCurrentSession().getEffectiveUserName());
		} catch (NotesException e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		NotesContext.termThread();

	}
}

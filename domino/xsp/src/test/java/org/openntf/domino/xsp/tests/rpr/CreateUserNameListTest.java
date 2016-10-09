package org.openntf.domino.xsp.tests.rpr;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openntf.domino.junit.DominoJUnitRunner;
import org.openntf.domino.thread.DominoThread;
import org.openntf.junit4xpages.OsgiTest;

import com.ibm.domino.napi.NException;
import com.ibm.domino.napi.c.NotesUtil;
import com.ibm.domino.napi.c.Os;

@RunWith(DominoJUnitRunner.class)
@OsgiTest
public class CreateUserNameListTest {

	private class UserNameListCreator implements Runnable {
		String s = "Test";
		int i;

		@Override
		public void run() {
			while (i < 100000 && !Thread.currentThread().isInterrupted()) {
				try {
					long userHandle = NotesUtil.createUserNameList(i + s);
					s = s + "x";
					if (i++ % 100 == 0)
						System.out.println(userHandle + " i:" + i + " size" + Os.OSMemGetSize(userHandle));
					//Os.OSMemFree(userHandle);

				} catch (NException e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Test
	public void testManyHandles() throws NException, InterruptedException {
		UserNameListCreator runner = new UserNameListCreator();
		List<Thread> threads = new ArrayList<Thread>();
		System.out.println("Creating threads");
		for (int i = 0; i < 10; i++) {
			threads.add(new DominoThread(runner));
		}
		System.out.println("Starting threads");
		for (Thread thread : threads) {
			thread.start();
			Thread.sleep(100);
		}

	}
}

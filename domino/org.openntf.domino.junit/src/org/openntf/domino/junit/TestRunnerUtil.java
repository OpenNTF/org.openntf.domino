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
package org.openntf.domino.junit;

import java.util.Collection;

import org.openntf.domino.session.ISessionFactory;
import org.openntf.domino.session.NativeSessionFactory;
import org.openntf.domino.session.TrustedSessionFactory;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

@SuppressWarnings("nls")
public enum TestRunnerUtil {
	;

	public static ISessionFactory NATIVE_SESSION = new NativeSessionFactory(null);
	public static ISessionFactory TRUSTED_SESSION = new TrustedSessionFactory(null);

	public static void runAsDominoThread(final Runnable r, final ISessionFactory sf) {
		Factory.startup();
		lotus.domino.NotesThread.sinitThread();
		Factory.initThread(Factory.STRICT_THREAD_CONFIG);
		Factory.setSessionFactory(sf, SessionType.CURRENT);

		Thread t = new DominoThread(r, "TestRunner");
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Factory.termThread();
		lotus.domino.NotesThread.stermThread();
		Factory.shutdown();
	}

	public static void runAsDominoThread(final Runnable r, final ISessionFactory sf, final int instances) {
		Factory.startup();
		lotus.domino.NotesThread.sinitThread();
		Factory.initThread(Factory.STRICT_THREAD_CONFIG);
		Factory.setSessionFactory(sf, SessionType.CURRENT);

		Thread[] t = new Thread[instances];
		for (int i = 0; i < instances; i++) {
			t[i] = new DominoThread(r, "TestRunner-" + i);
			//System.out.println("Starting Thread " + t[i].getName());
			t[i].start();
		}

		try {
			for (int i = 0; i < instances; i++) {
				t[i].join();
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Factory.termThread();
		lotus.domino.NotesThread.stermThread();
		Factory.shutdown();
	}

	public static void runAsDominoThread(final Class<? extends Runnable>[] runClasses, final ISessionFactory sf, final int instances) {
		Factory.startup();
		lotus.domino.NotesThread.sinitThread();
		Factory.initThread(Factory.STRICT_THREAD_CONFIG);
		Factory.setSessionFactory(sf, SessionType.CURRENT);
		Thread[] t = new Thread[instances * runClasses.length];

		int classCount = 0;
		for (Class<? extends Runnable> r : runClasses) {
			int tInst = classCount++ * instances;
			for (int i = 0; i < instances; i++) {
				try {
					t[tInst + i] = new DominoThread(r.newInstance(), "TestDominoRunner-" + tInst);
					t[tInst + i].start();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				}
			}
		}

		try {
			for (int i = 0; i < t.length; i++) {
				t[i].join();
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Factory.termThread();
		lotus.domino.NotesThread.stermThread();
		Factory.shutdown();
	}

	public static void runAsDominoThread(final Collection<Class<? extends Runnable>> runClasses, final ISessionFactory sf,
			final int instances) {
		Factory.startup();
		lotus.domino.NotesThread.sinitThread();
		Factory.initThread(Factory.STRICT_THREAD_CONFIG);
		Factory.setSessionFactory(sf, SessionType.CURRENT);
		Thread[] t = new Thread[instances * runClasses.size()];

		for (Class<? extends Runnable> r : runClasses) {
			for (int i = 0; i < instances; i++) {
				try {
					t[i] = new DominoThread(r.newInstance(), "TestRunner-" + i);
					t[i].start();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				}
			}
		}

		try {
			for (int i = 0; i < t.length; i++) {
				t[i].join();
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Factory.termThread();
		lotus.domino.NotesThread.stermThread();
		Factory.shutdown();
	}

	public static void runAsDominoThread(final Class<? extends Runnable> r, final ISessionFactory sf, final int instances) {
		Factory.startup();
		lotus.domino.NotesThread.sinitThread();
		Factory.initThread(Factory.STRICT_THREAD_CONFIG);
		Factory.setSessionFactory(sf, SessionType.CURRENT);

		Thread[] t = new Thread[instances];
		for (int i = 0; i < instances; i++) {
			try {
				t[i] = new DominoThread(r.newInstance(), "TestRunner-" + i);
				t[i].start();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			}
		}

		try {
			for (int i = 0; i < instances; i++) {
				t[i].join();
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Factory.termThread();
		lotus.domino.NotesThread.stermThread();
		Factory.shutdown();

	}

	public static void runAsNotesThread(final Class<? extends Runnable> r, final int instances) {
		lotus.domino.NotesThread.sinitThread();

		Thread[] t = new Thread[instances];
		for (int i = 0; i < instances; i++) {
			try {
				t[i] = new lotus.domino.NotesThread(r.newInstance(), "TestNotesRunner-" + i);
				t[i].start();
				Thread.sleep(300); // sleep some millis, as the legacy notes API may crash
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		try {
			for (int i = 0; i < instances; i++) {
				t[i].join();
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		lotus.domino.NotesThread.stermThread();

	}

}

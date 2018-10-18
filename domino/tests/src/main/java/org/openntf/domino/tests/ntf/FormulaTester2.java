package org.openntf.domino.tests.ntf;

import java.util.Vector;

import org.openntf.domino.Session;
import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

@SuppressWarnings("unused")
public class FormulaTester2 implements Runnable {
	public static void main(final String[] args) {
		//		Boolean.getBoolean("lotus.notes.internal.InfoPaneBuilder");
		//		String libname = Platform.getLibName("lsxbe");
		System.out.println(System.getProperty("java.library.path"));
		//		System.loadLibrary(libname);
		//		try {
		//			Field ntIsLoaded = lotus.domino.NotesThread.class.getDeclaredField("isLoaded");
		//			ntIsLoaded.setAccessible(true);
		//			ntIsLoaded.setBoolean(null, true);
		//			System.out.println("isLoaded: " + lotus.domino.NotesThread.isLoaded);
		//		} catch (Exception e) {
		//			e.printStackTrace();
		//		}
		TestRunnerUtil.runAsNotesThread(FormulaTester.class, 4);
	}

	public FormulaTester2() {
		// whatever you might want to do in your constructor, but stay away from Domino objects
	}

	@Override
	public void run() {
		Session session = Factory.getSession(SessionType.NATIVE);
		String source = "@TextToTime(\"01/01/2014-02/15/2014\")";
		Object raw = session.evaluate(source);
		if (raw instanceof Vector) {
			for (Object curraw : (Vector) raw) {
				System.out.println("Found a " + curraw.getClass());
			}
		}
	}

}

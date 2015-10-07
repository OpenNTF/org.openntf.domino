package org.openntf.dominoTests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.View;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xots.Tasklet;
import org.openntf.domino.xots.Xots;
import org.openntf.domino.xsp.XspOpenLogUtil;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public class XotsTests {

	@Tasklet(session = Tasklet.Session.CLONE)
	private static class SessionCallable implements Callable<String> {
		public String call() {
			try {
				return Factory.getSession(SessionType.CURRENT).getEffectiveUserName();
			} catch (Throwable t) {
				t.printStackTrace();
				return t.getMessage();
			}
		}
	}

	@Tasklet(session = Tasklet.Session.CLONE)
	private static class UserLookup implements Callable<String> {
		private String state_;
		private String stateName_;

		public UserLookup(String state, String stateName) {
			super();
			state_ = state;
			stateName_ = stateName;
		}

		public String call() {
			try {
				StringBuilder sb = new StringBuilder();
				View people = Factory.getSession(SessionType.CURRENT).getCurrentDatabase()
						.getView("AllContactsByState");
				System.out.println("Processing " + state_);
				sb.append(stateName_ + ": ");
				for (Document doc : people.getAllDocumentsByKey(state_, true)) {
					String name = doc.getItemValueString("FirstName") + " " + doc.getItemValueString("LastName");
					System.out.println("Adding " + name);
					sb.append(name);
					sb.append(", ");
				}
				return sb.substring(0, sb.length() - 2) + "<br/>";
			} catch (Throwable t) {
				t.printStackTrace();
				return t.getMessage();
			}
		}
	}

	@Tasklet(session = Tasklet.Session.CLONE)
	private static class UserOutput implements Runnable {
		private Map<String, Object> sessScope;

		public UserOutput(Map<String, Object> scopeObj) {
			sessScope = scopeObj;
		}

		public void run() {
			try {
				if (sessScope.containsKey("javaXotsOutput")) {
					sessScope.put("javaXotsOutput", null);
				}
				StringBuilder sb = new StringBuilder();
				Database currDb = Factory.getSession(SessionType.CURRENT).getCurrentDatabase();
				View states = currDb.getView("AllStates");
				View people = currDb.getView("AllContactsByState");
				for (Document state : states.getAllDocuments()) {
					String name = state.getItemValueString("Name");
					String key = state.getItemValueString("Key");
					sb.append("Processing " + name + "....<br/>");
					StringBuilder names = new StringBuilder();
					for (Document doc : people.getAllDocumentsByKey(key, true)) {
						String personName = doc.getItemValueString("FirstName") + " "
								+ doc.getItemValueString("LastName");
						names.append(personName);
						names.append(", ");
					}
					if (names.length() > 2) {
						sb.append(names.substring(0, names.length() - 2) + "<br/>");
					} else {
						sb.append("No names found.<br/>");
					}
				}
				sessScope.put("javaXotsOutput", sb.toString());
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}

	}

	public static void testSessionPassing() {
		try {
			Future<String> future = Xots.getService().submit(new SessionCallable());
			ExtLibUtil.getViewScope().put("MessageFromXots", future.get());
		} catch (Throwable t) {
			XspOpenLogUtil.logError(t);
		}
	}

	public static void testThread() {
		try {
			HashMap<String, String> states = new HashMap<String, String>();
			View allStates = Factory.getSession(SessionType.CURRENT).getCurrentDatabase().getView("AllStates");
			// Get first five states
			for (Document doc : allStates.getAllDocuments()) {
				states.put(doc.getItemValueString("Key"), doc.getItemValueString("Name"));
			}

			// Create a callable for each State
			List<Future<String>> results = new ArrayList<Future<String>>();
			for (String key : states.keySet()) {
				UserLookup getUsers = new UserLookup(key, states.get(key));
				results.add(Xots.getService().submit(getUsers));
			}

			// Now loop through the Xots tasks, and get the results
			TreeSet<String> output = new TreeSet<String>();
			for (Future<String> f : results) {
				output.add(f.get());
			}
			ExtLibUtil.getViewScope().put("MessageFromIterableXots", output);
		} catch (Throwable t) {
			XspOpenLogUtil.logError(t);
		}
	}

	public static void testBackground() {
		Xots.getService().submit(new UserOutput(ExtLibUtil.getSessionScope()));
	}

}

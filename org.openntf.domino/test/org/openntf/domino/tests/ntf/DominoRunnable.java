package org.openntf.domino.tests.ntf;

import java.util.List;

import lotus.domino.NotesFactory;

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.helpers.Formula;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class DominoRunnable implements Runnable {
	public static void main(final String[] args) {
		DominoThread thread = new DominoThread(new DominoRunnable(), "My thread");
		thread.start();
	}

	public DominoRunnable() {
		// whatever you might want to do in your constructor, but stay away from Domino objects
	}

	@Override
	public void run() {
		Session session = this.getSession();
		Database db = session.getDatabase("", "names.nsf");
		Formula formula = new Formula();
		String source = "thing := ((\"this\" + \" \") + \"that\") +(\" \" + \"the other\");\r\n"
				+ "\"me\" + \" \" + (\"myself\"  + thing) + @UserName";
		formula.setExpression(source);
		org.openntf.domino.helpers.Formula.Parser parser = formula.getParser();
		parser.parse();
		List<String> literals = parser.getLiterals();
		System.out.println("BEGIN LITERALS");
		for (String literal : literals) {
			System.out.print(literal + ", ");
		}
		System.out.println("END LITERALS");
		List<String> functions = parser.getFunctions();
		System.out.println("BEGIN FUNCTIONS");
		for (String function : functions) {
			System.out.print(function + ", ");
		}
		System.out.println("END FUNCTIONS");
		List<String> localVars = parser.getLocalVars();
		System.out.println("BEGIN VARIABLES");
		for (String var : localVars) {
			System.out.print(var + ", ");
		}
		System.out.println("END VARIABLES");
	}

	protected Session getSession() {
		try {
			Session session = Factory.fromLotus(NotesFactory.createSession(), Session.class, null);
			return session;
		} catch (Throwable t) {
			DominoUtils.handleException(t);
			return null;
		}
	}
}

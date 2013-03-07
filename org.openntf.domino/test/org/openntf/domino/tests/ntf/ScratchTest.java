package org.openntf.domino.tests.ntf;

import lotus.notes.NotesThread;

import org.openntf.domino.utils.Factory;

public enum ScratchTest {
	INSTANCE;

	private ScratchTest() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NotesThread.sinitThread();
		org.openntf.domino.Session s = Factory.getSession();
		System.out.println("Name: " + s.getEffectiveUserName());
		NotesThread.stermThread();
	}

}

package org.openntf.domino.tests.ntf;

import java.util.ArrayList;
import java.util.List;

import lotus.domino.NotesFactory;

import org.openntf.domino.Session;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class HexToByteConversionTest implements Runnable {
	public static void main(final String[] args) {
		DominoThread thread = new DominoThread(new HexToByteConversionTest(), "My thread");
		thread.start();
	}

	public HexToByteConversionTest() {
		// whatever you might want to do in your constructor, but stay away from Domino objects
	}

	@Override
	public void run() {
		//		Session session = this.getSession();
		List<String> hexes = new ArrayList<String>();
		hexes.add("DEAD");
		hexes.add("85255D220072624");
		hexes.add("85255D220072624285255BA400761D6C");

		for (String s : hexes) {
			byte[] bin = DominoUtils.toByteArray(s);
			String newHex = DominoUtils.toHex(bin);
			System.out.println("String " + s + " converted to a byte array of length " + bin.length + " and then back into string "
					+ newHex);
		}
	}

	protected Session getSession() {
		try {
			Session session = Factory.fromLotus(NotesFactory.createSession(), Session.SCHEMA, null);
			return session;
		} catch (Throwable t) {
			DominoUtils.handleException(t);
			return null;
		}
	}
}

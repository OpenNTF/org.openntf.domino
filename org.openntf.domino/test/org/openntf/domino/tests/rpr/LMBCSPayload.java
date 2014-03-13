package org.openntf.domino.tests.rpr;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import org.openntf.domino.Session;
import org.openntf.domino.Stream;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.LMBCSUtils;

public class LMBCSPayload implements Runnable {
	public static void main(final String[] args) {
		DominoThread thread = new DominoThread(new LMBCSPayload(), "My thread");
		thread.start();
	}

	public LMBCSPayload() {
		// whatever you might want to do in your constructor, but stay away from Domino objects
	}

	@Override
	public void run() {
		Session sess = Factory.getSession();
		boolean succ = true;
		char[] c = new char[1];
		int[] payloads = new int[2];
		Vector<String> diffv = new Vector<String>();
		try {
			//			PrintStream prs = new PrintStream("d:/stein/misc/payload-nativelotus.txt");
			for (int i = 0; i < 65536; i++) {
				if (i % 1000 == 0)
					System.out.println(i + " ...");
				c[0] = (char) i;
				if (!cmpOne(sess, new String(c), payloads, false)) {
					diffv.add("\t" + i + ":\tLotus=" + payloads[0] + "\tOwn=" + payloads[1]);
					succ = false;
				}
				//				prs.printf("%5d   %04x   %d\n", i, i, payloads[0]);
			}
			//			prs.close();
			if (succ)
				System.out.println("All payloads are equal");
			else {
				System.out.println("Differences (" + diffv.size() + "):");
				for (String s : diffv)
					System.out.println(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Factory.terminate();
	}

	private boolean cmpOne(final Session sess, final String s, final int[] payloads, final boolean protIfEq) throws IOException {
		payloads[0] = getStrPayloadLotus(sess, s);
		payloads[1] = LMBCSUtils.getPayload(s);
		if (payloads[0] == payloads[1]) {
			if (protIfEq)
				System.out.println("Payloads for \"" + s + "\" are equal: " + payloads[0]);
			return true;
		}
		System.out.println("Payloads for \"" + s + "\" are different: Lotus=" + payloads[0] + ", Own=" + payloads[1]);
		return false;
	}

	private int getStrPayloadLotus(final Session sess, final String whose) throws IOException {

		int payload = 0;
		File fAux = null;
		Stream str = null;
		try {
			fAux = File.createTempFile("ntfdom", "aux.tmp");
			str = sess.createStream();
			str.open(fAux.getPath(), "LMBCS");
			str.writeText(whose);
			payload = str.getBytes();

		} finally {
			if (str != null)
				str.close();
			if (fAux != null)
				fAux.delete();
		}
		return payload;
	}

}

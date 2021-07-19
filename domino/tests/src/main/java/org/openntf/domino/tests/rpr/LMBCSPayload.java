/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
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
package org.openntf.domino.tests.rpr;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openntf.domino.Session;
import org.openntf.domino.Stream;
import org.openntf.domino.junit.DominoJUnitRunner;
import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.utils.LMBCSUtils;

/**
 * This test checks if the LMBSUtils works correctly. It computes LMBCS payload by <br>
 * a) writing to a temp file and checking it's length (horrible slow)<br>
 * b) using LMBSUtils.getPayload()
 * 
 * The result is compared. At the end of the test you should see "All payloads are equal". Then you know that the LMBCUtils are working
 * correctly
 * 
 * @author Manfred Steinsiek, Foconis AG
 * 
 */
@RunWith(DominoJUnitRunner.class)
public class LMBCSPayload implements Runnable {
	public static void main(final String[] args) {
		TestRunnerUtil.runAsDominoThread(new LMBCSPayload(), TestRunnerUtil.NATIVE_SESSION);
	}

	public LMBCSPayload() {
		// whatever you might want to do in your constructor, but stay away from Domino objects
	}

	@Override
	@Test
	public void run() {
		Session sess = Factory.getSession(SessionType.CURRENT);
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
				Assert.fail("Payloads are not equal");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
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

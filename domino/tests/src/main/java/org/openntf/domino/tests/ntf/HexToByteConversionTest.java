/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
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
package org.openntf.domino.tests.ntf;

import java.util.ArrayList;
import java.util.List;

import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.DominoUtils;

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

}

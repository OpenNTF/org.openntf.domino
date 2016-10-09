/*
 * © Copyright FOCONIS AG, 2014
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 * 
 */
package org.openntf.domino.utils;

/**
 * Utility enum to compute LMBCS length (does no LMBCS conversion!)
 * 
 * @author steinsiek
 * 
 */
public enum LMBCSUtils {
	;

	/** the payload array. See static initializer */
	private static byte[] lPayloadArray = null;

	/* ======================================================================= */
	/**
	 * @param c
	 *            A character
	 * @return The length of the character in LMBCS (i.e. 1, 2, or 3)
	 */
	public static int getPayload(final char c) {
		return ((lPayloadArray[c >>> 2] >>> ((c & 3) << 1)) & 3);
	}

	/*-----------------------------------------------------------------------*/
	/**
	 * @param cs
	 *            A sequence of characters (e.g. a String)
	 * @return The length of the sequence in LMBCS
	 */
	public static int getPayload(final CharSequence cs) {
		int lh = cs.length();
		int ret = 0;
		for (int i = 0; i < lh; i++) {
			char c = cs.charAt(i);
			ret += ((lPayloadArray[c >>> 2] >>> ((c & 3) << 1)) & 3);
			// That's perceptibly faster than ret += getPayload(cs.charAt(i))
		}
		return ret;
	}

	/*-----------------------------------------------------------------------*/
	/**
	 * @param i
	 *            E.g. a Vector or an array of Strings
	 * @return The sum of getPayload for those members which are CharSequences
	 */
	public static int getPayload(final Iterable<Object> i) {
		int ret = 0;
		for (Object o : i) {
			if (o instanceof CharSequence)
				ret += getPayload((CharSequence) o);
		}
		return ret;
	}

	/* ======================================================================= */

	/**
	 * this initializes the payload array. The array contains 16384 bytes = 65536 * 2 bits. As the LMBCS length is between 1..3 bytes we use
	 * only 2 bits for storage here.
	 * 
	 * In total we have stored all length iformation of every Basic Multilingual Unicode characters (\u00000 to \u0FFFF).
	 * 
	 * Supplementary Unicode Extended unicode characters ( >= \u10000) are internally represented by a surrogate pair. (\u0d800..\u0dFFF)
	 * These pairs needs 6 bytes in total. The same value that does the normal LMBCS-conversion uses. So we can walk through the string with
	 * charAt() and we do not need to use codePointAt() or similar
	 * 
	 */
	static {
		byte[] bb = lPayloadArray = new byte[16384];
		/*
		 * Generated Code
		 */
		bb[0] = (byte) 0xA9;
		bb[1] = (byte) 0xAA;
		bb[2] = (byte) 0x96;
		bb[3] = (byte) 0xA6;
		for (int i = 4; i <= 5; i++)
			bb[i] = (byte) 0xAA;
		bb[6] = (byte) 0xA6;
		bb[7] = (byte) 0xAA;
		for (int i = 8; i <= 31; i++)
			bb[i] = (byte) 0x55;
		for (int i = 32; i <= 39; i++)
			bb[i] = (byte) 0xAA;
		for (int i = 40; i <= 63; i++)
			bb[i] = (byte) 0x55;
		for (int i = 64; i <= 68; i++)
			bb[i] = (byte) 0xAA;
		bb[69] = (byte) 0xAF;
		for (int i = 70; i <= 74; i++)
			bb[i] = (byte) 0xAA;
		bb[75] = (byte) 0xAF;
		bb[76] = (byte) 0xA6;
		for (int i = 77; i <= 82; i++)
			bb[i] = (byte) 0xAA;
		bb[83] = (byte) 0xFA;
		for (int i = 84; i <= 92; i++)
			bb[i] = (byte) 0xAA;
		bb[93] = (byte) 0xFF;
		bb[94] = (byte) 0xAA;
		bb[95] = (byte) 0xEA;
		for (int i = 96; i <= 99; i++)
			bb[i] = (byte) 0xFF;
		bb[100] = (byte) 0xDF;
		for (int i = 101; i <= 174; i++)
			bb[i] = (byte) 0xFF;
		bb[175] = (byte) 0xFA;
		bb[176] = (byte) 0xFF;
		bb[177] = (byte) 0xAF;
		for (int i = 178; i <= 181; i++)
			bb[i] = (byte) 0xFF;
		bb[182] = (byte) 0xAA;
		bb[183] = (byte) 0xFA;
		for (int i = 184; i <= 224; i++)
			bb[i] = (byte) 0xFF;
		for (int i = 225; i <= 226; i++)
			bb[i] = (byte) 0xEA;
		bb[227] = (byte) 0xAE;
		for (int i = 228; i <= 231; i++)
			bb[i] = (byte) 0xAA;
		bb[232] = (byte) 0xBA;
		for (int i = 233; i <= 242; i++)
			bb[i] = (byte) 0xAA;
		bb[243] = (byte) 0xEA;
		for (int i = 244; i <= 255; i++)
			bb[i] = (byte) 0xFF;
		bb[256] = (byte) 0xAB;
		for (int i = 257; i <= 258; i++)
			bb[i] = (byte) 0xAA;
		bb[259] = (byte) 0xAE;
		for (int i = 260; i <= 275; i++)
			bb[i] = (byte) 0xAA;
		bb[276] = (byte) 0xAB;
		for (int i = 277; i <= 278; i++)
			bb[i] = (byte) 0xAA;
		bb[279] = (byte) 0xAE;
		for (int i = 280; i <= 291; i++)
			bb[i] = (byte) 0xFF;
		bb[292] = (byte) 0xFA;
		for (int i = 293; i <= 363; i++)
			bb[i] = (byte) 0xFF;
		for (int i = 364; i <= 365; i++)
			bb[i] = (byte) 0xAA;
		bb[366] = (byte) 0xBA;
		for (int i = 367; i <= 368; i++)
			bb[i] = (byte) 0xAA;
		for (int i = 369; i <= 371; i++)
			bb[i] = (byte) 0xFF;
		for (int i = 372; i <= 377; i++)
			bb[i] = (byte) 0xAA;
		bb[378] = (byte) 0xEA;
		bb[379] = (byte) 0xFF;
		bb[380] = (byte) 0xEA;
		for (int i = 381; i <= 386; i++)
			bb[i] = (byte) 0xFF;
		bb[387] = (byte) 0xFE;
		for (int i = 388; i <= 389; i++)
			bb[i] = (byte) 0xFF;
		for (int i = 390; i <= 391; i++)
			bb[i] = (byte) 0xBF;
		bb[392] = (byte) 0xAB;
		for (int i = 393; i <= 397; i++)
			bb[i] = (byte) 0xAA;
		bb[398] = (byte) 0xEA;
		bb[399] = (byte) 0xFF;
		for (int i = 400; i <= 403; i++)
			bb[i] = (byte) 0xAA;
		bb[404] = (byte) 0xEA;
		for (int i = 405; i <= 413; i++)
			bb[i] = (byte) 0xFF;
		bb[414] = (byte) 0xFB;
		bb[415] = (byte) 0xEF;
		bb[416] = (byte) 0xFF;
		bb[417] = (byte) 0xEF;
		bb[418] = (byte) 0xFE;
		bb[419] = (byte) 0xFF;
		bb[420] = (byte) 0xFB;
		bb[421] = (byte) 0xFF;
		bb[422] = (byte) 0xFE;
		for (int i = 423; i <= 425; i++)
			bb[i] = (byte) 0xFF;
		bb[426] = (byte) 0xFB;
		bb[427] = (byte) 0xBF;
		for (int i = 428; i <= 895; i++)
			bb[i] = (byte) 0xFF;
		bb[896] = (byte) 0xAB;
		for (int i = 897; i <= 909; i++)
			bb[i] = (byte) 0xAA;
		bb[910] = (byte) 0xEA;
		bb[911] = (byte) 0xBF;
		for (int i = 912; i <= 918; i++)
			bb[i] = (byte) 0xAA;
		for (int i = 919; i <= 2050; i++)
			bb[i] = (byte) 0xFF;
		bb[2051] = (byte) 0xAA;
		bb[2052] = (byte) 0xBF;
		bb[2053] = (byte) 0x7E;
		for (int i = 2054; i <= 2056; i++)
			bb[i] = (byte) 0xEA;
		bb[2057] = (byte) 0xEF;
		for (int i = 2058; i <= 2059; i++)
			bb[i] = (byte) 0xFF;
		bb[2060] = (byte) 0xFE;
		bb[2061] = (byte) 0xFF;
		bb[2062] = (byte) 0xEB;
		bb[2063] = (byte) 0xFE;
		for (int i = 2064; i <= 2078; i++)
			bb[i] = (byte) 0xFF;
		bb[2079] = (byte) 0xBF;
		for (int i = 2080; i <= 2088; i++)
			bb[i] = (byte) 0xFF;
		bb[2089] = (byte) 0xBE;
		bb[2090] = (byte) 0xEF;
		bb[2091] = (byte) 0xFE;
		for (int i = 2092; i <= 2115; i++)
			bb[i] = (byte) 0xFF;
		bb[2116] = (byte) 0xBB;
		bb[2117] = (byte) 0xEF;
		bb[2118] = (byte) 0xFF;
		bb[2119] = (byte) 0xFE;
		for (int i = 2120; i <= 2121; i++)
			bb[i] = (byte) 0xEF;
		for (int i = 2122; i <= 2124; i++)
			bb[i] = (byte) 0xFF;
		bb[2125] = (byte) 0xFB;
		for (int i = 2126; i <= 2133; i++)
			bb[i] = (byte) 0xFF;
		bb[2134] = (byte) 0xBF;
		bb[2135] = (byte) 0xEA;
		for (int i = 2136; i <= 2147; i++)
			bb[i] = (byte) 0xFF;
		bb[2148] = (byte) 0xAA;
		bb[2149] = (byte) 0xFA;
		for (int i = 2150; i <= 2153; i++)
			bb[i] = (byte) 0xFF;
		bb[2154] = (byte) 0xFE;
		for (int i = 2155; i <= 2163; i++)
			bb[i] = (byte) 0xFF;
		bb[2164] = (byte) 0xBA;
		bb[2165] = (byte) 0xFB;
		for (int i = 2166; i <= 2175; i++)
			bb[i] = (byte) 0xFF;
		for (int i = 2176; i <= 2178; i++)
			bb[i] = (byte) 0xFB;
		for (int i = 2179; i <= 2181; i++)
			bb[i] = (byte) 0xFF;
		bb[2182] = (byte) 0xFB;
		bb[2183] = (byte) 0xBF;
		for (int i = 2184; i <= 2192; i++)
			bb[i] = (byte) 0xFF;
		bb[2193] = (byte) 0xFB;
		bb[2194] = (byte) 0xFE;
		for (int i = 2195; i <= 2200; i++)
			bb[i] = (byte) 0xFF;
		bb[2201] = (byte) 0xFA;
		for (int i = 2202; i <= 2212; i++)
			bb[i] = (byte) 0xFF;
		bb[2213] = (byte) 0xBB;
		for (int i = 2214; i <= 2223; i++)
			bb[i] = (byte) 0xFF;
		bb[2224] = (byte) 0xFE;
		for (int i = 2225; i <= 2243; i++)
			bb[i] = (byte) 0xFF;
		bb[2244] = (byte) 0xFE;
		bb[2245] = (byte) 0xFF;
		bb[2246] = (byte) 0xFE;
		bb[2247] = (byte) 0xFF;
		bb[2248] = (byte) 0xFA;
		for (int i = 2249; i <= 2367; i++)
			bb[i] = (byte) 0xFF;
		bb[2368] = (byte) 0xDD;
		for (int i = 2369; i <= 2370; i++)
			bb[i] = (byte) 0xFF;
		for (int i = 2371; i <= 2375; i++)
			bb[i] = (byte) 0xFD;
		bb[2376] = (byte) 0xFF;
		bb[2377] = (byte) 0xFD;
		bb[2378] = (byte) 0xFF;
		bb[2379] = (byte) 0xFD;
		bb[2380] = (byte) 0xFF;
		bb[2381] = (byte) 0xFD;
		bb[2382] = (byte) 0xFF;
		bb[2383] = (byte) 0xFD;
		for (int i = 2384; i <= 2387; i++)
			bb[i] = (byte) 0xFF;
		bb[2388] = (byte) 0xA5;
		bb[2389] = (byte) 0x69;
		bb[2390] = (byte) 0x9A;
		bb[2391] = (byte) 0xA6;
		bb[2392] = (byte) 0x69;
		bb[2393] = (byte) 0x9A;
		bb[2394] = (byte) 0xA6;
		bb[2395] = (byte) 0xFD;
		for (int i = 2396; i <= 2399; i++)
			bb[i] = (byte) 0xFF;
		for (int i = 2400; i <= 2402; i++)
			bb[i] = (byte) 0xFD;
		bb[2403] = (byte) 0xFE;
		bb[2404] = (byte) 0x56;
		for (int i = 2405; i <= 2407; i++)
			bb[i] = (byte) 0xFF;
		bb[2408] = (byte) 0xFD;
		for (int i = 2409; i <= 2410; i++)
			bb[i] = (byte) 0xFF;
		bb[2411] = (byte) 0xFE;
		bb[2412] = (byte) 0xEF;
		bb[2413] = (byte) 0xFF;
		bb[2414] = (byte) 0xEF;
		bb[2415] = (byte) 0xFE;
		bb[2416] = (byte) 0xFF;
		bb[2417] = (byte) 0xFE;
		bb[2418] = (byte) 0xAF;
		for (int i = 2419; i <= 2421; i++)
			bb[i] = (byte) 0xFF;
		bb[2422] = (byte) 0xFA;
		for (int i = 2423; i <= 2445; i++)
			bb[i] = (byte) 0xFF;
		bb[2446] = (byte) 0xAF;
		bb[2447] = (byte) 0xFE;
		bb[2448] = (byte) 0xEE;
		for (int i = 2449; i <= 2455; i++)
			bb[i] = (byte) 0xFF;
		bb[2456] = (byte) 0xBE;
		bb[2457] = (byte) 0xEB;
		bb[2458] = (byte) 0xAF;
		for (int i = 2459; i <= 2499; i++)
			bb[i] = (byte) 0xFF;
		bb[2500] = (byte) 0xBF;
		for (int i = 2501; i <= 15919; i++)
			bb[i] = (byte) 0xFF;	// here are also the supplementary characters
		bb[15920] = (byte) 0xAB;
		bb[15921] = (byte) 0xAA;
		bb[15922] = (byte) 0xFE;
		for (int i = 15923; i <= 16063; i++)
			bb[i] = (byte) 0xFF;
		bb[16064] = (byte) 0xEB;
		for (int i = 16065; i <= 16383; i++)
			bb[i] = (byte) 0xFF;
		/*
		 * End of Generated Code
		 */
	}
	/*-----------------------------------------------------------------------*/
}

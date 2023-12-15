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
package org.openntf.domino.nsfdata.structs;

/**
 * This structure holds the format for character time/date strings. You set up this structure based on the time/date format you want to use.
 * Definitions for the various fields of this structure are found in TDFMT_xxx, TTFMT_xxx, TZFMT_xxx, and TSFMT_xxx. (misc.h)
 *
 */
public class TFMT extends AbstractStruct {
	public static enum TDFMT {
		FULL, CPARTIAL, PARTIAL, DPARTIAL, FULL4, CPARTIAL4, DPARTIAL4
	}

	public static enum TTFMT {
		FULL, PARTIAL, HOUR
	}

	public static enum TZFMT {
		NEVER, SOMETIMES, ALWAYS
	}

	public static enum TSFMT {
		DATE, TIME, DATETIME, CDATETIME
	}

	// TODO Figure out why this sometimes fails (Nifty 50 FORMROUT.NSF contains a value 123)
	public final Unsigned8 Date = new Unsigned8();
	//	public final Enum8<TDFMT> Date = new Enum8<TDFMT>(TDFMT.values());
	// TODO FORMROUT.NSF contains 64
	public final Unsigned8 Time = new Unsigned8();
	//	public final Enum8<TTFMT> Time = new Enum8<TTFMT>(TTFMT.values());
	// TODO FORMROUT.NSF contains 105
	public final Unsigned8 Zone = new Unsigned8();
	//	public final Enum8<TZFMT> Zone = new Enum8<TZFMT>(TZFMT.values());
	// TODO FORMROUT.NSF contains 102
	public final Unsigned8 Structure = new Unsigned8();
	//	public final Enum8<TSFMT> Structure = new Enum8<TSFMT>(TSFMT.values());
}

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
package org.openntf.domino.nsfdata.structs;

/**
 * This structure holds the format for character text number strings. You set up this structure based on the number format you want to use.
 * Definitions for the various fields of this structure are found in NFMT_xxx and NATTR_xxx. (misc.h)
 *
 */
public class NFMT extends AbstractStruct {
	public static enum NumFormat {
		GENERAL, FIXED, SCIENTIFIC, CURRENCY, BYTES
	}

	/**
	 * Number of decimal digits
	 */
	public final Unsigned8 Digits = new Unsigned8();
	/**
	 * Display Format
	 */
	// TODO Figure out why this doesn't work as an enum (Nifty 50 FORMROUT.NSF contains a value 58 here)
	//	public final Enum8<NumFormat> Format = new Enum8<NumFormat>(NumFormat.values());
	public final Unsigned8 Format = new Unsigned8();
	/**
	 * Display Attributes
	 */
	public final Unsigned8 Attributes = new Unsigned8();
	public final Unsigned8 Unused = new Unsigned8();

	public short getAttributes() {
		// TODO make enum
		return Attributes.get();
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ", Digits: " + Digits.get() + ", Format: " + Format.get() + ", Attributes: " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				+ getAttributes() + "]"; //$NON-NLS-1$
	}
}

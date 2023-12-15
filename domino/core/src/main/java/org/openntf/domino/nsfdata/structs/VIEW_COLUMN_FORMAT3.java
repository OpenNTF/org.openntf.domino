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

import java.text.MessageFormat;

/**
 * @since Lotus Notes/Domino 6.0
 *
 */
public class VIEW_COLUMN_FORMAT3 extends AbstractStruct {
	public final Unsigned16 Signature = new Unsigned16();
	// TODO make enum
	public final Unsigned8 DTPref = new Unsigned8();
	// TODO make enum
	public final Unsigned32 DTFlags = new Unsigned32();
	// TODO make enum
	public final Unsigned32 DTFlags2 = new Unsigned32();
	// TODO make enum
	public final Unsigned8 DTDOWFmt = new Unsigned8();
	// TODO make enum
	public final Unsigned8 DTYearFmt = new Unsigned8();
	// TODO make enum
	public final Unsigned8 DTMonthFmt = new Unsigned8();
	// TODO make enum
	public final Unsigned8 DTDayFmt = new Unsigned8();
	public final Unsigned8 DTDsep1Len = new Unsigned8();
	public final Unsigned8 DTDsep2Len = new Unsigned8();
	public final Unsigned8 DTDsep3Len = new Unsigned8();
	public final Unsigned8 DTTsepLen = new Unsigned8();
	// TODO make enum
	public final Unsigned8 DTDShow = new Unsigned8();
	// TODO make enum
	public final Unsigned8 DTDSpecial = new Unsigned8();
	// TODO make enum
	public final Unsigned8 DTTShow = new Unsigned8();
	// TODO make enum
	public final Unsigned8 DTTZone = new Unsigned8();
	// TODO make enum?
	public final Unsigned16 DatePreference = new Unsigned16();
	public final Unsigned8 bUnused = new Unsigned8();
	public final Unsigned32 Unused = new Unsigned32();

	static {
		addVariableAsciiString("DTDsep1", "DTDsep1Len"); //$NON-NLS-1$ //$NON-NLS-2$
		addVariableAsciiString("DTDsep2", "DTDsep2Len"); //$NON-NLS-1$ //$NON-NLS-2$
		addVariableAsciiString("DTDsep3", "DTDsep3Len"); //$NON-NLS-1$ //$NON-NLS-2$
		addVariableAsciiString("DTTsep", "DTTsepLen"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public String getDTDSep1() {
		return (String) getVariableElement("DTDsep1"); //$NON-NLS-1$
	}

	public String getDTDSep2() {
		return (String) getVariableElement("DTDsep2"); //$NON-NLS-1$
	}

	public String getDTDSep3() {
		return (String) getVariableElement("DTDsep3"); //$NON-NLS-1$
	}

	public String getDTTSep() {
		return (String) getVariableElement("DTTsep"); //$NON-NLS-1$
	}

	@Override
	public String toString() {
		return MessageFormat.format(
				"[{0}: Signature={1}, DTPref={2}, DTFlags={3}, DTFlags2={4}, DTDOWFmt={5}, DTYearFmt={6}, DTMonthFmt={7}, DTDayFmt={8}, DTDsep1={9}, DTDSep2={10}, DTDSep3={11}, DTTSep={12}, DTDShow={13}, DTDSpecial={14}, DTTShow={15}, DTTZone={16}, DatePreference={17}]", //$NON-NLS-1$
				getClass().getSimpleName(), Signature.get(), DTPref.get(), DTFlags.get(), DTFlags2.get(), DTDOWFmt.get(), DTYearFmt.get(), DTMonthFmt.get(), DTDayFmt.get(),
				getDTDSep1(), getDTDSep2(), getDTDSep3(), getDTTSep(), DTDShow.get(), DTDSpecial.get(), DTTShow.get(), DTTZone.get(), DatePreference.get());
	}
}

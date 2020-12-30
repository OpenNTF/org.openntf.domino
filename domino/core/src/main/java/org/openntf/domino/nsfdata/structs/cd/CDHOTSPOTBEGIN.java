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
package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This structure specifies the start of a "hot" region in a rich text field. Clicking on a hot region causes some other action to occur.
 * For instance, clicking on a popup will cause a block of text associated with that popup to be displayed. (editods.h)
 *
 */
@SuppressWarnings("nls")
public class CDHOTSPOTBEGIN extends CDRecord {
	/**
	 * These flags are used to define what type of hotspot is being defined by a CDHOTSPOTBEGIN data structure. (editods.h)
	 *
	 */
	public static enum HotspotType {

		/** The hotspot is a popup */
		POPUP,
		/**
		 * The hotspot is a button whose presentation is an arbitrary region of the rich text field. This region is bounded by the
		 * CDHOTSPOTBEGIN and CDHOTSPOTEND records.
		 */
		HOREGION,
		/** The hotspot is a button. */
		BUTTON,
		/** The hotspot is a file attachment. */
		FILE, UNUSED5, UNUSED6,
		/** The hotspot is a Notes Release 3 section field hotspot. */
		SECTION,
		/** Unused */
		ANY, UNUSED9, UNUSED10,
		/**
		 * The hotspot is a document, database, or view link hotspot. The presentation of this hotspot is an arbitrary region of the rich
		 * text field. This region is bounded by the CDHOTSPOTBEGIN and CDHOTSPOTEND records.
		 */
		HOTLINK,
		/** The hotspot is a standard collapsible section which is not access controlled. */
		BUNDLE,
		/** The hotspot is an access controlled collapsible section. */
		V4_SECTION,
		/** The hotspot is a subform. */
		SUBFORM,
		/** The hotspot is an active object. */
		ACTIVEOBJECT, UNUSED16, UNUSED17,
		/** The hotspot is an OLE rich text object. */
		OLERICHTEXT,
		/** The hotspot is an embedded view. */
		EMBEDDEDVIEW,
		/** The hotspot is an embedded folder pane. */
		EMBEDDEDPANE,
		/** The hotspot is an embedded navigator. */
		EMBEDDEDNAV,
		/** The hotspot is mouse over text popup. */
		MOUSEOVER, UNUSED23,
		/** The hotspot is a file upload placeholder. */
		FILEUPLOAD, UNUSED25, UNUSED26,
		/** The hotspot is an embedded outline. */
		EMBEDDEDOUTLINE,
		/** The hotspot is an embedded control window. */
		EMBEDDEDCTL, UNUSED29,
		/** The hotspot is an embedded calendar control (date picker). */
		EMBEDDEDCALENDARCTL,
		/** The hotspot is an embedded scheduling control. */
		EMBEDDEDSCHEDCTL,
		/** The hotspot is a resource link. */
		RCLINK, UNUSED33,
		/** The hotspot is an embedded editor control. */
		EMBEDDEDEDITCEL, UNUSED35,
		/** Embeddable buddy list. */
		CONTACTLISTCTL;
	}

	public final WSIG Header = inner(new WSIG());
	public final Enum16<HotspotType> Type = new Enum16<HotspotType>(HotspotType.values());
	// TODO make enum
	public final Unsigned32 Flags = new Unsigned32();
	public final Unsigned16 DataLength = new Unsigned16();

	static {
		addVariableData("Data", "DataLength");
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	public byte[] getHotspotData() {
		return (byte[]) getVariableElement("Data");
	}

	// TODO add accessors for data
	//			/*  if HOTSPOTREC_RUNFLAG_SIGNED, WORD SigLen then SigData follows. */
}

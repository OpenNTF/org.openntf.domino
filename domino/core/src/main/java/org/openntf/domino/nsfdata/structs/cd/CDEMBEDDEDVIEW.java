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

import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.NSFCompiledFormula;
import org.openntf.domino.nsfdata.structs.FONTID;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This CD Record describes a view as an embedded element. A CDEMBEDDEDVIEW record will be preceded by a CDPLACEHOLDER record. Further
 * description of the embedded view can be found in the CD record CDPLACEHOLDER. (editods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDEMBEDDEDVIEW extends CDRecord {
	/**
	 * (editods.h)
	 * 
	 * @since Lotus Notes/Domino 5.0
	 *
	 */
	public static enum Flag {
		/** If flag is set, embedded view background is transparent. */
		TRANSPARENT(0x0001),
		/** If flag is set, use Applet in Browser. */
		USEAPPLET_INBROWSER(0x0002),
		/** If flag is set, use Applet View Property. */
		USEAPPLET_VIEWPROP(0x0004),
		/** If flag is set, use WEB lines. */
		USE_WEBLINES(0x0008),
		/** If flag is set, line selection in the view follows the mouse movement. */
		SIMPLE_VIEW_MOUSE_TRACK_ON(0x0010),
		/** If flag is set, hide the view headers. */
		SIMPLE_VIEW_HEADER_OFF(0x0020),
		/** If flag is set, line in view shown with underline to make it look like a web link. */
		SIMPLE_VIEW_SHOW_AS_WEB_LINK(0x0040),
		/** If flag is set, show action bar for view. */
		SIMPLE_VIEW_SHOW_ACTION_BAR(0x0080),
		/** If flag is set, show selection margin in view. */
		SIMPLE_VIEW_SHOW_SELECTION_MARGIN(0x0100),
		/** Show current thread given a noteid. */
		SIMPLE_VIEW_SHOW_CURRENT_THREAD(0x0200),
		/** On web, use selection margin stuff set here. */
		NOT_USE_WEBVIEW_DEFAULT(0x0400),
		/** Show only summarized for calendar view. */
		SIMPLE_VIEW_SHOW_ONLY_SUMMARIZED(0x0800),
		/** Embedded view has name. */
		HASNAME(0x1000), SIMPLE_VIEW_SHOW_NEWMAIL_AT_TOP(0x2000),
		/** Use Dojo control in browser */
		USEJSCTL_INBROWSER(0x4000),
		/** Use custom JS in browser */
		USECUSTOMJS_INBROWSER(0x8000);

		private final int value_;

		private Flag(final int value) {
			value_ = value;
		}

		public int getValue() {
			return value_;
		}

		public static Set<Flag> valuesOf(final int flags) {
			Set<Flag> result = EnumSet.noneOf(Flag.class);
			for (Flag flag : values()) {
				if ((flag.getValue() & flags) > 0) {
					result.add(flag);
				}
			}
			return result;
		}
	}

	public final WSIG Header = inner(new WSIG());
	/**
	 * Use getFlags for access.
	 */
	@Deprecated
	public final Unsigned32 Flags = new Unsigned32();
	public final FONTID SpareFontID = inner(new FONTID());
	public final Unsigned16 RestrictFormulaLength = new Unsigned16();
	public final Unsigned16 WebLines = new Unsigned16();
	public final Unsigned16 NameLength = new Unsigned16();
	public final Unsigned16 wSpare = new Unsigned16();
	public final Unsigned32[] Spare = array(new Unsigned32[3]);

	static {
		addVariableData("RestrictFormula", "RestrictFormulaLength");
		addVariableString("Name", "NameLength");
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	public Set<Flag> getFlags() {
		return Flag.valuesOf((int) Flags.get());
	}

	public NSFCompiledFormula getRestrictFormula() {
		return new NSFCompiledFormula((byte[]) getVariableElement("RestrictFormula"));
	}

	public String getName() {
		return (String) getVariableElement("Name");
	}
}

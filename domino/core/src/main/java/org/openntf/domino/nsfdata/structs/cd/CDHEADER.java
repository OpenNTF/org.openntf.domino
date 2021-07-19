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
package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.FONTID;
import org.openntf.domino.nsfdata.structs.ODSUtils;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * Contains the header or footer used in a document. (editods.h)
 * 
 * @since forever
 *
 */
@SuppressWarnings("nls")
public class CDHEADER extends CDRecord {

	public static final int MAXFACESIZE = 32;

	public final WSIG Header = inner(new WSIG());
	public final Unsigned8 FontPitchAndFamily = new Unsigned8();
	/**
	 * @deprecated Use {@link #getFontName} for access
	 */
	@Deprecated
	public final Unsigned8[] FontName = array(new Unsigned8[MAXFACESIZE]);
	public final FONTID Font = inner(new FONTID());
	public final Unsigned16 HeadLength = new Unsigned16();

	static {
		addVariableString("Text", "HeadLength");
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	public String getFontName() {
		return ODSUtils.fromAscii(FontName);
	}

	public String getText() {
		return (String) getVariableElement("Text");
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": FontPitchAndFamily=" + FontPitchAndFamily.get() + ", FontName=" + getFontName()
				+ ", Font=" + Font + ", HeadLength=" + HeadLength.get() + ", Text=" + getText() + "]";
	}
}

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
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This structure defines the start of a run of text in a rich-text field. (editods.h)
 *
 */
@SuppressWarnings("nls")
public class CDTEXT extends CDRecord {

	public final WSIG Header = inner(new WSIG());
	public final FONTID FontID = inner(new FONTID());

	static {
		addVariableString("Text", "getTextLength");
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	public String getText() {
		return (String) getVariableElement("Text");
	}

	public void setText(final String text) {
		int resultSize = setVariableElement("Text", text);
		Header.Length.set(Header.size() + ((int) (resultSize + getStructSize())));
	}

	public int getTextLength() {
		return (int) (Header.getRecordLength() - Header.size() - FontID.size());
	}

	@Override
	public int getExtraLength() {
		// Text is always an even number of bytes in LMBCS, even when Length doesn't indicate this
		return getTextLength() % 2;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ", Font ID: " + FontID + ", Text: " + getText() + "]";
	}
}

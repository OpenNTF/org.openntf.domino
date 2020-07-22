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
package org.openntf.domino.graph2.builtin.search;

import java.util.Set;

import org.openntf.domino.NoteCollection;
import org.openntf.domino.Session;
import org.openntf.domino.helpers.DocumentScanner;
import org.openntf.domino.types.CaseInsensitiveString;

public class IndexScanner extends DocumentScanner {

	public IndexScanner() {
		setCaseSensitive(false);
		setTrackValueLocation(true);
		setTrackTokenLocation(true);
		setTrackNameLocation(true);
		setTrackRichTextLocation(true);
	}

	public IndexScanner(final Set<CharSequence> stopTokenList) {
		super(stopTokenList);
		setCaseSensitive(false);
		setTrackValueLocation(true);
		setTrackTokenLocation(true);
		setTrackNameLocation(true);
		setTrackRichTextLocation(true);
	}

	@Override
	protected void processToken(final CharSequence token, final CharSequence itemName, final String address) {
		super.processToken(token, itemName, address);
	}

	@Override
	protected void processName(final CharSequence name, final CharSequence itemName, final Session session, final String address) {
		super.processName(name, itemName, session, address);
	}

	@Override
	public void processTextValue(final CaseInsensitiveString name, final Object value, final String address) {
		super.processTextValue(name, value, address);
	}

	@Override
	public void processValue(final CaseInsensitiveString name, final Object value, final String address) {
		super.processValue(name, value, address);
	}

	@Override
	public void processNoteCollection(final NoteCollection collection) {
		super.processNoteCollection(collection);
	}

}

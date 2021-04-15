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
/**
 * 
 */
package org.openntf.domino.tests.ntf;

/**
 * @author nfreeman
 * 
 */
public class WebinarSamples {

	public WebinarSamples() {
	}

	public void processViewOld(final lotus.domino.View view) {
		lotus.domino.ViewEntryCollection collection = null;
		lotus.domino.ViewEntry currentEntry = null;
		lotus.domino.ViewEntry nextEntry = null;
		try {
			collection = view.getAllEntries();
			currentEntry = collection.getFirstEntry();
			while (currentEntry != null) {
				nextEntry = collection.getNextEntry(currentEntry);
				try {
					currentEntry.getNoteID(); // Do whatever it is you actually want to get done
				} catch (lotus.domino.NotesException ne1) {
					ne1.printStackTrace();
				} finally {
					currentEntry.recycle();
				}
				currentEntry = nextEntry;
			}
		} catch (lotus.domino.NotesException ne) {
			ne.printStackTrace();
		} finally {
			try {
				if (collection != null)
					collection.recycle();
			} catch (lotus.domino.NotesException ne) {

			}
		}
	}

	public void processViewNew(final org.openntf.domino.View view) {
		org.openntf.domino.ViewEntryCollection collection = view.getAllEntries();
		for (org.openntf.domino.ViewEntry entry : collection) {
			entry.getNoteID(); // Do whatever it is you actually want to get done

		}
	}

}

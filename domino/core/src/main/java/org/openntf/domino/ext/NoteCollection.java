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
/**
 *
 */
package org.openntf.domino.ext;

import java.util.Set;

import org.openntf.domino.NoteCollection.SelectOption;

/**
 * OpenNTF extensions to NoteCollection class
 *
 * @author withersp
 *
 */
public interface NoteCollection {

	/**
	 * Checks whether a collection is the same object as another. There is not currently any specific implementation overriding standard
	 * Java functionality. This will not return true if two different NoteCollections contain the same notes, just if they are the same Java
	 * object.
	 *
	 * @param otherCollection
	 *            Object other collection to compare with
	 * @return true boolean if successful
	 * @since org.openntf.domino 2.5.0
	 */
	@Override
	public boolean equals(final Object otherCollection);

	/**
	 * Loads a set of SelectOption enums to be used for creating the collection. This is the preferred method over using the specific
	 * setters, to ensure easy access to all valid options.
	 *
	 * @param options
	 *            Set<SelectOption> enum values corresponding to desired note types
	 * @since org.openntf.domino 2.5.0
	 */
	public void setSelectOptions(final Set<SelectOption> options);

	/**
	 * Setter to allow easy setting of the last modified date since when to retrieve notes
	 *
	 * @param since
	 *            Date since when notes should have been modified to load into collection
	 * @since org.openntf.domino 3.0.0
	 */
	public void setSinceTime(final java.util.Date since);
}

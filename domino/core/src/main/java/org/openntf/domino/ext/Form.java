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
package org.openntf.domino.ext;

import org.openntf.domino.NoteCollection;

/**
 * OpenNTF extensions to Form class
 *
 * @author withersp
 *
 */
public interface Form {

	/**
	 * Gets the number of documents that use this Form and have been modified since a given Java date
	 *
	 * @param since
	 *            Date the method should compare against
	 * @return int number of documents using this Form modified
	 * @since org.openntf.domino 3.0.0
	 */
	public int getModifiedNoteCount(final java.util.Date since);

	/**
	 * Gets the XPage that this Form is set to launch on the web as ($XPageAlt field)
	 *
	 * @return String XPage to Form is designed to launch with on web
	 * @since org.openntf.domino 4.5.0
	 */
	public String getXPageAlt();

	/**
	 * Gets the XPage that this Form is set to launch in the client as ($XPageAltClient field, if defined, else $XPageAlt)
	 *
	 * @return String XPage to Form is designed to launch with on client
	 * @since org.openntf.domino 5.0.0
	 */
	public String getXPageAltClient();

	/**
	 * Generates a selection formula in format:
	 *
	 * <code>SELECT Form = "myFormName" | Form = "myFormAlias" | Form = "myFormSecondAlias" etc.</code>
	 *
	 * <p>
	 * Continues for as many aliases as the Form has, not including aliases if none is defined.
	 * </p>
	 *
	 * <p>
	 * NOTE: Forms view in an NSF only displays the first alias of a design element, deisng element's properties box will shows all.
	 * </p>
	 *
	 * @return String selection formula to access all Documents created with that Form
	 * @since org.openntf.domino 3.0.0
	 */
	public String getSelectionFormula();

	/**
	 * Returns documents associated with this form
	 *
	 * @return Documents as a <code>NotesCollection</code>
	 */
	public NoteCollection getNoteCollection();

	/**
	 * Returns a Metaversal ID consisting of a replica ID of the parent database and a universal ID of this form.
	 */
	public String getMetaversalID();

}

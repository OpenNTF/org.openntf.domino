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

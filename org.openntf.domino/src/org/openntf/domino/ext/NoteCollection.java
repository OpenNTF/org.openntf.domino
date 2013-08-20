/**
 * 
 */
package org.openntf.domino.ext;

import java.util.Set;

import org.openntf.domino.NoteCollection.SelectOption;

/**
 * @author withersp
 * 
 */
public interface NoteCollection {

	/**
	 * Equals.
	 * 
	 * @param otherCollection
	 *            the other collection
	 * @return true, if successful
	 */
	public boolean equals(final Object otherCollection);

	/**
	 * 
	 * @param options
	 *            a Set of SelectOption enum values corresponding to desired note types
	 */
	public void setSelectOptions(final Set<SelectOption> options);

	public void setSinceTime(final java.util.Date since);
}

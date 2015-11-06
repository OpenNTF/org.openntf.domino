/**
 * 
 */
package org.openntf.domino.ext;

import org.openntf.domino.NoteCollection;

/**
 * @author withersp
 * 
 *         OpenNTF extensions to Form class
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

	public NoteCollection getNoteCollection();

	public String getMetaversalID();

}

/**
 * 
 */
package org.openntf.domino.ext;

/**
 * @author withersp
 * 
 */
public interface Form {

	public int getModifiedNoteCount(final java.util.Date since);

	public String getXPageAlt();

	public String getSelectionFormula();

}

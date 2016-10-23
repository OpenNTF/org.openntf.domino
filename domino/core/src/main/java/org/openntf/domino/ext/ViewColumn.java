/**
 * 
 */
package org.openntf.domino.ext;

/**
 * @author withersp
 * 
 *         OpenNTF extensions to ViewColumn class
 */
public interface ViewColumn {
	/**
	 * Returns the column's true index. Unlike the core Domino method ({@link lotus.domino.ViewColumn#getColumnValuesIndex()}) this does not
	 * ignore columns with constant values
	 * 
	 * @return actual column index
	 * @since org.openntf.domino 5.0.0
	 */
	public int getIndex();

	/**
	 * Returns the columnValueIndex of this column if you want to read the value from an entry.
	 * 
	 * @param correctValue
	 *            if false, the uncorrected value of the delegete is returned, i.e. columns containing constants are ignored.<br/>
	 *            If true, getIndex is called (This is needed for VIEWENTRY_RETURN_CONSTANT_VALUES)
	 * @return the column index, corrected as required by the developer
	 * @since org.openntf.domino 5.0.0
	 */
	public int getColumnValuesIndex(boolean correctValue);
}

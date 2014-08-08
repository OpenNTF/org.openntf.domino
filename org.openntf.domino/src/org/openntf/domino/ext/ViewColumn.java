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
	public int getIndex();

	/**
	 * Returns the columnValueIndex of this column if you want to read the value from an entry.
	 * 
	 * @param correctValue
	 *            if false, the uncorrected value of the delegete is returned. If true, getIndex is returned (This is needed for
	 *            VIEWENTRY_RETURN_CONSTANT_VALUES)
	 * @return
	 */
	public int getColumnValuesIndex(boolean correctValue);
}

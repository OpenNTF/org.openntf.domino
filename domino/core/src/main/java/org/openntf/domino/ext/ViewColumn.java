/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
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

/**
 * OpenNTF extensions to ViewColumn class
 *
 * @author withersp
 *
 */
public interface ViewColumn {
	/**
	 * Returns the column's true index. Unlike the core Domino method ({@link lotus.domino.ViewColumn#getColumnValuesIndex()}) this does not
	 * ignore columns with constant values.
	 * <p>
	 * Indices start with 0 for the leftmost column and increase by one for each column. The core Domino method returns value 65535 for
	 * columns with constant values (like literal strings and numbers) and skips them. This method returns correct index even for these
	 * columns.
	 * </p>
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

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

import java.util.Date;
import java.util.List;

/**
 * OpenNTF extensions to DateRange class
 *
 * @author jgallagher
 *
 *
 */
public interface DateRange {

	/**
	 * Checks whether a DateRange contains a specific date/time
	 *
	 * @param dt
	 *            DateTime to check within the range
	 * @return true when the DateTime is within the range
	 * @since org.openntf.domino 2.5.0
	 */
	public boolean contains(final org.openntf.domino.DateTime dt);

	/**
	 * Checks whether a DateRange contains a specific Java date/time
	 *
	 * @param dt
	 *            Date to check within the range
	 * @return true when the Java Date is within the range
	 * @since org.openntf.domino 2.5.0
	 */
	public boolean contains(final Date date);

	/**
	 * Creates a copy of this object.
	 *
	 * @return DateRange with the same StartDateTime and EndDateTime.
	 */
	public org.openntf.domino.DateRange clone();

	/**
	 * Returns a list of <code>java.util.Date</code> instances from {@link org.openntf.domino.DateRange#getStartDateTime() StartDateTime} to
	 * {@link org.openntf.domino.DateRange#getEndDateTime() EndDateTime}. The <code>StartDateTime</code> and <code>EndDateTime</code> are
	 * included in the list.
	 *
	 * @return List of all days between <code>StartDateTime</code> and <code>EndDateTime</code> (including)
	 */
	public List<Date> getDays();
}

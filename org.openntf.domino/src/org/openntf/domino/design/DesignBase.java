/*
 * Copyright 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package org.openntf.domino.design;

import java.io.Serializable;

import org.openntf.domino.Database;

/**
 * @author jgallagher
 * 
 */
public interface DesignBase extends org.openntf.domino.types.Design, org.openntf.domino.types.DatabaseDescendant, Serializable {

	/**
	 * @return the DXL of the design element, as a String
	 */
	public String getDxlString();

	/**
	 * @return whether hidden from web
	 */
	public boolean isHideFromWeb();

	/**
	 * @return whether hidden from notes
	 */
	public boolean isHideFromNotes();

	/**
	 * @return whether refresh flag is set
	 */
	public boolean isNeedsRefresh();

	/**
	 * @return whether prohibit design refresh is set
	 */
	public boolean isPreventChanges();

	/**
	 * @return whether the design element propagates its prevent-changes settings
	 */
	public boolean isPropagatePreventChanges();

	/**
	 * Reattach the design element to a new Database, either due to deserialization or to copy to a new DB
	 * 
	 * @param database
	 *            the Database object to attach the design element to
	 */
	public void reattach(Database database);

	public void setHideFromWeb(boolean hideFromWeb);

	public void setHideFromNotes(boolean hideFromNotes);

	public void setNeedsRefresh(boolean needsRefresh);

	public void setPreventChanges(boolean preventChanges);

	public void setPropagatePreventChanges(boolean propagatePreventChanges);

	/**
	 * Save any changes to the design element (may change the Note ID)
	 */
	public void save();

}
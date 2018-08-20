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

import org.openntf.domino.design.DatabaseDesign.DbProperties;

/**
 * @author jgallagher
 * @author Paul Withers
 *
 */
public interface IconNote extends DesignBase {
	public static enum DASMode {
		NONE, VIEWS, VIEWSANDDOCUMENTS;
	}

	/**
	 * Sets the Domino Data Access Service mode for the database.
	 *
	 * @param mode
	 */
	void setDASMode(final DASMode mode);

	/**
	 * @return The Domino Data Access Service mode for the database.
	 */
	DASMode getDASMode();

	/**
	 * @return whether or not $AllowPost8HTML {@link DbProperties} is set
	 *
	 * @since ODA 4.1.0
	 */
	boolean isEnhancedHTML();

	/**
	 * Sets whether or not $AllowPost8HTML {@link DbProperties} is used
	 *
	 * @param whether
	 *            to set or unset
	 * @since ODA 4.1.0
	 */
	void setEnhancedHTML(boolean enabled);

	/**
	 * @return whether or not $DisallowOpenInNBP {@link DbProperties} is set
	 *
	 * @since 4.1.0
	 */
	boolean isBlockICAA();

	/**
	 * Sets whether or not $DisallowOpenInNBP {@link DbProperties} is used
	 *
	 * @param enabled
	 *            whether to set or unset
	 * @since 4.1.0
	 */
	void setBlockICAA(boolean enabled);

	/**
	 * @return whether or not $DisableExport {@link DbProperties} is set
	 *
	 * @since 4.1.0
	 */
	boolean isDisableViewExport();

	/**
	 * Sets whether or not $DisableExport {@link DbProperties} is used
	 *
	 * @param enabled
	 *            whether to set or unset
	 * @since 4.1.0
	 */
	void setDisableViewExport(boolean enabled);

	/**
	 * @return whether or not $LaunchXPageRunOnServer {@link DbProperties} is set
	 *
	 * @since 4.1.0
	 */
	boolean isLaunchXPageRunOnServer();

	/**
	 * Sets whether or not $LaunchXPageRunOnServer {@link DbProperties} is used
	 *
	 * @param enabled
	 *            whether to set or unset
	 * @since 4.1.0
	 */
	void setLaunchXPageRunOnServer(boolean enabled);

	/**
	 * @return whether or not $LargeSummary {@link DbProperties} is set
	 *
	 * @since 4.1.0
	 */
	boolean isDocumentSummary16MB();

	/**
	 * @return whether or not $Daos {@link DbProperties} is set
	 *
	 * @since 4.1.0
	 */
	boolean isDaosEnabled();

	/**
	 * @return whether or not DAS is enabled
	 *
	 * @since 4.1.0
	 */
	boolean isAllowDas();

	/**
	 * Number of days CSS files should expire in or Integer.MIN_VALUE if not set
	 *
	 * @return number of days
	 * @since ODA 4.3.0
	 */
	public int getCssExpiry();

	/**
	 * Sets the number of days CSS files should expire in.
	 *
	 * @param days
	 *            to expire after. Use Integer.MIN_VALUE to clear it
	 * @since 4.3.0
	 */
	public void setCssExpiry(int days);

	/**
	 * Number of days File resources should expire in or Integer.MIN_VALUE if not set
	 *
	 * @return number of days
	 * @since ODA 4.3.0
	 */
	public int getFileExpiry();

	/**
	 * Sets the number of days files should expire in.
	 *
	 * @param days
	 *            to expire after. Use Integer.MIN_VALUE to clear it
	 * @since 4.3.0
	 */
	public void setFileExpiry(int days);

	/**
	 * Number of days Image files should expire in or Integer.MIN_VALUE if not set
	 *
	 * @return number of days
	 * @since ODA 4.3.0
	 */
	public int getImageExpiry();

	/**
	 * Sets the number of days image files should expire in.
	 *
	 * @param days
	 *            to expire after. Use Integer.MIN_VALUE to clear it
	 * @since 4.3.0
	 */
	public void setImageExpiry(int days);

	/**
	 * Number of days JavaScript files should expire in or Integer.MIN_VALUE if not set
	 *
	 * @return number of days
	 * @since ODA 4.3.0
	 */
	public int getJsExpiry();

	/**
	 * Sets the number of days JavaScript files should expire in.
	 *
	 * @param days
	 *            to expire after. Use Integer.MIN_VALUE to clear it
	 * @since 4.3.0
	 */
	public void setJsExpiry(int days);

	String[] getXotsClassNames();
}

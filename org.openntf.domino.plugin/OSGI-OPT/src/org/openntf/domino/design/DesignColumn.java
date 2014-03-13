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

/**
 * @author jgallagher
 * 
 */
public interface DesignColumn {
	public static enum SortOrder {
		NONE, ASCENDING, DESCENDING;
	}

	public boolean isResizable();

	public void setResizable(final boolean resizable);

	public boolean isSeparateMultipleValues();

	public void setSeparateMultipleValues(final boolean separateMultipleValues);

	public boolean isSortNoAccent();

	public void setSortNoAccent(final boolean sortNoAccent);

	public boolean isSortNoCase();

	public void setSortNoCase(final boolean sortNoCase);

	public boolean isShowAsLinks();

	public void setShowAsLinks(final boolean showAsLinks);

	public String getItemName();

	public void setItemName(final String itemName);

	public SortOrder getSortOrder();

	public void setSortOrder(final SortOrder sortOrder);

	public String getTitle();

	public void setTitle(final String title);

	public String getFormula();

	public void setFormula(final String formula);
}

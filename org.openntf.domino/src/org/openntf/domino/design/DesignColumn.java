/**
 * 
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

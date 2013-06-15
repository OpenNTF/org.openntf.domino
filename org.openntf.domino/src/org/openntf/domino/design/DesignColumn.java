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

	public void setResizable(boolean resizable);

	public boolean isSeparateMultipleValues();

	public void setSeparateMultipleValues(boolean separateMultipleValues);

	public boolean isSortNoAccent();

	public void setSortNoAccent(boolean sortNoAccent);

	public boolean isSortNoCase();

	public void setSortNoCase(boolean sortNoCase);

	public boolean isShowAsLinks();

	public void setShowAsLinks(boolean showAsLinks);

	public String getItemName();

	public void setItemName(String itemName);

	public SortOrder getSortOrder();

	public void setSortOrder(SortOrder sortOrder);

	public String getTitle();

	public void setTitle(String title);

	public String getFormula();

	public void setFormula(String formula);
}

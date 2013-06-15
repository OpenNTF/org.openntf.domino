/**
 * 
 */
package org.openntf.domino.design;

/**
 * @author jgallagher
 * 
 */
public interface DesignView extends Folder {
	public String getSelectionFormula();

	public void setSelectionFormula(String selectionFormula);
}

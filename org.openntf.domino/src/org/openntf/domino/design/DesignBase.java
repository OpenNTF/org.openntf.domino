/**
 * 
 */
package org.openntf.domino.design;

import java.util.List;

/**
 * @author jgallagher
 * 
 */
public interface DesignBase extends org.openntf.domino.types.Design, org.openntf.domino.types.DatabaseDescendant {

	/**
	 * @return file resource name
	 */
	public String getName();

	/**
	 * @param name
	 *            The new name for the design element; any |-delimited values will be appended to the aliases
	 */
	public void setName(String name);

	/**
	 * @return a List of the design element's aliases
	 */
	public List<String> getAliases();

	/**
	 * 
	 * @param alias
	 *            The new alias(es) for the design element, |-delimited
	 */
	public void setAlias(String alias);

	/**
	 * 
	 * @param aliases
	 *            The new aliases for the design element; any |-delimited values will be exploded into the final list
	 */
	public void setAliases(Iterable<String> aliases);

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
	 * Save any changes to the design element (may change the Note ID)
	 */
	public void save();

}

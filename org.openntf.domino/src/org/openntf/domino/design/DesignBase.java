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
	 * @return a List of the design element's aliases
	 */
	public List<String> getAliases();

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

}

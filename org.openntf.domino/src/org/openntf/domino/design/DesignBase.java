/**
 * 
 */
package org.openntf.domino.design;

/**
 * @author jgallagher
 * 
 */
public interface DesignBase extends org.openntf.domino.types.Design, org.openntf.domino.types.DatabaseDescendant {

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

	public void setHideFromWeb(final boolean hideFromWeb);

	public void setHideFromNotes(final boolean hideFromNotes);

	public void setNeedsRefresh(final boolean needsRefresh);

	public void setPreventChanges(final boolean preventChanges);

	public void setPropagatePreventChanges(final boolean propagatePreventChanges);

	/**
	 * Save any changes to the design element (may change the Note ID)
	 */
	public void save();

}
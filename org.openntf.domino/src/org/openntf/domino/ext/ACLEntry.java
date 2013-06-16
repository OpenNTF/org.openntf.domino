/**
 * 
 */
package org.openntf.domino.ext;

import org.openntf.domino.ACL;

/**
 * @author withersp
 * 
 */
public interface ACLEntry {

	/**
	 * Set ACLEntry Level to an ACL.Level. Options are:
	 * <ol>
	 * <li>NOACCESS(ACL.LEVEL_NOACCESS)</li>
	 * <li>DESPOSITOR(ACL.LEVEL_DEPOSITOR)</li>
	 * <li>READER(ACL.LEVEL_READER)</li>
	 * <li>AUTHOR(ACL.LEVEL_AUTHOR)</li>
	 * <li>EDITOR(ACL.LEVEL_EDITOR)</li>
	 * <li>DESIGNER(ACL.LEVEL_DESIGNER)</li>
	 * <li>MANAGER(ACL.LEVEL_MANAGER)</li>
	 * </ol>
	 * 
	 * @param level
	 *            ACL.Level to set to
	 */
	public void setLevel(final ACL.Level level);

}

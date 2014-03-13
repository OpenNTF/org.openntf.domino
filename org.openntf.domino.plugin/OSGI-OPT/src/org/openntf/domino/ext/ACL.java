/**
 * 
 */
package org.openntf.domino.ext;

import org.openntf.domino.ACL.Level;
import org.openntf.domino.ACLEntry;

/**
 * @author withersp
 * 
 */
public interface ACL {

	/**
	 * Creates an entry in the ACL with the name and level that you specify.
	 * 
	 * @param name
	 *            The name of the person, group, or server for whom you want to create an entry in the ACL. You must supply the complete
	 *            name, but hierarchical names can be in abbreviated format. Case is not significant.
	 * @param level
	 *            The level that you want to assign to this person, group, or server in the ACL, of type ACL.Level.
	 * 
	 * @return The newly-created {@link org.openntf.domino.ACLEntry}.
	 * @since openntf.domino 1.0.0
	 * 
	 */
	public ACLEntry createACLEntry(final String name, final Level level);
}

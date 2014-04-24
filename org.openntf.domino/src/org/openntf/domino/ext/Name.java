/**
 * 
 */
package org.openntf.domino.ext;

/**
 * @author withersp
 * 
 */
public interface Name {

	/**
	 * Gets groups for the person / group / server etc the Name object pertains to.<br/>
	 * The groups include the hierarchical name for the Name object, all Group entries that Name is a member of, and any OUs and O the name
	 * relates to.<br/>
	 * <br/>
	 * Sample output:<br/>
	 * CN=admin/O=Intec-PW,admin,*,*\/O=Intec-PW,LocalDomainAdmins,Domino Developers,SEAS TestRole - Y1
	 * 
	 * @param serverName
	 *            String server name to check against
	 * @return Collection<String> of any Domino Directory Person or Group the Name is found in, plus generic hierarchical responses
	 */
	public java.util.Collection<String> getGroups(String serverName);

}

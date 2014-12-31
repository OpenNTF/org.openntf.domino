/**
 * 
 */
package org.openntf.domino.ext;

import org.openntf.domino.Name.NameFormat;

/**
 * @author withersp
 * 
 *         OpenNTF extensions to Name object
 */
public interface Name {

	/**
	 * Gets groups for the person / group / server etc the Name object pertains to.<br/>
	 * The groups include the hierarchical name for the Name object, all Group entries that Name is a member of, and any OUs and O the name
	 * relates to.
	 * 
	 * <p>
	 * Sample output: CN=admin/O=Intec-PW,admin,*,*\/O=Intec-PW,LocalDomainAdmins,Domino Developers,SEAS TestRole - Y1
	 * </p>
	 * 
	 * @param serverName
	 *            String server name to check against
	 * @return Collection<String> of any Domino Directory Person or Group the Name is found in, plus generic hierarchical responses
	 * @since org.opentf.domino 5.0.0
	 */
	public java.util.Collection<String> getGroups(String serverName);

	public org.openntf.domino.Name clone();

	public String getIDprefix();

	public void parse(final String name, final String lang);

	public void parse(final String name);

	/**
	 * Returns the Name format of this name
	 * 
	 * @return
	 */
	public NameFormat getNameFormat();
}

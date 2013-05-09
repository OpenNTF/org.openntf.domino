/**
 * 
 */
package org.openntf.domino.design;

/**
 * @author jgallagher
 * 
 */
public interface DatabaseDesign {

	public AboutDocument getAboutDocument();

	public ACL getACL();

	public Form getDefaultForm();

	public View getDefaultView();

	/**
	 * @param name
	 *            name of a file resource
	 * @return a file resource
	 */
	public FileResource getFileResource(String name);

	/**
	 * @return collection of all file resources
	 */
	public DesignCollection<FileResource> getFileResources();

	/**
	 * @param name
	 *            name of a hidden file resource
	 * @return a hidden file resource
	 */
	public FileResource getHiddenFileResource(String name);

	public DesignCollection<FileResource> getHiddenFileResources();

	public Form getForm(String name);

	public DesignCollection<Form> getForms();

	/**
	 * @return the icon note of the database
	 */
	public IconNote getIconNote();

	public ReplicationFormula getReplicationFormula();

	public UsingDocument getUsingDocument();

	public View getView(String name);

	public DesignCollection<View> getViews();
}

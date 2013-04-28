/**
 * 
 */
package org.openntf.domino.design;

/**
 * @author jgallagher
 * 
 */
public interface DatabaseDesign {
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
}

/**
 * 
 */
package org.openntf.domino.design;

/**
 * @author jgallagher
 * 
 */
public interface DatabaseDesign {

	public FileResource createFileResource();

	public DesignView createView();

	public AboutDocument getAboutDocument();

	public ACLNote getACL();

	public DesignForm getDefaultForm();

	public DesignView getDefaultView();

	/**
	 * @param name
	 *            name of a file resource
	 * @return a file resource
	 */
	public FileResource getFileResource(final String name);

	/**
	 * @return collection of all file resources
	 */
	public DesignCollection<FileResource> getFileResources();

	/**
	 * @param name
	 *            name of a hidden file resource
	 * @return a hidden file resource
	 */
	public FileResource getHiddenFileResource(final String name);

	public DesignCollection<FileResource> getHiddenFileResources();

	/**
	 * @param name
	 *            name of any type of file resource (file, Java, etc.)
	 * @return a named file resource
	 */
	public FileResource getAnyFileResource(final String name);

	public ImageResource getImageResource(String name);

	public DesignCollection<ImageResource> getImageResources();

	public StyleSheet getStyleSheet(String name);

	public DesignCollection<StyleSheet> getStyleSheets();

	public JavaResource getJavaResource(String name);

	public DesignCollection<JavaResource> getJavaResources();

	public XPage getXPage(String name);

	public DesignCollection<XPage> getXPages();

	public JarResource getJarResource(String name);

	public DesignCollection<JarResource> getJarResources();

	public ClassLoader getDatabaseClassLoader(ClassLoader parent);

	public ClassLoader getDatabaseClassLoader(ClassLoader parent, boolean includeJars);

	public DesignForm getForm(final String name);

	public DesignCollection<DesignForm> getForms();

	/**
	 * @return the icon note of the database
	 */
	public IconNote getIconNote();

	public ReplicationFormula getReplicationFormula();

	public UsingDocument getUsingDocument();

	public DesignView getView(final String name);

	public DesignCollection<DesignView> getViews();
}

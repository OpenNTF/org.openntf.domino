/*
 * Copyright 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package org.openntf.domino.design;

import java.util.SortedSet;

/**
 * @author jgallagher
 * 
 */
public interface DatabaseDesign extends org.openntf.domino.types.DatabaseDescendant {

	/**
	 * @return a new, nameless, empty file-resource object
	 */
	public FileResource createFileResource();

	/**
	 * @return a new, empty folder with no columns
	 */
	public Folder createFolder();

	/**
	 * @return a new, empty style sheet
	 */
	public StyleSheet createStyleSheet();

	/**
	 * @return a new view with no columns or selection formula
	 */
	public DesignView createView();

	/**
	 * @param create
	 *            whether a new using document should be created when one does not yet exist in the database
	 * @return the About Document note of the database, or null if no document exists and create is false
	 */
	public AboutDocument getAboutDocument(boolean create);

	/**
	 * @return the ACL note of the database
	 */
	public ACLNote getACL();

	/**
	 * @return the form marked as default for the database, as a DesignForm object
	 */
	public DesignForm getDefaultForm();

	/**
	 * @return the view marked as default for the database, as a DesignForm object
	 */
	public DesignView getDefaultView();

	/**
	 * @return an object representing the faces-config.xml file of the database
	 */
	public FacesConfig getFacesConfig();

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
	public FileResourceHidden getHiddenFileResource(String name);

	/**
	 * @return a collection of all "hidden" file resources (e.g. Eclipse/JSF artifacts and Java class files)
	 */
	public DesignCollection<FileResourceHidden> getHiddenFileResources();

	/**
	 * @param name
	 *            name of any type of file resource (file, Java, etc.)
	 * @return the named file resource
	 */
	public AnyFileResource getAnyFileResource(String name);

	/**
	 * @param name
	 *            name or alias of a folder in the database
	 * @return the named folder, as a Folder object
	 */
	public Folder getFolder(String name);

	/**
	 * @return a collection of all folders in the database, as Folder objects
	 */
	public DesignCollection<Folder> getFolders();

	/**
	 * @param name
	 *            name of an image resource
	 * @return the named image resource
	 */
	public ImageResource getImageResource(String name);

	/**
	 * @return a collection of all image resources in the database
	 */
	public DesignCollection<ImageResource> getImageResources();

	/**
	 * @param name
	 *            name of a style sheet resource
	 * @return the named style sheet resource
	 */
	public StyleSheet getStyleSheet(String name);

	/**
	 * @return a collection of all style sheet resources in the database
	 */
	public DesignCollection<StyleSheet> getStyleSheets();

	/**
	 * @param name
	 *            name of a Java resource in "path" format (e.g. "some/package/name/SomeClassName.java")
	 * @return the named Java resource
	 */
	public XspJavaResource getXspJavaResource(String name);

	/**
	 * @return a collection of all Java resources in the database
	 */
	public DesignCollection<XspJavaResource> getXspJavaResources();

	/**
	 * @return a SortedSet of the Java class names defined in Java resources in the database in "canonical" format (e.g.
	 *         "some.package.name.SomeClassName")
	 */
	public SortedSet<String> getJavaResourceClassNames();

	/**
	 * @param name
	 *            name of an XPage with the ".xsp" extension (e.g. "SomePage.xsp")
	 * @return the named XPage note
	 */
	public XPage getXPage(String name);

	/**
	 * @return a collection of all XPages in the databases
	 */
	public DesignCollection<XPage> getXPages();

	/**
	 * @param name
	 *            name of a JAR resource with the ".jar" extension (e.g. "some_library.jar")
	 * @return the named JAR resource
	 */
	public JarResource getJarResource(String name);

	/**
	 * @return a collection of all JAR resources in the database
	 */
	public DesignCollection<JarResource> getJarResources();

	/**
	 * @param parent
	 *            a ClassLoader used to delegate lookups for classes not found in the database
	 * @return a ClassLoader that looks up classes in all Java resources, XPages, and JAR resources in the database
	 */
	public DatabaseClassLoader getDatabaseClassLoader(ClassLoader parent);

	/**
	 * @param parent
	 *            a ClassLoader used to delegate lookups for classes not found in the database
	 * @param includeJars
	 *            whether or not to include JAR files in the lookup
	 * @return a ClassLoader that looks up classes in all Java resources, XPages, and, if requested, JAR resources in the database
	 */
	public DatabaseClassLoader getDatabaseClassLoader(ClassLoader parent, boolean includeJars);

	/**
	 * @param parent
	 *            a ClassLoader used to delegate lookups for classes not found in the database
	 * @param includeJars
	 *            whether or not to include JAR files in the lookup
	 * @return a ClassLoader that looks up classes in all Java resources, XPages, and, if requested, JAR resources in the database and Java
	 *         script libraries
	 */
	public DatabaseClassLoader getDatabaseClassLoader(ClassLoader parent, boolean includeJars, boolean includeLibraries);

	/**
	 * @param name
	 *            name or alias of a form in the database
	 * @return the named form, as a DesignForm object
	 */
	public DesignForm getForm(String name);

	/**
	 * @return a collection of all forms in the database, as DesignForm objects
	 */
	public DesignCollection<DesignForm> getForms();

	/**
	 * @return the icon note of the database
	 */
	public IconNote getIconNote();

	/**
	 * @return the replication-formula note for the database
	 */
	public ReplicationFormula getReplicationFormula();

	/**
	 * @param create
	 *            whether a new using document should be created when one does not yet exist in the database
	 * @return the Using Document note of the database, or null if no document exists and create is false
	 */
	public UsingDocument getUsingDocument(boolean create);

	/**
	 * @param name
	 *            name or alias of a view in the database
	 * @return the named view, as a DesignView object
	 */
	public DesignView getView(String name);

	/**
	 * @return a collection of all views in the database, as DesignView objects
	 */
	public DesignCollection<DesignView> getViews();

	/**
	 * @return a collection of all Java script libraries in the database
	 */
	public DesignCollection<ScriptLibraryJava> getScriptLibrariesJava();

	/**
	 * @param name
	 *            name of a Java script library in the database
	 * @return the named Java script library
	 */
	public ScriptLibraryJava getScriptLibraryJava(String name);

	/**
	 * Gets an XSP-Property from WEB-INF/xsp.properties
	 * 
	 * @param propertyName
	 * @return the value of that property
	 */
	String[] getXspProperty(String propertyName);

	/**
	 * Checks whether or not the API is enabled for the current database
	 * 
	 * @return boolean whether or not enabled
	 * @since org.openntf.domino 5.0.0
	 */
	public boolean isAPIEnabled();

	/**
	 * common code to test if a flag is set in the xsp.properties file for the "org.openntf.domino.xsp" value.
	 * 
	 * @param flagName
	 *            use upperCase for flagName, e.g. RAID
	 * @return true if the flag is set
	 * @since org.openntf.domino 5.0.0
	 */
	public boolean isAppFlagSet(final String flagName);

	/**
	 * Returns all design elements matching to the according formula. The type of the design element is autot detected
	 * 
	 * @param formula
	 *            the formula
	 * @return a subclass of DesignBase
	 */
	DesignCollection<DesignBase> getDesignElements(String formula);

	public <T extends DesignBase> DesignCollection<T> getDesignElements(Class<T> type);

	public <T extends DesignBase> DesignCollection<T> getDesignElements(final Class<T> type, final String name);

	public <T extends DesignBase> DesignCollection<T> getDesignElementsByName(Class<T> type, String name);

	public <T extends DesignBase> T getDesignElementByName(Class<T> type, String name);

}

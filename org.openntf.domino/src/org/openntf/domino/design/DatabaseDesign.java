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
public interface DatabaseDesign {

	/**
	 * @return a new, nameless, empty file-resource object
	 */
	public FileResource createFileResource();

	/**
	 * @return a new, empty folder with no columns
	 */
	public Folder createFolder();

	/**
	 * @return a new view with no columns or selection formula
	 */
	public DesignView createView();

	/**
	 * @return the About Document note of the database
	 */
	public AboutDocument getAboutDocument();

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

	/**
	 * @return a collection of all "hidden" file resources (e.g. Eclipse/JSF artifacts and Java class files)
	 */
	public DesignCollection<FileResource> getHiddenFileResources();

	/**
	 * @param name
	 *            name of any type of file resource (file, Java, etc.)
	 * @return the named file resource
	 */
	public FileResource getAnyFileResource(final String name);

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
	public JavaResource getJavaResource(String name);

	/**
	 * @return a collection of all Java resources in the database
	 */
	public DesignCollection<JavaResource> getJavaResources();

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
	public ClassLoader getDatabaseClassLoader(ClassLoader parent);

	/**
	 * @param parent
	 *            a ClassLoader used to delegate lookups for classes not found in the database
	 * @param includeJars
	 *            whether or not to include JAR files in the lookup
	 * @return a ClassLoader that looks up classes in all Java resources, XPages, and, if requested, JAR resources in the database
	 */
	public ClassLoader getDatabaseClassLoader(ClassLoader parent, boolean includeJars);

	/**
	 * @param name
	 *            name or alias of a form in the database
	 * @return the named form, as a DesignForm object
	 */
	public DesignForm getForm(final String name);

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
	 * @return the Using Document note of the database
	 */
	public UsingDocument getUsingDocument();

	/**
	 * @param name
	 *            name of alias of a view in the database
	 * @return the named view, as a DesignView object
	 */
	public DesignView getView(final String name);

	/**
	 * @return a collection of all views in the database, as DesignView objects
	 */
	public DesignCollection<DesignView> getViews();
}

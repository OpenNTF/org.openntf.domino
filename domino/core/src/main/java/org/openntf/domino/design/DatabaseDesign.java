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

import java.util.List;
import java.util.SortedSet;

import org.openntf.domino.utils.xml.XMLDocument;

/**
 * @author jgallagher
 * @author Paul Withers
 *
 */
public interface DatabaseDesign extends org.openntf.domino.types.DatabaseDescendant {

	/**
	 * Setting for how unread marks are replicated
	 *
	 * @author Paul Withers
	 * @since ODA 4.1.0
	 *
	 */
	public enum UnreadReplicationSetting {
		NEVER(""), CLUSTER("cluster"), ALL_SERVERS("all");

		String propName;

		private UnreadReplicationSetting(final String propName) {
			this.propName = propName;
		}

		public String getPropertyName() {
			return propName;
		}
	}

	/**
	 * DAS setting for the NSF
	 *
	 * @author Paul Withers
	 * @since ODA 4.1.0
	 *
	 */
	public enum DASEnabledFor {
		NOTHING("0"), VIEWS("1"), VIEWS_AND_DOCS("2");

		String value;

		private DASEnabledFor(final String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	/**
	 * Enum for database properties set on Database Properties box
	 *
	 * @author Paul Withers
	 * @since ODA 4.1.0
	 */
	public enum DbProperties {
		USE_JS("usejavascriptinpages"), REQUIRE_SSL("requiressl"), NO_URL_OPEN("nourlopen"), ENHANCED_HTML("$AllowPost8HTML"),
		BLOCK_ICAA("$DisallowOpenInNBP"), DISABLE_BACKGROUND_AGENTS("allowbackgroundagents"), ALLOW_STORED_FORMS("allowstoredforms"),
		DEFER_IMAGE_LOADING("imageloadsdeferred"), ALLOW_DOC_LOCKING("allowdocumentlocking"), INHERIT_OS_THEME("inheritostheme"),
		ALLOW_DESIGN_LOCKING("allowdesignlocking"), SHOW_IN_OPEN_DIALOG("showinopendialog"), MULTI_DB_INDEXING("multidbindexed"),
		MODIFIED_NOT_UNREAD("markmodifiedunread"), MARK_PARENT_REPLY_FORWARD("trackreplyforward"), INHERIT_FROM_TEMPLATE("fromtemplate"),
		DB_IS_TEMPLATE("templatename"), ADVANCED_TEMPLATE("advancedtemplate"), MULTILINGUAL("multilingual"),
		DONT_MAINTAIN_UNREAD("maintainunread"), REPLICATE_UNREAD("replicateunread"), OPTIMIZE_DOC_MAP("optimizetablebitmap"),
		DONT_OVERWRITE_FREE_SPACE("overwritefreespace"), MAINTAIN_LAST_ACCESSED("savelastaccessed"),
		DISABLE_TRANSACTION_LOGGING("logtransactions"), NO_SPECIAL_RESPONSE_HIERARCHY("allowspecialhierarchy"), USE_LZ1("uselz1"),
		NO_HEADLINE_MONITORING("allowheadlinemonitoring"), ALLOW_MORE_FIELDS("increasemaxfields"),
		SUPPORT_RESPONSE_THREADS("supportrespthread"), NO_SIMPLE_SEARCH("nosimplesearch"), COMPRESS_DESIGN("compressdesign"),
		COMPRESS_DATA("compressdata"), NO_AUTO_VIEW_UPDATE("noautoviewupdate"), NO_EXPORT_VIEW("$DisableExport"),
		ALLOW_SOFT_DELETE("allowsoftdeletion"), SOFT_DELETE_EXPIRY("softdeletionsexpirein"), MAX_UPDATED_BY("maxupdatedbyentries"),
		MAX_REVISIONS("maxrevisionentries"), ALLOW_DAS("$AllowRESTDbAPI");

		String propName;

		private DbProperties(final String propName) {
			this.propName = propName;
		}

		public String getPropertyName() {
			return propName;
		}

	}

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
	 * Gets an agent by name
	 *
	 * @param name
	 *            name of the agent
	 * @return AgentData object
	 */
	public DesignAgent getAgent(String name);

	/**
	 * @return a collection of all agents in the database, as AgentData objects
	 */
	public DesignCollection<DesignAgent> getAgents();

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
	 *            name of a style sheet resource
	 * @return the named subform
	 * @since ODA 4.1.0
	 */
	public Subform getSubform(String name);

	/**
	 * @return a collection of all subforms in the database
	 * @since ODA 4.1.0
	 */
	public DesignCollection<Subform> getSubforms();

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

	/**
	 * Gets database XML, for which we need a minimum of two design notes in exported DXL
	 *
	 * @return
	 */
	public XMLDocument getDatabaseXml();

	/**
	 * Retrieves the ODS version of the NSF. Options are:
	 * <ul>
	 * <li>52 = Domino 9.0.1</li>
	 * <li>51 = Domino 8.5</li>
	 * <li>48 = Domino 8.0</li>
	 * <li>43 = Domino 6.x / 7.0</li>
	 * <li>41 = Domino 5</li>
	 * <li>20 = Domino 4.x</li>
	 * </ul>
	 *
	 * @return ODS Version
	 * @since ODA 4.1.0
	 */
	public String getOdsVersion();

	/**
	 * Retrieves database properties from DXL, including only those set. Picks up settings on Database Basics and Advanced tabs of database
	 * properties box
	 *
	 * @return List of DbProperties enums, mapping to database properties.
	 * @since ODA 4.1.0
	 */
	public List<DbProperties> getDatabaseProperties();

	/**
	 * Name of the template this database inherits from
	 *
	 * @return name of template it inherits from
	 * @since ODA 4.1.0
	 */
	public String getTemplateName();

	/**
	 * Template name defined for this database
	 *
	 * @return template name defined for this database
	 * @since ODA 4.1.0
	 */
	public String getNameIfTemplate();

	/**
	 * Returns what the DAS setting is for the database
	 *
	 * @return DASEnabledFor enum option
	 * @since ODA 4.1.0
	 */
	public DASEnabledFor getDasSetting();

	/**
	 * Returns what the replicate unread setting is for the database
	 *
	 * @return UnreadReplicationSetting enum option
	 * @since ODA 4.1.0
	 */
	public UnreadReplicationSetting getReplicateUnreadSetting();

	/**
	 * Gets the maximum $UpdatedBy elements set for the database
	 *
	 * @return 0 if not set or more if set
	 * @since ODA 4.1.0
	 */
	public int getMaxUpdatedBy();

	/**
	 * Sets the maximum $UpdatedBy elements set for the database
	 *
	 * @param newMax
	 *            0 to clear the value or a positive number of entries to set
	 */
	public void setMaxUpdatedBy(int newMax);

	/**
	 * Gets the maximum $Revisions elements set for the database
	 *
	 * @return 0 if not set or more if set
	 * @since ODA 4.1.0
	 */
	public int getMaxRevisions();

	/**
	 * Sets the maximum $Revisions elements set for the database
	 *
	 * @param newMax
	 *            0 to clear the value or a positive number of entries to set
	 */
	public void setMaxRevisions(int newMax);

	/**
	 * Gets the number of hours soft deletions are set to expire in
	 *
	 * @return Integer.MIN_VALUE if soft deletions is not enabled, 48 if it's not set, otherwise the value
	 * @since ODA 4.1.0
	 */
	public int getSoftDeletionsExpireIn();

	/**
	 * Update the database DXL with changes made
	 *
	 * @return success or failure
	 * @since ODA 4.1.0
	 */
	public boolean save();

}

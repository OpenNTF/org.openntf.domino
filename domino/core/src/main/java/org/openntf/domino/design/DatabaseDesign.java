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
import java.util.Map;
import java.util.SortedSet;

import org.openntf.domino.design.IconNote.DASMode;
import org.openntf.domino.utils.xml.XMLDocument;

/**
 * @author jgallagher
 * @author Paul Withers
 *
 */
public interface DatabaseDesign extends org.openntf.domino.types.DatabaseDescendant {

	/**
	 * Enum for LaunchContext for Notes
	 *
	 * @author Paul Withers
	 * @since ODA 4.1.0
	 */
	public enum LaunchContextNotes {
		LAST_VIEWED(""), ABOUT_DOC("openaboutdocument"), NAVIGATOR("opennavigator"), NAVIGATOR_IN_WINDOW("opennavigatorinwindow"),
		FRAMESET("openframeset"), XPAGE("openxpage"), ABOUT_ATTACHMENT("openfirstaboutattachment"), ABOUT_DOCLINK("openfirstdoclink"),
		COMPOSITE_APP("opencompapp");

		String propName;

		private LaunchContextNotes(final String propName) {
			this.propName = propName;
		}

		public String getPropertyName() {
			return propName;
		}
	}

	/**
	 * Enum for LaunchContext for Web
	 *
	 * @author Paul Withers
	 * @since ODA 4.1.0
	 */
	public enum LaunchContextWeb {
		NOTES_LAUNCH(""), ABOUT_DOC("openaboutdocument"), NAVIGATOR("opennavigator"), NAVIGATOR_IN_WINDOW("opennavigatorinwindow"),
		FRAMESET("openframeset"), PAGE("openpage"), XPAGE("openxpage"), ABOUT_DOCLINK("openfirstdoclink"),
		SPECIFIC_DOC_LINK("openspecifieddoclink"), FIRST_DOC_IN_VIEW("openfirstdocumentinview");

		String propName;

		private LaunchContextWeb(final String propName) {
			this.propName = propName;
		}

		public String getPropertyName() {
			return propName;
		}
	}

	/**
	 * Enum for context for showing the about document
	 *
	 * @author Paul Withers
	 * @since ODA 4.1.0
	 */
	public enum ShowAboutContext {
		FIRST_OPENED(""), FIRST_OPENED_AND_CHANGED("firstopenandchanged"), NEVER("never");

		String propName;

		private ShowAboutContext(final String propName) {
			this.propName = propName;
		}

		public String getPropertyName() {
			return propName;
		}
	}

	/**
	 * Enum for default locations for preview pane
	 *
	 * @author Paul Withers
	 * @since ODA 4.1.0
	 */
	public enum PreviewPaneDefault {
		BOTTOM(""), BOTTOM_RIGHT("bottomright"), RIGHT("right");

		String propName;

		private PreviewPaneDefault(final String propName) {
			this.propName = propName;
		}

		public String getPropertyName() {
			return propName;
		}
	}

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
		MAX_REVISIONS("maxrevisionentries"), ALLOW_DAS("$AllowRESTDbAPI"), DAOS_ENABLED("$Daos"),
		LAUNCH_XPAGE_ON_SERVER("$LaunchXPageRunOnServer"), DOCUMENT_SUMMARY_16MB("$LargeSummary");

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
	 * @return a new Form with no fields
	 * @since ODA 4.3.0
	 */
	public DesignForm createForm();

	/**
	 * @return a new Subform with no fields
	 * @since ODA 4.3.0
	 */
	public Subform createSubform();

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
	 * @return the icon note of the database. The icon is cached, so all settings will be as they were at the start of the request (when the
	 *         Session was initiated)
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
	 * Sets database properties via DXL, setting as checked if true, not checked if false. Properties can be found on Database Basics and
	 * Advanced tabs. It's worth being aware of what the default values are.
	 *
	 * @param props
	 *            DbProperties option to set and whether to switch on or off
	 * @since ODA 4.1.0
	 */
	public void setDatabaseProperties(Map<DbProperties, Boolean> props);

	/**
	 * Name of the template this database inherits from
	 *
	 * @return name of template it inherits from
	 * @since ODA 4.1.0
	 */
	public String getTemplateName();

	/**
	 * Sets the template it inherits from
	 *
	 * @param name
	 *            of template
	 * @since ODA 4.1.0
	 */
	public void setTemplateName(String name);

	/**
	 * Template name defined for this database
	 *
	 * @return template name defined for this database
	 * @since ODA 4.1.0
	 */
	public String getNameIfTemplate();

	/**
	 * Sets template name for this database
	 *
	 * @param name
	 *            template name
	 * @since 4.1.0
	 */
	public void setNameIfTemplate(String name);

	/**
	 * Returns DAS setting (stored in icon note)
	 *
	 * @return DASMode enum option
	 * @since ODA 4.1.0
	 */
	public DASMode getDasMode();

	/**
	 * Sets the Domino Data Access Service mode for the database.
	 *
	 * @param mode
	 * @since ODA 4.3.0
	 */
	public void setDasMode(DASMode mode);

	/**
	 * Returns what the replicate unread setting is for the database
	 *
	 * @return {@link UnreadReplicationSetting} enum option
	 * @since ODA 4.1.0
	 */
	public UnreadReplicationSetting getReplicateUnreadSetting();

	/**
	 * Sets what the replicate unread setting should be for the database
	 *
	 * @param setting
	 *            {@link UnreadReplicationSetting} enum option
	 * @since 4.1.0
	 */
	public void setReplicateUnreadSetting(UnreadReplicationSetting setting);

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
	 * @since ODA 4.1.0
	 */
	public void setMaxRevisions(int newMax);

	/**
	 * What Notes should launch as (e.g. XPage, Frameset etc)
	 *
	 * @return LaunchContext
	 * @since ODA 4.1.0
	 */
	public LaunchContextNotes getNotesLaunchContext();

	/**
	 * Gets the design element that is to be launched
	 *
	 * @return design element name or blank if About document / attachment / doclink or restore as last viewed. For composite applications,
	 *         the value will be CompositeAppXmlFile##CompositeAppPage
	 * @since ODA 4.1.0
	 */
	public String getNotesLaunchDesignElement();

	/**
	 * When the about document should be shown in Notes Client
	 *
	 * @return when first opened, when first opened or changed, or never
	 * @since ODA 4.1.0
	 */
	public ShowAboutContext getShowAboutContext();

	/**
	 * Where the default setting for the preview pane is
	 *
	 * @return bottom, bottom-right, or right
	 * @since ODA 4.1.0
	 */
	public PreviewPaneDefault getDefaultPreviewPaneLocation();

	/**
	 * What Web should launch as (e.g. XPage, Page etc)
	 *
	 * @return LaunchContext
	 * @since ODA 4.1.0
	 */
	public LaunchContextWeb getWebLaunchContext();

	/**
	 * Gets the design element that is to be launched
	 *
	 * @return design element name or blank if restore as last viewed
	 * @since ODA 4.1.0
	 */
	public String getWebLaunchDesignElement();

	/**
	 * Number of days CSS files should expire in or Integer.MIN_VALUE if not set
	 *
	 * @return number of days
	 * @since ODA 4.1.0
	 */
	public int getCssExpiry();

	/**
	 * Sets the number of days CSS files should expire in.
	 *
	 * @param days
	 *            to expire after. Use Integer.MIN_VALUE to clear it
	 * @since 4.3.0
	 */
	public void setCssExpiry(int days);

	/**
	 * Number of days File resources should expire in or Integer.MIN_VALUE if not set
	 *
	 * @return number of days
	 * @since ODA 4.1.0
	 */
	public int getFileExpiry();

	/**
	 * Sets the number of days files should expire in.
	 *
	 * @param days
	 *            to expire after. Use Integer.MIN_VALUE to clear it
	 * @since 4.3.0
	 */
	public void setFileExpiry(int days);

	/**
	 * Number of days Image files should expire in or Integer.MIN_VALUE if not set
	 *
	 * @return number of days
	 * @since ODA 4.1.0
	 */
	public int getImageExpiry();

	/**
	 * Sets the number of days image files should expire in.
	 *
	 * @param days
	 *            to expire after. Use Integer.MIN_VALUE to clear it
	 * @since 4.3.0
	 */
	public void setImageExpiry(int days);

	/**
	 * Number of days JavaScript files should expire in or Integer.MIN_VALUE if not set
	 *
	 * @return number of days
	 * @since ODA 4.1.0
	 */
	public int getJsExpiry();

	/**
	 * Sets the number of days JavaScript files should expire in.
	 *
	 * @param days
	 *            to expire after. Use Integer.MIN_VALUE to clear it
	 * @since 4.3.0
	 */
	public void setJsExpiry(int days);

	/**
	 * Gets the number of hours soft deletions are set to expire in
	 *
	 * @return Integer.MIN_VALUE if soft deletions is not enabled, 48 if it's not set, otherwise the value
	 * @since ODA 4.1.0
	 */
	public int getSoftDeletionsExpireIn();

	/**
	 * Sets the number of hours soft deletions should expire in
	 *
	 * @param hours
	 *            to expire in or Integer.MIN_VALUE to disable soft deletions and reset to 48
	 * @since 4.3.0
	 */
	public void setSoftDeletionsExpireIn(int hours);

	/**
	 * Update the database DXL with changes made
	 *
	 * @return success or failure
	 * @since ODA 4.1.0
	 */
	public boolean save();

}

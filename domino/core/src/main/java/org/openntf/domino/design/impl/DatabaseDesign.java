/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.design.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;

import lotus.domino.NotesException;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.DxlExporter;
import org.openntf.domino.DxlImporter;
import org.openntf.domino.NoteCollection;
import org.openntf.domino.NoteCollection.SelectOption;
import org.openntf.domino.Session;
import org.openntf.domino.design.AnyFileResource;
import org.openntf.domino.design.DesignAgent;
import org.openntf.domino.design.DesignBase;
import org.openntf.domino.design.DesignBaseNamed;
import org.openntf.domino.design.DesignForm;
import org.openntf.domino.design.DesignView;
import org.openntf.domino.design.FileResource;
import org.openntf.domino.design.FileResourceHidden;
import org.openntf.domino.design.FileResourceWebContent;
import org.openntf.domino.design.IconNote.DASMode;
import org.openntf.domino.design.Subform;
import org.openntf.domino.design.XspJavaResource;
import org.openntf.domino.design.XspResource;
import org.openntf.domino.exceptions.OpenNTFNotesException;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Strings;
import org.openntf.domino.utils.xml.XMLDocument;
import org.openntf.domino.utils.xml.XMLNode;
import org.xml.sax.SAXException;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.StreamUtil;

/**
 * @author jgallagher
 * @author Paul Withers
 *
 */
@SuppressWarnings("nls")
public class DatabaseDesign implements org.openntf.domino.design.DatabaseDesign {

	/*
	 * Some handy constant Note IDs for getting specific elements. h/t http://www.nsftools.com/tips/NotesTips.htm#defaultelements
	 */
	public static final String ABOUT_NOTE = "FFFF0002"; //$NON-NLS-1$
	public static final String DEFAULT_FORM = "FFFF0004"; //$NON-NLS-1$
	public static final String DEFAULT_VIEW = "FFFF0008"; //$NON-NLS-1$
	public static final String ICON_NOTE = "FFFF0010"; //$NON-NLS-1$

	// Design collections are pretty impossible to work with using the Java API: the exported DXL is
	// blank and the raw $Collection data isn't readable as an Item
	public static final String DESIGN_COLLECTION = "FFFF0020"; //$NON-NLS-1$

	public static final String ACL_NOTE = "FFFF0040"; //$NON-NLS-1$
	public static final String USING_NOTE = "FFFF0100"; //$NON-NLS-1$
	public static final String REPLICATION_FORMULA = "FFFF0800"; //$NON-NLS-1$

	private transient Properties props;

	private final Database database_;
	private XMLDocument databaseXml;
	private IconNote iconNote_;
	private boolean isIconDirty_;

	public DatabaseDesign(final Database database) {
		database_ = database;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.DatabaseDesign#createFolder()
	 */
	@Override
	public Folder createFolder() {
		return new Folder(database_);
	}

	// TODO Decide whether this should check for an existing view first. Current behavior is
	// acting like it's creating a whole-cloth new view but then replacing any existing with
	// the same name on save
	@Override
	public DesignView createView() {
		return new org.openntf.domino.design.impl.DesignView(database_);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.DatabaseDesign#createForm()
	 */
	@Override
	public DesignForm createForm() {
		return new org.openntf.domino.design.impl.DesignForm(database_);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.DatabaseDesign#createForm()
	 */
	@Override
	public Subform createSubform() {
		return new org.openntf.domino.design.impl.Subform(database_);
	}

	@Override
	public FileResource createFileResource() {
		return new org.openntf.domino.design.impl.FileResource(database_);
	}

	@Override
	public StyleSheet createStyleSheet() {
		return new StyleSheet(database_);
	}

	@Override
	public AboutDocument getAboutDocument(final boolean create) {
		Document doc = database_.getDocumentByID(ABOUT_NOTE);
		if (doc != null) {
			return new AboutDocument(doc);
		} else if (create) {
			return new AboutDocument(getAncestorDatabase());
			//			try {
			//				InputStream is = AboutDocument.class.getResourceAsStream("/org/openntf/domino/design/impl/dxl_helpaboutdocument.xml");
			//				String dxl = StreamUtil.readString(is);
			//				is.close();
			//				DxlImporter importer = getAncestorSession().createDxlImporter();
			//				importer.setDesignImportOption(DxlImporter.DesignImportOption.REPLACE_ELSE_CREATE);
			//				importer.setReplicaRequiredForReplaceOrUpdate(false);
			//				importer.importDxl(dxl, database_);
			//				doc = database_.getDocumentByID(ABOUT_NOTE);
			//				return new AboutDocument(doc);
			//			} catch (IOException e) {
			//				DominoUtils.handleException(e);
			//			}
		}
		return null;
	}

	@Override
	public ACLNote getACL() {
		return new ACLNote(database_.getDocumentByID(ACL_NOTE));
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.DatabaseDesign#getAgent(java.lang.String)
	 */
	@Override
	public DesignAgent getAgent(final String name) {
		return getDesignElementByName(DesignAgent.class, name);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.DatabaseDesign#getAgents()
	 */
	@Override
	public DesignCollection<DesignAgent> getAgents() {
		return getDesignElements(org.openntf.domino.design.DesignAgent.class);
	}

	@Override
	public DesignForm getDefaultForm() {
		Document formDoc = database_.getDocumentByID(DEFAULT_FORM);
		if (formDoc != null) {
			return new org.openntf.domino.design.impl.DesignForm(formDoc);
		}
		return null;
	}

	@Override
	public DesignView getDefaultView() {
		Document viewDoc = database_.getDocumentByID(DEFAULT_VIEW);
		if (viewDoc != null) {
			return new org.openntf.domino.design.impl.DesignView(viewDoc);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.DatabaseDesign#getFacesConfig()
	 */
	@Override
	public FacesConfig getFacesConfig() {
		return new org.openntf.domino.design.impl.FacesConfig(this);
	}

	@Override
	public FileResource getFileResource(final String name) {
		return getDesignElementByName(FileResource.class, name);
		//		if (DominoUtils.isUnid(name)) {
		//			Document doc = database_.getDocumentByUNID(name);
		//			return new org.openntf.domino.design.impl.FileResource(doc);
		//		} else {
		//			NoteCollection notes = getNoteCollection(
		//					String.format(" !@Contains($Flags; '~') & @Contains($Flags; 'g') & @Explode($TITLE; '|')=\"%s\" ",
		//							DominoUtils.escapeForFormulaString(name)), EnumSet.of(SelectOption.MISC_FORMAT));
		//
		//			String noteId = notes.getFirstNoteID();
		//			if (!noteId.isEmpty()) {
		//				Document doc = database_.getDocumentByID(noteId);
		//				return new FileResource(doc);
		//			}
		//		}
		//		return null;
	}

	@Override
	public DesignCollection<org.openntf.domino.design.FileResource> getFileResources() {
		return getDesignElements(org.openntf.domino.design.FileResource.class);
		//NoteCollection notes = getNoteCollection(" !@Contains($Flags; '~') & @Contains($Flags; 'g') ", EnumSet.of(SelectOption.MISC_FORMAT));
		//return new DesignCollection<org.openntf.domino.design.FileResource>(notes, FileResource.class);
	}

	@Override
	public FileResourceHidden getHiddenFileResource(final String name) {
		return getDesignElementByName(FileResourceHidden.class, name);
	}

	@Override
	public DesignCollection<FileResourceHidden> getHiddenFileResources() {
		return getDesignElements(org.openntf.domino.design.FileResourceHidden.class);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.DatabaseDesign#getImageResource(java.lang.String)
	 */
	@Override
	public ImageResource getImageResource(final String name) {
		return getDesignElementByName(ImageResource.class, name);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.DatabaseDesign#getImageResources()
	 */
	@Override
	public org.openntf.domino.design.DesignCollection<org.openntf.domino.design.ImageResource> getImageResources() {
		return getDesignElements(org.openntf.domino.design.ImageResource.class);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.DatabaseDesign#getStyleSheet(java.lang.String)
	 */
	@Override
	public StyleSheet getStyleSheet(final String name) {
		return getDesignElementByName(StyleSheet.class, name);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.DatabaseDesign#getStyleSheets()
	 */
	@Override
	public org.openntf.domino.design.DesignCollection<org.openntf.domino.design.StyleSheet> getStyleSheets() {
		return getDesignElements(org.openntf.domino.design.StyleSheet.class);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.DatabaseDesign#getSubform(java.lang.String)
	 */
	@Override
	public Subform getSubform(final String name) {
		return getDesignElementByName(Subform.class, name);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.DatabaseDesign#getSubforms()
	 */
	@Override
	public org.openntf.domino.design.DesignCollection<Subform> getSubforms() {
		return getDesignElements(org.openntf.domino.design.Subform.class);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.design.DatabaseDesign#getJavaResource(java.lang.String)
	 */
	@Override
	public XspJavaResource getXspJavaResource(final String name) {
		return getDesignElementByName(XspJavaResource.class, name);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.design.DatabaseDesign#getJavaResources()
	 */

	@Override
	public DesignCollection<XspJavaResource> getXspJavaResources() {
		return getDesignElements(XspJavaResource.class);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.DatabaseDesign#getJavaResourceClassNames()
	 */
	@Override
	public SortedSet<String> getJavaResourceClassNames() {
		// TODO Decide if it's worth going through the result to remove class names that don't actually
		// 	exist in their notes. This happens when a Java class is renamed - Domino retains the old name
		//	in $ClassIndexItem for some reason

		SortedSet<String> result = new TreeSet<String>();
		DesignCollection<XspResource> resources = getDesignElements(XspResource.class);
		for (XspResource resource : resources) {
			result.addAll(resource.getClassNames());
		}
		result.remove(""); //$NON-NLS-1$
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.design.DatabaseDesign#getXPage(java.lang.String)
	 */
	@Override
	public XPage getXPage(final String name) {
		return getDesignElementByName(XPage.class, name);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.design.DatabaseDesign#getXPages()
	 */
	@Override
	public DesignCollection<org.openntf.domino.design.XPage> getXPages() {
		return getDesignElements(org.openntf.domino.design.XPage.class);
	}

	@Override
	public JarResource getJarResource(final String name) {
		return getDesignElementByName(JarResource.class, name);
	}

	@Override
	public DesignCollection<org.openntf.domino.design.JarResource> getJarResources() {
		return getDesignElements(org.openntf.domino.design.JarResource.class);
	}

	@Override
	public AnyFileResource getAnyFileResource(final String name) {
		return getDesignElementByName(AnyFileResource.class, name);
	}

	@Override
	public DesignForm getForm(final String name) {
		return getDesignElementByName(DesignForm.class, name);
	}

	@Override
	public DesignCollection<org.openntf.domino.design.DesignForm> getForms() {
		return getDesignElements(org.openntf.domino.design.DesignForm.class);
	}

	@Override
	public IconNote getIconNote() {
		if (null != iconNote_) {
			return iconNote_;
		}
		Document iconNoteDoc = database_.getDocumentByID(ICON_NOTE);
		if (iconNoteDoc != null) {
			iconNote_ = new IconNote(iconNoteDoc);
		} else {
			iconNote_ = new IconNote(database_);
		}
		return iconNote_;
	}

	@Override
	public ReplicationFormula getReplicationFormula() {
		Document repNote = database_.getDocumentByID(REPLICATION_FORMULA);
		if (repNote != null) {
			return new ReplicationFormula(repNote);
		}
		return null;
	}

	@Override
	public UsingDocument getUsingDocument(final boolean create) {
		Document doc = database_.getDocumentByID(USING_NOTE);
		if (doc != null) {
			return new UsingDocument(doc);
		} else if (create) {
			try {
				String dxl;
				try(InputStream is = AboutDocument.class.getResourceAsStream("/org/openntf/domino/design/impl/dxl_helpusingdocument.xml")) { //$NON-NLS-1$
					dxl = StreamUtil.readString(is);
				}
				DxlImporter importer = getAncestorSession().createDxlImporter();
				importer.setDesignImportOption(DxlImporter.DesignImportOption.REPLACE_ELSE_CREATE);
				importer.setReplicaRequiredForReplaceOrUpdate(false);
				importer.importDxl(dxl, database_);
				doc = database_.getDocumentByID(USING_NOTE);
				return new UsingDocument(doc);
			} catch (IOException e) {
				DominoUtils.handleException(e);
			}
		}
		return null;
	}

	@Override
	public DesignView getView(final String name) {
		return getDesignElementByName(DesignView.class, name);
	}

	@Override
	public DesignCollection<DesignView> getViews() {
		return getDesignElements(org.openntf.domino.design.DesignView.class);
	}

	@Override
	public Folder getFolder(final String name) {
		return getDesignElementByName(Folder.class, name);
	}

	@Override
	public DesignCollection<org.openntf.domino.design.Folder> getFolders() {
		return getDesignElements(org.openntf.domino.design.Folder.class);
	}

	@Override
	public DesignCollection<org.openntf.domino.design.ScriptLibraryJava> getScriptLibrariesJava() {
		return getDesignElements(org.openntf.domino.design.ScriptLibraryJava.class);
	}

	@Override
	public ScriptLibraryJava getScriptLibraryJava(final String name) {
		return getDesignElementByName(ScriptLibraryJava.class, name);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.design.DatabaseDesign#getDatabaseClassLoader()
	 */
	@Override
	public DatabaseClassLoader getDatabaseClassLoader(final ClassLoader parent) {
		return new DatabaseClassLoader(this, parent, true, false);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.design.DatabaseDesign#getDatabaseClassLoader(java.lang.ClassLoader, boolean)
	 */
	@Override
	public DatabaseClassLoader getDatabaseClassLoader(final ClassLoader parent, final boolean includeJars) {
		return new DatabaseClassLoader(this, parent, includeJars, false);
	}

	@Override
	public DatabaseClassLoader getDatabaseClassLoader(final ClassLoader parent, final boolean includeJars, final boolean includeLibraries) {
		return new DatabaseClassLoader(this, parent, includeJars, includeLibraries);
	}

	//	protected NoteCollection getNoteCollection(final String selectionFormula, final Set<SelectOption> options) {
	//		NoteCollection notes = database_.createNoteCollection(false);
	//		notes.setSelectOptions(options);
	//		notes.setSelectionFormula(selectionFormula);
	//		notes.buildCollection();
	//		return notes;
	//	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.types.DatabaseDescendant#getAncestorDatabase()
	 */
	@Override
	public Database getAncestorDatabase() {
		return database_;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public Session getAncestorSession() {
		return database_.getAncestorSession();
	}

	@Override
	public synchronized String[] getXspProperty(final String propertyName) {
		if (props == null) {
			props = new Properties();
			FileResourceWebContent res = getDesignElementByName(FileResourceWebContent.class, "WEB-INF/xsp.properties"); //$NON-NLS-1$
			if (res != null) {
				try {
					props.load(new ByteArrayInputStream(res.getFileData()));
				} catch (IOException e) {
					DominoUtils.handleException(e);
				}
			}
		}
		String setting = props.getProperty(propertyName);
		if (StringUtil.isNotEmpty(setting)) {
			if (StringUtil.indexOfIgnoreCase(setting, ",") > -1) { //$NON-NLS-1$
				return StringUtil.splitString(setting, ',');
			} else {
				return new String[] { setting };
			}
		}
		return new String[0];
	}

	@Override
	public boolean isAPIEnabled() {
		for (String s : getXspProperty("xsp.library.depends")) { //$NON-NLS-1$
			if (s.equalsIgnoreCase("org.openntf.domino.xsp.XspLibrary")) { //$NON-NLS-1$
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isAppFlagSet(final String flagName) {
		for (String s : getXspProperty("org.openntf.domino.xsp")) { //$NON-NLS-1$
			if (s.equalsIgnoreCase(flagName)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public DesignCollection<DesignBase> getDesignElements(final String formula) {
		NoteCollection notes = database_.createNoteCollection(false);
		notes.setSelectOptions(EnumSet.of(SelectOption.ALL_BUT_NOT, SelectOption.DOCUMENTS, SelectOption.PROFILES));
		notes.setSelectionFormula(formula);
		notes.buildCollection();
		return new DesignCollection<DesignBase>(notes, null);
	}

	@Override
	public <T extends DesignBase> DesignCollection<T> getDesignElements(final Class<T> type) {
		return getDesignElements(type, null);
	}

	@Override
	public <T extends DesignBase> DesignCollection<T> getDesignElements(final Class<T> type, final String search) {
		return DesignFactory.search(database_, type, search);
	}

	@Override
	public <T extends DesignBase> DesignCollection<T> getDesignElementsByName(final Class<T> type, final String name) {
		return DesignFactory.search(database_, type,
				String.format("@Explode($TITLE; '|')=\"%s\" ", DominoUtils.escapeForFormulaString(name))); //$NON-NLS-1$
	}

	@Override
	public <T extends DesignBase> T getDesignElementByName(final Class<T> type, final String name) {
		return getDesignElementByName(type, name, false);
	}

	@SuppressWarnings("unchecked")
	public <T extends DesignBase> T getDesignElementByName(final Class<T> type, final String name, final boolean create) {
		if (DominoUtils.isUnid(name)) {
			Document doc = database_.getDocumentByUNID(name);
			return (T) DesignFactory.fromDocument(doc);
		}
		Iterator<T> elems = getDesignElementsByName(type, name).iterator();
		if (elems.hasNext()) {
			return elems.next();
		}
		if (!create) {
			return null;
		}
		for (ODPMapping mapping : ODPMapping.values()) {
			Class<? extends AbstractDesignBase> cls = mapping.getInstanceClass();
			if (type.isAssignableFrom(cls)) {
				try {
					Constructor<? extends AbstractDesignBase> cTor = cls.getConstructor(Database.class);
					AbstractDesignBase ret = cTor.newInstance(getAncestorDatabase());
					if (ret instanceof DesignBaseNamed) {
						((DesignBaseNamed) ret).setName(name);
					}
					return (T) ret;
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		throw new IllegalArgumentException("Cannot Create a DesignElement of type " + type.getName() + " with name " + name);
	}

	@Override
	public String getOdsVersion() {
		return getDatabaseInfoNode().getAttribute("odsversion"); //$NON-NLS-1$
	}

	private XMLNode getDatabaseNode() {
		return getDatabaseXml().selectSingleNode("//database"); //$NON-NLS-1$
	}

	private XMLNode getDatabaseInfoNode() {
		return getDatabaseXml().selectSingleNode("//databaseinfo"); //$NON-NLS-1$
	}

	private XMLNode getNotesLaunchSettingsNode() {
		return getDatabaseXml().selectSingleNode("//noteslaunch"); //$NON-NLS-1$
	}

	private XMLNode getWebLaunchSettingsNode() {
		return getDatabaseXml().selectSingleNode("//weblaunch"); //$NON-NLS-1$
	}

	@Override
	public XMLDocument getDatabaseXml() {
		if (null == databaseXml) {
			loadDatabaseXml();
		}
		return databaseXml;
	}

	private void loadDatabaseXml() {
		DxlExporter exporter = getAncestorSession().createDxlExporter();
		exporter.setOutputDOCTYPE(false);
		NoteCollection nc = database_.createNoteCollection(false);
		nc.setSelectAcl(true);
		nc.setSelectIcon(true);
		nc.buildCollection();
		String xml = exporter.exportDxl(nc);
		databaseXml = loadDxl(xml);
	}

	protected final XMLDocument loadDxl(final String xml) {
		XMLDocument dxl_ = new XMLDocument();
		try {
			dxl_.loadString(xml);
		} catch (SAXException e) {
			DominoUtils.handleException(e);
		} catch (IOException e) {
			DominoUtils.handleException(e);
		} catch (ParserConfigurationException e) {
			DominoUtils.handleException(e);
		}
		return dxl_;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.DatabaseDesign#getDatabaseProperties()
	 */
	@Override
	public List<DbProperties> getDatabaseProperties() {
		List<DbProperties> returnVal = new ArrayList<DbProperties>();
		XMLNode node = getDatabaseNode();
		//		XMLDocument xml = getDatabaseXml();
		// Use Javascript is false, or missing in DXL if checked
		if (!"false".equals(node.getAttribute(DbProperties.USE_JS.getPropertyName()))) { //$NON-NLS-1$
			returnVal.add(DbProperties.USE_JS);
		}
		// Require SSL is true if checked or missing in DXL
		if ("true".equals(node.getAttribute(DbProperties.REQUIRE_SSL.getPropertyName()))) { //$NON-NLS-1$
			returnVal.add(DbProperties.REQUIRE_SSL);
		}
		// Don't Allow URL Open is true if checked or missing in DXL
		if ("true".equals(node.getAttribute(DbProperties.NO_URL_OPEN.getPropertyName()))) { //$NON-NLS-1$
			returnVal.add(DbProperties.NO_URL_OPEN);
		}
		// Enable enhanced HTML has an item called $AllowPost8HTML
		if (getIconNote().isEnhancedHTML()) {
			returnVal.add(DbProperties.ENHANCED_HTML);
		}
		// Don't allow open in ICAA has an item called $DisallowOpenInNBP
		if (getIconNote().isBlockICAA()) {
			returnVal.add(DbProperties.BLOCK_ICAA);
		}
		// Don't allow background agents is false if checked or missing in DXL - SO NEEDS REVERSING
		if ("false".equals(node.getAttribute(DbProperties.DISABLE_BACKGROUND_AGENTS.getPropertyName()))) { //$NON-NLS-1$
			returnVal.add(DbProperties.DISABLE_BACKGROUND_AGENTS);
		}
		// Allow stored forms is false or missing in DXL if checked
		if (!"false".equals(node.getAttribute(DbProperties.ALLOW_STORED_FORMS.getPropertyName()))) { //$NON-NLS-1$
			returnVal.add(DbProperties.ALLOW_STORED_FORMS);
		}
		// Display images after loading is true if checked or missing in DXL
		if ("true".equals(node.getAttribute(DbProperties.DEFER_IMAGE_LOADING.getPropertyName()))) { //$NON-NLS-1$
			returnVal.add(DbProperties.DEFER_IMAGE_LOADING);
		}
		// Allow document locking is true if checked or missing in DXL
		if ("true".equals(node.getAttribute(DbProperties.ALLOW_DOC_LOCKING.getPropertyName()))) { //$NON-NLS-1$
			returnVal.add(DbProperties.ALLOW_DOC_LOCKING);
		}
		// Inherit OS theme is true if checked or missing in DXL
		if ("true".equals(node.getAttribute(DbProperties.INHERIT_OS_THEME.getPropertyName()))) { //$NON-NLS-1$
			returnVal.add(DbProperties.INHERIT_OS_THEME);
		}
		// Allow design locking is true if checked or missing in DXL
		if ("true".equals(node.getAttribute(DbProperties.ALLOW_DESIGN_LOCKING.getPropertyName()))) { //$NON-NLS-1$
			returnVal.add(DbProperties.ALLOW_DESIGN_LOCKING);
		}
		// Show in open dialog is false or missing in DXL if checked
		if (!"false".equals(node.getAttribute(DbProperties.SHOW_IN_OPEN_DIALOG.getPropertyName()))) { //$NON-NLS-1$
			returnVal.add(DbProperties.SHOW_IN_OPEN_DIALOG);
		}
		// Multi db indexed is true if checked or missing in DXL
		if ("true".equals(node.getAttribute(DbProperties.MULTI_DB_INDEXING.getPropertyName()))) { //$NON-NLS-1$
			returnVal.add(DbProperties.MULTI_DB_INDEXING);
		}
		// Don't mark modified unread is false if checked or missing in DXL - SO NEEDS REVERSING
		if ("false".equals(node.getAttribute(DbProperties.MODIFIED_NOT_UNREAD.getPropertyName()))) { //$NON-NLS-1$
			returnVal.add(DbProperties.MODIFIED_NOT_UNREAD);
		}
		// Mark parent on reply or forward is true if checked or missing in DXL
		if ("true".equals(node.getAttribute(DbProperties.MARK_PARENT_REPLY_FORWARD.getPropertyName()))) { //$NON-NLS-1$
			returnVal.add(DbProperties.MARK_PARENT_REPLY_FORWARD);
		}
		// Source template name is set or missing
		if (!StringUtil.isEmpty(node.getAttribute(DbProperties.INHERIT_FROM_TEMPLATE.getPropertyName()))) {
			returnVal.add(DbProperties.INHERIT_FROM_TEMPLATE);
		}
		// Template name is set or missing
		if (!StringUtil.isEmpty(node.getAttribute(DbProperties.DB_IS_TEMPLATE.getPropertyName()))) {
			returnVal.add(DbProperties.DB_IS_TEMPLATE);
		}
		// Advanced template is true if checked or missing in DXL
		if ("true".equals(node.getAttribute(DbProperties.ADVANCED_TEMPLATE.getPropertyName()))) { //$NON-NLS-1$
			returnVal.add(DbProperties.ADVANCED_TEMPLATE);
		}
		// Multilingual is true if checked or missing in DXL
		if ("true".equals(node.getAttribute(DbProperties.MULTILINGUAL.getPropertyName()))) { //$NON-NLS-1$
			returnVal.add(DbProperties.MULTILINGUAL);
		}
		// Don't maintain unread is false or missing in DXL if checked
		if (!"false".equals(node.getAttribute(DbProperties.DONT_MAINTAIN_UNREAD.getPropertyName()))) { //$NON-NLS-1$
			returnVal.add(DbProperties.DONT_MAINTAIN_UNREAD);
		}
		// Replicate unread marks is "cluster", "all", or missing
		if (!StringUtil.isEmpty(node.getAttribute(DbProperties.REPLICATE_UNREAD.getPropertyName()))) {
			returnVal.add(DbProperties.REPLICATE_UNREAD);
		}
		// Optimize document table map is true if checked or missing in DXL
		if ("true".equals(node.getAttribute(DbProperties.OPTIMIZE_DOC_MAP.getPropertyName()))) { //$NON-NLS-1$
			returnVal.add(DbProperties.OPTIMIZE_DOC_MAP);
		}
		// Don't overwrite free space is false if checked or missing in DXL - SO NEEDS REVERSING
		if ("false".equals(node.getAttribute(DbProperties.DONT_OVERWRITE_FREE_SPACE.getPropertyName()))) { //$NON-NLS-1$
			returnVal.add(DbProperties.DONT_OVERWRITE_FREE_SPACE);
		}
		// Maintain last accessed is true if checked or missing in DXL
		if ("true".equals(node.getAttribute(DbProperties.MAINTAIN_LAST_ACCESSED.getPropertyName()))) { //$NON-NLS-1$
			returnVal.add(DbProperties.MAINTAIN_LAST_ACCESSED);
		}
		// Disable transaction logging is false if checked or missing in DXL - SO NEEDS REVERSING
		if ("false".equals(node.getAttribute(DbProperties.DISABLE_TRANSACTION_LOGGING.getPropertyName()))) { //$NON-NLS-1$
			returnVal.add(DbProperties.DISABLE_TRANSACTION_LOGGING);
		}
		// Don't support specialized response hierarchy is false if checked or missing in DXL - SO NEEDS REVERSING
		if ("false".equals(node.getAttribute(DbProperties.NO_SPECIAL_RESPONSE_HIERARCHY.getPropertyName()))) { //$NON-NLS-1$
			returnVal.add(DbProperties.NO_SPECIAL_RESPONSE_HIERARCHY);
		}
		// Use LZ1 compression is true if checked or missing in DXL
		if ("true".equals(node.getAttribute(DbProperties.USE_LZ1.getPropertyName()))) { //$NON-NLS-1$
			returnVal.add(DbProperties.USE_LZ1);
		}
		// Dont' allow headling monitoring is false if checked or missing in DXL - SO NEEDS REVERSING
		if ("false".equals(node.getAttribute(DbProperties.NO_HEADLINE_MONITORING.getPropertyName()))) { //$NON-NLS-1$
			returnVal.add(DbProperties.NO_HEADLINE_MONITORING);
		}
		// Allow more fields is true if checked or missing in DXL
		if ("true".equals(node.getAttribute(DbProperties.ALLOW_MORE_FIELDS.getPropertyName()))) { //$NON-NLS-1$
			returnVal.add(DbProperties.ALLOW_MORE_FIELDS);
		}
		// Support response thread history is true if checked or missing in DXL
		if ("true".equals(node.getAttribute(DbProperties.SUPPORT_RESPONSE_THREADS.getPropertyName()))) { //$NON-NLS-1$
			returnVal.add(DbProperties.SUPPORT_RESPONSE_THREADS);
		}
		// Don't allow simple search is true if checked or missing in DXL
		if ("true".equals(node.getAttribute(DbProperties.NO_SIMPLE_SEARCH.getPropertyName()))) { //$NON-NLS-1$
			returnVal.add(DbProperties.NO_SIMPLE_SEARCH);
		}
		// Compress design is true if checked or missing in DXL
		if ("true".equals(node.getAttribute(DbProperties.COMPRESS_DESIGN.getPropertyName()))) { //$NON-NLS-1$
			returnVal.add(DbProperties.COMPRESS_DESIGN);
		}
		// Compress data is true if checked or missing in DXL
		if ("true".equals(node.getAttribute(DbProperties.COMPRESS_DATA.getPropertyName()))) { //$NON-NLS-1$
			returnVal.add(DbProperties.COMPRESS_DATA);
		}
		// Disable view auto update is true if checked or missing in DXL
		if ("true".equals(node.getAttribute(DbProperties.NO_AUTO_VIEW_UPDATE.getPropertyName()))) { //$NON-NLS-1$
			returnVal.add(DbProperties.NO_AUTO_VIEW_UPDATE);
		}
		// Disable view export  has an item called $DisableExport
		if (getIconNote().isDisableViewExport()) {
			returnVal.add(DbProperties.NO_EXPORT_VIEW);
		}
		// Allow soft deletions is true if checked or missing in DXL
		if ("true".equals(node.getAttribute(DbProperties.ALLOW_SOFT_DELETE.getPropertyName()))) { //$NON-NLS-1$
			returnVal.add(DbProperties.ALLOW_SOFT_DELETE);
		}
		// Limit $UpdatedBy entries has a value or, if missing, = 0
		if (!StringUtil.isEmpty(node.getAttribute(DbProperties.MAX_UPDATED_BY.getPropertyName()))) {
			returnVal.add(DbProperties.MAX_UPDATED_BY);
		}
		// Limit $Revisions entries has a value or, if missing, = 0
		if (!StringUtil.isEmpty(node.getAttribute(DbProperties.MAX_REVISIONS.getPropertyName()))) {
			returnVal.add(DbProperties.MAX_REVISIONS);
		}
		// Allow DAS has an item called $AllowDas
		if (getIconNote().isAllowDas()) {
			returnVal.add(DbProperties.ALLOW_DAS);
		}
		// DAOS has an item called $Daos, set to 1
		if (getIconNote().isDaosEnabled()) {
			returnVal.add(DbProperties.DAOS_ENABLED);
		}
		// Lunch XPage Run On Server has an item called $LaunchXPageRunOnServer, set to 1
		if (getIconNote().isLaunchXPageRunOnServer()) {
			returnVal.add(DbProperties.LAUNCH_XPAGE_ON_SERVER);
		}
		// Document Summary 16Mb has an item called $LargeSummary, set to 1
		if (getIconNote().isDocumentSummary16MB()) {
			returnVal.add(DbProperties.DOCUMENT_SUMMARY_16MB);
		}
		return returnVal;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.DatabaseDesign#setDatabaseProperties(java.util.Map)
	 */
	@Override
	public void setDatabaseProperties(final Map<DbProperties, Boolean> props) {
		// Capture and error for non-settable options
		List<String> nonSettable = new ArrayList<>();
		List<String> setterMethods = new ArrayList<>();
		if (props.containsKey(DbProperties.DAOS_ENABLED)) {
			nonSettable.add(DbProperties.DAOS_ENABLED.name());
		}
		if (props.containsKey(DbProperties.DOCUMENT_SUMMARY_16MB)) {
			nonSettable.add(DbProperties.DOCUMENT_SUMMARY_16MB.name());
		}
		if (props.containsKey(DbProperties.ALLOW_DAS)) {
			setterMethods.add(DbProperties.ALLOW_DAS.name());
		}
		if (props.containsKey(DbProperties.DB_IS_TEMPLATE)) {
			setterMethods.add(DbProperties.DB_IS_TEMPLATE.name());
		}
		if (props.containsKey(DbProperties.INHERIT_FROM_TEMPLATE)) {
			setterMethods.add(DbProperties.INHERIT_FROM_TEMPLATE.name());
		}
		if (props.containsKey(DbProperties.REPLICATE_UNREAD)) {
			setterMethods.add(DbProperties.REPLICATE_UNREAD.name());
		}
		if (props.containsKey(DbProperties.MAX_REVISIONS)) {
			setterMethods.add(DbProperties.MAX_REVISIONS.name());
		}
		if (props.containsKey(DbProperties.MAX_UPDATED_BY)) {
			setterMethods.add(DbProperties.MAX_UPDATED_BY.name());
		}
		if (props.containsKey(DbProperties.SOFT_DELETE_EXPIRY)) {
			nonSettable.add(DbProperties.SOFT_DELETE_EXPIRY.name());
		}
		if (!setterMethods.isEmpty() || !nonSettable.isEmpty()) {
			String message = ""; //$NON-NLS-1$
			if (!nonSettable.isEmpty()) {
				message = "The following cannot be set programmatically but need admin processes to run: " + Strings.join(nonSettable, ",")
				+ ". ";
			}
			if (!setterMethods.isEmpty()) {
				message += "The following methods need to be set with specific setters in DatabaseDesign class: "
						+ Strings.join(setterMethods, ",") + ".";
			}
			throw new OpenNTFNotesException(message);
		}

		XMLNode node = getDatabaseNode();
		//		XMLDocument xml = getDatabaseXml();
		// Use Javascript is false, or missing in DXL if checked
		if (props.containsKey(DbProperties.USE_JS)) {
			if (props.get(DbProperties.USE_JS)) {
				node.removeAttribute(DbProperties.USE_JS.getPropertyName());
			} else {
				node.setAttribute(DbProperties.USE_JS.getPropertyName(), "false"); //$NON-NLS-1$
			}
		}
		// Require SSL is true if checked or missing in DXL
		if (props.containsKey(DbProperties.REQUIRE_SSL)) {
			if (props.get(DbProperties.REQUIRE_SSL)) {
				node.setAttribute(DbProperties.REQUIRE_SSL.getPropertyName(), "true"); //$NON-NLS-1$
			} else {
				node.removeAttribute(DbProperties.REQUIRE_SSL.getPropertyName());
			}
		}
		// Don't Allow URL Open is true if checked or missing in DXL
		if (props.containsKey(DbProperties.NO_URL_OPEN)) {
			if (props.get(DbProperties.NO_URL_OPEN)) {
				node.setAttribute(DbProperties.NO_URL_OPEN.getPropertyName(), "true"); //$NON-NLS-1$
			} else {
				node.removeAttribute(DbProperties.NO_URL_OPEN.getPropertyName());
			}
		}
		// $AllowPost8HTML is a node or not
		if (props.containsKey(DbProperties.ENHANCED_HTML)) {
			getIconNote().setEnhancedHTML(props.get(DbProperties.ENHANCED_HTML));
			isIconDirty_ = true;
		}
		// $DisallowOpenInNBP is a node or not
		if (props.containsKey(DbProperties.BLOCK_ICAA)) {
			getIconNote().setBlockICAA(props.get(DbProperties.BLOCK_ICAA));
			isIconDirty_ = true;
		}
		// Don't Allow Background Agents is false if checked or missing in DXL
		if (props.containsKey(DbProperties.DISABLE_BACKGROUND_AGENTS)) {
			if (props.get(DbProperties.DISABLE_BACKGROUND_AGENTS)) {
				node.setAttribute(DbProperties.DISABLE_BACKGROUND_AGENTS.getPropertyName(), "false"); //$NON-NLS-1$
			} else {
				node.removeAttribute(DbProperties.DISABLE_BACKGROUND_AGENTS.getPropertyName());
			}
		}
		// Allow stored forms is false, or missing in DXL if checked
		if (props.containsKey(DbProperties.ALLOW_STORED_FORMS)) {
			if (props.get(DbProperties.ALLOW_STORED_FORMS)) {
				node.removeAttribute(DbProperties.ALLOW_STORED_FORMS.getPropertyName());
			} else {
				node.setAttribute(DbProperties.ALLOW_STORED_FORMS.getPropertyName(), "false"); //$NON-NLS-1$
			}
		}
		// Display Images After Loading is true if checked or missing in DXL
		if (props.containsKey(DbProperties.DEFER_IMAGE_LOADING)) {
			if (props.get(DbProperties.DEFER_IMAGE_LOADING)) {
				node.setAttribute(DbProperties.DEFER_IMAGE_LOADING.getPropertyName(), "true"); //$NON-NLS-1$
			} else {
				node.removeAttribute(DbProperties.DEFER_IMAGE_LOADING.getPropertyName());
			}
		}
		// Allow document locking is true if checked or missing in DXL
		if (props.containsKey(DbProperties.ALLOW_DOC_LOCKING)) {
			if (props.get(DbProperties.ALLOW_DOC_LOCKING)) {
				node.setAttribute(DbProperties.ALLOW_DOC_LOCKING.getPropertyName(), "true"); //$NON-NLS-1$
			} else {
				node.removeAttribute(DbProperties.ALLOW_DOC_LOCKING.getPropertyName());
			}
		}
		// Inherit OS theme is true if checked or missing in DXL
		if (props.containsKey(DbProperties.INHERIT_OS_THEME)) {
			if (props.get(DbProperties.INHERIT_OS_THEME)) {
				node.setAttribute(DbProperties.INHERIT_OS_THEME.getPropertyName(), "true"); //$NON-NLS-1$
			} else {
				node.removeAttribute(DbProperties.INHERIT_OS_THEME.getPropertyName());
			}
		}
		// Allow design locking is true if checked or missing in DXL
		if (props.containsKey(DbProperties.ALLOW_DESIGN_LOCKING)) {
			if (props.get(DbProperties.ALLOW_DESIGN_LOCKING)) {
				node.setAttribute(DbProperties.ALLOW_DESIGN_LOCKING.getPropertyName(), "true"); //$NON-NLS-1$
			} else {
				node.removeAttribute(DbProperties.ALLOW_DESIGN_LOCKING.getPropertyName());
			}
		}
		// Show in open dialog is false, or missing in DXL if checked
		if (props.containsKey(DbProperties.SHOW_IN_OPEN_DIALOG)) {
			if (props.get(DbProperties.SHOW_IN_OPEN_DIALOG)) {
				node.removeAttribute(DbProperties.SHOW_IN_OPEN_DIALOG.getPropertyName());
			} else {
				node.setAttribute(DbProperties.SHOW_IN_OPEN_DIALOG.getPropertyName(), "false"); //$NON-NLS-1$
			}
		}
		// Multi Db indexing is true if checked or missing in DXL
		if (props.containsKey(DbProperties.MULTI_DB_INDEXING)) {
			if (props.get(DbProperties.MULTI_DB_INDEXING)) {
				node.setAttribute(DbProperties.MULTI_DB_INDEXING.getPropertyName(), "true"); //$NON-NLS-1$
			} else {
				node.removeAttribute(DbProperties.MULTI_DB_INDEXING.getPropertyName());
			}
		}
		// Don't mark modified unread is false if checked or missing in DXL
		if (props.containsKey(DbProperties.MODIFIED_NOT_UNREAD)) {
			if (props.get(DbProperties.MODIFIED_NOT_UNREAD)) {
				node.setAttribute(DbProperties.MODIFIED_NOT_UNREAD.getPropertyName(), "false"); //$NON-NLS-1$
			} else {
				node.removeAttribute(DbProperties.MODIFIED_NOT_UNREAD.getPropertyName());
			}
		}
		// Mark parent on reply or forward is true if checked or missing in DXL
		if (props.containsKey(DbProperties.MARK_PARENT_REPLY_FORWARD)) {
			if (props.get(DbProperties.MARK_PARENT_REPLY_FORWARD)) {
				node.setAttribute(DbProperties.MARK_PARENT_REPLY_FORWARD.getPropertyName(), "true"); //$NON-NLS-1$
			} else {
				node.removeAttribute(DbProperties.MARK_PARENT_REPLY_FORWARD.getPropertyName());
			}
		}
		// Advanced template is true if checked or missing in DXL
		if (props.containsKey(DbProperties.ADVANCED_TEMPLATE)) {
			if (props.get(DbProperties.ADVANCED_TEMPLATE)) {
				node.setAttribute(DbProperties.ADVANCED_TEMPLATE.getPropertyName(), "true"); //$NON-NLS-1$
			} else {
				node.removeAttribute(DbProperties.ADVANCED_TEMPLATE.getPropertyName());
			}
		}
		// Multilingual is true if checked or missing in DXL
		if (props.containsKey(DbProperties.MULTILINGUAL)) {
			if (props.get(DbProperties.MULTILINGUAL)) {
				node.setAttribute(DbProperties.MULTILINGUAL.getPropertyName(), "true"); //$NON-NLS-1$
			} else {
				node.removeAttribute(DbProperties.MULTILINGUAL.getPropertyName());
			}
		}
		// Don't maintain unread is false, or missing in DXL if checked
		if (props.containsKey(DbProperties.DONT_MAINTAIN_UNREAD)) {
			if (props.get(DbProperties.DONT_MAINTAIN_UNREAD)) {
				node.removeAttribute(DbProperties.DONT_MAINTAIN_UNREAD.getPropertyName());
			} else {
				node.setAttribute(DbProperties.DONT_MAINTAIN_UNREAD.getPropertyName(), "false"); //$NON-NLS-1$
			}
		}
		// Optimize document table map is true if checked or missing in DXL
		if (props.containsKey(DbProperties.OPTIMIZE_DOC_MAP)) {
			if (props.get(DbProperties.OPTIMIZE_DOC_MAP)) {
				node.setAttribute(DbProperties.OPTIMIZE_DOC_MAP.getPropertyName(), "true"); //$NON-NLS-1$
			} else {
				node.removeAttribute(DbProperties.OPTIMIZE_DOC_MAP.getPropertyName());
			}
		}
		// Don't overwrite free space is false if checked or missing in DXL
		if (props.containsKey(DbProperties.DONT_OVERWRITE_FREE_SPACE)) {
			if (props.get(DbProperties.DONT_OVERWRITE_FREE_SPACE)) {
				node.setAttribute(DbProperties.DONT_OVERWRITE_FREE_SPACE.getPropertyName(), "false"); //$NON-NLS-1$
			} else {
				node.removeAttribute(DbProperties.DONT_OVERWRITE_FREE_SPACE.getPropertyName());
			}
		}
		// Maintain last accessed is true if checked or missing in DXL
		if (props.containsKey(DbProperties.MAINTAIN_LAST_ACCESSED)) {
			if (props.get(DbProperties.MAINTAIN_LAST_ACCESSED)) {
				node.setAttribute(DbProperties.MAINTAIN_LAST_ACCESSED.getPropertyName(), "true"); //$NON-NLS-1$
			} else {
				node.removeAttribute(DbProperties.MAINTAIN_LAST_ACCESSED.getPropertyName());
			}
		}
		// Disable transaction logging is false if checked or missing in DXL
		if (props.containsKey(DbProperties.DISABLE_TRANSACTION_LOGGING)) {
			if (props.get(DbProperties.DISABLE_TRANSACTION_LOGGING)) {
				node.setAttribute(DbProperties.DISABLE_TRANSACTION_LOGGING.getPropertyName(), "false"); //$NON-NLS-1$
			} else {
				node.removeAttribute(DbProperties.DISABLE_TRANSACTION_LOGGING.getPropertyName());
			}
		}
		// Don't support specialized response hierarchy is false if checked or missing in DXL
		if (props.containsKey(DbProperties.NO_SPECIAL_RESPONSE_HIERARCHY)) {
			if (props.get(DbProperties.NO_SPECIAL_RESPONSE_HIERARCHY)) {
				node.setAttribute(DbProperties.NO_SPECIAL_RESPONSE_HIERARCHY.getPropertyName(), "false"); //$NON-NLS-1$
			} else {
				node.removeAttribute(DbProperties.NO_SPECIAL_RESPONSE_HIERARCHY.getPropertyName());
			}
		}
		// LZ1 compression is true if checked or missing in DXL
		if (props.containsKey(DbProperties.USE_LZ1)) {
			if (props.get(DbProperties.USE_LZ1)) {
				node.setAttribute(DbProperties.USE_LZ1.getPropertyName(), "true"); //$NON-NLS-1$
			} else {
				node.removeAttribute(DbProperties.USE_LZ1.getPropertyName());
			}
		}
		// Don't allow headline monitoring is false if checked or missing in DXL
		if (props.containsKey(DbProperties.NO_HEADLINE_MONITORING)) {
			if (props.get(DbProperties.NO_HEADLINE_MONITORING)) {
				node.setAttribute(DbProperties.NO_HEADLINE_MONITORING.getPropertyName(), "false"); //$NON-NLS-1$
			} else {
				node.removeAttribute(DbProperties.NO_HEADLINE_MONITORING.getPropertyName());
			}
		}
		// Allow more fields is true if checked or missing in DXL
		if (props.containsKey(DbProperties.ALLOW_MORE_FIELDS)) {
			if (props.get(DbProperties.ALLOW_MORE_FIELDS)) {
				node.setAttribute(DbProperties.ALLOW_MORE_FIELDS.getPropertyName(), "true"); //$NON-NLS-1$
			} else {
				node.removeAttribute(DbProperties.ALLOW_MORE_FIELDS.getPropertyName());
			}
		}
		// Support response thread hierarchy is true if checked or missing in DXL
		if (props.containsKey(DbProperties.SUPPORT_RESPONSE_THREADS)) {
			if (props.get(DbProperties.SUPPORT_RESPONSE_THREADS)) {
				node.setAttribute(DbProperties.SUPPORT_RESPONSE_THREADS.getPropertyName(), "true"); //$NON-NLS-1$
			} else {
				node.removeAttribute(DbProperties.SUPPORT_RESPONSE_THREADS.getPropertyName());
			}
		}
		// Don't allow simple search is true if checked or missing in DXL
		if (props.containsKey(DbProperties.NO_SIMPLE_SEARCH)) {
			if (props.get(DbProperties.NO_SIMPLE_SEARCH)) {
				node.setAttribute(DbProperties.NO_SIMPLE_SEARCH.getPropertyName(), "true"); //$NON-NLS-1$
			} else {
				node.removeAttribute(DbProperties.NO_SIMPLE_SEARCH.getPropertyName());
			}
		}
		// Compress design is true if checked or missing in DXL
		if (props.containsKey(DbProperties.COMPRESS_DESIGN)) {
			if (props.get(DbProperties.COMPRESS_DESIGN)) {
				node.setAttribute(DbProperties.COMPRESS_DESIGN.getPropertyName(), "true"); //$NON-NLS-1$
			} else {
				node.removeAttribute(DbProperties.COMPRESS_DESIGN.getPropertyName());
			}
		}
		// Compress data is true if checked or missing in DXL
		if (props.containsKey(DbProperties.COMPRESS_DATA)) {
			if (props.get(DbProperties.COMPRESS_DATA)) {
				node.setAttribute(DbProperties.COMPRESS_DATA.getPropertyName(), "true"); //$NON-NLS-1$
			} else {
				node.removeAttribute(DbProperties.COMPRESS_DATA.getPropertyName());
			}
		}
		// Disable view auto update is true if checked or missing in DXL
		if (props.containsKey(DbProperties.NO_AUTO_VIEW_UPDATE)) {
			if (props.get(DbProperties.NO_AUTO_VIEW_UPDATE)) {
				node.setAttribute(DbProperties.NO_AUTO_VIEW_UPDATE.getPropertyName(), "true"); //$NON-NLS-1$
			} else {
				node.removeAttribute(DbProperties.NO_AUTO_VIEW_UPDATE.getPropertyName());
			}
		}
		// $DisableViewExport is a node or not
		if (props.containsKey(DbProperties.NO_EXPORT_VIEW)) {
			getIconNote().setEnhancedHTML(props.get(DbProperties.NO_EXPORT_VIEW));
			isIconDirty_ = true;
		}
		// Allow soft deletions is true if checked or missing in DXL
		if (props.containsKey(DbProperties.ALLOW_SOFT_DELETE)) {
			if (props.get(DbProperties.ALLOW_SOFT_DELETE)) {
				node.setAttribute(DbProperties.ALLOW_SOFT_DELETE.getPropertyName(), "true"); //$NON-NLS-1$
			} else {
				node.removeAttribute(DbProperties.ALLOW_SOFT_DELETE.getPropertyName());
			}
		}
		// $LaunchXPageRunOnServer is a node or not
		if (props.containsKey(DbProperties.LAUNCH_XPAGE_ON_SERVER)) {
			getIconNote().setEnhancedHTML(props.get(DbProperties.LAUNCH_XPAGE_ON_SERVER));
			isIconDirty_ = true;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.DatabaseDesign#getTemplateName()
	 */
	@Override
	public String getTemplateName() {
		return getDatabaseNode().getAttribute(DbProperties.INHERIT_FROM_TEMPLATE.getPropertyName());
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.DatabaseDesign#setTemplateName(java.lang.String)
	 */
	@Override
	public void setTemplateName(final String name) {
		getDatabaseNode().setAttribute(DbProperties.INHERIT_FROM_TEMPLATE.getPropertyName(), name);
	}

	@Override
	public String getNameIfTemplate() {
		return getDatabaseNode().getAttribute(DbProperties.DB_IS_TEMPLATE.getPropertyName());
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.DatabaseDesign#setTemplateName(java.lang.String)
	 */
	@Override
	public void setNameIfTemplate(final String name) {
		getDatabaseNode().setAttribute(DbProperties.DB_IS_TEMPLATE.getPropertyName(), name);
	}

	@Override
	public DASMode getDasMode() {
		return getIconNote().getDASMode();
	}

	@Override
	public void setDasMode(final DASMode mode) {
		getIconNote().setDASMode(mode);
		isIconDirty_ = true;
	}

	@Override
	public UnreadReplicationSetting getReplicateUnreadSetting() {
		String replicateSetting = getDatabaseNode().getAttribute(DbProperties.REPLICATE_UNREAD.getPropertyName());
		if (!StringUtil.isEmpty(replicateSetting)) {
			for (UnreadReplicationSetting opt : UnreadReplicationSetting.values()) {
				if (opt.getPropertyName().equals(replicateSetting)) {
					return opt;
				}
			}
		}
		return UnreadReplicationSetting.NEVER;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.DatabaseDesign#setReplicateUnreadSetting(org.openntf.domino.design.DatabaseDesign.UnreadReplicationSetting)
	 */
	@Override
	public void setReplicateUnreadSetting(final UnreadReplicationSetting setting) {
		if (setting.equals(UnreadReplicationSetting.NEVER)) {
			getDatabaseNode().removeAttribute(DbProperties.REPLICATE_UNREAD.getPropertyName());
		} else {
			getDatabaseNode().setAttribute(DbProperties.REPLICATE_UNREAD.getPropertyName(), setting.getPropertyName());
		}
	}

	@Override
	public int getMaxUpdatedBy() {
		String updatedBy = getDatabaseNode().getAttribute(DbProperties.MAX_UPDATED_BY.getPropertyName());
		if (StringUtil.isEmpty(updatedBy)) {
			return 0;
		} else {
			return Integer.parseInt(updatedBy);
		}
	}

	@Override
	public int getMaxRevisions() {
		String revisions = getDatabaseNode().getAttribute(DbProperties.MAX_REVISIONS.getPropertyName());
		if (StringUtil.isEmpty(revisions)) {
			return 0;
		} else {
			return Integer.parseInt(revisions);
		}
	}

	@Override
	public int getSoftDeletionsExpireIn() {
		if (!"true".equals(getDatabaseNode().getAttribute(DbProperties.ALLOW_SOFT_DELETE.getPropertyName()))) {
			return Integer.MIN_VALUE;
		} else {
			String softDeleteExpiry = getDatabaseNode().getAttribute(DbProperties.SOFT_DELETE_EXPIRY.getPropertyName());
			if (StringUtil.isEmpty(softDeleteExpiry)) {
				return 48;
			} else {
				return Integer.parseInt(softDeleteExpiry);
			}
		}
	}

	@Override
	public void setSoftDeletionsExpireIn(final int hours) {
		if (hours > Integer.MIN_VALUE) {
			getDatabaseNode().setAttribute(DbProperties.SOFT_DELETE_EXPIRY.getPropertyName(), Integer.toString(hours));
		} else {
			getDatabaseNode().removeAttribute(DbProperties.ALLOW_SOFT_DELETE.getPropertyName());
			getDatabaseNode().setAttribute(DbProperties.SOFT_DELETE_EXPIRY.getPropertyName(), "48"); //$NON-NLS-1$
		}
	}

	@Override
	public boolean save() {
		DxlImporter importer = getAncestorSession().createDxlImporter();
		importer.setDesignImportOption(DxlImporter.DesignImportOption.REPLACE_ELSE_CREATE);
		importer.setReplicaRequiredForReplaceOrUpdate(false);
		importer.setReplaceDbProperties(true);
		Database database = getAncestorDatabase();
		try {
			lotus.domino.Stream stream = getAncestorSession().createStream();
			stream.writeText(getDatabaseXml().getXml());
			importer.importDxl(stream, database);
		} catch (IOException | NotesException e) {
			DominoUtils.handleException(e);
			if (importer != null) {
				System.out.println(importer.getLog());
			}
			return false;
		}

		if (isIconDirty_) {
			getIconNote().save();
			// Icon Note gets cached, so this won't be the latest version
		}

		return true;
	}

	@Override
	public void setMaxUpdatedBy(final int newMax) {
		if (newMax <= 0) {
			getDatabaseNode().removeAttribute(DbProperties.MAX_UPDATED_BY.getPropertyName());
		}
		getDatabaseNode().setAttribute(DbProperties.MAX_UPDATED_BY.getPropertyName(), Integer.toString(newMax));
	}

	@Override
	public void setMaxRevisions(final int newMax) {
		if (newMax <= 0) {
			getDatabaseNode().removeAttribute(DbProperties.MAX_REVISIONS.getPropertyName());
		}
		getDatabaseNode().setAttribute(DbProperties.MAX_REVISIONS.getPropertyName(), Integer.toString(newMax));
	}

	@Override
	public LaunchContextNotes getNotesLaunchContext() {
		XMLNode node = getNotesLaunchSettingsNode();
		if (null != node) {
			String val = node.getAttribute("whenopened"); //$NON-NLS-1$
			for (LaunchContextNotes context : LaunchContextNotes.values()) {
				if (context.getPropertyName().equals(val)) {
					return context;
				}
			}
		}
		return LaunchContextNotes.LAST_VIEWED;
	}

	@Override
	public String getNotesLaunchDesignElement() {
		XMLNode node = getNotesLaunchSettingsNode();
		switch (getNotesLaunchContext()) {
		case NAVIGATOR:
		case NAVIGATOR_IN_WINDOW:
			return node.getAttribute("navigator"); //$NON-NLS-1$
		case FRAMESET:
			return node.getAttribute("frameset"); //$NON-NLS-1$
		case XPAGE:
			return node.getAttribute("xpage"); //$NON-NLS-1$
		case COMPOSITE_APP:
			return node.getAttribute("compapp") + "##" + node.getAttribute("compapppage"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		default:
			return ""; //$NON-NLS-1$
		}
	}

	@Override
	public ShowAboutContext getShowAboutContext() {
		XMLNode node = getNotesLaunchSettingsNode();
		if (null != node) {
			String val = node.getAttribute("showaboutdocument"); //$NON-NLS-1$
			for (ShowAboutContext context : ShowAboutContext.values()) {
				if (context.getPropertyName().equals(val)) {
					return context;
				}
			}
		}
		return ShowAboutContext.FIRST_OPENED;
	}

	@Override
	public PreviewPaneDefault getDefaultPreviewPaneLocation() {
		XMLNode node = getNotesLaunchSettingsNode();
		if (null != node) {
			String val = node.getAttribute("previewdefault"); //$NON-NLS-1$
			for (PreviewPaneDefault location : PreviewPaneDefault.values()) {
				if (location.getPropertyName().equals(val)) {
					return location;
				}
			}
		}
		return PreviewPaneDefault.BOTTOM;
	}

	@Override
	public LaunchContextWeb getWebLaunchContext() {
		XMLNode node = getWebLaunchSettingsNode();
		if (null != node) {
			String val = node.getAttribute("whenopened"); //$NON-NLS-1$
			for (LaunchContextWeb context : LaunchContextWeb.values()) {
				if (context.getPropertyName().equals(val)) {
					return context;
				}
			}
		}
		return LaunchContextWeb.NOTES_LAUNCH;
	}

	@Override
	public String getWebLaunchDesignElement() {
		XMLNode node = getWebLaunchSettingsNode();
		switch (getWebLaunchContext()) {
		case NAVIGATOR:
		case NAVIGATOR_IN_WINDOW:
			return node.getAttribute("navigator"); //$NON-NLS-1$
		case FRAMESET:
			return node.getAttribute("frameset"); //$NON-NLS-1$
		case XPAGE:
			return node.getAttribute("xpage"); //$NON-NLS-1$
		case FIRST_DOC_IN_VIEW:
			return node.getAttribute("view"); //$NON-NLS-1$
		case PAGE:
			return node.getAttribute("page"); //$NON-NLS-1$
		case SPECIFIC_DOC_LINK:
			XMLNode docLinkDetails = node.selectSingleNode("//doclink"); //$NON-NLS-1$
			return docLinkDetails.getAttribute("database") + docLinkDetails.getAttribute("document"); //$NON-NLS-1$ //$NON-NLS-2$
		default:
			return ""; //$NON-NLS-1$
		}
	}

	@Override
	public int getCssExpiry() {
		return getIconNote().getCssExpiry();
	}

	@Override
	public int getFileExpiry() {
		return getIconNote().getFileExpiry();
	}

	@Override
	public int getImageExpiry() {
		return getIconNote().getImageExpiry();
	}

	@Override
	public int getJsExpiry() {
		return getIconNote().getJsExpiry();
	}

	@Override
	public void setCssExpiry(final int days) {
		getIconNote().setCssExpiry(days);
		isIconDirty_ = true;
	}

	@Override
	public void setFileExpiry(final int days) {
		getIconNote().setFileExpiry(days);
		isIconDirty_ = true;
	}

	@Override
	public void setImageExpiry(final int days) {
		getIconNote().setImageExpiry(days);
		isIconDirty_ = true;
	}

	@Override
	public void setJsExpiry(final int days) {
		getIconNote().setJsExpiry(days);
		isIconDirty_ = true;
	}

}
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

package org.openntf.domino.design.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

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
import org.openntf.domino.design.Subform;
import org.openntf.domino.design.XspJavaResource;
import org.openntf.domino.design.XspResource;
import org.openntf.domino.utils.DominoUtils;
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
public class DatabaseDesign implements org.openntf.domino.design.DatabaseDesign {
	private static final Logger log_ = Logger.getLogger(DatabaseDesign.class.getName());

	/*
	 * Some handy constant Note IDs for getting specific elements. h/t http://www.nsftools.com/tips/NotesTips.htm#defaultelements
	 */
	public static final String ABOUT_NOTE = "FFFF0002";
	public static final String DEFAULT_FORM = "FFFF0004";
	public static final String DEFAULT_VIEW = "FFFF0008";
	public static final String ICON_NOTE = "FFFF0010";

	// Design collections are pretty impossible to work with using the Java API: the exported DXL is
	// blank and the raw $Collection data isn't readable as an Item
	public static final String DESIGN_COLLECTION = "FFFF0020";

	public static final String ACL_NOTE = "FFFF0040";
	public static final String USING_NOTE = "FFFF0100";
	public static final String REPLICATION_FORMULA = "FFFF0800";

	private transient Properties props;

	private final Database database_;
	private XMLDocument databaseXml;

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
		result.remove("");
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
		Document iconNote = database_.getDocumentByID(ICON_NOTE);
		if (iconNote != null) {
			return new IconNote(iconNote);
		}
		log_.fine("No icon note found for database " + getAncestorDatabase().getApiPath());
		return null;
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
				InputStream is = AboutDocument.class.getResourceAsStream("/org/openntf/domino/design/impl/dxl_helpusingdocument.xml");
				String dxl = StreamUtil.readString(is);
				is.close();
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
			FileResourceWebContent res = getDesignElementByName(FileResourceWebContent.class, "WEB-INF/xsp.properties");
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
			if (StringUtil.indexOfIgnoreCase(setting, ",") > -1) {
				return StringUtil.splitString(setting, ',');
			} else {
				return new String[] { setting };
			}
		}
		return new String[0];
	}

	@Override
	public boolean isAPIEnabled() {
		for (String s : getXspProperty("xsp.library.depends")) {
			if (s.equalsIgnoreCase("org.openntf.domino.xsp.XspLibrary")) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isAppFlagSet(final String flagName) {
		for (String s : getXspProperty("org.openntf.domino.xsp")) {
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
				String.format("@Explode($TITLE; '|')=\"%s\" ", DominoUtils.escapeForFormulaString(name)));
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
		return getDatabaseInfoNode().getAttribute("odsversion");
	}

	private XMLNode getDatabaseNode() {
		return getDatabaseXml().selectSingleNode("//database");
	}

	private XMLNode getDatabaseInfoNode() {
		return getDatabaseXml().selectSingleNode("//databaseinfo");
	}

	/**
	 * Gets database XML, for which we need a minimum of two design notes in exported DXL
	 *
	 * @return
	 */
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

}
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
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.NoteCollection;
import org.openntf.domino.NoteCollection.SelectOption;
import org.openntf.domino.Session;
import org.openntf.domino.design.AboutDocument;
import org.openntf.domino.design.DesignBase;
import org.openntf.domino.design.DesignForm;
import org.openntf.domino.design.DesignView;
import org.openntf.domino.design.FileResourceWebContent;
import org.openntf.domino.design.XspResource;
import org.openntf.domino.ext.NoteClass;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

import com.ibm.commons.util.StringUtil;

/**
 * @author jgallagher
 * 
 */
public class DatabaseDesign implements org.openntf.domino.design.DatabaseDesign {
	private static final Logger log_ = Logger.getLogger(DatabaseDesign.class.getName());

	/*
	 * Some handy constant Note IDs for getting specific elements. h/t http://www.nsftools.com/tips/NotesTips.htm#defaultelements
	 */
	//	public static final String ABOUT_NOTE = "FFFF0002";
	//	public static final String DEFAULT_FORM = "FFFF0004";
	//	public static final String DEFAULT_VIEW = "FFFF0008";
	//	public static final String ICON_NOTE = "FFFF0010";
	//	public static final String DESIGN_COLLECTION = "FFFF0020";
	//	public static final String ACL_NOTE = "FFFF0040";
	//	public static final String USING_NOTE = "FFFF0100";
	//	public static final String REPLICATION_FORMULA = "FFFF0800";

	// Design collections are pretty impossible to work with using the Java API: the exported DXL is
	// blank and the raw $Collection data isn't readable as an Item

	private transient Properties props;

	private final Database database_;

	private NapiDatabaseDesign napiDesign_;

	public DatabaseDesign(final Database database) {
		database_ = database;
	}

	public <T extends org.openntf.domino.design.DesignBase> T create(final Class<T> type) {
		DesignFactory mapping = DesignFactory.valueOf(type);
		if (mapping == null || mapping.getImplClass() == null) {
			throw new IllegalArgumentException("Cannot create DesignBaset of Type " + type.getName());
		}

		try {
			AbstractDesignBase base = mapping.getImplClass().newInstance();
			base.init(database_);
			return (T) base;
		} catch (Exception e) {
			throw new IllegalArgumentException("Cannot create DesignBaset of Type " + type.getName(), e);
		}

	}

	@Override
	public AboutDocument getAboutDocument(final boolean create) {
		Document doc = database_.getDocumentByID(NoteClass.INFO.defaultID());
		if (doc != null) {
			return (AboutDocument) DesignFactory.fromDocument(doc);
		} else if (create) {
			return create(AboutDocument.class);
		}
		return null;
	}

	@Override
	public ACLNote getACL() {
		return (ACLNote) DesignFactory.fromDocument(database_.getDocumentByID(NoteClass.ACL.defaultID()));
	}

	@Override
	public DesignForm getDefaultForm() {
		Document formDoc = database_.getDocumentByID(NoteClass.FORM.defaultID());
		if (formDoc != null) {
			return (DesignForm) DesignFactory.fromDocument(formDoc);
		}
		return null;
	}

	@Override
	public DesignView getDefaultView() {
		Document viewDoc = database_.getDocumentByID(NoteClass.VIEW.defaultID());
		if (viewDoc != null) {
			return (DesignView) DesignFactory.fromDocument(viewDoc);
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

	//
	//	@Override
	//	public FileResource getFileResource(final String name) {
	//		return getDesignElementByName(FileResource.class, name);
	//		//		if (DominoUtils.isUnid(name)) {
	//		//			Document doc = database_.getDocumentByUNID(name);
	//		//			return new org.openntf.domino.design.impl.FileResource(doc);
	//		//		} else {
	//		//			NoteCollection notes = getNoteCollection(
	//		//					String.format(" !@Contains($Flags; '~') & @Contains($Flags; 'g') & @Explode($TITLE; '|')=\"%s\" ",
	//		//							DominoUtils.escapeForFormulaString(name)), EnumSet.of(SelectOption.MISC_FORMAT));
	//		//
	//		//			String noteId = notes.getFirstNoteID();
	//		//			if (!noteId.isEmpty()) {
	//		//				Document doc = database_.getDocumentByID(noteId);
	//		//				return new FileResource(doc);
	//		//			}
	//		//		}
	//		//		return null;
	//	}
	//
	//	@Override
	//	public DesignCollection<org.openntf.domino.design.FileResource> getFileResources() {
	//		return getDesignElements(org.openntf.domino.design.FileResource.class);
	//		//NoteCollection notes = getNoteCollection(" !@Contains($Flags; '~') & @Contains($Flags; 'g') ", EnumSet.of(SelectOption.MISC_FORMAT));
	//		//return new DesignCollection<org.openntf.domino.design.FileResource>(notes, FileResource.class);
	//	}
	//
	//	@Override
	//	public FileResourceHidden getHiddenFileResource(final String name) {
	//		return getDesignElementByName(FileResourceHidden.class, name);
	//	}
	//
	//	@Override
	//	public DesignCollection<FileResourceHidden> getHiddenFileResources() {
	//		return getDesignElements(org.openntf.domino.design.FileResourceHidden.class);
	//	}
	//
	//	/* (non-Javadoc)
	//	 * @see org.openntf.domino.design.DatabaseDesign#getImageResource(java.lang.String)
	//	 */
	//	@Override
	//	public ImageResource getImageResource(final String name) {
	//		return getDesignElementByName(ImageResource.class, name);
	//	}
	//
	//	/* (non-Javadoc)
	//	 * @see org.openntf.domino.design.DatabaseDesign#getImageResources()
	//	 */
	//	@Override
	//	public org.openntf.domino.design.DesignCollection<org.openntf.domino.design.ImageResource> getImageResources() {
	//		return getDesignElements(org.openntf.domino.design.ImageResource.class);
	//	}
	//
	//	/* (non-Javadoc)
	//	 * @see org.openntf.domino.design.DatabaseDesign#getStyleSheet(java.lang.String)
	//	 */
	//	@Override
	//	public StyleSheet getStyleSheet(final String name) {
	//		return getDesignElementByName(StyleSheet.class, name);
	//	}
	//
	//	/* (non-Javadoc)
	//	 * @see org.openntf.domino.design.DatabaseDesign#getStyleSheets()
	//	 */
	//	@Override
	//	public org.openntf.domino.design.DesignCollection<org.openntf.domino.design.StyleSheet> getStyleSheets() {
	//		return getDesignElements(org.openntf.domino.design.StyleSheet.class);
	//	}
	//
	//	/*
	//	 * (non-Javadoc)
	//	 * 
	//	 * @see org.openntf.domino.design.DatabaseDesign#getJavaResource(java.lang.String)
	//	 */
	//	@Override
	//	public XspJavaResource getXspJavaResource(final String name) {
	//		return getDesignElementByName(XspJavaResource.class, name);
	//	}
	//
	//	/*
	//	 * (non-Javadoc)
	//	 * 
	//	 * @see org.openntf.domino.design.DatabaseDesign#getJavaResources()
	//	 */
	//
	//	@Override
	//	public DesignCollection<XspJavaResource> getXspJavaResources() {
	//		return getDesignElements(XspJavaResource.class);
	//	}
	//
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

	//
	//	/*
	//	 * (non-Javadoc)
	//	 * 
	//	 * @see org.openntf.domino.design.DatabaseDesign#getXPage(java.lang.String)
	//	 */
	//	@Override
	//	public XPage getXPage(final String name) {
	//		return getDesignElementByName(XPage.class, name);
	//	}
	//
	//	/*
	//	 * (non-Javadoc)
	//	 * 
	//	 * @see org.openntf.domino.design.DatabaseDesign#getXPages()
	//	 */
	//	@Override
	//	public DesignCollection<org.openntf.domino.design.XPage> getXPages() {
	//		return getDesignElements(org.openntf.domino.design.XPage.class);
	//	}
	//
	//	@Override
	//	public JarResource getJarResource(final String name) {
	//		return getDesignElementByName(JarResource.class, name);
	//	}
	//
	//	@Override
	//	public DesignCollection<org.openntf.domino.design.JarResource> getJarResources() {
	//		return getDesignElements(org.openntf.domino.design.JarResource.class);
	//	}
	//
	//	@Override
	//	public AnyFileResource getAnyFileResource(final String name) {
	//		return getDesignElementByName(AnyFileResource.class, name);
	//	}
	//
	//	@Override
	//	public DesignForm getForm(final String name) {
	//		return getDesignElementByName(DesignForm.class, name);
	//	}
	//
	//	@Override
	//	public DesignCollection<org.openntf.domino.design.DesignForm> getForms() {
	//		return getDesignElements(org.openntf.domino.design.DesignForm.class);
	//	}
	//
	@Override
	public IconNote getIconNote() {
		Document iconNote = database_.getDocumentByID(NoteClass.ICON.defaultID());
		if (iconNote != null) {
			return (IconNote) DesignFactory.fromDocument(iconNote);
		}
		log_.fine("No icon note found for database " + getAncestorDatabase().getApiPath());
		return null;
	}

	@Override
	public ReplicationFormula getReplicationFormula() {
		Document repNote = database_.getDocumentByID(NoteClass.REPLFORMULA.defaultID());
		if (repNote != null) {
			return (ReplicationFormula) DesignFactory.fromDocument(repNote);
		}
		return null;
	}

	@Override
	public UsingDocument getUsingDocument(final boolean create) {
		Document doc = database_.getDocumentByID(NoteClass.HELP.defaultID());
		if (doc != null) {
			return (UsingDocument) DesignFactory.fromDocument(doc);
		} else if (create) {
			return create(UsingDocument.class);
		}
		return null;
	}

	//
	//	@Override
	//	public DesignView getView(final String name) {
	//		return getDesignElementByName(DesignView.class, name);
	//	}
	//
	//	@Override
	//	public DesignCollection<DesignView> getViews() {
	//		return getDesignElements(org.openntf.domino.design.DesignView.class);
	//	}
	//
	//	@Override
	//	public Folder getFolder(final String name) {
	//		return getDesignElementByName(Folder.class, name);
	//	}
	//
	//	@Override
	//	public DesignCollection<org.openntf.domino.design.Folder> getFolders() {
	//		return getDesignElements(org.openntf.domino.design.Folder.class);
	//	}
	//
	//	@Override
	//	public DesignCollection<org.openntf.domino.design.ScriptLibraryJava> getScriptLibrariesJava() {
	//		return getDesignElements(org.openntf.domino.design.ScriptLibraryJava.class);
	//	}
	//
	//	@Override
	//	public ScriptLibraryJava getScriptLibraryJava(final String name) {
	//		return getDesignElementByName(ScriptLibraryJava.class, name);
	//	}
	//
	//	/*
	//	 * (non-Javadoc)
	//	 * 
	//	 * @see org.openntf.domino.design.DatabaseDesign#getDatabaseClassLoader()
	//	 */
	//	@Override
	//	public DatabaseClassLoader getDatabaseClassLoader(final ClassLoader parent) {
	//		return new DatabaseClassLoader(this, parent, true, false);
	//	}
	//
	//	/*
	//	 * (non-Javadoc)
	//	 * 
	//	 * @see org.openntf.domino.design.DatabaseDesign#getDatabaseClassLoader(java.lang.ClassLoader, boolean)
	//	 */
	//	@Override
	//	public DatabaseClassLoader getDatabaseClassLoader(final ClassLoader parent, final boolean includeJars) {
	//		return new DatabaseClassLoader(this, parent, includeJars, false);
	//	}
	//
	//	@Override
	//	public DatabaseClassLoader getDatabaseClassLoader(final ClassLoader parent, final boolean includeJars, final boolean includeLibraries) {
	//		return new DatabaseClassLoader(this, parent, includeJars, includeLibraries);
	//	}
	//
	//	//	protected NoteCollection getNoteCollection(final String selectionFormula, final Set<SelectOption> options) {
	//	//		NoteCollection notes = database_.createNoteCollection(false);
	//	//		notes.setSelectOptions(options);
	//	//		notes.setSelectionFormula(selectionFormula);
	//	//		notes.buildCollection();
	//	//		return notes;
	//	//	}

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
			FileResourceWebContent res = getDesignElement(FileResourceWebContent.class, "WEB-INF/xsp.properties");
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
	public org.openntf.domino.design.DesignCollection<DesignBase> searchDesignElements(final String formula) {
		NoteCollection notes = database_.createNoteCollection(false);
		notes.setSelectOptions(EnumSet.of(SelectOption.ALL_BUT_NOT, SelectOption.DOCUMENTS, SelectOption.PROFILES));
		notes.setSelectionFormula(formula);
		notes.buildCollection();
		return new DesignCollection<DesignBase>(notes, null);
	}

	@Override
	public <T extends DesignBase> DesignCollection<T> searchDesignElements(final Class<T> type, final String search) {
		NoteCollection nnc = database_.createNoteCollection(false);

		DesignFactory mapping = DesignFactory.valueOf(type);

		// Set up selection formula
		StringBuilder sb = new StringBuilder();
		if (!StringUtil.isEmpty(search)) {
			sb.append('(');
			sb.append(search);
			sb.append(')');
		} else {
			sb.append("@True");
		}

		if (!StringUtil.isEmpty(mapping.getFlags())) {
			sb.append('&');
			sb.append(DesignFlags.buildFlagFormula("$FLAGS", mapping.getFlags()));
		}
		if (!StringUtil.isEmpty(mapping.getFlagsExt())) {
			sb.append('&');
			sb.append(DesignFlags.buildFlagFormula("$FLAGSEXT", mapping.getFlagsExt()));
		}
		if (mapping.getFilterXsp() != null) {
			if (mapping.getFilterXsp().booleanValue()) {
				sb.append(" & @Ends($TITLE; \".xsp\")");
			} else {
				sb.append(" & !@Ends($TITLE; \".xsp\")");
			}
		}
		if (mapping.getAssistFilter() != null) {
			if (mapping.getInclude()) {
				sb.append(" & ($AssistType=");
			} else {
				sb.append(" & !($AssistType=");
			}
			boolean addSep = false;
			for (int assistfilter : mapping.getAssistFilter()) {
				if (addSep)
					sb.append(':');
				sb.append(assistfilter);
				addSep = true;
			}
			sb.append(')');
		}
		//System.out.println("SelectFormula: " + sb.toString());

		nnc.setSelectionFormula(sb.toString());

		switch (mapping.getNoteClass()) {

		case DOCUMENT:
			nnc.setSelectDocuments(true);
			nnc.buildCollection();
			break;
		case FIELD:
			nnc.setSelectSharedFields(true);
			nnc.buildCollection();
			break;
		case FILTER:
			nnc.setSelectDataConnections(true);
			nnc.setSelectOutlines(true);
			nnc.setSelectScriptLibraries(true);
			nnc.setSelectDatabaseScript(true);
			nnc.setSelectAgents(true);
			nnc.setSelectMiscCodeElements(true);
			nnc.setSelectOutlines(true);
			nnc.buildCollection();
			break;
		case FORM:
			nnc.setSelectForms(true);
			nnc.setSelectMiscFormatElements(true);
			nnc.setSelectForms(true);
			nnc.setSelectPages(true);
			nnc.setSelectSubforms(true);
			nnc.setSelectActions(true);
			nnc.setSelectFramesets(true);
			nnc.setSelectImageResources(true);
			nnc.setSelectJavaResources(true);
			nnc.setSelectStylesheetResources(true);
			nnc.buildCollection();
			break;

		case DESIGN:
		case HELP_INDEX:
		case ICON:
		case HELP:
		case INFO:
		case ACL:
			nnc.add(nnc.getAncestorDatabase().getDocumentByID(Integer.toHexString(0xFFFF0000 | mapping.getNoteClass().nativeValue)));
			// NO BuildCollection required!
			break;
		case REPLFORMULA:
			nnc.setSelectReplicationFormulas(true);
			nnc.buildCollection();
			break;
		case VIEW:
			nnc.setSelectViews(true);
			nnc.setSelectFolders(true);
			nnc.setSelectNavigators(true);
			nnc.setSelectMiscIndexElements(true);

			nnc.buildCollection();
			break;

		case UNKNOWN:
		default:
			nnc.selectAllDesignElements(true);
			nnc.buildCollection();
			break;
		}

		return new DesignCollection<T>(nnc, type);
	}

	private NapiDatabaseDesign getNapiDesign() {
		if (!Factory.isNapiPresent())
			return null;
		if (napiDesign_ == null) {
			napiDesign_ = new NapiDatabaseDesign(database_);
		}
		return napiDesign_;
	}

	@Override
	public <T extends DesignBase> DesignCollection<T> getDesignElements(final Class<T> type) {
		return getDesignElements(type, null);
	}

	@Override
	public <T extends DesignBase> DesignCollection<T> getDesignElements(final Class<T> type, final String name) {

		if (getNapiDesign() != null) {
			DesignCollection<T> ret = getNapiDesign().getDesignElements(type, name);
			if (ret != null)
				return ret;
		}
		if (name == null) {
			return searchDesignElements(type, null);

		} else {
			return searchDesignElements(type, String.format("@Explode($TITLE; '|')=\"%s\" ", DominoUtils.escapeForFormulaString(name)));
		}
	}

	@Override
	public <T extends DesignBase> T getDesignElement(final Class<T> type, final String name) {
		return getDesignElement(type, name, false);
	}

	@Deprecated
	public <T extends DesignBase> T getDesignElementByName(final Class<T> type, final String name, final boolean create) {
		return getDesignElement(type, name, create);
	}

	@SuppressWarnings("unchecked")
	public <T extends DesignBase> T getDesignElement(final Class<T> type, final String name, final boolean create) {
		if (DominoUtils.isUnid(name)) {
			Document doc = database_.getDocumentByUNID(name);
			return (T) DesignFactory.fromDocument(doc);
		}
		Iterator<T> elems = getDesignElements(type, name).iterator();
		if (elems.hasNext())
			return elems.next();
		if (!create)
			return null;
		return create(type);
	}

	@Override
	public org.openntf.domino.design.DesignCollection<DesignBase> getDesignElements() {
		if (getNapiDesign() != null) {
			return getNapiDesign().getDesignElements();
		}
		return searchDesignElements(" !@Contains($Flags;{X}) ");
	}

}
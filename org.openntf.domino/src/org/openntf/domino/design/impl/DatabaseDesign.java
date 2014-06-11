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

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.DxlImporter;
import org.openntf.domino.NoteCollection;
import org.openntf.domino.NoteCollection.SelectOption;
import org.openntf.domino.Session;
import org.openntf.domino.utils.DominoUtils;

import com.ibm.commons.util.io.StreamUtil;

/**
 * @author jgallagher
 * 
 */
public class DatabaseDesign implements org.openntf.domino.design.DatabaseDesign, org.openntf.domino.types.DatabaseDescendant {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(DatabaseDesign.class.getName());

	/*
	 * Some handy constant Note IDs for getting specific elements. h/t http://www.nsftools.com/tips/NotesTips.htm#defaultelements
	 */
	private static final String ABOUT_NOTE = "FFFF0002";
	private static final String DEFAULT_FORM = "FFFF0004";
	private static final String DEFAULT_VIEW = "FFFF0008";
	private static final String ICON_NOTE = "FFFF0010";

	// Design collections are pretty impossible to work with using the Java API: the exported DXL is
	// blank and the raw $Collection data isn't readable as an Item
	@SuppressWarnings("unused")
	private static final String DESIGN_COLLECTION = "FFFF0020";

	private static final String ACL_NOTE = "FFFF0040";
	private static final String USING_NOTE = "FFFF0100";
	private static final String REPLICATION_FORMULA = "FFFF0800";

	private final Database database_;

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
		return new DesignView(database_);
	}

	public FileResource createFileResource() {
		return new FileResource(database_);
	}

	public StyleSheet createStyleSheet() {
		return new StyleSheet(database_);
	}

	@Override
	public AboutDocument getAboutDocument(final boolean create) {
		Document doc = database_.getDocumentByID(ABOUT_NOTE);
		if (doc != null) {
			return new AboutDocument(doc);
		} else if (create) {
			try {
				InputStream is = AboutDocument.class.getResourceAsStream("/org/openntf/domino/design/impl/dxl_helpaboutdocument.xml");
				String dxl = StreamUtil.readString(is);
				is.close();
				DxlImporter importer = getAncestorSession().createDxlImporter();
				importer.setDesignImportOption(DxlImporter.DesignImportOption.REPLACE_ELSE_CREATE);
				importer.setReplicaRequiredForReplaceOrUpdate(false);
				importer.importDxl(dxl, database_);
				doc = database_.getDocumentByID(ABOUT_NOTE);
				return new AboutDocument(doc);
			} catch (IOException e) {
				DominoUtils.handleException(e);
			}
		}
		return null;
	}

	@Override
	public ACLNote getACL() {
		return new ACLNote(database_.getDocumentByID(ACL_NOTE));
	}

	@Override
	public DesignForm getDefaultForm() {
		Document formDoc = database_.getDocumentByID(DEFAULT_FORM);
		if (formDoc != null) {
			return new DesignForm(formDoc);
		}
		return null;
	}

	@Override
	public DesignView getDefaultView() {
		Document viewDoc = database_.getDocumentByID(DEFAULT_VIEW);
		if (viewDoc != null) {
			return new DesignView(viewDoc);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.DatabaseDesign#getFacesConfig()
	 */
	@Override
	public FacesConfig getFacesConfig() {
		NoteCollection notes = getNoteCollection(" @Contains($Flags; 'g') & @Explode($TITLE; '|')='WEB-INF/faces-config.xml'",
				EnumSet.of(SelectOption.MISC_FORMAT));
		String noteId = notes.getFirstNoteID();
		if (!noteId.isEmpty()) {
			Document doc = database_.getDocumentByID(noteId);
			return new FacesConfig(doc);
		}
		return null;
	}

	@Override
	public FileResource getFileResource(final String name) {
		if (DominoUtils.isUnid(name)) {
			Document doc = database_.getDocumentByUNID(name);
			return new FileResource(doc);
		} else {
			NoteCollection notes = getNoteCollection(
					String.format(" !@Contains($Flags; '~') & @Contains($Flags; 'g') & @Explode($TITLE; '|')=\"%s\" ",
							DominoUtils.escapeForFormulaString(name)), EnumSet.of(SelectOption.MISC_FORMAT));

			String noteId = notes.getFirstNoteID();
			if (!noteId.isEmpty()) {
				Document doc = database_.getDocumentByID(noteId);
				return new FileResource(doc);
			}
		}
		return null;
	}

	@Override
	public DesignCollection<org.openntf.domino.design.FileResource> getFileResources() {
		NoteCollection notes = getNoteCollection(" !@Contains($Flags; '~') & @Contains($Flags; 'g') ", EnumSet.of(SelectOption.MISC_FORMAT));
		return new DesignCollection<org.openntf.domino.design.FileResource>(notes, FileResource.class);
	}

	@Override
	public FileResource getHiddenFileResource(final String name) {
		if (DominoUtils.isUnid(name)) {
			Document doc = database_.getDocumentByUNID(name);
			return new FileResource(doc);
		} else {
			NoteCollection notes = getNoteCollection(
					String.format(
							" @Contains($Flags; '~') & @Contains($Flags; 'g') & !@Contains($Flags; 'K':';':'[':',') & @Explode($TITLE; '|')=\"%s\" ",
							DominoUtils.escapeForFormulaString(name)), EnumSet.of(SelectOption.MISC_FORMAT));

			String noteId = notes.getFirstNoteID();
			if (!noteId.isEmpty()) {
				Document doc = database_.getDocumentByID(noteId);
				return new FileResource(doc);
			}
		}
		return null;
	}

	@Override
	public DesignCollection<org.openntf.domino.design.FileResource> getHiddenFileResources() {
		NoteCollection notes = getNoteCollection(" @Contains($Flags; '~') & @Contains($Flags; 'g') & !@Contains($Flags; 'K':';':'[':',')",
				EnumSet.of(SelectOption.MISC_FORMAT));
		return new DesignCollection<org.openntf.domino.design.FileResource>(notes, FileResource.class);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.DatabaseDesign#getImageResource(java.lang.String)
	 */
	@Override
	public ImageResource getImageResource(final String name) {
		if (DominoUtils.isUnid(name)) {
			Document doc = database_.getDocumentByUNID(name);
			return new ImageResource(doc);
		} else {
			NoteCollection notes = getNoteCollection(
					String.format(" @Contains($Flags; 'i') & @Explode($TITLE; '|')=\"%s\" ", DominoUtils.escapeForFormulaString(name)),
					EnumSet.of(SelectOption.IMAGE_RESOURCES));

			String noteId = notes.getFirstNoteID();
			if (!noteId.isEmpty()) {
				Document doc = database_.getDocumentByID(noteId);
				return new ImageResource(doc);
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.DatabaseDesign#getImageResources()
	 */
	@Override
	public org.openntf.domino.design.DesignCollection<org.openntf.domino.design.ImageResource> getImageResources() {
		NoteCollection notes = getNoteCollection(" @Contains($Flags; 'i') ", EnumSet.of(SelectOption.IMAGE_RESOURCES));
		return new DesignCollection<org.openntf.domino.design.ImageResource>(notes, ImageResource.class);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.DatabaseDesign#getStyleSheet(java.lang.String)
	 */
	@Override
	public StyleSheet getStyleSheet(final String name) {
		if (DominoUtils.isUnid(name)) {
			Document doc = database_.getDocumentByUNID(name);
			return new StyleSheet(doc);
		} else {
			NoteCollection notes = getNoteCollection(
					String.format(" @Contains($Flags; '=') & @Explode($TITLE; '|')=\"%s\" ", DominoUtils.escapeForFormulaString(name)),
					EnumSet.of(SelectOption.STYLESHEETS));

			String noteId = notes.getFirstNoteID();
			if (!noteId.isEmpty()) {
				Document doc = database_.getDocumentByID(noteId);
				return new StyleSheet(doc);
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.DatabaseDesign#getStyleSheets()
	 */
	@Override
	public org.openntf.domino.design.DesignCollection<org.openntf.domino.design.StyleSheet> getStyleSheets() {
		NoteCollection notes = getNoteCollection(" @Contains($Flags; '=') ", EnumSet.of(SelectOption.STYLESHEETS));
		return new DesignCollection<org.openntf.domino.design.StyleSheet>(notes, StyleSheet.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DatabaseDesign#getJavaResource(java.lang.String)
	 */
	@Override
	public JavaResource getJavaResource(final String name) {
		if (DominoUtils.isUnid(name)) {
			Document doc = database_.getDocumentByUNID(name);
			return new JavaResource(doc);
		} else {
			NoteCollection notes = getNoteCollection(
					String.format(" @Contains($Flags; 'g') & @Contains($Flags; '[') & @Explode($TITLE; '|')=\"%s\" ",
							DominoUtils.escapeForFormulaString(name)), EnumSet.of(SelectOption.MISC_FORMAT));

			String noteId = notes.getFirstNoteID();
			if (!noteId.isEmpty()) {
				Document doc = database_.getDocumentByID(noteId);
				return new JavaResource(doc);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DatabaseDesign#getJavaResources()
	 */
	@Override
	public DesignCollection<org.openntf.domino.design.JavaResource> getJavaResources() {
		NoteCollection notes = getNoteCollection(" @Contains($Flags; 'g') & @Contains($Flags; '[') ", EnumSet.of(SelectOption.MISC_FORMAT));
		return new DesignCollection<org.openntf.domino.design.JavaResource>(notes, JavaResource.class);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.DatabaseDesign#getJavaResourceClassNames()
	 */
	@Override
	public SortedSet<String> getJavaResourceClassNames() {
		SortedSet<String> result = new TreeSet<String>();
		NoteCollection notes = getNoteCollection(" @Contains($Flags; 'g') & @Contains($Flags; '[') ", EnumSet.of(SelectOption.MISC_FORMAT));
		for (String noteId : notes) {
			Document doc = getAncestorDatabase().getDocumentByID(noteId);
			for (Object pathName : doc.getItemValue("$ClassIndexItem")) {
				if (pathName != null && String.valueOf(pathName).startsWith("WEB-INF/classes/")) {
					result.add(DominoUtils.filePathToJavaBinaryName(((String) pathName).substring(16), "/"));
				}
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DatabaseDesign#getXPage(java.lang.String)
	 */
	@Override
	public XPage getXPage(final String name) {
		if (DominoUtils.isUnid(name)) {
			Document doc = database_.getDocumentByUNID(name);
			return new XPage(doc);
		} else {
			NoteCollection notes = getNoteCollection(
					String.format(" @Contains($Flags; 'g') & @Contains($Flags; 'K') & @Explode($TITLE; '|')=\"%s\" ",
							DominoUtils.escapeForFormulaString(name)), EnumSet.of(SelectOption.MISC_FORMAT));

			String noteId = notes.getFirstNoteID();
			if (!noteId.isEmpty()) {
				Document doc = database_.getDocumentByID(noteId);
				return new XPage(doc);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DatabaseDesign#getXPages()
	 */
	@Override
	public DesignCollection<org.openntf.domino.design.XPage> getXPages() {
		NoteCollection notes = getNoteCollection(" @Contains($Flags; 'g') & @Contains($Flags; 'K') ", EnumSet.of(SelectOption.MISC_FORMAT));
		return new DesignCollection<org.openntf.domino.design.XPage>(notes, XPage.class);
	}

	public JarResource getJarResource(final String name) {
		if (DominoUtils.isUnid(name)) {
			Document doc = database_.getDocumentByUNID(name);
			return new JarResource(doc);
		} else {
			NoteCollection notes = getNoteCollection(
					String.format(" @Contains($Flags; 'g') & @Contains($Flags; ',') & @Explode($TITLE; '|')=\"%s\" ",
							DominoUtils.escapeForFormulaString(name)), EnumSet.of(SelectOption.MISC_FORMAT));

			String noteId = notes.getFirstNoteID();
			if (!noteId.isEmpty()) {
				Document doc = database_.getDocumentByID(noteId);
				return new JarResource(doc);
			}
		}
		return null;
	}

	public DesignCollection<org.openntf.domino.design.JarResource> getJarResources() {
		NoteCollection notes = getNoteCollection(" @Contains($Flags; 'g') & @Contains($Flags; ',') ", EnumSet.of(SelectOption.MISC_FORMAT));
		return new DesignCollection<org.openntf.domino.design.JarResource>(notes, JarResource.class);
	}

	@Override
	public FileResource getAnyFileResource(final String name) {
		if (DominoUtils.isUnid(name)) {
			Document doc = database_.getDocumentByUNID(name);
			return new FileResource(doc);
		} else {
			NoteCollection notes = getNoteCollection(
					String.format(" @Contains($Flags; 'g') & @Explode($TITLE; '|')=\"%s\" ", DominoUtils.escapeForFormulaString(name)),
					EnumSet.of(SelectOption.MISC_FORMAT));

			String noteId = notes.getFirstNoteID();
			if (!noteId.isEmpty()) {
				Document doc = database_.getDocumentByID(noteId);
				return new FileResource(doc);
			}
		}
		return null;
	}

	@Override
	public DesignForm getForm(final String name) {
		if (DominoUtils.isUnid(name)) {
			Document doc = database_.getDocumentByUNID(name);
			return new DesignForm(doc);
		} else {
			// TODO Check if this returns subforms
			NoteCollection notes = getNoteCollection(
					String.format(" @Explode($TITLE; '|')=\"%s\" ", DominoUtils.escapeForFormulaString(name)),
					EnumSet.of(SelectOption.FORMS));

			String noteId = notes.getFirstNoteID();
			if (!noteId.isEmpty()) {
				Document doc = database_.getDocumentByID(noteId);
				return new DesignForm(doc);
			}
		}
		return null;
	}

	@Override
	public DesignCollection<org.openntf.domino.design.DesignForm> getForms() {
		NoteCollection notes = getNoteCollection(" @All ", EnumSet.of(SelectOption.FORMS));
		return new DesignCollection<org.openntf.domino.design.DesignForm>(notes, DesignForm.class);
	}

	@Override
	public IconNote getIconNote() {
		return new IconNote(database_.getDocumentByID(ICON_NOTE));
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
		if (DominoUtils.isUnid(name)) {
			Document doc = database_.getDocumentByUNID(name);
			return new DesignView(doc);
		} else {
			// TODO Check if this returns folders
			NoteCollection notes = getNoteCollection(
					String.format(" @Explode($TITLE; '|')=\"%s\" ", DominoUtils.escapeForFormulaString(name)),
					EnumSet.of(SelectOption.VIEWS));

			String noteId = notes.getFirstNoteID();
			if (!noteId.isEmpty()) {
				Document doc = database_.getDocumentByID(noteId);
				return new DesignView(doc);
			}
		}
		return null;
	}

	@Override
	public DesignCollection<org.openntf.domino.design.DesignView> getViews() {
		NoteCollection notes = getNoteCollection(" @All ", EnumSet.of(SelectOption.VIEWS));
		return new DesignCollection<org.openntf.domino.design.DesignView>(notes, DesignView.class);
	}

	@Override
	public Folder getFolder(final String name) {
		if (DominoUtils.isUnid(name)) {
			Document doc = database_.getDocumentByUNID(name);
			return new Folder(doc);
		} else {
			// TODO Check if this returns views
			NoteCollection notes = getNoteCollection(
					String.format(" @Explode($TITLE; '|')=\"%s\" ", DominoUtils.escapeForFormulaString(name)),
					EnumSet.of(SelectOption.VIEWS));

			String noteId = notes.getFirstNoteID();
			if (!noteId.isEmpty()) {
				Document doc = database_.getDocumentByID(noteId);
				return new Folder(doc);
			}
		}
		return null;
	}

	@Override
	public DesignCollection<org.openntf.domino.design.Folder> getFolders() {
		NoteCollection notes = getNoteCollection(" @All ", EnumSet.of(SelectOption.VIEWS));
		return new DesignCollection<org.openntf.domino.design.Folder>(notes, Folder.class);
	}

	public DesignCollection<org.openntf.domino.design.JavaScriptLibrary> getJavaScriptLibraries() {
		NoteCollection notes = getNoteCollection(" @Contains($Flags; 'j') ", EnumSet.of(SelectOption.SCRIPT_LIBRARIES));
		return new DesignCollection<org.openntf.domino.design.JavaScriptLibrary>(notes, JavaScriptLibrary.class);
	}

	public JavaScriptLibrary getJavaScriptLibrary(final String name) {
		if (DominoUtils.isUnid(name)) {
			Document doc = database_.getDocumentByUNID(name);
			return new JavaScriptLibrary(doc);
		} else {
			NoteCollection notes = getNoteCollection(
					String.format(" @Contains($Flags; 'j') & @Explode($TITLE; '|')=\"%s\" ", DominoUtils.escapeForFormulaString(name)),
					EnumSet.of(SelectOption.SCRIPT_LIBRARIES));

			String noteId = notes.getFirstNoteID();
			if (!noteId.isEmpty()) {
				Document doc = database_.getDocumentByID(noteId);
				return new JavaScriptLibrary(doc);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DatabaseDesign#getDatabaseClassLoader()
	 */
	@Override
	public ClassLoader getDatabaseClassLoader(final ClassLoader parent) {
		return new DatabaseClassLoader(this, parent, true, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DatabaseDesign#getDatabaseClassLoader(java.lang.ClassLoader, boolean)
	 */
	@Override
	public ClassLoader getDatabaseClassLoader(final ClassLoader parent, final boolean includeJars) {
		return new DatabaseClassLoader(this, parent, includeJars, false);
	}

	public ClassLoader getDatabaseClassLoader(final ClassLoader parent, final boolean includeJars, final boolean includeLibraries) {
		return new DatabaseClassLoader(this, parent, includeJars, includeLibraries);
	}

	protected NoteCollection getNoteCollection(final String selectionFormula, final Set<SelectOption> options) {
		NoteCollection notes = database_.createNoteCollection(false);
		notes.setSelectOptions(options);
		notes.setSelectionFormula(selectionFormula);
		notes.buildCollection();
		return notes;
	}

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
}
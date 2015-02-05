/*
 * Copyright 2015
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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.DxlExporter;
import org.openntf.domino.DxlImporter;
import org.openntf.domino.Stream;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.design.DatabaseDesign;
import org.openntf.domino.design.DesignBase;
import org.openntf.domino.design.DesignCollection;
import org.openntf.domino.design.SyncObject;
import org.openntf.domino.design.impl.OnDiskFile.State;
import org.openntf.domino.utils.DominoUtils;
import org.xml.sax.InputSource;

import com.ibm.commons.util.StringUtil;

/**
 * 
 * @author Alexander Wagner, FOCONIS AG
 * 
 */
public class OnDiskProject {

	public enum SyncDirection {
		EXPORT, IMPORT, SYNC;
	}

	static TransformerFactory tFactory = TransformerFactory.newInstance();

	public final static Transformer ImportTransformer = createImportTransformer();
	public final static Transformer ExportTransformer = createExportTransformer();

	private static final String DOC_DIR = "Documents";
	public final static String NOTEINFO_UNID = "noteinfo unid=\"";

	private File diskDir_;
	private Database db;
	private SyncDirection direction;

	private File timeStampsDesign_;
	private Map<String, OnDiskFile> files_;

	private File docDir_;
	Map<String, Long> lastModifiedMapDocs_ = null;
	private File timeStampsDocs_;

	private boolean gitFriendly = false;
	private boolean exportMetadata = true;

	public OnDiskProject(final File diskDir, final Database db, final SyncDirection direction) {
		diskDir_ = diskDir;
		this.db = db;
		this.direction = direction;
		//deserialize and update map
		prepareMap();
	}

	public static Transformer createImportTransformer() {
		Transformer tr = null;
		try {
			tr = tFactory.newTransformer(new StreamSource(OnDiskProject.class.getResourceAsStream("importFilter.xslt")));
		} catch (TransformerConfigurationException e) {
			DominoUtils.handleException(e);
		}
		return tr;
	}

	public static Transformer createExportTransformer() {
		Transformer tr = null;
		try {
			tr = tFactory.newTransformer(new StreamSource(OnDiskProject.class.getResourceAsStream("exportFilter.xslt")));
		} catch (TransformerConfigurationException e) {
			DominoUtils.handleException(e);
		}
		return tr;
	}

	protected void prepareMap() {
		try {
			timeStampsDesign_ = new File(diskDir_, ".timeStampsDesign_" + db.getReplicaID());

			if (timeStampsDesign_.exists()) {
				//deserialize timeStamp of design file map
				InputStream fis = new FileInputStream(timeStampsDesign_);
				InputStream bisr = new BufferedInputStream(fis);
				ObjectInput ois = new ObjectInputStream(bisr);
				files_ = (Map<String, OnDiskFile>) ois.readObject();
				ois.close();
			} else {
				files_ = new HashMap<String, OnDiskFile>();
			}
		} catch (IOException e) {
			DominoUtils.handleException(e);
		} catch (ClassNotFoundException e) {
			DominoUtils.handleException(e);
		}
		//update map
		updateMapRecursive(diskDir_);
	}

	/**
	 * Synchronizes all Documents in the view of viewName in the NSF with the {@link OnDiskProject}.
	 * 
	 * @param viewName
	 */
	public void syncDocs(final String viewName) {
		try {
			setUpDocSync();
			try {
				docDir_ = new File(this.diskDir_, DOC_DIR);

				if (!docDir_.exists()) {
					docDir_.mkdir();
				}

				Set<String> exportedFiles = new HashSet<String>();
				exportDocs(viewName, exportedFiles);
				importDocs(exportedFiles);
			} finally {
				tearDownDocSync();
			}
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
	}

	/**
	 * Synchronizes all DesignElements of the NSF with the {@link OnDiskProject}.
	 */
	public void syncDesign() {
		DatabaseDesign design = db.getDesign();
		DesignCollection<DesignBase> elems = design.getDesignElements(" !@Contains($Flags;{X}) & !@Begins($TITLE;{WEB-INF/classes}) ");

		System.out.println("Start Design Sync");

		for (DesignBase elem : elems) {
			sync(elem);
		}

		for (OnDiskFile odf : getUnprocessedFiles()) {
			sync(odf);
		}

		saveMap();
		System.out.println("End Design Sync");
	}

	/**
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	protected void setUpDocSync() throws IOException, ClassNotFoundException {
		this.timeStampsDocs_ = new File(diskDir_, ".timeStampsDocs_" + db.getReplicaID());

		lastModifiedMapDocs_ = new HashMap<String, Long>();
		if (timeStampsDocs_.exists()) {
			//deserialize doc timestamps map
			InputStream file = new FileInputStream(timeStampsDocs_);
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream(buffer);
			lastModifiedMapDocs_ = (Map<String, Long>) input.readObject();
			input.close();
		}

	}

	/**
	 * 
	 * @throws IOException
	 */
	protected void tearDownDocSync() throws IOException {
		//serialize doc timestamps map
		OutputStream file = new FileOutputStream(this.timeStampsDocs_);
		OutputStream buffer = new BufferedOutputStream(file);
		ObjectOutput output = new ObjectOutputStream(buffer);
		output.writeObject(lastModifiedMapDocs_);
		output.close();
	}

	protected void exportDocs(final String viewName, final Set<String> exportedFiles) throws TransformerConfigurationException,
			UnsupportedEncodingException {
		System.out.println("Start Doc Export");

		View view = db.getView(viewName);

		DxlExporter dxlExporter = db.getAncestorSession().createDxlExporter();

		//configure DxlExporter
		dxlExporter.setForceNoteFormat(false);
		dxlExporter.setExitOnFirstFatalError(false);
		dxlExporter.setDoctypeSYSTEM("");
		dxlExporter.setRichTextOption(DxlExporter.RichTextOption.DXL);
		dxlExporter.setMIMEOption(DxlExporter.MIMEOption.RAW);

		//iterate over Documents in view
		for (ViewEntry entry : view.getAllEntries()) {
			if (entry.isDocument()) {
				Document doc = entry.getDocument();

				Vector<?> colValues = entry.getColumnValues();
				File subDir = new File(docDir_, URLEncoder.encode((String) colValues.get(0), "UTF-8"));

				if (!subDir.exists()) {
					subDir.mkdir();
				}

				File file = new File(subDir, URLEncoder.encode((String) colValues.get(1), "UTF-8") + ".xml");
				exportedFiles.add(file.getAbsolutePath());

				exportOneDoc(doc, file, dxlExporter);
			}
		}
		System.out.println("End Doc Export");
	}

	/**
	 * 
	 * @param doc
	 * @param file
	 * @param dxlExporter
	 * @param tr
	 */
	protected void exportOneDoc(final Document doc, final File file, final DxlExporter dxlExporter) {

		long lastModifiedDoc = doc.getLastModifiedDate().getTime();

		long lastModifiedSync = 0;
		if (lastModifiedMapDocs_.containsKey(file.getAbsolutePath())) {
			lastModifiedSync = lastModifiedMapDocs_.get(file.getAbsolutePath());
		}

		Stream stream = db.getAncestorSession().createStream();

		//Case "Force-Export", "Sync-Export" -> export Document from NSF to file in ODP
		if (this.direction == SyncDirection.EXPORT || (this.direction != SyncDirection.IMPORT && lastModifiedDoc > lastModifiedSync)) {
			stream.open(file.getAbsolutePath(), "UTF-8");
			stream.truncate();
			stream.writeText(transformXslt(dxlExporter.exportDxl(doc)));
			stream.close();

			file.setLastModified(lastModifiedDoc);

			System.out.println("Update file " + file.getAbsolutePath());

			lastModifiedMapDocs_.put(file.getAbsolutePath(), lastModifiedDoc);

			//Case "File was deleted in ODP" -> delete Document in NSF
		} else {
			if (!file.exists()) {
				System.out.println("delete document " + doc.getUniversalID());
				lastModifiedMapDocs_.remove(file.getAbsoluteFile());
				doc.removePermanently(true);
			}
		}

	}

	protected String transformXslt(final String dxl) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			docFactory.setNamespaceAware(true);
			InputSource input = new InputSource(new StringReader(dxl));
			org.w3c.dom.Document document = docFactory.newDocumentBuilder().parse(input);

			DOMSource source = new DOMSource(document.getDocumentElement());
			StringWriter sw = new StringWriter();
			StreamResult result = new StreamResult(sw);

			ExportTransformer.transform(source, result);

			return sw.toString();
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/**
	 * 
	 * @param docDir
	 * @param exportedFiles
	 */
	protected void importDocs(final Set<String> exportedFiles) {
		System.out.println("Start Doc Import");

		DxlImporter dxlImporter = db.getAncestorSession().createDxlImporter();

		//configure DxlImporter
		dxlImporter.setDocumentImportOption(DxlImporter.DocumentImportOption.REPLACE_ELSE_IGNORE);
		dxlImporter.setExitOnFirstFatalError(false);
		dxlImporter.setCompileLotusScript(false);
		dxlImporter.setReplicaRequiredForReplaceOrUpdate(false);

		//iterate over Files in ODP
		for (File subDir : docDir_.listFiles()) {
			if (subDir.isDirectory()) {
				for (File file : subDir.listFiles()) {
					importOneDoc(file, dxlImporter, exportedFiles);
				}
			}
		}
		System.out.println("End Doc Import");
	}

	/**
	 * 
	 * @param file
	 * @param dxlImporter
	 * @param exportedFiles
	 */
	protected void importOneDoc(final File file, final DxlImporter dxlImporter, final Set<String> exportedFiles) {
		long lastModifiedFile = file.lastModified();

		long lastModifiedSync = 0;
		if (lastModifiedMapDocs_.containsKey(file.getAbsolutePath())) {
			lastModifiedSync = lastModifiedMapDocs_.get(file.getAbsolutePath());
		}

		Stream stream = db.getAncestorSession().createStream();

		//Case "Force-Import", "Sync-Import" -> import Document in NSF from ODP-File
		if (this.direction == SyncDirection.IMPORT || (this.direction != SyncDirection.EXPORT && lastModifiedFile > lastModifiedSync)) {
			stream.open(file.getAbsolutePath(), "UTF-8");

			String unid = "";
			for (int i = 0; i < 10; i++) {
				String tmp = stream.readText(Stream.STMREAD_LINE, Stream.EOL_ANY);
				if (tmp.contains(NOTEINFO_UNID)) {
					tmp = tmp.substring(tmp.indexOf(NOTEINFO_UNID) + NOTEINFO_UNID.length());
					unid = tmp.substring(0, tmp.indexOf("\""));
					break;
				}
			}
			stream.close();

			Document doc = db.getDocumentByUNID(unid);
			if (doc == null) {
				doc = db.createDocument();
				doc.setUniversalID(unid);
				doc.save();
			}

			stream.open(file.getAbsolutePath(), "UTF-8");
			stream.setPosition(0);
			dxlImporter.importDxl(stream, db);
			stream.close();

			System.out.println("Update doc " + file.getAbsolutePath());

			lastModifiedMapDocs_.put(file.getAbsolutePath(), lastModifiedFile);

			//Case "Document was deleted in NSF" -> delete file in ODP
		} else {

			if (!exportedFiles.contains(file.getAbsolutePath())) {
				System.out.println("Deleting file " + file.getAbsolutePath());
				lastModifiedMapDocs_.remove(file.getAbsolutePath());
				file.delete();
			}
		}

	}

	public void doImport(final org.openntf.domino.design.DesignBase elem_, final File file) throws IOException {
		AbstractDesignBase elem = (AbstractDesignBase) elem_;
		String name = elem.getName();
		String unid = elem.getUniversalID();
		if (exportMetadata && elem instanceof HasMetadata) {
			File metaFile = new File(file.getAbsolutePath() + ".metadata");
			if (metaFile.exists()) {
				((HasMetadata) elem).readOnDiskMeta(metaFile);
			}
		}
		if (elem instanceof HasConfig) {
			File configFile = new File(file.getAbsolutePath() + "-config");
			((HasConfig) elem).readOnDiskConfig(configFile);
		}
		elem.readOnDiskFile(file);
		elem.setName(name);
		if (StringUtil.isNotEmpty(unid)) {
			elem.setUniversalId(unid);
		}
		elem.save();
	}

	public void doExport(final org.openntf.domino.design.DesignBase elem_) throws IOException {
		AbstractDesignBase elem = (AbstractDesignBase) elem_;
		String odp = elem.getOnDiskPath();
		if (StringUtil.isEmpty(odp)) {
			odp = elem.getNoteID() + ".note";
		}
		File file = new File(diskDir_, odp);

		file.getParentFile().mkdirs(); // ensure the path exists
		elem.writeOnDiskFile(file);
		if (exportMetadata && elem instanceof HasMetadata) {
			File metaFile = new File(file.getAbsolutePath() + ".metadata");
			((HasMetadata) elem).writeOnDiskMeta(metaFile);
		}
		if (elem instanceof HasConfig) {
			File configFile = new File(file.getAbsolutePath() + "-config");
			((HasConfig) elem).writeOnDiskConfig(configFile);
		}

	}

	protected void updateMapRecursive(final File root) {

		if (root.listFiles() != null) {
			for (File file : root.listFiles()) {
				if (file.isDirectory()) {
					if (!isDocDir(file)) {
						updateMapRecursive(file);
					}
				} else if (!isTimeStampsFile(file) && !isMetadataFile(file) && !isConfigFile(file)) {
					OnDiskFile odf = new OnDiskFile(diskDir_, file);
					if (!files_.containsKey(odf.getFullName().toLowerCase())) {
						if (direction != SyncDirection.EXPORT) {
							odf.setState(State.WAS_CREATED);
							files_.put(odf.getFullName().toLowerCase(), odf);
						}
					} else {
						if (direction == SyncDirection.SYNC) {
							files_.get(odf.getFullName().toLowerCase()).setState(State.SYNC);
						} else if (direction == SyncDirection.EXPORT) {
							files_.get(odf.getFullName().toLowerCase()).setState(State.EXPORT);
						} else if (direction == SyncDirection.IMPORT) {
							files_.get(odf.getFullName().toLowerCase()).setState(State.IMPORT);
						}
					}
				}
			}
		}
	}

	protected boolean isMetadataFile(final File file) {
		return file.getName().endsWith(".metadata");
	}

	protected boolean isConfigFile(final File file) {
		return ODPMapping.CUSTOM_CONTROL.getFolder().equals(file.getParentFile().getName()) && file.getName().endsWith(".xsp-config");
	}

	protected boolean isTimeStampsFile(final File file) {
		return file.getName().startsWith(".timeStamps");
	}

	protected boolean isDocDir(final File file) {
		return file.getName().equals(DOC_DIR);
	}

	/**
	 * Synchronizes a SyncObject which is a DesignBase or a OnDiskFile.
	 * 
	 * @param syncObject
	 */
	public void sync(final SyncObject syncObject) {
		OnDiskFile odf;
		AbstractDesignBase elem;

		if (syncObject instanceof AbstractDesignBase) {
			elem = (AbstractDesignBase) syncObject;
			odf = find(elem);
		} else if (syncObject instanceof OnDiskFile) {
			odf = (OnDiskFile) syncObject;
			try {
				System.out.println("Creating " + odf.getFullName());
				Constructor<?> ctor = odf.getImplementingClass().getDeclaredConstructor(Database.class);
				elem = (AbstractDesignBase) ctor.newInstance(db);
				elem.setOnDiskName(odf.getName());
			} catch (IllegalAccessException e) {
				DominoUtils.handleException(e);
				return;
			} catch (InstantiationException e) {
				DominoUtils.handleException(e);
				return;
			} catch (SecurityException e) {
				DominoUtils.handleException(e);
				return;
			} catch (NoSuchMethodException e) {
				DominoUtils.handleException(e);
				return;
			} catch (IllegalArgumentException e) {
				DominoUtils.handleException(e);
				return;
			} catch (InvocationTargetException e) {
				DominoUtils.handleException(e);
				return;
			}
		} else {
			throw new IllegalArgumentException("Cannot sync Object of class " + syncObject.getClass().getName());
		}

		//OnDiskFile not found in TimeStamp Map
		if (odf == null) {
			File file = new File(diskDir_, elem.getOnDiskPath());
			odf = new OnDiskFile(diskDir_, file);

			//set state "CREATE", if direction is not Force-Import
			if (this.direction != SyncDirection.IMPORT) {
				odf.setState(State.CREATE);
				files_.put(odf.getFullName().toLowerCase(), odf);
			} else {
				odf.setState(State.WAS_DELETED);
			}
		} else {
			//OnDiskFile is an element of getUnprocessedFiles(), if state == null
			if (odf.getState() == null) {
				//set State "WAS_DELETED", if direction is not Force-Export
				if (this.direction != SyncDirection.EXPORT) {
					odf.setState(State.WAS_DELETED);
				} else {
					odf.setState(State.EXPORT);
				}
			}
		}

		File file = odf.getFile();
		file.getParentFile().mkdirs(); // ensure the path exists

		long lastModifiedDoc = elem.getLastModified() == null ? 0 : elem.getLastModified().getTime();
		long lastModifiedFile = file.lastModified();
		long lastModifiedSync = odf.getTimeStamp();

		//Case "DesignElement was deleted in NSF" -> "File has to be deleted in ODP"
		if (odf.getState() == State.DELETE) {
			System.out.println("Deleting file " + file.getAbsolutePath());
			files_.remove(odf.getFullName().toLowerCase());
			if (elem instanceof HasMetadata) {
				File metaFile = new File(file.getAbsoluteFile() + ".metadata");
				metaFile.delete();
			}
			if (elem instanceof HasConfig) {
				File configFile = new File(file.getAbsoluteFile() + "-config");
				configFile.delete();
			}
			file.delete();

			//Case "File was deleted in ODP" -> "DesignElement has to be deleted in NSF"
		} else if (odf.getState() == State.WAS_DELETED) {
			System.out.println("Deleting design element " + odf.getFullName());
			files_.remove(odf.getFullName().toLowerCase());
			db.getDocumentByUNID(elem.getUniversalID()).removePermanently(true);

			//Case "DesignElement was created in NSF", "Force-Export", "Sync-Export" -> "File has to be created/updated in ODP"
		} else if (odf.getState() == State.CREATE || odf.getState() == State.EXPORT || odf.getState() == State.SYNC
				&& lastModifiedDoc > lastModifiedSync) {

			System.out.println("Exporting " + file.getAbsolutePath());
			try {
				doExport(elem);
			} catch (FileNotFoundException fnfe) {
				System.err.println("Catched " + fnfe.getMessage());
			} catch (IOException e) {
				DominoUtils.handleException(e);
			}
			odf.setTimeStamp(lastModifiedDoc);

			//Case "File was created in ODP", "Force-Import", "Sync-Import" -> "DesignElement has to be created/updated in NSF"
		} else if (odf.getState() == State.WAS_CREATED || odf.getState() == State.IMPORT || odf.getState() == State.SYNC
				&& lastModifiedFile > lastModifiedSync) {
			System.out.println("Importing " + file.getAbsolutePath());
			try {
				doImport(elem, file);
			} catch (IOException e) {
				DominoUtils.handleException(e);
			}
			odf.setTimeStamp(elem.getLastModified().getTime());
		}

		odf.setProcessed(true);

	}

	/**
	 * Searches the corresponding OnDiskFile to the DesignElement.
	 * 
	 * @param elem
	 *            the DesignElement
	 * @return the corresponding OnDiskFile
	 */
	protected OnDiskFile find(final AbstractDesignBase elem) {
		return files_.get((elem.getClass().getName() + ":" + elem.getOnDiskName()).toLowerCase());
	}

	/**
	 * Serializes the map of the design file time stamps.
	 */
	public void saveMap() {
		try {
			//serialize TimeStamp Map of design files
			OutputStream fos = new FileOutputStream(timeStampsDesign_);
			OutputStream bos = new BufferedOutputStream(fos);
			ObjectOutput oos = new ObjectOutputStream(bos);
			oos.writeObject(files_);
			oos.close();
		} catch (IOException e) {
			DominoUtils.handleException(e);
		}
	}

	/**
	 * Returns all OnDiskFiles that don't have a corresponding DesignElement in the NSF (anymore/not yet).
	 * 
	 * @return a list of unprocessed OnDiskFiles.
	 */
	public List<OnDiskFile> getUnprocessedFiles() {
		List<OnDiskFile> resList = new ArrayList<OnDiskFile>();
		for (OnDiskFile file : files_.values()) {
			if (!file.isProcessed()) {
				resList.add(file);
				if (file.getState() != State.WAS_CREATED && file.getState() != State.IMPORT) {
					file.setState(State.DELETE);
				}
			}
		}
		return resList;
	}
}

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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.openntf.domino.exceptions.OpenNTFNotesException;
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

	public enum LogLevel {
		SEVERE, INFO
	}

	public enum WorkingState {
		PREPARING, DESIGNSYNC, DOCEXPORT, DOCIMPORT
	}

	static TransformerFactory tFactory = TransformerFactory.newInstance();

	public static Transformer ImportTransformer = createImportTransformer();
	public static Transformer ExportTransformer = createExportTransformer();

	public static final String TIMESTAMPS_DESIGN_PREFIX = ".timeStampsDesign_";
	public static final String TIMESTAMPS_DOCS_PREFIX = ".timeStampsDocs_";
	public static final String METADATA_SUFFIX = ".metadata";
	public static final String XSP_CONFIG_SUFFIX = ".xsp-config";
	public static final String CONFIG_SUFFIX = "-config";

	private static final int TIMESTAMPS_OFFSET = 15000;

	private static final String DOC_DIR = "Documents";
	private static final String NOTEINFO_UNID = "noteinfo unid=\"";
	private static final String LOG_DIR = "Logs";
	private static final String LOG_FILE_PREFIX = "log_";

	private File diskDir_;
	private Database db;
	private SyncDirection direction;

	private File timeStampsDesign_;
	private Map<String, OnDiskFile> files_;

	private PrintStream logStream_;
	private File logDir_;
	private File logFile_;

	private File docDir_;
	private Map<String, Long> lastModifiedMapDocs_ = null;
	private File timeStampsDocs_;

	private boolean gitFriendly = false;
	private boolean exportMetadata = true;
	private boolean enableLog = true;

	private WorkingState workingState = WorkingState.PREPARING;
	private String currentFile = "";
	private int workingIdx = 0;

	private int total = 0;

	private int amountDocsInSync = 0;
	private int amountDocsExported = 0;
	private int amountDocsImported = 0;
	private int amountDocsDeletedODP = 0;
	private int amountDocsDeletedNSF = 0;

	private int amountDesignElementsInSync = 0;
	private int amountDesignElementsExported = 0;
	private int amountDesignElementsImported = 0;
	private int amountDesignElementsDeletedODP = 0;
	private int amountDesignElementsDeletedNSF = 0;
	private int amountDesignElementsFailed = 0;

	public OnDiskProject(final File diskDir, final Database db, final SyncDirection direction) {
		diskDir_ = diskDir;
		this.db = db;
		this.direction = direction;
		//deserialize and update map
		prepareLogger();
		prepareMap();
	}

	protected void prepareLogger() {
		logDir_ = new File(this.diskDir_, LOG_DIR);
		if (!logDir_.exists()) {
			logDir_.mkdir();
		}
		try {
			Date now = new Date();
			String dateString = new SimpleDateFormat("yyyyMMdd_HHmmss").format(now);
			logFile_ = new File(logDir_, LOG_FILE_PREFIX + dateString + ".txt");
			logFile_.createNewFile();
			logStream_ = new PrintStream(logFile_);
		} catch (IOException e) {
			DominoUtils.handleException(e);
		}
	}

	public static Transformer createTransformer(final InputStream stream) {
		try {
			return tFactory.newTransformer(new StreamSource(stream));
		} catch (TransformerConfigurationException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	public static Transformer createImportTransformer() {
		if (ImportTransformer == null) {
			ImportTransformer = createTransformer(OnDiskProject.class.getResourceAsStream("importFilter.xslt"));
		}
		return ImportTransformer;
	}

	public static Transformer createExportTransformer() {
		if (ExportTransformer == null) {
			ExportTransformer = createTransformer(OnDiskProject.class.getResourceAsStream("exportFilter.xslt"));
		}
		return ExportTransformer;
	}

	@SuppressWarnings("unchecked")
	protected void prepareMap() {
		try {
			timeStampsDesign_ = new File(diskDir_, TIMESTAMPS_DESIGN_PREFIX + db.getReplicaID());

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
	 *            The name of the view.
	 */
	public void syncDocs(final String viewName) {
		try {
			setUpDocSync();
			try {
				docDir_ = new File(this.diskDir_, DOC_DIR);

				if (!docDir_.exists()) {
					docDir_.mkdir();
				}

				View view = db.getView(viewName);
				if (view == null) {
					return;
				}
				Set<String> exportedFiles = new HashSet<String>();
				exportDocs(view, exportedFiles);
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
	 * 
	 */
	public void syncDesign() {

		DatabaseDesign design = db.getDesign();
		DesignCollection<DesignBase> elems = design.getDesignElements(" !@Contains($Flags;{X}) & !@Begins($TITLE;{WEB-INF/classes}) ");
		int totalODP = total;
		int totalNSF = elems.getCount();
		total += totalNSF;

		workingState = WorkingState.DESIGNSYNC;

		try {
			log(LogLevel.INFO, "Start Design Sync, synchronizing " + totalNSF + " (NSF) Design Elements with " + totalODP + " (ODP) Files");

			for (DesignBase elem : elems) {
				sync(elem);
			}

			for (OnDiskFile odf : getUnprocessedFiles()) {
				sync(odf);
			}

			saveMap();
			log(LogLevel.INFO, "End Design Sync, exported: " + amountDesignElementsExported + ", imported: " + amountDesignElementsImported
					+ ", in sync: " + amountDesignElementsInSync + ", deleted (ODP): " + amountDesignElementsDeletedODP
					+ ", deleted (NSF): " + amountDesignElementsDeletedNSF + ", failed: " + amountDesignElementsFailed);
		} catch (IOException e) {
			DominoUtils.handleException(e);
		}
	}

	/**
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	protected void setUpDocSync() throws IOException, ClassNotFoundException {
		this.timeStampsDocs_ = new File(diskDir_, TIMESTAMPS_DOCS_PREFIX + db.getReplicaID());

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

	protected void exportDocs(final View view, final Set<String> exportedFiles) throws TransformerConfigurationException, IOException {
		total = view.getAllEntries().getCount();
		workingIdx = 0;
		workingState = WorkingState.DOCEXPORT;

		log(LogLevel.INFO, "Start Doc Export, synchronizing " + total + " documents");

		DxlExporter dxlExporter = db.getAncestorSession().createDxlExporter();

		//configure DxlExporter
		dxlExporter.setForceNoteFormat(false);
		dxlExporter.setExitOnFirstFatalError(false);
		dxlExporter.setDoctypeSYSTEM("");
		dxlExporter.setRichTextOption(DxlExporter.RichTextOption.DXL);
		dxlExporter.setMIMEOption(DxlExporter.MIMEOption.RAW);

		//iterate over Documents in view
		for (ViewEntry entry : view.getAllEntries()) {
			workingIdx++;
			if (entry.isDocument()) {
				Document doc = entry.getDocument();

				Vector<?> colValues = entry.getColumnValues();
				File subDir = new File(docDir_, URLEncoder.encode((String) colValues.get(0), "UTF-8"));

				if (!subDir.exists()) {
					subDir.mkdir();
				}

				File file = new File(subDir, URLEncoder.encode((String) colValues.get(1), "UTF-8") + ".xml");
				currentFile = file.getAbsolutePath();
				exportedFiles.add(file.getAbsolutePath());
				exportOneDoc(doc, file, dxlExporter);
			}
		}
		log(LogLevel.INFO, "End Doc Export, exported: " + amountDocsExported + ", in sync: " + amountDocsInSync + ", deleted (NSF): "
				+ amountDocsDeletedNSF);
	}

	/**
	 * 
	 * @param doc
	 * @param file
	 * @param dxlExporter
	 * @param tr
	 * @throws IOException
	 */
	protected void exportOneDoc(final Document doc, final File file, final DxlExporter dxlExporter) throws IOException {
		currentFile = file.getAbsolutePath();

		long lastModifiedDoc = doc.getLastModifiedDate().getTime();

		long lastModifiedSync = 0;
		if (lastModifiedMapDocs_.containsKey(file.getAbsolutePath())) {
			lastModifiedSync = lastModifiedMapDocs_.get(file.getAbsolutePath());
		}

		Stream stream = db.getAncestorSession().createStream();

		//Case "Force-Export", "Sync-Export" -> export Document from NSF to file in ODP
		if (this.direction == SyncDirection.EXPORT || (this.direction != SyncDirection.IMPORT && lastModifiedDoc > lastModifiedSync)) {
			log(LogLevel.INFO, "Exporting file " + file.getAbsolutePath());

			stream.open(file.getAbsolutePath(), "UTF-8");
			stream.truncate();
			stream.writeText(transformXslt(dxlExporter.exportDxl(doc)));
			stream.close();

			file.setLastModified(lastModifiedDoc);

			amountDocsExported++;

			lastModifiedMapDocs_.put(file.getAbsolutePath(), lastModifiedDoc);

			//Case "File was deleted in ODP" -> delete Document in NSF
		} else {
			if (!file.exists()) {

				log(LogLevel.INFO, "Deleting document " + file.getAbsolutePath());

				lastModifiedMapDocs_.remove(file.getAbsoluteFile());
				doc.removePermanently(true);

				amountDocsDeletedNSF++;
			} else {
				amountDocsInSync++;
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
	 * @throws IOException
	 */
	protected void importDocs(final Set<String> exportedFiles) throws IOException {
		List<File> importFiles = new ArrayList<File>();
		for (File subDir : docDir_.listFiles()) {
			if (subDir.isDirectory()) {
				for (File file : subDir.listFiles()) {
					importFiles.add(file);
				}
			}
		}

		total = importFiles.size();
		workingIdx = 0;
		amountDocsInSync = 0;
		workingState = WorkingState.DOCIMPORT;

		log(LogLevel.INFO, "Start Doc Import, synchronizing " + total + " documents");

		DxlImporter dxlImporter = db.getAncestorSession().createDxlImporter();

		//configure DxlImporter
		dxlImporter.setDocumentImportOption(DxlImporter.DocumentImportOption.REPLACE_ELSE_IGNORE);
		dxlImporter.setExitOnFirstFatalError(false);
		dxlImporter.setCompileLotusScript(false);
		dxlImporter.setReplicaRequiredForReplaceOrUpdate(false);

		//iterate over Files in ODP
		for (File file : importFiles) {
			workingIdx++;
			currentFile = file.getAbsolutePath();
			importOneDoc(file, dxlImporter, exportedFiles);
		}

		log(LogLevel.INFO, "End Doc Import, imported: " + amountDocsImported + ", in sync: " + amountDocsInSync + ", deleted (ODP): "
				+ amountDocsDeletedODP);
	}

	/**
	 * 
	 * @param file
	 * @param dxlImporter
	 * @param exportedFiles
	 * @throws IOException
	 */
	protected void importOneDoc(final File file, final DxlImporter dxlImporter, final Set<String> exportedFiles) throws IOException {
		currentFile = file.getAbsolutePath();

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

			log(LogLevel.INFO, "Importing document " + file.getAbsolutePath());

			stream.open(file.getAbsolutePath(), "UTF-8");
			stream.setPosition(0);
			dxlImporter.importDxl(stream, db);
			stream.close();

			amountDocsImported++;

			lastModifiedMapDocs_.put(file.getAbsolutePath(), lastModifiedFile);

			//Case "Document was deleted in NSF" -> delete file in ODP
		} else {

			if (!exportedFiles.contains(file.getAbsolutePath())) {

				log(LogLevel.INFO, "Deleting file " + file.getAbsolutePath());

				lastModifiedMapDocs_.remove(file.getAbsolutePath());
				file.delete();

				amountDocsDeletedODP++;
			} else {
				amountDocsInSync++;
			}
		}

	}

	public boolean doImport(final org.openntf.domino.design.DesignBase elem_, final File file) throws IOException {
		AbstractDesignBase elem = (AbstractDesignBase) elem_;
		String name = elem.getName();
		String unid = elem.getUniversalID();
		if (exportMetadata && elem instanceof HasMetadata) {
			File metaFile = new File(file.getAbsolutePath() + METADATA_SUFFIX);
			if (metaFile.exists()) {
				((HasMetadata) elem).readOnDiskMeta(metaFile);
			}
		}
		if (elem instanceof HasConfig) {
			File configFile = new File(file.getAbsolutePath() + CONFIG_SUFFIX);
			((HasConfig) elem).readOnDiskConfig(configFile);
		}
		if (!elem.readOnDiskFile(file)) {
			return false;
		}
		elem.setName(name);
		if (StringUtil.isNotEmpty(unid)) {
			elem.setUniversalId(unid);
		}
		return elem.save();
	}

	public boolean doExport(final org.openntf.domino.design.DesignBase elem_) throws IOException {
		AbstractDesignBase elem = (AbstractDesignBase) elem_;
		String odp = elem.getOnDiskPath();
		if (StringUtil.isEmpty(odp)) {
			odp = elem.getNoteID() + ".note";
		}
		File file = new File(diskDir_, odp);

		file.getParentFile().mkdirs(); // ensure the path exists
		if (exportMetadata && elem instanceof HasMetadata) {
			File metaFile = new File(file.getAbsolutePath() + METADATA_SUFFIX);
			((HasMetadata) elem).writeOnDiskMeta(metaFile);
		}
		if (elem instanceof HasConfig) {
			File configFile = new File(file.getAbsolutePath() + CONFIG_SUFFIX);
			((HasConfig) elem).writeOnDiskConfig(configFile);
		}
		return elem.writeOnDiskFile(file, gitFriendly);
	}

	protected void updateMapRecursive(final File root) {

		if (root.listFiles() != null) {
			for (File file : root.listFiles()) {
				if (file.isDirectory()) {
					if (!isDocDir(file) && !isLogDir(file)) {
						updateMapRecursive(file);
					}
				} else if (!isTimeStampsFile(file) && !isMetadataFile(file) && !isConfigFile(file)) {
					OnDiskFile odf = new OnDiskFile(diskDir_, file);
					total++;

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
		return file.getName().endsWith(METADATA_SUFFIX);
	}

	protected boolean isConfigFile(final File file) {
		return ODPMapping.CUSTOM_CONTROL.getFolder().equals(file.getParentFile().getName()) && file.getName().endsWith(XSP_CONFIG_SUFFIX);
	}

	protected boolean isTimeStampsFile(final File file) {
		return file.getName().startsWith(TIMESTAMPS_DESIGN_PREFIX) || file.getName().startsWith(TIMESTAMPS_DOCS_PREFIX);
	}

	protected boolean isDocDir(final File file) {
		return file.getName().equals(DOC_DIR);
	}

	protected boolean isLogDir(final File file) {
		return file.getName().equals(LOG_DIR);
	}

	/**
	 * Synchronizes a SyncObject which is a DesignBase or a OnDiskFile.
	 * 
	 * @param syncObject
	 *            The object that should be synchronized.
	 * @throws IOException
	 *             if I/O-Error occur.
	 */
	public void sync(final SyncObject syncObject) throws IOException {
		OnDiskFile odf;
		AbstractDesignBase elem;
		workingIdx++;

		if (syncObject instanceof AbstractDesignBase) {
			elem = (AbstractDesignBase) syncObject;
			odf = find(elem);
		} else if (syncObject instanceof OnDiskFile) {
			odf = (OnDiskFile) syncObject;
			try {
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
			workingIdx++;
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

		currentFile = file.getAbsolutePath();

		file.getParentFile().mkdirs(); // ensure the path exists

		long lastModifiedDoc = elem.getLastModified() == null ? 0 : elem.getLastModified().getTime();
		long lastModifiedFile = file.lastModified();
		long lastModifiedSync = odf.getTimeStamp();

		//Case "DesignElement was deleted in NSF" -> "File has to be deleted in ODP"
		if (odf.getState() == State.DELETE) {

			log(LogLevel.INFO, "Deleting File " + file.getAbsolutePath());

			files_.remove(odf.getFullName().toLowerCase());
			if (elem instanceof HasMetadata) {
				File metaFile = new File(file.getAbsoluteFile() + METADATA_SUFFIX);
				metaFile.delete();
			}
			if (elem instanceof HasConfig) {
				File configFile = new File(file.getAbsoluteFile() + CONFIG_SUFFIX);
				configFile.delete();
			}
			file.delete();

			amountDesignElementsDeletedODP++;

			//Case "File was deleted in ODP" -> "DesignElement has to be deleted in NSF"
		} else if (odf.getState() == State.WAS_DELETED) {

			log(LogLevel.INFO, "Deleting Design Element " + file.getAbsolutePath());

			files_.remove(odf.getFullName().toLowerCase());
			db.getDocumentByUNID(elem.getUniversalID()).removePermanently(true);

			amountDesignElementsDeletedNSF++;

			//Case "DesignElement was created in NSF", "Force-Export", "Sync-Export" -> "File has to be created/updated in ODP"
		} else if (odf.getState() == State.CREATE
				|| ((odf.getState() == State.EXPORT || odf.getState() == State.SYNC) && lastModifiedDoc > lastModifiedSync)) {

			log(LogLevel.INFO, "Exporting " + file.getAbsolutePath());

			try {
				if (doExport(elem)) {
					odf.setTimeStamp(lastModifiedDoc + TIMESTAMPS_OFFSET);
					amountDesignElementsExported++;
				}
			} catch (IOException e) {
				DominoUtils.handleException(e);
			} catch (OpenNTFNotesException e) {
				amountDesignElementsFailed++;
				log(LogLevel.SEVERE, "Could not export " + odf.getFullName());
				e.printStackTrace(logStream_);
				e.printStackTrace();
			}

			//Case "File was created in ODP", "Force-Import", "Sync-Import" -> "DesignElement has to be created/updated in NSF"
		} else if (odf.getState() == State.WAS_CREATED
				|| ((odf.getState() == State.IMPORT || odf.getState() == State.SYNC) && lastModifiedFile > lastModifiedSync)) {

			log(LogLevel.INFO, "Importing " + file.getAbsolutePath());

			try {
				if (doImport(elem, file)) {
					odf.setTimeStamp(elem.getLastModified().getTime() + TIMESTAMPS_OFFSET);
					amountDesignElementsImported++;
				}
			} catch (IOException e) {
				DominoUtils.handleException(e);
			} catch (OpenNTFNotesException e) {
				amountDesignElementsFailed++;
				log(LogLevel.SEVERE, "Could not import " + odf.getFullName());
				e.printStackTrace(logStream_);
				e.printStackTrace();
			}

			//Case "is in sync"
		} else {
			amountDesignElementsInSync++;
			//System.out.println(file.getAbsolutePath() + " is in sync");
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

	public void close() {
		logStream_.close();
		if (!enableLog) {
			logFile_.delete();
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

	public void setGitFriendly(final boolean gitFriendly) {
		this.gitFriendly = gitFriendly;
	}

	public void setExportMetadata(final boolean exportMetadata) {
		this.exportMetadata = exportMetadata;
	}

	public void setEnableLog(final boolean enableLog) {
		this.enableLog = enableLog;
	}

	protected void log(final LogLevel level, final String message) throws IOException {
		if (!enableLog) {
			return;
		}

		String prefix = "";

		if (level == LogLevel.SEVERE) {
			prefix = "[SEVERE] ";
		} else if (level == LogLevel.INFO) {
			prefix = "[INFO] ";
		}

		Date now = new Date();
		String dateString = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(now);

		//byte[] bytes = (prefix + message + "\n").getBytes();
		logStream_.println("[" + dateString + "] " + prefix + message);
	}

	public int getWorkingIndex() {
		return workingIdx;
	}

	public int getTotal() {
		return total;
	}

	public WorkingState getWorkingState() {
		return workingState;
	}

	public String getCurrentFile() {
		return currentFile;
	}

}

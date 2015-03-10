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
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.Callable;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.DxlExporter;
import org.openntf.domino.DxlImporter;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewEntryCollection;
import org.openntf.domino.progress.ProgressObservable;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.xml.XMLDocument;
import org.openntf.domino.utils.xml.XMLNode;
import org.xml.sax.SAXException;

/**
 * 
 * @author Alexander Wagner, FOCONIS AG
 * 
 */
public class OnDiskDocSync extends ProgressObservable implements Callable<OnDiskStatistics> {

	//	public static final String TIMESTAMPS_DESIGN_PREFIX = ".timeStampsDesign_";
	public static final String TIMESTAMPS_DOCS_PREFIX = ".timeStampsDocs_";
	//	public static final String METADATA_SUFFIX = ".metadata";
	//	public static final String XSP_CONFIG_SUFFIX = ".xsp-config";
	//	public static final String CONFIG_SUFFIX = "-config";
	//
	private static final int FFS_OFFSET = 4000; // FatFileSystem has an accuracy of 2 secs.
	//
	//	public static final String DOC_DIR = "Documents";
	//	private static final String LOG_DIR = "Logs";
	//	//private static final String LOG_FILE_PREFIX = "log_";

	private Transformer importTransformer = XMLDocument.createTransformer(GitFriendlyConverter.class
			.getResourceAsStream("importFilter.xslt"));
	private Transformer exportDocTransformer = XMLDocument.createTransformer(GitFriendlyConverter.class
			.getResourceAsStream("exportDocFilter.xslt"));

	private File diskDir_;
	private Database db;
	private String viewName;
	private OnDiskSyncDirection direction;
	//private File timeStampsDesign_;

	//protected OnDiskConverter odsConverter = new DefaultConverter(false);
	//private WorkingState workingState = WorkingState.PREPARING;
	//private String currentFile = "";
	//private int workingIdx = 0;

	//private int total = 0;

	//	private int amountDocsInSync = 0;
	//	private int amountDocsExported = 0;
	//	private int amountDocsImported = 0;
	//	private int amountDocsDeletedODP = 0;
	//	private int amountDocsDeletedNSF = 0;

	//	private int amountDesignElementsInSync = 0;
	//	private int amountDesignElementsExported = 0;
	//	private int amountDesignElementsImported = 0;
	//	private int amountDesignElementsDeletedODP = 0;
	//	private int amountDesignElementsDeletedNSF = 0;
	//	private int amountDesignElementsFailed = 0;
	private OnDiskStatistics stat = new OnDiskStatistics();

	private PrintStream log = System.out;
	private int[] cols;

	public OnDiskDocSync(final File diskDir, final Database db, final String viewName, final String viewCols,
			final OnDiskSyncDirection direction) {
		diskDir_ = diskDir;
		this.db = db;
		this.viewName = viewName;
		this.direction = direction;
		String[] tmp = viewCols.split("\\:");
		this.cols = new int[tmp.length];
		for (int i = 0; i < tmp.length; i++) {
			this.cols[i] = Integer.valueOf(tmp[i]);
		}
	}

	//	/**
	//	 * Creates a DocumentBuilder
	//	 * 
	//	 * @return
	//	 */
	//	public DocumentBuilder getDocBuilder() {
	//		if (docBuilder_ == null) {
	//			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	//			try {
	//				docBuilder_ = dbf.newDocumentBuilder();
	//			} catch (ParserConfigurationException e) {
	//				DominoUtils.handleException(e);
	//			}
	//		}
	//		return docBuilder_;
	//	}

	Map<String, OnDiskDocument> stateMap_;
	private DxlImporter dxlImporter;
	private DxlExporter dxlExporter;

	/**
	 * Sets up the designSync and builds the map with all OnDisk-files + timestamps
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	protected void setup() throws IOException, ClassNotFoundException {
		File timeStampsDesign = new File(diskDir_, TIMESTAMPS_DOCS_PREFIX + db.getReplicaID());

		if (timeStampsDesign.exists()) {
			//deserialize timeStamp of design file map
			InputStream fis = new FileInputStream(timeStampsDesign);
			InputStream bisr = new BufferedInputStream(fis);
			ObjectInput ois = new ObjectInputStream(bisr);
			stateMap_ = (Map<String, OnDiskDocument>) ois.readObject();
			ois.close();
		} else {
			stateMap_ = new HashMap<String, OnDiskDocument>();
		}

		scanDocDirectory(diskDir_); // now we have the ODS structure in the stateMap

	}

	/**
	 * Saves the map with files + timestamps back to disk
	 * 
	 * @param odpFiles
	 * @throws IOException
	 */
	protected void tearDown() throws IOException {
		//serialize TimeStamp Map of design files
		File timeStampsDesign = new File(diskDir_, TIMESTAMPS_DOCS_PREFIX + db.getReplicaID());
		OutputStream fos = new FileOutputStream(timeStampsDesign);
		OutputStream bos = new BufferedOutputStream(fos);
		ObjectOutput oos = new ObjectOutputStream(bos);
		oos.writeObject(stateMap_);
		oos.close();
	}

	/**
	 * Reads the DesginDirectory and add missing information to the map
	 * 
	 * @param currDir
	 * @param files
	 * @param isRoot
	 */
	protected void scanDocDirectory(final File currDir) {
		if (currDir.listFiles() != null) {
			for (File file : currDir.listFiles()) {
				String name = file.getName();
				if (file.isDirectory()) {
					if (!name.startsWith(".")) {
						scanDocDirectory(file);
					}
				} else if (name.endsWith(".xml")) {
					OnDiskDocument newODF = new OnDiskDocument(diskDir_, file);
					OnDiskDocument currODF = stateMap_.get(newODF.getPath());
					if (currODF == null) {
						stateMap_.put(newODF.getPath(), newODF);
					} else {
						currODF.setFile(file);
					}
				}
			}
		}
	}

	/**
	 * Synchronizes all DesignElements of the NSF with the {@link OnDiskProject}.
	 * 
	 */
	@Override
	public OnDiskStatistics call() {

		progressStart(2, "DesignSync");
		try {
			if (log != null) {
				log.println("Start Doc Sync. DB:" + this.db + ", disk: " + this.diskDir_ + ", direction " + this.direction);
			}
			setup();

			//configure DxlImporter
			dxlImporter = db.getAncestorSession().createDxlImporter();
			dxlImporter.setDocumentImportOption(DxlImporter.DocumentImportOption.REPLACE_ELSE_IGNORE);
			dxlImporter.setExitOnFirstFatalError(false);
			dxlImporter.setCompileLotusScript(false);
			dxlImporter.setReplicaRequiredForReplaceOrUpdate(false);

			dxlExporter = db.getAncestorSession().createDxlExporter();
			dxlExporter.setForceNoteFormat(false);
			dxlExporter.setExitOnFirstFatalError(false);
			dxlExporter.setDoctypeSYSTEM("");
			dxlExporter.setRichTextOption(DxlExporter.RichTextOption.DXL);
			dxlExporter.setMIMEOption(DxlExporter.MIMEOption.RAW);

			View view = db.getView(viewName);
			ViewEntryCollection coll = view.getAllEntries();

			// now read the NSF design
			log.println("NSF contains " + coll.getCount() + " documents in view " + view + ". Index cols: " + Arrays.toString(cols));
			log.println("DISK contains " + stateMap_.size() + " documents");

			progressStart(coll.getCount(), "Exporting documents");
			try {
				for (ViewEntry entry : coll) {
					OnDiskDocument diskElem = null;
					OnDiskSyncAction state = null;
					try {
						if (entry.isDocument()) {
							Vector<?> colValues = entry.getColumnValues();
							File xmlFile = diskDir_;
							for (int i = 0; i < cols.length - 1; i++) {
								xmlFile = new File(xmlFile, String.valueOf(colValues.get(cols[i])));
							}
							xmlFile = new File(xmlFile, OnDiskUtil.encodeResourceName(String.valueOf(colValues.get(cols[cols.length - 1])))
									+ ".xml");
							OnDiskDocument newDiskElem = new OnDiskDocument(diskDir_, xmlFile);

							diskElem = stateMap_.get(newDiskElem.getPath());

							if (diskElem == null) { // element is new in NSF
								diskElem = newDiskElem;
								//set state "CREATE", if direction is not Force-Import
								if (this.direction == OnDiskSyncDirection.IMPORT) {
									// if we import from Disk, we must delete the element in NSF
									state = OnDiskSyncAction.DELETE_NSF;
								} else {
									// otherwise, we export all missing files
									state = OnDiskSyncAction.FORCE_EXPROT;
								}
								stateMap_.put(newDiskElem.getPath(), diskElem); // add it to the map
							} else {
								// corresponding element exists on disk
								if (diskElem.getFile() == null) {
									// the element was exported in a previous step, but it is deleted on disk in the meantime
									if (this.direction == OnDiskSyncDirection.EXPORT) {
										// if we export, we must restore the element
										state = OnDiskSyncAction.FORCE_EXPROT;
									} else {
										// otherwise we
										state = OnDiskSyncAction.DELETE_NSF;
									}
								} else {
									if (this.direction == OnDiskSyncDirection.EXPORT) {
										state = OnDiskSyncAction.FORCE_EXPROT;
									} else if (this.direction == OnDiskSyncDirection.IMPORT) {
										state = OnDiskSyncAction.FORCE_IMPORT;
									} else {
										state = OnDiskSyncAction.SYNC;
									}
								}
							}

							sync(entry.getDocument(), diskElem, state);
						}

					} catch (Exception e) {
						System.err.println("[ERROR] entry: " + entry + ", diskElem " + diskElem + ", state: " + state);
						e.printStackTrace();
						stat.errors++;
					}
				}
			} finally {
				progressStop("Export done"); // Export
			}
			List<OnDiskDocument> unprocessed = getUnprocessedFiles();
			progressStart(unprocessed.size(), "Unprocessed");
			try {
				for (OnDiskDocument diskElem : unprocessed) {

					OnDiskSyncAction state = null;
					// the element exists on disk, but not in NSF
					if (this.direction == OnDiskSyncDirection.EXPORT) {
						state = OnDiskSyncAction.DELETE_DISK;
					} else {
						state = OnDiskSyncAction.FORCE_IMPORT;
					}
					try {
						sync(null, diskElem, state);
					} catch (Exception e) {
						System.err.println("[ERROR] diskElem " + diskElem + ", state: " + state);
						e.printStackTrace();
						stat.errors++;
					}
				}
				tearDown();
			} finally {
				progressStop("Unprocessed done");
			}

			//			log(LogLevel.INFO, "End Design Sync, exported: " + amountDesignElementsExported + ", imported: " + amountDesignElementsImported
			//					+ ", in sync: " + amountDesignElementsInSync + ", deleted (ODP): " + amountDesignElementsDeletedODP
			//					+ ", deleted (NSF): " + amountDesignElementsDeletedNSF + ", failed: " + amountDesignElementsFailed);
		} catch (IOException e) {
			DominoUtils.handleException(e);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			progressStop("DesignSync done");
		}

		return stat; // return statistics
	}

	/**
	 * Synchronizes a SyncObject which is a DesignBase or a OnDiskDocument.
	 * 
	 * @param syncObject
	 *            The object that should be synchronized.
	 * @throws IOException
	 *             if I/O-Error occur.
	 */
	private void sync(final Document doc, final OnDiskDocument diskElem, final OnDiskSyncAction state) throws IOException {
		if (diskElem.isProcessed()) {
			System.err.println("NameCollision: " + state.toString() + " " + diskElem.getPath());
			stat.errors++;
		}
		progressStep(state.toString() + " " + diskElem.getPath());
		File file;
		switch (state) {

		case DELETE_DISK:
			file = diskElem.getFile();
			stateMap_.remove(diskElem.getPath());
			if (file != null) {
				file.delete();
			}
			stat.deleteDisk++;
			return; // element removed from map!;

		case DELETE_NSF:
			stateMap_.remove(diskElem.getPath());
			if (doc != null) {
				doc.removePermanently(true);
			}
			stat.deleteNSF++;
			return; // element removed from map!;

		case FORCE_EXPROT:
			doExport(doc, diskElem);
			diskElem.setMD5(DominoUtils.checksum(diskElem.getFile(), "MD5"));
			stat.exported++;
			break;
		case FORCE_IMPORT:
			doImport(doc, diskElem);
			diskElem.setMD5(DominoUtils.checksum(diskElem.getFile(), "MD5"));
			stat.imported++;
			break;

		case SYNC:

			file = diskElem.getFile();
			file.getParentFile().mkdirs(); // ensure the path exists
			if (doc != null) {
				long lastModifiedDoc = doc.getLastModifiedDate().getTime();
				long lastModifiedFile = file.lastModified();

				if (Math.abs(lastModifiedDoc - diskElem.getDbTimeStamp()) > FFS_OFFSET) {
					// doc modified
					doExport(doc, diskElem);
					diskElem.setMD5(DominoUtils.checksum(diskElem.getFile(), "MD5"));
					stat.exported++;
				} else if (Math.abs(lastModifiedFile - diskElem.getDiskTimeStamp()) > FFS_OFFSET) {
					// file modified
					// TODO: MD5 check!
					String md5 = DominoUtils.checksum(diskElem.getFile(), "MD5");
					if (md5.equals(diskElem.getMD5())) {
						System.out.println("Only Date modified!");
						stat.inSync++;
					} else {
						doImport(doc, diskElem);
						diskElem.setMD5(md5);
						stat.exported++;
					}
				} else {
					stat.inSync++;
				}
			}

			break;
		default:
			break;

		}
		diskElem.setProcessed(true);
		diskElem.setDbTimeStamp(doc.getLastModifiedDate().getTime());
		diskElem.setDiskTimeStamp(diskElem.getFile().lastModified());

	}

	public void doImport(Document doc, final OnDiskDocument diskElem) throws IOException {
		File file = diskElem.getFile();

		try {
			XMLDocument dxl = new XMLDocument();
			FileInputStream is = new FileInputStream(file);
			try {
				dxl.loadInputStream(is);
			} finally {
				is.close();
			}
			XMLNode noteinfo = dxl.getDocumentElement().selectSingleNode("//noteinfo");

			String unid = noteinfo.getAttribute("unid");
			if (doc == null) {
				doc = db.getDocumentByID(unid);
				if (doc != null) {
					throw new IllegalStateException("Error while importing document " + diskElem);
				}
			} else {
				if (!unid.equals(doc.getUniversalID())) {
					throw new IllegalStateException("unid mismatch " + unid + " / " + doc.getUniversalID());
				}
			}

			dxlImporter.importDxl(transformXslt(importTransformer, dxl), db);

		} catch (SAXException e) {
			DominoUtils.handleException(e);
		} catch (ParserConfigurationException e) {
			DominoUtils.handleException(e);
		}
	}

	protected String transformXslt(final Transformer transformer, final XMLDocument dxl) {
		try {

			StreamResult result = new StreamResult(new StringWriter());
			DOMSource source = new DOMSource(dxl.getNode());
			try {
				transformer.transform(source, result);
			} catch (TransformerException e) {
				DominoUtils.handleException(e);
			}
			return result.getWriter().toString();

		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	public void doExport(final Document doc, final OnDiskDocument diskElem) throws IOException {
		File file = diskElem.getFile();
		file.getParentFile().mkdirs(); // ensure the path exists

		XMLDocument dxl = new XMLDocument();
		try {
			dxl.loadString(dxlExporter.exportDxl(doc));

			PrintWriter pw = new PrintWriter(file, "UTF-8");
			try {
				pw.write(transformXslt(exportDocTransformer, dxl));
			} finally {
				pw.close();
			}
		} catch (ParserConfigurationException e) {
			DominoUtils.handleException(e);
		} catch (SAXException e) {
			DominoUtils.handleException(e);
		}

	}

	//	public void close() {
	//		logStream_.close();
	//		if (!enableLog) {
	//			logFile_.delete();
	//		}
	//	}

	//	/**
	//	 * Synchronizes all Documents in the view of viewName in the NSF with the {@link OnDiskProject}.
	//	 * 
	//	 * @param viewName
	//	 *            The name of the view.
	//	 */
	//	public void syncDocs(final String viewName) {
	//		try {
	//			docDir_ = new File(this.diskDir_, DOC_DIR);
	//
	//			if (!docDir_.exists()) {
	//				docDir_.mkdir();
	//			}
	//
	//			View view = db.getView(viewName);
	//			if (view == null) {
	//				return;
	//			}
	//
	//			Map<String, Long> timeStamps = restoreDocTimestamps();
	//
	//			int totalODP = timeStamps.size();
	//			int totalNSF = view.getAllEntries().getCount();
	//
	//			notifyObservers(new ProgressStartEvent(Math.max(totalODP, totalNSF), //
	//					"Start Document Sync, synchronizing " + totalNSF + " (NSF) Documents with " + totalODP + " (ODP) Files"));
	//
	//			try {
	//				docDir_ = new File(this.diskDir_, DOC_DIR);
	//
	//				if (!docDir_.exists()) {
	//					docDir_.mkdir();
	//				}
	//
	//				Set<String> exportedFiles = new HashSet<String>();
	//				exportDocs(view, exportedFiles);
	//				importDocs(exportedFiles);
	//			} finally {
	//				saveDocTimestamps(timeStamps);
	//			}
	//
	//		} catch (Exception e) {
	//			DominoUtils.handleException(e);
	//		}
	//	}

	/**
	 * Returns all OnDiskDocuments that don't have a corresponding DesignElement in the NSF (anymore/not yet).
	 * 
	 * @return a list of unprocessed OnDiskDocuments.
	 */
	public List<OnDiskDocument> getUnprocessedFiles() {
		List<OnDiskDocument> resList = new ArrayList<OnDiskDocument>();
		for (OnDiskDocument file : stateMap_.values()) {
			if (!file.isProcessed()) {
				resList.add(file);
			}
		}
		return resList;
	}

	//	protected void log(final LogLevel level, final String message) throws IOException {
	//		if (!enableLog) {
	//			return;
	//		}
	//
	//		String prefix = "";
	//
	//		if (level == LogLevel.SEVERE) {
	//			prefix = "[SEVERE] ";
	//		} else if (level == LogLevel.INFO) {
	//			prefix = "[INFO] ";
	//		}
	//
	//		Date now = new Date();
	//		String dateString = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(now);
	//
	//		//byte[] bytes = (prefix + message + "\n").getBytes();
	//		logStream_.println("[" + dateString + "] " + prefix + message);
	//	}

}

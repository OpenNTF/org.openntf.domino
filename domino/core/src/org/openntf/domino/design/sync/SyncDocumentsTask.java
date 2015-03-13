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

package org.openntf.domino.design.sync;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Vector;
import java.util.concurrent.Callable;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import lotus.domino.NotesException;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.DxlExporter;
import org.openntf.domino.DxlImporter;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewEntryCollection;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.xml.XMLDocument;
import org.xml.sax.SAXException;

/**
 * 
 * @author Alexander Wagner, FOCONIS AG
 * 
 */
public class SyncDocumentsTask extends SyncTask<Document, OnDiskDocument> implements Callable<OnDiskStatistics> {

	private Transformer importTransformer = XMLDocument.createTransformer(SyncDocumentsTask.class.getResourceAsStream("importFilter.xslt"));
	private Transformer exportDocTransformer = XMLDocument.createTransformer(SyncDocumentsTask.class
			.getResourceAsStream("exportDocFilter.xslt"));

	private String viewName;
	private int[] cols;

	private PrintStream log = System.out;

	public SyncDocumentsTask(final File diskDir, final Database db, final String viewName, final String viewCols,
			final OnDiskSyncDirection direction) {
		super(diskDir, db, direction);
		this.viewName = viewName;
		String[] tmp = viewCols.split("\\:");
		this.cols = new int[tmp.length];
		for (int i = 0; i < tmp.length; i++) {
			this.cols[i] = Integer.valueOf(tmp[i]);
		}
	}

	public SyncDocumentsTask(final File diskDir, final String apiPath, final String viewName, final String viewCols,
			final OnDiskSyncDirection direction) {
		super(diskDir, apiPath, direction);
		this.viewName = viewName;
		String[] tmp = viewCols.split("\\:");
		this.cols = new int[tmp.length];
		for (int i = 0; i < tmp.length; i++) {
			this.cols[i] = Integer.valueOf(tmp[i]);
		}
	}

	private DxlImporter dxlImporter;
	private DxlExporter dxlExporter;

	/**
	 * Sets up the designSync and builds the map with all OnDisk-files + timestamps
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected void setup() throws IOException, ClassNotFoundException {
		super.setup();
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

	}

	//	/**
	//	 * Synchronizes all DesignElements of the NSF with the {@link OnDiskProject}.
	//	 * 
	//	 */
	//	@Override
	//	public OnDiskStatistics call() {
	//
	//		progressStart(2, "DesignSync");
	//		try {
	//			if (log != null) {
	//				log.println("Start Doc Sync. DB:" + this.db + ", disk: " + this.diskDir_ + ", direction " + this.direction);
	//			}
	//			setup();
	//
	//			//configure DxlImporter
	//			dxlImporter = db.getAncestorSession().createDxlImporter();
	//			dxlImporter.setDocumentImportOption(DxlImporter.DocumentImportOption.REPLACE_ELSE_IGNORE);
	//			dxlImporter.setExitOnFirstFatalError(false);
	//			dxlImporter.setCompileLotusScript(false);
	//			dxlImporter.setReplicaRequiredForReplaceOrUpdate(false);
	//
	//			dxlExporter = db.getAncestorSession().createDxlExporter();
	//			dxlExporter.setForceNoteFormat(false);
	//			dxlExporter.setExitOnFirstFatalError(false);
	//			dxlExporter.setDoctypeSYSTEM("");
	//			dxlExporter.setRichTextOption(DxlExporter.RichTextOption.DXL);
	//			dxlExporter.setMIMEOption(DxlExporter.MIMEOption.RAW);
	//
	//			View view = db.getView(viewName);
	//			ViewEntryCollection coll = view.getAllEntries();
	//
	//			// now read the NSF design
	//			log.println("NSF contains " + coll.getCount() + " documents in view " + view + ". Index cols: " + Arrays.toString(cols));
	//			log.println("DISK contains " + stateMap_.size() + " documents");
	//
	//			progressStart(coll.getCount(), "Exporting documents");
	//			try {
	//				for (ViewEntry entry : coll) {
	//					OnDiskDocument diskElem = null;
	//					OnDiskSyncAction state = null;
	//					try {
	//						if (entry.isDocument()) {
	//							Vector<?> colValues = entry.getColumnValues();
	//							File xmlFile = diskDir_;
	//							for (int i = 0; i < cols.length - 1; i++) {
	//								xmlFile = new File(xmlFile, String.valueOf(colValues.get(cols[i])));
	//							}
	//							xmlFile = new File(xmlFile, OnDiskUtil.encodeResourceName(String.valueOf(colValues.get(cols[cols.length - 1])))
	//									+ ".xml");
	//							OnDiskDocument newDiskElem = new OnDiskDocument(diskDir_, xmlFile);
	//
	//							diskElem = stateMap_.get(entry.getUniversalID());
	//
	//							if (diskElem == null) { // element is new in NSF
	//								diskElem = newDiskElem;
	//								//set state "CREATE", if direction is not Force-Import
	//								if (this.direction == OnDiskSyncDirection.IMPORT) {
	//									// if we import from Disk, we must delete the element in NSF
	//									state = OnDiskSyncAction.DELETE_NSF;
	//								} else {
	//									// otherwise, we export all missing files
	//									state = OnDiskSyncAction.FORCE_EXPROT;
	//								}
	//								stateMap_.put(entry.getUniversalID(), diskElem); // add it to the map
	//							} else {
	//								// corresponding element exists on disk
	//								if (diskElem.getFile() == null) {
	//									// the element was exported in a previous step, but it is deleted on disk in the meantime
	//									if (this.direction == OnDiskSyncDirection.EXPORT) {
	//										// if we export, we must restore the element
	//										state = OnDiskSyncAction.FORCE_EXPROT;
	//										diskElem = newDiskElem;
	//										stateMap_.put(entry.getUniversalID(), diskElem);
	//									} else {
	//										// otherwise we
	//										state = OnDiskSyncAction.DELETE_NSF;
	//									}
	//								} else {
	//									if (this.direction == OnDiskSyncDirection.EXPORT) {
	//										state = OnDiskSyncAction.FORCE_EXPROT;
	//									} else if (this.direction == OnDiskSyncDirection.IMPORT) {
	//										state = OnDiskSyncAction.FORCE_IMPORT;
	//									} else {
	//										state = OnDiskSyncAction.SYNC;
	//									}
	//								}
	//							}
	//
	//							sync(entry.getDocument(), diskElem, state);
	//						}
	//
	//					} catch (Exception e) {
	//						System.err.println("[ERROR] entry: " + entry + ", diskElem " + diskElem + ", state: " + state);
	//						e.printStackTrace();
	//						stat.errors++;
	//					}
	//				}
	//			} finally {
	//				progressStop("Export done"); // Export
	//			}
	//			List<OnDiskDocument> unprocessed = getUnprocessedFiles();
	//			progressStart(unprocessed.size(), "Unprocessed");
	//			try {
	//				for (OnDiskDocument diskElem : unprocessed) {
	//
	//					OnDiskSyncAction state = null;
	//					// the element exists on disk, but not in NSF
	//					if (this.direction == OnDiskSyncDirection.EXPORT) {
	//						state = OnDiskSyncAction.DELETE_DISK;
	//					} else {
	//						state = OnDiskSyncAction.FORCE_IMPORT;
	//					}
	//					try {
	//						sync(null, diskElem, state);
	//					} catch (Exception e) {
	//						System.err.println("[ERROR] diskElem " + diskElem + ", state: " + state);
	//						e.printStackTrace();
	//						stat.errors++;
	//					}
	//				}
	//				tearDown();
	//			} finally {
	//				progressStop("Unprocessed done");
	//			}
	//
	//			//			log(LogLevel.INFO, "End Design Sync, exported: " + amountDesignElementsExported + ", imported: " + amountDesignElementsImported
	//			//					+ ", in sync: " + amountDesignElementsInSync + ", deleted (ODP): " + amountDesignElementsDeletedODP
	//			//					+ ", deleted (NSF): " + amountDesignElementsDeletedNSF + ", failed: " + amountDesignElementsFailed);
	//		} catch (IOException e) {
	//			DominoUtils.handleException(e);
	//		} catch (ClassNotFoundException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		} finally {
	//			progressStop("DesignSync done");
	//		}
	//
	//		return stat; // return statistics
	//	}

	//	/**
	//	 * Synchronizes a SyncObject which is a DesignBase or a OnDiskDocument.
	//	 * 
	//	 * @param syncObject
	//	 *            The object that should be synchronized.
	//	 * @throws IOException
	//	 *             if I/O-Error occur.
	//	 */
	//	@Override
	//	private void sync(final Document doc, final OnDiskDocument diskElem, final OnDiskSyncAction state) throws IOException {
	//		if (diskElem.isProcessed()) {
	//			System.err.println("NameCollision: " + state.toString() + " " + diskElem.getPath());
	//			stat.errors++;
	//		}
	//		progressStep(state.toString() + " " + diskElem.getPath());
	//		File file;
	//		switch (state) {
	//
	//		case DELETE_DISK:
	//			file = diskElem.getFile();
	//			stateMap_.remove(diskElem.getUniversalID());
	//			if (file != null) {
	//				file.delete();
	//			}
	//			stat.deleteDisk++;
	//			return; // element removed from map!;
	//
	//		case DELETE_NSF:
	//			stateMap_.remove(doc.getUniversalID());
	//			if (doc != null) {
	//				doc.removePermanently(true);
	//			}
	//			stat.deleteNSF++;
	//			return; // element removed from map!;
	//
	//		case FORCE_EXPROT:
	//			doExport(doc, diskElem);
	//			diskElem.setMD5(DominoUtils.checksum(diskElem.getFile(), "MD5"));
	//			stat.exported++;
	//			break;
	//		case FORCE_IMPORT:
	//			doImport(doc, diskElem);
	//			diskElem.setMD5(DominoUtils.checksum(diskElem.getFile(), "MD5"));
	//			stat.imported++;
	//			break;
	//
	//		case SYNC:
	//
	//			file = diskElem.getFile();
	//			file.getParentFile().mkdirs(); // ensure the path exists
	//			if (doc != null) {
	//				long lastModifiedDoc = doc.getLastModifiedDate().getTime();
	//				long lastModifiedFile = file.lastModified();
	//
	//				if (Math.abs(lastModifiedDoc - diskElem.getDbTimeStamp()) > FFS_OFFSET) {
	//					// doc modified
	//					doExport(doc, diskElem);
	//					diskElem.setMD5(DominoUtils.checksum(diskElem.getFile(), "MD5"));
	//					stat.exported++;
	//				} else if (Math.abs(lastModifiedFile - diskElem.getDiskTimeStamp()) > FFS_OFFSET) {
	//					// file modified
	//					// TODO: MD5 check!
	//					String md5 = DominoUtils.checksum(diskElem.getFile(), "MD5");
	//					if (md5.equals(diskElem.getMD5())) {
	//						System.out.println("Only Date modified!");
	//						stat.inSync++;
	//					} else {
	//						doImport(doc, diskElem);
	//						diskElem.setMD5(md5);
	//						stat.exported++;
	//					}
	//				} else {
	//					stat.inSync++;
	//				}
	//			}
	//
	//			break;
	//		default:
	//			break;
	//
	//		}
	//		diskElem.setProcessed(true);
	//		diskElem.setDbTimeStamp(doc.getLastModifiedDate().getTime());
	//		diskElem.setDiskTimeStamp(diskElem.getFile().lastModified());
	//
	//	}

	@Override
	public Document doImport(Document doc, final OnDiskDocument diskElem) throws IOException {
		File file = diskElem.getFile();

		try {
			XMLDocument dxl = new XMLDocument();
			FileInputStream is = new FileInputStream(file);
			try {
				dxl.loadInputStream(is);
			} finally {
				is.close();
			}
			//			XMLNode noteinfo = dxl.getDocumentElement().selectSingleNode("//noteinfo");
			//
			//			String unid = noteinfo.getAttribute("unid");
			//			if (doc == null) {
			//				doc = db.getDocumentByID(unid);
			//				if (doc != null) {
			//					throw new IllegalStateException("Error while importing document " + diskElem);
			//				}
			//			} else {
			//				if (!unid.equals(doc.getUniversalID())) {
			//					throw new IllegalStateException("unid mismatch " + unid + " / " + doc.getUniversalID());
			//				}
			//			}

			String unid = diskElem.getKey();
			if (doc == null) {
				doc = db.getDocumentByUNID(unid);
				if (doc == null) {
					doc = db.createDocument(unid);
					doc.setUniversalID(unid);
					doc.save();
				}
			}
			dxlImporter.importDxl(transformXslt(importTransformer, dxl), db);
			doc.recycle();
			return db.getDocumentByUNID(unid);
		} catch (SAXException e) {
			DominoUtils.handleException(e);
		} catch (ParserConfigurationException e) {
			DominoUtils.handleException(e);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
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

	@Override
	public void doExport(final Document doc, final OnDiskDocument diskElem) throws IOException {
		File file = diskElem.getFile();
		file.getParentFile().mkdirs(); // ensure the path exists
		if (diskElem.isRenamed()) {
			System.out.println("RENAME " + diskElem);
		}
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

	@Override
	protected boolean isAllowedDir(final File file) {
		String name = file.getName();
		return !name.startsWith(".");
	}

	@Override
	protected boolean isAllowedFile(final File file) {
		String name = file.getName();
		return name.endsWith(".xml");
	}

	@Override
	protected OnDiskDocument createDiskFile(final File parent, final File file) {
		return new OnDiskDocument(parent, file);
	}

	@Override
	protected Document createDbElement(final OnDiskDocument source) {
		return null;
	}

	@Override
	protected void removeDbElement(final Document element) {
		dirMap.remove(element.getUniversalID());
		element.removePermanently(true);

	}

	@Override
	protected void processDbToDisk() {
		View view = db.getView(viewName);
		ViewEntryCollection coll = view.getAllEntries();

		// now read the NSF design
		log.println("NSF contains " + coll.getCount() + " documents in view " + view + ". Index cols: " + Arrays.toString(cols));
		log.println("DISK contains " + dirMap.size() + " documents");

		progressStart(coll.getCount(), "Exporting documents");
		try {
			for (ViewEntry entry : coll) {
				OnDiskDocument diskElem = null;
				OnDiskSyncAction state = null;
				try {
					if (entry.isDocument()) {
						Document doc = entry.getDocument();
						Vector<?> colValues = entry.getColumnValues();
						File xmlFile = diskDir_;
						for (int i = 0; i < cols.length - 1; i++) {
							xmlFile = new File(xmlFile, String.valueOf(colValues.get(cols[i])));
						}
						String fName = OnDiskUtil.encodeResourceName(String.valueOf(colValues.get(cols[cols.length - 1]))) + ".xml";
						xmlFile = new File(xmlFile, fName);

						String key = doc.getUniversalID();
						sync(key, doc, xmlFile);
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

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.design.DatabaseDesign;
import org.openntf.domino.design.DesignBase;
import org.openntf.domino.design.DesignBaseNamed;
import org.openntf.domino.design.DesignCollection;
import org.openntf.domino.design.OnDiskConverter;
import org.openntf.domino.progress.ProgressObservable;
import org.openntf.domino.utils.DominoUtils;

import com.ibm.commons.util.StringUtil;

/**
 * 
 * @author Alexander Wagner, FOCONIS AG
 * 
 */
public class OnDiskDesignSync extends ProgressObservable implements Callable<OnDiskStatistics> {

	public static final String TIMESTAMPS_DESIGN_PREFIX = ".timeStampsDesign_";
	//public static final String TIMESTAMPS_DOCS_PREFIX = ".timeStampsDocs_";
	public static final String METADATA_SUFFIX = ".metadata";
	public static final String XSP_CONFIG_SUFFIX = ".xsp-config";
	public static final String CONFIG_SUFFIX = "-config";

	private static final int FFS_OFFSET = 4000; // FatFileSystem has an accuracy of 2 secs.

	public static final String DOC_DIR = "Documents";
	//private static final String NOTEINFO_UNID = "noteinfo unid=\"";
	private static final String LOG_DIR = "Logs";
	//private static final String LOG_FILE_PREFIX = "log_";

	private File diskDir_;
	private Database db;
	private OnDiskSyncDirection direction;
	//private File timeStampsDesign_;

	protected OnDiskConverter odsConverter = new DefaultConverter(false);
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

	public OnDiskDesignSync(final File diskDir, final Database db, final OnDiskSyncDirection direction) {
		diskDir_ = diskDir;
		this.db = db;
		this.direction = direction;
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

	Map<String, OnDiskFile> stateMap_;

	/**
	 * Sets up the designSync and builds the map with all OnDisk-files + timestamps
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	protected void setup() throws IOException, ClassNotFoundException {
		File timeStampsDesign = new File(diskDir_, TIMESTAMPS_DESIGN_PREFIX + db.getReplicaID());

		if (timeStampsDesign.exists()) {
			//deserialize timeStamp of design file map
			InputStream fis = new FileInputStream(timeStampsDesign);
			InputStream bisr = new BufferedInputStream(fis);
			ObjectInput ois = new ObjectInputStream(bisr);
			stateMap_ = (Map<String, OnDiskFile>) ois.readObject();
			ois.close();
		} else {
			stateMap_ = new HashMap<String, OnDiskFile>();
		}
		scanDesignDirectory(diskDir_); // now we have the ODS structure in the stateMap
	}

	/**
	 * Saves the map with files + timestamps back to disk
	 * 
	 * @param odpFiles
	 * @throws IOException
	 */
	protected void tearDown() throws IOException {
		//serialize TimeStamp Map of design files
		File timeStampsDesign = new File(diskDir_, TIMESTAMPS_DESIGN_PREFIX + db.getReplicaID());
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
	protected void scanDesignDirectory(final File currDir) {
		if (currDir.listFiles() != null) {
			for (File file : currDir.listFiles()) {
				if (file.isDirectory()) {
					if (currDir != diskDir_ || isDesignDir(file)) {
						scanDesignDirectory(file);
					}
				} else if (isDesignFile(file)) {
					OnDiskFile newODF = new OnDiskFile(diskDir_, file);
					String key = (newODF.getImplementingClass().getName() + ":" + newODF.getName()).toLowerCase();
					OnDiskFile currODF = stateMap_.get(key);
					if (currODF == null) {
						stateMap_.put(key, newODF);
					} else {
						currODF.setFile(diskDir_, file);
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
				log.println("Start Design Sync. DB:" + this.db + ", disk: " + this.diskDir_ + ", direction " + this.direction);
			}
			setup();

			// now read the NSF design
			DatabaseDesign design = db.getDesign();
			DesignCollection<DesignBase> elems = design.getDesignElements(); //(" !@Contains($Flags;{X}) & !@Begins($TITLE;{WEB-INF/classes}) ");
			log.println("NSF contains " + elems.getCount() + " elements");
			log.println("DISK contains " + stateMap_.size() + " elements");

			progressStart(elems.getCount(), "Exporting elements");
			try {
				for (DesignBase dbElem : elems) {
					OnDiskFile diskElem = null;
					OnDiskSyncAction state = null;
					try {
						String key = (dbElem.getClass().getName() + ":" + dbElem.getOnDiskName()).toLowerCase();
						diskElem = stateMap_.get(key);

						if (diskElem == null) { // element is new in NSF
							File file = new File(diskDir_, dbElem.getOnDiskPath());
							diskElem = new OnDiskFile(diskDir_, file);

							//set state "CREATE", if direction is not Force-Import
							if (this.direction == OnDiskSyncDirection.IMPORT) {
								// if we import from Disk, we must delete the element in NSF
								state = OnDiskSyncAction.DELETE_NSF;
							} else {
								// otherwise, we export all missing files
								state = OnDiskSyncAction.FORCE_EXPROT;
							}
							stateMap_.put(key, diskElem); // add it to the map
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

						sync(dbElem, diskElem, state);
					} catch (Exception e) {
						System.err.println("[ERROR] dbElem: " + dbElem + ", diskElem " + diskElem + ", state: " + state);
						e.printStackTrace();
						stat.errors++;
					}
				}
			} finally {
				progressStop("Export done"); // Export
			}
			List<OnDiskFile> unprocessed = getUnprocessedFiles();
			progressStart(unprocessed.size(), "Unprocessed");
			try {
				for (OnDiskFile diskElem : unprocessed) {

					OnDiskSyncAction state = null;
					// the element exists on disk, but not in NSF
					if (this.direction == OnDiskSyncDirection.EXPORT) {
						state = OnDiskSyncAction.DELETE_DISK;
					} else {
						state = OnDiskSyncAction.FORCE_IMPORT;
					}
					AbstractDesignBase dbElem = null;
					try {

						dbElem = (AbstractDesignBase) diskElem.getImplementingClass().newInstance();
						//System.out.println("unprocessed: " + dbElem.getClass().getName() + "\t" + diskElem.getFile());
						dbElem.init(db);
						dbElem.setOnDiskName(diskElem.getName());
						sync(dbElem, diskElem, state);
					} catch (Exception e) {
						System.err.println("[ERROR] dbElem: " + dbElem + ", diskElem " + diskElem + ", state: " + state);
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
	 * Synchronizes a SyncObject which is a DesignBase or a OnDiskFile.
	 * 
	 * @param syncObject
	 *            The object that should be synchronized.
	 * @throws IOException
	 *             if I/O-Error occur.
	 */
	private void sync(final DesignBase dbElem, final OnDiskFile diskElem, final OnDiskSyncAction state) throws IOException {

		String key = dbElem == null ? "" : (dbElem.getClass().getName() + ":" + dbElem.getOnDiskName()).toLowerCase();
		if (diskElem.isProcessed()) {
			System.err.println("NameCollision: " + state.toString() + " " + diskElem.getPath());
			stat.errors++;
		}
		progressStep(state.toString() + " " + diskElem.getPath());
		Document doc;
		File file;
		switch (state) {

		case DELETE_DISK:
			file = diskElem.getFile();
			stateMap_.remove(key);
			if (file != null) {
				if (dbElem instanceof HasMetadata) {
					File metaFile = new File(file.getAbsoluteFile() + METADATA_SUFFIX);
					metaFile.delete();
				}
				if (dbElem instanceof HasConfig) {
					File configFile = new File(file.getAbsoluteFile() + CONFIG_SUFFIX);
					configFile.delete();
				}
				file.delete();

			}
			stat.deleteDisk++;
			return; // element removed from map!;

		case DELETE_NSF:
			doc = dbElem.getDocument();
			stateMap_.remove(key);
			if (doc != null) {
				doc.removePermanently(true);
			}
			stat.deleteNSF++;
			return; // element removed from map!;

		case FORCE_EXPROT:
			doExport(dbElem, diskElem);
			diskElem.setMD5(DominoUtils.checksum(diskElem.getFile(), "MD5"));
			stat.exported++;
			break;
		case FORCE_IMPORT:
			doImport(dbElem, diskElem);
			diskElem.setMD5(DominoUtils.checksum(diskElem.getFile(), "MD5"));
			stat.imported++;
			break;

		case SYNC:

			doc = dbElem.getDocument();
			file = diskElem.getFile();
			file.getParentFile().mkdirs(); // ensure the path exists
			if (doc != null) {
				long lastModifiedDoc = doc.getLastModifiedDate().getTime();
				long lastModifiedFile = file.lastModified();

				if (Math.abs(lastModifiedDoc - diskElem.getDbTimeStamp()) > FFS_OFFSET) {
					// doc modified
					doExport(dbElem, diskElem);
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
						doImport(dbElem, diskElem);
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
		diskElem.setDbTimeStamp(dbElem.getDocument().getLastModifiedDate().getTime());
		diskElem.setDiskTimeStamp(diskElem.getFile().lastModified());

	}

	public void doImport(final DesignBase dbElem, final OnDiskFile diskElem) throws IOException {
		File file = diskElem.getFile();
		String name = dbElem.getOnDiskName();
		String unid = dbElem.getUniversalID();
		if (odsConverter.isMetadataEnabled() && dbElem instanceof HasMetadata) {
			File metaFile = new File(file.getAbsolutePath() + METADATA_SUFFIX);
			if (metaFile.exists()) {
				((HasMetadata) dbElem).readOnDiskMeta(metaFile, odsConverter);
			}
		}
		if (dbElem instanceof HasConfig) {
			File configFile = new File(file.getAbsolutePath() + CONFIG_SUFFIX);
			if (configFile.exists()) {
				((HasConfig) dbElem).readOnDiskConfig(configFile, odsConverter);
			}
		}
		dbElem.readOnDiskFile(file, odsConverter);

		// restore the name
		if (dbElem instanceof DesignBaseNamed) {
			((DesignBaseNamed) dbElem).setName(name);
		}

		if (StringUtil.isNotEmpty(unid)) {
			dbElem.setUniversalID(unid);
		}

		if (!dbElem.save(odsConverter)) {
			throw new IllegalAccessError("Cannot save " + dbElem);
		}

	}

	public void doExport(final DesignBase dbElem, final OnDiskFile diskElem) throws IOException {
		File file = diskElem.getFile();
		file.getParentFile().mkdirs(); // ensure the path exists
		if (dbElem instanceof HasMetadata) {
			File metaFile = new File(file.getAbsolutePath() + METADATA_SUFFIX);
			if (odsConverter.isMetadataEnabled()) {
				((HasMetadata) dbElem).writeOnDiskMeta(metaFile, odsConverter);
			} else {
				metaFile.delete();
			}
		} else {
			// Element has no metaData, so it is native
		}
		if (dbElem instanceof HasConfig) {
			File configFile = new File(file.getAbsolutePath() + CONFIG_SUFFIX);
			((HasConfig) dbElem).writeOnDiskConfig(configFile, odsConverter);
		}
		dbElem.writeOnDiskFile(file, odsConverter);
	}

	// Directory, which contains designElements
	protected boolean isDesignDir(final File file) {
		String name = file.getName();

		if (name.equalsIgnoreCase(".settings"))
			return true;

		if (name.startsWith(".git"))
			return false; // exclude git dir

		if (name.equalsIgnoreCase(DOC_DIR))
			return false;

		if (name.equalsIgnoreCase(LOG_DIR))
			return false;

		return true;
	}

	protected boolean isDesignFile(final File file) {
		String name = file.getName();

		if (name.startsWith(TIMESTAMPS_DESIGN_PREFIX))
			return false;

		//if (name.startsWith(TIMESTAMPS_DOCS_PREFIX))
		//	return false;

		if (name.startsWith(".git"))
			return false; // 

		if (name.endsWith(METADATA_SUFFIX))
			return false;

		if (name.endsWith(XSP_CONFIG_SUFFIX))
			return false;

		return true;
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
	 * Returns all OnDiskFiles that don't have a corresponding DesignElement in the NSF (anymore/not yet).
	 * 
	 * @return a list of unprocessed OnDiskFiles.
	 */
	public List<OnDiskFile> getUnprocessedFiles() {
		List<OnDiskFile> resList = new ArrayList<OnDiskFile>();
		for (OnDiskFile file : stateMap_.values()) {
			if (!file.isProcessed()) {
				resList.add(file);
			}
		}
		return resList;
	}

	public void setOdsConverter(final OnDiskConverter odsConverter) {
		this.odsConverter = odsConverter;
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

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

package org.openntf.domino.design.sync;

//import static org.openntf.domino.design.impl.OnDiskSync.LOG_DIR;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.design.DatabaseDesign;
import org.openntf.domino.design.DesignBase;
import org.openntf.domino.design.DesignBaseNamed;
import org.openntf.domino.design.DesignCollection;
import org.openntf.domino.design.DxlConverter;
import org.openntf.domino.design.impl.AbstractDesignBase;
import org.openntf.domino.design.impl.DesignMapping;
import org.openntf.domino.design.impl.HasMetadata;
import org.openntf.domino.design.impl.HasXspConfig;
import org.openntf.domino.utils.DominoUtils;

import com.ibm.commons.util.StringUtil;

/**
 * 
 * @author Alexander Wagner, FOCONIS AG
 * 
 */

public class SyncDesignTask extends SyncTask<DesignBase, OnDiskDesign> implements Callable<OnDiskStatistics> {

	protected DxlConverter odsConverter = new DefaultDxlConverter(false);
	String noteId_;

	public SyncDesignTask(final File diskDir, final Database db, final OnDiskSyncDirection direction) {
		super(diskDir, db, direction);
	}

	public SyncDesignTask(final File diskDir, final String apiPath, final OnDiskSyncDirection direction) {
		super(diskDir, apiPath, direction);
	}

	public void setNoteId(final String noteId) {
		noteId_ = noteId;
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
	//				log.println("Start Design Sync. DB:" + this.db + ", disk: " + this.diskDir_ + ", direction " + this.direction);
	//			}
	//			setup();
	//
	//			// now read the NSF design
	//			DatabaseDesign design = db.getDesign();
	//			DesignCollection<DesignBase> elems = design.getDesignElements(); //(" !@Contains($Flags;{X}) & !@Begins($TITLE;{WEB-INF/classes}) ");
	//			log.println("NSF contains " + elems.getCount() + " elements");
	//			log.println("DISK contains " + stateMap_.size() + " elements");
	//
	//			progressStart(elems.getCount(), "Exporting elements");
	//			try {
	//				for (DesignBase dbElem : elems) {
	//					OnDiskDesign diskElem = null;
	//					OnDiskSyncAction state = null;
	//					try {
	//						String key = (dbElem.getClass().getName() + ":" + dbElem.getOnDiskName()).toLowerCase();
	//						diskElem = stateMap_.get(key);
	//
	//						if (diskElem == null) { // element is new in NSF
	//							File file = new File(diskDir_, dbElem.getOnDiskPath());
	//							diskElem = new OnDiskDesign(diskDir_, file);
	//
	//							//set state "CREATE", if direction is not Force-Import
	//							if (this.direction == OnDiskSyncDirection.IMPORT) {
	//								// if we import from Disk, we must delete the element in NSF
	//								state = OnDiskSyncAction.DELETE_NSF;
	//							} else {
	//								// otherwise, we export all missing files
	//								state = OnDiskSyncAction.FORCE_EXPROT;
	//							}
	//							stateMap_.put(key, diskElem); // add it to the map
	//						} else {
	//							// corresponding element exists on disk
	//							if (diskElem.getFile() == null) {
	//								// the element was exported in a previous step, but it is deleted on disk in the meantime
	//								if (this.direction == OnDiskSyncDirection.EXPORT) {
	//									// if we export, we must restore the element
	//									state = OnDiskSyncAction.FORCE_EXPROT;
	//								} else {
	//									// otherwise we
	//									state = OnDiskSyncAction.DELETE_NSF;
	//								}
	//							} else {
	//								if (this.direction == OnDiskSyncDirection.EXPORT) {
	//									state = OnDiskSyncAction.FORCE_EXPROT;
	//								} else if (this.direction == OnDiskSyncDirection.IMPORT) {
	//									state = OnDiskSyncAction.FORCE_IMPORT;
	//								} else {
	//									state = OnDiskSyncAction.SYNC;
	//								}
	//							}
	//						}
	//
	//						sync(dbElem, diskElem, state);
	//					} catch (Exception e) {
	//						System.err.println("[ERROR] dbElem: " + dbElem + ", diskElem " + diskElem + ", state: " + state);
	//						e.printStackTrace();
	//						stat.errors++;
	//					}
	//				}
	//			} finally {
	//				progressStop("Export done"); // Export
	//			}
	//			List<OnDiskDesign> unprocessed = getUnprocessedFiles();
	//			progressStart(unprocessed.size(), "Unprocessed");
	//			try {
	//				for (OnDiskDesign diskElem : unprocessed) {
	//
	//					OnDiskSyncAction state = null;
	//					// the element exists on disk, but not in NSF
	//					if (this.direction == OnDiskSyncDirection.EXPORT) {
	//						state = OnDiskSyncAction.DELETE_DISK;
	//					} else {
	//						state = OnDiskSyncAction.FORCE_IMPORT;
	//					}
	//					AbstractDesignBase dbElem = null;
	//					try {
	//
	//						dbElem = (AbstractDesignBase) diskElem.getImplementingClass().newInstance();
	//						//System.out.println("unprocessed: " + dbElem.getClass().getName() + "\t" + diskElem.getFile());
	//						dbElem.init(db);
	//						dbElem.setOnDiskName(diskElem.getName());
	//						sync(dbElem, diskElem, state);
	//					} catch (Exception e) {
	//						System.err.println("[ERROR] dbElem: " + dbElem + ", diskElem " + diskElem + ", state: " + state);
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
	//	 * Synchronizes a SyncObject which is a DesignBase or a OnDiskFile.
	//	 * 
	//	 * @param syncObject
	//	 *            The object that should be synchronized.
	//	 * @throws IOException
	//	 *             if I/O-Error occur.
	//	 */
	//	private void sync(final DesignBase dbElem, final OnDiskDesign diskElem, final OnDiskSyncAction state) throws IOException {
	//
	//		String key = dbElem == null ? "" : (dbElem.getClass().getName() + ":" + dbElem.getOnDiskName()).toLowerCase();
	//		if (diskElem.isProcessed()) {
	//			System.err.println("NameCollision: " + state.toString() + " " + diskElem.getPath());
	//			stat.errors++;
	//		}
	//		progressStep(state.toString() + " " + diskElem.getPath());
	//		Document doc;
	//		File file;
	//		switch (state) {
	//
	//		case DELETE_DISK:
	//			file = diskElem.getFile();
	//			stateMap_.remove(key);
	//			if (file != null) {
	//				if (dbElem instanceof HasMetadata) {
	//					File metaFile = new File(file.getAbsoluteFile() + METADATA_SUFFIX);
	//					metaFile.delete();
	//				}
	//				if (dbElem instanceof HasConfig) {
	//					File configFile = new File(file.getAbsoluteFile() + CONFIG_SUFFIX);
	//					configFile.delete();
	//				}
	//				file.delete();
	//
	//			}
	//			stat.deleteDisk++;
	//			return; // element removed from map!;
	//
	//		case DELETE_NSF:
	//			doc = dbElem.getDocument();
	//			stateMap_.remove(key);
	//			if (doc != null) {
	//				doc.removePermanently(true);
	//			}
	//			stat.deleteNSF++;
	//			return; // element removed from map!;
	//
	//		case FORCE_EXPROT:
	//			doExport(dbElem, diskElem);
	//			diskElem.setMD5(DominoUtils.checksum(diskElem.getFile(), "MD5"));
	//			stat.exported++;
	//			break;
	//		case FORCE_IMPORT:
	//			doImport(dbElem, diskElem);
	//			diskElem.setMD5(DominoUtils.checksum(diskElem.getFile(), "MD5"));
	//			stat.imported++;
	//			break;
	//
	//		case SYNC:
	//
	//			doc = dbElem.getDocument();
	//			file = diskElem.getFile();
	//			file.getParentFile().mkdirs(); // ensure the path exists
	//			if (doc != null) {
	//				long lastModifiedDoc = doc.getLastModifiedDate().getTime();
	//				long lastModifiedFile = file.lastModified();
	//
	//				if (Math.abs(lastModifiedDoc - diskElem.getDbTimeStamp()) > FFS_OFFSET) {
	//					// doc modified
	//					doExport(dbElem, diskElem);
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
	//						doImport(dbElem, diskElem);
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
	//		diskElem.setDbTimeStamp(dbElem.getDocument().getLastModifiedDate().getTime());
	//		diskElem.setDiskTimeStamp(diskElem.getFile().lastModified());
	//
	//	}

	@Override
	public DesignBase doImport(final DesignBase dbElem, final OnDiskDesign diskElem) throws IOException {
		File file = diskElem.getFile();
		//String name = dbElem.getOnDiskName();
		String unid = dbElem.getUniversalID();
		if (odsConverter.isMetadataEnabled() && dbElem instanceof HasMetadata) {
			File metaFile = new File(file.getAbsolutePath() + METADATA_SUFFIX);
			if (metaFile.exists()) {
				((HasMetadata) dbElem).importMeta(odsConverter, metaFile);
			}
		}
		if (dbElem instanceof HasXspConfig) {
			File configFile = new File(file.getAbsolutePath() + CONFIG_SUFFIX);
			if (configFile.exists()) {
				((HasXspConfig) dbElem).importXspConfig(odsConverter, configFile);
			}
		}
		dbElem.importDesign(odsConverter, file);

		//		// restore the name
		//		if (dbElem instanceof DesignBaseNamed) {
		//			((DesignBaseNamed) dbElem).setName(name); // TODO: decode!!!
		//		}

		if (StringUtil.isNotEmpty(unid)) {
			dbElem.setUniversalID(unid);
		}

		if (!dbElem.save(odsConverter)) {
			throw new IllegalAccessError("Cannot save " + dbElem);
		}
		return dbElem;

	}

	@Override
	public void doExport(final DesignBase dbElem, final OnDiskDesign diskElem) throws IOException {
		File file = diskElem.getFile();
		file.getParentFile().mkdirs(); // ensure the path exists
		if (dbElem instanceof HasMetadata) {
			File metaFile = new File(file.getAbsolutePath() + METADATA_SUFFIX);
			if (odsConverter.isMetadataEnabled()) {
				((HasMetadata) dbElem).exportMeta(odsConverter, metaFile);
			} else {
				metaFile.delete();
			}
		} else {
			// Element has no metaData, so it is native
		}
		if (dbElem instanceof HasXspConfig) {
			File configFile = new File(file.getAbsolutePath() + CONFIG_SUFFIX);
			((HasXspConfig) dbElem).exportXspConfig(odsConverter, configFile);
		}
		dbElem.exportDesign(odsConverter, file);
	}

	// Directory, which contains designElements
	@Override
	protected boolean isAllowedDir(final File file) {
		String name = file.getName();

		if (name.equalsIgnoreCase(".settings"))
			return true; // include .settings dir

		if (name.startsWith("."))
			return false; // exclude all other dot dirs

		if (file.getParentFile().equals(diskDir_)) {
			// we are in the disk dir!
			if (name.equalsIgnoreCase(DOC_DIR))
				return false;

			if (name.equalsIgnoreCase(LOG_DIR))
				return false;
		}
		return true;
	}

	@Override
	protected boolean isAllowedFile(final File file) {
		String name = file.getName();

		if (name.startsWith(TIMESTAMPS_FILE_PREFIX))
			return false;

		if (name.startsWith(".git"))
			return false; // exclude all files starting with .git

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

	public void setDxlConverter(final DxlConverter odsConverter) {
		this.odsConverter = odsConverter;
	}

	@Override
	protected OnDiskDesign createDiskFile(final File parent, final File file) {
		return new OnDiskDesign(parent, file);
	}

	@Override
	protected DesignBase createDbElement(final OnDiskDesign source) {
		// TODO Auto-generated method stub
		AbstractDesignBase dbElem;
		try {
			dbElem = (AbstractDesignBase) source.getImplementingClass().newInstance();
			//						//System.out.println("unprocessed: " + dbElem.getClass().getName() + "\t" + diskElem.getFile());
			dbElem.init(db);
			if (dbElem instanceof DesignBaseNamed) {
				((DesignBaseNamed) dbElem).setName(source.getName());
			}
			return dbElem;
		} catch (IllegalAccessException e) {
			DominoUtils.handleException(e);
		} catch (InstantiationException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	protected void removeDbElement(final DesignBase element) {
		dirMap.remove(OnDiskDesign.getKey(element));
		element.getDocument().removePermanently(true);

	}

	@Override
	public OnDiskStatistics call() {
		if (noteId_ != null) {
			Document doc = db.getDocumentByID(noteId_);
			if (doc == null) {
				return stat;
			}
			DesignBase dbElem = DesignMapping.fromDocument(doc);
			String key = OnDiskDesign.getKey(dbElem);

			File xmlFile = new File(diskDir_, dbElem.getMapping().getOnDiskFolder());
			xmlFile = new File(xmlFile, OnDiskDesign.getOnDiskName(dbElem));
			progressStart(1, "Exporting elements");
			try {
				sync(key, dbElem, xmlFile);
			} catch (IOException e) {
				DominoUtils.handleException(e);
				stat.errors++;
			} finally {
				progressStop("Exporting done");
			}
			return stat;
		}

		return super.call();
	}

	@Override
	protected void processDbToDisk() {
		// now read the NSF design
		DatabaseDesign design = db.getDesign();
		DesignCollection<DesignBase> elems = design.getDesignElements(); //(" !@Contains($Flags;{X}) & !@Begins($TITLE;{WEB-INF/classes}) ");
		log.println("NSF contains " + elems.getCount() + " elements");
		log.println("DISK contains " + dirMap.size() + " elements");

		progressStart(elems.getCount(), "Exporting elements");
		try {
			for (DesignBase dbElem : elems) {
				OnDiskSyncAction state = null;
				try {
					String key = OnDiskDesign.getKey(dbElem);

					File xmlFile = new File(diskDir_, dbElem.getMapping().getOnDiskFolder());
					xmlFile = new File(xmlFile, OnDiskDesign.getOnDiskName(dbElem));
					sync(key, dbElem, xmlFile);
				} catch (Exception e) {
					System.err.println("[ERROR] dbElem: " + dbElem + ", state: " + state);
					e.printStackTrace();
					stat.errors++;
				}
			}
		} finally {
			progressStop("Export done"); // Export
		}

	}

}

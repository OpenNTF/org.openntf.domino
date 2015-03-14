/*
 * © Copyright FOCONIS AG, 2015
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
 * 
 */

package org.openntf.domino.design.sync;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.design.impl.HasMetadata;
import org.openntf.domino.design.impl.HasXspConfig;
import org.openntf.domino.progress.ProgressObservable;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xots.Tasklet;

/**
 * 
 * @author Alexander Wagner, FOCONIS AG
 * 
 */
@Tasklet(session = Tasklet.Session.NATIVE, threadConfig = Tasklet.ThreadConfig.STRICT)
public abstract class SyncTask<DB, DISK extends OnDiskAbstract<DB>> extends ProgressObservable implements Callable<OnDiskStatistics> {

	public static final String TIMESTAMPS_FILE_PREFIX = ".timeStamps_";
	//public static final String TIMESTAMPS_DOCS_PREFIX = ".timeStampsDocs_";
	public static final String METADATA_SUFFIX = ".metadata";
	public static final String XSP_CONFIG_SUFFIX = ".xsp-config";
	public static final String CONFIG_SUFFIX = "-config";

	private static final int FFS_OFFSET = 4000; // FatFileSystem has an accuracy of 2 secs.

	public static final String DOC_DIR = "Documents";
	//private static final String NOTEINFO_UNID = "noteinfo unid=\"";
	public static final String LOG_DIR = "Logs";
	//private static final String LOG_FILE_PREFIX = "log_";

	private String apiPath;
	protected File diskDir_;
	private Database db_;
	protected OnDiskSyncDirection direction;
	protected OnDiskStatistics stat = new OnDiskStatistics();

	private PrintStream logStream = System.out;
	private Level logLevel = Level.SEVERE;

	Map<String, DISK> dirMap;

	public SyncTask(final File diskDir, final Database db, final OnDiskSyncDirection direction) {
		diskDir_ = diskDir;
		this.db_ = db;
		this.direction = direction;
	}

	public SyncTask(final File diskDir, final String apiPath, final OnDiskSyncDirection direction) {
		diskDir_ = diskDir;
		this.apiPath = apiPath;
		this.direction = direction;
	}

	protected Database getDb() {
		if (db_ == null) {
			Session sess = Factory.getSession(SessionType.CURRENT);
			log(Level.FINER, "Opening DB " + apiPath + " with Session " + sess);
			db_ = sess.getDatabase(apiPath);
		}
		return db_;
	}

	/**
	 * Sets up the designSync and builds the map with all OnDisk-files + timestamps
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	protected void setup() throws IOException, ClassNotFoundException {
		File timeStampsDesign = new File(diskDir_, TIMESTAMPS_FILE_PREFIX + getClass().getSimpleName() + "_" + getDb().getReplicaID());

		if (timeStampsDesign.exists()) {
			//deserialize timeStamp of design file map
			InputStream fis = new FileInputStream(timeStampsDesign);
			InputStream bisr = new BufferedInputStream(fis);
			ObjectInput ois = new ObjectInputStream(bisr);
			dirMap = (Map<String, DISK>) ois.readObject();
			ois.close();
		} else {
			dirMap = new HashMap<String, DISK>();
		}
		scanDirectory(diskDir_); // now we have the ODS structure in the stateMap
	}

	/**
	 * Saves the map with files + timestamps back to disk
	 * 
	 * @param odpFiles
	 * @throws IOException
	 */
	protected void tearDown() throws IOException {
		//serialize TimeStamp Map of design files
		File timeStampsDesign = new File(diskDir_, TIMESTAMPS_FILE_PREFIX + getClass().getSimpleName() + "_" + getDb().getReplicaID());
		OutputStream fos = new FileOutputStream(timeStampsDesign);
		OutputStream bos = new BufferedOutputStream(fos);
		ObjectOutput oos = new ObjectOutputStream(bos);
		oos.writeObject(dirMap);
		oos.close();
	}

	/**
	 * Reads the DesginDirectory and add missing information to the map
	 * 
	 * @param currDir
	 * @param files
	 * @param isRoot
	 */
	protected void scanDirectory(final File currDir) {
		if (currDir.listFiles() != null) {
			for (File file : currDir.listFiles()) {
				if (file.isDirectory()) {
					if (isAllowedDir(file)) {
						scanDirectory(file);
					}
				} else if (isAllowedFile(file)) {
					DISK newODF = createDiskFile(diskDir_, file);
					String key = newODF.getKey();
					DISK currODF = dirMap.get(key);
					if (currODF == null) {
						dirMap.put(key, newODF);
					} else {
						currODF.setFile(diskDir_, file);
					}
				}
			}
		}
	}

	/**
	 * Checks if the given dir is an allowed import/export dir.
	 * 
	 * @param file
	 * @return
	 */
	protected abstract boolean isAllowedDir(File file);

	/**
	 * checks if the given file is an allowed import/export file.
	 * 
	 * @param file
	 * @return
	 */
	protected abstract boolean isAllowedFile(final File file);

	/**
	 * Imports the given diskElem into the DB
	 * 
	 * @param dbElem
	 * @param diskElem
	 * @return
	 * @throws IOException
	 */
	public abstract void doImport(final DB dbElem, final DISK diskElem) throws IOException;

	public abstract void doExport(final DB dbElem, final DISK diskElem) throws IOException;

	protected abstract DISK createDiskFile(final File parent, final File file);

	protected abstract DB createDbElement(DISK source);

	/**
	 * Removes the given element from database. i.e. deletes it!
	 * 
	 * @param element
	 */
	protected abstract void removeDbElement(DB element);

	/**
	 * To implement: iterate over all elements in DB and sync them with the appropriate disk element
	 */
	protected abstract void processDbToDisk();

	/**
	 * Sync all (unprocessed) disk elements with DB
	 */
	protected void processDiskToDb() {
		List<DISK> unprocessed = getUnprocessedFiles();
		progressStart(unprocessed.size(), "Unprocessed");
		try {
			for (DISK diskElem : unprocessed) {

				OnDiskSyncAction state = null;
				// the element exists on disk, but not in NSF
				if (this.direction == OnDiskSyncDirection.EXPORT || diskElem.getFile() == null || !diskElem.getFile().exists()) {
					state = OnDiskSyncAction.DELETE_DISK;
				} else {
					state = OnDiskSyncAction.FORCE_IMPORT;
				}
				DB dbElem = null;
				try {

					dbElem = createDbElement(diskElem);
					sync(dbElem, diskElem, state);
				} catch (Exception e) {
					log(Level.SEVERE, "dbElem: " + dbElem + ", diskElem " + diskElem + ", state: " + state, e);
					stat.errors++;
				}
			}
		} finally {
			progressStop("Unprocessed done");
		}
	}

	/**
	 * Synchronizes all DesignElements of the NSF with the {@link OnDiskProject}.
	 * 
	 */
	@Override
	public OnDiskStatistics call() {
		log(Level.INFO, "Start: " + getClass().getSimpleName() + ". " + getDb() + " <=> " + diskDir_ + ", Direction:" + direction);
		progressStart(2, getClass().getSimpleName() + " start");
		try {
			setup();
			processDbToDisk(); // progress#1
			processDiskToDb(); // progress#2
			tearDown();
		} catch (IOException e) {
			DominoUtils.handleException(e);
		} catch (ClassNotFoundException e) {
			DominoUtils.handleException(e);
		} finally {
			log(stat.errors == 0 ? Level.INFO : Level.SEVERE, "Stop: " + getClass().getSimpleName() + ". " + stat);
			progressStop(getClass().getSimpleName() + " done");
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
	protected void sync(final DB dbElem, final DISK diskElem, final OnDiskSyncAction state) throws IOException {

		if (diskElem.isProcessed()) {
			// This happens mostly if you use the same name for different platforms or languages
			// (currently not supported!)
			log(Level.SEVERE, "Duplicate design element: " + diskElem.getPath());
			stat.errors++;
		}
		progressStep(state.toString() + " " + diskElem.getPath());
		File file;

		switch (state) {

		case DELETE_DISK:
			file = diskElem.getFile();
			log(Level.FINE, "DELETE_DISK:\t" + file);
			dirMap.remove(diskElem.getKey());
			if (file != null) {
				if (dbElem instanceof HasMetadata) {
					File metaFile = new File(file.getAbsoluteFile() + METADATA_SUFFIX);
					metaFile.delete();
				}
				if (dbElem instanceof HasXspConfig) {
					File configFile = new File(file.getAbsoluteFile() + CONFIG_SUFFIX);
					configFile.delete();
				}
				file.delete();

			}
			stat.deleteDisk++;
			return; // element removed from map!;

		case DELETE_NSF:
			log(Level.FINE, "DELETE_NSF:\t" + dbElem);
			removeDbElement(dbElem);
			stat.deleteNSF++;
			return; // element removed from map!;

		case FORCE_EXPROT:
			log(Level.FINE, "FORCE_EXPORT\t" + dbElem + "\t" + diskElem);
			doExport(dbElem, diskElem);
			diskElem.setMD5(DominoUtils.checksum(diskElem.getFile(), "MD5"));
			stat.exported++;
			break;
		case FORCE_IMPORT:
			log(Level.FINE, "FORCE_IMPORT\t" + dbElem + "\t" + diskElem);
			doImport(dbElem, diskElem);
			diskElem.setMD5(DominoUtils.checksum(diskElem.getFile(), "MD5"));
			stat.imported++;
			break;

		case SYNC:

			file = diskElem.getFile();
			file.getParentFile().mkdirs(); // ensure the path exists

			if (diskElem.isRenamed()) {
				System.out.println(diskElem + " was renamed");
			}
			long lastModifiedFile = file.lastModified();

			if (Math.abs(diskElem.getDbTimeStampDelta(dbElem)) > FFS_OFFSET) {
				// doc modified
				log(Level.FINE, "EXPORT\t" + dbElem + "\t" + diskElem);
				doExport(dbElem, diskElem);
				diskElem.setMD5(DominoUtils.checksum(diskElem.getFile(), "MD5"));
				stat.exported++;
			} else if (Math.abs(lastModifiedFile - diskElem.getDiskTimeStamp()) > FFS_OFFSET) {
				// file modified
				String md5 = DominoUtils.checksum(diskElem.getFile(), "MD5");
				if (md5.equals(diskElem.getMD5())) {
					log(Level.FINER, "CLEAN\t" + dbElem + "\t" + diskElem);
					stat.inSync++;
				} else {
					log(Level.FINE, "IMPORT\t" + dbElem + "\t" + diskElem);
					doImport(dbElem, diskElem);
					diskElem.setMD5(md5);
					stat.exported++;
				}
			} else {
				log(Level.FINER, "CLEAN\t" + dbElem + "\t" + diskElem);
				stat.inSync++;
			}

			break;
		default:
			break;

		}
		diskElem.setProcessed(true);
		diskElem.setDbTimeStamp(dbElem);
		diskElem.setDiskTimeStamp(diskElem.getFile().lastModified());
	}

	/**
	 * Returns all OnDiskFiles that don't have a corresponding DesignElement in the NSF (anymore/not yet).
	 * 
	 * @return a list of unprocessed OnDiskFiles.
	 */
	public List<DISK> getUnprocessedFiles() {
		List<DISK> resList = new ArrayList<DISK>();
		for (DISK file : dirMap.values()) {
			if (!file.isProcessed()) {
				resList.add(file);
			}
		}
		return resList;
	}

	protected void sync(final String key, final DB dbElem, final File xmlFile) throws IOException {
		DISK diskElem = dirMap.get(key);
		OnDiskSyncAction state;

		if (diskElem == null) { // element is new in NSF
			diskElem = createDiskFile(diskDir_, xmlFile);
			//set state "CREATE", if direction is not Force-Import
			if (this.direction == OnDiskSyncDirection.IMPORT) {
				// if we import from Disk, we must delete the element in NSF
				state = OnDiskSyncAction.DELETE_NSF;
			} else {
				// otherwise, we export all missing files
				state = OnDiskSyncAction.FORCE_EXPROT;
			}
			dirMap.put(key, diskElem); // add it to the map
		} else {
			// corresponding element exists on disk
			if (diskElem.getFile() == null) {
				// the element was exported in a previous step, but it is deleted on disk in the meantime
				if (this.direction == OnDiskSyncDirection.EXPORT) {
					// if we export, we must restore the element
					state = OnDiskSyncAction.FORCE_EXPROT;
					diskElem = createDiskFile(diskDir_, xmlFile);
					dirMap.put(key, diskElem);
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
	}

	protected void log(final Level level, final String message) {
		log(level, message, null);
	}

	protected void log(final Level level, final String message, final Throwable t) {
		if (logLevel.intValue() > level.intValue())
			return;
		Date now = new Date();
		String dateString = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(now);
		logStream.println(dateString + "\t" + level + "\t" + message);
		if (t != null) {
			t.printStackTrace(logStream);
		}
	}

	public void setupLog(final Level logLevel, final PrintStream out) {
		this.logLevel = logLevel;
		this.logStream = out;
	}
}

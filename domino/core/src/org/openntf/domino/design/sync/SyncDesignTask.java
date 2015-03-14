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
import java.util.logging.Level;

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

	@Override
	public void doImport(final DesignBase dbElem, final OnDiskDesign diskElem) throws IOException {
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
			dbElem.init(getDb());
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
			Document doc = getDb().getDocumentByID(noteId_);
			if (doc == null) {
				return stat;
			}
			DesignBase dbElem = DesignMapping.fromDocument(doc);
			String key = OnDiskDesign.getKey(dbElem);

			File xmlFile = new File(diskDir_, dbElem.getMapping().getOnDiskFolder());
			xmlFile = new File(xmlFile, OnDiskDesign.getOnDiskName(dbElem));
			progressStart(1, "Exporting element");
			try {
				sync(key, dbElem, xmlFile);
			} catch (IOException e) {
				log(Level.SEVERE, "NoteID: " + noteId_, e);
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
		DatabaseDesign design = getDb().getDesign();
		DesignCollection<DesignBase> elems = design.getDesignElements(); //(" !@Contains($Flags;{X}) & !@Begins($TITLE;{WEB-INF/classes}) ");
		log(Level.INFO, "NSF contains " + elems.getCount() + " elements. DISK contains " + dirMap.size() + " elements");

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
					log(Level.SEVERE, "dbElem: " + dbElem + ", state: " + state, e);
					stat.errors++;
				}
			}
		} finally {
			progressStop("Export done"); // Export
		}

	}

}

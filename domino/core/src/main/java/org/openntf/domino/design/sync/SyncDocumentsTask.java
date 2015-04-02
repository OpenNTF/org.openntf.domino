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
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.logging.Level;

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
public class SyncDocumentsTask extends SyncTask<DocumentWrapper, OnDiskDocument> implements Callable<OnDiskStatistics> {

	private Transformer importTransformer = XMLDocument.createTransformer(SyncDocumentsTask.class.getResourceAsStream("importFilter.xslt"));
	private Transformer exportDocTransformer = XMLDocument.createTransformer(SyncDocumentsTask.class
			.getResourceAsStream("exportDocFilter.xslt"));

	private String viewName;
	private int[] cols;

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
		dxlImporter = getDb().getAncestorSession().createDxlImporter();
		dxlImporter.setDocumentImportOption(DxlImporter.DocumentImportOption.REPLACE_ELSE_IGNORE);
		dxlImporter.setExitOnFirstFatalError(false);
		dxlImporter.setCompileLotusScript(false);
		dxlImporter.setReplicaRequiredForReplaceOrUpdate(false);

		dxlExporter = getDb().getAncestorSession().createDxlExporter();
		dxlExporter.setForceNoteFormat(false);
		dxlExporter.setExitOnFirstFatalError(false);
		dxlExporter.setDoctypeSYSTEM("");
		dxlExporter.setRichTextOption(DxlExporter.RichTextOption.DXL);
		dxlExporter.setMIMEOption(DxlExporter.MIMEOption.RAW);

	}

	@Override
	public void doImport(final DocumentWrapper docWrapper, final OnDiskDocument diskElem) throws IOException {
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
			//				doc = getDb().getDocumentByID(unid);
			//				if (doc != null) {
			//					throw new IllegalStateException("Error while importing document " + diskElem);
			//				}
			//			} else {
			//				if (!unid.equals(doc.getUniversalID())) {
			//					throw new IllegalStateException("unid mismatch " + unid + " / " + doc.getUniversalID());
			//				}
			//			}

			String unid = diskElem.getKey();
			Document doc = docWrapper.getDocument();
			if (doc == null) {
				doc = getDb().getDocumentByUNID(unid);
				if (doc == null) {
					doc = getDb().createDocument(unid);
					doc.setUniversalID(unid);
					doc.save();
				}
			}
			dxlImporter.importDxl(transformXslt(importTransformer, dxl), getDb());
			doc.recycle();
			docWrapper.setDocument(getDb().getDocumentByUNID(unid));
		} catch (SAXException e) {
			DominoUtils.handleException(e);
		} catch (ParserConfigurationException e) {
			DominoUtils.handleException(e);
		} catch (NotesException e) {
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

	@Override
	public void doExport(final DocumentWrapper docWrapper, final OnDiskDocument diskElem) throws IOException {
		File file = diskElem.getFile();
		file.getParentFile().mkdirs(); // ensure the path exists
		if (diskElem.isRenamed()) {
			System.out.println("RENAME " + diskElem);
		}
		XMLDocument dxl = new XMLDocument();
		try {
			dxl.loadString(dxlExporter.exportDxl(docWrapper.getDocument()));

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
	//			View view = getDb().getView(viewName);
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
	protected DocumentWrapper createDbElement(final OnDiskDocument source) {
		return new DocumentWrapper(null);
	}

	@Override
	protected void removeDbElement(final DocumentWrapper element) {
		Document doc = element.getDocument();
		dirMap.remove(doc.getUniversalID());
		doc.removePermanently(true);

	}

	@Override
	protected void processDbToDisk() {
		log(Level.INFO, "Using view: " + viewName + ". Index cols: " + Arrays.toString(cols));
		View view = getDb().getView(viewName);
		if (view == null) {
			throw new IllegalStateException("View '" + viewName + "' does not exist!");
		}

		ViewEntryCollection coll = view.getAllEntries();
		// now read the NSF design
		log(Level.INFO, "View contains " + coll.getCount() + " entries. DISK contains " + dirMap.size() + " documents");

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
						sync(key, new DocumentWrapper(doc), xmlFile);
					}

				} catch (Exception e) {
					log(Level.SEVERE, "entry: " + entry + ", diskElem " + diskElem + ", state: " + state, e);
					stat.errors++;
				}
			}
		} finally {
			progressStop("Export done");
		}

	}

}

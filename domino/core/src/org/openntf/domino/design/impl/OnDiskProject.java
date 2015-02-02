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
import org.openntf.domino.design.impl.OnDiskFile.State;
import org.openntf.domino.utils.DominoUtils;
import org.xml.sax.InputSource;

import com.ibm.commons.util.StringUtil;

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

	public OnDiskProject(final File diskDir, final Database db, final SyncDirection direction) {
		diskDir_ = diskDir;
		this.db = db;
		this.direction = direction;
		//deserialize and update design files map
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
				//deserialize map
				InputStream file = new FileInputStream(timeStampsDesign_);
				InputStream buffer = new BufferedInputStream(file);
				ObjectInput input = new ObjectInputStream(buffer);
				files_ = (Map<String, OnDiskFile>) input.readObject();
				input.close();
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

	public void syncDesign() {
		DatabaseDesign design = db.getDesign();
		DesignCollection<DesignBase> elems = design
				.getDesignElements(" !@Contains($Flags;{X}) & !($TITLE={WEB-INF/classes/plugin/Activator.class}) ");

		System.out.println("Start Design Sync");

		for (DesignBase elem : elems) {
			sync(elem, null);
		}

		for (OnDiskFile odf : getUnprocessedFiles()) {
			sync(null, odf);
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

		//Exporter konfigurieren
		dxlExporter.setForceNoteFormat(false);
		dxlExporter.setExitOnFirstFatalError(false);
		dxlExporter.setDoctypeSYSTEM("");
		dxlExporter.setRichTextOption(DxlExporter.RichTextOption.DXL);
		dxlExporter.setMIMEOption(DxlExporter.MIMEOption.RAW);

		//Dokumente durchgehen, neue Files erstellen, Dokumente löschen oder vorhandene Files updaten falls erforderlich

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

		if (this.direction == SyncDirection.EXPORT || (this.direction != SyncDirection.IMPORT && lastModifiedDoc > lastModifiedSync)) {
			// immer exportieren
			stream.open(file.getAbsolutePath(), "UTF-8");
			stream.truncate();
			stream.writeText(transformXslt(dxlExporter.exportDxl(doc)));
			stream.close();

			file.setLastModified(lastModifiedDoc);

			System.out.println("Update file " + file.getAbsolutePath());

			lastModifiedMapDocs_.put(file.getAbsolutePath(), lastModifiedDoc);

		} else {
			if (!file.exists()) {
				//lösche Dokument
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

		//Importer konfigurieren
		dxlImporter.setDocumentImportOption(DxlImporter.DocumentImportOption.REPLACE_ELSE_IGNORE);
		dxlImporter.setExitOnFirstFatalError(false);
		dxlImporter.setCompileLotusScript(false);
		dxlImporter.setReplicaRequiredForReplaceOrUpdate(false);

		//Files durchgehen, neue Dokumente erstellen, Files löschen oder vorhandene Dokumente updaten falls erforderlich
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

			//immer importieren
			stream.open(file.getAbsolutePath(), "UTF-8");
			stream.setPosition(0);
			dxlImporter.importDxl(stream, db);
			stream.close();

			System.out.println("Update doc " + file.getAbsolutePath());

			lastModifiedMapDocs_.put(file.getAbsolutePath(), lastModifiedFile);

		} else {

			if (!exportedFiles.contains(file.getAbsolutePath())) {
				//lösche File
				System.out.println("delete file " + file.getName());
				lastModifiedMapDocs_.remove(file.getAbsolutePath());
				file.delete();
			}
		}

	}

	public void doImport(final org.openntf.domino.design.DesignBase elem_, final File file) throws IOException {
		AbstractDesignBaseNamed elem = (AbstractDesignBaseNamed) elem_;
		String name = elem.getName();
		String unid = elem.getUniversalID();
		if (elem instanceof HasMetadata) {
			File metaFile = new File(file.getAbsolutePath() + ".metadata");
			((HasMetadata) elem).readOnDiskMeta(metaFile);
		}
		elem.readOnDiskFile(file);
		elem.setName(name);
		elem.setUniversalId(unid);
		elem.save();
	}

	public void doExport(final org.openntf.domino.design.DesignBase elem_) throws IOException {
		AbstractDesignBase elem = (AbstractDesignBase) elem_;
		String odp = elem.getOnDiskPath();
		if (StringUtil.isEmpty(odp)) {
			odp = elem.getNoteID() + ".note";
		}
		File odsFile = new File(diskDir_, odp);
		//System.out.println(elem.getClass().getName() + "\t\t\t" + odsFile + "\t" + elem.getNoteID());
		odsFile.getParentFile().mkdirs(); // ensure the path exists
		elem.writeOnDiskFile(odsFile);
		if (elem instanceof HasMetadata) {
			File meta = new File(odsFile.getAbsolutePath() + ".metadata");
			((HasMetadata) elem).writeOnDiskMeta(meta);
		}

	}

	protected void updateMapRecursive(final File root) {
		for (File file : root.listFiles()) {
			if (file.isDirectory()) {
				if (!isDocDir(file)) {
					updateMapRecursive(file);
				}
			} else if (!isTimeStampsFile(file) && !isMetadataFile(file)) {
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

	protected boolean isMetadataFile(final File file) {
		return file.getName().endsWith(".metadata");
	}

	protected boolean isTimeStampsFile(final File file) {
		return file.getName().startsWith(".timeStamps");
	}

	protected boolean isDocDir(final File file) {
		return file.getName().equals(DOC_DIR);
	}

	public void sync(final org.openntf.domino.design.DesignBase designElem_, final OnDiskFile odf_) {
		OnDiskFile odf;
		AbstractDesignBase elem;

		if (odf_ == null) {
			elem = (AbstractDesignBase) designElem_;
			odf = find(elem);
		} else if (designElem_ == null) {
			odf = odf_;
			try {
				Constructor<?> ctor = odf.getImplementingClass().getConstructor(Database.class);
				elem = (AbstractDesignBase) ctor.newInstance();
				if (odf.getState() != State.WAS_CREATED || odf.getState() != State.IMPORT) {
					odf.setState(State.DELETE);
				}
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
			throw new IllegalArgumentException();
		}

		if (odf == null) {
			//not found -> new
			File file = new File(diskDir_, elem.getOnDiskPath());
			odf = new OnDiskFile(diskDir_, file);
			odf.setState(State.CREATE);
			files_.put(odf.getFullName().toLowerCase(), odf);
		}

		File file = odf.getFile();
		file.getParentFile().mkdirs(); // ensure the path exists

		Document doc = db.getDocumentByUNID(elem.getUniversalID());

		long lastModifiedDoc = doc.getLastModifiedDate().getTime();
		long lastModifiedFile = file.lastModified();
		long lastModifiedSync = 0;

		lastModifiedSync = odf.getTimeStamp();

		if (odf.getState() == State.DELETE) {
			System.out.println("Deleting file " + file.getName());
			//files_.remove(odf);
			//file.delete();
		} else if (odf.getState() == State.WAS_DELETED) {
			System.out.println("Deleting design element document " + doc.getUniversalID());
			//lastModifiedMap.remove(file.getAbsoluteFile());
			//doc.removePermanently(true);
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

	}

	protected OnDiskFile find(final AbstractDesignBase elem) {
		return files_.get((elem.getClass().getName() + ":" + elem.getOnDiskName()).toLowerCase());
	}

	public void saveMap() {
		try {
			//serialize design files map
			OutputStream file = new FileOutputStream(timeStampsDesign_);
			OutputStream buffer = new BufferedOutputStream(file);
			ObjectOutput output = new ObjectOutputStream(buffer);
			output.writeObject(files_);
			output.close();
		} catch (IOException e) {
			DominoUtils.handleException(e);
		}
	}

	public List<OnDiskFile> getUnprocessedFiles() {
		List<OnDiskFile> unprocessed = new ArrayList<OnDiskFile>();
		for (OnDiskFile file : files_.values()) {
			if (file.getState() == State.WAS_DELETED || file.getState() == State.WAS_CREATED) {
				unprocessed.add(file);
			}
		}
		return unprocessed;
	}
}

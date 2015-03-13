package org.openntf.domino.tests.rpr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import lotus.domino.NotesException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openntf.domino.Database;
import org.openntf.domino.DbDirectory;
import org.openntf.domino.Session;
import org.openntf.domino.design.ACLNote;
import org.openntf.domino.design.AboutDocument;
import org.openntf.domino.design.AnyFileResource;
import org.openntf.domino.design.AnyFolderOrView;
import org.openntf.domino.design.AnyFormOrSubform;
import org.openntf.domino.design.CompositeApp;
import org.openntf.domino.design.CompositeComponent;
import org.openntf.domino.design.CompositeWiring;
import org.openntf.domino.design.CustomControl;
import org.openntf.domino.design.CustomControlFile;
import org.openntf.domino.design.DB2View;
import org.openntf.domino.design.DataConnectionResource;
import org.openntf.domino.design.DatabaseDesign;
import org.openntf.domino.design.DatabaseScript;
import org.openntf.domino.design.DbImage;
import org.openntf.domino.design.DesignAgent;
import org.openntf.domino.design.DesignAgentA;
import org.openntf.domino.design.DesignAgentF;
import org.openntf.domino.design.DesignAgentIJ;
import org.openntf.domino.design.DesignAgentJ;
import org.openntf.domino.design.DesignAgentLS;
import org.openntf.domino.design.DesignApplet;
import org.openntf.domino.design.DesignBase;
import org.openntf.domino.design.DesignCollection;
import org.openntf.domino.design.DesignForm;
import org.openntf.domino.design.DesignOutline;
import org.openntf.domino.design.DesignPage;
import org.openntf.domino.design.DesignView;
import org.openntf.domino.design.FileResource;
import org.openntf.domino.design.FileResourceHidden;
import org.openntf.domino.design.FileResourceWebContent;
import org.openntf.domino.design.Folder;
import org.openntf.domino.design.Frameset;
import org.openntf.domino.design.IconNote;
import org.openntf.domino.design.ImageResource;
import org.openntf.domino.design.JarResource;
import org.openntf.domino.design.Navigator;
import org.openntf.domino.design.ReplicationFormula;
import org.openntf.domino.design.SavedQuery;
import org.openntf.domino.design.ScriptLibrary;
import org.openntf.domino.design.ScriptLibraryCSJS;
import org.openntf.domino.design.ScriptLibraryJava;
import org.openntf.domino.design.ScriptLibraryLS;
import org.openntf.domino.design.ScriptLibrarySSJS;
import org.openntf.domino.design.SharedActions;
import org.openntf.domino.design.SharedColumn;
import org.openntf.domino.design.SharedField;
import org.openntf.domino.design.StyleSheet;
import org.openntf.domino.design.Subform;
import org.openntf.domino.design.Theme;
import org.openntf.domino.design.UsingDocument;
import org.openntf.domino.design.WebServiceConsumer;
import org.openntf.domino.design.WebServiceConsumerJava;
import org.openntf.domino.design.WebServiceConsumerLS;
import org.openntf.domino.design.WebServiceProvider;
import org.openntf.domino.design.WebServiceProviderJava;
import org.openntf.domino.design.WebServiceProviderLS;
import org.openntf.domino.design.XPage;
import org.openntf.domino.design.XPageFile;
import org.openntf.domino.design.XspJavaResource;
import org.openntf.domino.design.XspResource;
import org.openntf.domino.design.impl.DatabaseClassLoader;
import org.openntf.domino.design.impl.DesignMapping;
import org.openntf.domino.design.impl.OtherDesignElement;
import org.openntf.domino.exceptions.OpenNTFNotesException;
import org.openntf.domino.junit.DominoJUnitRunner;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

@RunWith(DominoJUnitRunner.class)
public class DesignClassTest {

	@SuppressWarnings("unchecked")
	public static Class<? extends DesignBase> clazzes[] = new Class[] { ACLNote.class,//
		AboutDocument.class,//
		// AgentData.class,// AgentData not part of the DesignIndex
		AnyFileResource.class,//
		AnyFolderOrView.class,//
		AnyFormOrSubform.class,//
		CompositeApp.class,//
		CompositeComponent.class,//
		CompositeWiring.class,//
		CustomControl.class,//
		CustomControlFile.class,//
		DB2View.class,//
		DataConnectionResource.class,//
		DatabaseScript.class,//
		DbImage.class,//
		DesignAgent.class,//
		DesignAgentA.class,//
		DesignAgentF.class,//
		DesignAgentIJ.class,//
		DesignAgentJ.class,//
		DesignAgentLS.class,//
		DesignApplet.class,//
		DesignBase.class,//
		//DesignBaseNamed.class,// not yet implemented
		DesignForm.class,//
		DesignOutline.class,//
		DesignPage.class,//
		DesignView.class,//
		FileResource.class,//
		FileResourceHidden.class,//
		FileResourceWebContent.class,//
		Folder.class,//
		Frameset.class,//
		IconNote.class,//
		ImageResource.class,//
		JarResource.class,//
		Navigator.class,//
		ReplicationFormula.class,//
		SavedQuery.class,//
		ScriptLibrary.class,//
		ScriptLibraryCSJS.class,//
		ScriptLibraryJava.class,//
		ScriptLibraryLS.class,//
		ScriptLibrarySSJS.class,//
		SharedActions.class,//
		SharedColumn.class,//
		SharedField.class,//
		StyleSheet.class,//
		Subform.class,//
		Theme.class,//
		UsingDocument.class,//
		WebServiceConsumer.class,//
		WebServiceConsumerJava.class,//
		WebServiceConsumerLS.class,//
		WebServiceProvider.class,//
		WebServiceProviderJava.class,//
		WebServiceProviderLS.class,//
		XPage.class,//
		XPageFile.class,//
		XspJavaResource.class,//
		XspResource.class };

	//@Test
	public void testDBClassLoader() throws ClassNotFoundException {
		Session sess = Factory.getSession(SessionType.CURRENT);
		Database db = sess.getDatabase("D:/Daten/notesdaten_9/localdb/proglib4work2.nsf");
		ClassLoader cl = new DatabaseClassLoader(db.getDesign(), Factory.getClassLoader(), true, true);
		Class<?> cls = cl.loadClass("de.foconis.core.util.LSStringUtil"); // XPage - java
		System.out.println(cls);
		cls = cl.loadClass("org.junit.Assert"); // JAR
		System.out.println(cls);
		//Class<?> cls = cl.loadClass("com.googlecode.htmlcompressor.compressor.Compressor");
		cls = cl.loadClass("FocMessageDigestFactory"); // Script-Lib
		System.out.println(cls);
	}

	//@Test
	public void testDesignFactory() throws IOException {

		Session sess = Factory.getSession(SessionType.CURRENT);
		DbDirectory dir = sess.getDbDirectory("");
		for (Database db : dir) {
			try {
				db.open();
			} catch (OpenNTFNotesException e) {
				e.printStackTrace();
			}
			if (db.isOpen()) {
				System.out.println("DB under test" + db);
				testDb(db);
			}
		}
		//Database db = sess.getDatabase("D:/Daten/notesdaten_9/localdb/proglib4work2.nsf");
		//Database db = sess.getDatabase("D:/Daten/notesdaten_9/localdb/empty.ns9");
	}

	protected void testDb(final Database db) {
		DatabaseDesign design = db.getDesign();
		System.out.println("Design: " + design.getClass().getName());
		DesignCollection<DesignBase> allDesign = design.searchDesignElements("@ALL");
		Map<Class, AtomicInteger> counter = new HashMap<Class, AtomicInteger>();
		for (DesignBase des : allDesign) {
			if (!counter.containsKey(des.getClass())) {
				counter.put(des.getClass(), new AtomicInteger());
			}
			counter.get(des.getClass()).incrementAndGet();
		}
		int i = 0;
		int j = 0;
		//		for (ODPMapping mapping : ODPMapping.values()) {
		//			Class<? extends DesignBase> cls = mapping.getInstanceClass();
		//			if (cls == OtherDesignElement.class) {
		//			} else {
		//				DesignCollection<? extends DesignBase> ret = design.getDesignElements(cls);
		//				j = 0;
		//				for (DesignBase b : ret) {
		//					i++;
		//					j++;
		//				}
		//				int cnt;
		//				if (!counter.containsKey(cls)) {
		//					cnt = 0;
		//				} else {
		//					cnt = counter.get(cls).get();
		//				}
		//				if (j == cnt) {
		//					System.out.println(j + "\t " + cnt + "\t" + cls.getName());
		//				} else {
		//					System.err.println(j + "\t " + cnt + "\t" + cls.getName());
		//				}
		//				//if (j == 0)
		//				//	System.err.println("No design element of " + cls.getName() + " found");
		//			}
		//		}
		System.out.println("Total design elements " + i);
	}

	//@Test
	public void testDesignClass() throws IOException {

		Session sess = Factory.getSession(SessionType.CURRENT);
		//Database db = sess.getDatabase("D:/Daten/notesdaten_9/localdb/empty.ns9");
		Database db = sess.getDatabase("D:/Daten/notesdaten_9/empty2.nsf");

		//Database db = sess.getDatabase("D:/Daten/notesdaten_9/localdb/proglib4work2.nsf");
		//Database db = sess.getDatabase("srv-01-ndev2!!entwicklung/alex/proglib4work22.nsf");
		//	testDb(db);
		DatabaseDesign design = db.getDesign();
		// -X = no AgentData
		DesignCollection<DesignBase> elems = design
				.searchDesignElements("!@Contains($Flags;{X}) & !($TITLE={WEB-INF/classes/plugin/Activator.class}:{$BEProfileR7}) " //
						+ "");
		//		+ "& @IsAvailable($ACLDigest) ");
		//+ "& @contains($TITLE;{gadproxy}) ");
		System.out.println("Count: " + elems.getCount());

		Map<Class<? extends DesignBase>, AtomicInteger> testMap = new HashMap<Class<? extends DesignBase>, AtomicInteger>();
		for (DesignBase elem : elems) {
			System.out.println("FIND:\t" + elem.getClass() + "\t" + elem.toString());
			AtomicInteger i = testMap.get(elem.getClass());
			if (i == null) {
				testMap.put(elem.getClass(), new AtomicInteger(1));
			} else {
				i.incrementAndGet();
			}
		}

		for (Entry<Class<? extends DesignBase>, AtomicInteger> entry : testMap.entrySet()) {
			DesignCollection<? extends DesignBase> deselems = design.getDesignElements(entry.getKey());
			for (DesignBase elem : deselems) {
				System.out.println("QUERY:\t" + elem.getClass() + "\t" + elem.toString());
			}
			assertEquals(entry.getKey().getName(), deselems.getCount(), entry.getValue().get());
		}

		File root = new File("D:/daten/temp/ods3");
		//OnDiskProject odp = new OnDiskProject(root);
		//PrintWriter pw = new PrintWriter(oFile);
		for (DesignBase elem : elems) {
			//System.out.println(elem.getClass().getSimpleName() + "'" + elem.getNoteID() + "\t" + elem.getName() + "\t"
			//		+ elem.getDocument().getItemValueString("$FLAGS"));
			try {
				//odp.export(elem);
				//				//elem.getDxlString(null)
				//				String odp = elem.getOnDiskPath();
				//				if (StringUtil.isEmpty(odp)) {
				//					odp = elem.getNoteID() + ".note";
				//				}
				//				File odsFile = new File(root, odp);
				//				System.out.println(elem.getClass().getName() + "\t\t\t" + odsFile);
				//				odsFile.getParentFile().mkdirs(); // ensure the path exists
				//				elem.writeOnDiskFile(odsFile);
				//				if (elem instanceof HasMetadata) {
				//					File meta = new File(odsFile.getAbsolutePath() + ".metadata");
				//					((HasMetadata) elem).writeOnDiskMeta(meta);
				//				}
			} catch (Exception ne) {
				ne.printStackTrace();
			}
			//			String path = "./" + elem.getOnDiskPath();
			//			String ext = elem.getOnDiskExtension();
			//			if (path != null && ext != null && !path.endsWith(ext))
			//				path = path + ext;
			//			pw.println(path + "\t'" + elem.getClass().getSimpleName() + "\t" + elem.getNoteID() + "\t" + elem.getName() + "\t"
			//					+ elem.getDocument().getItemValueString("$FLAGS"));
			//			if (elem instanceof HasMetadata) {
			//				pw.println(path + ".metadata");
			//			}
			//			if (elem instanceof CustomControl) {
			//				pw.println(path + "-config");
			//			}
		}

	}

	//@Test
	public void testifEveryElementIsFound() throws IOException {
		Session sess = Factory.getSession(SessionType.CURRENT);
		//Database db = sess.getDatabase("D:/Daten/notesdaten_9/localdb/empty.ns9");
		Database db = sess.getDatabase("D:/Daten/notesdaten_9/empty2.nsf");
		//Database db = sess.getDatabase("D:/Daten/notesdaten_9/bookmark.nsf");
		//Database db = sess.getDatabase("D:/Daten/notesdaten_9/localdb/proglib4work2.nsf");
		//Database db = sess.getDatabase("srv-01-ndev2!!entwicklung/alex/proglib4work22.nsf");

		DatabaseDesign design = db.getDesign();
		// -X = no AgentData
		DesignCollection<DesignBase> elems = design.searchDesignElements("!@Contains($Flags;{X})");
		System.out.println("Count: " + elems.getCount());

		Map<Class<? extends DesignBase>, AtomicInteger> testMap = new HashMap<Class<? extends DesignBase>, AtomicInteger>();
		for (DesignBase elem : elems) {
			System.out.println("FIND:\t" + elem.getClass() + "\t" + elem.toString());
			if (!(elem instanceof OtherDesignElement) && !elem.isPrivate()) {
				AtomicInteger i = testMap.get(elem.getClass());
				if (i == null) {
					testMap.put(elem.getClass(), new AtomicInteger(1));
				} else {
					i.incrementAndGet();
				}
			}
		}

		for (Entry<Class<? extends DesignBase>, AtomicInteger> entry : testMap.entrySet()) {

			DesignCollection<? extends DesignBase> deselems = design.getDesignElements(entry.getKey());
			for (DesignBase elem : deselems) {
				System.out.println("QUERY:\t" + elem.getClass() + "\t" + elem.toString());
			}
			assertEquals(entry.getKey().getName(), entry.getValue().get(), deselems.getCount());
		}

	}

	//@Test
	public void testCreation() {
		DbDirectory dbdir = Factory.getSession(SessionType.CURRENT).getDbDirectory("");

		Database db = dbdir.createDatabase("D:/Daten/notesdaten_9/localdb/pw" + System.currentTimeMillis() + ".nsf", true);

		//		AboutDocument abd = new org.openntf.domino.design.impl.AboutDocument(db);
		//
		//		abd.save();
	}

	//@Test
	public void testDXLImport() throws NotesException {
		lotus.domino.Session session = lotus.domino.NotesFactory.createSession(); //Factory.getSession(SessionType.CURRENT);
		lotus.domino.Database db = session.getDatabase("", "D:/Daten/notesdaten_9/empty2.nsf");
		lotus.domino.Stream stream = session.createStream();
		stream.open("d:/daten/form_dxl.txt", "UTF-8");
		lotus.domino.DxlImporter importer = session.createDxlImporter();
		importer.setDesignImportOption(6);
		importer.setCompileLotusScript(false);
		importer.setExitOnFirstFatalError(false);
		importer.setReplicaRequiredForReplaceOrUpdate(false);

		String dxl = stream.readText();
		stream.close();
		System.out.println("Importing....");
		try {
			importer.importDxl(dxl, db);
			System.out.println(importer.getFirstImportedNoteID());
		} catch (NotesException e) {
			e.printStackTrace();
			System.err.println(importer.getLog());
		}

	}

	//@Test
	public void testGetters() {

		Session sess = Factory.getSession(SessionType.CURRENT);
		//Database db = sess.getDatabase("D:/Daten/notesdaten_9/empty2.nsf");
		Database db = sess.getDatabase("D:/Daten/notesdaten_9/localdb/proglib4work2.nsf");
		DatabaseDesign design = db.getDesign();

		for (Class<? extends DesignBase> cls : clazzes) {

			DesignCollection<? extends DesignBase> els = design.getDesignElements(cls);

			if (cls == CustomControlFile.class) {
				// TODO
			} else if (cls == DB2View.class) {
				// TODO
			} else if (cls == DbImage.class) {
				// TODO
			} else if (cls == ReplicationFormula.class) {
				// TODO
			} else if (cls == SavedQuery.class) {
				// TODO
			} else if (cls == XPageFile.class) {
				// TODO
			} else if (cls == DataConnectionResource.class) {
				// TODO
			} else {
				if (els.getCount() <= 0)
					System.err.println(cls.getName() + " not found");
				//assertTrue(cls.getName() + " not found", els.getCount() > 0);
				for (DesignBase el : els) {
					assertTrue(el.getClass().getName() + " not instanceof " + cls.getName(), cls.isAssignableFrom(el.getClass()));
					//System.out.println(el.getClass().getName() + "\tinstanceof\t" + cls.getName());
				}
			}
		}

		for (DesignBase elem : design.getDesignElements()) {
			for (Class<? extends DesignBase> cls : clazzes) {
				if (cls.isAssignableFrom(elem.getClass())) {
					boolean found = false;
					DesignCollection<? extends DesignBase> els = design.getDesignElements(cls);
					for (DesignBase innerElem : els) {
						if (innerElem.getNoteID().equals(elem.getNoteID()))
							found = true;
					}
					assertTrue(elem + ", class: " + elem.getClass().getName() + " not found for type: " + cls.getName(), found);
				}
			}
		}

	}

	@Test
	public void testMapping() {
		File odpRoot = new File("D:\\daten\\odp");
		File odpChild = new File("D:\\daten\\odp\\Xpages/a.Xsp");
		System.out.println(DesignMapping.valueOf(odpRoot, odpChild));
	}
}

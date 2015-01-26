package org.openntf.domino.tests.rpr;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openntf.domino.Database;
import org.openntf.domino.DbDirectory;
import org.openntf.domino.Session;
import org.openntf.domino.design.AboutDocument;
import org.openntf.domino.design.DatabaseDesign;
import org.openntf.domino.design.DesignBase;
import org.openntf.domino.design.DesignCollection;
import org.openntf.domino.design.impl.DatabaseClassLoader;
import org.openntf.domino.design.impl.ODPMapping;
import org.openntf.domino.design.impl.OnDiskProject;
import org.openntf.domino.design.impl.OtherDesignElement;
import org.openntf.domino.exceptions.OpenNTFNotesException;
import org.openntf.domino.junit.DominoJUnitRunner;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

@RunWith(DominoJUnitRunner.class)
public class DesignClassTest {
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
		DesignCollection<DesignBase> allDesign = design.getDesignElements("@ALL");
		Map<Class, AtomicInteger> counter = new HashMap<Class, AtomicInteger>();
		for (DesignBase des : allDesign) {
			if (!counter.containsKey(des.getClass())) {
				counter.put(des.getClass(), new AtomicInteger());
			}
			counter.get(des.getClass()).incrementAndGet();
		}
		int i = 0;
		int j = 0;
		for (ODPMapping mapping : ODPMapping.values()) {
			Class<? extends DesignBase> cls = mapping.getInstanceClass();
			if (cls == OtherDesignElement.class) {
			} else {
				DesignCollection<? extends DesignBase> ret = design.getDesignElements(cls);
				j = 0;
				for (DesignBase b : ret) {
					i++;
					j++;
				}
				int cnt;
				if (!counter.containsKey(cls)) {
					cnt = 0;
				} else {
					cnt = counter.get(cls).get();
				}
				if (j == cnt) {
					System.out.println(j + "\t " + cnt + "\t" + cls.getName());
				} else {
					System.err.println(j + "\t " + cnt + "\t" + cls.getName());
				}
				//if (j == 0)
				//	System.err.println("No design element of " + cls.getName() + " found");
			}
		}
		System.out.println("Total design elements " + i);
	}

	@Test
	public void testDesignClass() throws IOException {

		Session sess = Factory.getSession(SessionType.CURRENT);
		//Database db = sess.getDatabase("D:/Daten/notesdaten_9/localdb/empty.ns9");
		//Database db = sess.getDatabase("D:/Daten/notesdaten_9/empty2.nsf");
		Database db = sess.getDatabase("D:/Daten/notesdaten_9/localdb/proglib4work2.nsf");
		testDb(db);
		DatabaseDesign design = db.getDesign();
		// -X = no AgentData
		DesignCollection<DesignBase> elems = design
				.getDesignElements("!@Contains($Flags;{X}) & !($TITLE={WEB-INF/classes/plugin/Activator.class}:{$BEProfileR7}) " //
						+ "");
		//		+ "& @IsAvailable($ACLDigest) ");
		//+ "& @contains($TITLE;{gadproxy}) ");
		System.out.println("Count: " + elems.getCount());

		File root = new File("D:/daten/temp/ods3");
		OnDiskProject odp = new OnDiskProject(root);
		//PrintWriter pw = new PrintWriter(oFile);
		for (DesignBase elem : elems) {
			//System.out.println(elem.getClass().getSimpleName() + "'" + elem.getNoteID() + "\t" + elem.getName() + "\t"
			//		+ elem.getDocument().getItemValueString("$FLAGS"));
			try {
				odp.export(elem);
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
	public void testCreation() {
		DbDirectory dbdir = Factory.getSession(SessionType.CURRENT).getDbDirectory("");

		Database db = dbdir.createDatabase("D:/Daten/notesdaten_9/localdb/pw" + System.currentTimeMillis() + ".nsf", true);

		AboutDocument abd = new org.openntf.domino.design.impl.AboutDocument(db);

		abd.save();
	}
}

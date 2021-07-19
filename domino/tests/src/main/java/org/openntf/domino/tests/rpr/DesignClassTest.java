/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.tests.rpr;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import lotus.domino.NotesException;

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
		Map<Class<?>, AtomicInteger> counter = new HashMap<Class<?>, AtomicInteger>();
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
				for (@SuppressWarnings("unused")
				DesignBase b : ret) {
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

	//@Test
	public void testDesignClass() throws IOException {

		Session sess = Factory.getSession(SessionType.CURRENT);
		//Database db = sess.getDatabase("D:/Daten/notesdaten_9/localdb/empty.ns9");
		//Database db = sess.getDatabase("D:/Daten/notesdaten_9/empty2.nsf");
		//Database db = sess.getDatabase("D:/Daten/notesdaten_9/localdb/proglib4work2.nsf");
		Database db = sess.getDatabase("srv-01-ndev2!!entwicklung/alex/proglib4work22.nsf");
		testDb(db);
		DatabaseDesign design = db.getDesign();
		// -X = no AgentData
		DesignCollection<DesignBase> elems = design
				.getDesignElements("!@Contains($Flags;{X}) & !($TITLE={WEB-INF/classes/plugin/Activator.class}:{$BEProfileR7}) " //
						+ "");
		//		+ "& @IsAvailable($ACLDigest) ");
		//+ "& @contains($TITLE;{gadproxy}) ");
		System.out.println("Count: " + elems.getCount());

		Path root = Paths.get("D:/daten/temp/ods3");
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

	@Test
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
}

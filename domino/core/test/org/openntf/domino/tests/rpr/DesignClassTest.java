package org.openntf.domino.tests.rpr;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openntf.domino.design.DatabaseDesign;
import org.openntf.domino.design.DesignBase;
import org.openntf.domino.design.DesignCollection;
import org.openntf.domino.design.impl.HasMetadata;
import org.openntf.domino.exceptions.OpenNTFNotesException;
import org.openntf.domino.ext.Database;
import org.openntf.domino.ext.Session;
import org.openntf.domino.junit.DominoJUnitRunner;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

import com.ibm.commons.util.StringUtil;

@RunWith(DominoJUnitRunner.class)
public class DesignClassTest {

	@Test
	public void testDesignClass() throws IOException {

		Session sess = Factory.getSession(SessionType.CURRENT);
		Database db = sess.getDatabase("D:/Daten/notesdaten_9/localdb/proglib4work2.nsf");
		DatabaseDesign design = db.getDesign();
		// -X = no AgentData
		DesignCollection<DesignBase> elems = design
				.getDesignElements("!@Contains($Flags;{X}) & !($TITLE={WEB-INF/classes/plugin/Activator.class}:{$BEProfileR7}) & !@IsAvailable($ACLDigest) "
						+ "");
		//+ "& @contains($TITLE;{.js}) ");
		System.out.println("Count: " + elems.getCount());

		File root = new File("D:/daten/temp/ods");

		//PrintWriter pw = new PrintWriter(oFile);
		for (DesignBase elem : elems) {
			//System.out.println(elem.getClass().getSimpleName() + "'" + elem.getNoteID() + "\t" + elem.getName() + "\t"
			//		+ elem.getDocument().getItemValueString("$FLAGS"));
			try {
				//elem.getDxlString(null)
				String odp = elem.getOnDiskPath();
				if (StringUtil.isEmpty(odp)) {
					odp = elem.getNoteID() + ".note";
				}
				File odsFile = new File(root, odp);
				System.out.println(elem.getClass().getName() + "\t\t\t" + odsFile);
				odsFile.getParentFile().mkdirs(); // ensure the path exists
				elem.writeOnDiskFile(odsFile);
				if (elem instanceof HasMetadata) {
					File meta = new File(odsFile.getAbsolutePath() + ".metadata");
					((HasMetadata) elem).writeOnDiskMeta(meta);
				}
			} catch (OpenNTFNotesException ne) {
				ne.printStackTrace();
				System.out.println(elem.getOnDiskPath());
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
}

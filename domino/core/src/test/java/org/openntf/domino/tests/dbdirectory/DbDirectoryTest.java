package org.openntf.domino.tests.dbdirectory;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openntf.domino.DbDirectory;
import org.openntf.domino.Session;
import org.openntf.domino.design.DxlConverter;
import org.openntf.domino.design.VFSNode;
import org.openntf.domino.design.sync.GitFriendlyDxlConverter;
import org.openntf.domino.junit.DominoJUnitRunner;
import org.openntf.domino.utils.Factory.SessionType;

@RunWith(DominoJUnitRunner.class)
public class DbDirectoryTest {
	DxlConverter converter = new GitFriendlyDxlConverter(false);

	@Test
	public void test() throws IOException {
		// OK
		// System.out.println(SessionType.CURRENT.get());
		Session session = SessionType.CURRENT.get();

		System.out.println("Session: " + session);

		DbDirectory dbDir = session.getDbDirectory("");

		VFSNode dbTree = dbDir.getVFS();//.cd("Test/empty3.nsf/");
		File out = new File("c:/dev/out");
		dumpNode(dbTree, out, "");

		//		VFSNode testFile = dbTree.cd("Resources/Files/HelloWorld.txt");
		//		ByteArrayInputStream is = new ByteArrayInputStream("Hello World".getBytes());
		//		testFile.setContent(converter, is, true);
		//		is.close();

	}

	private void dumpNode(final VFSNode node, File out, final String indent) throws IOException {

		if (node.isDirectory()) {
			out = new File(out, node.getName());
			if (node.isDatabase()) {
				System.out.println(indent + "{" + node.getName() + "}");
				//if (!"vaadin.nsf".equals(node.getName()))
				//	return;
			} else {
				System.out.println(indent + "[" + node.getName() + "]");
			}
		} else {
			if (!out.exists()) {
				out.mkdirs();
			}

			//			out = new File(out, node.getName());
			//			System.out.print(indent + "* " + node.getName());
			//			//			FileOutputStream fos = new FileOutputStream(out);
			//			//			node.getContent(converter, fos);
			//			//			fos.close();
			//			System.out.println(" Size: " + node.getContentLength(converter) + " == " + out.length());

		}
		for (VFSNode child : node.list()) {
			dumpNode(child, out, indent + "  ");
		}

	}

}

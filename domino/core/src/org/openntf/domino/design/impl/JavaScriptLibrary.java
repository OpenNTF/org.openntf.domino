package org.openntf.domino.design.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.EmbeddedObject;
import org.openntf.domino.Session;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.xml.XMLDocument;
import org.openntf.domino.utils.xml.XMLNode;

/**
 * A client side JavaScriptLibrary
 */
public class JavaScriptLibrary extends AbstractDesignBaseNamed implements org.openntf.domino.design.JavaScriptLibrary, HasMetadata {
	private static final long serialVersionUID = 1L;

	/**
	 * @param document
	 */
	protected JavaScriptLibrary(final Document document) {
		super(document);
	}

	protected JavaScriptLibrary(final Database database) {
		super(database);

		try {
			InputStream is = DesignView.class.getResourceAsStream("/org/openntf/domino/design/impl/dxl_javascriptlibrary.xml");
			loadDxl(is);
			is.close();

			// Set some defaults
			Session session = getAncestorSession();
			String dataDirectory = session.getEnvironmentString("Directory", true);
			XMLDocument dxl = getDxl();
			dxl.selectSingleNode("/scriptlibrary/code/javaproject").setAttribute("codepath", dataDirectory);

		} catch (IOException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public Map<String, byte[]> getClassData() {
		// For this one, we'll need the note in the database
		Document doc = getDocument();
		if (doc != null) {
			try {
				EmbeddedObject obj = doc.getAttachment("%%object%%.jar");

				InputStream objInputStream = obj.getInputStream();
				JarInputStream jis = new JarInputStream(objInputStream);
				JarEntry entry = jis.getNextJarEntry();
				Map<String, byte[]> classData = new TreeMap<String, byte[]>();
				while (entry != null) {
					String name = entry.getName();
					if (name.endsWith(".class")) {
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						while (jis.available() > 0) {
							bos.write(jis.read());
						}
						classData.put(DominoUtils.filePathToJavaBinaryName(name, "/"), bos.toByteArray());
					}

					entry = jis.getNextJarEntry();
				}
				jis.close();
				objInputStream.close();

				return classData;

			} catch (IOException e) {
				DominoUtils.handleException(e);
				return null;
			}
		}

		return null;
	}

	@Override
	public Map<String, String> getClassSource() {
		Map<String, String> classSource = new TreeMap<String, String>();
		for (XMLNode node : getDxl().selectNodes("/scriptlibrary/code/javaproject/java")) {
			classSource.put(node.getAttribute("name"), node.getText());
		}

		return classSource;
	}

	@Override
	public String getOnDiskFolder() {
		return "Code/ScriptLibraries";
	}

	@Override
	public String getOnDiskExtension() {
		return ".js";
	}

	//	@Override
	//	public void writeOnDiskFile(final File odsFile) throws IOException {
	//		// TODO Check for $Scriptlib_error => throw exception if item exists
	//		PrintWriter pw = new PrintWriter(odsFile);
	//		for (XMLNode rawitemdata : getDxl().selectNodes("//code/javascript")) {
	//			pw.write(rawitemdata.getText());
	//		}
	//		pw.close();
	//	}
}

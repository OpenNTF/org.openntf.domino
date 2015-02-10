package org.openntf.domino.design.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.xml.XMLNode;

/**
 * A Java - ScriptLibrary.
 * 
 * @author Roland Praml, FOCONIS AG
 */
public final class ScriptLibraryJava extends AbstractDesignFileResource implements org.openntf.domino.design.ScriptLibraryJava, HasMetadata {
	private static final long serialVersionUID = 1L;

	/**
	 * @param document
	 */
	protected ScriptLibraryJava(final Document document) {
		super(document);
	}

	@Override
	protected boolean enforceRawFormat() {
		//return false;

		//we use RAW format for script libraries, so that we are "on disk compatible"
		return true;
	}

	protected ScriptLibraryJava(final Database database) {
		super(database);
	}

	Map<String, byte[]> classData;

	@Override
	public Map<String, byte[]> getClassData() {
		// For this one, we'll need the note in the database
		if (classData == null) {
			classData = new TreeMap<String, byte[]>();
			try {
				byte[] jarData = getFileDataRaw("%%object%%.jar");

				InputStream objInputStream = new ByteArrayInputStream(jarData);
				JarInputStream jis = new JarInputStream(objInputStream);
				JarEntry entry = jis.getNextJarEntry();
				while (entry != null) {
					String name = entry.getName();
					if (name.endsWith(".class")) { //TODO our classloader should support resources also!
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

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return classData;

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
	public void setName(String title) {
		int ind = title.lastIndexOf(".javalib");
		if (ind >= 0) {
			title = title.substring(0, ind);
		}
		super.setName(title);
	}
}

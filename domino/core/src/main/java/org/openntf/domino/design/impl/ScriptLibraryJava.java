/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
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
package org.openntf.domino.design.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.xml.XMLDocument;
import org.openntf.domino.utils.xml.XMLNode;

/**
 * A client side JavaScriptLibrary
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

}

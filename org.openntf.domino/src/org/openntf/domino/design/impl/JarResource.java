/**
 * 
 */
package org.openntf.domino.design.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.logging.Logger;

import org.openntf.domino.Document;
import org.openntf.domino.utils.DominoUtils;

/**
 * @author jgallagher
 * 
 */
public class JarResource extends FileResource implements org.openntf.domino.design.JarResource {
	private static final Logger log_ = Logger.getLogger(JarResource.class.getName());
	private static final long serialVersionUID = 1L;

	protected JarResource(final Document document) {
		super(document);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.JarResource#getClassData()
	 */
	@Override
	public Map<String, byte[]> getClassData() {
		byte[] jarData = getFileData();

		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(jarData);
			JarInputStream jis = new JarInputStream(bis);
			JarEntry entry = jis.getNextJarEntry();
			Map<String, byte[]> classData = new HashMap<String, byte[]>();
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

			return classData;
		} catch (IOException ioe) {
			DominoUtils.handleException(ioe);
			return null;
		}
	}
}

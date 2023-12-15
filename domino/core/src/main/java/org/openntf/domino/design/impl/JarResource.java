/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
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
public final class JarResource extends AbstractDesignFileResource implements org.openntf.domino.design.JarResource, HasMetadata {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(JarResource.class.getName());

	protected JarResource(final Document document) {
		super(document);
	}

	@Override
	protected boolean enforceRawFormat() {
		// JarResource is exported in RAW-format. There is no DXL representation
		return true;
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
				if (name.endsWith(".class")) { //$NON-NLS-1$
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					while (jis.available() > 0) {
						bos.write(jis.read());
					}
					classData.put(DominoUtils.filePathToJavaBinaryName(name, "/"), bos.toByteArray()); //$NON-NLS-1$
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

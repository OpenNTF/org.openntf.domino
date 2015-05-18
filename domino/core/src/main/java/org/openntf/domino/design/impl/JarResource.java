/*
 * Copyright 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package org.openntf.domino.design.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.logging.Logger;

import org.openntf.domino.utils.DominoUtils;

import com.ibm.commons.util.io.ByteStreamCache;

/**
 * @author jgallagher
 * 
 */
public final class JarResource extends AbstractDesignNapiFileResource implements org.openntf.domino.design.JarResource, HasMetadata {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(JarResource.class.getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.JarResource#getClassData()
	 */
	@Override
	public Map<String, byte[]> getClassData() {

		try {
			ByteStreamCache bsc = new ByteStreamCache();
			getFileData(bsc.getOutputStream());
			JarInputStream jis = new JarInputStream(bsc.getInputStream());
			JarEntry entry = jis.getNextJarEntry();

			Map<String, byte[]> classData = new HashMap<String, byte[]>();
			while (entry != null) {
				String name = entry.getName();
				if (name.endsWith(".class")) {
					// FIXME: This will break resource loading!
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

	@Override
	protected String getDefaultFlags() {
		return "34567Cg~";
	}

	@Override
	protected String getDefaultFlagsExt() {
		return "";
	}

}

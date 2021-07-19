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
package org.openntf.domino.design.impl;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.openntf.domino.design.DatabaseDesign;
import org.openntf.domino.design.XspResource;
import org.openntf.domino.utils.DominoUtils;

/**
 * @author jgallagher
 *
 */
public class DatabaseClassLoader extends org.openntf.domino.design.DatabaseClassLoader {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(DatabaseClassLoader.class.getName());

	private final Map<String, byte[]> unloadedClasses_ = new HashMap<String, byte[]>();

	private final boolean includeJars_;
	private boolean loadedJars_ = false;

	private final boolean includeLibraries_;
	private boolean loadedLibraries_ = false;

	private final DatabaseDesign design_;

	public DatabaseClassLoader(final DatabaseDesign design, final ClassLoader parent, final boolean includeJars,
			final boolean includeLibraries) {
		super(parent);
		design_ = design;
		includeJars_ = includeJars;
		includeLibraries_ = includeLibraries;
	}

	@Override
	protected Class<?> findClass(final String name) throws ClassNotFoundException {
		// Check if it's in our pool of in-process classes
		if (unloadedClasses_.containsKey(name)) {
			byte[] classData = unloadedClasses_.remove(name);
			return defineClass(name, classData, 0, classData.length);
		}

		String binaryName = DominoUtils.escapeForFormulaString(DominoUtils.javaBinaryNameToFilePath(name, "/")); //$NON-NLS-1$

		Iterator<XspResource> classes = design_.getDesignElements(XspResource.class,
				String.format("$ClassIndexItem='WEB-INF/classes/%s' ", binaryName)).iterator(); //$NON-NLS-1$
		if (classes.hasNext()) {
			XspResource res = classes.next();
			// Load up our class queue with the data
			unloadedClasses_.putAll(res.getClassData());
			// Now attempt to load the named class
			byte[] classData = unloadedClasses_.remove(name);
			if (classData != null) {
				// It's possible that an old name of the Java class is still lingering in the NSF
				// In that case, we'd reach this point, but not have an actual class available to load
				return defineClass(name, classData, 0, classData.length);
			}
		}

		// It's also possible that it's stored only as a .class file (e.g. secondary, non-inner classes in a .java)

		Iterator<FileResourceWebContent> webContentFiles = design_.getDesignElements(FileResourceWebContent.class,
				String.format("$FileNames='WEB-INF/classes/%s' ", binaryName)).iterator(); //$NON-NLS-1$
		if (webContentFiles.hasNext()) {
			FileResourceWebContent res = webContentFiles.next();
			byte[] classData = res.getFileData();
			return defineClass(name, classData, 0, classData.length);
		}

		// TODO consider changing the below routines to only loop through resources until found,
		// keeping track of which libraries have been loaded

		// If we're here, see if we should look through the Jars - load them all now
		if (includeJars_ && !loadedJars_) {
			for (org.openntf.domino.design.JarResource jar : design_.getJarResources()) {
				System.out.println(jar.getName());
				unloadedClasses_.putAll(jar.getClassData());
			}
			loadedJars_ = true;

			if (unloadedClasses_.containsKey(name)) {
				byte[] classData = unloadedClasses_.remove(name);
				return defineClass(name, classData, 0, classData.length);
			}
		}

		// Now do the same for Java script libraries
		if (includeLibraries_ && !loadedLibraries_) {
			for (org.openntf.domino.design.ScriptLibraryJava lib : design_.getScriptLibrariesJava()) {
				Map<String, byte[]> classData = lib.getClassData();
				unloadedClasses_.putAll(classData);
			}
			loadedLibraries_ = true;

			if (unloadedClasses_.containsKey(name)) {
				byte[] classData = unloadedClasses_.remove(name);
				return defineClass(name, classData, 0, classData.length);
			}
		}

		return super.findClass(name);
	}

	@Override
	public Set<Class<?>> getClassesWithAnnotation(final Class<? extends Annotation> annotationClass) {
		Set<Class<?>> result = new LinkedHashSet<Class<?>>();
		for (String className : design_.getJavaResourceClassNames()) {
			try {
				Class<?> clazz = loadClass(className);
				if (clazz.getAnnotation(annotationClass) != null) {
					result.add(clazz);
				}
			} catch (ClassNotFoundException e) {
				// Ignore - Java resources keep copies of their old names, tripping up the search
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Set<Class<? extends T>> getClassesExtending(final Class<T> superClass) {
		Set<Class<? extends T>> result = new LinkedHashSet<Class<? extends T>>();
		for (String className : design_.getJavaResourceClassNames()) {
			try {
				Class<?> clazz = loadClass(className);
				if (superClass.isAssignableFrom(clazz)) {
					result.add((Class<? extends T>) clazz);
				}
			} catch (ClassNotFoundException e) {
				// Ignore - Java resources keep copies of their old names, tripping up the search
			}
		}
		return result;
	}

	@Override
	public org.openntf.domino.design.DatabaseDesign getParentDesign() {
		return design_;
	}

}

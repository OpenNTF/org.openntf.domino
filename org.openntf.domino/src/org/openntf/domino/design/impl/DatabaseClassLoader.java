/**
 * 
 */
package org.openntf.domino.design.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.logging.Logger;

import org.openntf.domino.Document;
import org.openntf.domino.NoteCollection;
import org.openntf.domino.NoteCollection.SelectOption;
import org.openntf.domino.utils.DominoUtils;

/**
 * @author jgallagher
 * 
 */
public class DatabaseClassLoader extends ClassLoader {
	private static final Logger log_ = Logger.getLogger(DatabaseClassLoader.class.getName());

	private final Map<String, byte[]> unloadedClasses_ = new HashMap<String, byte[]>();
	private final boolean includeJars_;
	private final DatabaseDesign design_;

	public DatabaseClassLoader(final DatabaseDesign design, final ClassLoader parent, final boolean includeJars) {
		super(parent);
		design_ = design;
		includeJars_ = includeJars;
	}

	@Override
	protected Class<?> findClass(final String name) throws ClassNotFoundException {
		// Check if it's in our pool of in-process classes
		if (unloadedClasses_.containsKey(name)) {
			byte[] classData = unloadedClasses_.remove(name);
			return defineClass(name, classData, 0, classData.length);
		}

		String binaryName = DominoUtils.escapeForFormulaString(DominoUtils.javaBinaryNameToFilePath(name, "/"));

		// Check for appropriate design elements in the DB
		NoteCollection notes = design_.getNoteCollection(String.format(
				" @Contains($Flags; 'g') & (@Contains($Flags; '[') | @Contains($Flags; 'K')) & $ClassIndexItem='WEB-INF/classes/%s' ",
				binaryName), EnumSet.of(SelectOption.MISC_FORMAT));
		String noteId = notes.getFirstNoteID();
		if (!noteId.isEmpty()) {
			Document doc = design_.getAncestorDatabase().getDocumentByID(noteId);
			JavaResource res = new JavaResource(doc);
			// Load up our class queue with the data
			unloadedClasses_.putAll(res.getClassData());
			// Now attempt to load the named class
			byte[] classData = unloadedClasses_.remove(name);
			return defineClass(name, classData, 0, classData.length);
		}

		// It's also possible that it's stored only as a .class file (e.g. secondary, non-inner classes in a .java)
		notes = design_.getNoteCollection(
				String.format(" @Contains($Flags; 'g') & @Contains($Flags; 'C') & $FileNames='WEB-INF/classes/%s' ", binaryName),
				EnumSet.of(SelectOption.MISC_FORMAT));
		noteId = notes.getFirstNoteID();
		if (!noteId.isEmpty()) {
			Document doc = design_.getAncestorDatabase().getDocumentByID(noteId);
			FileResource res = new FileResource(doc);
			byte[] classData = res.getFileData();
			return defineClass(name, classData, 0, classData.length);
		}

		// If we're here, see if we should look through the Jars - load them all now
		if (includeJars_) {
			DesignCollection<org.openntf.domino.design.JarResource> jars = design_.getJarResources();
			for (org.openntf.domino.design.JarResource jar : jars) {
				try {
					readJarClasses(jar);
				} catch (IOException e) {
					DominoUtils.handleException(e);
				}
			}

			if (unloadedClasses_.containsKey(name)) {
				byte[] classData = unloadedClasses_.remove(name);
				return defineClass(name, classData, 0, classData.length);
			}
		}

		return super.findClass(name);
	}

	private void readJarClasses(final org.openntf.domino.design.JarResource jar) throws IOException {
		byte[] jarData = jar.getFileData();

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

		unloadedClasses_.putAll(classData);
	}
}

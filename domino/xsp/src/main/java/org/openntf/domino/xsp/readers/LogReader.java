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
/**
 * 
 */
package org.openntf.domino.xsp.readers;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.openntf.domino.utils.Factory;
import org.openntf.domino.xsp.Activator;

import com.ibm.commons.util.io.SharedByteArrayOutputStream;
import com.ibm.commons.util.io.StreamUtil;
import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.SimpleDateFormat;

import lotus.domino.NotesException;
import lotus.notes.addins.DominoServer;
import lotus.notes.addins.ServerAccess;

/**
 * @author Nathan T. Freeman
 * 
 *         Class for reading text files from the server
 */
@SuppressWarnings("nls")
public class LogReader {
	private static final Logger log_ = Logger.getLogger(LogReader.class.getName());
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	private String version = "1.3"; //$NON-NLS-1$
	private static volatile Transformer XFORMER;

	/**
	 * Comparator, comparing last modified time of two files
	 * 
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	public static class FileComparator implements Comparator<Path> {
		@Override
		public int compare(final Path a, final Path b) {
			try {
				return Files.getLastModifiedTime(a).compareTo(Files.getLastModifiedTime(b));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Comparator, comparing filepaths of two files
	 * 
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	public static class FilePathComparator implements Comparator<String> {
		@Override
		public int compare(final String a, final String b) {
			Path pathA = Paths.get(a);
			Path pathB = Paths.get(b);
			try {
				return Files.getLastModifiedTime(pathA).compareTo(Files.getLastModifiedTime(pathB));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Constructor
	 */
	public LogReader() {
	}

	/**
	 * Whether or not the effective user of the Session has access to read logs
	 * 
	 * @param session
	 *            Session (remember you can use sessionAsSigner / sessionAsSignerWithFullAccess)
	 * @return boolean whether user has access
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	private boolean canReadLogs(final lotus.domino.Session session) {
		boolean result = false;
		try {
			String username = session.getEffectiveUserName();
			DominoServer server = new DominoServer();
			result = server.checkServerAccess(username, ServerAccess.PROG_UNRESTRICTED);
			result = result || server.checkServerAccess(username, ServerAccess.VIEW_ONLY_ADMIN);
		} catch (NotesException ne) {
			ne.printStackTrace();
		}
		return result;
	}

	/**
	 * Gets the Domino data folder
	 * 
	 * @return String data folder
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	public String getDataFolder() {
		String filename = Factory.getDataPath();
		filename = filename.replace("\\", "/"); //$NON-NLS-1$ //$NON-NLS-2$
		if (!filename.endsWith("/")) //$NON-NLS-1$
			filename += "/"; //$NON-NLS-1$
		return filename;
	}

	/**
	 * Gets the Domino program folder (notes for XPiNC app running locally)
	 * 
	 * @return String domino folder
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	public String getProgramFolder() {
		String filename = Factory.getProgramPath();
		filename = filename.replace("\\", "/"); //$NON-NLS-1$ //$NON-NLS-2$
		if (!filename.endsWith("/")) //$NON-NLS-1$
			filename += "/"; //$NON-NLS-1$
		return filename;
	}

	/**
	 * Gets a folder based on a key. Options are:
	 * <ul>
	 * <li>tech = IBM_TECHNICAL_SUPPORT</li>
	 * <li>ini = domino folder</li>
	 * <li>jvm = frameworks/rcp/deploy</li>
	 * <li>javapolicy = jvm/lib/security</li>
	 * <li>rcp = domino/workspace/.config</li>
	 * <li>xml = domino/workspace/logs</li>
	 * <li>otherwise param + "/"
	 * </ul>
	 * 
	 * @param section
	 *            String key from which to get folder
	 * @return String folder
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	public String getFolder(final String section) {
		String folder = ""; //$NON-NLS-1$
		if (section.equals("tech")) { //$NON-NLS-1$
			folder = getDataFolder() + "IBM_TECHNICAL_SUPPORT/"; //$NON-NLS-1$
		} else if (section.equals("ini")) { //$NON-NLS-1$
			folder = getProgramFolder();
		} else if (section.equals("jvm")) { //$NON-NLS-1$
			folder = getProgramFolder() + "framework/rcp/deploy/"; //$NON-NLS-1$
		} else if (section.equals("javapolicy")) { //$NON-NLS-1$
			folder = getProgramFolder() + "jvm/lib/security/"; //$NON-NLS-1$
		} else if (section.equals("rcp")) { //$NON-NLS-1$
			folder = getDataFolder() + "domino/workspace/.config/"; //$NON-NLS-1$
		} else if (section.equals("xml")) { //$NON-NLS-1$
			folder = getDataFolder() + "domino/workspace/logs/"; //$NON-NLS-1$
		} else {
			folder = getDataFolder() + section + "/"; //$NON-NLS-1$
		}
		return folder;
	}

	/**
	 * Performs a wildcard matching for the text and pattern provided.
	 * 
	 * @param text
	 *            the text to be tested for matches.
	 * @param pattern
	 *            the pattern to be matched for. This can contain the wildcard character '*' (asterisk).
	 * @return <tt>true</tt> if a match is found, <tt>false</tt> otherwise.
	 * @since org.openntf.domino.xsp 2.5.0
	 * @see "http://www.adarshr.com/papers/wildcard"
	 */
	public static boolean wildCardMatch(String text, final String pattern) {
		// Create the cards by splitting using a RegEx. If more speed
		// is desired, a simpler character based splitting can be done.
		if (pattern == null)
			return true;

		String[] cards = pattern.split("\\*"); //$NON-NLS-1$

		// Iterate over the cards.
		for (String card : cards) {
			int idx = text.indexOf(card);

			// Card not detected in the text.
			if (idx == -1) {
				return false;
			}

			// Move ahead, towards the right of the text.
			text = text.substring(idx + card.length());
		}

		return true;
	}

	/**
	 * Converts the file size to a human readable format, e.g. "48 Kbytes"
	 * 
	 * @param size
	 *            long file size
	 * @return String file size in readable format
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	public static String readableFileSize(final long size) {
		if (size <= 0)
			return "0 bytes";
		final String[] units = new String[] { "bytes", "Kbytes", "Mb", "Gb", "Tb" };
		int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}

	/**
	 * Converts the date to a human readable format
	 * 
	 * @param date
	 *            long
	 * @return String date in format yyyyMMdd'T'hhmmss
	 */
	public static String readableDate(final long date) {
		String DATE_FORMAT = "yyyyMMdd'T'hhmmss"; //$NON-NLS-1$
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		return sdf.format(date);
	}

	/**
	 * Retrieves a list of files in the specified folder
	 * 
	 * @param section
	 *            String key for the folder, passed to {@link #getFolder(String)}
	 * @param filter
	 *            String wildcard search for files
	 * @return List<File> of files in the relevant folder matching the search
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	private List<Path> getFiles(final String section, final String filter) {
		String folder = getFolder(section);
		Path dir = Paths.get(folder);
		
		try {
			return Files.list(dir)
				.filter(Files::isRegularFile)
				.filter(path -> {
					return filter == null || wildCardMatch(path.getFileName().toString(), filter);
				})
				.sorted(new FileComparator())
				.collect(Collectors.toList());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets all files in the specified folder
	 * 
	 * @param section
	 *            String key for the folder, passed to {@link #getFolder(String)}
	 * @return List<File> of files in the relevant folder matching the search
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	private List<Path> getFiles(final String section) {
		return getFiles(section, null);
	}

	/**
	 * Gets a List of filenames in the specified folder
	 * 
	 * @param session
	 *            Session with which to get the files
	 * @param section
	 *            String key for the folder, passed to {@link #getFolder(String)}
	 * @return List<String> of filenames
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	public List<String> getFileNames(final lotus.domino.Session session, final String section) {
		List<String> results = new LinkedList<String>();
		if (canReadLogs(session)) {
			List<Path> files = getFiles(section);
			for (Path file : files) {
				results.add(file.getFileName().toString());
			}
		}
		return results;
	}

	/**
	 * Gets a List of filenames in the specified folder matching the optional filter
	 * 
	 * @param session
	 *            Session with which to get the files
	 * @param section
	 *            String key for the folder, passed to {@link #getFolder(String)}
	 * @param filter
	 *            String wildcard search for files
	 * @return List<String> of filenames in the relevant folder matching the search
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	public List<String> getFileNames(final lotus.domino.Session session, final String section, final String filter) {
		List<String> results = new LinkedList<String>();
		if (canReadLogs(session)) {
			List<Path> files = getFiles(section, filter);
			for (Path file : files) {
				results.add(file.getFileName().toString());
			}
		}
		return results;
	}

	/**
	 * Reads a specific file, just getting any lines which include the filter string
	 * 
	 * @param filename
	 *            String file path and file name
	 * @param filter
	 *            String to search for
	 * @return String lines from the file matching the filter
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	public String readFileFast(final String filename, final String filter) {
		if (filename == null || filename.length() < 1 || filename.equalsIgnoreCase("null")) { //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
		Path file = Paths.get(filename);
		if (Files.isDirectory(file) || !Files.exists(file)) {
			return ""; //$NON-NLS-1$
		}
		StringBuilder result = new StringBuilder();
		try(BufferedReader reader = Files.newBufferedReader(file)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (filter == null) {
					result.append(line);
					result.append("\n"); //$NON-NLS-1$
				} else {
					if (line.indexOf(filter, 0) >= 0) {
						result.append(line);
						result.append("\n"); //$NON-NLS-1$
					}
				}
			}
		} catch (Exception e) {
			log_.log(Level.WARNING, e.toString(), e);
		}

		return result.toString();
	}

	/**
	 * Reads a file
	 * 
	 * @param filename
	 *            String file path and file name
	 * @return String output of the file
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	public String readFileFast(final String filename) {
		return readFileFast(filename, null);
	}

	/**
	 * Reads a stream, just getting any lines which include the filter string
	 * 
	 * @param stream
	 *            SharedByteArrayOutputStream to read
	 * @param filter
	 *            String to search for
	 * @return String lines from the file matching the filter
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	public String readStreamFast(final SharedByteArrayOutputStream stream, final String filter) {
		String result = null;
		try {
			result = StreamUtil.readString(new ByteArrayInputStream(stream.getByteArray()), "UTF-8"); //$NON-NLS-1$
		} catch (Exception e) {
			log_.log(Level.WARNING, e.toString(), e);
		} finally {
			try {
				if (stream != null)
					stream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * Reads a file from IBM_TECHNICAL_SUPPORT folder, just getting any lines which include the filter string
	 * 
	 * @param filename
	 *            file within IBM_TECHNICAL_SUPPORT folder
	 * @param filter
	 *            String to search for
	 * @return String lines from the file matching the filter
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	public String readConsole(String filename, String filter) {
		String html = ""; //$NON-NLS-1$
		if (filename == null)
			return "Please select a file";
		if (filter == null)
			filter = "HTTP"; //$NON-NLS-1$
		filename = getFolder("tech") + filename; //$NON-NLS-1$
		html = readFileFast(filename, filter);
		return html;
	}

	/**
	 * Reads a file from IBM_TECHNICAL_SUPPORT folder
	 * 
	 * @param filename
	 *            String file within IBM_TECHNICAL_SUPPORT folder
	 * @return String output of the file
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	public String readXPages(String filename) {
		String html = ""; //$NON-NLS-1$
		if (filename == null)
			return "Please select a file";
		filename = getFolder("tech") + filename; //$NON-NLS-1$
		html = readFileFast(filename);
		return html;
	}

	/**
	 * Reads the startup.log file
	 * 
	 * @return String output of the file
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	public String readStartup() {
		String html = ""; //$NON-NLS-1$
		String filename = getFolder("xml") + "startup.log"; //$NON-NLS-1$ //$NON-NLS-2$
		html = readFileFast(filename);
		return html;
	}

	/**
	 * Reads the notes.ini
	 * 
	 * @return String output of the file
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	public String readNotesini() {
		String html = ""; //$NON-NLS-1$
		String filename = getFolder("ini") + "notes.ini"; //$NON-NLS-1$ //$NON-NLS-2$
		html = readFileFast(filename);
		return html;
	}

	/**
	 * Reads the jvm.properties
	 * 
	 * @return String output of the file
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	public String readJVM() {
		String html = ""; //$NON-NLS-1$
		String filename = getFolder("jvm") + "jvm.properties"; //$NON-NLS-1$ //$NON-NLS-2$
		html = readFileFast(filename);
		return html;
	}

	/**
	 * Reads the java.policy
	 * 
	 * @return String output of the file
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	public String readJavaPolicy() {
		String html = ""; //$NON-NLS-1$
		String filename = getFolder("javapolicy") + "java.policy"; //$NON-NLS-1$ //$NON-NLS-2$
		html = readFileFast(filename);
		return html;
	}

	/**
	 * Reads the java.pol (if it has been set up)
	 * 
	 * @return String output of the file
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	public String readJavaPol() {
		String html = ""; //$NON-NLS-1$
		String filename = getFolder("javapolicy") + "java.pol"; //$NON-NLS-1$ //$NON-NLS-2$
		html = readFileFast(filename);
		return html;
	}

	/**
	 * Reads the rcpinstall.properties
	 * 
	 * @return String output of the file
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	public String readRCP() {
		String html = ""; //$NON-NLS-1$
		String filename = getFolder("rcp") + "rcpinstall.properties"; //$NON-NLS-1$ //$NON-NLS-2$
		html = readFileFast(filename);
		return html;
	}

	/**
	 * Reads a file in a specified folder
	 * <ul>
	 * <li>section="tech", filename</li>
	 * <li>section="xml", filename or startup.log if filename is null</li>
	 * <li>section="ini", filename in domino folder</li>
	 * <li>section="log", just returns "log"</li>
	 * </ul>
	 * 
	 * @param section
	 *            String key for the folder, passed to {@link #getFolder(String)}
	 * @param filename
	 *            String file within the specified folder
	 * @return String output of file
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	public String readHtml(final String section, String filename) {
		String html = ""; //$NON-NLS-1$
		if (section == null) { /* If the String is null... */
		} else if (section.equals("tech")) { //$NON-NLS-1$
			if (filename == null)
				return "Please select a file";
			filename = getFolder(section) + filename;
			html = readFileFast(filename, null);
		} else if (section.equals("xml")) { //$NON-NLS-1$
			if (filename == null)
				filename = "startup.log"; //$NON-NLS-1$
			filename = getFolder(section) + filename;
			html = readFileFast(filename, null);
		} else if (section.equals("ini")) { //$NON-NLS-1$
			filename = getFolder(section) + filename;
			html = readFileFast(filename);
		} else if (section.equals("log")) { //$NON-NLS-1$
			html = "log"; //$NON-NLS-1$
		} else { /* DEFAULT Operation */
		}
		return html;
	}

	/**
	 * Gets the version of the LogReader
	 * 
	 * @return String version label
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Gets an XML transformer loading the log-transform.xsl resource
	 * 
	 * @return Transformer
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	protected static Transformer getTransformer() {
		if (XFORMER == null) {
			synchronized (LogReader.class) {
				TransformerFactory tFactory = TransformerFactory.newInstance();
				InputStream input = null;
				try {
					input = Activator.getDefault().getResourceAsStream("/resources/log-transform.xsl"); //$NON-NLS-1$
					XFORMER = tFactory.newTransformer(new StreamSource(input));
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (input != null)
							input.close();
					} catch (Exception e) {
						// meh
					}
				}
			}
		}
		return XFORMER;
	}

	/**
	 * Reads a error-log-*.xml or trace-log-*.xml file
	 * 
	 * @param type
	 *            error or trace filename: must be one of the error-log-*.xml or trace-log-*.xml files
	 * @return String output
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	public String readXML(final String type, String filename) {

		// default file
		if (filename == null) {
			return "Please select a file";
		}

		String path = getFolder("xml"); //$NON-NLS-1$

		// Get the number of bytes in the file
		Path file = Paths.get(getFolder("xml")).resolve(filename); //$NON-NLS-1$
		try {
			if (Files.size(file) == 0) {
				log_.log(Level.FINE, "File is empty - return a blank page ...");
				return ""; //$NON-NLS-1$
			}
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}

		// XSL transform
		if (true) {

			try {
				SharedByteArrayOutputStream baos = new SharedByteArrayOutputStream();
				StreamResult result = new StreamResult(baos);

				// read the log file
				StreamSource inputStream = new StreamSource(path + filename);

				// do the transformation
				Transformer xfm = getTransformer();
				synchronized (xfm) {
					xfm.transform(inputStream, result);
				}
				return readStreamFast(baos, null);
			} catch (Exception e) {
				log_.log(Level.WARNING, e.toString(), e);
			}
		}
		// If transformation fails - just display the xml file
		String html = ""; //$NON-NLS-1$
		filename = getFolder("xml") + filename; //$NON-NLS-1$
		html = readFileFast(filename);
		return html;

	}

}

package org.openntf.domino.nsfdata.impldxl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;

import org.openntf.domino.nsfdata.NSFDatabase;
import org.openntf.domino.nsfdata.NSFNote;
import org.openntf.domino.utils.xml.XMLDocument;
import org.xml.sax.SAXException;

public class DXLDatabase implements Serializable, NSFDatabase {
	private static final long serialVersionUID = 1L;
	private static final boolean DEBUG = false;

	private Set<NSFNote> notes_ = new LinkedHashSet<NSFNote>();
	private transient Map<Integer, NSFNote> notesByNoteId_ = new TreeMap<Integer, NSFNote>();
	private transient Map<String, NSFNote> notesByUniversalId_ = new TreeMap<String, NSFNote>();

	public DXLDatabase(final InputStream is) throws IOException, SAXException, ParserConfigurationException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));

		boolean inNote = false;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while (reader.ready()) {
			String aLine = reader.readLine();

			// Assume that we don't have multiple notes per line, but one may end and another may start
			int startIndex = aLine.indexOf("<note ");
			int endIndex = aLine.indexOf("</note>");

			// We may be in a note
			if (inNote) {
				String remainder = aLine.substring(startIndex > -1 ? startIndex : 0, endIndex > -1 ? (endIndex + 7) : aLine.length());
				bos.write(remainder.getBytes());
			}

			// See if we ended
			if (endIndex > -1) {
				ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
				XMLDocument xml = new XMLDocument();
				xml.loadInputStream(bis);

				if (DEBUG)
					System.out.println("want to add note of class " + xml.getDocumentElement().getAttribute("class"));
				DXLNote note = DXLNote.create(xml.getDocumentElement());
				notes_.add(note);
				notesByNoteId_.put(note.getNoteId(), note);
				notesByUniversalId_.put(note.getUniversalId(), note);

				inNote = false;
			}

			if (startIndex > -1) {
				String outputLine = aLine.substring(startIndex, endIndex > -1 ? (endIndex + 7) : aLine.length());
				bos.reset();
				bos.write(outputLine.getBytes());

				inNote = true;
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.nsfdata.impldxl.NSFDatabase#getNotes()
	 */
	@Override
	public Set<NSFNote> getNotes() {
		return Collections.unmodifiableSet(notes_);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.nsfdata.impldxl.NSFDatabase#getNoteById(int)
	 */
	@Override
	public NSFNote getNoteById(final int noteId) {
		return notesByNoteId_.get(noteId);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.nsfdata.impldxl.NSFDatabase#getNoteByUniversalId(java.lang.String)
	 */
	@Override
	public NSFNote getNoteByUniversalId(final String universalId) {
		return notesByUniversalId_.get(universalId);
	}

	/*
	 * Reconstruct the ID views for fast access
	 */
	private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();

		notesByNoteId_ = new TreeMap<Integer, NSFNote>();
		notesByUniversalId_ = new TreeMap<String, NSFNote>();

		for (NSFNote note : notes_) {
			notesByNoteId_.put(note.getNoteId(), note);
			notesByUniversalId_.put(note.getUniversalId(), note);
		}
	}
}

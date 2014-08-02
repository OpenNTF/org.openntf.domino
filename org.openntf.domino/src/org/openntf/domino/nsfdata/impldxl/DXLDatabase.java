package org.openntf.domino.nsfdata.impldxl;

import java.io.IOException;
import java.io.InputStream;
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
import org.openntf.domino.utils.xml.XMLNode;
import org.xml.sax.SAXException;

public class DXLDatabase implements Serializable, NSFDatabase {
	private static final long serialVersionUID = 1L;
	private static final boolean DEBUG = false;

	private Set<NSFNote> notes_ = new LinkedHashSet<NSFNote>();
	private transient Map<Integer, NSFNote> notesByNoteId_ = new TreeMap<Integer, NSFNote>();
	private transient Map<String, NSFNote> notesByUniversalId_ = new TreeMap<String, NSFNote>();

	public DXLDatabase(final InputStream is) throws IOException, SAXException, ParserConfigurationException {
		XMLDocument xml = new XMLDocument();
		xml.loadInputStream(is);

		for (XMLNode noteNode : xml.selectNodes("/database/note")) {
			if (DEBUG)
				System.out.println("want to add note of class " + noteNode.getAttribute("class"));
			DXLNote note = DXLNote.create(noteNode);
			notes_.add(note);
			notesByNoteId_.put(note.getNoteId(), note);
			notesByUniversalId_.put(note.getUniversalId(), note);
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

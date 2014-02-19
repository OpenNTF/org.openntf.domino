/**
 * 
 */
package org.openntf.domino.tests.ntf;

/**
 * @author nfreeman
 * 
 */
public class WebinarSamples {

	public WebinarSamples() {
		// TODO Auto-generated constructor stub
	}

	public void processViewOld(final lotus.domino.View view) {
		lotus.domino.ViewEntryCollection collection = null;
		lotus.domino.ViewEntry currentEntry = null;
		lotus.domino.ViewEntry nextEntry = null;
		try {
			collection = view.getAllEntries();
			currentEntry = collection.getFirstEntry();
			while (currentEntry != null) {
				nextEntry = collection.getNextEntry(currentEntry);
				try {
					currentEntry.getNoteID(); // Do whatever it is you actually want to get done
				} catch (lotus.domino.NotesException ne1) {
					ne1.printStackTrace();
				} finally {
					currentEntry.recycle();
				}
				currentEntry = nextEntry;
			}
		} catch (lotus.domino.NotesException ne) {
			ne.printStackTrace();
		} finally {
			try {
				if (collection != null)
					collection.recycle();
			} catch (lotus.domino.NotesException ne) {

			}
		}
	}

	public void processViewNew(final org.openntf.domino.View view) {
		org.openntf.domino.ViewEntryCollection collection = view.getAllEntries();
		for (org.openntf.domino.ViewEntry entry : collection) {
			entry.getNoteID(); // Do whatever it is you actually want to get done

		}
	}

}

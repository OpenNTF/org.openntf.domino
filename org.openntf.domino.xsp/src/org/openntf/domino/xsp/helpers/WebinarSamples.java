/**
 * 
 */
package org.openntf.domino.xsp.helpers;

import java.util.Map;
import java.util.NavigableSet;

import org.openntf.domino.helpers.DocumentScanner;
import org.openntf.domino.helpers.DocumentSyncHelper;

/**
 * @author nfreeman
 * 
 */
public class WebinarSamples {

	public WebinarSamples() {
		// TODO Auto-generated constructor stub
	}

	// ******* BEGIN AUTO-LOGGING, GARBAGE COLLECTION AND ITERATOR SAMPLES

	public void processViewOld(final lotus.domino.View view) {
		lotus.domino.ViewEntryCollection collection = null;
		lotus.domino.ViewEntry currentEntry = null;
		lotus.domino.ViewEntry nextEntry = null;
		try {
			view.setAutoUpdate(false); 			// Don't be updating the collection underneath us!
			collection = view.getAllEntries();
			currentEntry = collection.getFirstEntry();
			while (currentEntry != null) {
				nextEntry = collection.getNextEntry(currentEntry);
				try { 							// We have to do an inner try/catch because we may want to keep walking the view
					currentEntry.getNoteID();	// Do whatever it is you actually want to get done
				} catch (lotus.domino.NotesException ne1) {
					ne1.printStackTrace();		// Is this even what you want to do with your exception? Probably not.
				} finally {
					currentEntry.recycle();
				}
				currentEntry = nextEntry;		// Note: if these entries happen to point to the same document, bad things will happen.
			}
		} catch (lotus.domino.NotesException ne) {
			ne.printStackTrace();
		} finally {								// Do our recycling in a finally block so it can't be skipped
			if (collection != null) {			// If for any reason you didn't get a collection, you'd throw an NPE
				try {
					collection.recycle();
				} catch (lotus.domino.NotesException ne) {
					// What would you even do with an exception from a .recycle attempt? Unrecycle it?
				}
			}
		}
	}

	public void processViewNew(final org.openntf.domino.View view) {
		for (org.openntf.domino.ViewEntry entry : view.getAllEntries()) {
			entry.getNoteID(); 					// Do whatever it is you actually want to get done
		}
	}

	// ******* END AUTO-LOGGING, GARBAGE COLLECTION AND ITERATOR SAMPLES

	// ******* BEGIN AUTO-BOXING SAMPLES

	public java.util.Date getProcessedDateOld(final lotus.domino.Document doc) {
		java.util.Date result = null;
		try {
			if (doc.hasItem("processDate")) {
				java.util.Vector<?> vector = doc.getItemValue("processDate");
				if (vector != null && !vector.isEmpty()) {
					Object o = vector.get(0);
					if (o != null) {
						if (o instanceof lotus.domino.DateTime) {
							lotus.domino.DateTime datetime = (lotus.domino.DateTime) o;
							try {
								result = datetime.toJavaDate();
							} catch (lotus.domino.NotesException ne1) {
								ne1.printStackTrace();
							} finally {
								datetime.recycle();	// You still have to recycle even if the conversion to java date failed!
							}
						} else {
							// Deal with having gotten something besides a Date, like a DateRange or a Number or a String
						}
					} else {
						// Deal with the vector having null entries
					}
				} else {
					// Deal with having gotten an empty vector (yes, it's possible)
				}
			} else {
				// Deal with the absence of a processDate field
				doc.getItemValue("processDate");	// This will return a Vector with a String of "" if the item isn't present.
			}
		} catch (lotus.domino.NotesException ne) {
			ne.printStackTrace();	// Again, probably not what you actually want to do
		}
		return result;
	}

	public java.util.Date getProcessedDateNew(final org.openntf.domino.Document doc) {
		return doc.getItemValue("processDate", java.util.Date.class);
	}

	public void setProcessedDateOld(final lotus.domino.Document doc, final java.util.Date date) {
		// if this was a List<Date> you could poke yourself in the eye with a pencil instead
		lotus.domino.Database db = null;
		lotus.domino.Session session = null;
		try {
			db = doc.getParentDatabase();
			session = db.getParent();
			lotus.domino.DateTime datetime = session.createDateTime(date);
			try {
				doc.replaceItemValue("processDate", datetime);
			} catch (lotus.domino.NotesException ne1) {
				ne1.printStackTrace();			// Once again, not what you actually want to do here.
			} finally {
				datetime.recycle();
			}
		} catch (lotus.domino.NotesException ne) {
			ne.printStackTrace();
		} finally {								// Apply our standard finally block recycling here
			if (db != null) {
				try {
					db.recycle();				// Uh oh...
				} catch (lotus.domino.NotesException ne) {
					ne.printStackTrace();
				}
			}
			if (session != null) {
				try {
					session.recycle();			// No, wait! I need that to live!
				} catch (lotus.domino.NotesException ne) {
					ne.printStackTrace();
				}
			}
			// Recycling the PARENTS of the argument is super-bad! We shouldn't be using our standard pattern here!
			// Welcome to Dante's ninth circle
		}
	}

	public void setProcessedDateNew(final org.openntf.domino.Document doc, final java.util.Date date) {
		doc.replaceItemValue("processDate", date);		// stores a single date/time value
	}

	public void setProcessedDateListNew(final org.openntf.domino.Document doc, final java.util.List<java.util.Date> dates) {
		doc.replaceItemValue("processDate", dates);		// stores a set of date/time values
	}

	public void setProcessedDateMapNew(final org.openntf.domino.Document doc, final java.util.Map<String, java.util.Date> dateMap) {
		doc.replaceItemValue("processDate", dateMap);	// serializes dateMap, compresses the byte stream and stores it as MIME
	}

	// ******* END AUTO-BOXING SAMPLES

	// ******* BEGIN HELPER SAMPLES

	public void doMapStuff(final org.openntf.domino.Document doc) {
		// Document implements Map<String, Object>
		Object val = doc.get("foo");	// gets contents of item "foo" or returns null;
		doc.put("foo", "bar");			// puts "bar" in item "foo"
		doc.clear();					// removes every item from doc (not recommended)
		doc.size();						// returns the number of items
		Object eval = doc.get("@Adjust(@Modified; 1; 0; 0; 0; 0; 0)");	// AVAILABLE IN M3
	}

	public Map<String, NavigableSet<String>> scanDatabase(final org.openntf.domino.Database db) {
		// DocumentScanner looks at values of all string-based items in a document
		DocumentScanner scanner = new DocumentScanner();
		for (org.openntf.domino.Document doc : db.getAllDocuments()) {
			scanner.processDocument(doc);
		}
		// The FieldTokenMap returns a Map with a key of the item name, and a sorted set of the unique strings in each of those items
		// Sample uses: Predictive typeahead, custom indexing, external term matching
		return scanner.getFieldTokenMap();
	}

	public void syncDatabases(final org.openntf.domino.Database sourceDb, final org.openntf.domino.Database targetDb) {
		// Note: I've already improved this substantially after writing this demo. Will be even easier in M3
		java.util.Map<String, String> syncMap = new java.util.HashMap<String, String>();
		syncMap.put("Name", "CompanyName");
		syncMap.put("Address", "CompanyAddress");
		syncMap.put("City", "CompanyCity");
		syncMap.put("State", "CompanyState");
		syncMap.put("ZIP", "CompanyZIP");
		syncMap.put("@Now", "LastSync");
		DocumentSyncHelper helper = new DocumentSyncHelper(DocumentSyncHelper.Strategy.CREATE_AND_REPLACE, syncMap);
		helper.setTargetServer(targetDb.getServer());
		helper.setTargetFilepath(targetDb.getFilePath());
		helper.setTargetLookupView("byCompanyID");
		// helper.setTargetDatabase(targetDb, "byCompanyID"); // AVAILABLE IN M3

		helper.setSourceKeyFormula("CompID");
		java.util.Date sinceDate = new java.util.Date(0);
		org.openntf.domino.DateTime dt = sourceDb.getAncestorSession().createDateTime(sinceDate);
		org.openntf.domino.DocumentCollection sourceCollection = sourceDb.getModifiedDocuments(dt);
		helper.process(sourceCollection);
		// helper.setTransactionRule(DocumentSyncHelper.TransactionRule.COMMIT_EVERY_SOURCE); // AVAILABLE IN M3
		// helper.processSince(sourceDb, sinceDate); // AVAILABLE IN M3
	}

	// ******* END HELPER SAMPLES

}

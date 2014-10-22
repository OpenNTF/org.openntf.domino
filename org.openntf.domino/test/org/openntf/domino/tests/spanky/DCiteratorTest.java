package org.openntf.domino.tests.spanky;

import java.util.Calendar;
import java.util.Iterator;

import lotus.domino.NotesException;

import org.openntf.calendars.CalendarRange;
import org.openntf.domino.Base;
import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.Session;
import org.openntf.domino.iterators.DocumentCollectionIterator;
import org.openntf.domino.utils.Dates;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

@SuppressWarnings({ "restriction", "deprecation" })
public class DCiteratorTest implements Runnable {
	private static int THREAD_COUNT = 1;

	public static void main(final String[] args) {
		org.openntf.domino.thread.DominoExecutor de = new org.openntf.domino.thread.DominoExecutor(10);
		for (int i = 0; i < THREAD_COUNT; i++) {
			de.execute(new DCiteratorTest());
		}

		de.shutdown();
	}

	public DCiteratorTest() {
		// whatever you might want to do in your constructor, but stay away from Domino objects
	}

	public class MyCollection extends org.openntf.domino.impl.DocumentCollection {

		public MyCollection(final lotus.domino.DocumentCollection delegate, final Base<?> parent) {
			super(delegate, parent);
		}

		@Override
		public Iterator<org.openntf.domino.Document> iterator() {
			return new MyCollectionIterator(this);
		}

	}

	public class MyCollectionIterator extends DocumentCollectionIterator {
		private org.openntf.domino.impl.DocumentCollection _dc;
		private String[] _universalIDs;
		private int _ordpos;
		private int _length;
		private boolean _hasnext;

		public MyCollectionIterator(final org.openntf.domino.impl.DocumentCollection source) {
			super(source);
			this.initialize(source);
		}

		@Override
		public boolean hasNext() {
			return this._hasnext;
		}

		@Override
		public Document next() {
			if (this._hasnext) {
				Document result = this.getNextDocument();
				this._ordpos++;
				this._hasnext = (this._ordpos < this._length);
				return result;
			}

			return null;
		}

		private Document getNextDocument() {
			String unid = null;
			try {
				if (this.hasNext()) {
					unid = this._universalIDs[this._ordpos];
					return this._dc.getParent().getDocumentByUNID(unid);
				}
			} catch (Exception e) {
				System.out.println("************");
				System.out.println("EXCEPTION");
				System.out.println("************");
				System.out.println("UNID: " + unid);
				System.out.println("ordPos: " + this._ordpos);
				DominoUtils.handleException(e);
				e.printStackTrace();
			}

			return null;
		}

		protected void initialize(final org.openntf.domino.impl.DocumentCollection source) {
			this._dc = source;
			lotus.domino.DocumentCollection dc = (lotus.domino.DocumentCollection) org.openntf.domino.impl.Base.getDelegate(source);
			lotus.domino.Document doc = null;
			lotus.domino.Document stupidRecycleHack = null;

			try {
				this._hasnext = false;
				this._ordpos = -1;
				if (null != dc) {
					this._length = dc.getCount();
					if (this._length > 0) {
						doc = dc.getFirstDocument();
						if (null != doc) {
							this._universalIDs = new String[this._length];
							this._ordpos = 0;
							int idx = 0;
							while (null != doc) {
								this._universalIDs[idx] = doc.getUniversalID();
								idx++;
								stupidRecycleHack = doc;
								doc = dc.getNextDocument(stupidRecycleHack);
								DominoUtils.incinerate(stupidRecycleHack);
							}

							this._hasnext = (idx > 0);
						}
					}
				}

			} catch (NotesException e) {
				DominoUtils.handleException(e);
				e.printStackTrace();
			} finally {
				DominoUtils.incinerate(stupidRecycleHack, doc, dc);
			}
		}

		@Override
		public void remove() {
			Document document = this.getNextDocument();
			if (null != document) {
				String unid = this._universalIDs[this._ordpos];
				System.out.println(this.getClass().getName() + ".remove(): " + unid);
				this._dc.remove(document);
				this.initialize(this._dc);
			}
		}

	}

	private CalendarRange iterateCollection(final DocumentCollection source) {
		Calendar calStart = Dates.getCalendar();

		int count = 0;
		for (Document doc : source) {
			System.out.println("nid: " + doc.getNoteID());
			count++;
		}

		System.out.println("Processed " + count + " documents");
		return new CalendarRange(calStart, Dates.getCalendar());
	}

	private CalendarRange iterateCollection(final MyCollection source) {
		Calendar calStart = Dates.getCalendar();

		int count = 0;
		for (Document doc : source) {
			System.out.println("nid: " + doc.getNoteID());
			count++;
		}

		System.out.println("Processed " + count + " documents");
		return new CalendarRange(calStart, Dates.getCalendar());
	}

	@Override
	public void run() {

		long nanosecondsStart = System.nanoTime();
		CalendarRange et = new CalendarRange(Dates.getCalendar(), null);

		try {
			Session session = Factory.getSession();
			//			Session session = this.getSession();
			Database db = session.getDatabase("", "C:/Program Files/IBM/domino/data/events4.nsf");
			System.out.println("Events4: Reading Documents");

			DocumentCollection dc = db.getAllDocuments();
			CalendarRange dcCol = this.iterateCollection(dc);

			int dcCount = dc.getCount();

			DocumentCollection dca = db.getAllDocuments();
			MyCollection mc = new MyCollection((lotus.domino.DocumentCollection) org.openntf.domino.impl.Base.getDelegate(dca), dca);
			CalendarRange mcCol = this.iterateCollection(mc);

			DocumentCollection dcc = db.getAllDocuments();
			CalendarRange dccCol = this.iterateCollection(dcc);

			long nanosecondsEnd = System.nanoTime();
			et.setLast(Dates.getCalendar());

			System.out.println("*");
			System.out.println("*");
			System.out.println("*");
			System.out.println("*");
			System.out.println("Collection contains " + dcCount + " documents.");
			System.out.println("Elapsed Time (DocumentCollection) : " + Dates.getMillisecondsBetween(dcCol.first(), dcCol.last())
					+ " milliseconds");
			System.out.println("Elapsed Time (MyCollection) : " + Dates.getMillisecondsBetween(mcCol.first(), mcCol.last())
					+ " milliseconds");
			System.out.println("Elapsed Time (DocumentCollection 2) : " + Dates.getMillisecondsBetween(dccCol.first(), dccCol.last())
					+ " milliseconds");
			System.out.println("*");

			System.out.println("Elapsed Time (TOTAL) : " + Dates.getMillisecondsBetween(et.first(), et.last()) + " milliseconds");
			System.out.println("Elapsed Time (TOTAL) : " + (nanosecondsEnd - nanosecondsStart) + " nanoseconds");

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}

package org.openntf.domino.tests.spanky;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import lotus.domino.NotesException;

import org.openntf.calendars.CalendarRange;
import org.openntf.domino.Base;
import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.Session;
import org.openntf.domino.iterators.DocumentCollectionIterator;
import org.openntf.domino.utils.CollectionUtils;
import org.openntf.domino.utils.Dates;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Strings;

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
		private boolean _hasnext;

		public MyCollectionIterator(final org.openntf.domino.impl.DocumentCollection source) {
			super(source);
			this.initialize(source);
		}

		@Override
		public boolean hasNext() {
			if (this._hasnext) {
				this._hasnext = (this._ordpos < this._universalIDs.length);
			}

			return this._hasnext;
		}

		@Override
		public Document next() {
			Document result = this.getNextDocument();
			this._ordpos++;
			return result;
		}

		protected void initialize(final org.openntf.domino.impl.DocumentCollection source) {
			//			this._db = source.getParent();
			this._dc = source;
			lotus.domino.DocumentCollection dc = (lotus.domino.DocumentCollection) org.openntf.domino.impl.Base.getDelegate(source);
			lotus.domino.Document doc = null;
			lotus.domino.Document stupidRecycleHack = null;

			try {
				this._hasnext = false;
				this._ordpos = -1;
				if (null != dc) {
					int count = dc.getCount();
					if (count > 0) {
						doc = dc.getFirstDocument();
						if (null != doc) {
							//							this._universalIDs = new String[count];
							//							this._ordpos = 0;
							List<String> unids = new ArrayList<String>();
							while (null != doc) {
								String unid = doc.getUniversalID();
								if (!Strings.isBlankString(unid)) {
									unids.add(unid);
									this._hasnext = true;
									stupidRecycleHack = doc;
									doc = dc.getNextDocument(stupidRecycleHack);
									DominoUtils.incinerate(stupidRecycleHack);
								}
							}

							if (this._hasnext) {
								this._universalIDs = CollectionUtils.getStringArray(unids);
								this._ordpos = 0;
							}
						}
					}
				}

			} catch (NotesException e) {
				DominoUtils.handleException(e);
				e.printStackTrace();
			} finally {
				//				DominoUtils.incinerate(stupidRecycleHack, doc, dc);
				DominoUtils.incinerate(stupidRecycleHack);
				DominoUtils.incinerate(doc);
				DominoUtils.incinerate(dc);
			}
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

	public class MyOtherCollection extends org.openntf.domino.impl.DocumentCollection {

		public MyOtherCollection(final lotus.domino.DocumentCollection delegate, final Base<?> parent) {
			super(delegate, parent);
		}

		@Override
		public Iterator<org.openntf.domino.Document> iterator() {
			return new MyOtherCollectionIterator(this);
		}

	}

	public class MyOtherCollectionIterator extends DocumentCollectionIterator {
		private org.openntf.domino.impl.DocumentCollection _dc;
		private String[] _noteIDs;
		private int _ordpos;
		private int _length;
		private boolean _hasnext;

		public MyOtherCollectionIterator(final org.openntf.domino.impl.DocumentCollection source) {
			super(source);
			this.initialize(source);
		}

		@Override
		public boolean hasNext() {
			return this._hasnext;
		}

		@Override
		public Document next() {
			if (this.hasNext()) {
				Document result = this.getNextDocument();
				this._ordpos++;
				if (this._ordpos >= this._length) {
					this._hasnext = false;
				}
				return result;
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
				if (null != dc) {
					this._length = dc.getCount();
					if (this._length > 0) {
						doc = dc.getFirstDocument();
						if (null != doc) {
							this._hasnext = true;
							this._noteIDs = new String[this._length];
							this._ordpos = 0;
							int idx = 0;
							while (null != doc) {
								this._noteIDs[idx] = doc.getNoteID();
								idx++;
								stupidRecycleHack = doc;
								doc = dc.getNextDocument(stupidRecycleHack);
								DominoUtils.incinerate(stupidRecycleHack);
							}
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

		private Document getNextDocument() {
			String id = null;
			try {
				if (this.hasNext()) {
					id = this._noteIDs[this._ordpos];
					return this._dc.getParent().getDocumentByID(id);
				}
			} catch (Exception e) {
				System.out.println("************");
				System.out.println("EXCEPTION");
				System.out.println("************");
				System.out.println("NoteID: " + id);
				System.out.println("ordPos: " + this._ordpos);
				DominoUtils.handleException(e);
				e.printStackTrace();
			}

			return null;
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove() {
			Document document = this.getNextDocument();
			if (null != document) {
				String id = this._noteIDs[this._ordpos];
				System.out.println(this.getClass().getName() + ".remove(): " + id);
				this._dc.remove(document);
				this.initialize(this._dc);
			}
		}

	}

	private CalendarRange iterateCollection(final DocumentCollection source) {

		try {
			if (null == source) {
				throw new IllegalArgumentException("source is null");
			}
			System.out.println("*");
			System.out.println("*");
			System.out.println("*");
			System.out.println("iterateCollection(final MyOtherCollection source)");

			long nanosecondsStart = System.nanoTime();
			Calendar calStart = Dates.getCalendar();

			int sourceCount = source.getCount();

			int count = 0;
			for (Document doc : source) {
				System.out.println("nid: " + doc.getNoteID());
				count++;
			}

			long nanosecondsEnd = System.nanoTime();
			Calendar calEnd = Dates.getCalendar();

			System.out.println("Collection contains " + sourceCount + " documents.");
			System.out.println("Read " + count + " documents");
			System.out.println("Elapsed Time: " + (nanosecondsEnd - nanosecondsStart) + " nanoseconds");
			System.out.println("Elapsed Time: " + Dates.getMillisecondsBetween(calStart, calEnd) + " milliseconds");

			return new CalendarRange(calStart, calEnd);

		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	private CalendarRange iterateCollection(final MyCollection source) {

		try {
			if (null == source) {
				throw new IllegalArgumentException("source is null");
			}
			System.out.println("*");
			System.out.println("*");
			System.out.println("*");
			System.out.println("iterateCollection(final MyOtherCollection source)");

			long nanosecondsStart = System.nanoTime();
			Calendar calStart = Dates.getCalendar();

			int sourceCount = source.getCount();

			int count = 0;
			for (Document doc : source) {
				System.out.println("nid: " + doc.getNoteID());
				count++;
			}

			long nanosecondsEnd = System.nanoTime();
			Calendar calEnd = Dates.getCalendar();

			System.out.println("Collection contains " + sourceCount + " documents.");
			System.out.println("Read " + count + " documents");
			System.out.println("Elapsed Time: " + (nanosecondsEnd - nanosecondsStart) + " nanoseconds");
			System.out.println("Elapsed Time: " + Dates.getMillisecondsBetween(calStart, calEnd) + " milliseconds");

			return new CalendarRange(calStart, calEnd);

		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	private CalendarRange iterateCollection(final MyOtherCollection source) {

		try {
			if (null == source) {
				throw new IllegalArgumentException("source is null");
			}
			System.out.println("*");
			System.out.println("*");
			System.out.println("*");
			System.out.println("iterateCollection(final MyOtherCollection source)");

			long nanosecondsStart = System.nanoTime();
			Calendar calStart = Dates.getCalendar();

			int sourceCount = source.getCount();

			int count = 0;
			for (Document doc : source) {
				System.out.println("nid: " + doc.getNoteID());
				count++;
			}

			long nanosecondsEnd = System.nanoTime();
			Calendar calEnd = Dates.getCalendar();

			System.out.println("Collection contains " + sourceCount + " documents.");
			System.out.println("Read " + count + " documents");
			System.out.println("Elapsed Time: " + (nanosecondsEnd - nanosecondsStart) + " nanoseconds");
			System.out.println("Elapsed Time: " + Dates.getMillisecondsBetween(calStart, calEnd) + " milliseconds");

			return new CalendarRange(calStart, calEnd);

		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
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

			DocumentCollection dcb = db.getAllDocuments();
			MyOtherCollection moc = new MyOtherCollection((lotus.domino.DocumentCollection) org.openntf.domino.impl.Base.getDelegate(dcb),
					dcb);

			CalendarRange mocCol = this.iterateCollection(moc);

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
			System.out.println("Elapsed Time (MyOtherCollection) : " + Dates.getMillisecondsBetween(mocCol.first(), mocCol.last())
					+ " milliseconds");
			System.out.println("*");

			System.out.println("Elapsed Time (TOTAL) : " + Dates.getMillisecondsBetween(et.first(), et.last()) + " milliseconds");
			System.out.println("Elapsed Time (TOTAL) : " + (nanosecondsEnd - nanosecondsStart) + " nanoseconds");

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	//	protected Session getSession() {
	//		try {
	//			Session session = Factory.fromLotus(NotesFactory.createSession(), Session.class, null);
	//			return session;
	//		} catch (Throwable t) {
	//			DominoUtils.handleException(t);
	//			return null;
	//		}
	//	}
}

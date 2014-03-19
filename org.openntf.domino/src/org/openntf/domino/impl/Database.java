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
package org.openntf.domino.impl;

import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Logger;

import lotus.domino.NotesError;
import lotus.domino.NotesException;

import org.openntf.domino.ACL;
import org.openntf.domino.ACL.Level;
import org.openntf.domino.Agent;
import org.openntf.domino.AutoMime;
import org.openntf.domino.DateTime;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.Form;
import org.openntf.domino.NoteCollection;
import org.openntf.domino.NoteCollection.SelectOption;
import org.openntf.domino.Outline;
import org.openntf.domino.Replication;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.annotations.Incomplete;
import org.openntf.domino.design.impl.DatabaseDesign;
import org.openntf.domino.events.EnumEvent;
import org.openntf.domino.events.IDominoEvent;
import org.openntf.domino.events.IDominoEventFactory;
import org.openntf.domino.exceptions.TransactionAlreadySetException;
import org.openntf.domino.ext.Session.Fixes;
import org.openntf.domino.schema.IDatabaseSchema;
import org.openntf.domino.transactions.DatabaseTransaction;
import org.openntf.domino.types.Encapsulated;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.GregorianCalendar;

// TODO: Auto-generated Javadoc
/**
 * The Class Database.
 */
public class Database extends Base<org.openntf.domino.Database, lotus.domino.Database, Session> implements org.openntf.domino.Database {
	private static final Logger log_ = Logger.getLogger(Database.class.getName());

	/** The server_. */
	private String server_;

	/** The path_. */
	private String path_;

	/** The replid_. */
	private String replid_;

	private String basedOnTemplate_;
	private String templateName_;
	private Date lastModDate_;
	private String title_;
	private Boolean isReplicationDisabled_;
	private AutoMime autoMime_;

	private String ident_;

	/**
	 * Instantiates a new database.
	 * 
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 * @param wf
	 *            the WrapperFactory
	 * @param cpp_id
	 *            the cpp_id
	 */
	public Database(final lotus.domino.Database delegate, final Session parent, final WrapperFactory wf, final long cpp_id) {
		super(delegate, parent, wf, cpp_id, NOTES_DATABASE);
		initialize(delegate, false);
	}

	/**
	 * Instantiates a new database.
	 * 
	 * @deprecated Use {@link #Database(lotus.domino.Database, Session, WrapperFactory, long)} instead
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	@Deprecated
	public Database(final lotus.domino.Database delegate, final lotus.domino.Base parent) {
		this(delegate, (Session) parent, Factory.getWrapperFactory(), 0L);
	}

	/**
	 * This constructor is used in the dbDirectory. The Delegate will get recycled!
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 * @param extendedMetadata
	 *            true if DB should load extended metadata
	 */
	public Database(final lotus.domino.Database delegate, final org.openntf.domino.Base<?> parent, final boolean extendedMetadata) {
		super(delegate, //
				(parent instanceof Session) ? (Session) parent : org.openntf.domino.utils.Factory.getSession(parent), //
				org.openntf.domino.utils.Factory.getWrapperFactory(), 0, NOTES_DATABASE);
		initialize(delegate, extendedMetadata);
		s_recycle(delegate);
	}

	private void initialize(final lotus.domino.Database delegate, final boolean extended) {
		try {
			server_ = delegate.getServer();
		} catch (NotesException e) {
			log_.log(java.util.logging.Level.FINE, "Unable to cache server name for Database due to exception: " + e.text);
		}
		try {
			path_ = delegate.getFilePath();
		} catch (NotesException e) {
			log_.log(java.util.logging.Level.FINE, "Unable to cache filepath for Database due to exception: " + e.text);
		}
		try {
			replid_ = delegate.getReplicaID();
		} catch (NotesException e) {
			log_.log(java.util.logging.Level.FINE, "Unable to cache replica id for Database due to exception: " + e.text);
		}
		try {
			basedOnTemplate_ = delegate.getDesignTemplateName();
		} catch (NotesException e) {
			log_.log(java.util.logging.Level.FINE, "Unable to cache design template name for Database due to exception: " + e.text);
		}
		try {
			templateName_ = delegate.getTemplateName();
		} catch (NotesException e) {
			log_.log(java.util.logging.Level.FINE, "Unable to cache template name for Database due to exception: " + e.text);
		}
		try {
			title_ = delegate.getTitle();
		} catch (NotesException e) {
			log_.log(java.util.logging.Level.FINE, "Unable to cache title for Database due to exception: " + e.text);
		}
		if (extended) {
			try {
				lotus.domino.DateTime dt = delegate.getLastModified();
				lastModDate_ = dt.toJavaDate();
				s_recycle(dt);
			} catch (NotesException e) {
				log_.log(java.util.logging.Level.FINE, "Unable to cache last modification date for Database due to exception: " + e.text);
			}
			try {
				lotus.domino.Replication repl = delegate.getReplicationInfo();
				isReplicationDisabled_ = repl.isDisabled();
				s_recycle(repl);
			} catch (NotesException e) {
				log_.log(java.util.logging.Level.FINE, "Unable to cache replication status for Database due to exception: " + e.text);
			}
		}
		ident_ = System.identityHashCode(getParent()) + "!!!" + server_ + "!!" + path_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#FTDomainSearch(java.lang.String, int, int, int, int, int, java.lang.String)
	 */
	public Document FTDomainSearch(final String query, final int maxDocs, final int sortOpt, final int otherOpt, final int start,
			final int count, final String entryForm) {
		try {
			return fromLotus(getDelegate().FTDomainSearch(query, maxDocs, sortOpt, otherOpt, start, count, entryForm), Document.SCHEMA,
					this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#FTDomainSearch(java.lang.String, int, org.openntf.domino.Database.SortOption, int, int, int,
	 * java.lang.String)
	 */
	@Override
	public Document FTDomainSearch(final String query, final int maxDocs, final FTDomainSortOption sortOpt,
			final Set<FTDomainSearchOption> otherOpt, final int start, final int count, final String entryForm) {
		int nativeOptions = 0;
		for (FTDomainSearchOption option : otherOpt) {
			nativeOptions += option.getValue();
		}

		return FTDomainSearch(query, maxDocs, sortOpt.getValue(), nativeOptions, start, count, entryForm);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#FTSearch(java.lang.String, int, int, int)
	 */
	public DocumentCollection FTSearch(final String query, final int maxDocs, final int sortOpt, final int otherOpt) {
		try {
			return fromLotus(getDelegate().FTSearch(query, maxDocs, sortOpt, otherOpt), DocumentCollection.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#FTSearch(java.lang.String, int, org.openntf.domino.Database.SortOption, int)
	 */
	@Override
	public DocumentCollection FTSearch(final String query, final int maxDocs, final FTSortOption sortOpt, final Set<FTSearchOption> otherOpt) {
		int nativeOptions = 0;
		for (FTSearchOption option : otherOpt) {
			nativeOptions += option.getValue();
		}
		return FTSearch(query, maxDocs, sortOpt.getValue(), nativeOptions);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#FTSearch(java.lang.String, int)
	 */
	public DocumentCollection FTSearch(final String query, final int maxDocs) {
		try {
			return fromLotus(getDelegate().FTSearch(query, maxDocs), DocumentCollection.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#FTSearch(java.lang.String)
	 */
	public DocumentCollection FTSearch(final String query) {
		try {
			return fromLotus(getDelegate().FTSearch(query), DocumentCollection.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#FTSearchRange(java.lang.String, int, int, int, int)
	 */
	public DocumentCollection FTSearchRange(final String query, final int maxDocs, final int sortOpt, final int otherOpt, final int start) {
		try {
			return fromLotus(getDelegate().FTSearchRange(query, maxDocs, sortOpt, otherOpt, start), DocumentCollection.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#FTSearchRange(java.lang.String, int, org.openntf.domino.Database.SortOption, int, int)
	 */
	@Override
	public DocumentCollection FTSearchRange(final String query, final int maxDocs, final FTSortOption sortOpt,
			final Set<FTSearchOption> otherOpt, final int start) {
		int nativeOptions = 0;
		for (FTSearchOption option : otherOpt) {
			nativeOptions += option.getValue();
		}
		return FTSearchRange(query, maxDocs, sortOpt.getValue(), nativeOptions, start);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#compact()
	 */
	public int compact() {
		try {
			return getDelegate().compact();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#compactWithOptions(int, java.lang.String)
	 */
	public int compactWithOptions(final int options, final String spaceThreshold) {
		try {
			return getDelegate().compactWithOptions(options, spaceThreshold);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#compactWithOptions(int)
	 */
	public int compactWithOptions(final int options) {
		try {
			return getDelegate().compactWithOptions(options);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#compactWithOptions(java.lang.String)
	 */
	public int compactWithOptions(final String spaceThreshold) {
		try {
			return getDelegate().compactWithOptions(spaceThreshold);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#createCopy(java.lang.String, java.lang.String, int)
	 */
	public org.openntf.domino.Database createCopy(final String server, final String dbFile, final int maxSize) {
		try {
			return fromLotus(getDelegate().createCopy(server, dbFile, maxSize), Database.SCHEMA, getAncestorSession());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#createCopy(java.lang.String, java.lang.String)
	 */
	public org.openntf.domino.Database createCopy(final String server, final String dbFile) {
		try {
			return fromLotus(getDelegate().createCopy(server, dbFile), Database.SCHEMA, getAncestorSession());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#createDocument()
	 */
	public Document createDocument() {
		//		System.out.println("Generating a new document in " + this.getFilePath());
		//		try {
		//			Thread.sleep(100);
		//		} catch (InterruptedException e1) {
		//			DominoUtils.handleException(e1);
		//			return null;
		//		}
		Document result = null;
		boolean go = true;
		go = fireListener(generateEvent(Events.BEFORE_CREATE_DOCUMENT, this, null));
		if (go) {
			try {
				result = fromLotus(getDelegate().createDocument(), Document.SCHEMA, this);
			} catch (NotesException e) {
				DominoUtils.handleException(e);
			}
			fireListener(generateEvent(Events.AFTER_CREATE_DOCUMENT, this, null));
		}
		//		System.out.println("Returning a newly created document in " + this.getFilePath());
		//		try {
		//			Thread.sleep(100);
		//		} catch (InterruptedException e1) {
		//			DominoUtils.handleException(e1);
		//			return null;
		//		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#createDocument(java.util.Map)
	 */
	@Override
	public Document createDocument(final Map<String, Object> itemValues) {
		Document doc = this.createDocument();
		for (Map.Entry<String, Object> entry : itemValues.entrySet()) {
			doc.replaceItemValue(entry.getKey(), entry.getValue());
		}
		return doc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#createDocument(java.lang.Object[])
	 */
	@Override
	public Document createDocument(final Object... keyValuePairs) {
		Document doc = this.createDocument();
		if (keyValuePairs.length >= 2) {
			for (int i = 0; i < keyValuePairs.length; i += 2) {
				doc.replaceItemValue(keyValuePairs[i].toString(), keyValuePairs[i + 1]);
			}
		}
		return doc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#createDocumentCollection()
	 */
	public DocumentCollection createDocumentCollection() {
		try {
			return fromLotus(getDelegate().createDocumentCollection(), DocumentCollection.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Incomplete
	public DocumentCollection createMergableDocumentCollection() {
		final boolean debug = false;
		try {
			lotus.domino.Database db = getDelegate();
			lotus.domino.DocumentCollection rawColl = getDelegate().search("@False", db.getLastModified(), 1);
			if (rawColl.getCount() > 0) {
				int[] nids = org.openntf.domino.impl.DocumentCollection.toNoteIdArray(rawColl);
				for (int nid : nids) {
					rawColl.subtract(nid);
				}
			}
			org.openntf.domino.DocumentCollection result = fromLotus(rawColl, DocumentCollection.SCHEMA, this);
			return result;
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#createFTIndex(int, boolean)
	 */
	public void createFTIndex(final int options, final boolean recreate) {
		try {
			getDelegate().createFTIndex(options, recreate);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#createFromTemplate(java.lang.String, java.lang.String, boolean, int)
	 */
	public org.openntf.domino.Database createFromTemplate(final String server, final String dbFile, final boolean inherit, final int maxSize) {
		try {
			return fromLotus(getDelegate().createFromTemplate(server, dbFile, inherit, maxSize), Database.SCHEMA, getAncestorSession());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#createFromTemplate(java.lang.String, java.lang.String, boolean)
	 */
	public org.openntf.domino.Database createFromTemplate(final String server, final String dbFile, final boolean inherit) {
		try {
			return fromLotus(getDelegate().createFromTemplate(server, dbFile, inherit), Database.SCHEMA, getAncestorSession());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#createNoteCollection(boolean)
	 */
	public NoteCollection createNoteCollection(final boolean selectAllFlag) {
		try {
			return fromLotus(getDelegate().createNoteCollection(selectAllFlag), NoteCollection.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#createOutline(java.lang.String, boolean)
	 */
	public Outline createOutline(final String name, final boolean defaultOutline) {
		try {
			return fromLotus(getDelegate().createOutline(name, defaultOutline), Outline.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#createOutline(java.lang.String)
	 */
	public Outline createOutline(final String name) {
		try {
			return fromLotus(getDelegate().createOutline(name), Outline.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#createQueryView(java.lang.String, java.lang.String, lotus.domino.View, boolean)
	 */
	public View createQueryView(final String viewName, final String query, final lotus.domino.View templateView,
			final boolean prohibitDesignRefresh) {
		try {
			return fromLotus(getDelegate().createQueryView(viewName, query, toLotus(templateView), prohibitDesignRefresh), View.SCHEMA,
					this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#createQueryView(java.lang.String, java.lang.String, lotus.domino.View)
	 */
	public View createQueryView(final String viewName, final String query, final lotus.domino.View templateView) {
		try {
			return fromLotus(getDelegate().createQueryView(viewName, query, toLotus(templateView)), View.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#createQueryView(java.lang.String, java.lang.String)
	 */
	public View createQueryView(final String viewName, final String query) {
		try {
			return fromLotus(getDelegate().createQueryView(viewName, query), View.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#createReplica(java.lang.String, java.lang.String)
	 */
	public org.openntf.domino.Database createReplica(final String server, final String dbFile) {
		try {
			return fromLotus(getDelegate().createReplica(server, dbFile), Database.SCHEMA, getAncestorSession());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#createView()
	 */
	public View createView() {
		try {
			return fromLotus(getDelegate().createView(), View.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#createView(java.lang.String, java.lang.String, lotus.domino.View, boolean)
	 */
	public View createView(final String viewName, final String selectionFormula, final lotus.domino.View templateView,
			final boolean prohibitDesignRefresh) {
		try {
			return fromLotus(getDelegate().createView(viewName, selectionFormula, toLotus(templateView), prohibitDesignRefresh),
					View.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#createView(java.lang.String, java.lang.String, lotus.domino.View)
	 */
	public View createView(final String viewName, final String selectionFormula, final lotus.domino.View templateView) {
		try {
			return fromLotus(getDelegate().createView(viewName, selectionFormula, toLotus(templateView)), View.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#createView(java.lang.String, java.lang.String)
	 */
	public View createView(final String viewName, final String selectionFormula) {
		try {
			return fromLotus(getDelegate().createView(viewName, selectionFormula), View.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#createView(java.lang.String)
	 */
	public View createView(final String viewName) {
		try {
			return fromLotus(getDelegate().createView(viewName), View.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#enableFolder(java.lang.String)
	 */
	public void enableFolder(final String folder) {
		try {
			getDelegate().enableFolder(folder);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#fixup()
	 */
	public void fixup() {
		try {
			getDelegate().fixup();
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#fixup(int)
	 */
	public void fixup(final int options) {
		try {
			getDelegate().fixup(options);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getACL()
	 */
	public ACL getACL() {
		try {
			return fromLotus(getDelegate().getACL(), ACL.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getACLActivityLog()
	 */
	@SuppressWarnings("unchecked")
	public Vector<String> getACLActivityLog() {
		try {
			return getDelegate().getACLActivityLog();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getAgent(java.lang.String)
	 */
	public Agent getAgent(final String name) {
		try {
			return fromLotus(getDelegate().getAgent(name), Agent.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getAgents()
	 */
	public Vector<org.openntf.domino.Agent> getAgents() {
		try {
			return fromLotusAsVector(getDelegate().getAgents(), org.openntf.domino.Agent.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getAllDocuments()
	 */
	public DocumentCollection getAllDocuments() {
		try {
			return fromLotus(getDelegate().getAllDocuments(), DocumentCollection.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getAllReadDocuments()
	 */
	public DocumentCollection getAllReadDocuments() {
		try {
			return fromLotus(getDelegate().getAllReadDocuments(), DocumentCollection.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getAllReadDocuments(java.lang.String)
	 */
	public DocumentCollection getAllReadDocuments(final String userName) {
		try {
			return fromLotus(getDelegate().getAllReadDocuments(userName), DocumentCollection.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getAllUnreadDocuments()
	 */
	public DocumentCollection getAllUnreadDocuments() {
		try {
			return fromLotus(getDelegate().getAllUnreadDocuments(), DocumentCollection.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getAllUnreadDocuments(java.lang.String)
	 */
	public DocumentCollection getAllUnreadDocuments(final String userName) {
		try {
			return fromLotus(getDelegate().getAllUnreadDocuments(userName), DocumentCollection.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getCategories()
	 */
	public String getCategories() {
		try {
			return getDelegate().getCategories();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getCreated()
	 */
	public DateTime getCreated() {
		try {
			lotus.domino.DateTime dt = getDelegate().getCreated();
			DateTime ret = fromLotus(dt, DateTime.SCHEMA, getAncestorSession());
			s_recycle(dt);
			return ret;
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getCurrentAccessLevel()
	 */
	public int getCurrentAccessLevel() {
		try {
			return getDelegate().getCurrentAccessLevel();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getDB2Schema()
	 */
	public String getDB2Schema() {
		try {
			return getDelegate().getDB2Schema();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public DatabaseDesign getDesign() {
		return new DatabaseDesign(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getDesignTemplateName()
	 */
	public String getDesignTemplateName() {
		try {
			return getDelegate().getDesignTemplateName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getDocumentByID(java.lang.String)
	 */
	public Document getDocumentByID(final String noteid) {
		try {
			return fromLotus(getDelegate().getDocumentByID(noteid), Document.SCHEMA, this);
		} catch (NotesException e) {
			// DominoUtils.handleException(e);
			return null;

		}
	}

	public Document getDocumentByKey(final Serializable key) {
		return this.getDocumentByKey(key, false);
	}

	public Document getDocumentByKey(final Serializable key, final boolean createOnFail) {
		try {
			if (key != null) {
				String checksum = DominoUtils.toUnid(key);

				Document doc = this.getDocumentByUNID(checksum);
				if (doc == null && createOnFail) {
					doc = this.createDocument();
					doc.setUniversalID(checksum);
					doc.replaceItemValue("$Created", new Date());
					doc.replaceItemValue("$$Key", key);
				}
				return doc;

			} else if (createOnFail) {
				log_.log(java.util.logging.Level.WARNING,
						"Document by key requested with null key. This is probably not what you meant to do...");
				Document doc = this.createDocument();
				doc.replaceItemValue("$Created", new Date());
				doc.replaceItemValue("$$Key", "");
				return doc;
			}
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getDocumentByUNID(java.lang.String)
	 */
	public Document getDocumentByUNID(final String unid) {
		try {
			return fromLotus(getDelegate().getDocumentByUNID(unid), Document.SCHEMA, this);
		} catch (NotesException e) {
			if (getAncestorSession().isFixEnabled(Fixes.DOC_UNID_NULLS) && "Invalid universal id".equals(e.text)) {
			} else {
				DominoUtils.handleException(e);
			}
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getDocumentByURL(java.lang.String, boolean, boolean, boolean, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	@SuppressWarnings("unused")
	public Document getDocumentByURL(final String url, final boolean reload, final boolean reloadIfModified, final boolean urlList,
			final String charSet, final String webUser, final String webPassword, final String proxyUser, final String proxyPassword,
			final boolean returnImmediately) {
		try {
			// Let's have some fun with this
			try {
				URL urlObj = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
				conn.connect();
				System.out.println("Headers: " + conn.getHeaderFields());
				System.out.println("Content-type: " + conn.getContentType());
				conn.disconnect();
			} catch (MalformedURLException e) {
				DominoUtils.handleException(e);
			} catch (IOException e) {
				DominoUtils.handleException(e);
			}
			if (true)
				return null;

			return fromLotus(
					getDelegate().getDocumentByURL(url, reload, reloadIfModified, urlList, charSet, webUser, webPassword, proxyUser,
							proxyPassword, returnImmediately), Document.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getDocumentByURL(java.lang.String, boolean)
	 */
	public Document getDocumentByURL(final String url, final boolean reload) {
		// try {
		// return fromLotus(getDelegate().getDocumentByURL(url, reload), Document.class, this);
		// } catch (NotesException e) {
		// DominoUtils.handleException(e);
		// return null;
		//
		// }
		return this.getDocumentByURL(url, reload, reload, false, null, null, null, null, null, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getFTIndexFrequency()
	 */
	public int getFTIndexFrequency() {
		try {
			return getDelegate().getFTIndexFrequency();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getFileFormat()
	 */
	public int getFileFormat() {
		try {
			return getDelegate().getFileFormat();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getFileName()
	 */
	public String getFileName() {
		try {
			return getDelegate().getFileName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getFilePath()
	 */
	public String getFilePath() {
		return path_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getFolderReferencesEnabled()
	 */
	public boolean getFolderReferencesEnabled() {
		try {
			return getDelegate().getFolderReferencesEnabled();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getForm(java.lang.String)
	 */
	public Form getForm(final String name) {
		try {
			return fromLotus(getDelegate().getForm(name), Form.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getForms()
	 */
	public Vector<Form> getForms() {
		try {
			return fromLotusAsVector(getDelegate().getForms(), Form.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getHttpURL()
	 */
	public String getHttpURL() {
		try {
			return getDelegate().getHttpURL();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public String getHttpURL(final boolean usePath) {
		if (usePath) {
			String baseURL = getHttpURL();
			URL url;
			try {
				url = new URL(baseURL);
			} catch (MalformedURLException e) {
				DominoUtils.handleException(e);
				return null;
			}
			String result = url.getProtocol();
			result += "://";
			result += url.getHost();
			result += url.getPort() > -1 ? ":" + url.getPort() : "";
			result += "/" + getFilePath().replace('\\', '/');
			return result;
		} else {
			return getHttpURL();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getLastFTIndexed()
	 */
	public DateTime getLastFTIndexed() {
		try {
			return fromLotus(getDelegate().getLastFTIndexed(), DateTime.SCHEMA, getAncestorSession());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public Date getLastFTIndexedDate() {
		try {
			lotus.domino.DateTime dt = getDelegate().getLastFTIndexed();
			Date ret = DominoUtils.toJavaDateSafe(dt); // recycles the javaDate!
			s_recycle(dt);
			return ret;
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getLastFixup()
	 */
	public DateTime getLastFixup() {
		try {
			return fromLotus(getDelegate().getLastFixup(), DateTime.SCHEMA, getAncestorSession());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public Date getLastFixupDate() {
		try {
			return DominoUtils.toJavaDateSafe(getDelegate().getLastFixup());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getLastModified()
	 */
	public DateTime getLastModified() {
		try {
			return fromLotus(getDelegate().getLastModified(), DateTime.SCHEMA, getAncestorSession());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public Date getLastModifiedDate() {
		if (lastModDate_ != null)
			return lastModDate_;
		try {
			lastModDate_ = DominoUtils.toJavaDateSafe(getDelegate().getLastModified());
			return lastModDate_;
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getLimitRevisions()
	 */
	public double getLimitRevisions() {
		try {
			return getDelegate().getLimitRevisions();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0d;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getLimitUpdatedBy()
	 */
	public double getLimitUpdatedBy() {
		try {
			return getDelegate().getLimitUpdatedBy();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0d;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getListInDbCatalog()
	 */
	public boolean getListInDbCatalog() {
		try {
			return getDelegate().getListInDbCatalog();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getManagers()
	 */
	@SuppressWarnings("unchecked")
	public Vector<String> getManagers() {
		try {
			return getDelegate().getManagers();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getMaxSize()
	 */
	public long getMaxSize() {
		try {
			return getDelegate().getMaxSize();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0L;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getModifiedDocuments()
	 */
	public DocumentCollection getModifiedDocuments() {
		try {
			return fromLotus(getDelegate().getModifiedDocuments(), DocumentCollection.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public DocumentCollection getModifiedDocuments(final java.util.Date since) {
		return getModifiedDocuments(since, ModifiedDocClass.DATA);
	}

	public DocumentCollection getModifiedDocuments(java.util.Date since, final ModifiedDocClass noteClass) {
		try {
			DocumentCollection result;
			if (since == null) {
				since = new Date(0);
			}
			lotus.domino.DateTime tempDT = getAncestorSession().createDateTime(since);
			lotus.domino.DateTime dt = toLotus(tempDT);
			result = fromLotus(getDelegate().getModifiedDocuments(dt, noteClass.getValue()), DocumentCollection.SCHEMA, this);
			if (tempDT instanceof Encapsulated) {
				dt.recycle();
			}
			return result;
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getModifiedDocuments(lotus.domino.DateTime, int)
	 */
	public DocumentCollection getModifiedDocuments(final lotus.domino.DateTime since, final int noteClass) {
		try {
			DocumentCollection result;
			lotus.domino.DateTime dt = toLotus(since);
			result = fromLotus(getDelegate().getModifiedDocuments(dt, noteClass), DocumentCollection.SCHEMA, this);
			if (since instanceof Encapsulated) {
				dt.recycle();
			}
			return result;
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getModifiedDocuments(lotus.domino.DateTime)
	 */
	public DocumentCollection getModifiedDocuments(final lotus.domino.DateTime since) {
		return getModifiedDocuments(since, 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getNotesURL()
	 */
	public String getNotesURL() {
		try {
			return getDelegate().getNotesURL();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getOption(int)
	 */
	public boolean getOption(final int optionName) {
		try {
			return getDelegate().getOption(optionName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getOutline(java.lang.String)
	 */
	public Outline getOutline(final String outlineName) {
		try {
			return fromLotus(getDelegate().getOutline(outlineName), Outline.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public Session getParent() {
		return getAncestor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getPercentUsed()
	 */
	public double getPercentUsed() {
		try {
			return getDelegate().getPercentUsed();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0d;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getProfileDocCollection(java.lang.String)
	 */
	public DocumentCollection getProfileDocCollection(final String profileName) {
		try {
			return fromLotus(getDelegate().getProfileDocCollection(profileName), DocumentCollection.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getProfileDocument(java.lang.String, java.lang.String)
	 */
	public Document getProfileDocument(final String profileName, final String key) {
		try {
			return fromLotus(getDelegate().getProfileDocument(profileName, key), Document.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getReplicaID()
	 */
	public String getReplicaID() {
		return replid_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getReplicationInfo()
	 */
	public Replication getReplicationInfo() {
		try {
			return fromLotus(getDelegate().getReplicationInfo(), Replication.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getServer()
	 */
	public String getServer() {
		return server_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getSize()
	 */
	public double getSize() {
		try {
			return getDelegate().getSize();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0d;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getSizeQuota()
	 */
	public int getSizeQuota() {
		try {
			return getDelegate().getSizeQuota();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getSizeWarning()
	 */
	public long getSizeWarning() {
		try {
			return getDelegate().getSizeWarning();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0L;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getTemplateName()
	 */
	public String getTemplateName() {
		try {
			return getDelegate().getTemplateName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getTitle()
	 */
	public String getTitle() {
		try {
			return getDelegate().getTitle();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getType()
	 */
	public int getType() {
		try {
			return getDelegate().getType();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getURL()
	 */
	public String getURL() {
		try {
			return getDelegate().getURL();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getURLHeaderInfo(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	public String getURLHeaderInfo(final String url, final String header, final String webUser, final String webPassword,
			final String proxyUser, final String proxyPassword) {
		try {
			return getDelegate().getURLHeaderInfo(url, header, webUser, webPassword, proxyUser, proxyPassword);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getUndeleteExpireTime()
	 */
	public int getUndeleteExpireTime() {
		try {
			return getDelegate().getUndeleteExpireTime();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getView(java.lang.String)
	 */
	public View getView(final String name) {
		try {
			View result = fromLotus(getDelegate().getView(name), View.SCHEMA, this);
			if (result != null) {
				if (getAncestorSession().isFixEnabled(Fixes.VIEW_UPDATE_OFF)) {
					result.setAutoUpdate(false);
				}
			}
			return result;
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#getViews()
	 */
	public Vector<org.openntf.domino.View> getViews() {
		try {
			return fromLotusAsVector(getDelegate().getViews(), org.openntf.domino.View.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#grantAccess(java.lang.String, int)
	 */
	public void grantAccess(final String name, final int level) {
		try {
			getDelegate().grantAccess(name, level);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#grantAccess(java.lang.String, org.openntf.domino.ACL.Level)
	 */
	@Override
	public void grantAccess(final String name, final Level level) {
		grantAccess(name, level.getValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#isAllowOpenSoftDeleted()
	 */
	public boolean isAllowOpenSoftDeleted() {
		try {
			return getDelegate().isAllowOpenSoftDeleted();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#isClusterReplication()
	 */
	public boolean isClusterReplication() {
		try {
			return getDelegate().isClusterReplication();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#isConfigurationDirectory()
	 */
	public boolean isConfigurationDirectory() {
		try {
			return getDelegate().isConfigurationDirectory();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#isCurrentAccessPublicReader()
	 */
	public boolean isCurrentAccessPublicReader() {
		try {
			return getDelegate().isCurrentAccessPublicReader();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#isCurrentAccessPublicWriter()
	 */
	public boolean isCurrentAccessPublicWriter() {
		try {
			return getDelegate().isCurrentAccessPublicWriter();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#isDB2()
	 */
	public boolean isDB2() {
		try {
			return getDelegate().isDB2();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#isDelayUpdates()
	 */
	public boolean isDelayUpdates() {
		try {
			return getDelegate().isDelayUpdates();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#isDesignLockingEnabled()
	 */
	public boolean isDesignLockingEnabled() {
		try {
			return getDelegate().isDesignLockingEnabled();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#isDirectoryCatalog()
	 */
	public boolean isDirectoryCatalog() {
		try {
			return getDelegate().isDirectoryCatalog();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#isDocumentLockingEnabled()
	 */
	public boolean isDocumentLockingEnabled() {
		try {
			return getDelegate().isDocumentLockingEnabled();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#isFTIndexed()
	 */
	public boolean isFTIndexed() {
		try {
			return getDelegate().isFTIndexed();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#isInMultiDbIndexing()
	 */
	public boolean isInMultiDbIndexing() {
		try {
			return getDelegate().isInMultiDbIndexing();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#isInService()
	 */
	public boolean isInService() {
		try {
			return getDelegate().isInService();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#isLink()
	 */
	public boolean isLink() {
		try {
			return getDelegate().isLink();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#isMultiDbSearch()
	 */
	public boolean isMultiDbSearch() {
		try {
			return getDelegate().isMultiDbSearch();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#isOpen()
	 */
	public boolean isOpen() {
		try {
			return getDelegate().isOpen();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#isPendingDelete()
	 */
	public boolean isPendingDelete() {
		try {
			return getDelegate().isPendingDelete();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#isPrivateAddressBook()
	 */
	public boolean isPrivateAddressBook() {
		try {
			return getDelegate().isPrivateAddressBook();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#isPublicAddressBook()
	 */
	public boolean isPublicAddressBook() {
		try {
			return getDelegate().isPublicAddressBook();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#markForDelete()
	 */
	public void markForDelete() {
		try {
			getDelegate().markForDelete();
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 */
	public boolean open() {
		try {
			boolean result = false;
			try {
				result = getDelegate().open();
			} catch (NotesException ne) {
				if (NotesError.NOTES_ERR_DBALREADY_OPEN == ne.id) {
					if (log_.isLoggable(java.util.logging.Level.FINE)) {
						log_.log(java.util.logging.Level.FINE, "Suppressing a db already open error because, why?");
					}
				} else {
					DominoUtils.handleException(ne);
					return false;
				}
			}
			if (result) {
				initialize(getDelegate(), false);
			}
			return result;
		} catch (Exception e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#openByReplicaID(java.lang.String, java.lang.String)
	 */
	public boolean openByReplicaID(final String server, final String replicaId) {
		try {
			boolean result = getDelegate().openByReplicaID(server, replicaId);
			if (result) {
				initialize(getDelegate(), false);
			}
			return result;
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#openIfModified(java.lang.String, java.lang.String, lotus.domino.DateTime)
	 */
	public boolean openIfModified(final String server, final String dbFile, final lotus.domino.DateTime modifiedSince) {
		try {
			boolean result = false;
			lotus.domino.DateTime dt = toLotus(modifiedSince);
			result = getDelegate().openIfModified(server, dbFile, dt);
			if (result) {
				initialize(getDelegate(), false);
			}
			dt.recycle();
			return result;
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#openWithFailover(java.lang.String, java.lang.String)
	 */
	public boolean openWithFailover(final String server, final String dbFile) {
		try {
			boolean result = getDelegate().openWithFailover(server, dbFile);
			if (result) {
				initialize(getDelegate(), false);
			}
			return result;
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#queryAccess(java.lang.String)
	 */
	public int queryAccess(final String name) {
		try {
			return getDelegate().queryAccess(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#queryAccessPrivileges(java.lang.String)
	 */
	public int queryAccessPrivileges(final String name) {
		try {
			return getDelegate().queryAccessPrivileges(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#queryAccessRoles(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public Vector<String> queryAccessRoles(final String name) {
		try {
			return getDelegate().queryAccessRoles(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#remove()
	 */
	public void remove() {
		try {
			getDelegate().remove();
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#removeFTIndex()
	 */
	public void removeFTIndex() {
		try {
			getDelegate().removeFTIndex();
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#replicate(java.lang.String)
	 */
	public boolean replicate(final String server) {
		boolean result = true;
		boolean go = true;
		go = fireListener(generateEvent(Events.BEFORE_REPLICATION, this, server));
		if (go) {
			try {
				result = getDelegate().replicate(server);
			} catch (NotesException e) {
				DominoUtils.handleException(e);
				result = false;
			}
			fireListener(generateEvent(Events.AFTER_REPLICATION, this, server));
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#revokeAccess(java.lang.String)
	 */
	public void revokeAccess(final String name) {
		try {
			getDelegate().revokeAccess(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#search(java.lang.String, lotus.domino.DateTime, int)
	 */
	public DocumentCollection search(final String formula, final lotus.domino.DateTime startDate, final int maxDocs) {
		try {
			DocumentCollection result;
			lotus.domino.DateTime dt = toLotus(startDate);
			result = fromLotus(getDelegate().search(formula, dt, maxDocs), DocumentCollection.SCHEMA, this);
			if (startDate instanceof Encapsulated) {
				dt.recycle();
			}
			return result;
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#search(java.lang.String, lotus.domino.DateTime)
	 */
	public DocumentCollection search(final String formula, final lotus.domino.DateTime startDate) {
		return search(formula, startDate, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#search(java.lang.String)
	 */
	public DocumentCollection search(final String formula) {
		try {
			return fromLotus(getDelegate().search(formula), DocumentCollection.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#setAllowOpenSoftDeleted(boolean)
	 */
	public void setAllowOpenSoftDeleted(final boolean flag) {
		try {
			getDelegate().setAllowOpenSoftDeleted(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#setCategories(java.lang.String)
	 */
	public void setCategories(final String categories) {
		try {
			getDelegate().setCategories(categories);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#setDelayUpdates(boolean)
	 */
	public void setDelayUpdates(final boolean flag) {
		try {
			getDelegate().setDelayUpdates(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#setDesignLockingEnabled(boolean)
	 */
	public void setDesignLockingEnabled(final boolean flag) {
		try {
			getDelegate().setDesignLockingEnabled(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#setDocumentLockingEnabled(boolean)
	 */
	public void setDocumentLockingEnabled(final boolean flag) {
		try {
			getDelegate().setDocumentLockingEnabled(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#setFTIndexFrequency(int)
	 */
	public void setFTIndexFrequency(final int frequency) {
		try {
			getDelegate().setFTIndexFrequency(frequency);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#setFolderReferencesEnabled(boolean)
	 */
	public void setFolderReferencesEnabled(final boolean flag) {
		try {
			getDelegate().setFolderReferencesEnabled(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#setInMultiDbIndexing(boolean)
	 */
	public void setInMultiDbIndexing(final boolean flag) {
		try {
			getDelegate().setInMultiDbIndexing(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#setInService(boolean)
	 */
	public void setInService(final boolean flag) {
		try {
			getDelegate().setInService(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#setLimitRevisions(double)
	 */
	public void setLimitRevisions(final double revisions) {
		try {
			getDelegate().setLimitRevisions(revisions);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#setLimitUpdatedBy(double)
	 */
	public void setLimitUpdatedBy(final double updatedBys) {
		try {
			getDelegate().setLimitUpdatedBy(updatedBys);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#setListInDbCatalog(boolean)
	 */
	public void setListInDbCatalog(final boolean flag) {
		try {
			getDelegate().setListInDbCatalog(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#setOption(int, boolean)
	 */
	public void setOption(final int optionName, final boolean flag) {
		try {
			getDelegate().setOption(optionName, flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#setOption(org.openntf.domino.Database.DBOption, boolean)
	 */
	public void setOption(final DBOption optionName, final boolean flag) {
		setOption(optionName.getValue(), flag);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#setSizeQuota(int)
	 */
	public void setSizeQuota(final int quota) {
		try {
			getDelegate().setSizeQuota(quota);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#setSizeWarning(int)
	 */
	public void setSizeWarning(final int warning) {
		try {
			getDelegate().setSizeWarning(warning);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#setTitle(java.lang.String)
	 */
	public void setTitle(final String title) {
		try {
			getDelegate().setTitle(title);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#setUndeleteExpireTime(int)
	 */
	public void setUndeleteExpireTime(final int hours) {
		try {
			getDelegate().setUndeleteExpireTime(hours);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#sign()
	 */
	public void sign() {
		try {
			getDelegate().sign();
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#sign(int, boolean, java.lang.String, boolean)
	 */
	public void sign(final int documentType, final boolean existingSigsOnly, final String name, final boolean nameIsNoteid) {
		try {
			getDelegate().sign(documentType, existingSigsOnly, name, nameIsNoteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#sign(org.openntf.domino.Database.SignDocType, boolean, java.lang.String, boolean)
	 */
	public void sign(final SignDocType documentType, final boolean existingSigsOnly, final String name, final boolean nameIsNoteid) {
		this.sign(documentType.getValue(), existingSigsOnly, name, nameIsNoteid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#sign(int, boolean, java.lang.String)
	 */
	public void sign(final int documentType, final boolean existingSigsOnly, final String name) {
		try {
			getDelegate().sign(documentType, existingSigsOnly, name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#sign(org.openntf.domino.Database.SignDocType, boolean, java.lang.String)
	 */
	public void sign(final SignDocType documentType, final boolean existingSigsOnly, final String name) {
		this.sign(documentType.getValue(), existingSigsOnly, name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#sign(int, boolean)
	 */
	public void sign(final int documentType, final boolean existingSigsOnly) {
		try {
			getDelegate().sign(documentType, existingSigsOnly);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#sign(org.openntf.domino.Database.SignDocType, boolean)
	 */
	public void sign(final SignDocType documentType, final boolean existingSigsOnly) {
		this.sign(documentType.getValue(), existingSigsOnly);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#sign(int)
	 */
	public void sign(final int documentType) {
		try {
			getDelegate().sign(documentType);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#sign(org.openntf.domino.Database.SignDocType)
	 */
	public void sign(final SignDocType documentType) {
		this.sign(documentType.getValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#updateFTIndex(boolean)
	 */
	public void updateFTIndex(final boolean create) {
		try {
			getDelegate().updateFTIndex(create);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	// private DatabaseTransaction currentTransaction_;
	private ThreadLocal<DatabaseTransaction> txnHolder_ = new ThreadLocal<DatabaseTransaction>() {
		@Override
		protected DatabaseTransaction initialValue() {
			return null;
		}

		@Override
		public DatabaseTransaction get() {
			return super.get();
		}

		@Override
		public void set(final DatabaseTransaction value) {
			super.set(value);
		}
	};

	@Override
	public DatabaseTransaction startTransaction() {
		if (txnHolder_.get() == null) {
			DatabaseTransaction txn = new DatabaseTransaction(this);
			//			System.out.println("******START Creating a new DatabaseTransaction for " + getApiPath());
			//			Throwable t = new Throwable();
			//			t.printStackTrace();
			//			System.out.println("******DONE Creating a new DatabaseTransaction for " + getApiPath());
			//			if (!getFilePath().toLowerCase().contains("graph.nsf") || getServer().contains("Shiva")) {
			//			}
			txnHolder_.set(txn);
		}
		return txnHolder_.get();
	}

	public void closeTransaction() {
		txnHolder_.set(null);
	}

	public DatabaseTransaction getTransaction() {
		return txnHolder_.get();
	}

	public void setTransaction(final DatabaseTransaction txn) {
		DatabaseTransaction current = txnHolder_.get();
		if (current == null) {
			txnHolder_.set(txn);
		} else {
			if (!current.equals(txn)) {
				throw new TransactionAlreadySetException(getServer().length() == 0 ? getFilePath() : (getServer() + "!!" + getFilePath()));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.impl.Base#getDelegate()
	 */
	@Override
	protected lotus.domino.Database getDelegate() {
		lotus.domino.Database db = super.getDelegate();
		if (isDead(db)) {
			resurrect();
		}
		return super.getDelegate();
	}

	public void resurrect() { // should only happen if the delegate has been destroyed somehow.
		// TODO: Currently gets session. Need to get session, sessionAsSigner or sessionAsSignerWithFullAccess, as appropriate somwhow
		Session rawSessionUs = getAncestorSession();
		if (rawSessionUs == null) {
			rawSessionUs = org.openntf.domino.utils.Factory.getSession();
		}
		lotus.domino.Session rawSession = toLotus(rawSessionUs);
		try {
			lotus.domino.Database d = rawSession.getDatabase(server_, path_);
			//d.open();
			setDelegate(d, 0);
			if (log_.isLoggable(java.util.logging.Level.FINE)) {
				Throwable t = new Throwable();
				StackTraceElement[] elements = t.getStackTrace();
				log_.log(java.util.logging.Level.FINE, "Database " + (server_.length() < 1 ? "" : server_ + "!!") + path_
						+ "had been recycled and was auto-restored. Changes may have been lost.");

				log_.log(java.util.logging.Level.FINER, elements[1].getClassName() + "." + elements[1].getMethodName() + " ( line "
						+ elements[1].getLineNumber() + ")");
				log_.log(java.util.logging.Level.FINER, elements[2].getClassName() + "." + elements[2].getMethodName() + " ( line "
						+ elements[2].getLineNumber() + ")");
				log_.log(java.util.logging.Level.FINER, elements[3].getClassName() + "." + elements[3].getMethodName() + " ( line "
						+ elements[3].getLineNumber() + ")");
				log_.log(java.util.logging.Level.FINE,
						"If you are using this Database in XPages and have attempted to hold it in an scoped variable between requests, this behavior is normal.");

			}
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public Session getAncestorSession() {
		return this.getParent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ext.Database#compactWithOptions(java.util.EnumSet)
	 */
	@Override
	public int compactWithOptions(final Set<CompactOption> options) {
		int nativeOptions = 0;
		for (CompactOption option : options) {
			nativeOptions += option.getValue();
		}
		return compactWithOptions(nativeOptions);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ext.Database#compactWithOptions(java.util.EnumSet, java.lang.String)
	 */
	@Override
	public int compactWithOptions(final Set<CompactOption> options, final String spaceThreshold) {
		int nativeOptions = 0;
		for (CompactOption option : options) {
			nativeOptions += option.getValue();
		}
		return compactWithOptions(nativeOptions, spaceThreshold);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ext.Database#createFTIndex(java.util.EnumSet, boolean)
	 */
	@Override
	public void createFTIndex(final Set<FTIndexOption> options, final boolean recreate) {
		int nativeOptions = 0;
		for (FTIndexOption option : options) {
			nativeOptions += option.getValue();
		}
		createFTIndex(nativeOptions, recreate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ext.Database#fixup(java.util.EnumSet)
	 */
	@Override
	public void fixup(final Set<FixupOption> options) {
		int nativeOptions = 0;
		for (FixupOption option : options) {
			nativeOptions += option.getValue();
		}
		fixup(nativeOptions);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ext.Database#getModifiedDocuments(lotus.domino.DateTime, org.openntf.domino.Database.ModifiedDocClass)
	 */
	@Override
	public DocumentCollection getModifiedDocuments(final lotus.domino.DateTime since, final ModifiedDocClass noteClass) {
		return getModifiedDocuments(since, noteClass.getValue());
	}

	// public org.openntf.domino.DocumentCollection getModifiedDocuments(Date since, ModifiedDocClass noteClass) {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ext.Database#getOption(org.openntf.domino.Database.DBOption)
	 */
	@Override
	public boolean getOption(final DBOption optionName) {
		return getOption(optionName.getValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ext.Database#setFTIndexFrequency(org.openntf.domino.Database.FTIndexFrequency)
	 */
	@Override
	public void setFTIndexFrequency(final FTIndexFrequency frequency) {
		setFTIndexFrequency(frequency.getValue());
	}

	public lotus.notes.addins.DominoServer getDominoServer() {
		try {
			lotus.notes.addins.DominoServer server = new lotus.notes.addins.DominoServer(getServer());
			return server;
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	public void refreshDesign() {
		try {
			lotus.notes.addins.DominoServer server = getDominoServer();
			server.refreshDesign(getFilePath());
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#FTDomainSearch(java.lang.String, int, org.openntf.domino.Database.FTSortOption, int, int, int,
	 * java.lang.String)
	 */
	@Override
	public org.openntf.domino.Document FTDomainSearch(final String query, final int maxDocs, final FTSortOption sortOpt,
			final int otherOpt, final int start, final int count, final String entryForm) {
		return this.FTDomainSearch(query, maxDocs, sortOpt.getValue(), otherOpt, start, count, entryForm);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#FTSearch(java.lang.String, int, org.openntf.domino.Database.FTSortOption, int)
	 */
	@Override
	public org.openntf.domino.DocumentCollection FTSearch(final String query, final int maxDocs, final FTSortOption sortOpt,
			final int otherOpt) {
		return this.FTSearch(query, maxDocs, sortOpt.getValue(), otherOpt);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Database#FTSearchRange(java.lang.String, int, org.openntf.domino.Database.FTSortOption, int, int)
	 */
	@Override
	public org.openntf.domino.DocumentCollection FTSearchRange(final String query, final int maxDocs, final FTSortOption sortOpt,
			final int otherOpt, final int start) {
		return this.FTSearchRange(query, maxDocs, sortOpt.getValue(), otherOpt, start);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ext.Database#getModifiedNoteCount(lotus.domino.DateTime, org.openntf.domino.NoteCollection.SelectOption)
	 */
	@Override
	public int getModifiedNoteCount(final java.util.Date since, final Set<SelectOption> noteClass) {
		if (since.after(this.getLastModified().toJavaDate()))
			return 0;
		NoteCollection nc = createNoteCollection(false);
		nc.setSinceTime(since);
		nc.setSelectOptions(noteClass);
		nc.buildCollection();
		return nc.getCount();
	}

	public int[] getDailyModifiedNoteCount(final java.util.Date since) {
		Set<SelectOption> noteClass = new java.util.HashSet<SelectOption>();
		noteClass.add(SelectOption.DOCUMENTS);
		return getDailyModifiedNoteCount(since, noteClass);
	}

	public static final int DAILY_ARRAY_LIMIT = 31;

	public int[] getDailyModifiedNoteCount(final java.util.Date since, final Set<SelectOption> noteClass) {
		Date now = new Date();
		Calendar cal = new GregorianCalendar();
		cal.setTime(since);
		int diffDays = cal.fieldDifference(now, Calendar.DAY_OF_YEAR);
		int[] result = null;
		if (diffDays > DAILY_ARRAY_LIMIT) {
			result = new int[DAILY_ARRAY_LIMIT];
		} else {
			result = new int[diffDays];
		}
		cal.setTime(now);
		for (int i = 0; i < result.length; i++) {
			if (i == 0) {
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
			} else {
				cal.add(Calendar.DAY_OF_YEAR, -1);
			}
			result[i] = getModifiedNoteCount(cal.getTime(), noteClass);
		}
		return result;
	}

	public int getModifiedNoteCount(final java.util.Date since) {
		if (since == null) {
			Set<SelectOption> noteClass = new java.util.HashSet<SelectOption>();
			noteClass.add(SelectOption.DOCUMENTS);
			return getModifiedNoteCount(since, noteClass);
		} else {
			java.util.Date last = this.getLastModifiedDate();
			if (since.after(last))
				return 0;
			Set<SelectOption> noteClass = new java.util.HashSet<SelectOption>();
			noteClass.add(SelectOption.DOCUMENTS);
			return getModifiedNoteCount(since, noteClass);
		}
	}

	private IDominoEventFactory localFactory_;

	public IDominoEventFactory getEventFactory() {
		if (localFactory_ == null) {
			return getAncestorSession().getEventFactory();
		}
		return localFactory_;
	}

	public void setEventFactory(final IDominoEventFactory factory) {
		localFactory_ = factory;
	}

	@SuppressWarnings("rawtypes")
	public IDominoEvent generateEvent(final EnumEvent event, final org.openntf.domino.Base source, final Object payload) {
		return getEventFactory().generate(event, source, this, payload);
	}

	@Override
	public boolean equals(final Object other) {
		if (other == null)
			return false;
		if (other instanceof lotus.domino.Database) {
			if (other instanceof Database) {
				return ident_.equalsIgnoreCase(((Database) other).ident_);
			} else {
				return getDelegate().equals(other);
			}
		}
		throw new IllegalArgumentException("Cannot compare a Database with a " + (other == null ? "null" : other.getClass().getName()));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getDelegate().hashCode();
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.Database#openMail()
	 */
	public org.openntf.domino.Database getMail() {
		return getAncestorSession().getDbDirectory(null).openMailDatabase();
	}

	public void openMail() {
		try {
			lotus.domino.Session rawSess = toLotus(getAncestorSession());
			lotus.domino.DbDirectory rawDir = rawSess.getDbDirectory(null);
			lotus.domino.Database rawDb = rawDir.openMailDatabase();
			s_recycle(getDelegate());
			this.setDelegate(rawDb, 0);
			rawDir.recycle();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.Database#getDocumentMap()
	 */
	public Map<Serializable, org.openntf.domino.Document> getDocumentMap() {
		return new DocumentMap();
	}

	private class DocumentMap implements Map<Serializable, org.openntf.domino.Document> {

		@Override
		public boolean isEmpty() {
			return getAllDocuments().isEmpty();
		}

		@Override
		public int size() {
			return getAllDocuments().size();
		}

		@Override
		public boolean containsKey(final Object key) {
			if (!(key instanceof Serializable))
				throw new IllegalArgumentException();
			return get(key) != null;
		}

		@Override
		public org.openntf.domino.Document get(final Object key) {
			if (!(key instanceof Serializable))
				throw new IllegalArgumentException();
			return getDocumentByKey((Serializable) key);
		}

		@Override
		public org.openntf.domino.Document put(final Serializable key, final org.openntf.domino.Document value) {
			// Ignore the value for now
			if (key != null) {
				Document doc = getDocumentByKey(key);
				if (doc == null) {
					Map<String, Object> valueMap = value;
					doc = createDocument(valueMap);
					doc.setUniversalID(DominoUtils.toUnid(key));
					doc.save();
					return null;
				} else {
					return doc;
				}
			}
			return null;
		}

		@Override
		/* (non-Javadoc)
		 * @see java.util.Map#remove(java.lang.Object)
		 */
		public org.openntf.domino.Document remove(final Object key) {
			if (key != null) {
				Document doc = getDocumentByKey(key.toString());
				if (doc != null) {
					doc.remove(false);
				}
				return null;
			}
			return null;
		}

		/* (non-Javadoc)
		 * @see java.util.Map#values()
		 */
		@Override
		public Collection<org.openntf.domino.Document> values() {
			return getAllDocuments();
		}

		/* (non-Javadoc)
		 * @see java.util.Map#clear()
		 */
		@Override
		public void clear() {

		}

		/* (non-Javadoc)
		 * @see java.util.Map#containsValue(java.lang.Object)
		 */
		@Override
		public boolean containsValue(final Object value) {
			return false;
		}

		/* (non-Javadoc)
		 * @see java.util.Map#entrySet()
		 */
		@Override
		public Set<java.util.Map.Entry<Serializable, org.openntf.domino.Document>> entrySet() {
			return null;
		}

		/* (non-Javadoc)
		 * @see java.util.Map#keySet()
		 */
		@Override
		public Set<Serializable> keySet() {
			// Pity NoteCollection doesn't have a .getUNIDs() method
			Set<Serializable> result = new HashSet<Serializable>(size());
			for (org.openntf.domino.Document doc : values()) {
				result.add(doc.getUniversalID());
			}
			return result;
		}

		/* (non-Javadoc)
		 * @see java.util.Map#putAll(java.util.Map)
		 */
		@Override
		public void putAll(final Map<? extends Serializable, ? extends org.openntf.domino.Document> m) {
			for (Map.Entry<? extends Serializable, ? extends org.openntf.domino.Document> entry : m.entrySet()) {
				put(entry.getKey(), entry.getValue());
			}
		}

	}

	public String getApiPath() {
		if (server_.length() > 0)
			return server_ + "!!" + path_;
		return path_;
	}

	private IDatabaseSchema schema_;
	private volatile Boolean isSchemaChecked_ = Boolean.FALSE;

	public IDatabaseSchema getSchema() {
		if (!isSchemaChecked_ && schema_ == null) {
			//TODO some way to load the schema from the design...
			isSchemaChecked_ = Boolean.TRUE;
		}
		return schema_;
	}

	public void setSchema(final IDatabaseSchema schema) {
		schema_ = schema;
		//TODO serialization of the schema into a design file
	}

	public boolean isReplicationDisabled() {
		if (this.isReplicationDisabled_ == null) {
			Replication repl = getReplicationInfo();
			isReplicationDisabled_ = repl.isDisabled();
		}
		return isReplicationDisabled_.booleanValue();
	}

	void setReplication(final boolean value) {
		isReplicationDisabled_ = value;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.impl.Base#findParent(lotus.domino.Base)
	 */
	@Override
	protected Session findParent(final lotus.domino.Database delegate) throws NotesException {
		return fromLotus(delegate.getParent(), Session.SCHEMA, null);
	}

	public AutoMime getAutoMime() {
		if (autoMime_ == null) {
			return getAncestorSession().getAutoMime();
		} else {
			return autoMime_;
		}
	}

	public void setAutoMime(final AutoMime autoMime) {
		autoMime_ = autoMime;
	}
}

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

import java.io.Externalizable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import lotus.domino.NotesError;
import lotus.domino.NotesException;

import org.openntf.domino.ACL;
import org.openntf.domino.ACL.Level;
import org.openntf.domino.Agent;
import org.openntf.domino.AutoMime;
import org.openntf.domino.DateTime;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.DxlExporter;
import org.openntf.domino.ExceptionDetails;
import org.openntf.domino.Form;
import org.openntf.domino.NoteCollection;
import org.openntf.domino.NoteCollection.SelectOption;
import org.openntf.domino.Outline;
import org.openntf.domino.Replication;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.annotations.Incomplete;
import org.openntf.domino.big.LocalNoteList;
import org.openntf.domino.design.DatabaseDesign;
import org.openntf.domino.design.IconNote;
import org.openntf.domino.events.EnumEvent;
import org.openntf.domino.events.IDominoEvent;
import org.openntf.domino.events.IDominoEventFactory;
import org.openntf.domino.exceptions.OpenNTFNotesException;
import org.openntf.domino.exceptions.TransactionAlreadySetException;
import org.openntf.domino.exceptions.UserAccessException;
import org.openntf.domino.ext.Session.Fixes;
import org.openntf.domino.helpers.DatabaseMetaData;
import org.openntf.domino.schema.IDatabaseSchema;
import org.openntf.domino.transactions.DatabaseTransaction;
import org.openntf.domino.types.Encapsulated;
import org.openntf.domino.utils.CollectionUtils;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.enums.DominoEnumUtil;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.GregorianCalendar;

// TODO: Auto-generated Javadoc
/**
 * The Class Database.
 */
public class Database extends BaseResurrectable<org.openntf.domino.Database, lotus.domino.Database, Session>
		implements org.openntf.domino.Database {
	private static final Logger log_ = Logger.getLogger(Database.class.getName());

	/** The server_. */
	private String server_;

	/** The path_. */
	private String path_;

	/** The apiPath */
	private transient String apiPath_;

	/** The fileName */
	private transient String fileName_;

	/** The replid_. */
	private String replid_;

	//private String basedOnTemplate_;
	//private String templateName_;
	//private Date lastModDate_;
	//private String title_;
	private transient Boolean isReplicationDisabled_;
	private AutoMime autoMime_;

	private DatabaseMetaData shadowedMetaData_;

	// private String ident_;

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
	protected Database(final lotus.domino.Database delegate, final Session parent) {
		super(delegate, parent, NOTES_DATABASE);
		initialize(delegate);
	}

	private void initialize(final lotus.domino.Database delegate) {
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
			if (getAncestorSession().isFixEnabled(Fixes.FORCE_HEX_LOWER_CASE)) {
				replid_ = delegate.getReplicaID().toLowerCase();
			} else {
				replid_ = delegate.getReplicaID();
			}
		} catch (NotesException e) {
			log_.log(java.util.logging.Level.FINE, "Unable to cache replica id for Database due to exception: " + e.text);
		}
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
	protected Database(final Session parent, final DatabaseMetaData metaData) {
		super(null, parent, NOTES_DATABASE);
		initialize(metaData);

	}

	protected void initialize(final DatabaseMetaData metaData) {
		shadowedMetaData_ = metaData;
		server_ = metaData.getServer();
		path_ = metaData.getFilePath();
		if (getAncestorSession().isFixEnabled(Fixes.FORCE_HEX_LOWER_CASE)) {
			replid_ = metaData.getReplicaID().toLowerCase();
		} else {
			replid_ = metaData.getReplicaID();
		}
	}

	@Override
	public Document FTDomainSearch(final String query, final int maxDocs, final FTDomainSortOption sortOpt, final int otherOpt,
			final int start, final int count, final String entryForm) {
		return this.FTDomainSearch(query, maxDocs, sortOpt.getValue(), otherOpt, start, count, entryForm);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#FTDomainSearch(java.lang.String, int, int, int, int, int, java.lang.String)
	 */
	@Override
	public Document FTDomainSearch(final String query, final int maxDocs, final int sortOpt, final int otherOpt, final int start,
			final int count, final String entryForm) {
		try {
			return fromLotus(getDelegate().FTDomainSearch(query, maxDocs, sortOpt, otherOpt, start, count, entryForm), Document.SCHEMA,
					this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this, "Query=" + query);
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
	@Override
	public DocumentCollection FTSearch(final String query, final int maxDocs, final int sortOpt, final int otherOpt) {
		try {
			return fromLotus(getDelegate().FTSearch(query, maxDocs, sortOpt, otherOpt), DocumentCollection.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this, "Query=" + query);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#FTSearch(java.lang.String, int, org.openntf.domino.Database.SortOption, int)
	 */
	@Override
	public DocumentCollection FTSearch(final String query, final int maxDocs, final FTSortOption sortOpt,
			final Set<FTSearchOption> otherOpt) {
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
	@Override
	public DocumentCollection FTSearch(final String query, final int maxDocs) {
		try {
			return fromLotus(getDelegate().FTSearch(query, maxDocs), DocumentCollection.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this, "Query=" + query);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#FTSearch(java.lang.String)
	 */
	@Override
	public DocumentCollection FTSearch(final String query) {
		try {
			return fromLotus(getDelegate().FTSearch(query), DocumentCollection.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this, "Query=" + query);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#FTSearchRange(java.lang.String, int, int, int, int)
	 */
	@Override
	public DocumentCollection FTSearchRange(final String query, final int maxDocs, final int sortOpt, final int otherOpt, final int start) {
		try {
			return fromLotus(getDelegate().FTSearchRange(query, maxDocs, sortOpt, otherOpt, start), DocumentCollection.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this, "Query=" + query);
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
	@Override
	public int compact() {
		try {
			return getDelegate().compact();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#compactWithOptions(int, java.lang.String)
	 */
	@Override
	public int compactWithOptions(final int options, final String spaceThreshold) {
		try {
			return getDelegate().compactWithOptions(options, spaceThreshold);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return 0;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#compactWithOptions(int)
	 */
	@Override
	public int compactWithOptions(final int options) {
		try {
			return getDelegate().compactWithOptions(options);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return 0;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#compactWithOptions(java.lang.String)
	 */
	@Override
	public int compactWithOptions(final String spaceThreshold) {
		try {
			return getDelegate().compactWithOptions(spaceThreshold);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return 0;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#createCopy(java.lang.String, java.lang.String, int)
	 */
	@Override
	public org.openntf.domino.Database createCopy(final String server, final String dbFile, final int maxSize) {
		try {
			return fromLotus(getDelegate().createCopy(server, dbFile, maxSize), Database.SCHEMA, getAncestorSession());
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#createCopy(java.lang.String, java.lang.String)
	 */
	@Override
	public org.openntf.domino.Database createCopy(final String server, final String dbFile) {
		try {
			return fromLotus(getDelegate().createCopy(server, dbFile), Database.SCHEMA, getAncestorSession());
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#createDocument()
	 */
	@Override
	public Document createDocument() {
		//		System.out.println("Generating a new document in " + this.getFilePath());
		//		try {
		//			Thread.sleep(100);
		//		} catch (InterruptedException e1) {
		//			DominoUtils.handleException(e1);
		//			return null;
		//		}
		Document result = null;
		boolean go;
		go = !hasListeners() ? true : fireListener(generateEvent(Events.BEFORE_CREATE_DOCUMENT, this, null));
		if (go) {
			try {
				open();
				result = fromLotus(getDelegate().createDocument(), Document.SCHEMA, this);
			} catch (NotesException e) {
				DominoUtils.handleException(e, this);
			}
			if (hasListeners()) {
				fireListener(generateEvent(Events.AFTER_CREATE_DOCUMENT, this, null));
			}
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
	 * @see org.openntf.domino.Database#createDocument(java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Document createDocument(final Object... keyValuePairs) {
		Document doc = this.createDocument();
		if (keyValuePairs.length == 1) {
			if (keyValuePairs[0] instanceof Map) {
				Map<String, Object> itemValues = (Map<String, Object>) keyValuePairs[0];
				for (Map.Entry<String, Object> entry : itemValues.entrySet()) {
					doc.replaceItemValue(entry.getKey(), entry.getValue());
				}
			}
		} else if (keyValuePairs.length >= 2) {
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
	@Override
	public DocumentCollection createDocumentCollection() {
		try {
			if (!getDelegate().isOpen()) {
				getDelegate().open();
			}
			return fromLotus(getDelegate().createDocumentCollection(), DocumentCollection.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;
		}
	}

	@Override
	@Incomplete
	public DocumentCollection createMergeableDocumentCollection() {
		try {
			lotus.domino.Database db = getDelegate();
			if (!db.isOpen()) {
				db.open();
			}
			lotus.domino.DocumentCollection rawColl = getDelegate().search("@False", db.getLastModified(), 1);
			if (rawColl.getCount() > 0) {
				int[] nids = CollectionUtils.getNoteIDs(rawColl);
				for (int nid : nids) {
					rawColl.subtract(nid);
				}
			}
			org.openntf.domino.DocumentCollection result = fromLotus(rawColl, DocumentCollection.SCHEMA, this);
			return result;
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#createFTIndex(int, boolean)
	 */
	@Override
	public void createFTIndex(final int options, final boolean recreate) {
		try {
			getDelegate().createFTIndex(options, recreate);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#createFromTemplate(java.lang.String, java.lang.String, boolean, int)
	 */
	@Override
	public org.openntf.domino.Database createFromTemplate(final String server, final String dbFile, final boolean inherit,
			final int maxSize) {
		try {
			return fromLotus(getDelegate().createFromTemplate(server, dbFile, inherit, maxSize), Database.SCHEMA, getAncestorSession());
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#createFromTemplate(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public org.openntf.domino.Database createFromTemplate(final String server, final String dbFile, final boolean inherit) {
		try {
			return fromLotus(getDelegate().createFromTemplate(server, dbFile, inherit), Database.SCHEMA, getAncestorSession());
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#createNoteCollection(boolean)
	 */
	@Override
	public NoteCollection createNoteCollection(final boolean selectAllFlag) {
		try {
			open();
			return fromLotus(getDelegate().createNoteCollection(selectAllFlag), NoteCollection.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#createOutline(java.lang.String, boolean)
	 */
	@Override
	public Outline createOutline(final String name, final boolean defaultOutline) {
		try {
			return fromLotus(getDelegate().createOutline(name, defaultOutline), Outline.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#createOutline(java.lang.String)
	 */
	@Override
	public Outline createOutline(final String name) {
		try {
			if (!getDelegate().isOpen()) {
				getDelegate().open();
			}
			return fromLotus(getDelegate().createOutline(name), Outline.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#createQueryView(java.lang.String, java.lang.String, lotus.domino.View, boolean)
	 */
	@Override
	public View createQueryView(final String viewName, final String query, final lotus.domino.View templateView,
			final boolean prohibitDesignRefresh) {
		try {
			if (!getDelegate().isOpen()) {
				getDelegate().open();
			}
			return fromLotus(getDelegate().createQueryView(viewName, query, toLotus(templateView), prohibitDesignRefresh), View.SCHEMA,
					this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#createQueryView(java.lang.String, java.lang.String, lotus.domino.View)
	 */
	@Override
	public View createQueryView(final String viewName, final String query, final lotus.domino.View templateView) {
		try {
			if (!getDelegate().isOpen()) {
				getDelegate().open();
			}
			return fromLotus(getDelegate().createQueryView(viewName, query, toLotus(templateView)), View.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#createQueryView(java.lang.String, java.lang.String)
	 */
	@Override
	public View createQueryView(final String viewName, final String query) {
		try {
			if (!getDelegate().isOpen()) {
				getDelegate().open();
			}
			return fromLotus(getDelegate().createQueryView(viewName, query), View.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#createReplica(java.lang.String, java.lang.String)
	 */
	@Override
	public org.openntf.domino.Database createReplica(final String server, final String dbFile) {
		try {
			return fromLotus(getDelegate().createReplica(server, dbFile), Database.SCHEMA, getAncestorSession());
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#createView()
	 */
	@Override
	@Deprecated
	public View createView() {
		try {
			return fromLotus(getDelegate().createView(), View.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#createView(java.lang.String, java.lang.String, lotus.domino.View, boolean)
	 */
	@Override
	public View createView(final String viewName, final String selectionFormula, final lotus.domino.View templateView,
			final boolean prohibitDesignRefresh) {
		try {
			if (!getDelegate().isOpen()) {
				getDelegate().open();
			}
			return fromLotus(getDelegate().createView(viewName, selectionFormula, toLotus(templateView), prohibitDesignRefresh),
					View.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this, "View=" + viewName);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#createView(java.lang.String, java.lang.String, lotus.domino.View)
	 */
	@Override
	public View createView(final String viewName, final String selectionFormula, final lotus.domino.View templateView) {
		return createView(viewName, selectionFormula, templateView, true);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#createView(java.lang.String, java.lang.String)
	 */
	@Override
	public View createView(final String viewName, final String selectionFormula) {
		//TODO NTF even though I'd prefer to just pass defaults to the next overloaded version, it's not clear what the default
		//templateView should be in order to get the native behavior. Does null work?
		try {
			if (!getDelegate().isOpen()) {
				getDelegate().open();
			}
			return fromLotus(getDelegate().createView(viewName, selectionFormula), View.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this, "View=" + viewName);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#createView(java.lang.String)
	 */
	@Override
	public View createView(final String viewName) {
		return createView(viewName, "SELECT @All");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#enableFolder(java.lang.String)
	 */
	@Override
	public void enableFolder(final String folder) {
		try {
			getDelegate().enableFolder(folder);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#fixup()
	 */
	@Override
	public void fixup() {
		try {
			getDelegate().fixup();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#fixup(int)
	 */
	@Override
	public void fixup(final int options) {
		try {
			getDelegate().fixup(options);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getACL()
	 */
	@Override
	public ACL getACL() {
		try {
			if (!getDelegate().isOpen()) {
				getDelegate().open();
			}
			return fromLotus(getDelegate().getACL(), ACL.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getACLActivityLog()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Vector<String> getACLActivityLog() {
		try {
			return getDelegate().getACLActivityLog();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getAgent(java.lang.String)
	 */
	@Override
	public Agent getAgent(final String name) {
		try {
			if (!getDelegate().isOpen()) {
				getDelegate().open();
			}
			return fromLotus(getDelegate().getAgent(name), Agent.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getAgents()
	 */
	@Override
	public Vector<org.openntf.domino.Agent> getAgents() {
		try {
			if (!getDelegate().isOpen()) {
				getDelegate().open();
			}
			return fromLotusAsVector(getDelegate().getAgents(), org.openntf.domino.Agent.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getAllDocuments()
	 */
	@Override
	public DocumentCollection getAllDocuments() {
		try {
			return fromLotus(getDelegate().getAllDocuments(), DocumentCollection.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getAllReadDocuments()
	 */
	@Override
	public DocumentCollection getAllReadDocuments() {
		try {
			return fromLotus(getDelegate().getAllReadDocuments(), DocumentCollection.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getAllReadDocuments(java.lang.String)
	 */
	@Override
	public DocumentCollection getAllReadDocuments(final String userName) {
		try {
			return fromLotus(getDelegate().getAllReadDocuments(userName), DocumentCollection.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getAllUnreadDocuments()
	 */
	@Override
	public DocumentCollection getAllUnreadDocuments() {
		try {
			return fromLotus(getDelegate().getAllUnreadDocuments(), DocumentCollection.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getAllUnreadDocuments(java.lang.String)
	 */
	@Override
	public DocumentCollection getAllUnreadDocuments(final String userName) {
		try {
			return fromLotus(getDelegate().getAllUnreadDocuments(userName), DocumentCollection.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getCategories()
	 */
	@Override
	public String getCategories() {
		if (shadowedMetaData_ != null) {
			return shadowedMetaData_.getCategories();
		}
		try {
			return getDelegate().getCategories();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getCreated()
	 */
	@Override
	public DateTime getCreated() {
		try {
			lotus.domino.DateTime dt = getDelegate().getCreated();
			DateTime ret = fromLotus(dt, DateTime.SCHEMA, getAncestorSession());
			s_recycle(dt);
			return ret;
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getCurrentAccessLevel()
	 */
	@Override
	public int getCurrentAccessLevel() {
		try {
			return getDelegate().getCurrentAccessLevel();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return 0;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getDB2Schema()
	 */
	@Override
	public String getDB2Schema() {
		try {
			return getDelegate().getDB2Schema();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	private Boolean designProtected_;

	public boolean isDesignProtected() {
		if (designProtected_ == null) {
			Document iconNote = getDocumentByID(org.openntf.domino.design.impl.DatabaseDesign.ICON_NOTE);
			designProtected_ = Boolean.FALSE;
			try {
				if (iconNote != null) {
					DxlExporter exporter = getAncestorSession().createDxlExporter();
					exporter.exportDxl(iconNote);
				}

			} catch (OpenNTFNotesException e) {
				designProtected_ = Boolean.TRUE;
			}

		}
		return designProtected_.booleanValue();
	}

	private transient DatabaseDesign design_;

	@Override
	public DatabaseDesign getDesign() {
		if (design_ == null) {
			if (isDesignProtected()) {
				design_ = new org.openntf.domino.design.impl.ProtectedDatabaseDesign(this);
			} else {
				design_ = new org.openntf.domino.design.impl.DatabaseDesign(this);
			}
		}
		return design_;
	}

	@Override
	public org.openntf.domino.Database getXPageSharedDesignTemplate() throws FileNotFoundException {
		IconNote icon = getDesign().getIconNote();
		if (icon == null) {
			return null;
		}
		Document iconDoc = icon.getDocument();
		if ("1".equals(iconDoc.getItemValueString("$XpageSharedDesign"))) {
			String templatePath = iconDoc.getItemValueString("$XpageSharedDesignTemplate");
			org.openntf.domino.Database template = getAncestorSession().getDatabase(templatePath);
			if (template == null || !template.isOpen()) {
				throw new FileNotFoundException("Could not open the XPage shared Design Template: " + templatePath);
			}
			return template;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getDesignTemplateName()
	 */
	@Override
	public String getDesignTemplateName() {
		if (shadowedMetaData_ != null) {
			return shadowedMetaData_.getDesignTemplateName();
		}
		try {
			return getDelegate().getDesignTemplateName();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getDocumentByID(java.lang.String)
	 */
	@Override
	public Document getDocumentByID(final String noteid) {
		try {
			if (!getDelegate().isOpen()) {
				getDelegate().open();
			}
			return fromLotus(getDelegate().getDocumentByID(noteid), Document.SCHEMA, this);
		} catch (Exception e) {
			DominoUtils.handleException(e, this, "NoteId=" + noteid);
			return null;

		}
	}

	public static final String NOTEID_ICONNOTE = "FFFF0010";

	@Override
	public Document getIconNote() {
		return getDocumentByID(NOTEID_ICONNOTE);
	}

	@Override
	public Document getDocumentByID_Or_UNID(final String id) {
		Document doc;
		doc = getDocumentByUNID(id);
		if (doc == null) {
			try {
				doc = getDocumentByID(id);
			} catch (Throwable te) {
				// Just couldn't get doc
			}
		}
		return doc;
	}

	@Override
	public Document getACLNote() {
		NoteCollection nc = createNoteCollection(false);
		nc.setSelectionFormula("@all");
		nc.setSelectAcl(true);
		nc.buildCollection();
		String nid = nc.getFirstNoteID();
		System.out.println("TEMP DEBUG getting ACL from noteid " + nid + " in a note collection with " + nc.getCount() + " entries");
		return getDocumentByID(nid);
	}

	@Override
	public Document getDocumentWithKey(final Serializable key) {
		return this.getDocumentWithKey(key, false);
	}

	@Override
	public Document getDocumentWithKey(final Serializable key, final boolean createOnFail) {
		try {
			if (key != null) {
				if (key instanceof String && ((String) key).length() == 32) {
					if ("000000000000000000000000000000000000".equals(key)) {
						Document result = getIconNote();
						if (result == null) {
							result = getACLNote();
						}
						return result;
					}
				}

				String checksum = DominoUtils.toUnid(key);
				Document doc = this.getDocumentByUNID(checksum);
				if (doc == null && createOnFail) {
					doc = this.createDocument();
					if (checksum != null) {
						doc.setUniversalID(checksum);
					}
					doc.replaceItemValue("$Created", new Date());
					doc.replaceItemValue("$$Key", key);
				}
				return doc;

			} else if (createOnFail) {
				//				log_.log(java.util.logging.Level.FINE,
				//						"Document by key requested with null key. This is probably not what you meant to do...");
				//NTF No, its exactly what we meant to do in the case of graph elements
				Document doc = this.createDocument();
				doc.replaceItemValue("$Created", new Date());
				doc.replaceItemValue("$$Key", "");
				return doc;
			}
		} catch (Exception e) {
			DominoUtils.handleException(e, this);
		}
		return null;
	}

	@Override
	public Document getDocumentByKey(final Serializable key) {
		return getDocumentWithKey(key);
	}

	@Override
	public Document getDocumentByKey(final Serializable key, final boolean createOnFail) {
		return getDocumentWithKey(key, createOnFail);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getDocumentByUNID(java.lang.String)
	 */
	@Override
	public Document getDocumentByUNID(final String unid) {
		try {
			if (unid == null || unid.isEmpty()) {
				return null;
			}
			if (!getDelegate().isOpen()) {
				getDelegate().open();
			}
			return fromLotus(getDelegate().getDocumentByUNID(unid), Document.SCHEMA, this);
		} catch (NotesException e) {
			if (getAncestorSession().isFixEnabled(Fixes.DOC_UNID_NULLS) && "Invalid universal id".equals(e.text)) {
			} else {
				DominoUtils.handleException(e, this, "UNId=" + unid);
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
	@Override
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
				DominoUtils.handleException(e, this);
			} catch (IOException e) {
				DominoUtils.handleException(e, this);
			}
			if (true) {
				return null;
			}

			return fromLotus(getDelegate().getDocumentByURL(url, reload, reloadIfModified, urlList, charSet, webUser, webPassword,
					proxyUser, proxyPassword, returnImmediately), Document.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getDocumentByURL(java.lang.String, boolean)
	 */
	@Override
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
	@Override
	public int getFTIndexFrequency() {
		try {
			return getDelegate().getFTIndexFrequency();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return 0;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getFileFormat()
	 */
	@Override
	public int getFileFormat() {
		try {
			return getDelegate().getFileFormat();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return 0;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getFileName()
	 */
	@Override
	public String getFileName() {
		if (fileName_ == null) {
			int idx = path_.lastIndexOf('/');
			if (idx != -1) {
				fileName_ = path_.substring(idx + 1);
			} else {
				idx = path_.lastIndexOf('\\');// if no \ is found, it returns -1 (-1+1=0)
				fileName_ = path_.substring(idx + 1);
			}
		}
		return fileName_;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getFilePath()
	 */
	@Override
	public String getFilePath() {
		return path_;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getFolderReferencesEnabled()
	 */
	@Override
	public boolean getFolderReferencesEnabled() {
		try {
			return getDelegate().getFolderReferencesEnabled();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getForm(java.lang.String)
	 */
	@Override
	public Form getForm(final String name) {
		try {
			if (!getDelegate().isOpen()) {
				getDelegate().open();
			}
			return fromLotus(getDelegate().getForm(name), Form.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getForms()
	 */
	@Override
	public Vector<Form> getForms() {
		try {
			if (!getDelegate().isOpen()) {
				getDelegate().open();
			}
			return fromLotusAsVector(getDelegate().getForms(), Form.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getHttpURL()
	 */
	@Override
	public String getHttpURL() {
		try {
			return getDelegate().getHttpURL();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	@Override
	public String getHttpURL(final boolean usePath) {
		if (usePath) {
			String baseURL = getHttpURL();
			URL url;
			try {
				url = new URL(baseURL);
			} catch (MalformedURLException e) {
				DominoUtils.handleException(e, this);
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
	@Override
	public DateTime getLastFTIndexed() {
		try {
			return fromLotus(getDelegate().getLastFTIndexed(), DateTime.SCHEMA, getAncestorSession());
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	@Override
	public Date getLastFTIndexedDate() {
		try {
			lotus.domino.DateTime dt = getDelegate().getLastFTIndexed();
			Date ret = DominoUtils.toJavaDateSafe(dt);// recycles the javaDate!
			s_recycle(dt);
			return ret;
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getLastFixup()
	 */
	@Override
	public DateTime getLastFixup() {
		try {
			return fromLotus(getDelegate().getLastFixup(), DateTime.SCHEMA, getAncestorSession());
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	@Override
	public Date getLastFixupDate() {
		try {
			return DominoUtils.toJavaDateSafe(getDelegate().getLastFixup());
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getLastModified()
	 */
	@Override
	public DateTime getLastModified() {
		try {
			return fromLotus(getDelegate().getLastModified(), DateTime.SCHEMA, getAncestorSession());
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	@Override
	public Date getLastModifiedDate() {
		if (shadowedMetaData_ != null) {
			return shadowedMetaData_.getLastModifiedDate();
		}
		try {
			return DominoUtils.toJavaDateSafe(getDelegate().getLastModified());
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getLimitRevisions()
	 */
	@Override
	public double getLimitRevisions() {
		try {
			return getDelegate().getLimitRevisions();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return 0d;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getLimitUpdatedBy()
	 */
	@Override
	public double getLimitUpdatedBy() {
		try {
			return getDelegate().getLimitUpdatedBy();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return 0d;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getListInDbCatalog()
	 */
	@Override
	public boolean getListInDbCatalog() {
		try {
			return getDelegate().getListInDbCatalog();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getManagers()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Vector<String> getManagers() {
		try {
			return getDelegate().getManagers();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getMaxSize()
	 */
	@Override
	public long getMaxSize() {
		try {
			return getDelegate().getMaxSize();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return 0L;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getModifiedDocuments()
	 */
	@Override
	public DocumentCollection getModifiedDocuments() {
		try {
			if (!getDelegate().isOpen()) {
				getDelegate().open();
			}
			return fromLotus(getDelegate().getModifiedDocuments(), DocumentCollection.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	@Override
	public DocumentCollection getModifiedDocuments(final java.util.Date since) {
		return getModifiedDocuments(since, ModifiedDocClass.DATA);
	}

	@Override
	public DocumentCollection getModifiedDocuments(java.util.Date since, final ModifiedDocClass noteClass) {
		try {
			DocumentCollection result;
			if (since == null) {
				since = new Date(0);
			}
			lotus.domino.DateTime tempDT = getAncestorSession().createDateTime(since);
			lotus.domino.DateTime dt = toLotus(tempDT);
			if (!getDelegate().isOpen()) {
				getDelegate().open();
			}
			result = fromLotus(getDelegate().getModifiedDocuments(dt, noteClass.getValue()), DocumentCollection.SCHEMA, this);
			if (tempDT instanceof Encapsulated) {
				dt.recycle();
			}
			return result;
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getModifiedDocuments(lotus.domino.DateTime, int)
	 */
	@Override
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
			DominoUtils.handleException(e, this);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getModifiedDocuments(lotus.domino.DateTime)
	 */
	@Override
	public DocumentCollection getModifiedDocuments(final lotus.domino.DateTime since) {
		return getModifiedDocuments(since, 1);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getNotesURL()
	 */
	@Override
	public String getNotesURL() {
		try {
			return getDelegate().getNotesURL();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getOption(int)
	 */
	@Override
	public boolean getOption(final int optionName) {
		try {
			return getDelegate().getOption(optionName);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getOutline(java.lang.String)
	 */
	@Override
	public Outline getOutline(final String outlineName) {
		try {
			return fromLotus(getDelegate().getOutline(outlineName), Outline.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public final Session getParent() {
		return parent;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getPercentUsed()
	 */
	@Override
	public double getPercentUsed() {
		try {
			return getDelegate().getPercentUsed();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return 0d;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getProfileDocCollection(java.lang.String)
	 */
	@Override
	public DocumentCollection getProfileDocCollection(final String profileName) {
		try {
			if (!getDelegate().isOpen()) {
				getDelegate().open();
			}
			return fromLotus(getDelegate().getProfileDocCollection(profileName), DocumentCollection.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getProfileDocument(java.lang.String, java.lang.String)
	 */
	@Override
	public Document getProfileDocument(final String profileName, final String key) {
		try {
			if (!getDelegate().isOpen()) {
				getDelegate().open();
			}
			return fromLotus(getDelegate().getProfileDocument(profileName, key), Document.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getReplicaID()
	 */
	@Override
	public String getReplicaID() {
		return replid_;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.Database#getMetaReplicaId()
	 */
	@Override
	public String getMetaReplicaID() {
		if (server_.length() > 0) {
			return server_ + "!!" + replid_;
		}
		return replid_;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getReplicationInfo()
	 */
	@Override
	public Replication getReplicationInfo() {
		try {
			return fromLotus(getDelegate().getReplicationInfo(), Replication.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getServer()
	 */
	@Override
	public String getServer() {
		return server_;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getSize()
	 */
	@Override
	public double getSize() {
		if (shadowedMetaData_ != null) {
			return shadowedMetaData_.getSize();
		}
		try {
			return getDelegate().getSize();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return 0d;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getSizeQuota()
	 */
	@Override
	public int getSizeQuota() {
		try {
			return getDelegate().getSizeQuota();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return 0;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getSizeWarning()
	 */
	@Override
	public long getSizeWarning() {
		try {
			return getDelegate().getSizeWarning();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return 0L;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getTemplateName()
	 */
	@Override
	public String getTemplateName() {
		if (shadowedMetaData_ != null) {
			return shadowedMetaData_.getTemplateName();
		}
		try {
			return getDelegate().getTemplateName();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getTitle()
	 */
	@Override
	public String getTitle() {
		if (shadowedMetaData_ != null) {
			return shadowedMetaData_.getTitle();
		}
		try {
			return getDelegate().getTitle();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getType()
	 */
	@Override
	@Deprecated
	public int getType() {
		try {
			return getDelegate().getType();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return 0;

		}
	}

	@Override
	public Type getTypeEx() {
		return Type.valueOf(getType());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getURL()
	 */
	@Override
	public String getURL() {
		try {
			return getDelegate().getURL();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getURLHeaderInfo(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public String getURLHeaderInfo(final String url, final String header, final String webUser, final String webPassword,
			final String proxyUser, final String proxyPassword) {
		try {
			return getDelegate().getURLHeaderInfo(url, header, webUser, webPassword, proxyUser, proxyPassword);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getUndeleteExpireTime()
	 */
	@Override
	public int getUndeleteExpireTime() {
		try {
			return getDelegate().getUndeleteExpireTime();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return 0;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getView(java.lang.String)
	 */
	@Override
	public View getView(final String name) {
		try {
			if (!getDelegate().isOpen()) {
				getDelegate().open();
			}
			View result = fromLotus(getDelegate().getView(name), View.SCHEMA, this);
			if (result != null) {
				if (getAncestorSession().isFixEnabled(Fixes.VIEW_UPDATE_OFF)) {
					result.setAutoUpdate(false);
				}
			}
			return result;
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	private static final Pattern PIPE_SPLIT = Pattern.compile("\\|");

	@Override
	public View getView(final Document viewDocument) {
		View result = null;
		if (viewDocument.hasItem("$Index") || viewDocument.hasItem("$Collection") || viewDocument.hasItem("$Collation")
				|| viewDocument.hasItem("$VIEWFORMAT")) {
			String unid = viewDocument.getUniversalID();
			String rawtitles = viewDocument.getItemValue("$Title", String.class);
			String[] titles = PIPE_SPLIT.split(rawtitles);
			for (String title : titles) {
				View chk = getView(title);
				if (chk.getUniversalID().equalsIgnoreCase(unid)) {
					result = chk;
					break;
				}
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#getViews()
	 */
	@Override
	public Vector<org.openntf.domino.View> getViews() {
		try {
			if (!getDelegate().isOpen()) {
				getDelegate().open();
			}
			return fromLotusAsVector(getDelegate().getViews(), org.openntf.domino.View.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#grantAccess(java.lang.String, int)
	 */
	@Override
	public void grantAccess(final String name, final int level) {
		try {
			getDelegate().grantAccess(name, level);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);

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
	@Override
	public boolean isAllowOpenSoftDeleted() {
		try {
			return getDelegate().isAllowOpenSoftDeleted();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#isClusterReplication()
	 */
	@Override
	public boolean isClusterReplication() {
		try {
			return getDelegate().isClusterReplication();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#isConfigurationDirectory()
	 */
	@Override
	public boolean isConfigurationDirectory() {
		try {
			return getDelegate().isConfigurationDirectory();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#isCurrentAccessPublicReader()
	 */
	@Override
	public boolean isCurrentAccessPublicReader() {
		try {
			return getDelegate().isCurrentAccessPublicReader();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#isCurrentAccessPublicWriter()
	 */
	@Override
	public boolean isCurrentAccessPublicWriter() {
		try {
			return getDelegate().isCurrentAccessPublicWriter();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#isDB2()
	 */
	@Override
	public boolean isDB2() {
		try {
			return getDelegate().isDB2();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#isDelayUpdates()
	 */
	@Override
	public boolean isDelayUpdates() {
		try {
			return getDelegate().isDelayUpdates();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#isDesignLockingEnabled()
	 */
	@Override
	public boolean isDesignLockingEnabled() {
		try {
			return getDelegate().isDesignLockingEnabled();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#isDirectoryCatalog()
	 */
	@Override
	public boolean isDirectoryCatalog() {
		try {
			return getDelegate().isDirectoryCatalog();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#isDocumentLockingEnabled()
	 */
	@Override
	public boolean isDocumentLockingEnabled() {
		try {
			return getDelegate().isDocumentLockingEnabled();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#isFTIndexed()
	 */
	@Override
	public boolean isFTIndexed() {
		try {
			return getDelegate().isFTIndexed();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#isInMultiDbIndexing()
	 */
	@Override
	public boolean isInMultiDbIndexing() {
		try {
			return getDelegate().isInMultiDbIndexing();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#isInService()
	 */
	@Override
	public boolean isInService() {
		try {
			return getDelegate().isInService();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#isLink()
	 */
	@Override
	public boolean isLink() {
		try {
			return getDelegate().isLink();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#isMultiDbSearch()
	 */
	@Override
	public boolean isMultiDbSearch() {
		try {
			return getDelegate().isMultiDbSearch();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#isOpen()
	 */
	@Override
	public boolean isOpen() {
		if (getDelegate_unchecked() == null) {
			return false;
		}
		try {
			return getDelegate().isOpen();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#isPendingDelete()
	 */
	@Override
	public boolean isPendingDelete() {
		try {
			return getDelegate().isPendingDelete();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#isPrivateAddressBook()
	 */
	@Override
	public boolean isPrivateAddressBook() {
		try {
			return getDelegate().isPrivateAddressBook();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#isPublicAddressBook()
	 */
	@Override
	public boolean isPublicAddressBook() {
		try {
			return getDelegate().isPublicAddressBook();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#markForDelete()
	 */
	@Override
	public void markForDelete() {
		try {
			getDelegate().markForDelete();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);

		}
	}

	protected transient boolean alreadyOpen_ = false;

	/*
	 * (non-Javadoc)
	 *
	 */
	@Override
	public boolean open() {
		if (alreadyOpen_ && !isDead(getDelegate_unchecked())) {
			return false;
		}
		try {
			boolean result = false;
			alreadyOpen_ = true;
			try {
				result = getDelegate().open();
			} catch (NotesException ne) {
				if (NotesError.NOTES_ERR_DBALREADY_OPEN == ne.id) {
					if (log_.isLoggable(java.util.logging.Level.FINE)) {
						log_.log(java.util.logging.Level.FINE, "Suppressing a db already open error because, why?");
					}
				} else {
					DominoUtils.handleException(ne, this);
					return false;
				}
			}
			if (result) {
				initialize(getDelegate());
			}
			return result;
		} catch (Exception e) {
			DominoUtils.handleException(e, this);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#openByReplicaID(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean openByReplicaID(final String server, final String replicaId) {
		try {
			boolean result = getDelegate().openByReplicaID(server, replicaId);
			if (result) {
				initialize(getDelegate());
			}
			return result;
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#openIfModified(java.lang.String, java.lang.String, lotus.domino.DateTime)
	 */
	@Override
	public boolean openIfModified(final String server, final String dbFile, final lotus.domino.DateTime modifiedSince) {
		try {
			boolean result = false;
			lotus.domino.DateTime dt = toLotus(modifiedSince);
			result = getDelegate().openIfModified(server, dbFile, dt);
			if (result) {
				initialize(getDelegate());
			}
			dt.recycle();
			return result;
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#openWithFailover(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean openWithFailover(final String server, final String dbFile) {
		try {
			boolean result = getDelegate().openWithFailover(server, dbFile);
			if (result) {
				initialize(getDelegate());
			}
			return result;
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#queryAccess(java.lang.String)
	 */
	@Override
	public int queryAccess(final String name) {
		try {
			return getDelegate().queryAccess(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return 0;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#queryAccessPrivileges(java.lang.String)
	 */
	@Override
	public int queryAccessPrivileges(final String name) {
		try {
			return getDelegate().queryAccessPrivileges(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return 0;

		}
	}

	@Override
	public Set<DBPrivilege> queryAccessPrivilegesEx(final String name) {
		return DominoEnumUtil.valuesOf(DBPrivilege.class, queryAccessPrivileges(name));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#queryAccessRoles(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Vector<String> queryAccessRoles(final String name) {
		try {
			return getDelegate().queryAccessRoles(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#remove()
	 */
	@Override
	public void remove() {
		try {
			getDelegate().remove();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#removeFTIndex()
	 */
	@Override
	public void removeFTIndex() {
		try {
			getDelegate().removeFTIndex();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#replicate(java.lang.String)
	 */
	@Override
	public boolean replicate(final String server) {
		boolean result = true;
		boolean go = true;
		go = fireListener(generateEvent(Events.BEFORE_REPLICATION, this, server));
		if (go) {
			try {
				result = getDelegate().replicate(server);
			} catch (NotesException e) {
				DominoUtils.handleException(e, this);
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
	@Override
	public void revokeAccess(final String name) {
		try {
			getDelegate().revokeAccess(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#search(java.lang.String, lotus.domino.DateTime, int)
	 */
	@Override
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
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#search(java.lang.String, lotus.domino.DateTime)
	 */
	@Override
	public DocumentCollection search(final String formula, final lotus.domino.DateTime startDate) {
		return search(formula, startDate, 0);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#search(java.lang.String)
	 */
	@Override
	public DocumentCollection search(final String formula) {
		try {
			return fromLotus(getDelegate().search(formula), DocumentCollection.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#setAllowOpenSoftDeleted(boolean)
	 */
	@Override
	public void setAllowOpenSoftDeleted(final boolean flag) {
		try {
			getDelegate().setAllowOpenSoftDeleted(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#setCategories(java.lang.String)
	 */
	@Override
	public void setCategories(final String categories) {
		try {
			getDelegate().setCategories(categories);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#setDelayUpdates(boolean)
	 */
	@Override
	public void setDelayUpdates(final boolean flag) {
		try {
			getDelegate().setDelayUpdates(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#setDesignLockingEnabled(boolean)
	 */
	@Override
	public void setDesignLockingEnabled(final boolean flag) {
		try {
			getDelegate().setDesignLockingEnabled(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#setDocumentLockingEnabled(boolean)
	 */
	@Override
	public void setDocumentLockingEnabled(final boolean flag) {
		try {
			getDelegate().setDocumentLockingEnabled(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#setFTIndexFrequency(int)
	 */
	@Override
	public void setFTIndexFrequency(final int frequency) {
		try {
			getDelegate().setFTIndexFrequency(frequency);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#setFolderReferencesEnabled(boolean)
	 */
	@Override
	public void setFolderReferencesEnabled(final boolean flag) {
		try {
			boolean current = getDelegate().getFolderReferencesEnabled();
			if (flag != current) {
				getDelegate().setFolderReferencesEnabled(flag);
			}
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#setInMultiDbIndexing(boolean)
	 */
	@Override
	public void setInMultiDbIndexing(final boolean flag) {
		try {
			getDelegate().setInMultiDbIndexing(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#setInService(boolean)
	 */
	@Override
	public void setInService(final boolean flag) {
		try {
			getDelegate().setInService(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#setLimitRevisions(double)
	 */
	@Override
	public void setLimitRevisions(final double revisions) {
		try {
			getDelegate().setLimitRevisions(revisions);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#setLimitUpdatedBy(double)
	 */
	@Override
	public void setLimitUpdatedBy(final double updatedBys) {
		try {
			getDelegate().setLimitUpdatedBy(updatedBys);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#setListInDbCatalog(boolean)
	 */
	@Override
	public void setListInDbCatalog(final boolean flag) {
		try {
			getDelegate().setListInDbCatalog(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#setOption(int, boolean)
	 */
	@Override
	public void setOption(final int optionName, final boolean flag) {
		try {
			getDelegate().setOption(optionName, flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#setOption(org.openntf.domino.Database.DBOption, boolean)
	 */
	@Override
	public void setOption(final DBOption optionName, final boolean flag) {
		setOption(optionName.getValue(), flag);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#setSizeQuota(int)
	 */
	@Override
	public void setSizeQuota(final int quota) {
		try {
			getDelegate().setSizeQuota(quota);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#setSizeWarning(int)
	 */
	@Override
	public void setSizeWarning(final int warning) {
		try {
			getDelegate().setSizeWarning(warning);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#setTitle(java.lang.String)
	 */
	@Override
	public void setTitle(final String title) {
		try {
			getDelegate().setTitle(title);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#setUndeleteExpireTime(int)
	 */
	@Override
	public void setUndeleteExpireTime(final int hours) {
		try {
			getDelegate().setUndeleteExpireTime(hours);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#sign()
	 */
	@Override
	public void sign() {
		try {
			getDelegate().sign();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#sign(int, boolean, java.lang.String, boolean)
	 */
	@Override
	public void sign(final int documentType, final boolean existingSigsOnly, final String name, final boolean nameIsNoteid) {
		try {
			getDelegate().sign(documentType, existingSigsOnly, name, nameIsNoteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#sign(org.openntf.domino.Database.SignDocType, boolean, java.lang.String, boolean)
	 */
	@Override
	public void sign(final SignDocType documentType, final boolean existingSigsOnly, final String name, final boolean nameIsNoteid) {
		this.sign(documentType.getValue(), existingSigsOnly, name, nameIsNoteid);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#sign(int, boolean, java.lang.String)
	 */
	@Override
	public void sign(final int documentType, final boolean existingSigsOnly, final String name) {
		try {
			getDelegate().sign(documentType, existingSigsOnly, name);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#sign(org.openntf.domino.Database.SignDocType, boolean, java.lang.String)
	 */
	@Override
	public void sign(final SignDocType documentType, final boolean existingSigsOnly, final String name) {
		this.sign(documentType.getValue(), existingSigsOnly, name);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#sign(int, boolean)
	 */
	@Override
	public void sign(final int documentType, final boolean existingSigsOnly) {
		try {
			getDelegate().sign(documentType, existingSigsOnly);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#sign(org.openntf.domino.Database.SignDocType, boolean)
	 */
	@Override
	public void sign(final SignDocType documentType, final boolean existingSigsOnly) {
		this.sign(documentType.getValue(), existingSigsOnly);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#sign(int)
	 */
	@Override
	public void sign(final int documentType) {
		try {
			getDelegate().sign(documentType);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#sign(org.openntf.domino.Database.SignDocType)
	 */
	@Override
	public void sign(final SignDocType documentType) {
		this.sign(documentType.getValue());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Database#updateFTIndex(boolean)
	 */
	@Override
	public void updateFTIndex(final boolean create) {
		try {
			getDelegate().updateFTIndex(create);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);

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

	@Override
	public void closeTransaction() {
		txnHolder_.set(null);
	}

	@Override
	public DatabaseTransaction getTransaction() {
		return txnHolder_.get();
	}

	@Override
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

	@Override
	public String toString() {
		return (server_.length() < 1 ? "" : server_ + "!!") + path_;
	}

	@Override
	public void resurrect() {// should only happen if the delegate has been destroyed somehow.
		shadowedMetaData_ = null;// clear metaData
		lotus.domino.Session rawSession = toLotus(parent);
		try {
			lotus.domino.Database d = rawSession.getDatabase(server_, path_);
			setDelegate(d, true);
			/* No special logging, since by now Database is a BaseThreadSafe */
		} catch (NotesException e) {
			if (e.id == NotesError.NOTES_ERR_DBNOACCESS) {
				throw new UserAccessException(
						"User " + parent.getEffectiveUserName() + " cannot open database " + path_ + " on server " + server_, e);
			} else {
				DominoUtils.handleException(e, this);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public final Session getAncestorSession() {
		return parent;
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

	@Override
	public lotus.notes.addins.DominoServer getDominoServer() {
		try {
			lotus.notes.addins.DominoServer server = new lotus.notes.addins.DominoServer(getServer());
			return server;
		} catch (Exception e) {
			DominoUtils.handleException(e, this);
		}
		return null;
	}

	@Override
	public void refreshDesign() {
		try {
			lotus.notes.addins.DominoServer server = getDominoServer();
			server.refreshDesign(getFilePath());
		} catch (Exception e) {
			DominoUtils.handleException(e, this);
		}
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

	public LocalNoteList getLocalNoteList() {
		Set<SelectOption> noteClass = new java.util.HashSet<SelectOption>();
		noteClass.add(SelectOption.DOCUMENTS);
		NoteCollection nc = createNoteCollection(false);
		nc.setSelectOptions(noteClass);
		nc.buildCollection();
		LocalNoteList result = new org.openntf.domino.big.impl.LocalNoteList(nc, new Date());
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.ext.Database#getModifiedNoteCount(lotus.domino.DateTime, org.openntf.domino.NoteCollection.SelectOption)
	 */
	@Override
	public int getModifiedNoteCount(final java.util.Date since, final Set<SelectOption> noteClass) {
		if (since != null && since.after(this.getLastModifiedDate())) {
			return 0;
		}
		NoteCollection nc = createNoteCollection(false);
		if (since != null) {
			nc.setSinceTime(since);
		}
		nc.setSelectOptions(noteClass);
		nc.buildCollection();
		return nc.getCount();
	}

	@Override
	public int getNoteCount() {
		return getModifiedNoteCount(null);
	}

	public int[] getDailyModifiedNoteCount(final java.util.Date since) {
		Set<SelectOption> noteClass = new java.util.HashSet<SelectOption>();
		noteClass.add(SelectOption.DOCUMENTS);
		return getDailyModifiedNoteCount(since, noteClass);
	}

	protected static final int DAILY_ARRAY_LIMIT = 31;

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

	@Override
	public int getModifiedNoteCount(final java.util.Date since) {
		if (since == null) {
			Set<SelectOption> noteClass = new java.util.HashSet<SelectOption>();
			noteClass.add(SelectOption.DOCUMENTS);
			return getModifiedNoteCount(since, noteClass);
		} else {
			java.util.Date last = this.getLastModifiedDate();
			if (since.after(last)) {
				return 0;
			}
			Set<SelectOption> noteClass = new java.util.HashSet<SelectOption>();
			noteClass.add(SelectOption.DOCUMENTS);
			return getModifiedNoteCount(since, noteClass);
		}
	}

	private IDominoEventFactory localFactory_;

	@Override
	public IDominoEventFactory getEventFactory() {
		if (localFactory_ == null) {
			return getAncestorSession().getEventFactory();
		}
		return localFactory_;
	}

	@Override
	public void setEventFactory(final IDominoEventFactory factory) {
		localFactory_ = factory;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public IDominoEvent generateEvent(final EnumEvent event, final org.openntf.domino.Base source, final Object payload) {
		return getEventFactory().generate(event, source, this, payload);
	}

	// this is wrong as it is NOT symmetric: oda.equals(lotus) != lotus.equals(oda)
	//	@Override
	//	public boolean equals(final Object other) {
	//		if (other == null)
	//			return false;
	//		if (other instanceof lotus.domino.Database) {
	//			if (other instanceof Database) {
	//				Database oDb = (Database) other;
	//				return oDb.getAncestorSession().equals(getAncestorSession()) && oDb.getApiPath().equals(getApiPath());
	//			} else {
	//				return getDelegate().equals(other);
	//			}
	//		}
	//		throw new IllegalArgumentException("Cannot compare a Database with a " + (other == null ? "null" : other.getClass().getName()));
	//	}
	//
	//	/* (non-Javadoc)
	//	 * @see java.lang.Object#hashCode()
	//	 */
	//	@Override
	//	public int hashCode() {
	//		return getDelegate().hashCode();
	//	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((path_ == null) ? 0 : path_.hashCode());
		result = prime * result + ((server_ == null) ? 0 : server_.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof Database)) {
			return false;
		}
		Database other = (Database) obj;
		if (path_ == null) {
			if (other.path_ != null) {
				return false;
			}
		} else if (!path_.equals(other.path_)) {
			return false;
		}
		if (server_ == null) {
			if (other.server_ != null) {
				return false;
			}
		} else if (!server_.equals(other.server_)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.Database#openMail()
	 */
	@Override
	public org.openntf.domino.Database getMail() {
		return getAncestorSession().getDbDirectory(null).openMailDatabase();
	}

	@Override
	public void openMail() {
		try {
			lotus.domino.Session rawSess = toLotus(getAncestorSession());
			lotus.domino.DbDirectory rawDir = rawSess.getDbDirectory(null);
			lotus.domino.Database rawDb = rawDir.openMailDatabase();
			s_recycle(getDelegate());
			this.setDelegate(rawDb, true);
			rawDir.recycle();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne, this);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.Database#getDocumentMap()
	 */
	@Override
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
			if (!(key instanceof Serializable)) {
				throw new IllegalArgumentException();
			}
			return get(key) != null;
		}

		@Override
		public org.openntf.domino.Document get(final Object key) {
			if (!(key instanceof Serializable)) {
				throw new IllegalArgumentException();
			}
			return getDocumentWithKey((Serializable) key);
		}

		@Override
		public org.openntf.domino.Document put(final Serializable key, final org.openntf.domino.Document value) {
			// Ignore the value for now
			if (key != null) {
				Document doc = getDocumentWithKey(key);
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
				Document doc = getDocumentWithKey(key.toString());
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.Database#getApiPath()
	 */
	@Override
	public String getApiPath() {
		if (apiPath_ == null) {
			if (server_.length() > 0) {
				apiPath_ = server_ + "!!" + path_;
			} else {
				apiPath_ = path_;
			}
		}
		return apiPath_;
	}

	private IDatabaseSchema schema_;
	private volatile Boolean isSchemaChecked_ = Boolean.FALSE;

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.Database#getSchema()
	 */
	@Override
	public IDatabaseSchema getSchema() {
		if (!isSchemaChecked_ && schema_ == null) {
			//TODO some way to load the schema from the design...
			isSchemaChecked_ = Boolean.TRUE;
		}
		return schema_;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.Database#setSchema(org.openntf.domino.schema.IDatabaseSchema)
	 */
	@Override
	public void setSchema(final IDatabaseSchema schema) {
		schema_ = schema;
		//TODO serialization of the schema into a design file
	}

	@Override
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

	@Override
	public AutoMime getAutoMime() {
		if (autoMime_ == null) {
			return getAncestorSession().getAutoMime();
		} else {
			return autoMime_;
		}
	}

	@Override
	public void setAutoMime(final AutoMime autoMime) {
		autoMime_ = autoMime;
	}

	private Locale dbLocale = null;
	private boolean getLocaleCalled = false;

	/*
	 * (non-Javadoc)
	 * @see org.openntf.domino.ext.Database#getLocale()
	 */
	@Override
	public Locale getLocale() {
		if (getLocaleCalled) {
			return dbLocale;
		}
		getLocaleCalled = true;

		Document doc = getDesign().getIconNote().getDocument();
		if (doc == null) {
			return null;
		}
		String lStr = doc.getItemValueString("$DefaultLanguage");
		if (lStr == null || lStr.length() < 2) {
			return null;
		}
		String language = lStr.substring(0, 2).toLowerCase();
		String country = (lStr.length() >= 5 && lStr.charAt(2) == '-') ? lStr.substring(3, 5).toUpperCase() : "";
		return dbLocale = new Locale(language, country);
	}

	private transient NoteCollection intNC_;

	private NoteCollection getInternalNoteCollection() {
		if (null == intNC_ || intNC_.isDead()) {
			intNC_ = this.createNoteCollection(false);
			//		} else {
			//			try {
			//				int junk = ((lotus.domino.NoteCollection) Base.getDelegate(intNC_)).getCount();
			//			} catch (NotesException ne) {
			//				intNC_ = this.createNoteCollection(false);
			//			}
		}
		return intNC_;
	}

	@Override
	public String getUNID(final String noteid) {
		return getInternalNoteCollection().getUNID(noteid);
	}

	@Override
	public String getUNID(final int noteid) {
		String nid = Integer.toHexString(noteid);
		return getUNID(nid);
	}

	@Override
	public Document getDocumentByUNID(final String unid, final boolean deferDelegate) {
		if (deferDelegate) {
			return getFactory().create(Document.SCHEMA, this, unid);
		} else {
			return getDocumentByUNID(unid);
		}
	}

	@Override
	public Document getDocumentByID(final String noteid, final boolean deferDelegate) {
		if (deferDelegate) {
			return getFactory().create(Document.SCHEMA, this, noteid);
		} else {
			return getDocumentByID(noteid);
		}
	}

	@Override
	public Document getDocumentByID(final int noteid, final boolean deferDelegate) {
		if (deferDelegate) {
			return getFactory().create(Document.SCHEMA, this, noteid);
		} else {
			return getDocumentByID(Integer.toHexString(noteid));
		}
	}

	@Override
	public Document getDocumentByID(final int noteid) {
		return getDocumentByID(noteid, false);
	}

	@Override
	public void fillExceptionDetails(final List<ExceptionDetails.Entry> result) {
		parent.fillExceptionDetails(result);
		result.add(new ExceptionDetails.Entry(this, getApiPath()));
	}

	//-------------- Externalize/Deexternalize stuff ------------------
	private static final int EXTERNALVERSIONUID = 20141205;

	/**
	 * @deprecated needed for {@link Externalizable} - do not use!
	 */
	@Deprecated
	public Database() {
		super(NOTES_DATABASE);
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		super.writeExternal(out);
		out.writeInt(EXTERNALVERSIONUID);// data version

		out.writeObject(server_);
		out.writeObject(path_);
		out.writeObject(replid_);
		out.writeObject(autoMime_);

		// out.writeObject(formatter_); not needed!
		// out.writeBoolean(noRecycle); not needed - done by factory

	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		super.readExternal(in);

		int version = in.readInt();
		if (version != EXTERNALVERSIONUID) {
			throw new InvalidClassException("Cannot read dataversion " + version);
		}

		server_ = (String) in.readObject();
		path_ = (String) in.readObject();
		replid_ = (String) in.readObject();

		autoMime_ = (AutoMime) in.readObject();
	}

	protected Object readResolve() {
		Database ret = (Database) getAncestorSession().getDatabase(server_, path_);
		readResolveCheck(server_, ret.server_);
		readResolveCheck(path_, ret.path_);
		readResolveCheck(replid_, ret.replid_);
		readResolveCheck(autoMime_, ret.autoMime_);
		return ret;
	}

	@Override
	protected WrapperFactory getFactory() {
		return parent.getFactory();
	}

	@Override
	public Set<String> getCurrentRoles() {
		Session s = getAncestorSession();
		String name = s.getEffectiveUserName();
		Vector<String> rawroles = this.queryAccessRoles(name);
		if (rawroles.size() > 0) {
			Set<String> result = new TreeSet<String>(rawroles);
			return result;
		} else {
			return null;
		}
	}

	@Override
	public EnumSet<ACL.Privilege> getCurrentPrivileges() {
		Session s = getAncestorSession();
		String name = s.getEffectiveUserName();
		int privs = this.queryAccessPrivileges(name);
		return ACL.Privilege.getPrivileges(privs);
	}

}

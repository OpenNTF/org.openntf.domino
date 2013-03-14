package org.openntf.domino.impl;

import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class Database extends Base<org.openntf.domino.Database, lotus.domino.Database> implements org.openntf.domino.Database {
	private String server_;
	private String path_;
	private String replid_;

	public Database(lotus.domino.Database delegate, org.openntf.domino.Base parent) {
		super(delegate, (parent instanceof org.openntf.domino.Session) ? parent : Factory.getSession(parent));
	}

	public Document FTDomainSearch(String query, int maxDocs, int sortOpt, int otherOpt, int start, int count, String entryForm) {
		try {
			return Factory.fromLotus(getDelegate().FTDomainSearch(query, maxDocs, sortOpt, otherOpt, start, count, entryForm),
					Document.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public DocumentCollection FTSearch(String query, int maxDocs, int sortOpt, int otherOpt) {
		try {
			return Factory.fromLotus(getDelegate().FTSearch(query, maxDocs, sortOpt, otherOpt), Document.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	public DocumentCollection FTSearch(String query, int maxDocs) {
		try {
			return Factory.fromLotus(getDelegate().FTSearch(query, maxDocs), Document.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	public DocumentCollection FTSearch(String query) {
		try {
			return Factory.fromLotus(getDelegate().FTSearch(query), Document.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public DocumentCollection FTSearchRange(String query, int maxDocs, int sortOpt, int otherOpt, int start) {
		try {
			return Factory.fromLotus(getDelegate().FTSearchRange(query, maxDocs, sortOpt, otherOpt, start), DocumentCollection.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public int compact() {
		try {
			return getDelegate().compact();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	public int compactWithOptions(int options, String spaceThreshold) {
		try {
			return getDelegate().compactWithOptions(options, spaceThreshold);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	public int compactWithOptions(int options) {
		try {
			return getDelegate().compactWithOptions(options);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	public int compactWithOptions(String spaceThreshold) {
		try {
			return getDelegate().compactWithOptions(spaceThreshold);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	public Database createCopy(String server, String dbFile, int maxSize) {
		try {
			return Factory.fromLotus(getDelegate().createCopy(server, dbFile, maxSize), Database.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public Database createCopy(String server, String dbFile) {
		try {
			return Factory.fromLotus(getDelegate().createCopy(server, dbFile), Database.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public Document createDocument() {
		try {
			return Factory.fromLotus(getDelegate().createDocument(), Document.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public DocumentCollection createDocumentCollection() {
		try {
			return Factory.fromLotus(getDelegate().createDocumentCollection(), DocumentCollection.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public void createFTIndex(int options, boolean recreate) {
		try {
			getDelegate().createFTIndex(options, recreate);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public Database createFromTemplate(String server, String dbFile, boolean inherit, int maxSize) {
		try {
			return Factory.fromLotus(getDelegate().createFromTemplate(server, dbFile, inherit, maxSize), Database.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public Database createFromTemplate(String server, String dbFile, boolean inherit) {
		try {
			return Factory.fromLotus(getDelegate().createFromTemplate(server, dbFile, inherit), Database.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public NoteCollection createNoteCollection(boolean selectAllFlag) {
		try {
			return Factory.fromLotus(getDelegate().createNoteCollection(selectAllFlag), NoteCollection.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public Outline createOutline(String name, boolean defaultOutline) {
		try {
			return Factory.fromLotus(getDelegate().createOutline(name, defaultOutline), Outline.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public Outline createOutline(String name) {
		try {
			return Factory.fromLotus(getDelegate().createOutline(name), Outline.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public View createQueryView(String viewName, String query, lotus.domino.View templateView, boolean prohibitDesignRefresh) {
		try {
			return Factory.fromLotus(getDelegate().createQueryView(viewName, query, (lotus.domino.View) toLotus(templateView),
					prohibitDesignRefresh), View.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public View createQueryView(String viewName, String query, lotus.domino.View templateView) {
		try {
			return Factory.fromLotus(getDelegate().createQueryView(viewName, query, (lotus.domino.View) toLotus(templateView)), View.class,
					this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public View createQueryView(String viewName, String query) {
		try {
			return Factory.fromLotus(getDelegate().createQueryView(viewName, query), View.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public Database createReplica(String server, String dbFile) {
		try {
			return Factory.fromLotus(getDelegate().createReplica(server, dbFile), Database.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public View createView() {
		try {
			return Factory.fromLotus(getDelegate().createView(), View.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public View createView(String viewName, String selectionFormula, lotus.domino.View templateView, boolean prohibitDesignRefresh) {
		try {
			return Factory.fromLotus(getDelegate().createView(viewName, selectionFormula, (lotus.domino.View) toLotus(templateView),
					prohibitDesignRefresh), View.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public View createView(String viewName, String selectionFormula, lotus.domino.View templateView) {
		try {
			return Factory.fromLotus(getDelegate().createView(viewName, selectionFormula, (lotus.domino.View) toLotus(templateView)),
					View.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public View createView(String viewName, String selectionFormula) {
		try {
			return Factory.fromLotus(getDelegate().createView(viewName, selectionFormula), View.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public View createView(String viewName) {
		try {
			return Factory.fromLotus(getDelegate().createView(viewName), View.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public void enableFolder(String folder) {
		try {
			getDelegate().enableFolder(folder);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void fixup() {
		try {
			getDelegate().fixup();
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void fixup(int options) {
		try {
			getDelegate().fixup(options);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public ACL getACL() {
		try {
			return Factory.fromLotus(getDelegate().getACL(), ACL.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@SuppressWarnings("unchecked")
	public Vector<String> getACLActivityLog() {
		try {
			return (Vector<String>) getDelegate().getACLActivityLog();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public Agent getAgent(String name) {
		try {
			return Factory.fromLotus(getDelegate().getAgent(name), Agent.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@SuppressWarnings("unchecked")
	public Vector<org.openntf.domino.Agent> getAgents() {
		try {
			return Factory.fromLotusAsVector(getDelegate().getAgents(), org.openntf.domino.Agent.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public DocumentCollection getAllDocuments() {
		try {
			return Factory.fromLotus(getDelegate().getAllDocuments(), org.openntf.domino.DocumentCollection.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public DocumentCollection getAllReadDocuments() {
		try {
			return Factory.fromLotus(getDelegate().getAllReadDocuments(), DocumentCollection.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public DocumentCollection getAllReadDocuments(String userName) {
		try {
			return Factory.fromLotus(getDelegate().getAllReadDocuments(userName), DocumentCollection.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public DocumentCollection getAllUnreadDocuments() {
		try {
			return Factory.fromLotus(getDelegate().getAllUnreadDocuments(), DocumentCollection.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public DocumentCollection getAllUnreadDocuments(String userName) {
		try {
			return Factory.fromLotus(getDelegate().getAllUnreadDocuments(userName), DocumentCollection.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public String getCategories() {
		try {
			return getDelegate().getCategories();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public DateTime getCreated() {
		try {
			return Factory.fromLotus(getDelegate().getCreated(), DateTime.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public int getCurrentAccessLevel() {
		try {
			return getDelegate().getCurrentAccessLevel();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	public String getDB2Schema() {
		try {
			return getDelegate().getDB2Schema();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public String getDesignTemplateName() {
		try {
			return getDelegate().getDesignTemplateName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public Document getDocumentByID(String noteid) {
		try {
			return Factory.fromLotus(getDelegate().getDocumentByID(noteid), Document.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public Document getDocumentByUNID(String unid) {
		try {
			return Factory.fromLotus(getDelegate().getDocumentByUNID(unid), Document.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public Document getDocumentByURL(String url, boolean reload, boolean reloadIfModified, boolean urlList, String charSet, String webUser,
			String webPassword, String proxyUser, String proxyPassword, boolean returnImmediately) {
		try {
			return Factory.fromLotus(getDelegate().getDocumentByURL(url, reload, reloadIfModified, urlList, charSet, webUser, webPassword,
					proxyUser, proxyPassword, returnImmediately), Document.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public Document getDocumentByURL(String url, boolean reload) {
		try {
			return Factory.fromLotus(getDelegate().getDocumentByURL(url, reload), Document.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public int getFTIndexFrequency() {
		try {
			return getDelegate().getFTIndexFrequency();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	public int getFileFormat() {
		try {
			return getDelegate().getFileFormat();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	public String getFileName() {
		try {
			return getDelegate().getFileName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public String getFilePath() {
		try {
			return getDelegate().getFilePath();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public boolean getFolderReferencesEnabled() {
		try {
			return getDelegate().getFolderReferencesEnabled();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public Form getForm(String name) {
		try {
			return Factory.fromLotus(getDelegate().getForm(name), Form.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public Vector<org.openntf.domino.Form> getForms() {
		try {
			return Factory.fromLotusAsVector(getDelegate().getForms(), org.openntf.domino.Form.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public String getHttpURL() {
		try {
			return getDelegate().getHttpURL();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public DateTime getLastFTIndexed() {
		try {
			return Factory.fromLotus(getDelegate().getLastFTIndexed(), DateTime.class, Factory.getSession(this));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public DateTime getLastFixup() {
		try {
			return Factory.fromLotus(getDelegate().getLastFixup(), DateTime.class, Factory.getSession(this));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public DateTime getLastModified() {
		try {
			return Factory.fromLotus(getDelegate().getLastModified(), DateTime.class, Factory.getSession(this));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public double getLimitRevisions() {
		try {
			return getDelegate().getLimitRevisions();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0d;

		}
	}

	public double getLimitUpdatedBy() {
		try {
			return getDelegate().getLimitUpdatedBy();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0d;

		}
	}

	public boolean getListInDbCatalog() {
		try {
			return getDelegate().getListInDbCatalog();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	@SuppressWarnings("unchecked")
	public Vector<String> getManagers() {
		try {
			return (Vector<String>) getDelegate().getManagers();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public long getMaxSize() {
		try {
			return getDelegate().getMaxSize();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0L;

		}
	}

	public DocumentCollection getModifiedDocuments() {
		try {
			return Factory.fromLotus(getDelegate().getModifiedDocuments(), DocumentCollection.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public DocumentCollection getModifiedDocuments(lotus.domino.DateTime since, int noteClass) {
		try {
			return Factory.fromLotus(getDelegate().getModifiedDocuments((lotus.domino.DateTime) toLotus(since), noteClass),
					DocumentCollection.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public DocumentCollection getModifiedDocuments(lotus.domino.DateTime since) {
		try {
			return Factory.fromLotus(getDelegate().getModifiedDocuments(since), DocumentCollection.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public String getNotesURL() {
		try {
			return getDelegate().getNotesURL();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public boolean getOption(int optionName) {
		try {
			return getDelegate().getOption(optionName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public Outline getOutline(String outlineName) {
		try {
			return Factory.fromLotus(getDelegate().getOutline(outlineName), Outline.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public Session getParent() {
		return (Session) super.getParent();
	}

	public double getPercentUsed() {
		try {
			return getDelegate().getPercentUsed();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0d;

		}
	}

	public DocumentCollection getProfileDocCollection(String profileName) {
		try {
			return Factory.fromLotus(getDelegate().getProfileDocCollection(profileName), DocumentCollection.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public Document getProfileDocument(String profileName, String key) {
		try {
			return Factory.fromLotus(getDelegate().getProfileDocument(profileName, key), Document.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public String getReplicaID() {
		try {
			return getDelegate().getReplicaID();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public Replication getReplicationInfo() {
		try {
			return Factory.fromLotus(getDelegate().getReplicationInfo(), Replication.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public String getServer() {
		try {
			return getDelegate().getServer();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public double getSize() {
		try {
			return getDelegate().getSize();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0d;

		}
	}

	public int getSizeQuota() {
		try {
			return getDelegate().getSizeQuota();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	public long getSizeWarning() {
		try {
			return getDelegate().getSizeWarning();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0L;

		}
	}

	public String getTemplateName() {
		try {
			return getDelegate().getTemplateName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public String getTitle() {
		try {
			return getDelegate().getTitle();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public int getType() {
		try {
			return getDelegate().getType();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	public String getURL() {
		try {
			return getDelegate().getURL();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public String getURLHeaderInfo(String url, String header, String webUser, String webPassword, String proxyUser, String proxyPassword) {
		try {
			return getDelegate().getURLHeaderInfo(url, header, webUser, webPassword, proxyUser, proxyPassword);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public int getUndeleteExpireTime() {
		try {
			return getDelegate().getUndeleteExpireTime();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	public View getView(String name) {
		try {
			return Factory.fromLotus(getDelegate().getView(name), View.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public Vector<org.openntf.domino.View> getViews() {
		try {
			return Factory.fromLotusAsVector(getDelegate().getViews(), org.openntf.domino.View.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public void grantAccess(String name, int level) {
		try {
			getDelegate().grantAccess(name, level);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public boolean isAllowOpenSoftDeleted() {
		try {
			return getDelegate().isAllowOpenSoftDeleted();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public boolean isClusterReplication() {
		try {
			return getDelegate().isClusterReplication();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public boolean isConfigurationDirectory() {
		try {
			return getDelegate().isConfigurationDirectory();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public boolean isCurrentAccessPublicReader() {
		try {
			return getDelegate().isCurrentAccessPublicReader();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public boolean isCurrentAccessPublicWriter() {
		try {
			return getDelegate().isCurrentAccessPublicWriter();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public boolean isDB2() {
		try {
			return getDelegate().isDB2();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public boolean isDelayUpdates() {
		try {
			return getDelegate().isDelayUpdates();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public boolean isDesignLockingEnabled() {
		try {
			return getDelegate().isDesignLockingEnabled();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public boolean isDirectoryCatalog() {
		try {
			return getDelegate().isDirectoryCatalog();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public boolean isDocumentLockingEnabled() {
		try {
			return getDelegate().isDocumentLockingEnabled();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public boolean isFTIndexed() {
		try {
			return getDelegate().isFTIndexed();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public boolean isInMultiDbIndexing() {
		try {
			return getDelegate().isInMultiDbIndexing();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public boolean isInService() {
		try {
			return getDelegate().isInService();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public boolean isLink() {
		try {
			return getDelegate().isLink();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public boolean isMultiDbSearch() {
		try {
			return getDelegate().isMultiDbSearch();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public boolean isOpen() {
		try {
			return getDelegate().isOpen();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public boolean isPendingDelete() {
		try {
			return getDelegate().isPendingDelete();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public boolean isPrivateAddressBook() {
		try {
			return getDelegate().isPrivateAddressBook();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public boolean isPublicAddressBook() {
		try {
			return getDelegate().isPublicAddressBook();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public void markForDelete() {
		try {
			getDelegate().markForDelete();
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public boolean open() {
		try {
			return getDelegate().open();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public boolean openByReplicaID(String server, String replicaId) {
		try {
			return getDelegate().openByReplicaID(server, replicaId);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public boolean openIfModified(String server, String dbFile, lotus.domino.DateTime modifiedSince) {
		try {
			return getDelegate().openIfModified(server, dbFile, (lotus.domino.DateTime) toLotus(modifiedSince));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public boolean openWithFailover(String server, String dbFile) {
		try {
			return getDelegate().openWithFailover(server, dbFile);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public int queryAccess(String name) {
		try {
			return getDelegate().queryAccess(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	public int queryAccessPrivileges(String name) {
		try {
			return getDelegate().queryAccessPrivileges(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	@SuppressWarnings("unchecked")
	public Vector<String> queryAccessRoles(String name) {
		try {
			return (Vector<String>) getDelegate().queryAccessRoles(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public void remove() {
		try {
			getDelegate().remove();
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void removeFTIndex() {
		try {
			getDelegate().removeFTIndex();
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public boolean replicate(String server) {
		try {
			return getDelegate().replicate(server);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public void revokeAccess(String name) {
		try {
			getDelegate().revokeAccess(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public DocumentCollection search(String formula, lotus.domino.DateTime startDate, int maxDocs) {
		try {
			return Factory.fromLotus(getDelegate().search(formula, (lotus.domino.DateTime) toLotus(startDate), maxDocs),
					DocumentCollection.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public DocumentCollection search(String formula, lotus.domino.DateTime startDate) {
		try {
			return Factory.fromLotus(getDelegate().search(formula, (lotus.domino.DateTime) toLotus(startDate)), DocumentCollection.class,
					this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public DocumentCollection search(String formula) {
		try {
			return Factory.fromLotus(getDelegate().search(formula), DocumentCollection.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public void setAllowOpenSoftDeleted(boolean flag) {
		try {
			getDelegate().setAllowOpenSoftDeleted(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setCategories(String categories) {
		try {
			getDelegate().setCategories(categories);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setDelayUpdates(boolean flag) {
		try {
			getDelegate().setDelayUpdates(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setDesignLockingEnabled(boolean flag) {
		try {
			getDelegate().setDesignLockingEnabled(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setDocumentLockingEnabled(boolean flag) {
		try {
			getDelegate().setDocumentLockingEnabled(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setFTIndexFrequency(int frequency) {
		try {
			getDelegate().setFTIndexFrequency(frequency);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setFolderReferencesEnabled(boolean flag) {
		try {
			getDelegate().setFolderReferencesEnabled(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setInMultiDbIndexing(boolean flag) {
		try {
			getDelegate().setInMultiDbIndexing(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setInService(boolean flag) {
		try {
			getDelegate().setInService(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setLimitRevisions(double revisions) {
		try {
			getDelegate().setLimitRevisions(revisions);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setLimitUpdatedBy(double updatedBys) {
		try {
			getDelegate().setLimitUpdatedBy(updatedBys);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setListInDbCatalog(boolean flag) {
		try {
			getDelegate().setListInDbCatalog(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setOption(int optionName, boolean flag) {
		try {
			getDelegate().setOption(optionName, flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setSizeQuota(int quota) {
		try {
			getDelegate().setSizeQuota(quota);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setSizeWarning(int warning) {
		try {
			getDelegate().setSizeWarning(warning);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setTitle(String title) {
		try {
			getDelegate().setTitle(title);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setUndeleteExpireTime(int hours) {
		try {
			getDelegate().setUndeleteExpireTime(hours);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void sign() {
		try {
			getDelegate().sign();
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void sign(int documentType, boolean existingSigsOnly, String name, boolean nameIsNoteid) {
		try {
			getDelegate().sign(documentType, existingSigsOnly, name, nameIsNoteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void sign(int documentType, boolean existingSigsOnly, String name) {
		try {
			getDelegate().sign(documentType, existingSigsOnly, name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void sign(int documentType, boolean existingSigsOnly) {
		try {
			getDelegate().sign(documentType, existingSigsOnly);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void sign(int documentType) {
		try {
			getDelegate().sign(documentType);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void updateFTIndex(boolean create) {
		try {
			getDelegate().updateFTIndex(create);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

}
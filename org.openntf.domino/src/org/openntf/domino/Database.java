package org.openntf.domino;

import java.util.Vector;

import org.openntf.domino.annotations.Legacy;

public interface Database extends lotus.domino.Database, org.openntf.domino.Base<lotus.domino.Database> {
	@Override
	@Deprecated
	@Legacy(Legacy.INTERFACES_WARNING)
	public Vector<String> getACLActivityLog();

	@Override
	public int compact();

	@Override
	public int compactWithOptions(int options);

	@Override
	public int compactWithOptions(int options, String spaceThreshold);

	@Override
	public int compactWithOptions(String options);

	@Override
	public Database createCopy(String server, String dbFile);

	@Override
	public Database createCopy(String server, String dbFile, int maxSize);

	@Override
	public Document createDocument();

	@Override
	public DocumentCollection createDocumentCollection();

	@Override
	public lotus.domino.Database createFromTemplate(String server, String dbFile, boolean inherit);

	@Override
	public lotus.domino.Database createFromTemplate(String server, String dbFile, boolean inherit, int maxSize);

	@Override
	public void createFTIndex(int options, boolean recreate);

	@Override
	// TODO Switch to new class
	public lotus.domino.NoteCollection createNoteCollection(boolean selectAllFlag);

	@Override
	// TODO Switch to new class
	public lotus.domino.Outline createOutline(String name);

	@Override
	public lotus.domino.Outline createOutline(String name, boolean defaultOutline);

	@Override
	public View createQueryView(String viewName, String query);

	@Override
	public View createQueryView(String viewName, String query, lotus.domino.View templateView);

	@Override
	public View createQueryView(String viewName, String query, lotus.domino.View templateView, boolean prohibitDesignRefresh);

	@Override
	public Database createReplica(String server, String dbFile);

	@Override
	public View createView();

	@Override
	public View createView(String viewName);

	@Override
	public View createView(String viewName, String selectionFormula);

	@Override
	public View createView(String viewName, String selectionFormula, lotus.domino.View templateView);

	@Override
	public View createView(String viewName, String selectionFormula, lotus.domino.View templateView, boolean prohibitDesignRefresh);

	@Override
	public void enableFolder(String folder);

	@Override
	public void fixup();

	@Override
	public void fixup(int options);

	@Override
	public Document FTDomainSearch(String query, int maxDocs, int sortOpt, int otherOpt, int start, int count, String entryForm);

	@Override
	public DocumentCollection FTSearch(String query);

	@Override
	public DocumentCollection FTSearch(String query, int maxDocs);

	@Override
	public DocumentCollection FTSearch(String query, int maxDocs, int sortOpt, int otherOpt);

	@Override
	public DocumentCollection FTSearchRange(String query, int maxDocs, int sortOpt, int otherOpt, int start);

	@Override
	public ACL getACL();

	@Override
	// TODO Switch to new class
	public lotus.domino.Agent getAgent(String name);

	@Override
	@Legacy(Legacy.INTERFACES_WARNING)
	public Vector<lotus.domino.Agent> getAgents();

	@Override
	public DocumentCollection getAllDocuments();

	@Override
	public DocumentCollection getAllReadDocuments();

	@Override
	public DocumentCollection getAllReadDocuments(String userName);

	@Override
	public DocumentCollection getAllUnreadDocuments();

	@Override
	public DocumentCollection getAllUnreadDocuments(String userName);

	@Override
	public String getCategories();

	@Override
	public DateTime getCreated();

	@Override
	public int getCurrentAccessLevel();

	@Override
	public String getDB2Schema();

	@Override
	public String getDesignTemplateName();

	@Override
	public Document getDocumentByID(String noteid);

	@Override
	public Document getDocumentByUNID(String unid);

	@Override
	public Document getDocumentByURL(String url, boolean reload);

	@Override
	public Document getDocumentByURL(String url, boolean reload, boolean reloadIfModified, boolean urlList, String charSet, String webUser,
			String webPassword, String proxyUser, String proxyPassword, boolean returnImmediately);

	@Override
	public int getFileFormat();

	@Override
	public String getFileName();

	@Override
	public String getFilePath();

	@Override
	public boolean getFolderReferencesEnabled();

	@Override
	// TODO Switch to new class
	public lotus.domino.Form getForm(String name);

	@Override
	@Legacy(Legacy.INTERFACES_WARNING)
	public Vector<Form> getForms();

	@Override
	public int getFTIndexFrequency();

	@Override
	public String getHttpURL();

	@Override
	public DateTime getLastFixup();

	@Override
	public DateTime getLastFTIndexed();

	@Override
	public DateTime getLastModified();

	@Override
	public double getLimitRevisions();

	@Override
	public double getLimitUpdatedBy();

	@Override
	public boolean getListInDbCatalog();

	@Override
	@Legacy(Legacy.INTERFACES_WARNING)
	public Vector<String> getManagers();

	@Override
	public long getMaxSize();

	@Override
	public DocumentCollection getModifiedDocuments();

	@Override
	public DocumentCollection getModifiedDocuments(lotus.domino.DateTime since);

	@Override
	public DocumentCollection getModifiedDocuments(lotus.domino.DateTime since, int noteClass);

	@Override
	public String getNotesURL();

	@Override
	public boolean getOption(int optionName);

	@Override
	// TODO Switch to new class
	public lotus.domino.Outline getOutline(String outlineName);

	@Override
	public Session getParent();

	@Override
	public double getPercentUsed();

	@Override
	public DocumentCollection getProfileDocCollection(String profileName);

	@Override
	public Document getProfileDocument(String profileName, String profileKey);

	@Override
	public String getReplicaID();

	@Override
	// TODO Switch to new class
	public lotus.domino.Replication getReplicationInfo();

	@Override
	public String getServer();

	@Override
	public double getSize();

	@Override
	public int getSizeQuota();

	@Override
	public long getSizeWarning();

	@Override
	public String getTemplateName();

	@Override
	public String getTitle();

	@Override
	public int getType();

	@Override
	public int getUndeleteExpireTime();

	@Override
	public String getURL();

	@Override
	public String getURLHeaderInfo(String url, String header, String webUser, String webPassword, String proxyUser, String proxyPassword);

	@Override
	public View getView(String name);

	@Override
	@Legacy(Legacy.INTERFACES_WARNING)
	public Vector<View> getViews();

	@Override
	public void grantAccess(String name, int level);

	@Override
	public boolean isAllowOpenSoftDeleted();

	@Override
	public boolean isClusterReplication();

	@Override
	public boolean isConfigurationDirectory();

	@Override
	public boolean isCurrentAccessPublicReader();

	@Override
	public boolean isCurrentAccessPublicWriter();

	@Override
	public boolean isDB2();

	@Override
	public boolean isDelayUpdates();

	@Override
	public boolean isDesignLockingEnabled();

	@Override
	public boolean isDirectoryCatalog();

	@Override
	public boolean isDocumentLockingEnabled();

	@Override
	public boolean isFTIndexed();

	@Override
	public boolean isInMultiDbIndexing();

	@Override
	public boolean isInService();

	@Override
	public boolean isLink();

	@Override
	public boolean isMultiDbSearch();

	@Override
	public boolean isOpen();

	@Override
	public boolean isPendingDelete();

	@Override
	public boolean isPrivateAddressBook();

	@Override
	public boolean isPublicAddressBook();

	@Override
	public void markForDelete();

	@Override
	public boolean open();

	@Override
	public boolean openByReplicaID(String server, String replicaId);

	@Override
	public boolean openIfModified(String server, String dbFile, lotus.domino.DateTime modifiedSince);

	@Override
	public boolean openWithFailover(String server, String dbFile);

	@Override
	public int queryAccess(String name);

	@Override
	public int queryAccessPrivileges(String name);

	@Override
	public Vector queryAccessRoles(String roles);

	@Override
	public void remove();

	@Override
	public void removeFTIndex();

	@Override
	public boolean replicate(String server);

	@Override
	public void revokeAccess(String name);

	@Override
	public DocumentCollection search(String formula);

	@Override
	public DocumentCollection search(String formula, lotus.domino.DateTime startDate);

	@Override
	public DocumentCollection search(String formula, lotus.domino.DateTime startDate, int maxDocs);

	@Override
	public void setAllowOpenSoftDeleted(boolean flag);

	@Override
	public void setCategories(String flag);

	@Override
	public void setDelayUpdates(boolean flag);

	@Override
	public void setDesignLockingEnabled(boolean flag);

	@Override
	public void setDocumentLockingEnabled(boolean flag);

	@Override
	public void setFolderReferencesEnabled(boolean flag);

	@Override
	public void setFTIndexFrequency(int frequency);

	@Override
	public void setInMultiDbIndexing(boolean flag);

	@Override
	public void setInService(boolean flag);

	@Override
	public void setLimitRevisions(double revisions);

	@Override
	public void setLimitUpdatedBy(double updatedBys);

	@Override
	public void setListInDbCatalog(boolean flag);

	@Override
	public void setOption(int optionName, boolean flag);

	@Override
	public void setSizeQuota(int quota);

	@Override
	public void setSizeWarning(int warning);

	@Override
	public void setTitle(String title);

	@Override
	public void setUndeleteExpireTime(int hours);

	@Override
	public void sign();

	@Override
	public void sign(int documentType);

	@Override
	public void sign(int documentType, boolean existingSigsOnly);

	@Override
	public void sign(int documentType, boolean existingSigsOnly, String name);

	@Override
	public void sign(int documentType, boolean existingSigsOnly, String name, boolean nameIsNoteid);

	@Override
	public void updateFTIndex(boolean create);

}

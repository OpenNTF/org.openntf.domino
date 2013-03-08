package org.openntf.domino.impl;

import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.DateTime;
import org.openntf.domino.View;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class Database extends Base<org.openntf.domino.Database, lotus.domino.Database> implements org.openntf.domino.Database {
	public Database() {
		// TODO Auto-generated constructor stub
	}

	public Database(lotus.domino.Database delegate) {
		super(delegate);
	}

	public Document FTDomainSearch(String arg0, int arg1, int arg2, int arg3, int arg4, int arg5, String arg6) {
		try {
			return Factory.fromLotus(getDelegate().FTDomainSearch(arg0, arg1, arg2, arg3, arg4, arg5, arg6), Document.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public DocumentCollection FTSearch(String arg0, int arg1, int arg2, int arg3) {
		try {
			return Factory.fromLotus(getDelegate().FTSearch(arg0, arg1, arg2, arg3), DocumentCollection.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public DocumentCollection FTSearch(String arg0, int arg1) {
		try {
			return Factory.fromLotus(getDelegate().FTSearch(arg0, arg1), DocumentCollection.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public DocumentCollection FTSearch(String arg0) {
		try {
			return Factory.fromLotus(getDelegate().FTSearch(arg0), DocumentCollection.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public DocumentCollection FTSearchRange(String arg0, int arg1, int arg2, int arg3, int arg4) {
		try {
			return Factory.fromLotus(getDelegate().FTSearchRange(arg0, arg1, arg2, arg3, arg4), DocumentCollection.class);
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

	public int compactWithOptions(int arg0, String arg1) {
		try {
			return getDelegate().compactWithOptions(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	public int compactWithOptions(int arg0) {
		try {
			return getDelegate().compactWithOptions(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	public int compactWithOptions(String arg0) {
		try {
			return getDelegate().compactWithOptions(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	public Database createCopy(String arg0, String arg1, int arg2) {
		try {
			return Factory.fromLotus(getDelegate().createCopy(arg0, arg1, arg2), Database.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public Database createCopy(String arg0, String arg1) {
		try {
			return Factory.fromLotus(getDelegate().createCopy(arg0, arg1), Database.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public Document createDocument() {
		try {
			return Factory.fromLotus(getDelegate().createDocument(), Document.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public DocumentCollection createDocumentCollection() {
		try {
			return Factory.fromLotus(getDelegate().createDocumentCollection(), DocumentCollection.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public void createFTIndex(int arg0, boolean arg1) {
		try {
			getDelegate().createFTIndex(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public Database createFromTemplate(String arg0, String arg1, boolean arg2, int arg3) {
		try {
			return Factory.fromLotus(getDelegate().createFromTemplate(arg0, arg1, arg2, arg3), Database.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public Database createFromTemplate(String arg0, String arg1, boolean arg2) {
		try {
			return Factory.fromLotus(getDelegate().createFromTemplate(arg0, arg1, arg2), Database.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public lotus.domino.NoteCollection createNoteCollection(boolean arg0) {
		try {
			return getDelegate().createNoteCollection(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public lotus.domino.Outline createOutline(String arg0, boolean arg1) {
		try {
			return getDelegate().createOutline(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public lotus.domino.Outline createOutline(String arg0) {
		try {
			return getDelegate().createOutline(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public View createQueryView(String arg0, String arg1, lotus.domino.View arg2, boolean arg3) {
		try {
			return Factory
					.fromLotus(getDelegate().createQueryView(arg0, arg1, (lotus.domino.View) Factory.toLotus(arg2), arg3), View.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public View createQueryView(String arg0, String arg1, lotus.domino.View arg2) {
		try {
			return Factory.fromLotus(getDelegate().createQueryView(arg0, arg1, (lotus.domino.View) Factory.toLotus(arg2)), View.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public View createQueryView(String arg0, String arg1) {
		try {
			return Factory.fromLotus(getDelegate().createQueryView(arg0, arg1), View.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public Database createReplica(String arg0, String arg1) {
		try {
			return Factory.fromLotus(getDelegate().createReplica(arg0, arg1), Database.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public View createView() {
		try {
			return Factory.fromLotus(getDelegate().createView(), View.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public View createView(String arg0, String arg1, lotus.domino.View arg2, boolean arg3) {
		try {
			return Factory.fromLotus(getDelegate().createView(arg0, arg1, (lotus.domino.View) Factory.toLotus(arg2), arg3), View.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public View createView(String arg0, String arg1, lotus.domino.View arg2) {
		try {
			return Factory.fromLotus(getDelegate().createView(arg0, arg1, (lotus.domino.View) Factory.toLotus(arg2)), View.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public View createView(String arg0, String arg1) {
		try {
			return Factory.fromLotus(getDelegate().createView(arg0, arg1), View.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public View createView(String arg0) {
		try {
			return Factory.fromLotus(getDelegate().createView(arg0), View.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public void enableFolder(String arg0) {
		try {
			getDelegate().enableFolder(arg0);
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

	public void fixup(int arg0) {
		try {
			getDelegate().fixup(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public ACL getACL() {
		try {
			return Factory.fromLotus(getDelegate().getACL(), ACL.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public Vector getACLActivityLog() {
		try {
			return getDelegate().getACLActivityLog();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public lotus.domino.Agent getAgent(String arg0) {
		try {
			return getDelegate().getAgent(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public Vector getAgents() {
		try {
			return getDelegate().getAgents();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public DocumentCollection getAllDocuments() {
		try {
			return Factory.fromLotus(getDelegate().getAllDocuments(), DocumentCollection.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public DocumentCollection getAllReadDocuments() {
		try {
			return Factory.fromLotus(getDelegate().getAllReadDocuments(), DocumentCollection.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public DocumentCollection getAllReadDocuments(String arg0) {
		try {
			return Factory.fromLotus(getDelegate().getAllReadDocuments(arg0), DocumentCollection.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public DocumentCollection getAllUnreadDocuments() {
		try {
			return Factory.fromLotus(getDelegate().getAllUnreadDocuments(), DocumentCollection.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public DocumentCollection getAllUnreadDocuments(String arg0) {
		try {
			return Factory.fromLotus(getDelegate().getAllUnreadDocuments(arg0), DocumentCollection.class);
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
			return Factory.fromLotus(getDelegate().getCreated(), DateTime.class);
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

	public Document getDocumentByID(String arg0) {
		try {
			return new Document(getDelegate().getDocumentByID(arg0));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public Document getDocumentByUNID(String arg0) {
		try {
			return new Document(getDelegate().getDocumentByUNID(arg0));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public Document getDocumentByURL(String arg0, boolean arg1, boolean arg2, boolean arg3, String arg4, String arg5, String arg6,
			String arg7, String arg8, boolean arg9) {
		try {
			return new Document(getDelegate().getDocumentByURL(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public Document getDocumentByURL(String arg0, boolean arg1) {
		try {
			return new Document(getDelegate().getDocumentByURL(arg0, arg1));
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

	public lotus.domino.Form getForm(String arg0) {
		try {
			return getDelegate().getForm(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public Vector getForms() {
		try {
			return getDelegate().getForms();
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
			return Factory.fromLotus(getDelegate().getLastFTIndexed(), DateTime.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public DateTime getLastFixup() {
		try {
			return Factory.fromLotus(getDelegate().getLastFixup(), DateTime.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public DateTime getLastModified() {
		try {
			return Factory.fromLotus(getDelegate().getLastModified(), DateTime.class);
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

	public Vector getManagers() {
		try {
			return getDelegate().getManagers();
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
			return Factory.fromLotus(getDelegate().getModifiedDocuments(), DocumentCollection.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public DocumentCollection getModifiedDocuments(lotus.domino.DateTime arg0, int arg1) {
		try {
			return Factory.fromLotus(getDelegate().getModifiedDocuments((lotus.domino.DateTime) Factory.toLotus(arg0)),
					DocumentCollection.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public DocumentCollection getModifiedDocuments(lotus.domino.DateTime arg0) {
		try {
			return Factory.fromLotus(getDelegate().getModifiedDocuments((lotus.domino.DateTime) Factory.toLotus(arg0)),
					DocumentCollection.class);
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

	public boolean getOption(int arg0) {
		try {
			return getDelegate().getOption(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public lotus.domino.Outline getOutline(String arg0) {
		try {
			return getDelegate().getOutline(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public Session getParent() {
		try {
			return Factory.fromLotus(getDelegate().getParent(), Session.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public double getPercentUsed() {
		try {
			return getDelegate().getPercentUsed();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0d;

		}
	}

	public DocumentCollection getProfileDocCollection(String arg0) {
		try {
			return Factory.fromLotus(getDelegate().getProfileDocCollection(arg0), DocumentCollection.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public Document getProfileDocument(String arg0, String arg1) {
		try {
			return new Document(getDelegate().getProfileDocument(arg0, arg1));
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

	public lotus.domino.Replication getReplicationInfo() {
		try {
			return getDelegate().getReplicationInfo();
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

	public String getURLHeaderInfo(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5) {
		try {
			return getDelegate().getURLHeaderInfo(arg0, arg1, arg2, arg3, arg4, arg5);
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

	public View getView(String arg0) {
		try {
			return Factory.fromLotus(getDelegate().getView(arg0), View.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@SuppressWarnings("unchecked")
	public Vector<View> getViews() {
		try {
			Vector<lotus.domino.View> views = getDelegate().getViews();
			org.openntf.domino.impl.Vector<View> result = new org.openntf.domino.impl.Vector<View>();
			for (lotus.domino.View view : views) {
				result.add((View) Factory.fromLotus(view, View.class));
			}
			return result;
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public void grantAccess(String arg0, int arg1) {
		try {
			getDelegate().grantAccess(arg0, arg1);
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

	public boolean openByReplicaID(String arg0, String arg1) {
		try {
			return getDelegate().openByReplicaID(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public boolean openIfModified(String arg0, String arg1, lotus.domino.DateTime arg2) {
		try {
			return getDelegate().openIfModified(arg0, arg1, (lotus.domino.DateTime) Factory.toLotus(arg2));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public boolean openWithFailover(String arg0, String arg1) {
		try {
			return getDelegate().openWithFailover(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public int queryAccess(String arg0) {
		try {
			return getDelegate().queryAccess(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	public int queryAccessPrivileges(String arg0) {
		try {
			return getDelegate().queryAccessPrivileges(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	public Vector queryAccessRoles(String arg0) {
		try {
			return getDelegate().queryAccessRoles(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public void recycle() {
		try {
			getDelegate().recycle();
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void recycle(Vector arg0) {
		try {
			getDelegate().recycle(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

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

	public boolean replicate(String arg0) {
		try {
			return getDelegate().replicate(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public void revokeAccess(String arg0) {
		try {
			getDelegate().revokeAccess(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public DocumentCollection search(String arg0, lotus.domino.DateTime arg1, int arg2) {
		try {
			return Factory.fromLotus(getDelegate().search(arg0, (lotus.domino.DateTime) Factory.toLotus(arg1), arg2),
					DocumentCollection.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public DocumentCollection search(String arg0, lotus.domino.DateTime arg1) {
		try {
			return Factory.fromLotus(getDelegate().search(arg0, (lotus.domino.DateTime) Factory.toLotus(arg1)), DocumentCollection.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public DocumentCollection search(String arg0) {
		try {
			return Factory.fromLotus(getDelegate().search(arg0), DocumentCollection.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public void setAllowOpenSoftDeleted(boolean arg0) {
		try {
			getDelegate().setAllowOpenSoftDeleted(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setCategories(String arg0) {
		try {
			getDelegate().setCategories(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setDelayUpdates(boolean arg0) {
		try {
			getDelegate().setDelayUpdates(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setDesignLockingEnabled(boolean arg0) {
		try {
			getDelegate().setDesignLockingEnabled(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setDocumentLockingEnabled(boolean arg0) {
		try {
			getDelegate().setDocumentLockingEnabled(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setFTIndexFrequency(int arg0) {
		try {
			getDelegate().setFTIndexFrequency(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setFolderReferencesEnabled(boolean arg0) {
		try {
			getDelegate().setFolderReferencesEnabled(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setInMultiDbIndexing(boolean arg0) {
		try {
			getDelegate().setInMultiDbIndexing(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setInService(boolean arg0) {
		try {
			getDelegate().setInService(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setLimitRevisions(double arg0) {
		try {
			getDelegate().setLimitRevisions(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setLimitUpdatedBy(double arg0) {
		try {
			getDelegate().setLimitUpdatedBy(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setListInDbCatalog(boolean arg0) {
		try {
			getDelegate().setListInDbCatalog(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setOption(int arg0, boolean arg1) {
		try {
			getDelegate().setOption(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setSizeQuota(int arg0) {
		try {
			getDelegate().setSizeQuota(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setSizeWarning(int arg0) {
		try {
			getDelegate().setSizeWarning(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setTitle(String arg0) {
		try {
			getDelegate().setTitle(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setUndeleteExpireTime(int arg0) {
		try {
			getDelegate().setUndeleteExpireTime(arg0);
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

	public void sign(int arg0, boolean arg1, String arg2, boolean arg3) {
		try {
			getDelegate().sign(arg0, arg1, arg2, arg3);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void sign(int arg0, boolean arg1, String arg2) {
		try {
			getDelegate().sign(arg0, arg1, arg2);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void sign(int arg0, boolean arg1) {
		try {
			getDelegate().sign(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void sign(int arg0) {
		try {
			getDelegate().sign(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void updateFTIndex(boolean arg0) {
		try {
			getDelegate().updateFTIndex(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

}

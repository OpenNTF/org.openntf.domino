package org.openntf.domino;

import java.util.Vector;

import lotus.domino.ACL;
import lotus.domino.Agent;
import lotus.domino.DateTime;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.Form;
import lotus.domino.NoteCollection;
import lotus.domino.NotesException;
import lotus.domino.Outline;
import lotus.domino.Replication;
import lotus.domino.Session;
import lotus.domino.View;

import org.openntf.domino.annotations.Legacy;

public interface Database extends lotus.domino.Database, org.openntf.domino.Base<lotus.domino.Database> {
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public Vector getACLActivityLog() throws NotesException;

	@Override
	public int compact();

	@Override
	public int compactWithOptions(int arg0) throws NotesException;

	@Override
	public int compactWithOptions(int arg0, String arg1) throws NotesException;

	@Override
	public int compactWithOptions(String arg0) throws NotesException;

	@Override
	public lotus.domino.Database createCopy(String arg0, String arg1) throws NotesException;

	@Override
	public lotus.domino.Database createCopy(String arg0, String arg1, int arg2) throws NotesException;

	@Override
	public Document createDocument() throws NotesException;

	@Override
	public DocumentCollection createDocumentCollection() throws NotesException;

	@Override
	public lotus.domino.Database createFromTemplate(String arg0, String arg1, boolean arg2) throws NotesException;

	@Override
	public lotus.domino.Database createFromTemplate(String arg0, String arg1, boolean arg2, int arg3) throws NotesException;

	@Override
	public void createFTIndex(int arg0, boolean arg1) throws NotesException;

	@Override
	public NoteCollection createNoteCollection(boolean arg0) throws NotesException;

	@Override
	public Outline createOutline(String arg0) throws NotesException;

	@Override
	public Outline createOutline(String arg0, boolean arg1) throws NotesException;

	@Override
	public View createQueryView(String arg0, String arg1) throws NotesException;

	@Override
	public View createQueryView(String arg0, String arg1, View arg2) throws NotesException;

	@Override
	public View createQueryView(String arg0, String arg1, View arg2, boolean arg3) throws NotesException;

	@Override
	public lotus.domino.Database createReplica(String arg0, String arg1) throws NotesException;

	@Override
	public View createView() throws NotesException;

	@Override
	public View createView(String arg0) throws NotesException;

	@Override
	public View createView(String arg0, String arg1) throws NotesException;

	@Override
	public View createView(String arg0, String arg1, View arg2) throws NotesException;

	@Override
	public View createView(String arg0, String arg1, View arg2, boolean arg3) throws NotesException;

	@Override
	public void enableFolder(String arg0) throws NotesException;

	@Override
	public void fixup() throws NotesException;

	@Override
	public void fixup(int arg0) throws NotesException;

	@Override
	public Document FTDomainSearch(String arg0, int arg1, int arg2, int arg3, int arg4, int arg5, String arg6) throws NotesException;

	@Override
	public DocumentCollection FTSearch(String arg0) throws NotesException;

	@Override
	public DocumentCollection FTSearch(String arg0, int arg1) throws NotesException;

	@Override
	public DocumentCollection FTSearch(String arg0, int arg1, int arg2, int arg3) throws NotesException;

	@Override
	public DocumentCollection FTSearchRange(String arg0, int arg1, int arg2, int arg3, int arg4) throws NotesException;

	@Override
	public ACL getACL() throws NotesException;

	@Override
	public Agent getAgent(String arg0) throws NotesException;

	@Override
	public Vector getAgents() throws NotesException;

	@Override
	public DocumentCollection getAllDocuments() throws NotesException;

	@Override
	public DocumentCollection getAllReadDocuments() throws NotesException;

	@Override
	public DocumentCollection getAllReadDocuments(String arg0) throws NotesException;

	@Override
	public DocumentCollection getAllUnreadDocuments() throws NotesException;

	@Override
	public DocumentCollection getAllUnreadDocuments(String arg0) throws NotesException;

	@Override
	public String getCategories() throws NotesException;

	@Override
	public DateTime getCreated() throws NotesException;

	@Override
	public int getCurrentAccessLevel() throws NotesException;

	@Override
	public String getDB2Schema() throws NotesException;

	@Override
	public lotus.domino.Database getDelegate();

	@Override
	public String getDesignTemplateName() throws NotesException;

	@Override
	public Document getDocumentByID(String arg0) throws NotesException;

	@Override
	public Document getDocumentByUNID(String arg0) throws NotesException;

	@Override
	public Document getDocumentByURL(String arg0, boolean arg1) throws NotesException;

	@Override
	public Document getDocumentByURL(String arg0, boolean arg1, boolean arg2, boolean arg3, String arg4, String arg5, String arg6,
			String arg7, String arg8, boolean arg9) throws NotesException;

	@Override
	public int getFileFormat() throws NotesException;

	@Override
	public String getFileName() throws NotesException;

	@Override
	public String getFilePath() throws NotesException;

	@Override
	public boolean getFolderReferencesEnabled() throws NotesException;

	@Override
	public Form getForm(String arg0) throws NotesException;

	@Override
	public Vector getForms() throws NotesException;

	@Override
	public int getFTIndexFrequency() throws NotesException;

	@Override
	public String getHttpURL() throws NotesException;

	@Override
	public DateTime getLastFixup() throws NotesException;

	@Override
	public DateTime getLastFTIndexed() throws NotesException;

	@Override
	public DateTime getLastModified() throws NotesException;

	@Override
	public double getLimitRevisions() throws NotesException;

	@Override
	public double getLimitUpdatedBy() throws NotesException;

	@Override
	public boolean getListInDbCatalog() throws NotesException;

	@Override
	public Vector getManagers() throws NotesException;

	@Override
	public long getMaxSize() throws NotesException;

	@Override
	public DocumentCollection getModifiedDocuments() throws NotesException;

	@Override
	public DocumentCollection getModifiedDocuments(DateTime arg0) throws NotesException;

	@Override
	public DocumentCollection getModifiedDocuments(DateTime arg0, int arg1) throws NotesException;

	@Override
	public String getNotesURL() throws NotesException;

	@Override
	public boolean getOption(int arg0) throws NotesException;

	@Override
	public Outline getOutline(String arg0) throws NotesException;

	@Override
	public Session getParent() throws NotesException;

	@Override
	public double getPercentUsed() throws NotesException;

	@Override
	public DocumentCollection getProfileDocCollection(String arg0) throws NotesException;

	@Override
	public Document getProfileDocument(String arg0, String arg1) throws NotesException;

	@Override
	public String getReplicaID() throws NotesException;

	@Override
	public Replication getReplicationInfo() throws NotesException;

	@Override
	public String getServer() throws NotesException;

	@Override
	public double getSize() throws NotesException;

	@Override
	public int getSizeQuota() throws NotesException;

	@Override
	public long getSizeWarning() throws NotesException;

	@Override
	public String getTemplateName() throws NotesException;

	@Override
	public String getTitle() throws NotesException;

	@Override
	public int getType() throws NotesException;

	@Override
	public int getUndeleteExpireTime() throws NotesException;

	@Override
	public String getURL() throws NotesException;

	@Override
	public String getURLHeaderInfo(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5) throws NotesException;

	@Override
	public View getView(String arg0) throws NotesException;

	@Override
	public org.openntf.domino.impl.Vector<View> getViews() throws NotesException;

	@Override
	public void grantAccess(String arg0, int arg1) throws NotesException;

}

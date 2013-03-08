package org.openntf.domino;

import java.util.Vector;

import lotus.domino.ACL;
import lotus.domino.Agent;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.Form;
import lotus.domino.NoteCollection;
import lotus.domino.Outline;
import lotus.domino.Replication;
import lotus.domino.Session;
import lotus.domino.View;

import org.openntf.domino.annotations.Legacy;

public interface Database extends lotus.domino.Database, org.openntf.domino.Base<lotus.domino.Database> {
	@Override
	@Deprecated
	@Legacy(Legacy.INTERFACES_WARNING)
	public Vector<String> getACLActivityLog();

	@Override
	public int compact();

	@Override
	public int compactWithOptions(int arg0);

	@Override
	public int compactWithOptions(int arg0, String arg1);

	@Override
	public int compactWithOptions(String arg0);

	@Override
	public lotus.domino.Database createCopy(String arg0, String arg1);

	@Override
	public lotus.domino.Database createCopy(String arg0, String arg1, int arg2);

	@Override
	public Document createDocument();

	@Override
	public DocumentCollection createDocumentCollection();

	@Override
	public lotus.domino.Database createFromTemplate(String arg0, String arg1, boolean arg2);

	@Override
	public lotus.domino.Database createFromTemplate(String arg0, String arg1, boolean arg2, int arg3);

	@Override
	public void createFTIndex(int arg0, boolean arg1);

	@Override
	public NoteCollection createNoteCollection(boolean arg0);

	@Override
	public Outline createOutline(String arg0);

	@Override
	public Outline createOutline(String arg0, boolean arg1);

	@Override
	public View createQueryView(String arg0, String arg1);

	@Override
	public View createQueryView(String arg0, String arg1, View arg2);

	@Override
	public View createQueryView(String arg0, String arg1, View arg2, boolean arg3);

	@Override
	public lotus.domino.Database createReplica(String arg0, String arg1);

	@Override
	public View createView();

	@Override
	public View createView(String arg0);

	@Override
	public View createView(String arg0, String arg1);

	@Override
	public View createView(String arg0, String arg1, View arg2);

	@Override
	public View createView(String arg0, String arg1, View arg2, boolean arg3);

	@Override
	public void enableFolder(String arg0);

	@Override
	public void fixup();

	@Override
	public void fixup(int arg0);

	@Override
	public Document FTDomainSearch(String arg0, int arg1, int arg2, int arg3, int arg4, int arg5, String arg6);

	@Override
	public DocumentCollection FTSearch(String arg0);

	@Override
	public DocumentCollection FTSearch(String arg0, int arg1);

	@Override
	public DocumentCollection FTSearch(String arg0, int arg1, int arg2, int arg3);

	@Override
	public DocumentCollection FTSearchRange(String arg0, int arg1, int arg2, int arg3, int arg4);

	@Override
	public ACL getACL();

	@Override
	public Agent getAgent(String arg0);

	@Override
	@Legacy(Legacy.INTERFACES_WARNING)
	public Vector<Agent> getAgents();

	@Override
	public org.openntf.domino.DocumentCollection getAllDocuments();

	@Override
	public DocumentCollection getAllReadDocuments();

	@Override
	public DocumentCollection getAllReadDocuments(String arg0);

	@Override
	public DocumentCollection getAllUnreadDocuments();

	@Override
	public DocumentCollection getAllUnreadDocuments(String arg0);

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
	public Document getDocumentByID(String arg0);

	@Override
	public Document getDocumentByUNID(String arg0);

	@Override
	public Document getDocumentByURL(String arg0, boolean arg1);

	@Override
	public Document getDocumentByURL(String arg0, boolean arg1, boolean arg2, boolean arg3, String arg4, String arg5, String arg6,
			String arg7, String arg8, boolean arg9);

	@Override
	public int getFileFormat();

	@Override
	public String getFileName();

	@Override
	public String getFilePath();

	@Override
	public boolean getFolderReferencesEnabled();

	@Override
	public Form getForm(String arg0);

	@Override
	@Legacy(Legacy.INTERFACES_WARNING)
	public Vector<org.openntf.domino.Form> getForms();

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
	public DocumentCollection getModifiedDocuments(lotus.domino.DateTime arg0);

	@Override
	public DocumentCollection getModifiedDocuments(lotus.domino.DateTime arg0, int arg1);

	@Override
	public String getNotesURL();

	@Override
	public boolean getOption(int arg0);

	@Override
	public Outline getOutline(String arg0);

	@Override
	public Session getParent();

	@Override
	public double getPercentUsed();

	@Override
	public DocumentCollection getProfileDocCollection(String arg0);

	@Override
	public Document getProfileDocument(String arg0, String arg1);

	@Override
	public String getReplicaID();

	@Override
	public Replication getReplicationInfo();

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
	public String getURLHeaderInfo(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5);

	@Override
	public View getView(String arg0);

	@Override
	@Legacy(Legacy.INTERFACES_WARNING)
	public org.openntf.domino.impl.Vector<View> getViews();

	@Override
	public void grantAccess(String arg0, int arg1);

}

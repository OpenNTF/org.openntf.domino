package org.openntf.domino;

import java.util.Vector;

import lotus.domino.Database;
import lotus.domino.DateTime;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.ViewColumn;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;
import lotus.domino.ViewNavigator;

import org.openntf.domino.annotations.Legacy;

public interface View extends lotus.domino.View, Base<lotus.domino.View> {
	@Override
	public void clear();

	@Override
	public ViewColumn copyColumn(int arg0);

	@Override
	public ViewColumn copyColumn(int arg0, int arg1);

	@Override
	public ViewColumn copyColumn(String arg0);

	@Override
	public ViewColumn copyColumn(String arg0, int arg1);

	@Override
	public ViewColumn copyColumn(ViewColumn arg0);

	@Override
	public ViewColumn copyColumn(ViewColumn arg0, int arg1);

	@Override
	public ViewColumn createColumn();

	@Override
	public ViewColumn createColumn(int arg0);

	@Override
	public ViewColumn createColumn(int arg0, String arg1);

	@Override
	public ViewColumn createColumn(int arg0, String arg1, String arg2);

	@Override
	public ViewEntryCollection createViewEntryCollection();

	@Override
	public ViewNavigator createViewNav();

	@Override
	public ViewNavigator createViewNav(int arg0);

	@Override
	public ViewNavigator createViewNavFrom(Object arg0);

	@Override
	public ViewNavigator createViewNavFrom(Object arg0, int arg1);

	@Override
	public ViewNavigator createViewNavFromAllUnread();

	@Override
	public ViewNavigator createViewNavFromAllUnread(String arg0);

	@Override
	public ViewNavigator createViewNavFromCategory(String arg0);

	@Override
	public ViewNavigator createViewNavFromCategory(String arg0, int arg1);

	@Override
	public ViewNavigator createViewNavFromChildren(Object arg0);

	@Override
	public ViewNavigator createViewNavFromChildren(Object arg0, int arg1);

	@Override
	public ViewNavigator createViewNavFromDescendants(Object arg0);

	@Override
	public ViewNavigator createViewNavFromDescendants(Object arg0, int arg1);

	@Override
	public ViewNavigator createViewNavMaxLevel(int arg0);

	@Override
	public ViewNavigator createViewNavMaxLevel(int arg0, int arg1);

	@Override
	public int FTSearch(String arg0);

	@Override
	public int FTSearch(String arg0, int arg1);

	@Override
	public int FTSearchSorted(String arg0);

	@Override
	public int FTSearchSorted(String arg0, int arg1);

	@Override
	public int FTSearchSorted(String arg0, int arg1, int arg2);

	@Override
	public int FTSearchSorted(String arg0, int arg1, int arg2, boolean arg3, boolean arg4, boolean arg5, boolean arg6);

	@Override
	public int FTSearchSorted(String arg0, int arg1, String arg2);

	@Override
	public int FTSearchSorted(String arg0, int arg1, String arg2, boolean arg3, boolean arg4, boolean arg5, boolean arg6);

	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public int FTSearchSorted(Vector arg0);

	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public int FTSearchSorted(Vector arg0, int arg1);

	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public int FTSearchSorted(Vector arg0, int arg1, int arg2);

	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public int FTSearchSorted(Vector arg0, int arg1, int arg2, boolean arg3, boolean arg4, boolean arg5, boolean arg6);

	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public int FTSearchSorted(Vector arg0, int arg1, String arg2);

	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public int FTSearchSorted(Vector arg0, int arg1, String arg2, boolean arg3, boolean arg4, boolean arg5, boolean arg6);

	@Override
	@Legacy(Legacy.INTERFACES_WARNING)
	public Vector<String> getAliases();

	@Override
	public DocumentCollection getAllDocumentsByKey(Object arg0);

	@Override
	public DocumentCollection getAllDocumentsByKey(Object arg0, boolean arg1);

	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public DocumentCollection getAllDocumentsByKey(Vector arg0);

	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public DocumentCollection getAllDocumentsByKey(Vector arg0, boolean arg1);

	@Override
	public ViewEntryCollection getAllEntries();

	@Override
	public ViewEntryCollection getAllEntriesByKey(Object arg0);

	@Override
	public ViewEntryCollection getAllEntriesByKey(Object arg0, boolean arg1);

	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public ViewEntryCollection getAllEntriesByKey(Vector arg0);

	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public ViewEntryCollection getAllEntriesByKey(Vector arg0, boolean arg1);

	@Override
	public ViewEntryCollection getAllReadEntries();

	@Override
	public ViewEntryCollection getAllReadEntries(String arg0);

	@Override
	public ViewEntryCollection getAllUnreadEntries();

	@Override
	public ViewEntryCollection getAllUnreadEntries(String arg0);

	@Override
	public int getBackgroundColor();

	@Override
	public Document getChild(Document arg0);

	@Override
	public ViewColumn getColumn(int arg0);

	@Override
	public int getColumnCount();

	@Override
	@Legacy(Legacy.INTERFACES_WARNING)
	public Vector<String> getColumnNames();

	@Override
	@Legacy(Legacy.INTERFACES_WARNING)
	public Vector<ViewColumn> getColumns();

	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public Vector getColumnValues(int arg0);

	@Override
	public DateTime getCreated();

	@Override
	public lotus.domino.View getDelegate();

	@Override
	public Document getDocumentByKey(Object arg0);

	@Override
	public Document getDocumentByKey(Object arg0, boolean arg1);

	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public Document getDocumentByKey(Vector arg0);

	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public Document getDocumentByKey(Vector arg0, boolean arg1);

	@Override
	public ViewEntry getEntryByKey(Object arg0);

	@Override
	public ViewEntry getEntryByKey(Object arg0, boolean arg1);

	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public ViewEntry getEntryByKey(Vector arg0);

	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public ViewEntry getEntryByKey(Vector arg0, boolean arg1);

	@Override
	public int getEntryCount();

	@Override
	public Document getFirstDocument();

	@Override
	public int getHeaderLines();

	@Override
	public String getHttpURL();

	@Override
	public Document getLastDocument();

	@Override
	public DateTime getLastModified();

	@Override
	@Legacy(Legacy.INTERFACES_WARNING)
	@Deprecated
	public Vector<String> getLockHolders();

	@Override
	public String getName();

	@Override
	public Document getNextDocument(Document arg0);

	@Override
	public Document getNextSibling(Document arg0);

	@Override
	public String getNotesURL();

	@Override
	public Document getNthDocument(int arg0);

	@Override
	public Database getParent();

	@Override
	public Document getParentDocument(Document arg0);

	@Override
	public Document getPrevDocument(Document arg0);

	@Override
	public Document getPrevSibling(Document arg0);

	@Override
	@Deprecated
	@Legacy(Legacy.INTERFACES_WARNING)
	public Vector<String> getReaders();

	@Override
	public int getRowLines();

	@Override
	public String getSelectionFormula();

	@Override
	public String getSelectionQuery();

	@Override
	public int getSpacing();

	@Override
	public int getTopLevelEntryCount();

	@Override
	public String getUniversalID();

	@Override
	public String getURL();

	@Override
	public String getViewInheritedName();

	@Override
	public boolean isAutoUpdate();

	@Override
	public boolean isCalendar();

	@Override
	public boolean isCategorized();

	@Override
	public boolean isConflict();

	@Override
	public boolean isDefaultView();

	@Override
	public boolean isEnableNoteIDsForCategories();

	@Override
	public boolean isFolder();

	@Override
	public boolean isHierarchical();

	@Override
	public boolean isModified();

	@Override
	public boolean isPrivate();

	@Override
	public boolean isProhibitDesignRefresh();

	@Override
	public boolean isProtectReaders();

	@Override
	public boolean isQueryView();

	@Override
	public boolean lock();

	@Override
	public boolean lock(boolean arg0);

	@Override
	public boolean lock(String arg0);

	@Override
	public boolean lock(String arg0, boolean arg1);

	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public boolean lock(Vector arg0);

	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public boolean lock(Vector arg0, boolean arg1);

	@Override
	public boolean lockProvisional();

	@Override
	public boolean lockProvisional(String arg0);

	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public boolean lockProvisional(Vector arg0);

	@Override
	public void markAllRead();

	@Override
	public void markAllRead(String arg0);

	@Override
	public void markAllUnread();

	@Override
	public void markAllUnread(String arg0);

	@Override
	public void refresh();

	@Override
	public void remove();

	@Override
	public void removeColumn();

	@Override
	public void removeColumn(int arg0);

	@Override
	public void removeColumn(String arg0);

	@Override
	public void resortView();

	@Override
	public void resortView(String arg0);

	@Override
	public void resortView(String arg0, boolean arg1);

	@Override
	public void setAliases(String arg0);

	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public void setAliases(Vector arg0);

	@Override
	public void setAutoUpdate(boolean arg0);

	@Override
	public void setBackgroundColor(int arg0);

	@Override
	public void setDefaultView(boolean arg0);

	@Override
	public void setEnableNoteIDsForCategories(boolean arg0);

	@Override
	public void setName(String arg0);

	@Override
	public void setProhibitDesignRefresh(boolean arg0);

	@Override
	public void setProtectReaders(boolean arg0);

	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public void setReaders(Vector arg0);

	@Override
	public void setSelectionFormula(String arg0);

	@Override
	public void setSelectionQuery(String arg0);

	@Override
	public void setSpacing(int arg0);

	@Override
	public void unlock();
}

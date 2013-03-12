package org.openntf.domino;

import java.util.Vector;

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
	public ViewColumn copyColumn(int sourceColumn);

	@Override
	public ViewColumn copyColumn(int sourceColumn, int destinationIndex);

	@Override
	public ViewColumn copyColumn(String sourceColumn);

	@Override
	public ViewColumn copyColumn(String sourceColumn, int destinationIndex);

	@Override
	public ViewColumn copyColumn(ViewColumn sourceColumn);

	@Override
	public ViewColumn copyColumn(ViewColumn sourceColumn, int destinationIndex);

	@Override
	public ViewColumn createColumn();

	@Override
	public ViewColumn createColumn(int position);

	@Override
	public ViewColumn createColumn(int position, String columnTitle);

	@Override
	public ViewColumn createColumn(int position, String columnTitle, String formula);

	@Override
	public ViewEntryCollection createViewEntryCollection();

	@Override
	public ViewNavigator createViewNav();

	@Override
	public ViewNavigator createViewNav(int cacheSize);

	@Override
	public ViewNavigator createViewNavFrom(Object entry);

	@Override
	public ViewNavigator createViewNavFrom(Object entry, int cacheSize);

	@Override
	public ViewNavigator createViewNavFromAllUnread();

	@Override
	public ViewNavigator createViewNavFromAllUnread(String userName);

	@Override
	public ViewNavigator createViewNavFromCategory(String categoryName);

	@Override
	public ViewNavigator createViewNavFromCategory(String categoryName, int cacheSize);

	@Override
	public ViewNavigator createViewNavFromChildren(Object entry);

	@Override
	public ViewNavigator createViewNavFromChildren(Object entry, int cacheSize);

	@Override
	public ViewNavigator createViewNavFromDescendants(Object entry);

	@Override
	public ViewNavigator createViewNavFromDescendants(Object entry, int cacheSize);

	@Override
	public ViewNavigator createViewNavMaxLevel(int level);

	@Override
	public ViewNavigator createViewNavMaxLevel(int level, int cacheSize);

	@Override
	public int FTSearch(String query);

	@Override
	public int FTSearch(String query, int maxDocs);

	@Override
	public int FTSearchSorted(String query);

	@Override
	public int FTSearchSorted(String query, int maxDocs);

	@Override
	public int FTSearchSorted(String query, int maxDocs, int column);

	@Override
	public int FTSearchSorted(String query, int maxDocs, int column, boolean ascending, boolean exact, boolean variants, boolean fuzzy);

	@Override
	public int FTSearchSorted(String query, int maxDocs, String column);

	@Override
	public int FTSearchSorted(String query, int maxDocs, String column, boolean ascending, boolean exact, boolean variants, boolean fuzzy);

	@Override
	@Deprecated
	@Legacy( { Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public int FTSearchSorted(Vector query);

	@Override
	@Deprecated
	@Legacy( { Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public int FTSearchSorted(Vector query, int maxDocs);

	@Override
	@Deprecated
	@Legacy( { Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public int FTSearchSorted(Vector query, int maxDocs, int column);

	@Override
	@Deprecated
	@Legacy( { Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public int FTSearchSorted(Vector query, int maxDocs, int column, boolean ascending, boolean exact, boolean variants, boolean fuzzy);

	@Override
	@Deprecated
	@Legacy( { Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public int FTSearchSorted(Vector query, int maxDocs, String column);

	@Override
	@Deprecated
	@Legacy( { Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public int FTSearchSorted(Vector query, int maxDocs, String column, boolean ascending, boolean exact, boolean variants, boolean fuzzy);

	@Override
	@Legacy(Legacy.INTERFACES_WARNING)
	public Vector<String> getAliases();

	@Override
	public DocumentCollection getAllDocumentsByKey(Object key);

	@Override
	public DocumentCollection getAllDocumentsByKey(Object key, boolean exact);

	@Override
	@Deprecated
	@Legacy( { Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public DocumentCollection getAllDocumentsByKey(Vector keys);

	@Override
	@Deprecated
	@Legacy( { Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public DocumentCollection getAllDocumentsByKey(Vector keys, boolean exact);

	@Override
	public ViewEntryCollection getAllEntries();

	@Override
	public ViewEntryCollection getAllEntriesByKey(Object key);

	@Override
	public ViewEntryCollection getAllEntriesByKey(Object key, boolean exact);

	@Override
	@Deprecated
	@Legacy( { Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public ViewEntryCollection getAllEntriesByKey(Vector keys);

	@Override
	@Deprecated
	@Legacy( { Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public ViewEntryCollection getAllEntriesByKey(Vector keys, boolean exact);

	@Override
	public ViewEntryCollection getAllReadEntries();

	@Override
	public ViewEntryCollection getAllReadEntries(String userName);

	@Override
	public ViewEntryCollection getAllUnreadEntries();

	@Override
	public ViewEntryCollection getAllUnreadEntries(String userName);

	@Override
	public int getBackgroundColor();

	@Override
	public Document getChild(Document doc);

	@Override
	public ViewColumn getColumn(int columnNumber);

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
	@Legacy( { Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public Vector<Object> getColumnValues(int column);

	@Override
	public DateTime getCreated();

	@Override
	public Document getDocumentByKey(Object key);

	@Override
	public Document getDocumentByKey(Object key, boolean exact);

	@Override
	@Deprecated
	@Legacy( { Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public Document getDocumentByKey(Vector keys);

	@Override
	@Deprecated
	@Legacy( { Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public Document getDocumentByKey(Vector keys, boolean exact);

	@Override
	public ViewEntry getEntryByKey(Object key);

	@Override
	public ViewEntry getEntryByKey(Object key, boolean exact);

	@Override
	@Deprecated
	@Legacy( { Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public ViewEntry getEntryByKey(Vector keys);

	@Override
	@Deprecated
	@Legacy( { Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public ViewEntry getEntryByKey(Vector keys, boolean exact);

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
	public Document getNextDocument(Document doc);

	@Override
	public Document getNextSibling(Document doc);

	@Override
	public String getNotesURL();

	@Override
	public Document getNthDocument(int n);

	@Override
	public org.openntf.domino.Database getParent();

	@Override
	public Document getParentDocument(Document doc);

	@Override
	public Document getPrevDocument(Document doc);

	@Override
	public Document getPrevSibling(Document doc);

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
	public boolean lock(boolean provisionalOk);

	@Override
	public boolean lock(String name);

	@Override
	public boolean lock(String name, boolean provisionalOk);

	@Override
	@Deprecated
	@Legacy( { Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public boolean lock(Vector names);

	@Override
	@Deprecated
	@Legacy( { Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public boolean lock(Vector names, boolean provisionalOk);

	@Override
	public boolean lockProvisional();

	@Override
	public boolean lockProvisional(String name);

	@Override
	@Deprecated
	@Legacy( { Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public boolean lockProvisional(Vector names);

	@Override
	public void markAllRead();

	@Override
	public void markAllRead(String userName);

	@Override
	public void markAllUnread();

	@Override
	public void markAllUnread(String userName);

	@Override
	public void refresh();

	@Override
	public void remove();

	@Override
	public void removeColumn();

	@Override
	public void removeColumn(int column);

	@Override
	public void removeColumn(String column);

	@Override
	public void resortView();

	@Override
	public void resortView(String column);

	@Override
	public void resortView(String column, boolean ascending);

	@Override
	public void setAliases(String alias);

	@Override
	@Deprecated
	@Legacy( { Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public void setAliases(Vector aliases);

	@Override
	public void setAutoUpdate(boolean flag);

	@Override
	public void setBackgroundColor(int color);

	@Override
	public void setDefaultView(boolean flag);

	@Override
	public void setEnableNoteIDsForCategories(boolean flag);

	@Override
	public void setName(String name);

	@Override
	public void setProhibitDesignRefresh(boolean flag);

	@Override
	public void setProtectReaders(boolean flag);

	@Override
	@Deprecated
	@Legacy( { Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public void setReaders(Vector readers);

	@Override
	public void setSelectionFormula(String formula);

	@Override
	public void setSelectionQuery(String query);

	@Override
	public void setSpacing(int spacing);

	@Override
	public void unlock();

	/*
	 * New methods
	 */
	public Document getDocument();
}

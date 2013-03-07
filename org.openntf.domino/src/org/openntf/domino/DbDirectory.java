package org.openntf.domino;

import java.util.Vector;

import lotus.domino.DateTime;
import lotus.domino.Session;

public interface DbDirectory extends Base<lotus.domino.DbDirectory>, lotus.domino.DbDirectory {

	@Override
	public Database createDatabase(String arg0);

	@Override
	public Database createDatabase(String arg0, boolean arg1);

	@Override
	public String getClusterName();

	@Override
	public String getClusterName(String arg0);

	@Override
	public lotus.domino.DbDirectory getDelegate();

	@Override
	public lotus.domino.Database getFirstDatabase(int arg0);

	@Override
	public String getName();

	@Override
	public lotus.domino.Database getNextDatabase();

	@Override
	public Session getParent();

	@Override
	public boolean isHonorShowInOpenDatabaseDialog();

	@Override
	public lotus.domino.Database openDatabase(String arg0);

	@Override
	public lotus.domino.Database openDatabase(String arg0, boolean arg1);

	@Override
	public lotus.domino.Database openDatabaseByReplicaID(String arg0);

	@Override
	public lotus.domino.Database openDatabaseIfModified(String arg0, DateTime arg1);

	@Override
	public lotus.domino.Database openMailDatabase();

	@Override
	public void recycle();

	@Override
	public void recycle(Vector arg0);

	@Override
	public void setHonorShowInOpenDatabaseDialog(boolean arg0);

	@Override
	public boolean equals(Object obj);

	@Override
	public int hashCode();

	@Override
	public String toString();

}

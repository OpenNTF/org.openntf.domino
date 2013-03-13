package org.openntf.domino;

public interface DbDirectory extends Base<lotus.domino.DbDirectory>, lotus.domino.DbDirectory {

	@Override
	public Database createDatabase(String dbFile);

	@Override
	public Database createDatabase(String dbFile, boolean open);

	@Override
	public String getClusterName();

	@Override
	public String getClusterName(String server);

	@Override
	public Database getFirstDatabase(int type);

	@Override
	public String getName();

	@Override
	public Database getNextDatabase();

	@Override
	public Session getParent();

	@Override
	public boolean isHonorShowInOpenDatabaseDialog();

	@Override
	public Database openDatabase(String dbFile);

	@Override
	public Database openDatabase(String dbFile, boolean failover);

	@Override
	public Database openDatabaseByReplicaID(String replicaId);

	@Override
	public Database openDatabaseIfModified(String dbFile, lotus.domino.DateTime date);

	@Override
	public Database openMailDatabase();

	@Override
	public void setHonorShowInOpenDatabaseDialog(boolean flag);

}

package org.openntf.domino.impl;

import lotus.domino.NotesException;

import org.openntf.domino.Database;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class DbDirectory extends Base<org.openntf.domino.DbDirectory, lotus.domino.DbDirectory> implements org.openntf.domino.DbDirectory {

	public DbDirectory(lotus.domino.DbDirectory delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	@Override
	public Database createDatabase(String dbFile) {
		try {
			return Factory.fromLotus(getDelegate().createDatabase(dbFile), Database.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public Database createDatabase(String dbFile, boolean open) {
		try {
			return Factory.fromLotus(getDelegate().createDatabase(dbFile, open), Database.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getClusterName() {
		try {
			return getDelegate().getClusterName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getClusterName(String server) {
		try {
			return getDelegate().getClusterName(server);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public Database getFirstDatabase(int type) {
		try {
			return Factory.fromLotus(getDelegate().getFirstDatabase(type), Database.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getName() {
		try {
			return getDelegate().getName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public Database getNextDatabase() {
		try {
			return Factory.fromLotus(getDelegate().getNextDatabase(), Database.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public org.openntf.domino.Session getParent() {
		return (org.openntf.domino.Session) super.getParent();
	}

	@Override
	public boolean isHonorShowInOpenDatabaseDialog() {
		try {
			return getDelegate().isHonorShowInOpenDatabaseDialog();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public Database openDatabase(String dbFile) {
		try {
			return Factory.fromLotus(getDelegate().openDatabase(dbFile), Database.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public Database openDatabase(String dbFile, boolean failover) {
		try {
			return Factory.fromLotus(getDelegate().openDatabase(dbFile, failover), Database.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public Database openDatabaseByReplicaID(String replicaId) {
		try {
			return Factory.fromLotus(getDelegate().openDatabaseByReplicaID(replicaId), Database.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public Database openDatabaseIfModified(String dbFile, lotus.domino.DateTime date) {
		try {
			return Factory.fromLotus(getDelegate().openDatabaseIfModified(dbFile, date), Database.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public Database openMailDatabase() {
		try {
			return Factory.fromLotus(getDelegate().openMailDatabase(), Database.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public void setHonorShowInOpenDatabaseDialog(boolean flag) {
		try {
			getDelegate().setHonorShowInOpenDatabaseDialog(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

}

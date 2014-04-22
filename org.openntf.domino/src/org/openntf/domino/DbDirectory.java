/*
 * Copyright 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package org.openntf.domino;

import java.util.Collection;

import org.openntf.domino.annotations.Legacy;
import org.openntf.domino.types.FactorySchema;
import org.openntf.domino.types.SessionDescendant;

/**
 * The Interface DbDirectory.
 */
public interface DbDirectory extends Base<lotus.domino.DbDirectory>, lotus.domino.DbDirectory, org.openntf.domino.ext.DbDirectory,
		Collection<org.openntf.domino.Database>, SessionDescendant {

	public static class Schema extends FactorySchema<DbDirectory, lotus.domino.DbDirectory, Session> {
		@Override
		public Class<DbDirectory> typeClass() {
			return DbDirectory.class;
		}

		@Override
		public Class<lotus.domino.DbDirectory> delegateClass() {
			return lotus.domino.DbDirectory.class;
		}

		@Override
		public Class<Session> parentClass() {
			return Session.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/**
	 * Enum to allow easy access to database types, e.g. Database, Template Candidate
	 * 
	 * @since org.openntf.domino 1.0.0
	 */
	public static enum Type {

		/** The database. */
		DATABASE(DbDirectory.DATABASE),
		/** The template. */
		TEMPLATE(DbDirectory.TEMPLATE),
		/** The replica candidate. */
		REPLICA_CANDIDATE(DbDirectory.REPLICA_CANDIDATE),
		/** The template candidate. */
		TEMPLATE_CANDIDATE(DbDirectory.TEMPLATE_CANDIDATE);

		public static Type getType(final int value) {
			for (Type type : Type.values()) {
				if (type.getValue() == value) {
					return type;
				}
			}
			return null;
		}

		/** The value_. */
		private final int value_;

		/**
		 * Instantiates a new type.
		 * 
		 * @param value
		 *            the value
		 */
		private Type(final int value) {
			value_ = value;
		}

		/**
		 * Gets the value.
		 * 
		 * @return the value
		 */
		public int getValue() {
			return value_;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DbDirectory#createDatabase(java.lang.String)
	 */
	@Override
	public Database createDatabase(final String dbFile);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DbDirectory#createDatabase(java.lang.String, boolean)
	 */
	@Override
	public Database createDatabase(final String dbFile, final boolean open);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DbDirectory#getClusterName()
	 */
	@Override
	public String getClusterName();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DbDirectory#getClusterName(java.lang.String)
	 */
	@Override
	public String getClusterName(final String server);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DbDirectory#getFirstDatabase(int)
	 */
	@Override
	@Deprecated
	@Legacy(Legacy.ITERATION_WARNING)
	public Database getFirstDatabase(final int type);

	/**
	 * Gets the first database.
	 * 
	 * @param type
	 *            the type
	 * @return the first database
	 */
	@Override
	@Deprecated
	@Legacy(Legacy.ITERATION_WARNING)
	public Database getFirstDatabase(final Type type);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DbDirectory#getName()
	 */
	@Override
	public String getName();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DbDirectory#getNextDatabase()
	 */
	@Override
	@Deprecated
	@Legacy(Legacy.ITERATION_WARNING)
	public Database getNextDatabase();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DbDirectory#getParent()
	 */
	@Override
	public Session getParent();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DbDirectory#isHonorShowInOpenDatabaseDialog()
	 */
	@Override
	public boolean isHonorShowInOpenDatabaseDialog();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DbDirectory#openDatabase(java.lang.String)
	 */
	@Override
	public Database openDatabase(final String dbFile);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DbDirectory#openDatabase(java.lang.String, boolean)
	 */
	@Override
	public Database openDatabase(final String dbFile, final boolean failover);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DbDirectory#openDatabaseByReplicaID(java.lang.String)
	 */
	@Override
	public Database openDatabaseByReplicaID(final String replicaId);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DbDirectory#openDatabaseIfModified(java.lang.String, lotus.domino.DateTime)
	 */
	@Override
	public Database openDatabaseIfModified(final String dbFile, final lotus.domino.DateTime date);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DbDirectory#openMailDatabase()
	 */
	@Override
	public Database openMailDatabase();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DbDirectory#setHonorShowInOpenDatabaseDialog(boolean)
	 */
	@Override
	public void setHonorShowInOpenDatabaseDialog(final boolean flag);

}

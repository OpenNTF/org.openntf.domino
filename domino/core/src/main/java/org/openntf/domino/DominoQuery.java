package org.openntf.domino;

import lotus.domino.DocumentCollection;

import org.openntf.domino.types.DatabaseDescendant;
import org.openntf.domino.types.FactorySchema;

/**
 * Represents the configuration and execution of a DQL query.
 *
 * @since Domino 10.0.1
 */
public interface DominoQuery extends lotus.domino.DominoQuery, Base<lotus.domino.DominoQuery>, DatabaseDescendant {
	public static class Schema extends FactorySchema<DominoQuery, lotus.domino.DominoQuery, Database> {

		@Override
		public Class<DominoQuery> typeClass() {
			return DominoQuery.class;
		}

		@Override
		public Class<lotus.domino.DominoQuery> delegateClass() {
			return lotus.domino.DominoQuery.class;
		}

		@Override
		public Class<Database> parentClass() {
			return Database.class;
		}
	}

	public static final Schema SCHEMA = new Schema();

	@Override
	DocumentCollection execute(String query);

	@Override
	String explain(String query);

	@Override
	int getMaxScanDocs();

	@Override
	int getMaxScanEntries();

	@Override
	int getTimeoutSec();

	@Override
	boolean isNoViews();

	@Override
	boolean isRefreshViews();

	@Override
	String parse(String query);

	@Override
	void resetNamedVariables();

	@Override
	void setMaxScanDocs(int maxScanDocs);

	@Override
	void setMaxScanEntries(int maxScanEntries);

	@Override
	void setNamedVariable(String varName, Object value);

	@Override
	void setNoViews(boolean noViews);

	@Override
	void setRefreshViews(boolean refreshViews);

	@Override
	void setTimeoutSec(int timeoutSec);

}

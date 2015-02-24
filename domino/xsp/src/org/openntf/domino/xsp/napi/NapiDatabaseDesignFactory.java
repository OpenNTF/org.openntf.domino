package org.openntf.domino.xsp.napi;

import org.openntf.domino.Database;

public class NapiDatabaseDesignFactory implements org.openntf.domino.design.NapiDatabaseDesignFactory {

	public static void init() {
		org.openntf.domino.design.impl.DatabaseDesign.napiDesignFactory = new NapiDatabaseDesignFactory();
	}

	@Override
	public org.openntf.domino.design.NapiDatabaseDesign create(final Database database) {
		return new NapiDatabaseDesign(database);
	}

}

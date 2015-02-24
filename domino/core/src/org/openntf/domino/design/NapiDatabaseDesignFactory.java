package org.openntf.domino.design;

import org.openntf.domino.Database;

public interface NapiDatabaseDesignFactory {

	NapiDatabaseDesign create(Database database);

}

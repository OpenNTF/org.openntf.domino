package org.openntf.domino.design;

import org.openntf.domino.design.impl.DesignCollection;

public interface NapiDatabaseDesign {
	public <T extends DesignBase> DesignCollection<T> getDesignElementsByName(final Class<T> type, final String name);

}

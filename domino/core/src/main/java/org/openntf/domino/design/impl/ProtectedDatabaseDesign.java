package org.openntf.domino.design.impl;

import org.openntf.domino.Database;
import org.openntf.domino.design.DesignBase;

public class ProtectedDatabaseDesign extends DatabaseDesign {

	public ProtectedDatabaseDesign(final Database database) {
		super(database);
	}

	@Override
	public DesignCollection<DesignBase> getDesignElements(final String formula) {
		return new org.openntf.domino.design.impl.DesignCollection<DesignBase>(null, DesignBase.class);
	}

	@Override
	public <T extends DesignBase> DesignCollection<T> getDesignElements(final Class<T> type, final String search) {
		return new org.openntf.domino.design.impl.DesignCollection<T>(null, type);
	}

	@Override
	public <T extends DesignBase> DesignCollection<T> getDesignElementsByName(final Class<T> type, final String name) {
		return new org.openntf.domino.design.impl.DesignCollection<T>(null, type);
	}

	@Override
	public <T extends DesignBase> T getDesignElementByName(final Class<T> type, final String name) {
		return null;
	}
}

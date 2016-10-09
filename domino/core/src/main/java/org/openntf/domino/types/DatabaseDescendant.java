package org.openntf.domino.types;

public interface DatabaseDescendant extends SessionDescendant {

	public org.openntf.domino.Database getAncestorDatabase();

}

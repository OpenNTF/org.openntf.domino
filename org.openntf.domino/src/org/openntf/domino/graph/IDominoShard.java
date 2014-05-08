package org.openntf.domino.graph;

public interface IDominoShard {
	public IDominoVertex addVertex(Object id);

	public IDominoVertex getVertex(Object id);

	public org.openntf.domino.Database getRawDatabase();

}

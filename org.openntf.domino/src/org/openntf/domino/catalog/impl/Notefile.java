package org.openntf.domino.catalog.impl;

import java.util.logging.Logger;

import org.openntf.domino.graph.GenericDominoGraph;
import org.openntf.domino.graph.DominoVertex;

public class Notefile extends DominoVertex {
	private static final long serialVersionUID = 1L;
	private static final Logger log_ = Logger.getLogger(Notefile.class.getName());

	public Notefile(final GenericDominoGraph parent, final org.openntf.domino.Document doc) {
		super(parent, doc);
	}

}

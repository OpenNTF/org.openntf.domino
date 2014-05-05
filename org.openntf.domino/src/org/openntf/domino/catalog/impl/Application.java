package org.openntf.domino.catalog.impl;

import java.util.logging.Logger;

import org.openntf.domino.graph.DominoGraph;
import org.openntf.domino.graph.DominoVertex;

public class Application extends DominoVertex {
	private static final long serialVersionUID = 1L;
	private static final Logger log_ = Logger.getLogger(Application.class.getName());

	public Application(final DominoGraph parent, final org.openntf.domino.Document doc) {
		super(parent, doc);
	}

}

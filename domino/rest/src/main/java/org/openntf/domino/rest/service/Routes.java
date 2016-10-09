package org.openntf.domino.rest.service;

public enum Routes {
	;

	public final static String ROOT = "oda";
	public final static String NAMESPACE = "namespace";
	public final static String NAMESPACE_PATH_PARAM = "{" + NAMESPACE + "}";
	public final static String METAID = "metaid";
	public final static String METAID_PATH_PARAM = "{" + METAID + "}";

	public final static String GRAPH = "graph";
	public final static String VERTEX = "vertex";
	public final static String VERTEXES = "vertexes";
	public final static String EDGE = "edge";
	public final static String EDGES = "edges";

	public final static String DATA = "data";
	public final static String DOCUMENT = "document";
	public final static String DOCUMENTS = "documents";
	public final static String VIEW = "view";
	public final static String VIEWS = "views";

	public final static String FRAMED = "frame";
	public final static String FRAMES = "frames";
	public final static String SCHEMA = "schema";
	public final static String COMMAND = "command";

}

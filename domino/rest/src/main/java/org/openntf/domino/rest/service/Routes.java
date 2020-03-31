/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
	public final static String SEARCH = "search";
	public final static String TERMS = "terms";
	public final static String INFO = "info";

}

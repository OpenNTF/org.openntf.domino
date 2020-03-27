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
package org.openntf.domino.rest.resources;

import javax.ws.rs.Path;

import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.rest.service.ODAGraphService;
import org.openntf.domino.rest.service.Routes;

@Path(Routes.NAMESPACE_PATH_PARAM)
public abstract class AbstractResource {
	protected ODAGraphService service_;

	public AbstractResource(ODAGraphService service) {
		service_ = service;
	}

	public ODAGraphService getService() {
		return service_;
	}

	@SuppressWarnings("rawtypes")
	public DFramedTransactionalGraph getGraph(String namespace) {
		return getService().getGraph(namespace);
	}

	// static Map<String, Object> getResourceTargetById(String id, String
	// namespace, ODAGraphService service) {
	// @SuppressWarnings("rawtypes")
	// DFramedTransactionalGraph graph = service.getGraph(namespace);
	// NoteCoordinate nc = NoteCoordinate.Utils.getNoteCoordinate(id);
	// @SuppressWarnings("unchecked")
	// Map<String, Object> jsonMap =
	// getService().toJsonableMap(graph.getElement(nc, null));
	// return jsonMap;
	// }
	//
	// protected Map<String, Object> getResourceTargetById(String id, String
	// namespace) {
	// return getResourceTargetById(id, namespace, getService());
	// }
	//
	// public Object getResourceTargets(final UriInfo uriInfo, final String
	// namespace) {
	// Object result = null;
	// ParamMap pm = Parameters.toParamMap(uriInfo);
	// if (pm.get(Parameters.ID) != null) {
	// List<String> ids = pm.get(Parameters.ID);
	// if (ids.size() == 0) {
	// return null;
	// } else if (ids.size() == 1) {
	// String id = ids.get(0);
	// result = getResourceTargetById(id, namespace);
	// } else {
	// List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
	// for (String id : ids) {
	// maps.add(getResourceTargetById(id, namespace));
	// }
	// result = maps;
	// }
	// } else {
	// System.out.println("TEMP DEBUG: ID was null therefore we can't report...");
	// MultivaluedMap<String, String> mvm = uriInfo.getQueryParameters();
	// for (String key : mvm.keySet()) {
	// System.out.println("TEMP DEBUG: " + key + ": " + mvm.getFirst(key));
	// }
	// }
	// return result;
	// }

}

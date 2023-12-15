/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
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

import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.rest.service.ODAGraphService;

public abstract class AbstractCollectionResource {
	protected ODAGraphService service_;

	public AbstractCollectionResource(ODAGraphService service) {
		service_ = service;
	}

	public ODAGraphService getService() {
		return service_;
	}

	@SuppressWarnings("rawtypes")
	public DFramedTransactionalGraph getGraph(String namespace) {
		return getService().getGraph(namespace);
	}

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
	// result = AbstractResource.getResourceTargetById(id, namespace,
	// getService());
	// } else {
	// List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
	// for (String id : ids) {
	// maps.add(AbstractResource.getResourceTargetById(id, namespace,
	// getService()));
	// }
	// result = maps;
	// }
	// } else if (pm.get(Parameters.TYPE) != null) {
	// List<String> classes = pm.get(Parameters.TYPE);
	//
	// if (classes.size() == 0) {
	// return null;
	// } else if (classes.size() == 1) {
	// String name = classes.get(0);
	// result = AbstractResource.getResourceTargetById(name, namespace,
	// getService());
	// } else {
	// List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
	// for (String name : classes) {
	// maps.add(AbstractResource.getResourceTargetById(name, namespace,
	// getService()));
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

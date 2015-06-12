package org.openntf.domino.rest.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.openntf.domino.big.NoteCoordinate;
import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.rest.service.ODAGraphService;
import org.openntf.domino.rest.service.Parameters;
import org.openntf.domino.rest.service.Parameters.ParamMap;
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

	static Map<String, Object> getResourceTargetById(String id, String namespace, ODAGraphService service) {
		@SuppressWarnings("rawtypes")
		DFramedTransactionalGraph graph = service.getGraph(namespace);
		NoteCoordinate nc = NoteCoordinate.Utils.getNoteCoordinate(id);
		@SuppressWarnings("unchecked")
		Map<String, Object> jsonMap = graph.toJsonableMap(graph.getElement(nc, null));
		return jsonMap;
	}

	protected Map<String, Object> getResourceTargetById(String id, String namespace) {
		return getResourceTargetById(id, namespace, getService());
	}

	public Object getResourceTargets(final UriInfo uriInfo, final String namespace) {
		Object result = null;
		ParamMap pm = Parameters.toParamMap(uriInfo);
		if (pm.get(Parameters.ID) != null) {
			List<String> ids = pm.get(Parameters.ID);
			if (ids.size() == 0) {
				return null;
			} else if (ids.size() == 1) {
				String id = ids.get(0);
				result = getResourceTargetById(id, namespace);
			} else {
				List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
				for (String id : ids) {
					maps.add(getResourceTargetById(id, namespace));
				}
				result = maps;
			}
		} else {
			System.out.println("TEMP DEBUG: ID was null therefore we can't report...");
			MultivaluedMap<String, String> mvm = uriInfo.getQueryParameters();
			for (String key : mvm.keySet()) {
				System.out.println("TEMP DEBUG: " + key + ": " + mvm.getFirst(key));
			}
		}
		return result;
	}

}

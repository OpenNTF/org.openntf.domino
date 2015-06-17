package org.openntf.domino.rest.resources.frames;

import com.ibm.commons.util.io.json.JsonException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.openntf.domino.big.NoteCoordinate;
import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.rest.json.JsonGraphWriter;
import org.openntf.domino.rest.resources.AbstractResource;
import org.openntf.domino.rest.service.ODAGraphService;
import org.openntf.domino.rest.service.Parameters;
import org.openntf.domino.rest.service.Parameters.ParamMap;
import org.openntf.domino.rest.service.Routes;

@Path(Routes.ROOT + "/" + Routes.FRAMED + "/" + Routes.NAMESPACE_PATH_PARAM)
public class FramedResource extends AbstractResource {

	public FramedResource(ODAGraphService service) {
		super(service);
	}

	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFramedObject(@Context final UriInfo uriInfo, @PathParam(Routes.NAMESPACE) final String namespace)
			throws JsonException, IOException {
		@SuppressWarnings("rawtypes")
		DFramedTransactionalGraph graph = this.getGraph(namespace);
		String jsonEntity = null;
		ResponseBuilder builder = Response.ok();
		ParamMap pm = Parameters.toParamMap(uriInfo);
		StringWriter sw = new StringWriter();
		JsonGraphWriter writer = new JsonGraphWriter(sw, false, true);

		// List<CaseInsensitiveString> props =
		// CaseInsensitiveString.toCaseInsensitive(pm.get(Parameters.PROPS));
		// List<CaseInsensitiveString> inProps =
		// CaseInsensitiveString.toCaseInsensitive(pm.get(Parameters.INPROPS));
		// List<CaseInsensitiveString> outProps =
		// CaseInsensitiveString.toCaseInsensitive(pm.get(Parameters.OUTPROPS));
		// List<CaseInsensitiveString> labels =
		// CaseInsensitiveString.toCaseInsensitive(pm.get(Parameters.LABEL));
		// boolean includeEdges = pm.get(Parameters.EDGES) != null;

		if (pm.get(Parameters.ID) != null) {
			List<String> ids = pm.get(Parameters.ID);
			if (ids.size() == 0) {
				writer.outNull();
			} else if (ids.size() == 1) {
				String id = ids.get(0);
				// System.out.println("TEMP DEBUG: Writing output for id " +
				// id);
				NoteCoordinate nc = NoteCoordinate.Utils.getNoteCoordinate(id);
				Map<String, Object> jsonMap = getService().toJsonableMap(graph, graph.getElement(nc, null), pm);
				writer.outObject(jsonMap);
			} else {
				List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
				for (String id : ids) {
					NoteCoordinate nc = NoteCoordinate.Utils.getNoteCoordinate(id);
					Map<String, Object> jsonMap = getService().toJsonableMap(graph, graph.getElement(nc, null), pm);
					maps.add(jsonMap);
				}
				writer.outArrayLiteral(maps);
			}
			jsonEntity = sw.toString();
		} else {
			// System.out.println("TEMP DEBUG: ID was null therefore we can't report...");

			MultivaluedMap<String, String> mvm = uriInfo.getQueryParameters();
			for (String key : mvm.keySet()) {
				// System.out.println("TEMP DEBUG: " + key + ": " +
				// mvm.getFirst(key));
			}
			Map<String, Object> jsonMap = new LinkedHashMap<String, Object>();
			jsonMap.put("namespace", namespace);
			jsonMap.put("status", "active");
			writer.outObject(jsonMap);
			jsonEntity = sw.toString();
		}

		builder.type(MediaType.APPLICATION_JSON_TYPE).entity(jsonEntity);
		Response response = builder.build();
		return response;
	}

}

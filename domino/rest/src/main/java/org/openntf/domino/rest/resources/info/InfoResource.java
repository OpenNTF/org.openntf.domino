package org.openntf.domino.rest.resources.info;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.rest.json.JsonGraphWriter;
import org.openntf.domino.rest.resources.AbstractResource;
import org.openntf.domino.rest.service.IGraphFactory;
import org.openntf.domino.rest.service.ODAGraphService;
import org.openntf.domino.rest.service.Parameters;
import org.openntf.domino.rest.service.Parameters.ParamMap;
import org.openntf.domino.rest.service.Routes;

import com.ibm.commons.util.io.json.JsonException;

@Path(Routes.ROOT + "/" + Routes.INFO + "/" + Routes.NAMESPACE_PATH_PARAM)
public class InfoResource extends AbstractResource {

	public InfoResource(ODAGraphService service) {
		super(service);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response performRequest(@Context final UriInfo uriInfo, @PathParam(Routes.NAMESPACE) final String namespace)
			throws JsonException, IOException {
		String jsonEntity = null;
		ResponseBuilder builder = Response.ok();
		ParamMap pm = Parameters.toParamMap(uriInfo);
		StringWriter sw = new StringWriter();
		DFramedTransactionalGraph<?> graph = this.getGraph(namespace);
		JsonGraphWriter writer = new JsonGraphWriter(sw, graph, pm, false, true, false);
		// writer.outObject(null);
		List<String> items = pm.get(Parameters.ITEM);
		if (items != null && items.size() > 0) {
			IGraphFactory factory = this.getService().getFactory(namespace);
			if (factory != null) {
				for (String item : items) {
					Object curResult = factory.processRequest(namespace, item, uriInfo.getQueryParameters());
					writer.outObject(curResult);
				}
			} else {
				System.err.println("No Factory found for namespace: " + namespace);
			}
		}
		jsonEntity = sw.toString();
		builder.type(MediaType.APPLICATION_JSON_TYPE).entity(jsonEntity);
		Response response = builder.build();
		return response;

	}

}

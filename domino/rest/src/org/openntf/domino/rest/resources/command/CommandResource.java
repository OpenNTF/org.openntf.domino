package org.openntf.domino.rest.resources.command;

import com.ibm.commons.util.io.json.JsonException;

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

import org.openntf.domino.rest.json.JsonGraphWriter;
import org.openntf.domino.rest.resources.AbstractResource;
import org.openntf.domino.rest.service.IGraphFactory;
import org.openntf.domino.rest.service.ODAGraphService;
import org.openntf.domino.rest.service.Parameters;
import org.openntf.domino.rest.service.Parameters.ParamMap;
import org.openntf.domino.rest.service.Routes;

@Path(Routes.ROOT + "/" + Routes.COMMAND + "/" + Routes.NAMESPACE_PATH_PARAM)
public class CommandResource extends AbstractResource {

	public CommandResource(ODAGraphService service) {
		super(service);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response performCommand(@Context final UriInfo uriInfo, @PathParam(Routes.NAMESPACE) final String namespace)
			throws JsonException, IOException {
		String jsonEntity = null;
		ResponseBuilder builder = Response.ok();
		ParamMap pm = Parameters.toParamMap(uriInfo);
		StringWriter sw = new StringWriter();
		JsonGraphWriter writer = new JsonGraphWriter(sw, false, true);
		// writer.outObject(null);
		List<String> commands = pm.get(Parameters.COMMAND);
		if (commands != null && commands.size() > 0) {
			IGraphFactory factory = this.getService().getFactory(namespace);
			List<String> args = pm.get(Parameters.SWITCH);
			for (String command : commands) {
				Object curResult = factory.processCommand(namespace, command, (String[]) args.toArray());
				writer.outObject(curResult);
			}
		}
		jsonEntity = sw.toString();
		builder.type(MediaType.APPLICATION_JSON_TYPE).entity(jsonEntity);
		Response response = builder.build();
		return response;

	}

}

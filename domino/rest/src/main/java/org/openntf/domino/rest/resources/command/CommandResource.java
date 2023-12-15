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

import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.rest.json.JsonGraphWriter;
import org.openntf.domino.rest.resources.AbstractResource;
import org.openntf.domino.rest.service.IGraphFactory;
import org.openntf.domino.rest.service.ODAGraphService;
import org.openntf.domino.rest.service.Parameters;
import org.openntf.domino.rest.service.Parameters.ParamMap;
import org.openntf.domino.rest.service.Routes;

@Path(Routes.ROOT + "/" + Routes.COMMAND + "/" + Routes.NAMESPACE_PATH_PARAM)
@SuppressWarnings("nls")
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
		DFramedTransactionalGraph<?> graph = this.getGraph(namespace);
		JsonGraphWriter writer = new JsonGraphWriter(sw, graph, pm, false, true, false);
		// writer.outObject(null);
		List<String> commands = pm.get(Parameters.COMMAND);
		if (commands != null && commands.size() > 0) {
			IGraphFactory factory = this.getService().getFactory(namespace);
			if (factory != null) {
				for (String command : commands) {
					// System.out.println("TEMP DEBUG processing command " +
					// command + " in namespace " + namespace);
					Object curResult = factory.processCommand(namespace, command, uriInfo.getQueryParameters());
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

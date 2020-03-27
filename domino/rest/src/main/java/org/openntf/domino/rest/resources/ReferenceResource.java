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

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.rest.Activator;
import org.openntf.domino.rest.json.JsonGraphWriter;
import org.openntf.domino.rest.service.ODAGraphService;
import org.openntf.domino.rest.service.Parameters;
import org.openntf.domino.rest.service.Parameters.ParamMap;
import org.openntf.domino.rest.service.Routes;
import org.openntf.domino.utils.DominoUtils;

import com.ibm.commons.util.io.json.JsonException;

@Path(Routes.ROOT)
public class ReferenceResource extends AbstractResource {

	public ReferenceResource(ODAGraphService service) {
		super(service);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFramedObject(@Context final UriInfo uriInfo, @PathParam(Routes.NAMESPACE) final String namespace,
			@Context Request request) throws JsonException, IOException {
		@SuppressWarnings("rawtypes")
		DFramedTransactionalGraph graph = this.getGraph(namespace);
		ParamMap pm = Parameters.toParamMap(uriInfo);
		StringWriter sw = new StringWriter();
		JsonGraphWriter writer = new JsonGraphWriter(sw, graph, pm, false, false, false);

		writer.outObject(Activator.getManifestInfo());

		String jsonEntity = sw.toString();
		ResponseBuilder berg = getBuilder(jsonEntity, null, true, request);
		Response response = berg.build();
		return response;
	}

	private ResponseBuilder getBuilder(String jsonEntity, Date lastMod, boolean includeEtag, Request request) {
		String etagSource = DominoUtils.md5(jsonEntity);
		EntityTag etag = new EntityTag(etagSource);
		ResponseBuilder berg = null;
		if (request != null) {
			berg = request.evaluatePreconditions(etag);
		}
		if (berg == null) {
			// System.out.println("TEMP DEBUG creating a new builder");
			berg = Response.ok();
			if (includeEtag) {
				berg.tag(etag);
			}
			berg.type(MediaType.APPLICATION_JSON_TYPE);
			berg.entity(jsonEntity);
			if (lastMod != null) {
				berg.lastModified(lastMod);
			}
			CacheControl cc = new CacheControl();
			cc.setMustRevalidate(true);
			cc.setPrivate(true);
			cc.setMaxAge(86400);
			cc.setNoTransform(true);
			berg.cacheControl(cc);
		}
		return berg;
	}

}

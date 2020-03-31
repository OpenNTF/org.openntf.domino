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
package org.openntf.domino.rest.servlet;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.domino.das.service.RestService;
import com.ibm.domino.das.servlet.DasServlet;
import com.ibm.domino.das.utils.ErrorHelper;

@Path("")
public class ODARootResource {

	public ODARootResource() {
		// TODO Auto-generated constructor stub
	}

	@GET
	public Response getInfo(@Context final UriInfo uriInfo) {
		String entity = null;

		RestService.verifyNoDatabaseContext();

		URI baseURI;
		try {
			baseURI = copy(uriInfo.getAbsolutePath(), true);
			entity = DasServlet.getServicesResponse(baseURI.toString());
		} catch (IOException e) {
			throw new WebApplicationException(ErrorHelper.createErrorResponse(e, Response.Status.INTERNAL_SERVER_ERROR));
		} catch (JsonException e) {
			throw new WebApplicationException(ErrorHelper.createErrorResponse(e, Response.Status.INTERNAL_SERVER_ERROR));
		}

		ResponseBuilder builder = Response.ok();
		builder.type(MediaType.APPLICATION_JSON_TYPE).entity(entity);
		Response response = builder.build();

		return response;
	}

	/**
	 * Make a relative copy of a URI. Copied from ExtLib because com.ibm.domino.commons is not in core ExtLib, only OpenNTF version
	 *
	 * <p>If makeRelative is false, this may return the original instance.
	 * That should be OK because a URI is immutable.
	 *
	 * @param original
	 * @param makeRelative
	 * @return
	 */
	public static URI copy(final URI original, final boolean makeRelative) {
		URI uri = original;

		if ( uri.isAbsolute() && makeRelative ) {
			String rel = uri.getRawPath();
			if ( uri.getQuery() != null ) {
				rel += "?" + uri.getRawQuery();
			}
			uri = URI.create(rel);
		}

		return uri;
	}

}

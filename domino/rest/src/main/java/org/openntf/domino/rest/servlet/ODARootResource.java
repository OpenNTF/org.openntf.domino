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
import com.ibm.domino.commons.util.UriHelper;
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
			baseURI = UriHelper.copy(uriInfo.getAbsolutePath(), true);
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

}

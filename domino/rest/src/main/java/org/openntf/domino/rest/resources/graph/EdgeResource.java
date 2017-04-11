package org.openntf.domino.rest.resources.graph;

import javax.ws.rs.Path;

import org.openntf.domino.rest.service.ODAGraphService;
import org.openntf.domino.rest.service.Routes;

@Path(Routes.NAMESPACE_PATH_PARAM + "/" + Routes.GRAPH + "/" + Routes.EDGE)
public class EdgeResource extends GraphResource {

	public EdgeResource(ODAGraphService service) {
		super(service);
	}

}

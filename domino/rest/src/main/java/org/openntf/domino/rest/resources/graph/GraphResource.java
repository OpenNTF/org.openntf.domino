package org.openntf.domino.rest.resources.graph;

import javax.ws.rs.Path;

import org.openntf.domino.rest.resources.AbstractResource;
import org.openntf.domino.rest.service.ODAGraphService;
import org.openntf.domino.rest.service.Routes;

@Path(Routes.NAMESPACE_PATH_PARAM + "/" + Routes.GRAPH)
public class GraphResource extends AbstractResource {

	public GraphResource(ODAGraphService service) {
		super(service);
	}

}

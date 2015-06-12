package org.openntf.domino.rest.resources.command;

import javax.ws.rs.Path;

import org.openntf.domino.rest.resources.AbstractResource;
import org.openntf.domino.rest.service.ODAGraphService;
import org.openntf.domino.rest.service.Routes;

@Path(Routes.ROOT + "/" + Routes.COMMAND + "/" + Routes.NAMESPACE_PATH_PARAM)
public class CommandResource extends AbstractResource {

	public CommandResource(ODAGraphService service) {
		super(service);
	}

}

package org.openntf.domino.rest.service;

import java.util.Set;

import org.openntf.domino.rest.resources.AbstractResource;

public interface IResourceProvider {
	public Set<AbstractResource> getSingletons(ODAGraphService service);

	public Set<Class<?>> getClasses();
}

package org.openntf.service;

import java.util.List;

/**
 * Callback interface for the FindService Method
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public interface IServiceLocator {
	public <T> List<T> findApplicationServices(final Class<T> serviceClazz);

}
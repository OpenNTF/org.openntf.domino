/*
 * Javolution - Java(TM) Solution for Real-Time and Embedded Systems
 * Copyright (C) 2012 - Javolution (http://javolution.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package javolution.osgi.internal;

import javolution.xml.internal.stream.XMLOutputFactoryImpl;
import javolution.xml.stream.XMLOutputFactory;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceRegistration;

/**
 * Holds the service factory providing XMLInputFactory instances.
 */
public final class XMLOutputFactoryProvider implements ServiceFactory {

	@Override
	public XMLOutputFactory getService(final Bundle bundle, final ServiceRegistration registration) {
		return new XMLOutputFactoryImpl();
	}

	@Override
	public void ungetService(final Bundle bundle, final ServiceRegistration registration, final Object service) {
	}
}

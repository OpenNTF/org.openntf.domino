package org.openntf.domino.xsp.application;

import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;

import com.ibm.xsp.application.ApplicationEx;
import com.ibm.xsp.application.ApplicationFactoryImpl;

public class ODAApplicationFactory extends ApplicationFactoryImpl {
	private final static boolean _debug = true;
	static {
		if (_debug) {
			System.out.println("DEBUG: " + ODAApplicationFactory.class.getName() + " loaded");
		}
	}

	public ODAApplicationFactory() {
		super();
		if (_debug)
			System.out.println("DEBUG: " + getClass().getName() + " created");
	}

	public ODAApplicationFactory(final ApplicationFactory factory) {
		super(factory);
		if (_debug)
			System.out.println("DEBUG: " + getClass().getName() + " created with delegate");
	}

	@Override
	protected ApplicationEx createApplicationInstance(final Application app) {
		return new ODAApplicationEx(app);
	}

}

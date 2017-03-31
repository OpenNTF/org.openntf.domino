package org.openntf.domino.rest;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.
	 * BundleContext)
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

	public static Map<String, String> getManifestInfo() {
		Map<String, String> result = new LinkedHashMap<String, String>();
		Bundle bundle = getContext().getBundle();
		Dictionary headers = bundle.getHeaders();
		Enumeration enumer = headers.keys();
		while (enumer.hasMoreElements()) {
			Object elem = enumer.nextElement();
			result.put(String.valueOf(elem), String.valueOf(headers.get(elem)));
		}
		return result;
	}

}

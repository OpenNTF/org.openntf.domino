/**
 * 
 */
package org.openntf.domino.xsp.adapter;

import com.ibm.designer.runtime.domino.adapter.HttpService;
import com.ibm.designer.runtime.domino.adapter.IServiceFactory;
import com.ibm.designer.runtime.domino.adapter.LCDEnvironment;

/**
 * This class is specified as com.ibm.xsp.adapter.serviceFactory to trigger a "autostart" on server start.
 * 
 * This triggers the Activator that does the rest of the the startup process
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public class OpenntfServiceFactory implements IServiceFactory {

	public OpenntfServiceFactory() {
		System.out.println("Openntf-Factory loaded");
	}

	@Override
	public HttpService[] getServices(final LCDEnvironment paramLCDEnvironment) {
		// We need this for proper XOTS working. But the service does NOTHING
		HttpService[] ret = new HttpService[1];
		ret[0] = new OpenntfHttpService(paramLCDEnvironment);
		return ret;
	}

}
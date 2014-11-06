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
 * It also creates a HTTP-Service. The HttpService that delegates the call to the other services.
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
		HttpService[] ret = new HttpService[1];
		ret[0] = new OpenntfHttpService(paramLCDEnvironment);
		return ret;
	}

}
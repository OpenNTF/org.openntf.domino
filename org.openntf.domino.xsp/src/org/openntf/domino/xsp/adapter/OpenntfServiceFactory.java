/**
 * 
 */
package org.openntf.domino.xsp.adapter;

import org.openntf.domino.instrument.ProfilerAgent;
import org.openntf.domino.xots.XotsDaemon;

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
	static {
		// attach the agent as soon as possible, you can only instrument classes, that are not loaded at this time
		// unfortunately the whole lotus.domino package is alredy loaded. 
		// (so you will need to use the -javaagent option if you want to instrument these)

		ProfilerAgent.attach();
	}
	private static XotsDaemon DAEMON = XotsDaemon.getInstance();

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
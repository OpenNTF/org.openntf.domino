/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * 
 */
package org.openntf.domino.xsp.adapter;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import org.openntf.domino.xsp.ODAPlatform;

import com.ibm.designer.runtime.domino.adapter.ComponentModule;
import com.ibm.designer.runtime.domino.adapter.HttpService;
import com.ibm.designer.runtime.domino.adapter.LCDEnvironment;
import com.ibm.designer.runtime.domino.bootstrap.adapter.HttpServletRequestAdapter;
import com.ibm.designer.runtime.domino.bootstrap.adapter.HttpServletResponseAdapter;
import com.ibm.designer.runtime.domino.bootstrap.adapter.HttpSessionAdapter;
import com.ibm.domino.xsp.module.nsf.NSFService;

/**
 * This class wraps doService calls and terminates the factory after execution
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
@SuppressWarnings("nls")
public class OpenntfHttpService extends HttpService {
	private static final Logger log_ = Logger.getLogger(OpenntfHttpService.class.getName());
	private static OpenntfHttpService INSTANCE;

	public static OpenntfHttpService getCurrentInstance() {
		return INSTANCE;
	}

	public OpenntfHttpService(final LCDEnvironment lcdEnv) {
		super(lcdEnv);
		
//		 System.out.println("Openntf-Service loaded");
		try {
			//this.services = lcdEnv.getServices();
			if (INSTANCE != null) {
				log_.severe("There is more than one OpenntfHttpService instance active. This may cause problems.");
			}
			INSTANCE = this;
			// here is the right place to initialize things on server start
			ODAPlatform.start();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	@Override
	public void destroyService() {
		// XotsDaemon.getInstance().stop();
		ODAPlatform.stop();
		super.destroyService();
		INSTANCE = null;
	}

	private NSFService nsfservice_;

	/**
	 * Method to find the active NSFService
	 * 
	 * @return the NSFService
	 */
	private NSFService getNsfService() {
		if (nsfservice_ == null) {
			for (HttpService service : getEnvironment().getServices()) {
				if (service instanceof NSFService) {
					nsfservice_ = (NSFService) service;
					break;
				}
			}
		}
		return nsfservice_;
	}

	public static NSFService sGetNsfService() {
		return INSTANCE.getNsfService();
	}

	@Override
	public boolean doService(final String contextPath, final String path, final HttpSessionAdapter httpSession,
			final HttpServletRequestAdapter httpRequest, final HttpServletResponseAdapter httpResponse)
			throws ServletException, IOException {
		// NOP
		return false;
	}

	@Override
	public int getPriority() {
		return 1000; // the higher the later this service will queried 
	}

	@Override
	public void getModules(final List<ComponentModule> paramList) {
		// NOP
	}

}

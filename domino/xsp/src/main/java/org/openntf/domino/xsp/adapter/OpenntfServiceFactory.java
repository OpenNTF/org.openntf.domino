/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
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
		//System.out.println("Openntf-Factory loaded");
	}

	@Override
	public HttpService[] getServices(final LCDEnvironment paramLCDEnvironment) {
		// We need this for proper XOTS working. But the service does NOTHING
		HttpService[] ret = new HttpService[1];
		ret[0] = new OpenntfHttpService(paramLCDEnvironment);
		return ret;
	}

}
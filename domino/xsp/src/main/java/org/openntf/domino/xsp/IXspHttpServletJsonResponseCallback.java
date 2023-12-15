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
package org.openntf.domino.xsp;

/*
 	Copyright 2018 Paul Withers Licensed under the Apache License, Version 2.0
	(the "License"); you may not use this file except in compliance with the
	License. You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
	or agreed to in writing, software distributed under the License is distributed
	on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
	express or implied. See the License for the specific language governing
	permissions and limitations under the License
	
*/

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.xsp.webapp.XspHttpServletResponse;

/**
 * This interface allows the developer to pass custom code to be run to process the HttpServletRequest and populate the
 * XspHttpServletResponse
 * 
 * @author Paul Withers
 * @since ODA 4.3.0
 * 
 */
public interface IXspHttpServletJsonResponseCallback extends IXspHttpServletResponseCallback {

	/**
	 * {@linkplain Utils#initialiseAndProcessResponse(IXspHttpServletJsonResponseCallback)} will extract the HttpServletRequest and
	 * XspHttpServletResponse, call this process() method, then terminate the response.
	 * 
	 * By using the callback approach custom code can be generated without needing to remember to code the boilerplate Java code that should
	 * sit around it.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            XspHttpServletResponse that will be posted back to the browser, from which the getDelegate() method gives access to the
	 *            HttpServletResponse, if needed
	 * @param jsonObj
	 *            to store JSON
	 */
	public void process(HttpServletRequest request, XspHttpServletResponse response, JsonJavaObject jsonObj) throws IOException;

}
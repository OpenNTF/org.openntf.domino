/*
 * © Copyright IBM Corp. 2012-2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package com.ibm.commons.util.io.json;

import com.ibm.commons.util.AbstractException;

/**
 * Json Exception.
 * <p>Exception sent by the Json parsers/generators.</p>
 * @ibm-api
 */
public class JsonException extends AbstractException {

	private static final long serialVersionUID = 1L;

    public JsonException(Throwable e) {
        super(e);
    }
	public JsonException(Throwable e,String msg, Object... params) {
        super(e,msg,params);
    }
}

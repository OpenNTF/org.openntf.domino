/*
 * © Copyright IBM Corp. 2011
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

package com.ibm.domino.das.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.ibm.commons.util.StringUtil;
import com.ibm.domino.services.HttpServiceConstants;

/**
 * This class is mainly for wrapping the http request for dispatching the request with 
 * 	X-HTTP-Method-Override header
 * @author Mao Chuan
 *
 */
public class DasHttpRequestWrapper extends HttpServletRequestWrapper{
	
	public DasHttpRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String getMethod() {				
		String oldMethod = super.getMethod();
		String overrideMethod = this.getHeader(HttpServiceConstants.HEADER_X_HTTP_METHOD_OVERRIDE);
		if(oldMethod.equalsIgnoreCase(HttpServiceConstants.HTTP_POST) && overrideMethod!=null){
			return overrideMethod;
		}
		
		return oldMethod;
	}
	
	public String getRequestURI() {
	    String uri = super.getRequestURI();
	    if ( StringUtil.isNotEmpty(uri) && !uri.startsWith("/") ) {
	        // SPR# WJBJ9MZ9U9: The API iRule sometimes doesn't add the 
	        // leading slash.  Fix that now.
	        uri = "/" + uri;
	    }

	    return uri;
	}
	
    public String getContextPath() {
        String path = super.getContextPath();
        if ( StringUtil.isNotEmpty(path) && !path.startsWith("/") ) {
            // SPR# WJBJ9MZ9U9: The API iRule sometimes doesn't add the 
            // leading slash.  Fix that now.
            path = "/" + path;
        }

        return encodeSpaces(path);
    }
    
    private static String encodeSpaces(String path) {
        
        if ( path == null || path.indexOf(' ') == -1 ) {
            return path;
        }
        
        StringBuffer encoded = new StringBuffer();
        for ( int i = 0; i < path.length(); i++ ) {
            char ch = path.charAt(i);
            if ( ch == ' ' ) {
                encoded.append("%20");
            }
            else {
                encoded.append(ch);
            }
        }
        
        return encoded.toString();
    }
}

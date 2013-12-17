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

package com.ibm.commons.util;

import java.net.URLDecoder;

/**
 * @deprecated
 * @ibm-not-published
 */
public class UrlUtil {

    /**
     * Takes a relative url in the form <code>mypath/mypage.xsp?foo=bar</code> and
     * adds the parameters to the portlet action URL.
     * The submitted URL is returned stripped of any parameters.
     * @param receiver   a receiver that will be filled with parameter values
     * @param url        the URL being encoded
     * @return           the URL minus any parameters
     * @ibm-api 
     */
    public static String addParametersAndReturnUrl(ParameterReceiver receiver, String url) {
        //TODO: need to handle URL encoded characters? (e.g. mypath/myfile.xsp?some%20key=value
        
        int n = url.indexOf('?');
        if (n >= 0) {
            if(n<url.length()) {
                String parameters = url.substring(n + 1);
                url = url.substring(0, n);
                addParameters(receiver, parameters);
            }
        }
        return url;
    }

    private static void addParameters(ParameterReceiver receiver, String params) {
        int last = 0;
        int n;
        while (true) {
            if(last>=params.length()) {
                break;
            }
            n = params.indexOf('&', last);
            if (n < 0) {
                addParameter(receiver, params, last, params.length());
                break;
            }
            addParameter(receiver, params, last, n);
            last = n+1;
        }
    }

    private static void addParameter(ParameterReceiver receiver, String params, int start, int end) {
        int n;
        n = params.indexOf('=', start);
        if (n < 0 || n > end) {
            n = end;
        }
        String key = params.substring(start, n);
        String value;
        if((n+1)>=end) {
            value = ""; //$NON-NLS-1$;
        } else {
            value = params.substring(n + 1, end);
        }
        if(key.length()>0) {
        	//PRID6WWSBT
        	//decode method should match the encode method used by XSPUrl
        	//at time of writing, it uses deprecated UrlEncoder.encode(String)
        	key = URLDecoder.decode(key);
        	value = URLDecoder.decode(value);
            receiver.setParameter(key, value);
        }
    }
    
    /**
     * An interface for receiving key-value pairs.
     */
    public static interface ParameterReceiver {
        public void setParameter(String key, String value);
    }
	
}

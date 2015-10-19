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
package com.ibm.domino.das;


import java.security.AccessController;
import java.security.PrivilegedAction;

/** Resource handler class 
 * @ibm-not-published
 * **/

public class DasResourceHandler {
    private static ExtLibResourceHandlerImpl s_impl;
    private static ExtLibResourceHandlerImpl impl(){
        if (s_impl == null) {
            s_impl = AccessController.doPrivileged(new PrivilegedAction<ExtLibResourceHandlerImpl>() {
                public ExtLibResourceHandlerImpl run() {
                    // privileged code goes here:
                    return new ExtLibResourceHandlerImpl(DasResourceHandler.class, 
                            "messages", //$NON-NLS-1$
                            "das", //$NON-NLS-1$
                            "specialAudience"); //$NON-NLS-1$
                }
            });
        }
        return s_impl;
    }
    public static String getLoggingString(String key) {
        return impl().getLoggingString(key);
    }
    public static String getString(String key) {
        return impl().getString(key);
    }
    public static String getSpecialAudienceString(String key) {
        return impl().getSpecialAudienceString(key);
    }  
}
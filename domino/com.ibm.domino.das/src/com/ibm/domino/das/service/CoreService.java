/*
 * © Copyright IBM Corp. 2013
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

package com.ibm.domino.das.service;

import static com.ibm.domino.das.servlet.DasServlet.DAS_LOGGER;

import java.util.HashSet;
import java.util.Set;

import com.ibm.domino.das.resources.CoreRootResource;
import com.ibm.domino.das.resources.ImaSettingsResource;
import com.ibm.domino.das.resources.NonceResource;
import com.ibm.domino.das.resources.PasswordStatisticsResource;
import com.ibm.domino.das.resources.StatsResource;

/**
 * Core REST service
 */
public class CoreService extends RestService {
    
    public static final String PATH_SEGMENT_CORE = "core"; // $NON-NLS-1$
    public static final String PATH_SEGMENT_PW_STATS = "pwstats"; // $NON-NLS-1$
    public static final String PATH_SEGMENT_IMA_SETTINGS = "imasettings"; // $NON-NLS-1$
    public static final String PATH_SEGMENT_NONCE = "nonce"; // $NON-NLS-1$
    public static final String PATH_SEGMENT_STATS = "stats"; // $NON-NLS-1$

    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        DAS_LOGGER.getLogger().fine("Adding core resources."); // $NON-NLS-1$
        classes.add(CoreRootResource.class);        
        classes.add(PasswordStatisticsResource.class);  
        classes.add(ImaSettingsResource.class);    
        classes.add(NonceResource.class);
        classes.add(StatsResource.class);
        return classes;
    }
    
}
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
import static com.ibm.domino.services.rest.RestParameterConstants.DEFAULT_VIEW_COUNT;
import static com.ibm.domino.services.rest.RestParameterConstants.MAX_VIEW_COUNT;

import java.util.HashSet;
import java.util.Set;

import lotus.domino.NotesException;
import lotus.domino.Session;

import com.ibm.commons.util.StringUtil;
import com.ibm.domino.das.resources.DbCollectionResource;
import com.ibm.domino.das.resources.DocumentCollectionResource;
import com.ibm.domino.das.resources.DocumentResource;
import com.ibm.domino.das.resources.ViewCollectionResource;
import com.ibm.domino.das.resources.ViewDesignResource;
import com.ibm.domino.das.resources.ViewEntryCollectionResource;
import com.ibm.domino.das.resources.ViewEntryResource;
import com.ibm.domino.osgi.core.context.ContextInfo;

/**
 * Data REST service
 */
public class DataService extends RestService {

    public static final String STAT_DB_COLLECTION         = "DbCollection"; // $NON-NLS-1$
    public static final String STAT_DOCUMENT              = "Document"; // $NON-NLS-1$
    public static final String STAT_DOC_COLLECTION        = "DocCollection"; // $NON-NLS-1$
    public static final String STAT_VIEW_COLLECTION       = "ViewCollection"; // $NON-NLS-1$
    public static final String STAT_VIEW_DESIGN           = "ViewDesign"; // $NON-NLS-1$
    public static final String STAT_VIEW_ENTRIES          = "ViewEntryCollection"; // $NON-NLS-1$
    public static final String STAT_VIEW_ENTRY            = "ViewEntry"; // $NON-NLS-1$

    private static Boolean s_useRelativeUrls = null;
    private static int s_maxViewEntries = -1;
    
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        DAS_LOGGER.getLogger().fine("Adding data resources."); // $NON-NLS-1$
        classes.add(DbCollectionResource.class);        
        classes.add(DocumentCollectionResource.class);        
        classes.add(DocumentResource.class); 
        classes.add(ViewCollectionResource.class);        
        classes.add(ViewDesignResource.class);        
        classes.add(ViewEntryCollectionResource.class);      
        classes.add(ViewEntryResource.class);            
        return classes;
    }

    public static boolean isUseRelativeUrls() {        
        boolean useRelativeUrls = true;
        
        try {
            Session session = ContextInfo.getUserSession();
            if ( s_useRelativeUrls == null && session != null ) {
                // One time intialization
                String value = session.getEnvironmentString("DataServiceAbsoluteUrls", true); // $NON-NLS-1$
                if ( "1".equals(value) ) {
                    useRelativeUrls = false;
                }                
                s_useRelativeUrls = new Boolean(useRelativeUrls);
            } 
        }
        catch (NotesException e) {
            // Ignore this
        }
        
        if ( s_useRelativeUrls != null ) {
            useRelativeUrls = s_useRelativeUrls;
        }
        
        return useRelativeUrls;
    }
    
    public static int getMaxViewEntries() {        
        
        if ( s_maxViewEntries == -1 ) {

            // One time intialization from notes.ini
            
            try {
                Session session = ContextInfo.getUserSession();
                if ( session != null ) {
                    String value = session.getEnvironmentString("DataServiceMaxViewEntries", true); // $NON-NLS-1$
                    if ( StringUtil.isNotEmpty(value) ) {
                        int maxCount = Integer.valueOf(value);
                        if ( maxCount > DEFAULT_VIEW_COUNT ) {
                            s_maxViewEntries = maxCount;
                        }
                    }
                } 
            }
            catch (Throwable e) {
                // Ignore all exceptions (including unchecked)
            }
            
            if ( s_maxViewEntries == -1 ) {
                // Static value is still not initialized.  Use the default value.
                s_maxViewEntries = MAX_VIEW_COUNT;
            }
        }
        
        return s_maxViewEntries;
    }
    
}
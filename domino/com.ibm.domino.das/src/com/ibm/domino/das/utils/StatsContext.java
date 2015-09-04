/*
 * © Copyright IBM Corp. 2014
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

package com.ibm.domino.das.utils;

import java.util.Date;

/**
 * Per request context for statistics.
 */
public class StatsContext {

    private static ThreadLocal<StatsContext> t_context = new ThreadLocal<StatsContext>();
    
    private Date _startTime = null;
    private String _serviceName = null;
    private String _requestCategory = null;
    
    private StatsContext() {
    }
    
    public static StatsContext getCurrentInstance() {
        StatsContext ctx = t_context.get();
        
        if ( ctx == null ) {
            ctx = new StatsContext();
            t_context.set(ctx);
        }
        
        return ctx;
    }
    
    public static StatsContext init() {
        StatsContext ctx = getCurrentInstance();
        ctx.setStartTime(new Date());
        ctx.setServiceName(null);
        ctx.setRequestCategory(null);
        
        return ctx;
    }
    
    /**
     * @param requestCategory the requestCategory to set
     */
    public void setRequestCategory(String requestCategory) {
        _requestCategory = requestCategory;
    }

    /**
     * Gets the start time of this request.
     * 
     * @return the startTime
     */
    public Date getStartTime() {
        return _startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(Date startTime) {
        _startTime = startTime;
    }

    /**
     * Gets the service name for this request.
     * 
     * @return the serviceName
     */
    public String getServiceName() {
        return _serviceName;
    }

    /**
     * @param serviceName the serviceName to set
     */
    public void setServiceName(String serviceName) {
        _serviceName = serviceName;
    }

    /**
     * Gets the category of this request.
     * 
     * @return the requestCategory
     */
    public String getRequestCategory() {
        return _requestCategory;
    }

}

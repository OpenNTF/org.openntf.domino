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


/**
 * SCN context.
 */
public class ScnContext {
	
	private static ThreadLocal<ScnContext> t_context = new ThreadLocal<ScnContext>();
	
	private boolean _scn = true;
    private String _ownerId = null;
    private String _customerId = null;
    private String _userId = null;
	
    private ScnContext() {
	}
	
	public static ScnContext getCurrentInstance() {
		ScnContext ctx = t_context.get();
		
		if ( ctx == null ) {
			ctx = new ScnContext();
			t_context.set(ctx);
		}
		
		return ctx;
	}
	
	public static ScnContext init() {
	    ScnContext ctx = getCurrentInstance();
	    ctx.setScn(false);
	    ctx.setOwnerId(null);
	    ctx.setCustomerId(null);
	    ctx.setUserId(null);
	    
	    return ctx;
	}
	
    /**
     * @return the hideDbPath
     */
    public boolean isHideDbPath() {
        return isScn();
    }

    /**
     * Returns true if this request is running in SCN.
     * 
     * @return
     */
    public boolean isScn() {
        return _scn;
    }
    
    /**
     * @param scn the scn to set
     */
    public void setScn(boolean scn) {
        _scn = scn;
    }

    /**
     * Returns the owner ID.
     * 
     * <p>The owner ID corresponds to the owner parameter in
     * the request URL.
     * 
     * @return the ownerId
     */
    public String getOwnerId() {
        return _ownerId;
    }

    /**
     * @param ownerId the ownerId to set
     */
    public void setOwnerId(String ownerId) {
        _ownerId = ownerId;
    }

    /**
     * Returns the customer ID for this request.
     * 
     * <p>The customer ID corresponds to the X-DominoCustomerID
     * header.
     * 
     * @return the customerId
     */
    public String getCustomerId() {
        return _customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(String customerId) {
        _customerId = customerId;
    }

    /**
     * Returns the user ID for this request.
     * 
     * <p>The user ID corresponds to the X-DominoUserID
     * header.
     * 
     * @return the customerId
     */
    public String getUserId() {
        return _userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        _userId = userId;
    }

}

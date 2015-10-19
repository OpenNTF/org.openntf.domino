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

package com.ibm.domino.das.resources;

import static com.ibm.domino.das.service.CoreService.PATH_SEGMENT_CORE;
import static com.ibm.domino.das.service.CoreService.PATH_SEGMENT_NONCE;
import static com.ibm.domino.das.servlet.DasServlet.DAS_LOGGER;

import java.util.UUID;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.ibm.domino.das.service.CoreService;

@Path(PATH_SEGMENT_CORE + "/" + PATH_SEGMENT_NONCE)
public class NonceResource {
    
    @GET
    public Response getNonce(@Context UriInfo uriInfo){
        DAS_LOGGER.traceEntry(this, "getNonce"); // $NON-NLS-1$
        
        CoreService.verifyUserContext();
        
        ResponseBuilder builder = Response.ok();
        
        String uid = UUID.randomUUID().toString();
        NewCookie cookie = new NewCookie("DasToken",uid,"/",null,1,"" ,-1, false); // $NON-NLS-1$
        builder.cookie(cookie);
        
        Response response = builder.build();
        
        DAS_LOGGER.traceExit(this, "getNonce", response); // $NON-NLS-1$
        
        return response;
    }
    
    
}
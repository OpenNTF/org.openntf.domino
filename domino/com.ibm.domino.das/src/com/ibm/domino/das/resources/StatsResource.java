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
import static com.ibm.domino.das.service.CoreService.PATH_SEGMENT_STATS;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonGenerator;
import com.ibm.commons.util.io.json.JsonGenerator.StringBuilderGenerator;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.domino.commons.json.JsonSortedObject;
import com.ibm.domino.das.service.CoreService;
import com.ibm.domino.das.servlet.DasStats;
import com.ibm.domino.das.servlet.DasStats.MutableDouble;
import com.ibm.domino.das.servlet.DasStats.MutableInteger;

@Path(PATH_SEGMENT_CORE + "/" + PATH_SEGMENT_STATS)
public class StatsResource {

    @GET
    public Response getStats() {
        
        String jsonEntity = null;
        
        CoreService.verifyUserContext();
        
        try {
            JsonSortedObject stats = new JsonSortedObject();
            
            Set<Map.Entry<String, Object>> set = DasStats.get().getEntries();
            for ( Map.Entry<String, Object> entry : set ) {
                String name = null;
                StringTokenizer tokenizer = new StringTokenizer(entry.getKey(), ".");
                while ( tokenizer.hasMoreTokens() ) {
                    String segment = tokenizer.nextToken();
                    if ( name == null ) {
                        name = segment.toLowerCase();
                    }
                    else {
                        name += segment;
                    }
                }    
                
                if ( entry.getValue() instanceof MutableInteger ) {
                    int value = ((MutableInteger)entry.getValue()).getValue();
                    stats.putInt(name, value);
                }
                else if ( entry.getValue() instanceof MutableDouble ) {
                    double value = ((MutableDouble)entry.getValue()).getValue();
                    stats.putDouble(name, value);
                }
            }

            // Serialize the JSON
            
            StringBuilder sb = new StringBuilder();
            JsonGenerator.Generator generator = new StringBuilderGenerator(JsonJavaFactory.instanceEx, sb, false);
            generator.toJson(stats);
            jsonEntity = sb.toString();
        } 
        catch (JsonException e) {
            throw new WebApplicationException(CoreService.createErrorResponse(e, Response.Status.INTERNAL_SERVER_ERROR));
        } 
        catch (IOException e) {
            throw new WebApplicationException(CoreService.createErrorResponse(e, Response.Status.INTERNAL_SERVER_ERROR));
        }
        finally {
            
        }

        ResponseBuilder builder = Response.ok();
        if ( jsonEntity != null ) {
            builder.type(MediaType.APPLICATION_JSON_TYPE).entity(jsonEntity);
        }
        
        Response response = builder.build();
        return response;
    }
}

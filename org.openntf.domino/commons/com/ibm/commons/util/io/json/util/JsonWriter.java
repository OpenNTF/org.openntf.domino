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

package com.ibm.commons.util.io.json.util;

import java.io.IOException;
import java.io.Writer;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonFactory;
import com.ibm.commons.util.io.json.JsonGenerator;
import com.ibm.commons.util.io.json.JsonJavaFactory;


/**
 * Specialized JSON writer.
 *
 * @author Philippe Riand
 */
public class JsonWriter extends JsonGenerator.WriterGenerator {

    private int objectLevels = 0;
    private boolean first[] = new boolean[32]; // max 32 for now...

    public JsonWriter(Writer writer, boolean compact) {
        super(JsonJavaFactory.instanceEx,writer,compact);
    }
    public JsonWriter(JsonFactory factory, Writer writer, boolean compact) {
        super(factory,writer,compact);
    }

    public void startObject() throws IOException {
        nl();
        indent();
        out('{');
        if(objectLevels==first.length-1) {
        	boolean[] b = new boolean[first.length*2];
        	System.arraycopy(first, 0, b, 0, first.length);
        	first = b;
        }
        first[++objectLevels]=true;
        incIndent();
    }
    public void endObject() throws IOException {
        nl();
        decIndent();
        indent();
        out('}');
        first[--objectLevels]=false;
    }
    public void startArray() throws IOException {
        nl();
        indent();
        out('[');
        if(objectLevels==first.length-1) {
        	boolean[] b = new boolean[first.length*2];
        	System.arraycopy(first, 0, b, 0, first.length);
        	first = b;
        }
        first[++objectLevels]=true;
        incIndent();
    }
    public void endArray() throws IOException {
        nl();
        decIndent();
        indent();
        out(']');
        first[--objectLevels]=false;
    }
    public void startArrayItem() throws IOException {
        if(!first[objectLevels]) {
            out(',');
        }
        nl();
        indent();
    }
    public void endArrayItem() throws IOException {
        first[objectLevels]=false;
    }
    public void startProperty(String propertyName) throws IOException {
        if(!first[objectLevels]) {
            out(',');
        } else {
            first[objectLevels]=false;
        }
        nl();
        incIndent();
        indent();
        outPropertyName(propertyName);
        out(':');
    }
    public void endProperty() throws IOException {
        decIndent();
    }

    //
    // Shortcuts for emitting properties
    //
    public void outStringProperty(String prop, String value) throws IOException {
    	if(value!=null) {
	    	startProperty(prop);
	    	outStringLiteral(value);
	    	endProperty();
    	}
    }
    public void outBooleanProperty(String prop, boolean value) throws IOException {
    	startProperty(prop);
    	outBooleanLiteral(value);
    	endProperty();
    }
    public void outNumberProperty(String prop, double value) throws IOException {
    	startProperty(prop);
    	outNumberLiteral(value);
    	endProperty();
    }
    public void outIntProperty(String prop, int value) throws IOException {
    	startProperty(prop);
    	outIntLiteral(value);
    	endProperty();
    }
    public void outLongProperty(String prop, long value) throws IOException {
    	startProperty(prop);
    	outLongLiteral(value);
    	endProperty();
    }
    public void outObjectProperty(String prop, Object value) throws IOException, JsonException {
    	if(value!=null) {
	    	startProperty(prop);
	    	outObject(value);
	    	endProperty();
    	}
    }
    public void outArrayProperty(String prop, Object value) throws IOException, JsonException {
    	if(value!=null) {
	    	startProperty(prop);
	    	outArrayLiteral(value);
	    	endProperty();
    	}
    }
    public void outProperty(String prop, Object value) throws IOException, JsonException {
    	startProperty(prop);
    	outLiteral(value);
    	endProperty();
    }
}
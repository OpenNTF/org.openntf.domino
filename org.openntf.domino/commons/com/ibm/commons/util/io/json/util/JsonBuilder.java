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
import java.util.Date;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonFactory;
import com.ibm.commons.util.io.json.JsonGenerator;
import com.ibm.commons.util.io.json.JsonJavaFactory;


/**
 * Specialized JSON writer within a StringBuilder.
 *
 * @author Philippe Riand
 */
public class JsonBuilder extends JsonGenerator.StringBuilderGenerator {

	private int objectLevels = 0;
	private boolean first[] = new boolean[32]; // max 32 for now...

	public JsonBuilder(boolean compact) {
		super(JsonJavaFactory.instanceEx,new StringBuilder(),compact);
	}

	public JsonBuilder(StringBuilder b, boolean compact) {
		super(JsonJavaFactory.instanceEx,b,compact);
	}

	public JsonBuilder(JsonFactory factory, StringBuilder b, boolean compact) {
		super(factory,b,compact);
	}

	public void startObject() throws IOException {
		nl();
        indent();
        out('{');
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

	public void set(String propertyName, Object value) throws IOException, JsonException {
		startProperty(propertyName);
		outLiteral(value);
	}

	public void setBoolean(String propertyName, boolean value) throws IOException, JsonException {
		startProperty(propertyName);
		outBooleanLiteral(value);
	}

	public void setNumber(String propertyName, double value) throws IOException, JsonException {
		startProperty(propertyName);
		outNumberLiteral(value);
	}

	public void setString(String propertyName, String value) throws IOException, JsonException {
		startProperty(propertyName);
		outStringLiteral(value);
	}

	public void setDate(String propertyName, Date value) throws IOException, JsonException {
		startProperty(propertyName);
		outDateLiteral_(value);
	}
}

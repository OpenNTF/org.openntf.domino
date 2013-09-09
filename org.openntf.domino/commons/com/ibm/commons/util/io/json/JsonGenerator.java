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

package com.ibm.commons.util.io.json;

import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import com.ibm.commons.util.StringUtil;



/**
 * JSON text generator.
 * <p>
 * This class provides some methods for generating JSON object representation. It uses
 * a factory to deal with the actual object classes.
 * </p>
 * @ibm-api
 */
public class JsonGenerator {
    
    /**
     * Convert an object hierarchy to a JSON text.
     * The string is written in a compact form (no extra spaces/new lines inserted).
     * @param factory the object factory
     * @param value the convert to serialize
     * @return the resulting JSON string
     * @throws IOException
     * @throws JsonException
     * @ibm-api
     */
    public static String toJson(JsonFactory factory, Object value) throws IOException, JsonException {
        return toJson(factory,value,true);
    }
    
    /**
     * Convert an object hierarchy to a JSON text.
     * @param factory the object factory
     * @param value the convert to serialize
     * @param compact indicates if the string should be in a compact format
     * @return the resulting JSON string
     * @throws IOException
     * @throws JsonException
     * @ibm-api
     */
    public static String toJson(JsonFactory factory, Object value, boolean compact) throws IOException, JsonException {
        StringGenerator gen = new StringGenerator(factory,compact);
        gen.toJson(value);
        return gen.b.toString();
    }
    
    /**
     * Convert an object hierarchy to a JSON text and append it to an existing String builder.
     * @param factory the object factory
     * @param b the StringBuilder to append the text to
     * @param value the convert to serialize
     * @param compact indicates if the string should be in a compact format
     * @return the resulting JSON string
     * @throws IOException
     * @throws JsonException
     * @ibm-api
     */
    public static StringBuilder toJson(JsonFactory factory, StringBuilder b, Object value, boolean compact) throws IOException, JsonException {
        StringBuilderGenerator gen = new StringBuilderGenerator(factory,b,compact);
        gen.toJson(value);
        return b;
    }
    
    
    /**
     * Convert an object hierarchy to a JSON text in a writer.
     * @param factory the object factory
     * @param writer the writer to write to
     * @param value the convert to serialize
     * @param compact indicates if the string should be in a compact format
     * @throws IOException
     * @throws JsonException
     * @ibm-api
     */
    public static void toJson(JsonFactory factory, Writer writer, Object value, boolean compact) throws IOException, JsonException {
        WriterGenerator gen = new WriterGenerator(factory,writer,compact);
        gen.toJson(value);
    }

    /**
     * Generator base class.
     * @ibm-not-published
     */
    public abstract static class Generator {
        
        private JsonFactory factory;
        private boolean compact;
        private int indentLevel;
        
        protected Generator(JsonFactory factory, boolean compact) {
            this.factory = factory;
            this.compact = compact;
        }
        
        public abstract void out(char c) throws IOException;
        public abstract void out(String s) throws IOException;
        public JsonFactory getFactory() {
            return factory;
        }
        public int getIndentLevel() {
            return indentLevel;
        }
        public void setIndentLevel(int indentLevel) {
            this.indentLevel = indentLevel;
        }
        public void incIndent() {
        	indentLevel++;
        }
        public void decIndent() {
        	indentLevel--;
        }
        public boolean isCompact() {
            return compact;
        }
        
        public void toJson(Object value) throws IOException, JsonException {
            outLiteral(value);
        }

        public void outLiteral(Object value) throws IOException, JsonException {
        	outLiteral(value, false);
        }
        
        /**
         * Writes a property value.
         * 
         * @param value The value to write.
         * @param named <code>true</code> if the property is named.
         * @throws IOException
         * @throws JsonException
         */
        protected void outLiteral(Object value, boolean named) throws IOException, JsonException {
            if(factory.isNull(value)) {
                outNull(); // $NON-NLS-1$
            } else if(factory.isString(value)) {
                outStringLiteral(factory.getString(value));
            } else if(factory.isNumber(value)) {
                outNumberLiteral(factory.getNumber(value));
            } else if(factory.isBoolean(value)) {
                outBooleanLiteral(factory.getBoolean(value));
            } else if(factory.isObject(value)) {
                outObject(value, named);
            } else if(factory.isArray(value)) {
                outArrayLiteral(value, named);
            } else if(value instanceof JsonReference) {
                outReference((JsonReference)value);
            } else {
                throw new JsonException(null,"Unknown literal of class {0}", value!=null?value.getClass().getName():"<null>"); // $NLS-JsonGenerator.Unknownliteralofclass0-1$ $NON-NLS-2$
            }
            
        }
        
        public void outNull() throws IOException, JsonException {
            out("null"); // $NON-NLS-1$
        }
        
        public void outObject(Object object) throws IOException, JsonException {
        	outObject(object, false);
        }
        
        /**
         * Writes a JSON object property.
         * 
         * @param object The object to write.
         * @param named <code>true</code> if the object is named.
         * @throws IOException
         * @throws JsonException
         */
        protected void outObject(Object object, boolean named) throws IOException, JsonException {
        	if ( named ) {
        		// Named objects are not indented
        		out(' ');
        	} else {
        		indent();
        	}
        	
            startObject(object);
            
            boolean coma = false;
            for(Iterator<String> it = factory.iterateObjectProperties(object); it.hasNext(); ) {
                String propName = it.next();
                Object propValue = factory.getProperty(object, propName);
                if(coma) {
                    out(',');
                } else {
                    coma = true;
                }
                indentLevel++;
                nl();
                indent();
                outPropertyName(propName);
                out(':');
                outLiteral(propValue, true);
                indentLevel--;
            }

            nl();
            indent();
            endObject(object);
        }
        protected void startObject(Object object) throws IOException, JsonException {
            out('{');
        }
        protected void endObject(Object object) throws IOException, JsonException {
            out('}');
        }

        public void outPropertyName(String s) throws IOException {
        	outStringLiteral(s);
        }
        public void outStringLiteral(String s) throws IOException {
            out('\"');
            int len = s.length();
            for(int i=0; i<len; i++) {
                char c = s.charAt(i);
                switch(c) {
                    case '"': {
                        out("\\\""); //$NON-NLS-1$
                    } break;
                    case '\\': {
                        out("\\\\"); //$NON-NLS-1$
                    } break;
                    case '/': {
                        out("\\/"); //$NON-NLS-1$
                    } break;
                    case '\b': {
                        out("\\b"); //$NON-NLS-1$
                    } break;
                    case '\f': {
                        out("\\f"); //$NON-NLS-1$
                    } break;
                    case '\n': {
                        out("\\n"); //$NON-NLS-1$
                    } break;
                    case '\r': {
                        out("\\r"); //$NON-NLS-1$
                    } break;
                    case '\t': {
                        out("\\t"); //$NON-NLS-1$
                    } break;
                    default: {
                        // Ensure that it will be transmitted correctly...
                        if(c>=32 && c<=128) {
                            out(c);
                        } else {
                            out("\\u"); //$NON-NLS-1$
                            out(StringUtil.toUnsignedHex(c,4));
                        }
                    }
                }
            }
            out('\"');
        }
        public void outCharInString(char c) throws IOException {
            switch(c) {
                case '"': {
                    out("\\\""); //$NON-NLS-1$
                } break;
                case '\'': {
                    out("\\\'"); //$NON-NLS-1$
                } break;
                case '\\': {
                    out("\\\\"); //$NON-NLS-1$
                } break;
                case '/': {
                    out("\\/"); //$NON-NLS-1$
                } break;
                case '\b': {
                    out("\\b"); //$NON-NLS-1$
                } break;
                case '\f': {
                    out("\\f"); //$NON-NLS-1$
                } break;
                case '\n': {
                    out("\\n"); //$NON-NLS-1$
                } break;
                case '\r': {
                    out("\\r"); //$NON-NLS-1$
                } break;
                case '\t': {
                    out("\\t"); //$NON-NLS-1$
                } break;
                default: {
                    // Ensure that it will be transmitted correctly...
                    if(c>=32 && c<=128) {
                        out(c);
                    } else {
                        out("\\u"); //$NON-NLS-1$
                        out(StringUtil.toUnsignedHex(c,4));
                    }
                }
            }
        }

        public void outNumberLiteral(double d) throws IOException {
        	long l = (long)d;
        	if(((double)l)==d) {
        		String s = Long.toString(l);
                out(s);
        	} else {
        		String s = Double.toString(d);
                out(s);
        	}
        }
        
    	public void outDateLiteral_(Date value) throws IOException {
            String s = dateToString(value);
            outStringLiteral(s);
        }

        public void outIntLiteral(int d) throws IOException {
            String s = Integer.toString(d);
            out(s);
        }
        
        public void outLongLiteral(long d) throws IOException {
            String s = Long.toString(d);
            out(s);
        }

        public void outBooleanLiteral(boolean b) throws IOException {
            out(b?"true":"false"); //$NON-NLS-1$ //$NON-NLS-2$
        }

        public void outArrayLiteral(Object array) throws IOException, JsonException {
        	outArrayLiteral(array, false);
        }
        
        /**
         * Writes an array property.
         * 
         * @param array The array to write.
         * @param named <code>true</code> if the property is named.
         * @throws IOException
         * @throws JsonException
         */
        protected  void outArrayLiteral(Object array, boolean named) throws IOException, JsonException {
            
        	if ( named ) {
        		// Named arrays are not indented
        		out(' ');
        	} else {
        		indent();
        	}
        	out('[');
            nl();
            
            boolean coma = false;
            for(Iterator<Object> it = factory.iterateArrayValues(array); it.hasNext(); ) {
                Object propValue = it.next();
                indentLevel++;
                if(coma) {
                    out(',');
                    nl();
                } else {
                    coma = true;
                }
                indent();
                outLiteral(propValue); 
                indentLevel--;
            }
            
            nl();
            indent();
            out(']');
        }
        
        public void outReference(JsonReference ref) throws IOException, JsonException {
            String s = ref.getRef();
            if(StringUtil.isNotEmpty(s)) {
                out(s);
            } else {
                out("null"); // $NON-NLS-1$
            }
        }
        
        public void indent() throws IOException {
            if(!compact && indentLevel>0) {
                for(int i=0; i<indentLevel; i++) {
                    out("  ");
                }
            }
        }
        
        public void nl() throws IOException {
            if(!compact) {
                out('\n');
            }
        }
    }
    
    /**
     * String generator class.
     * @ibm-not-published
     */
    public static class StringGenerator extends Generator { 
        protected StringBuilder b;
        public StringGenerator(JsonFactory factory, boolean compact) {
            super(factory,compact);
            this.b = new StringBuilder();
        }
        public StringBuilder getStringBuilder() {
        	return b;
        }
        public void out(char c) throws IOException {
            b.append(c);
        }
        public void out(String s) throws IOException {
            b.append(s);
        }
    }
    
    /**
     * StringBuilder generator class.
     * @ibm-not-published
     */
    public static class StringBuilderGenerator extends Generator { 
        protected StringBuilder b;
        public StringBuilderGenerator(JsonFactory factory, StringBuilder b, boolean compact) {
            super(factory,compact);
            this.b = b;
        }
        public StringBuilder getStringBuilder() {
        	return b;
        }
        public void out(char c) throws IOException {
            b.append(c);
        }
        public void out(String s) throws IOException {
            b.append(s);
        }
    }
    
    /**
     * Writer generator class.
     * @ibm-not-published
     */
    public static class WriterGenerator extends Generator { 
        private Writer writer;
        public WriterGenerator(JsonFactory factory, Writer writer, boolean compact) {
            super(factory,compact);
            this.writer = writer;
        }
    	public void flush() throws IOException {
    		writer.flush();
    	}
    	public void close() throws IOException {
    		writer.close();
    	}
        public void out(char c) throws IOException {
            writer.write(c);
        }
        public void out(String s) throws IOException {
            writer.write(s);
        }
    }

	public static final String TIME_FORMAT_B = "yyyy-MM-dd'T'HH:mm:ss"; //$NON-NLS-1$

	private static SimpleDateFormat ISO8601 = new SimpleDateFormat(TIME_FORMAT_B);

	public static String dateToString(Date value) throws IOException {
		return ISO8601.format(value);
	}
	public static Date stringToDate(String value) throws IOException, ParseException {
		return ISO8601.parse(value);
	}
}
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ibm.commons.util.ArrayIterator;
import com.ibm.commons.util.StringUtil;



/**
 * This factory is used manipulate Java object.
 * <p>
 * The values are mapped by Java objects (String, Number, List<?>, Map<?,?>...). Note that
 * the collections can either be Map or JsonJavaObject.
 * </p>
 * 
 * @ibm-api
 */
public class JsonJavaFactory implements JsonFactory {

	/**
	 * Singleton instance that uses java.util.Map for collection.
	 * @ibm-api
	 */
    public static final JsonJavaFactory instance = new JsonJavaFactory();

	/**
	 * Singleton instance that uses JsonJavaObject for collection.
	 * @ibm-api
	 */
    public static final JsonJavaFactory instanceEx = new JsonJavaFactory() {
        public Object createObject(Object parent, String propertyName) {
            return new JsonJavaObject();
        }
    };
    public static final JsonJavaFactory instanceEx2 = new JsonJavaFactory() {
        public Object createObject(Object parent, String propertyName) {
            return new JsonJavaObject();
        }
        public List<Object> createTemporaryArray(Object parent) throws JsonException {
            return new JsonJavaArray();
        }
		public Object createArray(Object parent, String propertyName, List<Object> values) {
			if(values instanceof JsonJavaArray) {
				return values;
			}
            return new JsonJavaArray(values);
        }
    };

    public boolean supportFeature(int feature) {
    	if(feature==FEATURE_INLINEJAVASCRIPT) {
    		return true;
    	}
    	return false;
	}

	public boolean isValidValue(Object value) throws JsonException {
		return isNull(value) || isUndefined(value) || isBoolean(value) || isNumber(value) || isString(value) || isObject(value) || isArray(value) || isJavaScriptCode(value);
	}

    public Object createNull() throws JsonException {
        return null;
    }
    	
    public Object createUndefined() throws JsonException {
        throw new JsonException(null,"Undefined does not exist in Java");
    }

    public Object createString(String value) throws JsonException {
        return value;
    }
    
    public Object createNumber(double value) throws JsonException {
        return Double.valueOf(value);
    }
    
    public Object createBoolean(boolean value) throws JsonException {
        return Boolean.valueOf(value);
    }

    public Object createObject(Object parent, String propertyName) throws JsonException {
        return new HashMap<String,Object>();
    }
    
    public Object createArray(Object parent, String propertyName, List<Object> values) throws JsonException {
        return values;
    }
    
    public Object createJavaScriptCode(String code) throws JsonException {
    	return new JsonReference(code);
    }

    @SuppressWarnings("unchecked") //$NON-NLS-1$
    public void setProperty(Object parent, String propertyName, Object value) throws JsonException {
        if(parent instanceof Map) {
            ((Map<String, Object>)parent).put(propertyName, value);
        } else if(parent instanceof JsonObject) {
            ((JsonObject)parent).putJsonProperty(propertyName, value);
        } else { 
            throw new IllegalArgumentException(StringUtil.format("Invalid Json object, class: {0}",parent!=null ? parent.getClass().toString() : "null")); // $NLS-JsonJavaFactory.InvalidJsonobjectclass0-1$ $NON-NLS-2$
        }
    }
    
    @SuppressWarnings("unchecked") //$NON-NLS-1$
    public Object getProperty(Object parent, String propertyName) throws JsonException {
        if(parent instanceof Map) {
            return ((Map<String, Object>)parent).get(propertyName);
        } else if(parent instanceof JsonObject) {
            return ((JsonObject)parent).getJsonProperty(propertyName);
        } else {
            throw new IllegalArgumentException(StringUtil.format("Invalid Json object, class: {0}",parent!=null ? parent.getClass().toString() : "null")); // $NLS-JsonJavaFactory.InvalidJsonobjectclass0.1-1$ $NON-NLS-2$
        }
    }
    
    public boolean isNull(Object value) throws JsonException {
        return value==null;
    }
    	
    public boolean isUndefined(Object value) {
        return false; // Doesn't exist in Java
    }

    public boolean isString(Object value) throws JsonException {
        return value instanceof String;
    }

    public String getString(Object value) throws JsonException {
        return (String)value;
    }
    
    public boolean isNumber(Object value) throws JsonException {
        return value instanceof Number;
    }

    public double getNumber(Object value) throws JsonException {
        return ((Number)value).doubleValue();
    }
    
    public boolean isBoolean(Object value) throws JsonException {
        return value instanceof Boolean;
    }

    public boolean getBoolean(Object value) throws JsonException {
        return ((Boolean)value).booleanValue();
    }
    
    public boolean isObject(Object value) throws JsonException {
        return (value instanceof Map) || (value instanceof JsonObject);
    }
	
    public boolean isJavaScriptCode(Object value) throws JsonException {
    	if(supportFeature(FEATURE_INLINEJAVASCRIPT)) {
    		return value instanceof JsonReference;
    	}
    	return false;
    }

    public String getJavaScriptCode(Object value) throws JsonException {
    	if(supportFeature(FEATURE_INLINEJAVASCRIPT)) {
    		return ((JsonReference)value).getRef();
    	}
    	return null;
    }

    @SuppressWarnings("unchecked") //$NON-NLS-1$
    public Iterator<String> iterateObjectProperties(Object object) throws JsonException {
        if(object instanceof Map) {
            Map<String, Object> map = (Map<String, Object>)object;
            return map.keySet().iterator();
        } else if(object instanceof JsonObject) {
            return ((JsonObject)object).getJsonProperties();
        } else { 
            throw new IllegalArgumentException(StringUtil.format("Invalid Json object, class: {0}",object!=null ? object.getClass().toString() : "null")); // $NLS-JsonJavaFactory.InvalidJsonobjectclass0.2-1$ $NON-NLS-2$
        }
    }
    
    public boolean isArray(Object value) throws JsonException {
		if (value == null) {
			return false;
		}
    	if(value instanceof JsonArray) {
    		return true;
    	}
        if(value instanceof List) {
            return true;
        }
        if (value.getClass().isArray()) {   
			return true;
		}
        return false;
    }

    public int getArrayCount(Object value) throws JsonException {
    	if(value instanceof JsonArray) {
    		return ((JsonArray)value).length();
    	}
        if(value instanceof List) {
    		return ((List<?>)value).size();
        }
        return ((Object[])value).length;
    }

    public Object getArrayItem(Object value, int index) throws JsonException {
    	if(value instanceof JsonArray) {
    		return ((JsonArray)value).get(index);
    	}
        if(value instanceof List) {
    		return ((List<?>)value).get(index);
        }
        return ((Object[])value)[index];
    }

    @SuppressWarnings("unchecked") //$NON-NLS-1$
    public Iterator<Object> iterateArrayValues(Object array) throws JsonException {
    	if(array instanceof JsonArray) {
    		return ((JsonArray)array).iterator();
    	}
        if(array instanceof List) {
            List<Object> list = (List<Object>)array;
            return list.iterator();
        }
        return new ArrayIterator<Object>(array);
    }   

    public List<Object> createTemporaryArray(Object parent) throws JsonException {
        return new ArrayList<Object>();
    }
 }
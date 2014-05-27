/*
 * © Copyright IBM Corp. 2012
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

/*
 * Created on May 29, 2005
 */
package com.ibm.commons.xml;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ibm.commons.util.FilteredIterator;
import com.ibm.commons.util.IteratorWrapper;
import com.ibm.commons.util.StringUtil;

/**
 * @author Philippe Riand
 * @fbscript
 */
public class NamespaceContextImpl implements NamespaceContext, Serializable {
	private static final long serialVersionUID = 1L;

	private static class Definition implements Serializable {
		private static final long serialVersionUID = 1L;
		String	prefix;
		String	uri;
		Definition(String prefix, String uri) {
			this.prefix = prefix;
			this.uri = uri;
		}
	}
	
	private Map<String,Definition> definitions;
	
	public NamespaceContextImpl() {
		this.definitions = new HashMap<String,Definition>();
	}
	
	
	/**
	 * Add a new namespace definition to the context.
	 * @param prefix the prefix 
	 * @param uri the URI
     * @fbscript
	 */
	public void addNamespace(String prefix, String uri) {
		definitions.put(prefix, new Definition(prefix,uri));
	}

	/**
	 * Remove a namespace definition from the context.
	 * @param prefix the prefix 
     * @fbscript
	 */
	public void remove(String prefix) {
		definitions.remove(prefix);
	}

	/**
	 * Clear the namespace context. 
     * @fbscript
	 */
	public void clear() {
		definitions.clear();
	}

	/**
	 * Returns the number of entried in the namespace context.
     * @fbscript
	 */
	public void size() {
		definitions.size();
	}
	
    /**
     * Get the Namespace URI for the specified prefix in this namespace context.
     * @fbscript
     */
    public String getNamespaceURI(String prefix) {
    	Definition def = (Definition)definitions.get(prefix);
    	return def!=null ? def.uri : null;
    }
    
    /**
     * Get the prefix for the specified Namespace URI in this namespace context.
     * @fbscript
     */
    public String getPrefix(String namespaceURI) {
    	for( Iterator it=definitions.values().iterator(); it.hasNext(); ) {
    		Definition d = (Definition)it.next();
    		if( StringUtil.equals(d.uri, namespaceURI) ) {
    			return d.prefix;
    		}
    	}
    	return null;
    }

    /**
     * Get all prefixes for the specified Namespace URI in this namespace context.
     * @fbscript
     */
    public Iterator getPrefixes(final String namespaceURI) {
    	return new IteratorWrapper(
    			new FilteredIterator(definitions.values().iterator()) {
    				protected boolean accept( Object object ) {
    					Definition d = (Definition)object;
    					return StringUtil.equals(d.uri, namespaceURI); 
    				}
    			}) {
    	    		protected Object wrap( Object o ) {
    	    			Definition d = (Definition)o;
    	    			return d.prefix;
    	    		}
    	};
    }
    
    /**
     * Get all prefixes in this namespace context.
     * @fbscript
     */
    public Iterator getPrefixes() {
    	return definitions.keySet().iterator();
    }
    
}

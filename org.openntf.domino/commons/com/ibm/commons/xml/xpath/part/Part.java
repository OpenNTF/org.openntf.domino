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
 * 
 */
package com.ibm.commons.xml.xpath.part;

/**
 * @author Philippe Riand
 * @author Mark Wallace
 */
abstract public class Part {

    protected String _name;
    
    /**
     * Construct a Part with the specified name
     * @param name
     */
    public Part(String name) {
        _name = name;
    }
    
    /**
     * Return the name of this Part
     * 
     * @return the name of this Part
     */
    public String getName() {
        return _name;
    }
    
    /**
     * Return the namespace prefix of this Part
     * 
     * @return the namespace prefix of this Part
     */
    public String getPrefix() {
        return null;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    abstract public String toString();
    
}

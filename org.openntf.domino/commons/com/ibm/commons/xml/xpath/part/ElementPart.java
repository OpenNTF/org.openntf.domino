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

import com.ibm.commons.util.StringUtil;

/**
 * @author Philippe Riand
 * @author Mark Wallace
 */
public class ElementPart extends Part {

    protected String _prefix;
    
    /**
     * @param name
     */
    public ElementPart(String prefix, String name) {
        super(name);
        
        _prefix = prefix;
    }
    
    /* (non-Javadoc)
     * @see com.ibm.xfaces.xpath.simple.Part#getPrefix()
     */
    public String getPrefix() {
        return _prefix;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        if (StringUtil.isEmpty(_prefix)) {
            return _name;
        } 
        else {
            StringBuffer sb = new StringBuffer();
            sb.append(_prefix).append(":").append(_name);  //$NON-NLS-1$
            return sb.toString();
        }
    }

}

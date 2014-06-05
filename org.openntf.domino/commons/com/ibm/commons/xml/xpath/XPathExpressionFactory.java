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
package com.ibm.commons.xml.xpath;



/**
 * @author Philippe Riand
 * @author Mark Wallace
 */
public interface XPathExpressionFactory {
    
    /**
     * Create an XPathExpression object for the specified expression string
     * 
     * @param data the data context the expression is being created for
     * @param expression XPath expression string
     * @param useCache indicate if it uses a cache for the xpaths
     * @return an XPathExpression object for the specified expression string
     * @throws XPathException
     */
    public XPathExpression createExpression(Object data, String expression, boolean useCache) throws XPathException;

    /**
     * Return true if the specified data context is valid for this factory.
     * 
     * @param data the data context the expression is being created for
     * @return true if the specified data context is valid
     */
    public boolean isValidDataContext(Object data);
    
}

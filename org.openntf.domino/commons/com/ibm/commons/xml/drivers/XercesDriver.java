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

package com.ibm.commons.xml.drivers;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.ibm.commons.util.io.NullInputStream;
import com.ibm.commons.xml.XMLException;

/**
 * W3C DOM Utilities.
 */
public class XercesDriver extends AbstractXercesDriver {
    
    private final static EntityResolver NULL_ENTITY_RESOLVER = new EntityResolver() {
        public InputSource resolveEntity(String arg0, String arg1) throws SAXException, IOException {
            return new InputSource(new NullInputStream());
        }
        
    };

    public XercesDriver() {
    }

    public Document parse(InputStream is, boolean ignoreBlanks, boolean resolveEntities, boolean validate) throws XMLException {
        try {
            DocumentBuilderFactory dbFactory = createDocumentBuilderFactory(resolveEntities,validate);
            DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();

            if (!resolveEntities) {
                docBuilder.setEntityResolver(NULL_ENTITY_RESOLVER);
            }
            
            Document doc = docBuilder.parse(is);

            if (ignoreBlanks) {
                removeEmtyTextNodes(doc);
            }

            return doc;
        }
        catch (Exception e) {
            throw new XMLException(e, "Error parsing XML stream"); // $NLS-XercesDriver.ErrorwhileparsingXMLstream-1$
        }
    }
    
    public Document parse(Reader reader, boolean ignoreBlanks, boolean resolveEntities, boolean validate) throws XMLException {
        try {
            DocumentBuilderFactory dbFactory = createDocumentBuilderFactory(resolveEntities,validate);
            DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
            
            if (!resolveEntities) {
                docBuilder.setEntityResolver(NULL_ENTITY_RESOLVER);
            }
            
            Document doc = docBuilder.parse(new InputSource(reader));

            if (ignoreBlanks) {
                removeEmtyTextNodes(doc);
            }

            return doc;
        }
        catch (Exception e) {
            throw new XMLException(e, "Error parsing XML stream"); // $NLS-XercesDriver.ErrorwhileparsingXMLstream.1-1$
        }
    }

    
    // public Document parse(Reader reader, boolean ignoreBlanks) throws
    // XMLException {
    // try {
    // XNI
    // DOMParser parser = (DOMParser)parserPool.get();
    // try {
    // parser.parse(new InputSource(reader));
    // return parser.getDocument();
    // } finally {
    // parserPool.recycle(parser);
    // }
    // JAXP
    // DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    // DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
    // Document doc = docBuilder.parse(new InputSource(reader));
    // return doc;
    // TrAX
    // TransformerFactory tf = TransformerFactory.newInstance();
    // Transformer transformer = tf.newTransformer();
    // DOMResult result = new DOMResult();
    // SAXSource saxSource = new SAXSource(new InputSource(reader));
    // transformer.transform(saxSource, result);
    // Document doc = (Document)result.getNode();
    // return doc;
    // } catch(Exception e) {
    // throw new XMLException(e,"Error while parsing XML stream"); $NLS-XercesDriver.ErrorwhileparsingXMLstream.2-1$
    // }
    // }
}

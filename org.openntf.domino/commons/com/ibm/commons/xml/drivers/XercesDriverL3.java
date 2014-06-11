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

import java.io.InputStream;
import java.io.Reader;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSParser;
import org.w3c.dom.ls.LSParserFilter;
import org.w3c.dom.traversal.NodeFilter;

import com.ibm.commons.util.ObjectPool;
import com.ibm.commons.xml.XMLException;



/**
 * XERCES driver feturing DOM level 3.
 */
public class XercesDriverL3 extends AbstractXercesDriver {

    private DOMImplementationLS domImplementationLS;
    private ParserPool parserPool;

    private static class ParserPool extends ObjectPool {
        private DOMImplementationLS domImplementationLS;
        ParserPool(DOMImplementationLS domImplementationLS) {
            super(8); // Use a parameter here?
            this.domImplementationLS = domImplementationLS;
        }
        protected Object createObject() throws Exception {
            LSParser parser = domImplementationLS.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS, null);
            return parser;
        }
    }
    
    // Filter to ignore blanks
    private static class BlankFilter implements LSParserFilter {
        public short acceptNode(Node node) {
            if( node instanceof Text ) {
                Text t = (Text)node;
                String data = t.getData();
                int length = data.length();
                int first=0;
                for( ; first<length; first++ ) {
                    char c = data.charAt(first);
                    if( c!=' ' && c!='\t' && c!='\n' && c!='\r' ) {
                        return FILTER_ACCEPT;
                    }
                }
                return FILTER_SKIP;
            }
            return FILTER_ACCEPT;
        }
        public int getWhatToShow() {
            return NodeFilter.SHOW_TEXT;
        }
        public short startElement(Element elt) {
            return FILTER_ACCEPT;
        }
    };
    private static BlankFilter BLANK_FILTER = new BlankFilter();
    
    
    public XercesDriverL3() {
        this.domImplementationLS = (DOMImplementationLS)getDOMImplementation();
        this.parserPool = new ParserPool(domImplementationLS);
    }

    private LSParser getParser() throws Exception {
        //TODO: Use a cache for the parsers?
        LSParser parser = (LSParser)parserPool.get();
        return parser;
    }
    
    private void recycleParser(LSParser parser) {
        parser.setFilter(null);
        parserPool.recycle(parser);
    }
    
    public Document parse(InputStream is, boolean ignoreBlanks) throws XMLException {
        try {
            LSParser parser = getParser();
            try {
                if(ignoreBlanks) {
                    parser.setFilter(BLANK_FILTER);
                }
                LSInput ip = domImplementationLS.createLSInput();
                ip.setByteStream(is);
                Document doc = parser.parse(ip);
                return doc;
            } finally {
                recycleParser(parser);
            }
        } catch(Exception e) {
            throw new XMLException(e,"Error parsing XML input stream"); // $NLS-XercesDriverL3.ErrorwhileparsingXMLinputstream-1$
        }
    }

    public Document parse(Reader reader, boolean ignoreBlanks) throws XMLException {
        try {
            LSParser parser = getParser();
            try {
                if(ignoreBlanks) {
                    parser.setFilter(BLANK_FILTER);
                }
                LSInput ip = domImplementationLS.createLSInput();
                ip.setCharacterStream(reader);
                Document doc = parser.parse(ip);
                return doc;
            } finally {
                recycleParser(parser);
            }
        } catch(Exception e) {
            throw new XMLException(e,"Error parsing XML character stream"); // $NLS-XercesDriverL3.ErrorwhileparsingXMLchracterstrea-1$
        }
    }

    /* (non-Javadoc)
     * @see com.ibm.commons.xml.drivers.XMLParserDriver#parse(java.io.InputStream, boolean, boolean)
     */
    public Document parse(InputStream is, boolean ignoreBlanks, boolean validate) throws XMLException {
        // TODO set validate
        return parse(is, ignoreBlanks);
    }

    /* (non-Javadoc)
     * @see com.ibm.commons.xml.drivers.XMLParserDriver#parse(java.io.Reader, boolean, boolean)
     */
    public Document parse(Reader reader, boolean ignoreBlanks, boolean validate) throws XMLException {
        // TODO set validate
        return parse(reader, ignoreBlanks);
    }
}

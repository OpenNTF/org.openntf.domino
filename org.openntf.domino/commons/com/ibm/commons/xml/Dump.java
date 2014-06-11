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

package com.ibm.commons.xml;

import java.io.PrintStream;
import java.util.ArrayList;

import org.w3c.dom.Attr;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Entity;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Notation;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;



/**
 * W3C DOM Dump.
 */
public class Dump {

    public static void dump( PrintStream ps, Node node ) {
        int depth = 0;
        dumpNodes(ps,node,new ArrayList());
    }

    private static void dumpNodes(PrintStream ps, Node node, ArrayList nodes){
        // Print the indentation
        for( int lv=1; lv<nodes.size(); lv++ ) {
            Node parent = (Node)nodes.get(lv-1);
            NodeList children = parent.getChildNodes();
            if( children.item(children.getLength()-1)==nodes.get(lv) ) {
                ps.print( "    " ); //$NON-NLS-1$
            } else {
                ps.print( "|   " ); //$NON-NLS-1$
            }
        }

        nodes.add(node);
        try {

            if(nodes.size()>1 ) {
                ps.print("+-"); //$NON-NLS-1$
            }
            switch(node.getNodeType()) {
                case Node.DOCUMENT_NODE: {
                    Document v = (Document)node;
                    ps.print("DOCUMENT:"); // $NON-NLS-1$
                } break;
                case Node.DOCUMENT_FRAGMENT_NODE: {
                    DocumentFragment v = (DocumentFragment)node;
                    ps.print("DOCUMENT FRAGMENT:"); // $NON-NLS-1$
                } break;
                case Node.DOCUMENT_TYPE_NODE: {
                    DocumentType v = (DocumentType)node;
                    ps.print("DOCUMENT TYPE:"); // $NON-NLS-1$
                } break;
                case Node.ELEMENT_NODE: {
                    Element v = (Element)node;
                    ps.print("ELEMENT:"); // $NON-NLS-1$
                    ps.print(" name='"+v.getTagName()+"'"); // $NON-NLS-1$
                } break;
                case Node.ATTRIBUTE_NODE: {
                    Attr v = (Attr)node;
                    ps.print("ATTRIBUTE:"); // $NON-NLS-1$
                    ps.print(" name='"+v.getName()+"'"); // $NON-NLS-1$
                    ps.print(" value='"+v.getValue()+"'"); // $NON-NLS-1$
                } break;
                case Node.CDATA_SECTION_NODE: {
                    CharacterData v = (CharacterData)node;
                    ps.print("CDATA:"); // $NON-NLS-1$
                    ps.print(" text='"+v.getData()+"'"); // $NON-NLS-1$
                } break;
                case Node.TEXT_NODE: {
                    Text v = (Text)node;
                    ps.print("TEXT:"); // $NON-NLS-1$
                    ps.print(" text='"+v.getData()+"'"); // $NON-NLS-1$
                } break;
                case Node.COMMENT_NODE: {
                    Comment v = (Comment)node;
                    ps.print("COMMENT:"); // $NON-NLS-1$
                    ps.print(" text='"+v.getData()+"'"); // $NON-NLS-1$
                } break;
                case Node.ENTITY_NODE: {
                    Entity v = (Entity)node;
                    ps.print("ENTITY:"); // $NON-NLS-1$
                } break;
                case Node.ENTITY_REFERENCE_NODE: {
                    EntityReference v = (EntityReference)node;
                    ps.print("ENTITY REFERENCE:"); // $NON-NLS-1$
                } break;
                case Node.NOTATION_NODE: {
                    Notation v = (Notation)node;
                    ps.print("NOTATION:"); // $NON-NLS-1$
                } break;
                case Node.PROCESSING_INSTRUCTION_NODE: {
                    ProcessingInstruction v = (ProcessingInstruction)node;
                    ps.print("PROCESSING INSTRUCTION:"); // $NON-NLS-1$
                } break;
            }

            NodeList children = node.getChildNodes();
            
            ps.print("\n"); //$NON-NLS-1$
            for(int i=0;i<children.getLength();i++){
                Node child=children.item(i);
                if(child!=null){
                    dumpNodes(ps,child,nodes);
                }else{
                    ps.print("<null node>\r\n"); //$NON-NLS-1$
                }
            }
        } finally {
            nodes.remove( nodes.size()-1 );
        }
    }   
}

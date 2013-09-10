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

package com.ibm.commons.util;



/**
 * HTML Common Text utilities.
 * 
 * <p>
 * This class is used to convert Strings to HTML by 
 * evaluating/generating entities for the special characters.
 * </p>
 * 
 * @ibm-not-published Presence of this functionality inside com.ibm.commons plugin
 *                    is required by currently 3 different instances of XSPErrorPage class:
 *                    com.ibm.designer.runtime.domino.adapter.util.XSPErrorPage
 *                    com.ibm.domino.xsp.bridge.websrv.util.XSPErrorPage
 *                    com.ibm.xsp.webapp.XSPErrorPage
 *                    
 *                    While HtmlTextUtil class in this plugin recommends in general use of com.ibm.xsp.util.HtmlUtil -
 *                    usage of toHTMLContentString() method from it is not possible for first 2 XSPErrorPage instances 
 *                    listed above.
 * 
 * @author Andrejus Chaliapinas andrecha@ie.ibm.com
 *
 */
public class HtmlCommonsUtil {

    private static String[] htmlEntities = null;
    
    private static void buildEntitiesList() {
        if (htmlEntities==null) {
            String[] entities=new String[256];
            entities[32] ="nbsp";           //$NON-NLS-1$
            entities[34] ="quot";           //$NON-NLS-1$
            entities[38] ="amp";            //$NON-NLS-1$
            entities[60] ="lt";             //$NON-NLS-1$
            entities[62] ="gt";             //$NON-NLS-1$
            entities[160]="nbsp";           //$NON-NLS-1$
            entities[161]="iexcl";          //$NON-NLS-1$
            entities[162]="cent";           //$NON-NLS-1$
            entities[163]="pound";          //$NON-NLS-1$
            entities[164]="curren";         //$NON-NLS-1$
            entities[165]="yen";            //$NON-NLS-1$
            entities[166]="brvbar";         //$NON-NLS-1$
            entities[167]="sect";           //$NON-NLS-1$
            entities[168]="uml";            //$NON-NLS-1$
            entities[169]="copy";           //$NON-NLS-1$
            entities[170]="ordf";           //$NON-NLS-1$
            entities[171]="laquo";          //$NON-NLS-1$
            entities[172]="not";            //$NON-NLS-1$
            entities[173]="shy";            //$NON-NLS-1$
            entities[174]="reg";            //$NON-NLS-1$
            entities[175]="macr";           //$NON-NLS-1$
            entities[176]="deg";            //$NON-NLS-1$
            entities[177]="plusmn";         //$NON-NLS-1$
            entities[178]="sup2";           //$NON-NLS-1$
            entities[179]="sup3";           //$NON-NLS-1$
            entities[180]="acute";          //$NON-NLS-1$
            entities[181]="micro";          //$NON-NLS-1$
            entities[182]="para";           //$NON-NLS-1$
            entities[183]="middot";         //$NON-NLS-1$
            entities[184]="cedil";          //$NON-NLS-1$
            entities[185]="sup1";           //$NON-NLS-1$
            entities[186]="ordm";           //$NON-NLS-1$
            entities[187]="raquo";          //$NON-NLS-1$
            entities[188]="frac14";         //$NON-NLS-1$
            entities[189]="frac12";         //$NON-NLS-1$
            entities[190]="frac34";         //$NON-NLS-1$
            entities[191]="iquest";         //$NON-NLS-1$
            entities[192]="Agrave";         //$NON-NLS-1$
            entities[193]="Aacute";         //$NON-NLS-1$
            entities[194]="Acirc";          //$NON-NLS-1$
            entities[195]="Atilde";         //$NON-NLS-1$
            entities[196]="Auml";           //$NON-NLS-1$
            entities[197]="Aring";          //$NON-NLS-1$
            entities[198]="AElig";          //$NON-NLS-1$
            entities[199]="Ccedil";         //$NON-NLS-1$
            entities[200]="Egrave";         //$NON-NLS-1$
            entities[201]="Eacute";         //$NON-NLS-1$
            entities[202]="Ecirc";          //$NON-NLS-1$
            entities[203]="Euml";           //$NON-NLS-1$
            entities[204]="Igrave";         //$NON-NLS-1$
            entities[205]="Iacute";         //$NON-NLS-1$
            entities[206]="Icirc";          //$NON-NLS-1$
            entities[207]="Iuml";           //$NON-NLS-1$
            entities[208]="ETH";            //$NON-NLS-1$
            entities[209]="Ntilde";         //$NON-NLS-1$
            entities[210]="Ograve";         //$NON-NLS-1$
            entities[211]="Oacute";         //$NON-NLS-1$
            entities[212]="Ocirc";          //$NON-NLS-1$
            entities[213]="Otilde";         //$NON-NLS-1$
            entities[214]="Ouml";           //$NON-NLS-1$
            entities[215]="times";          //$NON-NLS-1$
            entities[216]="Oslash";         //$NON-NLS-1$
            entities[217]="Ugrave";         //$NON-NLS-1$
            entities[218]="Uacute";         //$NON-NLS-1$
            entities[219]="Ucirc";          //$NON-NLS-1$
            entities[220]="Uuml";           //$NON-NLS-1$
            entities[221]="Yacute";         //$NON-NLS-1$
            entities[222]="THORN";          //$NON-NLS-1$
            entities[223]="szlig";          //$NON-NLS-1$
            entities[224]="agrave";         //$NON-NLS-1$
            entities[225]="aacute";         //$NON-NLS-1$
            entities[226]="acirc";          //$NON-NLS-1$
            entities[227]="atilde";         //$NON-NLS-1$
            entities[228]="auml";           //$NON-NLS-1$
            entities[229]="aring";          //$NON-NLS-1$
            entities[230]="aelig";          //$NON-NLS-1$
            entities[231]="ccedil";         //$NON-NLS-1$
            entities[232]="egrave";         //$NON-NLS-1$
            entities[233]="eacute";         //$NON-NLS-1$
            entities[234]="ecirc";          //$NON-NLS-1$
            entities[235]="euml";           //$NON-NLS-1$
            entities[236]="igrave";         //$NON-NLS-1$
            entities[237]="iacute";         //$NON-NLS-1$
            entities[238]="icirc";          //$NON-NLS-1$
            entities[239]="iuml";           //$NON-NLS-1$
            entities[240]="eth";            //$NON-NLS-1$
            entities[241]="ntilde";         //$NON-NLS-1$
            entities[242]="ograve";         //$NON-NLS-1$
            entities[243]="oacute";         //$NON-NLS-1$
            entities[244]="ocirc";          //$NON-NLS-1$
            entities[245]="otilde";         //$NON-NLS-1$
            entities[246]="ouml";           //$NON-NLS-1$
            entities[247]="divide";         //$NON-NLS-1$
            entities[248]="oslash";         //$NON-NLS-1$
            entities[249]="ugrave";         //$NON-NLS-1$
            entities[250]="uacute";         //$NON-NLS-1$
            entities[251]="ucirc";          //$NON-NLS-1$
            entities[252]="uuml";           //$NON-NLS-1$
            entities[253]="yacute";         //$NON-NLS-1$
            entities[254]="thorn";          //$NON-NLS-1$
            entities[255]="yuml";           //$NON-NLS-1$
            htmlEntities=entities;
        }
    }

    public static String toXhtml(String msg){
        String msgAsXhtml = toHTMLContentString(msg, false/*replaceSpaces*/, true);
        return msgAsXhtml;
    }
    
    public static String toHTMLContentString(String s, boolean replaceSpaces, boolean isXhtml){
        return toHTMLString(s, replaceSpaces, isXhtml);
    }

    private static boolean isXhtmlEntity(char ch){
        return '<' == ch || '>' == ch || '&' == ch || '"' == ch || '\''== ch;
    }

    private static String toHTMLString(String s, boolean replaceSpaces, boolean isXhtml) {
        buildEntitiesList();
        StringBuilder b = null;
        char[] chars = s.toCharArray();
        int length = chars.length;
        for( int i=0; i<length; i++ ) {
            char c = chars[i];

            // Is it a specific entity ?
            String replaceLabel=null;
            String replaceNumber=null;
            if (c==' ') {
                if (replaceSpaces && (i>0 && chars[i-1]==' ') ) {
                    if( isXhtml ){
                        // use #160 (non-breaking space) instead of #32
                        replaceNumber = "160"; //$NON-NLS-1$
                    }else{
                        replaceLabel=htmlEntities[c];
                    }
                } else {
                    // Nothing to do : in an attribute, don't convert the character ' '
                }
            } else if (replaceSpaces && c=='\n') {
                if (b == null) {
                    b = new StringBuilder();
                    b.append(s.toCharArray(), 0, i);
                }
                b.append("<br/>"); // $NON-NLS-1$
                if(i+1<length && chars[i+1]=='\r') {
                    i++;
                }
                continue;
            } else if (replaceSpaces && c=='\r') {
                if (b == null) {
                    b = new StringBuilder();
                    b.append(s.toCharArray(), 0, i);
                }
                b.append("<br/>"); // $NON-NLS-1$
                if(i+1<length && chars[i+1]=='\n') {
                    i++;
                }
                continue;
            } else if (c<256) {
                if( isXhtml ){
                    if( null != htmlEntities[c] ){
                        if( isXhtmlEntity(c) ){
                            replaceLabel=htmlEntities[c];
                        }else{
                            replaceNumber = Integer.toString(c);
                        }
                    } // else pass-through chars like 'a', 'b', 'c'
                }else{
                    replaceLabel=htmlEntities[c];
                }
            } else {
                if( c==0x20AC && !isXhtml) {
                    
                    replaceLabel="euro"; //$NON-NLS-1$
                } else {
                    replaceNumber=Integer.toString(c);
                }
            }
            if (replaceLabel!=null || replaceNumber!=null) {
                if (b == null) {
                    b = new StringBuilder();
                    b.append(s.toCharArray(), 0, i);
                }
                b.append("&"); //$NON-NLS-1$
                if (replaceLabel!=null) {
                    b.append(replaceLabel);
                } else {
                    b.append("#"); //$NON-NLS-1$
                    b.append(replaceNumber);
                }
                b.append(";"); //$NON-NLS-1$
            } else if( b!=null ) {
                b.append(c);
            }
        }
        return (b != null) ? b.toString() : s;
    }
    
}

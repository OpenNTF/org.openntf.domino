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
 * Some utilities for path handling.
 * @ibm-api
 */
public class PathUtil {
	
    /**
     * Add to path ensuring there is just one separator in between the 2 parts..
     * @ibm-api
     */
    public static String concat(String path1, String path2, char sep) {
    	if(StringUtil.isEmpty(path1)) {
    		return path2;
    	}
    	if(StringUtil.isEmpty(path2)) {
    		return path1;
    	}
    	StringBuilder b = new StringBuilder();
    	if(path1.charAt(path1.length()-1)==sep) {
    		b.append(path1,0,path1.length()-1);
    	} else {
    		b.append(path1);
    	}
    	b.append(sep);
    	if(path2.charAt(0)==sep) {
    		b.append(path2,1,path2.length());
    	} else {
    		b.append(path2);
    	}
    	return b.toString();
    }

    /**
     * Add to path ensuring that the resulting path will start with a '/' and
     * doesn't end with a '/'.
     * @ibm-api
     */
    public static String concatPath(String path1, String path2, char sep) {
    	StringBuilder b = new StringBuilder();
    	if(StringUtil.isNotEmpty(path1)) {
	    	if(path1.charAt(0)!=sep) {
	    		b.append(sep);
	    	}
	    	if(path1.charAt(path1.length()-1)==sep) {
	    		b.append(path1,0,path1.length()-1);
	    	} else {
	    		b.append(path1);
	    	}
    	}
    	if(StringUtil.isNotEmpty(path2)) {
	    	if(path2.charAt(0)!=sep) {
	    		b.append(sep);
	    	}
	    	if(path2.charAt(path2.length()-1)==sep) {
	    		b.append(path2,0,path2.length()-1);
	    	} else {
	    		b.append(path2);
	    	}
    	}
    	return b.toString();
    }
    
    /**
     * Add to path ensuring that the resulting path doesn't start with a '/' and
     * doesn't end with a '/'.
     * @ibm-api
     */
    public static String concatParts(String path1, String path2, char sep) {
    	StringBuilder b = new StringBuilder();
    	if(path1.charAt(path1.length()-1)==sep) {
    		b.append(path1,0,path1.length()-1);
    	} else {
    		b.append(path1);
    	}
    	if(path2.charAt(0)!=sep) {
    		b.append(sep);
    	}
    	if(path2.charAt(path2.length()-1)==sep) {
    		b.append(path2,0,path2.length()-1);
    	} else {
    		b.append(path2);
    	}
    	return b.toString();
    }
    
    /**
     * Normalize a path, ensuring that it start with a '/' and does not end with a '/'.
     * @ibm-api
     */
    public static String normalizePath(String path, char sep) {
    	boolean needLeadingSlash = path.charAt(0)!=sep;
    	boolean needRemoveTrailingSlash = path.charAt(path.length()-1)==sep;
    	if(needLeadingSlash || needRemoveTrailingSlash) {
    		if(needLeadingSlash && needRemoveTrailingSlash) {
    			StringBuilder b = new StringBuilder();
    			b.append(sep);
    			b.append(path,0,path.length()-1);
    			return b.toString();
    		} else if(needLeadingSlash) {
    			StringBuilder b = new StringBuilder();
    			b.append(sep);
    			b.append(path);
    			return b.toString();
    		} else {
    			return path.substring(0,path.length()-1);
    		}
    	}
    	return path;
    }    
    
    /**
     * Surrounds a path, ensuring that it starts with a '/' and ends with a '/'.
     * @ibm-api
     */
    public static String surroundPath(String path, char sep) {
        boolean needLeadingSlash = path.charAt(0)!=sep;
        boolean needTrailingSlash = path.charAt(path.length()-1)!=sep;
        if(needLeadingSlash || needTrailingSlash) {
            if(needLeadingSlash && needTrailingSlash) {
                StringBuilder b = new StringBuilder();
                b.append(sep);
                b.append(path);
                b.append(sep);
                return b.toString();
            } else if(needLeadingSlash) {
                StringBuilder b = new StringBuilder();
                b.append(sep);
                b.append(path);
                return b.toString();
            } else {
                StringBuilder b = new StringBuilder();
                b.append(path);
                b.append(sep);
                return b.toString();
            }
        }
        return path;
    }    
}

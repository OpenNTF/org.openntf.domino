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

package com.ibm.commons.util.io;

import java.io.File;


/**
 * Utilities for working with files
 * @ibm-api
 */
public class FileUtil {

    /**
     * Private constructor
     */
    private FileUtil() {}

    /**
     * Delete the a file or a directory.
     * @param file the file to delete
     */
    public static void deleteFile( File file ) {
    	// Delete its content, if a directory
    	if(file.isDirectory()) {
	        emptyDirectory(file);
    	}
        // Delete the file/directory
    	file.delete();
    }

    /**
     * Delete the content of a directory, but not the directory itself
     * @param directory the directory the clear
     */
    public static void emptyDirectory( File directory ) {
        File[] f = directory.listFiles();
        if( f!=null ) {
            for( int i=0; i<f.length; i++ ) {
                if( f[i].isDirectory() ) {
                	emptyDirectory( f[i] );
                }
                f[i].delete();
            }
        }
    }
    
}

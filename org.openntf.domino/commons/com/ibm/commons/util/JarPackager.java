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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;

import com.ibm.commons.util.io.ByteStreamCache;
import com.ibm.commons.util.io.StreamUtil;

/**
 * @ibm-not-published
 */
public class JarPackager {
    
    private JarOutputStream jarOS = null;
    private File file = null;   //underlying file
    private boolean bTempFile = false;
    private HashSet<String> entriesSet;
    
    /**
     * @param file
     * @param mf
     * @throws IOException
     */
    public JarPackager( File file, Manifest mf )throws IOException {
        this.file = file;
        if ( mf != null ){
            jarOS = new JarOutputStream( new FileOutputStream( file ), mf );
        }else{
            jarOS = new JarOutputStream( new FileOutputStream( file ) );
        }
        entriesSet = new HashSet<String>();
    }
    
    /**
     * @param file
     * @throws IOException
     */
    public JarPackager( File file ) throws IOException{
        this( file, null );
    }

    /**
     * @param filePath 
     * @throws IOException 
     * 
     */
    public JarPackager(String filePath) throws IOException {
        this( filePath, null );
    }
    
    /**
     * @param filePath
     * @param mf
     * @throws IOException
     */
    public JarPackager( String filePath, Manifest mf ) throws IOException{
        this( new File( filePath ), mf );
    }

    /**
     * @throws IOException 
     * @throws  
     * 
     */
    public JarPackager() throws IOException {        
        this( File.createTempFile("designer", "jar" ) ); // $NON-NLS-1$ $NON-NLS-2$
        bTempFile = true;
    }

    /**
     * @throws IOException 
     * 
     */
    public void close() throws IOException {
        if ( jarOS != null ){
            try{
                jarOS.close();
                if ( bTempFile && file != null ){
                    file.delete();
                    file = null;
                }
                if ( file != null ){
                	postFinishJar( file );
                }
            }finally{
                jarOS = null;
            }
        }
        
    }

    /**
     * @param file
     * called after the jar content is written to the file
     * subclasses can override
     */
    protected void postFinishJar(File file) {		
	}

	/**
     * @param string
     * @param is
     * @throws IOException 
     */
    public void addContents(String entryName, InputStream is) throws IOException {
        ensureOpen();
        
        if ( entriesSet.contains( entryName ) ){
            //Duplicate entry
            return;
        }
        
        JarEntry entry = new JarEntry( entryName );
        entry.setMethod( ZipEntry.DEFLATED );
        
        CRC32 checksumCalculator= new CRC32();
        ByteStreamCache bsc = new ByteStreamCache();
        entry.setSize( StreamUtil.copyStream( is, bsc.getOutputStream() ));
        checksumCalculator.update( bsc.toByteArray() );
        entry.setCrc(checksumCalculator.getValue());
        
        jarOS.putNextEntry( entry );
        //Now that we have the correct size, we can copy it to the jar stream
        StreamUtil.copyStream( bsc.getInputStream(), jarOS );
        
        entriesSet.add( entryName );
    }

    /**
     * 
     */
    private void ensureOpen() {
        if ( jarOS == null ){
            throw new IllegalStateException();
        }
    }

    /**
     * @return
     * @throws IOException 
     */
    public InputStream getInputStream() throws IOException {
        if ( jarOS != null && file != null ){
        	
        	if ( hasEntries() ){
        		jarOS.finish();
        	}
        	
        	postFinishJar( file );
            return new FileInputStream( file );
        }
        return null;
    }

    /**
     * @return
     */
    public boolean hasEntries() {
        return !entriesSet.isEmpty();
    }

	/**
	 * @param tempDir
	 * @throws IOException 
	 */
	public void addContents(File tempDir) throws IOException {
		if ( tempDir.isDirectory() ){
			String baseDir = tempDir.getAbsolutePath();
			internal_addContents( tempDir, baseDir );
		}
	}

	/**
	 * @param file2
	 * @param baseDir
	 * @throws IOException 
	 */
	private void internal_addContents(File file, String baseDir) throws IOException {
		if ( file.isFile() ){
			FileInputStream is = null;
			try{
				String entryName = file.getAbsolutePath();
				//Remove the baseDir
				entryName = entryName.substring( entryName.indexOf( baseDir ) + baseDir.length() + 1 );
				//Make sure to used forward slash in the name
				entryName = entryName.replace('\\', '/');
				is = new FileInputStream( file );
				addContents( entryName, is );
			}finally{
				StreamUtil.close( is );
			}
		}else{
			File[] childs = file.listFiles();
			if(childs!=null) {
    			for ( int i = 0; i < childs.length; i++ ){
    				internal_addContents( childs[i], baseDir );
    			}
			}
		}
	}

}

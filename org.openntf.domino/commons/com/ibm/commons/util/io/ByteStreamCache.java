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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.OutputStream;

/**
 * Byte buffer.
 * <p>
 * This class is used to store bytes and provides both an OutputStream for storing the
 * bytes and an InputStream for reading them. It stores the bytes in memory in a highly
 * optimized way, with a possibility of using a backing file when the content is bigger
 * than a predefined threshold.
 * </p> 
 * @ibm-api
 */
public class ByteStreamCache {

    public static final int DEFAULT_BLOCKSIZE     = 8192; // Size of a data block
    public static final int DEFAULT_THRESHOLD     = 32;   // Number of memory blocks before using a file

    private int blockSize;
    private int threshold;
    private int nBlock;
    private Block firstBlock;
    private Block lastBlock;

    private static class Block {
        Block       next;
        int         count;
        byte[]      data;
        Block(int blockSize) {
            data = new byte[blockSize];
        }
    }

    /** @ibm-api */
    public ByteStreamCache() {
        this.blockSize = DEFAULT_BLOCKSIZE;
        this.threshold = DEFAULT_THRESHOLD;
        this.nBlock = 1;
        firstBlock = lastBlock = new Block(blockSize);
    }
    /** @ibm-api */
    public ByteStreamCache(int blockSize) {
        this.blockSize = blockSize;
        this.threshold = DEFAULT_THRESHOLD;
        this.nBlock = 1;
        firstBlock = lastBlock = new Block(blockSize);
    }

    /** @ibm-api */
    public ByteStreamCache(int blockSize, int threshold) {
        this.blockSize = blockSize;
        this.threshold = threshold;
        this.nBlock = 1;
        firstBlock = lastBlock = new Block(blockSize);
    }
    
    /** @ibm-api */
    public boolean isEqual(ByteStreamCache other) {
        if(blockSize==other.blockSize) {
            long l1 = getLength();
            long l2 = other.getLength();
            if(l1==l2) {
                Block b1 = firstBlock;
                Block b2 = other.firstBlock;
                while(b1!=null && b2!=null) {
                    int count = b1.count;
                    for(int i=0; i<count; i++ ) {
                        if(b1.data[i]!=b2.data[i]) {
                            return false;
                        }
                    }
                    b1 = b1.next;
                    b2 = b2.next;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Get the number of stored bytes.
     * @ibm-api
     */
    public long getLength() {
        return (nBlock-1)*blockSize + lastBlock.count;
    }

    /**
     * Get an indexed byte.
     * @ibm-api
     */
    public byte getByte( int pos ) {
        Block b = firstBlock;
        while( pos>blockSize ) {
            b = b.next;
            pos -= blockSize;
        }
        return b.data[pos];
    }

    /**
     * Clear the data inside the cache.
     * @ibm-api
     */
    public void clear() {
        this.nBlock = 1;
        firstBlock = lastBlock = new Block(blockSize);
    }

    /**
     * Get an input stream on the stored data.
     * @ibm-api
     */
    public InputStream getInputStream() {
        return new InternalInputStream();
    }

    /**
     * Get an output stream to store the data.
     * @ibm-api
     */
    public OutputStream getOutputStream() {
        return new InternalOutputStream();
    }

    /**
     * Init the data from an existing InputStream.
     * @ibm-api
     */
    public void copyFrom( InputStream is ) throws java.io.IOException {
        // Fill the entire block
        while(true) {
            if( lastBlock.count==blockSize ) {
                addNewBlock();
            }
            int read = is.read(lastBlock.data,lastBlock.count,blockSize-lastBlock.count);
            if( read<0 ) {
                return; // The source stream end is reached
            }
            lastBlock.count += read;
        }
    }

    /**
     * Init the data from an existing InputStream with a maximum bytes to be read.
     * @ibm-api
     */
    public void copyFrom( InputStream is, int maxRead ) throws java.io.IOException {
        // Fill the entire block
        while(maxRead>0) {
            if( lastBlock.count==blockSize ) {
                addNewBlock();
            }
            int toRead = Math.min(maxRead,blockSize-lastBlock.count);
            int read = is.read(lastBlock.data,lastBlock.count,toRead);
            if( read<0 ) {
                return; // The source stream end is reached
            }
            lastBlock.count += read;
            maxRead -= read;
        }
    }

    /**
     * Init the data from an existing ObjectInput.
     * @ibm-api
     */
    public void copyFrom( ObjectInput is ) throws java.io.IOException {
        // Fill the entire block
        while(true) {
            if( lastBlock.count==blockSize ) {
                addNewBlock();
            }
            int read = is.read(lastBlock.data,lastBlock.count,blockSize-lastBlock.count);
            if( read<0 ) {
                return; // The source stream end is reached
            }
            lastBlock.count += read;
        }
    }


    /**
     * Init the data from an existing ObjectInput with a maximum bytes to be read.
     * @ibm-api
     */
    public void copyFrom( ObjectInput is, int maxRead ) throws java.io.IOException {
        // Fill the entire block
        while(maxRead>0) {
            if( lastBlock.count==blockSize ) {
                addNewBlock();
            }
            int toRead = Math.min(maxRead,blockSize-lastBlock.count);
            int read = is.read(lastBlock.data,lastBlock.count,toRead);
            if( read<0 ) {
                return; // The source stream end is reached
            }
            lastBlock.count += read;
            maxRead -= read;
        }
    }

    /**
     * Copy the stream to another stream.
     * @ibm-api
     */
    public void copyTo(OutputStream os) throws java.io.IOException {
        for( Block b=firstBlock; b!=null; b=b.next ) {
        	os.write(b.data, 0, b.count);
        }
    }

    /**
     * Copy the stream to another stream.
     * @ibm-api
     */
    public void copyTo(ObjectOutput os) throws java.io.IOException {
        for( Block b=firstBlock; b!=null; b=b.next ) {
        	os.write(b.data, 0, b.count);
        }
    }

    /**
     * Copy the stream to a file.
     * @ibm-api
     */
    public void copyTo(File f) throws java.io.IOException {
    	OutputStream os = new FastBufferedOutputStream(new FileOutputStream(f));
    	try {
    		copyTo(os);
    	} finally {
    		os.close();
    	}
    }
    
    /**
     * Convert to a byte array.
     * WARN: this fct is *not* really optimized!
     * @ibm-api
     */
    public byte[] toByteArray() {
        int length = (int)getLength();
        byte[] result = new byte[length];
        int pos = 0;
        for( Block b=firstBlock; b!=null; b=b.next ) {
            System.arraycopy( b.data, 0, result, pos, b.count );
            pos += b.count;
        }
        return result;
    }

    /**
     * Add a new block to the list.
     */
    private final void addNewBlock() {
        Block b = new Block(blockSize);
        lastBlock.next = b;
        lastBlock = b;
        nBlock++;
        // TODO: manage the persistence to a temporary file when the threshold is reached!
    }

    protected class InternalInputStream extends InputStream {

        private int currentPosition;
        private Block currentBlock = firstBlock;

        public void close() throws java.io.IOException {
        }

        public int read() throws IOException {
            if( currentBlock!=null ) {
                if( currentPosition>=currentBlock.count ) {
                    if( !nextBlock() || currentBlock.count==0 ) {
                        return -1;
                    }
                }
                return (int)currentBlock.data[currentPosition++] & 0xFF;
            }
            return -1;
        }

        public int read(byte b[], int off, int len) throws IOException {
            if( currentBlock!=null ) {
                if( currentPosition>=currentBlock.count ) {
                    if( !nextBlock() || currentBlock.count==0 ) {
                        return -1;
                    }
                }
                int read = Math.min( len, currentBlock.count-currentPosition );
                System.arraycopy( currentBlock.data, currentPosition, b, off, read );
                currentPosition += read;
                return read;
            }
            return -1;
        }

        private boolean nextBlock() {
            if( currentBlock!=null ) {
                currentBlock = currentBlock.next;
                currentPosition = 0;
            }
            return currentBlock!=null;
        }

        public long skip(long n) throws IOException {
            if( currentBlock!=null ) {
                if( currentPosition>=currentBlock.count ) {
                    if( !nextBlock() ) {
                        return -1;
                    }
                }
                int toSkip = Math.min( (int)n, currentBlock.count-currentPosition );
                currentPosition += toSkip;
                return toSkip;
            }
            return -1; // EOF
        }

        public int available() throws IOException {
            if( currentBlock!=null ) {
                //DTAIEB: fixed this code to check if there is another block
                if ( currentBlock.count-currentPosition > 0 ){
                    return currentBlock.count-currentPosition;
                }
                //Check the next block
                return currentBlock.next == null ? 0 : currentBlock.count;
            }
            return 0;
        }
    }

    protected class InternalOutputStream extends OutputStream {

        public void write(int b) throws IOException {
        	ByteStreamCache.this.write(b);
        }

        public void write(byte b[], int off, int len) throws IOException {
        	ByteStreamCache.this.write(b,off,len);
         }

        public void flush() throws IOException {
            // Nothing here...
        }

        public void close() throws IOException {
            // Nothing here...
        }
    }

    public final void write(int b) throws IOException {
        if( lastBlock.count==blockSize ) {
            addNewBlock();
        }
        lastBlock.data[lastBlock.count++] = (byte)b;
    }
    
    public final void write(byte b[]) throws IOException {
    	write(b,0,b.length);
    }

    public final void write(byte b[], int off, int len) throws IOException {
        while(len>0 ) {
            int max = Math.min( len, blockSize-lastBlock.count );
            if( max==0 ) {
                addNewBlock();
                max = Math.min( len, blockSize );
            }
            System.arraycopy(b,off,lastBlock.data,lastBlock.count,max);
            lastBlock.count += max;
            len-=max; off+=max;
        }
     }

/*
    public static void main( String[] args ) {
        byte[] b = new Byte[256];
        for(int i=0; i<b.length; i++ ) {
            b[i] = (byte)(i & 0xFF);
        }

        try {
            TByteStreamCache c = new TByteStreamCache();

            OutputStream os = c.getOutputStream();
            for( int i=0; i<100; i++ ) {
                for( int j=0; j<256; j++ ) {
                    os.write(i);
                    os.write(j);
                }
            }

            InputStream is = c.getInputStream();
            for( int i=0; i<100; i++ ) {
                for( int j=0; j<256; j++ ) {
                    int v1 = is.read()&0xFF;
                    if( v1!=i ) {
                        TDiag.trace( "Error on i, i={0}, j={1}", TString.toString(i), TString.toString(j) ); // $NON-NLS-1$
                    }
                    int v2 = is.read()&0xFF;
                    if( v2!=j ) {
                        TDiag.trace( "Error on j, i={0}, j={1}", TString.toString(i), TString.toString(j) ); // $NON-NLS-1$
                    }
                }
            }
            TDiag.trace( "test OK" ); // $NON-NLS-1$
         } catch( Exception e ) {
            com.ibm.workplace.designer.util.TDiag.exception(e);
        }
    }
*/
}

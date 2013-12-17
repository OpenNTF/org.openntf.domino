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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * Characters buffer.
 * <p>
 * This class is used to store characters and provides both an Writer for storing the
 * bytes and an Reader for reading them. It stores the characters in memory in a highly
 * optimized way, with a possibility of using a backing file when the content is bigger
 * than a predefined threshold.
 * </p> 
 * @ibm-api
 */
public class CharacterStreamCache {

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
        char[]      data;
        Block(int blockSize) {
            data = new char[blockSize];
        }
    }

    /** @ibm-api */
    public CharacterStreamCache() {
        this.blockSize = DEFAULT_BLOCKSIZE;
        this.threshold = DEFAULT_THRESHOLD;
        this.nBlock = 1;
        firstBlock = lastBlock = new Block(blockSize);
    }

    /** @ibm-api */
    public CharacterStreamCache(int blockSize, int threshold) {
        this.blockSize = blockSize;
        this.threshold = threshold;
        this.nBlock = 1;
        firstBlock = lastBlock = new Block(blockSize);
    }

    /** @ibm-api */
    public boolean isEqual(CharacterStreamCache other) {
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
     * Get an indexed character.
     * @ibm-api
     */
    public char getChar( int pos ) {
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
     * Get a reader on the stored data.
     * @ibm-api
     */
    public Reader getReader() {
        return new InternalReader();
    }

    /**
     * Get a writer to store the data.
     * @ibm-api
     */
    public Writer getWriter() {
        return new InternalWriter();
    }

    /**
     * Init the data from an existing reader.
     * @ibm-api
     */
    public void copyFrom( Reader r ) throws java.io.IOException {
        // Fill the entire block
        while(true) {
            if( lastBlock.count==blockSize ) {
                addNewBlock();
            }
            int read = r.read(lastBlock.data,lastBlock.count,blockSize-lastBlock.count);
            if( read<0 ) {
                return; // The source stream end is reached
            }
            lastBlock.count += read;
        }
    }

    /**
     * Copy the stream to another stream.
     * @ibm-api
     */
    public void copyTo(Writer w) throws java.io.IOException {
        for( Block b=firstBlock; b!=null; b=b.next ) {
        	w.write(b.data, 0, b.count);
        }
    }

    /**
     * Copy the stream to a file.
     * @ibm-api
     */
    public void copyTo(File f) throws java.io.IOException {
    	Writer w = new BufferedWriter(new FileWriter(f));
    	try {
    		copyTo(w);
    	} finally {
    		w.close();
    	}
    }

    /**
     * Convert to a char array.
     * WARN: this fct is *not* really optimized!
     * @ibm-api
     */
    public char[] toCharArray() {
        int length = (int)getLength();
        char[] result = new char[length];
        int pos = 0;
        for( Block b=firstBlock; b!=null; b=b.next ) {
            System.arraycopy( b.data, 0, result, pos, b.count );
            pos += b.count;
        }
        return result;
    }

    public String toString() {
        return new String(toCharArray());
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

    protected class InternalReader extends Reader {

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
                return (int)currentBlock.data[currentPosition++] & 0xffff;
            }
            return -1;
        }

        public int read(char b[], int off, int len) throws IOException {
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
                if ( currentBlock.count-currentPosition > 0 ){
                    return currentBlock.count-currentPosition;
                }
                //Check the next block
                return currentBlock.next == null ? 0 : currentBlock.count;
            }
            return 0;
        }
    }

    protected class InternalWriter extends Writer {

        public void write(int b) throws IOException {
        	CharacterStreamCache.this.write(b);
        }

        public void write(char b[], int off, int len) throws IOException {
        	CharacterStreamCache.this.write(b,off,len);
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
        lastBlock.data[lastBlock.count++] = (char)b;
    }

    public final void write(char b[]) throws IOException {
    	write(b, 0, b.length);
    }
    
    public final void write(char b[], int off, int len) throws IOException {
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
}

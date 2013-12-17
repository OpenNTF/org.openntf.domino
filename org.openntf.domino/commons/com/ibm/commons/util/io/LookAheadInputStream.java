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

import java.io.IOException;
import java.io.InputStream;

import com.ibm.commons.util.FastStringBuffer;

/**
 * The lexical inputstream is a specific inputstrean that cache some data of another inputstream
 * and that permits a lookup of characters.
 * @ibm-api
 */
public class LookAheadInputStream extends InputStream {

    /**
     * Constructor.
     * @ibm-api
     */
    public LookAheadInputStream( InputStream is, int bufferLength ) {
        this.is = is;
        this.buffer = new byte[bufferLength];

        // Initialize the reader data
        this.position = 0;
        this.count = 0;
        this.eof = false;
        this.index=0;
    }

    /**
     * @ibm-api
     */
    public int getBufferLength() {
        return buffer.length;
    }

    /**
     * @ibm-api
     */
    public final int read() throws IOException {
        // Need to read the buffer ?
        if( count==0 && !readData(1) ) {
            return -1;
        }

        // Return the next available char
        count--;
        index++;
        return buffer[position++];
    }

    /**
     * @ibm-api
     */
    public int read( byte cbuf[], int off, int len) throws IOException {
        if( len>0 ) {
            // Ensure that the buffer have some data
            if( count==0 && !readData(1) ) {
                return -1;
            }

            // Copy as many data as possible
            int n = Math.min( len, count );
            if( cbuf!=null ) {
                System.arraycopy( buffer, position, cbuf, off, n );
            }
            position += n;
            count -= n;
            index += n;
            return n;
        }
        return 0;
    }

    /**
     * @ibm-api
     */
    public long skip(long len) throws IOException {
        if( len>0 ) {
            if( len==1 ) {
                read(); return 1;
            } else {
                return read( null, 0, (int)len );
            }
        }
        return 0;
    }

    /**
     * @ibm-api
     */
    public void close() throws IOException {
        is.close();
    }

    /**
     * @ibm-api
     */
    public int getCurrentIndex() {
        return index;
    }

    private boolean readData(int minData) throws IOException {
        // Check for the end of the reader
        if( eof ) {
            return false;
        }

        // Move the existing data at the beggining of the buffer
        if( count>0 ) {
            System.arraycopy( buffer, position, buffer, 0, count );
        }
        position = 0;

        // Read as many data as possible
        while(count<minData) {
            // Read from the internal reader
            int r = readBuffer( buffer, count, buffer.length-count );
            if( r<=0 ) {
                eof = true;
                return false;
            } else {
                count += r;
            }
        }
        return true;
    }

    /**
     * Internal function that read data into the buffer.
     * It can be overriden in order to set some kind of filters (skipping blanks,
     * comments...).
     */
    protected int readBuffer( byte[] buffer, int position, int count ) throws IOException {
        return is.read( buffer, position, count );
    }


    // ========================================================================
    // Look ahead functions
    // ========================================================================

    /**
     * Check if the end of the stream has been reached
     * @ibm-api
     */
    public boolean hasByte() throws IOException {
        // Need to read the buffer ?
        if( count==0 && !readData(1) ) {
            return false;
        }
        return true;
    }

    /**
     * Read a byte ahead.
     * The position cannot be greater than the buffer length.
     * @param pos the character position
     * @return the character, -1 if not available (not enough character in the reader)
     * @ibm-api
     */
    public final int getByteAt( int pos ) throws IOException {
        // If the character not is already available, read it
        if( pos>=count ) {
            if( !readData(pos+1) ) {
                return -1;
            }
        }

        // And return it
        //if( (position+pos)>=buffer.length ) {
        //    return -1;
        //}
        return ((int)buffer[position+pos]) & 0xFF;
    }

    /**
     * Read one byte ahead.
     * @return the character, -1 if not available (not enough character in the reader)
     * @ibm-api
     */
    public final int getByte() throws IOException {
        //return getCharAt(0);
        // If the character not is already available, read it
        if( count==0 ) {
            if( !readData(1) ) {
                return -1;
            }
        }
        return ((int)buffer[position]) & 0xFF;
    }

    /**
     * Get a string of # characters.
     * Mainly for debugging!
     * @ibm-api
     */
    public byte[] getByteAhead( int maxChars ) throws IOException {
        // Ensure that the buffer is long enough
        if( maxChars>count ) {
            readData(maxChars);
        }

        // Get each character
        byte[] c = new byte[Math.min(maxChars,count)];
        for( int i=0; i<c.length; i++ ) {
            c[i]=buffer[position+i];
        }
        return c;
    }

    
    /**
     * @ibm-api
     */
    public char getChar() throws IOException {
        return (char)getByte();
    }

    /**
     * @ibm-api
     */
    public char getCharAt(int index) throws IOException {
        return (char)getByteAt(index);
    }
    
    /**
     * @ibm-api
     */
    public boolean startsWith( char c ) throws IOException {
        return getChar()==c;
    }

    /**
     * @ibm-api
     */
    public boolean startsWithIgnoreCase( char c ) throws IOException {
        return Character.toLowerCase((char)getCharAt(0))==Character.toLowerCase(c);
    }

    /**
     * @ibm-api
     */
    public boolean startsWith( String s ) throws IOException {
        int length = s.length();

        // Ensure that the buffer is long enough
        if( length>count ) {
            if( !readData(length) ) {
                return false;
            }
        }

        // Compare each character
        for( int i=0; i<length; i++ ) {
            if( buffer[position+i]!=s.charAt(i) ) {
                return false;
            }
        }
        return true;
    }

    /**
     * @ibm-api
     */
    public boolean startsWithIgnoreCase( String s ) throws IOException {
        int length = s.length();

        // Ensure that the buffer is long enough
        if( length>count ) {
            if( !readData(length) ) {
                return false;
            }
        }

        // Compare each character
        for( int i=0; i<length; i++ ) {
            if( Character.toLowerCase((char)buffer[position+i])!=Character.toLowerCase(s.charAt(i)) ) {
                return false;
            }
        }
        return true;
    }

    /**
     * @ibm-api
     */
    public boolean match( char c ) throws IOException {
        if( startsWith(c) ) {
            //read();
            count--;
            index++;
            position++;
            return true;
        }
        return false;
    }

    /**
     * @ibm-api
     */
    public boolean matchIgnoreCase( char c ) throws IOException {
        if( startsWithIgnoreCase(c) ) {
            //read();
            count--;
            index++;
            position++;
            return true;
        }
        return false;
    }

    /**
     * @ibm-api
     */
    public boolean match( String s ) throws IOException {
        if( startsWith(s) ) {
            //read( null, 0, s.length() );
            int len = s.length();
            count-=len;
            index+=len;
            position+=len;
            return true;
        }
        return false;
    }

    /**
     * @ibm-api
     */
    public boolean matchIgnoreCase( String s ) throws IOException {
        if( startsWithIgnoreCase(s) ) {
            //read( null, 0, s.length() );
            int len = s.length();
            count-=len;
            index+=len;
            position+=len;
            return true;
        }
        return false;
    }

    /**
     * Skip a list of blanks characters (space, new line, tab...).
     * @ibm-api
     */
    public boolean skipBlanks(FastStringBuffer b) throws IOException {
        int c = getChar();
        if( c==' ' || c=='\t' || c=='\n' || c=='\r' ) {
            b.append((char)c);
            count--; index++; position++;
            while(true) {
                c = getChar();
                if( c!=' ' && c!='\t' && c!='\n' && c!='\r' ) {
                    return true;
                }
                b.append((char)c);
                count--; index++; position++;
            }
        }
        return false;
    }
    
    /**
     * @ibm-api
     */
    public boolean skipBlanks() throws IOException {
        int c = getChar();
        if( c==' ' || c=='\t' || c=='\n' || c=='\r' ) {
            count--; index++; position++;
            while(true) {
                c = getChar();
                if( c!=' ' && c!='\t' && c!='\n' && c!='\r' ) {
                    return true;
                }
                count--; index++; position++;
            }
        }
        return false;
    }

    /**
     * @ibm-api
     */
    public void skipUpto( char c ) throws IOException {
        do {
            int cc = getChar();
            if( cc<0 || cc==c ) {
                return;
            }
            count--; index++; position++;
        } while(true);
    }

    /**
     * @ibm-api
     */
    public String upto( char c ) throws IOException {
        FastStringBuffer b = new FastStringBuffer();
        do {
            int cc = getChar();
            if( cc<0 || cc==c ) {
                return b.toString();
            }
            b.append(cc);
            count--; index++; position++;
        } while(true);
    }


    private InputStream  is;
    private byte[]  buffer;
    private int     count;
    private int     position;
    private boolean eof;
    private int     index;          // Index in the (embedded) stream of the next character
                                    //    = number of characters already read
}


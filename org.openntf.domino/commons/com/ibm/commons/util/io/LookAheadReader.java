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
import java.io.Reader;
import java.io.Writer;

import com.ibm.commons.util.FastStringBuffer;

/**
 * The lexical reader is a specific reader that cache some data of another reader
 * and that permits a lookup of characters.
 * @ibm-api
 */
public class LookAheadReader extends Reader {

    /**
     * Constructor.
     * @ibm-api
     */
    public LookAheadReader( Reader reader, int bufferLength ) {
        this.reader = reader;
        this.buffer = new char[bufferLength];

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
    public final int readCDATABase64() throws IOException {
        if( count==0 ) {
            if( !readData(1) ) {
                return -1;
            }
        }
        char c = buffer[position];
        if( c==']' ) {
            return -1;
        }
        position++;
        count--;
        return (int)c;
    }
    
    /**
     * @ibm-api
     */
    public final int readCDATABase64Bytes(byte[] b) throws IOException {
    	return readCDATABase64Bytes(b,0,b.length);
    }

    /**
     * @ibm-api
     */
    public final int readCDATABase64Bytes(byte[] b, int off, int len) throws IOException {
        if( count==0 ) {
            if( !readData(1) ) {
                return -1;
            }
        }
        int max = Math.min(len,count);
        for( int i=0; i<max; i++ ) {
            char c = buffer[position+i];
            if( c==']' ) {
                position += i;
                count -= i;
                return i;
            }
            b[i+off] = (byte)c;
        }
        position += max;
        count -= max;
        return max;
    }

    /**
     * @ibm-api
     */
    public int read( char cbuf[], int off, int len) throws IOException {
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
        reader.close();
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
    protected int readBuffer( char[] buffer, int position, int count ) throws IOException {
        return reader.read( buffer, position, count );
    }


    // ========================================================================
    // Look ahead function
    // ========================================================================

    /**
     * Check if the end of the strealm has been reached
     * @ibm-api
     */
    public boolean hasChar() throws IOException {
        // Need to read the buffer ?
        if( count==0 && !readData(1) ) {
            return false;
        }
        return true;
    }

    /**
     * Read a character ahead.
     * The position cannot be greater than the buffer length.
     * @param pos the character position
     * @return the character, -1 if not available (not enough character in the reader)
     * @ibm-api
     */
    public final int getCharAt( int pos ) throws IOException {
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
        return buffer[position+pos];
    }

    /**
     * Read one character ahead.
     * @return the character, -1 if not available (not enough character in the reader)
     * @ibm-api
     */
    public final int getChar() throws IOException {
        //return getCharAt(0);
        // If the character not is already available, read it
        if( count==0 ) {
            if( !readData(1) ) {
                return -1;
            }
        }
        return buffer[position];
    }

    /**
     * Get a string of # characters.
     * Mainly for debugging!
     * @ibm-api
     */
    public String getStringAhead( int maxChars ) throws IOException {
        // Ensure that the buffer is long enough
        if( maxChars>count ) {
            readData(maxChars);
        }

        // Get each character
        char[] c = new char[Math.min(maxChars,count)];
        for( int i=0; i<c.length; i++ ) {
            c[i]=buffer[position+i];
        }
        return new String(c);
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
            if( Character.toLowerCase(buffer[position+i])!=Character.toLowerCase(s.charAt(i)) ) {
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
    public void skipUpto( String s ) throws IOException {
        do {
        	int firstChar = s.charAt(0);
            int cc = getChar();
            if( cc<0 || (cc==firstChar&&startsWith(s)) ) {
                return;
            }
            count--; index++; position++;
        } while(true);
    }

    /**
     * @ibm-api
     */
    public void skipUptoEOL() throws IOException {
        do {
            int cc = getChar();
            if( cc<0 || (cc=='\n' || cc=='\r') ) {
                return;
            }
            count--; index++; position++;
        } while(true);
    }

    /**
     * @ibm-api
     */
    public String upto( char c ) throws IOException {
        StringBuilder b = new StringBuilder();
        do {
            int cc = getChar();
            if( cc<0 || cc==c ) {
                return b.toString();
            }
            b.append((char)cc);
            count--; index++; position++;
        } while(true);
    }

    /**
     * Copy to a writer
     * @ibm-api
     */
    public void copy( Writer w ) throws IOException {
        // TODO: optimize by using the internal buffer...
        char[] b = new char[1024];
        do {
            int r = read( b, 0, b.length );
            if( r>0 ) {
                w.write( b, 0, r );
            } else {
                return;
            }
        } while(true);
    }

    
    private Reader  reader;
    private char[]  buffer;
    private int     count;
    private int     position;
    private boolean eof;
    private int     index;          // Index in the (embedded) stream of the next character
                                    //    = number of characters already read
}


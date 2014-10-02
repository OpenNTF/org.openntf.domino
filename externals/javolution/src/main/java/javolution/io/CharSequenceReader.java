/*
 * Javolution - Java(TM) Solution for Real-Time and Embedded Systems
 * Copyright (C) 2012 - Javolution (http://javolution.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package javolution.io;

import java.io.IOException;
import java.io.Reader;
import javolution.lang.MathLib;
import javolution.text.CharArray;
import javolution.text.Text;
import javolution.text.TextBuilder;

/**
 * <p> This class allows any <code>CharSequence</code> to be used as 
 *     a reader.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.8, May 8, 2004
 */
public final class CharSequenceReader extends Reader {

    /**
     * Holds the character sequence input.
     */
    private CharSequence input;

    /**
     * Holds the current index into the character sequence.
     */
    private int index;

    /**
     * Creates a new character sequence reader for which the character 
     * sequence input is not set.
     * 
     * @see #setInput
     */
    public CharSequenceReader() {}

    /**
     * Creates a new character sequence reader for which the character 
     * sequence input is set.
       */
    public CharSequenceReader(CharSequence input) {
    	this.input = input;
    }

    /**
     * Sets the character sequence to use for reading.
     *
     * @param input the character sequence to be read.
    * @throws IllegalStateException if this reader is being reused and 
     *         it has not been {@link #close closed} or {@link #reset reset}.
     */
    public void setInput(CharSequence input) {
        if (input != null)
            throw new IllegalStateException("Reader not closed or reset");
        this.input = input;
    }

    /**
     * Returns  the character sequence to use for reading.
     */
    public CharSequence getInput() {
        return input;
    }
    
    /**
     * Indicates if this stream is ready to be read.
     *
     * @return <code>true</code> if this reader has remaining characters to 
     *         read; <code>false</code> otherwise.
     * @throws  IOException if an I/O error occurs.
     */
    public boolean ready() throws IOException {
        if (input == null)
            throw new IOException("Reader closed");
        return true;
    }

    /**
     * Closes and {@link #reset resets} this reader for reuse.
     */
    public void close() {
        if (input != null) {
            reset();
        }
    }

    /**
     * Reads a single character.  This method does not block, <code>-1</code>
     * is returned if the end of the character sequence input has been reached.
     *
     * @return the 31-bits Unicode of the character read, or -1 if there is 
     *         no more remaining bytes to be read.
     * @throws IOException if an I/O error occurs (e.g. incomplete 
     *         character sequence being read).
     */
    public int read() throws IOException {
        if (input == null)
            throw new IOException("Reader closed");
        return (index < input.length()) ? input.charAt(index++) : -1;
    }

    /**
     * Reads characters into a portion of an array.  This method does not 
     * block.
     *
     * @param  cbuf the destination buffer.
     * @param  off the offset at which to start storing characters.
     * @param  len the maximum number of characters to read
     * @return the number of characters read,  or -1 if there is no more
     *         character to be read.
     * @throws IOException if an I/O error occurs.
     */
    public int read(char cbuf[], int off, int len) throws IOException {
        if (input == null)
            throw new IOException("Reader closed");
        final int inputLength = input.length();
        if (index >= inputLength)
            return -1;
        final int count = MathLib.min(inputLength - index, len);
        final Object csq = input;
        if (csq instanceof String) {
            String str = (String) csq;
            str.getChars(index, index + count, cbuf, off);
        } else if (csq instanceof Text) {
            Text txt = (Text) csq;
            txt.getChars(index, index + count, cbuf, off);
        } else if (csq instanceof TextBuilder) {
            TextBuilder tb = (TextBuilder) csq;
            tb.getChars(index, index + count, cbuf, off);
        } else if (csq instanceof CharArray) {
            CharArray ca = (CharArray) csq;
            System.arraycopy(ca.array(), index + ca.offset(), cbuf, off, count);
        } else { // Generic CharSequence.
            for (int i = off, n = off + count, j = index; i < n;) {
                cbuf[i++] = input.charAt(j++);
            }
        }
        index += count;
        return count;
    }

    /**
     * Reads characters into the specified appendable. This method does not 
     * block.
     *
     * @param  dest the destination buffer.
     * @throws IOException if an I/O error occurs.
     */
    public void read(Appendable dest) throws IOException {
        if (input == null)
            throw new IOException("Reader closed");
        dest.append(input);
    }

    @Override
    public void reset() {
        index = 0;
        input = null;
    }

}
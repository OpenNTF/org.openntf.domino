/**
 * 
 */
package org.openntf.domino.types;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * @author nfreeman
 * 
 */

public class BigString implements CharSequence, Serializable, Comparable<BigString> {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(BigString.class.getName());
	private static final long serialVersionUID = 1L;

	private String delegate_;

	/**
	 * 
	 */
	public BigString() {

	}

	public BigString(final String value) {
		delegate_ = value;
	}

	public String getDelegate() {
		if (delegate_ == null) {
			delegate_ = "";
		}
		return delegate_;
	}

	@Override
	public char charAt(final int index) {
		return getDelegate().charAt(index);
	}

	public int compareTo(final String string) {
		return getDelegate().compareTo(string);
	}

	public int compareToIgnoreCase(final String string) {
		return getDelegate().compareToIgnoreCase(string);
	}

	@Override
	public int compareTo(final BigString string) {
		return getDelegate().compareTo(string.getDelegate());
	}

	public int compareToIgnoreCase(final BigString string) {
		return getDelegate().compareToIgnoreCase(string.getDelegate());
	}

	public BigString concat(final String string) {
		delegate_ = getDelegate().concat(string);
		return this;
	}

	public boolean endsWith(final String suffix) {
		return getDelegate().endsWith(suffix);
	}

	@Override
	public boolean equals(final Object object) {
		return getDelegate().equals(object);
	}

	public boolean equalsIgnoreCase(final String string) {
		return getDelegate().equalsIgnoreCase(string);
	}

	public byte[] getBytes() {
		return getDelegate().getBytes();
	}

	@Deprecated
	public void getBytes(final int start, final int end, final byte[] data, final int index) {
		getDelegate().getBytes(start, end, data, index);
	}

	public byte[] getBytes(final String encoding) throws UnsupportedEncodingException {
		return getDelegate().getBytes(encoding);
	}

	public void getChars(final int start, final int end, final char[] buffer, final int index) {
		getDelegate().getChars(start, end, buffer, index);
	}

	@Override
	public int hashCode() {
		return getDelegate().hashCode();
	}

	public int indexOf(final int c) {
		return getDelegate().indexOf(c);
	}

	public int indexOf(final int c, final int start) {
		return getDelegate().indexOf(c, start);
	}

	public int indexOf(final String string) {
		return getDelegate().indexOf(string);
	}

	public int indexOf(final String subString, final int start) {
		return getDelegate().indexOf(subString, start);
	}

	public String intern() {
		return getDelegate().intern();
	}

	public int lastIndexOf(final int c) {
		return getDelegate().lastIndexOf(c);
	}

	public int lastIndexOf(final int c, final int start) {
		return getDelegate().lastIndexOf(c, start);
	}

	public int lastIndexOf(final String string) {
		return getDelegate().lastIndexOf(string);
	}

	public int lastIndexOf(final String subString, final int start) {
		return getDelegate().lastIndexOf(subString, start);
	}

	@Override
	public int length() {
		return getDelegate().length();
	}

	public boolean regionMatches(final int thisStart, final String string, final int start, final int length) {
		return getDelegate().regionMatches(thisStart, string, start, length);
	}

	public boolean regionMatches(final boolean ignoreCase, final int thisStart, final String string, final int start, final int length) {
		return getDelegate().regionMatches(ignoreCase, thisStart, string, start, length);
	}

	public BigString replace(final char oldChar, final char newChar) {
		delegate_ = getDelegate().replace(oldChar, newChar);
		return this;
	}

	public boolean startsWith(final String prefix) {
		return getDelegate().startsWith(prefix);
	}

	public boolean startsWith(final String prefix, final int start) {
		return getDelegate().startsWith(prefix, start);
	}

	public BigString substring(final int start) {
		delegate_ = getDelegate().substring(start);
		return this;
	}

	public BigString substring(final int start, final int end) {
		delegate_ = getDelegate().substring(start, end);
		return this;
	}

	public char[] toCharArray() {
		return getDelegate().toCharArray();
	}

	public String toLowerCase() {
		delegate_ = getDelegate().toLowerCase();
		return getDelegate();
	}

	public BigString toLowerCase(final Locale locale) {
		delegate_ = getDelegate().toLowerCase(locale);
		return this;
	}

	@Override
	public String toString() {
		return getDelegate();
	}

	public BigString toUpperCase() {
		delegate_ = getDelegate().toUpperCase();
		return this;
	}

	public BigString toUpperCase(final Locale locale) {
		delegate_ = getDelegate().toUpperCase(locale);
		return this;
	}

	public BigString trim() {
		delegate_ = getDelegate().trim();
		return this;
	}

	public boolean contentEquals(final StringBuffer strbuf) {
		return getDelegate().contentEquals(strbuf);
	}

	public boolean matches(final String expr) {
		return getDelegate().matches(expr);
	}

	public BigString replaceAll(final String expr, final String substitute) {
		delegate_ = getDelegate().replaceAll(expr, substitute);
		return this;
	}

	public BigString replaceFirst(final String expr, final String substitute) {
		delegate_ = getDelegate().replaceFirst(expr, substitute);
		return this;
	}

	public String[] split(final String expr) {
		return getDelegate().split(expr);
	}

	public String[] split(final String expr, final int max) {
		return getDelegate().split(expr, max);
	}

	@Override
	public CharSequence subSequence(final int start, final int end) {
		delegate_ = getDelegate().substring(start, end);
		return this;
	}

	public int codePointAt(final int index) {
		return getDelegate().codePointAt(index);
	}

	public int codePointBefore(final int index) {
		return getDelegate().codePointBefore(index);
	}

	public int codePointCount(final int start, final int end) {
		return getDelegate().codePointCount(start, end);
	}

	public int offsetByCodePoints(final int start, final int codePointCount) {
		return getDelegate().offsetByCodePoints(start, codePointCount);
	}

	public boolean contentEquals(final CharSequence buffer) {
		return getDelegate().contentEquals(buffer);
	}

	public boolean contains(final CharSequence sequence) {
		return getDelegate().contains(sequence);
	}

	public BigString replace(final CharSequence sequence1, final CharSequence sequence2) {
		delegate_ = getDelegate().replace(sequence1, sequence2);
		return this;
	}

	public boolean isEmpty() {
		return getDelegate().isEmpty();
	}

	public byte[] getBytes(final Charset aCharset) {
		return getDelegate().getBytes(aCharset);
	}

}

/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 *
 */
package org.openntf.domino.types;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Nathan T. Freeman Much source code shamelessly stolen from com.ibm.icu.util.CaseInsensitiveString. We would have properly
 *         extended it, but folded is private in IBM's implementation :-/
 *
 */
public class CaseInsensitiveString implements CharSequence, Comparable<CharSequence>, Externalizable {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(CaseInsensitiveString.class.getName());
	private static final long serialVersionUID = 1L;

	protected String string;
	protected int hash = 0;
	protected String folded = null;

	public static CharSequence toCaseInsensitive(final CharSequence chars) {
		if (chars == null) {
			return null;
		}
		return new CaseInsensitiveString(chars);
	}

	public static List<CharSequence> toCaseInsensitive(final Iterable<? extends CharSequence> strings) {
		if (strings == null) {
			return null;
		}
		List<CharSequence> result = new ArrayList<CharSequence>();
		for (CharSequence str : strings) {
			result.add(new CaseInsensitiveString(str));
		}
		return result;
	}

	public static List<String> toStrings(final Iterable<CharSequence> cis) {
		if (cis == null) {
			return null;
		}
		List<String> result = new ArrayList<String>();
		for (CharSequence str : cis) {
			result.add(str.toString());
		}
		return result;
	}

	public static String toString(final Iterable<CaseInsensitiveString> cis) {
		String result = "";
		for (CaseInsensitiveString str : cis) {
			result = result + (result.length() > 0 ? ", " : "") + str.toString();
		}
		return result;
	}

	public String getFolded() {
		return folded;
	}

	/**
	 * Constructs an CaseInsentiveString object from the given string
	 *
	 * @param s
	 *            The string to construct this object from
	 */
	public CaseInsensitiveString(final CharSequence s) {
		string = String.valueOf(s);
		folded = string.toLowerCase();
	}

	public CaseInsensitiveString() {	//used for Externalization

	}

	/**
	 * returns the underlying string
	 *
	 * @return String
	 * @stable ICU 2.0
	 */
	public String getString() {
		return string;
	}

	/**
	 * Compare the object with this
	 *
	 * @param o
	 *            Object to compare this object with
	 */
	@Override
	public boolean equals(final Object o) {
		if (o == null) {
			return false;
		}
		if (this == o) {
			return true;
		}
		return string.equalsIgnoreCase(String.valueOf(o));
		//		getFolded();
		//		try {
		//			CaseInsensitiveString cis = (CaseInsensitiveString) o;
		//			cis.getFolded();
		//			return folded.equals(cis.folded);
		//		} catch (ClassCastException e) {
		//			try {
		//				String s = (String) o;
		//				return folded.equals(foldCase(s));
		//			} catch (ClassCastException e2) {
		//				return false;
		//			}
		//		}
	}

	/**
	 * Returns the hashCode of this object
	 *
	 * @return int hashcode
	 */
	@Override
	public int hashCode() {
		getFolded();

		if (hash == 0) {
			hash = folded.hashCode();
		}

		return hash;
	}

	/**
	 * Overrides superclass method
	 */
	@Override
	public String toString() {
		return string;
	}

	@Override
	public int compareTo(final CharSequence o) {
		if (o == null) {
			throw new IllegalArgumentException("Cannot compare to null");
		}
		if (this == o) {
			return 0;
		}
		getFolded();
		try {
			CaseInsensitiveString cis = (CaseInsensitiveString) o;
			cis.getFolded();
			return folded.compareTo(cis.folded);
		} catch (ClassCastException e) {
			try {
				String s = o.toString();
				return folded.compareTo(s.toLowerCase());
			} catch (ClassCastException e2) {
				throw new IllegalArgumentException("Cannot compare an object of type " + (o == null ? "null" : o.getClass().getName()));

			}
		}
	}

	public boolean startsWith(final Object arg0) {
		if (arg0 instanceof CaseInsensitiveString) {
			return getFolded().startsWith(((CaseInsensitiveString) arg0).getFolded());
		} else if (arg0 instanceof String) {
			return getFolded().startsWith(arg0.toString().toLowerCase());
		} else {
			return getFolded().startsWith(String.valueOf(arg0).toLowerCase());
		}
	}

	@Override
	public int length() {
		return string.length();
	}

	@Override
	public void readExternal(final ObjectInput arg0) throws IOException, ClassNotFoundException {
		string = arg0.readUTF();
	}

	@Override
	public void writeExternal(final ObjectOutput arg0) throws IOException {
		arg0.writeUTF(string);
	}

	@Override
	public char charAt(final int index) {
		return string.charAt(index);
	}

	@Override
	public CharSequence subSequence(final int start, final int end) {
		return new CaseInsensitiveString(string.subSequence(start, end));
	}
}

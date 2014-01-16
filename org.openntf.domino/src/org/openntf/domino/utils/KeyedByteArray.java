/*
 * Copyright 2013
 * 
 * @author Devin S. Olson (dolson@czarnowski.com)
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
 *
 */
package org.openntf.domino.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.activation.MimetypesFileTypeMap;

import org.openntf.domino.EmbeddedObject;

/**
 * Serializable Carrier class for an array of byte values, incorporating meta information using HashMap objects.
 * 
 * Objects of this class can be used for a multitude of purposes, but are <strong>idealy</strong> suited storage and transfer of File
 * Attachments read from or to be written to domino EmbeddedObjects.
 * 
 * @author Devin S. Olson dolson@czarnowski.com
 * 
 * @updated 01/2014
 */
public class KeyedByteArray extends StringMappedSerializables implements Comparable<KeyedByteArray> {

	public static enum Key {
		FileName, ContentType, FileSize, ObjectName, ItemName, CID
	};

	private static final long serialVersionUID = 1L;
	private byte[] Bytes;

	/**
	 * Default Constructor
	 */
	public KeyedByteArray() {
		super();
		this.setCID("");
	}

	/**
	 * Optional Constructor
	 * 
	 * @param cid
	 *            Unique identifier for this object. Note that this is NOT a UUID, Uniqueness of this ID is controlled by implementing code
	 *            external to this object.
	 */
	public KeyedByteArray(final String cid) {
		super();
		this.setCID(cid);
	}

	/**
	 * Optional Constructor
	 * 
	 * @param cid
	 *            Unique identifier for this object. Note that this is NOT a UUID, Uniqueness of this ID is controlled by implementing code
	 *            external to this object.
	 * 
	 * @param embeddedobject
	 *            Source from which to read the byte stream
	 */
	public KeyedByteArray(final String cid, final EmbeddedObject embeddedobject) {
		super();
		this.setCID(cid);
		this.setBytes(embeddedobject);
	}

	/**
	 * Optional Constructor
	 * 
	 * @param cid
	 *            Unique identifier for this object. Note that this is NOT a UUID, Uniqueness of this ID is controlled by implementing code
	 *            external to this object.
	 * 
	 * @param embeddedobject
	 *            Source from which to read the byte stream
	 */
	public KeyedByteArray(final String cid, final lotus.domino.EmbeddedObject embeddedobject) {
		super();
		this.setCID(cid);
		this.setBytes(embeddedobject);
	}

	/*
	 * ******************************************************************
	 * ******************************************************************
	 * 
	 * Serializable getters & setters
	 * 
	 * ******************************************************************
	 * ******************************************************************
	 */

	/**
	 * Gets the Bytes
	 * 
	 * @return Array of bytes for the object.
	 */
	public byte[] getBytes() {
		return this.Bytes;
	}

	/**
	 * Sets the Bytes
	 * 
	 * @param bytes
	 *            Array of bytes for the object.
	 */
	public void setBytes(final byte[] bytes) {
		this.Bytes = bytes;
	}

	/*
	 * ******************************************************************
	 * ******************************************************************
	 * 
	 * Other Methods
	 * 
	 * ******************************************************************
	 * ******************************************************************
	 */
	/**
	 * Gets the Common Identifier
	 * 
	 * @return Unique identifier for this object. Note that this is NOT a UUID, Uniqueness of this ID is controlled by implementing code
	 *         external to this object.
	 */
	public String getCID() {
		return this.getString(KeyedByteArray.Key.CID);
	}

	/**
	 * Sets the Common Identifier
	 * 
	 * @param cid
	 *            Unique identifier for this object. Note that this is NOT a UUID, Uniqueness of this ID is controlled by implementing code
	 *            external to this object.
	 */
	public void setCID(final String cid) {
		this.put(KeyedByteArray.Key.CID, (Strings.isBlankString(cid)) ? UUID.randomUUID().toString() : cid);
	}

	/**
	 * Number of elements in the Byte array.
	 */
	public int length() {
		return (null == this.Bytes) ? 0 : this.getBytes().length;
	}

	/**
	 * Gets a String for the specified key.
	 * 
	 * Returns the String to which the specified key is mapped, or "" if the Strings map contains no mapping for the key.
	 * 
	 * @param key
	 *            the key whose associated value is to be returned
	 * 
	 * @return the String to which the specified key is mapped, or "" if the Strings map contains no mapping for the key
	 * 
	 */
	public String getString(final KeyedByteArray.Key key) {
		try {
			if (null == key) {
				throw new IllegalArgumentException("Key is null");
			}

			return this.getString(key.name());

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return "";
	}

	/**
	 * Gets a Integer for the specified key.
	 * 
	 * Returns the Integer to which the specified key is mapped, or 0 if the Integers map contains no mapping for the key.
	 * 
	 * @param key
	 *            the key whose associated value is to be returned
	 * 
	 * @return the Integer to which the specified key is mapped, or 0 if the Integers map contains no mapping for the key
	 * 
	 */
	public Integer getInteger(final KeyedByteArray.Key key) {
		try {
			if (null == key) {
				throw new IllegalArgumentException("Key is null");
			}

			return this.getInteger(key.name());

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return 0;
	}

	/**
	 * Associates the specified String with the specified key in the Strings map. If the map previously contained a mapping for the key, the
	 * old value is replaced.
	 * 
	 * @param key
	 *            key with which the specified value is to be associated
	 * @param value
	 *            value to be associated with the specified key
	 * @return the previous value associated with key, or null if there was no mapping for key.
	 */
	public String put(final KeyedByteArray.Key key, final String value) {
		try {
			if (null == key) {
				throw new IllegalArgumentException("Key is null");
			}
			return this.putString(key.name(), value);
		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return null;
	}

	/**
	 * Associates the specified Integer with the specified key in the Integers map. If the map previously contained a mapping for the key,
	 * the old value is replaced.
	 * 
	 * @param key
	 *            key with which the specified value is to be associated
	 * @param value
	 *            value to be associated with the specified key
	 * @return the previous value associated with key, or null if there was no mapping for key.
	 */
	public Integer put(final KeyedByteArray.Key key, final Integer value) {
		try {
			if (null == key) {
				throw new IllegalArgumentException("Key is null");
			}
			return this.putInteger(key.name(), value);
		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return null;
	}

	/**
	 * Gets the Filename
	 * 
	 * @return Filename for the object.
	 */
	public String getFileName() {
		return this.getString(KeyedByteArray.Key.FileName);
	}

	/**
	 * Sets the Filename
	 * 
	 * @param filename
	 *            Filename for the object.
	 */
	public void setFileName(final String filename) {
		this.put(KeyedByteArray.Key.FileName, filename);
	}

	/**
	 * Gets the Content Type
	 * 
	 * @return Content Type for the object.
	 */
	public String getContentType() {
		return this.getString(KeyedByteArray.Key.ContentType);
	}

	/**
	 * Sets the Content Type
	 * 
	 * @param contenttype
	 *            Content Type for the object.
	 */
	public void setContentType(final String contenttype) {
		this.put(KeyedByteArray.Key.ContentType, contenttype);
	}

	/**
	 * Gets the Objectname
	 * 
	 * @return Objectname for the object.
	 */
	public String getObjectName() {
		return this.getString(KeyedByteArray.Key.ObjectName);
	}

	/**
	 * Sets the Objectname
	 * 
	 * @param objectname
	 *            Objectname for the object.
	 */
	public void setObjectName(final String objectname) {
		this.put(KeyedByteArray.Key.ObjectName, objectname);
	}

	/**
	 * Gets the Itemname
	 * 
	 * @return Itemname for the object.
	 */
	public String getItemName() {
		return this.getString(KeyedByteArray.Key.ItemName);
	}

	/**
	 * Sets the Itemname
	 * 
	 * @param itemname
	 *            Itemname for the object.
	 */
	public void setItemName(final String itemname) {
		this.put(KeyedByteArray.Key.ItemName, itemname);
	}

	/**
	 * Gets the Filesize
	 * 
	 * @return Filesize for the object.
	 */
	public int getFileSize() {
		return this.getInteger(KeyedByteArray.Key.FileSize);
	}

	/**
	 * Sets the Filesize
	 * 
	 * @param filesize
	 *            Filesize for the object.
	 */
	public void setFileSize(final int filesize) {
		this.put(KeyedByteArray.Key.FileSize, filesize);
	}

	/**
	 * Sets the Bytes by reading from an InputStream
	 * 
	 * @param input
	 *            InputStream from which the byte array will be read
	 */
	public boolean setBytes(final InputStream input) {
		try {
			if (null == input) {
				throw new IllegalArgumentException("InputStream is null");
			}

			this.setBytes(KeyedByteArray.getBytesFromInputStream(input));
			return (null != this.Bytes);

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/**
	 * Sets the Bytes by reading from an embeddedobject
	 * 
	 * @param input
	 *            Embedded Object from which the byte array will be read
	 */
	public boolean setBytes(final EmbeddedObject embeddedobject) {
		try {
			if (null == embeddedobject) {
				throw new IllegalArgumentException("EmbeddedObject is null");
			}

			final InputStream is = embeddedobject.getInputStream();

			if (this.setBytes(is)) {
				final String filename = embeddedobject.getSource();
				this.setFileName(filename);
				this.setObjectName(embeddedobject.getName());
				this.setFileSize(embeddedobject.getFileSize());
				this.setContentType(KeyedByteArray.getContentType(filename));
				is.close();
				return true;
			}

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return false;
	}

	/**
	 * Sets the Bytes by reading from an embeddedobject
	 * 
	 * @param input
	 *            Embedded Object from which the byte array will be read
	 */
	public boolean setBytes(final lotus.domino.EmbeddedObject embeddedobject) {
		try {
			if (null == embeddedobject) {
				throw new IllegalArgumentException("EmbeddedObject is null");
			}

			final InputStream is = embeddedobject.getInputStream();

			if (this.setBytes(is)) {
				final String filename = embeddedobject.getSource();
				this.setFileName(filename);
				this.setObjectName(embeddedobject.getName());
				this.setFileSize(embeddedobject.getFileSize());
				this.setContentType(KeyedByteArray.getContentType(filename));
				is.close();
				return true;
			}

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return false;
	}

	/*
	 * ******************************************************************
	 * ******************************************************************
	 * 
	 * Static Methods
	 * 
	 * ******************************************************************
	 * ******************************************************************
	 */

	/**
	 * Gets an array of bytes from a specified InputStream
	 * 
	 * @param input
	 *            InputStream from which to get the array of bytes.
	 * 
	 * @return array of bytes from the InputStream. Null on error.
	 */
	public static byte[] getBytesFromInputStream(final InputStream input) {
		try {
			final byte[] buffer = new byte[8192];
			int bytesRead;
			final ByteArrayOutputStream output = new ByteArrayOutputStream();
			if (input.available() > 0) {
				while ((bytesRead = input.read(buffer)) != -1) {
					output.write(buffer, 0, bytesRead);
				}

				return output.toByteArray();
			}

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return null;
	}

	/**
	 * Gets the content type for a given filename, if known.
	 * 
	 * @param filename
	 *            Name to check for the content type.
	 * 
	 * @return Content type for the filename. Empty string "" if not known.
	 */
	public static String getContentType(final String filename) {
		return (Strings.isBlankString(filename)) ? "" : new MimetypesFileTypeMap().getContentType(filename);
	}

	/*
	 * ******************************************************************
	 * ******************************************************************
	 * 
	 * hashcode, equals, and comparison methods
	 * 
	 * ******************************************************************
	 * ******************************************************************
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		return (obj instanceof KeyedByteArray) ? true : false;
	}

	/**
	 * Compares this object with another KeyedByteArray
	 * 
	 * @param handle
	 *            KeyedByteArray object to be compared.
	 * 
	 * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
	 * 
	 * 
	 * @see java.lang.Comparable#compareTo(Object)
	 * @see org.openntf.domino.utils.DominoUtils#LESS_THAN
	 * @see org.openntf.domino.utils.DominoUtils#EQUAL
	 * @see org.openntf.domino.utils.DominoUtils#GREATER_THAN
	 */
	public int compareTo(final KeyedByteArray handle) {
		return KeyedByteArray.compare(this, handle);
	}

	/**
	 * Default Comparable implementation. (Natural Comparison Method)
	 * 
	 * KeyedByteArray objects are compared as follows:
	 * <ol>
	 * <li>Equality using .equals() method</li>
	 * <li>CID</li>
	 * <li>length</li>
	 * <li>Integers (HashMap Keys and Values)</li>
	 * <li>Strings (HashMap Keys and Values)</li>
	 * <li>HashCode</li>
	 * </ol>
	 * 
	 * @param handle0
	 *            First KeyedByteArray object for comparison.
	 * 
	 * @param handle1
	 *            Second KeyedByteArray object for comparison.
	 * 
	 * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
	 * 
	 * 
	 * @see java.lang.Comparable#compareTo(Object)
	 * @see org.openntf.domino.utils.DominoUtils#LESS_THAN
	 * @see org.openntf.domino.utils.DominoUtils#EQUAL
	 * @see org.openntf.domino.utils.DominoUtils#GREATER_THAN
	 */
	public static int compare(final KeyedByteArray handle0, final KeyedByteArray handle1) {
		if (null == handle0) {
			return (null == handle1) ? DominoUtils.EQUAL : DominoUtils.LESS_THAN;
		} else if (null == handle1) {
			return DominoUtils.GREATER_THAN;
		}
		if (handle0.equals(handle1)) {
			return DominoUtils.EQUAL;
		}

		int result = handle0.getCID().compareTo(handle1.getCID());

		if (result == DominoUtils.EQUAL) {
			// compare lengths
			int int0 = handle0.length();
			int int1 = handle1.length();
			result = (int0 < int1) ? DominoUtils.LESS_THAN : (int1 < int0) ? DominoUtils.GREATER_THAN : DominoUtils.EQUAL;

			if (result == DominoUtils.EQUAL) {
				// compare Integers
				final HashMap<Serializable, Integer> integers0 = handle0.getIntegers();
				final HashMap<Serializable, Integer> integers1 = handle1.getIntegers();

				// compare # of Integer elements
				result = (integers0.size() < integers1.size()) ? DominoUtils.LESS_THAN
						: (integers1.size() < integers0.size()) ? DominoUtils.GREATER_THAN : DominoUtils.EQUAL;

				if (result == DominoUtils.EQUAL) {
					// compare Integer values
					final Iterator<Map.Entry<Serializable, Integer>> iit0 = integers0.entrySet().iterator();
					while (iit0.hasNext()) {
						final Map.Entry<Serializable, Integer> entry = iit0.next();
						if (!integers1.containsKey(entry.getKey())) {
							return DominoUtils.LESS_THAN;
						}
						result = handle0.getInteger(entry.getKey()).compareTo(handle1.getInteger(entry.getKey()));
						if (result != DominoUtils.EQUAL) {
							return result;
						}
					}

					final Iterator<Map.Entry<Serializable, Integer>> iit1 = integers0.entrySet().iterator();
					while (iit1.hasNext()) {
						final Map.Entry<Serializable, Integer> entry = iit1.next();
						if (!integers0.containsKey(entry.getKey())) {
							return DominoUtils.LESS_THAN;
						}
						result = handle0.getInteger(entry.getKey()).compareTo(handle1.getInteger(entry.getKey()));
						if (result != DominoUtils.EQUAL) {
							return result;
						}
					}

					if (result == DominoUtils.EQUAL) {
						// compare Strings
						final HashMap<Serializable, String> strings0 = handle0.getStrings();
						final HashMap<Serializable, String> strings1 = handle1.getStrings();

						// compare # of String elements
						result = (strings0.size() < strings1.size()) ? DominoUtils.LESS_THAN
								: (strings1.size() < strings0.size()) ? DominoUtils.GREATER_THAN : DominoUtils.EQUAL;

						if (result == DominoUtils.EQUAL) {
							// compare String values
							final Iterator<Map.Entry<Serializable, String>> sit0 = strings0.entrySet().iterator();
							while (sit0.hasNext()) {
								final Map.Entry<Serializable, String> entry = sit0.next();
								if (!strings1.containsKey(entry.getKey())) {
									return DominoUtils.LESS_THAN;
								}
								result = handle0.getString(entry.getKey()).compareTo(handle1.getString(entry.getKey()));
								if (result != DominoUtils.EQUAL) {
									return result;
								}
							}

							final Iterator<Map.Entry<Serializable, String>> sit1 = strings1.entrySet().iterator();
							while (sit1.hasNext()) {
								final Map.Entry<Serializable, String> entry = sit1.next();
								if (!strings0.containsKey(entry.getKey())) {
									return DominoUtils.LESS_THAN;
								}
								result = handle0.getString(entry.getKey()).compareTo(handle1.getString(entry.getKey()));
								if (result != DominoUtils.EQUAL) {
									return result;
								}
							}

						}
					}

					if (result == DominoUtils.EQUAL) {
						// compare hashCodes
						int0 = handle0.hashCode();
						int1 = handle1.hashCode();
						result = (int0 < int1) ? DominoUtils.LESS_THAN : (int1 < int0) ? DominoUtils.GREATER_THAN : DominoUtils.EQUAL;

					}
				}
			}
		}

		return result;
	}

} // KeyedByteArray
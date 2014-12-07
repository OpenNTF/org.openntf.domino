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

import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.Document;
import org.openntf.domino.iterators.DocumentList;

/**
 * CollectionUtils (Sets and Lists) utilities library
 * 
 * @author Devin S. Olson (dolson@czarnowski.com)
 */
public enum CollectionUtils {
	;

	/**
	 * Gets or generates an List of Strings from an Item on a Document
	 * 
	 * @param source
	 *            Document from which to get or generate the result.
	 * 
	 * @param itemname
	 *            Name of item from which to get the content
	 * 
	 * @return List of Strings retrieved or generated from the input. Returns null if the document does not contain the item.
	 * 
	 * @throws IllegalArgumentException
	 *             if source document is null or itemname is blank or null.
	 */
	public static List<String> getListStrings(final Document source, final String itemname) {
		if (null == source) {
			throw new IllegalArgumentException("Source document is null");
		}
		if (Strings.isBlankString(itemname)) {
			throw new IllegalArgumentException("ItemName is blank or null");
		}

		return (source.hasItem(itemname)) ? CollectionUtils.getListStrings(source.getItemValue(itemname)) : null;
	}

	/**
	 * Gets or generates an List of Strings from a Vector
	 * 
	 * @param vector
	 *            Vector from which to get or generate the result.
	 * 
	 * @return List of Strings retrieved or generated from the input. Returns null on error.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<String> getListStrings(final Vector vector) {
		return (null == vector) ? null : Collections.list(vector.elements());
	}

	/**
	 * Gets or generates an List of Strings from an AbstractCollection
	 * 
	 * @param collection
	 *            AbstractCollection from which to get or generate the result.
	 * 
	 * @return List of Strings retrieved or generated from the input. Returns null on error.
	 */
	@SuppressWarnings({ "rawtypes", "cast" })
	public static List<String> getListStrings(final AbstractCollection collection) {
		if ((null != collection) && (collection.size() > 0)) {
			final List<String> result = new ArrayList<String>();
			if (collection.iterator().next() instanceof Object) {
				// treat as an object
				for (final Object o : collection) {
					if (null != o) {
						result.add(o.toString());
					}
				}

			} else {
				// treat as a primitive
				final Iterator it = collection.iterator();
				while (it.hasNext()) {
					result.add(String.valueOf(it.next()));
				}
			}

			return result;
		}

		return null;
	}

	/**
	 * Gets or generates an List of Strings from an AbstractMap
	 * 
	 * @param map
	 *            AbstractMap from which to get or generate the result. Only the Values will be retrieved, the Keys will are ignored.
	 * 
	 * @return List of Strings retrieved or generated from the input. Returns null on error.
	 */
	@SuppressWarnings("rawtypes")
	public static List<String> getListStrings(final AbstractMap map) {
		return ((null != map) && (map.size() > 0)) ? CollectionUtils.getListStrings(map.values()) : null;
	}

	/**
	 * Gets or generates an List of Strings from a String
	 * 
	 * @param string
	 *            String from which to get or generate the result.
	 * 
	 * @return List of Strings retrieved or generated from the input. Returns null on error.
	 */
	public static List<String> getListStrings(final String string) {
		if (!Strings.isBlankString(string)) {
			final List<String> result = new ArrayList<String>();
			result.add(string);
			return result;
		}

		return null;
	}

	/**
	 * Gets or generates an List of Strings from a array of Strings
	 * 
	 * @param strings
	 *            Array of Strings from which to get or generate the result.
	 * 
	 * @return List of Strings retrieved or generated from the input. Returns null on error.
	 */
	public static List<String> getListStrings(final String[] strings) {
		if ((null != strings) && (strings.length > 0)) {
			final List<String> result = new ArrayList<String>();
			for (final String s : strings) {
				result.add(s);
			}
			return result;
		}

		return null;
	}

	/**
	 * Gets or generates an List of Strings from an Object
	 * 
	 * @param object
	 *            Object from which to get or generate the result. Attempts to retrieve the values from the object.
	 * 
	 * @return List of Strings retrieved or generated from the input. Returns null on error.
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public static List<String> getListStrings(final Object object) {
		String classname = null;
		try {
			if (null != object) {
				classname = object.getClass().getName();

				if (object instanceof Vector) {
					return CollectionUtils.getListStrings((Vector) object);
				}
				if (object instanceof AbstractCollection) {
					return CollectionUtils.getListStrings((AbstractCollection) object);
				}
				if (object instanceof AbstractMap) {
					return CollectionUtils.getListStrings((AbstractMap) object);
				}
				if (object instanceof String) {
					return CollectionUtils.getListStrings((String) object);
				}
				if (object instanceof String[]) {
					return CollectionUtils.getListStrings((String[]) object);
				}
				if (classname.equalsIgnoreCase("java.lang.String[]") || classname.equalsIgnoreCase("[Ljava.lang.String;")) {
					return CollectionUtils.getListStrings((String[]) object);
				}
				if (classname.equalsIgnoreCase("java.lang.String")) {
					return CollectionUtils.getListStrings((String) object);
				}

				throw new IllegalArgumentException("Unsupported Class:" + classname);
			}
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}

		return null;
	}

	/**
	 * Gets or generates a TreeSet of Strings from an Object
	 * 
	 * @param object
	 *            Object from which to get or generate the result. Attempts to retrieve the string values from the object.
	 * 
	 * @return TreeSet of Strings retrieved or generated from the input. Returns null on error.
	 * 
	 */
	public static TreeSet<String> getTreeSetStrings(final Object object) {
		final List<String> al = CollectionUtils.getListStrings(object);
		return (null == al) ? null : new TreeSet<String>(al);
	}

	/**
	 * Gets or generates an Array of Strings from an Object
	 * 
	 * @param object
	 *            Object from which to get or generate the result. Attempts to retrieve the string values from the object.
	 * 
	 * @return Array of Strings retrieved or generated from the input. Returns null on error.
	 * 
	 */
	public static String[] getStringArray(final Object object) {
		final List<String> al = CollectionUtils.getListStrings(object);
		return (null == al) ? null : al.toArray(new String[al.size()]);
	}

	/**
	 * Gets or generates an Array of Strings from an Object
	 * 
	 * Result array will contain only unique values and will be sorted according to the String object's natural sorting method
	 * 
	 * @param object
	 *            Object from which to get or generate the result. Attempts to retrieve the string values from the object.
	 * 
	 * @return Array of Strings retrieved or generated from the input. Returns null on error.
	 * 
	 * @see java.lang.String#compareTo(String)
	 * 
	 */
	public static String[] getSortedUnique(final Object object) {
		final TreeSet<String> ts = CollectionUtils.getTreeSetStrings(object);
		return ((null == ts) || (ts.size() < 1)) ? null : ts.toArray(new String[ts.size()]);
	}

	/**
	 * Compares two String[] objects
	 * 
	 * Arguments are first compared by existence, then by # of elements, then by values.
	 * 
	 * @param stringarray0
	 *            First String[] to compare.
	 * 
	 * @param stringarray1
	 *            Second String[] to compare.
	 * 
	 * @param descending
	 *            flags indicating comparison order. true = descending, false = ascending.
	 * 
	 * @return a negative integer, zero, or a positive integer indicating if the first object is less than, equal to, or greater than the
	 *         second object.
	 * 
	 * @see java.lang.Comparable#compareTo(Object)
	 * @see DominoUtils#LESS_THAN
	 * @see DominoUtils#EQUAL
	 * @see DominoUtils#GREATER_THAN
	 */
	public static int compareStringArrays(final String[] stringarray0, final String[] stringarray1, final boolean descending) {
		if (null == stringarray0) {
			return (null == stringarray1) ? DominoUtils.EQUAL : (descending) ? DominoUtils.GREATER_THAN : DominoUtils.LESS_THAN;
		} else if (null == stringarray1) {
			return (descending) ? DominoUtils.LESS_THAN : DominoUtils.GREATER_THAN;
		}

		if (stringarray0.length < stringarray1.length) {
			return (descending) ? DominoUtils.GREATER_THAN : DominoUtils.LESS_THAN;
		}
		if (stringarray1.length < stringarray0.length) {
			return (descending) ? DominoUtils.LESS_THAN : DominoUtils.GREATER_THAN;
		}

		for (int i = 0; i < stringarray0.length; i++) {
			final int result = stringarray0[i].compareTo(stringarray1[i]);
			if (DominoUtils.EQUAL != result) {
				return (descending) ? -result : result;
			}
		}

		return DominoUtils.EQUAL;
	}

	/**
	 * Compares two TreeSet<String> objects
	 * 
	 * Arguments are first compared by existence, then by size, then by values.
	 * 
	 * @param treeset0
	 *            First TreeSet<String> to compare.
	 * 
	 * @param treeset1
	 *            Second TreeSet<String> to compare.
	 * 
	 * @param descending
	 *            flags indicating comparison order. true = descending, false = ascending.
	 * 
	 * @return a negative integer, zero, or a positive integer indicating if the first object is less than, equal to, or greater than the
	 *         second object.
	 * 
	 * @see java.lang.Comparable#compareTo(Object)
	 * @see DominoUtils#LESS_THAN
	 * @see DominoUtils#EQUAL
	 * @see DominoUtils#GREATER_THAN
	 */
	public static int compareTreeSetStrings(final TreeSet<String> treeset0, final TreeSet<String> treeset1, final boolean descending) {
		if (null == treeset0) {
			return (null == treeset1) ? DominoUtils.EQUAL : (descending) ? DominoUtils.GREATER_THAN : DominoUtils.LESS_THAN;
		} else if (null == treeset1) {
			return (descending) ? DominoUtils.LESS_THAN : DominoUtils.GREATER_THAN;
		}

		if (treeset0.size() < treeset1.size()) {
			return (descending) ? DominoUtils.GREATER_THAN : DominoUtils.LESS_THAN;
		}
		if (treeset1.size() < treeset0.size()) {
			return (descending) ? DominoUtils.LESS_THAN : DominoUtils.GREATER_THAN;
		}

		// Compare as string arrays
		return CollectionUtils.compareStringArrays(CollectionUtils.getStringArray(treeset0), CollectionUtils.getStringArray(treeset1),
				descending);
	}

	/**
	 * Gets or generates a TreeSet containing Strings found in the source which begin with the prefix.
	 * 
	 * Performs a case-insensitive search.
	 * 
	 * @param source
	 *            Object from which to attempt to extract and match the strings.
	 * 
	 * @param prefix
	 *            String which each extracted string must begin with.
	 * 
	 * @return Strings found within source which begin with prefix.
	 * 
	 * @see Strings#startsWithIgnoreCase(String, String)
	 */
	public static TreeSet<String> getTreeSetStringsBeginsWith(final Object source, final String prefix) {
		final TreeSet<String> temp = CollectionUtils.getTreeSetStrings(source);
		if ((null != temp) && (temp.size() > 0)) {
			final TreeSet<String> result = new TreeSet<String>();
			for (final String s : temp) {
				if (Strings.startsWithIgnoreCase(s, prefix)) {
					result.add(s);
				}
			}

			return (result.size() > 0) ? result : null;
		}

		return null;
	}

	/**
	 * Convert a Document collection to Notes Collection
	 * 
	 * @param collection
	 * @return
	 */
	public static org.openntf.domino.NoteCollection xtoLotusNoteCollection(final lotus.domino.DocumentCollection collection) {
		org.openntf.domino.NoteCollection result = null;
		if (collection instanceof org.openntf.domino.DocumentCollection) {
			org.openntf.domino.Database db = ((org.openntf.domino.DocumentCollection) collection).getAncestorDatabase();
			result = db.createNoteCollection(false);
			result.add(collection);
		} else if (collection != null) {
			// TODO Eh?
			org.openntf.domino.Database db = ((org.openntf.domino.DocumentCollection) collection).getAncestorDatabase();
			result = db.createNoteCollection(false);
			result.add(collection);
		}
		return result;
	}

	/**
	 * Returns the Note IDs of the given (Notes) collection
	 * 
	 * @param collection
	 *            the DocumentCollection
	 * @return a array of NoteIDs
	 */
	public static int[] getNoteIDs(final lotus.domino.DocumentCollection collection) {
		int[] result = null;
		try {
			if (collection instanceof DocumentList) {
				result = ((DocumentList) collection).getNids();
			} else if (collection.isSorted()) {
				if (collection instanceof org.openntf.domino.DocumentCollection) {
					org.openntf.domino.DocumentCollection ocoll = (org.openntf.domino.DocumentCollection) collection;
					int size = ocoll.getCount();
					result = new int[size];
					int i = 0;
					for (org.openntf.domino.Document doc : ocoll) {
						result[i++] = Integer.valueOf(doc.getNoteID(), 16);
					}
				} else {
					int size = collection.getCount();
					result = new int[size];
					lotus.domino.Document doc = collection.getFirstDocument();
					lotus.domino.Document next = null;
					int i = 0;
					while (doc != null) {
						next = collection.getNextDocument(doc);
						result[i++] = Integer.valueOf(doc.getNoteID(), 16);
						doc.recycle();
						doc = next;
					}
				}
			} else {
				lotus.domino.Database db = collection.getParent();
				lotus.domino.NoteCollection nc = db.createNoteCollection(false);
				result = nc.getNoteIDs();
				nc.recycle();
			}
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return result;
	}
}

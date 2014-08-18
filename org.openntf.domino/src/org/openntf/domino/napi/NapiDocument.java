package org.openntf.domino.napi;

import java.util.List;
import java.util.Vector;

/**
 * This is a subset of methods that are available through the NAPI
 * 
 * @author Roland Praml, Foconis AG
 * 
 */
public interface NapiDocument {

	/**
	 * Delete an item
	 * 
	 * @param itemName
	 * @return true if item was deleted
	 */
	boolean deleteItem(String itemName);

	/**
	 * Returns the field type of an item
	 * 
	 * @param itemName
	 * @return fieldType
	 */
	int getFieldType(String itemName);

	/**
	 * Returns the form fields
	 * 
	 * @return the form fields
	 */
	String[] getFormFields();

	/**
	 * Returns the content as text-vector
	 * 
	 * @param itemName
	 * @return the content as a Vector of Strings
	 */
	Vector<String> getItemAsTextVector(String itemName);

	/**
	 * Returns the value as String
	 * 
	 * @param itemName
	 * @return the value as a String
	 */
	String getItemValueAsString(String itemName);

	/**
	 * @return a String array of Item names
	 */
	String[] getItemNames();

	//	/**
	//	 * @param arg0
	//	 * @return
	//	 */
	//	String getItemValueAsString(String arg0);

	/**
	 * @return The modified date/time seconds.
	 */
	long getModifiedDateTimeSeconds();

	/**
	 * @return The integer note class.
	 */
	int getNoteClass();

	/**
	 * @return An integer of the note flags.
	 */
	int getNoteFlags();

	/**
	 * @return The note ID, as a String.
	 */
	String getNoteId();

	/**
	 * @return The note UNID, as a String.
	 */
	String getNoteUnid();

	/**
	 * @param itemName
	 * @return Whether an item with the provided name is present.
	 */
	boolean isItemPresent(String itemName);

	/**
	 * @param noteClass
	 * @return
	 */
	boolean setClass(int noteClass);

	/**
	 * @param itemName
	 * @param value
	 * @return
	 */
	boolean setItemAsTextList(String itemName, List<String> value);

	/**
	 * @param itemName
	 * @param value
	 * @return
	 */
	boolean setItemText(String itemName, String value);

	/**
	 * @param unid
	 * @return
	 */
	boolean setUnid(String unid);

	/**
	 * @return
	 * 
	 */
	boolean update();

}

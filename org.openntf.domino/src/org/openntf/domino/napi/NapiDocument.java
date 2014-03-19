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
	 * @return
	 */
	String[] getFormFields();

	/**
	 * Returns the content as text-vector
	 * 
	 * @param itemName
	 * @return
	 */
	Vector<String> getItemAsTextVector(String itemName);

	/**
	 * Returns the value as String
	 * 
	 * @param itemName
	 * @return
	 */
	String getItemValueAsString(String itemName);

	/**
	 * @return
	 */
	String[] getItemNames();

	//	/**
	//	 * @param arg0
	//	 * @return
	//	 */
	//	String getItemValueAsString(String arg0);

	/**
	 * @return
	 */
	long getModifiedDateTimeSeconds();

	/**
	 * @return
	 */
	int getNoteClass();

	/**
	 * @return
	 */
	int getNoteFlags();

	/**
	 * @return
	 */
	String getNoteId();

	/**
	 * @return
	 */
	String getNoteUnid();

	/**
	 * @param arg0
	 * @return
	 */
	boolean isItemPresent(String arg0);

	/**
	 * @param arg0
	 * @return
	 */
	boolean setClass(int arg0);

	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 */
	boolean setItemAsTextList(String arg0, List<String> arg1);

	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 */
	boolean setItemText(String arg0, String arg1);

	/**
	 * @param arg0
	 * @return
	 */
	boolean setUnid(String arg0);

	/**
	 * @return
	 * 
	 */
	boolean update();

}

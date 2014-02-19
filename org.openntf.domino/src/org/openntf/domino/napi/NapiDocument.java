package org.openntf.domino.napi;

import java.util.List;

public interface NapiDocument {

	/**
	 * @param arg0
	 * @return
	 */
	byte[] bulkDecryptField(String arg0);

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @return
	 */
	boolean bulkEncryptField(String arg0, String arg1, String arg2, byte[] arg3, String arg4);

	/**
	 * @param arg0
	 * @return
	 */
	boolean deleteItem(String arg0);

	/**
	 * @param arg0
	 * @return
	 */
	boolean deleteItemIgnoreMissing(String arg0);

	/**
	 * @return
	 */
	String getClassName();

	/**
	 * @param arg0
	 * @return
	 */
	int getFieldType(String arg0);

	/**
	 * @return
	 */
	String[] getFormFields();

	/**
	 * @param arg0
	 * @return
	 */
	List<String> getItemAsTextList(String arg0);

	/**
	 * @return
	 */
	String[] getItemNames();

	/**
	 * @param arg0
	 * @return
	 */
	String getItemValueAsString(String arg0);

	/**
	 * @return
	 */
	String getModifiedDateTime();

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
	int getNoteId();

	/**
	 * @return
	 */
	String getNoteUnid();

	/**
	 * @return
	 * 
	 */
	boolean initAsFile();

	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 */
	boolean initAsFile(String arg0, String arg1);

	/**
	 * @param arg0
	 * @return
	 */
	boolean initAsFile(String arg0);

	/**
	 * @return
	 * 
	 */
	boolean initAsForm();

	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 */
	boolean initClassAndFlags(String arg0, int arg1);

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
	 * @return
	 */
	boolean setExtendedFlags(String arg0);

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
	 * @param arg1
	 * @return
	 */
	boolean setItemTextAllowEmpty(String arg0, String arg1);

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

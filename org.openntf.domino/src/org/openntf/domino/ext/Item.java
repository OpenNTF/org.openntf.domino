/**
 * 
 */
package org.openntf.domino.ext;

import java.util.Date;

/**
 * @author withersp
 * 
 */
public interface Item {

	/**
	 * Gets the values, casting them to the class passed in, e.g. ArrayList.class
	 * 
	 * @param T
	 *            Class to return values as
	 * @return values as an instance of Class<?> T
	 */
	public <T> T getValues(final Class<?> T);

	/**
	 * Gets the last modified date of the Item, casting it to a Java Date
	 * 
	 * @return Date the Item was last modified
	 */
	public Date getLastModifiedDate();

	/**
	 * Checks whether the Item has certain flags. Possible flags are:
	 * <ul>
	 * <li>Flags.PROTECTED</li>
	 * <li>Flags.AUTHORS</li>
	 * <li>Flags.ENCRYPTED</li>
	 * <li>Flags.NAMES</li>
	 * <li>Flags.READERS</li>
	 * <li>Flags.SIGNED</li>
	 * <li>Flags.SUMMARY</li>
	 * </ul>
	 * 
	 * @param flag
	 *            Flags enum
	 * @return whether the item has the relevant flag
	 */
	public boolean hasFlag(org.openntf.domino.Item.Flags flag);

	/**
	 * Checks whether an Item is one of either Names, Readers or Authors
	 * 
	 * @return true if the Item has Flags.NAMES, Flags.READERS or Flags.AUTHORS flag
	 */
	public boolean isReadersNamesAuthors();
}

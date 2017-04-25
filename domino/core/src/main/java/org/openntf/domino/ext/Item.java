/**
 *
 */
package org.openntf.domino.ext;

import java.util.Date;

import org.openntf.domino.Item.Type;

/**
 * OpenNTF extensions to Item class
 *
 * @author withersp
 *
 */
public interface Item {

	/**
	 * Gets the values, casting them to the class passed in (for example ArrayList.class). Use this method to get the values directly as an
	 * instance of a required type.
	 * <p>
	 * For example, when the item contains a single date, use this method to receive a java.util.Calendar instance:
	 *
	 * <pre>
	 * Item expireItem = doc.getFirstItem("ExpireDate");
	 * Calendar calExpires = expireItem.getValues(Calendar.class);
	 * </pre>
	 * </p>
	 * <p>
	 * When the item contains multiple values, pass a class implementing the java.util.Collection interface:
	 *
	 * <pre>
	 * Item orderItems = docOrder.getFirstItem("Items");
	 * ArrayList<String> items = orderItems.getValues(ArrayList.class);
	 * for (String orderItemID : items) {
	 * 	//process the item in the order
	 * }
	 * </pre>
	 * </p>
	 *
	 * @param T
	 *            Class to return values as
	 * @return values as an instance of Class<T> type
	 * @since org.openntf.domino 1.0.0
	 */
	public <T> T getValues(final Class<T> type);

	/**
	 * Gets the last modified date of the Item, casting it to a Java Date
	 *
	 * @return Date the Item was last modified
	 * @since org.openntf.domino 2.5.0
	 */
	public Date getLastModifiedDate();

	/**
	 * Helper method to allow consistent access to whether the Item has certain flags, rather than calling the relevant getters. Possible
	 * flags are:
	 * <ul>
	 * <li>Flags.PROTECTED</li>
	 * <li>Flags.AUTHORS</li>
	 * <li>Flags.ENCRYPTED</li>
	 * <li>Flags.NAMES</li>
	 * <li>Flags.READERS</li>
	 * <li>Flags.SIGNED</li>
	 * <li>Flags.SUMMARY</li>
	 * </ul>
	 * Authors, Readers and Names are included here because {@link org.openntf.domino.Item#getType()} does not necessarily identify this
	 *
	 * @param flag
	 *            Flags enum
	 * @return whether the item has the relevant flag
	 * @since org.openntf.domino 5.0.0
	 */
	public boolean hasFlag(org.openntf.domino.Item.Flags flag);

	/**
	 * Checks whether an Item is one of either Names, Readers or Authors
	 *
	 * @return true if the Item has Flags.NAMES, Flags.READERS or Flags.AUTHORS flag
	 * @since org.openntf.domino 5.0.0
	 */
	public boolean isReadersNamesAuthors();

	/**
	 * Sets the underlying document dirty.
	 */
	public void markDirty();

	/**
	 * Returns the {@link Type} of the Item as Type object
	 *
	 * @return the {@link Type} of the Item
	 */
	Type getTypeEx();
}

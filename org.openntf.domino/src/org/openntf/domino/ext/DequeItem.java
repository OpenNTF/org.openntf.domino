/**
 * 
 */
package org.openntf.domino.ext;

import java.util.Deque;

import org.openntf.domino.Item;

/**
 * @author nfreeman
 * 
 */
public interface DequeItem extends Item {

	public Deque<Object> deque(); // returns the inner deque wrapper around the item's values.

}

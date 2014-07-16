/**
 * 
 */
package org.openntf.domino.schema;

import org.openntf.domino.schema.exceptions.ItemException;

/**
 * @author nfreeman
 * 
 */
public interface IDominoType {

	public static enum Default {
		Unknown, Text, BigText, Integer, Currency, Decimal, Date, Time, DateTime, Phone, Email, Map, HTML, RichText, Name, Color, Month, Week, URL, Rating, Range, Tab, Accordion, Button, Hotspot, Image, Signature, Audio, Video
	}

	public String getUITypeName();

	//this method validates the type ONLY. It doesn't not apply other validation rules
	//TODO NTF - Possibly change this to a type-coercion instead?
	public boolean validateItem(org.openntf.domino.Item item) throws ItemException;

	//In theory this would validate both the data type and whatever validation rules were defined by the IItemDefinition
	public boolean validateItem(org.openntf.domino.Item item, IItemDefinition definition) throws ItemException;

	public void setItemToDefault(org.openntf.domino.Item item);

}

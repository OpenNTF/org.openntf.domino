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

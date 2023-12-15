/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
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
package org.openntf.domino.nsfdata.structs;

/**
 * This structure contains view format information for views saved in Domino Release 5 and later. This structure is one of the components of
 * a $VIEWFORMAT item in a view note. (viewfmt.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class VIEW_TABLE_FORMAT3 extends AbstractStruct {
	public final Unsigned16 Length = new Unsigned16();
	public final Unsigned32 Flags = new Unsigned32();
	public final COLOR_VALUE BackgroundColor = inner(new COLOR_VALUE());
	public final COLOR_VALUE AlternateBackgroundColor = inner(new COLOR_VALUE());
	/**
	 * @since Lotus Notes/Domino 6.0
	 */
	public final COLOR_VALUE GridColorValue = inner(new COLOR_VALUE());
	/**
	 * @since Lotus Notes/Domino 6.0
	 */
	public final Unsigned16 wViewMarginTop = new Unsigned16();
	/**
	 * @since Lotus Notes/Domino 6.0
	 */
	public final Unsigned16 wViewMarginLeft = new Unsigned16();
	/**
	 * @since Lotus Notes/Domino 6.0
	 */
	public final Unsigned16 wViewMarginRight = new Unsigned16();
	/**
	 * @since Lotus Notes/Domino 6.0
	 */
	public final Unsigned16 wViewMarginBottom = new Unsigned16();
	/**
	 * @since Lotus Notes/Domino 6.0
	 */
	public final COLOR_VALUE MarginBackgroundColor = inner(new COLOR_VALUE());
	/**
	 * @since Lotus Notes/Domino 6.0
	 */
	public final COLOR_VALUE HeaderBackgroundColor = inner(new COLOR_VALUE());
	/**
	 * @since Lotus Notes/Domino 6.0
	 */
	public final Unsigned16 wViewMarginTopUnder = new Unsigned16();
	/**
	 * @since Lotus Notes/Domino 6.0
	 */
	public final COLOR_VALUE UnreadColor = inner(new COLOR_VALUE());
	/**
	 * @since Lotus Notes/Domino 6.0
	 */
	public final COLOR_VALUE TotalsColor = inner(new COLOR_VALUE());
	/**
	 * @since Lotus Notes/Domino 7.0
	 */
	public final Unsigned16 wMaxRows = new Unsigned16();
	public final Unsigned16 wReserved = new Unsigned16();
	public final Unsigned32 dwReserved = new Unsigned32();

}

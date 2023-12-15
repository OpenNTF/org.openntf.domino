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
package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * Text in a rich-text field can have the "Pass-Thru HTML" attribute. Pass-through HTML text is not translated to the Domino rich text
 * format. Pass-through HTML text is marked by CDHTMLBEGIN and CDHTMLEND records. (editods.h)
 * 
 * @since Lotus Notes/Domino 4.6
 *
 */
public class CDHTMLBEGIN extends CDRecord {

	public final WSIG Header = inner(new WSIG());
	public final Unsigned8[] Spares = array(new Unsigned8[4]);

	@Override
	public SIG getHeader() {
		return Header;
	}

}

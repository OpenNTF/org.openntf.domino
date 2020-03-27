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
package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.BSIG;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * The definition for a layer on a form is stored as CD records in the $Body item of the form note. A layer is comprised of a Layer Object
 * Run (pointer to box that represents the layer), Box Run and Position Data. (editods.h)
 * 
 * @since Lotus Notes/Domino 6.0
 *
 */
public class CDLAYER extends CDRecord {

	public final BSIG Header = inner(new BSIG());
	public final Unsigned32[] Reserved = array(new Unsigned32[4]);

	@Override
	public SIG getHeader() {
		return Header;
	}

}

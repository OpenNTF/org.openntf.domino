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

import org.openntf.domino.nsfdata.structs.BSIG;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This structure defines the start of a new paragraph within a rich-text field. Each paragraph in a rich text field may have different
 * style attributes, such as indentation and interline spacing. Use this structure when accessing a rich text field at the level of the CD
 * records. (editods.h)
 *
 */
public class CDPARAGRAPH extends CDRecord {

	public final BSIG Header = inner(new BSIG());

	@Override
	public SIG getHeader() {
		return Header;
	}
}

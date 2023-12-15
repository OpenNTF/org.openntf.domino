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
 * One structure for each row or column. (fsods.h)
 * 
 * @since Lotus Notes/Domino 5.0.1
 *
 */
public class FRAMESETLENGTH extends AbstractStruct {
	public static enum LengthType {
		UNUSED0, PIXELS, PERCENTAGE, RELATIVE;
	}

	public final Enum16<LengthType> Type = new Enum16<LengthType>(LengthType.values());
	public final Unsigned16 Value = new Unsigned16();

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": Type=" + Type.get() + ", Value=" + Value.get() + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}
}

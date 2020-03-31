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
package org.openntf.domino.nsfdata.structs;


/**
 * Specifies a cropping rectangle for display of graphical data. (editods.h)
 *
 */
public class CROPRECT extends AbstractStruct {

	public final Unsigned16 left = new Unsigned16();
	public final Unsigned16 top = new Unsigned16();
	public final Unsigned16 right = new Unsigned16();
	public final Unsigned16 bottom = new Unsigned16();

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": Left=" + left.get() + ", Top=" + top.get() + ", Right=" + right.get() + ", Bottom="
				+ bottom.get() + "]";
	}
}

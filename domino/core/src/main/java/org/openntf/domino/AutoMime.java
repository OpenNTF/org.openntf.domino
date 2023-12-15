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
package org.openntf.domino;

/**
 * Enum for how MIME is managed
 * 
 * @since org.openntf.domino 5.0.0
 * 
 */
public enum AutoMime {

	WRAP_ALL, 	// Always try autowrap when using "replaceItemValue(String, Object)"
				// This is neccessary for compatibility with "old" applications.

	WRAP_32K, 	// Only autowrap Dominofriendly values that exceeds 32K.
				// This should be used if possible.
				// get/put should be used to do autowrap
				// replaceItemValueCustomData always wraps the given object.

	WRAP_NONE	// Never autowrap. Useful for foreign DBs that do not support AutoMime
				// put will only write dominofriendly objects.
				// the only way to write a MIME bean is to use replaceItemValueCustomData when 
				// specifying "mime-bean" as dataType
}

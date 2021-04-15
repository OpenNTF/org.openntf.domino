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
package org.openntf.domino.graph2.builtin;

import org.openntf.domino.graph2.annotations.TypedProperty;

import com.tinkerpop.frames.VertexFrame;
import com.tinkerpop.frames.modules.typedgraph.TypeField;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeField("form")
@TypeValue("Contact")
public interface Contact extends VertexFrame {
	@TypedProperty("firstName")
	public String getFirstName();

	public void setFirstName(String firstName);

	@TypedProperty("lastName")
	public String getLastName();

	public void setLastName(String lastName);

	@TypedProperty(derived = true, value = "firstName + \" \" + lastName")
	public String getFullName();
}
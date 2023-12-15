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
package org.openntf.domino.graph2.builtin;

import java.util.List;

import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.annotations.Shardable;
import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.builtin.ViewVertex.Contains;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.VertexFrame;
import com.tinkerpop.frames.modules.typedgraph.TypeField;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@Shardable
@TypeField("null")
@TypeValue("null")
public interface CategoryVertex extends VertexFrame {
	@TypedProperty("value")
	public String getValue();

	@TypedProperty("position")
	public String getPosition();

	@TypedProperty("noteid")
	public String getNoteid();

	@IncidenceUnique(label = "contents", direction = Direction.OUT)
	public List<Contains> getContents();

	@IncidenceUnique(label = "doccontents", direction = Direction.OUT)
	public List<Contains> getDocContents();

}

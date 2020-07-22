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
package org.openntf.domino.graph2.builtin.workflow;

import java.util.List;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.builtin.DVertexFrame;

import com.tinkerpop.blueprints.Direction;

public interface Flowable extends DVertexFrame {
	@IncidenceUnique(label = Requires.LABEL_REQUIRES, direction = Direction.IN)
	public List<Requires> getRequires();

	@IncidenceUnique(label = Requires.LABEL_REQUIRES, direction = Direction.IN)
	public Requires addRequires(Task task);

	@IncidenceUnique(label = Requires.LABEL_REQUIRES, direction = Direction.IN)
	public void removeRequires(Task task);

	@AdjacencyUnique(label = Requires.LABEL_REQUIRES, direction = Direction.IN)
	public List<Task> getTasks();

	@AdjacencyUnique(label = Requires.LABEL_REQUIRES, direction = Direction.IN)
	public Task addTask(Task task);

	@AdjacencyUnique(label = Requires.LABEL_REQUIRES, direction = Direction.IN)
	public void removeTask(Task task);
}

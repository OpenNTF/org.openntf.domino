/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
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

public interface Flower extends DVertexFrame {
	@IncidenceUnique(label = Assigns.LABEL_ASSIGNS)
	public List<Assigns> getAssigns();

	@IncidenceUnique(label = Assigns.LABEL_ASSIGNS)
	public Assigns addAssigns(Task task);

	@IncidenceUnique(label = Assigns.LABEL_ASSIGNS)
	public void removeAssigns(Task task);

	@AdjacencyUnique(label = Assigns.LABEL_ASSIGNS)
	public List<Task> getAssignsTasks();

	@AdjacencyUnique(label = Assigns.LABEL_ASSIGNS)
	public Task addAssignsTask(Task task);

	@AdjacencyUnique(label = Assigns.LABEL_ASSIGNS)
	public void removeAssignsTask(Task task);

	@IncidenceUnique(label = AssignedTo.LABEL_ASSIGNEDTO, direction = Direction.IN)
	public List<AssignedTo> getAssignedTo();

	@IncidenceUnique(label = AssignedTo.LABEL_ASSIGNEDTO, direction = Direction.IN)
	public AssignedTo addAssignedTo(Task task);

	@IncidenceUnique(label = AssignedTo.LABEL_ASSIGNEDTO, direction = Direction.IN)
	public void removeAssignedTo(Task task);

	@AdjacencyUnique(label = AssignedTo.LABEL_ASSIGNEDTO, direction = Direction.IN)
	public List<Task> getAssignedToTask();

	@AdjacencyUnique(label = AssignedTo.LABEL_ASSIGNEDTO, direction = Direction.IN)
	public Task addAssignedToTask(Task task);

	@AdjacencyUnique(label = AssignedTo.LABEL_ASSIGNEDTO, direction = Direction.IN)
	public void removeAssignedToTask(Task task);

	@IncidenceUnique(label = Performs.LABEL_PERFORMS)
	public List<Performs> getPerforms();

	@IncidenceUnique(label = Performs.LABEL_PERFORMS)
	public Performs addPerforms(Task task);

	@IncidenceUnique(label = Performs.LABEL_PERFORMS)
	public void removePerforms(Task task);

	@AdjacencyUnique(label = Performs.LABEL_PERFORMS)
	public List<Task> getPerformsTasks();

	@AdjacencyUnique(label = Performs.LABEL_PERFORMS)
	public Task addPerformsTask(Task task);

	@AdjacencyUnique(label = Performs.LABEL_PERFORMS)
	public void removePerformsTask(Task task);

}

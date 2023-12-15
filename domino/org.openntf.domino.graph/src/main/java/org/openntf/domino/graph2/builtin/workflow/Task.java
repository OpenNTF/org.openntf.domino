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
package org.openntf.domino.graph2.builtin.workflow;

import java.util.List;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.builtin.DVertexFrame;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.Adjacency;
import com.tinkerpop.frames.Incidence;

public interface Task extends DVertexFrame {
	@TypedProperty("Name")
	public String getName();

	@TypedProperty("Name")
	public void setName(String name);

	@TypedProperty("Outcome")
	public String getOutcome();

	@TypedProperty("Outcome")
	public void setOutcome(String outcome);

	@IncidenceUnique(label = Requires.LABEL_REQUIRES)
	public List<Requires> getRequires();

	@IncidenceUnique(label = Requires.LABEL_REQUIRES)
	public Requires addRequires(Flowable flowable);

	@IncidenceUnique(label = Requires.LABEL_REQUIRES)
	public void removeRequires(Flowable flowable);

	@AdjacencyUnique(label = Requires.LABEL_REQUIRES)
	public List<Flowable> getFlowables();

	@AdjacencyUnique(label = Requires.LABEL_REQUIRES)
	public Task addFlowable(Flowable flowable);

	@AdjacencyUnique(label = Requires.LABEL_REQUIRES)
	public void removeFlowable(Flowable flowable);

	@Incidence(label = Assigns.LABEL_ASSIGNS, direction = Direction.IN)
	public Flower getAssign();

	@Incidence(label = Assigns.LABEL_ASSIGNS, direction = Direction.IN)
	public Assigns addAssigns(Flower flower);

	@Incidence(label = Assigns.LABEL_ASSIGNS, direction = Direction.IN)
	public void removeAssigns(Flower flower);

	@Adjacency(label = Assigns.LABEL_ASSIGNS, direction = Direction.IN)
	public Flower getAssignsFlower();

	@Adjacency(label = Assigns.LABEL_ASSIGNS, direction = Direction.IN)
	public Flower addAssignsFlower(Flower flower);

	@Adjacency(label = Assigns.LABEL_ASSIGNS, direction = Direction.IN)
	public void removeAssignsFlower(Flower flower);

	@Incidence(label = AssignedTo.LABEL_ASSIGNEDTO)
	public List<AssignedTo> getAssignedTo();

	@Incidence(label = AssignedTo.LABEL_ASSIGNEDTO)
	public AssignedTo addAssignedTo(Flower flower);

	@Incidence(label = AssignedTo.LABEL_ASSIGNEDTO)
	public void removeAssignedTo(Flower flower);

	@Adjacency(label = AssignedTo.LABEL_ASSIGNEDTO)
	public List<Flower> getAssignedToFlower();

	@Adjacency(label = AssignedTo.LABEL_ASSIGNEDTO)
	public Flower addAssignedToFlower(Flower flower);

	@Adjacency(label = AssignedTo.LABEL_ASSIGNEDTO)
	public void removeAssignedToFlower(Flower flower);

	@Incidence(label = Performs.LABEL_PERFORMS, direction = Direction.IN)
	public List<Performs> getPerformedBy();

	@Incidence(label = Performs.LABEL_PERFORMS, direction = Direction.IN)
	public Performs addPerformedBy(Flower flower);

	@Incidence(label = Performs.LABEL_PERFORMS, direction = Direction.IN)
	public void removePerformedBy(Flower flower);

	@Adjacency(label = Performs.LABEL_PERFORMS, direction = Direction.IN)
	public List<Flower> getPerformedByFlowers();

	@Adjacency(label = Performs.LABEL_PERFORMS, direction = Direction.IN)
	public Task addPerformedByFlower(Flower flower);

	@Adjacency(label = Performs.LABEL_PERFORMS, direction = Direction.IN)
	public void removePerformedByFlower(Flower flower);

}

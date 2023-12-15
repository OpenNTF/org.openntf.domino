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
package org.openntf.domino.graph2.builtin.workflow.definition;

import java.util.List;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.builtin.DVertexFrame;
import org.openntf.domino.graph2.builtin.workflow.Task;

import com.tinkerpop.blueprints.Direction;

public interface TaskDefinition extends DVertexFrame {

	/* BEGIN Starts relationships */
	@IncidenceUnique(label = StartsWith.LABEL_STARTSWITH, direction = Direction.IN)
	public List<StartsWith> getStartsWith();

	@IncidenceUnique(label = StartsWith.LABEL_STARTSWITH, direction = Direction.IN)
	public StartsWith addStartsWith(FlowDefinition definition);

	@IncidenceUnique(label = StartsWith.LABEL_STARTSWITH, direction = Direction.IN)
	public void removeStartsWith(FlowDefinition definition);

	@AdjacencyUnique(label = StartsWith.LABEL_STARTSWITH, direction = Direction.IN)
	public List<FlowDefinition> getStartFlows();

	@AdjacencyUnique(label = StartsWith.LABEL_STARTSWITH, direction = Direction.IN)
	public FlowDefinition addStartFlow(FlowDefinition definition);

	@AdjacencyUnique(label = StartsWith.LABEL_STARTSWITH, direction = Direction.IN)
	public void removeStartFlow(FlowDefinition definition);

	/* END Starts relationships */

	/* BEGIN Finishes relationships */
	@IncidenceUnique(label = Finishes.LABEL_FINISHES)
	public List<Finishes> getFinishes();

	@IncidenceUnique(label = Finishes.LABEL_FINISHES)
	public Finishes addFinishes(FlowDefinition definition);

	@IncidenceUnique(label = Finishes.LABEL_FINISHES)
	public void removeFinishes(FlowDefinition definition);

	@AdjacencyUnique(label = Finishes.LABEL_FINISHES)
	public List<FlowDefinition> getFinishFlows();

	@AdjacencyUnique(label = Finishes.LABEL_FINISHES)
	public FlowDefinition addFinishFlow(FlowDefinition definition);

	@AdjacencyUnique(label = Finishes.LABEL_FINISHES)
	public void removeFinishFlow(FlowDefinition definition);

	/* END Finishes relationships */

	/* BEGIN FollowedBy relationships */
	@IncidenceUnique(label = FollowedBy.LABEL_FLOWFOLLOWEDBY)
	public List<FollowedBy> getFollowedBys();

	@IncidenceUnique(label = FollowedBy.LABEL_FLOWFOLLOWEDBY)
	public FollowedBy addFollowedBy(TaskDefinition definition);

	@IncidenceUnique(label = FollowedBy.LABEL_FLOWFOLLOWEDBY)
	public void removeFollowedBy(TaskDefinition definition);

	@AdjacencyUnique(label = FollowedBy.LABEL_FLOWFOLLOWEDBY)
	public List<TaskDefinition> getFollowedByTasks();

	@AdjacencyUnique(label = FollowedBy.LABEL_FLOWFOLLOWEDBY)
	public TaskDefinition addFollowedByTask(TaskDefinition definition);

	@AdjacencyUnique(label = FollowedBy.LABEL_FLOWFOLLOWEDBY)
	public void removeFollowedByTask(TaskDefinition definition);

	/* END FollowedBy relationships */

	/* BEGIN Follows relationships */
	@IncidenceUnique(label = FollowedBy.LABEL_FLOWFOLLOWEDBY, direction = Direction.IN)
	public List<FollowedBy> getFollows();

	@IncidenceUnique(label = FollowedBy.LABEL_FLOWFOLLOWEDBY, direction = Direction.IN)
	public FollowedBy addFollows(TaskDefinition definition);

	@IncidenceUnique(label = FollowedBy.LABEL_FLOWFOLLOWEDBY, direction = Direction.IN)
	public void removeFollows(TaskDefinition definition);

	@AdjacencyUnique(label = FollowedBy.LABEL_FLOWFOLLOWEDBY, direction = Direction.IN)
	public List<TaskDefinition> getFollowsTasks();

	@AdjacencyUnique(label = FollowedBy.LABEL_FLOWFOLLOWEDBY, direction = Direction.IN)
	public TaskDefinition addFollowsTask(TaskDefinition definition);

	@AdjacencyUnique(label = FollowedBy.LABEL_FLOWFOLLOWEDBY, direction = Direction.IN)
	public void removeFollowsTask(TaskDefinition definition);

	/* END Follows relationships */

	/* BEGIN Defines relationships */
	@IncidenceUnique(label = Defines.LABEL_FLOWABLEDEFINES)
	public List<Defines> getDefines();

	@IncidenceUnique(label = Defines.LABEL_FLOWABLEDEFINES)
	public Defines addDefines(Task task);

	@IncidenceUnique(label = Defines.LABEL_FLOWABLEDEFINES)
	public void removeDefines(Task task);

	@AdjacencyUnique(label = FollowedBy.LABEL_FLOWFOLLOWEDBY)
	public List<Task> getDefinesTasks();

	@AdjacencyUnique(label = FollowedBy.LABEL_FLOWFOLLOWEDBY)
	public Task addDefinesTask(Task task);

	@AdjacencyUnique(label = FollowedBy.LABEL_FLOWFOLLOWEDBY)
	public void removeDefinesTask(Task task);

	/* END Defines relationships */

}

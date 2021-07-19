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
package org.openntf.domino.graph2.builtin.workflow.definition;

import java.util.List;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.builtin.DVertexFrame;
import org.openntf.domino.graph2.builtin.workflow.Flowable;

import com.tinkerpop.blueprints.Direction;

public interface FlowDefinition extends DVertexFrame {

	/* BEGIN Starts relationships */
	@IncidenceUnique(label = StartsWith.LABEL_STARTSWITH)
	public StartsWith getStartsWith();

	@IncidenceUnique(label = StartsWith.LABEL_STARTSWITH)
	public StartsWith addStartsWith(TaskDefinition definition);

	@IncidenceUnique(label = StartsWith.LABEL_STARTSWITH)
	public void removeStartsWith(TaskDefinition definition);

	@AdjacencyUnique(label = StartsWith.LABEL_STARTSWITH)
	public TaskDefinition getStartTask();

	@AdjacencyUnique(label = StartsWith.LABEL_STARTSWITH)
	public TaskDefinition addStartTask(TaskDefinition definition);

	@AdjacencyUnique(label = StartsWith.LABEL_STARTSWITH)
	public void removeStartTask(TaskDefinition definition);

	/* END Starts relationships */

	/* BEGIN Finishes relationships */
	@IncidenceUnique(label = Finishes.LABEL_FINISHES, direction = Direction.IN)
	public List<Finishes> getFinishes();

	@IncidenceUnique(label = Finishes.LABEL_FINISHES, direction = Direction.IN)
	public Finishes addFinishes(TaskDefinition definition);

	@IncidenceUnique(label = Finishes.LABEL_FINISHES, direction = Direction.IN)
	public void removeFinishes(TaskDefinition definition);

	@AdjacencyUnique(label = Finishes.LABEL_FINISHES, direction = Direction.IN)
	public List<TaskDefinition> getFinishTasks();

	@AdjacencyUnique(label = Finishes.LABEL_FINISHES, direction = Direction.IN)
	public TaskDefinition addFinishTask(TaskDefinition definition);

	@AdjacencyUnique(label = Finishes.LABEL_FINISHES, direction = Direction.IN)
	public void removeFinishTask(TaskDefinition definition);

	/* END Finishes relationships */

	/* BEGIN Flowable relationships */
	@IncidenceUnique(label = Uses.LABEL_FLOWABLEUSES, direction = Direction.IN)
	public List<Uses> getUses();

	@IncidenceUnique(label = Uses.LABEL_FLOWABLEUSES, direction = Direction.IN)
	public Uses addUses(Flowable flowable);

	@IncidenceUnique(label = Uses.LABEL_FLOWABLEUSES, direction = Direction.IN)
	public void removeUses(Flowable flowable);

	@AdjacencyUnique(label = Uses.LABEL_FLOWABLEUSES, direction = Direction.IN)
	public List<Flowable> getUsesFlowables();

	@AdjacencyUnique(label = Uses.LABEL_FLOWABLEUSES, direction = Direction.IN)
	public Flowable addUsesFlowable(Flowable flowable);

	@AdjacencyUnique(label = Uses.LABEL_FLOWABLEUSES, direction = Direction.IN)
	public void removeUsesFlowable(Flowable flowable);
	/* END Flowable relationships */
}

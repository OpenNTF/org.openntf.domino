package org.openntf.domino.graph2.builtin.workflow.definition;

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
	public Iterable<Finishes> getFinishes();

	@IncidenceUnique(label = Finishes.LABEL_FINISHES, direction = Direction.IN)
	public Finishes addFinishes(TaskDefinition definition);

	@IncidenceUnique(label = Finishes.LABEL_FINISHES, direction = Direction.IN)
	public void removeFinishes(TaskDefinition definition);

	@AdjacencyUnique(label = Finishes.LABEL_FINISHES, direction = Direction.IN)
	public Iterable<TaskDefinition> getFinishTasks();

	@AdjacencyUnique(label = Finishes.LABEL_FINISHES, direction = Direction.IN)
	public TaskDefinition addFinishTask(TaskDefinition definition);

	@AdjacencyUnique(label = Finishes.LABEL_FINISHES, direction = Direction.IN)
	public void removeFinishTask(TaskDefinition definition);

	/* END Finishes relationships */

	/* BEGIN Flowable relationships */
	@IncidenceUnique(label = Uses.LABEL_FLOWABLEUSES, direction = Direction.IN)
	public Iterable<Uses> getUses();

	@IncidenceUnique(label = Uses.LABEL_FLOWABLEUSES, direction = Direction.IN)
	public Uses addUses(Flowable flowable);

	@IncidenceUnique(label = Uses.LABEL_FLOWABLEUSES, direction = Direction.IN)
	public void removeUses(Flowable flowable);

	@AdjacencyUnique(label = Uses.LABEL_FLOWABLEUSES, direction = Direction.IN)
	public Iterable<Flowable> getUsesFlowables();

	@AdjacencyUnique(label = Uses.LABEL_FLOWABLEUSES, direction = Direction.IN)
	public Flowable addUsesFlowable(Flowable flowable);

	@AdjacencyUnique(label = Uses.LABEL_FLOWABLEUSES, direction = Direction.IN)
	public void removeUsesFlowable(Flowable flowable);
	/* END Flowable relationships */
}

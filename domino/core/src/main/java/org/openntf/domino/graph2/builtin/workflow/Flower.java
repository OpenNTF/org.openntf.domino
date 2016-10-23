package org.openntf.domino.graph2.builtin.workflow;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.builtin.DVertexFrame;

import com.tinkerpop.blueprints.Direction;

public interface Flower extends DVertexFrame {
	@IncidenceUnique(label = Assigns.LABEL_ASSIGNS)
	public Iterable<Assigns> getAssigns();

	@IncidenceUnique(label = Assigns.LABEL_ASSIGNS)
	public Assigns addAssigns(Task task);

	@IncidenceUnique(label = Assigns.LABEL_ASSIGNS)
	public void removeAssigns(Task task);

	@AdjacencyUnique(label = Assigns.LABEL_ASSIGNS)
	public Iterable<Task> getAssignsTasks();

	@AdjacencyUnique(label = Assigns.LABEL_ASSIGNS)
	public Task addAssignsTask(Task task);

	@AdjacencyUnique(label = Assigns.LABEL_ASSIGNS)
	public void removeAssignsTask(Task task);

	@IncidenceUnique(label = AssignedTo.LABEL_ASSIGNEDTO, direction = Direction.IN)
	public Iterable<AssignedTo> getAssignedTo();

	@IncidenceUnique(label = AssignedTo.LABEL_ASSIGNEDTO, direction = Direction.IN)
	public AssignedTo addAssignedTo(Task task);

	@IncidenceUnique(label = AssignedTo.LABEL_ASSIGNEDTO, direction = Direction.IN)
	public void removeAssignedTo(Task task);

	@AdjacencyUnique(label = AssignedTo.LABEL_ASSIGNEDTO, direction = Direction.IN)
	public Iterable<Task> getAssignedToTask();

	@AdjacencyUnique(label = AssignedTo.LABEL_ASSIGNEDTO, direction = Direction.IN)
	public Task addAssignedToTask(Task task);

	@AdjacencyUnique(label = AssignedTo.LABEL_ASSIGNEDTO, direction = Direction.IN)
	public void removeAssignedToTask(Task task);

	@IncidenceUnique(label = Performs.LABEL_PERFORMS)
	public Iterable<Performs> getPerforms();

	@IncidenceUnique(label = Performs.LABEL_PERFORMS)
	public Performs addPerforms(Task task);

	@IncidenceUnique(label = Performs.LABEL_PERFORMS)
	public void removePerforms(Task task);

	@AdjacencyUnique(label = Performs.LABEL_PERFORMS)
	public Iterable<Task> getPerformsTasks();

	@AdjacencyUnique(label = Performs.LABEL_PERFORMS)
	public Task addPerformsTask(Task task);

	@AdjacencyUnique(label = Performs.LABEL_PERFORMS)
	public void removePerformsTask(Task task);

}

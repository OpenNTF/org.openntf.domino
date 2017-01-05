package org.openntf.domino.graph2.builtin.workflow;

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
	public Iterable<Requires> getRequires();

	@IncidenceUnique(label = Requires.LABEL_REQUIRES)
	public Requires addRequires(Flowable flowable);

	@IncidenceUnique(label = Requires.LABEL_REQUIRES)
	public void removeRequires(Flowable flowable);

	@AdjacencyUnique(label = Requires.LABEL_REQUIRES)
	public Iterable<Flowable> getFlowables();

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
	public Iterable<AssignedTo> getAssignedTo();

	@Incidence(label = AssignedTo.LABEL_ASSIGNEDTO)
	public AssignedTo addAssignedTo(Flower flower);

	@Incidence(label = AssignedTo.LABEL_ASSIGNEDTO)
	public void removeAssignedTo(Flower flower);

	@Adjacency(label = AssignedTo.LABEL_ASSIGNEDTO)
	public Iterable<Flower> getAssignedToFlower();

	@Adjacency(label = AssignedTo.LABEL_ASSIGNEDTO)
	public Flower addAssignedToFlower(Flower flower);

	@Adjacency(label = AssignedTo.LABEL_ASSIGNEDTO)
	public void removeAssignedToFlower(Flower flower);

	@Incidence(label = Performs.LABEL_PERFORMS, direction = Direction.IN)
	public Iterable<Performs> getPerformedBy();

	@Incidence(label = Performs.LABEL_PERFORMS, direction = Direction.IN)
	public Performs addPerformedBy(Flower flower);

	@Incidence(label = Performs.LABEL_PERFORMS, direction = Direction.IN)
	public void removePerformedBy(Flower flower);

	@Adjacency(label = Performs.LABEL_PERFORMS, direction = Direction.IN)
	public Iterable<Flower> getPerformedByFlowers();

	@Adjacency(label = Performs.LABEL_PERFORMS, direction = Direction.IN)
	public Task addPerformedByFlower(Flower flower);

	@Adjacency(label = Performs.LABEL_PERFORMS, direction = Direction.IN)
	public void removePerformedByFlower(Flower flower);

}

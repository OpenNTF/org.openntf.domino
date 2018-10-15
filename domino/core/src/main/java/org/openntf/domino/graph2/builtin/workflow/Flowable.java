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

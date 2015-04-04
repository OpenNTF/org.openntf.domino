package org.openntf.domino.graph2.builtin;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.Adjacency;
import com.tinkerpop.frames.Incidence;
import com.tinkerpop.frames.VertexFrame;

public interface Editable extends VertexFrame {
	@Incidence(label = Edits.LABEL_EDITS, direction = Direction.IN)
	public Iterable<Edits> getEdits();

	@Incidence(label = Edits.LABEL_EDITS, direction = Direction.IN)
	public Edits addEdits(Edits edits);

	@Incidence(label = Edits.LABEL_EDITS, direction = Direction.IN)
	public Edits findEdits(Edits edits);

	@Incidence(label = Edits.LABEL_EDITS, direction = Direction.IN)
	public void removeEdits(Edits edits);

	@Adjacency(label = Edits.LABEL_EDITS, direction = Direction.IN)
	public Iterable<Editor> getEditsEditor();

	@Adjacency(label = Edits.LABEL_EDITS, direction = Direction.IN)
	public Editor addEditsEditor(Editor editor);
}

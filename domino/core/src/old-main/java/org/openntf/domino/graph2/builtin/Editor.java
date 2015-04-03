package org.openntf.domino.graph2.builtin;

import com.tinkerpop.frames.Adjacency;
import com.tinkerpop.frames.Incidence;
import com.tinkerpop.frames.VertexFrame;

public interface Editor extends VertexFrame {
	@Incidence(label = Edits.LABEL_EDITS)
	public Iterable<Edits> getEdits();

	@Incidence(label = Edits.LABEL_EDITS)
	public Edits addEdits(Editable editable);

	@Incidence(label = Edits.LABEL_EDITS)
	public void removeEdits(Editable editable);

	@Adjacency(label = Edits.LABEL_EDITS)
	public Iterable<Editable> getEditables();

	@Adjacency(label = Edits.LABEL_EDITS)
	public Editable addEditable(Editable editable);

	@Adjacency(label = Edits.LABEL_EDITS)
	public void removeEditable(Editable editable);
}

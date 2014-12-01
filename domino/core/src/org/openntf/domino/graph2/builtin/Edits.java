package org.openntf.domino.graph2.builtin;

import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;

public interface Edits extends EdgeFrame {
	public static final String LABEL_EDITS = "edits";

	@OutVertex
	public Editor getEditor();

	@InVertex
	public Editable getEditable();

}

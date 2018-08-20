package org.openntf.domino.graph2.builtin;

import java.util.List;

import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.annotations.Shardable;
import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.builtin.ViewVertex.Contains;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.VertexFrame;
import com.tinkerpop.frames.modules.typedgraph.TypeField;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@Shardable
@TypeField("null")
@TypeValue("null")
public interface CategoryVertex extends VertexFrame {
	@TypedProperty("value")
	public String getValue();

	@TypedProperty("position")
	public String getPosition();

	@TypedProperty("noteid")
	public String getNoteid();

	@IncidenceUnique(label = "contents", direction = Direction.OUT)
	public List<Contains> getContents();

	@IncidenceUnique(label = "doccontents", direction = Direction.OUT)
	public List<Contains> getDocContents();

}

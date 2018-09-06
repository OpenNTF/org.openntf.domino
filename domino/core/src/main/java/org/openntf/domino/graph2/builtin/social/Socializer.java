package org.openntf.domino.graph2.builtin.social;

import java.util.List;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.builtin.DVertexFrame;

import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Socializer")
public interface Socializer extends Commenter, Liker, Rater {
	@AdjacencyUnique(label = Mentions.LABEL)
	public List<DVertexFrame> getMentionedOns();

	@AdjacencyUnique(label = Mentions.LABEL)
	public Mentions addMentionedOn(DVertexFrame mentionedOn);

	@AdjacencyUnique(label = Mentions.LABEL)
	public void removeMentionedOn(DVertexFrame mentionedOn);

	@IncidenceUnique(label = Mentions.LABEL)
	public List<Mentions> getMentioneds();

	@IncidenceUnique(label = Mentions.LABEL)
	public int countMentioneds();

	@IncidenceUnique(label = Mentions.LABEL)
	public void removeMentioned(Mentions mentions);

}

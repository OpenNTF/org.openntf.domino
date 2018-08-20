package org.openntf.domino.graph2.builtin.social;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.builtin.DVertexFrame;

import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Socializer")
public interface Socializer extends Commenter, Liker, Rater {
	@AdjacencyUnique(label = Mentions.LABEL)
	public Iterable<DVertexFrame> getMentionedOns();

	@AdjacencyUnique(label = Mentions.LABEL)
	public Mentions addMentionedOn(DVertexFrame mentionedOn);

	@AdjacencyUnique(label = Mentions.LABEL)
	public void removeMentionedOn(DVertexFrame mentionedOn);

	@IncidenceUnique(label = Mentions.LABEL)
	public Iterable<Mentions> getMentioneds();

	@IncidenceUnique(label = Mentions.LABEL)
	public int countMentioneds();

	@IncidenceUnique(label = Mentions.LABEL)
	public void removeMentioned(Mentions mentions);

}

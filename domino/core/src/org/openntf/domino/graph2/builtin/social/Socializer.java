package org.openntf.domino.graph2.builtin.social;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.builtin.DVertexFrame;

public interface Socializer extends Commenter, Liker, Rater {
	@IncidenceUnique(label = Mentions.LABEL)
	public Iterable<Mentions> getMentioned();

	@IncidenceUnique(label = Mentions.LABEL)
	public Mentions addMentioned(Socializer mentioned);

	@IncidenceUnique(label = Mentions.LABEL)
	public void removeMentioned(Socializer mentioned);

	@AdjacencyUnique(label = Mentions.LABEL)
	public Iterable<DVertexFrame> getMentionedOn();

	@AdjacencyUnique(label = Mentions.LABEL)
	public DVertexFrame addMentionedOn(DVertexFrame mentionedOn);

	@AdjacencyUnique(label = Mentions.LABEL)
	public void removeMentionedOn(DVertexFrame mentionedOn);
}

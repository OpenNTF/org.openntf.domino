package org.openntf.domino.graph2.builtin.social;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.builtin.DVertexFrame;

import com.tinkerpop.blueprints.Direction;

public interface Shareable extends DVertexFrame {
	@IncidenceUnique(label = ShareAbout.LABEL, direction = Direction.IN)
	public Iterable<ShareAbout> getShareAbout();

	@IncidenceUnique(label = ShareAbout.LABEL, direction = Direction.IN)
	public ShareAbout addShareAbout(Share share);

	@IncidenceUnique(label = ShareAbout.LABEL, direction = Direction.IN)
	public void removeShareAbout(Share share);

	@AdjacencyUnique(label = ShareAbout.LABEL, direction = Direction.IN)
	public Iterable<Share> getShares();

	@AdjacencyUnique(label = ShareAbout.LABEL, direction = Direction.IN)
	public Share addShare(Share share);

	@AdjacencyUnique(label = ShareAbout.LABEL, direction = Direction.IN)
	public void removeShare(Share share);

	@IncidenceUnique(label = Mentions.LABEL, direction = Direction.IN)
	public Iterable<Mentions> getMentions();

	@IncidenceUnique(label = Mentions.LABEL, direction = Direction.IN)
	public Mentions addMentions(Socializer mentioned);

	@IncidenceUnique(label = Mentions.LABEL, direction = Direction.IN)
	public void removeMentions(Socializer mentioned);

	@AdjacencyUnique(label = Mentions.LABEL, direction = Direction.IN)
	public Iterable<Socializer> getMentioned();

	@AdjacencyUnique(label = Mentions.LABEL, direction = Direction.IN)
	public Socializer addMentioned(Socializer mentioned);

	@AdjacencyUnique(label = Mentions.LABEL, direction = Direction.IN)
	public void removeMentioned(Socializer mentioned);
}

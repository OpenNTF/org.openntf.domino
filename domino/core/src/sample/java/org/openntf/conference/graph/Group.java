package org.openntf.conference.graph;

import org.openntf.conference.graph.Attendee.MemberOf;
import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.builtin.social.Commentable;
import org.openntf.domino.graph2.builtin.social.Likeable;
import org.openntf.domino.graph2.builtin.social.Rateable;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Group")
public interface Group extends Commentable, Likeable, Rateable {
	public static enum Type {
		OPEN, COMPANY, SOCIAL, PROGRAM, PRIVATE
	}

	@TypedProperty("Name")
	public String getName();

	@TypedProperty("Name")
	public void setName(String name);

	@TypedProperty("Type")
	public Type getType();

	@TypedProperty("Type")
	public void setType(Type type);

	@AdjacencyUnique(label = MemberOf.LABEL, direction = Direction.IN)
	public Iterable<Attendee> getMembers();

	@AdjacencyUnique(label = MemberOf.LABEL, direction = Direction.IN)
	public MemberOf addMember(Attendee attendee);

	@AdjacencyUnique(label = MemberOf.LABEL, direction = Direction.IN)
	public void removeMember(Attendee attendee);

	@IncidenceUnique(label = MemberOf.LABEL, direction = Direction.IN)
	public Iterable<MemberOf> getMemberOfs();

	@IncidenceUnique(label = MemberOf.LABEL, direction = Direction.IN)
	public void removeMemberOf(MemberOf memberOf);
}

package org.openntf.conference.graph;

import org.openntf.conference.graph.Attendee.MemberOf;
import org.openntf.domino.graph2.builtin.social.Commentable;
import org.openntf.domino.graph2.builtin.social.Likeable;
import org.openntf.domino.graph2.builtin.social.Rateable;

public interface Group extends Commentable, Likeable, Rateable {
	public static enum Type {
		OPEN, COMPANY, SOCIAL, PROGRAM, PRIVATE
	}

	public String getName();

	public void setName(String name);

	public Type getType();

	public void setType(Type type);

	public Iterable<Attendee> getMembers();

	public Iterable<MemberOf> getMemberOfs();
}

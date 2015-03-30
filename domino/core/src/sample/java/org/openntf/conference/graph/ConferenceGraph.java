package org.openntf.conference.graph;

import java.util.List;

import org.openntf.domino.graph2.builtin.social.Comment;
import org.openntf.domino.graph2.builtin.social.Likes;
import org.openntf.domino.graph2.builtin.social.Mentions;
import org.openntf.domino.graph2.builtin.social.Rates;
import org.openntf.domino.graph2.impl.DConfiguration;
import org.openntf.domino.graph2.impl.DElementStore;
import org.openntf.domino.graph2.impl.DFramedGraphFactory;
import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.graph2.impl.DGraph;

import com.google.common.collect.Lists;
import com.tinkerpop.frames.modules.Module;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerModule;

public class ConferenceGraph {
	public static final String ATTENDEE_PATH = "conference/attendees.nsf";
	public static final String GROUP_PATH = "conference/groups.nsf";
	public static final String EVENT_PATH = "conference/events.nsf";
	public static final String INVITE_PATH = "conference/invites.nsf";
	public static final String TIMES_PATH = "conference/times.nsf";
	public static final String LOCATION_PATH = "conference/locations.nsf";
	public static final String COMMENTS_PATH = "conference/comments.nsf";
	public static final String SOCIAL_PATH = "conference/social.nsf";
	public static final String DEFAULT_PATH = "conference/default.nsf";

	private DFramedTransactionalGraph<DGraph> framedGraph_;

	public ConferenceGraph() {
		initialize();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void initialize() {
		DElementStore attendeeStore = new DElementStore();
		attendeeStore.setStoreKey(ATTENDEE_PATH);
		attendeeStore.addType(Attendee.class);
		DElementStore groupStore = new DElementStore();
		groupStore.setStoreKey(GROUP_PATH);
		groupStore.addType(Group.class);
		groupStore.addType(Sponsor.class);
		DElementStore eventStore = new DElementStore();
		eventStore.setStoreKey(EVENT_PATH);
		eventStore.addType(Event.class);
		eventStore.addType(Presentation.class);
		eventStore.addType(Social.class);
		eventStore.addType(Meeting.class);
		eventStore.addType(Track.class);
		DElementStore inviteStore = new DElementStore();
		inviteStore.setStoreKey(INVITE_PATH);
		inviteStore.addType(Invite.class);
		DElementStore timesStore = new DElementStore();
		timesStore.setStoreKey(TIMES_PATH);
		timesStore.addType(TimeSlot.class);
		DElementStore commentStore = new DElementStore();
		commentStore.setStoreKey(COMMENTS_PATH);
		commentStore.addType(Comment.class);
		DElementStore locationStore = new DElementStore();
		locationStore.setStoreKey(LOCATION_PATH);
		locationStore.addType(Location.class);
		DElementStore socialStore = new DElementStore();
		socialStore.setStoreKey(SOCIAL_PATH);
		socialStore.addType(Likes.class);
		socialStore.addType(Rates.class);
		socialStore.addType(Mentions.class);
		DElementStore defaultStore = new DElementStore();
		defaultStore.setStoreKey(DEFAULT_PATH);

		DConfiguration config = new DConfiguration();
		DGraph graph = new DGraph(config);
		config.addElementStore(attendeeStore);
		config.addElementStore(groupStore);
		config.addElementStore(eventStore);
		config.addElementStore(inviteStore);
		config.addElementStore(timesStore);
		config.addElementStore(locationStore);
		config.addElementStore(defaultStore);
		config.setDefaultElementStore(defaultStore.getStoreKey());

		JavaHandlerModule jhm = new JavaHandlerModule();
		Module module = config.getModule();
		DFramedGraphFactory factory = new DFramedGraphFactory(module, jhm);
		framedGraph_ = (DFramedTransactionalGraph) factory.create(graph);
	}

	public DFramedTransactionalGraph<DGraph> getFramedGraph() {
		return framedGraph_;
	}

	public Attendee getAttendee(final Object key, final boolean create) {
		Attendee result = getAttendee(key);
		if (result == null && create) {
			result = getFramedGraph().addVertex(key, Attendee.class);
		}
		return result;
	}

	public Attendee getAttendee(final Object key) {
		Attendee result = null;
		try {
			result = getFramedGraph().getVertex(key, Attendee.class);
			if (result == null) {
				List<Attendee> verts = Lists.newArrayList(getFramedGraph().getVertices("Twitter", key, Attendee.class));
				if (!verts.isEmpty()) {
					result = verts.get(0);
				} else {
					verts = Lists.newArrayList(getFramedGraph().getVertices("Email", key, Attendee.class));
					if (!verts.isEmpty()) {
						verts.get(0);
					}
				}
			}
			return result;
		} catch (Exception e) {
			return null;
		}
	}

	public Group getGroup(final Object key, final boolean create) {
		Group result = getGroup(key);
		if (result == null && create) {
			result = getFramedGraph().addVertex(key, Group.class);
		}
		return result;
	}

	public Group getGroup(final Object key) {
		return getFramedGraph().getVertex(key, Group.class);
	}

	@SuppressWarnings("unchecked")
	public <T extends Event> T getEvent(final Object key) {
		return (T) getFramedGraph().getVertex(key, Event.class);
	}

	public TimeSlot getTimeSlot(final Object key) {
		return getFramedGraph().getVertex(key, TimeSlot.class);
	}

	public Iterable<TimeSlot> getTimeSlots() {
		return getFramedGraph().getVertices(null, null, TimeSlot.class);
	}

	public Location getLocation(final Object key) {
		return getFramedGraph().getVertex(key, Location.class);
	}

}

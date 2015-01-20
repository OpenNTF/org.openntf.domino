package org.openntf.conference.graph;

import org.openntf.domino.big.NoteCoordinate;
import org.openntf.domino.graph2.builtin.social.Comment;
import org.openntf.domino.graph2.builtin.social.Likes;
import org.openntf.domino.graph2.builtin.social.Mentions;
import org.openntf.domino.graph2.builtin.social.Rates;
import org.openntf.domino.graph2.impl.DConfiguration;
import org.openntf.domino.graph2.impl.DElementStore;
import org.openntf.domino.graph2.impl.DFramedGraphFactory;
import org.openntf.domino.graph2.impl.DGraph;

import com.tinkerpop.frames.FramedTransactionalGraph;
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

	private FramedTransactionalGraph<DGraph> framedGraph_;

	public ConferenceGraph() {
		initialize();
	}

	protected void initialize() {
		DElementStore attendeeStore = new DElementStore();
		attendeeStore.setStoreKey(ATTENDEE_PATH);
		attendeeStore.addType(Attendee.class);
		DElementStore groupStore = new DElementStore();
		groupStore.setStoreKey(GROUP_PATH);
		groupStore.addType(Group.class);
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
		defaultStore.setStoreKey(NoteCoordinate.Utils.getLongFromReplid(DEFAULT_PATH));

		DConfiguration config = new DConfiguration();
		config.addElementStore(attendeeStore);
		config.addElementStore(groupStore);
		config.addElementStore(eventStore);
		config.addElementStore(inviteStore);
		config.addElementStore(timesStore);
		config.addElementStore(locationStore);
		config.addElementStore(defaultStore);
		config.setDefaultElementStore(defaultStore.getStoreKey());
		DGraph graph = new DGraph(config);

		JavaHandlerModule jhm = new JavaHandlerModule();
		Module module = config.getModule();
		DFramedGraphFactory factory = new DFramedGraphFactory(module, jhm);
		framedGraph_ = factory.create(graph);
	}

	public FramedTransactionalGraph<DGraph> getFramedGraph() {
		return framedGraph_;
	}

}

package org.openntf.conference.graph;

import org.openntf.domino.big.NoteCoordinate;
import org.openntf.domino.graph2.impl.DConfiguration;
import org.openntf.domino.graph2.impl.DElementStore;
import org.openntf.domino.graph2.impl.DFramedGraphFactory;
import org.openntf.domino.graph2.impl.DGraph;

import com.tinkerpop.frames.FramedTransactionalGraph;
import com.tinkerpop.frames.modules.Module;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerModule;
import com.tinkerpop.frames.modules.typedgraph.TypedGraphModuleBuilder;

public class ConferenceGraph {
	public static final String ATTENDEE_PATH = "conference/attendees.nsf";
	public static final String GROUP_PATH = "conference/groups.nsf";
	public static final String EVENT_PATH = "conference/events.nsf";
	public static final String INVITE_PATH = "conference/invites.nsf";
	public static final String TIMES_PATH = "conference/times.nsf";
	public static final String LOCATION_PATH = "conference/locations.nsf";
	public static final String DEFAULT_PATH = "conference/default.nsf";

	private FramedTransactionalGraph<DGraph> framedGraph_;

	public ConferenceGraph() {
		initialize();
	}

	protected void initialize() {
		DElementStore attendeeStore = new DElementStore();
		attendeeStore.setStoreKey(NoteCoordinate.Utils.getLongFromReplid(ATTENDEE_PATH));
		attendeeStore.addType(Attendee.class);
		DElementStore groupStore = new DElementStore();
		groupStore.setStoreKey(NoteCoordinate.Utils.getLongFromReplid(GROUP_PATH));
		groupStore.addType(Group.class);
		DElementStore eventStore = new DElementStore();
		eventStore.setStoreKey(NoteCoordinate.Utils.getLongFromReplid(EVENT_PATH));
		eventStore.addType(Event.class);
		eventStore.addType(Session.class);
		eventStore.addType(Social.class);
		eventStore.addType(Meeting.class);
		eventStore.addType(Track.class);
		DElementStore inviteStore = new DElementStore();
		inviteStore.setStoreKey(NoteCoordinate.Utils.getLongFromReplid(INVITE_PATH));
		inviteStore.addType(Invite.class);
		DElementStore timesStore = new DElementStore();
		timesStore.setStoreKey(NoteCoordinate.Utils.getLongFromReplid(TIMES_PATH));
		timesStore.addType(TimeSlot.class);
		DElementStore locationStore = new DElementStore();
		locationStore.setStoreKey(NoteCoordinate.Utils.getLongFromReplid(LOCATION_PATH));
		locationStore.addType(Location.class);
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
		TypedGraphModuleBuilder typedBuilder = new TypedGraphModuleBuilder();
		typedBuilder.withClass(Attendee.class);
		typedBuilder.withClass(Group.class);
		typedBuilder.withClass(Event.class);
		typedBuilder.withClass(Session.class);
		typedBuilder.withClass(Social.class);
		typedBuilder.withClass(Meeting.class);
		typedBuilder.withClass(Track.class);
		typedBuilder.withClass(TimeSlot.class);
		typedBuilder.withClass(Location.class);
		Module module = typedBuilder.build();
		DFramedGraphFactory factory = new DFramedGraphFactory(module, jhm);
		framedGraph_ = factory.create(graph);
	}

	public FramedTransactionalGraph<DGraph> getFramedGraph() {
		return framedGraph_;
	}

}

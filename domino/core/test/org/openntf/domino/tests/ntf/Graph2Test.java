package org.openntf.domino.tests.ntf;

import org.openntf.domino.big.NoteCoordinate;
import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.impl.DConfiguration;
import org.openntf.domino.graph2.impl.DElementStore;
import org.openntf.domino.graph2.impl.DFramedGraphFactory;
import org.openntf.domino.graph2.impl.DGraph;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.FramedTransactionalGraph;
import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;
import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.VertexFrame;
import com.tinkerpop.frames.modules.Module;
import com.tinkerpop.frames.modules.javahandler.JavaHandler;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerClass;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerContext;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerModule;
import com.tinkerpop.frames.modules.typedgraph.TypeField;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;
import com.tinkerpop.frames.modules.typedgraph.TypedGraphModuleBuilder;

public class Graph2Test implements Runnable {
	private static int THREAD_COUNT = 1;
	private long marktime;

	private DGraph graph;

	public static String crewId = "85257D640018A1B3";
	public static String movieId = "85257D640018AD81";
	public static String characterId = "85257D6B007ECB47";
	public static String edgeId = "85257D640018BCDF";
	public static String usersId = "85257D810065979B";
	public static String nabId = "85257A600051D882";
	public static String ntfUnid = "2F25B5EDE23C245785257A600059FD2E";

	private static final String directedBy = "DirectedBy";
	private static final String likes = "Likes";
	private static final String appearsIn = "AppearsIn";
	private static final String portrays = "Portrays";
	private static final String starring = "Starring";
	private static final String spawns = "Spawns";
	private static final String kills = "Kills";

	@TypeField("form")
	public interface DVertexFrame extends VertexFrame {

	}

	@TypeField("form")
	public interface DEdgeFrame extends EdgeFrame {

	}

	@TypeValue("Person")
	public interface User extends DVertexFrame {
		@TypedProperty("FirstName")
		public String getFirstName();

		@TypedProperty("FirstName")
		public void setFirstName(String firstName);

		@TypedProperty("LastName")
		public String getLastName();

		@TypedProperty("LastName")
		public void setLastName(String lastName);

		@TypedProperty("FullName")
		public String[] getFullName();

		@TypedProperty("FullName")
		public void setFullName(String[] fullName);

		@IncidenceUnique(label = likes)
		public Likes getLikes();

		@IncidenceUnique(label = likes)
		public Likes addLikes(Movie movie);

		@IncidenceUnique(label = likes)
		public void removeLikes(Movie movie);

		@AdjacencyUnique(label = likes)
		public Movie getLikesMovies();

		@AdjacencyUnique(label = likes)
		public Movie addLikesMovie(Movie movie);

		@AdjacencyUnique(label = likes)
		public Movie removeLikesMovie(Movie movie);
	}

	@TypeValue(likes)
	public interface Likes extends DEdgeFrame {
		@TypedProperty("rating")
		public Integer getRating();

		@TypedProperty("rating")
		public void setRating(Integer rating);

		@OutVertex
		User getUser();

		@InVertex
		Movie getMovie();

	}

	@TypeValue("movie")
	public interface Movie extends DVertexFrame {
		@TypedProperty("title")
		public String getTitle();

		@TypedProperty("title")
		public void setTitle(String title);

		@IncidenceUnique(label = directedBy)
		public DirectedBy getDirectedBy();

		@IncidenceUnique(label = directedBy)
		public DirectedBy addDirectedBy(Crew crew);

		@IncidenceUnique(label = directedBy)
		public void removeDirectedBy(Crew crew);

		@AdjacencyUnique(label = directedBy)
		public Crew getDirectedByCrew();

		@AdjacencyUnique(label = directedBy)
		public Crew addDirectedByCrew(Crew crew);

		@AdjacencyUnique(label = directedBy)
		public Crew removeDirectedByCrew(Crew crew);

		@IncidenceUnique(label = starring, direction = Direction.IN)
		public Iterable<Starring> getStarring();

		@IncidenceUnique(label = starring, direction = Direction.IN)
		public Starring addStarring(Crew crew);

		@IncidenceUnique(label = starring, direction = Direction.IN)
		public void removeStarring(Crew crew);

		@AdjacencyUnique(label = starring, direction = Direction.IN)
		public Iterable<Crew> getStarringCrew();

		@AdjacencyUnique(label = starring, direction = Direction.IN)
		public Crew addStarringCrew(Crew crew);

		@AdjacencyUnique(label = starring, direction = Direction.IN)
		public Crew removeStarringCrew(Crew crew);

		@IncidenceUnique(label = likes, direction = Direction.IN)
		public Likes getLikedBy();

		@IncidenceUnique(label = likes, direction = Direction.IN)
		public Likes addLikedBy(User user);

		@IncidenceUnique(label = likes, direction = Direction.IN)
		public void removeLikedBy(User user);

		@AdjacencyUnique(label = likes, direction = Direction.IN)
		public Movie getLikedByUsers();

		@AdjacencyUnique(label = likes, direction = Direction.IN)
		public Movie addLikedByUser(User user);

		@AdjacencyUnique(label = likes, direction = Direction.IN)
		public Movie removeLikedByUser(User user);

	}

	@TypeValue(directedBy)
	public interface DirectedBy extends DEdgeFrame {
		@TypedProperty("rating")
		public Integer getRating();

		@TypedProperty("rating")
		public void setRating(Integer rating);

		@OutVertex
		Crew getDirector();

		@InVertex
		Movie getMovie();

	}

	@TypeValue(starring)
	public interface Starring extends DEdgeFrame {
		@InVertex
		Movie getMovie();

		@OutVertex
		Crew getStar();
	}

	@TypeValue(portrays)
	public interface Portrays extends DEdgeFrame {
		@OutVertex
		Crew getStar();

		@InVertex
		Character getCharacter();
	}

	@TypeValue(appearsIn)
	public interface AppearsIn extends DEdgeFrame {
		@OutVertex
		Character getCharacter();

		@InVertex
		Movie getMovie();
	}

	@TypeValue(spawns)
	public interface Spawns extends DEdgeFrame {
		@OutVertex
		Character getParent();

		@InVertex
		Character getCreated();
	}

	@TypeValue(kills)
	public interface Kills extends DEdgeFrame {
		@Property("film")
		public String getFilm();

		@Property("film")
		public void setFilm(String film);

		@OutVertex
		Character getKiller();

		@InVertex
		Character getVictim();
	}

	@TypeValue("character")
	public interface Character extends DVertexFrame {
		@Property("name")
		public String getName();

		@Property("name")
		public void setName(String name);

		@AdjacencyUnique(label = appearsIn)
		public Iterable<Movie> getAppearsInMovies();

		@IncidenceUnique(label = appearsIn)
		public Iterable<AppearsIn> getAppearsIn();

		@IncidenceUnique(label = appearsIn)
		public AppearsIn addAppearsIn(Movie movie);

		@IncidenceUnique(label = appearsIn)
		public void removeAppearsIn(AppearsIn appearance);

		@AdjacencyUnique(label = portrays, direction = Direction.IN)
		public Iterable<Crew> getPortrayedByCrew();

		@IncidenceUnique(label = portrays, direction = Direction.IN)
		public Iterable<Portrays> getPortrayedBy();

		@IncidenceUnique(label = portrays, direction = Direction.IN)
		public Portrays addPortrayedBy(Crew crew);

		@IncidenceUnique(label = portrays, direction = Direction.IN)
		public void removePortrayedBy(Portrays portrayal);

		@AdjacencyUnique(label = kills, direction = Direction.IN)
		public Iterable<Character> getKilledByCharacters();

		@IncidenceUnique(label = kills, direction = Direction.IN)
		public Iterable<Kills> getKilledBy();

		@IncidenceUnique(label = kills, direction = Direction.IN)
		public Kills addKilledBy(Character character);

		@IncidenceUnique(label = kills, direction = Direction.IN)
		public void removeKilledBy(Kills kill);

		@AdjacencyUnique(label = kills, direction = Direction.OUT)
		public Iterable<Character> getKillsCharacters();

		@IncidenceUnique(label = kills, direction = Direction.OUT)
		public Iterable<Kills> getKills();

		@IncidenceUnique(label = kills, direction = Direction.OUT)
		public Kills addKills(Character character);

		@IncidenceUnique(label = kills, direction = Direction.OUT)
		public void removeKills(Kills kill);

		@AdjacencyUnique(label = spawns, direction = Direction.IN)
		public Iterable<Character> getSpawnedByCharacters();

		@IncidenceUnique(label = spawns, direction = Direction.IN)
		public Iterable<Spawns> getSpawnedBy();

		@IncidenceUnique(label = spawns, direction = Direction.IN)
		public Spawns addSpawnedBy(Character character);

		@IncidenceUnique(label = spawns, direction = Direction.IN)
		public void removeSpawnedBy(Spawns spawn);

		@AdjacencyUnique(label = spawns, direction = Direction.OUT)
		public Iterable<Character> getSpawnsCharacters();

		@IncidenceUnique(label = spawns, direction = Direction.OUT)
		public Iterable<Spawns> getSpawns();

		@IncidenceUnique(label = spawns, direction = Direction.OUT)
		public Spawns addSpawns(Character character);

		@AdjacencyUnique(label = spawns, direction = Direction.OUT)
		public void removeSpawns(Character spawn);
	}

	@TypeValue("crew")
	@JavaHandlerClass(CrewImpl.class)
	public interface Crew extends DVertexFrame {
		@Property("firstName")
		public String getFirstName();

		@Property("lastName")
		public String getLastName();

		@Property("firstName")
		public void setFirstName(String firstName);

		@Property("lastName")
		public void setLastName(String lastName);

		@JavaHandler
		public void setFullName(String fullName);

		@AdjacencyUnique(label = portrays)
		public Iterable<Character> getPortraysCharacters();

		@IncidenceUnique(label = portrays)
		public Iterable<Portrays> getPortrayals();

		@IncidenceUnique(label = portrays)
		public Portrays addPortrayals(Character character);

		@IncidenceUnique(label = portrays)
		public void removePortrayals(Portrays portrayal);

		@AdjacencyUnique(label = starring)
		public Iterable<Movie> getStarsInMovies();

		@IncidenceUnique(label = starring)
		public Iterable<Starring> getStarsIn();

		@IncidenceUnique(label = starring)
		public Starring addStarsInMovie(Movie movie);

		@AdjacencyUnique(label = starring)
		public void removeStarsInMovie(Movie movie);
	}

	public static abstract class CrewImpl implements JavaHandlerContext<Vertex>, Crew {
		@Override
		public void setFullName(final String fullName) {
			int pos = fullName.lastIndexOf(' ');
			setFirstName(fullName.substring(0, pos));
			setLastName(fullName.substring(pos + 1));
		}
	}

	public static void main(final String[] args) {
		org.openntf.domino.thread.DominoExecutor de = new org.openntf.domino.thread.DominoExecutor(10);
		for (int i = 0; i < THREAD_COUNT; i++) {
			de.execute(new Graph2Test());
		}
		de.shutdown();
	}

	public void timelog(final String message) {
		long curtime = System.nanoTime();
		long elapsed = curtime - marktime;
		marktime = curtime;
		System.out.println(elapsed / 1000000 + " ms: " + message);
	}

	public Graph2Test() {
		// whatever you might want to do in your constructor, but stay away from Domino objects
	}

	@Override
	public void run() {
		run1();
		//		run2();
		run3();
	}

	public void run3() {
		long testStartTime = System.nanoTime();
		marktime = System.nanoTime();
		//Session session = Factory.getSession(SessionType.CURRENT);

		try {

			timelog("Beginning graph2 test3...");

			DElementStore crewStore = new DElementStore();
			crewStore.setStoreKey(NoteCoordinate.Utils.getLongFromReplid(crewId));
			crewStore.addType(Crew.class);
			DElementStore movieStore = new DElementStore();
			movieStore.setStoreKey(NoteCoordinate.Utils.getLongFromReplid(movieId));
			movieStore.addType(Movie.class);
			DElementStore characterStore = new DElementStore();
			characterStore.setStoreKey(NoteCoordinate.Utils.getLongFromReplid(characterId));
			characterStore.addType(Character.class);
			DElementStore edgeStore = new DElementStore();
			edgeStore.setStoreKey(NoteCoordinate.Utils.getLongFromReplid(edgeId));
			DConfiguration config = new DConfiguration();
			config.addElementStore(crewStore);
			config.addElementStore(movieStore);
			config.addElementStore(characterStore);
			config.addElementStore(edgeStore);
			config.setDefaultElementStore(NoteCoordinate.Utils.getLongFromReplid(edgeId));
			graph = new DGraph(config);

			JavaHandlerModule jhm = new JavaHandlerModule();

			Module module = new TypedGraphModuleBuilder().withClass(Movie.class).withClass(Character.class).withClass(Crew.class).build();
			DFramedGraphFactory factory = new DFramedGraphFactory(module, jhm);

			FramedTransactionalGraph<DGraph> framedGraph = factory.create(graph);

			Movie newhopeMovie = framedGraph.getVertex(movieId + "Star Wars", Movie.class);
			Movie empireMovie = framedGraph.getVertex(movieId + "The Empire Strikes Back", Movie.class);
			Movie jediMovie = framedGraph.getVertex(movieId + "Return of the Jedi", Movie.class);
			Movie phantomMovie = framedGraph.getVertex(movieId + "The Phantom Menace", Movie.class);
			Movie clonesMovie = framedGraph.getVertex(movieId + "Attack of the Clones", Movie.class);
			Movie revengeMovie = framedGraph.getVertex(movieId + "Revenge of the Sith", Movie.class);

			System.out.println("***************************");
			System.out.println("Don't miss " + newhopeMovie.getTitle() + " " + newhopeMovie.getDirectedBy().getRating() + " stars!");
			Iterable<Starring> starrings = newhopeMovie.getStarring();
			for (Starring starring : starrings) {
				Crew crew = starring.getStar();
				Character character = crew.getPortraysCharacters().iterator().next();
				System.out.println(crew.getFirstName() + " " + crew.getLastName() + " as " + character.getName());
			}

			System.out.println("***************************");
			System.out.println("Don't miss " + empireMovie.getTitle() + " " + empireMovie.getDirectedBy().getRating() + " stars!");
			starrings = empireMovie.getStarring();
			for (Starring starring : starrings) {
				Crew crew = starring.getStar();
				Character character = crew.getPortraysCharacters().iterator().next();
				System.out.println(crew.getFirstName() + " " + crew.getLastName() + " as " + character.getName());
			}

			System.out.println("***************************");
			System.out.println("Don't miss " + jediMovie.getTitle() + " " + jediMovie.getDirectedBy().getRating() + " stars!");
			starrings = jediMovie.getStarring();
			for (Starring starring : starrings) {
				Crew crew = starring.getStar();
				Character character = crew.getPortraysCharacters().iterator().next();
				System.out.println(crew.getFirstName() + " " + crew.getLastName() + " as " + character.getName());
			}

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public void run2() {
		long testStartTime = System.nanoTime();
		marktime = System.nanoTime();

		try {
			timelog("Beginning graph2 test...");

			for (Vertex v : graph.getVertices()) {
				System.out.println("RESULT: " + v.getId().toString());
			}

			for (Edge e : graph.getEdges()) {
				System.out.println("RESULT: " + e.getId().toString() + ":" + e.getLabel());
			}
			//			lotus.domino.Session s = Factory.terminate();
			//			s.recycle();
			graph = null;
			System.gc();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		long testEndTime = System.nanoTime();
		System.out.println("Completed " + getClass().getSimpleName() + " run2 in " + ((testEndTime - testStartTime) / 1000000) + " ms");

	}

	public void run1() {

		long testStartTime = System.nanoTime();
		marktime = System.nanoTime();

		try {
			timelog("Beginning graph2 test...");

			DElementStore crewStore = new DElementStore();
			crewStore.setStoreKey(NoteCoordinate.Utils.getLongFromReplid(crewId));
			crewStore.addType(Crew.class);
			DElementStore movieStore = new DElementStore();
			movieStore.setStoreKey(NoteCoordinate.Utils.getLongFromReplid(movieId));
			movieStore.addType(Movie.class);
			DElementStore characterStore = new DElementStore();
			characterStore.setStoreKey(NoteCoordinate.Utils.getLongFromReplid(characterId));
			characterStore.addType(Character.class);
			DElementStore edgeStore = new DElementStore();
			edgeStore.setStoreKey(NoteCoordinate.Utils.getLongFromReplid(edgeId));
			DElementStore usersStore = new DElementStore();
			usersStore.setStoreKey(NoteCoordinate.Utils.getLongFromReplid(nabId));
			usersStore.setProxyStoreKey(NoteCoordinate.Utils.getLongFromReplid(usersId));
			usersStore.addType(User.class);

			DConfiguration config = new DConfiguration();
			config.addElementStore(crewStore);
			config.addElementStore(movieStore);
			config.addElementStore(characterStore);
			config.addElementStore(edgeStore);
			config.addElementStore(usersStore);
			config.setDefaultElementStore(NoteCoordinate.Utils.getLongFromReplid(edgeId));
			graph = new DGraph(config);

			JavaHandlerModule jhm = new JavaHandlerModule();
			Module module = new TypedGraphModuleBuilder().withClass(User.class).withClass(Movie.class).withClass(Character.class)
					.withClass(Crew.class).build();
			DFramedGraphFactory factory = new DFramedGraphFactory(module, jhm);
			FramedTransactionalGraph<DGraph> framedGraph = factory.create(graph);

			User ntfUser = framedGraph.getVertex(nabId + ntfUnid, User.class);

			Movie newhopeMovie = framedGraph.addVertex("Star Wars", Movie.class);
			newhopeMovie.setTitle("Star Wars");
			Likes ntfLikesNH = ntfUser.addLikes(newhopeMovie);
			ntfLikesNH.setRating(4);

			Movie empireMovie = framedGraph.addVertex("The Empire Strikes Back", Movie.class);
			empireMovie.setTitle("The Empire Strikes Back");
			Likes ntfLikesESB = ntfUser.addLikes(empireMovie);
			ntfLikesESB.setRating(5);

			Movie jediMovie = framedGraph.addVertex("Return of the Jedi", Movie.class);
			jediMovie.setTitle("Return of the Jedi");
			Likes ntfLikesRoJ = ntfUser.addLikes(jediMovie);
			ntfLikesRoJ.setRating(5);

			Movie phantomMovie = framedGraph.addVertex("The Phantom Menace", Movie.class);
			phantomMovie.setTitle("The Phantom Menace");

			Movie clonesMovie = framedGraph.addVertex("Attack of the Clones", Movie.class);
			clonesMovie.setTitle("Attack of the Clones");

			Movie revengeMovie = framedGraph.addVertex("Revenge of the Sith", Movie.class);
			revengeMovie.setTitle("Revenge of the Sith");

			Crew lucasCrew = framedGraph.addVertex("George Lucas", Crew.class);
			lucasCrew.setFullName("George Lucas");

			Crew kershnerCrew = framedGraph.addVertex("Irvin Kershner", Crew.class);
			kershnerCrew.setFullName("Irvin Kershner");

			Crew marquandCrew = framedGraph.addVertex("Richard Marquand", Crew.class);
			marquandCrew.setFullName("Richard Marquand");

			DirectedBy nhDirector = newhopeMovie.addDirectedBy(lucasCrew);
			nhDirector.setRating(4);

			DirectedBy esbDirector = empireMovie.addDirectedBy(kershnerCrew);
			esbDirector.setRating(5);

			DirectedBy rojDirector = jediMovie.addDirectedBy(marquandCrew);
			rojDirector.setRating(4);

			DirectedBy pmDirector = phantomMovie.addDirectedBy(lucasCrew);
			pmDirector.setRating(3);

			DirectedBy aocDirector = clonesMovie.addDirectedBy(lucasCrew);
			aocDirector.setRating(2);

			DirectedBy rosDirector = revengeMovie.addDirectedBy(lucasCrew);
			rosDirector.setRating(1);

			Character luke = framedGraph.addVertex("Luke Skywalker", Character.class);
			luke.setName("Luke Skywalker");
			luke.addAppearsIn(newhopeMovie);
			luke.addAppearsIn(empireMovie);
			luke.addAppearsIn(jediMovie);
			Character leia = framedGraph.addVertex("Leia Organa", Character.class);
			leia.setName("Leia Organa");
			leia.addAppearsIn(newhopeMovie);
			leia.addAppearsIn(empireMovie);
			leia.addAppearsIn(jediMovie);
			Character han = framedGraph.addVertex("Han Solo", Character.class);
			han.setName("Han Solo");
			han.addAppearsIn(newhopeMovie);
			han.addAppearsIn(empireMovie);
			han.addAppearsIn(jediMovie);
			Character chewy = framedGraph.addVertex("Chewbacca", Character.class);
			chewy.setName("Chewbacca");
			chewy.addAppearsIn(newhopeMovie);
			chewy.addAppearsIn(empireMovie);
			chewy.addAppearsIn(jediMovie);
			chewy.addAppearsIn(revengeMovie);
			Character threepio = framedGraph.addVertex("C-3PO", Character.class);
			threepio.setName("C-3PO");
			threepio.addAppearsIn(newhopeMovie);
			threepio.addAppearsIn(empireMovie);
			threepio.addAppearsIn(jediMovie);
			threepio.addAppearsIn(phantomMovie);
			threepio.addAppearsIn(clonesMovie);
			threepio.addAppearsIn(revengeMovie);
			Character artoo = framedGraph.addVertex("R2D2", Character.class);
			artoo.setName("R2D2");
			artoo.addAppearsIn(newhopeMovie);
			artoo.addAppearsIn(empireMovie);
			artoo.addAppearsIn(jediMovie);
			artoo.addAppearsIn(phantomMovie);
			artoo.addAppearsIn(clonesMovie);
			artoo.addAppearsIn(revengeMovie);
			Character lando = framedGraph.addVertex("Lando Calrissian", Character.class);
			lando.setName("Lando Calrissian");
			lando.addAppearsIn(empireMovie);
			lando.addAppearsIn(jediMovie);
			Character anakin = framedGraph.addVertex("Anakin Skywalker", Character.class);
			anakin.setName("Anakin Skywalker");
			anakin.addAppearsIn(newhopeMovie);
			anakin.addAppearsIn(empireMovie);
			anakin.addAppearsIn(jediMovie);
			anakin.addAppearsIn(phantomMovie);
			anakin.addAppearsIn(clonesMovie);
			anakin.addAppearsIn(revengeMovie);
			Character palpatine = framedGraph.addVertex("Darth Sidious", Character.class);
			palpatine.setName("Darth Sidious");
			palpatine.addAppearsIn(empireMovie);
			palpatine.addAppearsIn(jediMovie);
			palpatine.addAppearsIn(phantomMovie);
			palpatine.addAppearsIn(clonesMovie);
			palpatine.addAppearsIn(revengeMovie);
			Character obiwan = framedGraph.addVertex("Obi-Wan Kenobi", Character.class);
			obiwan.setName("Obi-Wan Kenobi");
			obiwan.addAppearsIn(newhopeMovie);
			obiwan.addAppearsIn(empireMovie);
			obiwan.addAppearsIn(jediMovie);
			obiwan.addAppearsIn(phantomMovie);
			obiwan.addAppearsIn(clonesMovie);
			obiwan.addAppearsIn(revengeMovie);
			Character quigon = framedGraph.addVertex("Qui-Gon Jinn", Character.class);
			quigon.setName("Qui-Gon Jinn");
			quigon.addAppearsIn(phantomMovie);
			Character yoda = framedGraph.addVertex("Yoda", Character.class);
			yoda.setName("Yoda");
			yoda.addAppearsIn(empireMovie);
			yoda.addAppearsIn(jediMovie);
			yoda.addAppearsIn(phantomMovie);
			yoda.addAppearsIn(clonesMovie);
			yoda.addAppearsIn(revengeMovie);
			Character jango = framedGraph.addVertex("Jango Fett", Character.class);
			jango.setName("Jango Fett");
			jango.addAppearsIn(clonesMovie);
			Character boba = framedGraph.addVertex("Boba Fett", Character.class);
			boba.setName("Boba Fett");
			boba.addAppearsIn(empireMovie);
			boba.addAppearsIn(jediMovie);
			boba.addAppearsIn(clonesMovie);
			Character padme = framedGraph.addVertex("Padme", Character.class);
			padme.setName("Padme");
			padme.addAppearsIn(phantomMovie);
			padme.addAppearsIn(clonesMovie);
			padme.addAppearsIn(revengeMovie);
			Character shmi = framedGraph.addVertex("Shmi Skywalker", Character.class);
			shmi.setName("Shmi Skywalker");
			shmi.addAppearsIn(phantomMovie);
			shmi.addAppearsIn(clonesMovie);
			Character tyranus = framedGraph.addVertex("Darth Tyranus", Character.class);
			tyranus.setName("Darth Tyranus");
			tyranus.addAppearsIn(clonesMovie);
			tyranus.addAppearsIn(revengeMovie);
			Character maul = framedGraph.addVertex("Darth Maul", Character.class);
			maul.setName("Darth Maul");
			maul.addAppearsIn(phantomMovie);
			maul.addAppearsIn(clonesMovie);
			Character tarkin = framedGraph.addVertex("Grand Moff Tarkin", Character.class);
			tarkin.setName("Grand Moff Tarkin");
			tarkin.addAppearsIn(newhopeMovie);
			Character windu = framedGraph.addVertex("Mace Windu", Character.class);
			windu.setName("Mace Windu");
			windu.addAppearsIn(phantomMovie);
			windu.addAppearsIn(clonesMovie);
			windu.addAppearsIn(revengeMovie);
			Character greedo = framedGraph.addVertex("Greedo", Character.class);
			greedo.setName("Greedo");
			greedo.addAppearsIn(newhopeMovie);
			Character wedge = framedGraph.addVertex("Wedge Antilles", Character.class);
			wedge.setName("Wedge Antilles");
			wedge.addAppearsIn(newhopeMovie);
			wedge.addAppearsIn(empireMovie);
			wedge.addAppearsIn(jediMovie);

			Crew ford = framedGraph.addVertex("Harrison Ford", Crew.class);
			ford.setFullName("Harrison Ford");
			ford.addStarsInMovie(newhopeMovie);
			ford.addStarsInMovie(empireMovie);
			ford.addStarsInMovie(jediMovie);
			han.addPortrayedBy(ford);

			Crew fischer = framedGraph.addVertex("Carrie Fischer", Crew.class);
			fischer.setFullName("Carrie Fischer");
			fischer.addStarsInMovie(newhopeMovie);
			fischer.addStarsInMovie(empireMovie);
			fischer.addStarsInMovie(jediMovie);
			fischer.addPortrayals(leia);

			Crew hammill = framedGraph.addVertex("Mark Hammill", Crew.class);
			hammill.setFullName("Mark Hammill");
			hammill.addStarsInMovie(newhopeMovie);
			hammill.addStarsInMovie(empireMovie);
			hammill.addStarsInMovie(jediMovie);
			hammill.addPortrayals(luke);

			Crew daniels = framedGraph.addVertex("Anthony Daniels", Crew.class);
			daniels.setFullName("Anthony Daniels");
			daniels.addStarsInMovie(newhopeMovie);
			daniels.addStarsInMovie(empireMovie);
			daniels.addStarsInMovie(jediMovie);
			daniels.addStarsInMovie(phantomMovie);
			daniels.addStarsInMovie(clonesMovie);
			daniels.addStarsInMovie(revengeMovie);
			daniels.addPortrayals(threepio);

			Crew baker = framedGraph.addVertex("Kenny Baker", Crew.class);
			baker.setFullName("Kenny Baker");
			baker.addStarsInMovie(newhopeMovie);
			baker.addStarsInMovie(empireMovie);
			baker.addStarsInMovie(jediMovie);
			baker.addStarsInMovie(phantomMovie);
			baker.addStarsInMovie(clonesMovie);
			baker.addStarsInMovie(revengeMovie);
			baker.addPortrayals(artoo);

			Crew prowse = framedGraph.addVertex("David Prowse", Crew.class);
			prowse.setFullName("David Prowse");
			prowse.addStarsInMovie(newhopeMovie);
			prowse.addStarsInMovie(empireMovie);
			prowse.addStarsInMovie(jediMovie);
			prowse.addPortrayals(anakin);

			Crew williams = framedGraph.addVertex("Billy Dee Williams", Crew.class);
			williams.setFullName("Billy Dee Williams");
			williams.addStarsInMovie(empireMovie);
			williams.addStarsInMovie(jediMovie);
			williams.addPortrayals(lando);

			Crew guinness = framedGraph.addVertex("Alec Guinness", Crew.class);
			guinness.setFullName("Alec Guinness");
			guinness.addStarsInMovie(newhopeMovie);
			guinness.addStarsInMovie(empireMovie);
			guinness.addStarsInMovie(jediMovie);
			guinness.addPortrayals(obiwan);

			Crew ewan = framedGraph.addVertex("Ewan McGregor", Crew.class);
			ewan.setFullName("Ewan McGregor");
			ewan.addStarsInMovie(phantomMovie);
			ewan.addStarsInMovie(clonesMovie);
			ewan.addStarsInMovie(revengeMovie);
			ewan.addPortrayals(obiwan);

			Crew cushing = framedGraph.addVertex("Peter Cushing", Crew.class);
			cushing.setFullName("Peter Cushing");
			cushing.addStarsInMovie(newhopeMovie);
			cushing.addPortrayals(tarkin);

			Crew mayhew = framedGraph.addVertex("Peter Mayhew", Crew.class);
			mayhew.setFullName("Peter Mayhew");
			mayhew.addStarsInMovie(newhopeMovie);
			mayhew.addStarsInMovie(empireMovie);
			mayhew.addStarsInMovie(jediMovie);
			mayhew.addStarsInMovie(revengeMovie);
			mayhew.addPortrayals(chewy);

			Crew oz = framedGraph.addVertex("Frank Oz", Crew.class);
			oz.setFullName("Frank Oz");
			oz.addStarsInMovie(empireMovie);
			oz.addStarsInMovie(jediMovie);
			oz.addStarsInMovie(phantomMovie);
			oz.addStarsInMovie(clonesMovie);
			oz.addStarsInMovie(revengeMovie);
			oz.addPortrayals(yoda);

			Crew bulloch = framedGraph.addVertex("Jeremy Bulloch", Crew.class);
			bulloch.setFullName("Jeremy Bulloch");
			bulloch.addStarsInMovie(empireMovie);
			bulloch.addStarsInMovie(jediMovie);
			bulloch.addPortrayals(boba);

			Crew revill = framedGraph.addVertex("Clive Revill", Crew.class);
			revill.setFullName("Clive Revill");
			revill.addStarsInMovie(empireMovie);
			revill.addPortrayals(palpatine);

			Crew ian = framedGraph.addVertex("Ian McDiarmid", Crew.class);
			ian.setFullName("Ian McDiarmid");
			ian.addStarsInMovie(jediMovie);
			ian.addStarsInMovie(phantomMovie);
			ian.addStarsInMovie(clonesMovie);
			ian.addStarsInMovie(revengeMovie);
			ian.addPortrayals(palpatine);

			Crew shaw = framedGraph.addVertex("Sebastian Shaw", Crew.class);
			shaw.setFullName("Sebastian Shaw");
			shaw.addStarsInMovie(jediMovie);
			shaw.addPortrayals(anakin);

			Crew liam = framedGraph.addVertex("Liam Neeson", Crew.class);
			liam.setFullName("Liam Neeson");
			liam.addStarsInMovie(phantomMovie);
			liam.addPortrayals(quigon);

			Crew portman = framedGraph.addVertex("Natalie Portman", Crew.class);
			portman.setFullName("Natalie Portman");
			portman.addStarsInMovie(phantomMovie);
			portman.addStarsInMovie(clonesMovie);
			portman.addStarsInMovie(revengeMovie);
			portman.addPortrayals(padme);

			Crew lloyd = framedGraph.addVertex("Jake Lloyd", Crew.class);
			lloyd.setFullName("Jake Lloyd");
			lloyd.addStarsInMovie(phantomMovie);
			lloyd.addPortrayals(anakin);

			Crew hayden = framedGraph.addVertex("Hayden Christensen", Crew.class);
			hayden.setFullName("Hayden Christensen");
			hayden.addStarsInMovie(clonesMovie);
			hayden.addStarsInMovie(revengeMovie);
			hayden.addPortrayals(anakin);

			Crew august = framedGraph.addVertex("Pernill August", Crew.class);
			august.setFullName("Pernill August");
			august.addStarsInMovie(phantomMovie);
			august.addStarsInMovie(clonesMovie);
			august.addPortrayals(shmi);

			Crew park = framedGraph.addVertex("Ray Park", Crew.class);
			park.setFullName("Ray Park");
			park.addStarsInMovie(phantomMovie);
			park.addPortrayals(maul);

			Crew samuel = framedGraph.addVertex("Samuel L. Jackson", Crew.class);
			samuel.setFullName("Samuel L. Jackson");
			samuel.addStarsInMovie(phantomMovie);
			samuel.addStarsInMovie(clonesMovie);
			samuel.addStarsInMovie(revengeMovie);
			samuel.addPortrayals(windu);

			Crew lee = framedGraph.addVertex("Christopher Lee", Crew.class);
			lee.setFullName("Christopher Lee");
			lee.addStarsInMovie(clonesMovie);
			lee.addStarsInMovie(revengeMovie);
			lee.addPortrayals(tyranus);

			Crew morrison = framedGraph.addVertex("Temuera Morrison", Crew.class);
			morrison.setFullName("Temuera Morrison");
			morrison.addStarsInMovie(clonesMovie);
			morrison.addPortrayals(jango);

			Crew logan = framedGraph.addVertex("Daniel Logan", Crew.class);
			logan.setFullName("Daniel Logan");
			logan.addStarsInMovie(clonesMovie);
			logan.addPortrayals(boba);

			Crew blake = framedGraph.addVertex("Paul Blake", Crew.class);
			blake.setFullName("Paul Blake");
			blake.addStarsInMovie(newhopeMovie);
			blake.addPortrayals(greedo);

			Crew lawson = framedGraph.addVertex("Denis Lawson", Crew.class);
			lawson.setFullName("Denis Lawson");
			lawson.addStarsInMovie(newhopeMovie);
			lawson.addStarsInMovie(empireMovie);
			lawson.addStarsInMovie(jediMovie);
			lawson.addPortrayals(wedge);

			Kills curKills = null;
			curKills = anakin.addKills(obiwan);
			curKills.setFilm(newhopeMovie.asVertex().getId().toString());
			curKills = luke.addKills(tarkin);
			curKills.setFilm(newhopeMovie.asVertex().getId().toString());
			curKills = windu.addKills(jango);
			curKills.setFilm(clonesMovie.asVertex().getId().toString());
			curKills = palpatine.addKills(windu);
			curKills.setFilm(revengeMovie.asVertex().getId().toString());
			curKills = wedge.addKills(anakin);
			curKills.setFilm(jediMovie.asVertex().getId().toString());
			curKills = lando.addKills(anakin);
			curKills.setFilm(jediMovie.asVertex().getId().toString());
			curKills = anakin.addKills(palpatine);
			curKills.setFilm(jediMovie.asVertex().getId().toString());
			curKills = maul.addKills(quigon);
			curKills.setFilm(phantomMovie.asVertex().getId().toString());
			curKills = obiwan.addKills(maul);
			curKills.setFilm(phantomMovie.asVertex().getId().toString());
			curKills = han.addKills(greedo);
			curKills.setFilm(newhopeMovie.asVertex().getId().toString());
			curKills = han.addKills(boba);
			curKills.setFilm(jediMovie.asVertex().getId().toString());
			curKills = anakin.addKills(tyranus);
			curKills.setFilm(revengeMovie.asVertex().getId().toString());

			anakin.addSpawns(threepio);
			anakin.addSpawns(luke);
			anakin.addSpawns(leia);
			padme.addSpawns(luke);
			padme.addSpawns(leia);
			shmi.addSpawns(anakin);
			jango.addSpawns(boba);

			graph.commit();

		} catch (Throwable t) {
			t.printStackTrace();
		}
		long testEndTime = System.nanoTime();
		System.out.println("Completed " + getClass().getSimpleName() + " run in " + ((testEndTime - testStartTime) / 1000000) + " ms");
	}

}

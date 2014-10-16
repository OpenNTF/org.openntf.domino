package org.openntf.domino.tests.ntf;

import lotus.domino.NotesFactory;

import org.openntf.domino.Session;
import org.openntf.domino.graph2.impl.DConfiguration;
import org.openntf.domino.graph2.impl.DElementStore;
import org.openntf.domino.graph2.impl.DGraph;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.Adjacency;
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.FramedGraphFactory;
import com.tinkerpop.frames.FramedTransactionalGraph;
import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.Incidence;
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

	private static final String MV_SW = "Star Wars";
	private static final String MV_ESB = "The Empire Strikes Back";
	private static final String MV_RoJ = "Return of the Jedi";
	private static final String MV_PM = "The Phantom Menace";
	private static final String MV_AoC = "Attack of the Clones";
	private static final String MV_RoS = "Revenge of the Sith";

	private static final String CH_LS = "Luke Skywalker";
	private static final String CH_OWK = "Obi-Wan Kenobi";
	private static final String CH_AS = "Anakin Skywalker";
	private static final String CH_LO = "Leia Organa";
	private static final String CH_DS = "Darth Sidious";
	private static final String CH_WA = "Wedge Antilles";
	private static final String CH_Y = "Yoda";
	private static final String CH_QGJ = "Qui-Gon Jinn";
	private static final String CH_P = "Padme";
	private static final String CH_C3 = "C-3PO";
	private static final String CH_R2 = "R2D2";
	private static final String CH_DM = "Darth Maul";
	private static final String CH_CH = "Chewbacca";
	private static final String CH_HS = "Han Solo";
	private static final String CH_G = "Greedo";
	private static final String CH_LC = "Lando Calrissian";
	private static final String CH_GMT = "Grand Moff Tarkin";
	private static final String CH_MW = "Mace Windu";
	private static final String CH_JF = "Jango Fett";
	private static final String CH_BF = "Boba Fett";
	private static final String CH_SS = "Shmi Skywalker";
	private static final String CH_DT = "Darth Tyranus";

	private static final String DIR_GL = "George Lucas";
	private static final String DIR_RM = "Richard Marquand";
	private static final String DIR_IK = "Irvin Kershner";

	private static final String ACT_HF = "Harrison Ford";
	private static final String ACT_MH = "Mark Hammill";
	private static final String ACT_CF = "Carrie Fischer";
	private static final String ACT_AD = "Anthony Daniels";
	private static final String ACT_KB = "Kenny Baker";
	private static final String ACT_BDW = "Billy Dee Williams";
	private static final String ACT_DP = "David Prowse";
	private static final String ACT_AG = "Alec Guinness";
	private static final String ACT_PC = "Peter Cushing";
	private static final String ACT_PM = "Peter Mayhew";	//Chewie
	private static final String ACT_FO = "Frank Oz";	//Yoda's voice
	private static final String ACT_JB = "Jeremy Bulloch";	//Boba Fett
	private static final String ACT_CR = "Clive Revill"; 	//Palpatine's voice in ESB
	private static final String ACT_SS = "Sebastian Shaw";	//Old Anakin
	private static final String ACT_IM = "Ian McDiarmid";	//Palpatine
	private static final String ACT_LN = "Liam Neeson";
	private static final String ACT_EM = "Ewan McGregor";
	private static final String ACT_NP = "Natalie Portman";
	private static final String ACT_JL = "Jake Lloyd";	//young anakin
	private static final String ACT_HC = "Hayden Christensen"; 	//bitch anakin
	private static final String ACT_PA = "Pernill August";	//shmi skywalker
	private static final String ACT_RP = "Ray Park";	//Darth maul
	private static final String ACT_SLJ = "Samuel L. Jackson";
	private static final String ACT_CL = "Christopher Lee";	//Darth Tyranus
	private static final String ACT_TM = "Temuera Morrison";	//Jango Fett
	private static final String ACT_DL = "Daniel Logan";	//young boba fett
	private static final String ACT_PB = "Paul Blake";	//Greedo
	private static final String ACT_DLW = "Denis Lawson";	//wedge

	private static final String directedBy = "DirectedBy";
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

	@TypeValue("movie")
	public interface Movie extends DVertexFrame {
		@Property("title")
		public String getTitle();

		@Property("title")
		public void setTitle(String title);

		@Incidence(label = directedBy)
		public DirectedBy getDirectedBy();

		@Incidence(label = directedBy)
		public DirectedBy addDirectedBy(Crew crew);

		@Incidence(label = directedBy)
		public void removeDirectedBy(Crew crew);

		@Adjacency(label = directedBy)
		public Crew getDirectedByCrew();

		@Adjacency(label = directedBy)
		public Crew addDirectedByCrew(Crew crew);

		@Adjacency(label = directedBy)
		public Crew removeDirectedByCrew(Crew crew);

		@Incidence(label = starring)
		public Iterable<Starring> getStarring();

		@Incidence(label = starring)
		public Starring addStarring(Crew crew);

		@Incidence(label = starring)
		public void removeStarring(Crew crew);

		@Adjacency(label = starring)
		public Iterable<Crew> getStarringCrew();

		@Adjacency(label = starring)
		public Crew addStarringCrew(Crew crew);

		@Adjacency(label = starring)
		public Crew removeStarringCrew(Crew crew);

	}

	@TypeValue(directedBy)
	public interface DirectedBy extends DEdgeFrame {
		@Property("rating")
		public Integer getRating();

		@Property("rating")
		public void setRating(Integer rating);

		@OutVertex
		Crew getDirector();

		@InVertex
		Movie getMovie();

	}

	@TypeValue(starring)
	public interface Starring extends DEdgeFrame {
		@OutVertex
		Movie getMovie();

		@InVertex
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

		@Adjacency(label = appearsIn)
		public Iterable<Movie> getAppearsInMovies();

		@Incidence(label = appearsIn)
		public Iterable<AppearsIn> getAppearsIn();

		@Incidence(label = appearsIn)
		public AppearsIn addAppearsIn(Movie movie);

		@Incidence(label = appearsIn)
		public void removeAppearsIn(AppearsIn appearance);

		@Adjacency(label = portrays, direction = Direction.IN)
		public Iterable<Crew> getPortrayedByCrew();

		@Incidence(label = portrays, direction = Direction.IN)
		public Iterable<Portrays> getPortrayedBy();

		@Incidence(label = portrays, direction = Direction.IN)
		public Portrays addPortrayedBy(Crew crew);

		@Incidence(label = portrays, direction = Direction.IN)
		public void removePortrayedBy(Portrays portrayal);

		@Adjacency(label = kills, direction = Direction.IN)
		public Iterable<Character> getKilledByCharacters();

		@Incidence(label = kills, direction = Direction.IN)
		public Iterable<Kills> getKilledBy();

		@Incidence(label = kills, direction = Direction.IN)
		public Kills addKilledBy(Character character);

		@Incidence(label = kills, direction = Direction.IN)
		public void removeKilledBy(Kills kill);

		@Adjacency(label = kills, direction = Direction.OUT)
		public Iterable<Character> getKillsCharacters();

		@Incidence(label = kills, direction = Direction.OUT)
		public Iterable<Kills> getKills();

		@Incidence(label = kills, direction = Direction.OUT)
		public Kills addKills(Character character);

		@Incidence(label = kills, direction = Direction.OUT)
		public void removeKills(Kills kill);

		@Adjacency(label = spawns, direction = Direction.IN)
		public Iterable<Character> getSpawnedByCharacters();

		@Incidence(label = spawns, direction = Direction.IN)
		public Iterable<Spawns> getSpawnedBy();

		@Incidence(label = spawns, direction = Direction.IN)
		public Spawns addSpawnedBy(Character character);

		@Incidence(label = spawns, direction = Direction.IN)
		public void removeSpawnedBy(Spawns spawn);

		@Adjacency(label = spawns, direction = Direction.OUT)
		public Iterable<Character> getSpawnsCharacters();

		@Incidence(label = spawns, direction = Direction.OUT)
		public Iterable<Spawns> getSpawns();

		@Incidence(label = spawns, direction = Direction.OUT)
		public Spawns addSpawns(Character character);

		@Adjacency(label = spawns, direction = Direction.OUT)
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

		@Adjacency(label = portrays)
		public Iterable<Character> getPortraysCharacters();

		@Incidence(label = portrays)
		public Iterable<Portrays> getPortrayals();

		@Incidence(label = portrays)
		public Portrays addPortrayals(Character character);

		@Incidence(label = portrays)
		public void removePortrayals(Portrays portrayal);

		@Adjacency(label = starring)
		public Iterable<Movie> getStarsInMovies();

		@Incidence(label = starring)
		public Iterable<Starring> getStarsIn();

		@Incidence(label = starring)
		public Starring addStarsInMovie(Movie movie);

		@Adjacency(label = starring)
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
	}

	public void run1() {
		String crewId = "85257D640018A1B3";
		String movieId = "85257D640018AD81";
		String characterId = "85257D6B007ECB47";
		String edgeId = "85257D640018BCDF";
		long testStartTime = System.nanoTime();
		marktime = System.nanoTime();
		Session session = this.getSession();

		try {
			timelog("Beginning graph2 test...");

			DElementStore crewStore = new DElementStore();
			crewStore.setStoreKey(crewId);
			DElementStore movieStore = new DElementStore();
			movieStore.setStoreKey(movieId);
			DElementStore characterStore = new DElementStore();
			characterStore.setStoreKey(characterId);
			DElementStore edgeStore = new DElementStore();
			edgeStore.setStoreKey(edgeId);
			DConfiguration config = new DConfiguration();
			config.addElementStore(crewStore);
			config.addElementStore(movieStore);
			config.addElementStore(characterStore);
			config.addElementStore(edgeStore);
			config.setDefaultElementStore(edgeId);
			DGraph graph = new DGraph(config);

			JavaHandlerModule jhm = new JavaHandlerModule();

			Module module = new TypedGraphModuleBuilder().withClass(Movie.class).withClass(Character.class).withClass(Crew.class).build();
			FramedGraphFactory factory = new FramedGraphFactory(module, jhm);

			FramedTransactionalGraph<DGraph> framedGraph = factory.create(graph);

			Movie newhopeMovie = framedGraph.addVertex(movieId + MV_SW, Movie.class);
			newhopeMovie.setTitle(MV_SW);
			Vertex newhope = newhopeMovie.asVertex();

			Movie empireMovie = framedGraph.addVertex(movieId + MV_ESB, Movie.class);
			empireMovie.setTitle(MV_ESB);
			Vertex empire = empireMovie.asVertex();

			Movie jediMovie = framedGraph.addVertex(movieId + MV_RoJ, Movie.class);
			jediMovie.setTitle(MV_RoJ);
			Vertex jedi = jediMovie.asVertex();

			Movie phantomMovie = framedGraph.addVertex(movieId + MV_PM, Movie.class);
			phantomMovie.setTitle(MV_PM);
			Vertex phantom = phantomMovie.asVertex();

			Movie clonesMovie = framedGraph.addVertex(movieId + MV_AoC, Movie.class);
			clonesMovie.setTitle(MV_AoC);
			Vertex clones = clonesMovie.asVertex();

			Movie revengeMovie = framedGraph.addVertex(movieId + MV_RoS, Movie.class);
			revengeMovie.setTitle(MV_RoS);
			Vertex revenge = revengeMovie.asVertex();

			Crew lucasCrew = framedGraph.addVertex(crewId + DIR_GL, Crew.class);
			lucasCrew.setFirstName("George");
			lucasCrew.setLastName("Lucas");
			//			Vertex lucas = lucasCrew.asVertex();

			Crew kershnerCrew = framedGraph.addVertex(crewId + DIR_IK, Crew.class);
			kershnerCrew.setFirstName("Irvin");
			kershnerCrew.setLastName("Kershner");
			//			Vertex kershner = kershnerCrew.asVertex();

			Crew marquandCrew = framedGraph.addVertex(crewId + DIR_RM, Crew.class);
			marquandCrew.setFirstName("Richard");
			marquandCrew.setLastName("Marquand");
			//			Vertex marquand = marquandCrew.asVertex();

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

			Character luke = framedGraph.addVertex(characterId + CH_LS, Character.class);
			luke.addAppearsIn(newhopeMovie);
			luke.addAppearsIn(empireMovie);
			luke.addAppearsIn(jediMovie);
			luke.setName(CH_LS);
			Character leia = framedGraph.addVertex(characterId + CH_LO, Character.class);
			leia.addAppearsIn(newhopeMovie);
			leia.addAppearsIn(empireMovie);
			leia.addAppearsIn(jediMovie);
			leia.setName(CH_LO);
			Character han = framedGraph.addVertex(characterId + CH_HS, Character.class);
			han.addAppearsIn(newhopeMovie);
			han.addAppearsIn(empireMovie);
			han.addAppearsIn(jediMovie);
			han.setName(CH_HS);
			Character chewy = framedGraph.addVertex(characterId + CH_CH, Character.class);
			chewy.addAppearsIn(newhopeMovie);
			chewy.addAppearsIn(empireMovie);
			chewy.addAppearsIn(jediMovie);
			chewy.addAppearsIn(revengeMovie);
			chewy.setName(CH_CH);
			Character threepio = framedGraph.addVertex(characterId + CH_C3, Character.class);
			threepio.addAppearsIn(newhopeMovie);
			threepio.addAppearsIn(empireMovie);
			threepio.addAppearsIn(jediMovie);
			threepio.addAppearsIn(phantomMovie);
			threepio.addAppearsIn(clonesMovie);
			threepio.addAppearsIn(revengeMovie);
			threepio.setName(CH_C3);
			Character artoo = framedGraph.addVertex(characterId + CH_R2, Character.class);
			artoo.addAppearsIn(newhopeMovie);
			artoo.addAppearsIn(empireMovie);
			artoo.addAppearsIn(jediMovie);
			artoo.addAppearsIn(phantomMovie);
			artoo.addAppearsIn(clonesMovie);
			artoo.addAppearsIn(revengeMovie);
			artoo.setName(CH_R2);
			Character lando = framedGraph.addVertex(characterId + CH_LC, Character.class);
			lando.addAppearsIn(empireMovie);
			lando.addAppearsIn(jediMovie);
			lando.setName(CH_LC);
			Character anakin = framedGraph.addVertex(characterId + CH_AS, Character.class);
			anakin.addAppearsIn(newhopeMovie);
			anakin.addAppearsIn(empireMovie);
			anakin.addAppearsIn(jediMovie);
			anakin.addAppearsIn(phantomMovie);
			anakin.addAppearsIn(clonesMovie);
			anakin.addAppearsIn(revengeMovie);
			anakin.setName(CH_AS);
			Character palpatine = framedGraph.addVertex(characterId + CH_DS, Character.class);
			palpatine.addAppearsIn(empireMovie);
			palpatine.addAppearsIn(jediMovie);
			palpatine.addAppearsIn(phantomMovie);
			palpatine.addAppearsIn(clonesMovie);
			palpatine.addAppearsIn(revengeMovie);
			palpatine.setName(CH_DS);
			Character obiwan = framedGraph.addVertex(characterId + CH_OWK, Character.class);
			obiwan.addAppearsIn(newhopeMovie);
			obiwan.addAppearsIn(empireMovie);
			obiwan.addAppearsIn(jediMovie);
			obiwan.addAppearsIn(phantomMovie);
			obiwan.addAppearsIn(clonesMovie);
			obiwan.addAppearsIn(revengeMovie);
			obiwan.setName(CH_OWK);
			Character quigon = framedGraph.addVertex(characterId + CH_QGJ, Character.class);
			quigon.addAppearsIn(phantomMovie);
			quigon.setName(CH_QGJ);
			Character yoda = framedGraph.addVertex(characterId + CH_Y, Character.class);
			yoda.addAppearsIn(empireMovie);
			yoda.addAppearsIn(jediMovie);
			yoda.addAppearsIn(phantomMovie);
			yoda.addAppearsIn(clonesMovie);
			yoda.addAppearsIn(revengeMovie);
			yoda.setName(CH_Y);
			Character jango = framedGraph.addVertex(characterId + CH_JF, Character.class);
			jango.addAppearsIn(clonesMovie);
			jango.setName(CH_JF);
			Character boba = framedGraph.addVertex(characterId + CH_BF, Character.class);
			boba.addAppearsIn(empireMovie);
			boba.addAppearsIn(jediMovie);
			boba.addAppearsIn(clonesMovie);
			boba.setName(CH_BF);
			Character padme = framedGraph.addVertex(characterId + CH_P, Character.class);
			padme.addAppearsIn(phantomMovie);
			padme.addAppearsIn(clonesMovie);
			padme.addAppearsIn(revengeMovie);
			padme.setName(CH_P);
			Character shmi = framedGraph.addVertex(characterId + CH_SS, Character.class);
			shmi.addAppearsIn(phantomMovie);
			shmi.addAppearsIn(clonesMovie);
			shmi.setName(CH_SS);
			Character tyranus = framedGraph.addVertex(characterId + CH_DT, Character.class);
			tyranus.addAppearsIn(clonesMovie);
			tyranus.addAppearsIn(revengeMovie);
			tyranus.setName(CH_DT);
			Character maul = framedGraph.addVertex(characterId + CH_DM, Character.class);
			maul.addAppearsIn(phantomMovie);
			maul.addAppearsIn(clonesMovie);
			maul.setName(CH_DM);
			Character tarkin = framedGraph.addVertex(characterId + CH_GMT, Character.class);
			tarkin.addAppearsIn(newhopeMovie);
			tarkin.setName(CH_GMT);
			Character windu = framedGraph.addVertex(characterId + CH_MW, Character.class);
			windu.addAppearsIn(phantomMovie);
			windu.addAppearsIn(clonesMovie);
			windu.addAppearsIn(revengeMovie);
			windu.setName(CH_MW);
			Character greedo = framedGraph.addVertex(characterId + CH_G, Character.class);
			greedo.addAppearsIn(newhopeMovie);
			greedo.setName(CH_G);
			Character wedge = framedGraph.addVertex(characterId + CH_WA, Character.class);
			wedge.addAppearsIn(newhopeMovie);
			wedge.addAppearsIn(empireMovie);
			wedge.addAppearsIn(jediMovie);
			wedge.setName(CH_WA);

			Crew ford = framedGraph.addVertex(crewId + ACT_HF, Crew.class);
			ford.setFullName(ACT_HF);
			ford.addStarsInMovie(newhopeMovie);
			ford.addStarsInMovie(empireMovie);
			ford.addStarsInMovie(jediMovie);
			han.addPortrayedBy(ford);

			Crew fischer = framedGraph.addVertex(crewId + ACT_CF, Crew.class);
			fischer.setFullName(ACT_CF);
			fischer.addStarsInMovie(newhopeMovie);
			fischer.addStarsInMovie(empireMovie);
			fischer.addStarsInMovie(jediMovie);
			fischer.addPortrayals(leia);

			Crew hammill = framedGraph.addVertex(crewId + ACT_MH, Crew.class);
			hammill.setFullName(ACT_MH);
			hammill.addStarsInMovie(newhopeMovie);
			hammill.addStarsInMovie(empireMovie);
			hammill.addStarsInMovie(jediMovie);
			hammill.addPortrayals(luke);

			Crew daniels = framedGraph.addVertex(crewId + ACT_AD, Crew.class);
			daniels.setFullName(ACT_AD);
			daniels.addStarsInMovie(newhopeMovie);
			daniels.addStarsInMovie(empireMovie);
			daniels.addStarsInMovie(jediMovie);
			daniels.addStarsInMovie(phantomMovie);
			daniels.addStarsInMovie(clonesMovie);
			daniels.addStarsInMovie(revengeMovie);
			daniels.addPortrayals(threepio);

			Crew baker = framedGraph.addVertex(crewId + ACT_KB, Crew.class);
			baker.setFullName(ACT_KB);
			baker.addStarsInMovie(newhopeMovie);
			baker.addStarsInMovie(empireMovie);
			baker.addStarsInMovie(jediMovie);
			baker.addStarsInMovie(phantomMovie);
			baker.addStarsInMovie(clonesMovie);
			baker.addStarsInMovie(revengeMovie);
			baker.addPortrayals(artoo);

			Crew prowse = framedGraph.addVertex(crewId + ACT_DP, Crew.class);
			prowse.setFullName(ACT_DP);
			prowse.addStarsInMovie(newhopeMovie);
			prowse.addStarsInMovie(empireMovie);
			prowse.addStarsInMovie(jediMovie);
			prowse.addPortrayals(anakin);

			Crew williams = framedGraph.addVertex(crewId + ACT_BDW, Crew.class);
			williams.setFullName(ACT_BDW);
			williams.addStarsInMovie(empireMovie);
			williams.addStarsInMovie(jediMovie);
			williams.addPortrayals(lando);

			Crew guinness = framedGraph.addVertex(crewId + ACT_AG, Crew.class);
			guinness.setFullName(ACT_AG);
			guinness.addStarsInMovie(newhopeMovie);
			guinness.addStarsInMovie(empireMovie);
			guinness.addStarsInMovie(jediMovie);
			guinness.addPortrayals(obiwan);

			Crew ewan = framedGraph.addVertex(crewId + ACT_EM, Crew.class);
			ewan.setFullName(ACT_EM);
			ewan.addStarsInMovie(phantomMovie);
			ewan.addStarsInMovie(clonesMovie);
			ewan.addStarsInMovie(revengeMovie);
			ewan.addPortrayals(obiwan);

			Crew cushing = framedGraph.addVertex(crewId + ACT_PC, Crew.class);
			cushing.setFullName(ACT_PC);
			cushing.addStarsInMovie(newhopeMovie);
			cushing.addPortrayals(tarkin);

			Crew mayhew = framedGraph.addVertex(crewId + ACT_PM, Crew.class);
			mayhew.setFullName(ACT_PM);
			mayhew.addStarsInMovie(newhopeMovie);
			mayhew.addStarsInMovie(empireMovie);
			mayhew.addStarsInMovie(jediMovie);
			mayhew.addStarsInMovie(revengeMovie);
			mayhew.addPortrayals(chewy);

			Crew oz = framedGraph.addVertex(crewId + ACT_FO, Crew.class);
			oz.setFullName(ACT_FO);
			oz.addStarsInMovie(empireMovie);
			oz.addStarsInMovie(jediMovie);
			oz.addStarsInMovie(phantomMovie);
			oz.addStarsInMovie(clonesMovie);
			oz.addStarsInMovie(revengeMovie);
			oz.addPortrayals(yoda);

			Crew bulloch = framedGraph.addVertex(crewId + ACT_JB, Crew.class);
			bulloch.setFullName(ACT_JB);
			bulloch.addStarsInMovie(empireMovie);
			bulloch.addStarsInMovie(jediMovie);
			bulloch.addPortrayals(boba);

			Crew revill = framedGraph.addVertex(crewId + ACT_CR, Crew.class);
			revill.setFullName(ACT_CR);
			revill.addStarsInMovie(empireMovie);
			revill.addPortrayals(palpatine);

			Crew ian = framedGraph.addVertex(crewId + ACT_IM, Crew.class);
			ian.setFullName(ACT_IM);
			ian.addStarsInMovie(jediMovie);
			ian.addStarsInMovie(phantomMovie);
			ian.addStarsInMovie(clonesMovie);
			ian.addStarsInMovie(revengeMovie);
			ian.addPortrayals(palpatine);

			Crew shaw = framedGraph.addVertex(crewId + ACT_SS, Crew.class);
			shaw.setFullName(ACT_SS);
			shaw.addStarsInMovie(jediMovie);
			shaw.addPortrayals(anakin);

			Crew liam = framedGraph.addVertex(crewId + ACT_LN, Crew.class);
			liam.setFullName(ACT_LN);
			liam.addStarsInMovie(phantomMovie);
			liam.addPortrayals(quigon);

			Crew portman = framedGraph.addVertex(crewId + ACT_NP, Crew.class);
			portman.setFullName(ACT_NP);
			portman.addStarsInMovie(phantomMovie);
			portman.addStarsInMovie(clonesMovie);
			portman.addStarsInMovie(revengeMovie);
			portman.addPortrayals(padme);

			Crew lloyd = framedGraph.addVertex(crewId + ACT_JL, Crew.class);
			lloyd.setFullName(ACT_JL);
			lloyd.addStarsInMovie(phantomMovie);
			lloyd.addPortrayals(anakin);

			Crew hayden = framedGraph.addVertex(crewId + ACT_HC, Crew.class);
			hayden.setFullName(ACT_HC);
			hayden.addStarsInMovie(clonesMovie);
			hayden.addStarsInMovie(revengeMovie);
			hayden.addPortrayals(anakin);

			Crew august = framedGraph.addVertex(crewId + ACT_PA, Crew.class);
			august.setFullName(ACT_PA);
			august.addStarsInMovie(phantomMovie);
			august.addStarsInMovie(clonesMovie);
			august.addPortrayals(shmi);

			Crew park = framedGraph.addVertex(crewId + ACT_RP, Crew.class);
			park.setFullName(ACT_RP);
			park.addStarsInMovie(phantomMovie);
			park.addPortrayals(maul);

			Crew samuel = framedGraph.addVertex(crewId + ACT_SLJ, Crew.class);
			samuel.setFullName(ACT_SLJ);
			samuel.addStarsInMovie(phantomMovie);
			samuel.addStarsInMovie(clonesMovie);
			samuel.addStarsInMovie(revengeMovie);
			samuel.addPortrayals(windu);

			Crew lee = framedGraph.addVertex(crewId + ACT_CL, Crew.class);
			lee.setFullName(ACT_CL);
			lee.addStarsInMovie(clonesMovie);
			lee.addStarsInMovie(revengeMovie);
			lee.addPortrayals(tyranus);

			Crew morrison = framedGraph.addVertex(crewId + ACT_TM, Crew.class);
			morrison.setFullName(ACT_TM);
			morrison.addStarsInMovie(clonesMovie);
			morrison.addPortrayals(jango);

			Crew logan = framedGraph.addVertex(crewId + ACT_DL, Crew.class);
			logan.setFullName(ACT_DL);
			logan.addStarsInMovie(clonesMovie);
			logan.addPortrayals(boba);

			Crew blake = framedGraph.addVertex(crewId + ACT_PB, Crew.class);
			blake.setFullName(ACT_PB);
			blake.addStarsInMovie(newhopeMovie);
			blake.addPortrayals(greedo);

			Crew lawson = framedGraph.addVertex(crewId + ACT_DLW, Crew.class);
			lawson.setFullName(ACT_DLW);
			lawson.addStarsInMovie(newhopeMovie);
			lawson.addStarsInMovie(empireMovie);
			lawson.addStarsInMovie(jediMovie);
			lawson.addPortrayals(wedge);

			Kills curEdge = null;
			curEdge = anakin.addKills(obiwan);
			curEdge.setFilm(newhope.getId().toString());
			luke.addKills(tarkin);
			curEdge.setFilm(newhope.getId().toString());
			windu.addKills(jango);
			curEdge.setFilm(clones.getId().toString());
			palpatine.addKills(windu);
			curEdge.setFilm(revenge.getId().toString());
			wedge.addKills(anakin);
			curEdge.setFilm(jedi.getId().toString());
			lando.addKills(anakin);
			curEdge.setFilm(jedi.getId().toString());
			anakin.addKills(palpatine);
			curEdge.setFilm(jedi.getId().toString());
			maul.addKills(quigon);
			curEdge.setFilm(phantom.getId().toString());
			obiwan.addKills(maul);
			curEdge.setFilm(phantom.getId().toString());
			han.addKills(greedo);
			curEdge.setFilm(newhope.getId().toString());
			han.addKills(boba);
			curEdge.setFilm(jedi.getId().toString());
			anakin.addKills(tyranus);
			curEdge.setFilm(revenge.getId().toString());

			anakin.addSpawns(threepio);
			anakin.addSpawns(luke);
			anakin.addSpawns(leia);
			padme.addSpawns(luke);
			padme.addSpawns(leia);
			shmi.addSpawns(anakin);
			jango.addSpawns(boba);

			System.out.println("Starting GraphJung...");

			graph.commit();

		} catch (Throwable t) {
			t.printStackTrace();
		}
		long testEndTime = System.nanoTime();
		System.out.println("Completed " + getClass().getSimpleName() + " run in " + ((testEndTime - testStartTime) / 1000000) + " ms");
	}

	protected Session getSession() {
		try {
			Session session = Factory.fromLotus(NotesFactory.createSession(), Session.SCHEMA, null);
			return session;
		} catch (Throwable t) {
			DominoUtils.handleException(t);
			return null;
		}
	}
}

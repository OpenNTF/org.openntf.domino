package org.openntf.domino.tests.ntf;

import java.awt.Dimension;

import javax.swing.JFrame;

import lotus.domino.NotesFactory;

import org.apache.commons.collections15.Transformer;
import org.openntf.domino.Session;
import org.openntf.domino.graph2.impl.DConfiguration;
import org.openntf.domino.graph2.impl.DElementStore;
import org.openntf.domino.graph2.impl.DGraph;
import org.openntf.domino.graph2.impl.DTypedPropertyHandler;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.oupls.jung.GraphJung;
import com.tinkerpop.frames.Adjacency;
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.FramedGraphConfiguration;
import com.tinkerpop.frames.FramedGraphFactory;
import com.tinkerpop.frames.FramedTransactionalGraph;
import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.Incidence;
import com.tinkerpop.frames.OutVertex;
import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.VertexFrame;
import com.tinkerpop.frames.modules.typedgraph.TypeField;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;
import com.tinkerpop.frames.modules.typedgraph.TypedGraphModuleBuilder;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;

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

	public interface User extends DVertexFrame {
		@Property("name")
		public String getName();

		@Property("name")
		public void setName(String name);
	}

	public interface DWorkflowVertexFrame extends DVertexFrame {
		@Adjacency(label = "submitted")
		public User getSubmittedBy();

		@Adjacency(label = "submitted")
		public void setSubmittedBy(User submitted);
	}

	public abstract class WorkflowInstance implements DWorkflowVertexFrame {

	}

	public interface DSocialVertexFrame extends DVertexFrame {
		@Adjacency(label = "commented")
		public Iterable<Comment> getComments();

		@Adjacency(label = "commented")
		public Comment addComment(Comment comment);

		@Adjacency(label = "commented")
		public Comment removeComments(Comment comment);
	}

	public interface Comment extends DVertexFrame {
		@Property("body")
		public String getBody();

		@Property("body")
		public void setBody(String body);
	}

	@TypeValue("movie")
	public interface Movie extends DSocialVertexFrame {
		@Property("title")
		public String getTitle();

		@Property("title")
		public void setTitle(String title);

		@Incidence(label = "directedBy")
		public Iterable<DirectedBy> getDirectedBy();

		@Incidence(label = "directedBy")
		public DirectedBy addDirectedBy(Crew crew);

		@Incidence(label = "directedBy")
		public void removeDirectedBy(Crew crew);

		@Adjacency(label = "directedBy")
		public Crew getDirectedByCrew();

		@Adjacency(label = "directedBy")
		public Crew addDirectedByCrew(Crew crew);

		@Adjacency(label = "directedBy")
		public Crew removeDirectedByCrew(Crew crew);

		@Adjacency(label = "starring")
		public Iterable<Crew> getStarringCrew();

		@Adjacency(label = "starring")
		public Crew addStarringCrew(Crew crew);

		@Adjacency(label = "starring")
		public Crew removeStarringCrew(Crew crew);

	}

	public interface DirectedBy extends EdgeFrame {
		@Property("rating")
		public Integer getRating();

		@Property("rating")
		public void setRating(Integer rating);

		@OutVertex
		Crew getDirector();

		@InVertex
		Movie getMovie();

	}

	@TypeValue("character")
	public interface Character extends DVertexFrame {
		@Property("name")
		public String getName();

		@Property("name")
		public void setName(String name);

		@Adjacency(label = "appearsIn")
		public Iterable<Movie> getAppearsInMovies();

		@Adjacency(label = "appearsIn")
		public Edge addAppearsInMovies(Movie movie);

		@Adjacency(label = "appearsIn")
		public void removeAppearsInMovies(Movie movie);

	}

	@TypeValue("crew")
	public interface Crew extends DVertexFrame {
		abstract class CrewImpl implements Crew {
			@Override
			public void setFullName(final String fullName) {
				int pos = fullName.lastIndexOf(' ');
				setFirstName(fullName.substring(0, pos - 1));
				setLastName(fullName.substring(pos + 1));
			}
		}

		@Property("firstName")
		public String getFirstName();

		@Property("lastName")
		public String getLastName();

		@Property("firstName")
		public void setFirstName(String firstName);

		@Property("lastName")
		public void setLastName(String lastName);

		public void setFullName(String fullName);

		@Adjacency(label = "portrays")
		public Iterable<Character> getPortraysCharacters();

		@Adjacency(label = "portrays")
		public Edge addPortraysCharacter(Character character);

		@Adjacency(label = "portrays")
		public void removePortraysCharacter(Character character);

		@Adjacency(label = "starring")
		public Iterable<Movie> getStarsInMovies();

		@Adjacency(label = "starring")
		public Edge addStarsInMovie(Movie movie);

		@Adjacency(label = "starring")
		public void removeStarsInMovie(Movie movie);

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
		//		String crewId = "85257D640018A1B3";
		//		String movieId = "85257D640018AD81";
		//		String characterId = "85257D6B007ECB47";
		//		String edgeId = "85257D640018BCDF";
		String crewId = "80257D6D0035CE87";
		String movieId = "80257D6D0035D5A8";
		String characterId = "80257D6D0035DAD9";
		String edgeId = "80257D6D0035E02A";
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

			FramedGraphFactory factory = new FramedGraphFactory(new TypedGraphModuleBuilder().withClass(Movie.class)
					.withClass(Character.class).withClass(Crew.class).build());
			FramedGraphConfiguration frameConfig = new FramedGraphConfiguration();
			frameConfig.addMethodHandler(new DTypedPropertyHandler());

			FramedTransactionalGraph<DGraph> framedGraph = factory.create(graph);

			//	framedGraph.registerAnnotationHandler(new DTypedPropertyHandler());

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

			//			Vertex luke = graph.addVertex(characterId + CH_LS);
			//			graph.addEdge(null, luke.asVertex(), newhope, appearsIn);
			//			graph.addEdge(null, luke, empire, appearsIn);
			//			graph.addEdge(null, luke, jedi, appearsIn);
			Character luke = framedGraph.addVertex(characterId + CH_LS, Character.class);
			luke.addAppearsInMovies(newhopeMovie);
			luke.addAppearsInMovies(empireMovie);
			luke.addAppearsInMovies(jediMovie);
			Vertex leia = graph.addVertex(characterId + CH_LO);
			graph.addEdge(null, leia, newhope, appearsIn);
			graph.addEdge(null, leia, empire, appearsIn);
			graph.addEdge(null, leia, jedi, appearsIn);
			Vertex han = graph.addVertex(characterId + CH_HS);
			graph.addEdge(null, han, newhope, appearsIn);
			graph.addEdge(null, han, empire, appearsIn);
			graph.addEdge(null, han, jedi, appearsIn);
			Vertex chewy = graph.addVertex(characterId + CH_CH);
			graph.addEdge(null, chewy, newhope, appearsIn);
			graph.addEdge(null, chewy, empire, appearsIn);
			graph.addEdge(null, chewy, jedi, appearsIn);
			Vertex threepio = graph.addVertex(characterId + CH_C3);
			graph.addEdge(null, threepio, newhope, appearsIn);
			graph.addEdge(null, threepio, empire, appearsIn);
			graph.addEdge(null, threepio, jedi, appearsIn);
			graph.addEdge(null, threepio, phantom, appearsIn);
			graph.addEdge(null, threepio, clones, appearsIn);
			graph.addEdge(null, threepio, revenge, appearsIn);
			Vertex artoo = graph.addVertex(characterId + CH_R2);
			graph.addEdge(null, artoo, newhope, appearsIn);
			graph.addEdge(null, artoo, empire, appearsIn);
			graph.addEdge(null, artoo, jedi, appearsIn);
			graph.addEdge(null, artoo, phantom, appearsIn);
			graph.addEdge(null, artoo, clones, appearsIn);
			graph.addEdge(null, artoo, revenge, appearsIn);
			Vertex lando = graph.addVertex(characterId + CH_LC);
			graph.addEdge(null, lando, empire, appearsIn);
			graph.addEdge(null, lando, jedi, appearsIn);
			Vertex anakin = graph.addVertex(characterId + CH_AS);
			graph.addEdge(null, anakin, newhope, appearsIn);
			graph.addEdge(null, anakin, empire, appearsIn);
			graph.addEdge(null, anakin, jedi, appearsIn);
			graph.addEdge(null, anakin, phantom, appearsIn);
			graph.addEdge(null, anakin, clones, appearsIn);
			graph.addEdge(null, anakin, revenge, appearsIn);
			Vertex palpatine = graph.addVertex(characterId + CH_DS);
			graph.addEdge(null, palpatine, empire, appearsIn);
			graph.addEdge(null, palpatine, jedi, appearsIn);
			graph.addEdge(null, palpatine, phantom, appearsIn);
			graph.addEdge(null, palpatine, clones, appearsIn);
			graph.addEdge(null, palpatine, revenge, appearsIn);
			Vertex obiwan = graph.addVertex(characterId + CH_OWK);
			graph.addEdge(null, obiwan, newhope, appearsIn);
			graph.addEdge(null, obiwan, empire, appearsIn);
			graph.addEdge(null, obiwan, jedi, appearsIn);
			graph.addEdge(null, obiwan, phantom, appearsIn);
			graph.addEdge(null, obiwan, clones, appearsIn);
			graph.addEdge(null, obiwan, revenge, appearsIn);
			Vertex quigon = graph.addVertex(characterId + CH_QGJ);
			graph.addEdge(null, quigon, phantom, appearsIn);
			Vertex yoda = graph.addVertex(characterId + CH_Y);
			graph.addEdge(null, yoda, empire, appearsIn);
			graph.addEdge(null, yoda, jedi, appearsIn);
			graph.addEdge(null, yoda, phantom, appearsIn);
			graph.addEdge(null, yoda, clones, appearsIn);
			graph.addEdge(null, yoda, revenge, appearsIn);
			Vertex jango = graph.addVertex(characterId + CH_JF);
			graph.addEdge(null, jango, clones, appearsIn);
			graph.addEdge(null, jango, revenge, appearsIn);
			Vertex boba = graph.addVertex(characterId + CH_BF);
			graph.addEdge(null, boba, empire, appearsIn);
			graph.addEdge(null, boba, jedi, appearsIn);
			graph.addEdge(null, boba, clones, appearsIn);
			graph.addEdge(null, boba, revenge, appearsIn);
			Vertex padme = graph.addVertex(characterId + CH_P);
			graph.addEdge(null, padme, phantom, appearsIn);
			graph.addEdge(null, padme, clones, appearsIn);
			graph.addEdge(null, padme, revenge, appearsIn);
			Vertex shmi = graph.addVertex(characterId + CH_SS);
			graph.addEdge(null, shmi, phantom, appearsIn);
			graph.addEdge(null, shmi, clones, appearsIn);
			Vertex tyranus = graph.addVertex(characterId + CH_DT);
			graph.addEdge(null, tyranus, clones, appearsIn);
			graph.addEdge(null, tyranus, revenge, appearsIn);
			Vertex maul = graph.addVertex(characterId + CH_DM);
			graph.addEdge(null, maul, phantom, appearsIn);
			Vertex tarkin = graph.addVertex(characterId + CH_GMT);
			graph.addEdge(null, tarkin, newhope, appearsIn);
			Vertex windu = graph.addVertex(characterId + CH_MW);
			graph.addEdge(null, windu, phantom, appearsIn);
			graph.addEdge(null, windu, clones, appearsIn);
			graph.addEdge(null, windu, revenge, appearsIn);
			Vertex greedo = graph.addVertex(characterId + CH_G);
			graph.addEdge(null, greedo, newhope, appearsIn);
			Vertex wedge = graph.addVertex(characterId + CH_WA);
			graph.addEdge(null, wedge, newhope, appearsIn);
			graph.addEdge(null, wedge, empire, appearsIn);
			graph.addEdge(null, wedge, jedi, appearsIn);

			Vertex ford = graph.addVertex(crewId + ACT_HF);

			ford.setProperty("firstName", "Harrison");
			ford.setProperty("lastName", "Ford");
			graph.addEdge(null, newhope, ford, starring);
			graph.addEdge(null, empire, ford, starring);
			graph.addEdge(null, jedi, ford, starring);
			graph.addEdge(null, ford, han, portrays);

			Vertex fischer = graph.addVertex(crewId + "Carrie Fischer");
			fischer.setProperty("firstName", "Carrie");
			fischer.setProperty("lastName", "Fischer");
			graph.addEdge(null, newhope, fischer, starring);
			graph.addEdge(null, empire, fischer, starring);
			graph.addEdge(null, jedi, fischer, starring);
			graph.addEdge(null, fischer, leia, portrays);

			Vertex hammill = graph.addVertex(crewId + ACT_MH);
			hammill.setProperty("firstName", "Mark");
			hammill.setProperty("lastName", "Hammill");
			graph.addEdge(null, newhope, hammill, starring);
			graph.addEdge(null, empire, hammill, starring);
			graph.addEdge(null, jedi, hammill, starring);
			graph.addEdge(null, hammill, luke.asVertex(), portrays);

			Vertex daniels = graph.addVertex(crewId + ACT_AD);
			daniels.setProperty("firstName", "Anthony");
			daniels.setProperty("lastName", "Daniels");
			graph.addEdge(null, newhope, daniels, starring);
			graph.addEdge(null, empire, daniels, starring);
			graph.addEdge(null, jedi, daniels, starring);
			graph.addEdge(null, phantom, daniels, starring);
			graph.addEdge(null, clones, daniels, starring);
			graph.addEdge(null, revenge, daniels, starring);
			graph.addEdge(null, daniels, threepio, portrays);

			Vertex baker = graph.addVertex(crewId + ACT_KB);
			baker.setProperty("firstName", "Kenny");
			baker.setProperty("lastName", "Baker");
			graph.addEdge(null, newhope, baker, starring);
			graph.addEdge(null, empire, baker, starring);
			graph.addEdge(null, jedi, baker, starring);
			graph.addEdge(null, phantom, baker, starring);
			graph.addEdge(null, clones, baker, starring);
			graph.addEdge(null, revenge, baker, starring);
			graph.addEdge(null, baker, artoo, portrays);

			Vertex prowse = graph.addVertex(crewId + ACT_DP);
			prowse.setProperty("firstName", "David");
			prowse.setProperty("lastName", "Prowse");
			graph.addEdge(null, newhope, prowse, starring);
			graph.addEdge(null, empire, prowse, starring);
			graph.addEdge(null, jedi, prowse, starring);
			graph.addEdge(null, prowse, anakin, portrays);

			Vertex williams = graph.addVertex(crewId + ACT_BDW);
			williams.setProperty("firstName", "Billy Dee");
			williams.setProperty("lastName", "Williams");
			graph.addEdge(null, empire, williams, starring);
			graph.addEdge(null, jedi, williams, starring);
			graph.addEdge(null, williams, lando, portrays);

			Vertex guinness = graph.addVertex(crewId + ACT_AG);
			guinness.setProperty("firstName", "Alec");
			guinness.setProperty("lastName", "Guinness");
			graph.addEdge(null, newhope, guinness, starring);
			graph.addEdge(null, empire, guinness, starring);
			graph.addEdge(null, jedi, guinness, starring);
			graph.addEdge(null, guinness, obiwan, portrays);

			Vertex ewan = graph.addVertex(crewId + ACT_EM);
			ewan.setProperty("firstName", "Ewan");
			ewan.setProperty("lastName", "McGregor");
			graph.addEdge(null, phantom, ewan, starring);
			graph.addEdge(null, clones, ewan, starring);
			graph.addEdge(null, revenge, ewan, starring);
			graph.addEdge(null, ewan, obiwan, portrays);

			Vertex cushing = graph.addVertex(crewId + ACT_PC);
			cushing.setProperty("firstName", "Peter");
			cushing.setProperty("lastName", "Cushing");
			graph.addEdge(null, newhope, cushing, starring);
			graph.addEdge(null, cushing, tarkin, portrays);

			Vertex mayhew = graph.addVertex(crewId + ACT_PM);
			mayhew.setProperty("firstName", "Peter");
			mayhew.setProperty("lastName", "Mayhew");
			graph.addEdge(null, newhope, mayhew, starring);
			graph.addEdge(null, empire, mayhew, starring);
			graph.addEdge(null, jedi, mayhew, starring);
			graph.addEdge(null, revenge, mayhew, starring);
			graph.addEdge(null, mayhew, chewy, portrays);

			Vertex oz = graph.addVertex(crewId + ACT_FO);
			oz.setProperty("firstName", "Frank");
			oz.setProperty("lastName", "Oz");
			graph.addEdge(null, phantom, oz, starring);
			graph.addEdge(null, empire, oz, starring);
			graph.addEdge(null, jedi, oz, starring);
			graph.addEdge(null, revenge, oz, starring);
			graph.addEdge(null, clones, oz, starring);
			graph.addEdge(null, oz, yoda, portrays);

			Vertex bulloch = graph.addVertex(crewId + ACT_JB);
			bulloch.setProperty("firstName", "Jeremy");
			bulloch.setProperty("lastName", "Bulloch");
			graph.addEdge(null, empire, bulloch, starring);
			graph.addEdge(null, jedi, bulloch, starring);
			graph.addEdge(null, bulloch, boba, portrays);

			Vertex revill = graph.addVertex(crewId + ACT_CR);
			revill.setProperty("firstName", "Clive");
			revill.setProperty("lastName", "Revill");
			graph.addEdge(null, empire, revill, starring);
			graph.addEdge(null, revill, palpatine, portrays);

			Vertex ian = graph.addVertex(crewId + ACT_IM);
			ian.setProperty("firstName", "Ian");
			ian.setProperty("lastName", "McDiarmid");
			graph.addEdge(null, jedi, ian, starring);
			graph.addEdge(null, phantom, ian, starring);
			graph.addEdge(null, clones, ian, starring);
			graph.addEdge(null, revenge, ian, starring);
			graph.addEdge(null, ian, palpatine, portrays);

			Vertex shaw = graph.addVertex(crewId + ACT_SS);
			shaw.setProperty("firstName", "Sebastian");
			shaw.setProperty("lastName", "Shaw");
			graph.addEdge(null, jedi, shaw, starring);
			graph.addEdge(null, shaw, anakin, portrays);

			Vertex liam = graph.addVertex(crewId + ACT_LN);
			liam.setProperty("firstName", "Liam");
			liam.setProperty("lastName", "Neeson");
			graph.addEdge(null, phantom, liam, starring);
			graph.addEdge(null, liam, quigon, portrays);

			Vertex portman = graph.addVertex(crewId + ACT_NP);
			portman.setProperty("firstName", "Natalie");
			portman.setProperty("lastName", "Portman");
			graph.addEdge(null, phantom, portman, starring);
			graph.addEdge(null, clones, portman, starring);
			graph.addEdge(null, revenge, portman, starring);
			graph.addEdge(null, portman, padme, portrays);

			Vertex lloyd = graph.addVertex(crewId + ACT_JL);
			lloyd.setProperty("firstName", "Jake");
			lloyd.setProperty("lastName", "Lloyd");
			graph.addEdge(null, phantom, lloyd, starring);
			graph.addEdge(null, lloyd, anakin, portrays);

			Vertex hayden = graph.addVertex(crewId + ACT_HC);
			hayden.setProperty("firstName", "Hayden");
			hayden.setProperty("lastName", "Christensen");
			graph.addEdge(null, clones, hayden, starring);
			graph.addEdge(null, revenge, hayden, starring);
			graph.addEdge(null, hayden, anakin, portrays);

			Vertex august = graph.addVertex(crewId + ACT_PA);
			august.setProperty("firstName", "Pernill");
			hayden.setProperty("lastName", "August");
			graph.addEdge(null, phantom, august, starring);
			graph.addEdge(null, clones, august, starring);
			graph.addEdge(null, august, shmi, portrays);

			Vertex park = graph.addVertex(crewId + ACT_RP);
			park.setProperty("firstName", "Ray");
			park.setProperty("lastName", "Park");
			graph.addEdge(null, phantom, park, starring);
			graph.addEdge(null, park, maul, portrays);

			Vertex samuel = graph.addVertex(crewId + ACT_SLJ);
			samuel.setProperty("firstName", "Samuel L.");
			samuel.setProperty("lastName", "Jackson");
			graph.addEdge(null, phantom, samuel, starring);
			graph.addEdge(null, clones, samuel, starring);
			graph.addEdge(null, revenge, samuel, starring);
			graph.addEdge(null, samuel, windu, portrays);

			Vertex lee = graph.addVertex(crewId + ACT_CL);
			lee.setProperty("firstName", "Christopher");
			lee.setProperty("lastName", "Lee");
			graph.addEdge(null, clones, lee, starring);
			graph.addEdge(null, revenge, lee, starring);
			graph.addEdge(null, lee, tyranus, portrays);

			Vertex morrison = graph.addVertex(crewId + ACT_TM);
			morrison.setProperty("firstName", "Temuera");
			morrison.setProperty("lastName", "Morrison");
			graph.addEdge(null, clones, morrison, starring);
			graph.addEdge(null, morrison, jango, portrays);

			Vertex logan = graph.addVertex(crewId + ACT_DL);
			logan.setProperty("firstName", "Daniel");
			logan.setProperty("lastName", "Logan");
			graph.addEdge(null, clones, logan, starring);
			graph.addEdge(null, logan, boba, portrays);

			Vertex blake = graph.addVertex(crewId + ACT_PB);
			blake.setProperty("firstName", "Paul");
			blake.setProperty("lastName", "Blake");
			graph.addEdge(null, newhope, blake, starring);
			graph.addEdge(null, blake, greedo, portrays);

			Vertex lawson = graph.addVertex(crewId + ACT_DLW);
			lawson.setProperty("firstName", "Denis");
			lawson.setProperty("lastName", "Lawson");
			graph.addEdge(null, newhope, lawson, starring);
			graph.addEdge(null, empire, lawson, starring);
			graph.addEdge(null, jedi, lawson, starring);
			graph.addEdge(null, lawson, wedge, portrays);

			Edge curEdge = null;
			curEdge = graph.addEdge(null, anakin, obiwan, kills);
			curEdge.setProperty("film", newhope.getId().toString());
			curEdge = graph.addEdge(null, luke.asVertex(), tarkin, kills);
			curEdge.setProperty("film", newhope.getId().toString());
			curEdge = graph.addEdge(null, windu, jango, kills);
			curEdge.setProperty("film", clones.getId().toString());
			curEdge = graph.addEdge(null, palpatine, windu, kills);
			curEdge.setProperty("film", revenge.getId().toString());
			curEdge = graph.addEdge(null, wedge, anakin, kills);
			curEdge.setProperty("film", jedi.getId().toString());
			curEdge = graph.addEdge(null, lando, anakin, kills);
			curEdge.setProperty("film", jedi.getId().toString());
			curEdge = graph.addEdge(null, anakin, palpatine, kills);
			curEdge.setProperty("film", jedi.getId().toString());
			curEdge = graph.addEdge(null, maul, quigon, kills);
			curEdge.setProperty("film", phantom.getId().toString());
			curEdge = graph.addEdge(null, obiwan, maul, kills);
			curEdge.setProperty("film", phantom.getId().toString());
			curEdge = graph.addEdge(null, han, greedo, kills);
			curEdge.setProperty("film", newhope.getId().toString());
			curEdge = graph.addEdge(null, han, boba, kills);
			curEdge.setProperty("film", jedi.getId().toString());
			curEdge = graph.addEdge(null, anakin, tyranus, kills);
			curEdge.setProperty("film", revenge.getId().toString());

			graph.addEdge(null, anakin, threepio, spawns);
			graph.addEdge(null, anakin, luke.asVertex(), spawns);
			graph.addEdge(null, anakin, leia, spawns);
			graph.addEdge(null, padme, luke.asVertex(), spawns);
			graph.addEdge(null, padme, leia, spawns);
			graph.addEdge(null, shmi, anakin, spawns);
			graph.addEdge(null, jango, boba, spawns);

			// Will fail until DGraph.getVertices() and getEdges() are implemented
			GraphJung graph2 = new GraphJung(graph);
			Layout<Vertex, Edge> layout = new CircleLayout<Vertex, Edge>(graph2);
			layout.setSize(new Dimension(300, 300));
			BasicVisualizationServer<Vertex, Edge> viz = new BasicVisualizationServer<Vertex, Edge>(layout);
			viz.setPreferredSize(new Dimension(350, 350));

			Transformer<Vertex, String> vertexLabelTransformer = new Transformer<Vertex, String>() {
				@Override
				public String transform(final Vertex vertex) {
					return (String) vertex.getProperty("name");
				}
			};

			Transformer<Edge, String> edgeLabelTransformer = new Transformer<Edge, String>() {
				@Override
				public String transform(final Edge edge) {
					return edge.getLabel();
				}
			};

			viz.getRenderContext().setEdgeLabelTransformer(edgeLabelTransformer);
			viz.getRenderContext().setVertexLabelTransformer(vertexLabelTransformer);

			JFrame frame = new JFrame("Star Wars");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().add(viz);
			frame.pack();
			frame.setVisible(true);

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

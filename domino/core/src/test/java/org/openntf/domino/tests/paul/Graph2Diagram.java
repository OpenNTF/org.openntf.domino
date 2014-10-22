package org.openntf.domino.tests.paul;

import java.awt.Dimension;

import javax.swing.JFrame;

import lotus.domino.NotesFactory;

import org.apache.commons.collections15.Transformer;
import org.openntf.domino.Session;
import org.openntf.domino.graph2.impl.DConfiguration;
import org.openntf.domino.graph2.impl.DElementStore;
import org.openntf.domino.graph2.impl.DGraph;
import org.openntf.domino.tests.paul.Graph2DataModel.Character;
import org.openntf.domino.tests.paul.Graph2DataModel.Crew;
import org.openntf.domino.tests.paul.Graph2DataModel.DirectedBy;
import org.openntf.domino.tests.paul.Graph2DataModel.Movie;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.oupls.jung.GraphJung;
import com.tinkerpop.frames.FramedGraphFactory;
import com.tinkerpop.frames.FramedTransactionalGraph;
import com.tinkerpop.frames.modules.Module;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerModule;
import com.tinkerpop.frames.modules.typedgraph.TypedGraphModuleBuilder;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;

public class Graph2Diagram implements Runnable {
	private static int THREAD_COUNT = 1;
	private long marktime;

	private static final String MV_SW = "Star Wars";

	private static final String CH_AS = "Anakin Skywalker";
	private static final String CH_C3 = "C-3PO";
	private static final String CH_BF = "Boba Fett";

	private static final String DIR_GL = "George Lucas";

	private static final String ACT_MH = "Mark Hammill";

	private static final String directedBy = "DirectedBy";
	private static final String appearsIn = "AppearsIn";
	private static final String portrays = "Portrays";
	private static final String starring = "Starring";
	private static final String spawns = "Spawns";
	private static final String kills = "Kills";

	public static void main(final String[] args) {
		org.openntf.domino.thread.DominoExecutor de = new org.openntf.domino.thread.DominoExecutor(10);
		for (int i = 0; i < THREAD_COUNT; i++) {
			de.execute(new Graph2Diagram());
		}
		de.shutdown();
	}

	public void timelog(final String message) {
		long curtime = System.nanoTime();
		long elapsed = curtime - marktime;
		marktime = curtime;
		System.out.println(elapsed / 1000000 + " ms: " + message);
	}

	public Graph2Diagram() {
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

			JavaHandlerModule jhm = new JavaHandlerModule();

			Module module = new TypedGraphModuleBuilder().withClass(Movie.class).withClass(Character.class).withClass(Crew.class).build();
			FramedGraphFactory factory = new FramedGraphFactory(module, jhm);

			FramedTransactionalGraph<DGraph> framedGraph = factory.create(graph);

			// Now create a graph for generating a visual model of the data model
			Movie modelMovie = framedGraph.addVertex(movieId + MV_SW, Movie.class);
			modelMovie.setTitle(MV_SW);
			Vertex modelMovieVertex = modelMovie.asVertex();
			Crew modelCrew = framedGraph.addVertex(crewId + DIR_GL, Crew.class);
			modelCrew.setFirstName("George");
			modelCrew.setLastName("Lucas");
			DirectedBy modelDirector = modelMovie.addDirectedBy(modelCrew);
			modelDirector.setRating(4);
			Crew modelCrew2 = framedGraph.addVertex(crewId + DIR_GL, Crew.class);
			modelCrew2.addStarsInMovie(modelMovie);
			Character modelChar = framedGraph.addVertex(characterId + CH_AS, Character.class);
			Character modelChar2 = framedGraph.addVertex(characterId + CH_BF, Character.class);
			Character modelChar3 = framedGraph.addVertex(characterId + CH_C3, Character.class);
			modelChar.addAppearsIn(modelMovie);
			modelChar.setName("Character 1");
			modelChar2.setName("Character 2");
			modelChar3.setName("Character 3");
			modelChar.addKills(modelChar2);
			modelChar2.addKilledBy(modelChar);
			modelChar2.addSpawns(modelChar3);
			modelChar3.addSpawnedBy(modelChar);
			modelCrew.addPortrayals(modelChar);

			System.out.println("Starting GraphJung...");

			GraphJung graph2 = new GraphJung(graph);
			Layout<Vertex, Edge> layout = new CircleLayout<Vertex, Edge>(graph2);
			layout.setSize(new Dimension(300, 300));
			BasicVisualizationServer<Vertex, Edge> viz = new BasicVisualizationServer<Vertex, Edge>(layout);
			viz.setPreferredSize(new Dimension(350, 350));

			Transformer<Vertex, String> vertexLabelTransformer = new Transformer<Vertex, String>() {
				@Override
				public String transform(final Vertex vertex) {
					String form = vertex.getProperty("form");
					try {
						System.out.println("Vertex: " + form);
						if ("character".equals(form)) {
							return vertex.getProperty("name");
						}
						return form;
					} catch (Exception e) {
						System.out.println("No form property on vertex ");
						String retVal_ = vertex.getProperty(vertex.getPropertyKeys().iterator().next());
						System.out.println(retVal_);
						return retVal_;
					}
				}
			};

			Transformer<Edge, String> edgeLabelTransformer = new Transformer<Edge, String>() {
				@Override
				public String transform(final Edge edge) {
					System.out.println(edge.getLabel());
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

			graph.rollback();

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

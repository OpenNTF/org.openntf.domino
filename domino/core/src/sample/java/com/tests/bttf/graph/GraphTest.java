package com.tests.bttf.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.graph2.impl.DGraph;
import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

import com.bttf.buzzquotebingo.graph.BTTF_Graph;
import com.bttf.buzzquotebingo.graph.BTTF_Initializer;
import com.bttf.buzzquotebingo.graph.Card;
import com.bttf.buzzquotebingo.graph.Card.HasQuote;
import com.bttf.buzzquotebingo.graph.Quote;
import com.tinkerpop.frames.FramedTransactionalGraph;

public class GraphTest implements Runnable {

	public enum SectionName {
		INTRODUCTION("Intro"), PINES("Twin Pines Mall / Lone Pine Mall"), PHOTO("Marty's Family Photo"),
		MARTY_TANNENS("Marty McFly and the Tannens"), POWER_OF_LOVE("Power of Love"), ALTERNATE("Alternate 1985"), INDIANS("Indians!"),
		ERASED("It's Erased!"), NONE("Not Used");
		private String value;

		private SectionName(final String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	public GraphTest() {

	}

	@Override
	public void run() {
		try {
			BTTF_Graph graph = BTTF_Graph.getInstance();

			// removeData();

			FramedTransactionalGraph<DGraph> framedGraph = graph.getFramedGraph();
			ArrayList<Quote> quoteObjList = new ArrayList<Quote>();
			List<Quote> allQuotes = BTTF_Graph.getInstance().getQuotesSortedByProperty("");
			if (allQuotes.isEmpty()) {
				BTTF_Initializer.createSections(framedGraph);
				BTTF_Initializer.createQuotes(framedGraph);
				allQuotes = BTTF_Graph.getInstance().getQuotesSortedByProperty("");
			}

			createCard(framedGraph, quoteObjList, allQuotes);

			framedGraph.commit();

			Iterable<Card> cards = framedGraph.getVertices(null, null, Card.class);
			Iterator<Card> it = cards.iterator();
			int i = 0;
			while (it.hasNext()) {
				Card nextCard = it.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createCard(final FramedTransactionalGraph<DGraph> framedGraph, final ArrayList<Quote> quoteObjList,
			final List<Quote> allQuotes) {
		Card card = framedGraph.addVertex("japple@intec.co.uk", Card.class);
		card.setName("Jane Apple");
		for (int x = 0; x < 9; x++) {
			boolean foundValue = false;
			while (!foundValue) {
				Random randomizer = new Random();
				Quote randomQuote = allQuotes.get(randomizer.nextInt(allQuotes.size()));
				if (!quoteObjList.contains(randomQuote)) {
					quoteObjList.add(randomQuote);
					HasQuote edge = card.addQuote(randomQuote);
					edge.setHeardByAttendee(false);
					foundValue = true;
				}
			}
		}
	}

	private void removeData() {
		Session s = Factory.getSession(SessionType.NATIVE);
		Database db = s.getDatabase(s.getServerName(), BTTF_Graph.QUOTE_PATH);
		db.getAllDocuments().removeAll(true);
		db = s.getDatabase(s.getServerName(), BTTF_Graph.CARDS_PATH);
		db.getAllDocuments().removeAll(true);
		db = s.getDatabase(s.getServerName(), BTTF_Graph.DEFAULT_PATH);
		db.getAllDocuments().removeAll(true);
	}

	public static void main(final String[] args) {
		TestRunnerUtil.runAsDominoThread(new GraphTest(), TestRunnerUtil.NATIVE_SESSION);
	}

}

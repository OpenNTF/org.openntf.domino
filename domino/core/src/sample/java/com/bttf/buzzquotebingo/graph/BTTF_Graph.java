package com.bttf.buzzquotebingo.graph;

import java.util.List;
import java.util.logging.Logger;

import org.openntf.domino.graph2.builtin.DVertexFrameComparator;
import org.openntf.domino.graph2.impl.DConfiguration;
import org.openntf.domino.graph2.impl.DElementStore;
import org.openntf.domino.graph2.impl.DFramedGraphFactory;
import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.graph2.impl.DGraph;

import com.google.common.collect.Ordering;
import com.ibm.commons.util.StringUtil;
import com.tinkerpop.frames.FramedTransactionalGraph;

public class BTTF_Graph {
	public static String QUOTE_PATH;
	public static String CARDS_PATH;
	public static String DEFAULT_PATH;
	private static final Logger log = Logger.getLogger(BTTF_Graph.class.getName());
	private static BTTF_Graph INSTANCE;

	private DFramedTransactionalGraph<DGraph> framedGraph_;

	public BTTF_Graph() {
		String baseFolder = "ibmconnect/bttf/";
		QUOTE_PATH = baseFolder + "quotes.nsf";
		CARDS_PATH = baseFolder + "cards.nsf";
		DEFAULT_PATH = baseFolder + "default.nsf";
		initialise();
	}

	public void initialise() {
		DElementStore quoteStore = new DElementStore();
		quoteStore.setStoreKey(QUOTE_PATH);
		quoteStore.addType(Quote.class);
		quoteStore.addType(FilmCharacter.class);
		quoteStore.addType(Section.class);
		DElementStore cardStore = new DElementStore();
		cardStore.setStoreKey(CARDS_PATH);
		cardStore.addType(Card.class);
		DElementStore defaultStore = new DElementStore();
		defaultStore.setStoreKey(DEFAULT_PATH);

		DConfiguration config = new DConfiguration();
		DGraph graph = new DGraph(config);
		config.addElementStore(quoteStore);
		config.addElementStore(cardStore);
		config.addElementStore(defaultStore);
		config.setDefaultElementStore(defaultStore.getStoreKey());

		DFramedGraphFactory factory = new DFramedGraphFactory(config);
		framedGraph_ = (DFramedTransactionalGraph) factory.create(graph);
		createSections(framedGraph_);
		framedGraph_.commit();
	}

	public DFramedTransactionalGraph<DGraph> getFramedGraph() {
		return framedGraph_;
	}

	public static BTTF_Graph getInstance() {
		if (null == INSTANCE) {
			INSTANCE = new BTTF_Graph();
		}
		return INSTANCE;
	}

	private void createSections(final FramedTransactionalGraph<DGraph> framedGraph) {
		for (int i = 0; i < SectionName.values().length; i++) {
			Section section = framedGraph.addVertex(SectionName.values()[i].name(), Section.class);
			section.setName(SectionName.values()[i].getValue());
		}
	}

	public synchronized Iterable<Section> getSections() {
		return getFramedGraph().getVertices(null, null, Section.class);
	}

	public List<Quote> getQuotesSortedByProperty(String property) {
		List<Quote> retVal_ = null;
		try {
			Iterable<Quote> quotes = framedGraph_.getVertices(null, null, Quote.class);
			if (StringUtil.isEmpty(property)) {
				property = "summary";
			}
			Ordering ord = Ordering.from(new DVertexFrameComparator(property));
			retVal_ = ord.sortedCopy(quotes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal_;
	}

	//	public List<QuoteBean> loadQuoteBeans() {
	//		List<QuoteBean> retVal_ = new ArrayList<QuoteBean>();
	//		try {
	//			Iterable<Quote> quotes = framedGraph_.getVertices(null, null, Quote.class);
	//			Iterator<Quote> it = quotes.iterator();
	//			int i = 0;
	//			while (it.hasNext()) {
	//				Quote nextQuote = it.next();
	//				QuoteBean quote = new QuoteBean(nextQuote);
	//				retVal_.add(quote);
	//				if (i > 200) {
	//					break;
	//				}
	//				i++;
	//			}
	//		} catch (Exception e) {
	//			Utils.throwError(log, e, "");
	//		}
	//		return retVal_;
	//	}
	//
	//	public List<CardBean> loadCardBeans() {
	//		List<CardBean> retVal_ = new ArrayList<CardBean>();
	//		try {
	//			Iterable<Card> cards = framedGraph_.getVertices(null, null, Card.class);
	//			Iterator<Card> it = cards.iterator();
	//			int i = 0;
	//			while (it.hasNext()) {
	//				Card nextCard = it.next();
	//				CardBean card = new CardBean(nextCard);
	//				retVal_.add(card);
	//				if (i > 200) {
	//					break;
	//				}
	//				i++;
	//			}
	//		} catch (Exception e) {
	//			Utils.throwError(log, e, "");
	//		}
	//		return retVal_;
	//	}

}

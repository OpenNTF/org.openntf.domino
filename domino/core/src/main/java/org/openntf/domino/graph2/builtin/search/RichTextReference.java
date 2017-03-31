package org.openntf.domino.graph2.builtin.search;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

import org.openntf.domino.Document;
import org.openntf.domino.EmbeddedObject;
import org.openntf.domino.RichTextItem;
import org.openntf.domino.big.IndexDatabase;
import org.openntf.domino.exceptions.UserAccessException;
import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.builtin.DEdgeFrame;
import org.openntf.domino.graph2.builtin.DVertexFrame;
import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.helpers.DocumentScanner;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;
import com.tinkerpop.frames.modules.javahandler.JavaHandler;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerClass;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerContext;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue(IndexDatabase.RICH_TEXT_FORM_NAME)
@JavaHandlerClass(RichTextReference.RichTextReferenceImpl.class)
public interface RichTextReference extends DVertexFrame {
	public static enum Utils {
		;
		public static void processContent(final RichTextReference value, final DFramedTransactionalGraph graph, final boolean caseSensitive,
				final boolean commit) {
			Boolean processed = value.isTokenProcessed();
			if (processed == null || !processed) {
				String val = value.getText();
				if (val != null) {
					String replicaid = value.getSourceMetaid().substring(0, 15);
					String unid = value.getSourceMetaid().substring(16);
					boolean hasReaders = value.hasReaders();
					String form = value.getForm();
					String address = unid + (hasReaders ? "1" : "0") + form;
					Scanner s = new Scanner(val);
					s.useDelimiter(DocumentScanner.REGEX_NONALPHANUMERIC);
					while (s.hasNext()) {
						CharSequence token = DocumentScanner.scrubToken(s.next(), caseSensitive);
						if (token != null && (token.length() > 2)) {
							Term tokenV = (Term) graph.addVertex(token.toString().toLowerCase(), Term.class);
							if (tokenV.getValue() == null || tokenV.getValue().length() == 0) {
								tokenV.setValue(token.toString());
							}
							Map<CharSequence, Set<CharSequence>> hitMap = tokenV.getHits(replicaid);
							if (hitMap == null) {
								hitMap = new HashMap<CharSequence, Set<CharSequence>>();
							}
							Set<CharSequence> addressList = hitMap.get(value.getItemName());
							if (addressList == null) {
								addressList = new HashSet<CharSequence>();
								hitMap.put(value.getItemName(), addressList);
							}
							addressList.add(address);
							tokenV.setHits(hitMap, replicaid);
							value.addTerm(tokenV);
						}
					}
					s.close();
					value.setTokenProcessed(true);
					if (commit) {
						graph.commit();
					}
				}
			}
		}
	}

	@TypeValue(RichTextContainsTerm.LABEL)
	public static interface RichTextContainsTerm extends DEdgeFrame {
		public static final String LABEL = "RichTextContainsTerm";

		@InVertex
		public RichTextReference getRichText();

		@OutVertex
		public Term getTerm();
	}

	@TypedProperty("isTokenProcessed")
	public Boolean isTokenProcessed();

	@TypedProperty("isTokenProcessed")
	public void setTokenProcessed(boolean processed);

	@TypedProperty("itemName")
	public String getItemName();

	@TypedProperty("itemName")
	public void setItemName(String itemName);

	@TypedProperty("sourceMetaid")
	public String getSourceMetaid();

	@TypedProperty("sourceMetaid")
	public void setSourceMetaid(String sourceMetaid);

	@JavaHandler
	@TypedProperty("text")
	public String getText();

	@JavaHandler
	@TypedProperty("attachments")
	public Map<String, Integer> getAttachments();

	@JavaHandler
	public boolean hasReaders();

	@JavaHandler
	public String getForm();

	@AdjacencyUnique(label = RichTextContainsTerm.LABEL, direction = Direction.IN)
	public Iterable<Term> getTerms();

	@AdjacencyUnique(label = RichTextContainsTerm.LABEL, direction = Direction.IN)
	public RichTextContainsTerm addTerm(Term term);

	@AdjacencyUnique(label = RichTextContainsTerm.LABEL, direction = Direction.IN)
	public void removeTerm(Term term);

	@IncidenceUnique(label = RichTextContainsTerm.LABEL, direction = Direction.IN)
	public Iterable<RichTextContainsTerm> getContainsTerms();

	@IncidenceUnique(label = RichTextContainsTerm.LABEL, direction = Direction.IN)
	public int countContainsTerms();

	@IncidenceUnique(label = RichTextContainsTerm.LABEL, direction = Direction.IN)
	public void removeContainsTerm(RichTextContainsTerm containsTerm);

	public static abstract class RichTextReferenceImpl extends DVertexFrameImpl implements RichTextReference, JavaHandlerContext<Vertex> {
		private Document doc_;
		private RichTextItem rtitem_;

		protected DVertexFrame getDVertexFrame() {
			DVertexFrame dv = g().getVertex(getSourceMetaid(), DVertexFrame.class);
			return dv;
		}

		protected Document getDocument() throws UserAccessException {
			if (doc_ == null) {
				try {
					Document doc = getDVertexFrame().asDocument();
					doc_ = doc;
				} catch (UserAccessException uae) {
					throw uae;
				} catch (Throwable t) {
					t.printStackTrace();
					return null;
				}
			}
			return doc_;
		}

		@Override
		public String getForm() {
			return getDocument().getItemValueString("form");
		}

		@Override
		public boolean hasReaders() {
			return getDocument().hasReaders();
		}

		protected RichTextItem getRTItem() throws UserAccessException {
			//			System.out.println("TEMP DEBUG Getting RTItem from vertex id " + getSourceMetaid() + " in item " + getItemName());
			if (rtitem_ == null) {
				try {
					Document doc = getDocument();
					if (doc != null) {
						RichTextItem rtitem = (RichTextItem) doc.getFirstItem(getItemName());
						rtitem_ = rtitem;
					}
				} catch (UserAccessException uae) {
					throw uae;
				} catch (Throwable t) {
					t.printStackTrace();
					return null;
				}
			}
			return rtitem_;
		}

		@Override
		public String getText() {
			String result = null;
			RichTextItem rti = getRTItem();
			if (rti != null) {
				result = getRTItem().getUnformattedText();
			}
			//			System.out.println("TEMP DEBUG Rich text length is " + result.length());
			return result;
		}

		@Override
		public Map<String, Integer> getAttachments() {
			Map<String, Integer> result = new LinkedHashMap<String, Integer>();
			RichTextItem rti = getRTItem();
			if (rti != null) {
				Vector<EmbeddedObject> objects = getRTItem().getEmbeddedObjects();
				for (EmbeddedObject obj : objects) {
					result.put(obj.getName(), obj.getFileSize());
				}
			}
			return result;
		}

	}

}

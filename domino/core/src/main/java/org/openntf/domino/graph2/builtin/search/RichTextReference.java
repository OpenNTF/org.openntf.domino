package org.openntf.domino.graph2.builtin.search;

import java.util.Date;
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
import org.openntf.domino.graph2.DVertex;
import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.builtin.DEdgeFrame;
import org.openntf.domino.graph2.builtin.DVertexFrame;
import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.graph2.impl.DProxyVertex;
import org.openntf.domino.helpers.DocumentScanner;
import org.openntf.domino.types.BigString;
import org.openntf.domino.types.CaseInsensitiveString;

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
		@SuppressWarnings("rawtypes")
		public static void processContent(final RichTextReference value, final DFramedTransactionalGraph graph, final boolean caseSensitive,
				final boolean commit) {
			Boolean processed = value.isTokenProcessed();
			if (processed == null || !processed) {
				String val = value.getText();
				if (val != null) {
					String replicaid = value.getSourceMetaid().substring(0, 16);
					String unid = value.getSourceMetaid().substring(16);
					boolean hasReaders = value.hasReaders();
					String form = value._getForm();
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
							RichTextContainsTerm e = value.addTerm(tokenV);
							e.setReplicaID(replicaid);
							e.setFormName(form);
							e.setItemName(value.getItemName());
							e.setUNID(unid);
							e.setReaders(hasReaders);
						}
					}
					s.close();
					value.setTokenProcessed(true);
					value.setForm(form);
					//					value.setText(new BigString(val));
					if (commit) {
						graph.commit();
					}
				}
			}
		}

		public static void processRemoveContent(final Document sourceDoc, final RichTextReference value,
				final DFramedTransactionalGraph graph, final boolean caseSensitive, final boolean commit) {
			String val = value.getText();
			if (val != null) {
				Scanner s = new Scanner(val);
				s.useDelimiter(DocumentScanner.REGEX_NONALPHANUMERIC);
				while (s.hasNext()) {
					CharSequence token = DocumentScanner.scrubToken(s.next(), caseSensitive);
					if (token != null && (token.length() > 2)) {
						Term tokenV = (Term) graph.getVertex(token.toString().toLowerCase(), Term.class);
						tokenV.removeDocument(sourceDoc);
					}
				}
				s.close();
				value.asVertex().remove();
				if (commit) {
					graph.commit();
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

		@TypedProperty("replicaid")
		public String getReplicaID();

		@TypedProperty("replicaid")
		public void setReplicaID(String replicaid);

		@TypedProperty("formname")
		public String getFormName();

		@TypedProperty("formname")
		public void setFormName(String formname);

		@TypedProperty("itemname")
		public String getItemName();

		@TypedProperty("itemname")
		public void setItemName(String itemname);

		@TypedProperty("unid")
		public String getUNID();

		@TypedProperty("unid")
		public void setUNID(String unid);

		@TypedProperty("hasreaders")
		public Boolean hasReaders();

		@TypedProperty("hasreaders")
		public void setReaders(boolean hasReaders);

		@TypedProperty("hasattachements")
		public Boolean hasAttachments();

		@TypedProperty("hasattachements")
		public void setAttachments(boolean hasAttachments);

	}

	@TypedProperty("isTokenProcessed")
	public Boolean isTokenProcessed();

	@TypedProperty("isTokenProcessed")
	public void setTokenProcessed(boolean processed);

	@TypedProperty("itemName")
	public String getItemName();

	@TypedProperty("itemName")
	public void setItemName(String itemName);

	@TypedProperty("formName")
	public String getForm();

	@TypedProperty("formName")
	public void setForm(String formName);

	@TypedProperty("sourceMetaid")
	public String getSourceMetaid();

	@TypedProperty("sourceMetaid")
	public void setSourceMetaid(String sourceMetaid);

	@TypedProperty("lastIndexed")
	public Date getLastIndexed();

	@TypedProperty("lastIndexed")
	public void setLastIndexed(Date now);

	//	@JavaHandler
	@TypedProperty("text")
	public String getText();

	@TypedProperty("text")
	public void setText(BigString text);

	@JavaHandler
	@TypedProperty("attachments")
	public Map<String, Integer> getAttachments();

	@JavaHandler
	public boolean hasReaders();

	@JavaHandler
	public String _getForm();

	@JavaHandler
	public String _getText();

	@JavaHandler
	public boolean isFilterMatch(final Map<CharSequence, Set<CharSequence>> filterMap);

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
		//		private Document doc_;
		//		private RichTextItem rtitem_;

		public String getReplicaid() {
			return getSourceMetaid().substring(0, 16);
		}

		public String getUnid() {
			return getSourceMetaid().substring(16);
		}

		@Override
		public boolean isFilterMatch(final Map<CharSequence, Set<CharSequence>> filterMap) {
			boolean result = true;
			try {
				if (filterMap.containsKey(Value.REPLICA_KEY)) {
					CharSequence rid = new CaseInsensitiveString(getReplicaid());
					Set<CharSequence> replicas = filterMap.get(Value.REPLICA_KEY);
					if (!replicas.contains(rid)) {
						result = false;
					}
				}
				if (filterMap.containsKey(Value.FORM_KEY)) {
					String rawform = getForm();
					if (rawform != null) {
						CharSequence formname = new CaseInsensitiveString(rawform);
						Set<CharSequence> forms = filterMap.get(Value.FORM_KEY);
						if (!forms.contains(formname)) {
							result = false;
						}
					} else {
						result = false;
					}
				}
				if (filterMap.containsKey(Value.FIELD_KEY)) {
					CharSequence item = new CaseInsensitiveString(getItemName());
					Set<CharSequence> fields = filterMap.get(Value.FIELD_KEY);
					if (!fields.contains(item)) {
						result = false;
					}
				}
			} catch (RuntimeException re) {
				throw re;
			} catch (Throwable t) {
				throw new RuntimeException(t);
			}
			return result;
		}

		protected DVertexFrame getDVertexFrame() {
			try {
				DVertexFrame dv = g().getVertex(getSourceMetaid(), null);
				return dv;
			} catch (UserAccessException uae) {
				return null;
			} catch (Throwable t) {
				System.out.println("EXCEPTION getDVertexFrame got an exception of " + t.getClass().getName());
				return null;
			}
		}

		protected Document getDocument() throws UserAccessException {
			try {
				DVertexFrame dvf = getDVertexFrame();
				if (dvf != null) {
					DVertex v = (DVertex) dvf.asVertex();
					if (v instanceof DProxyVertex) {
						v = ((DProxyVertex) v).getProxyDelegate();
					}
					Document doc = (Document) v.getDelegate();
					return doc;
				} else {
					return null;
				}
			} catch (UserAccessException uae) {
				return null;
				//throw uae;
			} catch (Throwable t) {
				//				t.printStackTrace();
				return null;
			}
		}

		@Override
		public String _getForm() {
			Document doc = getDocument();
			if (doc != null) {
				if (doc.hasItem("form")) {
					return doc.getItemValueString("form");
				} else {
					return null;
				}
			} else {
				return null;
			}
		}

		@Override
		public boolean hasReaders() {
			Document doc = getDocument();
			if (doc != null) {
				return doc.hasReaders();
			} else {
				return false;
			}
		}

		protected RichTextItem getRTItem() throws UserAccessException {
			//			System.out.println("TEMP DEBUG Getting RTItem from vertex id " + getSourceMetaid() + " in item " + getItemName());
			try {
				String itemName = getItemName();
				Document doc = getDocument();
				if (doc != null && itemName != null) {
					RichTextItem rtitem = (RichTextItem) doc.getFirstItem(itemName);
					return rtitem;
				}
			} catch (UserAccessException uae) {
				throw uae;
			} catch (Throwable t) {
				t.printStackTrace();
				return null;
			}
			return null;
		}

		@Override
		public String _getText() {
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
			try {
				RichTextItem rti = getRTItem();
				if (rti != null) {
					Vector<EmbeddedObject> objects = getRTItem().getEmbeddedObjects();
					for (EmbeddedObject obj : objects) {
						result.put(obj.getName(), obj.getFileSize());
					}
				}
			} catch (UserAccessException uae) {
				throw uae;
			}
			return result;
		}

	}

}

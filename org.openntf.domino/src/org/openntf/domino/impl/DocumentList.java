package org.openntf.domino.impl;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lotus.domino.NotesException;

import org.openntf.domino.DocumentCollection;
import org.openntf.domino.View;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.annotations.Incomplete;
import org.openntf.domino.exceptions.UnimplementedException;
import org.openntf.domino.iterators.DocumentCollectionIterator;
import org.openntf.domino.iterators.DocumentIterator;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.TypeUtils;

public class DocumentList extends Base<org.openntf.domino.DocumentList, lotus.domino.DocumentCollection, org.openntf.domino.Database>
		implements org.openntf.domino.DocumentList {
	protected int realNidLength_;
	/*TODO 
	 * NTF for maximum performance, we really should track the length
	 * of the intended noteid array, rather than use nids_.length. This would allow us
	 * to grow and shrink the array in blocks, rather than each time we need to make a change
	 */

	protected int[] nids_;
	protected boolean usingList_;
	protected List<Integer> nidList_;
	protected int walkPos = -1;
	protected int walkNid = 0;
	protected boolean sorted_ = false;

	public static int getNid(final lotus.domino.Document doc) {
		int nid = 0;
		try {
			nid = Integer.valueOf(doc.getNoteID(), 16);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
		return nid;
	}

	public static boolean hasNid(final int[] nids, final int nid) {
		return findNid(nids, nid) > -1;
	}

	public static int[] intersectNids(final int[] nids1, final Collection<Integer> nidList) {
		int[] nids2 = TypeUtils.toIntArray(nidList);
		return intersectNids(nids1, nids2);
	}

	public static int[] intersectNids(final int[] nids1, final int[] nids2) {
		boolean largerIs1 = nids1.length >= nids2.length;
		int[] search = null;
		int[] temp = new int[Math.max(nids1.length, nids2.length)];
		if (largerIs1) {
			search = Arrays.copyOf(nids1, nids1.length);
		} else {
			search = Arrays.copyOf(nids2, nids2.length);
		}
		Arrays.sort(search);
		int hitCount = 0;
		if (largerIs1) {
			for (int nid : nids2) {
				if (Arrays.binarySearch(search, nid) >= 0) {
					temp[hitCount++] = nid;
				}
			}
		} else {
			for (int nid : nids1) {
				if (Arrays.binarySearch(search, nid) >= 0) {
					temp[hitCount++] = nid;
				}
			}
		}
		int[] result = Arrays.copyOf(temp, hitCount);
		return result;
	}

	public static int[] diffNids(final int[] nids1, final Collection<Integer> nidList) {
		int[] nids2 = TypeUtils.toIntArray(nidList);
		return diffNids(nids1, nids2);
	}

	public static int[] diffNids(final int[] nids1, final int[] nids2) {
		boolean largerIs1 = nids1.length >= nids2.length;
		int[] search = null;
		int[] temp = new int[Math.max(nids1.length, nids2.length)];
		if (largerIs1) {
			search = Arrays.copyOf(nids1, nids1.length);
		} else {
			search = Arrays.copyOf(nids2, nids2.length);
		}
		Arrays.sort(search);
		int missCount = 0;
		if (largerIs1) {
			for (int nid : nids2) {
				if (Arrays.binarySearch(search, nid) < 0) {
					temp[missCount++] = nid;
				}
			}
		} else {
			for (int nid : nids1) {
				if (Arrays.binarySearch(search, nid) < 0) {
					temp[missCount++] = nid;
				}
			}
		}
		int[] result = Arrays.copyOf(temp, missCount);
		return result;
	}

	public static int findNid(final int[] nids, final int nid) {
		int pos = -1;
		if (nids != null && nids.length > 0) {
			for (int n : nids) {
				pos++;
				if (n == nid)
					return pos;
			}
		}
		return -1;
	}

	public static int[] findAllNids(final int[] nids, final int[] nidsToFind) {
		//TODO - NTF
		if (nids != null && nids.length > 0 && nidsToFind != null && nidsToFind.length > 0) {
			int[] results = new int[nidsToFind.length];
			for (int i = 0; i < results.length; i++) {
				int pos = -1;
				int nid = nidsToFind[i];
				results[i] = pos;
				for (int n : nids) {
					pos++;
					if (n == nid)
						results[i] = pos;
					break;
				}
			}
			return results;
		} else {
			return null;
		}
	}

	public static int[] findOnlyMatchedNids(final int[] nids, final int[] nidsToFind) {
		//TODO - NTF
		if (nids != null && nids.length > 0 && nidsToFind != null && nidsToFind.length > 0) {
			int[] temp = new int[nidsToFind.length];
			int hitCount = 0;
			for (int i = 0; i < temp.length; i++) {
				int pos = -1;
				int nid = nidsToFind[i];
				for (int n : nids) {
					pos++;
					if (n == nid)
						temp[hitCount++] = pos;
					break;
				}
			}
			int[] results = new int[hitCount];
			System.arraycopy(temp, 0, results, 0, hitCount);
			return results;
		} else {
			return null;
		}
	}

	public static int[] toNids(final lotus.domino.DocumentCollection collection) {
		return org.openntf.domino.impl.DocumentCollection.toNoteIdArray(collection);
	}

	public static List<Integer> toNidsList(final lotus.domino.DocumentCollection collection) {
		if (collection instanceof DocumentList) {
			List<Integer> list = ((DocumentList) collection).getNidList();
			return new ArrayList<Integer>(list);
		} else {
			org.openntf.domino.NoteCollection nc = org.openntf.domino.impl.DocumentCollection.toLotusNoteCollection(collection);
			List<Integer> result = new ArrayList<Integer>();
			int[] nids = nc.getNoteIDs();
			for (int nid : nids) {
				result.add(nid);
			}
			return result;
		}
	}

	public static int getSize(final lotus.domino.DocumentCollection collection) {
		try {
			return collection.getCount();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return 0;
		}
	}

	public DocumentList(final lotus.domino.DocumentCollection delegate, final org.openntf.domino.Database parent, final WrapperFactory wf,
			final long cppId) {
		super(delegate, parent, wf, cppId, NOTES_DOCCOLL);
		try {
			setSorted(delegate.isSorted());
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	public DocumentList(final int[] nids, final org.openntf.domino.Database parent) {
		super(null, parent, null, 0l, NOTES_DOCCOLL);
		nids_ = nids;
	}

	public int[] getNids() {
		if (usingList_) {
			return TypeUtils.toIntArray(getNidList());
		} else {
			return nids_;
		}
	}

	public List<Integer> getNidList() {
		if (nidList_ == null) {
			nidList_ = new ArrayList<Integer>();
			if (nids_ != null && nids_.length > 0) {
				for (int nid : nids_) {
					nidList_.add(nid);
				}
			}
		}
		return nidList_;
	}

	public void addDocument(final lotus.domino.Document doc) {
		usingList_ = true;
		int nid = getNid(doc);
		if (nid != 0) {
			if (!getNidList().contains(nid)) {
				getNidList().add(nid);
			}
		}
	}

	public void addDocument(final lotus.domino.Document doc, final boolean checkDups) {
		addDocument(doc);	//NTF - only the IIOP stuff actually supports dups in DocumentCollections anyway
	}

	public org.openntf.domino.DocumentCollection cloneCollection() {
		if (usingList_) {
			return new DocumentList(TypeUtils.toIntArray(nidList_), getParent());
		} else {
			return new DocumentList(Arrays.copyOf(nids_, nids_.length), getParent());
		}
	}

	public boolean contains(final int noteid) {
		if (usingList_) {
			return getNidList().contains(noteid);
		} else {
			return hasNid(nids_, noteid);
		}
	}

	public boolean contains(final lotus.domino.Base doc) {
		if (doc instanceof lotus.domino.Document) {
			int nid = getNid((lotus.domino.Document) doc);
			if (usingList_) {
				return getNidList().contains(nid);
			} else {
				return hasNid(nids_, nid);
			}
		} else if (doc instanceof lotus.domino.DocumentCollection) {
			if (usingList_) {
				if (getSize((lotus.domino.DocumentCollection) doc) > getNidList().size())
					return false;
			} else {
				if (getSize((lotus.domino.DocumentCollection) doc) > nids_.length)
					return false;
			}
			int[] nids = toNids((lotus.domino.DocumentCollection) doc);
			if (usingList_) {
				List<Integer> list = new ArrayList<Integer>();
				for (int n : nids) {
					list.add(n);
				}
				return getNidList().containsAll(list);
			} else {
				for (int nid : nids) {
					if (!hasNid(nids_, nid))
						return false;
				}
				return true;
			}
		} else {
			throw new IllegalArgumentException("Cannot check a DocumentList to see if it contains a " + doc.getClass().getName());
		}

	}

	public boolean contains(final String noteid) {
		return contains(Integer.valueOf(noteid, 16));
	}

	public void deleteDocument(final lotus.domino.Document doc) {
		int nid = getNid(doc);
		usingList_ = true;
		List<Integer> tlist = new ArrayList<Integer>();
		tlist.add(nid);
		// NTF Why? because otherwise we'll be calling .remove(int) which removes the element by POSITION rather than value
		getNidList().removeAll(tlist);
	}

	@Incomplete
	public void FTSearch(final String query) {
		throw new UnimplementedException("FTSearch not implemented on DocumentList yet. Sorry.");
	}

	@Incomplete
	public void FTSearch(final String query, final int maxDocs) {
		throw new UnimplementedException("FTSearch not implemented on DocumentList yet. Sorry.");
	}

	public int getCount() {
		if (usingList_) {
			return getNidList().size();
		} else {
			return nids_.length;
		}
	}

	public org.openntf.domino.Document getDocument(final lotus.domino.Document doc) {
		// TODO Auto-generated method stub
		return null;
	}

	public org.openntf.domino.Document getFirstDocument() {
		walkPos = -1;
		if (usingList_) {
			walkNid = getNidList().get(++walkPos);
			return getParent().getDocumentByID(Integer.toString(walkNid, 16));
		} else {
			walkNid = nids_[++walkPos];
			return getParent().getDocumentByID(Integer.toString(walkNid, 16));
		}
	}

	public org.openntf.domino.Document getLastDocument() {
		if (usingList_) {
			walkPos = getNidList().size() - 1;
			walkNid = getNidList().get(walkPos);
			return getParent().getDocumentByID(Integer.toString(walkNid, 16));
		} else {
			walkPos = nids_.length - 1;
			walkNid = nids_[walkPos];
			return getParent().getDocumentByID(Integer.toString(walkNid, 16));
		}
	}

	public org.openntf.domino.Document getNextDocument() {
		if (usingList_) {
			walkNid = getNidList().get(++walkPos);
			return getParent().getDocumentByID(Integer.toString(walkNid, 16));
		} else {
			walkNid = nids_[++walkPos];
			return getParent().getDocumentByID(Integer.toString(walkNid, 16));
		}
	}

	public org.openntf.domino.Document getNextDocument(final lotus.domino.Document doc) {
		int nid = getNid(doc);
		if (nid == walkNid) {
			if (usingList_) {
				walkNid = getNidList().get(++walkPos);
				return getParent().getDocumentByID(Integer.toString(walkNid, 16));
			} else {
				walkNid = nids_[++walkPos];
				return getParent().getDocumentByID(Integer.toString(walkNid, 16));
			}
		} else {
			if (usingList_) {
				walkPos = getNidList().indexOf(nid);
				walkNid = getNidList().get(++walkPos);
				return getParent().getDocumentByID(Integer.toString(walkNid, 16));
			} else {
				walkPos = findNid(nids_, nid);
				walkNid = nids_[++walkPos];
				return getParent().getDocumentByID(Integer.toString(walkNid, 16));
			}
		}
	}

	public org.openntf.domino.Document getNthDocument(final int n) {
		if (usingList_) {
			walkPos = n;
			walkNid = getNidList().get(n);
			return getParent().getDocumentByID(Integer.toString(walkNid, 16));
		} else {
			walkPos = n;
			walkNid = nids_[n];
			return getParent().getDocumentByID(Integer.toString(walkNid, 16));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public org.openntf.domino.Database getParent() {
		return getAncestor();
	}

	public org.openntf.domino.Document getPrevDocument() {
		if (usingList_) {
			walkNid = getNidList().get(--walkPos);
			return getParent().getDocumentByID(Integer.toString(walkNid, 16));
		} else {
			walkNid = nids_[--walkPos];
			return getParent().getDocumentByID(Integer.toString(walkNid, 16));
		}
	}

	public org.openntf.domino.Document getPrevDocument(final lotus.domino.Document doc) {
		int nid = getNid(doc);
		if (nid == walkNid) {
			if (usingList_) {
				walkNid = getNidList().get(--walkPos);
				return getParent().getDocumentByID(Integer.toString(walkNid, 16));
			} else {
				walkNid = nids_[--walkPos];
				return getParent().getDocumentByID(Integer.toString(walkNid, 16));
			}
		} else {
			if (usingList_) {
				walkPos = getNidList().indexOf(nid);
				walkNid = getNidList().get(--walkPos);
				return getParent().getDocumentByID(Integer.toString(walkNid, 16));
			} else {
				walkPos = findNid(nids_, nid);
				walkNid = nids_[--walkPos];
				return getParent().getDocumentByID(Integer.toString(walkNid, 16));
			}
		}
	}

	public String getQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	public DateTime getUntilTime() {
		// TODO Auto-generated method stub
		return null;
	}

	public void intersect(final int noteid) {
		if (usingList_) {
			if (getNidList().contains(noteid)) {
				getNidList().clear();
				getNidList().add(noteid);
			} else {
				getNidList().clear();
			}
		} else {
			if (hasNid(nids_, noteid)) {
				nids_ = new int[1];
				nids_[0] = noteid;
			} else {
				nids_ = new int[0];
			}
		}
	}

	public void intersect(final int[] noteids) {
		if (usingList_) {
			List<Integer> curList = getNidList();
			List<Integer> paramList = new ArrayList<Integer>();
			for (int n : noteids) {
				paramList.add(n);
			}
			curList.retainAll(paramList);
		} else {
			nids_ = intersectNids(nids_, noteids);
		}
	}

	public void intersect(final Collection<Integer> paramList) {
		if (usingList_) {
			List<Integer> curList = getNidList();
			curList.retainAll(paramList);
		} else {
			nids_ = intersectNids(nids_, paramList);
		}
	}

	public void intersect(final lotus.domino.Base doc) {
		if (doc instanceof lotus.domino.Document) {
			int nid = getNid((lotus.domino.Document) doc);
			intersect(nid);
		} else if (doc instanceof lotus.domino.DocumentCollection) {
			int[] nids = toNids((lotus.domino.DocumentCollection) doc);
			intersect(nids);
		} else {
			//TODO why not a View, ViewEntryCollection, NoteCollection, Iterable<Document>, ViewEntry,
			throw new IllegalArgumentException("Cannot intersect a DocumentList with a " + doc.getClass().getName());
		}
	}

	public void intersect(final String noteid) {
		int nid = Integer.valueOf(noteid, 16);
		intersect(nid);
	}

	public boolean isSorted() {
		return sorted_;
	}

	public void setSorted(final boolean sorted) {
		sorted_ = sorted;
	}

	@Incomplete
	public void markAllRead() {
		throw new UnimplementedException("Why do you care about read marks? This isn't 1998.");
	}

	@Incomplete
	public void markAllRead(final String userName) {
		throw new UnimplementedException("Why do you care about read marks? This isn't 1998.");
	}

	@Incomplete
	public void markAllUnread() {
		throw new UnimplementedException("Why do you care about read marks? This isn't 1998.");
	}

	@Incomplete
	public void markAllUnread(final String userName) {
		throw new UnimplementedException("Why do you care about read marks? This isn't 1998.");
	}

	public void merge(final int noteid) {
		if (usingList_) {
			if (!getNidList().contains(noteid))
				getNidList().add(noteid);
		} else {
			if (!hasNid(nids_, noteid)) {
				nids_ = Arrays.copyOf(nids_, nids_.length + 1);
				nids_[nids_.length] = noteid;
			}
		}
	}

	public void merge(final int[] noteids) {
		if (usingList_) {
			for (int noteid : noteids) {
				if (!getNidList().contains(noteid))
					getNidList().add(noteid);
			}
		} else {
			int origLength = nids_.length;
			int[] newNids = diffNids(nids_, noteids);
			nids_ = Arrays.copyOf(nids_, origLength + newNids.length);
			for (int i = 0; i < newNids.length; i++) {
				nids_[i + origLength] = newNids[i];
			}
		}
	}

	public void merge(final lotus.domino.Base doc) {
		if (doc instanceof lotus.domino.Document) {
			merge(getNid((lotus.domino.Document) doc));
		} else if (doc instanceof lotus.domino.DocumentCollection) {
			merge(toNids((lotus.domino.DocumentCollection) doc));
		} else {
			//TODO why not a View, ViewEntryCollection, NoteCollection, Iterable<Document>, ViewEntry,
			throw new IllegalArgumentException("Cannot merge a DocumentList with a " + doc.getClass().getName());
		}
	}

	public void merge(final String noteid) {
		merge(Integer.valueOf(noteid, 16));
	}

	public void putAllInFolder(final String folderName) {
		// TODO Auto-generated method stub
		// NTF it's important to create a lotus collection of some kind for this operation
		// rather than iterating. Each call to putInFolder is a transaction history for the folder itself,
		// so it will be highly inefficient to create 1 transaction per document rather than the 
		// whole collection
		throw new UnimplementedException("Not yet implemented, sorry");

	}

	public void putAllInFolder(final String folderName, final boolean createOnFail) {
		// TODO Auto-generated method stub
		//NTF See above
		throw new UnimplementedException("Not yet implemented, sorry");

	}

	public void removeAll(final boolean force) {
		// TODO Auto-generated method stub
		//NTF Also important to do this with a real DocumentCollection as this is one operation in the C API
		throw new UnimplementedException("Not yet implemented, sorry");

	}

	public void removeAllFromFolder(final String folderName) {
		// TODO Auto-generated method stub
		// NTF See putAllInFolder(String)
		throw new UnimplementedException("Not yet implemented, sorry");

	}

	public void stampAll(final String itemName, final Object value) {
		// TODO Auto-generated method stub
		throw new UnimplementedException("Not yet implemented, sorry");
	}

	public void subtract(final int noteid) {
		if (usingList_) {
			List<Integer> tlist = new ArrayList<Integer>();
			tlist.add(noteid);
			// NTF Why? because otherwise we'll be calling .remove(int) which removes the element by POSITION rather than value
			getNidList().removeAll(tlist);
		} else {
			int pos = findNid(nids_, noteid);
			int[] result = new int[nids_.length - 1];
			System.arraycopy(nids_, 0, result, 0, pos);
			System.arraycopy(nids_, pos + 1, result, pos, result.length - pos);
			//TODO NTF This should be optimized to an in-place copy like 
			//System.arraycopy(nids_,pos+1,nids_,pos,nids_.length-1-pos);
			//but that will necessitate that we keep track of the "real" end of the array independent of the length
			//if we want to more highly optimize our performance, we can do this, 
			//but at that point, why not just punt to using the List<Integer> instead?
			//All depends on the size of the memory footprint
			nids_ = result;
		}
	}

	public void subtract(final int[] noteids) {
		if (usingList_) {
			List<Integer> tlist = new ArrayList<Integer>();
			for (int noteid : noteids) {
				tlist.add(noteid);
			}
			getNidList().removeAll(tlist);
		} else {
			int[] posSet = findOnlyMatchedNids(nids_, noteids);
			Arrays.sort(posSet);
			for (int pos : posSet) {
				System.arraycopy(nids_, pos + 1, nids_, pos, nids_.length - 1 - pos);
			}
			int[] result = Arrays.copyOf(nids_, nids_.length - posSet.length);
			nids_ = result;
		}
	}

	public void subtract(final lotus.domino.Base doc) {
		if (doc instanceof lotus.domino.Document) {
			subtract(getNid((lotus.domino.Document) doc));
		} else if (doc instanceof lotus.domino.DocumentCollection) {
			subtract(toNids((lotus.domino.DocumentCollection) doc));
		} else {
			//TODO why not a View, ViewEntryCollection, NoteCollection, Iterable<Document>, ViewEntry,
			throw new IllegalArgumentException("Cannot subtract from a DocumentList with a " + doc.getClass().getName());
		}
	}

	public void subtract(final String noteid) {
		subtract(Integer.valueOf(noteid, 16));
	}

	public void updateAll() {
		// TODO Auto-generated method stub
		throw new UnimplementedException("Not yet implemented, sorry");
	}

	public void stampAll(final Map<String, Object> map) {
		// TODO Auto-generated method stub
		throw new UnimplementedException("Not yet implemented, sorry");

	}

	public View getParentView() {
		// TODO Auto-generated method stub
		throw new UnimplementedException("Not yet implemented, sorry");
	}

	public void setParentView(final View view) {
		throw new UnimplementedException("Not yet implemented, sorry");
	}

	public DocumentCollection filter(final Object value) {
		// TODO Auto-generated method stub
		throw new UnimplementedException("Not yet implemented, sorry");
	}

	public DocumentCollection filter(final Object value, final String[] itemnames) {
		// TODO Auto-generated method stub
		throw new UnimplementedException("Not yet implemented, sorry");
	}

	public DocumentCollection filter(final Object value, final Collection<String> itemnames) {
		// TODO Auto-generated method stub
		throw new UnimplementedException("Not yet implemented, sorry");
	}

	public DocumentCollection filter(final Map<String, Object> filterMap) {
		// TODO Auto-generated method stub
		throw new UnimplementedException("Not yet implemented, sorry");
	}

	public boolean add(final org.openntf.domino.Document arg0) {
		// TODO Auto-generated method stub
		throw new UnimplementedException("Not yet implemented, sorry");
	}

	public boolean addAll(final Collection<? extends org.openntf.domino.Document> arg0) {
		// TODO Auto-generated method stub
		throw new UnimplementedException("Not yet implemented, sorry");
	}

	public void clear() {
		// TODO Auto-generated method stub
		throw new UnimplementedException("Not yet implemented, sorry");

	}

	public boolean contains(final Object arg0) {
		// TODO Auto-generated method stub
		throw new UnimplementedException("Not yet implemented, sorry");
	}

	public boolean containsAll(final Collection<?> arg0) {
		// TODO Auto-generated method stub
		throw new UnimplementedException("Not yet implemented, sorry");
	}

	public boolean isEmpty() {
		return getCount() == 0;
	}

	public Iterator<org.openntf.domino.Document> iterator() {
		//NTF if the DocumentList was sorted, then we need to use a regular DocumentIterator that will
		//walk the noteid array, because the order matters.
		//If it's not sorted (ie: the original DocumentCollection was not sorted) then we merge the noteids into 
		//a new DocumentCollection from the parent and use the DocumentCollectionIterator because it's 4 times faster
		if (isSorted()) {
			return new DocumentIterator(this);
		} else {
			org.openntf.domino.Database db = getParentDatabase();
			org.openntf.domino.impl.DocumentCollection mergeColl = (org.openntf.domino.impl.DocumentCollection) db
					.createMergableDocumentCollection();
			for (int nid : getNids()) {
				mergeColl.merge(nid);
			}
			return new DocumentCollectionIterator(mergeColl);
		}
	}

	public boolean remove(final Object arg0) {
		// TODO Auto-generated method stub
		throw new UnimplementedException("Not yet implemented, sorry");
	}

	public boolean removeAll(final Collection<?> arg0) {
		// TODO Auto-generated method stub
		throw new UnimplementedException("Not yet implemented, sorry");
	}

	public boolean retainAll(final Collection<?> arg0) {
		// TODO Auto-generated method stub
		throw new UnimplementedException("Not yet implemented, sorry");
	}

	public int size() {
		return getCount();
	}

	public Object[] toArray() {
		return getNidList().toArray();
	}

	public <T> T[] toArray(final T[] arg0) {
		return getNidList().toArray(arg0);
	}

	public org.openntf.domino.Database getParentDatabase() {
		return getParent();
	}

	public org.openntf.domino.Database getAncestorDatabase() {
		return this.getParentDatabase();
	}

	public org.openntf.domino.Session getAncestorSession() {
		return getAncestorDatabase().getAncestorSession();
	}

	public void readExternal(final ObjectInput arg0) throws IOException, ClassNotFoundException {
		sorted_ = arg0.readBoolean();
		walkPos = arg0.readInt();
		walkNid = arg0.readInt();
		int nLen = arg0.readInt();
		nids_ = new int[nLen];
		for (int i = 0; i < nLen; i++) {
			nids_[i] = arg0.readInt();
		}
	}

	public void writeExternal(final ObjectOutput arg0) throws IOException {
		int[] nids = getNids();
		arg0.writeBoolean(sorted_);
		arg0.writeInt(walkPos);
		arg0.writeInt(walkNid);
		arg0.writeInt(nids.length);
		for (int i : nids) {
			arg0.writeInt(i);
		}
	}

}

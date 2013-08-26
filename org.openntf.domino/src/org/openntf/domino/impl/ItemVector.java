/**
 * 
 */
package org.openntf.domino.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import lotus.domino.NotesException;

import org.openntf.domino.Item;
import org.openntf.domino.Session;
import org.openntf.domino.exceptions.UnimplementedException;
import org.openntf.domino.iterators.ItemVectorIterator;
import org.openntf.domino.utils.DominoUtils;

/**
 * @author nfreeman
 * 
 */
public class ItemVector extends Vector<Item> {
	private static final Logger log_ = Logger.getLogger(ItemVector.class.getName());
	private static final long serialVersionUID = 1L;
	private Document doc_;
	private final List<String> itemNames_ = new ArrayList<String>();
	private int size_ = 0;

	/**
	 * 
	 */
	public ItemVector(final Document doc) {
		super();
		doc_ = doc;
		initialize();
	}

	private ItemVector(final Document doc, final boolean initialize) {
		super();
		doc_ = doc;
		if (initialize)
			initialize();
	}

	private void initialize() {
		try {
			Session session = doc_.getAncestorSession();
			boolean convertMime = session.isConvertMIME();
			session.setConvertMIME(false);
			java.util.Vector<?> lotusItems = doc_.getDelegate().getItems();
			if (!lotusItems.isEmpty()) {
				String lastName = null;
				for (Object o : lotusItems) {
					String name = null;
					if (o instanceof lotus.domino.Item) {
						try {
							name = ((lotus.domino.Item) o).getName();
							itemNames_.add(name);
							lastName = name;
						} catch (NotesException ne1) {
							log_.log(Level.WARNING, "Problem iterating over item following " + String.valueOf(lastName) + ". Skipping...");
						}
					} else {
						log_.log(Level.WARNING, "Object from Document.getItems() Vector is not an Item. It is a "
								+ (o == null ? "null" : o.getClass().getName()));
					}
				}
				size_ = itemNames_.size();
			}
			session.setConvertMIME(convertMime);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	@Override
	public boolean add(final Item arg0) {
		size_++;
		return itemNames_.add(arg0.getName());
	}

	public boolean add(final String arg0) {
		size_++;
		return itemNames_.add(arg0);
	}

	@Override
	public void add(final int arg0, final Item arg1) {
		size_++;
		itemNames_.add(arg0, arg1.getName());
	}

	@Override
	public boolean addAll(final Collection<? extends Item> arg0) {
		List<String> names = new ArrayList<String>();
		for (Item i : arg0) {
			names.add(i.getName());
		}
		return itemNames_.addAll(names);
	}

	protected boolean addAllStrings(final Collection<? extends String> arg0) {
		return itemNames_.addAll(arg0);
	}

	@Override
	public boolean addAll(final int arg0, final Collection<? extends Item> arg1) {
		List<String> names = new ArrayList<String>();
		for (Item i : arg1) {
			names.add(i.getName());
		}
		return itemNames_.addAll(arg0, names);
	}

	@Override
	@Deprecated
	public void addElement(final Item arg0) {
		add(arg0);
	}

	@Override
	public void clear() {
		itemNames_.clear();
	}

	@Override
	public Object clone() {
		ItemVector iv = new ItemVector(doc_, false);
		iv.addAllStrings(itemNames_);
		return iv;
	}

	@Override
	public boolean contains(final Object arg0) {
		if (arg0 instanceof Item) {
			return itemNames_.contains(((Item) arg0).getName());
		} else if (arg0 instanceof String) {
			return itemNames_.contains((String) arg0);
		}
		return false;
	}

	@Override
	public boolean containsAll(final Collection<?> arg0) {
		boolean result = true;
		for (Object o : arg0) {
			result = contains(o);
			if (!result)
				return result;
		}
		return result;
	}

	@Override
	@Deprecated
	public void copyInto(final Object[] arg0) {
		throw new UnimplementedException("copyInto not yet implemented because it's a deprecated call anyway!");
	}

	@Override
	@Deprecated
	public Item elementAt(final int arg0) {
		return get(arg0);
	}

	@Override
	@Deprecated
	public Enumeration<Item> elements() {
		throw new UnimplementedException("elements not yet implemented because it's a deprecated call anyway!");
	}

	@Override
	public Item firstElement() {
		return get(0);
	}

	@Override
	public Item get(final int arg0) {
		if (arg0 >= size_) {
			throw new IndexOutOfBoundsException();
		}
		String itemName = itemNames_.get(arg0);
		return doc_.getFirstItem(itemName);
	}

	public String[] getNames() {
		return itemNames_.toArray(new String[itemNames_.size()]);
	}

	@Override
	@Deprecated
	public int indexOf(final Object arg0, final int arg1) {
		throw new UnimplementedException("indexOf with two arguments not yet implemented because it's a deprecated call anyway!");
	}

	@Override
	public int indexOf(final Object arg0) {
		if (arg0 instanceof Item) {
			return itemNames_.indexOf(((Item) arg0).getName());
		} else if (arg0 instanceof String) {
			return itemNames_.indexOf((String) arg0);
		}
		return -1;
	}

	@Override
	@Deprecated
	public void insertElementAt(final Item arg0, final int arg1) {
		add(arg1, arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.impl.Vector#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return itemNames_.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.impl.Vector#size()
	 */
	@Override
	public int size() {
		return itemNames_.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.impl.Vector#capacity()
	 */
	@Override
	public int capacity() {
		return itemNames_.size();
	}

	@Override
	public Iterator<Item> iterator() {
		return new ItemVectorIterator(doc_, this);
	}

	@Override
	@Deprecated
	public Item lastElement() {
		return doc_.getFirstItem(itemNames_.get(itemNames_.size() - 1));
	}

	@Override
	@Deprecated
	public int lastIndexOf(final Object arg0, final int arg1) {
		throw new UnimplementedException("lastIndexOf with two arguments not yet implemented because it's a deprecated call anyway!");
	}

	@Override
	public int lastIndexOf(final Object arg0) {
		if (arg0 instanceof Item) {
			return itemNames_.lastIndexOf(((Item) arg0).getName());
		} else if (arg0 instanceof String) {
			return itemNames_.lastIndexOf((String) arg0);
		}
		return -1;
	}

	@Override
	public ListIterator<Item> listIterator() {
		return new ItemVectorIterator(doc_, this);
	}

	@Override
	public ListIterator<Item> listIterator(final int arg0) {
		return new ItemVectorIterator(doc_, this);
	}

	@Override
	public Item remove(final int arg0) {
		String name = itemNames_.remove(arg0);
		return doc_.getFirstItem(name);
	}

	@Override
	public boolean remove(final Object arg0) {
		if (arg0 instanceof Item) {
			String name = ((Item) arg0).getName();
			return itemNames_.remove(name);
		} else if (arg0 instanceof String) {
			return itemNames_.remove((String) arg0);
		}
		return itemNames_.remove(arg0);
	}

	@Override
	public boolean removeAll(final Collection<?> arg0) {
		List<String> names = new ArrayList<String>();
		for (Object o : arg0) {
			if (o instanceof Item) {
				names.add(((Item) o).getName());
			} else if (o instanceof String) {
				names.add((String) o);
			}
		}
		return itemNames_.removeAll(names);
	}

	@Override
	public void removeAllElements() {
		clear();
	}

	@Override
	public boolean removeElement(final Object arg0) {
		return remove(arg0);
	}

	@Override
	public void removeElementAt(final int arg0) {
		remove(arg0);
	}

	@Override
	public boolean retainAll(final Collection<?> arg0) {
		List<String> names = new ArrayList<String>();
		for (Object o : arg0) {
			if (o instanceof Item) {
				names.add(((Item) o).getName());
			} else if (o instanceof String) {
				names.add((String) o);
			}
		}
		return itemNames_.retainAll(names);
	}

	@Override
	public Item set(final int arg0, final Item arg1) {
		itemNames_.set(arg0, arg1.getName());
		return arg1;
	}

	@Override
	@Deprecated
	public void setElementAt(final Item arg0, final int arg1) {
		set(arg1, arg0);
	}

	@Override
	public List<Item> subList(final int arg0, final int arg1) {
		ItemVector result = new ItemVector(doc_, false);
		for (int i = arg0; i <= arg1; i++) {
			result.add(itemNames_.get(i));
		}
		return result;
	}

}

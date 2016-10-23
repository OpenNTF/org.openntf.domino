package org.openntf.domino.graph2;

import java.util.Iterator;
import java.util.ListIterator;

import com.tinkerpop.blueprints.Element;

public interface DElementIterable {
	public interface DElementIterator extends ListIterator<Element> {

	}

	public Iterator<? extends Element> iterator();

	public ListIterator<? extends Element> listIterator();
}

/**
 * 
 */
package org.openntf.domino.utils.xml;

import java.util.AbstractList;
import java.util.logging.Logger;

/**
 * @author jgallagher
 * 
 */
public class ModifiableStringNodeList extends AbstractList<String> {
	private static final Logger log_ = Logger.getLogger(ModifiableStringNodeList.class.getName());
	private static final long serialVersionUID = 1L;

	private final XMLNode xml_;
	private final String parentNodePath_;
	private final String nodeName_;

	public ModifiableStringNodeList(final XMLNode xml, final String parentNodePath, final String nodeName) {
		xml_ = xml;
		parentNodePath_ = parentNodePath;
		nodeName_ = nodeName;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractList#get(int)
	 */
	@Override
	public String get(final int index) {
		return xml_.selectNodes(parentNodePath_ + "/" + nodeName_).get(index).getText();
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#size()
	 */
	@Override
	public int size() {
		return xml_.selectNodes(parentNodePath_ + "/" + nodeName_).size();
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractList#add(java.lang.Object)
	 */
	@Override
	public boolean add(final String e) {
		XMLNode parent = xml_.selectSingleNode(parentNodePath_);
		// TODO Add code to add missing parent trees as needed
		XMLNode node = parent.addChildElement(nodeName_);
		node.setText(e);
		return true;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractList#set(int, java.lang.Object)
	 */
	@Override
	public String set(final int index, final String element) {
		String current = get(index);
		xml_.selectNodes(parentNodePath_ + "/" + nodeName_).get(index).setText(element);
		return current;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractList#remove(int)
	 */
	@Override
	public String remove(final int index) {
		String current = get(index);
		xml_.selectNodes(parentNodePath_ + "/" + nodeName_).remove(index);
		return current;
	}
}

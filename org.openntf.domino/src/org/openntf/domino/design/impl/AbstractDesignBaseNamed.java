/**
 * 
 */
package org.openntf.domino.design.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.openntf.domino.Document;
import org.openntf.domino.design.DesignBaseNamed;

/**
 * @author jgallagher
 * 
 */
public abstract class AbstractDesignBaseNamed extends AbstractDesignBase implements DesignBaseNamed {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(AbstractDesignBaseNamed.class.getName());
	private static final long serialVersionUID = 1L;

	private String title_ = null;
	private List<String> aliases_ = null;

	/**
	 * @param document
	 */
	public AbstractDesignBaseNamed(Document document) {
		super(document);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#getAliases()
	 */
	@Override
	public List<String> getAliases() {
		if (title_ == null) {
			fetchTitle();
		}
		return new ArrayList<String>(aliases_);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#getName()
	 */
	@Override
	public String getName() {
		if (title_ == null) {
			fetchTitle();
		}
		return title_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#setAlias(java.lang.String)
	 */
	@Override
	public void setAlias(String alias) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#setAliases(java.lang.Iterable)
	 */
	@Override
	public void setAliases(Iterable<String> aliases) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	private void fetchTitle() {
		// Sometimes $TITLE is a multi-value field of title + aliases.
		// Sometimes it's a |-delimited single value.
		// Meh!

		List<String> titles = getDocument().getItemValue("$TITLE");
		if (titles.size() == 0) {
			String titleField = getDocument().getItemValueString("$TITLE");
			if (titleField.contains("|")) {
				String[] bits = titleField.split("\\|");
				title_ = bits[0];
				aliases_ = new ArrayList<String>(bits.length - 1);
				for (int i = 1; i < bits.length; i++) {
					aliases_.add(bits[i]);
				}
			} else {
				title_ = titleField;
				aliases_ = new ArrayList<String>(0);
			}
		} else if (titles.size() == 1) {
			title_ = titles.get(0);
			aliases_ = new ArrayList<String>(0);
		} else {
			title_ = titles.get(0);
			aliases_ = titles.subList(1, titles.size());
		}

	}
}

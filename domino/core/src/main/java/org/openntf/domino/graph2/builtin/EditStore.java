package org.openntf.domino.graph2.builtin;

import org.openntf.domino.graph2.impl.DElementStore;

public class EditStore extends DElementStore {

	public EditStore() {
		this.addType(Edits.class);
		this.addType(Editable.class);
	}

}

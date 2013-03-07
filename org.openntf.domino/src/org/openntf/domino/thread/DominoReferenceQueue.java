package org.openntf.domino.thread;

import java.lang.ref.ReferenceQueue;

public class DominoReferenceQueue extends ReferenceQueue<org.openntf.domino.Base> {

	public DominoReferenceQueue() {
		// this should only happen once per-thread
	}

}

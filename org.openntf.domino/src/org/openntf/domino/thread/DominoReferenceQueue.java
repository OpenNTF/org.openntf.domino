package org.openntf.domino.thread;

import java.lang.ref.ReferenceQueue;

import org.openntf.domino.Base;

public class DominoReferenceQueue extends ReferenceQueue<Base> {

	public DominoReferenceQueue() {
		// this should only happen once per-thread
	}

}

package org.openntf.domino.design.impl;

import java.io.IOException;
import java.io.OutputStream;

class CountOutputStream extends OutputStream {
	private int count;

	@Override
	public void write(final int b) throws IOException {
		count += 1;
	}

	@Override
	public void write(final byte[] buf) throws IOException {
		count += buf.length;
	}

	@Override
	public void write(final byte[] buf, final int start, final int len) throws IOException {
		count += len;
	}

	public int getCount() {
		return count;
	}
}
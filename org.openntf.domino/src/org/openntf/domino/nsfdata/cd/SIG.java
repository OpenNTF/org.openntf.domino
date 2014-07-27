package org.openntf.domino.nsfdata.cd;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class SIG implements Serializable {

	private CDSignature signature_;
	private int length_;


	protected SIG(final CDSignature signature, final int length) {
		signature_ = signature;
		length_ = length;
	}

	public CDSignature getSignature() { return signature_; }

	public int getLength() { return length_; }

	public abstract int getSigLength();
}

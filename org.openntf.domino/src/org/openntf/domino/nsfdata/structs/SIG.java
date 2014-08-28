package org.openntf.domino.nsfdata.structs;

import java.io.Serializable;

import org.openntf.domino.nsfdata.structs.cd.CDSignature;

@SuppressWarnings("serial")
public abstract class SIG implements Serializable {

	private CDSignature signature_;
	private long length_;

	protected SIG(final CDSignature signature, final long length) {
		signature_ = signature;
		length_ = length;
	}

	public CDSignature getSignature() {
		return signature_;
	}

	public long getLength() {
		return length_;
	}

	public abstract int getSigLength();

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ", Signature: " + getSignature() + ", Length: " + getLength() + "]";
	}
}

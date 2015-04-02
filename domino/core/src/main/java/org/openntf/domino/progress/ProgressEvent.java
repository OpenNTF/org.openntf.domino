package org.openntf.domino.progress;

public class ProgressEvent {
	private String info_;

	ProgressEvent(final String info) {
		info_ = info;
	}

	public String getInfo() {
		return info_;
	}

}

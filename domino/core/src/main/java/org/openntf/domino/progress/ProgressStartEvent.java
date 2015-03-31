package org.openntf.domino.progress;

public class ProgressStartEvent extends ProgressEvent {

	private int work_;
	private int weight_;

	public ProgressStartEvent(final int work, final int weight, final String info) {
		super(info);
		this.weight_ = weight;
		this.work_ = work;
	}

	public int getWeight() {
		return weight_;
	}

	public int getWork() {
		return work_;
	}
}

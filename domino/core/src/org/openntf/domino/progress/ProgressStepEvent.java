package org.openntf.domino.progress;

public class ProgressStepEvent extends ProgressEvent {

	private int steps_;

	public ProgressStepEvent(final int steps, final String info) {
		super(info);
		steps_ = steps;
	}

	public int getSteps() {
		return steps_;
	}

}

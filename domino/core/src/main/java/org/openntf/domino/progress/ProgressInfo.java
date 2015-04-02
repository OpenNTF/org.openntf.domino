package org.openntf.domino.progress;

import java.util.ArrayList;
import java.util.List;

public class ProgressInfo {

	private ProgressInfo parent;
	private List<ProgressInfo> children = new ArrayList<ProgressInfo>();

	private int weight;
	private int workTodo;
	private int workDone;
	private boolean allDone;

	public ProgressInfo(final ProgressInfo parent, final int weight, final int workTodo) {
		// TODO Auto-generated constructor stub
		if (parent != null) {
			parent.children.add(this);
		}
		this.parent = parent;
		this.weight = weight;
		this.workTodo = workTodo;
	}

	public void step(final int steps) {
		// TODO Auto-generated method stub
		int tmp = workDone + steps;
		if (tmp > workTodo) {
			workDone = workTodo;
		} else {
			workDone = tmp;
		}

	}

	public void stop() {
		workDone = workTodo;
		allDone = true;
	}

	public double getProgress() {
		if (allDone) {
			return 1.0;
		}
		if (workTodo == 0) {
			return 0.5;
		}
		double ret = workDone / (double) workTodo; // my work
		for (ProgressInfo child : children) { // plus work of all children
			ret += child.getProgress() * child.weight / workTodo;
		}
		if (ret >= 0.99999999) { // almost done.
			stop();
			ret = 1.0;
		}

		return ret;

	}

	public ProgressInfo getParent() {
		return parent;
	}
}

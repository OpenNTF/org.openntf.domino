package org.openntf.domino.progress;

import java.util.Observable;

public class ProgressObservable extends Observable {

	/**
	 * Starts a task
	 * 
	 * @param work
	 *            the amount of work that is processed in this task (-1 if work is unknown)
	 * @param weight
	 *            the weight of this task (if it is a subtask)
	 * @param info
	 *            an InfoObject
	 * 
	 */
	public void progressStart(final int work, final int weight, final String info) {
		setChanged();
		notifyObservers(new ProgressStartEvent(work, weight, info));
	}

	/**
	 * Starts a task. The weight of this task is 1
	 * 
	 * @param work
	 *            the amount of work that is processed in this task (-1 if work is unknown)
	 * @param info
	 *            an InfoObject
	 */
	public void progressStart(final int work, final String info) {
		progressStart(work, 1, info);
	}

	/**
	 * performs the amount of steps in this task
	 * 
	 * @param steps
	 *            the amount of steps
	 * @param info
	 *            an InfoObject
	 */
	public void progressStep(final int steps, final String info) {
		setChanged();
		notifyObservers(new ProgressStepEvent(steps, info));
	}

	/**
	 * performs one step in this task
	 * 
	 * @param info
	 *            an InfoObject
	 */
	public void progressStep(final String info) {
		progressStep(1, info);
	}

	/**
	 * Stops the given task. This method MUST be called if you have called startTask.
	 */
	public void progressStop(final String info) {
		setChanged();
		notifyObservers(new ProgressStopEvent(info));
	}
}

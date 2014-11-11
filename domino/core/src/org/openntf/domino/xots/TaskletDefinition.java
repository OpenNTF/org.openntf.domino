package org.openntf.domino.xots;

import org.openntf.domino.thread.model.Schedule;
import org.openntf.domino.thread.model.Trigger;

public class TaskletDefinition {

	private Schedule schedule;
	private Trigger trigger;
	private String className;
	private transient Runnable instance;

	public TaskletDefinition(final Class<? extends Runnable> clazz) {
		// TODO Auto-generated constructor stub
		schedule = clazz.getAnnotation(Schedule.class);
		trigger = clazz.getAnnotation(Trigger.class);
		className = clazz.getName();
	}

	public TaskletDefinition(final Runnable runnable) {
		this(runnable.getClass());
		instance = runnable;
	}

	@Override
	public String toString() {
		return "TaskletDefinition [schedule=" + schedule + ", trigger=" + trigger + "]";
	}

}

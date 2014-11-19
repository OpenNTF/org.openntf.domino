package org.openntf.domino.xots;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

/*
 * This class and package is intended to become the space for the XPages implementation
 * of IBM's DOTS. Except it will use modern thread management instead of acting like it was
 * written in Java 1.1
 */
/**
 * Use XotsService now!
 */
@Deprecated
public class XotsDaemon {

	;
	private XotsDaemon() {
		super();
	}

	@Deprecated
	public static ScheduledExecutorService getInstance() {
		return Xots.getInstance();
	}

	// ---- delegate methods

	/**
	 * Submits the callable to the executor for immediate execution
	 * 
	 * @param callable
	 * @return
	 */
	@Deprecated
	public static <T> Future<T> queue(final Callable<T> callable) {
		return Xots.queue(callable);
	}

	/**
	 * Submits the runnable to the executor for immediate execution
	 * 
	 * @param callable
	 * @return
	 */
	@Deprecated
	public static Future<?> queue(final Runnable callable) {
		return Xots.queue(callable);
	}

/**
	 * Registers a new tasklet for periodic execution
	 * @param moduleName
	 *            the ModuleName (i.e. DatabaseName)
	 * @param className
	 *            the ClassName. (Must be annotated with {@link XotsTasklet
	 * @param onEvent
	 * String array with events
	 * @return
	 */
	@Deprecated
	public static Future<?> registerTasklet(final String moduleName, final String className, final String... cron) {
		//CronExpression
		return null;

	}
}

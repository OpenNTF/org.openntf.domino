package org.openntf.domino.instrument;

public enum Profiler {
	;

	static ThreadLocal<ThreadProfiler> threadProfiler = new ThreadLocal<ThreadProfiler>() {
		@Override
		protected ThreadProfiler initialValue() {
			return new ThreadProfiler();
		}
	};

	public static void enter() {
		long wallTime = System.nanoTime();
		threadProfiler.get().enter(wallTime);
	}

	public static void leave(final Class<?> cls, final String method, final String signature) {
		long wallTime = System.nanoTime();
		threadProfiler.get().leave(cls, method, signature, wallTime);
	}
}

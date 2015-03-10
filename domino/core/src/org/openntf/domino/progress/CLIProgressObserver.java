package org.openntf.domino.progress;

import java.io.PrintStream;

public class CLIProgressObserver extends ProgressObserver {

	private PrintStream out_;
	private int lastLen;
	String spaces = "                                                                                                                                                                                                                              ";

	public CLIProgressObserver(final int workTodo, final PrintStream out) {
		super(workTodo);
		out_ = out;
	}

	@Override
	protected void stop(final String info) {
		out_.println("\n");

	}

	@Override
	protected void start(final String info) {

	}

	@Override
	protected void progress(final double progress, final String info) {
		String s = String.format("[ %3d%% ] %s", (int) (progress * 100), info);

		int pad = lastLen - s.length();
		lastLen = s.length();
		StringBuilder sb = new StringBuilder();
		sb.append(s);
		if (pad > 0) {

			while (pad-- > 0)
				sb.append(' ');
		}

		sb.append('\r');
		out_.print(sb.toString());

	}
}

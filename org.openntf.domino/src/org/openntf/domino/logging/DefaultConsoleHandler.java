package org.openntf.domino.logging;

import java.io.OutputStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class DefaultConsoleHandler extends ConsoleHandler {

	public DefaultConsoleHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		super.close();
	}

	@Override
	public void publish(LogRecord arg0) {
		// TODO Auto-generated method stub
		super.publish(arg0);
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub
		super.flush();
	}

	@Override
	protected void setOutputStream(OutputStream arg0) {
		// TODO Auto-generated method stub
		super.setOutputStream(arg0);
	}

	@Override
	public Formatter getFormatter() {
		// TODO Auto-generated method stub
		return super.getFormatter();
	}

	@Override
	protected void reportError(String msg, Exception ex, int code) {
		// TODO Auto-generated method stub
		super.reportError(msg, ex, code);
	}

	@Override
	public void setFormatter(Formatter newFormatter) {
		// TODO Auto-generated method stub
		super.setFormatter(newFormatter);
	}

}

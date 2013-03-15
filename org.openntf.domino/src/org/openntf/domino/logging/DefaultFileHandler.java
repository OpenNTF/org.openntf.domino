package org.openntf.domino.logging;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class DefaultFileHandler extends FileHandler {

	public DefaultFileHandler() throws IOException {
		// TODO Auto-generated constructor stub
	}

	public DefaultFileHandler(String pattern) throws IOException {
		super(pattern);
		// TODO Auto-generated constructor stub
	}

	public DefaultFileHandler(String pattern, boolean append) throws IOException {
		super(pattern, append);
		// TODO Auto-generated constructor stub
	}

	public DefaultFileHandler(String pattern, int limit, int count) throws IOException {
		super(pattern, limit, count);
		// TODO Auto-generated constructor stub
	}

	public DefaultFileHandler(String pattern, int limit, int count, boolean append) throws IOException {
		super(pattern, limit, count, append);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		super.close();
	}

	@Override
	public synchronized void publish(LogRecord record) {
		// TODO Auto-generated method stub
		super.publish(record);
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub
		super.flush();
	}

	@Override
	protected void setOutputStream(OutputStream out) {
		// TODO Auto-generated method stub
		super.setOutputStream(out);
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

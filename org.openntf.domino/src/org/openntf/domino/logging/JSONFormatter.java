package org.openntf.domino.logging;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class JSONFormatter extends Formatter {

	// private XMLFormatter xf;
	private boolean compact;
	private int indentLevel;
	// private Writer _writer;
	private StringBuilder _builder;
	private boolean UTC_Format = false;
	private int objectLevels = 0;
	private boolean first[] = new boolean[32]; // max 32 for now...

	public JSONFormatter() {
		_builder = new StringBuilder();
	}

	public boolean isUTC_Format() {
		return UTC_Format;
	}

	public void setUTC_Format(boolean uTC_Format) {
		UTC_Format = uTC_Format;
	}

	@Override
	public String format(LogRecord record) {
		try {
			startObject();

			startProperty("level");
			out(record.getLevel().getName());
			endProperty();

			startProperty("message");
			out(record.getMessage());
			endProperty();

			startProperty("event");
			out(record.getSourceClassName() + "." + record.getSourceMethodName() + "()");
			endProperty();

			startProperty("time");
			out(record.getMillis());
			endProperty();

			startProperty("datetime");
			Date recordDate = new Date(record.getMillis());
			out(LogUtils.dateToString(recordDate, UTC_Format));
			endProperty();

			formatThrowable(record);
			endObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _builder.toString();
	}

	private void formatThrowable(LogRecord record) {
		try {
			startProperty("exception");
			startArray();
			for (StackTraceElement element : record.getThrown().getStackTrace()) {
				startProperty("message");
				startObject();

				startProperty("class");
				out(element.getClassName());
				endProperty();

				startProperty("method");
				out(element.getMethodName());
				endProperty();

				startProperty("line");
				out(element.getLineNumber());
				endProperty();
				out(element.getClassName() + "." + element.getMethodName() + "()");
				endObject();
				endProperty();
			}
			endArray();
			endProperty();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getHead(Handler h) {
		return "{\"id\": \"0001\",\"records\":";

	}

	@Override
	public String getTail(Handler h) {
		return "";
	}

	public void startObject() throws IOException {
		nl();
		indent();
		out('{');
		first[++objectLevels] = true;
		incIndent();
	}

	public void endObject() throws IOException {
		nl();
		decIndent();
		indent();
		out('}');
		first[--objectLevels] = false;
	}

	public void startArray() throws IOException {
		nl();
		indent();
		out('[');
		first[++objectLevels] = true;
		incIndent();
	}

	public void endArray() throws IOException {
		nl();
		decIndent();
		indent();
		out(']');
		first[--objectLevels] = false;
	}

	public void startArrayItem() throws IOException {
		if (!first[objectLevels]) {
			out(',');
		}
	}

	public void endArrayItem() throws IOException {
		first[objectLevels] = false;
	}

	public void startProperty(String propertyName) throws IOException {
		if (!first[objectLevels]) {
			out(',');
		} else {
			first[objectLevels] = false;
		}
		nl();
		incIndent();
		indent();
		out(propertyName);
		out(':');
	}

	public void endProperty() throws IOException {
		decIndent();
	}

	public void out(char paramChar) throws IOException {
		_builder.append(paramChar);
	}

	public void out(String paramString) throws IOException {
		_builder.append(paramString);
	}

	public void out(int paramint) throws IOException {
		_builder.append(paramint);
	}

	public void out(long paramlong) throws IOException {
		_builder.append(paramlong);
	}

	public int getIndentLevel() {
		return this.indentLevel;
	}

	public void setIndentLevel(int paramInt) {
		this.indentLevel = paramInt;
	}

	public void incIndent() {
		this.indentLevel += 1;
	}

	public void decIndent() {
		this.indentLevel -= 1;
	}

	public boolean isCompact() {
		return this.compact;
	}

	public void indent() throws IOException {
		if ((!(this.compact)) && (this.indentLevel > 0))
			for (int i = 0; i < this.indentLevel; ++i)
				out("  ");
	}

	public void nl() throws IOException {
		if (!(this.compact))
			out('\n');
	}

}

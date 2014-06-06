package org.openntf.domino.logging;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * JSONFormatter class for outputting logs as JSON objects. Not used currently and should probably be replaced with Java classes better
 * designed to write JSON rather than building content up with a StringBuilder
 */
public class JSONFormatter extends Formatter {

	// private XMLFormatter xf;
	/** The compact. */
	private boolean compact;

	/** The indent level. */
	private int indentLevel;
	// private Writer _writer;
	/** The _builder. */
	private StringBuilder _builder;

	/** The UT c_ format. */
	private boolean UTC_Format = false;

	/** The object levels. */
	private int objectLevels = 0;

	/** The first. */
	private boolean first[] = new boolean[32]; // max 32 for now...

	/**
	 * Instantiates a new jSON formatter.
	 */
	public JSONFormatter() {
		_builder = new StringBuilder();
	}

	/**
	 * Checks if is uT c_ format.
	 * 
	 * @return true, if is uT c_ format
	 */
	public boolean isUTC_Format() {
		return UTC_Format;
	}

	/**
	 * Sets the uT c_ format.
	 * 
	 * @param uTC_Format
	 *            the new uT c_ format
	 */
	public void setUTC_Format(final boolean uTC_Format) {
		UTC_Format = uTC_Format;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
	 */
	@Override
	public String format(final LogRecord record) {
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

	/**
	 * Format throwable.
	 * 
	 * @param record
	 *            the record
	 */
	private void formatThrowable(final LogRecord record) {
		try {
			if (record.getThrown() != null) {
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
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.logging.Formatter#getHead(java.util.logging.Handler)
	 */
	@Override
	public String getHead(final Handler h) {
		return "{\"id\": \"0001\",\"records\":";

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.logging.Formatter#getTail(java.util.logging.Handler)
	 */
	@Override
	public String getTail(final Handler h) {
		return "";
	}

	/**
	 * Start object.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void startObject() throws IOException {
		nl();
		indent();
		out('{');
		first[++objectLevels] = true;
		incIndent();
	}

	/**
	 * End object.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void endObject() throws IOException {
		nl();
		decIndent();
		indent();
		out('}');
		first[--objectLevels] = false;
	}

	/**
	 * Start array.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void startArray() throws IOException {
		nl();
		indent();
		out('[');
		first[++objectLevels] = true;
		incIndent();
	}

	/**
	 * End array.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void endArray() throws IOException {
		nl();
		decIndent();
		indent();
		out(']');
		first[--objectLevels] = false;
	}

	/**
	 * Start array item.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void startArrayItem() throws IOException {
		if (!first[objectLevels]) {
			out(',');
		}
	}

	/**
	 * End array item.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void endArrayItem() throws IOException {
		first[objectLevels] = false;
	}

	/**
	 * Start property.
	 * 
	 * @param propertyName
	 *            the property name
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void startProperty(final String propertyName) throws IOException {
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

	/**
	 * End property.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void endProperty() throws IOException {
		decIndent();
	}

	/**
	 * Out.
	 * 
	 * @param paramChar
	 *            the param char
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void out(final char paramChar) throws IOException {
		_builder.append(paramChar);
	}

	/**
	 * Out.
	 * 
	 * @param paramString
	 *            the param string
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void out(final String paramString) throws IOException {
		_builder.append(paramString);
	}

	/**
	 * Out.
	 * 
	 * @param paramint
	 *            the paramint
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void out(final int paramint) throws IOException {
		_builder.append(paramint);
	}

	/**
	 * Out.
	 * 
	 * @param paramlong
	 *            the paramlong
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void out(final long paramlong) throws IOException {
		_builder.append(paramlong);
	}

	/**
	 * Gets the indent level.
	 * 
	 * @return the indent level
	 */
	public int getIndentLevel() {
		return this.indentLevel;
	}

	/**
	 * Sets the indent level.
	 * 
	 * @param paramInt
	 *            the new indent level
	 */
	public void setIndentLevel(final int paramInt) {
		this.indentLevel = paramInt;
	}

	/**
	 * Inc indent.
	 */
	public void incIndent() {
		this.indentLevel += 1;
	}

	/**
	 * Dec indent.
	 */
	public void decIndent() {
		this.indentLevel -= 1;
	}

	/**
	 * Checks if is compact.
	 * 
	 * @return true, if is compact
	 */
	public boolean isCompact() {
		return this.compact;
	}

	/**
	 * Indent.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void indent() throws IOException {
		if ((!(this.compact)) && (this.indentLevel > 0))
			for (int i = 0; i < this.indentLevel; ++i)
				out("  ");
	}

	/**
	 * Nl.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void nl() throws IOException {
		if (!(this.compact))
			out('\n');
	}

}

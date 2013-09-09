/*
 * © Copyright IBM Corp. 2012-2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package com.ibm.commons.util.print;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.commons.util.EmptyIterator;
import com.ibm.commons.util.FastStringBuffer;
import com.ibm.commons.util.StringUtil;

//import com.ibm.commons.util.TDiag;

/**
 * @ibm-not-published
 */
public class TablePrinter {
	private static final Logger log_ = Logger.getLogger(TablePrinter.class.getName());

	public interface ITable {
		// Column Access
		public int getColumnCount() throws Exception;

		public String getColumnTitle(int col) throws Exception;

		public int getColumnSize(int col) throws Exception;

		// Row Access
		public Iterator getRows(int start, int count) throws Exception;
	}

	public interface IRow {
		public String getRowValue(int col) throws Exception;
	}

	private PrintStream ps;
	private ITable table;

	public TablePrinter() {
	}

	public TablePrinter(final ITable table) {
		this.table = table;
	}

	public void println(final String s) {
		getOutputStream().println(s);
	}

	public PrintStream getPrintStream() {
		return ps;
	}

	public void setPrintStream(final PrintStream ps) {
		this.ps = ps;
	}

	public PrintStream getOutputStream() {
		return ps != null ? ps : System.out;
	}

	public int getMaxColSize() {
		return 128;
	}

	public ITable getTable() {
		return table;
	}

	public void setTable(final ITable table) {
		this.table = table;
	}

	public int print() {
		return print(0, Integer.MAX_VALUE);
	}

	public int print(final int start, final int c) {
		boolean printIfEmpty = true;
		try {
			Iterator it = table != null ? table.getRows(start, c) : EmptyIterator.getInstance();
			if (it.hasNext() || printIfEmpty) {
				prtSeparator();
				prtHeader();
				prtSeparator();
			}

			int rowCount = 0;
			while (rowCount < c && it.hasNext()) {
				IRow row = (IRow) it.next();
				prtRow(row);
				rowCount++;
			}

			if (rowCount > 0 || printIfEmpty) {
				prtSeparator();
			}
			return rowCount;
		} catch (Exception e) {
			log_.log(Level.WARNING, e.getMessage(), e);
			//            TDiag.exception(e);
		}
		return 0;
	}

	private void prtSeparator() throws Exception {
		FastStringBuffer b = new FastStringBuffer();
		int colCount = table.getColumnCount();
		for (int i = 0; i < colCount; i++) {
			int sz = Math.min(table.getColumnSize(i), getMaxColSize());
			b.append('+');
			b.repeat('-', sz);
		}
		b.append('+');
		println(b.toString());
	}

	private void prtHeader() throws Exception {
		FastStringBuffer b = new FastStringBuffer();
		int colCount = table.getColumnCount();
		for (int i = 0; i < colCount; i++) {
			int sz = Math.min(table.getColumnSize(i), getMaxColSize());
			b.append('|');
			b.append(pad(table.getColumnTitle(i), sz));
		}
		b.append('|');
		println(b.toString());
	}

	private void prtRow(final IRow row) throws Exception {
		FastStringBuffer b = new FastStringBuffer();
		int colCount = table.getColumnCount();
		for (int i = 0; i < colCount; i++) {
			int sz = Math.min(table.getColumnSize(i), getMaxColSize());
			b.append('|');
			b.append(pad(colString(row, i), sz));
		}
		b.append('|');
		println(b.toString());
	}

	private String colString(final IRow row, final int col) throws Exception {
		return row.getRowValue(col);
	}

	private static String pad(final String s, final int sz) {
		if (s != null) {
			int strLen = s.length();
			if (strLen == sz) {
				return s;
			}
			if (strLen > sz) {
				return s.substring(0, sz);
			}
			return s + StringUtil.repeat(' ', sz - strLen);
		} else {
			return StringUtil.repeat(' ', sz);
		}
	}
}

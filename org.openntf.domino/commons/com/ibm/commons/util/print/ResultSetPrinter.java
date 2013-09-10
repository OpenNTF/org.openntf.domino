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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Iterator;

/**
 * @ibm-not-published 
 */
public class ResultSetPrinter extends TablePrinter {
	
	private class ViewTable implements ITable {
        ResultSet rs;
        ResultSetMetaData meta;
		ViewTable(ResultSet rs) {
			this.rs = rs;
			try {
				this.meta = rs.getMetaData();
			} catch(SQLException ex) {ex.printStackTrace();}
		}
		public int getColumnCount() throws Exception {
			return meta!=null ? meta.getColumnCount() : 0;
		}
		public String getColumnTitle(int col) throws Exception {
			return meta.getColumnLabel(col+1);
		}
		public int getColumnSize(int col) throws Exception {
	        int sz = meta.getColumnDisplaySize(col+1);
	        int type = meta.getColumnType(col+1);
	        switch( type ) {
	            case Types.CHAR:
	            case Types.VARCHAR:
	            case Types.LONGVARCHAR: {
	                sz = Math.min( Math.max( sz, 20 ), 64 );
	            } break;
	            case Types.OTHER: {
	            	sz = 32;
	            }
	        }
	        if(sz<=10) {
	        	sz = 10;
	        }
	        return sz;
		}
		
		public Iterator getRows(int start, int count) throws Exception {
			if(start>0) {
				rs.relative(start);
			}
			return new ViewRowIterator(rs);
		}
	}
	private class ViewRowIterator implements Iterator {
		ResultSet rs;
		Boolean next;
		ViewRowIterator(ResultSet rs) {
			this.rs = rs;
		}
	    public boolean hasNext() {
	    	if(next==null) {
	    		try {
	    			next = Boolean.valueOf(rs.next());
	    		} catch(SQLException ex) {
	    			ex.printStackTrace();
	    			next = Boolean.FALSE;
	    		}
	    	}
	    	return next.booleanValue();
	    }
	    public Object next() {
	    	if(next!=null && next.booleanValue()) {
	    		next = null;
	    		return new ViewRow(rs);
	    	}
	    	return null;
	    }
	    public void remove() {
	    }
	}
	private class ViewRow implements IRow {
		ResultSet rs;
		public ViewRow(ResultSet rs) {
			this.rs = rs;
		}
		public String getRowValue(int col) {
    		try {
    			return rs.getString(col+1);
    		} catch(SQLException ex) {
    			ex.printStackTrace();
    			return "<????>";
    		}
		}
	}

	
	public ResultSetPrinter(ResultSet rs) {
		setTable(new ViewTable(rs));
	}
}

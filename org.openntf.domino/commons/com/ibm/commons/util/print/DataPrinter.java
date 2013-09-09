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

import java.util.ArrayList;
import java.util.Iterator;

import com.ibm.commons.util.StringUtil;

/**
 * @ibm-not-published 
 */
public class DataPrinter extends TablePrinter {
    
    public static class Column {
        String  name;
        int     size;
        public Column(String name, int size) {
            this.name = name;
            this.size = size;
        }
        public Column(String name) {
            this(name,16);
        }
    }
    private class DataTable implements ITable {
        DataTable() {
        }
        public int getColumnCount() throws Exception {
            return columns.length;
        }
        public String getColumnTitle(int col) throws Exception {
            return columns[col].name;
        }
        public int getColumnSize(int col) throws Exception {
            return columns[col].size;
        }
        
        public Iterator getRows(int start, int count) throws Exception {
            Iterator it = rows.iterator();
            if(start>0) {
                while(it.hasNext() && start>0) {
                    it.next();
                }
            }
            return new DataRowIterator(it);
        }
    }
    private class DataRowIterator implements Iterator {
        Iterator it;
        DataRowIterator(Iterator it) {
            this.it = it;
        }
        public boolean hasNext() {
            return it.hasNext();
        }
        public Object next() {
            return new DataRow(it.next());
        }
        public void remove() {
        }
    }
    private class DataRow implements IRow {
        Object instance;
        public DataRow(Object instance) {
            this.instance = instance;
        }
        public String getRowValue(int col) {
            if(instance!=null) {
                Object o = ((Object[])instance)[col];
                if(o!=null) {
                    return o.toString();
                }
            }
            return "<null>"; // $NON-NLS-1$
        }
    }

    private Column[] columns;
    private ArrayList rows;
    
    public DataPrinter(Column[] columns) {
        this.columns = columns;
        this.rows = new ArrayList();
        setTable(new DataTable());
    }

    public DataPrinter(String[] columns) {
        this.columns = new Column[columns.length];
        for(int i=0; i<columns.length; i++) {
            this.columns[i] = new Column(columns[i]);
        }
        this.rows = new ArrayList();
        setTable(new DataTable());
    }
    
    public void addRow(Object[] row) {
        // Compute the column size
        if(row!=null) {
            for( int i=0; i<row.length; i++) {
                if(row[i]!=null) {
                    String s = row[i].toString();
                    if(StringUtil.isNotEmpty(s)) {
                        int sz = Math.min(s.length(),200);
                        columns[i].size = Math.max(columns[i].size, sz);
                    }
                }
            }
        }
        rows.add(row);
    }
    
    public void addRow(Object v1) {
        Object[] v = new Object[]{v1};
        addRow(v);
    }
    
    public void addRow(Object v1, Object v2) {
        Object[] v = new Object[]{v1,v2};
        addRow(v);
    }
    
    public void addRow(Object v1, Object v2, Object v3) {
        Object[] v = new Object[]{v1,v2,v3};
        addRow(v);
    }
    
    public void addRow(Object v1, Object v2, Object v3, Object v4) {
        Object[] v = new Object[]{v1,v2,v3,v4};
        addRow(v);
    }
    
    public void addRow(Object v1, Object v2, Object v3, Object v4, Object v5) {
        Object[] v = new Object[]{v1,v2,v3,v4,v5};
        addRow(v);
    }
}
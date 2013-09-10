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

package com.ibm.commons.util.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import com.ibm.commons.util.StringUtil;

/**
 * This Trace class is used in very particular cases when normal trace cannot be used.
 */
public final class TraceFile {

	public static void trace( String file, String msg, Object...p) {
		(new TraceFile(file)).trace(msg, p);
	}
	public static void dumpStack( String file, String msg, Object...p) {
		(new TraceFile(file)).dumpStack(msg, p);
	}

	private static Set<String> files;
	
	private String file;
	private PrintWriter pw;
	private int lockCount;
	
	public TraceFile(String file) {
		if(files==null) {
			files = new HashSet<String>();
		}
		this.file = file;
		if(!files.contains(file)) {
			File f = new File(file);
			if(f.exists()) {
				f.delete();
			}
			files.add(file);
		}
	}
	
	protected PrintWriter createPrintWriter() throws IOException {
		return new PrintWriter(new FileWriter(file, true));
	}
		
	
	public void lock() {
		if(lockCount==0) {
			try {
				pw = createPrintWriter();
			} catch(IOException ex) {
				// Ignore and return
				return;
			}
		}
		lockCount++;
	}
	
	public void unlock() {
		lockCount--;
		if(lockCount==0 && pw!=null) {
			pw.close();
		}
		lockCount++;
	}
	
	public void flush() {
		if(pw!=null) {
			pw.flush();
		}
	}
	
	public void trace( String msg, Object...p) {
		String s = StringUtil.format(msg,p);
		output(s,false);
	}
	
	public void dumpStack( String msg, Object...p) {
		String s = StringUtil.format(msg,p);
		output(s,true);
	}
	
	private void output( String s, boolean stack ) {
		try {
			synchronized(TraceFile.class) {
				PrintWriter p = pw!=null ? pw : createPrintWriter();
				try {
					p.write(s);
					p.write('\n');
					if(stack) {
						new Throwable().printStackTrace(p);
						p.write('\n');
					}
				} finally {
					if(pw==null) p.close();
				}
			}
		} catch(IOException ex) {
			// Ignore
		}
	}
	
}

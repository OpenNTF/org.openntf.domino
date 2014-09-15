package org.openntf.dominoTests;

/*
 	Copyright 2013 Paul Withers Licensed under the Apache License, Version 2.0
	(the "License"); you may not use this file except in compliance with the
	License. You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
	or agreed to in writing, software distributed under the License is distributed
	on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
	express or implied. See the License for the specific language governing
	permissions and limitations under the License
	
*/

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;

import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.faces.context.FacesContext;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public class DataInitializerComparator implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private Object dataInitializerMethods;
	@SuppressWarnings("unused")
	private String lotusMethodText;
	@SuppressWarnings("unused")
	private String ourMethodText;

	public DataInitializerComparator() {

	}

	public Object getDataInitializerMethods() {
		ArrayList<String> retVal_ = new ArrayList<String>();
		try {
			Class<?> dataInit = Class.forName("extlib.DataInitializerOpenNTF");
			Method[] methods = dataInit.getDeclaredMethods();
			for (Method method : methods) {
				retVal_.add(method.getName());
			}
		} catch (Throwable e) {
			Utils.handleException(e);
		}
		return retVal_.toArray();
	}

	public String getLotusMethodText(String methodName) {
		String retVal_ = "";
		try {
			InputStream in = Utils.class.getResourceAsStream((String) ExtLibUtil.resolveVariable(FacesContext
					.getCurrentInstance(), "lotusClassName"));
			CompilationUnit cu;
			cu = JavaParser.parse(in);
			// visit and print the methods names
			ExtendedDumpVisitor dv = new ExtendedDumpVisitor();
			dv.setSearchMethodName(methodName);
			dv.visit(cu, null);
			retVal_ = dv.getSource();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return retVal_;
	}

	public String getOurMethodText(String methodName) {
		String retVal_ = "";
		try {
			InputStream in = Utils.class.getResourceAsStream((String) ExtLibUtil.resolveVariable(FacesContext
					.getCurrentInstance(), "ourClassName"));
			CompilationUnit cu;
			cu = JavaParser.parse(in);
			// visit and print the methods names
			ExtendedDumpVisitor dv = new ExtendedDumpVisitor();
			dv.setSearchMethodName(methodName);
			dv.visit(cu, null);
			retVal_ = dv.getSource();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return retVal_;
	}

}

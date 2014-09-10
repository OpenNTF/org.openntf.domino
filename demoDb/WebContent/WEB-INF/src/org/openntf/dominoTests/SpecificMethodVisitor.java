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
	
	Extended from examples in the JavaParser classes in the Google japa project
	http://code.google.com/p/javaparser/source/browse/trunk/JavaParser/src/japa/parser/ast/visitor/?r=93
	
*/

import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.CatchClause;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.stmt.TryStmt;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class SpecificMethodVisitor extends VoidVisitorAdapter<Object> {
	private class SourcePrinter {

		private int level = 0;

		private boolean indented = false;

		private final StringBuilder buf = new StringBuilder();

		public void indent() {
			level++;
		}

		public void unindent() {
			level--;
		}

		private void makeIndent() {
			for (int i = 0; i < level; i++) {
				buf.append("    ");
			}
		}

		public void print(String arg) {
			if (!indented) {
				makeIndent();
				indented = true;
			}
			buf.append(arg);
		}

		public void printLn(String arg) {
			print(arg);
			printLn();
		}

		public void printLn() {
			buf.append("<br/>");
			indented = false;
		}

		public String getSource() {
			return buf.toString();
		}

		@Override
		public String toString() {
			return getSource();
		}
	}

	private final SourcePrinter printer = new SourcePrinter();
	private String searchMethodName = "";

	public String getSource() {
		return printer.getSource();
	}

	public String getSearchMethodName() {
		return searchMethodName;
	}

	public void setSearchMethodName(String searchMethodName) {
		this.searchMethodName = searchMethodName;
	}

	@Override
	public void visit(MethodDeclaration n, Object arg) {
		if ("".equals(getSearchMethodName())) {
			if (n.getBody() == null) {
				printer.print(";");
			} else {
				BlockStmt bm = n.getBody();
				List<Statement> smList = bm.getStmts();
				for (Statement sm : smList) {
					printer.printLn(sm.toString());
				}
				n.getBody().accept(this, arg);
			}
		} else {
			if (n.getName().equals(getSearchMethodName())) {
				if (n.getBody() == null) {
					printer.print(";");
				} else {
					BlockStmt bm = n.getBody();
					List<Statement> smList = bm.getStmts();
					for (Statement sm : smList) {
						printer.printLn(sm.toString());
					}
					n.getBody().accept(this, arg);
				}
			}
		}
	}

	@Override
	public void visit(TryStmt n, Object arg) {
		printer.print("try ");
		n.getTryBlock().accept(this, arg);
		if (n.getCatchs() != null) {
			for (CatchClause c : n.getCatchs()) {
				c.accept(this, arg);
			}
		}
		if (n.getFinallyBlock() != null) {
			printer.print(" finally ");
			n.getFinallyBlock().accept(this, arg);
		}
	}

}

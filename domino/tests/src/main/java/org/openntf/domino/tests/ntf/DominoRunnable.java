/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.tests.ntf;

import java.util.Set;

import org.openntf.domino.Session;
import org.openntf.domino.helpers.Formula;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

@SuppressWarnings("unused")
public class DominoRunnable implements Runnable {
	public static void main(final String[] args) {
		DominoThread thread = new DominoThread(new DominoRunnable(), "My thread");
		thread.start();
	}

	public DominoRunnable() {
		// whatever you might want to do in your constructor, but stay away from Domino objects
	}

	@Override
	public void run() {
		Session session = Factory.getSession(SessionType.CURRENT);
		Formula formula = new Formula();
		String source = "REM {the quick \"brown\" fox jumped};\r\n" + "REM {over the \"lazy\" dog};\r\n"
				+ "DEFAULT defVar := @If(isThing2; \"thing2\"; thing);\r\n" + "ENVIRONMENT envVar := @Now;\r\n"
				+ "FIELD field1 := \"the cow jumped over the moon\";\r\n" + "FIELD field2 := \"dish... spoon... you know the score.\";\r\n"
				+ "FIELD field3 := @Adjust([08/08/2002]; 1; 2; 3; 4; 5; 6);\r\n"
				+ "tmpVar := field1 + \" rhyming \\\"time\\\" \" + field2[2];\r\n" + "tmpVar2 := 54938 + docField2;\r\n"
				+ "tmpVar + tmpVar2 + docField3 + @Name([CN]; @UserName) + 8;\r\n" + "@Command([AddBookmark]; \"foo\"; docField);\r\n" + "";
		formula.setExpression(source);
		org.openntf.domino.helpers.Formula.Parser parser = formula.getParser();
		if (parser != null) {
			parser.parse();
			Set<String> literals = parser.getLiterals();
			System.out.println("BEGIN LITERALS");
			for (String literal : literals) {
				System.out.print(literal + ", ");
			}
			System.out.println("END LITERALS");
			Set<String> functions = parser.getFunctions();
			System.out.println("BEGIN FUNCTIONS");
			for (String function : functions) {
				System.out.print(function + ", ");
			}
			System.out.println("END FUNCTIONS");
			Set<String> localVars = parser.getLocalVars();
			System.out.println("BEGIN LOCAL VARIABLES");
			for (String var : localVars) {
				System.out.print(var + ", ");
			}
			System.out.println("END LOCAL VARIABLES");
			Set<String> fieldVars = parser.getFieldVars();
			System.out.println("BEGIN FIELDS");
			for (String var : fieldVars) {
				System.out.print(var + ", ");
			}
			System.out.println("END FIELDS");
			Set<String> envVars = parser.getEnvVars();
			System.out.println("BEGIN ENVIRONMENTS");
			for (String var : envVars) {
				System.out.print(var + ", ");
			}
			System.out.println("END ENVIRONMENTS");
			Set<String> keywords = parser.getKeywords();
			System.out.println("BEGIN KEYWORDS");
			for (String var : keywords) {
				System.out.print(var + ", ");
			}
			System.out.println("END KEYWORDS");
			Set<String> numbers = parser.getNumberLiterals();
			System.out.println("BEGIN NUMBERS");
			for (String var : numbers) {
				System.out.print(var + ", ");
			}
			System.out.println("END NUMBERS");
		}
	}

}

/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
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
public class FormulaTester implements Runnable {
	public static void main(final String[] args) {
		DominoThread thread = new DominoThread(new FormulaTester(), "My thread");
		thread.start();
	}

	public FormulaTester() {
		// whatever you might want to do in your constructor, but stay away from Domino objects
	}

	@Override
	public void run() {
		Session session = Factory.getSession(SessionType.CURRENT);
		Formula formula = new Formula();
		String source = "REM {Begin_Do_Not_Tag};\r\n" + "\r\n" + "SenderName := @If(SendTo = \"\";EnterSendTo;SendTo);\r\n"
				+ "Send := @Subset(SenderName; 1);\r\n" + "CN1 := @Trim(@Name([CN]; Send));\r\n"
				+ "CN := @If(@Contains(@Right(Send;\"@\");\">\") & CN1 = \"\";@Trim(Send); CN1);\r\n"
				+ "G := @If(CN = \"\"; @Name([G]; @Subset(SenderName; 1)); \"\");\r\n"
				+ "S := @If(CN = \"\"; @Name([S]; @Subset(SenderName; 1)); \"\");\r\n"
				+ "Person := @If(CN != \"\"; CN; G + \" \" + S);\r\n"
				+ "@If(@Left(Person;1)=\"\\\"\" & @Right(Person;1)=\"\\\"\"; @LeftBack(@RightBack(Person;1);1); Person);\r\n"
				+ "REM {End_Do_Not_Tag};";
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
		} else {
			System.out.println("Parser was null?");
		}
	}

}

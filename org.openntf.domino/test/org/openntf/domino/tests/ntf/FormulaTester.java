package org.openntf.domino.tests.ntf;

import java.util.Set;

import lotus.domino.NotesFactory;

import org.openntf.domino.Session;
import org.openntf.domino.helpers.Formula;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

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
		Session session = this.getSession();
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

	protected Session getSession() {
		try {
			Session session = Factory.fromLotus(NotesFactory.createSession(), Session.class, null);
			return session;
		} catch (Throwable t) {
			DominoUtils.handleException(t);
			return null;
		}
	}
}

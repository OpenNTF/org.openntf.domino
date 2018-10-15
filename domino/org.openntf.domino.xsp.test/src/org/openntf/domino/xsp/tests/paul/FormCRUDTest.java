package org.openntf.domino.xsp.tests.paul;

import java.util.List;

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.design.DesignForm;
import org.openntf.domino.design.FormField;
import org.openntf.domino.design.FormField.Kind;
import org.openntf.domino.design.FormField.Type;
import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

public class FormCRUDTest implements Runnable {

	public FormCRUDTest() {

	}

	public static void main(final String[] args) {
		TestRunnerUtil.runAsDominoThread(new FormCRUDTest(), TestRunnerUtil.NATIVE_SESSION);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		try {
			Session sess = Factory.getSession(SessionType.NATIVE);
			Database db = sess.getDatabase("PrivateTest.nsf");
			DesignForm form = db.getDesign().createForm();
			form.setName("EclipseTest");
			FormField field = form.addField();
			field.setName("EclipseTest");
			field.setKind(Kind.COMPUTED);
			field.setDefaultValueFormula("@Today");
			field.setFieldType(Type.DATETIME);
			FormField field2 = form.addField();
			field2.setName("EclipseTest2");
			field2.setKind(Kind.EDITABLE);
			field2.setDefaultValueFormula("Hello");
			field2.setAllowMultiValues(true);
			form.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getFormInfo(final Database db, final StringBuilder sb) {
		DesignForm form = db.getDesign().getForm("testForm");
		sb.append("Outputting explicit subforms for testForm");
		addNewLine(sb);
		List<String> subforms = form.getExplicitSubforms();
		for (String subformName : subforms) {
			sb.append(subformName);
			addNewLine(sb);
		}
		sb.append("Outputting computed subforms for testForm");
		addNewLine(sb);
		subforms = form.getComputedSubforms();
		for (String subformName : subforms) {
			sb.append(subformName);
			addNewLine(sb);
		}
	}

	private StringBuilder addNewLine(final StringBuilder sb) {
		sb.append("\r\n");
		return sb;
	}

}

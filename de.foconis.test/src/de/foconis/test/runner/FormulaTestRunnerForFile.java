package de.foconis.test.runner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.openntf.domino.formula.impl.TextFunctions;
import org.openntf.domino.utils.Strings;

public class FormulaTestRunnerForFile extends ParentRunner<Runner> {
	private final List<Runner> fRunners;
	private final String fileName;

	protected FormulaTestRunnerForFile(final Class<?> testClass, final File file) throws Exception {
		super(testClass);
		fRunners = Collections.unmodifiableList(createRunners(file));
		fileName = file.getAbsolutePath();
	}

	@Override
	protected Description describeChild(final Runner child) {
		// TODO Auto-generated method stub
		return child.getDescription();

	}

	@Override
	protected List<Runner> getChildren() {
		// TODO Auto-generated method stub
		return fRunners;
	}

	@Override
	protected void runChild(final Runner child, final RunNotifier notifier) {
		child.run(notifier);

	}

	static int idx = 0; // must generate unique test number!

	private List<Runner> createRunners(final File file) throws Exception {
		try {

			List<Runner> children = new ArrayList<Runner>();

			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			String lcline;
			StringBuilder sb = null;
			String currentFormula = "";
			int lineNr = 0;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				lcline = line.toLowerCase();
				lineNr++;
				if (Strings.isBlankString(line)) {

				} else if (line.startsWith("#")) {
					if (sb != null) {
						currentFormula = sb.toString();
						sb = null;
					}

					String cmd = TextFunctions.atLeft(line + " ", " ");
					String expect = TextFunctions.atRight(line, " ");
					String ifo = "line " + lineNr + ": " + cmd
							+ " "   //
							+ currentFormula.replace('(', '[').replace(')', ']').replace('\r', ' ').replace('\n', ' ')
							+ (Strings.isBlankString(expect) ? "" : " = " + expect);

					TestParameter param = new TestParameter(ifo, currentFormula);

					param.expect = expect;

					if (lcline.startsWith("#allfail")) {
						param.lotus = TestMode.FAIL;
						param.doc = TestMode.FAIL;
						param.map = TestMode.FAIL;

					} else if (lcline.startsWith("#allpass")) {
						param.lotus = TestMode.PASS;
						param.doc = TestMode.PASS;
						param.map = TestMode.PASS;

					} else if (lcline.startsWith("#lotuspass")) {
						param.lotus = TestMode.PASS;

					} else if (lcline.startsWith("#docpass")) {
						param.doc = TestMode.PASS;

					} else if (lcline.startsWith("#lotusdocpass")) {
						param.lotus = TestMode.PASS;
						param.doc = TestMode.PASS;

					} else if (lcline.startsWith("#docmappass")) {
						param.map = TestMode.PASS;
						param.doc = TestMode.PASS;

					} else if (lcline.startsWith("#mappass")) {
						param.map = TestMode.PASS;

					} else if (lcline.startsWith("#lotusfail")) {
						param.lotus = TestMode.FAIL;

					} else if (lcline.startsWith("#docfail")) {
						param.doc = TestMode.FAIL;

					} else if (lcline.startsWith("#mapfail")) {
						param.map = TestMode.FAIL;
					} else if (lcline.startsWith("#rem")) {
						continue;
					} else if (lcline.startsWith("#todo")) {
						System.err.println("#TOOD");
						System.err.println(currentFormula);
						System.err.println(line);
						continue;
					} else {
						throw new IllegalStateException("cannot interpret '" + line + "'");
					}

					children.add(createRunner(ifo, param, idx++));

				} else {
					if (sb == null) {
						sb = new StringBuilder();
					} else {
						sb.append("\r\n");
					}
					sb.append(line);
				}
			}

			//			for (Object parametersOfSingleTest : allParameters) {
			//children.add(createRunnerWithNotNormalizedParameters("Test", 1, new Object[] { "a", "b" }));
			//			}
			return children;
		} catch (ClassCastException e) {
			return null;
			//throw parametersMethodReturnedWrongType();
		}
	}

	protected Runner createRunner(final String pattern, final TestParameter parameter, final int idx) throws InitializationError {
		return new FormulaTestRunnerForParameters(getTestClass().getJavaClass(), pattern, parameter, idx);
	}

	public String getFileName() {
		return fileName;
	}

	/* (non-Javadoc)
	 * @see org.junit.runners.ParentRunner#getDescription()
	 */
	@Override
	public Description getDescription() {
		Description description = Description.createSuiteDescription("FILE: " + fileName, getRunnerAnnotations());
		for (Runner child : getChildren()) {
			description.addChild(describeChild(child));
		}
		return description;
	}

}

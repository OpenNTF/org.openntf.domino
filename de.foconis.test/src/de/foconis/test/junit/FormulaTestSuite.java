package de.foconis.test.junit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.openntf.domino.formula.impl.TextFunctions;
import org.openntf.domino.utils.Strings;

public class FormulaTestSuite extends Suite {

	protected class TestClassRunnerForParameters extends BlockJUnit4ClassRunner {
		private final TestParameter fParameter;

		private final String fName;

		protected TestClassRunnerForParameters(final Class<?> type, final String pattern, final TestParameter parameter)
				throws InitializationError {
			super(type);

			fParameter = parameter;
			fName = parameter.ifo;
		}

		@Override
		public Object createTest() throws Exception {
			return createTestUsingConstructorInjection();
		}

		private Object createTestUsingConstructorInjection() throws Exception {
			return getTestClass().getOnlyConstructor().newInstance(fParameter);
		}

		@Override
		protected String getName() {
			return fName;
		}

		@Override
		protected String testName(final FrameworkMethod method) {
			return method.getName() + " " + fParameter.formula
					+ (Strings.isBlankString(fParameter.expect) ? "" : " = " + fParameter.expect);
		}

		@Override
		protected void validateConstructor(final List<Throwable> errors) {
			validateOnlyOneConstructor(errors);
		}

		@Override
		protected Statement classBlock(final RunNotifier notifier) {
			return childrenInvoker(notifier);
		}

		@Override
		protected Annotation[] getRunnerAnnotations() {
			return new Annotation[0];
		}

		/**
		 * Returns the methods that run tests. Default implementation returns all methods annotated with {@code @Test} on this class and
		 * superclasses that are not overridden.
		 */
		@Override
		protected List<FrameworkMethod> getChildren() {
			List<FrameworkMethod> ret = new ArrayList<FrameworkMethod>();
			List<FrameworkMethod> testMethods = computeTestMethods();

			for (FrameworkMethod method : testMethods) {
				if (fParameter.lotus == TestMode.PASS && method.getName().equals("testLotus")) {
					ret.add(method);
				}
				if (fParameter.doc == TestMode.PASS && method.getName().equals("testDoc")) {
					ret.add(method);
				}
				if (fParameter.map == TestMode.PASS && method.getName().equals("testMap")) {
					ret.add(method);
				}
				if (fParameter.lotus == TestMode.FAIL && method.getName().equals("testLotusFail")) {
					ret.add(method);
				}
				if (fParameter.doc == TestMode.FAIL && method.getName().equals("testDocFail")) {
					ret.add(method);
				}
				if (fParameter.map == TestMode.FAIL && method.getName().equals("testMapFail")) {
					ret.add(method);
				}

			}

			for (FrameworkMethod method : testMethods) {
				if (fParameter.lotus == TestMode.PASS && fParameter.doc == TestMode.PASS && method.getName().equals("compareLotusDoc")) {
					ret.add(method);
				}
				if (fParameter.lotus == TestMode.PASS && fParameter.doc == TestMode.PASS && method.getName().equals("compareLotusMap")) {
					ret.add(method);
				}
				if (fParameter.doc == TestMode.PASS && fParameter.map == TestMode.PASS && method.getName().equals("compareDocMap")) {
					ret.add(method);
				}

			}
			return ret;

		}
	}

	private static final List<Runner> NO_RUNNERS = Collections.<Runner> emptyList();

	private final List<Runner> fRunners;

	/**
	 * Only called reflectively. Do not use programmatically.
	 */
	public FormulaTestSuite(final Class<?> klass) throws Throwable {
		super(klass, NO_RUNNERS);
		//		Parameters parameters = getParametersMethod().getAnnotation(Parameters.class);
		fRunners = Collections.unmodifiableList(createRunners());
	}

	@Override
	protected List<Runner> getChildren() {
		return fRunners;
	}

	private static FilenameFilter filefilter = new FilenameFilter() {
		public boolean accept(final File dir, final String name) {
			return name.endsWith(".txt") || dir.isDirectory();
		}
	};

	private static void displayDirectoryContents(final File dir, final List<File> fileList) {
		File[] files = dir.listFiles(filefilter);
		for (File file : files) {
			if (file.isDirectory()) {
				displayDirectoryContents(file, fileList);
			} else {
				fileList.add(file);
			}
		}

	}

	protected Runner createRunner(final String pattern, final TestParameter parameter) throws InitializationError {
		return new TestClassRunnerForParameters(getTestClass().getJavaClass(), pattern, parameter);
	}

	//	@SuppressWarnings("unchecked")
	//	private Iterable<Object> allParameters() throws Throwable {
	//		Object parameters = getParametersMethod().invokeExplosively(null);
	//		if (parameters instanceof Iterable) {
	//			return (Iterable<Object>) parameters;
	//		} else if (parameters instanceof Object[]) {
	//			return Arrays.asList((Object[]) parameters);
	//		} else {
	//			throw parametersMethodReturnedWrongType();
	//		}
	//	}

	//	private FrameworkMethod getParametersMethod() throws Exception {
	//		List<FrameworkMethod> methods = getTestClass().getAnnotatedMethods(Parameters.class);
	//		for (FrameworkMethod each : methods) {
	//			if (each.isStatic() && each.isPublic()) {
	//				return each;
	//			}
	//		}
	//
	//		throw new Exception("No public static parameters method on class " + getTestClass().getName());
	//	}

	private List<Runner> createRunners() throws Exception {
		try {
			int i = 0;
			List<Runner> children = new ArrayList<Runner>();

			String sourcePath = "W:\\Daten\\Entwicklung\\UnitTest\\FormulaEngine\\";
			File currentDir = new File(sourcePath); // current directory

			List<File> files = new ArrayList<File>();
			displayDirectoryContents(currentDir, files);

			List<Object[]> params = new ArrayList<Object[]>();

			for (File file : files) {
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
						String ifo = file.getAbsolutePath().replace(sourcePath, "") + " [" + lineNr + "] "
								+ TextFunctions.atLeft(currentFormula + "(", "(");
						TestParameter param = new TestParameter(ifo, currentFormula);

						param.expect = TextFunctions.atRight(line, " ");

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

						} else if (lcline.startsWith("#mappass")) {
							param.map = TestMode.PASS;

						} else if (lcline.startsWith("#lotusfail")) {
							param.lotus = TestMode.FAIL;

						} else if (lcline.startsWith("#docfail")) {
							param.doc = TestMode.FAIL;

						} else if (lcline.startsWith("#mapfail")) {
							param.map = TestMode.FAIL;
						} else {
							throw new IllegalStateException("cannot interpret '" + line + "'");
						}

						children.add(createRunner(ifo, param));

					} else {
						if (sb == null) {
							sb = new StringBuilder();
						} else {
							sb.append("\r\n");
						}
						sb.append(line);
					}
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
	//	private Exception parametersMethodReturnedWrongType() throws Exception {
	//		String className = getTestClass().getName();
	//		String methodName = getParametersMethod().getName();
	//		String message = MessageFormat.format("{0}.{1}() must return an Iterable of arrays.", className, methodName);
	//		return new Exception(message);
	//	}

	//	private List<FrameworkField> getAnnotatedFieldsByParameter() {
	//		return getTestClass().getAnnotatedFields(Parameter.class);
	//	}

	//	private boolean fieldsAreAnnotated() {
	//		return !getAnnotatedFieldsByParameter().isEmpty();
	//	}
}
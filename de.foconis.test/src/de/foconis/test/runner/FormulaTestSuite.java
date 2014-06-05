package de.foconis.test.runner;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.runner.Runner;
import org.junit.runners.Suite;
import org.junit.runners.model.FrameworkMethod;

/**
 * Testrunner for formula tests
 * 
 * @author Roland Praml, Foconis AG
 * 
 */
public class FormulaTestSuite extends Suite {

	private static final List<Runner> NO_RUNNERS = Collections.<Runner> emptyList();

	private final List<Runner> fRunners;

	/**
	 * Only called reflectively. Do not use programmatically.
	 */
	public FormulaTestSuite(final Class<?> klass) throws Throwable {
		super(klass, NO_RUNNERS);
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

	private FrameworkMethod getFormulaFilesMethod() throws Exception {
		List<FrameworkMethod> methods = getTestClass().getAnnotatedMethods(FormulaFile.class);
		for (FrameworkMethod each : methods) {
			if (each.isStatic() && each.isPublic()) {
				return each;
			}
		}

		throw new Exception("No public static FormulaFile method on class " + getTestClass().getName());
	}

	private List<Runner> createRunners() throws Throwable {
		try {
			int i = 0;
			List<Runner> children = new ArrayList<Runner>();

			String sourcePath = (String) getFormulaFilesMethod().invokeExplosively(null);

			File currentDir = new File(sourcePath); // current directory

			List<File> files = new ArrayList<File>();
			if (currentDir.isDirectory()) {
				displayDirectoryContents(currentDir, files);
			} else {
				files.add(currentDir);
			}

			List<Object[]> params = new ArrayList<Object[]>();

			for (File file : files) {
				children.add(new FormulaTestRunnerForFile(getTestClass().getJavaClass(), file));
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
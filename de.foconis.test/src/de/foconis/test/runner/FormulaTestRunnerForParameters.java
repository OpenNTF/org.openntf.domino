package de.foconis.test.runner;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

class FormulaTestRunnerForParameters extends BlockJUnit4ClassRunner {
	private final TestParameter fParameter;

	private final String fName;

	private int idx;

	protected FormulaTestRunnerForParameters(final Class<?> type, final String pattern, final TestParameter parameter, final int idx)
			throws InitializationError {
		super(type);

		fParameter = parameter;
		fName = parameter.ifo;
		this.idx = idx;
	}

	@Override
	public Object createTest() throws Exception {
		return getTestClass().getOnlyConstructor().newInstance(fParameter);
	}

	/**
	 * This is the text filename of the test
	 */
	@Override
	protected String getName() {
		return fName;
	}

	@Override
	protected String testName(final FrameworkMethod method) {
		return method.getName() + " [" + idx + "]";
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

	/* (non-Javadoc)
	 * @see org.junit.runners.ParentRunner#run(org.junit.runner.notification.RunNotifier)
	 */
	@Override
	public void run(final RunNotifier notifier) {
		System.out.println("======" + getName());
		super.run(notifier);

	}

}
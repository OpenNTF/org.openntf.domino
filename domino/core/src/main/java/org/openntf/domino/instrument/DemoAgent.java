package org.openntf.domino.instrument;

import java.lang.instrument.Instrumentation;

public class DemoAgent {
	public static void agentmain(final String agentArgs, final Instrumentation inst) {
		System.out.println("Executing agentmain.........");
		inst.addTransformer(new DemoTransformer());
	}

	public static void attach() {
		try {
			AgentBuilder ab = new AgentBuilder(DemoAgent.class);
			ab.addClass(DemoTransformer.class);
			ab.addClass(DemoClassVisitor.class);
			ab.addClass(DemoMethodVisitor.class);
			ab.addJar("lib-instrument/asm-5.0.3.jar");
			ab.attach();
		} catch (Exception e) {
			System.out.println("attach failed");
			e.printStackTrace();
		}
	}
}
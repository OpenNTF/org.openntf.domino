package org.openntf.domino.instrument;

import java.lang.instrument.Instrumentation;

public class ProfilerAgent {
	public static void agentmain(final String agentArgs, final Instrumentation inst) {
		inst.addTransformer(new ProfilerTransformer());
	}

	public static void attach() {
		try {
			AgentBuilder ab = new AgentBuilder(ProfilerAgent.class);
			ab.addClass(ProfilerTransformer.class);
			ab.addClass(ProfilerClassVisitor.class);
			ab.addClass(ProfilerMethodVisitor.class);
			ab.addJar("lib-instrument/asm-5.0.3.jar");
			ab.attach();
		} catch (Exception e) {
			System.out.println("attach failed");
			e.printStackTrace();
		}
	}
}
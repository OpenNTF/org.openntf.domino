package org.openntf.domino.instrument;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ProfilerMethodVisitor extends MethodVisitor implements Opcodes {
	private String className_;
	private String methodName_;
	private String signature_;

	protected ProfilerMethodVisitor(final int version, final MethodVisitor delegate, final String className, final String methodName,
			final String signature) {
		super(version, delegate);
		className_ = className;
		methodName_ = methodName;
		signature_ = signature;
	}

	@Override
	public void visitCode() {
		// on top of each method, we notify the profiler, that we will enter a new method
		super.visitMethodInsn(INVOKESTATIC, "org/openntf/domino/instrument/Profiler", "enter", "()V", false);
		super.visitCode();
	}

	@Override
	public void visitInsn(final int opCode) {
		// On every return statement, we will invoke Profiler.leave
		// TODO: There is still some work to do for exception handling!
		switch (opCode) {
		case IRETURN:
		case LRETURN:
		case FRETURN:
		case DRETURN:
		case ARETURN:
		case RETURN:
			super.visitIntInsn(LDC, 1); // 1 is the current class
			super.visitLdcInsn(methodName_);
			super.visitLdcInsn(signature_);
			super.visitMethodInsn(INVOKESTATIC, "org/openntf/domino/instrument/Profiler", "leave",
					"(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/String;)V", false);
		}
		super.visitInsn(opCode);
	}

	@Override
	public void visitMaxs(int maxStack, final int maxLocales) {
		//if (maxStack < 6)
		maxStack += 3; // the stack must be at least 6 more
		super.visitMaxs(maxStack, maxLocales);
	}

}

package org.openntf.domino.instrument;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class DemoMethodVisitor extends MethodVisitor {
	private String name_;

	protected DemoMethodVisitor(final int arg0, final String name, final MethodVisitor delegate) {
		super(arg0, delegate);
		name_ = name;
	}

	@Override
	public void visitCode() {
		// TODO Auto-generated method stub
		super.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
		super.visitLdcInsn("method: " + name_);
		super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
		super.visitCode();
	}

	@Override
	public void visitMaxs(int maxStack, final int maxLocales) {
		// TODO Auto-generated method stub
		if (maxStack < 2)
			maxStack = 2; // the stack must be at least 2 for the sysout command
		super.visitMaxs(maxStack, maxLocales);
	}

}

package org.openntf.domino.instrument;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class DemoClassVisitor extends ClassVisitor {

	private String className_;

	public DemoClassVisitor(final ClassWriter writer) {
		super(Opcodes.ASM4, writer);
	}

	@Override
	public void visit(final int version, final int access, final String name, final String signature, final String superName,
			final String[] interfaces) {
		System.out.println(name + " extends " + superName + " {");
		className_ = name;
		super.visit(version, access, name, signature, superName, interfaces);
	}

	@Override
	public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature,
			final String[] exceptions) {
		MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
		//if (!name.startsWith("get")) {
		System.out.println(" " + name + desc);
		return new DemoMethodVisitor(api, className_ + "." + name, mv);
		//}
		//return mv;

	}

	@Override
	public void visitEnd() {
		System.out.println("}");
		super.visitEnd();
	}
}
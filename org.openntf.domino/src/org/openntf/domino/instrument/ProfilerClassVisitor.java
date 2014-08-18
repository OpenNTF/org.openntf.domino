package org.openntf.domino.instrument;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ProfilerClassVisitor extends ClassVisitor {

	private String className_;

	public ProfilerClassVisitor(final ClassWriter writer) {
		super(Opcodes.ASM4, writer);
	}

	@Override
	public void visit(final int version, final int access, final String name, final String signature, final String superName,
			final String[] interfaces) {
		className_ = name;
		super.visit(version, access, name, signature, superName, interfaces);
	}

	@Override
	public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature,
			final String[] exceptions) {

		MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
		if (name.startsWith("_"))
			return mv;
		System.out.println(className_ + "." + name + signature);
		return new ProfilerMethodVisitor(api, mv, className_, name, signature == null ? "" : signature);
	}

}
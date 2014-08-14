package org.openntf.domino.instrument;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

public class ProfilerTransformer implements ClassFileTransformer {
	public static final String DEBUG_DIR = null; //"d:/daten/dump/";

	@Override
	public byte[] transform(final ClassLoader loader, final String className, final Class classBeingRedefined,
			final ProtectionDomain protectionDomain, final byte[] classfileBuffer) throws IllegalClassFormatException {
		byte[] byteCode = classfileBuffer;
		if (className.startsWith("org/openntf/domino/impl")) {
			System.out.println("Injecting code in class " + className + " for profiling");
			try {
				// dump the result for further analysis
				if (DEBUG_DIR != null) {
					FileOutputStream fos = new FileOutputStream(new File(DEBUG_DIR + className.replace('/', '.') + "-old.class"));
					fos.write(byteCode, 0, byteCode.length);
					fos.close();
				}

				ClassReader reader = new ClassReader(byteCode);
				ClassWriter writer = new ClassWriter(reader, 0);
				// The visitor will traverse the parsed bytecode
				ClassVisitor visitor = new ProfilerClassVisitor(writer);
				reader.accept(visitor, 0);
				byteCode = writer.toByteArray();

				if (DEBUG_DIR != null) {
					FileOutputStream fos = new FileOutputStream(new File("d:/daten/dump/" + className.replace('/', '.') + "-new.class"));
					fos.write(byteCode, 0, byteCode.length);
					fos.close();
				}
			} catch (Throwable ex) {
				System.out.println("Exception: " + ex);
				ex.printStackTrace();
			}
		}

		return byteCode;
	}
}
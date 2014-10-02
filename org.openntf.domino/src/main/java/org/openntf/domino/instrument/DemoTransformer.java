package org.openntf.domino.instrument;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

public class DemoTransformer implements ClassFileTransformer {
	@Override
	public byte[] transform(final ClassLoader loader, final String className, final Class<?> classBeingRedefined,
			final ProtectionDomain protectionDomain, final byte[] classfileBuffer) throws IllegalClassFormatException {
		byte[] byteCode = classfileBuffer;
		if (className.startsWith("org/openntf/domino/impl")) {
			System.out.println("Instrumenting......" + className + " " + classBeingRedefined);
			try {
				// dump the result for further analysis
				//				FileOutputStream fos = new FileOutputStream(new File("d:/daten/dump/" + className.replace('/', '.') + "-old.class"));
				//				fos.write(byteCode, 0, byteCode.length);
				//				fos.close();
				ClassReader reader = new ClassReader(byteCode);
				ClassWriter writer = new ClassWriter(reader, 0);
				ClassVisitor visitor = new DemoClassVisitor(writer);
				reader.accept(visitor, 0);
				byteCode = writer.toByteArray();
				// dump the result for further analysis
				//				fos = new FileOutputStream(new File("d:/daten/dump/" + className.replace('/', '.') + "-new.class"));
				//				fos.write(byteCode, 0, byteCode.length);
				//				fos.close();
			} catch (Throwable ex) {
				System.out.println("Exception: " + ex);
				ex.printStackTrace();
			}
		}

		// since this transformer will be called when all the classes are
		// loaded by the classloader, we are restricting the instrumentation
		// using if block only for the Lion class
		//		if (className.equals("com/javapapers/java/instrumentation/Lion")) {
		//			System.out.println("Instrumenting......");
		//			try {
		//				ClassPool classPool = ClassPool.getDefault();
		//				CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));
		//				CtMethod[] methods = ctClass.getDeclaredMethods();
		//				for (CtMethod method : methods) {
		//					method.addLocalVariable("startTime", CtClass.longType);
		//					method.insertBefore("startTime = System.nanoTime();");
		//					method.insertAfter("System.out.println(\"Execution Duration " + "(nano sec): \"+ (System.nanoTime() - startTime) );");
		//				}
		//				byteCode = ctClass.toBytecode();
		//				ctClass.detach();
		//				System.out.println("Instrumentation complete.");
		//			} catch (Throwable ex) {
		//				System.out.println("Exception: " + ex);
		//				ex.printStackTrace();
		//			}
		//		}
		return byteCode;
	}
}
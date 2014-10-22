package org.openntf.domino.instrument;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import com.ibm.tools.attach.AgentInitializationException;
import com.ibm.tools.attach.AgentLoadException;
import com.ibm.tools.attach.AttachNotSupportedException;
import com.ibm.tools.attach.VirtualMachine;
import com.ibm.tools.attach.VirtualMachineDescriptor;

/**
 * A utility class for loading Java agents.
 * 
 * Some useful web resources for Instrumentation:
 * 
 * http://javapapers.com/core-java/java-instrumentation/
 * 
 * https://github.com/Xyene/ASM-Late-Bind-Agent/
 * 
 * http://www.tomsquest.com/blog/2014/01/intro-java-agent-and-bytecode-manipulation/
 * 
 * HINT: Don't try to use Javassist. It does not work with the OSGI-Classloader system (I think!)
 * 
 */
public class AgentBuilder {

	private Class<?> agent_;
	private List<Class<?>> clazzes_ = new ArrayList<Class<?>>();
	private List<String> jars_ = new ArrayList<String>();
	private List<String> resources_ = new ArrayList<String>();

	public AgentBuilder(final Class<?> agent) {
		agent_ = agent;
	}

	public void addClass(final Class<?> clazz) {
		clazzes_.add(clazz);
	}

	public void addJar(final String file) {
		jars_.add(file);
	}

	public void addResource(final String file) {
		resources_.add(file);
	}

	public void attach() throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException {

		File jar = generateAgentJar();
		System.out.println("Jar is " + jar.getAbsolutePath());
		List<VirtualMachineDescriptor> vmds = VirtualMachine.list();
		for (VirtualMachineDescriptor vmd : vmds) {
			System.out.println("VM " + vmd);
			VirtualMachine vm = VirtualMachine.attach(vmd);
			vm.loadAgent(jar.getAbsolutePath());
			vm.detach();
		}
	}

	/**
	 * Generates a temporary agent file to be loaded.
	 * 
	 * @param agent
	 *            The main agent class.
	 * @param resources
	 *            Array of classes to be included with agent.
	 * @return Returns a temporary jar file with the specified classes included.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private File generateAgentJar() throws IOException {
		File jarFile = File.createTempFile("agent", ".jar");
		jarFile.deleteOnExit();
		Manifest manifest = new Manifest();
		Attributes mainAttributes = manifest.getMainAttributes();

		// Create manifest stating that agent is allowed to transform classes
		mainAttributes.put(Name.MANIFEST_VERSION, "1.0");
		mainAttributes.put(new Name("Agent-Class"), agent_.getName());
		mainAttributes.put(new Name("Can-Retransform-Classes"), "true");
		mainAttributes.put(new Name("Can-Redefine-Classes"), "true");
		//		if (jars_.size() > 0) {
		//			StringBuilder sb = new StringBuilder();
		//			sb.append('.');
		//			for (int i = 0; i < jars_.size(); i++) {
		//				sb.append(", jar/");
		//				sb.append(i);
		//			}
		//			mainAttributes.put(new Name("Class-Path"), sb.toString());
		//			mainAttributes.put(new Name("Bundle-Class-Path"), sb.toString());
		//		}

		JarOutputStream jos = new JarOutputStream(new FileOutputStream(jarFile), manifest);
		appendClass(jos, agent_);
		for (Class<?> clazz : clazzes_) {
			appendClass(jos, clazz);
		}
		// Copy all jar content in the subdir
		for (int i = 0; i < jars_.size(); i++) {
			JarInputStream jis = new JarInputStream(agent_.getClassLoader().getResourceAsStream(jars_.get(i)));
			JarEntry jEnt;
			while ((jEnt = jis.getNextJarEntry()) != null) {
				if (jEnt.getName().endsWith(".class")) {
					jos.putNextEntry(new JarEntry(jEnt.getName()));

					byte[] buf = new byte[8192];
					int count;
					while ((count = jis.read(buf, 0, buf.length)) > 0) {
						jos.write(buf, 0, count);
					}
					jos.closeEntry();
				}
			}
			jis.close();
		}

		for (String res : resources_) {
			appendFile(jos, agent_.getClassLoader(), res);
		}
		jos.close();

		return jarFile;
	}

	/**
	 * Appends a Class to the jar output stream.
	 * 
	 * @param jos
	 * @param clazz
	 * @throws IOException
	 */
	private void appendClass(final JarOutputStream jos, final Class<?> clazz) throws IOException {
		String clazzFile = clazz.getName().replace('.', '/') + ".class";
		appendFile(jos, clazz.getClassLoader(), clazzFile);
	}

	private void appendFile(final JarOutputStream jos, final ClassLoader cl, final String fileName) throws IOException {
		InputStream is = cl.getResourceAsStream(fileName);
		if (is != null) {
			byte[] buf = new byte[8192];
			jos.putNextEntry(new JarEntry(fileName));
			int count;
			while ((count = is.read(buf, 0, buf.length)) > 0) {
				jos.write(buf, 0, count);
			}
			jos.closeEntry();
		} else {
			throw new IOException("Could not load data from '" + fileName + "' ");
		}
	}
}
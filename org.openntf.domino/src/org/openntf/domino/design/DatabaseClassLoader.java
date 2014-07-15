package org.openntf.domino.design;

import java.lang.annotation.Annotation;
import java.util.Set;

public abstract class DatabaseClassLoader extends ClassLoader {
	public DatabaseClassLoader(final ClassLoader parent) {
		super(parent);
	}

	public abstract Set<Class<?>> getClassesWithAnnotation(Class<? extends Annotation> annotationClass);

	public abstract Set<Class<?>> getClassesExtending(final Class<?> superClass);

	public abstract DatabaseDesign getParentDesign();

}

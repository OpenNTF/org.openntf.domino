package com.tinkerpop.frames;

/**
 * Thrown if a method could not be handled because an appropriate
 * {@link AnnotationHandler} or {@link MethodHandler} could not be found that
 * responds to the method
 * 
 * @author Bryn Cooke
 * 
 */
public class UnhandledMethodException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UnhandledMethodException(String message) {
		super(message);
	}

}

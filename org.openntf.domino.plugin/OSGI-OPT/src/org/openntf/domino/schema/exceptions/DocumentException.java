/**
 * 
 */
package org.openntf.domino.schema.exceptions;
import java.util.logging.Logger;
/**
 * @author nfreeman
 *
 */
public class DocumentException extends SchemaException {
	private static final Logger log_ = Logger.getLogger(DocumentException.class.getName());
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public DocumentException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public DocumentException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public DocumentException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
}

package de.ludwig.smt;

/**
 * Some serious exception or problem occured.
 * 
 * @author daniel
 *
 */
public class SmartTrashException extends RuntimeException {

	/**
	 * The serial version uid.
	 */
	private static final long serialVersionUID = 4474023066894484130L;

	public SmartTrashException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public SmartTrashException(String arg0) {
		super(arg0);
	}

	public SmartTrashException(Throwable arg0) {
		super(arg0);
	}

}

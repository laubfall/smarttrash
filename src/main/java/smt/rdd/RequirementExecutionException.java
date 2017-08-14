package smt.rdd;

/**
 * Exception occured while executing a requirement.
 * @author daniel
 *
 */
public class RequirementExecutionException extends RuntimeException {

	/**
	 * The serial version uid.
	 */
	private static final long serialVersionUID = 4517030097207786155L;

	public RequirementExecutionException(String message) {
		super(message);
	}

	public RequirementExecutionException(Throwable cause) {
		super(cause);
	}

	public RequirementExecutionException(String message, Throwable cause) {
		super(message, cause);
	}
}

package collector.exception;

public class ConstraintViolationException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7762979513470440241L;
	
	public ConstraintViolationException(String message) {
		super(message);
	}

}

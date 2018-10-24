package collector.exception;

public class DataNotFoundException extends RuntimeException{

	private static final long serialVersionUID = -4123701272868427860L;

	public DataNotFoundException(String message) {
		super(message);
	}
}
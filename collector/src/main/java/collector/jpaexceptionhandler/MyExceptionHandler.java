package collector.jpaexceptionhandler;

import org.eclipse.persistence.exceptions.ExceptionHandler;

public class MyExceptionHandler implements ExceptionHandler {

	@Override
	public Object handleException(RuntimeException exception) {
		throw new RuntimeException(exception.getMessage());
	}

}

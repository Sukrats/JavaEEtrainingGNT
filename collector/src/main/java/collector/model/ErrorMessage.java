package collector.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ErrorMessage {
	private String message;
	private Integer code;

	public ErrorMessage() {
	}

	public ErrorMessage(String message, Integer statusCode) {
		this.message = message;
		this.code = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

}

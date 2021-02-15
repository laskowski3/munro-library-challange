package com.challange.munrolibrary.common;

public class ParamException extends RuntimeException {
	private static final long serialVersionUID = -3133955402676449651L;
	private String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public ParamException() {

	}

	public ParamException(final String message) {
		super(message);
	}

	public ParamException(final String message, final Throwable cause) {
		super(message, cause);
	}
}

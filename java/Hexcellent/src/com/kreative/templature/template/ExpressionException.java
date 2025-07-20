package com.kreative.templature.template;

public class ExpressionException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public ExpressionException(String expected, String actual) {
		super("Expected " + expected + " but found " + actual + ".");
	}
}

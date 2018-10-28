package com.simon.wa.exceptions;

import org.slf4j.Logger;

public class RestObjectNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 5399556031777052204L;

	public RestObjectNotFoundException(String message) {
		super();
	}
	
	public RestObjectNotFoundException(Logger log,String type, String field, String value) {
		super("Couldn't find "+ type + " with " + field + " equal to " + value);
		log.warn("Couldn't find "+ type + " with " + field + " equal to " + value);
	}


}

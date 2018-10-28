package com.simon.wa.exceptions;

import org.slf4j.Logger;

public class RestObjectAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 6892226370776250629L;
	
	public RestObjectAlreadyExistsException(Logger log,String typeName, String name) {
		super("A "+ typeName + " with name " + name + " already exists");
		log.warn("A "+ typeName + " with name " + name + " already exists");
	}

}

package com.simon.wa.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.simon.wa.exceptions.RestObjectAlreadyExistsException;
import com.simon.wa.exceptions.RestObjectNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.hateoas.VndErrors;

@ControllerAdvice
@RequestMapping(produces = "application/vnd.error")
public class RestObjectControllerAdvice {

	@ResponseBody
    @ExceptionHandler(RestObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    VndErrors bookNotFoundExceptionHandler(RestObjectNotFoundException ex) {
        return new VndErrors("error", ex.getMessage());
    }
	
	 @ResponseBody
	    @ExceptionHandler(RestObjectAlreadyExistsException.class)
	    @ResponseStatus(HttpStatus.CONFLICT)
	    VndErrors bookIsbnAlreadyExistsExceptionHandler(RestObjectAlreadyExistsException ex) {
	        return new VndErrors("error", ex.getMessage());
	    }
	
}
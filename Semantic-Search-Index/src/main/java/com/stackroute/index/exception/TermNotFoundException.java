package com.stackroute.index.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TermNotFoundException extends Exception{

	private static final long serialVersionUID = 1L;
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	public TermNotFoundException(String message) {
        super(message);
        log.info("Term not found in the intent graph");
    }

}

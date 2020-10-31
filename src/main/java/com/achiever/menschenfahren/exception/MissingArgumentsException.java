package com.achiever.menschenfahren.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Missing arguments in query")
public class MissingArgumentsException extends Exception {

	private static final long serialVersionUID = 1L;

	public MissingArgumentsException(final String reason) {
		super(reason);
	}

}

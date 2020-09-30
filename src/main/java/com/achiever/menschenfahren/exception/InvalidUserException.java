package com.achiever.menschenfahren.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User data invalid")
public class InvalidUserException extends Exception {

	private static final long serialVersionUID = -503341549235515462L;

	public InvalidUserException(final String reason) {
		super(reason);
	}

}

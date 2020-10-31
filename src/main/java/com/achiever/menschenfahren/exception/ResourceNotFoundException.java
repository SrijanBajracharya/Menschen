package com.achiever.menschenfahren.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Resource not found in datas")
public class ResourceNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(final String message) {
		super(message);
	}

}

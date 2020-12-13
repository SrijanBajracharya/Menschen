package com.achiever.menschenfahren.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.MULTIPLE_CHOICES, reason = "Multiple resource found in datas")
public class MultipleResourceFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public MultipleResourceFoundException(final String message) {
        super(message);
    }

}

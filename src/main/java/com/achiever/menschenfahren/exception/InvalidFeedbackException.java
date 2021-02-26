package com.achiever.menschenfahren.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Feedback data invalid")
public class InvalidFeedbackException extends Exception {
    private static final long serialVersionUID = -503341549235515462L;

    public InvalidFeedbackException(final String reason) {
        super(reason);
    }
}

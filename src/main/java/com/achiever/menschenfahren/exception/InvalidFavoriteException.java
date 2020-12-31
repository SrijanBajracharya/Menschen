package com.achiever.menschenfahren.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown if the favorite request is invalid.
 * 
 * @author Srijan Bajracharya
 *
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Favorite data invalid")
public class InvalidFavoriteException extends Exception {

    private static final long serialVersionUID = 2608867287779463540L;

    public InvalidFavoriteException(final String reason) {
        super(reason);
    }

}
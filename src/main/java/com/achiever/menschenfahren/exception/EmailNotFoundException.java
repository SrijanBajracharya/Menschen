package com.achiever.menschenfahren.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Thrown if the email is not found.
 *
 * @author Srijan Bajracharya
 *
 */
public class EmailNotFoundException extends AuthenticationException {

    private static final long serialVersionUID = -972196968490756189L;

    public EmailNotFoundException(String msg) {
        super(msg);
    }

    /**
     * Constructs a {@code EmailNotFoundException} with the specified message and root cause.
     *
     * @param msg
     *            the detail message.
     * @param t
     *            root cause
     */
    public EmailNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }
}
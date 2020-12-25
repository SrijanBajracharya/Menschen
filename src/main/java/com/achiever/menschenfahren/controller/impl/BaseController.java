package com.achiever.menschenfahren.controller.impl;

import javax.annotation.Nonnull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.achiever.menschenfahren.base.dto.response.DataResponse;

/**
 *
 * @author Srijan Bajracharya
 *
 */
public class BaseController {

    /**
     * Wraps the given data into a response entity and data response
     *
     * @param <T>
     *            The type of the data
     * @param data
     *            The data to wrap.
     * @param status
     *            The HTTP status to assign to the response.
     * @return
     */
    protected <T> ResponseEntity<DataResponse<T>> buildResponse(@Nonnull final T data, @Nonnull final HttpStatus status) {
        return new ResponseEntity<>(new DataResponse<>(data), status);
    }

}
